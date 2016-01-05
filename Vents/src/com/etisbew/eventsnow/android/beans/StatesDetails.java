package com.etisbew.eventsnow.android.beans;

public class StatesDetails {
	int id;
	int state_id;
	String state_name;
	int country_id;
	
	public StatesDetails(){
		
	}
	public StatesDetails(String state_name, int country_id) {
		this.state_name = state_name;
		this.country_id = country_id;

	}

	public StatesDetails(int state_id, String state_name, int country_id) {
		
		this.state_id = state_id;
		this.state_name = state_name;
		this.country_id = country_id;

	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getState_id() {
		return state_id;
	}

	public void setState_id(int state_id) {
		this.state_id = state_id;
	}

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	public int getCountry_id() {
		return country_id;
	}

	public void setCountry_id(int country_id) {
		this.country_id = country_id;
	}


}