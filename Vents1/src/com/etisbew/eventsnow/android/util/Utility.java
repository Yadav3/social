package com.etisbew.eventsnow.android.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;


public class Utility {
	public Context mContext;  

	public Utility(Context context) {
		mContext = context;
	}

	String baseUrl = "http://www.happeninghyderabad.com/xmlevents/";
	String baseUrl1 = "http://www.happeninghyderabad.com/webservices/";

	public String setFutureEvents() {
		String f_events = baseUrl+ "GetFeaturedEvents/getxmlFeaturedEvents.xml";
		return f_events;

	}//http://www.happeninghyderabad.com/webservices/getLocations.xml
	public String setLocations() {
		String locations = baseUrl1+ "getLocations.xml";
		return locations;
 
	}//getCategoriesList/22960/getCategoriesList.xml
	public String setCategories(int city_id) {
		String categories = baseUrl1+ "getCategoriesList/"+city_id+"/getCategoriesList.xml";
		return categories;
   
	} 
	public String setPrintTicket(String ticketid,String email) {
		String pticket ="http://dev.eventsnow.com/WebServices/printTicket/"+ticketid+"/"+email;
		return pticket;
   
	} 
 
	public String setEvents(String category_id,int lcoation_id) {
		String events = baseUrl1 + "FilteredEvents/"+category_id+"/"+lcoation_id+"/sd.xml";
		return events; 

	}	  
	public String setEventsDetails(int id) {
		String events_details = baseUrl1 + "Eventdetails/"+id+"/eventdetails.xml";
		return events_details;

	} 
	
	public String setLogin() {
			String login = baseUrl1 + "login/gg.xml";
			return login; 

		}
	public String setService() {
		String service = baseUrl1 + "ConfirmOrder/gg.xml";
		return service;

	}
	public String setAuthenticate(int transaction_id) {
		//http://dev.eventsnow.com/transactions/econfirm/"+transaction_id+"?mobile=1
		//http://www.eventsnow.com/transactions/econfirm/100757?mobile=0
		String service = "http://www.eventsnow.com/" + "transactions/econfirm/"+transaction_id+"?mobile=1";
		return service;

	}
	public String setMyAccount(int user_id) {
			String myaccount = baseUrl1 + "MyAccount/"+user_id+"/gg.xml";
			return myaccount;

		} 
	public String setAddList(int user_id,int event_id) {
		String add_list = baseUrl1 + "AddToMyList/"+event_id+"/"+user_id+"/sdd.xml";
		return add_list;
 
	}
	public String setGmailEmail() { 
		String gmail = baseUrl1 + "LoginWithGoogle/sdd.xml";
		return gmail; 

	}
	public String setFbEmail(String token,String user_name,String email) {
		String fb = baseUrl1 + "LoginWithFacebook/"+user_name+"/"+email+"/"+token+"/sdd.xml";
		return fb;

	}
	public String setStates() {
		String states = baseUrl1 + "getStatesByCountry/110/gg.xml";
		return states;

	}
	public String setCities(int city_id) {
		String city = baseUrl1 + "getCitiesByState/"+city_id+"/gg.xml";
		return city;

	}//http://www.happeninghyderabad.com/webservices/UpdateProfile/136/ramesh/qwerty2/36/22960/charminar/9000000000/gg.xml
	public String setUpdateMyAccount(int user_id,String name,String cname,int state_id,int city_id,String address,String phone) {
		String profile = baseUrl1 + "UpdateProfile/"+user_id+"/"+name+"/"+cname+"/"+state_id+"/"+city_id+"/"+address+"/"+phone+"/gg.xml";
		return profile;

	}//://www.happeninghyderabad.com/webservices/SignUp/name/email/password/confirmpassword/company/phone/stateid/cityid/test.xml
	public String setNewMyAccount(String name,String email,String password,String cpassword,String cname,String phone,int state_id,int city_id) {
		String new_account = baseUrl1 + "SignUp/"+name+"/"+email+"/"+password+"/"+cpassword+"/"+cname+"/"+phone+"/"+state_id+"/"+city_id+"/test.xml";
		return new_account;

	}
	public String setTickets(int event_id) {
		String tickets = baseUrl1 + "getTicketsByEvent/"+event_id+"/gg.xml";
		return tickets;

	}//http://www.happeninghyderabad.com/webservices/MyticketDetails/90/253/42/gg.xml
	
