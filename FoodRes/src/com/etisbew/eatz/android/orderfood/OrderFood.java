package com.etisbew.eatz.android.orderfood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eatz.android.Explore;
import com.etisbew.eatz.android.Launcher;
import com.etisbew.eatz.android.Localsecrets;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.android.SearchDetails;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.objects.reviewDO;
import com.etisbew.eatz.web.WebServiceCalls;

public class OrderFood extends Activity implements OnClickListener {
	OrderDetailsAdapter adapterP;
	ExpandableListAdapter adapter;
	public ExpandableListView expandableList;
	ListView order_listview;
	public ArrayList<String> indesWiseGroupItemIds = new ArrayList<String>();

	public HashMap<String, String> itemWithQuantity = new HashMap<String, String>();
	public HashMap<String, String> itemWithCatId = new HashMap<String, String>();
	public HashMap<String, String> itemWithPrice = new HashMap<String, String>();
	public HashMap<String, String> itemWithName = new HashMap<String, String>();
	public HashMap<String, String> itemWithIcon = new HashMap<String, String>();
	TextView itemQuntity = null, itemQuntity1 = null, tv_ordertime1, tv_dist1,
			tv_Cash1, tv_minOrder, textview_order_food, tv_chartamount, txtVat,
			txtTax, txtDelivery, tvPhone, tv_address1,tv_timing_message;
	LinearLayout llMinOrder, linearCart, ll_rightavailable;
	ImageView imgv_btn_back;
	RelativeLayout goNext, relative_cart;
	View view_id1;

