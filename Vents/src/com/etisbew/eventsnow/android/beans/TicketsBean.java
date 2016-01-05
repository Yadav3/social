package com.etisbew.eventsnow.android.beans;

import java.util.ArrayList;

public class TicketsBean {
	public int id;
	public String title;
	public String description;
	public int ticket_total;
	public int minimum_persons;
	public int price_per_ticket;
	public String start_date;
	public String end_date;
	public int ticket_buy_limit;
	public int ticket_sold;
	public int alert_me;
	public int event_id;
	public int display_status;
	public int show_soldout_status;
	public Double tax;
	
	public ArrayList<Integer> promotion_id = new ArrayList<Integer>();
	public ArrayList<String> discount_code = new ArrayList<String>();
	public ArrayList<Integer> code_max_allowed= new ArrayList<Integer>();
	public ArrayList<String> p_start_date= new ArrayList<String>();
	public ArrayList<String> p_end_date= new ArrayList<String>();
	public ArrayList<String> discount_type= new ArrayList<String>();
	public ArrayList<Double> discount= new ArrayList<Double>();

}
