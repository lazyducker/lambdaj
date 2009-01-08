package ch.lambdaj;

import static ch.lambdaj.group.Groups.*;
import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.Util.*;
import static ch.lambdaj.mock.ExposureBy.*;
import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.*;

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
				setCountryIso("FR");
				setCountryFlag("/flags/fr.jpg");
			}
		};

		FexCanada = new Exposure() {
			{
				setCountryName("Canada");
				setInsuredName("Fex Canada");
				setCountryIso("CA");
				setCountryFlag("/flags/ca.jpg");
			}
		};

		exposures = asList(FexFrance, FexCanada);
	}

	private Exposure FexFrance, FexCanada;

	private List<Exposure> exposures;

	@Test
	public void testGroupByInsuredName() {
		String string = toJsonString(group(exposures, by(Exposure).insuredName()));

		string = cleanJsonString(string);
		assertThat(string, containsString("insuredName:Fex France"));
		assertThat(string, containsString("insuredName:Fex Canada"));
	}

	@Test
	public void testGroupByInsuredNameAndCountryName() {
		String string = toJsonString(group(exposures, by(Exposure).insuredName().countryName()));

		string = cleanJsonString(string);
		assertThat(string, containsString("insuredName:Fex France,children:"));
		assertThat(string, containsString("countryName:France,children:"));
		assertThat(string, containsString("insuredName:Fex Canada,children:"));
		assertThat(string, containsString("countryName:Canada,children:"));
	}

	@Test
	public void testGroupByInsuredNameAsExposures() {
		String string = toJsonString(group(exposures, by(Exposure).insuredName().asExposures()));
	}

	@Test
	public void testGroupByCountryName() {
		String string = toJsonString(group(exposures, by(Exposure).countryName().asInsureds().headCountryIso()));
		
		string = cleanJsonString(string);
		assertThat(string, allOf(containsString("insureds:[{"), containsString("countryName:France"), containsString("insuredName:Fex France"),containsString("countryFlag:/flags/fr.jpg"),containsString("countryIso:FR")));
		assertThat(string, allOf(containsString("insureds:[{"), containsString("countryName:Canada"), containsString("insuredName:Fex Canada"),containsString("countryFlag:/flags/fr.jpg"),containsString("countryIso:FR")));
		
		string = cleanJsonString(string, cleanJsonString(toJsonString(FexFrance)));
		string = cleanJsonString(string, cleanJsonString(toJsonString(FexCanada)));
		assertThat(string, containsString("countryIso:FR"));
		assertThat(string, containsString("countryIso:CA"));
	}

	@Test
	public void testGroupByCountryNameAndInsuredName() {
		String string = toJsonString(group(exposures, by(Exposure).countryName().insuredName()));
	}

	@Test
	public void testGroupByCountryNameAsCountries() {
		String string = toJsonString(group(exposures, by(Exposure).countryName().asCountries()));
		string = cleanJsonString(string);
	}

	@Test
	public void testGroupByCountryNameAsExposures() {
		String string = toJsonString(group(exposures, by(Exposure).countryName().asExposures()));
		string = cleanJsonString(string);
	}
}