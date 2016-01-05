package com.etisbew.eventsnow.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.etisbew.eventsnow.android.beans.TicketsBean;
import com.etisbew.eventsnow.android.buyticket.BuyTickets;
import com.etisbew.eventsnow.android.login.LoginActivity;
import com.etisbew.eventsnow.android.readmore.ReadMoreActivity;
import com.etisbew.eventsnow.android.util.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

@SuppressLint("NewApi")
public class Details extends FragmentActivity implements OnClickListener,
		OnMarkerClickListener, OnMarkerDragListener {
	Button buyticket, readmore;
	int user_id;
	TextView back, event_title, event_date, event_time, event_desc, event_day,
			event_full_desc, address, contactinfo, name, phone, contact,
			website, facebook, twitter, button1, button2, button3, category1;
	EventBean event;
	int event_id, success_id, del_id, collectinfo;
	ImageView image;
	Utility util;
	String url, url1, url2, url3, url4;
	AQuery androidAQuery;
	private GoogleMap googleMap;
	FragmentManager mapFrag; 
	Button zoombtn;
	RelativeLayout menuLayout;
	ArrayList<Integer> id_list;
	LinearLayout addtocalendar, addtofavorite;
	String title, description, startdate, enddate, category, venue, thumbnail,
			city, region, lat, lon, cperson, web, email, phoneno, gps,
			facebook1, twitter1, event_type, ext_link;
	ArrayList<LatLng> markerPoints;
	ArrayList<Integer> id;
	ArrayList<String> title1;
	ArrayList<String> description1;
	ArrayList<Integer> ticket_total;
	ArrayList<Integer> minimum_persons;
	ArrayList<Integer> price_per_ticket;
	ArrayList<String> start_date;
	ArrayList<String> end_date;
	ArrayList<Integer> ticket_buy_limit;
	ArrayList<Integer> ticket_sold;
	ArrayList<Integer> alert_me;
	ArrayList<Integer> event_id1;
	ArrayList<Integer> display_status;
	ArrayList<Integer> show_soldout_status;
	ArrayList<Double> tax;
	ArrayList<Double> discount;
	ArrayList<Integer> promotion_id;
	ArrayList<String> discount_code;
	ArrayList<Integer> code_max_allowed;
	ArrayList<String> p_start_date;
	ArrayList<String> p_end_date;

	ArrayList<TicketsBean> tickets_details;
	ScrollView scr;
	GPSTracker gps1;
	Double latitude, longitude;
	int mMode = 0;
	final int MODE_DRIVING = 0;
	final int MODE_BICYCLING = 1;
	final int MODE_WALKING = 2;
	EditText email1;
	Dialog d;
	int status;
	String contact_url;
	List<NameValuePair> nameValuePairs;
	SharedPreferences pref;
	Editor editor;
	String search;
	public static final String MyPREFERENCES = "MyPrefs";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {
			event_id = extras.getInt("event_id", 0);
			collectinfo = extras.getInt("collectinfo", 1);
			title = extras.getString("title");
			description = extras.getString("description");
			startdate = extras.getString("startdate");
			enddate = extras.getString("enddate");
			category = extras.getString("category");
			venue = extras.getString("venue");
			thumbnail = extras.getString("thumbnail");
			city = extras.getString("city");
			region = extras.getString("region");
			gps = extras.getString("gps");
			event_type = extras.getString("event_type");
			scr = (ScrollView) findViewById(R.id.scrollView1);
			cperson = extras.getString("cperson");
			web = extras.getString("web");
			email = extras.getString("email");
			phoneno = extras.getString("phoneno");
			facebook1 = extras.getString("facebook1");
			twitter1 = extras.getString("twitter1");
			ext_link = extras.getString("ext_link");
			search=extras.getString("search");

		}
		if(TextUtils.isEmpty(search)){
			search="no search";
		}
		event = (EventBean) getApplicationContext();
		pref = getApplicationContext().getSharedPreferences("MyPref",
				MODE_PRIVATE);
		editor = pref.edit();
		if(pref.getInt("user_id", 0)>0){
			event.setUserId(pref.getInt("user_id", 0));
			event.setUserName(pref.getString("user_name", ""));
			event.setUser_image(pref.getString("user_image", ""));
			event.setEmail(pref.getString("email", ""));
			event.setPhone(pref.getString("phone", ""));
			event.setAddress(pref.getString("address", "")); 
			event.setState_name(pref.getString("state", ""));
			event.setCity_name(pref.getString("city", ""));
			event.setState_id(pref.getInt("state_id", 0)); 
			event.setCity_id(pref.getInt("city_id", 0));
			
			editor.commit(); 
		} 
		util = new Utility(Details.this);
		androidAQuery = new AQuery(Details.this);
		System.out.println("event_id"+event_id+event.getEventId());
		event_id = event.getEventId();
		url = util.setEventsDetails(event_id);
		user_id = event.getUserId();

		id = new ArrayList<Integer>();
		title1 = new ArrayList<String>();
		description1 = new ArrayList<String>();
		start_date = new ArrayList<String>();
		end_date = new ArrayList<String>();
		ticket_total = new ArrayList<Integer>();
		minimum_persons = new ArrayList<Integer>();
		price_per_ticket = new ArrayList<Integer>();
		ticket_buy_limit = new ArrayList<Integer>();
		ticket_sold = new ArrayList<Integer>();
		alert_me = new ArrayList<Integer>();
		event_id1 = new ArrayList<Integer>();
		display_status = new ArrayList<Integer>();

		tax = new ArrayList<Double>();
		discount = new ArrayList<Double>();

		promotion_id = new ArrayList<Integer>();
		discount_code = new ArrayList<String>();
		code_max_allowed = new ArrayList<Integer>();
		p_start_date = new ArrayList<String>();
		p_end_date = new ArrayList<String>();

		show_soldout_status = new ArrayList<Integer>();

		tickets_details = new ArrayList<TicketsBean>();

		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		event_desc = (TextView) findViewById(R.id.event_desc);
		event_day = (TextView) findViewById(R.id.event_day);
		contactinfo = (TextView) findViewById(R.id.contactinfo);
		event_full_desc = (TextView) findViewById(R.id.event_full_desc);
		address = (TextView) findViewById(R.id.address);
		buyticket = (Button) findViewById(R.id.buyticket);
		readmore = (Button) findViewById(R.id.readmore);
		button1 = (TextView) findViewById(R.id.button1);
		button2 = (TextView) findViewById(R.id.button2);
		button3 = (TextView) findViewById(R.id.button3);
		category1 = (TextView) findViewById(R.id.category);
		id_list = new ArrayList<Integer>();

		image = (ImageView) findViewById(R.id.item_icon);

		zoombtn = (Button) findViewById(R.id.zoombtn);

		addtocalendar = (LinearLayout) findViewById(R.id.adddtocalendar);
		addtofavorite = (LinearLayout) findViewById(R.id.adddtofavorite);
		menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);

		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		contact = (TextView) findViewById(R.id.contact);
		website = (TextView) findViewById(R.id.website);
		facebook = (TextView) findViewById(R.id.facebook);
		twitter = (TextView) findViewById(R.id.twitter);

		event_title.setTypeface(event.getTextBold());
		buyticket.setTypeface(event.getTextBold());
		readmore.setTypeface(event.getTextBold());
		button1.setTypeface(event.getTextBold());
		button2.setTypeface(event.getTextBold());
		category1.setTypeface(event.getTextBold());

		event_desc.setTypeface(event.getTextBold());
		event_day.setTypeface(event.getTextBold());
		event_full_desc.setTypeface(event.getTextNormal());
		address.setTypeface(event.getTextNormal());
		contactinfo.setTypeface(event.getTextBold());
		address.setTypeface(event.getTextNormal());
		name.setTypeface(event.getTextNormal());
		phone.setTypeface(event.getTextNormal());
		contact.setTypeface(event.getTextNormal());
		website.setTypeface(event.getTextNormal());
		facebook.setTypeface(event.getTextNormal());
		twitter.setTypeface(event.getTextNormal());

		// ImageView iv = (ImageView)home.findViewById(R.id.image);
		buyticket.setOnClickListener(this);
		readmore.setOnClickListener(this);
		zoombtn.setOnClickListener(this);
		facebook.setOnClickListener(this);
		twitter.setOnClickListener(this);
		addtocalendar.setOnClickListener(this);
		addtofavorite.setOnClickListener(this);
		phone.setOnClickListener(this);
		contact.setOnClickListener(this);
		website.setOnClickListener(this);
		facebook.setOnClickListener(this);
		twitter.setOnClickListener(this);
		back.setOnClickListener(this);
		event_title.setOnClickListener(this);
		menuLayout.setOnClickListener(this);
		// iv.setOnClickListener(this);
		gps1 = new GPSTracker(getBaseContext()) {
		};
		if (gps1.canGetLocation()) {
			latitude = gps1.getLatitude();
			longitude = gps1.getLongitude();
		}
		Setting_Event_Details();
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed(); 
		if(search.equalsIgnoreCase("search")){
			finish();
		}else{
			startActivity(new Intent(Details.this, MainActivity.class));
			finish();
		}
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.readmore:

			Intent n = new Intent(Details.this, ReadMoreActivity.class);
			n.putExtra("title", title);
			n.putExtra("date", util.dateConvert(startdate, enddate));
			n.putExtra("desc", description);
			n.putExtra("event_type", event_type);
			n.putExtra("event_id", event_id);
			n.putExtra("gps", gps);
			n.putExtra("start_date", startdate);
			n.putExtra("end_date", enddate);
			n.putExtra("venue", venue);
			n.putExtra("ext_link", ext_link);
			n.putExtra("collectinfo", collectinfo);
			startActivity(n);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;
		case R.id.buyticket:
			url4 = util.setTickets(event_id);
			if (Integer.parseInt(event_type) == 3) {
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(ext_link));
				startActivity(intent);

				overridePendingTransition(R.anim.trans_left_in,
						R.anim.trans_left_out);
			} else {

				Boolean status = util.IsNetConnected(Details.this);
				if (status) {
					new GettingTickets().execute();
				} else {
					util.showAlertNoInternetService(Details.this);
				}
			}
			break;
		case R.id.zoombtn:
			Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?   saddr="
							+ latitude + "," + longitude + "&daddr=" + lat
							+ "," + lon));
			intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent1.addCategory(Intent.CATEGORY_LAUNCHER);
			intent1.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			startActivity(intent1);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;

		case R.id.facebook:
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook1));
			startActivity(intent);

			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);

			break;
		case R.id.twitter:
			Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter1));
			startActivity(intent2);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);

			break;

		case R.id.adddtofavorite:

			if (user_id > 0) {

				try {
					if (id_list.contains(event_id)) {
						url2 = util.setDeleteList(event.getUserId(), event_id);
						Boolean status = util.IsNetConnected(Details.this);
						if (status) {
							new DeleteList().execute();

						} else {
							util.showAlertNoInternetService(Details.this);
						}
					} else {
						url1 = util.setAddList(event.getUserId(), event_id);
						Boolean status = util.IsNetConnected(Details.this);
						if (status) {
							new AddToMyList().execute();

						} else {
							util.showAlertNoInternetService(Details.this);
						}
					}
				} catch (Exception e) {

				}

			} else {
				Intent in1 = new Intent(Details.this, LoginActivity.class);
				// in1.putExtra("activity","com.etisbew.eventsnow.android.MainActivity");
				in1.putExtra("target", "Details");
				startActivity(in1);
				// Details.this.finish();
				overridePendingTransition(R.anim.trans_left_in,
						R.anim.trans_left_out);
			}
			break;
		case R.id.adddtocalendar:
			if (Build.VERSION.SDK_INT > 11) {
				addReminder();
			}

			break;
		case R.id.website:
			String url = web;
			Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent3);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;

		case R.id.phone:
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:+" + phoneno));
			startActivity(callIntent);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;

		case R.id.contact:
			contact_organizer();
			break;

		case R.id.textView1:
			if(search.equalsIgnoreCase("search")){
				finish();
			}else{
				startActivity(new Intent(Details.this, MainActivity.class));
				finish();
			}
			
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
			break;
		case R.id.event_title:
			if(search.equalsIgnoreCase("search")){
				finish();
			}else{
				startActivity(new Intent(Details.this, MainActivity.class));
				finish();
			}
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
			break;
		case R.id.menuLayout:
			if(search.equalsIgnoreCase("search")){
				finish();
			}else{
				startActivity(new Intent(Details.this, MainActivity.class));
				finish();
			}
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
			break;

		}

	}

	private void parsingMapData() {
		try {
			googleMap.clear();
			if (googleMap != null) {

				// marker using custom image
				LatLng WALL_STREET = new LatLng(Double.parseDouble(lat),
						Double.parseDouble(lon));
				googleMap
						.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
								.position(WALL_STREET)
								.title("")
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

			}
			LatLng centerCoord = new LatLng(17.3660, 78.4760);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerCoord,
					10));

		} catch (Exception e) {
		}
	}

	private void addGoogleMap() {
		// check if we have got the googleMap already
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.setOnMarkerClickListener(this);
			googleMap.setOnMarkerDragListener(this);
		}

	}

	public void Setting_Event_Details() {
		String[] gps_array = gps.split(",");
		lat = gps_array[0];
		lon = gps_array[1];
		markerPoints = new ArrayList<LatLng>();
		// 19th commented
		addGoogleMap();
		parsingMapData();
		event_title.setText(title);
		LatLng WALL_STREET = new LatLng(Double.parseDouble(lat),
				Double.parseDouble(lon));

		LatLng userLocation = new LatLng(latitude, longitude);
		// Adding new item to the ArrayList
		markerPoints.add(userLocation);
		markerPoints.add(WALL_STREET);

		if (markerPoints.size() >= 2) {

			// Getting URL to the Google Directions API
			String url = getDirectionsUrl(userLocation, WALL_STREET);

			DownloadTask downloadTask = new DownloadTask();

			// Start downloading json data from Google Directions API
			// 19th commented
			downloadTask.execute(url);

			drawStartStopMarkers();
		} 

		LatLng centerCoord = new LatLng(Double.parseDouble(lat),
				Double.parseDouble(lon));
		// 19th commented
		/*googleMap
				.moveCamera(CameraUpdateFactory.newLatLngZoom(centerCoord, 12));*/

		// }
		// 19th commented
		/*googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
			}
		});*/
		event_day.setText(util.dateConvert(startdate, enddate));
		event_full_desc.setText(Html.fromHtml(description));
		address.setText(venue);
		name.setText(cperson);
		phone.setText(phoneno);
		category1.setText(category);
		if (facebook1.length() == 1 || facebook1.length() == 0) {
			facebook.setVisibility(View.GONE);
		} else {
			facebook.setVisibility(View.VISIBLE);
			facebook.setText(R.string.facebook);
		}
		if (twitter1.length() == 1 || twitter1.length() == 0) {
			twitter.setVisibility(View.GONE);
		} else {
			twitter.setVisibility(View.VISIBLE);
			twitter.setText(R.string.twitter);
		}
		if (web.length() == 1 || web.length() == 0) {
			website.setVisibility(View.GONE);
		} else {
			website.setVisibility(View.VISIBLE);
			website.setText(R.string.viewwebsite);
		}
		if (email.length() == 1 || email.length() == 0) {
			contact.setVisibility(View.GONE);
		} else {
			contact.setVisibility(View.GONE);
			contact.setText(R.string.organizer);
		}
		if (cperson.length() == 1 || cperson.length() == 0) {
			name.setVisibility(View.GONE);
		} else {
			name.setVisibility(View.VISIBLE);
		}
		if (phoneno.length() == 1 || phoneno.length() == 0) {
			phone.setVisibility(View.GONE);
		} else {
			phone.setVisibility(View.VISIBLE);
		}
		image.setBackgroundResource(Color.TRANSPARENT);
		androidAQuery.id(image).image(thumbnail, true, true);
		try {
			if (event.getUserId() > 0) {
				url3 = util.setMyList(event.getUserId());

				Boolean status = util.IsNetConnected(Details.this);
				if (status) {
					new GettingEvents().execute();

				} else {
					util.showAlertNoInternetService(Details.this);
				}
			}
		} catch (Exception e) {

		}
		scr.fullScroll(ScrollView.FOCUS_UP);
		if (Build.VERSION.SDK_INT > 11) {
			scr.smoothScrollTo(0, (int) menuLayout.getY());
		}
		if (Integer.parseInt(event_type) == 1) {

		} else if (Integer.parseInt(event_type) == 2) {

		} else if (Integer.parseInt(event_type) == 3) {
			buyticket.setText("Registeration Now");
		} else if (Integer.parseInt(event_type) == 4) {
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout7);
			rl.setVisibility(View.GONE);
			buyticket.setVisibility(View.GONE);
		}

	}

	private void drawStartStopMarkers() {

		for (int i = 0; i < markerPoints.size(); i++) {

			// Creating MarkerOptions
			MarkerOptions options = new MarkerOptions();

			// Setting the position of the marker
			options.position(markerPoints.get(i));

			/**
			 * For the start location, the color of marker is GREEN and for the
			 * end location, the color of marker is RED.
			 */
			if (i == 0) {
				options.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			} else if (i == 1) {
				options.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			}

			// Add new marker to the Google Map Android API V2
			googleMap.addMarker(options);
		}
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Travelling Mode
		String mode = "mode=driving";

		// if(rbDriving.isChecked()){
		mode = "mode=driving";
		mMode = 0;
		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"
				+ mode;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);

				// Changing the color polyline according to the mode
				if (mMode == MODE_DRIVING)
					lineOptions.color(Color.RED);
				else if (mMode == MODE_BICYCLING)
					lineOptions.color(Color.GREEN);
				else if (mMode == MODE_WALKING)
					lineOptions.color(Color.BLUE);
			}

			if (result.size() < 1) {
				Toast.makeText(getBaseContext(), "No Points",
						Toast.LENGTH_SHORT).show();
				return;
			}

			// Drawing polyline in the Google Map for the i-th route
			googleMap.addPolyline(lineOptions);

		}
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub

	}

	private class AddToMyList extends AsyncTask<String, Void, String> {
		private static final String ADDTOMYLISTRESPONSE = "AddToMyListResponse";
		private static final String RESULT = "Result";
		private static final String RESULTCODE = "ResultCode";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(Details.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(url1);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("url1"+url1);
			System.out.println("result"+result);
			if (null == result || result.length() == 0
					|| TextUtils.isEmpty(result)) {

				util.dialogExample();

			} else {
				try {
					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser parser = factory.newPullParser();

					parser.setInput(new StringReader(result));

					int eventType = parser.getEventType();

					while (eventType != XmlPullParser.END_DOCUMENT) {
						String name = null;
						switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(ADDTOMYLISTRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(RESULTCODE)) {
									success_id = Integer.parseInt(parser
											.nextText());
								}
							}

							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(ADDTOMYLISTRESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				AddToMylist_Update();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	private class DeleteList extends AsyncTask<String, Void, String> {
		private static final String RESULT = "Result";
		private static final String DELETEFROMMYLISTRESPONSE = "DeleteFromMyListResponse";
		private static final String RESULTCODE = "ResultCode";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(Details.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(url2);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null == result || result.length() == 0
					|| TextUtils.isEmpty(result)) {

				util.dialogExample();

			} else {
				try {
					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser parser = factory.newPullParser();

					parser.setInput(new StringReader(result));

					int eventType = parser.getEventType();

					while (eventType != XmlPullParser.END_DOCUMENT) {
						String name = null;
						switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(DELETEFROMMYLISTRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(RESULTCODE)) {
									del_id = Integer
											.parseInt(parser.nextText());
								}
							}

							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(DELETEFROMMYLISTRESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}

			}
			Delete_From_Mylist_Update();
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	public void AddToMylist_Update() {

		if (success_id == 1) {
			id_list.add(event_id);
			util.dialogExample1(title + " "
					+ getResources().getString(R.string.added));
			button1.setText(R.string.removefav);
			button3.setBackgroundResource(R.drawable.add_to_favorite2);
		}
	}

	public void Delete_From_Mylist_Update() {
		try {
			ArrayList<Integer> new_item_list = new ArrayList<Integer>();
			for (int j = 0; j < id_list.size(); j++) {
				if (id_list.get(j) == event_id) {
				} else {
					new_item_list.add(id_list.get(j));
				}
			}
			id_list.clear();
			id_list = new_item_list;
		} catch (Exception e) {

		}
		if (del_id == 1) {
			util.dialogExample1(title + " "
					+ getResources().getString(R.string.removed));
			button1.setText(R.string.addfav);
			button3.setBackgroundResource(R.drawable.add_to_favorite);

		}

	}

	public void update3() {
		try {
			if (id_list.contains(event_id)) {
				button1.setText(getResources().getString(R.string.removefav));
				button3.setBackgroundResource(R.drawable.add_to_favorite2);
			}

		} catch (Exception e) {

		}
	}

	private class GettingEvents extends AsyncTask<String, Void, String> {

		private static final String EVENTSLIST = "EventsList";
		private static final String EVENT = "Event";
		private static final String ID = "ID";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(Details.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(url3);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null == result || result.length() == 0
					|| TextUtils.isEmpty(result)) {

				util.dialogExample();

			} else {
				try {
					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser parser = factory.newPullParser();

					parser.setInput(new StringReader(result));

					int eventType = parser.getEventType();

					while (eventType != XmlPullParser.END_DOCUMENT) {
						String name = null;
						switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(EVENTSLIST)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									id_list.add(Integer.parseInt(parser
											.nextText()));
								}
							}

							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(EVENTSLIST)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				update3();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	@Override
	public void onResume() {
		
		if(event.getUserId()>0){
		}else{
			if(pref.getInt("user_id", 0)>0){
				event.setUserId(pref.getInt("user_id", 0));
				event.setUserName(pref.getString("user_name", ""));
				event.setUser_image(pref.getString("user_image", ""));
				event.setEmail(pref.getString("email", ""));
				event.setPhone(pref.getString("phone", ""));
				event.setAddress(pref.getString("address", ""));
				event.setState_name(pref.getString("state", ""));
				event.setCity_name(pref.getString("city", ""));
				event.setState_id(pref.getInt("state_id", 0));
				event.setCity_id(pref.getInt("city_id", 0));
				
				editor.commit();
			}
		}
		user_id = event.getUserId();
		try {
			if (event.getUserId() > 0) {
				url3 = util.setMyList(event.getUserId());

				Boolean status = util.IsNetConnected(Details.this);
				if (status) {
					new GettingEvents().execute();

				} else {
					util.showAlertNoInternetService(Details.this);
				}
			}
		} catch (Exception e) {

		}
		super.onResume();

	}

	@SuppressLint("NewApi")
	private void addReminder() {
		// TODO Auto-generated method stub
		ContentResolver cr = Details.this.getContentResolver();
		Cursor cursor = cr.query(
				Uri.parse("content://com.android.calendar/events"),
				new String[] { "title" }, null, null, null);
		cursor.moveToFirst();
		ArrayList<String> check_name = new ArrayList<String>();
		String[] CalNames = new String[cursor.getCount()];
		for (int i = 0; i < CalNames.length; i++) {
			check_name.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
		if (check_name.contains(title)) {
			util.dialogExample1(title + " already added to your calendar");
		} else {

			String[] start_date = startdate.split("T");
			String[] end_date = enddate.split("T");
			String[] start_time = start_date[1].split(":");
			String[] end_time = end_date[1].split(":");
			String start = start_date[0];
			String end = end_date[0];

			int shour = Integer.parseInt(start_time[0]);
			int smin = Integer.parseInt(start_time[1]);

			int ehour = Integer.parseInt(end_time[0]);
			int emin = Integer.parseInt(end_time[1]);

			Calendar beginTime = Calendar.getInstance();
			String[] sDat = start.split("-");
			String[] eDat = end.split("-");
			int sDay = Integer.parseInt(sDat[2]);
			int sMonth = Integer.parseInt(sDat[1]);
			int sYear = Integer.parseInt(sDat[0]);

			int eDay = Integer.parseInt(eDat[2]);
			int eMonth = Integer.parseInt(eDat[1]);
			int eYear = Integer.parseInt(eDat[0]);

			Log.i("msg", "StartDay=" + sDay + "StartMonth=" + sMonth
					+ "startYear" + sYear + "and sale end      date" + eDay
					+ "Month" + eMonth + "Year=" + eYear);

			/*
			 * Cursor cursor =
			 * cr.query(Uri.parse("content://calendar/calendars"), new String[]{
			 * "_id", "displayname" }, null, null, null); cursor.moveToFirst();
			 * String[] CalNames = new String[cursor.getCount()]; int[] CalIds =
			 * new int[cursor.getCount()]; for (int i = 0; i < CalNames.length;
			 * i++) { CalIds[i] = cursor.getInt(0); CalNames[i] =
			 * cursor.getString(1); cursor.moveToNext(); } cursor.close();
			 * System.out.println("details"+CalNames+CalIds);
			 */

			beginTime.set(sYear, sMonth - 1, sDay, shour, smin);

			long startTime = beginTime.getTimeInMillis();
			Calendar endTime = Calendar.getInstance();

			endTime.set(eYear, eMonth - 1, eDay, ehour, emin);
			long end1 = endTime.getTimeInMillis();
			ContentValues calEvent = new ContentValues();
			calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
			calEvent.put(CalendarContract.Events.TITLE, title);
			calEvent.put(CalendarContract.Events.DTSTART, startTime);
			calEvent.put(CalendarContract.Events.DTEND, end1);
			calEvent.put(CalendarContract.Events.HAS_ALARM, 1);
			calEvent.put(CalendarContract.Events.EVENT_TIMEZONE,
					CalendarContract.Calendars.CALENDAR_TIME_ZONE);
			// calEvent.put(Events.EVENT_TIMEZONE, "India");
			Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, calEvent);

			int id = Integer.parseInt(uri.getLastPathSegment());

			ContentValues reminders = new ContentValues();
			reminders.put(Reminders.EVENT_ID, id);
			reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
			reminders.put(Reminders.MINUTES, 5);

			// reminders.put(CalendarContract.Events.RRULE, "FREQ=" + "DAILY" +
			// ";UNTIL=" + end1);
			// Uri uri2 = cr.insert(Reminders.CONTENT_URI, reminders);

			// Toast.makeText(getApplicationContext(),
			// "Reminder have been saved succes fully",
			// Toast.LENGTH_SHORT).show();

			util.dialogExample1(title + " added to your calendar");
		}
	}

	private class GettingTickets extends AsyncTask<String, Void, String> {

		private static final String TICKETRESPONSE = "TicketResponse";
		private static final String ID = "id";
		private static final String TITLE = "title";
		private static final String DESCRIPTION = "description";
		private static final String TICKET_TOTAL = "ticket_total";
		private static final String MINIMUM_PERSONS = "minimum_persons";
		private static final String PRICE_PER_TICKET = "price_per_ticket";
		private static final String START_DATE = "start_date";
		private static final String END_DATE = "end_date";
		private static final String TICKET_BUY_LIMIT = "ticket_buy_limit";
		private static final String TICKET_SOLD = "ticket_sold";
		private static final String ALERT_ME = "alert_me";
		private static final String EVENT_ID = "event_id";
		private static final String DISPLAY_STATUS = "display_status";
		private static final String SHOW_SOLDOUT_STATUS = "show_soldout_status";
		private static final String PROMOTION_ID = "promotion_id";
		private static final String DISCOUNT_CODE = "discount_code";
		private static final String CODE_MAX_ALLOWED = "code_max_allowed";
		private static final String P_START_DATE = "p_start_date";
		private static final String P_END_DATE = "p_end_date";
		private static final String TAX = "tax";
		private static final String DISCOUNT = "discount";
		private static final String DISCOUNT_TYPE = "discount_type";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(Details.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(url4);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null == result || result.length() == 0
					|| TextUtils.isEmpty(result)) {

				util.dialogExample();

			} else {
				try {
					tickets_details.clear();
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(TICKETRESPONSE);
					if (jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							TicketsBean ticket = new TicketsBean();
							JSONObject objJson = jsonArray.getJSONObject(i);
							ticket.id = Integer.parseInt(objJson.getString(ID));
							ticket.title = objJson.getString(TITLE);
							ticket.description = objJson.getString(DESCRIPTION);
							ticket.ticket_total = Integer.parseInt(objJson
									.getString(TICKET_TOTAL));
							ticket.minimum_persons = Integer.parseInt(objJson
									.getString(MINIMUM_PERSONS));
							ticket.price_per_ticket = Integer.parseInt(objJson
									.getString(PRICE_PER_TICKET));
							ticket.start_date = objJson.getString(START_DATE);
							ticket.end_date = objJson.getString(END_DATE);
							ticket.ticket_buy_limit = Integer.parseInt(objJson
									.getString(TICKET_BUY_LIMIT));
							ticket.ticket_sold = Integer.parseInt(objJson
									.getString(TICKET_SOLD));
							ticket.alert_me = Integer.parseInt(objJson
									.getString(ALERT_ME));
							ticket.event_id = Integer.parseInt(objJson
									.getString(EVENT_ID));
							ticket.display_status = Integer.parseInt(objJson
									.getString(DISPLAY_STATUS));
							ticket.show_soldout_status = Integer
									.parseInt(objJson
											.getString(SHOW_SOLDOUT_STATUS));

							if (!TextUtils.isEmpty(objJson.getString(TAX))
									|| objJson.getString(TAX).equalsIgnoreCase(
											"null")) {
								ticket.tax = Double.parseDouble(objJson
										.getString(TAX));
							} else {
								ticket.tax = 0.0;
							}

							JSONArray jsonArray1 = objJson
									.getJSONArray("Promotions");
							if (jsonArray1.length() > 0) {
								for (int j = 0; j < jsonArray1.length(); j++) {
									JSONObject objJson2 = jsonArray1
											.getJSONObject(j);

									int promotion_id1 = Integer
											.parseInt(objJson2
													.getString("promotion_id"));
									ticket.promotion_id.add(promotion_id1);
									ticket.discount_code.add(objJson2
											.getString(DISCOUNT_CODE));
									if (!TextUtils.isEmpty(objJson2
											.getString("code_max_allowed"))
											|| objJson2.getString(
													"code_max_allowed")
													.equalsIgnoreCase("null")) {
										ticket.code_max_allowed
												.add(Integer.parseInt(objJson2
														.getString("code_max_allowed")));
									} else {
										ticket.code_max_allowed.add(0);
									}
									ticket.p_start_date.add(objJson2
											.getString(P_START_DATE));
									ticket.p_end_date.add(objJson2
											.getString(P_END_DATE));
									ticket.discount_type.add(objJson2
											.getString(DISCOUNT_TYPE));
									ticket.discount.add(Double
											.parseDouble(objJson2
													.getString(DISCOUNT)));

								}
							}
							tickets_details.add(ticket);

						}
					}
				} catch (Exception e) {

					progressDialog.dismiss();

				}

			}
			Notify_Method();
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	public void Notify_Method() {
		if (Integer.parseInt(event_type) == 1 && tickets_details.size() == 0) {

			d = new Dialog(Details.this); //
			d.setTitle("Booking not yet Open");

			d.setContentView(R.layout.notify);
			TextView title = (TextView) d.findViewById(R.id.title);
			// TextView notify_title = (TextView)
			// d.findViewById(R.id.notify_title);
			title.setTypeface(event.getTextBold());
			// notify_title.setTypeface(event.getTextBold());
			email1 = (EditText) d.findViewById(R.id.email);
			Button submit = (Button) d.findViewById(R.id.submit);
			email1.setTypeface(event.getTextNormal());
			submit.setTypeface(event.getTextBold());

			submit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!util.validEmail(email1.getText().toString())) {
						util.dialogExample1("Please enter a valid email address");

					} else {
						url = util.setLogin();

						Boolean status = util.IsNetConnected(Details.this);
						if (status) {
							new Notify_Service().execute();

						} else {
							util.showAlertNoInternetService(Details.this);
						}
					}
				}
			});

			d.show();

		} else if (Integer.parseInt(event_type) == 1
				&& tickets_details.size() > 0) { 
			event.setTickets_details(null);
			event.setTickets_details(tickets_details);

			event.setTitle(title);
			event.setVenue(venue);
			event.setDate1(util.dateConvert(startdate, enddate));
			event.setGps(gps);
			event.setEventId(event_id);
			event.setCollectinfo(collectinfo);
			// Intent in = new Intent(Details.this,BuyTickets.class);
			/*
			 * Intent i = new Intent();"com.etisbew.eventsnow.android.Details"
			 * "com.etisbew.eventsnow.android.buyticket.BuyTickets"
			 * i.setClass(Details.this, BuyTickets.class);
			 */
			// startActivity(new
			// Intent(getApplicationContext(),BuyTickets.class));
			/*
			 * Intent i = new Intent(); i.setClass(Details.this,
			 * BuyTickets.class);//Home startActivity(i);
			 */
			/*
			 * Intent intentDeviceTest = new Intent();
			 * intentDeviceTest.setComponent(new
			 * ComponentName("com.etisbew.eventsnow.android",
			 * "com.etisbew.eventsnow.android.buyticket.BuyTickets"));
			 * startActivity(intentDeviceTest);
			 */
			event.setDetails(null);
			Intent n = new Intent(Details.this, BuyTickets.class);

			startActivity(n);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
		} else {
			util.dialogExample1("No tickets found for this event");
		}

	}

	public void contact_organizer() {
		d = new Dialog(Details.this); //
		d.setTitle("Contact Organizer");

		d.setContentView(R.layout.contact_organizer);

		final EditText email1 = (EditText) d.findViewById(R.id.email);
		final EditText fname = (EditText) d.findViewById(R.id.fname);
		final EditText lname = (EditText) d.findViewById(R.id.lname);
		final EditText phone = (EditText) d.findViewById(R.id.phone);
		final EditText message1 = (EditText) d.findViewById(R.id.message);

		Button submit = (Button) d.findViewById(R.id.submit);
		email1.setTypeface(event.getTextNormal());
		fname.setTypeface(event.getTextNormal());
		lname.setTypeface(event.getTextNormal());
		phone.setTypeface(event.getTextNormal());
		message1.setTypeface(event.getTextNormal());
		submit.setTypeface(event.getTextBold());
		fname.setText(event.getUserName());
		email1.setText(event.getEmail());
		phone.setText(event.getPhone());

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (fname.getText().toString().length() == 0
						|| lname.getText().toString().length() == 0
						|| email1.getText().toString().length() == 0
						|| phone.getText().toString().length() == 0
						|| address.getText().toString().length() == 0
						|| message1.getText().toString().length() == 0) {
					util.dialogExample1("All fields are mandatory");
				} else {

					if (!util.validEmail(email1.getText().toString())) {
						util.dialogExample1("Please enter a valid email address");

					} else {
						String address1 = message1.getText().toString().trim();
						address1 = address1.replace("\n", "%20");
						address1.replace(" ", "%20");

						nameValuePairs = new ArrayList<NameValuePair>(2);
						nameValuePairs.add(new BasicNameValuePair("firstname",
								fname.getText().toString().trim()));
						nameValuePairs.add(new BasicNameValuePair("lastname",
								lname.getText().toString().trim()));
						nameValuePairs.add(new BasicNameValuePair("email",
								email1.getText().toString().trim()));
						nameValuePairs.add(new BasicNameValuePair("phone",
								phone.getText().toString().trim()));
						nameValuePairs.add(new BasicNameValuePair("message",
								address1.replace(" ", "%20")));
						nameValuePairs.add(new BasicNameValuePair("organiser",
								email));

						Boolean status = util.IsNetConnected(Details.this);
						if (status) {
							new Contact_Us().execute();

						} else {
							util.showAlertNoInternetService(Details.this);
						}
					}
				}
			}
		});

		d.show();
	}

	private class Notify_Service extends AsyncTask<String, Void, String> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(Details.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			String notify = util.setNotifyme(event_id, email1.getText()
					.toString());
			return util.getXmlFromUrl(notify);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null == result || result.length() == 0
					|| TextUtils.isEmpty(result)) {

				util.dialogExample();

			} else {
				String status;
				String message;
				try {
					JSONObject mainJson = new JSONObject(result);
					status = mainJson.getString("status");
					message = mainJson.getString("error");
					AlertDialog.Builder altDialog = new AlertDialog.Builder(
							Details.this);
					altDialog.setMessage(message); // here add your message
					altDialog.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									d.cancel();
								}
							});
					altDialog.show();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	private class Contact_Us extends AsyncTask<String, Void, String> {
		private static final String FEEDBACKRESPONSE = "FeedbackResponse";
		private static final String RESPONSE = "Response";
		private static final String SUCCESS = "success";

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(Details.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(util.setOrganizer());
			String result = null;
			try {
				// Add your data
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, HTTP.UTF_8).trim();

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null == result || result.length() == 0
					|| TextUtils.isEmpty(result)) {

				util.dialogExample();

			} else {
				try {
					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser parser = factory.newPullParser();

					parser.setInput(new StringReader(result));

					int eventType = parser.getEventType();

					while (eventType != XmlPullParser.END_DOCUMENT) {
						String name = null;
						switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(FEEDBACKRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(SUCCESS)) {
									status = Integer
											.parseInt(parser.nextText());
								}
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(FEEDBACKRESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				Contact_Organizer_status();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	public void Contact_Organizer_status() {
		if (status == 1) {
			// util.dialogExample1("Data submitted successfully");
			AlertDialog.Builder altDialog = new AlertDialog.Builder(
					Details.this);
			altDialog
					.setMessage("Thanks for contacting us! We will get in touch with you shortly."); // here
																										// add
																										// your
																										// message
			altDialog.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							d.cancel();
						}
					});
			altDialog.show();
		} else {
			util.dialogExample1("There was a problem with your submission.");
		}
	}
}