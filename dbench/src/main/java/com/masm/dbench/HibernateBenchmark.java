package com.masm.dbench;

import static com.masm.dbench.Constants.CITY_TABLE;
import static com.masm.dbench.Constants.COUNTRYLANGUAGE_TABLE;
import static com.masm.dbench.Constants.COUNTRY_TABLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;

import com.masm.dbench.SQLHelper.Dialect;
import com.masm.dbench.model.City;
import com.masm.dbench.model.Country;
import com.masm.dbench.model.CountryLanguage;

public class HibernateBenchmark implements Benchmark {

	static Logger log = Logger.getLogger(HibernateBenchmark.class.getName());

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

		log.info("Found " + allCities.size() + " cities");

	}

	@Override
	public void fechtAllCountries() {
		CriteriaBuilder cb = session.getCriteriaBuilder();

		CriteriaQuery<Country> cq = cb.createQuery(Country.class);
		Root<Country> from = cq.from(Country.class);

		cq.select(from);
		TypedQuery<Country> q = session.createQuery(cq);
		List<Country> allCountries = q.getResultList();

		log.info("Found " + allCountries.size() + " countries");

	}

	@Override
	public void fetchAllCapitals() {

		Criteria cr = session.createCriteria(Country.class)
				.setProjection(Projections.projectionList().add(Projections.property("capital"), "capital"))
				.add(Restrictions.isNotNull("capital")).setResultTransformer(Transformers.TO_LIST);

		List<City> capitals = cr.list();

		log.info("Found " + capitals.size() + " capitals");

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

		log.info("Found " + allCities.size() + " cities");

	}

	@Override
	public void fechtCountriesByLanguage(String language) {

		Criteria cr = session.createCriteria(CountryLanguage.class)
				.setProjection(Projections.projectionList().add(Projections.property("countryCode"), "countryCode"))
				.add(Restrictions.eq("language", language)).add(Restrictions.isNotNull("countryCode"))
				.setResultTransformer(Transformers.TO_LIST);

		List<Country> capitals = cr.list();

		log.info("Found " + capitals.size() + " countries with language " + language);

	}

	@Override
	public void fechtOneCountry(String code) {

		CriteriaBuilder cb = session.getCriteriaBuilder();

		CriteriaQuery<Country> cq = cb.createQuery(Country.class);
		Root<Country> from = cq.from(Country.class);

		cq.select(from).where(cb.equal(from.get("code"), code));
		TypedQuery<Country> q = session.createQuery(cq);
		Country country = q.getSingleResult();

		log.info("Found " + (country != null ? 1 : 0) + " country with code " + code);
	}

	@Override
	public void fechtCitiesFromTopCountriesByCriteria(String criteria, boolean oracle, int limit, boolean reverse) {

		CriteriaBuilder cb = session.getCriteriaBuilder();

		CriteriaQuery<Country> cq = cb.createQuery(Country.class);
		Root<Country> from = cq.from(Country.class);

		cq.select(from);
		cq.orderBy(!reverse ? cb.asc(from.get(criteria)) : cb.desc(from.get(criteria)));
		TypedQuery<Country> q = session.createQuery(cq).setMaxResults(limit);

		List<Country> countries = q.getResultList();

		List<String> codes = new ArrayList<String>(countries.size());

		for (Country country : countries) {
			codes.add(country.getCode());
		}

		// now cities

		CriteriaQuery<City> cqCities = cb.createQuery(City.class);
		Root<City> fromCities = cqCities.from(City.class);

		cqCities.select(fromCities);
		cqCities.where(fromCities.get("countryCode").in(codes));

		TypedQuery<City> qCities = session.createQuery(cqCities);
		List<City> allCities = qCities.getResultList();

		log.info("Got " + allCities.size() + " cities from top countries by " + criteria);
	}

	@Override
	public void fetchTopNCitiesInRegionByCriteria(String criteria, Dialect dialect, int count) {

		Query query = session.createNativeQuery("SELECT * FROM " + CITY_TABLE
				+ " WHERE ID IN (SELECT ID FROM(SELECT c.ID, ROW_NUMBER() OVER (PARTITION BY cc.Region ORDER BY cc."
				+ criteria + " desc) as region_rank " + " FROM city c JOIN " + COUNTRY_TABLE
				+ " cc ON c.CountryCode = cc.Code ) ranked WHERE region_rank <= ?)");
		query.setParameter(1, count);
		List<City> allCities = query.getResultList();

		log.info("Got " + allCities.size() + " (top " + count + " per region) cities by criteria: " + criteria);
	}

	@Override
	public Map<String, String> getTopLanguagesByRegion(Dialect dialect) {

		Map<String, String> langs = new HashMap<>();

		Query query = session.createNativeQuery("SELECT region, language FROM"
				+ "(SELECT counted.region, total, language, ROW_NUMBER() OVER (PARTITION BY region ORDER BY total DESC) as position "
				+ "FROM(select Region, SUM(Population*Percentage) as total, Language FROM " + COUNTRY_TABLE + " JOIN "
				+ COUNTRYLANGUAGE_TABLE
				+ " on CountryCode=Code group by region,  Language ORDER BY region, total desc) "
				+ "counted ORDER BY counted.region, counted.total desc) WHERE position=1");

		List<Object[]> rows = query.getResultList();

		for (Object[] m : rows) {
			langs.put(m[0].toString(), m[1].toString());
		}

		langs.forEach((k, v) -> System.out.println(k + " -> " + v));

		return langs;
	}

}
