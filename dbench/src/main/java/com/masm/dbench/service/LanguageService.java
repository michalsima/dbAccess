package com.masm.dbench.service;

import static com.masm.dbench.Constants.COUNTRYLANGUAGE_TABLE;
import static com.masm.dbench.Constants.COUNTRY_TABLE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.jdbc.core.JdbcTemplate;

import com.masm.dbench.SQLHelper.Dialect;
import com.masm.dbench.SelectBenchmark;

public class LanguageService {

	private static Logger log = Logger.getLogger(SelectBenchmark.class.getName());

	private JdbcTemplate jdbcTemplate;

	public LanguageService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Map<String, String> getMostPopularLanguageInRegions(Dialect dialect) {

		Map<String, String> result = new HashMap<>();

		String sql = null;

		switch (dialect) {
		case MYSQL:
			sql = "SELECT Language, Region FROM(SELECT Region, SUM(Population*Percentage) as total, Language FROM "
					+ COUNTRY_TABLE + " JOIN " + COUNTRYLANGUAGE_TABLE
					+ " on CountryCode=Code group by region, Language order by region, total desc) "
					+ "counted group by Region";
			break;
		default:
			sql = "SELECT region, language FROM"
					+ "(SELECT counted.region, total, language, ROW_NUMBER() OVER (PARTITION BY region ORDER BY total DESC) as position "
					+ "FROM(select Region, SUM(Population*Percentage) as total, Language FROM dbench.country "
					+ "JOIN dbench.countrylanguage on CountryCode=Code group by region,  Language ORDER BY region, total desc) "
					+ "counted ORDER BY counted.region, counted.total desc) WHERE position=1";
			break;
		case H2:
			sql = "SELECT region, language FROM "
					+ "(SELECT counted.region, total, language, @region_rank := CASEWHEN(@current_region = region, @region_rank + 1, 1) AS position, "
					+ "@current_region := counted.region FROM(select Region, SUM(Population*Percentage) as total, Language  FROM dbench.country "
					+ "JOIN dbench.countrylanguage on CountryCode=Code group by region,  Language order by region, total desc) counted ORDER BY counted.region, "
					+ "counted.total desc) WHERE position=1";
			break;
		}

		log.info(sql);

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

		for (Map<String, Object> m : rows) {
			result.put(m.get("Region").toString(), m.get("Language").toString());
		}

		return result;
	}
}
