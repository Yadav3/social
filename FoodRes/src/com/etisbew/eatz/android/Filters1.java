package com.etisbew.eatz.android;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
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

@SuppressLint("InflateParams")
public class Filters1 extends BaseActivity implements OnClickListener {

//	private String selectedPrice = "";
	private TextView tvCancel = null, tvSubmit = null, textView1,tvReset=null;
	private AutoCompleteTextView etLocation = null;
	// TableLayout tlLayout = null;
	private LayoutInflater inflator = null;
	JSONObject jj;
	JSONObject jj1;
	JSONObject jj2;
	private QuickAction quickAction; 
	ImageView options;
	JSONArray jcuisines = null;
	JSONArray jlocations = null;
	static int flag;
	LinearLayout ll_takeway_filter, ll_homedelivery_filter;
	TableLayout tlayout_takeaway = null, tlayout_homedelivery = null;
	ArrayList<String> takeaway_filters;
	ArrayList<String> homedelivery_filters;
	ListView listView1;
	ArrayList<String>arraylocation ,arraylocationIds ;
	ScrollView scrollView1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.filters1); 

		inflator = getLayoutInflater();
		hideKeyboard(); 

		tvCancel = (TextView) findViewById(R.id.tvCancel);

		tvSubmit = (TextView) findViewById(R.id.tvSubmit);
		tvReset = (TextView) findViewById(R.id.tvReset);

		textView1 = (TextView) findViewById(R.id.textView1);
		etLocation = (AutoCompleteTextView) findViewById(R.id.etLocation);
		
		listView1 = (ListView) findViewById(R.id.listView1);
		scrollView1 =(ScrollView) findViewById(R.id.scrollView1);
		
		ll_takeway_filter = (LinearLayout) findViewById(R.id.ll_takeway_filter);
		ll_homedelivery_filter = (LinearLayout) findViewById(R.id.ll_homedelivery_filter);
		
		tlayout_takeaway = (TableLayout) findViewById(R.id.tlayout_takeaway);
		tlayout_homedelivery=(TableLayout) findViewById(R.id.tlayout_homedelivery);
		
		
		takeaway_filters = new ArrayList<String>();
		takeaway_filters.add("Open now"); 
		//takeaway_filters.add("Accepts Online Payments");
		takeaway_filters.add("Fastlane pickup");

		homedelivery_filters = new ArrayList<String>();
		homedelivery_filters.add("No Minimum");
		homedelivery_filters.add("Free Delivery");
		if (Appconstants.filters_flg == "0") {
			ll_takeway_filter.setVisibility(View.VISIBLE);
			ll_homedelivery_filter.setVisibility(View.GONE);
			
			setDetails();
		} else if (Appconstants.filters_flg == "1") {
			ll_takeway_filter.setVisibility(View.GONE);
			ll_homedelivery_filter.setVisibility(View.VISIBLE);
			setDetails1();
		} else if (Appconstants.filters_flg == "2") {
			ll_takeway_filter.setVisibility(View.GONE);
			ll_takeway_filter.setVisibility(View.GONE);
			setDetails2();
		}
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			//Toast.makeText(getApplicationContext(), "clicked"+position+"("+arraylocation.get(position)+")", 5).show();
			String ss = Appconstants.MAIN_HOST + "restaurantslisting/"
					+ Explore.type + "/" + Appconstants.strCityId + "/"
					+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE
		 			+ "/1?location="+arraylocation.get(position);
			Explore.map_flag = Filters1.flag;
			Explore.search_string="location=" + arraylocation.get(position);
			if (ConnectivityReceiver.checkInternetConnection(Filters1.this)) {
			new RestaurantsList(Filters1.this, ss).execute();
			} else {
				ConnectivityReceiver.showCustomDialog(Filters1.this);
			}
			}  
			});

		etLocation.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (etLocation.getText().toString().length() > 0) {
					textView1.setVisibility(View.VISIBLE);
				} else {
					textView1.setVisibility(View.GONE);
				}

			}

		});

		textView1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etLocation.setText("");
			}
		});

		// tlLayout = (TableLayout) findViewById(R.id.tlayout);

		ArrayList<String> allLocationsForFilter = new ArrayList<String>();
		//allLocationsForFilter.add("All Hyderabad");
		allLocationsForFilter.addAll(Appconstants.location_array);

		ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
				R.layout.ato_row, allLocationsForFilter);
		etLocation.setAdapter(adp);
		etLocation.setThreshold(1);
		if (Appconstants.ltDo.name.length() > 0) {
			etLocation.setText(Appconstants.ltDo.name);
			textView1.setVisibility(View.VISIBLE);
			/*if(etLocation.getText().length() > 0){
				textView1.setVisibility(View.VISIBLE);
			}else{
				textView1.setVisibility(View.GONE);
			}*/
		} else {
			textView1.setVisibility(View.GONE);
		}

		tvCancel.setOnClickListener(this);
		tvReset.setOnClickListener(this);

		tvSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if(Appconstants.filters_flg=="0"){

				submitValues();

				/*
				 * }else if(Appconstants.filters_flg=="1"){ submitValues1(); }
				 */
			}
		});

		ImageView ivBack = (ImageView) findViewById(R.id.back);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setResult(10116);
				finish();

			}
		});

		TextView etFind = (TextView) findViewById(R.id.etFind);
		etFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setResult(10117);
				finish();

			}
		});

		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				quickAction.show(v);

			}
		});

	}
