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

	public void fetchAllCapitals() {

		List<Country> countries = countryDAO.findCountries();

		int capitals = 0;
		for (Country country : countries) {
			City capital = cityDAO.fetchCity(country.getCapitalId());
			if (capital != null) {
				capitals++;
			}
		}

		log.info("Got " + capitals + " capitals for " + countries.size() + " countries.");
	}

	public void fechtAllCities(String orderBy, boolean asc) {

		List<City> cities = cityDAO.findCities(orderBy, asc);

		log.info("Got " + cities.size() + " cities ordered by " + orderBy);
	}

	public void fechtCountriesByLanguage(String language) {

		List<Country> countries = countryDAO.findCountriesByLanguage(language);

		log.info("Got " + countries.size() + " countries with language " + language);
	}

	public void fechtOneCountry(String code) {

		Country country = countryDAO.fetchCountry(code);

		log.info("Got " + (country != null ? "1" : "0") + " countries with code " + code);

	}

}
