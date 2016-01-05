package com.etisbew.eatz.android.orderfood;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etisbew.eatz.android.BaseActivity;
import com.etisbew.eatz.android.Explore;
import com.etisbew.eatz.android.Localsecrets;
import com.etisbew.eatz.android.Login;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.web.WebServiceCalls;

public class OrderDetails extends Activity {

	OrderDetailsAdapter adapterP;
	ListView order_listview;
	static ArrayList<OrderDetails_1> order_data_sublist = null;
	static HashMap<String, String> ietmWithPrice = null;
	static HashMap<String, String> ietmWithQty = null;
	static HashMap<String, String> ietmWithIcon = null;
	static HashMap<String, String> ietmWithCatid = null;

	public String name = "";
	public static String if_any = "";
	public static String Strdatetime = "";
	public static String ordertype = "";
	public static String StrIs_pick = "0";
	int selected_date = 0;

	JSONObject jsondates, jsontimes;
	ArrayList<String> arrayList = new ArrayList<String>();
	String[] arrayListDates, arrayListTimeval;

	ArrayList<String> arrayListTime = new ArrayList<String>();
	ArrayList<String> strOrderTimes = new ArrayList<String>();

	public ArrayList<String> indesWiseGroupItemIds = new ArrayList<String>();
	public HashMap<String, String> itemWithCatid = new HashMap<String, String>();
	public HashMap<String, String> itemWithQuantity = new HashMap<String, String>();
	public HashMap<String, String> itemWithPrice = new HashMap<String, String>();
	public HashMap<String, String> itemWithName = new HashMap<String, String>();
	public HashMap<String, String> itemWithIcon = new HashMap<String, String>();
	ArrayAdapter<String> arrayAdapter;

	TextView tv2, tv3;
	double totalPrice = 0.00;
	double totalVat = 0.00;
	double vatcharges, service_charges, delivery_charges;

