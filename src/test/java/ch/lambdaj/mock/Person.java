package ch.lambdaj.mock;


public class Person {
	
	private String firstName;
	private String lastName;
	private int age;
	
	private Person bestFriend;
	
	public Person() { }

	public Person(String firstName) { 
		this.firstName = firstName;
	}
	
	public Person(String firstName, String lastName) { 
		this(firstName);
		this.lastName = lastName;
	}
	
	public Person(String firstName, int age) { 
		this(firstName);
		this.age = age;
	}
	
	public Person(String firstName, String lastName, int age) { 
		this(firstName, lastName);
		this.age = age;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public Person getBestFriend() {
		return bestFriend;
	}
	public void setBestFriend(Person bestFriend) {
		this.bestFriend = bestFriend;
	}
}
