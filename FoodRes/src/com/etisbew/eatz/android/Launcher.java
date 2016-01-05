package com.etisbew.eatz.android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eatz.android.Explore.AccountDetails;
import com.etisbew.eatz.android.dropdownlist.MyDeals;
import com.etisbew.eatz.android.dropdownlist.MyFav;
import com.etisbew.eatz.android.dropdownlist.MyOrders;
import com.etisbew.eatz.android.dropdownlist.MyReservations;
import com.etisbew.eatz.android.dropdownlist.MyReviews;
import com.etisbew.eatz.android.dropdownlist.RedemptionHistory;
import com.etisbew.eatz.bussinesslayer.DBHandler;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.facebook.Facebook;
import com.etisbew.eatz.objects.LocationDO;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;

public class Launcher extends BaseActivity implements OnClickListener {

	private TextView etWhattofind = null, etLocation = null;

	private TextView llOrderFood = null, llDeals = null,
			llOpenNow = null,
			llHappyHours = null,
			llBuffetPlace = null, // llEvents = null,
			llBookTable = null, llMore = null, txtBookTableOnline = null,
			txtExploreOnline = null, txtLoc, txtCity, tvFastlane_2,
			tvFastlane_1, txtOrderFoodOnline = null, knowmore, got_it;

	String strGetOpenMenu;
	static String strErrorMsg = "";

	LinearLayout lyLoc;
	private Button btnLogin = null, btnSignUp = null;
	static String getRestaurent;
	static String userLocation = "";
	static String strDetailsResp, strVenueId;
	static JSONArray jsonArrayRestaurants;
	public static JSONArray accountDetailsArray;
	private QuickAction quickAction;

