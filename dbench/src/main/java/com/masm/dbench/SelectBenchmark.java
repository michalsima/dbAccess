package com.masm.dbench;

import java.util.List;
import java.util.logging.Logger;

import com.masm.dbench.dao.CityDAO;
import com.masm.dbench.dao.CountryDAO;
import com.masm.dbench.model.City;
import com.masm.dbench.model.Country;

public class SelectBenchmark {

	static Logger log = Logger.getLogger(SelectBenchmark.class.getName());

	private CityDAO cityDAO;
	private CountryDAO countryDAO;

	public void setCityDAO(CityDAO cityDAO) {
		this.cityDAO = cityDAO;
	}

	public void setCountryDAO(CountryDAO countryDAO) {
		this.countryDAO = countryDAO;
	}

	public void fechtAllCities() {

		List<City> cities = cityDAO.findCities();

		log.info("Got " + cities.size() + " cities.");
	}

	public void fechtAllCountries() {

		List<Country> countries = countryDAO.findCountries();

		log.info("Got " + countries.size() + " countries.");
	}

}
