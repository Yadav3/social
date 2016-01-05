package com.etisbew.eatz.android;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etisbew.eatz.android.dropdownlist.MyAccount;
import com.etisbew.eatz.android.dropdownlist.MyDeals;
import com.etisbew.eatz.android.dropdownlist.MyFav;
import com.etisbew.eatz.android.dropdownlist.MyOrders;
import com.etisbew.eatz.android.dropdownlist.MyReservations;
import com.etisbew.eatz.android.dropdownlist.MyReviews;
import com.etisbew.eatz.android.dropdownlist.RedemptionHistory;
import com.etisbew.eatz.android.orderfood.OrderFood;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.RestaurentDO;
import com.etisbew.eatz.objects.reviewDO;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("ViewHolder")
public class Explore extends BaseActivity implements OnClickListener {

	private OnInfoWindowElemTouchListener infoButtonListener;
	Button tv_booktable_img, tv_order_food_img;
	String url1 = "";
	private static ListView lvResults = null;
	private SearchAdapter adapter = null;
	private ArrayList<RestaurentDO> restrent = null;
	private ImageView ivBack = null, options = null;
	private LinearLayout llSort = null, llFilter = null, llNoResult = null,
			llMap = null;
	public static String strRestaurantTitle, strRestaurantAddress;
	public static String strBookMsg = "";
	public static String strBookDateMsg = "";
	public static String strBookDate = "";
	static String strDate;
	public static String strRestaurantRating, strVenueId, strPartySize,
			strSessionName, strBookingSession, strBookingDate, strphone;
	static ProgressDialog pDialog1;
	Boolean sortFlag = false;
	TextView etFind = null, tvOpenNow = null, tvLocation = null;
	public static ArrayList<String> vid = new ArrayList<String>();
	public static ArrayList<String> opncls = new ArrayList<String>();
	public static ArrayList<String> venueNames = new ArrayList<String>();
	public static ArrayList<Double> latitude = new ArrayList<Double>();
	public static ArrayList<Double> longitude = new ArrayList<Double>();
	public static ArrayList<String> phone = new ArrayList<String>();
	public static ArrayList<String> address = new ArrayList<String>();
	public static ArrayList<String> orderfood = new ArrayList<String>();
	public static ArrayList<String> bookatable = new ArrayList<String>();
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

	public static String selectedPrice = "0";
	public static int selectedCusine = 0;
	public static ArrayList<String> selectedItemsArray = null;

	private RelativeLayout tvAlbha, tvRating, tvDistence, tvPrice;
	private RelativeLayout rlError, llExplore = null, llayoutSort = null,
			orderfood_layout;
	ImageView ivIcon;
	Button btnBrowse;
	private TextView txtErrorMsg = null, map_view;
	public static String strBookDate1;
	public static int id1 = 2;
	RestaurentDO objItem;
	static JSONObject restJsonDetails1;
	static JSONArray jsonArrayRestaurants1;
	public static int type;
	int flag = 1;
	static String search;
	public static String strlat;
	public static String strlon;

	int flag1 = 1;
	static int map_flag = 1;

	String map_info = "";
	private GoogleMap mMap;
	private MapFragment mMapFragment;
	// private static View view;
	private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
	private HashMap<Marker, MyMarker> mMarkersHashMap;
	String service_url1 = "";
	String id;
	
