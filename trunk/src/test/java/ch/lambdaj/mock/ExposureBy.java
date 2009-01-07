package ch.lambdaj.mock;

import java.util.*;

public class ExposureBy extends LinkedList {

	public static final ExposureBy Exposure = new ExposureBy();

	public ExposureBy insuredName() {
		add("insuredName");
		add("by");
		return this;
	}

	public ExposureBy id() {
		add("id");
		add("by");
		return this;
	}

	public ExposureBy countryName() {
		add("countryName");
		add("by");
		return this;
	}

	public ExposureBy asInsureds() {
		add("insureds");
		add("as");
		return this;
	}

	public ExposureBy asCountries() {
		add("countries");
		add("as");
		return this;
	}

	public ExposureBy asExposures() {
		add("exposures");
		add("as");
		return this;
	}

}