	TextView cost, datepicker, timepicker, etRestName, VatItemsCost,
			txt_service_tax, txt_delivery_charges, txt_min_amount;
	ImageView back = null;
	RelativeLayout rr_vat, rr_service_tax, rr_delivery_charges;
	DecimalFormat twoDForm;
	EditText edt_spl_instruction;
	static int flag_clicked = 1;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_details);

		cost = (TextView) findViewById(R.id.TotalItemsCost);
		back = (ImageView) findViewById(R.id.back);
		order_listview = (ListView) findViewById(R.id.order_listview);
		datepicker = (TextView) findViewById(R.id.date_picker);
		VatItemsCost = (TextView) findViewById(R.id.VatItemsCost);

		txt_service_tax = (TextView) findViewById(R.id.serviceItemsCost);
		txt_delivery_charges = (TextView) findViewById(R.id.delivery_chargesItemsCost);

		twoDForm = new DecimalFormat("#.##");
		// vatcharges=(double)((OrderFood.tag_vatcharges*totalPrice)/100);
		// vatcharges=((Double.parseDouble(OrderFood.TAG_VATCHARGES)*(Double.parseDouble(OrderFood.TAG_TOTAL_PRICE)/100);
		// String VATCHARGES= Double.toString(vatcharges);

		rr_vat = (RelativeLayout) findViewById(R.id.rr_vat);
		rr_service_tax = (RelativeLayout) findViewById(R.id.rr_service_tax);
		rr_delivery_charges = (RelativeLayout) findViewById(R.id.rr_delivery_charges);
		txt_min_amount = (TextView) findViewById(R.id.mim_amount);
		if (Localsecrets.flg == 0) {
			rr_vat.setVisibility(View.GONE);
			rr_service_tax.setVisibility(View.GONE);
			rr_delivery_charges.setVisibility(View.GONE);
			// txt_min_amount.setVisibility(View.GONE);

		} else {
			rr_vat.setVisibility(View.VISIBLE);
			rr_service_tax.setVisibility(View.VISIBLE);
			rr_delivery_charges.setVisibility(View.VISIBLE);
			// txt_min_amount.setVisibility(View.VISIBLE);
		}

		if (Localsecrets.flg == 0)// ---------------------------------------------
		{
			txt_min_amount.setVisibility(View.INVISIBLE);
		} else if (Localsecrets.flg == 1) {

			if (Double.parseDouble(OrderFood.TAG_MIN_AMOUNT) > Double
					.parseDouble(OrderFood.TAG_TOTAL_PRICE)) {
				txt_min_amount.setVisibility(View.VISIBLE);
				txt_min_amount.setText("Minimum Order amount Rs: "
						+ OrderFood.TAG_MIN_AMOUNT);

			} else {

				txt_min_amount.setVisibility(View.INVISIBLE);
			}
		}

		/*
		 * if(totalPrice>=Double.parseDouble(OrderFood.TAG_MIN_AMOUNT.replace("Rs"
		 * , ""))) { txt_min_amount.setVisibility(View.GONE); } else {
		 * txt_min_amount.setVisibility(View.VISIBLE);
		 * txt_min_amount.setText("Minimum Order amount Rs: "
		 * +OrderFood.TAG_MIN_AMOUNT); }
		 */
		if (OrderFood.TAG_VATCHARGES.equalsIgnoreCase("0")) {
			rr_vat.setVisibility(View.GONE);
		} else {
			rr_vat.setVisibility(View.VISIBLE);
			if (OrderFood.TAG_VATCHARGES.equalsIgnoreCase("null")
					|| TextUtils.isEmpty(OrderFood.TAG_VATCHARGES)) {
				OrderFood.TAG_VATCHARGES = "0";
			}
			vatcharges = (Double.parseDouble(OrderFood.TAG_VATCHARGES) * Double
					.parseDouble(OrderFood.TAG_TOTAL_PRICE)) / 100;
			NumberFormat nm = NumberFormat.getNumberInstance();

			VatItemsCost.setText(getString(R.string.oneRupee) + " "
					+ nm.format(vatcharges));
		}

		if (OrderFood.TAG_SERVICE_CHARGE.equalsIgnoreCase("0")) {
			rr_service_tax.setVisibility(View.GONE);
		} else {
			rr_service_tax.setVisibility(View.VISIBLE);

			service_charges = (Double.parseDouble(OrderFood.TAG_SERVICE_CHARGE) * Double
					.parseDouble(OrderFood.TAG_TOTAL_PRICE)) / 100;

			// NumberFormat nm = NumberFormat.getNumberInstance();

			txt_service_tax.setText(getString(R.string.oneRupee) + " "
					+ twoDForm.format(service_charges));
		}

		if (OrderFood.TAG_DELIVERY_CHARGES.equalsIgnoreCase("0")) {
			rr_delivery_charges.setVisibility(View.GONE);
		} else {
			rr_delivery_charges.setVisibility(View.VISIBLE);
			if (OrderFood.TAG_DELIVERY_CHARGES.equalsIgnoreCase("null")) {
				OrderFood.TAG_DELIVERY_CHARGES = "0";
			}
			delivery_charges = (Double
					.parseDouble(OrderFood.TAG_DELIVERY_CHARGES));

			txt_delivery_charges.setText(getString(R.string.oneRupee) + " "
					+ twoDForm.format(delivery_charges));
		}

		// VatItemsCost.setText(VATCHARGES);

		/*
		 * String string1 = ".5";//Double in a string String string2 = "6";
		 * //Integer in a string double multiplied = Double.parseDouble(string1)
		 * * Integer.parseInt(string2) * 3; //.5 * 6
		 */

		etRestName = (TextView) findViewById(R.id.etRestName);
		etRestName.setText(Explore.strRestaurantTitle);

		timepicker = (TextView) findViewById(R.id.timepicker);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		arrayAdapter = new ArrayAdapter<String>(OrderDetails.this,
				android.R.layout.simple_list_item_1);

		try {
			ietmWithPrice = (HashMap<String, String>) getIntent()
					.getSerializableExtra("eachidprices");
			ietmWithQty = (HashMap<String, String>) getIntent()
					.getSerializableExtra("eachidqty");
			ietmWithIcon = (HashMap<String, String>) getIntent()
					.getSerializableExtra("eachidicon");
			ietmWithCatid = (HashMap<String, String>) getIntent()
					.getSerializableExtra("eachcatid");
			DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra(
					"orders");

			order_data_sublist = dw.getOrders();
			
			System.out.println("values are"+ietmWithPrice+ietmWithQty+order_data_sublist);
			// order_data_sublist=(ArrayList<OrderDetails_1>)
			// getIntent().getSerializableExtra("orderitems");
			// for (int i = 0; i < order_data_sublist.size(); i++) {
			// }

		} catch (Exception e) {
			Log.e("", "" + e);
		}

		edt_spl_instruction = (EditText) findViewById(R.id.edt_spl_instruction);
		tv2 = (TextView) findViewById(R.id.confirm_button);
		tv3 = (TextView) findViewById(R.id.textView1);
		if (Localsecrets.flg == 1) {
			tv3.setText("Home Delivery");
			ordertype = tv3.getText().toString();
			StrIs_pick = "0";
		} else if (Localsecrets.flg == 0) {

			tv3.setText("Pickup Order");
			ordertype = tv3.getText().toString();
			StrIs_pick = "1";
		} else if (Localsecrets.flg == 2) {
			tv3.setText("Fastlane Order");
			ordertype = tv3.getText().toString();
			StrIs_pick = "2";
		}

		if (!TextUtils.isEmpty(Appconstants.strOrderType)) {
			if (Appconstants.strOrderType.equalsIgnoreCase("Fastlane")) {
				tv3.setText("Fastlane Order");
			} else {
				tv3.setText(Appconstants.strOrderType);
			}

			ordertype = tv3.getText().toString();
			if (Appconstants.strOrderType.equalsIgnoreCase("Delivery")) {
				StrIs_pick = "0";
			} else if (Appconstants.strOrderType.equalsIgnoreCase("Fastlane")) {
				StrIs_pick = "2";
			} else {
				StrIs_pick = "1";
			}
			Appconstants.strOrderType = "";
		}
		tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if_any = edt_spl_instruction.getText().toString();
				Strdatetime = datepicker.getText().toString() + " "
						+ timepicker.getText().toString();
				if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
					if (Appconstants.user_flag == 2) {
						if (Strdatetime.length() > 0) {
							Calling();
						} else {
							Toast.makeText(getApplicationContext(),
									"Sorry! no available timings.",
									Toast.LENGTH_LONG).show();
						}
					} else {
						flag_clicked = 2;
						startActivity(new Intent(v.getContext(), Login.class));
					}
				} else {

					if (Strdatetime.length() > 0) {
						Calling();
					} else {
						Toast.makeText(getApplicationContext(),
								"Sorry! no available timings.",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		// new getOrderDateAndTimeTask().execute();
		if (ConnectivityReceiver.checkInternetConnection(OrderDetails.this)) {

			new getOrderDateAndTimeTask().execute();

		} else {
			ConnectivityReceiver.showCustomDialog(OrderDetails.this);
		}

		datepicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showTimesDialog(arrayListDates);
			}
		});

		timepicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showTimesDialog1();

				// final Calendar c = Calendar.getInstance();
				// hour = c.get(Calendar.HOUR_OF_DAY);
				// minute = c.get(Calendar.MINUTE);
				// showDialog(TIME_DIALOG_ID);

			}
		});
		// order_data_sublist.clear();

		adapterP = new OrderDetailsAdapter(getApplicationContext(),
				R.layout.order_details_1, order_data_sublist);

		order_listview.setAdapter(adapterP);
		getTotalPrice();
		// getTotalVat();

	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("details" + PreferenceUtils.getUserSession());
		if (flag_clicked == 2) {
			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				if (Appconstants.user_flag == 2) {
					if (Strdatetime.length() > 0) {
						Calling();
					} else {
						Toast.makeText(getApplicationContext(),
								"Sorry! no available timings.",
								Toast.LENGTH_LONG).show();
					}
				} else {
					flag_clicked = 2;
					// startActivity(new Intent(OrderDetails.this,
					// Login.class));
				}
			} else {

				if (Strdatetime.length() > 0) {
					Calling();
				} else {
					Toast.makeText(getApplicationContext(),
							"Sorry! no available timings.",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	private void Calling() {

		if (Localsecrets.flg == 0) {

			Intent secondIntent = new Intent(getApplicationContext(),
					OrderDetails_2.class);
			name = cost.getText().toString();

			Iterator<Map.Entry<String, String>> iterator = itemWithQuantity
					.entrySet().iterator();
			ArrayList<OrderDetails_1> ordersList = new ArrayList<OrderDetails_1>();
			while (iterator.hasNext()) {
				Map.Entry<String, String> pairs = iterator.next();
				String itemID = pairs.getKey().trim();
				OrderDetails_1 item = new OrderDetails_1(itemID,
						itemWithName.get(itemID), itemWithQuantity.get(itemID),
						itemWithIcon.get(itemID), itemWithCatid.get(itemID));
				ordersList.add(item);
				// ordersList.remove(item); // avoids a
				// ConcurrentModificationException
			}

			secondIntent.putExtra("name", name);
			secondIntent.putExtra("Strdatetime", Strdatetime);
			// secondIntent.putExtra("name1", name1);
			// secondIntent.putExtra("name2", name2);
			secondIntent.putExtra("orders", new DataWrapper(ordersList));
			secondIntent.putExtra("eachidprices", itemWithPrice);
			secondIntent.putExtra("eachidqty", itemWithQuantity);

			startActivity(secondIntent);
		} else if (Localsecrets.flg == 1) {

			if (OrderFood.TAG_MIN_AMOUNT.length() > 0) {
				if (Double.parseDouble(OrderFood.TAG_MIN_AMOUNT) <= Double
						.parseDouble(OrderFood.TAG_TOTAL_PRICE)) {
					Intent secondIntent = new Intent(getApplicationContext(),
							OrderDetails_2.class);
					name = cost.getText().toString();
					// name1=datepicker.getText().toString();
					// name2=timepicker.getText().toString();

					Iterator<Map.Entry<String, String>> iterator = itemWithQuantity
							.entrySet().iterator();
					ArrayList<OrderDetails_1> ordersList = new ArrayList<OrderDetails_1>();
					while (iterator.hasNext()) {
						Map.Entry<String, String> pairs = iterator.next();
						String itemID = pairs.getKey().trim();
						OrderDetails_1 item = new OrderDetails_1(itemID,
								itemWithName.get(itemID),
								itemWithQuantity.get(itemID),
								itemWithIcon.get(itemID),
								itemWithCatid.get(itemID));
						ordersList.add(item);
						// ordersList.remove(item); // avoids a
						// ConcurrentModificationException
					}

					secondIntent.putExtra("name", name);
					secondIntent.putExtra("Strdatetime", Strdatetime);
					// secondIntent.putExtra("name1", name1);
					// secondIntent.putExtra("name2", name2);
					secondIntent
							.putExtra("orders", new DataWrapper(ordersList));
					secondIntent.putExtra("eachidprices", itemWithPrice);
					secondIntent.putExtra("eachidqty", itemWithQuantity);

					startActivity(secondIntent);
				}
			}

		} else if (Localsecrets.flg == 2) {

			Intent secondIntent = new Intent(getApplicationContext(),
					OrderDetails_2.class);
			name = cost.getText().toString();

			Iterator<Map.Entry<String, String>> iterator = itemWithQuantity
					.entrySet().iterator();
			ArrayList<OrderDetails_1> ordersList = new ArrayList<OrderDetails_1>();
			while (iterator.hasNext()) {
				Map.Entry<String, String> pairs = iterator.next();
				String itemID = pairs.getKey().trim();
				OrderDetails_1 item = new OrderDetails_1(itemID,
						itemWithName.get(itemID), itemWithQuantity.get(itemID),
						itemWithIcon.get(itemID), itemWithCatid.get(itemID));
				ordersList.add(item);
				// ordersList.remove(item); // avoids a
				// ConcurrentModificationException
			}

			secondIntent.putExtra("name", name);
			secondIntent.putExtra("Strdatetime", Strdatetime);
			// secondIntent.putExtra("name1", name1);
			// secondIntent.putExtra("name2", name2);
			secondIntent.putExtra("orders", new DataWrapper(ordersList));
			secondIntent.putExtra("eachidprices", itemWithPrice);
			secondIntent.putExtra("eachidqty", itemWithQuantity);

			startActivity(secondIntent);

		}

	}

	private void showTimesDialog(final String[] citems) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				OrderDetails.this);
		builderSingle.setTitle("Select Date");

		builderSingle.setItems(citems, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, final int position) {
				// Do something with the selection

				try {
					datepicker.setText(citems[position]);
					selected_date = position;

					JSONArray jArray = jsontimes.getJSONArray(arrayListTime
							.get(position));
					arrayListTimeval = new String[jArray.length()];
					for (int i = 0; i < jArray.length(); i++) {
						arrayListTimeval[i] = jArray.getString(i);
					}

					timepicker.setText(arrayListTimeval[0]);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		});

		builderSingle.show();
	}

	private void showTimesDialog1() {
		// TODO Auto-generated method stub

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				OrderDetails.this);
		builderSingle.setTitle("Select Time");
		builderSingle.setItems(arrayListTimeval,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							final int position) {

						timepicker.setText(arrayListTimeval[position]);
					}

				});

		builderSingle.show();
	}

	// @Override
	// protected Dialog onCreateDialog(int id) {
	// switch (id) {
	// case TIME_DIALOG_ID:
	// // set time picker as current time
	// return new TimePickerDialog(this, timePickerListener, hour, minute,
	// false);
	//
	// }
	// return null;
	// }

	// private TimePickerDialog.OnTimeSetListener timePickerListener = new
	// TimePickerDialog.OnTimeSetListener() {
	// public void onTimeSet(TimePicker view, int selectedHour,
	// int selectedMinute) {
	// hour = selectedHour;
	// minute = selectedMinute;
	//
	// // set current time into textview
	// timepicker.setText(new StringBuilder().append(pad(hour))
	// .append(":").append(pad(minute)));
	//
	// }
	// };

	// private static String pad(int c) {
	// if (c >= 10)
	// return String.valueOf(c);
	// else
	// return "0" + String.valueOf(c);
	// }

	private void getTotalPrice() {

		Iterator<Map.Entry<String, String>> it = ietmWithQty.entrySet()
				.iterator();
		totalPrice = 0.00;
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = it.next();
			totalPrice = totalPrice
					+ (Double.parseDouble(ietmWithPrice.get(pairs.getKey())) * Integer
							.parseInt(pairs.getValue().trim()));
		}
		if (Localsecrets.flg == 1) {
			if (OrderFood.TAG_MIN_AMOUNT.length() > 0) {
				if (Double.parseDouble(OrderFood.TAG_MIN_AMOUNT) > totalPrice) {

					tv2.setVisibility(View.GONE);
					txt_min_amount.setVisibility(View.VISIBLE);
					txt_min_amount.setText("Minimum Order amount Rs: "
							+ OrderFood.TAG_MIN_AMOUNT);

				} else {
					tv2.setVisibility(View.VISIBLE);
					txt_min_amount.setVisibility(View.INVISIBLE);
				}
			}
		}
		double vat = totalPrice * Double.parseDouble(OrderFood.TAG_VATCHARGES)
				/ 100;
		NumberFormat nm = NumberFormat.getNumberInstance();
		VatItemsCost.setText(getString(R.string.oneRupee) + " "
				+ nm.format(vat));

		double service_chg = totalPrice
				* Double.parseDouble(OrderFood.TAG_SERVICE_CHARGE) / 100;
		// NumberFormat nm1 = NumberFormat.getNumberInstance();
		double service_chg1 = Math.round(service_chg * 100) / 100.0d;
		txt_service_tax.setText(getString(R.string.oneRupee) + " "
				+ service_chg1);
		if (OrderFood.TAG_DELIVERY_CHARGES.equalsIgnoreCase("null")) {
			OrderFood.TAG_DELIVERY_CHARGES = "0";
		}
		double grandtotal = totalPrice + vat + service_chg
				+ Double.parseDouble(OrderFood.TAG_DELIVERY_CHARGES);

		double grandtotal1 = Math.round(grandtotal * 100) / 100.0d;
		cost.setText(getString(R.string.oneRupee) + " " + grandtotal1);

	}

	class getOrderDateAndTimeTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		String strVenueId1, jsonString;
		String commonKey;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(OrderDetails.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "venueordertimings/" + Explore.strVenueId);

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			if (result == null || "".equals(result)) {
				return;
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					JSONArray categories = mainJson
							.getJSONArray("ordertimings");
					for (int i = 0; i < categories.length(); i++) {
						JSONObject eachCategory = categories.getJSONObject(i);
						jsondates = eachCategory.getJSONObject("dates");
						jsontimes = eachCategory.getJSONObject("times");
					}

					/** Dates array */

					Iterator<String> iter = jsondates.keys();
					arrayList.clear();
					// arrayListDates.clear();
					while (iter.hasNext()) {
						try {
							String key = iter.next();
							arrayList.add(key);
							// Object value = jsondates.get(key);
						} catch (Exception e) {
						}
					}
					Collections.sort(arrayList);
					arrayListDates = new String[arrayList.size()];
					for (int i = 0; i < +arrayList.size(); i++) {

						arrayListDates[i] = jsondates.getString(arrayList
								.get(i));
					}

					/** Times array */

					Iterator<String> iters = jsontimes.keys();
					arrayListTime.clear();
					// arrayListTimeval.clear();
					while (iters.hasNext()) {
						try {
							String key = iters.next();
							arrayListTime.add(key);
						} catch (Exception e) {
						}
					}

					Collections.sort(arrayListTime);

					JSONArray jArray = jsontimes.getJSONArray(arrayListTime
							.get(selected_date));
					arrayListTimeval = new String[jArray.length()];
					for (int i = 0; i < jArray.length(); i++) {
						arrayListTimeval[i] = jArray.getString(i);
					}

					datepicker.setText(arrayListDates[0]);
					timepicker.setText(arrayListTimeval[0]);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class OrderDetailsAdapter extends ArrayAdapter<OrderDetails_1> {

		private ArrayList<OrderDetails_1> dataValues;
		@SuppressWarnings("unused")
		private Context mContext;

		public OrderDetailsAdapter(Context context, int textViewResourceId,
				ArrayList<OrderDetails_1> items) {
			super(context, textViewResourceId, items);
			this.dataValues = items;
			this.mContext = context;
		}

		@Override
		public int getCount() {

			/*
			 * if (order_data_sublist.size() == 0) { Intent i = new
			 * Intent(getApplicationContext(), OrderFood.class);
			 * startActivity(i); Login.showDialogMsg(OrderDetails.this,
			 * "Your cart is empty. Please wait you are redirected ......."); }
			 */
			return order_data_sublist.size();
		}

		@Override
		public OrderDetails_1 getItem(int position) {
			return dataValues.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;

			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.order_details_1, null);
			}

			final TextView t1 = (TextView) view.findViewById(R.id.title);
			final TextView t2 = (TextView) view.findViewById(R.id.count_txt);
			final TextView t3 = (TextView) view.findViewById(R.id.total);
			final ImageView iv = (ImageView) view.findViewById(R.id.iv_image);
			final OrderDetails_1 temp = order_data_sublist.get(position);
			t1.setText(temp.title.trim());
			t2.setText(ietmWithQty.get(temp.itemId));

			if (temp.image.equalsIgnoreCase("1")) {
				iv.setImageResource(R.drawable.ic_non_veg);
			} else {
				iv.setImageResource(R.drawable.green);
			}

			t3.setText(""
					+ (Double.parseDouble(ietmWithPrice.get(temp.itemId)) * Integer
							.parseInt(ietmWithQty.get(temp.itemId))));

			final TextView btnMinus = ((TextView) view.findViewById(R.id.minus));
			final TextView btnPlus = ((TextView) view.findViewById(R.id.plus));
			btnMinus.setTag("" + temp.itemId);
			btnPlus.setTag("" + temp.itemId);

			btnPlus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String prevStrQty = ietmWithQty.get(arg0.getTag()
							.toString());
					if (prevStrQty == null)
						prevStrQty = "0";
					int prevQty = Integer.parseInt(prevStrQty);

					int newQty = prevQty + 1;
					if (newQty > 0) {

						t2.setText("" + newQty);
						ietmWithQty.put(arg0.getTag().toString(), newQty + "");
						t3.setText(""
								+ (Double.parseDouble(ietmWithPrice.get(arg0
										.getTag().toString())) * Integer
										.parseInt(ietmWithQty.get(arg0.getTag()
												.toString()))));
						getTotalPrice();

					}
				}
			});

			btnMinus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String prevStrQty = ietmWithQty.get(arg0.getTag()
							.toString());
					int prevQty = Integer.parseInt((prevStrQty == null) ? "0"
							: prevStrQty);

					int newQty = prevQty - 1;
					if (newQty >= 0) {

						t2.setText("" + newQty);
						ietmWithQty.put(arg0.getTag().toString(), newQty + "");
						t3.setText(""
								+ (Double.parseDouble(ietmWithPrice.get(arg0
										.getTag().toString())) * Integer
										.parseInt(ietmWithQty.get(arg0.getTag()
												.toString()))));

						getTotalPrice();

						int x = Integer.parseInt(OrderDetails.ietmWithQty
								.get(temp.itemId));
						if (x <= 0) {

							OrderDetails.order_data_sublist.remove(temp);
							getBackToPrevious();
							adapterP.notifyDataSetChanged();

						}
					}
				}
			});

			return view;
		}
	}

	public void getBackToPrevious() {
		if (order_data_sublist.size() == 0) {
			Intent i = new Intent(getApplicationContext(), OrderFood.class);
			startActivity(i);
			OrderDetails.this.finish();
			BaseActivity
					.showDialogMsg(OrderDetails.this,
							"Your cart is empty. Please wait you are redirected .......");
		}
	}
}