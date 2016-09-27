package com.masm.dbench;

import java.util.Map;

import com.masm.dbench.SQLHelper.Dialect;

public interface Benchmark {

	public void fechtAllCities();

	public void fechtAllCountries();

	public void fetchAllCapitals();

	public void fechtAllCities(String orderBy, boolean asc);

	public void fechtCountriesByLanguage(String language);

	public void fechtOneCountry(String code);

	public void fechtCitiesFromTopCountriesByCriteria(String criteria, boolean oracle, int limit, boolean reverse);

	public void fetchTopNCitiesInRegionByCriteria(String criteria, Dialect dialect, int count);

	public Map<String, String> getTopLanguagesByRegion(Dialect dialect);
}
