package com.etisbew.eatz.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.etisbew.eatz.android.orderfood.OrderConfirm;
import com.etisbew.eatz.android.orderfood.PayTm;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.common.PurchasedItems;
import com.etisbew.eatz.web.WebServiceCalls;

public class GetVoucher extends Activity {

	ListView deals_listview;
	EditText tv_firstname, tv_lastname, tv_email, tv_phone;
	TextView tv_totalprice, tv_terms, tv_bottom_click, txt_count,
			tv_address_value;
	ImageView back;
	String[] str = { "" + Deals.jobjDeals.optString("title") };
	Boolean boolean1_glag = false;
	String payment = "1";
	Dialog d;
	LinearLayout Lay5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.get_voucher);

		tv_firstname = (EditText) findViewById(R.id.tv_cname_value);
		tv_address_value = (EditText) findViewById(R.id.tv_address_value);
		Lay5 = (LinearLayout) findViewById(R.id.Lay5);

		if (!PreferenceUtils.getUserId().equalsIgnoreCase("none")) {
			if (!PreferenceUtils.getUserFirstName().equalsIgnoreCase("none")) {
				tv_firstname.setText(PreferenceUtils.getUserFirstName());
			}
		} else {
			tv_firstname.setText(Appconstants.firstname);
		}

		tv_lastname = (EditText) findViewById(R.id.tv_lastname_value);

		if (!PreferenceUtils.getUserId().equalsIgnoreCase("none")) {
			if (!PreferenceUtils.getUserLastName().equalsIgnoreCase("none")) {
				tv_lastname.setText(PreferenceUtils.getUserLastName());
			}
		} else {
			tv_lastname.setText(Appconstants.userName);
		}

		tv_email = (EditText) findViewById(R.id.tv_email_value);

		if (!PreferenceUtils.getUserEmail1().equalsIgnoreCase("none")) {
			tv_email.setText(PreferenceUtils.getUserEmail1());
		}

		tv_phone = (EditText) findViewById(R.id.tv_phone_value);

		if (!PreferenceUtils.getUserPhone1().equalsIgnoreCase("none")) {
			tv_phone.setText(PreferenceUtils.getUserPhone1());
		}
		//DealsDetails.paytm_flag=1;
		System.out.println("payment"+Deals.pay);
		if (Deals.pay.equalsIgnoreCase("0")) {
			if(DealsDetails.paytm_flag==2){
			if (!PreferenceUtils.getUserId().equalsIgnoreCase("none")) {
				Lay5.setVisibility(View.GONE);
			} else {
				Lay5.setVisibility(View.GONE);
			}
			}else {
				Lay5.setVisibility(View.GONE);
			}
		} else {
			if(DealsDetails.paytm_flag==2){
				if (!PreferenceUtils.getUserId().equalsIgnoreCase("none")) {
					Lay5.setVisibility(View.VISIBLE);
				} else {
					Lay5.setVisibility(View.VISIBLE);
				}
				}else {
					Lay5.setVisibility(View.GONE);
				}
		}

		tv_totalprice = (TextView) findViewById(R.id.txtDealsTotal1);

		CheckBox chkIs = (CheckBox) findViewById(R.id.tv_checkbox_bottom);
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

		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				GetVoucher.this.finish();
			}
		});

		tv_bottom_click = (TextView) findViewById(R.id.tv_bottom_click);
		tv_bottom_click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (boolean1_glag) {
					if (TextUtils.isEmpty(tv_email.getText().toString())) {
						BaseActivity.showDialogMsg(GetVoucher.this,
								"Please enter email");
					} else if (TextUtils.isEmpty(tv_phone.getText().toString())) {
						BaseActivity.showDialogMsg(GetVoucher.this,
								"Please enter phone number");
					} else if (TextUtils.isEmpty(tv_firstname.getText()
							.toString())) {
						BaseActivity.showDialogMsg(GetVoucher.this,
								"Please enter first name");
					} else if (TextUtils.isEmpty(tv_lastname.getText()
							.toString())) {
						BaseActivity.showDialogMsg(GetVoucher.this,
								"Please enter last name");
					} else {

						Boolean bool = BaseActivity.eMailValidation(tv_email
								.getText().toString().trim());

						if (bool.equals(true)) { 
							if (tv_phone.getText().toString().length() == 10) {
								if (!PreferenceUtils.getUserId()
										.equalsIgnoreCase("none")) {
									 //Lay5.setVisibility(View.GONE);
									if (Deals.pay.equalsIgnoreCase("0")) {
										if (ConnectivityReceiver
												.checkInternetConnection(GetVoucher.this)) {
											new PurchaseItemTask().execute();
										} else {
											ConnectivityReceiver.showCustomDialog(GetVoucher.this);
										}
									} else {

										if(DealsDetails.paytm_flag ==2){
											if (!TextUtils.isEmpty(tv_address_value.getText().toString())) {

												if (ConnectivityReceiver.checkInternetConnection(GetVoucher.this)) {
													new PurchaseItemTask().execute();
												} else {
													ConnectivityReceiver.showCustomDialog(GetVoucher.this);
												}
											} else {
												BaseActivity.showDialogMsg(GetVoucher.this,"Please enter Address");

											}
										}else{
											if (ConnectivityReceiver.checkInternetConnection(GetVoucher.this)) {
												new PurchaseItemTask().execute();
											} else {
												ConnectivityReceiver.showCustomDialog(GetVoucher.this);
											}
										}
									}
								} else { 

									if (Deals.pay.equalsIgnoreCase("0")) {
										if (ConnectivityReceiver
												.checkInternetConnection(GetVoucher.this)) {
											new PurchaseItemTask().execute();
										} else {
											ConnectivityReceiver.showCustomDialog(GetVoucher.this);
										}
									} else {

										if(DealsDetails.paytm_flag ==2){
											if (!TextUtils.isEmpty(tv_address_value.getText().toString())) {

												if (ConnectivityReceiver.checkInternetConnection(GetVoucher.this)) {
													new PurchaseItemTask().execute();
												} else {
													ConnectivityReceiver.showCustomDialog(GetVoucher.this);
												}
											} else {
												BaseActivity.showDialogMsg(GetVoucher.this,"Please enter Address");

											}
										}else{
											if (ConnectivityReceiver.checkInternetConnection(GetVoucher.this)) {
												new PurchaseItemTask().execute();
											} else {
												ConnectivityReceiver.showCustomDialog(GetVoucher.this);
											}
										}
										
										/*if (!TextUtils.isEmpty(tv_address_value
												.getText().toString())) {

											if (ConnectivityReceiver
													.checkInternetConnection(GetVoucher.this)) {
												new PurchaseItemTask()
														.execute();
											} else {
												ConnectivityReceiver.showCustomDialog(GetVoucher.this);
											}
										} else {
											Login.showDialogMsg(
													GetVoucher.this,
													"Please enter Address");

										}*/
									}
								} 
							} else {
								BaseActivity.showDialogMsg(GetVoucher.this,
										"Please enter valid phone number");
							}
						} else {
							BaseActivity.showDialogMsg(v.getContext(),
									"Please enter valid email address");
						}
					}
				}

				else {
					AlertDialog.Builder alert = new AlertDialog.Builder(
							GetVoucher.this);
					alert.setTitle("eatz");
					// Create TextView
					final TextView txtMsg = new TextView(GetVoucher.this);
					txtMsg.setTextColor(Color.WHITE);
					txtMsg.setPadding(10, 0, 0, 0);
					txtMsg.setTypeface(null, Typeface.BOLD);
					txtMsg.setText("Please accept terms and conditions");
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

		deals_listview = (ListView) findViewById(R.id.listview_row_value);
		deals_listview.setAdapter(new DealsDetailsAdapter(str));
	}

	class DealsDetailsAdapter extends BaseAdapter {

		String[] Title;
		TextView title, txt_minus, txt_plus, txt_each_item, txt_total;

		DealsDetailsAdapter() {
			Title = null;
		}

		public DealsDetailsAdapter(String[] text) {
			// TODO Auto-generated constructor stub
			Title = text;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Title.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row;
			row = inflater.inflate(R.layout.get_voucher_row, parent, false);

			title = (TextView) row.findViewById(R.id.title);
			txt_count = (TextView) row.findViewById(R.id.count_txt);
			txt_plus = (TextView) row.findViewById(R.id.plus);
			txt_minus = (TextView) row.findViewById(R.id.minus);
			txt_each_item = (TextView) row.findViewById(R.id.eachitemprice);
			txt_total = (TextView) row.findViewById(R.id.totalprice);

			title.setText(Title[position]);
			txt_each_item.setText(Deals.pay);
			txt_count.setText(DealsDetails.item);
			txt_total.setText(String.valueOf(Integer
					.parseInt(DealsDetails.item)
					* Integer.parseInt(txt_each_item.getText().toString()
							.trim())));
			tv_totalprice.setText(String.valueOf(Integer
					.parseInt(DealsDetails.item)
					* Integer.parseInt(txt_each_item.getText().toString()
							.trim())));

			txt_plus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String prevStrQty = txt_count.getText().toString();
					if (prevStrQty == null)
						prevStrQty = "0";
					int prevQty = Integer.parseInt(prevStrQty);

					int newQty = prevQty + 1;
					txt_count.setText(String.valueOf(newQty));

					int newQty1 = newQty
							* Integer.parseInt(txt_each_item.getText()
									.toString().trim());
					txt_total.setText(String.valueOf(newQty1));
					tv_totalprice.setText(String.valueOf(newQty1));

				}
			});

			txt_minus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String prevStrQty = txt_count.getText().toString();
					int prevQty = Integer.parseInt((prevStrQty == null) ? "0"
							: prevStrQty);

					if (prevQty > 0) {
						int newQty = prevQty - 1;

						txt_count.setText(String.valueOf(newQty));

						int newQty1 = newQty
								* Integer.parseInt(txt_each_item.getText()
										.toString().trim());
						txt_total.setText(String.valueOf(newQty1));
						tv_totalprice.setText(String.valueOf(newQty1));

					} else {
						Intent in = new Intent(getApplicationContext(),
								DealsDetails.class);
						startActivity(in);
						finish();
					}
				}
			});

			return (row);
		}

	}

	class PurchaseItemTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(GetVoucher.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			if (Deals.pay.equalsIgnoreCase("0")) {
				payment = "1";
			} else {
				payment = "2";
			}
			String sessionid = "";
			if (Appconstants.user_flag == 2) {
				sessionid = Appconstants.sessionId;
			} else {
				sessionid = PreferenceUtils.getUserSession();

			}
			String userid = "";
			if (Appconstants.user_flag == 2) {
				userid = Appconstants.userId;
			} else {
				userid = PreferenceUtils.getUserId();

			}

			System.out.println("url is" + Appconstants.MAIN_HOST
					+ "getvoucher?user_id=" + userid + "&payment=" + payment
					+ "&deal_id=" + Deals.strDealId + "&sessionid=" + sessionid
					+ "&amount=" + Deals.pay
					+ "&payment_status=0&paymenttransactionid=0" + "&quantity="
					+ txt_count.getText().toString() + "&phonenumber="
					+ tv_phone.getText().toString());
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "getvoucher?user_id=" + userid + "&payment=" + payment
					+ "&deal_id=" + Deals.strDealId
					+ "&sessionid="
					+ sessionid
					// + "&amount=" + tv_totalprice.getText().toString()
					+ "&amount=" + Deals.pay
					+ "&payment_status=0&paymenttransactionid=0" + "&quantity="
					+ txt_count.getText().toString() + "&phonenumber="
					+ tv_phone.getText().toString());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			System.out.println("result"+result);

			if (null == result || result.length() == 0) {
			} else {

				if (result.contains("errorMessage")) { 

					try {
						String strErrorMsg = new JSONObject(result)
								.optString("errormsg");
						BaseActivity.showDialogMsg(getApplicationContext(),
								strErrorMsg);
					} catch (Exception e) {
						e.printStackTrace();
					}
 
				} else {
					Appconstants.strPurchaseSubUrl = "deals/mobileapp/";
					String id="";
					if (payment.equalsIgnoreCase("1")) {
						
						try {
							
							Appconstants.strPurchaseId = new JSONObject(result).optString("orderid");
							Appconstants.strWebviewUrl = new JSONObject(result).optString("pdfpath");
							id=new JSONObject(result).optString("orderid");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(id.length()>0){
							startActivity(new Intent(getApplicationContext(),
									OrderConfirm.class));
						}
						 
					} else { 
						try {
							System.out.println("result" + result);
							id=new JSONObject(result).optString("orderid");
							if(id.length()>0){
							Appconstants.strPurchaseId = new JSONObject(result).optString("orderid");
							Appconstants.strWebviewUrl = new JSONObject(result).optString("pdfpath");
							System.out.println("url"+Appconstants.strWebviewUrl);
						/*startActivity(new Intent(getApplicationContext(),
									PurchasedItems.class));*/
							Appconstants.strFinalAmount=""+(Integer.parseInt(txt_count.getText().toString())*Double.parseDouble(Deals.pay));
							
							PurchasedItems.address = tv_address_value.getText()
									.toString();
							if(DealsDetails.paytm_flag ==2){
								startActivity(new Intent(getApplicationContext(),
										PurchasedItems.class));
							}else{
								startActivity(new Intent(getApplicationContext(),PayTm.class));
							}
							
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}

			}

		}
	}

	public void Guest() {
		d = new Dialog(GetVoucher.this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//
		// d.setTitle("My Reservation");

		d.setContentView(R.layout.guest_bookatable_status);
		TextView textView1 = (TextView) d.findViewById(R.id.textView1);
		TextView textView10 = (TextView) d.findViewById(R.id.textView10);

		textView10.setText("My Deals");
		textView1.setText("Login to check your Deal details");

		d.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.WHITE));

		// d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
		// ViewGroup.LayoutParams.MATCH_PARENT);
		// TextView title = (TextView) d.findViewById(R.id.title);

		Button home = (Button) d.findViewById(R.id.home);
		Button login = (Button) d.findViewById(R.id.login);

		home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetVoucher.this.finish();
				startActivity(new Intent(GetVoucher.this, Launcher.class));
			}
		});
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Appconstants.user_flag == 2) {
					Appconstants.user_flag = 1;
					Appconstants.sessionId = "";
					// PreferenceUtils.setUserSession(strSession);

					Appconstants.userId = "";
					// PreferenceUtils.setUserId(strUserId);

					Appconstants.firstname = "";

					Appconstants.phone = "";
					Appconstants.email_ = "";

				}
				Login.redirect_flag = 1;
				GetVoucher.this.finish();
				Intent in = new Intent(GetVoucher.this, Login.class);
				in.putExtra("guest_flag", "1");
				startActivity(in);
			}
		});

		d.show();
	}
}
