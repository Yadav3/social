package com.etisbew.eatz.objects;

import java.io.Serializable;

public class RestaurentDO implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	public String id = "";
	public String favoriteid = "";
	public String name = "";
	public String addressLine1 = "";
	public String addressLine2 = "";
	public String phone = "";
	public String lat = "";
	public String lang = "";
	public String distence = "";
	public String location = "";
	public String cuisines_list = " ";
	public String review_count = "0";
	public String review_rating = "0";
	public double currentdistance = 0;
	public boolean has_deals;
	public boolean happy_hours;
	public boolean has_booktable;
	public boolean has_orderfood;
	public String price = "0";
	public String happy_hours_start = "";
	public String happy_hours_end = "";
	public String next_sunday_brunch = "";
	public String has_sundayBrunch = "";
	public String brunch_timings = "";
	public String brunch_description = "";
	public String existing_buffets = "";
	public String check_open = "";
	public String serving_timings = "";
	public String facilities = "";
	public String drinks_names = "";
	public boolean CardsAccepted;
	public boolean FineDining;
	public boolean is_veg;
	public boolean is_delivery;
	public boolean is_takeaway;
	public boolean liqour_served;
	public String has_features = "";
	public String serving_items = "";
	public String latitude="";
	public String longitude="";
	public String keyword="";
	public String orderfood="";
	public String bookatable="";
	public String show_price="";
	public String orderdate="";
	public String orderamount="";
	public String ordertype="";
}
