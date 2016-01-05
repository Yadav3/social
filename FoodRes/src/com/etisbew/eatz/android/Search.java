
package com.etisbew.eatz.android;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.etisbew.eatz.bussinesslayer.DBHandler;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.objects.LocationDO;
import com.etisbew.eatz.objects.reviewDO;
import com.etisbew.eatz.web.WebServiceCalls;

@SuppressLint({ "ClickableViewAccessibility", "ViewHolder", "InflateParams" })
public class Search extends BaseActivity {

	private ImageView ivBack = null;
	private EditText etWhattoFind = null, etLocation = null;
	private ArrayList<LocationDO> commonArray = null;

	private ListView lvResults, lvLocation;
	private SearchAdapter adapter;
	private LocationAdapter lAdp;
	public static String selectedLocationId = "null", 
			selectedLocationName = "null";
	DBHandler dHandler;
	TextView txtGo;
	ScrollView staticIconsScroll;

	TextView txtBookTableIcon, txtOrderTkeAwayIcon, txtHomeDelIcon,
			txtFindDealsIcon, txtOpenNowIcon, txtHappyHrsIcon, txtStarIcon,
			txtRestaurantsIcon, txtDrinksIcon, txtServingIcon, txtSundayIcon,txtFindFastlaneIcon,
			txtSuggest, txtRelated;
	LinearLayout lay;
	HttpResponse response;
	ListView relate_lit, lvsuggested;
	ArrayList<Events1> brands_data = new ArrayList<Events1>();
	ArrayList<Events1> brands_data1 = new ArrayList<Events1>();

