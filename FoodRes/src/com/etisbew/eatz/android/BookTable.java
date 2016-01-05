package com.etisbew.eatz.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.etisbew.eatz.objects.RestaurentDO;
import com.etisbew.eatz.objects.reviewDO;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;

public class BookTable extends Activity implements OnClickListener {

	static RestaurentDO restDetails = null;
	Button btnBookNow;
	ImageView imgBack, imgnext;
	RatingBar ratingBar;
	RelativeLayout relativelayout;
	TextView txtRestName, txtAddress, txtDate, tvPhone;
	String strSession1 = "", strPoints = "", strSlotTime = "";
	private QuickAction quickAction;
	ImageView options;
	// Button btN;
	// TableLayout table;
	// TableRow tr;
	static JSONObject BookingConfirmationJObj;
	LinearLayout cal_lay, ly1;// , ly2, ly3;

	Calendar c = Calendar.getInstance();
	TextView display;
	int cday, cmonth, cyear, select_color;
	Date date;
	ArrayList<String> tims = new ArrayList<String>();
	String hrs, minuts;
	String getime;
	int flag = 0, ID;
	ArrayList<String> times = new ArrayList<String>();

	Button btn;
	View vv;
	String slot_time = "";
	EditText edtContactNumber;
	ArrayList<String> slots = new ArrayList<String>();
	GridviewAdapter adapter;
	String url1 = "";
	int flag_clicked = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.book_atable1);
		LinearLayout llCall = (LinearLayout) findViewById(R.id.llCall);
		llCall.setOnClickListener(this);

		LinearLayout llWriteReview = (LinearLayout) findViewById(R.id.writeReview);
		llWriteReview.setOnClickListener(this);

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

		ImageView imgnext = (ImageView) findViewById(R.id.iv_rightarrow);
		imgnext.setOnClickListener(this);

		RelativeLayout relativelayout = (RelativeLayout) findViewById(R.id.rlayout_nextarrow);
		relativelayout.setOnClickListener(this);

		ImageView imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(this);
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

		edtContactNumber = (EditText) findViewById(R.id.edtMobile);
		edtContactNumber.setVisibility(View.GONE);
		// if (Appconstants.user_flag == 2) {
		//
		// if (Appconstants.phone.length() <= 8) {
		// edtContactNumber.setText("");
		// edtContactNumber.setVisibility(View.VISIBLE);
		// } else {
		// edtContactNumber.setVisibility(View.GONE);
		// edtContactNumber.setText(Appconstants.phone);
		// }
		//
		// } else {
		// if (TextUtils.isEmpty(PreferenceUtils.getUserPhone1())) {
		// edtContactNumber.setText("");
		// edtContactNumber.setVisibility(View.VISIBLE);
		// } else {
		// edtContactNumber.setVisibility(View.GONE);
		// edtContactNumber.setText(PreferenceUtils.getUserPhone1());
		// }
		//
		// }

		TextView txtRestName = (TextView) findViewById(R.id.etRestName);
		txtRestName.setText(Explore.strRestaurantTitle);
		txtRestName.setOnClickListener(this);

		TextView txtAddress = (TextView) findViewById(R.id.txtAddress);
		System.out.println("address" + Explore.strRestaurantAddress);

		txtAddress.setText(Explore.strRestaurantAddress);
		txtAddress.setOnClickListener(this);

		TextView txtMsg = (TextView) findViewById(R.id.txtMsg);

		TextView txtDate = (TextView) findViewById(R.id.tvdatedisplay);

		ly1 = (LinearLayout) findViewById(R.id.cal_layout1);

		try {
			JSONObject jObj = new JSONObject(Appconstants.booktableres);

			strSession1 = jObj.optString("sessions");
			if (strSession1.contains(",")) {
				String[] strSlots = strSession1.split(",");
				Explore.strBookingSession = strSlots[0];

			} else {
				Explore.strBookingSession = strSession1;
			}

		} catch (Exception e) {
			// TODO: handle exception
		} 

		if (TextUtils.isEmpty(Explore.strBookMsg)) {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd MMMM yyyy");
			String strDate = simpleDateFormat.format(calendar.getTime());
			Explore.strBookDateMsg = strDate + " - "
					+ Explore.strBookingSession + " - party of 2 ";
			Explore.strBookMsg = Explore.strBookingSession
					+ " times available for 2 people on " + strDate;

		}
		if (TextUtils.isEmpty(Explore.strBookDateMsg)) {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd MMMM yyyy");
			String strDate = simpleDateFormat.format(calendar.getTime());
			Explore.strBookDateMsg = strDate + " - "
					+ Explore.strBookingSession + " - party of 2 ";
			Explore.strBookMsg = Explore.strBookingSession
					+ " times available for 2 people on " + strDate;

		}
		txtDate.setText(Explore.strBookDateMsg);
		txtMsg.setText(Explore.strBookMsg);
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
		ratingBar.setOnClickListener(this);

		btnBookNow = (Button) findViewById(R.id.btn_booknow);
		btnBookNow.setOnClickListener(this);

		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				quickAction.show(v);

			}
		});

		try {

			JSONObject jObj1 = new JSONObject(Appconstants.booktableSlotsres);
			JSONArray jArray = jObj1.getJSONArray("Slots");
			slots.clear();
			final ArrayList<String> slotPoints = new ArrayList<String>();
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jobj1 = jArray.getJSONObject(i);
				slots.add(jobj1.optString("slot"));
				slotPoints.add(jobj1.optString("points"));

				// System.out.println("slotsdata"+jobj1.optString("slot"));
				// System.out.println("pointsdata"+jobj1.optString("points"));
			}

			/*
			 * slots.add("14:00"); slots.add("14:00"); slots.add("14:00");
			 * slots.add("14:00"); slots.add("14:00"); slots.add("14:00");
			 */

			adapter = new GridviewAdapter(BookTable.this, slots, slotPoints);
			// gridView.setAdapter(adapter);

			ExpandableHeightGridView mGridView = (ExpandableHeightGridView) findViewById(R.id.spotsView);

			// /getView().findViewById(R.id.gridView);
			mGridView.setExpanded(true);
			mGridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();

			mGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					// user clicked a list item, make it "selected"
					adapter.setSelectedPosition(position);

					slot_time = slots.get(position);

					strPoints = slotPoints.get(position).toString();

					strSlotTime = slot_time.replace(":", "-");

				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showAlertDialog() {
		// TODO Auto-generated method stub

		AlertDialog.Builder alert = new AlertDialog.Builder(BookTable.this);
		alert.setTitle("eatz");
		// Create TextView
		final TextView txtMsg = new TextView(BookTable.this);
		txtMsg.setTextColor(Color.WHITE);
		txtMsg.setPadding(10, 0, 0, 0);
		txtMsg.setTypeface(null, Typeface.BOLD);
		txtMsg.setText("Are you sure, you want to Book a table?");
		alert.setView(txtMsg);

		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				if (ConnectivityReceiver
						.checkInternetConnection(BookTable.this)) {

					new bookingConfirmationTask().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(BookTable.this);
				}
			}
		});

		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();

	}

	private void showAlertDialog1() {
		// TODO Auto-generated method stub

		AlertDialog.Builder alert = new AlertDialog.Builder(BookTable.this);
		alert.setTitle("eatz");
		// Create TextView
		final TextView txtMsg = new TextView(BookTable.this);
		txtMsg.setTextColor(Color.WHITE);
		txtMsg.setPadding(10, 0, 0, 0);
		txtMsg.setTypeface(null, Typeface.BOLD);
		txtMsg.setText("Please select the time of your party");
		alert.setView(txtMsg);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});

		alert.show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.writeReview:

			SetData();
			break;

		case R.id.llCall:

			/*
			 * try { Intent callIntent = new Intent(Intent.ACTION_CALL,
			 * Uri.parse("tel:" + Explore.strphone));// restDetails.phone
			 * startActivity(callIntent); } catch (ActivityNotFoundException e)
			 * { }
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

					Intent callIntent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:" + Explore.strphone));
					startActivity(callIntent);
				}

			} catch (ActivityNotFoundException e) {
			}

			break;
		case R.id.iv_rightarrow:
			Intent i = new Intent(getApplicationContext(),
					BookTableSessions.class);
			startActivity(i);
			break;

		case R.id.rlayout_nextarrow:
			BookTable.this.finish();
			Intent i1 = new Intent(getApplicationContext(),
					BookTableSessions.class);
			startActivity(i1);
			break;

		case R.id.back:
			BookTable.this.finish();
			break;

		case R.id.btn_booknow:

			System.out.println("login values are" + Appconstants.user_flag
					+ ":" + Appconstants.sessionId + ":" +

					":" + Appconstants.userId + ":" + Appconstants.firstname
					+ ":" + Appconstants.phone + ":" + Appconstants.email_);

			if (strSlotTime.length() > 0) {

				//if (PreferenceUtils.getUserPhone1().length() > 5) {
					if (!PreferenceUtils.getUserSession().equalsIgnoreCase(
							"none")) {
						
						showAlertDialog();
						
					} else if (Appconstants.user_flag == 2) {
						showAlertDialog();
					} else {
						flag_clicked = 2;
						startActivity(new Intent(BookTable.this, Login.class));
					}

//				} else {
//					flag_clicked=2;
//					startActivity(new Intent(BookTable.this, Login.class));
					/*if (!PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
						if (edtContactNumber.getText().toString().length() > 0) {
							if (edtContactNumber.getText().toString().length() == 10) {
								showAlertDialog();
							} else {
								Login.showDialogMsg(BookTable.this,
										"Please enter valid phone number");
							}
						} else {
							Login.showDialogMsg(BookTable.this,
									"Please enter contact number");
						}
						}else if(Appconstants.user_flag ==2){
							
							showAlertDialog();
						}else {
							flag_clicked=2;
								startActivity(new Intent(BookTable.this, Login.class));
							}
*/

					/*
					 * if (edtContactNumber.getText().toString().length() > 0) {
					 * if (edtContactNumber.getText().toString().length() == 10)
					 * { showAlertDialog(); } else {
					 * Login.showDialogMsg(BookTable.this,
					 * "Please enter valid phone number"); } } else {
					 * Login.showDialogMsg(BookTable.this,
					 * "Please enter contact number"); }
					 */
					// showAlertDialog();
