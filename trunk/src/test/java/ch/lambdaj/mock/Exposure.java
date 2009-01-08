package ch.lambdaj.mock;

import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.*;

public class Exposure {
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	
	public String getCountryIso() {
		return countryIso;
	}
	public void setCountryIso(String countryIso) {
		this.countryIso = countryIso;
	}
	
	public String getCountryFlag() {
		return countryFlag;
	}
	public void setCountryFlag(String countryFlag) {
		this.countryFlag = countryFlag;
	}

	private String countryName;

	private String insuredName;

	private String countryIso;
	
	private String countryFlag;
	
	public Exposure() {}
	
	public Exposure(String countryName, String insuredName) {
		this.countryName = countryName;
		this.insuredName = insuredName;
	}
	
	@Override
	public String toString() {
		return join(forEach(asList(countryName, insuredName)));
	}
}
