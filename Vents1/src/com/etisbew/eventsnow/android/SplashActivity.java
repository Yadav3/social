package com.etisbew.eventsnow.android;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import com.etisbew.eventsnow.android.beans.CategoriesBean;
import com.etisbew.eventsnow.android.beans.EventsBean;
import com.etisbew.eventsnow.android.beans.LocationsBean;
import com.etisbew.eventsnow.android.beans.StatesDetails;
import com.etisbew.eventsnow.android.util.Utility;

@SuppressLint("HandlerLeak")
public class SplashActivity extends Activity {

	ProgressDialog progressDialog;
	Utility util;
	ArrayList<EventsBean> events_upcoming;
	ArrayList<Integer> id;
	ArrayList<String> title;
	ArrayList<String> startDate;
	ArrayList<String> endDate;
	ArrayList<Integer> categoryId;
	ArrayList<String> category1;
	ArrayList<String> venue;
	ArrayList<String> thumbnail;
	EventBean event;
	String url, locations_url, categories_url, states_url;
	ArrayList<Integer> location_id;
	ArrayList<String> location_name;
	ArrayList<Integer> category_id;
	ArrayList<String> category_name;
	ArrayList<LocationsBean> locations_details;
	ArrayList<CategoriesBean> categories_details;
	double latitude;
	double longitude;
	GPSTracker gps;
	ArrayList<Integer> state_id;
	ArrayList<String> state_name;
	ArrayList<Integer> country_id;
	StatesDetails states;
	boolean network_enabled;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);

		util = new Utility(SplashActivity.this);
		
		locations_details = new ArrayList<LocationsBean>();
		categories_details = new ArrayList<CategoriesBean>();
		events_upcoming = new ArrayList<EventsBean>();
		locations_url = util.setLocations();

		event = (EventBean) getApplicationContext();
		
		id = new ArrayList<Integer>();
		title = new ArrayList<String>();
		startDate = new ArrayList<String>();
		endDate = new ArrayList<String>();
		categoryId = new ArrayList<Integer>();
		category1 = new ArrayList<String>();
		venue = new ArrayList<String>();
		thumbnail = new ArrayList<String>();

		location_id = new ArrayList<Integer>();
		location_name = new ArrayList<String>();

		category_id = new ArrayList<Integer>();

		state_id = new ArrayList<Integer>();
		state_name = new ArrayList<String>();

	
		country_id = new ArrayList<Integer>();
		category_name = new ArrayList<String>();
		LocationManager locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);

		try {
			network_enabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!network_enabled) {
				final Builder dialog = new AlertDialog.Builder(SplashActivity.this);
				dialog.setMessage("Your location service is currently disabled. Eventsnow app requires location services enabled to serve you with location specific events, Click on location settings to enable the service or choose your location");
				dialog.setPositiveButton("Location settings",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface paramDialogInterface,
									int paramInt) {
								// TODO Auto-generated method stub
								finish();

								startActivity(new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS));
								

							}
						});
				dialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface paramDialogInterface,
									int paramInt) {
								// TODO Auto-generated method stub

								latitude = 17.3939;
								longitude = 78.4677;
								Boolean status = util
										.IsNetConnected(SplashActivity.this);
								if (status) {
									new Locations().execute();

								} else {
									util.showAlertNoInternetService(SplashActivity.this);
								}
							
							} 
						});
				dialog.show();
				

			} else {
				gps = new GPSTracker(getBaseContext()) {
				};
				if (gps.canGetLocation()) {
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
				}
				Boolean status = util.IsNetConnected(SplashActivity.this);
				if (status) {
					if(event.getLocationId()>0){
						categories_url = util.setCategories(event.getLocationId());
						url = util.setEvents(null, event.getLocationId());
						new Categories().execute();
					}else{
						new Locations().execute();
					}
					

				} else {
					util.showAlertNoInternetService(SplashActivity.this);
				}
			}
		} catch (Exception ex) {
		}

}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
	}
 
	private class GettingEvents extends AsyncTask<String, Void, String> {

		private static final String EVENTSLIST = "EventsList";
		private static final String EVENT = "Event";
		private static final String ID = "ID";
		private static final String UNIQUENAME = "UniqueName";
		private static final String TITLE = "Title";
		private static final String STARTDATE = "StartDate";
		private static final String ENDDATE = "EndDate";
		private static final String CATEGORYID = "CategoryID";
		private static final String CATEGORY = "Category";
		private static final String VENU = "Venu";
		private static final String THUMBNAIL = "Thumbnail";
		private static final String URL = "Url";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(SplashActivity.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(url);

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
									id.add(Integer.parseInt(parser.nextText()));
								} else if (name.equalsIgnoreCase(TITLE)) {
									title.add(parser.nextText());
								} else if (name.equalsIgnoreCase(STARTDATE)) {
									startDate.add(parser.nextText());
								} else if (name.equalsIgnoreCase(ENDDATE)) {
									endDate.add(parser.nextText());
								} else if (name.equalsIgnoreCase(CATEGORYID)) {
									categoryId.add(Integer.parseInt(parser
											.nextText()));
								} else if (name.equalsIgnoreCase(CATEGORY)) {
									category1.add(parser.nextText());
								} else if (name.equalsIgnoreCase(VENU)) {
									venue.add(parser.nextText());
								} else if (name.equalsIgnoreCase(THUMBNAIL)) {
									thumbnail.add(parser.nextText());
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
				update2();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	private class Locations extends AsyncTask<String, Void, String> {
		private static final String RESPONSE = "Response";
		private static final String CITIESRESPONSE = "CitiesResponse";
		private static final String ID = "id";
		private static final String TITLE = "title";

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(SplashActivity.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false); 
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(locations_url);

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
							if (name.equalsIgnoreCase(CITIESRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
								location_id.add(Integer.parseInt(parser
											.nextText()));
									//System.out.println("location_id"+parser.nextText());
								} else if (name.equalsIgnoreCase(TITLE)) { 
									location_name.add(parser.nextText());
									//System.out.println("location_name"+parser.nextText());
								}  
							}
 
							break; 
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(CITIESRESPONSE)) {

							}
							break;
						} 
						eventType = parser.next();
					}
	
				} catch (Exception e) {

					progressDialog.dismiss();

				}

				update();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		} 

	} 

	private class Categories extends AsyncTask<String, Void, String> {
		private static final String RESPONSE = "Response";
		private static final String CATEGORYRESPONSE = "CategoryResponse";
		private static final String ID = "id";
		private static final String TITLE = "title";

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(SplashActivity.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(categories_url);

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
							if (name.equalsIgnoreCase(CATEGORYRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									category_id.add(Integer.parseInt(parser
											.nextText()));
								} else if (name.equalsIgnoreCase(TITLE)) {
									category_name.add(parser.nextText());
								}
							}

							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(CATEGORYRESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}

				update1();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	@Override
	public void onResume() {
		super.onResume();

	}  

	public void update() { 
		int status1 = 0; 
		String city = null; 
		System.out.println("location_id"+location_id);
		Geocoder geocoder = new Geocoder(SplashActivity.this,
				Locale.getDefault());
  
		List<Address> addresses = null;
		try {
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (addresses != null && addresses.size() > 0) {
			Address address = addresses.get(0);  
			city = address.getLocality();
		}

			try {
			for (int i = 0; i < location_id.size(); i++) {
				LocationsBean location = new LocationsBean();

				location.locationId = location_id.get(i);
				location.locationName = location_name.get(i); 
                  
				if (event.getLocationId() > 0) { 
					categories_url = util.setCategories(event.getLocationId());
					url = util.setEvents(null, event.getLocationId());
					event.setCity_status(0);
				} else if (city.equalsIgnoreCase(location_name.get(i))) {
 
					event.setLocationId(location_id.get(i)); 
					categories_url = util.setCategories(location_id.get(i));
 
					url = util.setEvents(null, location_id.get(i));
					event.setCity_status(0);
				}

				locations_details.add(location);
			} 
		} catch (Exception e) {
			e.printStackTrace();
			for (int i = 0; i < location_id.size(); i++) {
				LocationsBean location = new LocationsBean();

				location.locationId = location_id.get(i);
				location.locationName = location_name.get(i); 
				locations_details.add(location);
			}
			status1 = 1;
			event.setCity_status(1); 
		}
 System.out.println("details"+locations_details);
		event.setLocations(locations_details);
		if (status1 == 1) {
			event.setLocationId(22960);
			categories_url = util.setCategories(22960);
			url = util.setEvents(null, 22960);
			new Categories().execute();
		} else {

			new Categories().execute();
		}
	}

	public void update1() {
		for (int i = 0; i < category_id.size(); i++) {
			CategoriesBean categories = new CategoriesBean();

			categories.categoryId = category_id.get(i);
			categories.categoryName = category_name.get(i);

			categories_details.add(categories);
		}
		event.setCategories(categories_details);
		new GettingEvents().execute();

	}

	public void update2() {
		for (int i = 0; i < id.size(); i++) {
			EventsBean event_ = new EventsBean();

			event_.id = id.get(i);
			event_.title = title.get(i);
			event_.startDate = startDate.get(i);
			event_.endDate = endDate.get(i);
			event_.categoryId = categoryId.get(i);
			event_.category = category1.get(i);
			event_.venue = venue.get(i);
			event_.thumbnail = thumbnail.get(i);

			events_upcoming.add(event_);
		}
		event.setObject(events_upcoming);
		Intent i = new Intent();
		i.setClass(SplashActivity.this, MainActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
		finish();

	}

}
