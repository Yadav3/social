package com.etisbew.eatz.android.dropdownlist;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.etisbew.eatz.common.WebViewLoader;
import com.etisbew.eatz.objects.Item;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

public class RedemptionHistory extends BaseActivity {

	Button btnAccount;

	List<Item> arrayOfList;
	ListView lvRedemption;
	RowAdapter objAdapter;

	Dialog dialog1;
	Button btnDropDown;
	TextView txtUser, txtlocation, txtPointsEarned, txtPointPending,
			txtRedemptionTitle, txtTotalPts, txtArrowLeft;
	ImageView imgLogo, imgUser;

	TextView txtClickHereRedeem, txtShortMsg;
	String strTotalRedeemPoints = "2000", strRedeemPoints;
	String strRedemptionId, strRedemptionDate, strRedeemedPoints,
			strVoucherValue, strVouchernumber, strStatus, strGlobalPoints,
			strPdfUrl;
	JSONArray redeemjsonArray;

	private QuickAction quickAction;
	ImageView options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.redemptionhistory);

		txtUser = (TextView) findViewById(R.id.txtUser);
		// txtUser.setTypeface(Localsecrets.Titillium_Bold);
		txtUser.setText(Html.fromHtml(PreferenceUtils.getUserName1()));

		txtlocation = (TextView) findViewById(R.id.txtlocation);
		// txtlocation.setTypeface(Localsecrets.Titillium_Bold);

		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				quickAction.show(v);

			}
		});

		// txtArrowLeft = (TextView) findViewById(R.id.txtArrowLeft);
		// txtArrowLeft.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// RedemptionHistory.this.finish();
		// }
		// });
		//
		imgLogo = (ImageView) findViewById(R.id.back);
		imgLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				RedemptionHistory.this.finish();
				startActivity(new Intent(getApplicationContext(),	Launcher.class));
			}
		});

		imgUser = (ImageView) findViewById(R.id.imgReviewPerson);
		Picasso.with(RedemptionHistory.this)
				.load(PreferenceUtils.getUserProfilePic().replace(" ", "%20"))
				// .resize(100, 150).into(imgUser);
				.resize(100, 100).into(imgUser);

		txtPointsEarned = (TextView) findViewById(R.id.txtPointsEarned);
		txtPointPending = (TextView) findViewById(R.id.txtPointPending);

		txtRedemptionTitle = (TextView) findViewById(R.id.txtRedemptionTitle);
		txtTotalPts = (TextView) findViewById(R.id.txtTotalPts);
		txtShortMsg = (TextView) findViewById(R.id.txtShortMsg);
		txtClickHereRedeem = (TextView) findViewById(R.id.txtClickHereRedeem);
		txtClickHereRedeem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showRedeemDialog();
			}
		});

		lvRedemption = (ListView) findViewById(R.id.lvRedemption);
		arrayOfList = new ArrayList<Item>();

		lvRedemption
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						// TODO Auto-generated method stub
						try {
							strRedemptionId = redeemjsonArray
									.getJSONObject(pos)
									.getString("RedemptionId").toString();
							strRedemptionDate = redeemjsonArray
									.getJSONObject(pos).getString("Date")
									.toString();
							strRedeemedPoints = redeemjsonArray
									.getJSONObject(pos)
									.getString("RedeemedPoints").toString();
							strVoucherValue = redeemjsonArray
									.getJSONObject(pos)
									.getString("VoucherValue").toString();
							strVouchernumber = redeemjsonArray
									.getJSONObject(pos)
									.getString("Vouchernumber").toString();
							strStatus = redeemjsonArray.getJSONObject(pos)
									.getString("Status").toString();
							strPdfUrl = redeemjsonArray.getJSONObject(pos)
									.getString("pdf").toString();
							
							
							if (ConnectivityReceiver.checkInternetConnection(RedemptionHistory.this)) {

								new RedemptionDetailsTask().execute();
							} else {
								ConnectivityReceiver.showCustomDialog(RedemptionHistory.this);
							}

//							new RedemptionDetailsTask().execute();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		if (ConnectivityReceiver.checkInternetConnection(RedemptionHistory.this)) {

			new MyTask().execute();
		} else {
			ConnectivityReceiver.showCustomDialog(RedemptionHistory.this);
		}

