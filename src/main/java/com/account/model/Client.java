package com.account.model;

public class Client {

	private String firstName;
	private String lastName;

	public Client(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Client [firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}
