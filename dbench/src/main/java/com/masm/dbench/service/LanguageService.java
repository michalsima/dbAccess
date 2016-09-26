package com.masm.dbench.service;

import static com.masm.dbench.Constants.COUNTRYLANGUAGE_TABLE;
import static com.masm.dbench.Constants.COUNTRY_TABLE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.masm.dbench.SQLHelper.Dialect;

public class LanguageService {

	private JdbcTemplate jdbcTemplate;

	public LanguageService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Map<String, String> getMostPopularLanguageInRegions(Dialect dialect) {

		Map<String, String> result = new HashMap<>();

		String sql = null;

		switch (dialect) {
		case MYSQL:
			sql = "select Language, Region from(select Region, SUM(Population*Percentage) as suma, Language  " + "from "
					+ COUNTRY_TABLE + " join " + COUNTRYLANGUAGE_TABLE
					+ " on CountryCode=Code group by region, Language order by region, suma desc) "
					+ "counted group by Region";
			break;
		default:
			sql = "select region, language from "
					+ "(select counted.region, suma, language, ROW_NUMBER() OVER (PARTITION BY region ORDER BY suma DESC) as position "
					+ "FROM(select Region, SUM(Population*Percentage) as suma, Language  from dbench.country "
					+ "join dbench.countrylanguage on CountryCode=Code group by region,  Language order by region, suma desc) "
					+ "counted order by counted.region, counted.suma desc) where position=1";
			break;
		case H2:
			sql = "select region, language from "
					+ "(select counted.region, suma, language, @region_rank := CASEWHEN(@current_region = region, @region_rank + 1, 1) AS position, "
					+ "@current_region := counted.region from(select Region, SUM(Population*Percentage) as suma, Language  from dbench.country "
					+ "join dbench.countrylanguage on CountryCode=Code group by region,  Language order by region, suma desc) counted order by counted.region, "
					+ "counted.suma desc) where  position=1";
			break;
		}
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

		for (Map<String, Object> m : rows) {
			result.put(m.get("Region").toString(), m.get("Language").toString());
		}

		return result;
	}
}
