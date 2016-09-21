package com.masm.dbench;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		SelectBenchmark selectBenchmarkH2 = (SelectBenchmark) context.getBean("selectBenchmarkH2");
		SelectBenchmark selectBenchmarkOracle = (SelectBenchmark) context.getBean("selectBenchmarkOracle");
		SelectBenchmark selectBenchmarkMySQL = (SelectBenchmark) context.getBean("selectBenchmarkMySQL");

		TableHelper.setupTable(4, 5);

		int iterations = 10;

		for (int i = 0; i < iterations; i++) {

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

			System.out.println("*********** Get list of cities **************");
			TableHelper.addValue2Table("Cities again");

			selectBenchmarkH2.fechtAllCities();
			selectBenchmarkOracle.fechtAllCities();
			selectBenchmarkMySQL.fechtAllCities();
		}

		TableHelper.drawTable(new String[] { "", "H2", "Oracle", "MySQL" });

	}

}
