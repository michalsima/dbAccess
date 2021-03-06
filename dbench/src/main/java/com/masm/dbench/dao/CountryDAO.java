package com.masm.dbench.dao;

import java.util.List;

import com.masm.dbench.exceptions.CountryNotFoundException;
import com.masm.dbench.model.Country;

public interface CountryDAO {

	Country getCountry(String code) throws CountryNotFoundException;

	Country fetchCountry(String code);

	Country findCountryByName(String name);

	List<Country> findCountries();

	List<Country> findCountriesByLanguage(String language);

	List<Country> findCountries(String orderBy, boolean isOracle, boolean asc, int from, int to);

}