	public String setMyTicketDetails(int event_id,int user_id,int ticket_id) {
		String tickets = baseUrl1 + "MyticketDetails/"+event_id+"/"+user_id+"/"+ticket_id+"/gg.xml";
		return tickets;

	}
	public String setMyTickets(int user_id) {
		String tickets1 = baseUrl1 + "getMyTickets/"+user_id+"/gg.xml";
		return tickets1;

	}
	public String setMyList(int user_id) {
		String my_list = baseUrl1 + "UserFavorites/"+user_id+"/UserFavorites.xml";
		return my_list;

	}//http://www.happeninghyderabad.com/xmlevents/DeleteFromMyList/1531/2/sdd.xml
	public String setDeleteList(int user_id,int event_id) {
		String del_list = baseUrl1 + "DeleteFromMyList/"+event_id+"/"+user_id+"/sdd.xml";
		return del_list;

	}
	public String setAbout() {
		String events_details = baseUrl + "pages/AboutUs/pages.xml";
		return events_details;

	}
	public String setChangePwd(int user_id,String old,String new_pwd) {
		String events_details = baseUrl1 + "ChangePassword/"+user_id+"/"+old+"/"+new_pwd+"/ChangePassword.xml";
		return events_details;

	}
	public String setForgot(String email) {
		String forgot = baseUrl1 + "forgotPassword/"+email+"/gg.xml";
		return forgot;

	}
	public boolean validEmail(String email) {
		Pattern pattern = Patterns.EMAIL_ADDRESS;
		return pattern.matcher(email).matches(); 
		}
	public String setContact(String fname,String lastname,String city,String country,String email,String gender,String phone,String requesttype,String message) {
		String events_details = baseUrl + "PostFeedback/test.xml?"+"firstname="+fname+"&"+"lastname="+lastname+"&"+"city="+city+"&"+"country="+country+"&"+"email="+email+"&"+"gender="+gender+"&"+"phone="+phone+"&"+"requesttype="+requesttype+"&"+"message="+message;
		return events_details;

	}
	/*String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\
		      @([\\w]+\\.)+[\\w]+[\\w]$";
		      String email1 = "user@domain.com";
		      Boolean b = email1.matches(EMAIL_REGEX);
		      System.out.println("is e-mail: "+email1+" :Valid = " + b);
		      String email2 = "user^domain.co.in";
		      b = email2.matches(EMAIL_REGEX);
		      System.out.println("is e-mail: "+email2
		      +" :Valid = " + b);*/

