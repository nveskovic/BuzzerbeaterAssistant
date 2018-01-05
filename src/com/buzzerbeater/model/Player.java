package com.buzzerbeater.model;

import java.util.HashMap;

public class Player {


	long id;
	String name;
	String height;
	String owner;
	String age;
	String dmi;
	String salary;
	int potential;
	HashMap<String, Integer> skills;
	public static String[] skillNames = {"js", "jr", "od", "ha", "dr", "pa", "is", "id", "rb", "sb", "st", "ft", "ex"};
	
	public Player(long id, String name, String owner, String age, 
			String dmi, String salary, String height, int potential, HashMap<String, Integer> skills) {
		super();
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.age = age;
		this.height = height;
		this.dmi = dmi;
		this.salary = salary;
		this.potential = potential;
		this.skills = skills;
	}
	
	public static String getCsvHeader() {
		String toReturn = "name,url,pot,owner,age,height,dmi,salary";
		for (String skill : skillNames) {
			toReturn+=","+skill;
		}
		return toReturn;
	}
	
	public String getCsvPlayerInfo() {
		String toReturn = name + ",http://www.buzzerbeater.com/player/" + id + "/overview.aspx"
				+ "," + potential + "," + owner + "," + age + "," + height
				+ "," + dmi + "," + salary;
		for (String skill : skillNames) {
				toReturn+=","+(skills.containsKey(skill) ? skills.get(skill) : "");
		}
		return toReturn;
	}

	public int getPotential() {
		return this.potential;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
