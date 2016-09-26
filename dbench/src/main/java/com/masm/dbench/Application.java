package com.masm.dbench;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.masm.dbench.SQLHelper.Dialect;

public class Application {

	private static final int ITERATIONS = 1;

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		SelectBenchmark selectBenchmarkH2 = (SelectBenchmark) context.getBean("selectBenchmarkH2");
		SelectBenchmark selectBenchmarkOracle = (SelectBenchmark) context.getBean("selectBenchmarkOracle");
		SelectBenchmark selectBenchmarkMySQL = (SelectBenchmark) context.getBean("selectBenchmarkMySQL");

		TableHelper.setupTable(4, 9);

		for (int i = 0; i < ITERATIONS; i++) {

			TableHelper.setPosition(0);

			System.out.println("*********** Get list of cities **************");
			TableHelper.addValue2Table("Cities");

			selectBenchmarkH2.fechtAllCities();
			selectBenchmarkOracle.fechtAllCities();
			selectBenchmarkMySQL.fechtAllCities();

			System.out.println("*********** Get list of countries **************");
			TableHelper.addValue2Table("Countries");

			selectBenchmarkH2.fechtAllCountries();
			selectBenchmarkOracle.fechtAllCountries();
			selectBenchmarkMySQL.fechtAllCountries();

			System.out.println("*********** Get list of capitals **************");
			TableHelper.addValue2Table("Capitals");

			selectBenchmarkH2.fetchAllCapitals();
			selectBenchmarkOracle.fetchAllCapitals();
			selectBenchmarkMySQL.fetchAllCapitals();

			System.out.println("*********** Get ordered list of cities by population **************");
			TableHelper.addValue2Table("Ordered");

			selectBenchmarkH2.fechtAllCities("Population", false);
			selectBenchmarkOracle.fechtAllCities("Population", false);
			selectBenchmarkMySQL.fechtAllCities("Population", false);

			System.out.println("*********** Get list of countires by language **************");
			TableHelper.addValue2Table("Countries by language");

			selectBenchmarkH2.fechtCountriesByLanguage("Spanish");
			selectBenchmarkOracle.fechtCountriesByLanguage("Spanish");
			selectBenchmarkMySQL.fechtCountriesByLanguage("Spanish");

			System.out.println("*********** Get country by code **************");
			TableHelper.addValue2Table("Countries by code");

			selectBenchmarkH2.fechtOneCountry("POL");
			selectBenchmarkOracle.fechtOneCountry("POL");
			selectBenchmarkMySQL.fechtOneCountry("POL");

			System.out.println("*********** Get cities from 10 top countries by size **************");
			TableHelper.addValue2Table("Cities from 10 biggest countries");

			selectBenchmarkH2.fechtCitiesFromTopCountriesByCriteria("SurfaceArea", false, 10, true);
			selectBenchmarkOracle.fechtCitiesFromTopCountriesByCriteria("SurfaceArea", true, 10, true);
			selectBenchmarkMySQL.fechtCitiesFromTopCountriesByCriteria("SurfaceArea", false, 10, true);

			System.out.println("*********** Get top 3 cities from every Region *************");
			TableHelper.addValue2Table("Top 3 cities from every Region");

			selectBenchmarkH2.fetchTopNCitiesInRegionByCriteria("Population", Dialect.H2, 3);
			selectBenchmarkOracle.fetchTopNCitiesInRegionByCriteria("Population", Dialect.ORACLE, 3);
			selectBenchmarkMySQL.fetchTopNCitiesInRegionByCriteria("Population", Dialect.MYSQL, 3);

			System.out.println("*********** Get most popular languages from every Region *************");
			TableHelper.addValue2Table("Top languages for Region");

			Map wynikH2 = selectBenchmarkH2.getTopLanguagesByRegion(Dialect.H2);
			Map wynikOracle = selectBenchmarkOracle.getTopLanguagesByRegion(Dialect.ORACLE);
			Map wynikMySQL = selectBenchmarkMySQL.getTopLanguagesByRegion(Dialect.MYSQL);

			System.out.println(wynikH2.equals(wynikMySQL));
		}

		TableHelper.drawTable(new String[] { "", "H2", "Oracle", "MySQL" });

	}

}
