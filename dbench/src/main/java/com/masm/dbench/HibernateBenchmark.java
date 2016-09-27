package com.masm.dbench;

import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.masm.dbench.SQLHelper.Dialect;
import com.masm.dbench.model.City;
import com.masm.dbench.model.Country;
import com.masm.dbench.model.CountryLanguage;

public class HibernateBenchmark implements Benchmark {

	Session session;

	public HibernateBenchmark() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	@Override
	public void fechtAllCities() {

		CriteriaBuilder cb = session.getCriteriaBuilder();

		CriteriaQuery<City> cq = cb.createQuery(City.class);
		Root<City> from = cq.from(City.class);

		cq.select(from);
		TypedQuery<City> q = session.createQuery(cq);
		List<City> allCities = q.getResultList();

		System.out.println("Found " + allCities.size() + " cities");

	}

	@Override
	public void fechtAllCountries() {
		CriteriaBuilder cb = session.getCriteriaBuilder();

		CriteriaQuery<Country> cq = cb.createQuery(Country.class);
		Root<Country> from = cq.from(Country.class);

		cq.select(from);
		TypedQuery<Country> q = session.createQuery(cq);
		List<Country> allCountries = q.getResultList();

		System.out.println("Found " + allCountries.size() + " countries");

	}

	@Override
	public void fetchAllCapitals() {

		Criteria cr = session.createCriteria(Country.class)
				.setProjection(Projections.projectionList().add(Projections.property("capital"), "capital"))
				.add(Restrictions.isNotNull("capital")).setResultTransformer(Transformers.TO_LIST);

		List<City> capitals = cr.list();

		System.out.println("Found " + capitals.size() + " capitals");

	}

	@Override
	public void fechtAllCities(String orderBy, boolean asc) {

		CriteriaBuilder cb = session.getCriteriaBuilder();

		CriteriaQuery<City> cq = cb.createQuery(City.class);
		Root<City> from = cq.from(City.class);

		cq.select(from);
		cq.orderBy(asc ? cb.asc(from.get(orderBy)) : cb.desc(from.get(orderBy)));

		TypedQuery<City> q = session.createQuery(cq);
		List<City> allCities = q.getResultList();

		System.out.println("Found " + allCities.size() + " cities");

	}

	@Override
	public void fechtCountriesByLanguage(String language) {

		Criteria cr = session.createCriteria(CountryLanguage.class)
				.setProjection(Projections.projectionList().add(Projections.property("countryCode"), "countryCode"))
				.add(Restrictions.eq("language", language)).add(Restrictions.isNotNull("countryCode"))
				.setResultTransformer(Transformers.TO_LIST);

		List<Country> capitals = cr.list();

		System.out.println("Found " + capitals.size() + " countries with language " + language);

	}

	@Override
	public void fechtOneCountry(String code) {

		CriteriaBuilder cb = session.getCriteriaBuilder();

		CriteriaQuery<Country> cq = cb.createQuery(Country.class);
		Root<Country> from = cq.from(Country.class);

		cq.select(from).where(cb.equal(from.get("code"), code));
		TypedQuery<Country> q = session.createQuery(cq);
		Country country = q.getSingleResult();

	}

	@Override
	public void fechtCitiesFromTopCountriesByCriteria(String criteria, boolean oracle, int limit, boolean reverse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchTopNCitiesInRegionByCriteria(String criteria, Dialect dialect, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, String> getTopLanguagesByRegion(Dialect dialect) {
		// TODO Auto-generated method stub
		return null;
	}

}