	public String getXmlFromUrl(String url) {
		System.out.println("given url" + url);
		String xml = "";  
	 	
		try { 
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				xml = EntityUtils.toString(httpEntity);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return XML
		return xml;
	}

	public double distance(double lat1, double lon1, double lat2, double lon2,
			char unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private double deg2rad(double deg) {

		return (deg * Math.PI / 180.0);
 
	}

	private double rad2deg(double rad) {

		return (rad * 180 / Math.PI);
 
	} 
	

	public boolean IsNetConnected(Context context) {
		boolean isConnected = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++) {

					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						isConnected = true;
					}
				}
		}
		System.out.println("Internet Connection is: " + isConnected);
		return isConnected; 
	}

	public void showAlertNoInternetService(Context context) {
		AlertDialog.Builder altDialog = new AlertDialog.Builder(context);
		//altDialog.setTitle("Alert");
		altDialog
				.setMessage("Sorry, Network is not available. Please try again later"); // here
																					// add
																					// your
																					// message
		altDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		altDialog.show();
	}

	public void showAlertFavorites(Context context, String message) {
		AlertDialog.Builder altDialog = new AlertDialog.Builder(context);
		altDialog.setMessage(message); // here add your message
		altDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		altDialog.show();
	}

		public String dateConvert(String startDate,String endDate) {
		String text = null;
		
		boolean flag=dateComparison(startDate.replace("T", " "), endDate.replace("T", " "));
		if(flag){
			try {
		    	String string = startDate.replace("T", " ");
			    String[] parts = string.split(" ");
			    String[] parts1 = parts[1].split(":");
			    
			      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    	  SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		          Date date1 = formatter.parse(parts[0]);
		          DateFormat out = new SimpleDateFormat("EEE dd MMM, yyyy");
		         
		         text=out.format(date1)+" "+parts1[0]+":"+parts1[1];
		           
	               
		          } catch (ParseException e) {
		          e.printStackTrace();
		      }
			
		}else{
			try {
		    	String string = startDate.replace("T", " "); 
			    String[] parts = string.split(" ");
			    String[] parts2 = parts[1].split(":");
			    
			    String string1 = endDate.replace("T", " ");
			    String[] parts1 = string1.split(" "); 
			    String[] parts3 = parts1[1].split(":");
			    
//			    String[] parts1 = parts[1].split("sai");
		    	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    	  SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		          Date date1 = formatter.parse(parts[0]);
		          Date date12 = formatter.parse(parts1[0]);
		          DateFormat out = new SimpleDateFormat("EEE dd MMM, yyyy");
		          DateFormat out1 = new SimpleDateFormat("EEE dd MMM, yyyy");
		          boolean flag1=Date_Comparison(parts[0], parts1[0]);
		          if(flag1){
		              text=out.format(date1)+" "+parts2[0]+":"+parts2[1]+" To "+parts3[0]+":"+parts3[1];
		          }else{
		        	  text=out.format(date1)+" "+parts2[0]+":"+parts2[1]+" To "+out1.format(date12)+" "+parts3[0]+":"+parts3[1];  
		          }
		           
	               
		          } catch (ParseException e) {
		          e.printStackTrace();
		      }
			
		}
		
		return text;
	}

	public String dateConvert1(String D) {

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy",
				Locale.US);
		Date date = null;
		try {

			date = format1.parse(D);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dateString = format2.format(date);
		dateString = dateString.replace("-", " ");
		return ((dateString));
	}
	public boolean dateComparison(String date,String dateafter){
		boolean flag = false;
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
		    Date convertedDate = new Date();
		    Date convertedDate2 = new Date();
		    try {
		        convertedDate = dateFormat.parse(date);
		        convertedDate2 = dateFormat.parse(dateafter);
		        if (convertedDate2.equals(convertedDate)) {
		            flag=true;
		        } else {
		        	 flag=false;
		        }
		    } catch (ParseException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
			return flag; 
	}
	public boolean dateComparison1(String date,String dateafter){
		boolean flag = false;
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
		    Date convertedDate = new Date();
		    Date convertedDate2 = new Date();
		    try { 
		        convertedDate = dateFormat.parse(date);
		        convertedDate2 = dateFormat.parse(dateafter);
		        System.out.println("dates"+convertedDate+convertedDate2);
		        if (convertedDate.before(convertedDate2)) {
		        	
		            flag=false;
		        } else {
		        	 flag=true;  
		        }
		    } catch (ParseException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    } 
			return flag; 
	}
	public boolean dateComparison2(String date,String dateafter){
		boolean flag = false;
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
		    Date convertedDate = new Date();
		    Date convertedDate2 = new Date();
		    try { 
		        convertedDate = dateFormat.parse(date);
		        convertedDate2 = dateFormat.parse(dateafter);
		        System.out.println("before dates"+convertedDate+convertedDate2);
		        if (convertedDate.before(convertedDate2)) {
		        	
		            flag=false;
		        } else {
		        	 flag=true;
		        }
		    } catch (ParseException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
			return flag; 
	}
	public boolean dateComparison3(String date,String dateafter){
		boolean flag = false;
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
		    Date convertedDate = new Date();
		    Date convertedDate2 = new Date();
		    try { 
		        convertedDate = dateFormat.parse(date);
		        convertedDate2 = dateFormat.parse(dateafter);
		        System.out.println("dateComparison3"+convertedDate+convertedDate2);
		        if (convertedDate2.after(convertedDate)) {
		        	System.out.println("sys true");
		            flag=false; 
		        } else {
		        	System.out.println("sys false");
		        	 flag=true;  
		        }
		    } catch (ParseException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    } 
			return flag; 
	}
	public  boolean CheckDates(String startDate, String endDate) {

	    SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

	    boolean b = false;

	    try {
	      
	    	java.util.Date d1 = dfDate.parse( startDate );  
	    	java.util.Date d2 = dfDate.parse( endDate );  
	    	if ( compareTo( d1, d2 ) <= 0 )  
	    	{  
	    	   System.out.println( "d1 is before d2" );  
	    	   b=false;
	    	}  
	    	else if ( compareTo( d1, d2 ) > 0 )   
	    	{  
	    	   System.out.println( "d1 is after d2" );  
	    	   b=true;
	    	}   
	    	
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    return b;
	}
	public long compareTo( java.util.Date date1, java.util.Date date2 )  
	{  
	
	  return date1.getTime() - date2.getTime();  
	}  
	public boolean Date_Comparison(String date,String dateafter){
		boolean flag = false;
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		    Date convertedDate = new Date();
		    Date convertedDate2 = new Date();
		    try {
		        convertedDate = dateFormat.parse(date);
		        convertedDate2 = dateFormat.parse(dateafter);
		        if (convertedDate2.equals(convertedDate)) {
		            flag=true;
		        } else { 
		        	 flag=false;
		        }
		    } catch (ParseException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
			return flag;
	}
	public void dialogExample1(String sss) {
		AlertDialog.Builder altDialog = new AlertDialog.Builder(mContext);
		altDialog.setMessage(sss); // here add your message
		altDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		altDialog.show();
	}
	public void dialogExample() {
		/*
		 * AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		 * builder.setMessage("No data from server!"); builder.show();
		 */

		AlertDialog.Builder altDialog = new AlertDialog.Builder(mContext);
		altDialog.setMessage("No data from server!"); // here add your message
		altDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		altDialog.show();
	}

}
