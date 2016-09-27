package com.masm.dbench.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CountryLanguage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String isOfficial;
	private Double percentage;
	private String countryCode;
	private String language;

	public String getCountryCode() {
		return countryCode;
	}

	public String getLanguage() {
		return language;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getIsOfficial() {
		return isOfficial;
	}

	public void setIsOfficial(String isOfficial) {
		this.isOfficial = isOfficial;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	@Override
	public boolean equals(Object b) {
		return (b instanceof CountryLanguage) && countryCode.equals(((CountryLanguage) b).getCountryCode())
				&& language.equals(((CountryLanguage) b).language);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(language).append(countryCode).toHashCode();
	}
}