public void setDetails2(){
	scrollView1.setVisibility(View.GONE);
	listView1.setVisibility(View.VISIBLE);
	arraylocation = new ArrayList<String>();
	arraylocationIds = new ArrayList<String>();
	try {
		Launcher.locations_list = Launcher.restJsonDetails
				.getJSONArray("locations_list");
		jj2 = Launcher.locations_list.getJSONObject(0);
		jlocations = jj2.names();

	} catch (JSONException e) {

		e.printStackTrace(); 
	}
	if (jlocations == null) {
	} else {
		arraylocation.clear();
		arraylocationIds.clear();
		
		for (int i = 0; i < jlocations.length(); i++) {
			try {

				arraylocation.add(jj2.optString(jlocations.getString(i)).toString());
				arraylocationIds.add(jlocations.getString(i));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	if(arraylocationIds.size()>0){
		String[] stockArr = new String[arraylocationIds.size()];
		stockArr = arraylocationIds.toArray(stockArr);
		listView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stockArr));
	}else{
		
	}
}
	public void setDetails() {
		scrollView1.setVisibility(View.VISIBLE);
		listView1.setVisibility(View.GONE);
		Localsecrets.selectedItemsArray_color.clear();
		for (int i = 0; i < takeaway_filters.size(); i++) {
			final View view = inflator.inflate(R.layout.features, null);
			view.setTag("false");
			final TextView tvItem = (TextView) view
					.findViewById(R.id.tvCrediCards);
			String text = takeaway_filters.get(i);
			tvItem.setText(text);

			// text.
			
			if(takeaway_filters.get(i).equalsIgnoreCase("Accepts Online Payments")){
			tvItem.setTag("&"+takeaway_filters.get(i) + "=53");
			}else{
				tvItem.setTag("&"+takeaway_filters.get(i) + "=1");
			}

			if (Localsecrets.selectedItemsArray_color.contains(text)) {
				view.setTag("true");
				tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.checked, 0);
			} else {
				view.setTag("false");
				tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.uncheck, 0);
			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (view.getTag().toString().contains("false")) {
						view.setTag("true");
						tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0,
								R.drawable.checked, 0);
					} else {
						view.setTag("false");
						tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0,
								R.drawable.uncheck, 0);
					}
				}
			});

			tlayout_takeaway.addView(view);
		}
	}

	public void setDetails1() {
		scrollView1.setVisibility(View.VISIBLE);
		listView1.setVisibility(View.GONE);
		Localsecrets.selectedItemsArray_color.clear();
		for (int i = 0; i < homedelivery_filters.size(); i++) {
			final View view = inflator.inflate(R.layout.features, null);
			view.setTag("false");
			final TextView tvItem = (TextView) view
					.findViewById(R.id.tvCrediCards);
			String text = homedelivery_filters.get(i);
			tvItem.setText(text);

			// text.
			tvItem.setTag(homedelivery_filters.get(i) + "="+(i+1));

			if (Localsecrets.selectedItemsArray_color.contains(text)) {
				view.setTag("true");
				tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.checked, 0);
			} else {
				view.setTag("false");
				tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.uncheck, 0);
			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (view.getTag().toString().contains("false")) {
						view.setTag("true");
						tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0,
								R.drawable.checked, 0);
					} else {
						view.setTag("false");
						tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0,
								R.drawable.uncheck, 0);
					}
				}
			});

			tlayout_homedelivery.addView(view);
		}
	}

	// ArrayList<String> arrayCusineIds;

	@Override
	protected void onResume() {
		super.onResume();

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
							startActivity(new Intent(Filters1.this, Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish(); 
						} else if (actionId == Explore.ID_MYACCOUNT) {
							if (ConnectivityReceiver.checkInternetConnection(Filters1.this)) {
							new AccountDetails(Filters1.this).execute();
						} else {
							ConnectivityReceiver.showCustomDialog(Filters1.this);
						}
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(Filters1.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(Filters1.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(Filters1.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(Filters1.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(Filters1.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(Filters1.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(Filters1.this, Login.class));
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 10116) {
			setResult(10116);
			finish();
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tvCancel: 
			finish();
			break;
		case R.id.tvReset:
			Explore.map_flag = Filters1.flag;
			int pos1=Appconstants.locationID_array.indexOf("0");
    		Appconstants.ltDo.id = Appconstants.locationID_array
				.get(pos1);
		Appconstants.ltDo.name = Appconstants.location_array
				.get(pos1);
		Appconstants.LATTITUDE = Appconstants.location_lat
				.get(pos1);
		Appconstants.LANGITUDE = Appconstants.location_lang
				.get(pos1);
		Localsecrets.selectedItemsArray_color.clear();
/*		    	if (ConnectivityReceiver.checkInternetConnection(Filters.this)) {
		    new getBooktableFilters().execute();

		 } else { ConnectivityReceiver.showCustomDialog(Filters.this); }*/
		String ss = Appconstants.MAIN_HOST + "restaurantslisting/"
				+ Explore.type + "/" + Appconstants.strCityId + "/" 
				+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE
				+ "/1?";
		
			ss = ss + "location=" + Appconstants.ltDo.id;
			System.out.println("url is"+ss);
		Explore.search_string="location=" + Appconstants.ltDo.id;
		if (ConnectivityReceiver.checkInternetConnection(Filters1.this)) {
			new RestaurantsList(Filters1.this, ss).execute();

		} else {
			ConnectivityReceiver.showCustomDialog(Filters1.this);
		}
			break;

		default:
			break;
		}

	}



	private void submitValues() {
		String items = "";
		String items1 = "";
		Explore.selectedItemsArray.clear();

		if (etLocation.getText().length() > 0) {

			/*if (etLocation.getText().toString().contains("All")) {
				// return;
			} else {*/
				for (int i = 0; i < Appconstants.location_array.size(); i++) {
					if (etLocation
							.getText()
							.toString()
							.equalsIgnoreCase(
									Appconstants.location_array.get(i))) {
						Appconstants.ltDo.id = Appconstants.locationID_array
								.get(i);
						Appconstants.ltDo.name = Appconstants.location_array
								.get(i);
						Appconstants.LATTITUDE = Appconstants.location_lat
								.get(i);
						Appconstants.LANGITUDE = Appconstants.location_lang
								.get(i);
						break;

					}
				}
			//}
		}

		if (Appconstants.filters_flg == "0") {
			for (int i = 0; i < tlayout_takeaway.getChildCount(); i++) {
				TableRow viewGroup = (TableRow) tlayout_takeaway.getChildAt(i);
				if (viewGroup.getTag().toString().equalsIgnoreCase("true")) {

					TextView tv1 = (TextView) viewGroup.getChildAt(0);
					takeaway_filters.add(tv1.getText().toString());

					if (Localsecrets.selectedItemsArray_color.contains(tv1
							.getText().toString())) {

					} else {
						Localsecrets.selectedItemsArray_color.add(tv1.getText()
								.toString());
					}

					items = items + tv1.getTag().toString() + "";

				}
			} 
		
		} else if (Appconstants.filters_flg == "1") {
			for (int i = 0; i < tlayout_homedelivery.getChildCount(); i++) {
				TableRow viewGroup = (TableRow) tlayout_homedelivery.getChildAt(i);
				if (viewGroup.getTag().toString().equalsIgnoreCase("true")) {

					TextView tv1 = (TextView) viewGroup.getChildAt(0);
					homedelivery_filters.add(tv1.getText().toString());

					if (Localsecrets.selectedItemsArray_color.contains(tv1
							.getText().toString())) {

					} else {
						Localsecrets.selectedItemsArray_color.add(tv1.getText()
								.toString());
					}

					items1 = items1 + tv1.getTag().toString() + "";
 
				} 
			}
		
		}
		String result = "";
		Explore.search_string="";

		if ((Appconstants.strMenuflag.equalsIgnoreCase("Opennow"))) {
			//
			String ss = Appconstants.MAIN_HOST + "restaurantslisting/"
					+ Explore.type + "/" + Appconstants.strCityId + "/"
					+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE
		 			+ "/1?";

			if (!TextUtils.isEmpty(Appconstants.ltDo.id) && etLocation
					.getText()
					.toString().length()>0) {
				if (Integer.parseInt(Appconstants.ltDo.id) > 0) {
					ss = ss + "location=" + Appconstants.ltDo.id;
					Explore.search_string = Explore.search_string + "location="
							+ Appconstants.ltDo.id; 
				}
			}
			if (Appconstants.filters_flg == "0") { 
				items=items.replace("Open now", "open");
				items=items.replace("Accepts Online Payments", "features");
				items=items.replace("Fastlane pickup", "fastlane");
				ss=ss+items;
				Explore.search_string = Explore.search_string +items;
				
			} else if (Appconstants.filters_flg == "1") {
				

				int s=0;
				int s1=0;
				if(items1.contains("=1")){
					s=1;
				}
				if(items1.contains("=2")){
					s1=1;
				}
					
				String final_list="";
				
				if(s == 1 && s1 == 0){
					final_list=final_list+"&vorder=1";
				}else if(s == 0  && s1 == 1){
					final_list=final_list+"&vorder=2";
				}else if(s == 1 && s == 1){
					final_list=final_list+"&vorder=1,2"; 
				}
					ss=ss+final_list; 
					Explore.search_string = Explore.search_string +final_list;
				
			
			}
			
			if (!TextUtils.isEmpty(Explore.search)) { 
				ss = ss + "&search_name=" + Explore.search;
				Explore.search_string = Explore.search_string + "&search_name="
						+ Explore.search;
			}
			ss = ss.replace("?&", "?");

		} else {

			String ss = Appconstants.MAIN_HOST + "restaurantslisting/"
					+ Explore.type + "/" + Appconstants.strCityId + "/"
					+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE
					+ "/1?";
			if (!TextUtils.isEmpty(Appconstants.ltDo.id) && etLocation
					.getText()
					.toString().length()>0) {
				if (Integer.parseInt(Appconstants.ltDo.id) > 0) {
					ss = ss + "location=" + Appconstants.ltDo.id;
					Explore.search_string = Explore.search_string + "location="
							+ Appconstants.ltDo.id;
				}
			}
			if (Appconstants.filters_flg == "0") { 
				items=items.replace("Open now", "open");
				items=items.replace("Accepts Online Payments", "features");
				items=items.replace("Fastlane pickup", "fastlane");
				ss=ss+items;
				Explore.search_string = Explore.search_string + items;
				 
			} else if (Appconstants.filters_flg == "1") {
			int s=0;
			int s1=0;
			if(items1.contains("=1")){
				s=1;
			}
			if(items1.contains("=2")){
				s1=1;
			}
				
			String final_list="";
			
			if(s == 1 && s1 == 0){
				final_list=final_list+"&vorder=1";
			}else if(s == 0  && s1 == 1){
				final_list=final_list+"&vorder=2";
			}else if(s == 1 && s == 1){
				final_list=final_list+"&vorder=1,2";
			}
				ss=ss+final_list; 
				Explore.search_string = Explore.search_string +final_list;
			}
			
			if (!TextUtils.isEmpty(Explore.search)) { 
				ss = ss + "&search_name=" + Explore.search;
				Explore.search_string = Explore.search_string + "&search_name="
						+ Explore.search;
			}
			ss = ss.replace("?&", "?");
			result = ss;
		}
		System.out.println("url is"+result);
		Explore.map_flag = Filters1.flag;
		if (ConnectivityReceiver.checkInternetConnection(Filters1.this)) {
		new RestaurantsList(Filters1.this, result).execute();

		} else {
			ConnectivityReceiver.showCustomDialog(Filters1.this);
		}

	}
}
