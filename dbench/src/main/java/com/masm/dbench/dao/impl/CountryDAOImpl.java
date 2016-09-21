package com.masm.dbench.dao.impl;

import static com.masm.dbench.Constants.COUNTRYLANGUAGE_TABLE;
import static com.masm.dbench.Constants.COUNTRY_TABLE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.masm.dbench.dao.CountryDAO;
import com.masm.dbench.exceptions.CountryNotFoundException;
import com.masm.dbench.model.Country;

@Repository
public class CountryDAOImpl implements CountryDAO {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public CountryDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public Country getCountry(String code) throws CountryNotFoundException {

		Country result = fetchCountry(code);

		if (result == null) {
			throw new CountryNotFoundException();
		}

		return result;

	}

	@Override
	public Country fetchCountry(String code) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);

		String sql = "SELECT * FROM " + COUNTRY_TABLE + " WHERE Code=:code";
		List<Country> result = namedParameterJdbcTemplate.query(sql, params, new CountryMapper());

		return result.size() > 0 ? result.get(0) : null;
	}

	@Override
	public Country findCountryByName(String name) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);

		String sql = "SELECT * FROM " + COUNTRY_TABLE + " WHERE Name=:name";
		Country result = namedParameterJdbcTemplate.queryForObject(sql, params, new CountryMapper());

		return result;
	}

	@Override
	public List<Country> findCountriesByLanguage(String language) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("language", language);

		String sql = "SELECT c.* FROM " + COUNTRY_TABLE + " c JOIN " + COUNTRYLANGUAGE_TABLE
				+ " l ON l.CountryCode=c.Code WHERE l.Language=CAST(:language as CHAR(30))";

		List<Country> result = namedParameterJdbcTemplate.query(sql, params, new CountryMapper());

		return result;
	}

	@Override
	public List<Country> findCountries() {

		String sql = "SELECT * FROM " + COUNTRY_TABLE;
		List<Country> result = namedParameterJdbcTemplate.query(sql, new CountryMapper());

		return result;
	}

	private final class CountryMapper implements RowMapper<Country> {

		@Override
		public Country mapRow(ResultSet rs, int rowNum) throws SQLException {

			Country country = new Country();

			country.setCode(rs.getString("Code"));
			country.setCode2(rs.getString("Code2"));
			country.setName(rs.getString("Name"));
			country.setContinent(rs.getString("Continent"));
			country.setRegion(rs.getString("Region"));
			country.setSurfaceArea(rs.getDouble("SurfaceArea"));
			country.setIndepYear(rs.getShort("IndepYear"));
			country.setPopulation(rs.getInt("Population"));
			country.setLifeExpectancy(rs.getDouble("LifeExpectancy"));
			country.setGnp(rs.getDouble("GNP"));
			country.setGnpOld(rs.getDouble("GNPOld"));
			country.setLocalName(rs.getString("LocalName"));
			country.setGovernmentForm(rs.getString("GovernmentForm"));
			country.setHeadOfState(rs.getString("HeadOfState"));
			country.setCapitalId(rs.getLong("Capital"));

			return country;
		}
	}

}
