package com.masm.dbench.dao.impl;

import static com.masm.dbench.Constants.CITY_TABLE;
import static com.masm.dbench.Constants.COUNTRY_TABLE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.masm.dbench.SQLHelper.Dialect;
import com.masm.dbench.dao.CityDAO;
import com.masm.dbench.exceptions.CityNotFoundException;
import com.masm.dbench.model.City;
import com.masm.dbench.model.Country;

@Repository
public class CityDAOImpl implements CityDAO {

	static Logger log = Logger.getLogger(CityDAOImpl.class.getName());

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public CityDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public City getCity(Long id) throws CityNotFoundException {

		City result = fetchCity(id);

		if (result == null) {
			throw new CityNotFoundException();
		}

		return result;
	}

	@Override
	public City fetchCity(Long id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		String sql = "SELECT * FROM " + CITY_TABLE + " WHERE ID=:id";

		List<City> result = namedParameterJdbcTemplate.query(sql, params, new CityMapper());

		return result.size() > 0 ? result.get(0) : null;
	}

	@Override
	public List<City> findCitiesByName(String name) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);

		String sql = "SELECT * FROM " + CITY_TABLE + "  WHERE Name=:name";
		List<City> result = namedParameterJdbcTemplate.query(sql, params, new CityMapper());

		return result;
	}

	@Override
	public List<City> findCities() {

		String sql = "SELECT * FROM " + CITY_TABLE;
		List<City> result = namedParameterJdbcTemplate.query(sql, new CityMapper());

		return result;
	}

	@Override
	public List<City> findCities(String orderBy, boolean asc) {

		String sql = "SELECT * FROM " + CITY_TABLE + " ORDER BY " + orderBy + (asc ? " asc" : " desc");

		List<City> result = namedParameterJdbcTemplate.query(sql, new CityMapper());

		return result;
	}

	@Override
	public List<City> findCitiesByNameAndCountry(String name, Country country) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("code", country.getCode());

		String sql = "SELECT * FROM " + CITY_TABLE + " WHERE Name=:name AND CountryCode=:code";

		List<City> result = namedParameterJdbcTemplate.query(sql, params, new CityMapper());

		return result;
	}

	@Override
	public List<City> findCitiesInCountries(List<Country> countries) {

		Map<String, Object> params = new HashMap<String, Object>();

		Set<String> codes = new HashSet<String>(countries.size());

		countries.forEach(country -> codes.add(country.getCode()));

		params.put("codes", codes);

		String sql = "SELECT * FROM " + CITY_TABLE + " WHERE CountryCode in (:codes)";

		List<City> result = namedParameterJdbcTemplate.query(sql, params, new CityMapper());

		return result;
	}

	@Override
	public List<City> fetchTopNCitiesInRegionByCriteria(String criteria, Dialect sqlDialect, boolean reverse,
			int count) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("count", count);

		String sql = null;

		switch (sqlDialect) {
		case MYSQL:
			sql = "SELECT * FROM(SELECT c.*,"
					+ "@region_rank := IF(@current_region = region, @region_rank + 1, 1) AS region_rank, "
					+ "@current_region := region FROM " + CITY_TABLE + " c JOIN " + COUNTRY_TABLE
					+ " r ON c.CountryCode = r.Code " + "ORDER BY r.Region, r." + criteria + " "
					+ (reverse ? "asc" : "desc") + ") ranked WHERE region_rank <= :count";

			break;
		case H2:
			sql = "SELECT * from (SELECT sorted.*,"
					+ "@region_rank := CASEWHEN(@current_region = region, @region_rank + 1, 1) AS region_rank,"
					+ "@current_region := sorted.region FROM ( SELECT c.*, r.Region FROM " + CITY_TABLE + " c JOIN "
					+ COUNTRY_TABLE + " r ON c.CountryCode = r.Code "
					+ "ORDER BY r.Region, r.Population DESC) sorted) ordered where region_rank <= :count";
			break;
		default:
			sql = "SELECT * FROM " + CITY_TABLE
					+ " WHERE ID IN (SELECT ID FROM(SELECT c.ID, ROW_NUMBER() OVER (PARTITION BY cc.Region ORDER BY cc."
					+ criteria + " " + (reverse ? "asc" : "desc") + ") as region_rank " + " FROM city c JOIN "
					+ COUNTRY_TABLE + " cc ON c.CountryCode = cc.Code ) ranked WHERE region_rank <= :count)";
			break;

		}
		List<City> result = namedParameterJdbcTemplate.query(sql, params, new CityMapper());

		return result;
	}

	private static final class CityMapper implements RowMapper<City> {

		@Override
		public City mapRow(ResultSet rs, int rowNum) throws SQLException {
			City city = new City();

			city.setId(rs.getLong("ID"));
			city.setName(rs.getString("Name"));
			city.setCountryCode(rs.getString("CountryCode"));
			city.setDistrict(rs.getString("District"));
			city.setPopulation(rs.getInt("Population"));

			return city;
		}
	}

}