	ImageView iv_menubar1, iv_menubar2, iv_menubar3, iv_menubar4, map_list;
	private ViewGroup infoWindow;
	CustomLayout mapWrapperLayout;
	static String search_string = "";
	int no_records_flag = 0;
	GPSTracker gps;
	Double lat, lang;
	ArrayList<LatLng> markerPoints;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.explore);

		hideKeyboard();
		hideLoader();

		// CustomLayout mapWrapperLayout = (CustomLayout)
		// findViewById(R.id.map_relative_layout);

		selectedPrice = "0";
		selectedCusine = 0;
		selectedItemsArray = new ArrayList<String>();

		tvLocation = (TextView) findViewById(R.id.tvLocation);
		etFind = (TextView) findViewById(R.id.etFind);
		tvOpenNow = (TextView) findViewById(R.id.tvOpenNow);

		map_view = (TextView) findViewById(R.id.map_view);

		btnBrowse = (Button) findViewById(R.id.btnBrowseRestaurants);
		btnBrowse.setOnClickListener(this);
		System.out.println("str_menu_flag is" + Appconstants.strMenuflag);

		// tvLocation.setText("Near "+ Appconstants.ltDo.name);
		System.out.println("map_flag is" + map_flag);
		/*
		 * if(map_flag ==2){ map_view.setText("List View"); }
		 */

		try {
			System.out.println("name" + Launcher.restJsonDetails);
			/*
			 * if (!Launcher.restJsonDetails.isNull("name")) {
			 * 
			 * tvOpenNow.setText(Launcher.restJsonDetails.getString("name")); }
			 */
			tvOpenNow.setText(Localsecrets.str_titles);

			if (!Launcher.restJsonDetails.isNull("location")) {
				/*
				 * if (type == 17 || type == 15 || type == 19 || type == 11 ||
				 * type == 12 || type == 13 || type == 14) {
				 */

				if (type == 15) {

					tvLocation.setText("All of " + Appconstants.strCityName);
				} else {
					if (no_records_flag == 1) {
						tvLocation
								.setText("All of " + Appconstants.strCityName);
					} else {
						tvLocation.setText("Near "
								+ Launcher.restJsonDetails
										.getString("location"));
					}
				}

			} else {
				if (type == 15) {
					tvLocation.setText("All of " + Appconstants.strCityName);
				} else {
					if (Localsecrets.selectedLocationId
							.equalsIgnoreCase("null")) {
						if (no_records_flag == 1) {
							tvLocation.setText("All of "
									+ Appconstants.strCityName);
						} else {
							tvLocation.setText("Near " + Launcher.userLocation);
						}

					} else {
						if (no_records_flag == 1) {
							tvLocation.setText("All of "
									+ Appconstants.strCityName);
						} else {
							if (Appconstants.ltDo.name
									.equalsIgnoreCase("All of "
											+ Appconstants.strCityName)) {
								tvLocation.setText("" + Appconstants.ltDo.name);
							} else {
								tvLocation.setText("Near "
										+ Appconstants.ltDo.name);
							}
						}
					}
				}
			}

		} catch (JSONException e) {
			// startActivity(new Intent(Explore.this, Launcher.class));
			e.printStackTrace();
		}

		lvResults = (ListView) findViewById(R.id.listView1);

		llSort = (LinearLayout) findViewById(R.id.llSort);
		llFilter = (LinearLayout) findViewById(R.id.llFilter);

		llExplore = (RelativeLayout) findViewById(R.id.llExplore);
		llNoResult = (LinearLayout) findViewById(R.id.llNoResult);
		llayoutSort = (RelativeLayout) findViewById(R.id.llayoutSort);
		llMap = (LinearLayout) findViewById(R.id.llMap);
		ivBack = (ImageView) findViewById(R.id.back);

		iv_menubar1 = (ImageView) findViewById(R.id.iv_menubar1);
		iv_menubar2 = (ImageView) findViewById(R.id.iv_menubar2);
		iv_menubar3 = (ImageView) findViewById(R.id.iv_menubar3);
		iv_menubar4 = (ImageView) findViewById(R.id.iv_menubar4);
		map_list = (ImageView) findViewById(R.id.map_list);

		orderfood_layout = (RelativeLayout) findViewById(R.id.orderfood_layout);

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * Intent i = new Intent(getApplicationContext(),
				 * Launcher.class); startActivity(i);
				 */
				if (Appconstants.strMenuflag.equalsIgnoreCase("BookTable")) {
					finish();
					startActivity(new Intent(getApplicationContext(),
							Launcher.class));
					
				} else {
					setResult(1012);
					finish();
				}

				// startActivity(new Intent(Explore.this, OrderList.class));
				// Explore.this.finish();

			}
		});
		gps = new GPSTracker(Explore.this) {
		};
		if (gps.canGetLocation()) {
			System.out.println("lat long" + gps.getLatitude()
					+ gps.getLongitude());
			System.out.println("od details are" + Appconstants.LATTITUDE
					+ Appconstants.LANGITUDE + Appconstants.strCityId
					+ Appconstants.strCityName);
			lat = gps.getLatitude();
			lang = gps.getLongitude();
		} else {
			lat = Double.parseDouble(Appconstants.LANGITUDE);
			lang = Double.parseDouble(Appconstants.LANGITUDE);
		}

		lvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				System.out.println("flag is" + flag1);
				if (flag1 == 1) {
					if (type == 4) {
						strVenueId = restrent.get(position).id;
						strRestaurantTitle = restrent.get(position).name;
						strlat = restrent.get(position).latitude;
						strlon = restrent.get(position).longitude;
						strphone = restrent.get(position).phone;
						strRestaurantAddress = restrent.get(position).addressLine1
								+ ","
								+ restrent.get(position).addressLine2
								+ "" + restrent.get(position).location;
						strRestaurantRating = restrent.get(position).review_rating;

						Calendar c = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						if (TextUtils.isEmpty(strBookDate)) {
							strBookDate = sdf.format(c.getTime());
							strBookDateMsg = "";
							Explore.strBookingDate = strBookDate;
							Explore.strPartySize = "2";
						} else {
							strBookDateMsg = "";
							Explore.strBookingDate = strBookDate;
							Explore.strPartySize = "2";
						}
						if (ConnectivityReceiver
								.checkInternetConnection(Explore.this)) {
							new getSessionsSlotsTask(Explore.this, strVenueId)
									.execute();
						} else {
							ConnectivityReceiver.showCustomDialog(Explore.this);
						}
					} else if (type == 1 || type == 2 || type == 3) {

						strVenueId = restrent.get(position).id;
						strRestaurantTitle = restrent.get(position).name;
						// new getSessionsSlotsTask(strVenueId).execute();
						// startActivity(new
						// Intent(getApplicationContext(),BookTable.class));
						/*
						 * startActivity(new Intent(getApplicationContext(),
						 * OrderFood.class));
						 */
						if (ConnectivityReceiver
								.checkInternetConnection(Explore.this)) {

							new getOrderCategoriesTask().execute();

						} else {
							ConnectivityReceiver.showCustomDialog(Explore.this);
						}

					} else {
						TextView txt = (TextView) view
								.findViewById(R.id.tvRestaurentTitle);
						position = venueNames.indexOf(txt.getText().toString()
								.trim());
						strVenueId = vid.get(position);

						strVenueId = vid.get(position);
						strRestaurantTitle = venueNames.get(position);
						SetData();
					}
				} else {
					flag1 = 1;
					llayoutSort.setVisibility(View.GONE);
				}

				/*
				 * Intent in = new Intent(view.getContext(),
				 * SearchDetails.class); .putExtra("item", ((RestaurentDO)
				 * view.getTag())); startActivityForResult(in, 5);
				 */

			}
		});
		/*
		 * lvResults.setOnTouchListener(new OnTouchListener() {
		 * 
		 * public boolean onTouch(View v, MotionEvent event) { if
		 * (event.getAction() == MotionEvent.ACTION_MOVE) { return true; //
		 * Indicates that this has been handled by you and will not be forwarded
		 * further. } Toast.makeText(getApplicationContext(), "touched",
		 * Toast.LENGTH_LONG).show(); return false; }
		 * 
		 * 
		 * });
		 */

		llSort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag1 == 1) {
					// SlideToAbove();
					flag1 = 2;
					sortFlag = true;
					llayoutSort.setVisibility(View.VISIBLE);

					/*
					 * lvResults.setScrollContainer(false);
					 * lvResults.setClickable(false);
					 * lvResults.setEnabled(false);
					 */
				} else {
					// SlideToDown();
					/*
					 * lvResults.setScrollContainer(true);
					 * lvResults.setClickable(true); lvResults.setEnabled(true);
					 */
					flag1 = 1;
					llayoutSort.setVisibility(View.GONE);

				}
				// llExplore.setVisibility(View.GONE);

			}
		});
		llFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				System.out.println("flag" + Appconstants.filters_flg);

				if (Appconstants.filters_flg == "1"
						|| Appconstants.filters_flg == "2"
						|| Appconstants.filters_flg == "0") {
					Filters1.flag = map_flag;
					startActivityForResult(new Intent(v.getContext(),
							Filters1.class), 1);

					// new getBooktableFilters

				} else {
					Filters.flag = map_flag;

					/*
					 * startActivityForResult(new Intent(v.getContext(),
					 * Filters.class), 1);
					 */
					if (ConnectivityReceiver
							.checkInternetConnection(Explore.this)) {
						new getBooktableFilters().execute();

					} else {
						ConnectivityReceiver.showCustomDialog(Explore.this);
					}
				}
			}
		});
		llMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String ss = map_view.getText().toString();

				if (ss.equalsIgnoreCase("map view")) {
					System.out.println("mapview");
					mMyMarkersArray.clear();
					mMarkersHashMap = new HashMap<Marker, MyMarker>();
					System.out.println("lengths are "
							+ Explore.venueNames.size() + ":"
							+ Explore.venueNames.size());
					for (int i = 0; i < Explore.venueNames.size(); i++) {

						mMyMarkersArray.add(new MyMarker(Explore.venueNames
								.get(i), Explore.address.get(i),
								Explore.latitude.get(i), Explore.longitude
										.get(i), Explore.vid.get(i), Integer
										.parseInt(Explore.orderfood.get(i)),
								Integer.parseInt(Explore.bookatable.get(i))));

					}
					map_flag = 2;

					mMap.clear();
					mMap = null;
					setUpMap();
					plotMarkers(mMyMarkersArray);
					map_view.setText("List View");
					map_list.setImageResource(R.drawable.list);

					lvResults.setVisibility(View.GONE);
				} else if (ss.equalsIgnoreCase("list view")) {
					System.out.println("listview");
					map_flag = 1;

					mMap.clear();
					mMap = null;
					setUpMap();
					map_view.setText("Map View");
					map_list.setImageResource(R.drawable.map);
					adapter.notifyDataSetChanged();
					lvResults.setVisibility(View.VISIBLE);
				}

				// Intent in = new Intent(v.getContext(), MapLocation.class);
				// startActivityForResult(in, 1);
			}
		});

		etFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent inExplore = new Intent(v.getContext(), Search.class)
						.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(inExplore);
				//finish();

			}
		});

		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				quickAction.show(v);

			}
		});

		tvAlbha = (RelativeLayout) findViewById(R.id.tvAlbha);
		// tvCancel = (RelativeLayout) findViewById(R.id.tvCancel);
		tvRating = (RelativeLayout) findViewById(R.id.tvRating);
		tvDistence = (RelativeLayout) findViewById(R.id.tvDistence);
		tvPrice = (RelativeLayout) findViewById(R.id.tvPrice);

		tvAlbha.setOnClickListener(this);
		// tvCancel.setOnClickListener(this);
		tvRating.setOnClickListener(this);
		tvDistence.setOnClickListener(this);
		tvPrice.setOnClickListener(this);

		rlError = (RelativeLayout) findViewById(R.id.rlErrorMsg);
		txtErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
		ivIcon = (ImageView) findViewById(R.id.ivCross);

		if (Launcher.userLocation.equalsIgnoreCase("Hyderabad")) {
			txtErrorMsg.setText(Launcher.strErrorMsg);
			rlError.setVisibility(View.VISIBLE);
		}

		ivIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rlError.setVisibility(View.GONE);
			}
		});
		// if (ConnectivityReceiver.checkInternetConnection(Explore.this)) {
		new DisplayRestaurents().execute();

		/*
		 * } else { ConnectivityReceiver.showCustomDialog(Explore.this); }
		 */
	}

	TextView textView1, textView2;

	@SuppressLint("NewApi")
	private void setUpMap() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.

			mMap = ((MapFragment) Explore.this.getFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			mMapFragment = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map));
			// googleMap = ((MapFragment) getFragmentManager()
			// .findFragmentById(R.id.map)).getMap();
			/*
			 * FragmentManager fm = getSupportFragmentManager();
			 * FragmentTransaction ft = fm.beginTransaction();
			 * ft.hide(mMapFragment).commit();
			 */

			mMap = mMapFragment.getMap();
			/*
			 * mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
			 * 28.611892, 77.376226), 16));
			 */

			if (map_flag == 1) {

				mMapFragment.getView().setVisibility(View.GONE);
			} else if (map_flag == 2) {
				mMapFragment.getView().setVisibility(View.VISIBLE);
			}

			this.infoWindow = (ViewGroup) getLayoutInflater().inflate(
					R.layout.custom_map, null);
			textView1 = (TextView) infoWindow.findViewById(R.id.textView1);
			textView2 = (TextView) infoWindow.findViewById(R.id.textView2);

			tv_order_food_img = (Button) infoWindow
					.findViewById(R.id.tv_order_food_img);
			// tv_order_food_img.setTag(tag)

			tv_booktable_img = (Button) infoWindow
					.findViewById(R.id.tv_booktable_img);
			// asdsaa

			/*
			 * if (Appconstants.strMenuflag.equalsIgnoreCase("FastLane")) {
			 * 
			 * tv_order_food_img.setVisibility(View.VISIBLE);
			 * tv_booktable_img.setVisibility(View.GONE);
			 * 
			 * } else if
			 * (Appconstants.strMenuflag.equalsIgnoreCase("BookTable")) {
			 * tv_order_food_img.setVisibility(View.GONE);
			 * tv_booktable_img.setVisibility(View.VISIBLE);
			 * 
			 * } else if (Appconstants.strMenuflag.equalsIgnoreCase("Explore"))
			 * { tv_order_food_img.setVisibility(View.GONE);
			 * tv_booktable_img.setVisibility(View.GONE); } else if
			 * (Appconstants.strMenuflag.equalsIgnoreCase("DeliveryNow")) {
			 * tv_order_food_img.setVisibility(View.VISIBLE);
			 * tv_booktable_img.setVisibility(View.GONE); } else if
			 * (Appconstants.strMenuflag.equalsIgnoreCase("HappyHrs") ||
			 * Appconstants.strMenuflag.equalsIgnoreCase("Sunday")) {
			 * tv_order_food_img.setVisibility(View.GONE);
			 * tv_booktable_img.setVisibility(View.GONE); } else if
			 * (Appconstants.strMenuflag.equalsIgnoreCase("BuffetPlace")) {
			 * tv_order_food_img.setVisibility(View.GONE);
			 * tv_booktable_img.setVisibility(View.GONE);
			 * 
			 * } else if
			 * (Appconstants.strMenuflag.equalsIgnoreCase("OrderFood")) {
			 * tv_order_food_img.setVisibility(View.VISIBLE);
			 * tv_booktable_img.setVisibility(View.GONE); }
			 */
			
			infoButtonListener = new OnInfoWindowElemTouchListener(
					tv_order_food_img, getResources().getDrawable(
							R.drawable.order_now_map), getResources()
							.getDrawable(R.drawable.order_now_map)) {
				@Override
				protected void onClickConfirmed(View v, Marker marker) {

					if (flag1 == 1) {

						int position = (Explore.vid.indexOf(tv_order_food_img
								.getTag()));
						strVenueId = restrent.get(position).id;
						strRestaurantTitle = restrent.get(position).name;
						// new getSessionsSlotsTask(strVenueId).execute();
						// startActivity(new
						// Intent(getApplicationContext(),BookTable.class));
						/*
						 * startActivity(new Intent(getApplicationContext(),
						 * OrderFood.class));
						 */
						if (ConnectivityReceiver
								.checkInternetConnection(Explore.this)) {

							new getOrderCategoriesTask().execute();

						} else {
							ConnectivityReceiver.showCustomDialog(Explore.this);
						}

						try {

						} catch (Exception e) {

						}

					}
				}
			};
			tv_order_food_img.setOnTouchListener(infoButtonListener);

			infoButtonListener = new OnInfoWindowElemTouchListener(
					tv_booktable_img, getResources().getDrawable(
							R.drawable.book_table_map), getResources()
							.getDrawable(R.drawable.book_table_map)) {
				@Override
				protected void onClickConfirmed(View v, Marker marker) {

					if (flag1 == 1) {
						int position = (Explore.vid.indexOf(tv_booktable_img
								.getTag()));

						strVenueId = restrent.get(position).id;
						strRestaurantTitle = restrent.get(position).name;
						strlat = restrent.get(position).latitude;
						strlon = restrent.get(position).longitude;
						strphone = restrent.get(position).phone;
						strRestaurantAddress = restrent.get(position).addressLine1
								+ ","
								+ restrent.get(position).addressLine2
								+ "" + restrent.get(position).location;
						strRestaurantAddress = strRestaurantAddress.replace(
								",,", ",");
						strRestaurantRating = restrent.get(position).review_rating;

						Calendar c = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						if (TextUtils.isEmpty(strBookDate)) {
							strBookDate = sdf.format(c.getTime());
							strBookDateMsg = "";
							Explore.strBookingDate = strBookDate;
							Explore.strPartySize = "2";
						} else {
							strBookDateMsg = "";
							Explore.strBookingDate = strBookDate;
							Explore.strPartySize = "2";
						}
						if (ConnectivityReceiver
								.checkInternetConnection(Explore.this)) {
							new getSessionsSlotsTask(Explore.this, strVenueId)
									.execute();

						} else {
							ConnectivityReceiver.showCustomDialog(Explore.this);
						}
					} else {
						flag1 = 1;
						// llayoutSort.setVisibility(View.GONE);
					}
				}
			};
			tv_booktable_img.setOnTouchListener(infoButtonListener);

			// Check if we were successful in obtaining the map.

			if (mMap != null) {

				// mMap.setOnMarkerClickListener(new
				// GoogleMap.OnMarkerClickListener() {
				// @Override
				// public boolean onMarkerClick(
				// com.google.android.gms.maps.model.Marker marker) {
				// System.out.println("marker" + marker.getTitle()
				// + marker.getId());
				// marker.showInfoWindow();
				//
				// return true;
				// }
				// });
				//
				// mMap.setOnInfoWindowClickListener(new
				// OnInfoWindowClickListener() {
				//
				// @Override
				// public void onInfoWindowClick(Marker marker) {
				// try {
				// MyMarker myMarker = mMarkersHashMap.get(marker);
				//
				// id = Explore.vid.get(Explore.venueNames
				// .indexOf(myMarker.getmTitle()));
				// SetData1();
				// } catch (Exception e) {
				//
				// }
				//
				// }
				//
				// });

			}

			else
				Toast.makeText(Explore.this, "Unable to create Maps",
						Toast.LENGTH_SHORT).show();
		}

	}

	public static int getPixelsFromDp(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	private void plotMarkers(ArrayList<MyMarker> markers) {
		System.out.println("size of array" + markers.size());
		if (markers.size() > 0) {
			for (MyMarker myMarker : markers) {

				MarkerOptions markerOption = new MarkerOptions()
						.position(new LatLng(myMarker.getmLatitude(), myMarker
								.getmLongitude()));
				markerOption.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.map_icn));

				Marker currentMarker = mMap.addMarker(markerOption);

				LatLng centerCoord = new LatLng(
						Double.parseDouble(Appconstants.LATTITUDE),
						Double.parseDouble(Appconstants.LANGITUDE));
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerCoord,
						15));
				mMarkersHashMap.put(currentMarker, myMarker);
				markerPoints = new ArrayList<LatLng>();

				LatLng userLocation = new LatLng(lat, lang);
				// Adding new item to the ArrayList
				markerPoints.add(userLocation);
				MarkerOptions options = new MarkerOptions();

				// Setting the position of the marker
				options.position(markerPoints.get(0));

				options.icon(
						BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
						.title("Current Location");

				// Add new marker to the Google Map Android API V2

				// mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());

				final CustomLayout mapWrapperLayout = (CustomLayout) findViewById(R.id.map_relative_layout);
 
				mapWrapperLayout.init(mMap, getPixelsFromDp(this, 39 + 20));

				mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
					@Override
					public View getInfoWindow(Marker marker) {
						return null;
					}

				    	@Override
					public View getInfoContents(Marker marker) {
						// infoTitle.setText(marker.getTitle());
						// infoSnippet.setText(marker.getSnippet());
						infoButtonListener.setMarker(marker);
						mapWrapperLayout.setMarkerWithInfoWindow(marker,
								infoWindow);
						final MyMarker myMarker = mMarkersHashMap.get(marker);
						try {
							textView1.setText(myMarker.getmTitle());
							textView2.setText(myMarker.getAddress());
							tv_order_food_img.setTag(myMarker.getId());
							tv_booktable_img.setTag(myMarker.getId());
							textView2.setVisibility(View.VISIBLE);

							System.out
									.println("orderfood_booktable values are "
											+ myMarker.getOrderfood() + ":"
											+ myMarker.getBookatable());

							if (myMarker.getOrderfood() == 1) {
								tv_order_food_img.setVisibility(View.VISIBLE);
							} else {
								tv_order_food_img.setVisibility(View.GONE);
							}
							if (myMarker.getBookatable() == 1) {
								tv_booktable_img.setVisibility(View.VISIBLE);
							} else {
								tv_booktable_img.setVisibility(View.GONE);
							}

							/*
							 * tv_order_food_img.setVisibility(View.VISIBLE);
							 * tv_booktable_img.setVisibility(View.VISIBLE);
							 */
						} catch (Exception e) {
							textView1.setText("Current Location");
							textView2.setVisibility(View.GONE);
							tv_order_food_img.setVisibility(View.GONE);
							tv_booktable_img.setVisibility(View.GONE);
						}

						return infoWindow;
					}
				});

				mMap.addMarker(options);
			}
		}
	}

	// public class MarkerInfoWindowAdapter implements
	// GoogleMap.InfoWindowAdapter {
	// private Marker mSelectedMarker;
	// private boolean mRefreshingInfoWindow;
	//
	// public MarkerInfoWindowAdapter() {
	// }
	//
	// @Override
	// public View getInfoWindow(Marker marker) {
	// return null;
	// }
	//
	// @Override
	// public View getInfoContents(final Marker marker) {
	// View v = Explore.this.getLayoutInflater().inflate(
	// R.layout.custom_map, null);
	//
	// final MyMarker myMarker = mMarkersHashMap.get(marker);
	//
	// TextView textView1 = (TextView) v.findViewById(R.id.textView1);
	// TextView textView2 = (TextView) v.findViewById(R.id.textView2);
	// tv_order_food_img = (TextView) v
	// .findViewById(R.id.tv_order_food_img);
	// tv_booktable_img = (TextView) v.findViewById(R.id.tv_booktable_img);
	// infoButtonListener = new OnInfoWindowElemTouchListener(
	// tv_order_food_img, getResources().getDrawable(
	// R.drawable.order_now_map), getResources()
	// .getDrawable(R.drawable.order_now_map)) {
	// @Override
	// protected void onClickConfirmed(View v, Marker marker) {
	//
	// Toast.makeText(Explore.this, "Order Food clicked!",
	// Toast.LENGTH_LONG).show();
	// }
	// };
	// tv_order_food_img.setOnTouchListener(infoButtonListener);
	//
	// infoButtonListener = new OnInfoWindowElemTouchListener(
	// tv_booktable_img, getResources().getDrawable(
	// R.drawable.book_table_map), getResources()
	// .getDrawable(R.drawable.book_table_map)) {
	// @Override
	// protected void onClickConfirmed(View v, Marker marker) {
	// Toast.makeText(Explore.this, "Order Food clicked!",
	// Toast.LENGTH_SHORT).show();
	// }
	// };
	// tv_booktable_img.setOnTouchListener(infoButtonListener);
	//
	// textView1.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// Toast.makeText(getApplicationContext(), "clicked", 5)
	// .show();
	// }
	// });
	// try {
	// textView1.setText(myMarker.getmTitle());
	// textView2.setText(myMarker.getAddress());
	// textView2.setVisibility(View.VISIBLE);
	//
	// } catch (Exception e) {
	// textView1.setText("Current Location");
	// textView2.setVisibility(View.GONE);
	// }
	//
	// return v;
	// }
	//
	// }

	public void SlideToAbove() {
		Animation slide = null;
		slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -5.0f);

		slide.setDuration(400);
		slide.setFillAfter(true);
		slide.setFillEnabled(true);
		llayoutSort.startAnimation(slide);

		slide.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				llayoutSort.clearAnimation();

				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						llayoutSort.getWidth(), llayoutSort.getHeight());
				// lp.setMargins(0, 0, 0, 0);
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				llayoutSort.setLayoutParams(lp);

			}

		});

	}

	public void SlideToDown() {
		Animation slide = null;
		slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 5.2f);

		slide.setDuration(400);
		slide.setFillAfter(true);
		slide.setFillEnabled(true);
		llayoutSort.startAnimation(slide);

		slide.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				llayoutSort.clearAnimation();

				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						llayoutSort.getWidth(), llayoutSort.getHeight());
				lp.setMargins(0, llayoutSort.getWidth(), 0, 0);
				lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				llayoutSort.setLayoutParams(lp);

			}

		});

	}

	class getRestaurantsList1 extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		Context context;
		String strUrl;

		public getRestaurantsList1(Context con) {
			// TODO Auto-generated constructor stub

			context = con;
			try {

				/*
				 * strUrl = Appconstants.MAIN_HOST + "orderfoodlinks/" +
				 * Appconstants.ltDo.id + "/" + Appconstants.LATTITUDE + "/" +
				 * Appconstants.LANGITUDE + "/" + Appconstants.strCityId +
				 * "/1?arg=1&searchpage="+id1+"&sort_by=distance";
				 */
				if (type == 2) {

					if (TextUtils.isEmpty(search_string)) {
						strUrl = Appconstants.MAIN_HOST + "restaurantslisting/"
								+ type + "/" + Appconstants.strCityId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/" + id1
								+ "?location=" + Search.selectedLocationId;
					} else {
						strUrl = Appconstants.MAIN_HOST + "restaurantslisting/"
								+ type + "/" + Appconstants.strCityId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/" + id1 + "?"
								+ search_string;
					}
				} else if (type == 20) {
					if (TextUtils.isEmpty(search_string)) {
						strUrl = Appconstants.MAIN_HOST + "restaurantslisting/"
								+ type + "/" + Appconstants.strCityId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/" + id1
								+ "?search_name=" + search;
					} else {
						strUrl = Appconstants.MAIN_HOST + "restaurantslisting/"
								+ type + "/" + Appconstants.strCityId + "/"
								+ Appconstants.LATTITUDE + "/"
								+ Appconstants.LANGITUDE + "/" + id1 + "?"
								+ search_string;
					}

				} else {

					if (type == 17) {
						if (TextUtils.isEmpty(search_string)) {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1
									+ "?features=8";
						} else {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1 + "?"
									+ search_string + "&" + "features=8";
							;
						}
					} else if (type == 15) {
						if (TextUtils.isEmpty(search_string)) {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1
									+ "?features=9";
						} else {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1 + "?"
									+ search_string + "&" + "features=9";
							;
						}
					} else if (type == 19) {
						if (TextUtils.isEmpty(search_string)) {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1
									+ "?features=18";
						} else {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1 + "?"
									+ search_string + "&" + "features=18";
							;
						}
					} else if (type == 11) {
						if (TextUtils.isEmpty(search_string)) {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1
									+ "?features=3";
						} else {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1 + "?"
									+ search_string + "&" + "features=3";
							;
						}
					} else if (type == 12) {
						if (TextUtils.isEmpty(search_string)) {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1
									+ "?features=15";
						} else {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1 + "?"
									+ search_string + "&" + "features=15";
							;
						}
					} else if (type == 14) {
						if (TextUtils.isEmpty(search_string)) {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1
									+ "?features=10";
						} else {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1 + "?"
									+ search_string + "&" + "features=10";
							;
						}
					} else if (type == 13) {
						if (TextUtils.isEmpty(search_string)) {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1
									+ "?features=38";
						} else {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/0/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1 + "?"
									+ search_string + "&" + "features=38";
							;
						}
					} else {
						if (TextUtils.isEmpty(search_string)) {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/" + type + "/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1;
						} else {
							strUrl = Appconstants.MAIN_HOST
									+ "restaurantslisting/" + type + "/"
									+ Appconstants.strCityId + "/"
									+ Appconstants.LATTITUDE + "/"
									+ Appconstants.LANGITUDE + "/" + id1 + "?"
									+ search_string;
						}
					}
				}
			} catch (NullPointerException e) {

				/*
				 * strUrl = Appconstants.MAIN_HOST + "orderfoodlinks/" +
				 * Appconstants.ltDo.id + "/" + Appconstants.LATTITUDE + "/" +
				 * Appconstants.LANGITUDE + "/" + Appconstants.strCityId +
				 * "/1?arg=1&searchpage="+id1+"&sort_by=distance";
				 */
				if (type == 2) {
					strUrl = Appconstants.MAIN_HOST + "restaurantslisting/"
							+ type + "/" + Appconstants.strCityId + "/"
							+ Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/" + id1 + "?location="
							+ Search.selectedLocationId;
				} else if (type == 20) {

					strUrl = Appconstants.MAIN_HOST + "restaurantslisting/"
							+ type + "/" + Appconstants.strCityId + "/"
							+ Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/" + id1
							+ "?search_name=" + search;

				} else {
					strUrl = Appconstants.MAIN_HOST + "restaurantslisting/"
							+ type + "/" + Appconstants.strCityId + "/"
							+ Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/" + id1;
				}
				// http://www.localsecrets.in/WebServices/orderfoodlinks/34/17.399799/78.476601/22960/1?arg=1&searchpage=1&sort_by=distance

			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("fetching results...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			System.out.println("url is" + strUrl);
			Appconstants.staticURL = strUrl;
			return WebServiceCalls.getJSONString(strUrl);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("restaurent result" + result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
			} else {

				if (result.contains("errormsg")) {

					try {
						Launcher.strErrorMsg = new JSONObject(result)
								.optString("errormsg");
						Launcher.userLocation = "Hyderabad";

						Explore.restJsonDetails1 = new JSONObject(result);
						System.out.println("result in if try block" + result);
						update();
						/*
						 * ((Activity) context).startActivityForResult(new
						 * Intent( context, Explore.class), 0);
						 */
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {

					try {
						Explore.restJsonDetails1 = new JSONObject(result);
						JSONObject mainJson = new JSONObject(result);

						JSONArray jsonArray = mainJson
								.getJSONArray("Restaurants");
						if (jsonArray.length() > 0) {
							flag = 1;
						} else {
							flag = 2;
						}
						System.out.println("result in else block" + result);
						update();
						/*
						 * ((Activity) context).startActivityForResult(new
						 * Intent( context, Explore.class), 0);
						 */
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		}
	}

	public void update() {
		System.out.println("pending");

		// if (ConnectivityReceiver.checkInternetConnection(Explore.this)) {

		new DisplayRestaurents1().execute();

		/*
		 * } else { ConnectivityReceiver.showCustomDialog(Explore.this); }
		 */
		/*
		 * int position = lvResults.getLastVisiblePosition();
		 * 
		 * 
		 * lvResults.setSelectionFromTop(position, 0);
		 */

	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMap();
		plotMarkers(mMyMarkersArray);
		if (type == 15) {
			tvLocation.setText("All of " + Appconstants.strCityName);
		} else {
			if (no_records_flag == 1) {
				tvLocation.setText("All of " + Appconstants.strCityName);
			} else {
				if (Appconstants.ltDo.name.equalsIgnoreCase("All of "
						+ Appconstants.strCityName)) {
					tvLocation.setText("" + Appconstants.ltDo.name);
				} else {
					tvLocation.setText("Near " + Appconstants.ltDo.name);
				}
			}
		}

		quickAction = new QuickAction(this, QuickAction.VERTICAL);
		if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
			quickAction.addActionItem(new ActionItem(ID_LOGIN, "Login",
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
							startActivity(new Intent(Explore.this, Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == ID_MYACCOUNT) {
							if (ConnectivityReceiver
									.checkInternetConnection(Explore.this)) {
								new AccountDetails(Explore.this).execute();
							} else {
								ConnectivityReceiver
										.showCustomDialog(Explore.this);
							}
						} else if (actionId == ID_RES) {
							startActivityForResult(new Intent(Explore.this,
									MyReservations.class), 1);
						} else if (actionId == ID_ORDERS) {
							startActivityForResult(new Intent(Explore.this,
									MyOrders.class), 1);
						} else if (actionId == ID_DEALS) {
							startActivityForResult(new Intent(Explore.this,
									MyDeals.class), 1);
						} else if (actionId == ID_REV) {
							startActivityForResult(new Intent(Explore.this,
									MyReviews.class), 1);
						} else if (actionId == ID_REDEMPTION) {
							startActivityForResult(new Intent(Explore.this,
									RedemptionHistory.class), 1);
						} else if (actionId == ID_FAV) {
							startActivityForResult(new Intent(Explore.this,
									MyFav.class), 1);
						} else if (actionId == ID_LOGIN) {
							startActivity(new Intent(Explore.this, Login.class));
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

	public class CustomComparatorAlpabet implements Comparator<RestaurentDO> {
		@Override
		public int compare(RestaurentDO o1, RestaurentDO o2) {
			return o1.name.compareTo(o2.name);
		}
	}

	public class CustomComparatorRating implements Comparator<RestaurentDO> {
		@Override
		public int compare(RestaurentDO one, RestaurentDO another) {
			int returnValue = 0;

			if (Integer.parseInt(one.review_rating) < Integer
					.parseInt(another.review_rating)) {
				returnValue = -1;
			} else if (Integer.parseInt(one.review_rating) > Integer
					.parseInt(another.review_rating)) {
				returnValue = 1;
			} else if (Integer.parseInt(one.review_rating) == Integer
					.parseInt(another.review_rating)) {
				returnValue = 0;
			}

			return returnValue;
		}
	}

	public class CustomComparatorDistence implements Comparator<RestaurentDO> {
		@Override
		public int compare(RestaurentDO one, RestaurentDO another) {
			int returnValue = 0;

			if (one.currentdistance < another.currentdistance) {
				returnValue = -1;
			} else if (one.currentdistance > another.currentdistance) {
				returnValue = 1;
			} else if (one.currentdistance == another.currentdistance) {
				returnValue = 0;
			}

			return returnValue;
		}
	}

	public class CustomComparatorPrice implements Comparator<RestaurentDO> {
		@Override
		public int compare(RestaurentDO one, RestaurentDO another) {
			int returnValue = 0;

			if (Integer.parseInt(one.price) < Integer.parseInt(another.price)) {
				returnValue = -1;
			} else if (Integer.parseInt(one.price) > Integer
					.parseInt(another.price)) {
				returnValue = 1;
			} else if (Integer.parseInt(one.price) == Integer
					.parseInt(another.price)) {
				returnValue = 0;
			}

			return returnValue;
		}
	}

	public class SearchAdapter extends BaseAdapter {

		private Context mContext;
		ArrayList<RestaurentDO> array;

		public SearchAdapter(Context context) {
			super();
			mContext = context;
			this.array = restrent;

		}

		@Override
		public int getCount() {
			// return the number of records in cursor
			if (array == null)
				return 0;
			return array.size();
		}

		// getView method is called for each item of ListView
		@Override
		@SuppressLint("InflateParams")
		public View getView(final int position, View view, ViewGroup parent) {
			// inflate the layout for each item of listView
			// if (view == null) {
			try {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.restaurent_row, null);
				view.setTag(array.get(position));
				if (position == getCount() - 1) {
					if (flag == 1) {
						if (ConnectivityReceiver
								.checkInternetConnection(Explore.this)) {
							new getRestaurantsList1(Explore.this).execute();
						} else {
							ConnectivityReceiver.showCustomDialog(Explore.this);
						}
						id1 = id1 + 1;
					}
				}
				// adasdsad

				// } else {
				// view.setTag(array.get(position));
				// }

				LinearLayout lyExplore = (LinearLayout) view
						.findViewById(R.id.lyExplore);
				LinearLayout lyBookTable = (LinearLayout) view
						.findViewById(R.id.lyBookTable);
				LinearLayout lyOrderFood = (LinearLayout) view
						.findViewById(R.id.lyOrderFood);

				TextView tvItem = (TextView) view
						.findViewById(R.id.tvRestaurentTitle);
				TextView tvAddressLine = (TextView) view
						.findViewById(R.id.tvAddressLine1);
				TextView tvCusineList = (TextView) view
						.findViewById(R.id.tvCusinelsit);
				TextView tvDistance = (TextView) view
						.findViewById(R.id.tvDistance);
				TextView priceRate = (TextView) view
						.findViewById(R.id.priceRate);
				TextView keyword = (TextView) view.findViewById(R.id.tvkeyword);

				LinearLayout llStars = (LinearLayout) view
						.findViewById(R.id.llStars);

				TextView btnPhoneNumber = (TextView) view
						.findViewById(R.id.btnPhoneNumber);
				RatingBar rating = (RatingBar) view.findViewById(R.id.rating);
				// RatingBar ratingBar = (RatingBar)
				// findViewById(R.id.ratingBar);
				LayerDrawable stars = (LayerDrawable) rating
						.getProgressDrawable();
				stars.getDrawable(2).setColorFilter(Color.RED,
						PorterDuff.Mode.SRC_ATOP);

				ImageView ivHasDeals = (ImageView) view
						.findViewById(R.id.ivHasDeals);
				ImageView openOrClose = (ImageView) view
						.findViewById(R.id.openOrClose);
				TextView tvOthers = (TextView) view.findViewById(R.id.tvOthers);
				TextView tvBrunchTimings = (TextView) view
						.findViewById(R.id.tvBrunchTimings);
				TextView tvHas = (TextView) view.findViewById(R.id.tvHas);
				TextView tvService = (TextView) view
						.findViewById(R.id.tvService);
				TextView tvExistingBuffet = (TextView) view
						.findViewById(R.id.tvExistingBuffet);
				TextView txtBookTable = (TextView) view
						.findViewById(R.id.txtBookTable);
				LinearLayout llDirections = (LinearLayout) view
						.findViewById(R.id.llDirections);

				LinearLayout llPhone = (LinearLayout) view
						.findViewById(R.id.llPhone);
				// sdfdsfsdfsdf
				if (Appconstants.strMenuflag.equalsIgnoreCase("FastLane")) {
					orderfood_layout.setVisibility(View.GONE);
					lyBookTable.setVisibility(View.GONE);
					lyOrderFood.setVisibility(View.VISIBLE);
					lyExplore.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.GONE);
					tvExistingBuffet.setVisibility(View.GONE);
					tvOthers.setVisibility(View.GONE);
					tvService.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.GONE);
					openOrClose.setVisibility(View.GONE);

				} else if (Appconstants.strMenuflag
						.equalsIgnoreCase("BookTable")) {
					orderfood_layout.setVisibility(View.GONE);
					lyBookTable.setVisibility(View.VISIBLE);
					lyOrderFood.setVisibility(View.GONE);
					lyExplore.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.GONE);
					tvExistingBuffet.setVisibility(View.GONE);
					tvOthers.setVisibility(View.GONE);
					tvService.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.GONE);
					openOrClose.setVisibility(View.GONE);

				} else if (Appconstants.strMenuflag.equalsIgnoreCase("Explore")) {
					orderfood_layout.setVisibility(View.GONE);
					lyBookTable.setVisibility(View.GONE);
					lyOrderFood.setVisibility(View.GONE);
					lyExplore.setVisibility(View.VISIBLE);
					tvBrunchTimings.setVisibility(View.GONE);
					tvExistingBuffet.setVisibility(View.GONE);
					tvOthers.setVisibility(View.GONE);
					tvService.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.GONE);
				} else if (Appconstants.strMenuflag
						.equalsIgnoreCase("DeliveryNow")) {
					orderfood_layout.setVisibility(View.GONE);
					lyBookTable.setVisibility(View.GONE);
					lyOrderFood.setVisibility(View.VISIBLE);
					lyExplore.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.GONE);
					tvExistingBuffet.setVisibility(View.GONE);
					tvOthers.setVisibility(View.GONE);
					tvService.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.GONE);
				} else if (Appconstants.strMenuflag
						.equalsIgnoreCase("HappyHrs")
						|| Appconstants.strMenuflag.equalsIgnoreCase("Sunday")) {
					orderfood_layout.setVisibility(View.GONE);
					lyBookTable.setVisibility(View.GONE);
					lyOrderFood.setVisibility(View.GONE);
					lyExplore.setVisibility(View.VISIBLE);
					tvBrunchTimings.setVisibility(View.GONE);
					tvExistingBuffet.setVisibility(View.GONE);
					tvOthers.setVisibility(View.VISIBLE);
					tvService.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.GONE);
				} else if (Appconstants.strMenuflag
						.equalsIgnoreCase("BuffetPlace")) {
					orderfood_layout.setVisibility(View.GONE);
					lyBookTable.setVisibility(View.GONE);
					llDirections.setVisibility(View.VISIBLE);
					llPhone.setVisibility(View.VISIBLE);
					lyOrderFood.setVisibility(View.GONE);
					lyExplore.setVisibility(View.VISIBLE);
					tvBrunchTimings.setVisibility(View.GONE);
					tvExistingBuffet.setVisibility(View.VISIBLE);
					tvOthers.setVisibility(View.GONE);
					tvService.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.VISIBLE);
					// openOrClose.setImageResource(R.drawable.open);
				} else if (Appconstants.strMenuflag
						.equalsIgnoreCase("OrderFood")) {
					orderfood_layout.setVisibility(View.GONE);
					lyBookTable.setVisibility(View.GONE);
					lyOrderFood.setVisibility(View.VISIBLE);
					lyExplore.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.GONE);
					tvExistingBuffet.setVisibility(View.GONE);
					tvOthers.setVisibility(View.GONE);
					tvService.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.GONE);
					openOrClose.setVisibility(View.GONE);

				}

				txtBookTable.setTag(view.getTag());
				txtBookTable.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (flag1 == 1) {
							strVenueId = array.get(position).id;
							strRestaurantTitle = array.get(position).name;
							strlat = array.get(position).latitude;
							strlon = array.get(position).longitude;
							strphone = array.get(position).phone;
							strRestaurantAddress = restrent.get(position).addressLine1
									+ ","
									+ restrent.get(position).addressLine2
									+ "" + restrent.get(position).location;
							strRestaurantRating = array.get(position).review_rating;

							Calendar c = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");
							if (TextUtils.isEmpty(strBookDate)) {
								strBookDate = sdf.format(c.getTime());
								strBookDateMsg = "";
								Explore.strBookingDate = strBookDate;
								Explore.strPartySize = "2";
							} else {
								strBookDateMsg = "";
								Explore.strBookingDate = strBookDate;
								Explore.strPartySize = "2";
							}
							if (ConnectivityReceiver
									.checkInternetConnection(Explore.this)) {
								new getSessionsSlotsTask(Explore.this,
										strVenueId).execute();
							} else {
								ConnectivityReceiver
										.showCustomDialog(Explore.this);
							}
						} else {
							flag1 = 1;
							llayoutSort.setVisibility(View.GONE);
						}
						// startActivity(new
						// Intent(getApplicationContext(),BookTable.class));
					}
				});

				TextView txtOrderFood = (TextView) view
						.findViewById(R.id.txtOrderFoodClick);

				txtOrderFood.setTag(view.getTag());
				txtOrderFood.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (flag1 == 1) {
							strVenueId = array.get(position).id;
							strRestaurantTitle = array.get(position).name;
							// new getSessionsSlotsTask(strVenueId).execute();
							// startActivity(new
							// Intent(getApplicationContext(),BookTable.class));
							/*
							 * startActivity(new Intent(getApplicationContext(),
							 * OrderFood.class));
							 */
							if (ConnectivityReceiver
									.checkInternetConnection(Explore.this)) {

								new getOrderCategoriesTask().execute();

							} else {
								ConnectivityReceiver
										.showCustomDialog(Explore.this);
							}
						} else {
							flag1 = 1;
							llayoutSort.setVisibility(View.GONE);
						}
					}
				});

				tvItem.setText(array.get(position).name);
				tvCusineList.setText(array.get(position).cuisines_list + "");

				if (array.get(position).cuisines_list.equalsIgnoreCase("")) {
					tvCusineList.setVisibility(View.GONE);
				} else {
					tvCusineList.setVisibility(View.VISIBLE);
				}

				tvAddressLine.setText(array.get(position).location);

				if (array.get(position).phone != null
						&& array.get(position).phone.length() > 2) {

					if (array.get(position).phone.contains(",")) {
						// String s[] = array.get(position).phone.split(",");

						// btnPhoneNumber.setText("Call    Ext No : " + s[1]);
						btnPhoneNumber.setText("Call");
					} else {
						btnPhoneNumber.setText("Call");
					}

					llPhone.setVisibility(View.VISIBLE);
				} else {
					llPhone.setVisibility(View.GONE);
				}
				// System.out.println("position"+position+array.get(position)+array.get(position).name+array.get(position).cuisines_list+array.get(position).location);

				btnPhoneNumber.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						try {
							if (array.get(position).phone.contains(",")) {
								String s[] = array.get(position).phone
										.split(",");

								// Intent callIntent = new
								// Intent(Intent.ACTION_CALL,
								// Uri.parse("tel:" + s[0]+""
								// + PhoneNumberUtils.PAUSE + "#"
								// + s[1]));

								Intent callIntent = new Intent(
										Intent.ACTION_CALL);

								callIntent.setData(Uri.parse("tel:" + s[0] + ""
										+ PhoneNumberUtils.PAUSE + "P" + s[1]
										+ ";"));
								startActivity(callIntent);
							} else {

								Intent callIntent = new Intent(
										Intent.ACTION_CALL, Uri.parse("tel:"
												+ array.get(position).phone));
								startActivity(callIntent);
							}

						} catch (ActivityNotFoundException e) {
						}
					}
				});

				if (array.get(position).lat != null) {
					llDirections.setVisibility(View.VISIBLE);
				} else {
					llDirections.setVisibility(View.GONE);
				}

				llDirections.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(
								android.content.Intent.ACTION_VIEW,
								Uri.parse("http://maps.google.com/maps?saddr="
										+ Appconstants.LATTITUDE + ","
										+ Appconstants.LANGITUDE + "&daddr="
										+ array.get(position).lat + ","
										+ array.get(position).lang));
						startActivity(intent);
					}
				});

				// if (array.get(position).currentdistance >= 1000) {

				DecimalFormat twoDForm = new DecimalFormat("#.##");

				tvDistance
						.setText(""
								+ twoDForm.format((array.get(position).currentdistance) / 1000)
								+ "\nkm");
				// }

				// else
				// tvDistance.setText("" + array.get(position).currentdistance
				// + "\nm");

				if (array.get(position).currentdistance == 0) {
					tvDistance.setText("Near\nby");
				}
				// set the price.

				int show_price = 0;
				try {
					show_price = Integer
							.parseInt(array.get(position).show_price);
				} catch (Exception e) {
					show_price = 0;
				}
				int price = 0;
				try {
					price = Integer.parseInt(array.get(position).price);
				} catch (Exception e) {
					price = 0;
				}
				System.out.println("str" + array.get(position));
				String str = "";
				if (show_price == 1) {
					priceRate.setText("" + price);
				} else {
					for (int i = 0; i < price; i++) {
						str = str + getResources().getString(R.string.oneRupee);
					}
					priceRate.setText(str);
				}

				if (TextUtils.isEmpty(str)
						&& array.get(position).review_rating
								.equalsIgnoreCase("0")
						&& !array.get(position).has_deals) {
					llStars.setVisibility(View.GONE);
				}
				// set the rating.
				if (!array.get(position).review_rating.equalsIgnoreCase("0")) {
					int rate = Integer
							.parseInt(array.get(position).review_rating);
					llStars.setVisibility(View.VISIBLE);
					System.out.println("rating" + rate
							+ array.get(position).review_rating);
					rating.setRating(rate);
				} else {
					rating.setVisibility(View.GONE);
				}

				// check has deals or not.
				if (array.get(position).has_deals) {
					llStars.setVisibility(View.VISIBLE);
					ivHasDeals.setVisibility(View.VISIBLE);
				} else {
					ivHasDeals.setVisibility(View.GONE);
				}
				// opncls
				/*
				 * if ((array.get(position).check_open).equalsIgnoreCase("0")) {
				 * openOrClose.setVisibility(View.VISIBLE);
				 * openOrClose.setImageResource(R.drawable.close);
				 * 
				 * } else { openOrClose.setImageResource(R.drawable.open);
				 * openOrClose.setVisibility(View.VISIBLE); }
				 */

				if (Appconstants.strMenuflag.equalsIgnoreCase("OrderFood")
						|| Appconstants.strMenuflag
								.equalsIgnoreCase("FastLane")
						|| Appconstants.strMenuflag
								.equalsIgnoreCase("BookTable")) {
					openOrClose.setVisibility(View.GONE);
					tvOthers.setVisibility(View.GONE);
					tvBrunchTimings.setVisibility(View.GONE);
					tvExistingBuffet.setVisibility(View.GONE);

				} else {
					if (opncls.get(position).equalsIgnoreCase("0")) {
						openOrClose.setVisibility(View.VISIBLE);
						openOrClose.setImageResource(R.drawable.close);

					} else {
						openOrClose.setImageResource(R.drawable.open);
						openOrClose.setVisibility(View.VISIBLE);
					}

					if (!array.get(position).next_sunday_brunch
							.equalsIgnoreCase("")) {

						if (!Appconstants.strMenuflag
								.equalsIgnoreCase("HappyHrs")) {
							tvOthers.setVisibility(View.VISIBLE);
							tvOthers.setText(Html
									.fromHtml("<html><body><b>Next Sunday Brunch</b>-"
											+ array.get(position).next_sunday_brunch
											+ "</body></html>"));
						}

					} else {
						tvOthers.setVisibility(View.GONE);
					}

					if (!array.get(position).brunch_timings
							.equalsIgnoreCase("")) {
						if (!Appconstants.strMenuflag
								.equalsIgnoreCase("HappyHrs")) {
							tvBrunchTimings.setVisibility(View.VISIBLE);
							tvBrunchTimings.setText("Brunch Timings-"
									+ array.get(position).brunch_timings);
						}

					} else {
						tvBrunchTimings.setVisibility(View.GONE);
					}

					if (array.get(position).happy_hours == true) {
						if (Appconstants.strMenuflag
								.equalsIgnoreCase("HappyHrs")) {
							tvOthers.setVisibility(View.VISIBLE);
							tvOthers.setText(Html
									.fromHtml("<html><body><b>Happy Hours</b>-"
											+ array.get(position).happy_hours_start
											+ " TO "
											+ array.get(position).happy_hours_end
											+ "</body></html>"));
						}
					} else {
						tvOthers.setVisibility(View.GONE);
					}

					if (!array.get(position).has_features.equalsIgnoreCase("")) {
						tvHas.setVisibility(View.VISIBLE);
						tvHas.setText("Has: "
								+ array.get(position).has_features);
					} else {
						tvHas.setVisibility(View.GONE);
					}
					if (Appconstants.strMenuflag.equalsIgnoreCase("Sunday")
							|| Appconstants.strMenuflag
									.equalsIgnoreCase("Explore")
							|| Appconstants.strMenuflag
									.equalsIgnoreCase("HappyHrs")) {

					} else {

						if (!array.get(position).existing_buffets
								.equalsIgnoreCase("")) {
							tvExistingBuffet.setVisibility(View.VISIBLE);

							tvExistingBuffet
									.setText(array.get(position).existing_buffets);
							Localsecrets.str_buffet_time = tvExistingBuffet
									.getText().toString();
							System.out.println("buffet timings"
									+ array.get(position).existing_buffets);

						} else {
							tvExistingBuffet.setVisibility(View.GONE);
						}
					}
				}
				System.out.println("bunch"
						+ array.get(position).next_sunday_brunch
						+ Appconstants.strMenuflag);
				if (!Appconstants.strMenuflag.equalsIgnoreCase("HappyHrs")) {
					if (!array.get(position).serving_items.equalsIgnoreCase("")) {
						tvService.setVisibility(View.VISIBLE);
						tvService.setText("Time : "
								+ array.get(position).serving_items);
					} else {
						tvService.setVisibility(View.GONE);
					}
				} else {

					tvService.setVisibility(View.GONE);
				}
				if (!array.get(position).keyword.equalsIgnoreCase("")) {
					keyword.setVisibility(View.VISIBLE);
					keyword.setText("Keyword: " + array.get(position).keyword);
				} else {
					keyword.setVisibility(View.GONE);
				}

				priceRate.setVisibility(View.VISIBLE);

			} catch (Exception e) {

			}
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void refresh(ArrayList<RestaurentDO> array) {

			this.array = array;
			this.notifyDataSetChanged();
		}
	}

	String errorMsg = "";
	String restType;

	private class DisplayRestaurents extends
			AsyncTask<String, ArrayList<RestaurentDO>, ArrayList<RestaurentDO>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoader();
		}

		@Override
		protected ArrayList<RestaurentDO> doInBackground(String... params) {

			restrent = new ArrayList<RestaurentDO>();

			try {

				if (Launcher.restJsonDetails.has("Restaurants")) {
					Launcher.jsonArrayRestaurants = Launcher.restJsonDetails
							.getJSONArray("Restaurants");

					llExplore.setVisibility(View.VISIBLE);
					llNoResult.setVisibility(View.GONE);

					vid.clear();
					opncls.clear();
					venueNames.clear();

					latitude.clear();
					longitude.clear();
					phone.clear();
					address.clear();
					bookatable.clear();
					orderfood.clear();
					for (int i = 0; i < Launcher.jsonArrayRestaurants.length(); i++) {

						JSONObject objJson = Launcher.jsonArrayRestaurants
								.getJSONObject(i);

						objItem = new RestaurentDO();

						if (!objJson.isNull("Venueid")) {
							objItem.id = objJson.getString("Venueid");
							vid.add(objJson.getString("Venueid"));
						}
						if (!objJson.isNull("orderfood")) {
							objItem.orderfood = objJson.getString("orderfood");
							orderfood.add(objJson.getString("orderfood"));
						}
						if (!objJson.isNull("booktable")) {
							objItem.bookatable = objJson.getString("booktable");
							bookatable.add(objJson.getString("booktable"));
						}
						if (!objJson.isNull("VenueName")) {
							objItem.name = objJson.getString("VenueName");
							venueNames.add(objJson.getString("VenueName"));
						}
						if (!objJson.isNull("address1")
								&& !objJson.isNull("address2")) {
							objItem.addressLine1 = objJson
									.getString("address1")
									+ ","
									+ objJson.getString("address2");

							address.add(objJson.getString("address1") + ","
									+ objJson.getString("address2") + ","
									+ objJson.getString("Location"));
						}
						if (!objJson.isNull("Location")) {
							objItem.location = objJson.getString("Location");
						}
						if (!objJson.isNull("cuisines_list")) {
							objItem.cuisines_list = objJson
									.getString("cuisines_list");
						}
						if (!objJson.isNull("latitude")) {
							objItem.latitude = objJson.getString("latitude");

							latitude.add(Double.parseDouble(objJson
									.getString("latitude")));
						}
						if (!objJson.isNull("longitude")) {
							objItem.longitude = objJson.getString("longitude");
							longitude.add(Double.parseDouble(objJson
									.getString("longitude")));
						}
						if (!objJson.isNull("keyword")) {
							objItem.keyword = objJson.getString("keyword");

						}

						if (!objJson.isNull("review_rating")) {
							objItem.review_rating = objJson
									.getString("review_rating");
						}
						if (!objJson.isNull("currentdistance")) {

							// String distence = "0";
							float distencef = Float.parseFloat(objJson
									.getString("currentdistance"));
							int k = (int) (distencef * 1000);

							objItem.currentdistance = k;
						}
						if (!objJson.isNull("phone")) {
							objItem.phone = objJson.getString("phone").replace(
									"- Ext: ", ",");
						}
						if (!objJson.isNull("hasdeals")) {
							objItem.has_deals = objJson.getBoolean("hasdeals");
						}
						// if (!objJson.isNull("booktable")) {
						// objItem.has_booktable =
						// objJson.getBoolean("booktable");
						// }
						// if (!objJson.isNull("orderfood")) {
						// objItem.has_orderfood =
						// objJson.getBoolean("orderfood");
						// }
						if (!objJson.isNull("averagemealpertwo")) {
							objItem.price = objJson
									.getString("averagemealpertwo");
						}
						if (!objJson.isNull("show_price")) {
							objItem.show_price = objJson
									.getString("show_price");
						}
						if (!objJson.isNull("latitude")) {
							objItem.lat = objJson.getString("latitude");
						}
						if (!objJson.isNull("longitude")) {
							objItem.lang = objJson.getString("longitude");
						}

						if (!objJson.isNull("openstatus")) {
							objItem.check_open = objJson
									.getString("openstatus");
							opncls.add(objJson.getString("openstatus"));
						}

						if (!objJson.isNull("next_sunday_brunch")) {
							objItem.next_sunday_brunch = objJson
									.getString("next_sunday_brunch");
						}

						if (!objJson.isNull("brunch_timings")) {
							objItem.brunch_timings = objJson
									.getString("brunch_timings");
						}

						if (!objJson.isNull("happy_hours")) {
							objItem.happy_hours = objJson
									.getBoolean("happy_hours");
							if (objItem.happy_hours == true) {
								objItem.happy_hours_start = objJson
										.getString("happy_hours_start");
								objItem.happy_hours_end = objJson
										.getString("happy_hours_end");
							}
						}

						if (!objJson.isNull("has_features")) {
							objItem.has_features = objJson
									.getString("has_features");
						}

						if (!objJson.isNull("serving_timings")) {
							objItem.serving_items = objJson
									.getString("serving_timings");
						}

						if (!objJson.isNull("existing_buffets")) {
							String str = objJson.getString("existing_buffets");
							String values = "";
							if (str.length() > 0 && str.startsWith("[")) {
								try {
									JSONArray jarray = new JSONArray(str);
									for (int j = 0; j < jarray.length(); j++) {
										JSONObject jObject = jarray
												.getJSONObject(j);
										values = values
												+ jObject.getString("name")
												+ ":"
												+ jObject
														.getString("serving_timings")
												+ "("
												+ jObject.getString("opened")
												+ ")";

										if (j != jarray.length() - 1) {
											values = values + "\n";
										}

									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							objItem.existing_buffets = values;
						}

						restrent.add(objItem);

					}

					if (Launcher.jsonArrayRestaurants.length() == 1) {
						llSort.setVisibility(View.GONE);
						llFilter.setVisibility(View.GONE);
						LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
								android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
								android.view.ViewGroup.LayoutParams.MATCH_PARENT);
						params1.weight = 3.0f;
						llMap.setLayoutParams(params1);
					}

					Launcher.features = Launcher.restJsonDetails
							.getJSONArray("features");
					Launcher.locations_list = Launcher.restJsonDetails
							.getJSONArray("locations_list");
					Launcher.existing_cusines = Launcher.restJsonDetails
							.getJSONArray("existing_cusines");
					Launcher.pricelist = Launcher.restJsonDetails
							.getString("pricelist");

					if (Launcher.userLocation.equalsIgnoreCase("Hyderabad")) {
						if (Launcher.restJsonDetails.has("errMsg")) {
							errorMsg = Launcher.restJsonDetails
									.getString("errMsg");
						}

					}

				} else {
					llExplore.setVisibility(View.GONE);
					llNoResult.setVisibility(View.VISIBLE);
				}

			} catch (JSONException e) {
				System.out.println("no values found");
				llExplore.setVisibility(View.GONE);
				llNoResult.setVisibility(View.VISIBLE);
				e.printStackTrace();
			}
			System.out.println("size" + restrent.size());
			if (restrent.size() > 9) {
				flag = 1;
			} else {
				flag = 2;
			}

			return restrent;
		}

		@Override
		protected void onPostExecute(ArrayList<RestaurentDO> result) {
			super.onPostExecute(result);

			hideLoader();
			// dummyRestrent = (ArrayList<RestaurentDO>) result.clone();
			adapter = new SearchAdapter(Explore.this);
			lvResults.setAdapter(adapter);
			if (map_flag == 2) {
				mMyMarkersArray.clear();
				mMarkersHashMap = new HashMap<Marker, MyMarker>();
				System.out.println("lengths are " + Explore.venueNames.size()
						+ ":" + Explore.venueNames.size());
				for (int i = 0; i < Explore.venueNames.size(); i++) {

					mMyMarkersArray.add(new MyMarker(Explore.venueNames.get(i),
							Explore.address.get(i), Explore.latitude.get(i),
							Explore.longitude.get(i), Explore.vid.get(i),
							Integer.parseInt(Explore.orderfood.get(i)), Integer
									.parseInt(Explore.bookatable.get(i))));

				}

				mMap.clear();
				mMap = null;
				setUpMap();
				map_flag = 1;
				plotMarkers(mMyMarkersArray);
				map_view.setText("List View");
				map_list.setImageResource(R.drawable.list);
			}

			if (!errorMsg.equalsIgnoreCase("")) {
				rlError = (RelativeLayout) findViewById(R.id.rlErrorMsg);
				rlError.setVisibility(View.VISIBLE);

				TextView tvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
				tvErrorMsg.setText(errorMsg);

				ImageView ivCross = (ImageView) findViewById(R.id.ivCross);
				ivCross.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						rlError.setVisibility(View.GONE);

					}
				});
			}

		}

	}

	private class DisplayRestaurents1 extends
			AsyncTask<String, ArrayList<RestaurentDO>, ArrayList<RestaurentDO>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoader();
		}

		@Override
		protected ArrayList<RestaurentDO> doInBackground(String... params) {

			// restrent = new ArrayList<RestaurentDO>();

			try {

				if (Explore.restJsonDetails1.has("Restaurants")) {
					llExplore.setVisibility(View.VISIBLE);
					llNoResult.setVisibility(View.GONE);

					Explore.jsonArrayRestaurants1 = Explore.restJsonDetails1
							.getJSONArray("Restaurants");
					/*
					 * vid.clear(); opncls.clear(); venueNames.clear();
					 */
					for (int i = 0; i < Explore.jsonArrayRestaurants1.length(); i++) {

						JSONObject objJson = Explore.jsonArrayRestaurants1
								.getJSONObject(i);

						System.out.println("objJson"
								+ objJson.getString("Venueid")
								+ objJson.getString("VenueName"));
						objItem = new RestaurentDO();

						if (!objJson.isNull("Venueid")) {
							objItem.id = objJson.getString("Venueid");
							vid.add(objJson.getString("Venueid"));
						}
						if (!objJson.isNull("orderfood")) {
							objItem.orderfood = objJson.getString("orderfood");
							orderfood.add(objJson.getString("orderfood"));
						}
						if (!objJson.isNull("booktable")) {
							objItem.bookatable = objJson.getString("booktable");
							bookatable.add(objJson.getString("booktable"));
						}
						if (!objJson.isNull("VenueName")) {
							objItem.name = objJson.getString("VenueName");
							venueNames.add(objJson.getString("VenueName"));
						}
						if (!objJson.isNull("address1")
								&& !objJson.isNull("address2")) {
							objItem.addressLine1 = objJson
									.getString("address1")
									+ ","
									+ objJson.getString("address2");
							address.add(objJson.getString("address1") + ","
									+ objJson.getString("address2") + ","
									+ objJson.getString("Location"));
						}
						if (!objJson.isNull("Location")) {
							objItem.location = objJson.getString("Location");
						}
						if (!objJson.isNull("cuisines_list")) {
							objItem.cuisines_list = objJson
									.getString("cuisines_list");
						}

						if (!objJson.isNull("latitude")) {
							objItem.latitude = objJson.getString("latitude");

							latitude.add(Double.parseDouble(objJson
									.getString("latitude")));
						}
						if (!objJson.isNull("longitude")) {
							objItem.longitude = objJson.getString("longitude");
							longitude.add(Double.parseDouble(objJson
									.getString("longitude")));
						}
						if (!objJson.isNull("keyword")) {
							objItem.keyword = objJson.getString("keyword");

						}
						if (!objJson.isNull("review_rating")) {
							objItem.review_rating = objJson
									.getString("review_rating");
						}
						if (!objJson.isNull("currentdistance")) {

							// String distence = "0";
							float distencef = Float.parseFloat(objJson
									.getString("currentdistance"));
							int k = (int) (distencef * 1000);

							objItem.currentdistance = k;
						}
						if (!objJson.isNull("phone")) {
							objItem.phone = objJson.getString("phone").replace(
									"- Ext: ", ",");
						}
						if (!objJson.isNull("hasdeals")) {
							objItem.has_deals = objJson.getBoolean("hasdeals");
						}
						// if (!objJson.isNull("booktable")) {
						// objItem.has_booktable =
						// objJson.getBoolean("booktable");
						// }
						// if (!objJson.isNull("orderfood")) {
						// objItem.has_orderfood =
						// objJson.getBoolean("orderfood");
						// }
						if (!objJson.isNull("averagemealpertwo")) {
							objItem.price = objJson
									.getString("averagemealpertwo");
						}
						if (!objJson.isNull("show_price")) {
							objItem.show_price = objJson
									.getString("show_price");
						}
						if (!objJson.isNull("latitude")) {
							objItem.lat = objJson.getString("latitude");
						}
						if (!objJson.isNull("longitude")) {
							objItem.lang = objJson.getString("longitude");
						}

						if (!objJson.isNull("openstatus")) {
							objItem.check_open = objJson
									.getString("openstatus");
							opncls.add(objJson.getString("openstatus"));
						}

						if (!objJson.isNull("next_sunday_brunch")) {
							objItem.next_sunday_brunch = objJson
									.getString("next_sunday_brunch");
						}

						if (!objJson.isNull("brunch_timings")) {
							objItem.brunch_timings = objJson
									.getString("brunch_timings");
						}

						if (!objJson.isNull("happy_hours")) {
							objItem.happy_hours = objJson
									.getBoolean("happy_hours");
							if (objItem.happy_hours == true) {
								objItem.happy_hours_start = objJson
										.getString("happy_hours_start");
								objItem.happy_hours_end = objJson
										.getString("happy_hours_end");
							}
						}

						if (!objJson.isNull("has_features")) {
							objItem.has_features = objJson
									.getString("has_features");
						}

						if (!objJson.isNull("serving_timings")) {
							objItem.serving_items = objJson
									.getString("serving_timings");
						}

						if (!objJson.isNull("existing_buffets")) {
							String str = objJson.getString("existing_buffets");
							String values = "";
							if (str.length() > 0 && str.startsWith("[")) {
								try {
									JSONArray jarray = new JSONArray(str);
									for (int j = 0; j < jarray.length(); j++) {
										JSONObject jObject = jarray
												.getJSONObject(j);
										values = values
												+ jObject.getString("name")
												+ ":"
												+ jObject
														.getString("serving_timings")
												+ "("
												+ jObject.getString("opened")
												+ ")";

										if (j != jarray.length() - 1) {
											values = values + "\n";
										}

									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							objItem.existing_buffets = values;
						}

						restrent.add(objItem);

					}

					if (Launcher.jsonArrayRestaurants.length() == 1) {
						llSort.setVisibility(View.GONE);
						llFilter.setVisibility(View.GONE);
					}

					Launcher.features = Launcher.restJsonDetails
							.getJSONArray("features");
					Launcher.locations_list = Launcher.restJsonDetails
							.getJSONArray("locations_list");
					Launcher.existing_cusines = Launcher.restJsonDetails
							.getJSONArray("existing_cusines");

					Launcher.pricelist = Launcher.restJsonDetails
							.getString("pricelist");

					if (Launcher.userLocation.equalsIgnoreCase("Hyderabad")) {
						if (Launcher.restJsonDetails.has("errMsg")) {
							errorMsg = Launcher.restJsonDetails
									.getString("errMsg");
						}

					}
				} else {
					/*
					 * llExplore.setVisibility(View.GONE);
					 * llNoResult.setVisibility(View.VISIBLE);
					 */
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

			return restrent;
		}

		@Override
		protected void onPostExecute(ArrayList<RestaurentDO> result) {
			super.onPostExecute(result);

			hideLoader();
			// dummyRestrent = (ArrayList<RestaurentDO>) result.clone();
			/*
			 * adapter = new SearchAdapter(Explore.this, restrent);
			 * lvResults.setAdapter(adapter);
			 */

			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					adapter = new SearchAdapter(Explore.this);
					int currentPosition = lvResults.getFirstVisiblePosition();
					System.out.println("possssssssssssssss" + currentPosition);
					lvResults.setAdapter(adapter);
					lvResults.setSelection(currentPosition + 1);
					/*
					 * if (currentPosition > 1) {
					 * lvResults.setSelectionFromTop(currentPosition, 0); //
					 * adapter.notifyDataSetChanged(); }
					 */

					// lvResults.setAdapter(adapter);

				}
			});
			if (!errorMsg.equalsIgnoreCase("")) {
				rlError = (RelativeLayout) findViewById(R.id.rlErrorMsg);
				rlError.setVisibility(View.VISIBLE);

				TextView tvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
				tvErrorMsg.setText(errorMsg);

				ImageView ivCross = (ImageView) findViewById(R.id.ivCross);
				ivCross.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						rlError.setVisibility(View.GONE);

					}
				});
			}

		}

	}

	void refreshList() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				/*
				 * int position = lvResults.getLastVisiblePosition();
				 * lvResults.setSelectionFromTop(position, 0); adapter = new
				 * SearchAdapter(Explore.this); lvResults.setAdapter(adapter);
				 * //lvResults.requestLayout();
				 * 
				 * adapter.notifyDataSetChanged();
				 */
				/*
				 * int position = lvResults.getLastVisiblePosition();
				 * System.out.println("possssssssssssssss"+position);
				 * 
				 * //adapter.notifyDataSetChanged();
				 * lvResults.setSelectionFromTop(position, 0);
				 */

				adapter = new SearchAdapter(Explore.this);
				int currentPosition = lvResults.getFirstVisiblePosition();
				System.out.println("possssssssssssssss" + currentPosition);
				lvResults.setAdapter(adapter);
				if (currentPosition > 1) {
					lvResults.setSelectionFromTop(currentPosition, 0);
					// adapter.notifyDataSetChanged();
				}

				// lvResults.setAdapter(adapter);

			}
		});
	}

	static class getSessionsSlotsTask extends AsyncTask<String, Void, String> {

		String strVenueId1;
		Context context;

		public getSessionsSlotsTask(Context con, String strVenueId) {
			// TODO Auto-generated constructor stub
			strVenueId1 = strVenueId;
			context = con;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog1 = new ProgressDialog(context);
			pDialog1.setMessage("Loading ...");
			pDialog1.setCancelable(false);
			pDialog1.show();
		}

		@Override
		protected String doInBackground(String... params) {
			strBookDate1 = strBookDate;
			System.out.println("url is " + Appconstants.MAIN_HOST
					+ "getAvailableSessionsList/" + strVenueId1 + "/"
					+ strBookDate);
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "getAvailableSessionsList/" + strVenueId1 + "/"
					+ strBookDate);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			/*
			 * if (null != pDialog1 && pDialog1.isShowing()) {
			 * pDialog1.dismiss(); }
			 */
			if (!((Activity) context).isFinishing()) {
				if (null != pDialog1 && pDialog1.isShowing()) {
					pDialog1.dismiss();
				}
			}

			if (null == result || result.length() == 0) {
				Log.e("", "" + result);
				if (null != pDialog1 && pDialog1.isShowing()) {
					pDialog1.dismiss();
				}
			} else {
				Appconstants.booktableres = result;
				try {
					JSONObject jObj = new JSONObject(Appconstants.booktableres);
					String strSession, strSession1;
					if (Integer.parseInt(jObj.optString("errorCode")) == 0) {
						strSession1 = jObj.optString("sessions");
						if (strSession1.contains(",")) {
							String[] strSlots = strSession1.split(",");

							strSession = strSlots[0];

						} else {
							strSession = strSession1;
						}
						if (TextUtils.isEmpty(strSessionName)) {
							if (ConnectivityReceiver
									.checkInternetConnection(context)) {
								new getSlotsTaskTask(context, strSession,
										strVenueId1).execute();
							} else {
								ConnectivityReceiver.showCustomDialog(context);
							}
						} else {
							if (ConnectivityReceiver
									.checkInternetConnection(context)) {
								new getSlotsTaskTask(context, strSessionName,
										strVenueId1).execute();
							} else {
								ConnectivityReceiver.showCustomDialog(context);
							}
						}

					} else {
						pDialog1.dismiss();
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	}

	static class getSlotsTaskTask extends AsyncTask<String, Void, String> {

		String strSession1, strVenueId1;
		Context context;

		public getSlotsTaskTask(Context con, String strSession,
				String strVenueId) {
			// TODO Auto-generated constructor stub
			context = con;
			strSession1 = strSession;
			strVenueId1 = strVenueId;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// pDialog1 = new ProgressDialog(context);
			// pDialog1.setMessage("Loading ...");
			// pDialog1.setCancelable(false);
			// pDialog1.show();

			// pDialog = new ProgressDialog(context);
			// pDialog.setMessage("Loading ...");
			// pDialog.setCancelable(false);
			// pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			System.out.println("slots" + Appconstants.MAIN_HOST + "getslots/"
					+ strVenueId1 + "/" + strSession1 + "/" + strBookDate + "/"
					+ strPartySize);
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "getslots/" + strVenueId1 + "/" + strSession1 + "/"
					+ strBookDate + "/" + strPartySize);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (!((Activity) context).isFinishing()) {
				if (null != pDialog1 && pDialog1.isShowing()) {
					pDialog1.dismiss();
				}
			}

			if (null == result || result.length() == 0) {
				Log.e("", "" + result);
				if (null != pDialog1 && pDialog1.isShowing()) {
					pDialog1.dismiss();
				}
			} else {

				if (!((Activity) context).isFinishing()) {
					if (null != pDialog1 && pDialog1.isShowing()) {
						pDialog1.dismiss();
					}
				}
				try {
					JSONObject jObj1 = new JSONObject(result);
					if (jObj1.has("errorCode")) {
						BaseActivity.showDialogMsg(context,
								jObj1.optString("errorCode"));
					} else {
						Appconstants.booktableSlotsres = result;
						/*
						 * try{ if (!((Activity) context).isFinishing()) {
						 * //((Activity) context).finish(); Activity activity =
						 * (Activity) context;
						 * System.out.println("context"+activity); }
						 * }catch(Exception e){
						 * 
						 * }
						 */
						context.startActivity(new Intent(context,
								BookTable.class));
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	}

	class getRestaurantsAllList extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		Context context;
		String strUrl;

		public getRestaurantsAllList(Context con, String strUrl1) {
			// TODO Auto-generated constructor stub

			context = con;
			strUrl = strUrl1;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			return WebServiceCalls.getJSONString(strUrl);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (!((Activity) context).isFinishing()) {
				if (null != pDialog && pDialog.isShowing()) {
					pDialog.dismiss();
				}
			}

			if (null == result || result.length() == 0) {
			} else {

				if (result.contains("errorMessage")) {

					rlError = (RelativeLayout) findViewById(R.id.rlErrorMsg);
					rlError.setVisibility(View.VISIBLE);

					TextView tvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
					tvErrorMsg.setText(errorMsg);

					ImageView ivCross = (ImageView) findViewById(R.id.ivCross);
					ivCross.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							rlError.setVisibility(View.GONE);

						}
					});

					// try {
					// String strErrMsg = new JSONObject(result)
					// .optString("errorMessage");
					// AlertDialog.Builder alertDialogBuilder = new
					// AlertDialog.Builder(
					// context);
					//
					// // set title
					// // alertDialogBuilder.setTitle("Your Title");
					//
					// // set dialog message
					// alertDialogBuilder
					// .setMessage(strErrMsg)
					// .setCancelable(false)
					// .setPositiveButton("Yes",
					// new DialogInterface.OnClickListener() {
					// public void onClick(
					// DialogInterface dialog,
					// int id) {
					// Launcher.strTitleFlag = "1";
					// // new
					// // getRestaurantsList(context,strUrl1).execute();
					// }
					// })
					// .setNegativeButton("No",
					// new DialogInterface.OnClickListener() {
					// public void onClick(
					// DialogInterface dialog,
					// int id) {
					// // if this button is clicked,
					// // just close
					// // the dialog box and do nothing
					// dialog.cancel();
					// }
					// });
					//
					// // create alert dialog
					// AlertDialog alertDialog = alertDialogBuilder.create();
					//
					// // show it
					// alertDialog.show();
					// } catch (JSONException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					// }
				} else {
					try {
						Launcher.restJsonDetails = new JSONObject(result);
						// if
						// (ConnectivityReceiver.checkInternetConnection(Explore.this))
						// {
						new DisplayRestaurents().execute();
						/*
						 * } else {
						 * ConnectivityReceiver.showCustomDialog(Explore.this);
						 * }
						 */
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tvAlbha:

			Collections.sort(restrent, new CustomComparatorAlpabet());
			adapter.refresh(restrent);

			llayoutSort.setVisibility(View.GONE);
			llExplore.setVisibility(View.VISIBLE);
			iv_menubar1.setImageResource(R.drawable.check);
			iv_menubar2.setImageResource(R.drawable.un_check);
			iv_menubar3.setImageResource(R.drawable.un_check);
			iv_menubar4.setImageResource(R.drawable.un_check);

			/*
			 * tvAlbha.setBackgroundResource(R.drawable.
			 * textfield_background_sort_with_color);
			 * tvRating.setBackgroundResource
			 * (R.drawable.textfield_background_search_details); tvDistence
			 * .setBackgroundResource
			 * (R.drawable.textfield_background_search_details);
			 * tvPrice.setBackgroundResource
			 * (R.drawable.textfield_background_search_details);
			 */

			break;
		/*
		 * case R.id.tvCancel: flag1=1; llayoutSort.setVisibility(View.GONE);
		 * llExplore.setVisibility(View.VISIBLE);
		 * 
		 * break;
		 */
		case R.id.tvRating:

			Collections.sort(restrent, new CustomComparatorRating());
			Collections.reverse(restrent);
			adapter.refresh(restrent);

			llayoutSort.setVisibility(View.GONE);
			llExplore.setVisibility(View.VISIBLE);

			iv_menubar1.setImageResource(R.drawable.un_check);
			iv_menubar2.setImageResource(R.drawable.check);
			iv_menubar3.setImageResource(R.drawable.un_check);
			iv_menubar4.setImageResource(R.drawable.un_check);
			/*
			 * tvAlbha.setBackgroundResource(R.drawable.
			 * textfield_background_search_details);
			 * tvRating.setBackgroundResource
			 * (R.drawable.textfield_background_sort_with_color); tvDistence
			 * .setBackgroundResource
			 * (R.drawable.textfield_background_search_details);
			 * tvPrice.setBackgroundResource
			 * (R.drawable.textfield_background_search_details);
			 */

			break;

		case R.id.tvDistence:

			Collections.sort(restrent, new CustomComparatorDistence());
			// Collections.reverse(restrent);
			adapter.refresh(restrent);

			llayoutSort.setVisibility(View.GONE);
			llExplore.setVisibility(View.VISIBLE);
			iv_menubar1.setImageResource(R.drawable.un_check);
			iv_menubar2.setImageResource(R.drawable.un_check);
			iv_menubar3.setImageResource(R.drawable.check);
			iv_menubar4.setImageResource(R.drawable.un_check);
			/*
			 * tvAlbha.setBackgroundResource(R.drawable.
			 * textfield_background_search_details);
			 * tvRating.setBackgroundResource
			 * (R.drawable.textfield_background_search_details); tvDistence
			 * .setBackgroundResource
			 * (R.drawable.textfield_background_sort_with_color);
			 * tvPrice.setBackgroundResource
			 * (R.drawable.textfield_background_search_details);
			 */

			break;
		case R.id.tvPrice:

			Collections.sort(restrent, new CustomComparatorPrice());
			adapter.refresh(restrent);

			llayoutSort.setVisibility(View.GONE);
			llExplore.setVisibility(View.VISIBLE);

			iv_menubar1.setImageResource(R.drawable.un_check);
			iv_menubar2.setImageResource(R.drawable.un_check);
			iv_menubar3.setImageResource(R.drawable.un_check);
			iv_menubar4.setImageResource(R.drawable.check);
			/*
			 * tvAlbha.setBackgroundResource(R.drawable.
			 * textfield_background_search_details);
			 * tvRating.setBackgroundResource
			 * (R.drawable.textfield_background_search_details); tvDistence
			 * .setBackgroundResource
			 * (R.drawable.textfield_background_search_details);
			 * tvPrice.setBackgroundResource
			 * (R.drawable.textfield_background_sort_with_color);
			 */

			break;

		case R.id.btnBrowseRestaurants:

			// Appconstants.strMenuflag = Appconstants.strMenuflag;
			// Localsecrets.str_titles = "Restaurants";

			// wednesday
			// String s4 = "";
			try {
				Launcher.userLocation = Appconstants.ltDo.name;

				// if (Search.selectedLocationId.equalsIgnoreCase("null")) {
				// s4 = Appconstants.ltDo.id;
				// } else {
				// s4 = Search.selectedLocationId;
				//
				// }
				Launcher.getRestaurent = Appconstants.MAIN_HOST
						+ "restaurantslisting/" + type + "/"
						+ Appconstants.strCityId + "/" + Appconstants.LATTITUDE
						+ "/" + Appconstants.LANGITUDE + "/1";

				if (type == 2) {
					no_records_flag = 1;
				}
				/*
				 * Launcher.getRestaurent = Appconstants.MAIN_HOST +
				 * "globalsearchmenu/" + Appconstants.strCityId + "/" + s4 + "/"
				 * + Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE +
				 * "/0?arg=1&searchpage=1&sort_by=distance";
				 */

			} catch (NullPointerException e) {
				Launcher.getRestaurent = Appconstants.MAIN_HOST
						+ "restaurantslisting/" + type + "/"
						+ Appconstants.strCityId + "/" + Appconstants.LATTITUDE
						+ "/" + Appconstants.LANGITUDE + "/1";
				if (type == 2) {
					no_records_flag = 1;
				}
				/*
				 * Launcher.getRestaurent = Appconstants.MAIN_HOST +
				 * "globalsearchmenu/" + Appconstants.strCityId + "/" + s4 + "/"
				 * + Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE +
				 * "/0?arg=1&searchpage=1&sort_by=distance";
				 */

			}

			System.out.println("url is" + Launcher.getRestaurent);
			if (ConnectivityReceiver.checkInternetConnection(Explore.this)) {
				new RestaurantsList(v.getContext(), Launcher.getRestaurent)
						.execute();
			} else {
				ConnectivityReceiver.showCustomDialog(Explore.this);
			}

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0 && resultCode == 1011) {
			Collections.sort(restrent, new CustomComparatorAlpabet());
			adapter.refresh(restrent);
		} else if (requestCode == 0 && resultCode == 1012) {
			Collections.sort(restrent, new CustomComparatorRating());
			Collections.reverse(restrent);
			adapter.refresh(restrent);
		} else if (requestCode == 0 && resultCode == 1013) {
			// adapter.refresh(dummyRestrent);
		} else if (requestCode == 0 && resultCode == 1014) {
			Collections.sort(restrent, new CustomComparatorPrice());
			adapter.refresh(restrent);
		} else if (requestCode == 1 && resultCode == 1015) {
			if (ConnectivityReceiver.checkInternetConnection(Explore.this)) {
				new getRestaurantsAllList(Explore.this,
						data.getStringExtra("url")).execute();
			} else {
				ConnectivityReceiver.showCustomDialog(Explore.this);
			}
		} else if (requestCode == 5 && resultCode == 1016) {
			Intent inExplore = new Intent(Explore.this, Search.class)
					.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(inExplore);
			finish();
		} else if (requestCode == 1 && resultCode == 10116) {
			setResult(1012);
			finish();
		} else if (requestCode == 1 && resultCode == 10117) {

			Intent inExplore = new Intent(Explore.this, Search.class)
					.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(inExplore);
			finish();

		} else if (requestCode == 1 && resultCode == 10118) {
			startActivity(new Intent(Explore.this, Login.class));
		} else if (requestCode == 1 && resultCode == 10119) {
			startActivity(new Intent(Explore.this, Login.class));
			finish();
		}
	}

	public static class AccountDetails extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		Context con;

		public AccountDetails(Context context) {
			con = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(con);
			pDialog.setMessage("Loading your account details. Please wait ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "myAccount/" + PreferenceUtils.getUserSession());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {

			} else {

				try {

					JSONObject jObject = new JSONObject(result);
					Launcher.accountDetailsArray = jObject
							.getJSONArray("userdetails");

					if (result.contains("ErrorMessage")) {
						String errmsg = Launcher.accountDetailsArray
								.getJSONObject(0).getString("ErrorMessage")
								.toString();
						BaseActivity.showDialogMsg(con, errmsg);
					} else {

						((Activity) con)
								.startActivityForResult(
										new Intent(con, MyAccount.class)
												.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),
										1);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
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

		if (sortFlag) {
			sortFlag = false;
			llayoutSort.setVisibility(View.GONE);
			llExplore.setVisibility(View.VISIBLE);
		} else {

			/*
			 * if (no_records_flag == 1){ no_records_flag=0; sfsdfsfsd }else{
			 */
			Explore.this.finish();

			// if(Appconstants.strMenuflag.equalsIgnoreCase("Fastlane")){
			// startActivity(new Intent(Explore.this,Launcher.class));
			// }
			// else
			if (Appconstants.strMenuflag.equalsIgnoreCase("Orderfood")
					|| Appconstants.strMenuflag.equalsIgnoreCase("DeliveryNow")) {
				startActivity(new Intent(Explore.this, OrderList.class));
			} else

			if (Appconstants.strMenuflag.equalsIgnoreCase("Explore")) {
				startActivity(new Intent(Explore.this, Search.class));
			} else {
				startActivity(new Intent(Explore.this, Launcher.class));
			}
		}
		// }
		return;
	}

	public void SetData() {

		if (Localsecrets.search_details_flag == 1) {
			url1 = Localsecrets.det_fl;
			if (Localsecrets.det_fl.length() > 0) {

				if (ConnectivityReceiver.checkInternetConnection(Explore.this)) {

					new DeatilsTasks().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(Explore.this);
				}
				Localsecrets.search_details_flag = 0;
			} else if (Explore.strVenueId.length() > 0) {
				// url1
				Localsecrets.det_fl = Appconstants.MAIN_HOST
						+ "restaurantDetails/" + Explore.strVenueId;
				if (ConnectivityReceiver.checkInternetConnection(Explore.this)) {

					new DeatilsTasks().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(Explore.this);
				}
				Localsecrets.search_details_flag = 0;
			}
		} else {
			Localsecrets.det_fl = Appconstants.MAIN_HOST + "restaurantDetails/"
					+ Explore.strVenueId;
			if (ConnectivityReceiver.checkInternetConnection(Explore.this)) {

				new DeatilsTasks().execute();

			} else {
				ConnectivityReceiver.showCustomDialog(Explore.this);
			}
			Localsecrets.search_details_flag = 0;

		}
	}

	public void SetData1() {

		if (Localsecrets.search_details_flag == 1) {
			service_url1 = Localsecrets.det_fl;
			if (Localsecrets.det_fl.length() > 0) {

				if (ConnectivityReceiver.checkInternetConnection(Explore.this)) {

					new DeatilsTasks().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(Explore.this);
				}
				Localsecrets.search_details_flag = 0;
			} else if (id.length() > 0) {
				// service_url1
				Localsecrets.det_fl = Appconstants.MAIN_HOST
						+ "restaurantDetails/" + id;
				if (ConnectivityReceiver.checkInternetConnection(Explore.this)) {

					new DeatilsTasks().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(Explore.this);
				}
				Localsecrets.search_details_flag = 0;
			}
		} else {
			Localsecrets.det_fl = Appconstants.MAIN_HOST + "restaurantDetails/"
					+ id;
			if (ConnectivityReceiver.checkInternetConnection(Explore.this)) {

				new DeatilsTasks().execute();

			} else {
				ConnectivityReceiver.showCustomDialog(Explore.this);
			}
			Localsecrets.search_details_flag = 0;

		}
	}

	class DeatilsTasks extends AsyncTask<String, String, String> {

		ProgressDialog myProgressDialog;
		ArrayList<reviewDO> reviews = new ArrayList<reviewDO>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			myProgressDialog = new ProgressDialog(Explore.this);
			myProgressDialog.setMessage("Loading...");
			myProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			System.out.println("det_url" + Localsecrets.det_fl);
			return WebServiceCalls.getJSONString(Localsecrets.det_fl);
		}

		@SuppressLint("InflateParams")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject jObject = new JSONObject(result);
				if (jObject.has("id")) {
					Intent in = new Intent(getApplicationContext(),
							SearchDetails.class);
					in.putExtra("result", result);
					startActivityForResult(in, 5);
				}

				myProgressDialog.dismiss();
			} catch (Exception e) {
				myProgressDialog.dismiss();
				Log.e("", "" + e);
			}

		}
	}

	class getOrderCategoriesTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		String strVenueId1, jsonString, StrCat_Id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Explore.this);
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

			System.out.println("url is " + service_url);
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

	class getBooktableFilters extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		String strVenueId1, jsonString, StrCat_Id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Explore.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String service_url = Appconstants.MAIN_HOST + "restaurantslisting/"
					+ type + "/" + Appconstants.strCityId + "/"
					+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE
					+ "/" + id1 + "?filtersearch=1&" + "location="
					+ Appconstants.ltDo.id;
			System.out.println("url is " + service_url);
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
				JSONArray jsonArray = mainObject.getJSONArray("features");
				JSONArray jsonArray1 = mainObject
						.getJSONArray("existing_cusines");
				System.out.println("sss" + jsonArray);
				if (jsonArray.length() > 0) {

					Launcher.features = jsonArray;
					Launcher.existing_cusines = jsonArray1;
					// Launcher.restOrderDetails = mainObject;
					if (jsonArray.length() > 0) {
						Intent in = new Intent(getApplicationContext(),
								Filters.class);
						startActivity(in);
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}
}
