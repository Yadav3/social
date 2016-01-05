package com.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewExamples extends Activity {

	// public static final String CALLBACK_URL = "myapp://connect";
	// private static final String AUTH_URL =
	// "https://foursquare.com/oauth2/authenticate?response_type=code";
	// private static final String TOKEN_URL =
	// "https://foursquare.com/oauth2/access_token?grant_type=authorization_code";
	private static final String API_URL = "https://api.foursquare.com/v2";
	private ListView mListView;
	private NearbyAdapter mAdapter;
	private ArrayList<FsqVenue> mNearbyList;
	WebView webView;
	static String mAccessToken;
	EditText search;
	FsqVenue venue1;
	ArrayList<FsqVenue> venueList1 = new ArrayList<FsqVenue>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		webView = (WebView) findViewById(R.id.webview);
		mListView = (ListView) findViewById(R.id.listView1);

		mAdapter = new NearbyAdapter(this);
		mNearbyList = new ArrayList<FsqVenue>();

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().getAllowFileAccess();
		webView.loadUrl("https://foursquare.com/oauth2/authorize?client_id=0BTAHE0BSCCMWHGHEPJS25UFPWHQUVG2EVMPCH2FWV1NX4E3&response_type=code&redirect_uri=http://uat.myplatinumlife.com/newwebservice/Loading.html");

		Button btn = (Button) findViewById(R.id.button1);
		search = (EditText) findViewById(R.id.search);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				String latitude = "17.3998";
				String longitude = "78.4766";
				search.setText("");

				if (latitude.equals("") || longitude.equals("")) {
					Toast.makeText(WebViewExamples.this,
							"Latitude or longitude is empty",
							Toast.LENGTH_SHORT).show();
					return;
				}

				double lat = Double.valueOf(latitude);
				double lon = Double.valueOf(longitude);

				loadNearbyPlaces(lat, lon);

			}
		});

		Button btnCheckin = (Button) findViewById(R.id.button2);
		btnCheckin.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					search.setText("");
					HttpPost post = new HttpPost(
							"https://api.foursquare.com/v2/checkins/add?venueId="
									// + mNearbyList.get(0).id
									+ "4fb9f196e4b0d55659c8fdf7&shout=great....&v=20141111&broadcast=public&oauth_token="
									+ mAccessToken);

					HttpClient hc = new DefaultHttpClient();
					HttpResponse rp = hc.execute(post);
					//
					// Log.v(TAG,"response from server "+EntityUtils.toString(rp.getEntity()));
					if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						Toast.makeText(WebViewExamples.this, "Done",
								Toast.LENGTH_SHORT).show();
						String response = EntityUtils.toString(rp.getEntity());
						// Log.v(TAG,
						// "response from server====="
						// + response);
					}
				} catch (Exception e) {  
				}
 
				// try {
				// // Construct data
				// String data = URLEncoder.encode("ll", "UTF-8")
				// + "="
				// + URLEncoder.encode(
				// "17.398728907340647,78.47586322391133",
				// "UTF-8");
				// data += "&"
				// + URLEncoder.encode("venueId", "UTF-8")
				// + "="
				// + URLEncoder.encode("4fb9f196e4b0d55659c8fdf7",
				// "UTF-8");
				// data += "&" + URLEncoder.encode("oauth_token", "UTF-8")
				// + "=" + URLEncoder.encode(mAccessToken, "UTF-8");
				//
				// // Send data
				// URL url = new URL(
				// "https://api.foursquare.com/v2/checkins/add");
				// URLConnection conn = url.openConnection();
				// conn.setDoOutput(true);
				// OutputStreamWriter wr = new OutputStreamWriter(conn
				// .getOutputStream());
				// wr.write(data);
				// wr.flush();
				//
				// // Get the response
				// BufferedReader rd = new BufferedReader(
				// new InputStreamReader(conn.getInputStream()));
				//
				// String line;
				// while ((line = rd.readLine()) != null) {
				// // Process line...
				// }
				// wr.close();
				// rd.close();
				// } catch (Exception e) {
				// }

			}
		});
		webView.setWebViewClient(new HelloWebViewClient());
		
		search.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				venueList1.clear(); 
				for (int i = 0; i < mNearbyList.size(); i++) {
					String string = mNearbyList.get(i).name.toLowerCase(Locale.US);
					
					if (string.contains(search.getText().toString().toLowerCase(Locale.US))) {
						
						venue1 = new FsqVenue();

						venue1.id = mNearbyList.get(i).id;
						venue1.name = string;
						venue1.distance = mNearbyList.get(i).distance;
						venue1.direction = mNearbyList.get(i).direction;
						
						venueList1.add(venue1);
 
					}
				}
				if(venueList1.size()>0){
					mAdapter.setData(venueList1);
					mListView.setAdapter(mAdapter);
					
				}else{
				/*	mAdapter.setData(mNearbyList);
					mListView.setAdapter(mAdapter);*/
					Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_LONG).show();
					
				}
			}
		});
	}

	private void loadNearbyPlaces(final double latitude, final double longitude) {

		// new Thread() {
		// @Override
		// public void run() {
		// int what = 0;
		//
		try {

			mNearbyList = getNearby(latitude, longitude);
			webView.setVisibility(View.INVISIBLE);
			mListView.setVisibility(View.VISIBLE);
			mAdapter.setData(mNearbyList);
			mListView.setAdapter(mAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// mHandler.sendMessage(mHandler.obtainMessage(what));
		// }
		//
		// }.start();
	}

	// private Handler mHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	//
	// if (msg.what == 0) {
	// if (mNearbyList.size() == 0) {
	// Toast.makeText(WebViewExamples.this,
	// "No nearby places available", Toast.LENGTH_SHORT)
	// .show();
	// return;
	// }
	//
	// webView.setVisibility(View.INVISIBLE);
	// mListView.setVisibility(View.VISIBLE);
	// mAdapter.setData(mNearbyList);
	// mListView.setAdapter(mAdapter);
	// } else {
	// Toast.makeText(WebViewExamples.this,
	// "Failed to load nearby places", Toast.LENGTH_SHORT)
	// .show();
	// }
	// }
	// };

	public ArrayList<FsqVenue> getNearby(double latitude, double longitude)
			throws Exception {
		ArrayList<FsqVenue> venueList = new ArrayList<FsqVenue>();

		try {
			String v = timeMilisToString(System.currentTimeMillis());
			String ll = String.valueOf(latitude) + ","
					+ String.valueOf(longitude);
			URL url = new URL(API_URL + "/venues/search?ll=" + ll
					+ "&oauth_token=" + mAccessToken + "&v=" + v);

			// Log.d(TAG, "Opening URL " + url.toString());

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			// urlConnection.setDoOutput(true);

			urlConnection.connect();

			String response = streamToString(urlConnection.getInputStream());
			JSONObject jsonObj = (JSONObject) new JSONTokener(response)
					.nextValue();

			JSONArray groups = (JSONArray) jsonObj.getJSONObject("response")
					.getJSONArray("venues");

			int length = groups.length();

			if (length > 0) {
				JSONObject location;
				for (int i = 0; i < length; i++) {
					JSONObject group = (JSONObject) groups.get(i);

					FsqVenue venue = new FsqVenue();

					venue.id = group.getString("id");
					venue.name = group.getString("name");

					location = (JSONObject) group.getJSONObject("location");

					venue.distance = location.getInt("distance");
					venueList.add(venue);
					// JSONArray items = (JSONArray)
					// group.getJSONArray("items").get(0).ge;
					//
					// int ilength = items.length();
					//
					// for (int j = 0; j < ilength; j++) {
					// JSONObject item = (JSONObject) items.get(j);
					//
					// FsqVenue venue = new FsqVenue();
					//
					// venue.id = item.getString("id");
					// venue.name = item.getString("name");
					//
					// JSONObject location = (JSONObject) item
					// .getJSONObject("location");
					//
					// Location loc = new Location(
					// LocationManager.GPS_PROVIDER);
					//
					// loc.setLatitude(Double.valueOf(location
					// .getString("lat")));
					// loc.setLongitude(Double.valueOf(location
					// .getString("lng")));
					//
					// venue.location = loc;
					// venue.address = location.getString("address");
					// venue.distance = location.getInt("distance");
					// venue.herenow = item.getJSONObject("hereNow").getInt(
					// "count");
					// venue.type = group.getString("type");
					//
					// venueList.add(venue);
					// }
				}
			}
		} catch (Exception ex) {
			throw ex;
		}

		return venueList;
	}

	private String timeMilisToString(long milis) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(milis);

		return sd.format(calendar.getTime());
	}

	private String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}
}