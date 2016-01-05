package com.etisbew.eatz.android.dropdownlist;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.etisbew.eatz.android.BaseActivity;
import com.etisbew.eatz.android.Explore;
import com.etisbew.eatz.android.Explore.AccountDetails;
import com.etisbew.eatz.android.Launcher;
import com.etisbew.eatz.android.Login;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.android.RowAdapter;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.Item;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

public class MyPoints extends BaseActivity {

	Button btnAccount;
	Dialog dialog, dialog1, dialog2, dialog3;
	List<Item> arrayOfList;
	ListView lvOrders;
	RowAdapter objAdapter;
	RadioGroup rg;
	RadioButton rb1;
	LinearLayout ly;

	ImageView imgLogo, imgUser;
	TextView txtUser, txtlocation, txtPointsEarned, txtPointPending,
			txtReservationTitle, txtArrowLeft;
	JSONArray jsonArray;
	static JSONArray dealDetailsArray;
	static String strReservationId;
	EditText edtWRTitle, edtReviewComments;
	String strOverAllRating, strValueRating, strAtmosRating, strFoodRating,
			strServRating, strSize, strReason;

	private QuickAction quickAction;
	ImageView options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mypoints);

		txtUser = (TextView) findViewById(R.id.txtUser);
		txtUser.setText(Html.fromHtml(PreferenceUtils.getUserName1()));
		// txtUser.setTypeface(Localsecrets.Titillium_Bold);

		txtlocation = (TextView) findViewById(R.id.txtlocation);
		// txtlocation.setTypeface(Localsecrets.Titillium_Bold);

		txtPointsEarned = (TextView) findViewById(R.id.txtPointsEarned);
		txtPointPending = (TextView) findViewById(R.id.txtPointPending);
		// txtPointPending.setTypeface(Localsecrets.Titillium_Bold);

		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				quickAction.show(v);

			}
		});

		imgLogo = (ImageView) findViewById(R.id.back);
		imgLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				MyPoints.this.finish();
					startActivity(new Intent(getApplicationContext(),	Launcher.class));
			}
		});

		imgUser = (ImageView) findViewById(R.id.imgReviewPerson);
		Picasso.with(MyPoints.this)
				.load(PreferenceUtils.getUserProfilePic().replace(" ", "%20"))
				// .resize(100, 150).into(imgUser);
				.resize(100, 100).into(imgUser);

		txtReservationTitle = (TextView) findViewById(R.id.txtReservationTitle);
		lvOrders = (ListView) findViewById(R.id.lvOrders);
		arrayOfList = new ArrayList<Item>();

		// lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener()
		// {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
		// long arg3) {
		// // TODO Auto-generated method stub
		// // showReservationDetails();
		//
		// try {
		// strReservationId = jsonArray.getJSONObject(pos)
		// .getString("ReservationId").toString();
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// // new ReservationDetailsTask().execute();
		// }
		// });

		if (ConnectivityReceiver.checkInternetConnection(MyPoints.this)) {

			new MyTask().execute();

		} else {
			ConnectivityReceiver.showCustomDialog(MyPoints.this);
		}
		
		