	public static String TAG_VATCHARGES = "", TAG_DELIVERY_CHARGES = "",
			TAG_SERVICE_CHARGE = "", TAG_CATEGORY_ID = "",
			TAG_PRODUCT_PRICE = "", TAG_PRODUCT_ID = "";
	public static String TAG_TOTAL_PRICE = "", TAG_MIN_AMOUNT = "";
	public static String IMAGE = "";
	double totalprice = 0;
	Double lat = 0.0, lon = 0.0;
	String phone = "";
	String url1 = "";
	ListView lv_cart;
	TextView count_txt, title, total;
	int repeat_order = 0;
	int order_id = 0;
	static ArrayList<OrderDetails_1> order_data_sublist = null;
	static HashMap<String, String> ietmWithPrice = null;
	static HashMap<String, String> ietmWithQty = null;
	static HashMap<String, String> ietmWithIcon = null;
	int flag = 1;
	ImageView iv_menubar;
	ImageView ivNext;
	static int gateway;
	static int ordertype;
	static int paytm_flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.orderlist);

		Intent iin = getIntent();
		Bundle extras = iin.getExtras();

		if (extras != null) {
			repeat_order = Integer.parseInt(extras.getString("myorder"));
			order_id = Integer.parseInt(extras.getString("order_id"));

		}

		LinearLayout llCall = (LinearLayout) findViewById(R.id.llCall);
		llCall.setOnClickListener(this);

		linearCart = (LinearLayout) findViewById(R.id.linearCart);
		linearCart.setOnClickListener(this);

		LinearLayout llWriteReview = (LinearLayout) findViewById(R.id.writeReview);
		llWriteReview.setOnClickListener(this);

		ll_rightavailable = (LinearLayout) findViewById(R.id.ll_rightavailable);

		LinearLayout llDirections = (LinearLayout) findViewById(R.id.llDirections);
		llDirections.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse("http://maps.google.com/maps?saddr="
								+ Appconstants.LATTITUDE + ","
								+ Appconstants.LANGITUDE + "&daddr=" + lat
								+ "," + lon));
				startActivity(intent);
			}
		});

		expandableList = (ExpandableListView) findViewById(R.id.list);

		order_listview = (ListView) findViewById(R.id.order_listview);

		expandableList.setDividerHeight(2);
		expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);

		RelativeLayout goNext = (RelativeLayout) findViewById(R.id.rr_rightheader);
		goNext.setOnClickListener(this);

		ivNext = (ImageView) findViewById(R.id.img_back);
		ivNext.setOnClickListener(this);

		// for displaying Total selected items Quantity in Text view.
		itemQuntity = (TextView) findViewById(R.id.tv_itemno);
		itemQuntity1 = (TextView) findViewById(R.id.tv_itemno1);
		tv_ordertime1 = (TextView) findViewById(R.id.tv_ordertime1);
		tv_dist1 = (TextView) findViewById(R.id.tv_dist1);
		tv_Cash1 = (TextView) findViewById(R.id.tv_Cash1);
		tv_minOrder = (TextView) findViewById(R.id.tv_minOrder1);
		textview_order_food = (TextView) findViewById(R.id.textview_order_food);
		textview_order_food.setText(Explore.strRestaurantTitle);
		tv_chartamount = (TextView) findViewById(R.id.tv_chartamount);
		tv_chartamount.setOnClickListener(this);
		imgv_btn_back = (ImageView) findViewById(R.id.imgv_btn_back);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		tv_address1 = (TextView) findViewById(R.id.tv_address1);
		
		tv_timing_message = (TextView) findViewById(R.id.tv_timing_message);
		
		relative_cart = (RelativeLayout) findViewById(R.id.relative_cart);
		view_id1 = findViewById(R.id.view_id1);
		relative_cart.setOnClickListener(this);
		iv_menubar = (ImageView) findViewById(R.id.iv_menubar);
		System.out.println("repeat order" + repeat_order + order_id);
		Spanned text = Html.fromHtml("cart amount :  <b>"
				+ getString(R.string.oneRupee) + " 0 </b>/-");

		tv_chartamount.setText(text);
		// for my orders data
		txtVat = (TextView) findViewById(R.id.txtVat);
		txtVat.setOnClickListener(this);
		txtTax = (TextView) findViewById(R.id.txtTax);
		txtTax.setOnClickListener(this);
		txtDelivery = (TextView) findViewById(R.id.txtDelivery);
		txtDelivery.setOnClickListener(this);

		imgv_btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				OrderFood.this.finish();

			}
		});

		llMinOrder = (LinearLayout) findViewById(R.id.llMinOrder);
		System.out.println("flag is" + Localsecrets.flg);
		if (Localsecrets.flg == 0) {
			llMinOrder.setVisibility(View.GONE);
		} else {
			if (Localsecrets.flg == 2) {
				llMinOrder.setVisibility(View.GONE);
			} else {
				llMinOrder.setVisibility(View.VISIBLE);
			}
		}
		if (Localsecrets.flg == 2) {
			ll_rightavailable.setVisibility(View.GONE);

		} else {
			ll_rightavailable.setVisibility(View.VISIBLE);

		}

		expandableList.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousItem = -1;

			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				order_listview.setVisibility(View.GONE);
				view_id1.setVisibility(View.VISIBLE);
				if (groupPosition != previousItem)
					expandableList.collapseGroup(previousItem);
				previousItem = groupPosition;
				iv_menubar.setImageResource(R.drawable.down_accessory);
			}
		});

		//if (ConnectivityReceiver.checkInternetConnection(OrderFood.this)) {

			new getOrderCategoriesTask().execute();

		/*} else {
			ConnectivityReceiver.showCustomDialog(OrderFood.this);
		}*/

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	public void Dialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		// ...Irrelevant code for customizing the buttons and title
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.selected_cart_list, null);
		dialogBuilder.setView(dialogView);

		/*
		 * EditText editText = (EditText)
		 * dialogView.findViewById(R.id.label_field);
		 * editText.setText("test label");
		 */

		lv_cart = (ListView) dialogView.findViewById(R.id.lv_cart);
		count_txt = (TextView) dialogView.findViewById(R.id.count_txt);
		title = (TextView) dialogView.findViewById(R.id.title);
		total = (TextView) dialogView.findViewById(R.id.total);
		AlertDialog alertDialog = dialogBuilder.create();

		alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		alertDialog.show();
	}

	public void updateQtyTextView() {
		adapter.notifyDataSetChanged();
		// setMyOrder();
		Iterator<Map.Entry<String, String>> it = itemWithQuantity.entrySet()
				.iterator();
		int selectedItemCount = 0;
		totalprice = 0;
		while (it.hasNext()) {
			Map.Entry<String, String> pairs = it
					.next();
			selectedItemCount = selectedItemCount
					+ Integer.parseInt(pairs.getValue().trim());
			totalprice = totalprice
					+ (Double.parseDouble(itemWithPrice.get(pairs
							.getKey())) * Integer.parseInt(pairs.getValue()
							.trim()));
			// it.remove(); // avoids a ConcurrentModificationException
		}
		itemQuntity.setText(selectedItemCount + "");
		itemQuntity1.setText(selectedItemCount + "");
		if (Integer.parseInt(itemQuntity.getText().toString()) > 0) {
			tv_chartamount.setVisibility(View.VISIBLE);

			if (Localsecrets.flg == 0) {
				Spanned text = Html.fromHtml("cart amount :  <b>"
						+ getString(R.string.oneRupee) + " " + totalprice
						+ "</b>/-");

				tv_chartamount.setText(text);
				ivNext.setVisibility(View.VISIBLE);
			} else if (Localsecrets.flg == 1) {
				if (TAG_MIN_AMOUNT.length() > 0) {
					if (totalprice < Double.parseDouble(TAG_MIN_AMOUNT.replace(
							"Rs", ""))) {

						ivNext.setVisibility(View.INVISIBLE);
						tv_chartamount.setBackgroundColor(Color
								.parseColor("#FF0000"));
						tv_chartamount.setText("Minimum Order :"
								+ getString(R.string.oneRupee) + " "
								+ TAG_MIN_AMOUNT + "\n Cart amount " + "( "
								+ getString(R.string.oneRupee) + +totalprice
								+ " )");
						// tv_chartamount.setBackgroundColor(Color.RED);
					} else {
						// tv_chartamount.setBackgroundColor(Color.GREEN);//#088A08
						ivNext.setVisibility(View.VISIBLE);
						tv_chartamount.setBackgroundColor(Color
								.parseColor("#088A08"));
						Spanned text = Html.fromHtml("cart amount :  <b>"
								+ getString(R.string.oneRupee) + " "
								+ totalprice + "</b>/-");

						tv_chartamount.setText(text);
						/*
						 * tv_chartamount.setText("cart amount : "
						 * +getString(R.string.oneRupee) +" "+ totalprice +
						 * "/-");
						 */
					}
				}
			} else if (Localsecrets.flg == 2) {
				if (TAG_MIN_AMOUNT.length() > 0) {
					/*
					 * if (totalprice <
					 * Double.parseDouble(TAG_MIN_AMOUNT.replace( "Rs", ""))) {
					 * 
					 * ivNext.setVisibility(View.INVISIBLE);
					 * tv_chartamount.setBackgroundColor(Color
					 * .parseColor("#FF0000")); tv_chartamount.setText(
					 * " Cart amount" + "( " + totalprice + " )"); //
					 * tv_chartamount.setBackgroundColor(Color.RED); } else {
					 */
					// tv_chartamount.setBackgroundColor(Color.GREEN);//#088A08
					ivNext.setVisibility(View.VISIBLE);
					/*
					 * tv_chartamount.setBackgroundColor(Color
					 * .parseColor("#088A08"));
					 */
					Spanned text = Html.fromHtml("cart amount :  <b>"
							+ getString(R.string.oneRupee) + " " + totalprice
							+ "</b>/-");

					tv_chartamount.setText(text);
					/*
					 * tv_chartamount.setText("cart amount : "
					 * +getString(R.string.oneRupee) +" "+ totalprice + "/-");
					 */
					// }
				}

			} else {
				ivNext.setVisibility(View.VISIBLE);
				tv_chartamount.setText("cart amount : "
						+ getString(R.string.oneRupee) + " " + totalprice
						+ "/-");
			}

			TAG_TOTAL_PRICE = String.valueOf(totalprice);

		} else {
			Spanned text = Html.fromHtml("cart amount :  <b>"
					+ getString(R.string.oneRupee) + " 0 </b>/-");

			tv_chartamount.setText(text);
		}

	}

	public void getGroupChildItems(int groupPosition) {

		if (ConnectivityReceiver.checkInternetConnection(OrderFood.this)) {

			new GetGroupChildItems().execute(groupPosition + "");

		} else {
			ConnectivityReceiver.showCustomDialog(OrderFood.this);
		}

	}

	class getOrderCategoriesTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		String strVenueId1, jsonString, StrCat_Id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(OrderFood.this);
			pDialog.setMessage("Loading ..."); 
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String service_url = "";
			
			if (Launcher.restOrderDetails.has("settings")) {
				try {
					JSONObject mainObject = Launcher.restOrderDetails;
					JSONArray jsonArray = mainObject.getJSONArray("settings");
					if (mainObject.has("Products")) {
						JSONArray jsonArray1 = mainObject.getJSONArray("Products");
						for (int i = 0; i < jsonArray1.length(); i++) {

							if (jsonArray1.getJSONObject(i).has("id")
									&& jsonArray1.getJSONObject(i).has("title")
									&& jsonArray1.getJSONObject(i).has(
											"product_qty")
									&& jsonArray1.getJSONObject(i).has("spicy")
									&& jsonArray1.getJSONObject(i).has(
											"product_price")) {
								/*
								 * strTime = jsonArray.getJSONObject(0)
								 * .optString("start_time");
								 */

								itemWithQuantity
										.put(jsonArray1.getJSONObject(i).optString(
												"id"), jsonArray1.getJSONObject(i)
												.optString("product_qty"));
								itemWithCatId
										.put(jsonArray1.getJSONObject(i).optString(
												"id"), jsonArray1.getJSONObject(i)
												.optString("venuemenucategory_id"));
								itemWithPrice
										.put(jsonArray1.getJSONObject(i).optString(
												"id"), jsonArray1.getJSONObject(i)
												.optString("product_price"));
								itemWithName
										.put(jsonArray1.getJSONObject(i).optString(
												"id"), jsonArray1.getJSONObject(i)
												.optString("title"));
								itemWithIcon
										.put(jsonArray1.getJSONObject(i).optString(
												"id"), jsonArray1.getJSONObject(i)
												.optString("spicy"));
							}
						}
					}

					String strTime = "";
					if (jsonArray.getJSONObject(0).has("start_time")) {
						strTime = jsonArray.getJSONObject(0)
								.optString("start_time");
					}
					if (jsonArray.getJSONObject(0).has("latitude")) {
						lat = Double.parseDouble(jsonArray.getJSONObject(0)
								.optString("latitude"));
						strTime = jsonArray.getJSONObject(0)
								.optString("start_time");
					}
					if (jsonArray.getJSONObject(0).has("longitude")) {
						lon = Double.parseDouble(jsonArray.getJSONObject(0)
								.optString("longitude"));
						strTime = jsonArray.getJSONObject(0)
								.optString("start_time");
					}
					if (jsonArray.getJSONObject(0).has("pgstatus")) {
						paytm_flag = Integer.parseInt(jsonArray.getJSONObject(0).optString("pgstatus"));
						System.out.println("paytm_flag"+paytm_flag);
						//paytm_flag=1;
					
					}
					
					
					if (jsonArray.getJSONObject(0).has("phone")) {
						phone = jsonArray.getJSONObject(0).optString("phone"); 
						
						try {
							
							if (phone.contains(",")) {
								String s[] = phone.split(",");

								tvPhone.setText(s[0] +" \nExt. "+s[1]);
								
							} else {

								tvPhone.setText(phone);
							}

						} catch (Exception e) {
							tvPhone.setText(phone);
						}
						
					}
					String add="";
					if (jsonArray.getJSONObject(0).has("address1")
							&& jsonArray.getJSONObject(0).has("address2")) {
						add = jsonArray.getJSONObject(0).optString(
								"address1")
								+ "," 
								+ jsonArray.getJSONObject(0).optString("address2");
						
					}
					if (jsonArray.getJSONObject(0).has("today")){
						if(Integer.parseInt(jsonArray.getJSONObject(0).optString("today")) ==0){
							tv_timing_message.setVisibility(View.VISIBLE);
							
						tv_timing_message.setText(Explore.strRestaurantTitle+" is not accepting Orders for today.");
						}else{
							tv_timing_message.setVisibility(View.GONE);
						}
						
					}else{
						tv_timing_message.setVisibility(View.GONE);
					}
					if (jsonArray.getJSONObject(0).has("location")){
						add=add+", "+jsonArray.getJSONObject(0).optString("location");
						tv_address1.setText(add);
					}else{
						tv_address1.setText(add);
					}

					if (jsonArray.getJSONObject(0).has("end_time")) {
						strTime = strTime + " - "
								+ jsonArray.getJSONObject(0).optString("end_time");
					}
					if (jsonArray.getJSONObject(0).has("start_time2")) {
						if (!jsonArray.getJSONObject(0).optString("start_time2")
								.equalsIgnoreCase("null")
								&& jsonArray.getJSONObject(0)
										.optString("start_time2").length() > 2)
							strTime = strTime
									+ " & "
									+ jsonArray.getJSONObject(0).optString(
											"start_time2");
					}
					if (jsonArray.getJSONObject(0).has("end_time2")) {
						if (!jsonArray.getJSONObject(0).optString("end_time2")
								.equalsIgnoreCase("null")
								&& jsonArray.getJSONObject(0)
										.optString("end_time2").length() > 2)
							strTime = strTime
									+ " - "
									+ jsonArray.getJSONObject(0).optString(
											"end_time2");
					}
					tv_ordertime1.setText(strTime);

					if (jsonArray.getJSONObject(0).has("vat_charges")) {
						TAG_VATCHARGES = jsonArray.getJSONObject(0)
								.optString("vat_charges").replace("null", "0");
						txtVat.setText(TAG_VATCHARGES);
					}
					if (jsonArray.getJSONObject(0).has("service_charges")) {
						TAG_SERVICE_CHARGE = jsonArray.getJSONObject(0)
								.optString("service_charges").replace("null", "0");
						txtTax.setText(TAG_SERVICE_CHARGE);
					}
					if (jsonArray.getJSONObject(0).has("deliverycharges")) {

						TAG_DELIVERY_CHARGES = jsonArray.getJSONObject(0)
								.optString("deliverycharges").replace("null", "0");
						txtDelivery.setText(TAG_DELIVERY_CHARGES);
					}
					
					
					if (jsonArray.getJSONObject(0).has("ordertype")) {

						ordertype = Integer.parseInt(jsonArray.getJSONObject(0)
								.optString("ordertype"));
					}
				
						if (jsonArray.getJSONObject(0).has("paymentgateway")) {

							gateway = Integer.parseInt(jsonArray.getJSONObject(0)
									.optString("paymentgateway"));
						}
				
					System.out.println("flag" + Localsecrets.flg + "::"
							+ jsonArray.getJSONObject(0).optString("min_order"));
					if (Localsecrets.flg == 1) {

						if (Integer.parseInt(jsonArray.getJSONObject(0).optString(
								"min_order")) == 0) {
							TAG_MIN_AMOUNT = jsonArray.getJSONObject(0)
									.optString("min_order").replace("null", "");
							// llMinOrder.setVisibility(View.GONE);
							tv_minOrder.setText(jsonArray.getJSONObject(0)
									.optString("min_order"));
							// Toast.makeText(getApplicationContext(),
							// ""+jsonArray.getJSONObject(0).optString(
							// "min_order"), 200).show();
						} else {
							// llMinOrder.setVisibility(View.VISIBLE);
							if (jsonArray.getJSONObject(0).has("min_order")) {
								TAG_MIN_AMOUNT = jsonArray.getJSONObject(0)
										.optString("min_order").replace("null", "");
								tv_minOrder.setText(getString(R.string.oneRupee)
										+ " " + TAG_MIN_AMOUNT);
								Localsecrets.flg = 1;
								llMinOrder.setVisibility(View.VISIBLE);

							}
						}
					} else if (Localsecrets.flg == 2) {
						if (Integer.parseInt(jsonArray.getJSONObject(0).optString(
								"min_order")) == 0) {
							TAG_MIN_AMOUNT = jsonArray.getJSONObject(0)
									.optString("min_order").replace("null", "");
							// llMinOrder.setVisibility(View.GONE);
							tv_minOrder.setText(jsonArray.getJSONObject(0)
									.optString("min_order"));
							// Toast.makeText(getApplicationContext(),
							// ""+jsonArray.getJSONObject(0).optString(
							// "min_order"), 200).show();
						} else {
							// llMinOrder.setVisibility(View.VISIBLE);
							if (jsonArray.getJSONObject(0).has("min_order")) {
								TAG_MIN_AMOUNT = jsonArray.getJSONObject(0)
										.optString("min_order").replace("null", "");
								tv_minOrder.setText(getString(R.string.oneRupee)
										+ " " + TAG_MIN_AMOUNT);
								Localsecrets.flg = 2;
								// llMinOrder.setVisibility(View.VISIBLE);

							}
						}
					}

					ArrayList<ExpandableListItems> parentWithChild = new ArrayList<ExpandableListItems>();
					JSONArray categories = mainObject.getJSONArray("Categories");
					for (int i = 0; i < categories.length(); i++) {
						JSONObject eachCategory = categories.getJSONObject(i);
						ExpandableListItems item = new ExpandableListItems();
						item.setParent(eachCategory.getString("title"));

						item.setParentChildren(new ArrayList<HashMap<String, String>>());
						parentWithChild.add(item);
						indesWiseGroupItemIds.add(eachCategory.getString("id"));

						TAG_CATEGORY_ID = eachCategory.getString("id");

					}

					adapter = new ExpandableListAdapter(OrderFood.this,
							parentWithChild);
					expandableList
							.setOnChildClickListener(new OnChildClickListener() {
								@Override
								public boolean onChildClick(
										ExpandableListView parent, View v,
										int groupPosition, int childPosition,
										long id) {
									// TODO Auto-generated method stub
									// Toast.makeText(getApplicationContext(),
									// "" + id, 200).show();
									return false;
								}
							});

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				expandableList.setAdapter(adapter);
				// if (itemWithQuantity.size() > 0) {
				setUpdate();
			}
		
			return service_url;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	}

	public void setUpdate() {
		try {
			if (repeat_order == 2) {
				/*
				 * itemWithQuantity.put("" + 57879, 2 + "");
				 * itemWithPrice.put("" + 57879, 40.00 + "");
				 * itemWithName.put("" + 57879, "Green Salad");
				 * itemWithIcon.put("" + 57879, "0");
				 */
				if (itemWithQuantity.size() == 0) {
					iv_menubar.setImageResource(R.drawable.up_accessory);
					relative_cart.setVisibility(View.GONE);
					order_listview.setVisibility(View.GONE);
				} else {
					Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
							.entrySet().iterator();
					ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

					while (iterator2.hasNext()) {
						Map.Entry<String, String> pairs = iterator2
								.next();
						String itemID = pairs.getKey().trim();
						OrderDetails_1 item = new OrderDetails_1(itemID,
								itemWithName.get(itemID),
								itemWithQuantity.get(itemID),
								itemWithIcon.get(itemID),
								itemWithCatId.get(itemID));
						ordersList2.add(item);
						// it.remove(); // avoids a
						// ConcurrentModificationException
					}
					order_data_sublist = ordersList2;

					ietmWithPrice = itemWithPrice;
					ietmWithQty = itemWithQuantity;
					ietmWithIcon = itemWithIcon;

					updateQtyTextView();
					adapterP = new OrderDetailsAdapter(getApplicationContext(),
							R.layout.order_details_1, order_data_sublist);

					order_listview.setAdapter(adapterP);
					relative_cart.setVisibility(View.VISIBLE);
					order_listview.setVisibility(View.VISIBLE);
					if (itemWithQuantity.size() == 0) {
						order_listview.setVisibility(View.GONE);
					}
				}
			} else {
				relative_cart.setVisibility(View.GONE);
				order_listview.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (repeat_order == 2) {
			adapterP = new OrderDetailsAdapter(getApplicationContext(),
					R.layout.order_details_1, order_data_sublist);

			order_listview.setAdapter(adapterP);
		}

	}

	public void setUpdate1() {

		if (itemWithQuantity.size() == 0) {
			iv_menubar.setImageResource(R.drawable.up_accessory);
			relative_cart.setVisibility(View.GONE);
			order_listview.setVisibility(View.GONE);
		} else {
			Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
					.entrySet().iterator();
			ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

			while (iterator2.hasNext()) {
				Map.Entry<String, String> pairs = iterator2
						.next();
				String itemID = pairs.getKey().trim();
				OrderDetails_1 item = new OrderDetails_1(itemID,
						itemWithName.get(itemID), itemWithQuantity.get(itemID),
						itemWithIcon.get(itemID), itemWithCatId.get(itemID));
				ordersList2.add(item);
				// it.remove(); // avoids a
				// ConcurrentModificationException
			}
			order_data_sublist = ordersList2;

			ietmWithPrice = itemWithPrice;
			ietmWithQty = itemWithQuantity;
			ietmWithIcon = itemWithIcon;

			updateQtyTextView();
			adapterP = new OrderDetailsAdapter(getApplicationContext(),
					R.layout.order_details_1, order_data_sublist);

			order_listview.setAdapter(adapterP);
			relative_cart.setVisibility(View.VISIBLE);
			order_listview.setVisibility(View.VISIBLE);
			if (itemWithQuantity.size() == 0) {
				order_listview.setVisibility(View.GONE);
			}
			relative_cart.setVisibility(View.VISIBLE);
			order_listview.setVisibility(View.VISIBLE);
		}
	}

	class GetGroupChildItems extends AsyncTask<Object, String, String> {

		// private static final ExpandableListView expandableList = null;
		// private static final ListAdapter adapter = null;
		// private static final ArrayList<String> parentItems = null;
		ProgressDialog pDialog;
		String strVenueId1, jsonString;
		public int groupPosition;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(OrderFood.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(Object... params) {
			groupPosition = Integer.parseInt(params[0].toString().trim());
			System.out.println("url is" + Appconstants.MAIN_HOST
					+ "orderFoodProducts_ad/" + Explore.strVenueId + "/"
					+ indesWiseGroupItemIds.get(groupPosition));

			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "orderFoodProducts_ad/" + Explore.strVenueId + "/"
					+ indesWiseGroupItemIds.get(groupPosition));
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
				ArrayList<HashMap<String, String>> childrenItems = adapter.items
						.get(groupPosition).getParentChildren();
				JSONArray items = mainObject.getJSONArray("Products");
				for (int i = 0; i < items.length(); i++) {
					JSONObject eachChildItem = items.getJSONObject(i);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("title", eachChildItem.getString("title"));
					map.put("price", eachChildItem.getString("product_price"));
					map.put("veg_type", eachChildItem.getString("veg_type"));
					map.put("id", eachChildItem.getString("id"));

					itemWithPrice.put(eachChildItem.getString("id"),
							eachChildItem.getString("product_price"));
					itemWithCatId.put(eachChildItem.getString("id"),
							eachChildItem.getString("catid"));
					itemWithName.put(eachChildItem.getString("id"),
							eachChildItem.getString("title"));
					itemWithIcon.put(eachChildItem.getString("id"),
							eachChildItem.getString("veg_type"));

					TAG_PRODUCT_PRICE = eachChildItem
							.getString("product_price");
					TAG_PRODUCT_ID = eachChildItem.getString("id");

					childrenItems.add(map);
				}
				adapter.notifyDataSetChanged();
				adapter.notifyDataSetInvalidated();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.linearCart: 
			if (Integer.parseInt(itemQuntity.getText().toString()) > 0) {
			relative_cart.setVisibility(View.VISIBLE);
			order_listview.setVisibility(View.VISIBLE);
			view_id1.setVisibility(View.GONE);

			setMyOrder();
			}
			break;
		case R.id.relative_cart:
			if (flag == 1) {
				flag = 2;
				order_listview.setVisibility(View.VISIBLE);
				view_id1.setVisibility(View.GONE);
				iv_menubar.setImageResource(R.drawable.up_accessory);
			} else {
				order_listview.setVisibility(View.GONE);
				view_id1.setVisibility(View.VISIBLE);
				flag = 1;
				iv_menubar.setImageResource(R.drawable.down_accessory);
			}
			break;

		case R.id.writeReview:

			SetData();
			break;
		case R.id.img_back:
			if (Integer.parseInt(itemQuntity.getText().toString()) > 0) {
				System.out.println("flag " + Localsecrets.flg + TAG_MIN_AMOUNT);
				if (Localsecrets.flg == 1) {
					String s = TAG_MIN_AMOUNT.replace("Rs", "");

					if (s.length() > 0) {
						if (totalprice >= Double.parseDouble(s)) {
							Intent secondIntent2 = new Intent(
									getApplicationContext(), OrderDetails.class);

							Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
									.entrySet().iterator();
							ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

							while (iterator2.hasNext()) {
								Map.Entry<String, String> pairs = iterator2
										.next();
								String itemID = pairs.getKey().trim();
								OrderDetails_1 item = new OrderDetails_1(
										itemID, itemWithName.get(itemID),
										itemWithQuantity.get(itemID),
										itemWithIcon.get(itemID),
										itemWithCatId.get(itemID));
								ordersList2.add(item);
								// it.remove(); // avoids a
								// ConcurrentModificationException
							}

							System.out.println("values are"+itemWithPrice+itemWithQuantity+ordersList2);
							secondIntent2.putExtra("orders", new DataWrapper(
									ordersList2));
							secondIntent2.putExtra("eachidprices",
									itemWithPrice);
							secondIntent2.putExtra("eachidqty",
									itemWithQuantity);
							secondIntent2.putExtra("eachidicon", itemWithIcon);
							secondIntent2.putExtra("eachcatid", itemWithCatId);
							startActivity(secondIntent2);
						}
					}
				} else if (Localsecrets.flg == 2) {

					Intent secondIntent2 = new Intent(getApplicationContext(),
							OrderDetails.class);

					Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
							.entrySet().iterator();
					ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

					while (iterator2.hasNext()) {
						Map.Entry<String, String> pairs = iterator2
								.next();
						String itemID = pairs.getKey().trim();
						OrderDetails_1 item = new OrderDetails_1(itemID,
								itemWithName.get(itemID),
								itemWithQuantity.get(itemID),
								itemWithIcon.get(itemID),
								itemWithCatId.get(itemID));
						ordersList2.add(item);
						// it.remove(); // avoids a
						// ConcurrentModificationException
					}

					secondIntent2.putExtra("orders", new DataWrapper(
							ordersList2));
					secondIntent2.putExtra("eachidprices", itemWithPrice);
					secondIntent2.putExtra("eachidqty", itemWithQuantity);
					secondIntent2.putExtra("eachidicon", itemWithIcon);
					secondIntent2.putExtra("eachcatid", itemWithCatId);
					startActivity(secondIntent2);
				} else {
					Intent secondIntent2 = new Intent(getApplicationContext(),
							OrderDetails.class);

					Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
							.entrySet().iterator();
					ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

					while (iterator2.hasNext()) {
						Map.Entry<String, String> pairs = iterator2
								.next();
						String itemID = pairs.getKey().trim();
						OrderDetails_1 item = new OrderDetails_1(itemID,
								itemWithName.get(itemID),
								itemWithQuantity.get(itemID),
								itemWithIcon.get(itemID),
								itemWithCatId.get(itemID));
						ordersList2.add(item);
						// it.remove(); // avoids a
						// ConcurrentModificationException
					}

					secondIntent2.putExtra("orders", new DataWrapper(
							ordersList2));
					secondIntent2.putExtra("eachidprices", itemWithPrice);
					secondIntent2.putExtra("eachidqty", itemWithQuantity);
					secondIntent2.putExtra("eachidicon", itemWithIcon);
					secondIntent2.putExtra("eachcatid", itemWithCatId);
					startActivity(secondIntent2);
				}

			}

			break;
		case R.id.llReviews:
			OrderFood.this.finish();
			break;

		case R.id.llCall:

			/*try {
				Intent callIntent = new Intent(Intent.ACTION_CALL,
						Uri.parse("tel:" + phone));// restDetails.phone
				startActivity(callIntent);
			} catch (ActivityNotFoundException e) {
			}*/
			try {
			
				if (phone.contains(",")) {
					String s[] = phone.split(",");

					// Intent callIntent = new
					// Intent(Intent.ACTION_CALL,
					// Uri.parse("tel:" + s[0]+""
					// + PhoneNumberUtils.PAUSE + "#"
					// + s[1]));

					Intent callIntent = new Intent(Intent.ACTION_CALL);

					callIntent.setData(Uri.parse("tel:" + s[0] + ""
							+ PhoneNumberUtils.PAUSE + "P" + s[1]+";"));
					startActivity(callIntent);
				} else {

					Intent callIntent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:"
									+ phone)); 
					startActivity(callIntent);
				}

			} catch (ActivityNotFoundException e) {
			}

			break;

		case R.id.rr_rightheader:
			if (Integer.parseInt(itemQuntity.getText().toString()) > 0) {
				System.out.println("flag " + Localsecrets.flg + TAG_MIN_AMOUNT);
				if (Localsecrets.flg == 1) {
					String s = TAG_MIN_AMOUNT.replace("Rs", "");

					if (s.length() > 0) {
						if (totalprice >= Double.parseDouble(s)) {
							Intent secondIntent2 = new Intent(
									getApplicationContext(), OrderDetails.class);

							Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
									.entrySet().iterator();
							ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

							while (iterator2.hasNext()) {
								Map.Entry<String, String> pairs = iterator2
										.next();
								String itemID = pairs.getKey().trim();
								OrderDetails_1 item = new OrderDetails_1(
										itemID, itemWithName.get(itemID),
										itemWithQuantity.get(itemID),
										itemWithIcon.get(itemID),
										itemWithCatId.get(itemID));
								ordersList2.add(item);
								// it.remove(); // avoids a
								// ConcurrentModificationException
							}

							secondIntent2.putExtra("orders", new DataWrapper(
									ordersList2));
							secondIntent2.putExtra("eachidprices",
									itemWithPrice);
							secondIntent2.putExtra("eachidqty",
									itemWithQuantity);
							secondIntent2.putExtra("eachidicon", itemWithIcon);
							secondIntent2.putExtra("eachcatid", itemWithCatId);
							startActivity(secondIntent2);
						}
					}
				} else if (Localsecrets.flg == 2) {

					Intent secondIntent2 = new Intent(getApplicationContext(),
							OrderDetails.class);

					Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
							.entrySet().iterator();
					ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

					while (iterator2.hasNext()) {
						Map.Entry<String, String> pairs = iterator2
								.next();
						String itemID = pairs.getKey().trim();
						OrderDetails_1 item = new OrderDetails_1(itemID,
								itemWithName.get(itemID),
								itemWithQuantity.get(itemID),
								itemWithIcon.get(itemID),
								itemWithCatId.get(itemID));
						ordersList2.add(item);
						// it.remove(); // avoids a
						// ConcurrentModificationException
					}

					secondIntent2.putExtra("orders", new DataWrapper(
							ordersList2));
					secondIntent2.putExtra("eachidprices", itemWithPrice);
					secondIntent2.putExtra("eachidqty", itemWithQuantity);
					secondIntent2.putExtra("eachidicon", itemWithIcon);
					secondIntent2.putExtra("eachcatid", itemWithCatId);
					startActivity(secondIntent2);
				} else {
					Intent secondIntent2 = new Intent(getApplicationContext(),
							OrderDetails.class);

					Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
							.entrySet().iterator();
					ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

					while (iterator2.hasNext()) {
						Map.Entry<String, String> pairs = iterator2
								.next();
						String itemID = pairs.getKey().trim();
						OrderDetails_1 item = new OrderDetails_1(itemID,
								itemWithName.get(itemID),
								itemWithQuantity.get(itemID),
								itemWithIcon.get(itemID),
								itemWithCatId.get(itemID));
						ordersList2.add(item);
						// it.remove(); // avoids a
						// ConcurrentModificationException
					}

					secondIntent2.putExtra("orders", new DataWrapper(
							ordersList2));
					secondIntent2.putExtra("eachidprices", itemWithPrice);
					secondIntent2.putExtra("eachidqty", itemWithQuantity);
					secondIntent2.putExtra("eachidicon", itemWithIcon);
					secondIntent2.putExtra("eachcatid", itemWithCatId);
					startActivity(secondIntent2);
				}

			}

			break;

		case R.id.tv_chartamount:

			if (Integer.parseInt(itemQuntity.getText().toString()) > 0) {
				System.out
						.println("flag1 " + Localsecrets.flg + TAG_MIN_AMOUNT);

				System.out.println("value is" + itemWithQuantity);
				System.out.println("value1 is" + itemWithPrice);
				if (Localsecrets.flg == 1) {
					String s = TAG_MIN_AMOUNT.replace("Rs", "");
					{
						if (totalprice >= Double.parseDouble(s)) {
							Intent secondIntent2 = new Intent(
									getApplicationContext(), OrderDetails.class);

							Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
									.entrySet().iterator();
							ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

							while (iterator2.hasNext()) {
								Map.Entry<String, String> pairs = iterator2
										.next();
								String itemID = pairs.getKey().trim();
								OrderDetails_1 item = new OrderDetails_1(
										itemID, itemWithName.get(itemID),
										itemWithQuantity.get(itemID),
										itemWithIcon.get(itemID),
										itemWithCatId.get(itemID));
								ordersList2.add(item);
								// it.remove(); // avoids a
								// ConcurrentModificationException
							}

							secondIntent2.putExtra("orders", new DataWrapper(
									ordersList2));
							secondIntent2.putExtra("eachidprices",
									itemWithPrice);
							secondIntent2.putExtra("eachidqty",
									itemWithQuantity);
							secondIntent2.putExtra("eachidicon", itemWithIcon);
							secondIntent2.putExtra("eachcatid", itemWithCatId);
							startActivity(secondIntent2);
						}
					}
				} else if (Localsecrets.flg == 2) {

					Intent secondIntent2 = new Intent(getApplicationContext(),
							OrderDetails.class);

					Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
							.entrySet().iterator();
					ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

					while (iterator2.hasNext()) {
						Map.Entry<String, String> pairs = iterator2
								.next();
						String itemID = pairs.getKey().trim();
						OrderDetails_1 item = new OrderDetails_1(itemID,
								itemWithName.get(itemID),
								itemWithQuantity.get(itemID),
								itemWithIcon.get(itemID),
								itemWithCatId.get(itemID));
						ordersList2.add(item);
						// it.remove(); // avoids a
						// ConcurrentModificationException
					}

					secondIntent2.putExtra("orders", new DataWrapper(
							ordersList2));
					secondIntent2.putExtra("eachidprices", itemWithPrice);
					secondIntent2.putExtra("eachidqty", itemWithQuantity);
					secondIntent2.putExtra("eachidicon", itemWithIcon);
					secondIntent2.putExtra("eachcatid", itemWithCatId);
					startActivity(secondIntent2);
				} else {
					Intent secondIntent2 = new Intent(getApplicationContext(),
							OrderDetails.class);

					Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
							.entrySet().iterator();
					ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

					while (iterator2.hasNext()) {
						Map.Entry<String, String> pairs = iterator2
								.next();
						String itemID = pairs.getKey().trim();
						OrderDetails_1 item = new OrderDetails_1(itemID,
								itemWithName.get(itemID),
								itemWithQuantity.get(itemID),
								itemWithIcon.get(itemID),
								itemWithCatId.get(itemID));
						ordersList2.add(item);
						// it.remove(); // avoids a
						// ConcurrentModificationException
					}

					secondIntent2.putExtra("orders", new DataWrapper(
							ordersList2));
					secondIntent2.putExtra("eachidprices", itemWithPrice);
					secondIntent2.putExtra("eachidqty", itemWithQuantity);
					secondIntent2.putExtra("eachcatid", itemWithCatId);
					secondIntent2.putExtra("eachidicon", itemWithIcon);
					startActivity(secondIntent2);
				}

			}

			break;

		}

	}

	public void setMyOrder() {
		Iterator<Map.Entry<String, String>> iterator21 = itemWithQuantity
				.entrySet().iterator();
		ArrayList<OrderDetails_1> ordersList21 = new ArrayList<OrderDetails_1>();

		while (iterator21.hasNext()) {
			Map.Entry<String, String> pairs = iterator21
					.next();
			String itemID = pairs.getKey().trim();
			OrderDetails_1 item = new OrderDetails_1(itemID,
					itemWithName.get(itemID), itemWithQuantity.get(itemID),
					itemWithIcon.get(itemID), itemWithCatId.get(itemID));
			ordersList21.add(item);

		}
		order_data_sublist = ordersList21;

		ietmWithPrice = itemWithPrice;
		ietmWithQty = itemWithQuantity;
		ietmWithIcon = itemWithIcon;

		updateQtyTextView();
		adapterP = new OrderDetailsAdapter(getApplicationContext(),
				R.layout.order_details_1, order_data_sublist);

		order_listview.setAdapter(adapterP);
	}

	public void SetData() {

		if (Localsecrets.search_details_flag == 1) {
			url1 = Localsecrets.det_fl;
			if (Localsecrets.det_fl.length() > 0) {

				if (ConnectivityReceiver
						.checkInternetConnection(OrderFood.this)) {

					new DeatilsTasks().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(OrderFood.this);
				}

				Localsecrets.search_details_flag = 0;
			} else if (Explore.strVenueId.length() > 0) {
				// url1
				Localsecrets.det_fl = Appconstants.MAIN_HOST
						+ "restaurantDetails/" + Explore.strVenueId;
				if (ConnectivityReceiver
						.checkInternetConnection(OrderFood.this)) {

					new DeatilsTasks().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(OrderFood.this);
				}
				Localsecrets.search_details_flag = 0;
			}
		} else {
			Localsecrets.det_fl = Appconstants.MAIN_HOST + "restaurantDetails/"
					+ Explore.strVenueId;
			if (ConnectivityReceiver.checkInternetConnection(OrderFood.this)) {

				new DeatilsTasks().execute();

			} else {
				ConnectivityReceiver.showCustomDialog(OrderFood.this);
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
			myProgressDialog = new ProgressDialog(OrderFood.this);
			myProgressDialog.setMessage("Loading...");
			myProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
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
					// OrderFood.this.finish();
				}

				myProgressDialog.dismiss();
			} catch (Exception e) {
				myProgressDialog.dismiss();
				Log.e("", "" + e);
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
			try {
				if (view == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = vi.inflate(R.layout.order_details_1, null);
				}

				final TextView t1 = (TextView) view.findViewById(R.id.title);
				final TextView t2 = (TextView) view
						.findViewById(R.id.count_txt);
				final TextView t3 = (TextView) view.findViewById(R.id.total);
				final ImageView iv = (ImageView) view
						.findViewById(R.id.iv_image);
				final OrderDetails_1 temp = order_data_sublist.get(position);
				t1.setText(temp.title.trim());
				t2.setText(ietmWithQty.get(temp.itemId));

				if (temp.image.equalsIgnoreCase("1")) {
					iv.setImageResource(R.drawable.ic_non_veg);
				} else {
					iv.setImageResource(R.drawable.green);
				}

				t3.setText(""
						+ (Double.parseDouble(ietmWithPrice
								.get(temp.itemId)) * Integer
								.parseInt(ietmWithQty.get(temp.itemId))));

				final TextView btnMinus = ((TextView) view
						.findViewById(R.id.minus));
				final TextView btnPlus = ((TextView) view
						.findViewById(R.id.plus));
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
							ietmWithQty.put(arg0.getTag().toString(), newQty
									+ "");
							t3.setText(""
									+ (Double
											.parseDouble(ietmWithPrice.get(arg0
													.getTag().toString())) * Integer
											.parseInt(ietmWithQty.get(arg0
													.getTag().toString()))));
							itemWithQuantity.put(arg0.getTag().toString(),
									newQty + "");
							updateQtyTextView();
							
							// getTotalPrice();

						}
					}  
				}); 
 
				btnMinus.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						String prevStrQty = ietmWithQty.get(arg0.getTag()
								.toString());
						int prevQty = Integer
								.parseInt((prevStrQty == null) ? "0"
										: prevStrQty);

						int newQty = prevQty - 1;
						if (newQty >= 0) {

							t2.setText("" + newQty);
							ietmWithQty.put(arg0.getTag().toString(), newQty
									+ "");
							t3.setText(""
									+ (Double
											.parseDouble(ietmWithPrice.get(arg0
													.getTag().toString())) * Integer
											.parseInt(ietmWithQty.get(arg0
													.getTag().toString()))));
							itemWithQuantity.put(arg0.getTag().toString(),
									newQty + "");
							updateQtyTextView();
							if (newQty == 0) {
								itemWithQuantity.remove(arg0.getTag()
										.toString());
								adapterP.notifyDataSetChanged();
								if (repeat_order == 2) {
									setUpdate();
								
								} else {
									setUpdate1();
								}
							}

							// getTotalPrice();

							/*
							 * int x = Integer.parseInt(OrderDetails.ietmWithQty
							 * .get(temp.itemId)); if (x <= 0) {
							 * 
							 * // OrderDetails.order_data_sublist.remove(temp);
							 * // getBackToPrevious();
							 * adapterP.notifyDataSetChanged();
							 * 
							 * }
							 */
						}
					}
				});
			} catch (Exception e) {

			}
			return view;
		}
	}
}
