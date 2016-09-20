package com.masm.dbench.dao.impl;

import static com.masm.dbench.Constants.CITY_TABLE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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

	public City getCity(Long id) throws CityNotFoundException {

		City result = fetchCity(id);

		if (result == null) {
			throw new CityNotFoundException();
		}

		return result;
	}

	public City fetchCity(Long id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		String sql = "SELECT * FROM " + CITY_TABLE + " WHERE ID=:id";

		List<City> result = namedParameterJdbcTemplate.query(sql, params, new CityMapper());

		return result.size() > 0 ? result.get(0) : null;
	}

	public List<City> findCitiesByName(String name) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);

		String sql = "SELECT * FROM " + CITY_TABLE + "  WHERE Name=:name";
		List<City> result = namedParameterJdbcTemplate.query(sql, params, new CityMapper());

		return result;
	}

	public List<City> findCities() {

		String sql = "SELECT * FROM " + CITY_TABLE;
		List<City> result = namedParameterJdbcTemplate.query(sql, new CityMapper());

		return result;
	}

	public List<City> findCitiesByNameAndCountry(String name, Country country) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("code", country.getCode());

		String sql = "SELECT * FROM " + CITY_TABLE + " WHERE Name=:name AND CountryCode=:code";

		List<City> result = namedParameterJdbcTemplate.query(sql, params, new CityMapper());

		return result;
	}

	private static final class CityMapper implements RowMapper<City> {

		public City mapRow(ResultSet rs, int rowNum) throws SQLException {
			City city = new City();

			city.setID(rs.getLong("ID"));
			city.setName(rs.getString("Name"));
			city.setCountryCode(rs.getString("CountryCode"));
			city.setDistrict(rs.getString("District"));
			city.setPopulation(rs.getInt("Population"));

			return city;
		}
	}

}
