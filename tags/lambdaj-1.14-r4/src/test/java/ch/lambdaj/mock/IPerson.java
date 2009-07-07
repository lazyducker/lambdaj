package ch.lambdaj.mock;

import java.util.*;

public interface IPerson {
	public enum Gender {
		MALE, FEMALE;
	}
	
	Gender getGender();
	
	String getFirstName();
	void setFirstName(String firstName);

	String getLastName();
	void setLastName(String lastName);

	int getAge();
	void setAge(int age);
	
	Date getBirthDate();

	Person getBestFriend();
	void setBestFriend(Person bestFriend);
	
	boolean isYoungerThan(int maxAge);
}
