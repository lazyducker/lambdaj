package ch.lambdaj;

import static ch.lambdaj.TestUtil.*;
import static ch.lambdaj.mock.ExposureBy.*;
import static java.util.Arrays.*;
import static org.hamcrest.collection.IsCollectionContaining.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import ch.lambdaj.group.*;
import ch.lambdaj.mock.*;

public class GroupsTest {

	@Before
	public void setUp() {
		FexFrance1 = new Exposure() {
			{
				setCountryName("France");
				setInsuredName("Fex France 1");
			}
		};

		FexFrance2 = new Exposure() {
			{
				setCountryName("France");
				setInsuredName("Fex France 2");
			}
		};

		FexCanada1 = new Exposure() {
			{
				setCountryName("Canada");
				setInsuredName("Fex Canada 1");
			}
		};

		FexCanada2 = new Exposure() {
			{
				setCountryName("Canada");
				setInsuredName("Fex Canada 2");
			}
		};

		exposures = asList(FexFrance1, FexFrance2, FexCanada1, FexCanada2);
	}

	private Exposure FexFrance1, FexFrance2, FexCanada1, FexCanada2;

	private List<Exposure> exposures;

	private static final <T> T by(T t) {
		T result = null;
		try {
			result = (T) t.getClass().newInstance();
		} catch (Exception e) {
			e.printStackTrace(); // TODO
		}
		return result;
	}

	@Test
	public void testByCriteria() {
		by(Exposure).countryName().insuredName();
	}

	@Test
	public void testGroupByCountry() {
		Group<Exposure> group = Groups.group(exposures, "countryName");
		
		assertThat(group.keySet(), hasItems("France", "Canada"));
		
		Iterable<Exposure> groupFrance = group.find("France");
		assertThat(groupFrance, hasItems(FexFrance1, FexFrance2));

		Iterable<Exposure> groupCanada = group.find("Canada");
		assertThat(groupCanada, hasItems(FexCanada1, FexCanada2));

System.out.println(toJsonString(group));
	}
	
	@Test
	public void testGroupByInsuredName() {
		Group<Exposure> group = Groups.group(exposures, "insuredName");
		
		assertThat(group.keySet(), hasItems("Fex France 1", "Fex France 2", "Fex Canada 1", "Fex Canada 2"));
		
		Iterable<Exposure> groupFrance = group.find("Fex France 2");
		assertThat(groupFrance, hasItems(FexFrance2));

		Iterable<Exposure> groupCanada = group.find("Fex Canada 1");
		assertThat(groupCanada, hasItems(FexCanada1));
	}

	@Test
	public void testGroupByCountryAndInsuredName() {
		Group<Exposure> group = Groups.group(exposures, "countryName", "insuredName");
		
		assertThat(group.keySet(), hasItems("France", "Canada"));

		Group<Exposure> groupFrance = group.findGroup("France");
		assertThat(groupFrance.findAll(), hasItems(FexFrance1, FexFrance2));

		Iterable<Exposure> groupFexFrance = groupFrance.find("Fex France 1");
		assertThat(groupFexFrance, hasItems(FexFrance1));

		Group<Exposure> groupCanada = group.findGroup("Canada");
		assertThat(groupCanada.findAll(), hasItems(FexCanada1, FexCanada2));

		Iterable<Exposure> groupFexCanada = groupCanada.find("Fex Canada 2");
		assertThat(groupFexCanada, hasItems(FexCanada2));
		
System.out.println(toJsonString(group));
	}

	@Test
	public void testGroupTypedByCountryAndInsuredName() {
		Group<Exposure> group = Groups.group(exposures, by(Exposure).countryName().insuredName());
		
		assertThat(group.keySet(), hasItems("France", "Canada"));

		Group<Exposure> groupFrance = group.findGroup("France");
		assertThat(groupFrance.findAll(), hasItems(FexFrance1, FexFrance2));

		Iterable<Exposure> groupFexFrance = groupFrance.find("Fex France 1");
		assertThat(groupFexFrance, hasItems(FexFrance1));

		Group<Exposure> groupCanada = group.findGroup("Canada");
		assertThat(groupCanada.findAll(), hasItems(FexCanada1, FexCanada2));

		Iterable<Exposure> groupFexCanada = groupCanada.find("Fex Canada 2");
		assertThat(groupFexCanada, hasItems(FexCanada2));
	}
}
