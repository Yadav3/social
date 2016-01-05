package com.etisbew.eatz.android;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.etisbew.eatz.objects.reviewDO;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;

public class BookingConfirmation extends BaseActivity {

	private QuickAction quickAction;
	ImageView options;
	ImageView imgBack;
	TextView tv_bookRestaurantName, tv_bookAddress, tv_bookingTime,
			txtLunchTime, txtBookingUsername, txtBookUserEmail,
			txtBookUserPhNo, txtBookingConfirmationNumber, txtAddress, rating,
			tvPhone, editText2, tv_login, editType, etRestName;

	Button btn_send, btn_skip;
	EditText et_mailadr1, et_comments1;
	Dialog d;
	String url1;
	int userstatus;
	String points = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bookconfirmation);
 
		LinearLayout llCall = (LinearLayout) findViewById(R.id.llCall);
		llCall.setOnClickListener(new OnClickListener() { 
 
			@Override 
			public void onClick(View v) {
				/*
				 * try { Intent callIntent = new Intent(Intent.ACTION_CALL,
				 * Uri.parse("tel:" + Explore.strphone));// restDetails.phone
				 * startActivity(callIntent); } catch (ActivityNotFoundException
				 * e) { }
				 */
				try {
					if (Explore.strphone.contains(",")) {
						String s[] = Explore.strphone.split(",");

						// Intent callIntent = new
						// Intent(Intent.ACTION_CALL,
						// Uri.parse("tel:" + s[0]+""
						// + PhoneNumberUtils.PAUSE + "#"
						// + s[1]));

						Intent callIntent = new Intent(Intent.ACTION_CALL);

						callIntent.setData(Uri.parse("tel:" + s[0] + ""
								+ PhoneNumberUtils.PAUSE + "P" + s[1] + ";"));
						startActivity(callIntent);
					} else {

						Intent callIntent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + Explore.strphone));
						startActivity(callIntent);
					}

				} catch (ActivityNotFoundException e) {
				}

			}
		});

		LinearLayout llWriteReview = (LinearLayout) findViewById(R.id.writeReview);
		llWriteReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SetData();
			}
		});

		editText2 = (TextView) findViewById(R.id.editText2);
		editType = (TextView) findViewById(R.id.editType);
		tv_login = (TextView) findViewById(R.id.tv_login);
		etRestName = (TextView) findViewById(R.id.etRestName);
		txtAddress = (TextView) findViewById(R.id.txtAddress);

		tvPhone = (TextView) findViewById(R.id.tvPhone);
		// tvPhone.setText(Explore.strphone);
		try { 

			if (Explore.strphone.contains(",")) {
				String s[] = Explore.strphone.split(",");

				tvPhone.setText(s[0] +" \nExt. "+s[1]); 

			} else {

				tvPhone.setText(Explore.strphone);
			}

		} catch (Exception e) {
			tvPhone.setText(Explore.strphone);
		}

		RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
		LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
		stars.getDrawable(2)
				.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		int rate = Integer.parseInt(Explore.strRestaurantRating);
		if (rate > 0) {
			ratingBar.setRating(rate);
		} else {
			ratingBar.setVisibility(View.INVISIBLE);
		}

		LinearLayout llDirections = (LinearLayout) findViewById(R.id.llDirections);
		llDirections.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,

				Uri.parse("http://maps.google.com/maps?saddr="
						+ Appconstants.LATTITUDE + "," + Appconstants.LANGITUDE
						+ "&daddr=" + Explore.strlat + "," + Explore.strlon));
				startActivity(intent);
			}
		});

		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				quickAction.show(v);

			}
		});

		imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BookingConfirmation.this.finish();
				startActivity(new Intent(BookingConfirmation.this,
						Launcher.class));
			}
		});

		tv_bookRestaurantName = (TextView) findViewById(R.id.tv_bookRestaurantName);
		tv_bookRestaurantName.setText(BookTable.BookingConfirmationJObj
				.optString("Venuename"));
		etRestName.setText(BookTable.BookingConfirmationJObj
				.optString("Venuename"));
		txtAddress.setText(BookTable.BookingConfirmationJObj
				.optString("address1")
				+ ","
				+ BookTable.BookingConfirmationJObj.optString("address2")
				+ ","
				+ BookTable.BookingConfirmationJObj.optString("Location"));

		tv_bookAddress = (TextView) findViewById(R.id.tv_bookAddress);
		tv_bookAddress.setText(BookTable.BookingConfirmationJObj
				.optString("address1")
				+ "\n"
				+ BookTable.BookingConfirmationJObj.optString("address2")
				+ "\n"
				+ BookTable.BookingConfirmationJObj.optString("Location")
				+ "\n"
				+ BookTable.BookingConfirmationJObj.optString("venuephone"));

		tv_bookingTime = (TextView) findViewById(R.id.tv_bookingTime);
		tv_bookingTime.setText(BookTable.BookingConfirmationJObj
				.optString("Date"));

		txtLunchTime = (TextView) findViewById(R.id.txtLunchTime);
		txtLunchTime
				.setText(BookTable.BookingConfirmationJObj
						.optString("partySize")
						+ " people at "
						+ BookTable.BookingConfirmationJObj.optString("Slot")
						+ " ( "
						+ BookTable.BookingConfirmationJObj
								.optString("Session") + " )");

		userstatus = Integer.parseInt(BookTable.BookingConfirmationJObj
				.optString("usertype"));
		try {
			JSONObject jObj1 = BookTable.BookingConfirmationJObj
					.getJSONObject("aftersuccess");
			points = jObj1.getString("points");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Appconstants.user_flag == 2) {

			if (userstatus == 1) {

				editType.setText("Upon honoring this booking you earn "
						+ points + "Eatz reward points. ");
				editText2
						.setText(Html
								.fromHtml("<html><body><font> Your email address is already registered with us. Login to check your booking details. </font> <br> </body><html>"));

				tv_login.setText("Login");
			} else if (userstatus == 0) {
				editType.setVisibility(View.GONE);
				editText2
						.setText(Html
								.fromHtml("<html><body><font> Only registered members are eligible for "
										+ points
										+ " Eatz reward points. Kindly register to claim your points </font> <br> </body><html>"));
				tv_login.setText("Register me");
			}

		} else {
			editText2.setVisibility(View.GONE);
			tv_login.setVisibility(View.GONE);
			editType.setVisibility(View.GONE);

		}

		txtBookingUsername = (TextView) findViewById(R.id.txtBookingUsername);
		txtBookingUsername.setText(BookTable.BookingConfirmationJObj
				.optString("Username"));

		txtBookUserEmail = (TextView) findViewById(R.id.txtBookUserEmail);
		txtBookUserEmail.setText(BookTable.BookingConfirmationJObj
				.optString("Email"));

		txtBookUserPhNo = (TextView) findViewById(R.id.txtBookUserPhNo);
		if(TextUtils.isEmpty(BookTable.BookingConfirmationJObj
				.optString("Phone")) || BookTable.BookingConfirmationJObj
				.optString("Phone").equalsIgnoreCase("null")|| BookTable.BookingConfirmationJObj
				.optString("Phone").equalsIgnoreCase(null)){
			txtBookUserPhNo.setVisibility(View.GONE);
		}else{
		txtBookUserPhNo.setText(BookTable.BookingConfirmationJObj
				.optString("Phone"));
		}

		txtBookingConfirmationNumber = (TextView) findViewById(R.id.txtBookingConfirmationNumber);
		txtBookingConfirmationNumber
				.setText("Booking confirmation number : "
						+ BookTable.BookingConfirmationJObj
								.optString("confirmationId"));

		btn_send = (Button) findViewById(R.id.btn_send);
		et_mailadr1 = (EditText) findViewById(R.id.et_mailadr1);
		et_comments1 = (EditText) findViewById(R.id.et_comments1);
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ConnectivityReceiver
						.checkInternetConnection(BookingConfirmation.this)) {
					if (eMailValidation(et_mailadr1.getText().toString().trim())) {
						new InviteFriendsTask().execute();
					} else {
						showDialogMsg("Please enter valid email address");
					}
				} else {
					ConnectivityReceiver
							.showCustomDialog(BookingConfirmation.this);
				}
			}
		});
		tv_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BookingConfirmation.this.finish();
				Intent in = new Intent(BookingConfirmation.this, Login.class);
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
				Appconstants.user_flag = 1;

				if (tv_login.getText().toString().equalsIgnoreCase("Login")) {
					in.putExtra("guest_flag", "1");
					startActivity(in);
				} else {
					startActivity(new Intent(BookingConfirmation.this,
							SignUp.class));
				}
			}
		});

		btn_skip = (Button) findViewById(R.id.btn_skip);
		btn_skip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Appconstants.user_flag == 2) {
					// Guest();
					startActivity(new Intent(getApplicationContext(),
							Launcher.class));
				} else {

					startActivity(new Intent(getApplicationContext(),
							MyReservations.class));
				}
			}
		});
	}

	public void SetData() {

		if (Localsecrets.search_details_flag == 1) {
			url1 = Localsecrets.det_fl;
			if (Localsecrets.det_fl.length() > 0) {
				if (ConnectivityReceiver
						.checkInternetConnection(BookingConfirmation.this)) {
					new DeatilsTasks().execute();
				} else {
					ConnectivityReceiver
							.showCustomDialog(BookingConfirmation.this);
				}
				Localsecrets.search_details_flag = 0;
			} else if (Explore.strVenueId.length() > 0) {
				// url1
				Localsecrets.det_fl = Appconstants.MAIN_HOST
						+ "restaurantDetails/" + Explore.strVenueId;
				if (ConnectivityReceiver
						.checkInternetConnection(BookingConfirmation.this)) {
					new DeatilsTasks().execute();
				} else {
					ConnectivityReceiver
							.showCustomDialog(BookingConfirmation.this);
				}
				Localsecrets.search_details_flag = 0;
			}
		} else {
			Localsecrets.det_fl = Appconstants.MAIN_HOST + "restaurantDetails/"
					+ Explore.strVenueId;
			if (ConnectivityReceiver
					.checkInternetConnection(BookingConfirmation.this)) {
				new DeatilsTasks().execute();
			} else {
				ConnectivityReceiver.showCustomDialog(BookingConfirmation.this);
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
			myProgressDialog = new ProgressDialog(BookingConfirmation.this);
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
					// BookTable.this.finish();
				}

				myProgressDialog.dismiss();
			} catch (Exception e) {
				myProgressDialog.dismiss();
				Log.e("", "" + e);
			}

		}
	}

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
							startActivity(new Intent(BookingConfirmation.this,
									Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {

							if (ConnectivityReceiver
									.checkInternetConnection(BookingConfirmation.this)) {
								new AccountDetails(BookingConfirmation.this)
										.execute();

							} else {
								ConnectivityReceiver
										.showCustomDialog(BookingConfirmation.this);
							}
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(
									BookingConfirmation.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(
									BookingConfirmation.this, MyOrders.class),
									1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(
									BookingConfirmation.this, MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(
									BookingConfirmation.this, MyReviews.class),
									1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(
									BookingConfirmation.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(
									BookingConfirmation.this, MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(BookingConfirmation.this,
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

	class InviteFriendsTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(BookingConfirmation.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			String userid = "";
			if (!PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				userid = PreferenceUtils.getUserId();
			} else if (Appconstants.user_flag == 2) {
				userid = Appconstants.userId;
			}

			String ser_url = Appconstants.MAIN_HOST
					+ "inviteFriends/"
					+ userid
					+ "/"
					+ Explore.strVenueId
					+ "/"
					+ BookTable.BookingConfirmationJObj
							.optString("confirmationId")
					+ "/"
					+ et_mailadr1.getText().toString().trim()
					+ "/"
					+ et_comments1.getText().toString().replace(" ", "%20")
							.trim();
			return WebServiceCalls.getJSONString(ser_url);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg("No data found from web!!!");

			} else {

				try {
					String strErrMsg = new JSONObject(result)
							.optString("errorMessage");

					String strErrorCode = new JSONObject(result)
							.optString("errorCode");

					if (Integer.parseInt(strErrorCode) == 0) {
						if (Appconstants.user_flag == 2) {
							// Guest();
							startActivity(new Intent(getApplicationContext(),
									Launcher.class));
						} else {

							startActivity(new Intent(getApplicationContext(),
									MyReservations.class));
						}

					} else {
						showDialogMsg(strErrMsg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	public void Guest() {
		d = new Dialog(BookingConfirmation.this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//
		// d.setTitle("My Reservation");

		d.setContentView(R.layout.guest_bookatable_status);
		TextView textView1 = (TextView) d.findViewById(R.id.textView1);
		TextView textView10 = (TextView) d.findViewById(R.id.textView10);

		textView10.setText("My Reservations");
		textView1.setText("Login to check your Booking status");
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
				BookingConfirmation.this.finish();
				startActivity(new Intent(BookingConfirmation.this,
						Launcher.class));
			}
		});
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Login.redirect_flag = 1;
				BookingConfirmation.this.finish();
				Intent in = new Intent(BookingConfirmation.this, Login.class);
				in.putExtra("guest_flag", "1");
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
				startActivity(in);
			}
		});

		d.show();
	}

	@Override
	public void onBackPressed() {
		finish();

		startActivity(new Intent(getApplicationContext(), Launcher.class));
	}
}
