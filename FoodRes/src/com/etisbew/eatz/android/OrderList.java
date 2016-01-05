package com.etisbew.eatz.android;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etisbew.eatz.android.Explore.AccountDetails;
import com.etisbew.eatz.android.dropdownlist.MyDeals;
import com.etisbew.eatz.android.dropdownlist.MyFav;
import com.etisbew.eatz.android.dropdownlist.MyOrders;
import com.etisbew.eatz.android.dropdownlist.MyReservations;
import com.etisbew.eatz.android.dropdownlist.MyReviews;
import com.etisbew.eatz.android.dropdownlist.RedemptionHistory;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;

public class OrderList extends BaseActivity {

	TextView tvTakeway, tvGetHomeDelivery, tvDeliveringNow, tvOrderFoodTitle,
			tvFastLane, tvReorder, location;
	ImageView imgBack;
	int catId = 1;
	LinearLayout lyDeliveryLoc;
	Button btnGO;
	AutoCompleteTextView autoLocation;
	// String strLocId, strLocName;

	private QuickAction quickAction;
	ImageView options;
	static String strErrorMsg = "";
	static JSONObject restJsonDetails;
	ArrayList<String> venue_id = new ArrayList<String>();

	String errorMsg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.orderstypelist);

		if (TextUtils.isEmpty(Appconstants.ltDo.id)) {
			Appconstants.ltDo.id = "null";
		}
 
		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				quickAction.show(v);

			}
		});
		// PreferenceUtils.setOrderSession("3,7");
		System.out.println("value are" + PreferenceUtils.getOrderSession()
				+ PreferenceUtils.getOrderTypes());
		String url = Appconstants.MAIN_HOST + "offlineOrders/"
				+ PreferenceUtils.getOrderSession() + "/"
				+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE;

		System.out.println("offlineOrders "+url);
		if (ConnectivityReceiver.checkInternetConnection(OrderList.this)) {

			if (PreferenceUtils.getOrderSession().equalsIgnoreCase("0")) {

			} else {
				new getRestaurantsList1(OrderList.this, url).execute();
			}

		} else {
			ConnectivityReceiver.showCustomDialog(OrderList.this);
		}

		tvOrderFoodTitle = (TextView) findViewById(R.id.tvOrderFoodTitle);
		tvReorder = (TextView) findViewById(R.id.tvReorder);
		location = (TextView) findViewById(R.id.location);

		tvOrderFoodTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// startActivity(new Intent(getApplicationContext(),
				// Launcher.class));
				OrderList.this.finish();
			}
		});
		tvReorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						RepeatOrders.class));

				// TODO Auto-generated method stub
				// startActivity(new Intent(getApplicationContext(),
				// Launcher.class));
				// OrderList.this.finish();
				/*
				 * if (flag == 1) { if(restrent.size()>0){
				 * listView1.setVisibility(View.VISIBLE);
				 * lyDeliveryLoc.setVisibility(View.GONE); } flag = 2; } else {
				 * flag = 1; listView1.setVisibility(View.GONE);
				 * lyDeliveryLoc.setVisibility(View.GONE); }
				 */
			}
		});

		imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// startActivity(new Intent(getApplicationContext(),
				// Launcher.class));

				OrderList.this.finish();
			}
		});

		tvTakeway = (TextView) findViewById(R.id.tvOrderTakeAway);
		tvGetHomeDelivery = (TextView) findViewById(R.id.tvGetHomeDelivery);
		tvDeliveringNow = (TextView) findViewById(R.id.tvDeliveringNow);
		lyDeliveryLoc = (LinearLayout) findViewById(R.id.lyDeliveryLoc);
		lyDeliveryLoc = (LinearLayout) findViewById(R.id.lyDeliveryLoc);
		tvFastLane = (TextView) findViewById(R.id.tvFastLane);

		lyDeliveryLoc.setVisibility(View.GONE);

		tvFastLane.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// startActivity(new Intent(OrderList.this,KnowMore.class));
				Appconstants.strMenuflag = "FastLane";
				Localsecrets.flg = 2;
				Appconstants.filters_flg = "2";
				// Localsecrets.str_titles = "Takeaway restaurants";
				Localsecrets.str_titles = "FastLane restaurants";
				lyDeliveryLoc.setVisibility(View.GONE);

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

				System.out.println("url is" + Launcher.getRestaurent);

				if (ConnectivityReceiver
						.checkInternetConnection(OrderList.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
							.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(OrderList.this);
				}
			}
		});

		tvTakeway.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Localsecrets.flg = 0;
				Appconstants.filters_flg = "0";
				Localsecrets.str_titles = "Takeaway restaurants";
				lyDeliveryLoc.setVisibility(View.GONE);

				try {
					Explore.type = 1;
					Explore.id1 = 2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/1/" + Appconstants.strCityId
							+ "/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/1";
					/*
					 * Launcher.getRestaurent = Appconstants.MAIN_HOST +
					 * "orderfoodlinks/" + Appconstants.ltDo.id + "/" +
					 * Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE +
					 * "/" + Appconstants.strCityId +
					 * "/1?arg=1&searchpage=1&sort_by=distance";
					 */
				} catch (NullPointerException e) {
					Explore.type = 1;
					Explore.id1 = 2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/1/" + Appconstants.strCityId
							+ "/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/1";
					/*
					 * Launcher.getRestaurent = Appconstants.MAIN_HOST +
					 * "orderfoodlinks/" + Appconstants.ltDo.id + "/" +
					 * Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE +
					 * "/" + Appconstants.strCityId +
					 * "/1?arg=1&searchpage=1&sort_by=distance";
					 */

					// http://www.localsecrets.in/WebServices/orderfoodlinks/34/17.399799/78.476601/22960/1?arg=1&searchpage=1&sort_by=distance

				}

				System.out.println("url is" + Launcher.getRestaurent);

				Explore.search_string = "";
				if (ConnectivityReceiver
						.checkInternetConnection(OrderList.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
							.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(OrderList.this);
				}
			}
		});

		tvGetHomeDelivery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Localsecrets.flg = 0;
				Appconstants.filters_flg = "0";
				catId = 2;
				Localsecrets.str_titles = "Restaurants that deliver";
				// Search.selectedLocationId = Appconstants.ltDo.id;
				lyDeliveryLoc.setVisibility(View.VISIBLE);

			}
		});

		tvDeliveringNow.setOnClickListener(new OnClickListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Localsecrets.flg = 1;
				Appconstants.filters_flg = "1";
				catId = 3;
				// Search.selectedLocationId = Appconstants.ltDo.id;
				Localsecrets.str_titles = "Restaurants that deliver";
				lyDeliveryLoc.setVisibility(View.VISIBLE);
			}
		});

		autoLocation = (AutoCompleteTextView) findViewById(R.id.autoLoc);

		location.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				autoLocation.setText("");
			}
		});

		/*
		 * autoLocation.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @SuppressLint("ClickableViewAccessibility")
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { //
		 * final int DRAWABLE_LEFT = 0; // final int DRAWABLE_TOP = 1; final int
		 * DRAWABLE_RIGHT = 2; // final int DRAWABLE_BOTTOM = 3;
		 * 
		 * if (event.getAction() == MotionEvent.ACTION_UP) { if (event.getX() >=
		 * (autoLocation.getRight() - autoLocation
		 * .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds() .width())) {
		 * autoLocation.setText(""); } } return false; } });
		 */
		System.out.println("array" + Appconstants.location_array);
		autoLocation.setThreshold(0);
		autoLocation.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line,
				Appconstants.location_array));
		Search.selectedLocationId = Appconstants.ltDo.id;
		Search.selectedLocationName = Appconstants.ltDo.name;
		try {
			if (null != Appconstants.ltDo.name)
				autoLocation.setText(Appconstants.ltDo.name);
		} catch (NullPointerException e) {
		}
		autoLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int pos = Appconstants.location_array.indexOf(autoLocation
						.getText().toString());
				Search.selectedLocationId = Appconstants.locationID_array
						.get(pos);
				Search.selectedLocationName = Appconstants.location_array
						.get(pos);
				Appconstants.ltDo.id = Appconstants.locationID_array.get(pos);
				Appconstants.ltDo.name = Appconstants.location_array.get(pos);
			}
		});
		// etLocation.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		//
		// int textlength = s.length();
		// ArrayList<LocationDO> tempArrayList = new ArrayList<LocationDO>();
		// for (LocationDO c : Appconstants.location_do) {
		// if (textlength <= c.name.length()) {
		// if (c.name.toLowerCase().contains(
		// s.toString().toLowerCase())) {
		// tempArrayList.add(c);
		// }
		// }
		// }
		// lAdp.refresh(tempArrayList);
		//
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		btnGO = (Button) findViewById(R.id.btnGo);
		btnGO.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Launcher.userLocation = Search.selectedLocationName;
				Appconstants.filters_flg = "1";
				Localsecrets.flg = 1;
				try {
					Explore.type = 2;
					Explore.id1 = 2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/2/" + Appconstants.strCityId
							+ "/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/1?location="
							+ Search.selectedLocationId;
					System.out.println("id" + Search.selectedLocationId);

					/*
					 * Launcher.getRestaurent = Appconstants.MAIN_HOST +
					 * "orderfoodlinks/" + Search.selectedLocationId + "/" +
					 * Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE +
					 * "/" + Appconstants.strCityId + "/" + catId;
					 */

				} catch (NullPointerException e) {
					Explore.type = 2;
					Explore.id1 = 2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/2/" + Appconstants.strCityId
							+ "/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/1?location="
							+ Search.selectedLocationId;
					System.out.println("id" + Search.selectedLocationId);
					/*
					 * Launcher.getRestaurent = Appconstants.MAIN_HOST +
					 * "orderfoodlinks/" + Search.selectedLocationId + "/" +
					 * Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE +
					 * "/" + Appconstants.strCityId + "/" + catId;
					 */
				}
				System.out.println("url" + Launcher.getRestaurent);
				Explore.search_string = "";

				if (ConnectivityReceiver
						.checkInternetConnection(OrderList.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
							.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(OrderList.this);
				}

			}
		});
		autoLocation.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				System.out.println("size"
						+ autoLocation.getText().toString().length());
				if (autoLocation.getText().toString().length() > 0) {
					location.setVisibility(View.VISIBLE);
				} else {
					location.setVisibility(View.GONE);
				}
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
	}

	@Override
	protected void onResume() {
		super.onResume();

		try {
			if (null != Appconstants.ltDo.name)
				autoLocation.setText(Appconstants.ltDo.name);
		} catch (NullPointerException e) {
		}
		quickAction = new QuickAction(this, QuickAction.VERTICAL);
		if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
			quickAction.addActionItem(new ActionItem(Explore.ID_LOGIN, "Login",
					getResources().getDrawable(R.drawable.iocn_myaccount)));
		} else {
			quickAction.addActionItem(new ActionItem(Explore.ID_MYACCOUNT,
					"My Account", getResources().getDrawable(
							R.drawable.my_account)));
			quickAction.addActionItem(new ActionItem(Explore.ID_RES,
					" My Reservations", getResources().getDrawable(
							R.drawable.my_reservations)));
			quickAction.addActionItem(new ActionItem(Explore.ID_ORDERS,
					" My Orders", getResources().getDrawable(
							R.drawable.my_orders)));
			quickAction.addActionItem(new ActionItem(Explore.ID_DEALS,
					" My Deals", getResources()
							.getDrawable(R.drawable.my_deals)));
			quickAction.addActionItem(new ActionItem(Explore.ID_REV,
					"My Reviews", getResources().getDrawable(
							R.drawable.my_reviews)));
			quickAction.addActionItem(new ActionItem(Explore.ID_REDEMPTION,
					" Redemption History", getResources().getDrawable(
							R.drawable.redemption_history)));
			quickAction.addActionItem(new ActionItem(Explore.ID_FAV,
					" My Favourites", getResources().getDrawable(
							R.drawable.favs)));
			quickAction
					.addActionItem(new ActionItem(Explore.ID_LOGOUT, "Logout",
							getResources().getDrawable(R.drawable.my_logout)));
		}
		// Set listener for action item clicked
		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {

						quickAction.dismiss();
						if (actionId == Explore.ID_LOGOUT) {
							PreferenceUtils.removeUserName();
							startActivity(new Intent(OrderList.this,
									Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {

							if (ConnectivityReceiver
									.checkInternetConnection(OrderList.this)) {

								new AccountDetails(OrderList.this).execute();

							} else {
								ConnectivityReceiver
										.showCustomDialog(OrderList.this);
							}
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(OrderList.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(OrderList.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(OrderList.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(OrderList.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(OrderList.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(OrderList.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(OrderList.this,
									Login.class));
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

	public class getRestaurantsList1 extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		Context context;
		String strUrl;

		public getRestaurantsList1(Context con, String strUrl1) {
			// TODO Auto-generated constructor stub

			context = con;
			strUrl = strUrl1;
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
			String result = "";
			Appconstants.staticURL = strUrl;
			// if(TextUtils.isEmpty(Search.selectedLocationId)){
			result = WebServiceCalls.getJSONString(strUrl);
			System.out.println("if block");
			/*
			 * }else{ List<NameValuePair> nameValuePairs = new
			 * ArrayList<NameValuePair>( 2); nameValuePairs.add(new
			 * BasicNameValuePair("location", Search.selectedLocationId));
			 * result=WebServiceCalls.getJSONString1(strUrl,nameValuePairs);
			 * System.out.println("else block"+result); }
			 */
			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("restaurent result" + result);

			/*
			 * try { File myFile = new File("/sdcard/serverErrorFile.txt");
			 * myFile.createNewFile(); FileOutputStream fOut = new
			 * FileOutputStream(myFile); OutputStreamWriter myOutWriter = new
			 * OutputStreamWriter(fOut); myOutWriter.append(result);
			 * myOutWriter.close(); } catch (Exception e) { // TODO: handle
			 * exception }
			 */
			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
			} else {

				if (result.contains("errormsg")) {

					try {
						strErrorMsg = new JSONObject(result)
								.optString("errormsg");
						Launcher.userLocation = "Hyderabad";

						restJsonDetails = new JSONObject(result);
						if (restJsonDetails.has("Restaurants")) {

							Launcher.jsonArrayRestaurants = restJsonDetails
									.getJSONArray("Restaurants");

							for (int i = 0; i < Launcher.jsonArrayRestaurants
									.length(); i++) {

								JSONObject objJson = Launcher.jsonArrayRestaurants
										.getJSONObject(i);

								if (!objJson.isNull("Venueid")) {

									venue_id.add(objJson.getString("Venueid"));
								}
							}
						}
						if (venue_id.size() > 0) {
							tvReorder.setVisibility(View.VISIBLE);
						} else {
							tvReorder.setVisibility(View.GONE);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						restJsonDetails = new JSONObject(result);
						if (restJsonDetails.has("Restaurants")) {

							Launcher.jsonArrayRestaurants = restJsonDetails
									.getJSONArray("Restaurants");

							for (int i = 0; i < Launcher.jsonArrayRestaurants
									.length(); i++) {

								JSONObject objJson = Launcher.jsonArrayRestaurants
										.getJSONObject(i);

								if (!objJson.isNull("Venueid")) {

									venue_id.add(objJson.getString("Venueid"));
								}
							}
						}
						if (venue_id.size() > 0) {
							tvReorder.setVisibility(View.VISIBLE);
						} else {
							tvReorder.setVisibility(View.GONE);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		}
	}

}