	public static final int ID_MYACCOUNT = 0;
	public static final int ID_RES = 1;
	public static final int ID_ORDERS = 2;
	public static final int ID_DEALS = 3;
	public static final int ID_REV = 4;
	public static final int ID_REDEMPTION = 5;
	public static final int ID_FAV = 6;
	public static final int ID_LOGOUT = 7;
	public static final int ID_LOGIN = 8;
	SharedPreferences settings;
	Facebook fb;
	RelativeLayout master1, master2, master3, know_more_bg, fastlane,
			fastlane1;
	int flag = 1;
	GPSTracker gps;
	String result1 = "";
	Dialog d;
	String[] mcountries;
	String[] mcities;
	String[] mcityid;
	Integer[] total_images = { R.drawable.india_flag, R.drawable.uae_flag };
	ListView listView1, listView2;
	RelativeLayout relativeLayout1, relativeLayout2;
	ImageView imageview1, imageview2;
	TextView textview1, textview2;
	private DBHandler dHandler = null;
	SQLiteDatabase db;
	PackageInfo pInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity);

		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			PreferenceUtils.setVersionCode(pInfo.versionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		master1 = (RelativeLayout) findViewById(R.id.master_layout01);
		master2 = (RelativeLayout) findViewById(R.id.master_layout02);
		master3 = (RelativeLayout) findViewById(R.id.master_layout03);
		know_more_bg = (RelativeLayout) findViewById(R.id.know_more_bg);
		settings = getSharedPreferences("eatz", 0);
		flag = settings.getInt("fastlane", 1);

		if (Appconstants.user_flag == 2) {
			Appconstants.user_flag = 1;
			Appconstants.sessionId = "";

			Appconstants.userId = "";

			Appconstants.firstname = "";

			Appconstants.phone = "";
			Appconstants.email_ = "";

		}

		gps = new GPSTracker(Launcher.this) {
		};
		if (gps.canGetLocation()) {
			System.out.println("lat long" + gps.getLatitude()
					+ gps.getLongitude());

		}
		System.out.println("citynames" + Appconstants.strCityName
				+ PreferenceUtils.getCityName());
		txtLoc = (TextView) findViewById(R.id.tvLoc);
		txtCity = (TextView) findViewById(R.id.tvCity);
		tvFastlane_2 = (TextView) findViewById(R.id.tvFastlane_2);
		tvFastlane_1 = (TextView) findViewById(R.id.tvFastlane_1);
		lyLoc = (LinearLayout) findViewById(R.id.lyLoc);

		Spanned text = Html.fromHtml("Introducing <b>fastlane</b> pickup");
		tvFastlane_2.setText(text);
		tvFastlane_1.setText(text);
		txtCity.setText(Appconstants.strCityName);

		if (Appconstants.ltDo.name.length() <= 0) {
			if (ConnectivityReceiver.checkInternetConnection(Launcher.this)) {
				getLocationName();
			} else {
				ConnectivityReceiver.showCustomDialog(Launcher.this);
			}

		} else {
			txtLoc.setText(Appconstants.ltDo.name + ",");
		}

		if (Appconstants.strCityName.length() <= 0) {

			if (ConnectivityReceiver.checkInternetConnection(Launcher.this)) {
				getLocationName();
			} else {
				ConnectivityReceiver.showCustomDialog(Launcher.this);
			}

		} else {
			txtCity.setText(Appconstants.strCityName);
		}

		hideKeyboard();

		etWhattofind = (TextView) findViewById(R.id.etWhattoFind);
		etLocation = (TextView) findViewById(R.id.etLocation);
		etLocation.setText("" + Appconstants.ltDo.name);

		llOrderFood = (TextView) findViewById(R.id.tvOrderFood);
		llBookTable = (TextView) findViewById(R.id.tvBookTable);
		llMore = (TextView) findViewById(R.id.tvMore);

		llOpenNow = (TextView) findViewById(R.id.tvOpenNow);
		llHappyHours = (TextView) findViewById(R.id.tvHappyHours);
		llBuffetPlace = (TextView) findViewById(R.id.tvBuffetPlace);

		fastlane = (RelativeLayout) findViewById(R.id.fastlane);
		fastlane1 = (RelativeLayout) findViewById(R.id.fastlane1);

		knowmore = (TextView) findViewById(R.id.knowmore);
		got_it = (TextView) findViewById(R.id.gotit);

		llDeals = (TextView) findViewById(R.id.tvDeals);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnSignUp = (Button) findViewById(R.id.btnSignup);

		txtBookTableOnline = (TextView) findViewById(R.id.txtBookTableOnline);
		txtOrderFoodOnline = (TextView) findViewById(R.id.txtOrderFood);
		txtExploreOnline = (TextView) findViewById(R.id.txtExploreOnline);

		etWhattofind.setOnClickListener(this);
		etLocation.setOnClickListener(this);
		llOrderFood.setOnClickListener(this);
		llBookTable.setOnClickListener(this);
		llMore.setOnClickListener(this);
		llDeals.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnSignUp.setOnClickListener(this);
		txtBookTableOnline.setOnClickListener(this);
		txtOrderFoodOnline.setOnClickListener(this);
		txtExploreOnline.setOnClickListener(this);

		llOpenNow.setOnClickListener(this);
		llHappyHours.setOnClickListener(this);
		llBuffetPlace.setOnClickListener(this);
		fastlane.setOnClickListener(this);
		// llEvents.setOnClickListener(this);
		lyLoc.setOnClickListener(this);
		knowmore.setOnClickListener(this);
		fastlane1.setOnClickListener(this);

		System.out.println("");
		if (flag == 1) {

			llOpenNow.setClickable(false);
			llOrderFood.setClickable(false);
			llBookTable.setClickable(false);
			llDeals.setClickable(false);
			llHappyHours.setClickable(false);
			llBuffetPlace.setClickable(false);
			llMore.setClickable(false);

			etWhattofind.setClickable(false);
			etLocation.setClickable(false);
			btnLogin.setClickable(false);
			btnSignUp.setClickable(false);
			txtBookTableOnline.setClickable(false);
			txtOrderFoodOnline.setClickable(false);
			txtExploreOnline.setClickable(false);
			llOpenNow.setClickable(false);
			llHappyHours.setClickable(false);
			llBuffetPlace.setClickable(false);
			lyLoc.setClickable(false);

		} else if (flag == 2) {
			master1.setBackgroundResource(Color.TRANSPARENT);
			master2.setVisibility(View.VISIBLE);
			master3.setVisibility(View.GONE);
			know_more_bg.setVisibility(View.GONE);
		}

		got_it.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				know_more_bg.setVisibility(View.GONE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("fastlane", 2);
				editor.commit();
				flag = 2;
				SetEnabled();
			}
		});

	}

	public void SetEnabled() {
		if (flag == 1) {
			llOpenNow.setClickable(false);
			llOrderFood.setClickable(false);
			llBookTable.setClickable(false);
			llDeals.setClickable(false);
			llHappyHours.setClickable(false);
			llBuffetPlace.setClickable(false);
			llMore.setClickable(false);
		} else {
			//

			knowmore.setClickable(false);
			master1.setBackgroundResource(Color.TRANSPARENT);
			master2.setVisibility(View.VISIBLE);
			master3.setVisibility(View.GONE);

			llOpenNow.setClickable(true);
			llOrderFood.setClickable(true);
			llBookTable.setClickable(true);
			llDeals.setClickable(true);
			llHappyHours.setClickable(true);
			llBuffetPlace.setClickable(true);
			llMore.setClickable(true);

			etWhattofind.setClickable(true);
			etLocation.setClickable(true);
			btnLogin.setClickable(true);
			btnSignUp.setClickable(true);
			txtBookTableOnline.setClickable(true);
			txtOrderFoodOnline.setClickable(true);
			txtExploreOnline.setClickable(true);
			llOpenNow.setClickable(true);
			llHappyHours.setClickable(true);
			llBuffetPlace.setClickable(true);
			lyLoc.setClickable(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (Appconstants.ltDo.name.length() <= 0) {
			// txtLoc.setText("MGBS,");
		} else {
			txtLoc.setText(Appconstants.ltDo.name + ",");
		}

		if (Appconstants.strCityName.length() <= 0) {
			// txtCity.setText("Hyderabad");
		} else {
			txtCity.setText(Appconstants.strCityName);
		}

		if (!PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
			btnLogin.setText(PreferenceUtils.getUserName1());
			// btnLogin.setBackgroundResource(R.drawable.signup_background);
			// btnLogin.setTextColor(Color.WHITE);
			btnSignUp.setVisibility(View.INVISIBLE);
			txtLoc.setVisibility(View.VISIBLE);
			txtCity.setVisibility(View.VISIBLE);

		} else {
			btnLogin.setText("Login");
			// btnLogin.setTextColor(Color.WHITE);

			btnSignUp.setVisibility(View.VISIBLE);
		}

		quickAction = new QuickAction(this, QuickAction.VERTICAL);
		if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
			quickAction.addActionItem(new ActionItem(Explore.ID_LOGIN, "Login",
					getResources().getDrawable(R.drawable.iocn_myaccount)));
		} else {
			quickAction.addActionItem(new ActionItem(ID_MYACCOUNT,
					"My Account", getResources().getDrawable(
							R.drawable.my_account)));
			quickAction.addActionItem(new ActionItem(ID_RES,
					" My Reservations", getResources().getDrawable(
							R.drawable.my_reservations)));
			quickAction.addActionItem(new ActionItem(ID_ORDERS, " My Orders",
					getResources().getDrawable(R.drawable.my_orders)));
			quickAction.addActionItem(new ActionItem(ID_DEALS, " My Deals",
					getResources().getDrawable(R.drawable.my_deals)));
			quickAction.addActionItem(new ActionItem(ID_REV, "My Reviews",
					getResources().getDrawable(R.drawable.my_reviews)));
			quickAction.addActionItem(new ActionItem(ID_REDEMPTION,
					" Redemption History", getResources().getDrawable(
							R.drawable.redemption_history)));
			quickAction.addActionItem(new ActionItem(ID_FAV, " My Favourites",
					getResources().getDrawable(R.drawable.favs)));
			quickAction.addActionItem(new ActionItem(ID_LOGOUT, "Logout",
					getResources().getDrawable(R.drawable.my_logout)));
		}
		// Set listener for action item clicked
		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {

						quickAction.dismiss();
						if (actionId == ID_LOGOUT) {
							PreferenceUtils.removeUserName();
							startActivity(new Intent(Launcher.this, Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

						} else if (actionId == ID_MYACCOUNT) {
							if (ConnectivityReceiver
									.checkInternetConnection(Launcher.this)) {
								new AccountDetails(Launcher.this).execute();
							} else {
								ConnectivityReceiver
										.showCustomDialog(Launcher.this);
							}
						} else if (actionId == ID_RES) {
							startActivityForResult(new Intent(Launcher.this,
									MyReservations.class), 1);
						} else if (actionId == ID_ORDERS) {
							startActivityForResult(new Intent(Launcher.this,
									MyOrders.class), 1);
						} else if (actionId == ID_DEALS) {
							startActivityForResult(new Intent(Launcher.this,
									MyDeals.class), 1);
						} else if (actionId == ID_REV) {
							startActivityForResult(new Intent(Launcher.this,
									MyReviews.class), 1);
						} else if (actionId == ID_REDEMPTION) {
							startActivityForResult(new Intent(Launcher.this,
									RedemptionHistory.class), 1);
						} else if (actionId == ID_FAV) {
							startActivityForResult(new Intent(Launcher.this,
									MyFav.class), 1);
						} else if (actionId == ID_LOGIN) {
							startActivity(new Intent(Launcher.this, Login.class));
						}

					}
				});

		// actions
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {

			}
		});

	}

	TextView textName;

	class IndiaAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Appconstants.india_city_name.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		@SuppressLint("ViewHolder")
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row;
			row = inflater.inflate(R.layout.cityname_match, parent, false);
			TextView title;
			ImageView i1;
			title = (TextView) row.findViewById(R.id.textView1);
			title.setText("" + Appconstants.india_city_name.get(position));
			i1 = (ImageView) row.findViewById(R.id.imageView1);
			try {
				if (Appconstants.india_city_name.get(position)
						.equalsIgnoreCase(Appconstants.strCityName)) {
					i1.setVisibility(View.VISIBLE);
				} else {
					i1.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				i1.setVisibility(View.GONE);
			}

			return (row);
		}

	}

	class UaeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Appconstants.uae_city_name.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		@SuppressLint("ViewHolder")
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row;
			row = inflater.inflate(R.layout.cityname_match, parent, false);
			TextView title;
			ImageView i1;
			title = (TextView) row.findViewById(R.id.textView1);
			title.setText("" + Appconstants.uae_city_name.get(position));
			i1 = (ImageView) row.findViewById(R.id.imageView1);
			try {
				if (Appconstants.uae_city_name.get(position).equalsIgnoreCase(
						Appconstants.strCityName)) {
					i1.setVisibility(View.VISIBLE);
				} else {
					i1.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				i1.setVisibility(View.GONE);
			}

			return (row);
		}

	}

	public void getLocationName() {
		showLoader("Resolving your location.");
		new Thread(new Runnable() {

			@Override
			public void run() {

				Appconstants.ltDo = new LocationDO();

				String url = Appconstants.MAIN_HOST + "getNearestPlaces/"
						+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE;
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

						PreferenceUtils.setCityName(Appconstants.strCityName);

						System.out.println("city_id"
								+ locationObject.optString("city_id")
								+ PreferenceUtils.getCityName());

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

						if (dHandler.LocationTableEmpty() == 0) {
							if (ConnectivityReceiver
									.checkInternetConnection(Launcher.this)) {

								new gettingAllLocations().execute();

							} else {
								ConnectivityReceiver
										.showCustomDialog(Launcher.this);
							}

						}

					}
				});

			}
		}).start();

	}

	class gettingAllLocations extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			showLoader();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			String url = Appconstants.MAIN_HOST + "getLocations/"
					+ Appconstants.strCityId + "/" + Appconstants.LATTITUDE
					+ "/" + Appconstants.LANGITUDE;
			;
			System.out.println("url is" + Appconstants.MAIN_HOST
					+ "getLocations/" + Appconstants.strCityId + "/"
					+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE);
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

			Appconstants.location_array.clear();
			Appconstants.location_lat.clear();
			Appconstants.location_lang.clear();
			Appconstants.locationID_array.clear();

			Appconstants.location_array = dHandler.getAllLocationsArray();
			Appconstants.location_lat = dHandler.getAllLatitude();
			Appconstants.location_lang = dHandler.getAllLangitude();
			Appconstants.locationID_array = dHandler.getAllLocationIDs();
			hideLoader();
			Launcher.this.finish();
			startActivity(new Intent(Launcher.this, Launcher.class));

		}

	}

	@Override
	public void onClick(final View v) {

		switch (v.getId()) {
		case R.id.etWhattoFind:
			Appconstants.strMenuflag = "Explore";
			startActivity(new Intent(Launcher.this, Search.class));
			break;
		case R.id.knowmore:
			startActivity(new Intent(Launcher.this, KnowMore.class));
			break;
		case R.id.etLocation:
			Appconstants.strMenuflag = "Explore";
			startActivity(new Intent(Launcher.this, Search.class));
			break;

		case R.id.lyLoc:

			d = new Dialog(Launcher.this);
			d.requestWindowFeature(Window.FEATURE_NO_TITLE);
			//
			// d.setTitle("My Reservation");

			d.setContentView(R.layout.county_dialog);

			relativeLayout1 = (RelativeLayout) d
					.findViewById(R.id.relativeLayout1);
			relativeLayout2 = (RelativeLayout) d
					.findViewById(R.id.relativeLayout2);

			relativeLayout1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					listView1.setVisibility(View.VISIBLE);
				}
			});
			relativeLayout2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					listView2.setVisibility(View.VISIBLE);
				}
			});

			listView1 = (ListView) d.findViewById(R.id.listView1);
			listView2 = (ListView) d.findViewById(R.id.listView2);

			imageview1 = (ImageView) d.findViewById(R.id.imageView1);
			imageview2 = (ImageView) d.findViewById(R.id.imageView2);

			textview1 = (TextView) d.findViewById(R.id.textView1);
			textview2 = (TextView) d.findViewById(R.id.textView3);

			/*
			 * textview1.setText(Appconstants.strCountryName);
			 * textview2.setText(Appconstants.strCityName);
			 */
			System.out.println("values are" + Appconstants.india_city_name
					+ Appconstants.uae_city_name);
			listView1.setAdapter(new IndiaAdapter());
			listView2.setAdapter(new UaeAdapter());
			listView1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					dHandler = new DBHandler(Launcher.this);
					db = dHandler.getWritableDatabase();
					Appconstants.strCityId = Appconstants.india_city_id
							.get(position);
					Appconstants.strCityName = Appconstants.india_city_name
							.get(position);
					Appconstants.LATTITUDE = Appconstants.india_city_lat
							.get(position);
					Appconstants.LANGITUDE = Appconstants.india_city_lang
							.get(position);

					getLocationName();
				}
			});
			listView2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					dHandler = new DBHandler(Launcher.this);
					db = dHandler.getWritableDatabase();
					Appconstants.strCityId = Appconstants.uae_city_id
							.get(position);
					Appconstants.strCityName = Appconstants.uae_city_name
							.get(position);
					Appconstants.LATTITUDE = Appconstants.uae_city_lat
							.get(position);
					Appconstants.LANGITUDE = Appconstants.uae_city_lang
							.get(position);

					getLocationName();
				}
			});

			d.show();

			break;

		case R.id.tvOrderFood:
			userLocation = Appconstants.ltDo.name;
			Appconstants.strMenuflag = "OrderFood";
			startActivity(new Intent(getApplicationContext(), OrderList.class));

			break;

		case R.id.txtOrderFood:
			userLocation = Appconstants.ltDo.name;
			Appconstants.strMenuflag = "OrderFood";
			startActivity(new Intent(getApplicationContext(), OrderList.class));

			break;

		case R.id.txtExploreOnline:
			Appconstants.strMenuflag = "Explore";
			Localsecrets.str_titles = "Restaurants";
			// String s4 = "";
			try {
				userLocation = Appconstants.ltDo.name;

				// if (Appconstants.ltDo.id.length() > 0) {
				// s4 = Appconstants.ltDo.id;
				// } else {
				// s4 = "null";
				//
				// }
				Explore.type = 0;
				Explore.id1 = 2;
				Launcher.getRestaurent = Appconstants.MAIN_HOST
						+ "restaurantslisting/0/" + Appconstants.strCityId
						+ "/" + Appconstants.LATTITUDE + "/"
						+ Appconstants.LANGITUDE + "/1";

				/*
				 * getRestaurent = Appconstants.MAIN_HOST + "globalsearchmenu/"
				 * + Appconstants.strCityId + "/" + s4 + "/" +
				 * Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE +
				 * "/0?arg=1&searchpage=1&sort_by=distance";
				 */

			} catch (NullPointerException e) {
				Explore.type = 0;
				Explore.id1 = 2;
				Launcher.getRestaurent = Appconstants.MAIN_HOST
						+ "restaurantslisting/0/" + Appconstants.strCityId
						+ "/" + Appconstants.LATTITUDE + "/"
						+ Appconstants.LANGITUDE + "/1";
				/*
				 * getRestaurent = Appconstants.MAIN_HOST + "globalsearchmenu/"
				 * + Appconstants.strCityId + "/" + s4 + "/" +
				 * Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE +
				 * "/0?arg=1&searchpage=1&sort_by=distance";
				 */

			}

			if (ConnectivityReceiver.checkInternetConnection(Launcher.this)) {

				new RestaurantsList(v.getContext(), getRestaurent).execute();

			} else {
				ConnectivityReceiver.showCustomDialog(Launcher.this);
			}
			break;

		case R.id.tvDeals:
			Appconstants.strMenuflag = "Deals";
			userLocation = Appconstants.ltDo.name;
			// getting all Deals.
			// final String dealsUrl = Appconstants.MAIN_HOST +
			// "restaurantDeals";
			final String dealsUrl = Appconstants.MAIN_HOST
					+ "restaurantslisting/15/" + Appconstants.strCityId + "/"
					+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE;

			showLoader();
			new Thread(new Runnable() {

				@Override
				public void run() {

					if (ConnectivityReceiver
							.checkInternetConnection(Launcher.this)) {

						result1 = new WebServiceCalls().urlPost(dealsUrl);

					} else {
						ConnectivityReceiver.showCustomDialog(Launcher.this);
					}
					/*
					 * final String result = new WebServiceCalls()
					 * .urlPost(dealsUrl);
					 */

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							hideLoader();

							if (result1 != null) {
								Intent inDeals = new Intent(v.getContext(),
										Deals.class)
										.putExtra("result", result1);
								startActivity(inDeals);
								// hideLoader();
							}

						}
					});

				}
			}).start();

			break;

		case R.id.btnSignup:
			Appconstants.strMenuflag = "Explore";
			startActivity(new Intent(v.getContext(), SignUp.class));
			break;
		case R.id.btnLogin:
			// Toast.makeText(getApplicationContext(), "clicked", 5).show();

			Appconstants.strMenuflag = "Explore";
			if (!PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				quickAction.show(v);
				return;
			}

			Intent in = new Intent(Launcher.this, Login.class);
			in.putExtra("guest_flag", "1");
			startActivity(in);
			break;

		case R.id.tvOpenNow:
			Appconstants.strMenuflag = "Opennow";
			userLocation = Appconstants.ltDo.name;

			startActivity(new Intent(getApplicationContext(), OpenNow.class));

			break;

		case R.id.tvHappyHours:
			Appconstants.strMenuflag = "HappyHrs";
			Localsecrets.str_titles = "Happy hours";
			// String s = "";
			try {
				userLocation = Appconstants.ltDo.name;

				// if (Appconstants.ltDo.id.length() > 0) {
				// s = Appconstants.ltDo.id;
				// } else {
				// s = "null";
				//
				// }

				/*
				 * getRestaurent = Appconstants.MAIN_HOST + "globalsearchmenu/"
				 * + Appconstants.strCityId + "/" + s + "/" +
				 * Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE +
				 * "/6?arg=1&searchpage=1&sort_by=distance";
				 */
				Explore.type = 17;
				Explore.id1 = 2;
				Launcher.getRestaurent = Appconstants.MAIN_HOST
						+ "restaurantslisting/17/" + Appconstants.strCityId
						+ "/" + Appconstants.LATTITUDE + "/"
						+ Appconstants.LANGITUDE + "/1";

			} catch (NullPointerException e) {

				Explore.type = 17;
				Explore.id1 = 2;
				Launcher.getRestaurent = Appconstants.MAIN_HOST
						+ "restaurantslisting/17/" + Appconstants.strCityId
						+ "/" + Appconstants.LATTITUDE + "/"
						+ Appconstants.LANGITUDE + "/1";

			}

			if (ConnectivityReceiver.checkInternetConnection(Launcher.this)) {

				new RestaurantsList(v.getContext(), getRestaurent).execute();

			} else {
				ConnectivityReceiver.showCustomDialog(Launcher.this);
			}

			break;

		case R.id.tvBuffetPlace:
			Appconstants.strMenuflag = "BuffetPlace";
			Localsecrets.str_titles = "Buffet Restaurants";
			// String s3 = "";
			try {
				userLocation = Appconstants.ltDo.name;

				// if (Appconstants.ltDo.id.length() > 0) {
				// s3 = Appconstants.ltDo.id;
				// } else {
				// s3 = "null";
				//
				// }

				try {

					/*
					 * Launcher.getRestaurent = Appconstants.MAIN_HOST +
					 * "morelinks/" + Appconstants.LATTITUDE + "/" +
					 * Appconstants.LANGITUDE + "/" + Appconstants.strCityId +
					 * "/4/1";
					 */
					Explore.type = 18;
					Explore.id1 = 2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/18/" + Appconstants.strCityId
							+ "/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/1";

				} catch (NullPointerException e) {

					/*
					 * Launcher.getRestaurent = Appconstants.MAIN_HOST +
					 * "morelinks/" + Appconstants.LATTITUDE + "/" +
					 * Appconstants.LANGITUDE + "/" + Appconstants.strCityId +
					 * "/4/1";
					 */
					Explore.type = 18;
					Explore.id1 = 2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/18/" + Appconstants.strCityId
							+ "/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/1";
				}

			} catch (NullPointerException e) {

				/*
				 * Launcher.getRestaurent = Appconstants.MAIN_HOST +
				 * "globalsearchmenu/" + Appconstants.strCityId + "/" + s3 + "/"
				 * + Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE +
				 * "/10";
				 */
				Explore.type = 18;
				Explore.id1 = 2;
				Launcher.getRestaurent = Appconstants.MAIN_HOST
						+ "restaurantslisting/18/" + Appconstants.strCityId
						+ "/" + Appconstants.LATTITUDE + "/"
						+ Appconstants.LANGITUDE + "/1";

			}
			if (ConnectivityReceiver.checkInternetConnection(Launcher.this)) {

				new RestaurantsList(v.getContext(), getRestaurent).execute();

			} else {
				ConnectivityReceiver.showCustomDialog(Launcher.this);
			}
			// new RestaurantsList(v.getContext(), getRestaurent).execute();

			break;

		case R.id.tvBookTable:
			Appconstants.strMenuflag = "BookTable";
			Appconstants.filters_flg = "3";
			// String s1 = "";
			try {
				userLocation = Appconstants.ltDo.name;
				Localsecrets.str_titles = "Restaurants";

				// if (Appconstants.ltDo.id.length() > 0) {
				// s1 = Appconstants.ltDo.id;
				// } else {
				// s1 = "null";
				//
				// }
				// http: //
				// www.eatz.com/WebServices/restaurantslisting/5/22960/17.4009551/78.4767212/3
				/*
				 * getRestaurent = Appconstants.MAIN_HOST +
				 * "restaurantslisting/5/" + s1 + "/null" + "/" +
				 * Appconstants.strCityId + "/null/" + Appconstants.LATTITUDE +
				 * "/" + Appconstants.LANGITUDE;
				 */
				Explore.type = 4;
				Explore.id1 = 2;
				getRestaurent = Appconstants.MAIN_HOST
						+ "restaurantslisting/4/" + Appconstants.strCityId
						+ "/" + Appconstants.LATTITUDE + "/"
						+ Appconstants.LANGITUDE + "/1";

			} catch (NullPointerException e) {

				/*
				 * getRestaurent = Appconstants.MAIN_HOST +
				 * "restaurantslisting/5/" + s1 + "/null" + "/" +
				 * Appconstants.strCityId + "/null/" + Appconstants.LATTITUDE +
				 * "/" + Appconstants.LANGITUDE;
				 */
				Explore.type = 4;
				Explore.id1 = 2;
				getRestaurent = Appconstants.MAIN_HOST
						+ "restaurantslisting/4/" + Appconstants.strCityId
						+ "/" + Appconstants.LATTITUDE + "/"
						+ Appconstants.LANGITUDE + "/1";

			}

			System.out.println("url" + getRestaurent);
			if (ConnectivityReceiver.checkInternetConnection(Launcher.this)) {

				new RestaurantsList(v.getContext(), getRestaurent).execute();

			} else {
				ConnectivityReceiver.showCustomDialog(Launcher.this);
			}
			// new RestaurantsList(v.getContext(), getRestaurent).execute();

			break;
		case R.id.fastlane:

			startActivity(new Intent(Launcher.this, KnowMore.class));
			Appconstants.strMenuflag = "FastLane";
			/*
			 * Localsecrets.flg = 2; Appconstants.strMenuflag = "FastLane";
			 * String s5 = ""; try { userLocation = Appconstants.ltDo.name;
			 * Localsecrets.str_titles = "FastLane Restaurants";
			 * 
			 * if (Appconstants.ltDo.id.length() > 0) { s5 =
			 * Appconstants.ltDo.id; } else { s5 = "null";
			 * 
			 * } Explore.type = 3; Explore.id1 = 2; getRestaurent =
			 * Appconstants.MAIN_HOST + "restaurantslisting/3/" +
			 * Appconstants.strCityId + "/" + Appconstants.LATTITUDE + "/" +
			 * Appconstants.LANGITUDE + "/1";
			 * 
			 * } catch (NullPointerException e) {
			 * 
			 * Explore.type = 3; Explore.id1 = 2; getRestaurent =
			 * Appconstants.MAIN_HOST + "restaurantslisting/3/" +
			 * Appconstants.strCityId + "/" + Appconstants.LATTITUDE + "/" +
			 * Appconstants.LANGITUDE + "/1";
			 * 
			 * }
			 * 
			 * System.out.println("url" + getRestaurent); new
			 * getRestaurantsList(v.getContext(), getRestaurent).execute();
			 */

			break;
		case R.id.fastlane1:
			startActivity(new Intent(Launcher.this, KnowMore.class));
			Appconstants.strMenuflag = "FastLane";
			/*
			 * Localsecrets.flg = 2; Appconstants.strMenuflag = "FastLane";
			 * String s6 = ""; try { userLocation = Appconstants.ltDo.name;
			 * Localsecrets.str_titles = "FastLane Restaurants";
			 * 
			 * if (Appconstants.ltDo.id.length() > 0) { s6 =
			 * Appconstants.ltDo.id; } else { s6 = "null";
			 * 
			 * } Explore.type = 3; Explore.id1 = 2; getRestaurent =
			 * Appconstants.MAIN_HOST + "restaurantslisting/3/" +
			 * Appconstants.strCityId + "/" + Appconstants.LATTITUDE + "/" +
			 * Appconstants.LANGITUDE + "/1";
			 * 
			 * } catch (NullPointerException e) {
			 * 
			 * Explore.type = 3; Explore.id1 = 2; getRestaurent =
			 * Appconstants.MAIN_HOST + "restaurantslisting/3/" +
			 * Appconstants.strCityId + "/" + Appconstants.LATTITUDE + "/" +
			 * Appconstants.LANGITUDE + "/1";
			 * 
			 * }
			 * 
			 * System.out.println("url" + getRestaurent); new
			 * getRestaurantsList(v.getContext(), getRestaurent).execute();
			 */
			break;
		case R.id.txtBookTableOnline:
			Appconstants.strMenuflag = "BookTable";
			// String s2 = "";
			try {
				userLocation = Appconstants.ltDo.name;
				Localsecrets.str_titles = "Restaurants";

				// if (Appconstants.ltDo.id.length() > 0) {
				// s2 = Appconstants.ltDo.id;
				// } else {
				// s2 = "null";
				//
				// }
				Explore.type = 4;
				Explore.id1 = 2;
				getRestaurent = Appconstants.MAIN_HOST
						+ "restaurantslisting/4/" + Appconstants.strCityId
						+ "/" + Appconstants.LATTITUDE + "/"
						+ Appconstants.LANGITUDE + "/1";

				/*
				 * getRestaurent = Appconstants.MAIN_HOST +
				 * "restaurantslisting/table-booking/" + s2 + "/null" + "/" +
				 * Appconstants.strCityId + "/null/" + Appconstants.LATTITUDE +
				 * "/" + Appconstants.LANGITUDE;
				 */

			} catch (NullPointerException e) {
				Explore.type = 4;
				Explore.id1 = 2;
				getRestaurent = Appconstants.MAIN_HOST
						+ "restaurantslisting/4/" + Appconstants.strCityId
						+ "/" + Appconstants.LATTITUDE + "/"
						+ Appconstants.LANGITUDE + "/1";
				/*
				 * getRestaurent = Appconstants.MAIN_HOST +
				 * "restaurantslisting/table-booking/" + s2 + "/null" + "/" +
				 * Appconstants.strCityId + "/null/" + Appconstants.LATTITUDE +
				 * "/" + Appconstants.LANGITUDE;
				 */

			}
			if (ConnectivityReceiver.checkInternetConnection(Launcher.this)) {

				new RestaurantsList(v.getContext(), getRestaurent).execute();

			} else {
				ConnectivityReceiver.showCustomDialog(Launcher.this);
			}
			// new RestaurantsList(v.getContext(), getRestaurent).execute();

			break;

		case R.id.tvMore:
			userLocation = Appconstants.ltDo.name;
			Appconstants.strMenuflag = "More";
			startActivityForResult(new Intent(Launcher.this, More.class), 0);
			break;

		default:
			Appconstants.strMenuflag = "Explore";
			break;
		}
	}

	public class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.custom_spinner, parent, false);

			TextView label = (TextView) row.findViewById(R.id.textView1);
			label.setText(mcountries[position]);

			// TextView sub=(TextView)row.findViewById(R.id.textView2);
			// sub.setText(mydata2[position]);

			ImageView icon = (ImageView) row.findViewById(R.id.imageView1);
			icon.setImageResource(total_images[position]);

			return row;
		}

	}

	String results = "";

	static JSONObject restJsonDetails;
	public static JSONObject restOrderDetails;
	static JSONArray features = null;
	static JSONArray locations_list = null;
	static JSONArray existing_cusines = null;
	static String pricelist = "0,0";

	public Handler mainHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			if (msg.what == 5) {

				startActivity(new Intent(Launcher.this, Explore.class));
				hideLoader();

			} else {

			}
		}
	};

	class LoadEventsTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			showLoader();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			hideLoader();
			startActivity(new Intent(getApplicationContext(), HaveFun.class));
		}

	}

	static String strTitleFlag = "0";

	/*
	 * public class getRestaurantsList extends AsyncTask<String, Void, String> {
	 * 
	 * ProgressDialog pDialog; Context context; String strUrl;
	 * 
	 * public getRestaurantsList(Context con, String strUrl1) { // TODO
	 * Auto-generated constructor stub
	 * 
	 * context = con; strUrl = strUrl1; }
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute();
	 * 
	 * pDialog = new ProgressDialog(context);
	 * pDialog.setMessage("fetching results..."); pDialog.setCancelable(false);
	 * pDialog.show(); }
	 * 
	 * @Override protected String doInBackground(String... params) { String
	 * result = ""; Appconstants.staticURL = strUrl; //
	 * if(TextUtils.isEmpty(Search.selectedLocationId)){ result =
	 * WebServiceCalls.getJSONString(strUrl); System.out.println("if block");
	 * 
	 * }else{ List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
	 * 2); nameValuePairs.add(new BasicNameValuePair("location",
	 * Search.selectedLocationId));
	 * result=WebServiceCalls.getJSONString1(strUrl,nameValuePairs);
	 * System.out.println("else block"+result); }
	 * 
	 * return result;
	 * 
	 * }
	 * 
	 * @Override protected void onPostExecute(String result) {
	 * super.onPostExecute(result); System.out.println("restaurent result" +
	 * result);
	 * 
	 * 
	 * try { File myFile = new File("/sdcard/serverErrorFile.txt");
	 * myFile.createNewFile(); FileOutputStream fOut = new
	 * FileOutputStream(myFile); OutputStreamWriter myOutWriter = new
	 * OutputStreamWriter(fOut); myOutWriter.append(result);
	 * myOutWriter.close(); } catch (Exception e) { // TODO: handle exception }
	 * 
	 * 
	 * 
	 * if (null == result || result.length() == 0) { } else {
	 * 
	 * if (result.contains("errormsg")) {
	 * 
	 * try { strErrorMsg = new JSONObject(result) .optString("errormsg");
	 * Launcher.userLocation = "Hyderabad";
	 * 
	 * restJsonDetails = new JSONObject(result); Explore.map_flag=1; ((Activity)
	 * context).startActivityForResult(new Intent( context, Explore.class), 0);
	 * } catch (Exception e) { e.printStackTrace(); } } else { try {
	 * restJsonDetails = new JSONObject(result); Explore.map_flag=1;
	 * //((Activity) context).finish(); ((Activity)
	 * context).startActivityForResult(new Intent( context, Explore.class), 0);
	 * if (null != pDialog && pDialog.isShowing()) { pDialog.dismiss(); }
	 * 
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } }
	 * 
	 * }
	 * 
	 * } }
	 */

	public void showDialogMsgs(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(Launcher.this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
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