	ArrayList<String> outlet = new ArrayList<String>();
	ArrayList<String> name = new ArrayList<String>();
	ArrayList<String> ser = new ArrayList<String>();
	ArrayList<String> layout_view = new ArrayList<String>();
	ArrayList<String> layout_view1 = new ArrayList<String>();
	ArrayList<String> loc_name_array = new ArrayList<String>();
	ArrayList<String> outlet1 = new ArrayList<String>();
	ArrayList<String> name1 = new ArrayList<String>();
	ArrayList<String> ser1 = new ArrayList<String>();
	AttractionsAdapter adapterP;
	AttractionsAdapter1 adapterP1;
	String url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(tp);
		}
		hideKeyboard();

		dHandler = new DBHandler(this);

		try {
			selectedLocationId = Appconstants.ltDo.id;
			selectedLocationName = Appconstants.ltDo.name;

		} catch (NullPointerException e) {
		}

		lvsuggested = (ListView) findViewById(R.id.suggested_listView1);
		relate_lit = (ListView) findViewById(R.id.related_listView);
		lay = (LinearLayout) findViewById(R.id.rlt_sut_lay);

		lvsuggested.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Localsecrets.search_details_flag = 1;
				if (layout_view.get(position).equalsIgnoreCase("0")) {
					Localsecrets.det_fl = ser.get(position);
					//in.putExtra("result",result);
					Localsecrets.str_titles = name.get(position);
					new DeatilsTasks(ser.get(position)).execute(); 
					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new DeatilsTasks(ser.get(position)).execute(); 

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
					/*Intent intent = new Intent(getApplicationContext(),
							SearchDetails.class);
					startActivity(intent);*/

				} else {
					if (!TextUtils.isEmpty(etLocation.getText().toString() 
							.trim())) {
						Localsecrets.str_titles = name.get(position); 
						Launcher.getRestaurent = ser.get(position);

						if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

							new RestaurantsList(Search.this,
								Launcher.getRestaurent.replace(" ", "%20"))									.execute();

						} else {
							ConnectivityReceiver.showCustomDialog(Search.this);
						}
						
					} else {
						BaseActivity.showDialogMsg(Search.this,
								"Please select location");
					}
				}
			}
		});
		
		
		relate_lit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Localsecrets.search_details_flag = 1;
				if (layout_view1.get(position).equalsIgnoreCase("0")) {
					Localsecrets.str_titles = name1.get(position);
					Localsecrets.det_fl = ser1.get(position);
					startActivity(new Intent(getApplicationContext(),
							SearchDetails.class));

				} else {
					if (!TextUtils.isEmpty(etLocation.getText().toString()
							.trim())) {
						Localsecrets.str_titles = name1.get(position);
						Launcher.getRestaurent = ser1.get(position);
						Launcher.userLocation = Appconstants.ltDo.name;
//						new RestaurantsList(Search.this,
//								Launcher.getRestaurent.replace(" ", "%20"))
//								.execute();
						if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

							new RestaurantsList(Search.this,
									Launcher.getRestaurent.replace(" ", "%20"))
									.execute();

						} else {
							ConnectivityReceiver.showCustomDialog(Search.this);
						}
					} else {
						BaseActivity.showDialogMsg(Search.this,
								"Please select location");
					}
				}
			}
		});

		commonArray = new ArrayList<LocationDO>();

		ivBack = (ImageView) findViewById(R.id.back);
		etWhattoFind = (EditText) findViewById(R.id.etFind);

		txtGo = (TextView) findViewById(R.id.txtGo);
		txtGo.setOnClickListener(new OnClickListener() { 

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!TextUtils.isEmpty(etLocation.getText().toString().trim())) {
					if (!TextUtils.isEmpty(etWhattoFind.getText().toString()
							.trim())) {
						try {
							Launcher.userLocation = Appconstants.ltDo.name;
//http://dev.eatz.com/WebServices/restaurantslisting/20/22960/17.4010131/78.4767408/1?search_name=chicken
							Explore.type=20;
							Explore.id1=2;
							Appconstants.filters_flg="120";
							Explore.search=etWhattoFind.getText().toString()
									.replace(" ", "%20");
							Launcher.getRestaurent = Appconstants.MAIN_HOST

									+ "restaurantslisting/20/"
																		
									
									+ Appconstants.strCityId
									+ "/"
									+ Appconstants.LATTITUDE
									+ "/"
									+ Appconstants.LANGITUDE
									+ "/1?search_name="
									+ etWhattoFind.getText().toString()
											.replace(" ", "%20");
							/*Launcher.getRestaurent = Appconstants.MAIN_HOST

									+ "keywordsearch/"
									+ Appconstants.strCityId
									+ "/"
									+ selectedLocationId
									+ "/"
									+ Appconstants.LATTITUDE
									+ "/"
									+ Appconstants.LANGITUDE
									+ "/1/"
									+ etWhattoFind.getText().toString()
											.replace(" ", "%20");*/

						} catch (NullPointerException e) {

							Launcher.userLocation = Appconstants.ltDo.name;
							Explore.type=20;
							Explore.id1=2;
							Appconstants.filters_flg="120";
							Explore.search=etWhattoFind.getText().toString()
									.replace(" ", "%20");
							Launcher.getRestaurent = Appconstants.MAIN_HOST

									+ "restaurantslisting/20/"
																		
									
									+ Appconstants.strCityId
									+ "/"
									+ Appconstants.LATTITUDE
									+ "/"
									+ Appconstants.LANGITUDE
									+ "/1?search_name="
									+ etWhattoFind.getText().toString()
											.replace(" ", "%20");
							/*Launcher.getRestaurent = Appconstants.MAIN_HOST

									+ "keywordsearch/"
									+ Appconstants.strCityId
									+ "/"
									+ selectedLocationId
									+ "/"
									+ Appconstants.LATTITUDE
									+ "/"
									+ Appconstants.LANGITUDE
									+ "/1/"
									+ etWhattoFind.getText().toString()
											.replace(" ", "%20");*/

						}
						
						Explore.search_string="";

						if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

							new RestaurantsList(v.getContext(),
									Launcher.getRestaurent).execute();

						} else {
							ConnectivityReceiver.showCustomDialog(Search.this);
						}
					} else {
						BaseActivity.showDialogMsg(Search.this,
								"Please enter your craving");
					}
				} else {
					BaseActivity.showDialogMsg(Search.this, "Please select location");
				}

			}
		});

		txtBookTableIcon = (TextView) findViewById(R.id.txtBookTableIcon);
	
		txtBookTableIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!TextUtils.isEmpty(etLocation.getText().toString().trim())) {
					try {
						Appconstants.strMenuflag = "BookTable";
						Appconstants.filters_flg="3";
						Localsecrets.str_titles = "Restaurants";
						Launcher.userLocation = selectedLocationName;
						Explore.type=4;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/4/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "globalsearchmenu/" + Appconstants.strCityId
								+ "/" + selectedLocationId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/1/1";*/

					} catch (NullPointerException e) {

						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=4;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/4/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					}
					Explore.search_string="";

					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new RestaurantsList(v.getContext(),
								Launcher.getRestaurent).execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
				} else {
					BaseActivity.showDialogMsg(Search.this, "Please select location");
				}
			}
		});
		
		txtFindFastlaneIcon = (TextView) findViewById(R.id.txtFindFastlaneIcon);
		txtFindFastlaneIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "FastLane";
				Localsecrets.flg = 2;
				Appconstants.filters_flg="2";
				// Localsecrets.str_titles = "Takeaway restaurants";
				Localsecrets.str_titles = "FastLane restaurants";
				
				try { 
					Explore.type = 3;  
					Explore.id1 = 2;

					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/3/" + Appconstants.strCityId
							+ "/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/1";

				} catch (NullPointerException e) {
					Explore.type = 3;
					Explore.id1 = 2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/3/" + Appconstants.strCityId
							+ "/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/1";

				}

				if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

					new RestaurantsList(v.getContext(),
							Launcher.getRestaurent).execute();

				} else {
					ConnectivityReceiver.showCustomDialog(Search.this);
				}
			}
		});
		
		
		txtOrderTkeAwayIcon = (TextView) findViewById(R.id.txtOrderTkeAwayIcon);
		txtOrderTkeAwayIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!TextUtils.isEmpty(etLocation.getText().toString().trim())) {
					Appconstants.strMenuflag = "orderfood";
					Appconstants.filters_flg="0";
					Localsecrets.str_titles = "Takeaway restaurants";
					try {
						Launcher.userLocation = Appconstants.ltDo.name;
						Localsecrets.flg = 0;
						Explore.type=1;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/1/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";

						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "globalsearchmenu/" + Appconstants.strCityId
								+ "/" + selectedLocationId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/2/1";*/

					} catch (NullPointerException e) {
						Localsecrets.flg = 0;
						Appconstants.filters_flg="0";
						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=1;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/1/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "globalsearchmenu/" + Appconstants.strCityId
								+ "/" + selectedLocationId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/2/1";*/

					}
					Explore.search_string="";
					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new RestaurantsList(v.getContext(),
								Launcher.getRestaurent).execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
				} else {
					BaseActivity.showDialogMsg(Search.this, "Please select location");
				}
			}
		});
		txtHomeDelIcon = (TextView) findViewById(R.id.txtHomeDelIcon);
		txtHomeDelIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(etLocation.getText().toString().trim())) {
					Appconstants.strMenuflag = "orderfood";
					Appconstants.filters_flg="1";
					Localsecrets.str_titles = "Restaurants that deliver";
					try {
						Localsecrets.flg = 1;
						
						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=2;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/2/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "globalsearchmenu/" + Appconstants.strCityId
								+ "/" + selectedLocationId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/3/1";
						*/

					} catch (NullPointerException e) {
						Localsecrets.flg = 1;
						Appconstants.filters_flg="1";
						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=2;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/2/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*	Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "globalsearchmenu/" + Appconstants.strCityId
								+ "/" + selectedLocationId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/3/1";*/

					}
					Explore.search_string="";
					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new RestaurantsList(v.getContext(),
								Launcher.getRestaurent).execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
				} else {
					BaseActivity.showDialogMsg(Search.this, "Please select location");
				}
			}
		});
		txtFindDealsIcon = (TextView) findViewById(R.id.txtFindDealsIcon);
		txtFindDealsIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Appconstants.strMenuflag = "Deals";
				//userLocation = Appconstants.ltDo.name;
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

						final String result = new WebServiceCalls()
								.urlPost(dealsUrl);

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								hideLoader();

								if (result != null) {
									Intent inDeals = new Intent(Search.this,
											Deals.class).putExtra("result", result);
									startActivity(inDeals);
									// hideLoader();
								}

							}
						});

					}
				}).start();
			}
		});
		txtOpenNowIcon = (TextView) findViewById(R.id.txtOpenNowIcon);
		txtOpenNowIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!TextUtils.isEmpty(etLocation.getText().toString().trim())) {
					Appconstants.strMenuflag = "Explore";
					Localsecrets.str_titles = "Restaurants open";
					Appconstants.filters_flg="6";
					try {
						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=5;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/5/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";;
					} catch (NullPointerException e) {

						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=5;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/5/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";;
					}
					Explore.search_string="";
					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new RestaurantsList(v.getContext(),
								Launcher.getRestaurent).execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
				} else {
					BaseActivity.showDialogMsg(Search.this, "Please select location");
				}
			}
		});
		txtHappyHrsIcon = (TextView) findViewById(R.id.txtHappyHrsIcon);
		txtHappyHrsIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!TextUtils.isEmpty(etLocation.getText().toString().trim())) {
					Appconstants.strMenuflag = "HappyHrs";
					Localsecrets.str_titles = "Happy hours";
					Appconstants.filters_flg="7";
					try {
						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=17;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=8";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "globalsearchmenu/" + Appconstants.strCityId
								+ "/" + selectedLocationId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/6/1";*/

					} catch (NullPointerException e) {

						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=17;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=8";
					}
					Explore.search_string="";
					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new RestaurantsList(v.getContext(),
								Launcher.getRestaurent).execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
				} else {
					BaseActivity.showDialogMsg(Search.this, "Please select location");
				}
			}
		});
		txtStarIcon = (TextView) findViewById(R.id.txtStarIcon);
		txtStarIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!TextUtils.isEmpty(etLocation.getText().toString().trim())) {
					Localsecrets.str_titles = "Star properties";
					Appconstants.strMenuflag = "Star";
					Appconstants.filters_flg="8";
					try {
						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=19;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=18";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "globalsearchmenu/" + Appconstants.strCityId
								+ "/" + selectedLocationId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/7/1";*/

					} catch (NullPointerException e) {

						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=19;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=18";
					/*	Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "globalsearchmenu/" + Appconstants.strCityId
								+ "/" + selectedLocationId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/7/1";*/

					}
					Explore.search_string="";
					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new RestaurantsList(v.getContext(),
								Launcher.getRestaurent).execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
				} else {
					BaseActivity.showDialogMsg(Search.this, "Please select location");
				}

			}
		});
		txtRestaurantsIcon = (TextView) findViewById(R.id.txtRestaurantsIcon);
		txtRestaurantsIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!TextUtils.isEmpty(etLocation.getText().toString().trim())) {
					Appconstants.strMenuflag = "Explore";
					Localsecrets.str_titles = "Vegetarian restaurants";
					Appconstants.filters_flg="9";
					try {
						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=11;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=3";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "globalsearchmenu/" + Appconstants.strCityId
								+ "/" + selectedLocationId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/8/1";*/

					} catch (NullPointerException e) {

						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=11;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=3";

					}
					Explore.search_string="";
					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new RestaurantsList(v.getContext(),
								Launcher.getRestaurent).execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
				} else {
					BaseActivity.showDialogMsg(Search.this, "Please select location");
				}
			}
		});
		txtDrinksIcon = (TextView) findViewById(R.id.txtDrinksIcon);
		txtDrinksIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!TextUtils.isEmpty(etLocation.getText().toString().trim())) {
					Localsecrets.str_titles = "Drinks served";
					Appconstants.strMenuflag = "HappyHrs";
					Appconstants.filters_flg="10";
					try {
						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=12;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=15";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "globalsearchmenu/" + Appconstants.strCityId
								+ "/" + selectedLocationId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/9/1";*/

					} catch (NullPointerException e) {

						Launcher.userLocation = Appconstants.ltDo.name;
						Explore.type=12;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=15";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "globalsearchmenu/" + Appconstants.strCityId
								+ "/" + selectedLocationId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/9/1";*/

					}
					Explore.search_string="";

					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new RestaurantsList(v.getContext(),
								Launcher.getRestaurent).execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
				} else {
					BaseActivity.showDialogMsg(Search.this, "Please select location");
				}
			}
		});
		txtServingIcon = (TextView) findViewById(R.id.txtServingIcon);
		txtServingIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!TextUtils.isEmpty(etLocation.getText().toString().trim())) {
					Appconstants.strMenuflag = "BuffetPlace";
					Localsecrets.str_titles = "Buffet restaurants";
					Appconstants.filters_flg="11";
					try {
						Explore.type=13;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=38";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "morelinks/" + Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/"
								+ Appconstants.strCityId + "/4";*/
						// + Appconstants.moreMenuJsonDetails
						// .optString("Restaurants serving buffet");

					} catch (NullPointerException e) {
						Explore.type=13;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=38";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "morelinks/" + Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/"
								+ Appconstants.strCityId + "/4";*/
						// + Appconstants.moreMenuJsonDetails
						// .optString("Restaurants serving buffet");

					}
					Explore.search_string="";
//					new RestaurantsList(v.getContext(),
//							Launcher.getRestaurent).execute();
					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new RestaurantsList(v.getContext(),
								Launcher.getRestaurent).execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
				} else {
					BaseActivity.showDialogMsg(Search.this, "Please select location");
				}
			}
		});
		txtSundayIcon = (TextView) findViewById(R.id.txtSundayIcon);
		txtSundayIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!TextUtils.isEmpty(etLocation.getText().toString().trim())) {
					Localsecrets.str_titles = "Sunday brunches";
					Appconstants.strMenuflag = "Sunday";
					Appconstants.filters_flg="12";
					/*
					 * try { Launcher.userLocation = Appconstants.ltDo.name;
					 * 
					 * Launcher.getRestaurent = Appconstants.MAIN_HOST +
					 * "globalsearchmenu/" + Appconstants.strCityId + "/" +
					 * selectedLocationId + "/" + Appconstants.LATTITUDE + "/" +
					 * Appconstants.LANGITUDE + "/11";
					 * 
					 * } catch (NullPointerException e) {
					 * 
					 * Launcher.userLocation = Appconstants.ltDo.name;
					 * 
					 * Launcher.getRestaurent = Appconstants.MAIN_HOST +
					 * "globalsearchmenu/" + Appconstants.strCityId + "/" +
					 * selectedLocationId + "/" + Appconstants.LATTITUDE + "/" +
					 * Appconstants.LANGITUDE + "/11";
					 * 
					 * } new RestaurantsList(v.getContext(),
					 * Launcher.getRestaurent) .execute();
					 */

					try {
						Explore.type=14;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=10";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "morelinks/" + Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/"
								+ Appconstants.strCityId + "/5";*/
						// + Appconstants.moreMenuJsonDetails
						// .optString("Sundaybrunch");

					} catch (NullPointerException e) {
						Explore.type=14;
						Explore.id1=2;
						Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "restaurantslisting/0/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1?features=10";
						/*Launcher.getRestaurent = Appconstants.MAIN_HOST
								+ "morelinks/" + Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/"
								+ Appconstants.strCityId + "/5";*/
						// + Appconstants.moreMenuJsonDetails
						// .optString("Sundaybrunch");

					}
					Explore.search_string="";
//					new RestaurantsList(v.getContext(),
//							Launcher.getRestaurent).execute();
					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new RestaurantsList(v.getContext(),
								Launcher.getRestaurent).execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
					
				} else {
					BaseActivity.showDialogMsg(Search.this, "Please select location");
				}
			}
		});

		etWhattoFind.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// final int DRAWABLE_LEFT = 0;
				// final int DRAWABLE_TOP = 1;
				final int DRAWABLE_RIGHT = 2;
				// final int DRAWABLE_BOTTOM = 3;
				if (etWhattoFind.getText().toString().length() > 0) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (event.getX() >= (etWhattoFind.getRight() - etWhattoFind
								.getCompoundDrawables()[DRAWABLE_RIGHT]
								.getBounds().width())) {
							etWhattoFind.setText("");
						}
					}

				}
				return false; 
			}
		});
		etLocation = (EditText) findViewById(R.id.etLocation);
		// etLocation.setOnTouchListener(new CustomEditText(etLocation) {
		//
		// @Override
		// public boolean onDrawableTouch(MotionEvent event) {
		// // TODO Auto-generated method stub
		// etLocation.setText("");
		// return false;
		// }
		// });
		etLocation.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// final int DRAWABLE_LEFT = 0;
				// final int DRAWABLE_TOP = 1;
				final int DRAWABLE_RIGHT = 2;
				// final int DRAWABLE_BOTTOM = 3;

				lvsuggested.setVisibility(View.GONE);
				relate_lit.setVisibility(View.GONE);
				if (etLocation.getText().toString().length() > 0) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (event.getX() >= (etLocation.getRight() - etLocation
								.getCompoundDrawables()[DRAWABLE_RIGHT]
								.getBounds().width())) {
							etLocation.setText("");
						}
					}
				}
				return false;
			}
		});

		// tvAllHyd = (TextView) findViewById(R.id.tvAllHyderabad);
		// tvAllHyd.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// etLocation.setText(tvAllHyd.getText().toString());
		// selectedLocationName = tvAllHyd.getText().toString();
		// }
		// });
		try {
			if (null != Appconstants.ltDo.name)
				etLocation.setText(Appconstants.ltDo.name);
		} catch (NullPointerException e) {
		}

		// Appconstants.location_do.indexOf(etLocation.getText().toString());

		lvResults = (ListView) findViewById(R.id.listView1);
		adapter = new SearchAdapter(Search.this, commonArray);

		lvLocation = (ListView) findViewById(R.id.listView2);
		lAdp = new LocationAdapter(Search.this, Appconstants.location_do);
		lvLocation.setAdapter(lAdp);

		lvLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				// getting all Buffet Place Restaurants.
				if (((LocationDO) v.getTag()).cat.equalsIgnoreCase("loc")) {
					selectedLocationId = ((LocationDO) v.getTag()).id;
					etLocation.setText(((LocationDO) v.getTag()).name);
					selectedLocationName = etLocation.getText().toString();
					Appconstants.ltDo.name = etLocation.getText().toString();
					return;
				}

			}
		});

		showLoader();
		new Thread(new Runnable() {

			@Override
			public void run() {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						lvResults.setAdapter(adapter);
						hideLoader();
					}
				});

			}
		}).start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (Appconstants.location_do.size() == 0)
					Appconstants.location_do = dHandler.getAllLocations();

				// Appconstants.Keys_do = dHandler.getAllKeys();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						hideLoader();

						/*
						 * commonArray = (ArrayList<LocationDO>)
						 * Appconstants.Keys_do.clone();
						 * adapter.refresh(commonArray);
						 */
					}
				});
			}
		}).start();

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});

		etLocation.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				int textlength = s.length();
				ArrayList<LocationDO> tempArrayList = new ArrayList<LocationDO>();
				for (LocationDO c : Appconstants.location_do) {
					if (textlength <= c.name.length()) {
						if (c.name.toLowerCase().contains(
								s.toString().toLowerCase())) {
							tempArrayList.add(c);
						}
					}
				}
				lAdp.refresh(tempArrayList);

				if (textlength > 0)
					etLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.close1, 0);
				else
					etLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							0);
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
		etWhattoFind.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				int textlength = s.length();
				// ArrayList<LocationDO> tempArrayList = null;

				if (textlength > 0)
					etWhattoFind.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							R.drawable.close1, 0);
				else
					etWhattoFind.setCompoundDrawablesWithIntrinsicBounds(0, 0,
							0, 0);

				if (textlength == 0) {
					commonArray.clear();
					// commonArray = (ArrayList<LocationDO>)
					// Appconstants.Keys_do
					// .clone();
					// adapter.refresh(commonArray);
					return;
				}

				if (textlength > 2) {
					// String url = Appconstants.url2 + "getkeywords/" + s +
					// "/"+ Appconstants.strCityId + "/" +
					// Appconstants.LATTITUDE + "/" 
					// + Appconstants.LANGITUDE;
					// selectedLocationId="159";

					lvsuggested.setVisibility(View.VISIBLE);
					relate_lit.setVisibility(View.VISIBLE);
					url = Appconstants.MAIN_HOST + "getkeyword/" + s + "/"
							+ selectedLocationId + "/" + Appconstants.strCityId
							+ "/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE;

					// url = Appconstants.url2 + "getkeywords/"+ s +
					// "/"+selectedLocationId+"/" + Appconstants.strCityId+ "/"+
					// Appconstants.LATTITUDE + "/" +Appconstants.LANGITUDE;
					
					if (url.contains(" ")) {
						url = url.replaceAll(" ", "%20");
					}
					lvLocation.setVisibility(View.GONE);
					lvResults.setVisibility(View.GONE);
					// tvAllHyd.setVisibility(View.GONE);
					staticIconsScroll.setVisibility(View.GONE);
					txtGo.setVisibility(View.VISIBLE);
					lay.setVisibility(View.VISIBLE);
					// new gettingKeys(url).execute();

					outlet.clear();
					ser.clear();
					name.clear();
					layout_view.clear();
					layout_view1.clear();
					outlet1.clear();
					ser1.clear();
					name1.clear();
					loc_name_array.clear();

//					new ContactTasks().execute();
					if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

						new ContactTasks().execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Search.this);
					}
					
				} else {
					lay.setVisibility(View.GONE);
					lvLocation.setVisibility(View.GONE);
					lvResults.setVisibility(View.GONE);
					// tvAllHyd.setVisibility(View.GONE);
					staticIconsScroll.setVisibility(View.VISIBLE);
					txtGo.setVisibility(View.GONE);
					// tempArrayList = new ArrayList<LocationDO>();
					// for (LocationDO c : commonArray) {
					// if (textlength <= c.name.length()) {
					// if (c.name.toLowerCase().contains(
					// s.toString().toLowerCase())) {
					// tempArrayList.add(c);
					// }
					// }
					// }
				}
				// adapter.refresh(tempArrayList);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		if (etLocation.getText().toString().length() > 0)
			etLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.close1, 0);
		else
			etLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		if (etWhattoFind.getText().toString().length() > 0)
			etWhattoFind.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.close1, 0);
		else
			etWhattoFind.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		staticIconsScroll = (ScrollView) findViewById(R.id.staticIconsScroll);
		etWhattoFind.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					lvLocation.setVisibility(View.GONE);
					lvResults.setVisibility(View.GONE);
					// tvAllHyd.setVisibility(View.GONE);
					staticIconsScroll.setVisibility(View.VISIBLE);
					// do job here when Edit-text lose focus
					// commonArray.clear();
					// commonArray = (ArrayList<LocationDO>)
					// Appconstants.Keys_do
					// .clone();
					// adapter.refresh(commonArray);
				}
			}
		});

		etWhattoFind.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				String url;
				if (!TextUtils.isEmpty(etWhattoFind.getText().toString()
						.trim())) {
				try {
					Launcher.userLocation = Appconstants.ltDo.name;
					Explore.type=20;
					Explore.id1=2;
					Appconstants.filters_flg="120";
					Explore.search=etWhattoFind.getText().toString()
							.replace(" ", "%20");
					url = Appconstants.MAIN_HOST

							+ "restaurantslisting/20/"
																
							
							+ Appconstants.strCityId
							+ "/"
							+ Appconstants.LATTITUDE
							+ "/"
							+ Appconstants.LANGITUDE
							+ "/1?search_name="
							+ etWhattoFind.getText().toString()
									.replace(" ", "%20");
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST

							+ "keywordsearch/"
							+ Appconstants.strCityId
							+ "/"
							+ selectedLocationId
							+ "/"
							+ Appconstants.LATTITUDE
							+ "/"
							+ Appconstants.LANGITUDE
							+ "/1/"
							+ etWhattoFind.getText().toString()
									.replace(" ", "%20");*/

				} catch (NullPointerException e) {

					Launcher.userLocation = Appconstants.ltDo.name;
					Explore.type=20;
					Explore.id1=2;
					Appconstants.filters_flg="120";
					Explore.search=etWhattoFind.getText().toString()
							.replace(" ", "%20");
					url = Appconstants.MAIN_HOST

							+ "restaurantslisting/20/"
																
							
							+ Appconstants.strCityId
							+ "/"
							+ Appconstants.LATTITUDE
							+ "/"
							+ Appconstants.LANGITUDE
							+ "/1?search_name="
							+ etWhattoFind.getText().toString()
									.replace(" ", "%20");
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST

							+ "keywordsearch/"
							+ Appconstants.strCityId
							+ "/"
							+ selectedLocationId
							+ "/"
							+ Appconstants.LATTITUDE
							+ "/"
							+ Appconstants.LANGITUDE
							+ "/1/"
							+ etWhattoFind.getText().toString()
									.replace(" ", "%20");*/

				}
				if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

					new RestaurantsList(Search.this, url).execute();

				} else {
					ConnectivityReceiver.showCustomDialog(Search.this);
				}
				} else {
				BaseActivity.showDialogMsg(Search.this,
						"Please enter your craving");
			}
				return false; 
			}  
		});

		etLocation.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {

				String url = Appconstants.MAIN_HOST + "exploreRestaurants/"
						+ selectedLocationId + "/null/"
						+ Appconstants.strCityId + "/" 
						+ etWhattoFind.getText().toString() + "/"
						+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE;

				if (inEditText.equalsIgnoreCase(etWhattoFind.getText()
						.toString())) {
					try {
						url = etWhattoFind.getTag().toString();
					} catch (NullPointerException e) {
					}
				}  

				if (etLocation.getText().length() == 0) {
					selectedLocationId = "null";
					Launcher.userLocation = "Hyderabad"; 
				} else if (etLocation.getText().toString().contains("All")) {
					selectedLocationId = "null";
					Launcher.userLocation = "Hyderabad";
				} else {
					Launcher.userLocation = etLocation.getText().toString();
				}

				if (url.contains(" ")) { 
					url = url.replaceAll(" ", "%20");
				}
				
				if (ConnectivityReceiver.checkInternetConnection(Search.this)) {

					new RestaurantsList(Search.this, url).execute();

				} else {
					ConnectivityReceiver.showCustomDialog(Search.this);
				}

				return false;
			}
		}); 

		etLocation.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					lvLocation.setVisibility(View.VISIBLE);
					// tvAllHyd.setVisibility(View.VISIBLE);
					lvResults.setVisibility(View.GONE);
					staticIconsScroll.setVisibility(View.GONE);
					lAdp.refresh(Appconstants.location_do);

					/*
					 * commonArray.clear(); commonArray =
					 * (ArrayList<LocationDO>) Appconstants.location_do.clone();
					 * adapter.refresh(commonArray);
					 */
				}
			}
		});

		lvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View v,
					int position, long id) {

				// getting all Buffet Place Restaurants.
				if (((LocationDO) v.getTag()).cat.equalsIgnoreCase("loc")) {

					selectedLocationId = ((LocationDO) v.getTag()).id;
					etLocation.setText(((LocationDO) v.getTag()).name);
					return;
				}

				if (etLocation.getText().length() == 0) {
					selectedLocationId = "null";
				} else if (etLocation.getText().toString().contains("All")) {
					selectedLocationId = "null";
					Launcher.userLocation = "Hyderabad";
				} else {
					Launcher.userLocation = etLocation.getText().toString();
				}

				// showLoader();
				String strKey = "null";

				strKey = ((LocationDO) v.getTag()).id;
				Appconstants.type = ((LocationDO) v.getTag()).name;

				if (Appconstants.LATTITUDE == null
						|| Appconstants.LATTITUDE.contains("null")) {
					Appconstants.LATTITUDE = "0.0";
					Appconstants.LANGITUDE = "0.0";
				}

				finalURL = Appconstants.MAIN_HOST;
				String[] arrayURL = strKey.split("/");
				finalURL1 = finalURL + arrayURL[0] + "/" + "null" + "/";
				finalURL = finalURL + arrayURL[0] + "/" + selectedLocationId
						+ "/";
				
				for (int i = 2; i < arrayURL.length; i++) {
					finalURL = finalURL + arrayURL[i] + "/";
					finalURL1 = finalURL1 + arrayURL[i] + "/";
				}

				// Launcher.getRestaurent = finalURL;
				// if(finalURL.contains(" ")){
				finalURL = finalURL.replaceAll(" ", "%20");
				finalURL1 = finalURL1.replaceAll(" ", "%20");

				// }

				Launcher.getRestaurent = finalURL;

				etWhattoFind.setText(((LocationDO) v.getTag()).name);
				inEditText = ((LocationDO) v.getTag()).name;
				etWhattoFind.setTag(strKey);

				// new
				// Launcher.RestaurantsList(Search.this,Launcher.getRestaurent).execute();
 
			}
		});

		lvLocation.setVisibility(View.GONE);
		lvResults.setVisibility(View.GONE);
		// tvAllHyd.setVisibility(View.GONE);
		staticIconsScroll.setVisibility(View.VISIBLE);

		txtSuggest = (TextView) findViewById(R.id.txtSuggest);
		txtRelated = (TextView) findViewById(R.id.txtRelated);
	}
	
	class DeatilsTasks extends AsyncTask<String, String, String> {

		ProgressDialog myProgressDialog;
		ArrayList<reviewDO> reviews = new ArrayList<reviewDO>();
		String url;
		DeatilsTasks(String url){
			this.url=url;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute(); 
			myProgressDialog = new ProgressDialog(Search.this);
			myProgressDialog.setMessage("Loading..."); 
			myProgressDialog.show();
		}
 
		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(url);
		}

		@SuppressLint("InflateParams")
		@Override
		protected void onPostExecute(String result) { 
			super.onPostExecute(result);
			try{
				JSONObject jObject = new JSONObject(result);
				if (jObject.has("id")) {
					Intent in = new Intent(getApplicationContext(), SearchDetails.class);
					in.putExtra("result",result);
					startActivityForResult(in, 5);
				}
			
			myProgressDialog.dismiss();
		} catch (Exception e) {
			myProgressDialog.dismiss();
			Log.e("", "" + e);
		}

		}
	}

	static String finalURL, finalURL1;

	String inEditText = "";
	ArrayList<LocationDO> keys = new ArrayList<LocationDO>();

	class gettingKeys extends AsyncTask<String, String, String> {

		String url;

		public gettingKeys(String url) {

			this.url = url;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// showLoader();
		}

		@Override
		protected String doInBackground(String... params) {

			return new WebServiceCalls().urlGet(url);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result == null) {
				return;
			}

			keys.clear();
			try {
				JSONObject jResponce = new JSONObject(result);
				JSONArray jArray = jResponce.getJSONArray("Keywords");

				for (int i = 0; i < jArray.length(); i++) {

					LocationDO ltDo = new LocationDO();
					JSONObject jo = jArray.getJSONObject(i);
					ltDo.id = jo.getString("url");
					ltDo.name = jo.getString("keyword");
					ltDo.location = jo.getString("location");
					ltDo.type = jo.getString("type");
					keys.add(ltDo);

				}

				commonArray = (ArrayList<LocationDO>) keys.clone();
				adapter.refresh(commonArray);
				// hideLoader();

			} catch (JSONException e) {
			}
		}

	}

	static class SearchAdapter extends BaseAdapter {

		private Context mContext;
		ArrayList<LocationDO> array;

		public SearchAdapter(Context context, ArrayList<LocationDO> array) {
			super();
			mContext = context;
			this.array = array;

		}

		@Override
		public int getCount() {
			if (array == null)
				return 0;
			return array.size();
		}

		// getView method is called for each item of ListView
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// inflate the layout for each item of listView
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listview_search_item, null);

			TextView tvItem = (TextView) view
					.findViewById(R.id.txtBookTableIcon);
			if (array.get(position).location.equalsIgnoreCase("")) {
				// SpannableString content;
				if (array.get(position).type.equalsIgnoreCase("as Cuisine")) {
					tvItem.setText(array.get(position).name + " "
							+ array.get(position).type);
					// content = new
					// SpannableString(array.get(position).name+" "+array.get(position).type);
					// content.setSpan(new RelativeSizeSpan(0.7f),
					// (array.get(position).name+",").length(),
					// content.length(), 0);
					// content.setSpan(new
					// StyleSpan(android.graphics.Typeface.BOLD),(array.get(position).name+",").length(),
					// content.length(), 0);
				} else {
					tvItem.setText(array.get(position).type + " "
							+ array.get(position).name);
					// content = new
					// SpannableString(array.get(position).type+" "+array.get(position).name);
					// content.setSpan(new RelativeSizeSpan(0.7f), 0,
					// (array.get(position).type).length(), 0);
					// content.setSpan(new
					// StyleSpan(android.graphics.Typeface.BOLD), 0,
					// (array.get(position).type).length(), 0);
				}

			} else {
				SpannableString content = new SpannableString(
						array.get(position).name + ", "
								+ array.get(position).location);
				content.setSpan(new RelativeSizeSpan(0.7f),
						(array.get(position).name + ",").length(),
						content.length(), 0);
				content.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
						(array.get(position).name + ",").length(),
						content.length(), 0);
				tvItem.setText(content);
			}

			view.setTag(array.get(position));

			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void refresh(ArrayList<LocationDO> array) {
			this.array = array;
			this.notifyDataSetChanged();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0 && resultCode == 1012) {
			finish();
		}

	}

	private class LocationAdapter extends BaseAdapter {

		private Context mContext;
		ArrayList<LocationDO> array;

		private LocationAdapter(Context context, ArrayList<LocationDO> array) {
			super();
			mContext = context;
			this.array = array;

		}

		@Override
		public int getCount() {
			if (array == null)
				return 0;
			return array.size();
		}

		// getView method is called for each item of ListView
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// inflate the layout for each item of listView
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listview_search_item, null);

			TextView tvItem = (TextView) view
					.findViewById(R.id.txtBookTableIcon);
			if (array.get(position).location.equalsIgnoreCase("")) {
				// SpannableString content;
				if (array.get(position).type.equalsIgnoreCase("as Cuisine")) {
					tvItem.setText(array.get(position).name + " "
							+ array.get(position).type);
					// content = new
					// SpannableString(array.get(position).name+" "+array.get(position).type);
					// content.setSpan(new RelativeSizeSpan(0.7f),
					// (array.get(position).name+",").length(),
					// content.length(), 0);
					// content.setSpan(new
					// StyleSpan(android.graphics.Typeface.BOLD),(array.get(position).name+",").length(),
					// content.length(), 0);
				} else {
					tvItem.setText(array.get(position).type + " "
							+ array.get(position).name);
					// content = new
					// SpannableString(array.get(position).type+" "+array.get(position).name);
					// content.setSpan(new RelativeSizeSpan(0.7f), 0,
					// (array.get(position).type).length(), 0);
					// content.setSpan(new
					// StyleSpan(android.graphics.Typeface.BOLD), 0,
					// (array.get(position).type).length(), 0);
				}

			} else {
				SpannableString content = new SpannableString(
						array.get(position).name + ", "
								+ array.get(position).location);
				content.setSpan(new RelativeSizeSpan(0.7f),
						(array.get(position).name + ",").length(),
						content.length(), 0);
				content.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
						(array.get(position).name + ",").length(),
						content.length(), 0);
				tvItem.setText(content);
			}

			view.setTag(array.get(position));

			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void refresh(ArrayList<LocationDO> array) {
			this.array = array;
			this.notifyDataSetChanged();
		}
	}

	// class gettingAllKeys extends AsyncTask<String, String, String> {
	//
	// @Override
	// protected void onPreExecute() {
	// showLoader();
	// super.onPreExecute();
	// }
	//
	// @Override
	// protected String doInBackground(String... params) {
	//
	// String url = Appconstants.url2 + "getkeywords/null/null/"
	// + Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE;
	//
	// String responce = new WebServiceCalls().urlGet(url);
	//
	// ArrayList<LocationDO> keys = new ArrayList<LocationDO>();
	//
	// if (responce != null) {
	// // Appconstants.location_do.clear();
	// try {
	// JSONObject jResponce = new JSONObject(responce);
	// JSONArray jArray = jResponce.getJSONArray("Keywords");
	//
	// for (int i = 0; i < jArray.length(); i++) {
	//
	// LocationDO ltDo = new LocationDO();
	// JSONObject jo = jArray.getJSONObject(i);
	// ltDo.id = jo.getString("url");
	// ltDo.name = jo.getString("keyword");
	// keys.add(ltDo);
	//
	// }
	//
	// Appconstants.Keys_do = keys;
	// /*
	// * if(keys.size()>0) {
	// *
	// * dHandler.insertKeys(keys); }
	// */
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// return null;
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	// hideLoader();
	//
	// commonArray = (ArrayList<LocationDO>) Appconstants.Keys_do.clone();
	// adapter.refresh(commonArray);
	//
	// /*
	// * Intent in = new Intent(Launcher.this, Search.class);
	// * startActivity(in);
	// */
	//
	// }
	//
	// }

	private class ContactTasks extends AsyncTask<Object, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Object... params) {

			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(url);

				response = client.execute(post);

			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			super.onPostExecute(success);

			try {

				HttpEntity entity = response.getEntity();
				String re = EntityUtils.toString(entity, HTTP.UTF_8).trim();
				JSONObject jObject = new JSONObject(re);
				JSONArray jArray = jObject.getJSONArray("suggestedvenues");
				JSONArray jArray1 = jObject.getJSONArray("relatedsearches");

				name.clear();
				loc_name_array.clear();
				ser.clear();
				outlet.clear();
				layout_view.clear();

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject jObjects = new JSONObject(jArray.getString(i));
					if (jObjects.has("name")) {
						name.add("" + jObjects.getString("name"));
					}
					if (jObjects.has("locationname")) {
						loc_name_array.add(""
								+ jObjects.getString("locationname"));
					}

					if (jObjects.has("service")) {
						ser.add("" + jObjects.getString("service"));
					}
					if (jObjects.has("outlets")) {
						outlet.add("" + jObjects.getString("outlets"));
					}

					if (jObjects.has("layoutview")) {
						layout_view.add("" + jObjects.getString("layoutview"));
					}
				}

				brands_data.clear();
				Events1 p1, p2;
				for (int i = 0; i < name.size(); i++) {
					p1 = new Events1(name.get(i), outlet.get(i),
							loc_name_array.get(i));
					brands_data.add(p1);
				}

				if (name.size() == 0) {
					txtSuggest.setVisibility(View.INVISIBLE);
				} else {
					txtSuggest.setVisibility(View.VISIBLE);
				}

				adapterP = new AttractionsAdapter(getApplicationContext(),
						R.layout.row_1, brands_data);
				 adapter.notifyDataSetChanged();
				lvsuggested.setAdapter(adapterP);

				for (int i = 0; i < jArray1.length(); i++) {
					JSONObject jObjects1 = new JSONObject(jArray1.getString(i));
					if (jObjects1.has("name")) {
						name1.add("" + jObjects1.getString("name"));
					}
					if (jObjects1.has("service")) {
						ser1.add("" + jObjects1.getString("service"));
					}
					if (jObjects1.has("type")) {
						outlet1.add("" + jObjects1.getString("type"));
					}
					if (jObjects1.has("layoutview")) {
						layout_view1
								.add("" + jObjects1.getString("layoutview"));
					}
				}

				brands_data1.clear();
				for (int i = 0; i < name1.size(); i++) {
					p2 = new Events1(name1.get(i), outlet1.get(i), "");
					brands_data1.add(p2);
				}
				if (name1.size() == 0) {
					txtRelated.setVisibility(View.INVISIBLE);
				} else {
					txtRelated.setVisibility(View.VISIBLE);
				}
				adapterP1 = new AttractionsAdapter1(getApplicationContext(),
						R.layout.row_1, brands_data1);
				relate_lit.setAdapter(adapterP1);

			} catch (Exception e) {
				e.printStackTrace();

			}

		}
	}

	private class AttractionsAdapter extends ArrayAdapter<Events1> {

		private ArrayList<Events1> dataValues;
		@SuppressWarnings("unused")
		private Context mContext;

		public AttractionsAdapter(Context context, int textViewResourceId,
				ArrayList<Events1> items) {
			super(context, textViewResourceId, items);
			this.dataValues = items;
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return brands_data.size();
		}

		@Override
		public Events1 getItem(int position) {
			return dataValues.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_1, null);
			}
			TextView t = (TextView) v.findViewById(R.id.textView1);
			TextView t3 = (TextView) v.findViewById(R.id.textView2);
			TextView t2 = (TextView) v.findViewById(R.id.textView3);
			Events1 temp = brands_data.get(position);
			t.setText(temp.Image.trim());
			t2.setText(temp.loc_name.trim());
			if (temp.Title.trim().length() > 0) {
				if (temp.Title.trim().contains("1")) {
					t3.setText(temp.Title.trim() + " outlet");
				} else {
					t3.setText(temp.Title.trim() + " outlets");
				}

			} else {
				t3.setText(temp.Title.trim());
			}

			return v;
		}
	}

	private class AttractionsAdapter1 extends ArrayAdapter<Events1> {

		private ArrayList<Events1> dataValues;
		@SuppressWarnings("unused")
		private Context mContext;

		public AttractionsAdapter1(Context context, int textViewResourceId,
				ArrayList<Events1> items) {
			super(context, textViewResourceId, items);
			this.dataValues = items;
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return brands_data1.size();
		}

		@Override
		public Events1 getItem(int position) {
			return dataValues.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_1, null);
			}
			TextView t = (TextView) v.findViewById(R.id.textView1);
			TextView t3 = (TextView) v.findViewById(R.id.textView2);
			Events1 temp = brands_data1.get(position);
			t.setText(temp.Image.trim());
			t3.setVisibility(View.GONE);
			if (temp.Title.trim().length() > 0) {
				if (temp.Title.trim().contains("1")) {
					t3.setText(temp.Title.trim());// +" outlet"
				} else {
					t3.setText(temp.Title.trim());// +" outlets"
				}

			} else {
				t3.setText(temp.Title.trim());
			}

			return v;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Appconstants.type = "";
	}

}
