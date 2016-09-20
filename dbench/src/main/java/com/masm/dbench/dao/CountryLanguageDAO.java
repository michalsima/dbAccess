package com.masm.dbench.dao;

import java.util.List;

import com.masm.dbench.model.Country;
import com.masm.dbench.model.CountryLanguage;

public interface CountryLanguageDAO {

	List<CountryLanguage> getCountryLanguages(Country country);

}
