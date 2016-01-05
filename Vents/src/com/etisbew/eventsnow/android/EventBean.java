package com.etisbew.eventsnow.android;

import java.util.ArrayList;
import java.util.Map;

import com.etisbew.eventsnow.android.beans.CategoriesBean;
import com.etisbew.eventsnow.android.beans.EventsBean;
import com.etisbew.eventsnow.android.beans.LocationsBean;
import com.etisbew.eventsnow.android.beans.TicketsBean;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;

public class EventBean extends Application{
	int user_id;
	
	String user_name;
	String user_image;
	String email,phone;
	String address,state_name,city_name;
	/*public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
	public Editor editor = pref.edit();
	*/
	
	int state_id,city_id;
	
	Typeface text_normal,text_bold;
	
	ArrayList<EventsBean> events_upcoming;
	ArrayList<LocationsBean> location_details;
	ArrayList<CategoriesBean> categories_details;
	ArrayList<TicketsBean> tickets_details;
		
	String title;
	String venue;
	String date1;
	String gps;
	Map<Integer, String> details;
	int collectinfo;
	
	int location_id;
	int event_id;
	int city_status;
	int check_id;
	public int getState_id() {
		return state_id;
	}
	public void setState_id(int state_id) {
		this.state_id = state_id;
	}
	public int getCity_id() {
		return city_id;
	}
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public int getCollectinfo() {
		return collectinfo;
	}
	public void setCollectinfo(int collectinfo) {
		this.collectinfo = collectinfo;
	}
	public Map<Integer, String> getDetails() {
		return details;
	}
	public void setDetails(Map<Integer, String> details) {
		this.details = details;
	}
	public int getCheck_id() {
		return check_id;
	}
	public void setCheck_id(int check_id) {
		this.check_id = check_id;
	}
	public String getGps() {
		return gps;
	}
	public void setGps(String gps) {
		this.gps = gps;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getCity_status() {
		return city_status;
	}
	public void setCity_status(int city_status) {
		this.city_status = city_status;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	public ArrayList<TicketsBean> getTickets_details() {
		return tickets_details;
	}
	public void setTickets_details(ArrayList<TicketsBean> tickets_details) {
		this.tickets_details = tickets_details;
	}
	
	public String getUser_image() {
		return user_image;
	}
	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}
	
	public void setLocationId(int location_id){
		this.location_id=location_id;
	}
	public int getLocationId(){
		return location_id;
	}
	public void setUserId(int user_id){
		this.user_id=user_id;
	}
	public int getUserId(){
		return user_id;
	}
	public void setEventId(int event_id){
		this.event_id=event_id;
	}
	public int getEventId(){
		return event_id;
	}
	
	public void setUserName(String user_name){
		this.user_name=user_name;
	}
	public String getUserName(){
		return user_name;
	}
	
	public void setObject(ArrayList<EventsBean> events_upcoming){
		this.events_upcoming=events_upcoming;
	}
	public ArrayList<EventsBean> getObject(){
		return events_upcoming;
	}
	public void setLocations(ArrayList<LocationsBean> location_details){
		this.location_details=location_details;
	}
	public ArrayList<LocationsBean> getLocations(){
		return location_details;
	}
	public void setCategories(ArrayList<CategoriesBean> categories_details){
		this.categories_details=categories_details;
	}
	public ArrayList<CategoriesBean> getCategories(){
		return categories_details;
	}
	public void setTextNormal(Typeface text_normal){
		this.text_normal=text_normal;
	}
	public Typeface getTextNormal(){
		return text_normal;
	}
	public void setTextBold(Typeface text_bold){
		this.text_bold=text_bold;
	}
	public Typeface getTextBold(){
		return text_bold;
	}
	
}
