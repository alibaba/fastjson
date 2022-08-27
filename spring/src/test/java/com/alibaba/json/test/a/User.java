package com.alibaba.json.test.a;


public class User{
	public User() {
	}
	public User(int age, String name) {
		super();
		this.age = age;
		this.name = name;
	}

	private int age;
	private String name;

	@Override
	public String toString() {
		return "User [age=" + age + ", name=" + name + "]";
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