//		new MyTask().execute();
	}

	class MyTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyPoints.this);
			pDialog.setMessage("Loading your points. Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "pointsearned/" + PreferenceUtils.getUserSession());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyPoints.this, "No data found from web!!!");

			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					jsonArray = mainJson.getJSONArray("points");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject objJson = jsonArray.getJSONObject(i);

						Item objItem = new Item();

						objItem.setPointsDate(objJson.getString("dateorder"));
						objItem.setPointsVenue(objJson.getString("venuename"));
						objItem.setPointsType(objJson.getString("type"));
						objItem.setPointsEarned(objJson.getString("points"));
						arrayOfList.add(objItem);

					}

					txtPointsEarned.setText(Html
							.fromHtml("<font color=\"#3d3d3d\">"
									+ "Points earned : " + "</font>"
									+ "<font color=\"#d42a2b\">"
									+ PreferenceUtils.getUserPoints() + "</font>"));

					txtPointPending.setText(Html
							.fromHtml("<font color=\"#3d3d3d\">"
									+ "Points pending : " + "</font>"
									+ "<font color=\"#d42a2b\">"
									+ PreferenceUtils.getUserPointsPending() + "</font>"));

				} catch (JSONException e) {
					e.printStackTrace();
				}
				setAdapterToListview();

			}

		}
	}

	// // @Override
	// // public void onItemClick(AdapterView<?> parent, View view, int
	// position,
	// // long id) {
	// // showReservationDetails();
	// // }
	//
	// // private void showReservationDetails() {
	// // // TODO Auto-generated method stub
	// //
	// // dialog = new Dialog(MyReservations.this);
	// // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// // dialog.setContentView(R.layout.reservationdetails);
	// //
	// // TextView txtReservationDetailsTitle = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsTitle);
	// // txtReservationDetailsTitle.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsDate = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsDate);
	// // txtReservationDetailsDate.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsDate1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsDate1);
	// //
	// txtReservationDetailsDate1.setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsVenue = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsVenue);
	// // txtReservationDetailsVenue.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsVenue1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsVenue1);
	// // txtReservationDetailsVenue1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsSession = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsSession);
	// // txtReservationDetailsSession.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsSession1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsSession1);
	// // txtReservationDetailsSession1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsPartySize = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsPartySize);
	// //
	// txtReservationDetailsPartySize.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsPartySize1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsPartySize1);
	// // txtReservationDetailsPartySize1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsConfirmationNo = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsConfirmationNo);
	// // txtReservationDetailsConfirmationNo
	// // .setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsConfirmationNo1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsConfirmationNo1);
	// // txtReservationDetailsConfirmationNo1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsStatus = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsStatus);
	// // txtReservationDetailsStatus.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsStatus1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsStatus1);
	// // txtReservationDetailsStatus1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsPoints = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsPoints);
	// // txtReservationDetailsPoints.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsPoints1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsPoints1);
	// // txtReservationDetailsPoints1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsMadeOn = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsMadeOn);
	// // txtReservationDetailsMadeOn.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsMadeOn1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsMadeOn1);
	// // txtReservationDetailsMadeOn1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
	// //
	// // btnClose.setOnClickListener(new OnClickListener() {
	// // @Override
	// // public void onClick(View v) {
	// // dialog.dismiss();
	// // }
	// // });
	// //
	// // dialog.show();
	// // }

	public void setAdapterToListview() {
		objAdapter = new RowAdapter(MyPoints.this, R.layout.mypoints1,
				arrayOfList);
		lvOrders.setAdapter(objAdapter);

	}

	// class ReservationDetailsTask extends AsyncTask<String, Void, String> {
	//
	// ProgressDialog pDialog;
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	//
	// pDialog = new ProgressDialog(MyDeals.this);
	// pDialog.setMessage("Loading...");
	// pDialog.setCancelable(false);
	// pDialog.show();
	// }
	//
	// @Override
	// protected String doInBackground(String... params) {
	// return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
	// + "reservationDetails/" + PreferenceUtils.getUserSession()
	// + "/" + strReservationId);
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	//
	// if (null != pDialog && pDialog.isShowing()) {
	// pDialog.dismiss();
	// }
	//
	// if (null == result || result.length() == 0) {
	// showDialogMsg(MyDeals.this, "No data found from Server!!!");
	//
	// } else {
	//
	// try {
	//
	// JSONObject jObject = new JSONObject(result);
	// dealDetailsArray = jObject
	// .getJSONArray("getReservations");
	// if (result.contains("ErrorMessage")) {
	// String errmsg = dealDetailsArray
	// .getJSONObject(0).getString("ErrorMessage")
	// .toString();
	// } else {
	//
	// dialog = new Dialog(MyDeals.this);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.mydeals1);
	//
	// // TextView txtReservationDetailsTitle = (TextView)
	// // dialog
	// // .findViewById(R.id.txtReservationDetailsTitle);
	// // txtReservationDetailsTitle
	// // .setTypeface(Localsecrets.Titillium_Bold);
	//
	// // TextView txtReservationDetailsDate = (TextView)
	// // dialog
	// // .findViewById(R.id.txtReservationDetailsDate);
	// // txtReservationDetailsDate
	// // .setTypeface(Localsecrets.Titillium_Bold);
	//
	// TextView txtItemName1 = (TextView) dialog
	// .findViewById(R.id.txtItemName1);
	// // txtReservationDetailsDate1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// txtItemName1.setText(Html
	// .fromHtml(dealDetailsArray
	// .getJSONObject(0)
	// .getString("reservation_date")
	// .toString()));
	//
	// // TextView txtReservationDetailsVenue = (TextView)
	// // dialog
	// // .findViewById(R.id.txtReservationDetailsVenue);
	// // txtReservationDetailsVenue
	// // .setTypeface(Localsecrets.Titillium_Bold);
	//
	// TextView txtVouchers1 = (TextView) dialog
	// .findViewById(R.id.txtVouchers1);
	// // txtReservationDetailsVenue1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// txtVouchers1.setText(Html
	// .fromHtml(dealDetailsArray
	// .getJSONObject(0)
	// .getString("vouchers").toString()));
	//
	// // TextView txtReservationDetailsSession = (TextView)
	// // dialog
	// // .findViewById(R.id.txtReservationDetailsSession);
	// // txtReservationDetailsSession
	// // .setTypeface(Localsecrets.Titillium_Bold);
	//
	// TextView txtVoucherType1 = (TextView) dialog
	// .findViewById(R.id.txtVoucherType1);
	// // txtReservationDetailsSession1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// txtVoucherType1.setText(Html
	// .fromHtml(dealDetailsArray
	// .getJSONObject(0).getString("voucher_type")
	// .toString()));
	//
	// // TextView txtReservationDetailsPartySize = (TextView)
	// // dialog
	// // .findViewById(R.id.txtReservationDetailsPartySize);
	// // txtReservationDetailsPartySize
	// // .setTypeface(Localsecrets.Titillium_Bold);
	//
	// TextView txtUnitPrice1 = (TextView) dialog
	// .findViewById(R.id.txtUnitPrice1);
	// // txtReservationDetailsPartySize1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// txtUnitPrice1.setText(Html
	// .fromHtml(dealDetailsArray
	// .getJSONObject(0)
	// .getString("unit_price").toString()));
	//
	// // TextView txtReservationDetailsConfirmationNo =
	// // (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsConfirmationNo);
	// // txtReservationDetailsConfirmationNo
	// // .setTypeface(Localsecrets.Titillium_Bold);
	//
	// TextView txtQuantity1 = (TextView) dialog
	// .findViewById(R.id.txtQuantity1);
	// // txtReservationDetailsConfirmationNo1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// txtQuantity1.setText(Html
	// .fromHtml(dealDetailsArray
	// .getJSONObject(0)
	// .getString("confirmation_number")
	// .toString()));
	//
	// // TextView txtReservationDetailsStatus = (TextView)
	// // dialog
	// // .findViewById(R.id.txtReservationDetailsStatus);
	// // txtReservationDetailsStatus
	// // .setTypeface(Localsecrets.Titillium_Bold);
	//
	// TextView txtPrice1 = (TextView) dialog
	// .findViewById(R.id.txtPrice1);
	// // txtReservationDetailsStatus1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// txtPrice1.setText(Html
	// .fromHtml(dealDetailsArray
	// .getJSONObject(0).getString("status")
	// .toString()));
	//
	// // TextView txtReservationDetailsPoints = (TextView)
	// // dialog
	// // .findViewById(R.id.txtReservationDetailsPoints);
	// // txtReservationDetailsPoints
	// // .setTypeface(Localsecrets.Titillium_Bold);
	//
	// TextView txtReservationDetailsPoints1 = (TextView) dialog
	// .findViewById(R.id.txtReservationDetailsPoints1);
	// // txtReservationDetailsPoints1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// txtReservationDetailsPoints1.setText(Html
	// .fromHtml(dealDetailsArray
	// .getJSONObject(0).getString("points")
	// .toString()));
	//
	// // TextView txtReservationDetailsMadeOn = (TextView)
	// // dialog
	// // .findViewById(R.id.txtReservationDetailsMadeOn);
	// // txtReservationDetailsMadeOn
	// // .setTypeface(Localsecrets.Titillium_Bold);
	//
	// TextView txtReservationDetailsMadeOn1 = (TextView) dialog
	// .findViewById(R.id.txtReservationDetailsMadeOn1);
	// // txtReservationDetailsMadeOn1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// txtReservationDetailsMadeOn1.setText(Html
	// .fromHtml(dealDetailsArray
	// .getJSONObject(0)
	// .getString("reservation_madeon")
	// .toString()));
	//
	// Button btnClose = (Button) dialog
	// .findViewById(R.id.btnClose);
	//
	// btnClose.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// dialog.dismiss();
	// }
	// });
	//
	// TextView txtCancel = (TextView) dialog
	// .findViewById(R.id.txtCancel);
	// // txtCancel.setTypeface(Localsecrets.Titillium_Semibold);
	// if (Integer.parseInt(dealDetailsArray
	// .getJSONObject(0).getString("Cancel")
	// .toString()) == 1) {
	// txtCancel.setVisibility(View.VISIBLE);
	// } else {
	// txtCancel.setVisibility(View.GONE);
	// }
	// txtCancel.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// ShowCancelAlert();
	// }
	// });
	//
	// // TextView txtRateVenue = (TextView) dialog
	// // .findViewById(R.id.txtRateVenue);
	// // // txtRateVenue
	// // // .setTypeface(Localsecrets.Titillium_Semibold);
	// // if (Integer.parseInt(dealDetailsArray
	// // .getJSONObject(0).getString("Rate this venue")
	// // .toString()) == 1) {
	// // txtRateVenue.setVisibility(View.VISIBLE);
	// // } else {
	// // txtRateVenue.setVisibility(View.GONE);
	// // }
	// // txtRateVenue.setOnClickListener(new OnClickListener() {
	// //
	// // @Override
	// // public void onClick(View v) {
	// // // TODO Auto-generated method stub
	// // RateVenue();
	// // }
	// //
	// // });
	//
	// // TextView txtClaimPoints = (TextView) dialog
	// // .findViewById(R.id.txtClaimPoints);
	// // // txtClaimPoints
	// // // .setTypeface(Localsecrets.Titillium_Semibold);
	// // if (Integer.parseInt(dealDetailsArray
	// // .getJSONObject(0)
	// // .getString("Claim your points").toString()) == 1) {
	// // txtClaimPoints.setVisibility(View.VISIBLE);
	// // } else {
	// // txtClaimPoints.setVisibility(View.GONE);
	// // }
	// // txtClaimPoints
	// // .setOnClickListener(new OnClickListener() {
	// //
	// // @Override
	// // public void onClick(View v) {
	// // // TODO Auto-generated method stub
	// // ShowClaimPointsAlert();
	// // }
	// // });
	//
	// dialog.show();
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
	// }
	//
	// }

	// private void ShowCancelAlert() {
	// // TODO Auto-generated method stub
	//
	// AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
	// MyDeals.this);
	// alertDialogBuilder.setTitle("Reason for cancellation ");
	//
	// // LayoutParams layoutParams = new
	// // LayoutParams(LayoutParams.FILL_PARENT,
	// // LayoutParams.WRAP_CONTENT);
	// // layoutParams.setMargins(10, 10, 10, 10);
	//
	// final EditText edt = new EditText(MyDeals.this);
	// edt.setBackgroundColor(Color.WHITE);
	// edt.setHint("Reason");
	// edt.setLines(2);
	// // edt.setLayoutParams(layoutParams);
	//
	// // LinearLayout layout = new LinearLayout(ReservationConfirmed.this);
	// // layout.addView(edt);
	// // alertDialogBuilder.setView(layout);
	// alertDialogBuilder.setView(edt);
	// alertDialogBuilder.setPositiveButton("Ok",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int id) {
	//
	// strReason = edt.getText().toString();
	// if (!TextUtils.isEmpty(strReason)) {
	// new CancelReservationTask().execute();
	// } else {
	// showDialogMsg(MyDeals.this, "Please enter reason");
	// }
	//
	// }
	// });
	//
	// AlertDialog alertDialog = alertDialogBuilder.create();
	// alertDialog.show();
	// }

	class CancelReservationTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyPoints.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "cancelReservation/" + PreferenceUtils.getUserSession()
					+ "/" + strReservationId + "/"
					+ strReason.replace(" ", "%20").trim());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyPoints.this, "No data found from web!!!");

			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					String strErrorCode = mainJson.optString("errorCode");
					if (Integer.parseInt(strErrorCode) == 0) {
						dialog.dismiss();
					}
					String strErrorMsg = mainJson.optString("errorMessage");
					showDialogMsg(MyPoints.this, strErrorMsg);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void RateVenue() {

		dialog1 = new Dialog(MyPoints.this);
		dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog1.setContentView(R.layout.writereview);

		// TextView txtWriteReviewTitle = (TextView) dialog1
		// .findViewById(R.id.txtWriteReviewTitle);
		// txtWriteReviewTitle.setTypeface(Localsecrets.Titillium_Bold);

		// TextView txtBeTheFirstReview = (TextView) dialog1
		// .findViewById(R.id.txtBeTheFirstReview);
		// // txtBeTheFirstReview.setTypeface(Localsecrets.Titillium_Semibold);
		// try {
		// txtBeTheFirstReview.setText("Rate "
		// + Html.fromHtml(dealDetailsArray.getJSONObject(0)
		// .getString("Venuename").toString()));
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// TextView txtService = (TextView)
		// dialog1.findViewById(R.id.txtService);
		// txtService.setTypeface(Localsecrets.Titillium_Semibold);

		// TextView txtFood = (TextView) dialog1.findViewById(R.id.txtFood);
		// txtFood.setTypeface(Localsecrets.Titillium_Semibold);

		// TextView txtAtmos = (TextView) dialog1.findViewById(R.id.txtAtmos);
		// txtAtmos.setTypeface(Localsecrets.Titillium_Semibold);

		// TextView txtValue = (TextView) dialog1.findViewById(R.id.txtValue);
		// txtValue.setTypeface(Localsecrets.Titillium_Semibold);

		// TextView txtOverAll = (TextView)
		// dialog1.findViewById(R.id.txtOverAll);
		// txtOverAll.setTypeface(Localsecrets.Titillium_Semibold);

		// TextView txtLimitMsg = (TextView) dialog1
		// .findViewById(R.id.txtLimitMsg);
		// txtLimitMsg.setTypeface(Localsecrets.Titillium_Regular);

		final TextView txtMsgCount = (TextView) dialog1
				.findViewById(R.id.txtMsgCount);
		// txtMsgCount.setTypeface(Localsecrets.Titillium_Regular);

		Button btnReview = (Button) dialog1.findViewById(R.id.btnReviewCount);
		btnReview.setVisibility(View.GONE);

		edtWRTitle = (EditText) dialog1.findViewById(R.id.edtWRTitle);
		// edtWRTitle.setTypeface(Localsecrets.Titillium_Semibold);

		edtReviewComments = (EditText) dialog1
				.findViewById(R.id.edtReviewComments);
		// edtReviewComments.setTypeface(Localsecrets.Titillium_Semibold);
		edtReviewComments.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				txtMsgCount.setText(s.length() + " Characters");
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

		RatingBar rbService = (RatingBar) dialog1.findViewById(R.id.RBService);
		rbService.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {

				strServRating = String.valueOf(rating);

			}
		});
		RatingBar rbFood = (RatingBar) dialog1.findViewById(R.id.RBFood);
		rbFood.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strFoodRating = String.valueOf(rating);

			}
		});
		RatingBar rbAtmos = (RatingBar) dialog1.findViewById(R.id.RBAtmos);
		rbAtmos.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strAtmosRating = String.valueOf(rating);

			}
		});
		RatingBar rbValue = (RatingBar) dialog1.findViewById(R.id.RBValue);
		rbValue.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strValueRating = String.valueOf(rating);

			}
		});

		RatingBar rbOverAll = (RatingBar) dialog1.findViewById(R.id.RBOverAll);
		rbOverAll.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strOverAllRating = String.valueOf(rating);

			}
		});

		Button btnWriteReviewSubmit = (Button) dialog1
				.findViewById(R.id.btnWriteReviewSubmit);
		// btnWriteReviewSubmit.setTypeface(Localsecrets.Titillium_Semibold);

		btnWriteReviewSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(edtWRTitle.getText().toString())) {
					showDialogMsg(MyPoints.this, "Please enter review title");
				}
				if (TextUtils.isEmpty(edtReviewComments.getText().toString())) {
					showDialogMsg(MyPoints.this, "Please enter review comments");
				}
				if (!TextUtils.isEmpty(edtWRTitle.getText().toString())
						&& !TextUtils.isEmpty(edtReviewComments.getText()
								.toString())) {
					// if (ConnectivityReceiver
					// .checkInternetConnection(MyReservations.this)) {
					// new RateThisVenueTask().execute();
					// } else {
					// Login.showDialogMsg(MyReservations.this,
					// "Sorry, Network is not available. Please try again later");
					// }

				}

			}
		});

		dialog1.show();
	}

	class RateThisVenueTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyPoints.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "rateThisVenue/"
					+ PreferenceUtils.getUserSession()
					+ "/"
					+ strReservationId
					+ "/"
					+ edtWRTitle.getText().toString().replace(" ", "%20")
							.trim()
					+ "/"
					+ edtReviewComments.getText().toString()
							.replace(" ", "%20").trim() + "/"
					+ strOverAllRating + "/" + strServRating + "/"
					+ strValueRating + "/" + strAtmosRating + "/"
					+ strFoodRating);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyPoints.this, "No data found from web!!!");
				// MyReservations.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					String strErrorCode = mainJson.optString("errorCode");
					if (Integer.parseInt(strErrorCode) == 0) {
						dialog1.dismiss();
						if (dialog.isShowing()) {
							dialog.cancel();
						}
					} else {

					}
					String strErrorMsg = mainJson.optString("errorMessage");
					showDialogMsg(MyPoints.this, strErrorMsg);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	// private void ShowClaimPointsAlert() {
	// // TODO Auto-generated method stub
	// dialog3 = new Dialog(MyDeals.this);
	// // dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog3.setContentView(R.layout.claimdialog);
	//
	// dialog3.setTitle("Claim your points");
	//
	// TextView txtGuestSize = (TextView) dialog3
	// .findViewById(R.id.txtGuestSize);
	// // txtGuestSize.setTypeface(Localsecrets.Titillium_Bold);
	// try {
	// strSize = MyDeals.dealDetailsArray.getJSONObject(0)
	// .getString("party_size").toString();
	// txtGuestSize.setText("Your guests size is "
	// + MyDeals.dealDetailsArray.getJSONObject(0)
	// .getString("party_size").toString());
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	// // TextView txtConfirmation = (TextView) dialog3
	// // .findViewById(R.id.txtConfirmation);
	// // txtConfirmation.setTypeface(Localsecrets.Titillium_Regular);
	//
	// rg = (RadioGroup) dialog3.findViewById(R.id.radioGroup1);
	//
	// ly = (LinearLayout) dialog3.findViewById(R.id.ly);
	//
	// rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	//
	// @Override
	// public void onCheckedChanged(RadioGroup group, int checkedId) {
	// // TODO Auto-generated method stub
	//
	// rb1 = (RadioButton) dialog3.findViewById(rg
	// .getCheckedRadioButtonId());
	// String name = rb1.getText().toString();
	//
	// if (name.equalsIgnoreCase("No")) {
	// ly.setVisibility(View.VISIBLE);
	// } else {
	// ly.setVisibility(View.GONE);
	// }
	//
	// }
	// });
	//
	// // TextView txtChkGuests = (TextView)
	// // dialog3.findViewById(R.id.textView1);
	// // txtChkGuests.setTypeface(Localsecrets.Titillium_Regular);
	//
	// EditText edtChkGuest = (EditText) dialog3
	// .findViewById(R.id.edtChkinGuests);
	//
	// if (!TextUtils.isEmpty(edtChkGuest.getText().toString())) {
	// strSize = edtChkGuest.getText().toString();
	// }
	//
	// Button dialogButton = (Button) dialog3.findViewById(R.id.btnSubmit);
	// dialogButton.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	//
	// new ClaimYourPointsTask().execute();
	//
	// }
	// });
	//
	// dialog3.show();
	// }

	class ClaimYourPointsTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyPoints.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "claimYourPoints/" + PreferenceUtils.getUserSession()
					+ "/" + strReservationId + "/" + strSize.trim());

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyPoints.this, "No data found from web!!!");
				// // MyReservations.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					String strErrorCode = mainJson.optString("errorCode");
					if (Integer.parseInt(strErrorCode) == 0) {
						if (dialog1.isShowing())
							dialog1.cancel();
						if (dialog3.isShowing()) {
							dialog3.cancel();
						}
					}
					String strErrorMsg = mainJson.optString("errorMessage");
					showDialogMsg(MyPoints.this, strErrorMsg);

				} catch (JSONException e) {
					e.printStackTrace();
				}
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
							startActivity(new Intent(MyPoints.this, Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							
							if (ConnectivityReceiver.checkInternetConnection(MyPoints.this)) {

								new AccountDetails(MyPoints.this).execute();

							} else {
								ConnectivityReceiver.showCustomDialog(MyPoints.this);
							}
							
//							new AccountDetails(MyPoints.this).execute();
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(MyPoints.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(MyPoints.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(MyPoints.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(MyPoints.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(MyPoints.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							finish();
							startActivity(new Intent(MyPoints.this, Login.class));
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(),	Launcher.class));
	}

}