//				}

			} else {
				showAlertDialog1();
			}

			/*
			 * }else if(Appconstants.user_flag ==2){ if (strSlotTime.length() >
			 * 0) {
			 * 
			 * if (Appconstants.phone.length() > 5) { showAlertDialog();
			 * 
			 * } else {
			 * 
			 * if (edtContactNumber.getText().toString().length() > 0) { if
			 * (edtContactNumber.getText().toString().length() == 10) {
			 * showAlertDialog(); } else { Login.showDialogMsg(BookTable.this,
			 * "Please enter valid phone number"); } } else {
			 * Login.showDialogMsg(BookTable.this,
			 * "Please enter contact number"); } showAlertDialog();
			 * 
			 * }
			 * 
			 * } else { showAlertDialog1(); }
			 * 
			 * 
			 * }else { flag_clicked=2; startActivity(new Intent(BookTable.this,
			 * Login.class)); }
			 */
			break;

		}
 
	}

	public void SetData() {

		if (Localsecrets.search_details_flag == 1) {
			url1 = Localsecrets.det_fl;
			if (Localsecrets.det_fl.length() > 0) {
				if (ConnectivityReceiver
						.checkInternetConnection(BookTable.this)) {
					new DeatilsTasks().execute();
				} else {
					ConnectivityReceiver.showCustomDialog(BookTable.this);
				}
				Localsecrets.search_details_flag = 0;
			} else if (Explore.strVenueId.length() > 0) {
				// url1
				Localsecrets.det_fl = Appconstants.MAIN_HOST
						+ "restaurantDetails/" + Explore.strVenueId;
				if (ConnectivityReceiver
						.checkInternetConnection(BookTable.this)) {
					new DeatilsTasks().execute();
				} else {
					ConnectivityReceiver.showCustomDialog(BookTable.this);
				}
				Localsecrets.search_details_flag = 0;
			}
		} else {
			Localsecrets.det_fl = Appconstants.MAIN_HOST + "restaurantDetails/"
					+ Explore.strVenueId;
			if (ConnectivityReceiver.checkInternetConnection(BookTable.this)) {
				new DeatilsTasks().execute();
			} else {
				ConnectivityReceiver.showCustomDialog(BookTable.this);
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
			myProgressDialog = new ProgressDialog(BookTable.this);
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
		System.out.println("user session"
				+ PreferenceUtils.getUserSession()
				+ PreferenceUtils.getUserId()+PreferenceUtils.getUserPhone1());
		if (flag_clicked == 2) {
			System.out.println("user session"
					+ PreferenceUtils.getUserSession()
					+ PreferenceUtils.getUserId()); 
			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				if (Appconstants.user_flag == 2) {
					if (ConnectivityReceiver
							.checkInternetConnection(BookTable.this)) {
						flag_clicked=1;
						new bookingConfirmationTask().execute();
						
					} else {
						ConnectivityReceiver.showCustomDialog(BookTable.this);
					}
				} else {
					flag_clicked = 2;
					// startActivity(new Intent(OrderDetails.this,
					// Login.class));
				}
			} else {
				if (ConnectivityReceiver
						.checkInternetConnection(BookTable.this)) {
					flag_clicked=1;
					new bookingConfirmationTask().execute();
				} else {
					ConnectivityReceiver.showCustomDialog(BookTable.this);
				}
			}
		}
		/*
		if (Appconstants.user_flag == 2) {

			if (Appconstants.phone.length() <= 8) {
				edtContactNumber.setText("");
				edtContactNumber.setVisibility(View.VISIBLE);
			} else {
				edtContactNumber.setVisibility(View.GONE);
				edtContactNumber.setText(Appconstants.phone);
			}

		} else {
			if (TextUtils.isEmpty(PreferenceUtils.getUserPhone1())) {
				edtContactNumber.setText("");
				edtContactNumber.setVisibility(View.VISIBLE);
			} else {
				edtContactNumber.setVisibility(View.GONE);
				edtContactNumber.setText(PreferenceUtils.getUserPhone1());
			}

		}*/
		/*System.out.println("flag clicked" + flag_clicked);
		if(flag_clicked ==2){
			if(Appconstants.user_flag ==2){
				
				if (Appconstants.phone.length() <=8) {
					edtContactNumber.setText("");
					edtContactNumber.setVisibility(View.VISIBLE);
				} else {
					edtContactNumber.setVisibility(View.GONE);
					edtContactNumber.setText(Appconstants.phone);
				}
		
			} else {
				if (TextUtils.isEmpty(PreferenceUtils.getUserPhone1())) {
					edtContactNumber.setText("");
					edtContactNumber.setVisibility(View.VISIBLE);
				} else {
					edtContactNumber.setVisibility(View.GONE);
					edtContactNumber.setText(PreferenceUtils.getUserPhone1());
				}

			}
			System.out.println("user session"+PreferenceUtils.getUserSession()+PreferenceUtils.getUserId());
			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				if(Appconstants.user_flag==2){
					if (ConnectivityReceiver.checkInternetConnection(BookTable.this)) {
					new bookingConfirmationTask().execute();
					} else {
						ConnectivityReceiver.showCustomDialog(BookTable.this);
					}
				}else{
				flag_clicked=2;
				//startActivity(new Intent(OrderDetails.this, Login.class));
				}
			} else {
				if (edtContactNumber.getText().toString().length() > 0) {
					if (edtContactNumber.getText().toString().length() == 10) {
						if (ConnectivityReceiver.checkInternetConnection(BookTable.this)) {
							new bookingConfirmationTask().execute();
						} else {
							ConnectivityReceiver.showCustomDialog(BookTable.this);
						}
					} else {
						Login.showDialogMsg(BookTable.this,
								"Please enter valid phone number");
					}
				} else {
					flag_clicked=1;
					Login.showDialogMsg(BookTable.this,
							"Please enter contact number");
				}
				
			}
		}
*/
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
							startActivity(new Intent(BookTable.this,
									Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							new AccountDetails(BookTable.this).execute();
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(BookTable.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(BookTable.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(BookTable.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(BookTable.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(BookTable.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(BookTable.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(BookTable.this,
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

		if (TextUtils.isEmpty(PreferenceUtils.getUserPhone1())) {
			edtContactNumber.setText("");
		} else {
			if (PreferenceUtils.getUserPhone1().equalsIgnoreCase("none")) {
				edtContactNumber.setText("");
			} else {
				edtContactNumber.setText(PreferenceUtils.getUserPhone1());
			}

		}
	}

	class bookingConfirmationTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(BookTable.this);
			pDialog.setMessage("Processing. Please wait ...");
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

			String ser_url = Appconstants.MAIN_HOST + "confirmReservation/"
					+ userid + "/" + Explore.strVenueId + "/"
					+ Explore.strPartySize + "/" + Explore.strBookingSession
					+ "/" + Explore.strBookDate1 + "/" + strSlotTime + "/"
					+ strPoints + "/";
			if (PreferenceUtils.getUserPhone1().length() > 5) {
				ser_url = ser_url + PreferenceUtils.getUserPhone1();
			} else if (Appconstants.user_flag == 2) {

				ser_url = ser_url + Appconstants.phone;
			} else {
				ser_url = ser_url + edtContactNumber.getText().toString();
			}
			System.out.println("url is" + ser_url);
			return WebServiceCalls.getJSONString(ser_url);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("result" + result);
			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
			} else {
				try {
					BookingConfirmationJObj = new JSONObject(result);
					if (BookingConfirmationJObj.has("errorCode")) {
						BaseActivity.showDialogMsg(BookTable.this,
								BookingConfirmationJObj
										.optString("errorMessage"));
					} else {
						flag_clicked = 0;
						BookTable.this.finish();
						startActivity(new Intent(getApplicationContext(),
								BookingConfirmation.class));
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	}
}
