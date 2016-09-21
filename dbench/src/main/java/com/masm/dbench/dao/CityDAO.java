package com.masm.dbench.dao;

import java.util.List;

import com.masm.dbench.exceptions.CityNotFoundException;
import com.masm.dbench.model.City;
import com.masm.dbench.model.Country;

public interface CityDAO {

	City getCity(Long id) throws CityNotFoundException;

	City fetchCity(Long id);

	List<City> findCities();

	List<City> findCities(String orderBy, boolean asc);

	List<City> findCitiesByName(String name);

	List<City> findCitiesByNameAndCountry(String name, Country country);
}
