package com.etisbew.eatz.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.objects.LocationDO;

public class BackgroundService extends Service {

	public static final String BROADCAST_ACTION = "Hello World";
	Intent intent;
 
	@Override 
	public void onCreate() {
		super.onCreate(); 
		intent = new Intent(BROADCAST_ACTION);

		Timer t = new Timer();

		// Set the schedule function and rate
		t.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (BaseActivity
						.checkInternetConnection(BackgroundService.this)) {
					// if (!Localsecrets.serviceFalg) {
					// Localsecrets.serviceFalg = true;
					new LocationsListTask().execute();
					// }
				}
				else{
					ConnectivityReceiver.showCustomDialog(BackgroundService.this);
				}
			}
		},

		0,

		1440000);// 1440000 = 1 day
	}

	@Override
	public void onStart(Intent intent, int startId) {

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		// handler.removeCallbacks(sendUpdatesToUI);
		super.onDestroy();
		Log.v("STOP_SERVICE", "DONE");

	}

	public static Thread performOnBackgroundThread(final Runnable runnable) {
		final Thread t = new Thread() {
			@Override
			public void run() {
				try {
					runnable.run();
				} finally {

				}
			}
		};
		t.start();
		return t;
	}

	class LocationsListTask extends AsyncTask<Object, Void, Boolean> {

		HttpResponse response1;
		String strLocationsRes;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(Object... params) {

			try {

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(Appconstants.MAIN_HOST
						+ "getLocations/" + Appconstants.strCityId + "/"
						+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE);

				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				post.setEntity(new UrlEncodedFormEntity(pairs));
				response1 = client.execute(post);
				if (response1.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response1.getEntity();
					String re = EntityUtils.toString(entity, HTTP.UTF_8);

					strLocationsRes = re.trim();
				}

			} catch (Exception e) {
				// Toast.makeText(Login.this, "XML Pasing Exception = " + e,
				// Toast.LENGTH_LONG).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			super.onPostExecute(success);

			try {
				if (response1.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

					JSONObject jObject = new JSONObject(strLocationsRes);
					JSONArray locationsArray = jObject
							.getJSONArray("locations");

					Appconstants.location_id = new ArrayList<String>();
					Appconstants.location_name = new ArrayList<String>();

					Appconstants.location_do.clear();
					// Localsecrets.cuisine_id = new ArrayList<String>();
					// Localsecrets.cuisine_name = new ArrayList<String>();

					for (int i = 0; i < locationsArray.length(); i++) {
						String id;
						LocationDO ld = new LocationDO();
						try {
							id = locationsArray.getJSONObject(i)
									.getString("id").toString();
							// store id.
							ld.id = id;
							Appconstants.location_id.add(id);

							String loc_name = locationsArray.getJSONObject(i)
									.getString("name").toString();
							// store name.
							ld.name = loc_name;
							Appconstants.location_name.add(loc_name);

							// add object to array.
							Appconstants.location_do.add(ld);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					Appconstants.location_ids = Appconstants.location_id
							.toArray(new String[Appconstants.location_id.size()]);
					Appconstants.location_names = Appconstants.location_name
							.toArray(new String[Appconstants.location_name
									.size()]);

					// for (int i = 0; i < locationsArray.length(); i++) {
					// locationDatasource.insertLocation(location_ids[i],
					// location_names[i]);
					// }

					// int locationscount = locationsArray.length();

					// locationDatasource.getAllLocations();

					// locationDatasource.close();

					// new CuisinesListTask().execute();

				}
			} catch (Exception e) {

			}

		}
	}

	/*
	 * class CuisinesListTask extends AsyncTask<Object, Void, Boolean> {
	 * 
	 * HttpResponse response2; String strCuisinesRes;
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute();
	 * 
	 * }
	 * 
	 * @Override protected Boolean doInBackground(Object... params) {
	 * 
	 * try {
	 * 
	 * HttpClient client = new DefaultHttpClient(); HttpPost post = new
	 * HttpPost(Appconstants.MAIN_HOST + "getCuisines");
	 * 
	 * List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	 * post.setEntity(new UrlEncodedFormEntity(pairs)); response2 =
	 * client.execute(post); if (response2.getStatusLine().getStatusCode() ==
	 * HttpStatus.SC_OK) { HttpEntity entity = response2.getEntity(); String re
	 * = EntityUtils.toString(entity, HTTP.UTF_8);
	 * 
	 * strCuisinesRes = re.trim(); }
	 * 
	 * } catch (Exception e) { // Toast.makeText(Login.this,
	 * "XML Pasing Exception = " + e, // Toast.LENGTH_LONG).show(); } return
	 * null; }
	 * 
	 * @Override protected void onPostExecute(Boolean success) {
	 * super.onPostExecute(success);
	 * 
	 * try { if (response2.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	 * {
	 * 
	 * JSONObject jObject = new JSONObject(strCuisinesRes); JSONArray
	 * cuisinesArray = jObject.getJSONArray("cuisines");
	 * 
	 * // locationDatasource = new LocationDatasource( // Localsecrets.this); //
	 * // locationDatasource.open();
	 * 
	 * for (int i = 0; i < cuisinesArray.length(); i++) { String id; try { id =
	 * cuisinesArray.getJSONObject(i).getString("id") .toString();
	 * Localsecrets.cuisine_id.add(id);
	 * 
	 * String strCuisinename = cuisinesArray .getJSONObject(i).getString("name")
	 * .toString(); Localsecrets.cuisine_name.add(strCuisinename);
	 * 
	 * } catch (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * } Localsecrets.cuisine_ids = Localsecrets.cuisine_id .toArray(new
	 * String[Localsecrets.cuisine_id.size()]); Localsecrets.cuisine_names =
	 * Localsecrets.cuisine_name .toArray(new String[Localsecrets.cuisine_name
	 * .size()]);
	 * 
	 * // for (int i = 0; i < cuisinesArray.length(); i++) { //
	 * locationDatasource.insertCuisine(cuisine_ids[i], // cuisine_names[i]); //
	 * }
	 * 
	 * // int locationscount = locationsArray.length();
	 * 
	 * // locationDatasource.getAllLocations();
	 * 
	 * // locationDatasource.close();
	 * 
	 * }
	 * 
	 * } catch (Exception e) {
	 * 
	 * }
	 * 
	 * } }
	 */
}
