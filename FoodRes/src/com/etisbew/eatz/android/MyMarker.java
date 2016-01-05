package com.etisbew.eatz.android;

public class MyMarker {
	private String title;
	private String id;
	private String address;
	private int orderfood;
	
	private int bookatable;
	

	private Double mLatitude;
	private Double mLongitude;

	public MyMarker(String title, String address, Double latitude,
			Double longitude,String id,int orderfood,int bookatable) {
		this.title = title;
		this.address = address;
		this.mLatitude = latitude;
		this.mLongitude = longitude;
		this.id = id;
		this.orderfood = orderfood;
		this.bookatable = bookatable;

	} 
	
	public Integer getOrderfood() {
		return orderfood;
	}

	public void setOrderfood(Integer orderfood) {
		this.orderfood = orderfood;
	}

	public Integer getBookatable() {
		return bookatable;
	}

	public void setBookatable(Integer bookatable) {
		this.bookatable = bookatable;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getmTitle() {
		return title; 
	}

	public void setmTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getmLatitude() {
		return mLatitude;
	}

	public void setmLatitude(Double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public Double getmLongitude() {
		return mLongitude;
	}

	public void setmLongitude(Double mLongitude) {
		this.mLongitude = mLongitude;
	}
}
