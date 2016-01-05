package com.etisbew.eatz.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.etisbew.eatz.android.Explore.getSessionsSlotsTask;
import com.etisbew.eatz.android.orderfood.OrderFood;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.RestaurentDO;
import com.etisbew.eatz.objects.reviewDO;
import com.etisbew.eatz.web.WebServiceCalls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class SearchDetails extends BaseActivity implements OnClickListener,
		OnMarkerClickListener, OnMarkerDragListener {

	private ImageView ivback = null;
	private LinearLayout llReviews = null;
	private LayoutInflater inflator = null;
	static RestaurentDO restDetails = null;
	private TextView tvHappyHours = null, cusineText = null;
	private LinearLayout llFav = null;
	private ScrollView svMainView = null;
	private String favorites_cnt = "0";
//	private String wanttry_cnt = "0";
	private GoogleMap googleMap;
	ArrayList<LatLng> markerPoints;
	int mMode = 0;
	final int MODE_DRIVING = 0;
	final int MODE_BICYCLING = 1;
	final int MODE_WALKING = 2;
	private TextView cusineText_dummy, tvTimings_dummy, tvSundayTimings_dummy,
			tvNextSundayTimings_dummy, tvexisting_buffets_dummy, tvCost_dummy,
			tvHappyHours_dummy, tvDrinks_dummy, tvFacilities_dummy,
			tvOverview_dummy, tv_highlights_dummy;

	private TextView tVBeenThere, tvuser_favorites, tVWriteReview, tViewMap,
			tVRecommended, tvEmail, tvSendToFrnd, tvSendToMobile, tvWriteBottm,
			tvViewAll;
	Button btnOrderFood, btnBookTable, btnviewdeals;
	String url = "", url1 = "";
	String total_been = "";
	HttpResponse response;
	static String str_happy_hours = "", str_happy_hours_start = "",
			str_happy_hours_end = "", str_serving_timings = "",
			str_facilities = "", str_drinks_names = "", str_CardsAccepted = "",
			str_FineDining = "", str_is_veg = "", str_is_delivery = "",
			str_is_takeaway = "", str_liqour_served = "",
			str_reviews_count = "", str_costpertwo = "", str_OverView = "",
			str_favorites_cnt = "", str_cuisines = "", str_beenthere_cnt = "",
			str_wantTry_cnt = "", str_user_favorites = "",
			str_user_total_been = "", str_restaurant_name = "", str_want = "";

	String str_id = "";
	String str_address = "";
	String str_phon = "";
	String str_sunday_bunch = "";
	String str_next_sunday_bunch = "";
	String str_lat = "";
	String str_lng = "";
	String str_existing_buffets = "";
	String str_brunch_timings = "";
	String str_Location = "";
	String str_has_deals = "";
	String str_booktables = "";
	String str_orderfood = "";
	String str_overall_rating = "";

	TextView tvRestrentTitle, tvAddressLine1, tvPhone, tvDeals;
	String result;
	ArrayList<reviewDO> reviews = new ArrayList<reviewDO>();
	GPSTracker gps;
	Double lat,lang;
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_details);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {
			result = extras.getString("result");
		}
		tv_highlights_dummy = (TextView) findViewById(R.id.tv_highlights_dummy);
		tv_highlights_dummy.setText("Highlights");

		cusineText_dummy = (TextView) findViewById(R.id.cusineText_dummy);
		cusineText_dummy.setText("Cuisines");

		tvTimings_dummy = (TextView) findViewById(R.id.tvTimings_dummy);
		tvTimings_dummy.setText("Timings");

		tvSundayTimings_dummy = (TextView) findViewById(R.id.tvSundayTimings_dummy);
		tvSundayTimings_dummy.setText("Sunday Brunch");

		tvNextSundayTimings_dummy = (TextView) findViewById(R.id.tvNextSundayTimings_dummy);
		tvNextSundayTimings_dummy.setText("Next Sunday Brunch");

		tvexisting_buffets_dummy = (TextView) findViewById(R.id.tvexisting_buffets_dummy);
		tvexisting_buffets_dummy.setText("Buffets Timings");

		tvCost_dummy = (TextView) findViewById(R.id.tvCost_dummy);
		tvCost_dummy.setText("Cost");

		tvHappyHours_dummy = (TextView) findViewById(R.id.tvHappyHours_dummy);
		tvHappyHours_dummy.setText("Happy Hours");

		tvDrinks_dummy = (TextView) findViewById(R.id.tvDrinks_dummy);
		tvDrinks_dummy.setText("Drinks");

		tvFacilities_dummy = (TextView) findViewById(R.id.tvFacilities_dummy);
		tvFacilities_dummy.setText("Facilities");

		tvOverview_dummy = (TextView) findViewById(R.id.tvOverview_dummy);
		tvOverview_dummy.setText("Overview");

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(tp);
		}
		hideKeyboard();

		inflator = getLayoutInflater();

		svMainView = (ScrollView) findViewById(R.id.sVMainView);
		llFav = (LinearLayout) findViewById(R.id.llFav);

		svMainView.setTag("true");

		llFav.setTag("false");
		llFav.setVisibility(View.GONE);

		btnOrderFood = (Button) findViewById(R.id.btnOrderFood);
		btnOrderFood.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// startActivity(new Intent(getApplicationContext(),
				// OrderFood.class));
				if (ConnectivityReceiver
						.checkInternetConnection(SearchDetails.this)) {

					new getOrderCategoriesTask().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(SearchDetails.this);
				}

			}
		});
		btnBookTable = (Button) findViewById(R.id.btnBookTable);
		btnBookTable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String strVenueId = Explore.strVenueId;// restDetails.id
				Explore.strRestaurantTitle = str_restaurant_name;// restDetails.name
				Explore.strRestaurantAddress = str_address;
				if (str_overall_rating.equalsIgnoreCase("null")) {
					str_overall_rating = "0";
				}
				Explore.strRestaurantRating = str_overall_rating;// restDetails.review_rating

				Calendar c = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Explore.strBookDate = sdf.format(c.getTime());

				Explore.strBookDateMsg = "";
				Explore.strBookDateMsg = "";

				// new getSessionsSlotsTask(SearchDetails.this, strVenueId)
				// .execute();
				if (ConnectivityReceiver
						.checkInternetConnection(SearchDetails.this)) {

					new getSessionsSlotsTask(SearchDetails.this, strVenueId)
							.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(SearchDetails.this);
				}
			}
		});

		btnviewdeals = (Button) findViewById(R.id.btnviewdeals);
		btnviewdeals.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "Deals";
				Launcher.userLocation = Appconstants.ltDo.name;
				final String dealsUrl = Appconstants.MAIN_HOST
						+ "restaurantDeals/" + Explore.strVenueId;
				showLoader();
				new Thread(new Runnable() {

					@Override
					public void run() {

						final String result = new WebServiceCalls()
								.urlPost(dealsUrl);

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								hideLoader();

								if (result != null) {
									Intent inDeals = new Intent(
											SearchDetails.this, Deals.class)
											.putExtra("result", result);
									startActivity(inDeals);
									// hideLoader();
								}

							}
						});

					}
				}).start();
			}
		});
		
		gps = new GPSTracker(SearchDetails.this) {
		};
		if (gps.canGetLocation()) { 
				lat = gps.getLatitude();
				lang = gps.getLongitude();
		}else{
			lat = Double.parseDouble(Appconstants.LANGITUDE);
			lang = Double.parseDouble(Appconstants.LANGITUDE);
		}

		tvRestrentTitle = (TextView) findViewById(R.id.txtDetailVenueName);
		tvAddressLine1 = (TextView) findViewById(R.id.tvAddressLine1);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		tvDeals = (TextView) findViewById(R.id.tvDeals);

		ivback = (ImageView) findViewById(R.id.back);
		ivback.setOnClickListener(this);

		llReviews = (LinearLayout) findViewById(R.id.llReviews);

		LinearLayout llCall = (LinearLayout) findViewById(R.id.llCall);
		llCall.setOnClickListener(this);

		LinearLayout llWriteReview = (LinearLayout) findViewById(R.id.writeReview);
		llWriteReview.setOnClickListener(this);

		TextView etFind = (TextView) findViewById(R.id.etFind);
		etFind.setOnClickListener(this);

		RelativeLayout rlOptions = (RelativeLayout) findViewById(R.id.options);
		rlOptions.setOnClickListener(this);

		tVRecommended = (TextView) llFav.findViewById(R.id.tVRecommended);
		tVRecommended.setOnClickListener(this);

		tVBeenThere = (TextView) llFav.findViewById(R.id.tVBeenThere);
		tVBeenThere.setOnClickListener(this);

		tvuser_favorites = (TextView) llFav.findViewById(R.id.tVAddfav);
		tvuser_favorites.setOnClickListener(this);

		tVWriteReview = (TextView) llFav.findViewById(R.id.tVWriteReview);
		tVWriteReview.setOnClickListener(this);

		tViewMap = (TextView) llFav.findViewById(R.id.tViewMap);
		tViewMap.setOnClickListener(this);

		tvEmail = (TextView) llFav.findViewById(R.id.tvEmail);
		tvEmail.setOnClickListener(this);

		tvSendToFrnd = (TextView) llFav.findViewById(R.id.tvSendTo);
		tvSendToFrnd.setOnClickListener(this);

		tvSendToMobile = (TextView) llFav.findViewById(R.id.tvSendToMobile);
		tvSendToMobile.setOnClickListener(this);

		tvWriteBottm = (TextView) findViewById(R.id.tvWriteReviewBottm);
		tvWriteBottm.setOnClickListener(this);

		tvViewAll = (TextView) findViewById(R.id.tvViewAll);
		tvViewAll.setOnClickListener(this);

		tvDeals.setOnClickListener(this);

		// ////////////////////////////

		setDetails();
	}

	static RestaurentDO restDO;

	public void Setting_Event_Details() {
		/*
		 * String[] gps_array = gps.split(","); lat = gps_array[0]; lon =
		 * gps_array[1];
		 */
		markerPoints = new ArrayList<LatLng>();
		// 19th commented
		addGoogleMap();
		parsingMapData();

		LatLng WALL_STREET = new LatLng(Double.parseDouble(str_lat),
				Double.parseDouble(str_lng));

		LatLng userLocation = new LatLng(lat,lang);
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

//		LatLng centerCoord = new LatLng(Double.parseDouble(str_lat),
//				Double.parseDouble(str_lng));

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
				options.icon(
						BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
						.title("Current Location");
			} else if (i == 1) {
				options.icon(
						BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_RED))
						.title(str_restaurant_name);
			}

			// Add new marker to the Google Map Android API V2
			googleMap.addMarker(options);
			LatLng centerCoord = new LatLng(
					Double.parseDouble(Appconstants.LATTITUDE),
					Double.parseDouble(Appconstants.LANGITUDE));
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerCoord,
					13));
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

	private void addGoogleMap() {
		// check if we have got the googleMap already
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			/*
			 * googleMap = ((MapFragment) getFragmentManager().findFragmentById(
			 * R.id.map)).getMap();
			 */
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.setOnMarkerClickListener(this);
			googleMap.setOnMarkerDragListener(this);
		}

	}

	private void parsingMapData() {
		try {
			googleMap.clear();
			if (googleMap != null) {

				// marker using custom image
				LatLng WALL_STREET = new LatLng(Double.parseDouble(str_lat),
						Double.parseDouble(str_lng));
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
//			MarkerOptions markerOptions = new MarkerOptions();

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

	@Override
	public void onBackPressed() {

		if (svMainView.getTag().toString().contains("false")) {
			svMainView.setVisibility(View.VISIBLE);
			svMainView.setTag("true");
			llFav.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}
	}

	public void optionVisible() {
		if (svMainView.getTag().toString().contains("true")) {
			svMainView.setVisibility(View.GONE);
			svMainView.setTag("false");
			llFav.setVisibility(View.VISIBLE);

		} else {
			svMainView.setVisibility(View.VISIBLE);
			svMainView.setTag("true");
			llFav.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(str_user_favorites)) {// user_favorites
			if (Integer.parseInt(str_user_favorites) == 0) {
				tvuser_favorites.setText("Add to\n Favorites" + "("
						+ str_favorites_cnt + ")");
			} else {
				tvuser_favorites.setText("In \nFavorites" + "("
						+ str_favorites_cnt + ")");
			}
		}

		if (!TextUtils.isEmpty(str_wantTry_cnt)) {// user_favorites
			if (Integer.parseInt(str_wantTry_cnt) == 0) {
				tVRecommended.setText("Want to try" + "\n(" + str_want + ")");
			} else {
				tVRecommended.setText("Want to try" + "\n(" + str_want + ")");
			}
		}

	}

	class WantTryTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(SearchDetails.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "venueWanttotry/" + PreferenceUtils.getUserSession()
					+ "/" + Explore.strVenueId);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg("No data found from web!!!");
			} else {

				if (result.contains("errorMessage")) {
					try {
						String strErrMsg = new JSONObject(result)
								.optString("errorMessage");
						showDialogMsg(strErrMsg);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {

					try {
						total_been = new JSONObject(result)
								.optString("total_try");
						str_want = "" + (Integer.parseInt(total_been) + 0);
						tVRecommended.setText("Want to try" + "\n(" + str_want
								+ ")");
						BaseActivity.showDialogMsg(SearchDetails.this, "Total Try "
								+ total_been);
						if (total_been.equalsIgnoreCase("1")) {
							tVBeenThere.setText("Been  There \n(0)");
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}
	}

	class WantBeenTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(SearchDetails.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "venueBeenthere/" + PreferenceUtils.getUserSession()
					+ "/" + Explore.strVenueId);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg("No data found from web!!!");
			} else {

				if (result.contains("errorMessage")) {
					try {
						String strErrMsg = new JSONObject(result)
								.optString("errorMessage");
						showDialogMsg(strErrMsg);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {

					try {
						String total_been = new JSONObject(result)
								.optString("total_been");
						// String user_beenthere = new JSONObject(result)
						// .optString("user_beenthere");
						// Login.showDialogMsg(SearchDetails.this,
						// user_beenthere);
						// String user_wanttry = new JSONObject(result)
						// .optString("user_wanttry");
						// Login.showDialogMsg(SearchDetails.this,
						// user_wanttry);
						str_beenthere_cnt = total_been;
						BaseActivity.showDialogMsg(SearchDetails.this, "Total Been "
								+ total_been);
						tVBeenThere.setText("Been  There \n("
								+ str_beenthere_cnt + ")");

						if (total_been.equalsIgnoreCase("1")) {
							tVRecommended.setText("Want to try" + "\n(0)");
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tVBeenThere:

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				startActivity(new Intent(v.getContext(), Login.class));
				return;
			}

			if (!TextUtils.isEmpty(str_user_total_been)) {// user_total_been
				if (Integer.parseInt(str_user_total_been) == 0) {
					if (ConnectivityReceiver
							.checkInternetConnection(SearchDetails.this)) {

						new WantBeenTask().execute();

					} else {
						ConnectivityReceiver
								.showCustomDialog(SearchDetails.this);
					}
					// new WantBeenTask().execute();
				}
			}

			break;

		case R.id.tVRecommended:

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				startActivity(new Intent(v.getContext(), Login.class));
				return;
			}

			if (!TextUtils.isEmpty(str_wantTry_cnt)) {// user_total_been
				if (Integer.parseInt(str_user_total_been) == 0) {
					// new WantTryTask().execute();
					if (ConnectivityReceiver
							.checkInternetConnection(SearchDetails.this)) {

						new WantTryTask().execute();

					} else {
						ConnectivityReceiver
								.showCustomDialog(SearchDetails.this);
					}
				}
			}

			break;

		case R.id.etFind:

			setResult(1016);
			finish();

			break;

		case R.id.llCall:

			/*
			 * try { Intent callIntent = new Intent(Intent.ACTION_CALL,
			 * Uri.parse("tel:" + str_phon));// restDetails.phone
			 * startActivity(callIntent); } catch (ActivityNotFoundException e)
			 * { }
			 */
			try {
				if (str_phon.contains(",")) {
					String s[] = str_phon.split(",");

					// Intent callIntent = new
					// Intent(Intent.ACTION_CALL,
					// Uri.parse("tel:" + s[0]+""
					// + PhoneNumberUtils.PAUSE + "#"
					// + s[1]));

					Intent callIntent = new Intent(Intent.ACTION_CALL);

					callIntent.setData(Uri.parse("tel:" + s[0] + ""
							+ PhoneNumberUtils.PAUSE + "P" + s[1] + ";"));
					startActivity(callIntent);
				} else {

					Intent callIntent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:" + str_phon));
					startActivity(callIntent);
				}

			} catch (ActivityNotFoundException e) {
			}
			break;

		case R.id.back:

			finish();

			break;

		case R.id.options:

			optionVisible();

			break;

		case R.id.tVAddfav:

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				startActivity(new Intent(v.getContext(), Login.class));
				return;
			}

			if (!TextUtils.isEmpty(str_user_favorites)) {// user_favorites
				if (Integer.parseInt(str_user_favorites) == 0) {
					strFavActionMsg = "add";
				} else {
					strFavActionMsg = "Remove";
				}
			}
			// new AddFavouriteTask().execute();
			if (ConnectivityReceiver
					.checkInternetConnection(SearchDetails.this)) {

				new AddFavouriteTask().execute();

			} else {
				ConnectivityReceiver.showCustomDialog(SearchDetails.this);
			}

			break;

		case R.id.tvWriteReviewBottm:

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				if (Appconstants.user_flag == 2) {
					getWriteReview(SearchDetails.this);
				} else {
					startActivity(new Intent(v.getContext(), Login.class));
				}
			} else {
				getWriteReview(SearchDetails.this);
			}

			// getWriteReview(SearchDetails.this);

			break;

		case R.id.writeReview:

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				if (Appconstants.user_flag == 2) {
					getWriteReview(SearchDetails.this);
				} else {
					startActivity(new Intent(v.getContext(), Login.class));
				}
			} else {
				getWriteReview(SearchDetails.this);
			}

			break;

		case R.id.tViewMap:

			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?saddr="
							+ Appconstants.LATTITUDE + ","
							+ Appconstants.LANGITUDE + "&daddr=" + str_lat
							+ "," + str_lng));// restDetails.lat,
												// restDetails.lang
			startActivity(intent);

			break;

		case R.id.tvEmail:

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				startActivity(new Intent(v.getContext(), Login.class));
				return;
			}

			sendEmailVenue(SearchDetails.this);

			break;

		case R.id.tvSendTo:

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				startActivity(new Intent(v.getContext(), Login.class));
				return;
			}

			SendToFriend(SearchDetails.this);

			break;

		case R.id.tvSendToMobile:

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				startActivity(new Intent(v.getContext(), Login.class));
				return;
			}
			sendMobile(SearchDetails.this);

			break;

		case R.id.tvViewAll:
			restDO = restDetails;

			startActivity(new Intent(v.getContext(), RestuarentReviews.class));

			break;
		case R.id.tVWriteReview:

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				if (Appconstants.user_flag == 2) {
					getWriteReview(SearchDetails.this);
				} else {
					startActivity(new Intent(v.getContext(), Login.class));
				}
			} else {
				getWriteReview(SearchDetails.this);
			}

			break;
		default:
			break;
		}

	}

	String strFavActionMsg;

	class AddFavouriteTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(SearchDetails.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

//			String url = Appconstants.MAIN_HOST + "venueFavorites/"
//					+ PreferenceUtils.getUserSession() + "/"
//					+ Explore.strVenueId + "/" + strFavActionMsg;
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "venueFavorites/" + PreferenceUtils.getUserSession()
					+ "/" + Explore.strVenueId + "/" + strFavActionMsg);// restDetails.id

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg("No data found from web!!!");

			} else {

				try {
					String strErrMsg = new JSONObject(result)
							.optString("errorMessage");

					favorites_cnt = "" + (Integer.parseInt(favorites_cnt) + 1);

					/*
					 * Login.showDialogMsg(SearchDetails.this, strErrMsg);
					 * 
					 * // optionVisible();
					 */
					str_favorites_cnt = ""
							+ (Integer.parseInt(str_favorites_cnt) + 1);
					// int
					// count=Integer.parseInt(str_favorites_cnt)+Integer.parseInt(favorites_cnt);
					tvuser_favorites.setText("Add to\n Favorites" + "("
							+ str_favorites_cnt + ")");
					// Login.showDialogMsg(SearchDetails.this, strErrMsg);
					AlertDialog.Builder altDialog = new AlertDialog.Builder(
							SearchDetails.this);
					altDialog.setMessage(strErrMsg); // here add your message
					altDialog.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									llFav.setVisibility(View.GONE);
									svMainView.setVisibility(View.VISIBLE);
								}
							});
					altDialog.show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	static Dialog dialog;
	static EditText edtWRTitle, edtReviewComments, edtSendMobile;
	static EditText edtReceiverName;
	static EditText edtReceiverEmail;
	static EditText edtMsg;
	static TextView txtMsgCount;
	static String strServRating, strFoodRating, strAtmosRating, strValueRating,
			strOverAllRating, strVenueName, strDeliveryLocationId;

	public static void getWriteReview(final Context con) {
		// TODO Auto-generated method stub

		dialog = new Dialog(con);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.writereview);

		TextView txtBeTheFirstReview = (TextView) dialog
				.findViewById(R.id.txtBeTheFirstReview);

		TextView btnClose1 = (TextView) dialog.findViewById(R.id.btnClose1);

		btnClose1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		// txtBeTheFirstReview.setTypeface(Localsecrets.Titillium_Semibold);
		txtBeTheFirstReview.setText("" + str_restaurant_name + ","
				+ Appconstants.ltDo.name);// restDetails.name

		final TextView txtLimitMsg = (TextView) dialog
				.findViewById(R.id.txtLimitMsg);
		// txtLimitMsg.setTypeface(Localsecrets.Titillium_Regular);

		txtMsgCount = (TextView) dialog.findViewById(R.id.txtMsgCount);
		// txtMsgCount.setTypeface(Localsecrets.Titillium_Regular);

		edtWRTitle = (EditText) dialog.findViewById(R.id.edtWRTitle);
		// edtWRTitle.setTypeface(Localsecrets.Titillium_Semibold);

		edtReviewComments = (EditText) dialog
				.findViewById(R.id.edtReviewComments);
		// edtReviewComments.setTypeface(Localsecrets.Titillium_Semibold);
		edtReviewComments.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() == 0) {
					txtLimitMsg
							.setText("Write a review of atleast 75 characters");
				} else
					txtLimitMsg.setText(s.length() + " Characters)");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		RatingBar rbService = (RatingBar) dialog.findViewById(R.id.RBService);
		LayerDrawable stars = (LayerDrawable) rbService.getProgressDrawable();
		stars.getDrawable(2)
				.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		rbService.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {

				strServRating = String.valueOf(rating);

			}
		});
		RatingBar rbFood = (RatingBar) dialog.findViewById(R.id.RBFood);
		LayerDrawable stars1 = (LayerDrawable) rbFood.getProgressDrawable();
		stars1.getDrawable(2).setColorFilter(Color.RED,
				PorterDuff.Mode.SRC_ATOP);
		rbFood.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strFoodRating = String.valueOf(rating);

			}
		});
		RatingBar rbAtmos = (RatingBar) dialog.findViewById(R.id.RBAtmos);
		LayerDrawable stars11 = (LayerDrawable) rbAtmos.getProgressDrawable();
		stars11.getDrawable(2).setColorFilter(Color.RED,
				PorterDuff.Mode.SRC_ATOP);
		rbAtmos.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strAtmosRating = String.valueOf(rating);

			}
		});
		RatingBar rbValue = (RatingBar) dialog.findViewById(R.id.RBValue);
		LayerDrawable stars3 = (LayerDrawable) rbValue.getProgressDrawable();
		stars3.getDrawable(2).setColorFilter(Color.RED,
				PorterDuff.Mode.SRC_ATOP);
		rbValue.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strValueRating = String.valueOf(rating);

			}
		});

		RatingBar rbOverAll = (RatingBar) dialog.findViewById(R.id.RBOverAll);
		LayerDrawable stars33 = (LayerDrawable) rbOverAll.getProgressDrawable();
		stars33.getDrawable(2).setColorFilter(Color.RED,
				PorterDuff.Mode.SRC_ATOP);
		rbOverAll.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strOverAllRating = String.valueOf(rating);

			}
		});

		Button btnWriteReviewSubmit = (Button) dialog
				.findViewById(R.id.btnWriteReviewSubmit);

		btnWriteReviewSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(edtWRTitle.getText().toString())) {
					BaseActivity.showDialogMsg(con, "Please enter review title");
				} else if (TextUtils.isEmpty(edtReviewComments.getText()
						.toString())) {
					BaseActivity.showDialogMsg(con, "Please enter review comments");
				} else if (TextUtils.isEmpty(strServRating)) {
					BaseActivity.showDialogMsg(con,
							"Please enter service rating for the venue");
				} else if (TextUtils.isEmpty(strFoodRating)) {
					BaseActivity.showDialogMsg(con,
							"Please enter food rating for the venue");
				} else if (TextUtils.isEmpty(strAtmosRating)) {
					BaseActivity.showDialogMsg(con,
							"Please enter atmos rating for the venue");
				} else if (TextUtils.isEmpty(strValueRating)) {
					BaseActivity.showDialogMsg(con,
							"Please enter value rating for the venue");
				} else if (TextUtils.isEmpty(strOverAllRating)) {
					BaseActivity.showDialogMsg(con,
							"Please enter overall rating for the venue");
				} else if (!TextUtils.isEmpty(strServRating)
						&& !TextUtils.isEmpty(strFoodRating)
						&& !TextUtils.isEmpty(strAtmosRating)
						&& !TextUtils.isEmpty(strValueRating)
						&& !TextUtils.isEmpty(strOverAllRating)
						&& !TextUtils.isEmpty(edtWRTitle.getText().toString())
						&& !TextUtils.isEmpty(edtReviewComments.getText()
								.toString())) {
					if (ConnectivityReceiver.checkInternetConnection(con)) {
						new WriteReviewTask(con).execute();
					} else {
						ConnectivityReceiver.showCustomDialog(con);
					}

				}

			}
		});

		dialog.show();
	}

	static class WriteReviewTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		Context context;

		public WriteReviewTask(Context con) {
			// TODO Auto-generated constructor stub
			context = con;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Submitting your review. Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			String strGuestName = null;
			String strGuestEmail = null, usersession = null;

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				if (Appconstants.user_flag == 2) {
					usersession = Appconstants.sessionId;
				} else {
					context.startActivity(new Intent(context, Login.class));
				}

			} else {
				usersession = PreferenceUtils.getUserSession();
			}

			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "writeaReview/"
					+ usersession
					+ "/"
					+ strGuestName
					+ "/"
					+ strGuestEmail
					+ "/"
					+ Explore.strVenueId
					+ "/"
					+ edtWRTitle.getText().toString().replace(" ", "%20")
							.trim()
					+ "/"
					+ edtReviewComments.getText().toString()
							.replace(" ", "%20").trim() + "/"
					+ strOverAllRating + "/" + strServRating + "/"
					+ strValueRating + "/" + strAtmosRating + "/"
					+ strFoodRating);// restDetails.id

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				BaseActivity.showDialogMsg(context, "No data found from web!!!");
			} else {

				try {

					String strErrCode = new JSONObject(result)
							.optString("errorCode");
					if (Integer.parseInt(strErrCode) == 0) {
						dialog.dismiss();
					}
					String strErrMsg = new JSONObject(result)
							.optString("errorMessage").replace("<p>", "")
							.replace("</p>", "");
					BaseActivity.showDialogMsg(context, strErrMsg);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}
	}

	static String strSubject, strMsg;

	public void sendEmailVenue(final Context con) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(con);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.sendmobile);

		TextView txtSendMobile = (TextView) dialog
				.findViewById(R.id.txtSendMobileTitle);
		txtSendMobile.setText("Email This Venue");

		edtWRTitle = (EditText) dialog.findViewById(R.id.edtWRTitle);
		edtWRTitle.setHint("Enter Subject");
		edtReviewComments = (EditText) dialog.findViewById(R.id.edtMsg);

		Button btnWriteReviewSubmit = (Button) dialog
				.findViewById(R.id.btnWriteReviewSubmit);
		btnWriteReviewSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				strSubject = edtWRTitle.getText().toString();
				strMsg = edtReviewComments.getText().toString();

				if (!TextUtils.isEmpty(strSubject)
						&& !TextUtils.isEmpty(strMsg)) {
					if (ConnectivityReceiver.checkInternetConnection(con)) {
						dialog.dismiss();
						new EmailVenueTask(con).execute();
					} else {
						ConnectivityReceiver
								.showCustomDialog(SearchDetails.this);
					}

				} else {
					showDialogMsg(con, "Please enter subject / message");
				}
			}

		});

		dialog.show();

	}

	class EmailVenueTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		Context context;

		public EmailVenueTask(Context con) {
			// TODO Auto-generated constructor stub
			context = con;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Sending mail...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "emailVenue/" + PreferenceUtils.getUserSession() + "/"
					+ Explore.strVenueId + "/" + strSubject + "/" + strMsg);// restDetails.id

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				BaseActivity.showDialogMsg(context, "No data found from web!!!");

			} else {

				try {
					String strErrMsg = new JSONObject(result)
							.optString("errorMessage");
					BaseActivity.showDialogMsg(context, strErrMsg);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	class SendToFriend extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog1;
		Context context;

		public SendToFriend(Context cont) {
			// TODO Auto-generated constructor stub
			context = cont;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog1 = new ProgressDialog(context);
			pDialog1.setMessage("Sending mail to your friend. Please wait...");
			pDialog1.setCancelable(false);
			pDialog1.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "sendToFriend/" + PreferenceUtils.getUserSession() + "/"
					+ Explore.strVenueId + "/"
					+ strReceiverName.replace(" ", "%20").trim() + "/"
					+ strReceiverEmail.replace(" ", "%20").trim() + "/"
					+ strMsg.replace(" ", "%20").trim());// restDetails.id

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			/*
			 * if (null != pDialog && pDialog.isShowing()) { pDialog.dismiss();
			 * }
			 */

			if (!((Activity) context).isFinishing()) {
				if (null != pDialog1 && pDialog1.isShowing()) {
					pDialog1.dismiss();
				}
			}

			if (null == result || result.length() == 0) {
				BaseActivity.showDialogMsg(context, "No data found from web!!!");
			} else {

				try {
					String strErrMsg = new JSONObject(result)
							.optString("errorMessage");
					BaseActivity.showDialogMsg(context, strErrMsg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	static String strReceiverName, strReceiverEmail;;

	public void SendToFriend(final Context con) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(con);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.sendtofriend);

		edtReceiverName = (EditText) dialog.findViewById(R.id.edtreceivername);
		edtReceiverEmail = (EditText) dialog
				.findViewById(R.id.edtReceiverEmail);
		edtMsg = (EditText) dialog.findViewById(R.id.edtMsg);

		Button btnWriteReviewSubmit = (Button) dialog
				.findViewById(R.id.btnWriteReviewSubmit);
		btnWriteReviewSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				strReceiverName = edtReceiverName.getText().toString();
				strMsg = edtMsg.getText().toString();
				strReceiverEmail = edtReceiverEmail.getText().toString();

				if (!TextUtils.isEmpty(strReceiverName)
						&& !TextUtils.isEmpty(strReceiverEmail)
						&& !TextUtils.isEmpty(strMsg)) {
					Boolean bool = eMailValidation(strReceiverEmail);

					if (bool.equals(true)) {

						dialog.dismiss();
						// new SendToFriend(SearchDetails.this).execute();
						if (ConnectivityReceiver
								.checkInternetConnection(SearchDetails.this)) {

							new SendToFriend(SearchDetails.this).execute();
						} else {
							ConnectivityReceiver
									.showCustomDialog(SearchDetails.this);
						}

					} else {
						showDialogMsg(con, "Please enter valid email address");
					}

				} else {
					showDialogMsg(con, "All fields are mandatory");
				}

			}
		});

		dialog.show();

	}

	public void sendMobile(final Context con) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(con);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.sendtomobile);

		TextView txtSendMobile = (TextView) dialog
				.findViewById(R.id.txtSendMobileTitle);
		txtSendMobile.setText("Send To Mobile");

		TextView txtMsg = (TextView) dialog.findViewById(R.id.txtMsg);
		txtMsg.setText("Send " + str_restaurant_name
				+ " address as a SMS to mobile phone");

		edtSendMobile = (EditText) dialog.findViewById(R.id.edtSendMobile);
		edtSendMobile.setHint("Enter Mobile Number");

		Button btnWriteReviewSubmit = (Button) dialog
				.findViewById(R.id.btnWriteReviewSubmit);
		btnWriteReviewSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				strSubject = edtSendMobile.getText().toString();

				if (!TextUtils.isEmpty(strSubject)) {
					if (strSubject.length() == 10) {
						if (ConnectivityReceiver.checkInternetConnection(con)) {
							dialog.dismiss();
							new SendMobileTask(SearchDetails.this).execute();

						} else {

							ConnectivityReceiver
									.showCustomDialog(SearchDetails.this);
						}
					} else {
						showDialogMsg(con, "Please enter valid mobile number");

					}
				} else {
					showDialogMsg(con, "Please enter mobile number");

				}

			}
		});

		dialog.show();

	}

	class SendMobileTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		Context con;

		public SendMobileTask(Context context) {
			// TODO Auto-generated constructor stub
			con = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(con);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "sendToMobile/" + PreferenceUtils.getUserSession() + "/"
					+ Explore.strVenueId + "/"
					+ strSubject.replace(" ", "%20").trim());// restDetails.id

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (!((Activity) con).isFinishing()) {
				if (null != pDialog && pDialog.isShowing()) {
					pDialog.dismiss();
				}
			}

			/*
			 * if (null != pDialog && pDialog.isShowing()) { pDialog.dismiss();
			 * }
			 */

			if (null == result || result.length() == 0) {
				BaseActivity.showDialogMsg(con, "No data found from web!!!");
			} else {

				try {
					String strErrMsg = new JSONObject(result)
							.optString("errorMessage");
					BaseActivity.showDialogMsg(con, strErrMsg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

	public void setDetails() {

		try {

			JSONObject jObject = new JSONObject(result);
			if (jObject.has("id")) {
				Explore.strVenueId = jObject.getString("id");
			}

			if (jObject.has("happy_hours")) {
				str_happy_hours = jObject.getString("happy_hours");
			}

			if (jObject.has("happy_hours_start")) {
				str_happy_hours_start = jObject.getString("happy_hours_start");
			}

			if (jObject.has("happy_hours_end")) {
				str_happy_hours_end = jObject.getString("happy_hours_end");
			}

			if (jObject.has("serving_timings")) {
				str_serving_timings = jObject.getString("serving_timings");
			}

			if (jObject.has("facilities")) {
				str_facilities = jObject.getString("facilities");
			}
			if (jObject.has("drinks_names")) {
				str_drinks_names = jObject.getString("drinks_names");
			}

			if (jObject.has("CardsAccepted")) {
				str_CardsAccepted = jObject.getString("CardsAccepted");
			}

			if (jObject.has("FineDining")) {
				str_FineDining = jObject.getString("FineDining");
			}

			if (jObject.has("is_veg")) {
				str_is_veg = jObject.getString("is_veg");
			}

			if (jObject.has("is_delivery")) {
				str_is_delivery = jObject.getString("is_delivery");
			}

			if (jObject.has("is_takeaway")) {
				str_is_takeaway = jObject.getString("is_takeaway");
			}

			if (jObject.has("liqour_served")) {
				str_liqour_served = jObject.getString("liqour_served");
			}

			if (jObject.has("reviews_count")) {
				str_reviews_count = jObject.getString("reviews_count");
			}

			if (jObject.has("costpertwo")) {
				str_costpertwo = jObject.getString("costpertwo");
			}
			if (jObject.has("OverView")) {
				str_OverView = jObject.getString("OverView");
			}

			if (jObject.has("favorites_cnt")) {
				str_favorites_cnt = jObject.getString("favorites_cnt");
			}

			if (jObject.has("beenthere_cnt")) {
				str_beenthere_cnt = jObject.getString("beenthere_cnt");
			}
			if (jObject.has("wanttotry_cnt")) {
				str_wantTry_cnt = jObject.getString("wanttotry_cnt");
			}

			if (jObject.has("user_favorites")) {
				str_user_favorites = jObject.getString("user_favorites");
			}
			if (jObject.has("user_total_been")) {
				str_user_total_been = jObject.getString("user_total_been");
			}
			if (jObject.has("RestaurantName")) {
				str_restaurant_name = jObject.getString("RestaurantName");
				Explore.strRestaurantTitle = str_restaurant_name;
			}
			if (jObject.has("Address")) {
				str_address = jObject.getString("Address");
			}
			if (jObject.has("Phone")) {
				str_phon = jObject.getString("Phone");
			}
			if (jObject.has("cuisines_list")) {
				str_cuisines = jObject.getString("cuisines_list");
			}

			if (jObject.has("has_sundayBrunch")) {
				str_sunday_bunch = jObject.getString("has_sundayBrunch");
			}
			if (jObject.has("next_sunday_brunch")) {
				str_next_sunday_bunch = jObject.getString("next_sunday_brunch");
			}

			if (jObject.has("Latitude")) {
				str_lat = jObject.getString("Latitude");
			}

			if (jObject.has("Longitude")) {
				str_lng = jObject.getString("Longitude");
			}

			if (jObject.has("existing_buffets")) {
				str_existing_buffets = jObject.getString("existing_buffets");
			}

			if (jObject.has("brunch_timings")) {
				str_brunch_timings = jObject.getString("brunch_timings");
			}

			if (jObject.has("Location")) {
				str_Location = jObject.getString("Location");
			}

			if (jObject.has("has_deals")) {
				str_has_deals = jObject.getString("has_deals");
			}

			if (jObject.has("booktable")) {
				str_booktables = jObject.getString("booktable");
			}

			if (jObject.has("orderfood")) {
				str_orderfood = jObject.getString("orderfood");
			}

			if (jObject.has("overall_rating")) {
				str_overall_rating = jObject.getString("overall_rating")
						.replace("null", "0");
			}

			// getting reviews.
			reviews.clear();
			JSONArray reviewArray = jObject.getJSONArray("reviewdetails");
			if (reviewArray.length() > 0) {
				for (int i = 0; i < reviewArray.length(); i++) {
					reviewDO reviewsObj = new reviewDO();
					JSONObject joObj = reviewArray.getJSONObject(i);

					reviewsObj.id = joObj.getString("id");
					reviewsObj.title = joObj.getString("title");
					reviewsObj.comment = joObj.getString("comment");
					reviewsObj.rating = joObj.getString("rating");
					reviewsObj.datecreated = joObj.getString("datecreated");
					reviewsObj.firstname = joObj.getString("firstname");
					reviewsObj.lastname = joObj.getString("lastname");

					reviews.add(reviewsObj);
				}
			}

			for (int i = 0; i < reviews.size(); i++) {
				View view = inflator.inflate(R.layout.review_row, null);

				TextView tvName = (TextView) view.findViewById(R.id.tvName);
				RatingBar rating = (RatingBar) view
						.findViewById(R.id.rtReviewRating);
				LayerDrawable stars = (LayerDrawable) rating
						.getProgressDrawable();
				stars.getDrawable(2).setColorFilter(Color.RED,
						PorterDuff.Mode.SRC_ATOP);

				TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
				TextView tvDisc = (TextView) view.findViewById(R.id.tvDisc);
				tvDisc.setEllipsize(TextUtils.TruncateAt.END);

				tvName.setText(reviews.get(i).firstname.replace("null", " ")
						+ " " + reviews.get(i).lastname.replace("null", " "));
				tvTime.setText(reviews.get(i).datecreated.replace("null", " "));
				tvDisc.setText(reviews.get(i).comment.replace("null", " "));

				final reviewDO rvDo = reviews.get(i);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						startActivity(new Intent(v.getContext(),
								ReviewDetails.class).putExtra("review", rvDo));
					}
				});

				int rates = Integer.parseInt(reviews.get(i).rating.replace(
						"null", "0"));
				rating.setRating(rates);

				llReviews.addView(view);
			}

			if (reviews.size() > 3) {
				tvViewAll.setVisibility(View.VISIBLE);
			} else {
				tvViewAll.setVisibility(View.GONE);
			}

			tvRestrentTitle.setText(str_restaurant_name);

			tvAddressLine1.setText(str_address + "," + str_Location);

			try {

				if (str_phon.contains(",")) {
					String s[] = str_phon.split(",");

					tvPhone.setText(s[0] +" \nExt. "+s[1]);

				} else {

					tvPhone.setText(str_phon);
				}

			} catch (Exception e) {
				tvPhone.setText(str_phon);
			}

			if (str_has_deals.equalsIgnoreCase("0")) {
				btnviewdeals.setVisibility(View.GONE);
			} else {
				btnviewdeals.setVisibility(View.VISIBLE);
			}

			if (str_booktables.equalsIgnoreCase("0")) {
				btnBookTable.setVisibility(View.GONE);
			} else {
				btnBookTable.setVisibility(View.VISIBLE);
			}

			if (str_orderfood.equalsIgnoreCase("0")) {
				btnOrderFood.setVisibility(View.GONE);
			} else {
				btnOrderFood.setVisibility(View.VISIBLE);
			}

			tVBeenThere.setText(tVBeenThere.getText().toString() + "\n("
					+ str_beenthere_cnt + ")");

			if (str_happy_hours.equalsIgnoreCase("false")) {
				((TableRow) findViewById(R.id.tlHappyhours))
						.setVisibility(View.GONE);
				findViewById(R.id.viewhappyHours)
						.setVisibility(View.GONE);
			} else {
				tvHappyHours = (TextView) findViewById(R.id.tvHappyHours);
				tvHappyHours.setText(str_happy_hours_start + " TO "
						+ str_happy_hours_end);
			}

			RatingBar ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
			// RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
			LayerDrawable stars = (LayerDrawable) ratingBar1
					.getProgressDrawable();
			stars.getDrawable(2).setColorFilter(Color.RED,
					PorterDuff.Mode.SRC_ATOP);
			if (str_overall_rating.equalsIgnoreCase("null")
					|| str_overall_rating.equalsIgnoreCase("0")) {
				str_overall_rating = "0";
			}
			int rate = Integer.parseInt(str_overall_rating);//
			if (rate > 0) {
				ratingBar1.setRating(rate);
			} else {
				ratingBar1.setVisibility(View.GONE);
			}

			LinearLayout lyReviews = (LinearLayout) findViewById(R.id.lyReviews);
			lyReviews.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// restDO = restDetails;
					startActivity(new Intent(v.getContext(),
							RestuarentReviews.class));

				}
			});

			if (str_cuisines.length() == 0) {
				((TableRow) findViewById(R.id.tlCusine))
						.setVisibility(View.GONE);
				findViewById(R.id.viewtlCusine)
						.setVisibility(View.GONE);
			} else {

				((TableRow) findViewById(R.id.tlCusine))
						.setVisibility(View.VISIBLE);
				findViewById(R.id.viewtlCusine)
						.setVisibility(View.VISIBLE);
				cusineText = (TextView) findViewById(R.id.cusineText);
				cusineText.setText(Html.fromHtml(str_cuisines));
			}

			if (str_serving_timings.length() == 0) {
				((TableRow) findViewById(R.id.tlTimings))
						.setVisibility(View.GONE);
				findViewById(R.id.viewTimings)
						.setVisibility(View.GONE);
			} else {
				TextView timings = (TextView) findViewById(R.id.tvTimings);
				timings.setText(Html.fromHtml(str_serving_timings));
			}

			if (str_drinks_names.length() == 0) {
				((TableRow) findViewById(R.id.tlDrinks))
						.setVisibility(View.GONE);
				findViewById(R.id.viewDrinks).setVisibility(View.GONE);
			} else {
				TextView tvDrinks = (TextView) findViewById(R.id.tvDrinks);
				tvDrinks.setText(str_drinks_names);
			}

			if (str_facilities.length() == 0) {
				((TableRow) findViewById(R.id.tlFacilities))
						.setVisibility(View.GONE);

			} else {
				TextView tvFacilities = (TextView) findViewById(R.id.tvFacilities);
				tvFacilities.setText(str_facilities);
			}

			if (str_OverView.length() == 0) {
				((TableRow) findViewById(R.id.tlOverview))
						.setVisibility(View.GONE);
				findViewById(R.id.viewFacilities)
						.setVisibility(View.GONE);
			} else {
				TextView tvOverview = (TextView) findViewById(R.id.tvOverview);
				tvOverview.setText(Html.fromHtml(str_OverView));
			}

			TextView tvReviewTop = (TextView) findViewById(R.id.tvReviewTop);
			if (!str_reviews_count.equalsIgnoreCase("0")) {
				tvReviewTop.setText("(" + str_reviews_count + ")Reviews");
			} else {
				tvReviewTop.setVisibility(View.GONE);
			}

			tvReviewTop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					startActivity(new Intent(v.getContext(),
							RestuarentReviews.class));

				}
			});

			LinearLayout lyHighLights = (LinearLayout) findViewById(R.id.lyHighlights);
			JSONArray jArray = jObject.getJSONArray("highlets");
			JSONArray jObj1;
			TextView txt;
			for (int i = 0; i < jArray.length(); i++) {
				jObj1 = jArray.getJSONObject(i).names();
				txt = new TextView(SearchDetails.this);
				String s = jObj1.toString().replace("[\"", "")
						.replace("\"]", "");
				if (jArray.getJSONObject(i).optInt(s) == 1) {
					txt.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.avilable, 0, 0, 0);
				} else {
					txt.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.not_avilable, 0, 0, 0);
				}
				txt.setCompoundDrawablePadding(5);
				// ///roo
				txt.setEllipsize(TextUtils.TruncateAt.END);
				txt.setText(s);
				lyHighLights.addView(txt);
			}

			TextView tvLableReview = (TextView) findViewById(R.id.tvRatingsAndReview);
			tvLableReview.setText("Ratings & Reviews(" + str_reviews_count
					+ ")");

			LinearLayout llRatingSection = (LinearLayout) findViewById(R.id.llRatingSection);
			if (str_reviews_count.equalsIgnoreCase("0")) {
				llRatingSection.setVisibility(View.GONE);
			}

			if (str_next_sunday_bunch.length() == 0
					|| str_next_sunday_bunch.equalsIgnoreCase("null")) {
				((TableRow) findViewById(R.id.tlNextSundayTimings))
						.setVisibility(View.GONE);
				findViewById(R.id.viewNextSundayTimings)
						.setVisibility(View.GONE);

			} else {
				TextView tvNextSundayTimings = (TextView) findViewById(R.id.tvNextSundayTimings);
				tvNextSundayTimings.setText(str_next_sunday_bunch.replace(
						"null", " "));
			}

			if (str_brunch_timings.length() == 0
					|| str_next_sunday_bunch.equalsIgnoreCase("null")) {
				((TableRow) findViewById(R.id.tlSundayBrunch))
						.setVisibility(View.GONE);
				findViewById(R.id.viewSundayTimings)
						.setVisibility(View.GONE);

			} else {
				TextView tvSundayTimings = (TextView) findViewById(R.id.tvSundayTimings);
				tvSundayTimings
						.setText(str_brunch_timings.replace("null", " "));
			}

			if (str_existing_buffets.length() == 0
					|| str_next_sunday_bunch.equalsIgnoreCase("null")) {
				((TableRow) findViewById(R.id.tlexisting_buffets))
						.setVisibility(View.GONE);
				findViewById(R.id.viewExisting_buffets)
						.setVisibility(View.GONE);

			} else {
				TextView tvexisting_buffets = (TextView) findViewById(R.id.tvexisting_buffets);
				/*
				 * tvexisting_buffets.setText(str_existing_buffets.replace(
				 * "null", " "));
				 */
				tvexisting_buffets.setText(Localsecrets.str_buffet_time);
			}

			TextView tvCost = (TextView) findViewById(R.id.tvCost);
 
			if (str_costpertwo.equalsIgnoreCase("1")
					|| str_costpertwo.equalsIgnoreCase("0")
					|| str_costpertwo.equalsIgnoreCase(""))
				tvCost.setText(getResources().getString(R.string.oneRupee));
			else if (str_costpertwo.equalsIgnoreCase("2"))
				tvCost.setText(getResources().getString(R.string.twoRupees));
			else if (str_costpertwo.equalsIgnoreCase("3"))
				tvCost.setText(getResources().getString(R.string.threeRupee));
			else if (str_costpertwo.equalsIgnoreCase("4"))
				tvCost.setText(getResources().getString(R.string.fourRupee));
			else if (str_costpertwo.equalsIgnoreCase("5"))
				tvCost.setText(getResources().getString(R.string.fourRupee)
						+ getResources().getString(R.string.oneRupee));

			LinearLayout llDirections = (LinearLayout) findViewById(R.id.llDirections);
			llDirections.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(
							android.content.Intent.ACTION_VIEW, Uri
									.parse("http://maps.google.com/maps?saddr="
											+ Appconstants.LATTITUDE + ","
											+ Appconstants.LANGITUDE
											+ "&daddr=" + str_lat + ","
											+ str_lng));
					startActivity(intent);
				}
			});

			svMainView.setVisibility(View.VISIBLE);

		} catch (Exception e) {

			Log.e("", "" + e);
		}

		Setting_Event_Details();
	}

	class getOrderCategoriesTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		String strVenueId1, jsonString, StrCat_Id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(SearchDetails.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String service_url = "";

			if (Localsecrets.flg == 0) {
				service_url = Appconstants.MAIN_HOST + "orderFoodCategories2/"
						+ Explore.strVenueId + "/1";
			} else if (Localsecrets.flg == 1) {
				service_url = Appconstants.MAIN_HOST + "orderFoodCategories2/"
						+ Explore.strVenueId + "/0";
			} else {
				service_url = Appconstants.MAIN_HOST + "orderFoodCategories2/"
						+ Explore.strVenueId + "/" + Localsecrets.flg;
			}

			return WebServiceCalls.getJSONString(service_url);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			if (result == null || "".equals(result)) {
				return;
			}
			try {
				JSONObject mainObject = new JSONObject(result);
				JSONArray jsonArray = mainObject.getJSONArray("settings");
				if (jsonArray.length() > 0) {
					Launcher.restOrderDetails = mainObject;
					Intent in = new Intent(getApplicationContext(),
							OrderFood.class);

					startActivity(in);

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}
}
