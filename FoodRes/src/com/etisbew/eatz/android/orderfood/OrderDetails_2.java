package com.etisbew.eatz.android.orderfood;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eatz.android.BaseActivity;
import com.etisbew.eatz.android.Explore;
import com.etisbew.eatz.android.Localsecrets;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.android.TermsAndConditions;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.common.PurchasedItems;

public class OrderDetails_2 extends Activity {

	ListView list;
	DataDetailsAdapter adapterP;
	HttpResponse response;

	int x = 0;
	double y = 0;
	double z = 0;
	Boolean boolean1_glag = false;
	double totalPrice = 0.00, delivery_charges;
	TextView totalItemsCostt = null, etRestName, tv_checkout2, tv_bottom_click,
			tv_checkout1, tv_terms, tv_billing, tv_address,
			txt_delivery_charges;
	EditText tv_cname_value, tv_cphone_value, tv_email_value, tv_address_value,
			tv_address_value1, edtLandmark;
	ImageView back = null;
	String strEmail, strPhone, strAddress, strFName;
	RelativeLayout rr_delivery_charges;
	RadioButton rb_payment, rb_paynow;
	RadioGroup radiopay;
	int selectedValueId;
	static String selectWay;
	String strLandmark;
	public static String ITEM_QNTY;
	LinearLayout lyLandmark, Lay5;

