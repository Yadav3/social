package com.etisbew.eatz.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.etisbew.eatz.bussinesslayer.AssetDatabaseOpenHelper;
import com.etisbew.eatz.bussinesslayer.DBHandler;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.CitiesDO;
import com.etisbew.eatz.objects.LocationDO;
import com.etisbew.eatz.web.WebServiceCalls;

public class Localsecrets extends BaseActivity implements OnClickListener,
		OnTouchListener {

	public static String str_titles = "", str_buffet_time;
	int mFlipping = 0; // Initially flipping is off
	Button btnSkip;
	ViewFlipper flipper;
	private Context mContext;
	TextView mDotsText[];
	private int mDotsCount;
	private LinearLayout mDotsLayout;

	public static String selectedLocationId = "null",
			selectedLocationName = "null";
	public static ArrayList<String> selectedItemsArray_color = null;
	public static int flg = 0, search_details_flag = 0;

	public static String det_fl = "";
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private AnimationListener mAnimationListener;

	double lat, lng;
	boolean network_enabled;
	GPSTracker gps;
	private DBHandler dHandler = null;
	SQLiteDatabase db;
	AssetDatabaseOpenHelper astDBHelper = null;
	// int citychanged=0;
	@SuppressWarnings("deprecation")
	private final GestureDetector detector = new GestureDetector(
			new SwipeGestureDetector());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.localsecrets);

		try {

			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			int version = pInfo.versionCode;
			System.out.println("version" + version
					+ PreferenceUtils.getVersionCode());
			if (PreferenceUtils.getVersionCode() == version) {
				//
			} else {
				MyApplication.getInstance().clearApplicationData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		selectedItemsArray_color = new ArrayList<String>();

		LocationManager locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);

		try {
			network_enabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!network_enabled) {
				Builder dialog = new AlertDialog.Builder(Localsecrets.this);
				dialog.setMessage("Your location service is currently disabled! please click location settings to enable it.");
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
								Appconstants.ltDo.id = "0";
								Appconstants.ltDo.name = "All of Hyderabad";
								Appconstants.strCityName = "Hyderabad";
								Appconstants.LATTITUDE = "17.3939";
								Appconstants.LANGITUDE = "78.4677";

								lat = 17.3939;
								lng = 78.4677;

								createDatabase();
								if (ConnectivityReceiver
										.checkInternetConnection(Localsecrets.this)) {

									try {
										// getLocationByCityName(loc);
										getLocationName();

									} catch (NullPointerException e) {
									} catch (Exception e) {
									}

								} else {
									ConnectivityReceiver
											.showCustomDialog(Localsecrets.this);
								}

							}
						});
				dialog.show();

			} else {

				try {
					createDatabase();

				} catch (Exception e) {
					e.printStackTrace();
				}

				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							Appconstants.location_array = dHandler
									.getAllLocationsArray();
							Appconstants.locationID_array = dHandler
									.getAllLocationIDs();
							Appconstants.location_lat = dHandler
									.getAllLatitude();
							Appconstants.location_lang = dHandler
									.getAllLangitude();

							Appconstants.india_city_id.clear();
							Appconstants.india_city_name.clear();
							Appconstants.india_city_lat.clear();
							Appconstants.india_city_lang.clear();
							Appconstants.uae_city_id.clear();
							Appconstants.uae_city_name.clear();
							Appconstants.uae_city_lat.clear();
							Appconstants.uae_city_lang.clear();

							Appconstants.india_city_id = dHandler
									.getAllCityId("India");
							Appconstants.india_city_name = dHandler
									.getAllCityNames("India");
							Appconstants.india_city_lat = dHandler
									.getAllLat("India");
							Appconstants.india_city_lang = dHandler
									.getAllLang("India");

							Appconstants.uae_city_id = dHandler
									.getAllCityId("UAE");
							Appconstants.uae_city_name = dHandler
									.getAllCityNames("UAE");
							Appconstants.uae_city_lat = dHandler
									.getAllLat("UAE");
							Appconstants.uae_city_lang = dHandler
									.getAllLang("UAE");
						} catch (NullPointerException e) {
						} catch (Exception e) {
						}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// hideLoader();
							}
						});
					}
				}).start();

				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							Appconstants.location_array = dHandler
									.getAllLocationsArray();
							Appconstants.location_lat = dHandler
									.getAllLatitude();
							Appconstants.location_lang = dHandler
									.getAllLangitude();
							Appconstants.locationID_array = dHandler
									.getAllLocationIDs();

							Appconstants.india_city_id.clear();
							Appconstants.india_city_name.clear();
							Appconstants.india_city_lat.clear();
							Appconstants.india_city_lang.clear();
							Appconstants.uae_city_id.clear();
							Appconstants.uae_city_name.clear();
							Appconstants.uae_city_lat.clear();
							Appconstants.uae_city_lang.clear();

							Appconstants.india_city_id = dHandler
									.getAllCityId("India");
							Appconstants.india_city_name = dHandler
									.getAllCityNames("India");
							Appconstants.india_city_lat = dHandler
									.getAllLat("India");
							Appconstants.india_city_lang = dHandler
									.getAllLang("India");

							Appconstants.uae_city_id = dHandler
									.getAllCityId("UAE");
							Appconstants.uae_city_name = dHandler
									.getAllCityNames("UAE");
							Appconstants.uae_city_lat = dHandler
									.getAllLat("UAE");
							Appconstants.uae_city_lang = dHandler
									.getAllLang("UAE");
						} catch (NullPointerException e) {
						} catch (Exception e) {
						}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// hideLoader();
							}
						});
					}
				}).start();
			}
		} catch (Exception ex) {
		}

		btnSkip = (Button) findViewById(R.id.btnSkip);
		btnSkip.setOnClickListener(this);

		flipper = (ViewFlipper) this.findViewById(R.id.flipper1);
		flipper.setEnabled(true);
		flipper.setClickable(true);
		flipper.setFocusable(true);
		flipper.setOnTouchListener(this);

		flipper.setAutoStart(true);
		flipper.setFlipInterval(3000);
		flipper.setInAnimation(AnimationUtils.loadAnimation(Localsecrets.this,
				R.anim.left_in));

		mDotsLayout = (LinearLayout) findViewById(R.id.image_count);
		mDotsCount = flipper.getChildCount();
		mDotsText = new TextView[mDotsCount];
		// here we set the dots
		System.out.println("count" + mDotsCount + flipper.getChildCount());
		for (int i = 0; i < mDotsCount; i++) {

			mDotsText[i] = new TextView(this);
			mDotsText[i].setText(".");
			mDotsText[i].setTextSize(45);
			mDotsText[i].setTypeface(null, Typeface.BOLD);
			mDotsText[i].setTextColor(android.graphics.Color.GRAY);
			mDotsLayout.addView(mDotsText[i]);
			mDotsText[i].setId(i);

			mDotsText[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					flipper.setDisplayedChild(v.getId());

					for (int i = 0; i < mDotsCount; i++) {
						mDotsText[i].setTextColor(Color.GRAY);
					}

					mDotsText[v.getId()].setTextColor(Color.WHITE);
					// gallery.setSelection(v.getId());
				}
			});

			mDotsText[flipper.getDisplayedChild()].setTextColor(Color.WHITE);
		}
		gps = new GPSTracker(Localsecrets.this) {
		};
		if (gps.canGetLocation()) {
			System.out.println("lat long" + gps.getLatitude()
					+ gps.getLongitude());
			System.out.println("od details are" + Appconstants.LATTITUDE
					+ Appconstants.LANGITUDE + Appconstants.strCityId
					+ Appconstants.strCityName);

			lat = gps.getLatitude();
			lng = gps.getLongitude();

		}
		if (ConnectivityReceiver.checkInternetConnection(Localsecrets.this)) {
			if (network_enabled) {
				try {
					// getLocationByCityName(loc);
					getLocationName();

				} catch (NullPointerException e) {
				} catch (Exception e) {
				}
			}
		} else {
			ConnectivityReceiver.showCustomDialog(Localsecrets.this);
		}

		autoDotsSelection();
	}

	int i = 0;

	private void autoDotsSelection() {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {

					while (i < mDotsCount) {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								for (int j = 0; j < mDotsCount; j++) {
									mDotsText[j].setTextColor(Color.GRAY);
								}

								mDotsText[i].setTextColor(Color.WHITE);
							}
						});

						try {
							Thread.sleep(3000);
							i++;
							if (i == mDotsCount) {
								i = 0;
							}
						} catch (InterruptedException e) {
						}

					}
				}
			}
		});
		thread.start();
	}

	private void createDatabase() {

		dHandler = new DBHandler(this);
		db = dHandler.getWritableDatabase();
		db.close();
	}

	class gettingCities extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			showLoader();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			String url = Appconstants.MAIN_HOST + "cities/"

			;
			System.out.println("url is" + url);
			String responce = new WebServiceCalls().urlPost(url);

			if (responce != null) {
				Appconstants.cities_do.clear();
				try {
					JSONObject jResponce = new JSONObject(responce);
					JSONArray jArray = jResponce.getJSONArray("India");
					JSONArray jArray1 = jResponce.getJSONArray("UAE");

					for (int i = 0; i < jArray.length(); i++) {

						CitiesDO ctDo = new CitiesDO();
						JSONObject jo = jArray.getJSONObject(i);
						ctDo.id = jo.getString("city_id");
						ctDo.city_name = jo.getString("city_name");
						ctDo.country_name = jo.getString("country_name");
						ctDo.lat = jo.getString("latitude");
						ctDo.lang = jo.getString("longitude");

						Appconstants.cities_do.add(ctDo);

					}
					for (int i = 0; i < jArray1.length(); i++) {

						CitiesDO ctDo = new CitiesDO();
						JSONObject jo = jArray1.getJSONObject(i);
						ctDo.id = jo.getString("city_id");
						ctDo.city_name = jo.getString("city_name");
						ctDo.country_name = jo.getString("country_name");
						ctDo.lat = jo.getString("latitude");
						ctDo.lang = jo.getString("longitude");

						Appconstants.cities_do.add(ctDo);

					}

					dHandler.insertCities(Appconstants.cities_do);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// hideLoader();
			if (ConnectivityReceiver.checkInternetConnection(Localsecrets.this)) {

				new gettingAllLocations().execute();

			} else {
				ConnectivityReceiver.showCustomDialog(Localsecrets.this);
			}

		}

	}

	class gettingAllLocations extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// showLoader();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			String url = Appconstants.MAIN_HOST + "getLocations/"
					+ Appconstants.strCityId + "/" + lat + "/" + lng;
			;
			System.out.println("url is" + Appconstants.MAIN_HOST
					+ "getLocations/" + Appconstants.strCityId + "/" + lat
					+ "/" + lng);
			String responce = new WebServiceCalls().urlPost(url);

			if (responce != null) {
				Appconstants.location_do.clear();
				try {
					JSONObject jResponce = new JSONObject(responce);
					JSONArray jArray = jResponce.getJSONArray("locations");

					for (int i = 0; i < jArray.length(); i++) {

						LocationDO ltDo = new LocationDO();
						JSONObject jo = jArray.getJSONObject(i);
						ltDo.id = jo.getString("id");
						ltDo.name = jo.getString("name");
						if (jo.has("latitude")) {
							if (TextUtils.isEmpty(jo.getString("latitude"))
									|| jo.getString("latitude") == null) {
								ltDo.lat = "17.3700";
							} else {
								ltDo.lat = jo.getString("latitude");
							}
						} else {
							ltDo.lat = "17.3700";
						}
						if (jo.has("longitude")) {
							if (TextUtils.isEmpty(jo.getString("longitude"))
									|| jo.getString("longitude") == null) {
								ltDo.lang = "78.4800";
							} else {
								ltDo.lang = jo.getString("longitude");
							}
						} else {
							ltDo.lang = "78.4800";
						}

						Appconstants.location_do.add(ltDo);

					}

					dHandler.insertLocations(Appconstants.location_do);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			hideLoader();

		}

	}

	// public void getLocationName(final Location location) {
	public void getLocationName() {
		showLoader("Resolving your location.");
		new Thread(new Runnable() {

			@Override
			public void run() {

				Appconstants.ltDo = new LocationDO();

				Geocoder geocoder = new Geocoder(Localsecrets.this, Locale
						.getDefault());

				List<Address> addresses = null;

				try {
					addresses = geocoder.getFromLocation(lat, lng, 1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("lat lng address" + addresses + lat + lng);

				if (addresses != null && addresses.size() > 0) {
					Address address = addresses.get(0);
					Appconstants.ltDo.name = address.getThoroughfare();
					Appconstants.strCityName = address.getLocality();

					String getlocation2 = address.getAddressLine(2);
					String[] find_zipcode = getlocation2.split(" ");
					if (!TextUtils.isEmpty(find_zipcode[2])) {
						Appconstants.strPostalcode = find_zipcode[2];
					}
					Appconstants.strCityName = address.getCountryName();

					System.out.println("postal & country name"
							+ address.getCountryName() + find_zipcode[2]);

					Appconstants.LATTITUDE = String.valueOf("" + lat);
					Appconstants.LANGITUDE = String.valueOf("" + lng);
				}

				String url = Appconstants.MAIN_HOST + "getNearestPlaces/" + lat
						+ "/" + lng;
				System.out.println("url" + url);
				String result = new WebServiceCalls().urlPost(url);

				try {
					JSONObject locationObject = new JSONObject(result);
					if (result.contains("ErrorMessage")) {
						String errmsg = locationObject
								.optString("ErrorMessage");
						BaseActivity.showDialogMsg(getApplicationContext(),
								errmsg);
					} else {

						Appconstants.ltDo.name = locationObject
								.optString("location");
						Appconstants.ltDo.id = locationObject
								.optString("location_id");

						Appconstants.strCityId = locationObject
								.optString("city_id");
						Appconstants.strCityName = locationObject
								.optString("cityname");

						Appconstants.LATTITUDE = String.valueOf("" + lat);
						Appconstants.LANGITUDE = String.valueOf("" + lng);

						System.out.println("city_id"
								+ locationObject.optString("city_id"));

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// etLocation.setText("" +
								// Appconstants.ltDo.name);

								hideLoader();
							}
						});

					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("Exception : ", "" + e);
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						hideLoader();

						System.out.println("citynames"
								+ Appconstants.strCityName
								+ PreferenceUtils.getCityName());
						if (Appconstants.strCityName
								.equalsIgnoreCase(PreferenceUtils.getCityName())) {
							PreferenceUtils
									.setCityName(Appconstants.strCityName);
						} else {
							PreferenceUtils
									.setCityName(Appconstants.strCityName);
							dHandler.LocationTableEmpty();
							dHandler.CountryTableEmpty();
							if (dHandler.isTableExist() == 0) {
								if (ConnectivityReceiver
										.checkInternetConnection(Localsecrets.this)) {

									new gettingCities().execute();

								} else {
									ConnectivityReceiver
											.showCustomDialog(Localsecrets.this);
								}
							}
						}
					}
				});

			}
		}).start();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*
		 * if (lat > 0 && lng > 0) { //new GetLocationIdTask(lat,
		 * lng).execute(); } else {
		 */
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Appconstants.location_array = dHandler
							.getAllLocationsArray();
					Appconstants.locationID_array = dHandler
							.getAllLocationIDs();
					Appconstants.location_lat = dHandler.getAllLatitude();
					Appconstants.location_lang = dHandler.getAllLangitude();

					Appconstants.india_city_id.clear();
					Appconstants.india_city_name.clear();
					Appconstants.india_city_lat.clear();
					Appconstants.india_city_lang.clear();
					Appconstants.uae_city_id.clear();
					Appconstants.uae_city_name.clear();
					Appconstants.uae_city_lat.clear();
					Appconstants.uae_city_lang.clear();

					Appconstants.india_city_id = dHandler.getAllCityId("India");
					Appconstants.india_city_name = dHandler
							.getAllCityNames("India");
					Appconstants.india_city_lat = dHandler.getAllLat("India");
					Appconstants.india_city_lang = dHandler.getAllLang("India");

					Appconstants.uae_city_id = dHandler.getAllCityId("UAE");
					Appconstants.uae_city_name = dHandler
							.getAllCityNames("UAE");
					Appconstants.uae_city_lat = dHandler.getAllLat("UAE");
					Appconstants.uae_city_lang = dHandler.getAllLang("UAE");
				} catch (NullPointerException e) {
				} catch (Exception e) {
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// hideLoader();
					}
				});
			}
		}).start();
		startActivity(new Intent(getApplicationContext(), Launcher.class));
	}

	// }

	class SwipeGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					flipper.setInAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.left_in));
					flipper.setOutAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.left_out));
					// controlling animation
					flipper.getInAnimation().setAnimationListener(
							mAnimationListener);
					flipper.showNext();
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					flipper.setInAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.right_in));
					flipper.setOutAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.right_out));
					// controlling animation
					flipper.getInAnimation().setAnimationListener(
							mAnimationListener);
					flipper.showPrevious();
					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (flipper.isInTouchMode()) {
			return detector.onTouchEvent(event);
		} else {
			return super.onTouchEvent(event);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() >= 0) {
			onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {

		moveTaskToBack(true);
		finish();
		return;
	}
}
