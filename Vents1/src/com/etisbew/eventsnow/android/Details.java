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

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
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

public class Details extends FragmentActivity implements OnClickListener,
		OnMarkerClickListener, OnMarkerDragListener {
	Button buyticket, readmore;
	int user_id;
	TextView back, event_title, event_date, event_time, event_desc, event_day,
			event_full_desc, address, contactinfo, name, phone, contact,
			website, facebook, twitter, button1, button2, button3, category1;
	EventBean event;
	int event_id, success_id, del_id;
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {
			System.out.println("gps" + gps);
			event_id = extras.getInt("event_id", 0);
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

		}
		event = (EventBean) getApplicationContext();
		util = new Utility(Details.this);
		androidAQuery = new AQuery(Details.this);
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

		/*
		 * Boolean status = util.IsNetConnected(Details.this); if (status) { new
		 * GettingEventsDetails().execute(); } else {
		 * util.showAlertNoInternetService(Details.this); }
		 * System.out.println("user_id value" + user_id);
		 */
		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		/*
		 * event_date = (TextView)home.findViewById(R.id.event_date); event_time
		 * = (TextView)home.findViewById(R.id.event_time);
		 */
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
		/*
		 * event_date.setTypeface(event.getTextBold());
		 * event_time.setTypeface(event.getTextBold());
		 */
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
		update();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
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
			startActivity(n);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;
		case R.id.buyticket:
			System.out.println("event_id" + event_id);
			url4 = util.setTickets(event_id);
			new GettingTickets().execute();
			/*
			 * if (user_id > 0) { startActivity(new Intent(Details.this,
			 * BuyTickets.class));
			 * overridePendingTransition(R.anim.trans_right_in,
			 * R.anim.trans_right_out); } else { Intent in = new
			 * Intent(Details.this, LoginActivity.class);
			 * in.putExtra("activity",
			 * "com.etisbew.android.eventsnow.buyticket.BuyTickets");
			 * startActivity(in);
			 * overridePendingTransition(R.anim.trans_right_in,
			 * R.anim.trans_right_out); }
			 */
			break;
		case R.id.zoombtn:
			/*
			 * Intent in = new Intent(Details.this, GenralMap.class);
			 * System.out.println("latlon"+lat+lon); in.putExtra("lat", lat);
			 * in.putExtra("lon", lon); startActivity(in);
			 * overridePendingTransition(R.anim.trans_right_in,
			 * R.anim.trans_right_out);
			 */

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
						new DeleteList().execute();
					} else {
						url1 = util.setAddList(event.getUserId(), event_id);
						new AddToMyList().execute();
					}
				} catch (Exception e) {

				}

			} else {
				Intent in1 = new Intent(Details.this, LoginActivity.class);
				in1.putExtra("activity",
						"com.etisbew.eventsnow.android.MainActivity");
				startActivity(in1);
				Details.this.finish();
				overridePendingTransition(R.anim.trans_left_in,
						R.anim.trans_left_out);
			}
			break;
		case R.id.adddtocalendar:
			addReminder();

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
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.setType("plain/text");
			// sendIntent.setData(Uri.parse("test@gmail.com"));
			sendIntent.setClassName("com.google.android.gm",
					"com.google.android.gm.ComposeActivityGmail");
			sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, "test");
			sendIntent.putExtra(Intent.EXTRA_TEXT, title);
			startActivity(sendIntent);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;

		case R.id.textView1:
			finish();
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
			break;
		case R.id.event_title:

			finish();
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
			break;
		case R.id.menuLayout:

			finish();
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
			break;

		}

	}

	private class GettingEventsDetails extends AsyncTask<String, Void, String> {

		private static final String EVENTDETAILRESPONSE = "EventDetailResponse";
		private static final String EVENTDETAIL = "EventDetail";
		private static final String ID = "ID";
		private static final String UNIQUENAME = "UniqueName";
		private static final String DESCRIPTION = "Description";
		private static final String TITLE = "Title";
		private static final String STARTDATE = "StartDate";
		private static final String ENDDATE = "EndDate";
		private static final String CATEGORYID = "CategoryID";
		private static final String CATEGORY = "Category";
		private static final String VENU = "Venu";
		private static final String THUMBNAIL = "Thumbnail";
		private static final String URL = "Url";
		private static final String CITY = "City";
		private static final String REGION = "Region";
		private static final String GPSLOCATION = "GPSLocation";
		private static final String CONTACTINFO = "ContactInfo";
		private static final String WEBSITE = "Website";
		private static final String EMAIL = "Email";
		private static final String PHONE = "Phone";
		private static final String MOBILE = "Mobile";
		private static final String FAX = "Fax";
		private static final String FACEBOOK = "Facebook";
		private static final String TWITTER = "Twitter";
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
					int i = 0;
					while (eventType != XmlPullParser.END_DOCUMENT) {
						String name = null;
						switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(EVENTDETAILRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									event_id = Integer.parseInt(parser
											.nextText());
								} else if (name.equalsIgnoreCase(UNIQUENAME)) {
									// uniqueName.add(parser.nextText());
								} else if (name.equalsIgnoreCase(TITLE)) {
									if (i == 0) {
										title = parser.nextText();
									}
									i++;
								} else if (name.equalsIgnoreCase(STARTDATE)) {
									startdate = parser.nextText();
								} else if (name.equalsIgnoreCase(ENDDATE)) {
									enddate = parser.nextText();
								} else if (name.equalsIgnoreCase(DESCRIPTION)) {
									description = parser.nextText();
								} else if (name.equalsIgnoreCase(CATEGORY)) {
									category = parser.nextText();
								} else if (name.equalsIgnoreCase(VENU)) {
									venue = parser.nextText();
								} else if (name.equalsIgnoreCase(THUMBNAIL)) {
									thumbnail = parser.nextText();
								} else if (name.equalsIgnoreCase(CITY)) {
									city = parser.nextText();
								} else if (name.equalsIgnoreCase(REGION)) {
									region = parser.nextText();
								} else if (name.equalsIgnoreCase(GPSLOCATION)) {
									gps = parser.nextText();
								} else if (name.equalsIgnoreCase(CONTACTINFO)) {
									cperson = parser.nextText();
								} else if (name.equalsIgnoreCase(WEBSITE)) {
									web = parser.nextText();
								} else if (name.equalsIgnoreCase(EMAIL)) {
									email = parser.nextText();
								} else if (name.equalsIgnoreCase(PHONE)) {
									phoneno = parser.nextText();
								} else if (name.equalsIgnoreCase(FACEBOOK)) {
									facebook1 = parser.nextText();
								} else if (name.equalsIgnoreCase(TWITTER)) {
									twitter1 = parser.nextText();
								} else if (name.equalsIgnoreCase(URL)) {
									// url1.add(parser.nextText());
								}
							}

							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(EVENTDETAILRESPONSE)) {

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

	private void parsingMapData() {
		try {
			googleMap.clear();
			/*
			 * if(map_flg==2) {
			 */
			if (googleMap != null) {

				// marker using custom image
				System.out.println("latlon in " + lat + lon);
				LatLng WALL_STREET = new LatLng(Double.parseDouble(lat),
						Double.parseDouble(lon));
				googleMap
						.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
								.position(WALL_STREET)
								.title("")
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

			}
			// }

			/*
			 * else { for (int i = 0; i < latitude.size(); i++) {
			 * 
			 * if (googleMap != null) {
			 * 
			 * // marker using custom image LatLng WALL_STREET = new LatLng(
			 * Double.parseDouble(latitude.get(i)),
			 * Double.parseDouble(longitude.get(i))); googleMap .addMarker(new
			 * MarkerOptions() .position(WALL_STREET) .title("")
			 * .icon(BitmapDescriptorFactory
			 * .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
			 * str_lat=latitude.get(0); str_lng=longitude.get(0); }
			 * 
			 * } }
			 */
			LatLng centerCoord = new LatLng(17.3660, 78.4760);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerCoord,
					10));

		} catch (Exception e) {
		}
	}

	private void addGoogleMap() {
		// check if we have got the googleMap already
		if (googleMap == null) {
			// android.support.v4.app.FragmentTransaction ft =
			// getFragmentManager().beginTransaction();
			// googleMap = ((SupportMapFragment)
			// getFragmentManager().findFragmentById(R.id.map)).getMap();
			/*
			 * FragmentManager myFM = getActivity().getSupportFragmentManager();
			 * 
			 * final SupportMapFragment myMAPF = (SupportMapFragment)
			 * myFM.findFragmentById(R.id.map);
			 */
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.setOnMarkerClickListener(this);
			googleMap.setOnMarkerDragListener(this);
		}

	}

	public void update() {
		System.out.println("title" + title);
		String[] gps_array = gps.split(",");
		lat = gps_array[0];
		lon = gps_array[1];
		markerPoints = new ArrayList<LatLng>();
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

			// LatLng origin = markerPoints.get(0);
			// LatLng dest = markerPoints.get(1);

			// Getting URL to the Google Directions API
			String url = getDirectionsUrl(userLocation, WALL_STREET);

			DownloadTask downloadTask = new DownloadTask();

			// Start downloading json data from Google Directions API
			downloadTask.execute(url);

			drawStartStopMarkers();
		}

		LatLng centerCoord = new LatLng(Double.parseDouble(lat),
				Double.parseDouble(lon));
		googleMap
				.moveCamera(CameraUpdateFactory.newLatLngZoom(centerCoord, 12));

		// }
		googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
			}
		});
		/*
		 * boolean flag=util.dateComparison(startdate.replace("T", " "),
		 * enddate.replace("T", " ")); System.out.println("flag"+flag);
		 * if(flag){ event_day.setText(startdate.replace("T", " ")); }else{
		 * event_day.setText(startdate.replace("T",
		 * " ")+" To "+enddate.replace("T", " ")); }
		 */
		System.out.println("two dates" + startdate + enddate);
		event_day.setText(util.dateConvert(startdate, enddate));
		event_full_desc.setText(Html.fromHtml(description));
		address.setText(venue);
		name.setText(cperson);
		phone.setText(phoneno);
		category1.setText(category);
		System.out.println("web length" + web.length());
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
			contact.setVisibility(View.VISIBLE);
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
		androidAQuery.id(image).image(thumbnail, true, true);
		try {
			if (event.getUserId() > 0) {
				url3 = util.setMyList(event.getUserId());
				new GettingEvents().execute();

			}
		} catch (Exception e) {

		}
		scr.fullScroll(ScrollView.FOCUS_UP);
		scr.smoothScrollTo(0, (int) menuLayout.getY());
		System.out.println("event_type" + event_type);
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
		// }else if(rbBiCycling.isChecked()){
		// mode = "mode=bicycling";
		// mMode = 1;
		// }else if(rbWalking.isChecked()){
		// mode = "mode=walking";
		// mMode = 2;
		// }

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
			System.out.println("result" + result);

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
									System.out.println("in if" + success_id);

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
				update1();
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
			update2();
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	public void update1() {
		System.out.println("success_id" + success_id);

		if (success_id == 1) {
			id_list.add(event_id);
			util.dialogExample1(title + " "
					+ getResources().getString(R.string.added));
			button1.setText(R.string.removefav);
			button3.setBackgroundResource(R.drawable.add_to_favorite2);
		}
	}

	public void update2() {
		try {
			ArrayList<Integer> new_item_list = new ArrayList<Integer>();
			for (int j = 0; j < id_list.size(); j++) {
				if (id_list.get(j) == event_id) {
				} else {
					new_item_list.add(id_list.get(j));
				}
			}
			System.out.println("new list" + new_item_list);
			id_list.clear();
			id_list = new_item_list;
		} catch (Exception e) {

		}
		System.out.println("delete_id" + del_id);
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
		super.onResume();

	}

	@SuppressLint("NewApi")
	private void addReminder() {
		// TODO Auto-generated method stub
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
		ContentResolver cr = Details.this.getContentResolver();

		Calendar beginTime = Calendar.getInstance();
		String[] sDat = start.split("-");
		String[] eDat = end.split("-");
		int sDay = Integer.parseInt(sDat[2]);
		int sMonth = Integer.parseInt(sDat[1]);
		int sYear = Integer.parseInt(sDat[0]);

		int eDay = Integer.parseInt(eDat[2]);
		int eMonth = Integer.parseInt(eDat[1]);
		int eYear = Integer.parseInt(eDat[0]);

		Log.i("msg", "StartDay=" + sDay + "StartMonth=" + sMonth + "startYear"
				+ sYear + "and sale end      date" + eDay + "Month" + eMonth
				+ "Year=" + eYear);
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

		// The returned Uri contains the content-retriever URI for
		// the newly-inserted event, including its id
		int id = Integer.parseInt(uri.getLastPathSegment());
		/*
		 * Toast.makeText(Details.this, "Created Calendar Event " + id,
		 * Toast.LENGTH_SHORT).show();
		 */

		// String reminderUriString =
		// "content://com.android.calendar/reminders";

		ContentValues reminders = new ContentValues();
		reminders.put(Reminders.EVENT_ID, id);
		reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		reminders.put(Reminders.MINUTES, 5);
		// reminders.put(CalendarContract.Events.RRULE, "FREQ=" + "DAILY" +
		// ";UNTIL=" + end1);
		Uri uri2 = cr.insert(Reminders.CONTENT_URI, reminders);

		// Toast.makeText(getApplicationContext(),
		// "Reminder have been saved succes fully", Toast.LENGTH_SHORT).show();
		util.dialogExample1("Reminder has been saved succesfully");
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
					System.out.println("result" + result);
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(TICKETRESPONSE);
					System.out.println("jsonArray" + jsonArray
							+ jsonArray.length());
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
							ticket.tax = Double.parseDouble(objJson
									.getString(TAX));

							/*
							 * JSONObject objJson1 = objJson
							 * .getJSONObject("Promotions");
							 */
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
									System.out.println("values"
											+ objJson2
											+ objJson2
													.getString("promotion_id")
											+ promotion_id1);
									ticket.discount_code.add(objJson2
											.getString(DISCOUNT_CODE));
									ticket.code_max_allowed
											.add(Integer.parseInt(objJson2
													.getString("code_max_allowed")));
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
							System.out.println("ticket" + ticket);
							tickets_details.add(ticket);
							System.out.println("tickets_details1"
									+ tickets_details.size() + tickets_details);
							System.out.println("objJson"
									+ objJson.getString(ID)
									+ objJson.getString(TITLE));

						}
					}
					/*
					 * XmlPullParserFactory factory = XmlPullParserFactory
					 * .newInstance(); factory.setNamespaceAware(true);
					 * XmlPullParser parser = factory.newPullParser();
					 * 
					 * parser.setInput(new StringReader(result));
					 * 
					 * int eventType = parser.getEventType();
					 * 
					 * while (eventType != XmlPullParser.END_DOCUMENT) { String
					 * name = null; switch (eventType) { case
					 * XmlPullParser.START_DOCUMENT: break; case
					 * XmlPullParser.START_TAG: name = parser.getName(); if
					 * (name.equalsIgnoreCase(TICKETRESPONSE)) { } else {
					 * 
					 * if (name.equalsIgnoreCase(ID)) {
					 * id.add(Integer.parseInt(parser.nextText())); } else if
					 * (name.equalsIgnoreCase(TITLE)) {
					 * title1.add(parser.nextText()); } else if
					 * (name.equalsIgnoreCase(DESCRIPTION)) {
					 * description1.add(parser.nextText()); } else if
					 * (name.equalsIgnoreCase(TICKET_TOTAL)) {
					 * ticket_total.add(Integer.parseInt(parser .nextText())); }
					 * else if (name .equalsIgnoreCase(MINIMUM_PERSONS)) {
					 * minimum_persons.add(Integer.parseInt(parser
					 * .nextText())); } else if (name
					 * .equalsIgnoreCase(PRICE_PER_TICKET)) {
					 * price_per_ticket.add(Integer
					 * .parseInt(parser.nextText())); } else if
					 * (name.equalsIgnoreCase(START_DATE)) {
					 * start_date.add(parser.nextText()); } else if
					 * (name.equalsIgnoreCase(END_DATE)) {
					 * end_date.add(parser.nextText()); } else if (name
					 * .equalsIgnoreCase(TICKET_BUY_LIMIT)) {
					 * ticket_buy_limit.add(Integer
					 * .parseInt(parser.nextText())); } else if
					 * (name.equalsIgnoreCase(TICKET_SOLD)) {
					 * ticket_sold.add(Integer.parseInt(parser .nextText())); }
					 * else if (name.equalsIgnoreCase(ALERT_ME)) {
					 * alert_me.add(Integer.parseInt(parser .nextText())); }
					 * else if (name.equalsIgnoreCase(EVENT_ID)) {
					 * event_id1.add(Integer.parseInt(parser .nextText())); }
					 * else if (name .equalsIgnoreCase(DISPLAY_STATUS)) {
					 * display_status.add(Integer.parseInt(parser .nextText()));
					 * } else if (name .equalsIgnoreCase(SHOW_SOLDOUT_STATUS)) {
					 * show_soldout_status.add(Integer
					 * .parseInt(parser.nextText())); } else if
					 * (name.equalsIgnoreCase(PROMOTION_ID)) {
					 * promotion_id.add(Integer.parseInt(parser .nextText()));
					 * }else if (name.equalsIgnoreCase(DISCOUNT_CODE)) {
					 * discount_code.add(parser .nextText()); }else if
					 * (name.equalsIgnoreCase(CODE_MAX_ALLOWED)) {
					 * code_max_allowed.add(Integer.parseInt(parser
					 * .nextText())); }else if
					 * (name.equalsIgnoreCase(P_START_DATE)) {
					 * p_start_date.add(parser .nextText()); }else if
					 * (name.equalsIgnoreCase(P_END_DATE)) {
					 * p_end_date.add(parser .nextText()); }else if
					 * (name.equalsIgnoreCase(TAX)) {
					 * tax.add(Double.parseDouble(parser .nextText())); }else if
					 * (name.equalsIgnoreCase(DISCOUNT)) {
					 * discount.add(Double.parseDouble(parser .nextText())); } }
					 * 
					 * break; case XmlPullParser.END_TAG: name =
					 * parser.getName(); if
					 * (name.equalsIgnoreCase(TICKETRESPONSE)) {
					 * 
					 * } break; } eventType = parser.next(); }
					 */
				} catch (Exception e) {

					progressDialog.dismiss();

				}
				update4();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	public void update4() {
		System.out.println("titles" + title1 + id);
		System.out.println("titles" + tickets_details);
		/*
		 * for (int i = 0; i < id.size(); i++) { TicketsBean ticket = new
		 * TicketsBean();
		 * 
		 * ticket.id = id.get(i); ticket.title = title1.get(i);
		 * ticket.description = description1.get(i); ticket.ticket_total =
		 * ticket_total.get(i); ticket.minimum_persons = minimum_persons.get(i);
		 * ticket.price_per_ticket = price_per_ticket.get(i); ticket.start_date
		 * = start_date.get(i); ticket.end_date = end_date.get(i);
		 * ticket.ticket_buy_limit = ticket_buy_limit.get(i); ticket.ticket_sold
		 * = ticket_sold.get(i); ticket.alert_me = alert_me.get(i);
		 * ticket.event_id = event_id1.get(i); ticket.display_status =
		 * display_status.get(i); ticket.show_soldout_status =
		 * show_soldout_status.get(i); ticket.tax = tax.get(i);
		 * ticket.promotion_id=promotion_id.get(i);
		 * ticket.discount_code=discount_code.get(i);
		 * ticket.code_max_allowed=code_max_allowed.get(i);
		 * ticket.p_start_date=p_start_date.get(i);
		 * ticket.p_end_date=p_end_date.get(i); ticket.discount=discount.get(i);
		 * 
		 * tickets_details.add(ticket); }
		 */
		System.out.println("tickets_details" + tickets_details.size()
				+ tickets_details);
		if (Integer.parseInt(event_type) == 1 && tickets_details.size() == 0) {

			final Dialog d = new Dialog(Details.this); //
			d.setTitle("Booking not yet Open");
		 
			 
			d.setContentView(R.layout.notify);
			TextView title = (TextView) d.findViewById(R.id.title);
			//TextView notify_title = (TextView) d.findViewById(R.id.notify_title);
			title.setTypeface(event.getTextBold());
			//notify_title.setTypeface(event.getTextBold()); 
			final EditText email = (EditText) d.findViewById(R.id.email);
			Button submit = (Button) d.findViewById(R.id.submit);
			email.setTypeface(event.getTextNormal());
			submit.setTypeface(event.getTextBold());
			
			submit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!util.validEmail(email.getText().toString())) {
						util.dialogExample1("Please Enter valid Email");

					} else {
						url = util.setLogin();
							new Notify_Service().execute();
					}
				}
			});
		/*	final NumberPicker np = (NumberPicker) d 
					.findViewById(R.id.numberPicker1);
			np.setMaxValue(100);
			np.setMinValue(0);
			np.setValue(in);
			np.setSelected(true);
			np.setWrapSelectorWheel(true);
			np.setOnClickListener(this);
			b1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					d.dismiss();
					update(price, add, np.getValue());
				}
			});
			b2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					d.dismiss();
				}
			});*/
			d.show();

		} else {
			event.setTickets_details(tickets_details);

			event.setTitle(title);
			event.setVenue(venue);
			event.setDate1(util.dateConvert(startdate, enddate));
			event.setGps(gps);
			event.setEventId(event_id);
			startActivity(new Intent(Details.this, BuyTickets.class));
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
		}
		/*
		 * if (user_id > 0) { startActivity(new Intent(Details.this,
		 * BuyTickets.class)); overridePendingTransition(R.anim.trans_left_in,
		 * R.anim.trans_left_out); } else { Intent in = new Intent(Details.this,
		 * LoginActivity.class); in.putExtra("activity",
		 * "com.etisbew.eventsnow.android.buyticket.BuyTickets");
		 * startActivity(in); overridePendingTransition(R.anim.trans_left_in,
		 * R.anim.trans_left_out); }
		 */
	}
	private class Notify_Service extends AsyncTask<String, Void, String> {
		private static final String RESULT = "Result";
		private static final String FBRESPONSE = "FbResponse";
		private static final String ID = "ID";
		private static final String USERNAME = "username";
		private static final String PROFILEPIC = "Profilepic";
		private static final String EMAIL = "Email";
		private static final String PHONE = "Phone";
		private static final String ERRORMESSAGE = "ErrorMessage";
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
		//	fb_url=util.setFbEmail(fb_access_token, fb_user_name, fb_email);
		return util.getXmlFromUrl("");

		}  

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("result1" + result);
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
							if (name.equalsIgnoreCase(FBRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									user_id = Integer.parseInt(parser.nextText());
								} else if (name.equalsIgnoreCase(USERNAME)) {
								//	user_name = parser.nextText();
								} else if (name.equalsIgnoreCase(PROFILEPIC)) {
								//	user_image = parser.nextText();
								}else if (name.equalsIgnoreCase(EMAIL)) {
								//	email1 = parser.nextText();
								}else if (name.equalsIgnoreCase(PHONE)) {
								//	phone = parser.nextText();
								}else if (name.equalsIgnoreCase(ERRORMESSAGE)) {
								//	error_message = parser.nextText();
								}
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(FBRESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				notify_update();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}
	public void notify_update() {
		

	}

}