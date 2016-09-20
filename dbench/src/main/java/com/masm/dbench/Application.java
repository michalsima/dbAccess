package com.masm.dbench;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		SelectBenchmark selectBenchmarkH2 = (SelectBenchmark) context.getBean("selectBenchmarkH2");
		SelectBenchmark selectBenchmarkOracle = (SelectBenchmark) context.getBean("selectBenchmarkOracle");

		// selectBenchmarkH2.fechtAllCities();
		selectBenchmarkOracle.fechtAllCities();

		// selectBenchmarkH2.fechtAllCountries();
		selectBenchmarkOracle.fechtAllCountries();

	}

}