//		new MyTask().execute();
	}

	private void showRedeemDialog() {
		// TODO Auto-generated method stub
		dialog1 = new Dialog(RedemptionHistory.this);
		// dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog1.setContentView(R.layout.redeemdialog);
		dialog1.setTitle("Redeem your points ");

		TextView txtPoints = (TextView) dialog1.findViewById(R.id.Desc);
		txtPoints.setText("Your total points earned: " + strTotalRedeemPoints);

		btnDropDown = (Button) dialog1.findViewById(R.id.btnDropDown);
		btnDropDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowRedeemPointsDialog();
			}

		});

		Button btnRedeemed = (Button) dialog1.findViewById(R.id.btnRedeemed);
		btnRedeemed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (ConnectivityReceiver.checkInternetConnection(RedemptionHistory.this)) {

					new RedeemPointsTask().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(RedemptionHistory.this);
				}

//				new RedeemPointsTask().execute();
			}
		});

		dialog1.show();
	}

	private void ShowRedeemPointsDialog() {
		// TODO Auto-generated method stub

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				RedemptionHistory.this);
		builderSingle.setTitle("Select redeem points");
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				RedemptionHistory.this, android.R.layout.select_dialog_item);
		int i = 2000;
		int value;
		if (TextUtils.isEmpty(strTotalRedeemPoints)) {
			value = 0;
		} else {
			value = Integer.parseInt(strTotalRedeemPoints);
		}

		while (i <= value) {
			arrayAdapter.add(String.valueOf(i));
			i = i + 1000;
		}

		builderSingle.setAdapter(arrayAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int pos) {

						strRedeemPoints = arrayAdapter.getItem(pos);
						btnDropDown.setText(strRedeemPoints);

					}
				});
		builderSingle.show();
	}

	class MyTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(RedemptionHistory.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "myRedemptions/" + PreferenceUtils.getUserSession());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				BaseActivity.showDialogMsg(RedemptionHistory.this,
						"No data found from web!!!");
				RedemptionHistory.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					JSONArray jArray = mainJson.getJSONArray("points");
					Login.strPointsEarned = jArray.getJSONObject(0).optString(
							"points_earned");
					PreferenceUtils.setUserPoints(jArray.getJSONObject(0).optString(
							"points_earned"));
					Login.strPointsPending = jArray.getJSONObject(0).optString(
							"points_pending");
					PreferenceUtils.setUserPointsPending(jArray.getJSONObject(0).optString(
							"points_pending"));

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

					if (result.contains("errorMessage")) {
						String strErrorMsg = mainJson.optString("errorMessage");

						showDialogMsg(RedemptionHistory.this, strErrorMsg);
					} else {
						redeemjsonArray = mainJson.getJSONArray("redemptions");

						for (int i = 0; i < redeemjsonArray.length(); i++) {
							JSONObject objJson = redeemjsonArray
									.getJSONObject(i);

							Item objItem = new Item();

							objItem.setRedeemDate(objJson.getString("Date"));
							objItem.setRedeemedPoints(objJson
									.getString("RedeemedPoints"));
							objItem.setVoucherValue(objJson
									.getString("VoucherValue"));
							objItem.setVouchernumber(objJson
									.getString("Vouchernumber"));
							objItem.setStatus(objJson.getString("Status"));

							arrayOfList.add(objItem);

						}
					}
					strTotalRedeemPoints = Login.strPointsEarned;
					if (!TextUtils.isEmpty(strTotalRedeemPoints)) {
						if (Integer.parseInt(strTotalRedeemPoints) >= 2000) {
							txtClickHereRedeem.setVisibility(View.VISIBLE);
							txtShortMsg.setVisibility(View.GONE);
						} else {
							txtClickHereRedeem.setVisibility(View.INVISIBLE);
							txtTotalPts.setVisibility(View.INVISIBLE);
							txtShortMsg.setVisibility(View.VISIBLE);
							int pts = 2000 - (Integer
									.parseInt(strTotalRedeemPoints));
							txtShortMsg
									.setText("You can only redeem once you earn 2000 points, you are "
											+ pts + " points short ");
						}
					} else {
						txtClickHereRedeem.setVisibility(View.INVISIBLE);
						txtTotalPts.setVisibility(View.INVISIBLE);
						txtShortMsg.setVisibility(View.VISIBLE);
						int pts = 2000 - (Integer
								.parseInt(strTotalRedeemPoints));
						txtShortMsg
								.setText("You can only redeem once you earn 2000 points, you are "
										+ pts + " points short ");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				setAdapterToListview();
			}
		}
	}

	class RedemptionDetailsTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(RedemptionHistory.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "redemptionDetails/" + PreferenceUtils.getUserSession()
					+ "/" + strRedemptionId);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				BaseActivity.showDialogMsg(RedemptionHistory.this,
						"No data found from server!!!");
				RedemptionHistory.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);

					if (result.contains("errorMessage")) {
						String strErrorMsg = mainJson.optString("errorMessage")
								.toString();
						BaseActivity.showDialogMsg(RedemptionHistory.this, strErrorMsg);
					} else {

						final Dialog dialog = new Dialog(RedemptionHistory.this);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.custom);

						TextView txtDate = (TextView) dialog
								.findViewById(R.id.txtDate);
						txtDate.setText(strRedemptionDate);

						TextView txtRedeemPoints = (TextView) dialog
								.findViewById(R.id.txtRedeemPoints);
						txtRedeemPoints.setText(strRedeemedPoints);

						TextView txtVoucherValue = (TextView) dialog
								.findViewById(R.id.txtVoucherValue);
						txtVoucherValue.setText(strVoucherValue);

						TextView txtVoucherNumber = (TextView) dialog
								.findViewById(R.id.txtVoucherNumber);
						txtVoucherNumber.setText(strVouchernumber);

						TextView txtRedeemStatus = (TextView) dialog
								.findViewById(R.id.txtRedeemStatus);
						txtRedeemStatus.setText(strStatus);

						ImageView imgQr = (ImageView) dialog
								.findViewById(R.id.imageView1);

						if (!TextUtils.isEmpty(strPdfUrl)) {
							imgQr.setVisibility(View.VISIBLE);
						} else {
							imgQr.setVisibility(View.INVISIBLE);
						}
						imgQr.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								if (strPdfUrl.length() > 0) {
									Appconstants.strWebviewUrl = strPdfUrl
											.replace(" ", "%20");
								}
								startActivity(new Intent(
										getApplicationContext(),
										WebViewLoader.class));
							}
						});

						Button dialogButton = (Button) dialog
								.findViewById(R.id.btnOk);
						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});

						dialog.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}
	}

	class RedeemPointsTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(RedemptionHistory.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "redeemYourPoints/" + PreferenceUtils.getUserSession()
					+ "/" + strRedeemPoints);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				BaseActivity.showDialogMsg(RedemptionHistory.this,
						"No data found from server!!!");
				RedemptionHistory.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);

					if (result.contains("errorMessage")) {
						String strErrorMsg = mainJson.optString("errorMessage")
								.toString();
						String strErrorCode = mainJson.optString("errorCode")
								.toString();
						BaseActivity.showDialogMsg(RedemptionHistory.this, strErrorMsg);

						if (Integer.parseInt(strErrorCode) == 0) {
							dialog1.dismiss();
						}

					}
					redeemjsonArray = mainJson.getJSONArray("redemptions");
					for (int i = 0; i < redeemjsonArray.length(); i++) {
						JSONObject objJson = redeemjsonArray.getJSONObject(i);

						Item objItem = new Item();

						objItem.setRedeemDate(objJson.getString("Date"));
						objItem.setRedeemedPoints(objJson
								.getString("RedeemedPoints"));
						objItem.setVoucherValue(objJson
								.getString("VoucherValue"));
						objItem.setVouchernumber(objJson
								.getString("Vouchernumber"));
						objItem.setStatus(objJson.getString("Status"));

						arrayOfList.add(objItem);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}
	}

	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int
	// position,
	// long id) {
	// // showDeleteDialog(position);
	// }

	public void setAdapterToListview() {
		objAdapter = new RowAdapter(RedemptionHistory.this,
				R.layout.redemptionlist1, arrayOfList);
		lvRedemption.setAdapter(objAdapter);

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
							startActivity(new Intent(RedemptionHistory.this,
									Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							
							if (ConnectivityReceiver.checkInternetConnection(RedemptionHistory.this)) {

								new AccountDetails(RedemptionHistory.this)
								.execute();
							} else {
								ConnectivityReceiver.showCustomDialog(RedemptionHistory.this);
							}
							
							
							
//							new AccountDetails(RedemptionHistory.this)
//									.execute();
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(
									RedemptionHistory.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(
									RedemptionHistory.this, MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(
									RedemptionHistory.this, MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(
									RedemptionHistory.this, MyReviews.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(
									RedemptionHistory.this, MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(RedemptionHistory.this,
									Login.class));
							finish();
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