	static String firstname = "";
	static String phone = "";
	static String email = "";
	static String eatznumber = "";
	static String total = "";
	static String points = "";
	static int userstatus;
	static String eatz_email = "support@eatz.com";
	String total_ = "0";
	DecimalFormat twoDForm;
	String strtime = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.orderdetails_print);
		if (OrderDetails.flag_clicked == 2) {
			OrderDetails.flag_clicked = 1;
		}
		twoDForm = new DecimalFormat("#.##");
		etRestName = (TextView) findViewById(R.id.etRestName);
		etRestName.setText(Explore.strRestaurantTitle);

		totalItemsCostt = (TextView) findViewById(R.id.txtOrderTotal1);
		tv_checkout2 = (TextView) findViewById(R.id.tv_checkout2);
		Intent secondIntent = getIntent();

		tv_checkout1 = (TextView) findViewById(R.id.tv_checkout1);
		tv_billing = (TextView) findViewById(R.id.tv_billing);
		tv_address = (TextView) findViewById(R.id.tv_address);

		totalItemsCostt.setText(secondIntent.getStringExtra("name"));
		strtime = secondIntent.getStringExtra("Strdatetime");

		rr_delivery_charges = (RelativeLayout) findViewById(R.id.rr_delivery_charges);

		total_ = secondIntent.getStringExtra("name");

		Appconstants.Strdatetime = secondIntent.getStringExtra("Strdatetime");
		Appconstants.orders = secondIntent.getStringExtra("orders");
		Appconstants.eachidprices = secondIntent.getStringExtra("eachidprices");
		Appconstants.eachidqty = secondIntent.getStringExtra("eachidqty");
		Appconstants.name = secondIntent.getStringExtra("name");

		tv_checkout1.setText(OrderDetails.ordertype);
		tv_checkout2.setText(OrderDetails.Strdatetime);

		back = (ImageView) findViewById(R.id.back);
		list = (ListView) findViewById(R.id.listview_row_value);

		tv_cname_value = (EditText) findViewById(R.id.tv_cname_value);

		LinearLayout lyLandmark, Lay5, Lay4;

		Lay5 = (LinearLayout) findViewById(R.id.Lay5);
		Lay4 = (LinearLayout) findViewById(R.id.Lay4);
		if (OrderFood.paytm_flag == 2) {
			if (Integer.parseInt(OrderDetails.StrIs_pick) == 0) {
				// || Integer.parseInt(OrderDetails.StrIs_pick) == 1
				// || Integer.parseInt(OrderDetails.StrIs_pick) == 2) {
				Lay4.setVisibility(View.VISIBLE);
			} else {
				Lay4.setVisibility(View.GONE);
			}

		} else if (OrderFood.paytm_flag != 2) {

			if (Integer.parseInt(OrderDetails.StrIs_pick) == 0) {
				Lay4.setVisibility(View.VISIBLE);
			} else {
				Lay4.setVisibility(View.GONE);
			}
		}
		if (Integer.parseInt(OrderDetails.StrIs_pick) == 0) {

			Lay5.setVisibility(View.VISIBLE);

		} else if (Integer.parseInt(OrderDetails.StrIs_pick) != 0) {
			tv_address.setText("Address :");
			Lay5.setVisibility(View.GONE);
		}
		if (!PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
			String StrName = PreferenceUtils.getUserName1();
			tv_cname_value.setText(StrName);
		} else if (Appconstants.user_flag == 2) {
			String StrName = Appconstants.firstname;
			tv_cname_value.setText(StrName);
		}

		tv_cphone_value = (EditText) findViewById(R.id.tv_cphone_value);
		if (!PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
			if (!PreferenceUtils.getUserPhone1().equalsIgnoreCase("none")) {
				String Strphone = PreferenceUtils.getUserPhone1();
				tv_cphone_value.setText(Strphone);
			}
		} else if (Appconstants.user_flag == 2) {
			String Strphone = Appconstants.phone;
			tv_cphone_value.setText(Strphone);
		}
		tv_cphone_value.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// final int DRAWABLE_LEFT = 0;
				// final int DRAWABLE_TOP = 1;
				final int DRAWABLE_RIGHT = 2;
				// final int DRAWABLE_BOTTOM = 3;

				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (event.getX() >= (tv_cphone_value.getRight() - tv_cphone_value
							.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
							.width())) {
						tv_cphone_value.setText("");
					}
				}
				return false;
			}
		});

		tv_email_value = (EditText) findViewById(R.id.tv_email_value);

		if (!PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
			String StrEmail = PreferenceUtils.getUserEmail1();
			tv_email_value.setText(StrEmail);
		} else if (Appconstants.user_flag == 2) {
			String StrEmail = Appconstants.email_;
			tv_email_value.setText(StrEmail);
		}

		tv_address_value = (EditText) findViewById(R.id.tv_address_value);
		tv_address_value.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// final int DRAWABLE_LEFT = 0;
				// final int DRAWABLE_TOP = 1;
				final int DRAWABLE_RIGHT = 2;
				// final int DRAWABLE_BOTTOM = 3;

				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (event.getX() >= (tv_address_value.getRight() - tv_address_value
							.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
							.width())) {
						tv_address_value.setText("");
					}
				}
				return false;
			}
		});
		txt_delivery_charges = (TextView) findViewById(R.id.delivery_chargesItemsCost);
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
		/*
		 * if (!PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
		 * String StrAddress =
		 * PreferenceUtils.getUserAddress1().replace("null","");
		 * tv_address_value.setText(Html.fromHtml(StrAddress)); }else
		 * if(Appconstants.user_flag==2){
		 * 
		 * tv_address_value.setText(Html.fromHtml(StrAddress)); }
		 */
		tv_address_value.setText(Appconstants.address);
		tv_address_value1 = (EditText) findViewById(R.id.tv_address_value1);

		tv_address_value1.setText(Appconstants.ltDo.name + ","
				+ Appconstants.strCityName);

		tv_terms = (TextView) findViewById(R.id.tv_terms);
		String StrCheckBox = "By placing this order you are also agreeing to accept the <font color='#1979bd'> Terms and Conditions</font> associated with it.";
		tv_terms.setText(Html.fromHtml(StrCheckBox));
		tv_terms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(getApplicationContext(),
						TermsAndConditions.class));
			}
		});

		radiopay = (RadioGroup) findViewById(R.id.radiopay);
		rb_payment = (RadioButton) findViewById(R.id.rb_payment);
		rb_paynow = (RadioButton) findViewById(R.id.rb_paynow);
		CheckBox chkIs = (CheckBox) findViewById(R.id.tv_checkbox_bottom1);

		if (OrderFood.gateway == 0 && OrderFood.ordertype == 1
				|| OrderFood.gateway == 0 && OrderFood.ordertype == 0) {
			rb_paynow.setVisibility(View.GONE);
			rb_payment.setVisibility(View.VISIBLE);
			rb_payment.setChecked(true);

		} else if (OrderFood.gateway == 1) {
			rb_payment.setVisibility(View.VISIBLE);
			rb_paynow.setVisibility(View.VISIBLE);
		} else if (OrderFood.gateway == 2) {

			rb_paynow.setVisibility(View.VISIBLE);
			rb_paynow.setChecked(true);
			rb_payment.setVisibility(View.GONE);
		}
		if ((OrderFood.gateway == 0 && OrderFood.ordertype == 1)
				|| (OrderFood.gateway == 0 && OrderFood.ordertype == 0)) {
			rb_paynow.setVisibility(View.GONE);
			rb_payment.setVisibility(View.VISIBLE);
			rb_payment.setChecked(true);

		} else if ((OrderFood.gateway == 1 && OrderFood.ordertype == 1)
				|| (OrderFood.gateway == 1 && OrderFood.ordertype == 0)) {
			rb_payment.setVisibility(View.VISIBLE);
			rb_paynow.setVisibility(View.VISIBLE);
		} else if (OrderFood.gateway == 2) {

			rb_paynow.setVisibility(View.VISIBLE);
			rb_paynow.setChecked(true);
			rb_payment.setVisibility(View.GONE);
		}

		/*
		 * if (OrderFood.gateway == 0) { rb_paynow.setVisibility(View.GONE);
		 * rb_payment.setVisibility(View.VISIBLE); rb_payment.setChecked(true);
		 * 
		 * } else if(OrderFood.gateway == 1){
		 * rb_payment.setVisibility(View.VISIBLE);
		 * rb_paynow.setVisibility(View.VISIBLE); }else if (OrderFood.gateway ==
		 * 2) {
		 * 
		 * rb_paynow.setVisibility(View.VISIBLE); rb_paynow.setChecked(true);
		 * rb_payment.setVisibility(View.GONE); }
		 */
		chkIs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// is chkIos checked?
				if (((CheckBox) v).isChecked()) {
					boolean1_glag = true;
				} else {
					boolean1_glag = false;
				}

			}
		});

		tv_bottom_click = (TextView) findViewById(R.id.tv_bottom_click);
		tv_bottom_click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedValueId = radiopay.getCheckedRadioButtonId();
				if (selectedValueId == rb_payment.getId()) {
					selectWay = "1";
				} else {
					selectWay = "2";
				}

				if (boolean1_glag) {
					if (tv_email_value.getText().toString().length() > 0
							&& tv_cphone_value.getText().toString().length() > 0
							&& tv_email_value.getText().toString().length() > 0) {
						if (tv_cphone_value.getText().toString().length() > 0) {
							if (tv_cphone_value.getText().toString().length() == 10) {
								if (!PreferenceUtils.getUserSession()
										.equalsIgnoreCase("none")) {
									if (TextUtils.isEmpty(PreferenceUtils
											.getUserPhone1())) {
										PreferenceUtils
												.setUserPhone1(tv_cphone_value
														.getText().toString());
									}
								}
								if (eMailValidation(tv_email_value.getText()
										.toString())) {
									if (OrderFood.paytm_flag == 2) {
										/*
										 * if
										 * (tv_address_value.getText().toString
										 * ().length() > 0) {
										 * 
										 * if
										 * (tv_address_value.getText().toString
										 * ().equalsIgnoreCase("none")) {
										 * Login.showDialogMsg
										 * (OrderDetails_2.this
										 * ,"Please enter address"); } else {
										 */
										if (Integer
												.parseInt(OrderDetails.StrIs_pick) == 0) {
											if (!TextUtils.isEmpty(edtLandmark
													.getText().toString())) {
												strLandmark = edtLandmark
														.getText().toString();

												if (!TextUtils
														.isEmpty(tv_address_value
																.getText()
																.toString())) {

													if (ConnectivityReceiver
															.checkInternetConnection(OrderDetails_2.this)) {

														new PostingData()
																.execute();

													} else {
														ConnectivityReceiver
																.showCustomDialog(OrderDetails_2.this);
													}
												} else {
													BaseActivity
															.showDialogMsg(
																	OrderDetails_2.this,
																	"Please enter address1");
												}
											} else {
												BaseActivity
														.showDialogMsg(
																OrderDetails_2.this,
																"Please enter landmark");
											}
										} /*
										 * else if (Integer
										 * .parseInt(OrderDetails.StrIs_pick) ==
										 * 1 || Integer
										 * .parseInt(OrderDetails.StrIs_pick) ==
										 * 2) { if (!TextUtils
										 * .isEmpty(tv_address_value .getText()
										 * .toString())) {
										 * 
										 * if (ConnectivityReceiver
										 * .checkInternetConnection
										 * (OrderDetails_2.this)) {
										 * 
										 * new PostingData().execute();
										 * 
										 * } else { ConnectivityReceiver
										 * .showCustomDialog
										 * (OrderDetails_2.this); } } else {
										 * Login.showDialogMsg(
										 * OrderDetails_2.this,
										 * "Please enter address"); } }
										 */else {

											strLandmark = null;
											// new PostingData().execute();
											if (ConnectivityReceiver
													.checkInternetConnection(OrderDetails_2.this)) {

												new PostingData().execute();

											} else {
												ConnectivityReceiver
														.showCustomDialog(OrderDetails_2.this);
											}
										}
										/*
										 * }
										 * 
										 * } else { Login.showDialogMsg(
										 * OrderDetails_2.this,
										 * "Please enter address"); }
										 */
									} else {
										if (Integer
												.parseInt(OrderDetails.StrIs_pick) == 0) {
											if (!TextUtils.isEmpty(edtLandmark
													.getText().toString())) {
												strLandmark = edtLandmark
														.getText().toString();

												if (!TextUtils
														.isEmpty(tv_address_value
																.getText()
																.toString())) {

													if (ConnectivityReceiver
															.checkInternetConnection(OrderDetails_2.this)) {

														new PostingData()
																.execute();

													} else {
														ConnectivityReceiver
																.showCustomDialog(OrderDetails_2.this);
													}
												} else {
													BaseActivity
															.showDialogMsg(
																	OrderDetails_2.this,
																	"Please enter address1");
												}
											} else {
												BaseActivity
														.showDialogMsg(
																OrderDetails_2.this,
																"Please enter landmark");
											}
										} else {
											strLandmark = null;
											// new PostingData().execute();
											if (ConnectivityReceiver
													.checkInternetConnection(OrderDetails_2.this)) {

												new PostingData().execute();

											} else {
												ConnectivityReceiver
														.showCustomDialog(OrderDetails_2.this);
											}
										}
									}
								} else {
									BaseActivity.showDialogMsg(
											OrderDetails_2.this,
											"Please enter valid email address1");
								}
							} else {
								BaseActivity.showDialogMsg(OrderDetails_2.this,
										"Please enter valid phone number");
							}

						} else {
							BaseActivity.showDialogMsg(OrderDetails_2.this,
									"Please enter phone number");
						}
					} else {
						BaseActivity.showDialogMsg(OrderDetails_2.this,
								"Please enter all fields");
					}

				} else {
					AlertDialog.Builder alert = new AlertDialog.Builder(
							OrderDetails_2.this);
					alert.setTitle("eatz");
					// Create TextView
					final TextView txtMsg = new TextView(OrderDetails_2.this);
					txtMsg.setTextColor(Color.WHITE);
					txtMsg.setPadding(10, 0, 0, 0);
					txtMsg.setTypeface(null, Typeface.BOLD);
					txtMsg.setText("Please check terms and conditions");
					alert.setView(txtMsg);

					alert.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.cancel();
								}
							});

					alert.show();
				}
			}
		});

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		// try {
		// ietmWithPrice = (HashMap<String, String>) getIntent()
		// .getSerializableExtra("eachidprices");
		// ietmWithQty = (HashMap<String, String>) getIntent()
		// .getSerializableExtra("eachidqty");
		//
		// DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra(
		// "orders");
		// order_data_sublist = dw.getOrders();
		//
		// } catch (Exception e) {
		// Log.e("", "" + e);
		// }
		adapterP = new DataDetailsAdapter(getApplicationContext(),
				R.layout.orderdetails_row, OrderDetails.order_data_sublist);

		list.setAdapter(adapterP);

		lyLandmark = (LinearLayout) findViewById(R.id.lyLandmark);
		if (Integer.parseInt(OrderDetails.StrIs_pick) == 0) {
			tv_billing.setText("Delivery Address");
			lyLandmark.setVisibility(View.VISIBLE);
		} else {
			tv_billing.setText("Billing Address");
			lyLandmark.setVisibility(View.GONE);
		}
		edtLandmark = (EditText) findViewById(R.id.edtLandmark);
	}

	private class PostingData extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;
		String Strcity = "Hyderabad";
		String StrState = "TG";
		String Strpostal_code = null;
		String is_mobile = "1";
		String order_status = "1";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(OrderDetails_2.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance

			try {

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(Appconstants.MAIN_HOST
						+ "addMultipleProducts/");

				PreferenceUtils.setUserName1(tv_cname_value.getText()
						.toString());
				PreferenceUtils.setUserPhone1(tv_cphone_value.getText()
						.toString());
				PreferenceUtils.setUserEmail1(tv_email_value.getText()
						.toString());

				List<NameValuePair> params1 = new ArrayList<NameValuePair>();
				if (Appconstants.user_flag == 2) {
					params1.add(new BasicNameValuePair("user_id",
							Appconstants.userId));
				} else {
					params1.add(new BasicNameValuePair("user_id",
							PreferenceUtils.getUserId()));
				}
				params1.add(new BasicNameValuePair("payment", selectWay));
				params1.add(new BasicNameValuePair("venue_id",
						Explore.strVenueId));
				total_ = total_.replace(getString(R.string.oneRupee), "")
						.trim();
				double total_amount = Double.parseDouble(total_);

				params1.add(new BasicNameValuePair("total_price", ""
						+ total_amount));
				if (TextUtils.isEmpty(OrderDetails.if_any)) {
					params1.add(new BasicNameValuePair("splinstructions", ""));
				} else {
					params1.add(new BasicNameValuePair("splinstructions",
							OrderDetails.if_any));
				}

				params1.add(new BasicNameValuePair("delivery_charges",
						OrderFood.TAG_DELIVERY_CHARGES));
				String address1 = tv_address_value.getText().toString();
				String address2 = tv_address_value1.getText().toString();
				Appconstants.address = address1;

				if (Localsecrets.flg == 1) {
					if (TextUtils.isEmpty(address2)) {
						address1 = address1 + "," + Appconstants.ltDo.name;
					} else {
						address1 = address1 + "," + address2;
					}
					if (TextUtils.isEmpty(address1)) {
						address1 = Appconstants.ltDo.name;
					}

				} else {
					if (TextUtils.isEmpty(address1)) {
						address1 = Appconstants.ltDo.name + ","
								+ Appconstants.strCityName;
					}
				}

				params1.add(new BasicNameValuePair("address", address1));

				if (Appconstants.user_flag == 2) {
					params1.add(new BasicNameValuePair("phone_number",
							Appconstants.phone));
				} else {

					params1.add(new BasicNameValuePair("phone_number",
							PreferenceUtils.getUserPhone1()));
				}
				if (Appconstants.user_flag == 2) {
					params1.add(new BasicNameValuePair("sessionid",
							Appconstants.sessionId));
				} else {
					params1.add(new BasicNameValuePair("sessionid",
							PreferenceUtils.getUserSession()));
				}
				params1.add(new BasicNameValuePair("is_mobile", is_mobile));// Need
																			// to
																			// Check
				params1.add(new BasicNameValuePair("venuedeliverylocation_id",
						Appconstants.ltDo.id));
				params1.add(new BasicNameValuePair("order_vat",
						OrderFood.TAG_VATCHARGES));
				params1.add(new BasicNameValuePair("order_stax",
						OrderFood.TAG_SERVICE_CHARGE));

				params1.add(new BasicNameValuePair("state", StrState));// Need
																		// to
				if (TextUtils.isEmpty(Strpostal_code)) {
					params1.add(new BasicNameValuePair("postal_code", ""));// Need
																			// to
																			// Change
																			// backend
				} else {
					params1.add(new BasicNameValuePair("postal_code",
							Strpostal_code));// Need to Change backend
				}

				params1.add(new BasicNameValuePair("city", Strcity));// Need to
																		// Change
																		// backend

				params1.add(new BasicNameValuePair("is_pick",
						OrderDetails.StrIs_pick));
				if (TextUtils.isEmpty(strLandmark)) {
					params1.add(new BasicNameValuePair("landmark", ""));
				} else {
					params1.add(new BasicNameValuePair("landmark", strLandmark));
				}

				String s = OrderDetails.Strdatetime.replace("(", "@");
				String strDate1[] = s.split("@");
				String strDate = strDate1[1].replace(")", "");

				strDate = strDate.replace("/", "-");
				JSONArray productArray = new JSONArray();
				JSONObject jsonObject;

				DateFormat srcDf = new SimpleDateFormat("dd-mm-yyyy HH:MM");

				Date date1 = srcDf.parse(strDate);
				DateFormat destDf = new SimpleDateFormat("yyyy-mm-dd HH:MM");

				String strDate2 = destDf.format(date1);

				params1.add(new BasicNameValuePair("date_order", ""));
				params1.add(new BasicNameValuePair("date_delivery", strDate2));
				params1.add(new BasicNameValuePair("order_status", order_status));

				for (int i = 0; i < OrderDetails.order_data_sublist.size(); i++) {
					jsonObject = new JSONObject();

					OrderDetails_1 temp = OrderDetails.order_data_sublist
							.get(i);

					jsonObject.put("venuemenucategory_id",
							OrderDetails.ietmWithCatid.get(temp.itemId));
					jsonObject.put("product_qty",// OrderDetails.order_data_sublist.get(i).itemId
							OrderDetails.ietmWithQty.get(temp.itemId));
					jsonObject.put("product_added", strDate2);
					jsonObject.put("venuemenuproduct_id", temp.itemId);
					jsonObject.put("product_price",
							OrderDetails.ietmWithPrice.get(temp.itemId));
					productArray.put(jsonObject);
				}

				params1.add(new BasicNameValuePair("productdetails",
						productArray.toString()));

				post.setEntity(new UrlEncodedFormEntity(params1));
				response = client.execute(post);

			} catch (Exception e) {
				Log.e("exception in doinbackground", "" + e.getMessage());
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			try {
				if (pDialog.isShowing())
					pDialog.dismiss();

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					try {
						String re = EntityUtils.toString(entity, HTTP.UTF_8);
						JSONObject jObj = new JSONObject(re.trim());
						Appconstants.strPurchaseId = jObj.optString("orderid");
						JSONObject jObj1 = jObj.getJSONObject("aftersuccess");

						if (jObj1.has("firstname")) {
							firstname = jObj1.getString("firstname");
						} else {
							firstname = "";
						}
						if (jObj1.has("phone")) {
							phone = jObj1.getString("phone");
						} else {
							phone = "";
						}
						if (jObj1.has("email")) {
							email = jObj1.getString("email");
						} else {
							email = "";
						}
						if (jObj1.has("eatznumber")) {
							eatznumber = jObj1.getString("eatznumber");
						} else {
							eatznumber = "";
						}
						if (jObj1.has("total")) {
							total = jObj1.getString("total");
							Appconstants.strFinalAmount = total;
						} else {
							total = "";
						}
						if (jObj1.has("points")) {
							points = jObj1.getString("points");
						} else {
							points = "";
						}
						if (jObj1.has("userstatus")) {
							userstatus = Integer.parseInt(jObj1
									.getString("userstatus"));
						}

						String ss1 = "";
						String ss2 = "";

						try {
							if (PreferenceUtils.getOrderSession()
									.equals("none")) {
								ss1 = Appconstants.strPurchaseId + ",";
								ss2 = Localsecrets.flg + ",";
								PreferenceUtils.setOrderSession(ss1);
								PreferenceUtils.setOrderTypes(ss2);
							} else {
								String[] ss = PreferenceUtils.getOrderSession()
										.split(",");
								String[] ss_ = PreferenceUtils.getOrderTypes()
										.split(",");

								if (ss.length == 3) {
									ss1 = Appconstants.strPurchaseId + ","
											+ ss[0] + "," + ss[1];
									ss2 = Localsecrets.flg + "," + ss_[0] + ","
											+ ss_[1];
								} else if (ss.length == 2) {
									ss1 = Appconstants.strPurchaseId + ","
											+ ss[0] + "," + ss[1];
									ss2 = Localsecrets.flg + "," + ss_[0] + ","
											+ ss_[1];
								} else if (ss.length == 1) {
									ss1 = Appconstants.strPurchaseId + ","
											+ ss[0];
									ss2 = Localsecrets.flg + "," + ss_[0];
								} else {
									ss1 = Appconstants.strPurchaseId + ",";
									ss2 = Localsecrets.flg + ",";
								}
								PreferenceUtils.setOrderSession(ss1);
								PreferenceUtils.setOrderTypes(ss2);

							}
						} catch (Exception e) {
							System.out.println("exception" + e.getMessage());

							ss1 = Appconstants.strPurchaseId + ",";
							ss2 = Localsecrets.flg + ",";
							PreferenceUtils.setOrderSession(ss1);
							PreferenceUtils.setOrderTypes(ss2);
						}
						System.out.println("final"
								+ PreferenceUtils.getOrderSession()
								+ PreferenceUtils.getOrderTypes());
						Appconstants.strPurchaseSubUrl = "torders/mobileapp/";
						if (selectWay.equalsIgnoreCase("1")) {
							Appconstants.strWebviewUrl = Appconstants.PURCHASED_ITEMS_HOST
									+ "/files/order_invoice_"
									+ Appconstants.strPurchaseId + ".pdf";

							startActivity(new Intent(getApplicationContext(),
									OrderConfirm.class));
						} else {
							Appconstants.strWebviewUrl = Appconstants.PURCHASED_ITEMS_HOST
									+ "/files/order_invoice_"
									+ Appconstants.strPurchaseId + ".pdf";
							/*
							 * startActivity(new Intent(getApplicationContext(),
							 * PurchasedItems.class));
							 */
							if (OrderFood.paytm_flag == 2) {
								startActivity(new Intent(
										getApplicationContext(),
										PurchasedItems.class));
							} else {
								startActivity(new Intent(
										getApplicationContext(), PayTm.class));
							}

						}

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}

	private class DataDetailsAdapter extends ArrayAdapter<OrderDetails_1> {

		private ArrayList<OrderDetails_1> dataValues;

		public DataDetailsAdapter(Context context, int textViewResourceId,
				ArrayList<OrderDetails_1> items) {
			super(context, textViewResourceId, items);
			this.dataValues = items;
		}

		@Override
		public int getCount() {
			return OrderDetails.order_data_sublist.size();
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
				view = vi.inflate(R.layout.orderdetails_row, null);
			}
			TextView ItemName = (TextView) view
					.findViewById(R.id.txtOrderItem111);
			final TextView ItemQuantity = (TextView) view
					.findViewById(R.id.txtOrderQty111);
			final TextView ItmunicPrice = (TextView) view
					.findViewById(R.id.txtOrderPrice111);
			final TextView ItemtotPrice = (TextView) view
					.findViewById(R.id.txtOrderTotal111);
			OrderDetails_1 temp = OrderDetails.order_data_sublist.get(position);
			ItemName.setText(temp.title.trim());
			ItemQuantity.setText(OrderDetails.ietmWithQty.get(temp.itemId));
			ITEM_QNTY = OrderDetails.ietmWithQty.get(temp.itemId);
			x = Integer.parseInt(OrderDetails.ietmWithQty.get(temp.itemId));
			y = Double.parseDouble(OrderDetails.ietmWithPrice.get(temp.itemId));
			ItmunicPrice.setText(OrderDetails.ietmWithPrice.get(temp.itemId));
			z = x * y;
			ItemtotPrice.setText(Double.toString(z));
			if (x <= 0) {
				OrderDetails.order_data_sublist.remove(temp);
				adapterP.notifyDataSetChanged();
			}

			return view;
		}
	}

	public static boolean eMailValidation(String emailstring) {
		Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		return emailMatcher.matches();
	}
}
