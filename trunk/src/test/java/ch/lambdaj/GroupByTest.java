package ch.lambdaj;

import static ch.lambdaj.Groups.*;
import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.mock.ExposureBy.*;
import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;

import net.sf.json.*;
import net.sf.json.util.*;

import org.jmock.*;
import org.junit.*;

import ch.lambdaj.mock.*;

public class GroupByTest {
	private Mockery context = new Mockery();

	@Before
	public void setUp() {
		FexFrance = new Exposure() {
			{
				setCountryName("France");
				setInsuredName("Fex France");
			}
		};

		FexCanada = new Exposure() {
			{
				setCountryName("Canada");
				setInsuredName("Fex Canada");
			}
		};

		exposures = asList(FexFrance, FexCanada);
	}

	private Exposure FexFrance, FexCanada;

	private List<Exposure> exposures;

	@Test
	public void testGroupByInsuredName() {
		String string = string(group(exposures, by(Exposure).insuredName()));

		assertThat(string, containsString("\"insuredName\":\"Fex France"));
		assertThat(string, containsString("\"insuredName\":\"Fex Canada"));
	}

	@Test
	public void testGroupByInsuredNameAndCountryName() {
		String string = string(group(exposures, by(Exposure).insuredName().countryName()));
		assertThat(string, containsString("\"insuredName\":\"Fex France\",\"children\":"));
		assertThat(string, containsString("\"countryName\":\"France\",\"children\":"));
		assertThat(string, containsString("\"insuredName\":\"Fex Canada\",\"children\":"));
		assertThat(string, containsString("\"countryName\":\"Canada\",\"children\":"));
	}

	@Test
	public void testGroupByInsuredNameAsExposures() {
		String string = string(group(exposures, by(Exposure).insuredName().asExposures()));
	}

	@Test
	public void testGroupByCountryName() {
		String string = string(group(exposures, by(Exposure).countryName()));
	}

	@Test
	public void testGroupByCountryNameAndInsuredName() {
		String string = string(group(exposures, by(Exposure).countryName().insuredName()));
	}

	@Test
	public void testGroupByCountryNameAsCountries() {
		String string = string(group(exposures, by(Exposure).countryName().asCountries()));
	}

	@Test
	public void testGroupByCountryNameAsExposures() {
		String string = string(group(exposures, by(Exposure).countryName().asExposures()));
	}

	private String string(Object object) {
		if (JSONUtils.isArray(object)) return JSONArray.fromObject(object).toString();
		if (JSONUtils.isObject(object)) return JSONObject.fromObject(object).toString();
		return object.toString();
	}
}