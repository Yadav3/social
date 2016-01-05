package com.etisbew.eatz.android;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Spinner;
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
import com.etisbew.eatz.objects.featuresDO;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;

@SuppressLint("InflateParams")
public class Filters extends BaseActivity implements OnClickListener {

//	private String selectedPrice = "";
	private TextView tvCancel = null, tvOneRupee, tvTwoRupee, tvThreeRupee,
			tvFourRupee = null, tvSubmit = null, textView1, textView2,tvReset = null;
	private AutoCompleteTextView etLocation = null, etCuisine1 = null;
	private Spinner etCusine;
	TableLayout tlLayout = null;
	private LayoutInflater inflator = null;
	JSONObject jj;
	JSONObject jj1;
	private QuickAction quickAction;
	ImageView options;
	JSONArray jcuisines = null;
	static int flag;
	ArrayList<String> allLocationsForFilter = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.filters);
		inflator = getLayoutInflater();
		hideKeyboard();
		Localsecrets.selectedItemsArray_color.clear();
		tvCancel = (TextView) findViewById(R.id.tvCancel);
		tvOneRupee = (TextView) findViewById(R.id.tvOneRupee);
		tvTwoRupee = (TextView) findViewById(R.id.tvTwoRupee);
		tvThreeRupee = (TextView) findViewById(R.id.tvThreeRupee);
		tvFourRupee = (TextView) findViewById(R.id.tvFourRupee);
		tvSubmit = (TextView) findViewById(R.id.tvSubmit);
		
		tvReset = (TextView) findViewById(R.id.tvReset);

		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);

		etCusine = (Spinner) findViewById(R.id.etCusine);
		etLocation = (AutoCompleteTextView) findViewById(R.id.etLocation);
		etCuisine1 = (AutoCompleteTextView) findViewById(R.id.etCuisine1);

		etLocation.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				System.out.println("text changed");

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
				if (Appconstants.ltDo.name.length() > 0) {
					/*etLocation.setText(Appconstants.ltDo.name);
					textView1.setVisibility(View.VISIBLE);*/ 
					if(etLocation.getText().length() > 0){
						textView1.setVisibility(View.VISIBLE);
					}else{
						textView1.setVisibility(View.GONE);
					}
				} else {
					textView1.setVisibility(View.GONE);
				}

			}

		});
		
		etLocation.setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
		        //... your stuff
		    	int pos1=Appconstants.location_array.indexOf(etLocation.getText().toString());
		    	System.out.println("position"+position+allLocationsForFilter.get(position)+pos1+etLocation.getText().toString()+Appconstants.locationID_array.get(pos1));
		    	Appconstants.ltDo.id = Appconstants.locationID_array
						.get(pos1);
				Appconstants.ltDo.name = Appconstants.location_array
						.get(pos1);
				Appconstants.LATTITUDE = Appconstants.location_lat
						.get(pos1);
				Appconstants.LANGITUDE = Appconstants.location_lang
						.get(pos1);
		    	
		    	if (ConnectivityReceiver.checkInternetConnection(Filters.this)) {
				    new getBooktableFilters().execute();

				 } else { ConnectivityReceiver.showCustomDialog(Filters.this); }
		    	/*for (int i = 0; i < allLocationsForFilter.size(); i++) { 
		    		
		    	
		    	}*/
		    }
		});

		textView1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etLocation.setText("");
			}
		});
		textView2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etCuisine1.setText("");
			}
		});
		etCuisine1.addTextChangedListener(new TextWatcher() {

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
				if (etCuisine1.getText().toString().length() > 0) {
					textView2.setVisibility(View.VISIBLE);
				} else {
					textView2.setVisibility(View.GONE);
				}

			}

		});

		tlLayout = (TableLayout) findViewById(R.id.tlayout);

		
	//	allLocationsForFilter.add("All Hyderabad");
		allLocationsForFilter.addAll(Appconstants.location_array);

		ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
				R.layout.ato_row, allLocationsForFilter);
		etLocation.setAdapter(adp);
		etLocation.setThreshold(1);
		if (Appconstants.ltDo.name.length() > 0) {
			etLocation.setText(Appconstants.ltDo.name);
			textView1.setVisibility(View.VISIBLE);
		} else {
			textView1.setVisibility(View.GONE);
		}

		if (Launcher.pricelist.contains("1")) {
			tvOneRupee.setVisibility(View.VISIBLE);
		}
		if (Launcher.pricelist.contains("2")) {
			tvTwoRupee.setVisibility(View.VISIBLE);
		}
		if (Launcher.pricelist.contains("3")) {
			tvThreeRupee.setVisibility(View.VISIBLE);
		}
		if (Launcher.pricelist.contains("4")) {
			tvFourRupee.setVisibility(View.VISIBLE);
		}

		if (!Explore.selectedPrice.equalsIgnoreCase("0")) {
			if (Launcher.pricelist.contains("1")) {
				tvOneRupee
						.setBackgroundResource(R.drawable.textfield_blackborders_filter_selected);
			} else if (Launcher.pricelist.contains("2")) {
				tvTwoRupee
						.setBackgroundResource(R.drawable.textfield_blackborders_filter_selected);
			} else if (Launcher.pricelist.contains("3")) {
				tvThreeRupee
						.setBackgroundResource(R.drawable.textfield_blackborders_filter_selected);
			} else if (Launcher.pricelist.contains("4")) {
				tvFourRupee
						.setBackgroundResource(R.drawable.textfield_blackborders_filter_selected);
			} 
		}
		arrayCusine = new ArrayList<String>();
		arrayCusineIds = new ArrayList<String>();
		try {
			System.out.println("sss"+Launcher.existing_cusines.getJSONObject(0));
			jj1 = Launcher.existing_cusines.getJSONObject(0);
			jcuisines = jj1.names();

		} catch (JSONException e) {

			e.printStackTrace();
		}
		if (jcuisines == null) {
		} else {
			arrayCusine.clear();
			arrayCusineIds.clear();
			arrayCusine.add("All Cusines");
			arrayCusineIds.add("null");
			for (int i = 0; i < jcuisines.length(); i++) {
				try {

					arrayCusineIds.add(jj1.optString(jcuisines.getString(i))
							.toString());
					arrayCusine.add(jcuisines.getString(i));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (arrayCusine.size() > 0) {
			showCuisin();
		} else {
			showCuisin();
		}

		new DisplayViews().execute();
		

		tvCancel.setOnClickListener(this);
		tvOneRupee.setOnClickListener(this);
		tvTwoRupee.setOnClickListener(this);
		tvThreeRupee.setOnClickListener(this);
		tvFourRupee.setOnClickListener(this);
		tvSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submitValues();
			}
		});
		tvReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Explore.map_flag = Filters.flag;
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
				if (ConnectivityReceiver.checkInternetConnection(Filters.this)) {
					new RestaurantsList(Filters.this, ss).execute();

				} else {
					ConnectivityReceiver.showCustomDialog(Filters.this);
				}
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

	ArrayList<String> arrayCusineIds;

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
							startActivity(new Intent(Filters.this, Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							if (ConnectivityReceiver.checkInternetConnection(Filters.this)) {
							new AccountDetails(Filters.this).execute();

							} else {
								ConnectivityReceiver.showCustomDialog(Filters.this);
							}
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(Filters.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(Filters.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(Filters.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(Filters.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(Filters.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(Filters.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(Filters.this, Login.class));
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
		case R.id.tvOneRupee:
			tvOneRupee
					.setBackgroundResource(R.drawable.textfield_blackborders_filter_selected);
			tvTwoRupee.setBackgroundResource(R.drawable.textfield_background);
			tvThreeRupee.setBackgroundResource(R.drawable.textfield_background);
			tvFourRupee.setBackgroundResource(R.drawable.textfield_background);

			tvOneRupee.setTag("true");
			tvTwoRupee.setTag("false");
			tvThreeRupee.setTag("false");
			tvFourRupee.setTag("false");

			break;
		case R.id.tvTwoRupee:
			tvOneRupee.setBackgroundResource(R.drawable.textfield_background);
			tvTwoRupee
					.setBackgroundResource(R.drawable.textfield_blackborders_filter_selected);
			tvThreeRupee.setBackgroundResource(R.drawable.textfield_background);
			tvFourRupee.setBackgroundResource(R.drawable.textfield_background);
			tvOneRupee.setTag("false");
			tvTwoRupee.setTag("true");
			tvThreeRupee.setTag("false");
			tvFourRupee.setTag("false");
			break;
		case R.id.tvThreeRupee:
			tvOneRupee.setBackgroundResource(R.drawable.textfield_background);
			tvTwoRupee.setBackgroundResource(R.drawable.textfield_background);
			tvThreeRupee
					.setBackgroundResource(R.drawable.textfield_blackborders_filter_selected);
			tvFourRupee.setBackgroundResource(R.drawable.textfield_background);
			tvOneRupee.setTag("false");
			tvTwoRupee.setTag("false");
			tvThreeRupee.setTag("true");
			tvFourRupee.setTag("false");
			break;
		case R.id.tvFourRupee:
			tvOneRupee.setBackgroundResource(R.drawable.textfield_background);
			tvTwoRupee.setBackgroundResource(R.drawable.textfield_background);
			tvThreeRupee.setBackgroundResource(R.drawable.textfield_background);
			tvFourRupee
					.setBackgroundResource(R.drawable.textfield_blackborders_filter_selected);

			tvOneRupee.setTag("false");
			tvTwoRupee.setTag("false");
			tvThreeRupee.setTag("false");
			tvFourRupee.setTag("true");
			break;
		// case R.id.tvSubmit:
		//
		// submitValues();
		//
		// break;

		default:
			break;
		}

	}

	String items = "";

	private void submitValues() {

		Explore.selectedItemsArray.clear();
		Localsecrets.selectedItemsArray_color.clear();

		for (int i = 0; i < tlLayout.getChildCount(); i++) {
			TableRow viewGroup = (TableRow) tlLayout.getChildAt(i);
			if (viewGroup.getTag().toString().equalsIgnoreCase("true")) {

				TextView tv1 = (TextView) viewGroup.getChildAt(0);
				Explore.selectedItemsArray.add(tv1.getText().toString());

				if (Localsecrets.selectedItemsArray_color.contains(tv1
						.getText().toString())) {

				} else {
					Localsecrets.selectedItemsArray_color.add(tv1.getText()
							.toString());
				}

				items = items + tv1.getTag().toString() + "/";
			} 
		} 

		if (tvOneRupee.getTag().toString().contains("true")) {
//			selectedPrice = "price=1";
			Explore.selectedPrice = "1";
		} else if (tvTwoRupee.getTag().toString().contains("true")) {
//			selectedPrice = "price=2";
			Explore.selectedPrice = "2";
		} else if (tvThreeRupee.getTag().toString().contains("true")) {
//			selectedPrice = "price=3";
			Explore.selectedPrice = "3";
		} else if (tvFourRupee.getTag().toString().contains("true")) {
//			selectedPrice = "price=4";
			Explore.selectedPrice = "4";
		}

		// Intent in = new Intent();

		if (etLocation.getText().length() > 0) {

			if (etLocation.getText().toString().contains("All")) {
				// return;
			} else {
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
			}
		}

		if (arrayCusine.size() > 0)
			if (etCuisine1.getText().toString().length() > 0) {
				for (int i = 0; i < arrayCusineIds.size(); i++) {
					if (etCuisine1.getText().toString()
							.equalsIgnoreCase(arrayCusine.get(i))) {
						Explore.selectedCusine = i;
						break;
					}
				}
			}

		/*
		 * if (arrayCusine.size() > 0) if
		 * (etCusine1.getSelectedItem().toString().length() > 0) { for (int i =
		 * 0; i < arrayCusineIds.size(); i++) { if
		 * (etCusine.getSelectedItem().toString()
		 * .equalsIgnoreCase(arrayCusine.get(i))) { Explore.selectedCusine = i;
		 * break; } } }
		 */

		// String ss[] = Appconstants.staticURL.split("/");
		// String str = "";
		//
		// try {
		// for (int i = 0; i < 6; i++) {
		// str = str + ss[i] + "/";
		// }
		//
		// str = str + cusineId + "/";
		// for (int i = 7; i < ss.length; i++) {
		// str = str + ss[i] + "/";
		// }
		//
		// // str = str.sub
		//
		// } catch (Exception e) {
		// str = Appconstants.staticURL;
		// }
		// String url =
		// Appconstants.MAIN_HOST+"exploreRestaurants/"+locationId+cusineId+"/22960/null/"+Appconstants.LATTITUDE+"/"+Appconstants.LANGITUDE+"/";

		// in.putExtra("url", str + items + "/" + selectedPrice);
		// setResult(1015, in); 
		// finish();
		String result="";  
		if (Appconstants.strMenuflag.equalsIgnoreCase("Opennow")) {
			/*result = (Launcher.getRestaurent.substring(0,
					Launcher.getRestaurent.length() - 2) + "?" + items)
					.replace("?/", "?");
*/
			/*
			 * String ss = Appconstants.MAIN_HOST + "globalsearchmenu/" +
			 * Appconstants.strCityId + "/null/" + Appconstants.LATTITUDE + "/"
			 * + Appconstants.LANGITUDE +
			 * "/0?arg=1&searchpage=1&sort_by=distance";
			 */
			String ss = Appconstants.MAIN_HOST + "restaurantslisting/"
					+ Explore.type + "/" + Appconstants.strCityId + "/" 
					+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE
					+ "/1?";
			if (Integer.parseInt(Appconstants.ltDo.id) > 0) {
				ss = ss + "location=" + Appconstants.ltDo.id;
				Explore.search_string=Explore.search_string + "location=" + Appconstants.ltDo.id;
			}
			if (Explore.selectedCusine > 0) {
				ss = ss + "&cuisines="
						+ arrayCusineIds.get(Explore.selectedCusine);
				Explore.search_string=Explore.search_string + "&cuisines="
						+ arrayCusineIds.get(Explore.selectedCusine);
			}
			if (items.length() > 0) {
				ss = ss
						+ "&features="
						+ ((items.replace("=1//", ",").replace("=1/", ""))
								.replace("/", ""));
				Explore.search_string=Explore.search_string + "&features="
						+ ((items.replace("=1//", ",").replace("=1/", ""))
								.replace("/", ""));
			}
			if(!TextUtils.isEmpty(Explore.search)){
				ss = ss+ "&search_name="+Explore.search;
				Explore.search_string=Explore.search_string + "&search_name="+Explore.search;
			}
			ss = ss.replace("?&", "?");
		} else {

			String ss = Appconstants.MAIN_HOST + "restaurantslisting/"
					+ Explore.type + "/" + Appconstants.strCityId + "/"
					+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE
					+ "/1?";
			if (Integer.parseInt(Appconstants.ltDo.id) > 0) {
				ss = ss + "location=" + Appconstants.ltDo.id;
				Explore.search_string=Explore.search_string + "location=" + Appconstants.ltDo.id;
			}
			if (Explore.selectedCusine > 0) {
				ss = ss + "&cuisines="
						+ arrayCusineIds.get(Explore.selectedCusine);
				Explore.search_string=Explore.search_string + "&cuisines="
						+ arrayCusineIds.get(Explore.selectedCusine);
			}
			if (items.length() > 0) {
				ss = ss
						+ "&features="
						+ ((items.replace("=1//", ",").replace("=1/", ""))
								.replace("/", ""));
				Explore.search_string=Explore.search_string + "&features="
						+ ((items.replace("=1//", ",").replace("=1/", ""))
								.replace("/", ""));
			}
			if(!TextUtils.isEmpty(Explore.search)){
				ss = ss+ "&search_name="+Explore.search;
				Explore.search_string=Explore.search_string + "&search_name="+Explore.search;
			}
			ss = ss.replace("?&", "?");
			result = ss;
		}
		System.out.println("url is"+result);
		Explore.map_flag = Filters.flag;
		if (ConnectivityReceiver.checkInternetConnection(Filters.this)) {
		new RestaurantsList(Filters.this, result).execute();

	} else {
		ConnectivityReceiver.showCustomDialog(Filters.this);
	}

	}

	ArrayList<String> arrayCusine;

	class DisplayViews extends AsyncTask<String, String, String> {

		ArrayList<featuresDO> features = null;
		JSONArray jArray = null;

		@Override
		protected String doInBackground(String... params) {

			/*
			 * try {
			 * 
			 * if (!Launcher.restJsonDetails.isNull("existing_cusines")) {
			 * arrayCusine.clear(); arrayCusine.add("All Cusines");
			 * arrayCusineIds.add("null"); JSONObject cusine_lsit =
			 * Launcher.restJsonDetails .getJSONObject("existing_cusines");
			 * JSONArray tags = cusine_lsit.names(); for (int i = 0; i <
			 * tags.length(); i++) { arrayCusineIds.add(tags.get(i).toString());
			 * arrayCusine.add(cusine_lsit.getString(tags.get(i) .toString()));
			 * 
			 * } } else { arrayCusine.clear(); }
			 * 
			 * } catch (JSONException e) { e.printStackTrace(); }
			 */

			features = new ArrayList<featuresDO>();
			
			try {
				jj = Launcher.features.getJSONObject(0);
				jArray = jj.names();

			} catch (JSONException e) {

				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (arrayCusine.size() > 0) {
				ArrayAdapter<String> cusineAdp = new ArrayAdapter<String>(
						Filters.this, android.R.layout.simple_spinner_item,
						arrayCusine);
				cusineAdp
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				etCusine.setAdapter(cusineAdp);
				etCusine.setSelection(Explore.selectedCusine);
			} else {
				etCusine.setVisibility(View.GONE);
			}
			//

			if (jArray == null) {
			} else {
				for (int i = 0; i < jArray.length(); i++) {
					final View view = inflator.inflate(R.layout.features, null);
					view.setTag("false");
					final TextView tvItem = (TextView) view
							.findViewById(R.id.tvCrediCards);
					try {
						String text = jArray.getString(i);
						tvItem.setText(text);

						// text.
						tvItem.setTag("/"
								+ jj.optString(jArray.getString(i)).toString()
								+ "=1");

						if (Localsecrets.selectedItemsArray_color
								.contains(text)) {
							view.setTag("true");
							tvItem.setCompoundDrawablesWithIntrinsicBounds(0,
									0, R.drawable.checked, 0);
						} else {
							view.setTag("false");
							tvItem.setCompoundDrawablesWithIntrinsicBounds(0,
									0, R.drawable.uncheck, 0);
						}

						// if (text.contains("Credit")) {
						// tvItem.setTag("/paymenttype_id:1");
						// } else if (text.contains("Veg")) {
						// tvItem.setTag("/is_veg:1");
						// } else if (text.contains("Break")) {
						// tvItem.setTag("/has_breakfastbuffet:1");
						// } else if (text.contains("Lunch")) {
						// tvItem.setTag("/has_lunchbuffet:1");
						// } else if (text.contains("Dinner")) {
						// tvItem.setTag("/has_dinnerbuffet:1");
						// } else if (text.contains("Mid Night")) {
						// tvItem.setTag("/has_midnightbuffett:1");
						// } else if (text.contains("Hours")) {
						// tvItem.setTag("/happy_hours:1");
						// } else if (text.contains("Deals")) {
						// tvItem.setTag("/has_deals:1");
						// } else if (text.contains("Sunday")) {
						// tvItem.setTag("/has_sundayBrunch:1");
						// } else if (text.contains("Events")) {
						// tvItem.setTag("/has_events:1");
						// } else if (text.contains("Wifi")) {
						// tvItem.setTag("/has_wifi:1");
						// } else if (text.contains("Private")) {
						// tvItem.setTag("/private_dinning:1");
						// } else if (text.contains("Fine")) {
						// tvItem.setTag("/is_dinein:1");
						// } else if (text.contains("Liquor Served")) {
						// tvItem.setTag("/liqour_served:1");
						// } else if (text.contains("Group Friendly")) {
						// tvItem.setTag("/goodforgroups:1");
						// } else if (text.contains("Open Air Dining")) {
						// tvItem.setTag("/openair_dinning:1");
						// } else if (text.contains("Draught Beer")) {
						// tvItem.setTag("/draught_Beer:1");
						// } else if (text.contains("Notable Wine List")) {
						// tvItem.setTag("/notable_wine:1");
						// } else if (text.contains("chkImfl")) {
						// tvItem.setTag("/IMFL :1");
						// } else if (text.contains("Foreign Liquor")) {
						// tvItem.setTag("/foreign_liquor :1");
						// } else if (text.contains("Serves Cocktail")) {
						// tvItem.setTag("/serves_cocktail  :1");
						// } else if (text.contains("Live Music")) {
						// tvItem.setTag("/music:1");
						// } else if (text.contains("Star Property")) {
						// tvItem.setTag("/star:1");
						// }

						// else {
						// tvItem.setTag("");
						// }

					} catch (JSONException e) {
						e.printStackTrace();
					}

					/*
					 * for (int g = 0; g < Explore.selectedItemsArray.size();
					 * g++) { if
					 * (tvItem.getText().toString().equalsIgnoreCase(Explore.
					 * selectedItemsArray.get(g).toString())) {
					 * if(Localsecrets.selectedItemsArray_color
					 * .contains(Explore.selectedItemsArray.get(g).toString()))
					 * { view.setTag("true");
					 * tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					 * R.drawable.checked, 0); } else { view.setTag("false");
					 * tvItem.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					 * R.drawable.uncheck, 0); }
					 * 
					 * break; }
					 * 
					 * }
					 */

					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (view.getTag().toString().contains("false")) {
								view.setTag("true");
								tvItem.setCompoundDrawablesWithIntrinsicBounds(
										0, 0, R.drawable.checked, 0);
							} else {
								view.setTag("false");
								tvItem.setCompoundDrawablesWithIntrinsicBounds(
										0, 0, R.drawable.uncheck, 0);
							}
						}
					});

					tlLayout.addView(view);
				}

			}
		}
	}

	public void showCuisin() {
		ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
				R.layout.ato_row, arrayCusine);
		etCuisine1.setAdapter(adp);
		etCuisine1.setThreshold(1);
		/*
		 * if (arrayCusine.get(Integer.parseInt(arrayCusineIds
		 * .get(Explore.selectedCusine))).length() > 0) {
		 * etCuisine1.setText(arrayCusine.get(Integer.parseInt(arrayCusineIds
		 * .get(Explore.selectedCusine))));
		 * textView2.setVisibility(View.VISIBLE); }else{
		 */
		textView2.setVisibility(View.GONE);

		// }
	}
	class getBooktableFilters extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		String strVenueId1, jsonString, StrCat_Id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Filters.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String service_url = Appconstants.MAIN_HOST + "restaurantslisting/"
			+ Explore.type + "/" + Appconstants.strCityId + "/"
			+ Appconstants.LATTITUDE + "/"
			+ Appconstants.LANGITUDE + "/" + Explore.id1+"?filtersearch=1&"
			+ "location=" + Appconstants.ltDo.id;
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
				JSONArray jsonArray1 = mainObject.getJSONArray("existing_cusines");
				if (jsonArray.length() > 0) {
					
					Launcher.features = jsonArray;
					Launcher.existing_cusines = jsonArray1;
					Filters.this.finish();
					Intent in = new Intent(getApplicationContext(),
							Filters.class);

					startActivity(in);

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}
}
