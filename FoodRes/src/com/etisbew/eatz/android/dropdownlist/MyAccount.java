package com.etisbew.eatz.android.dropdownlist;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.etisbew.eatz.android.BaseActivity;
import com.etisbew.eatz.android.Explore;
import com.etisbew.eatz.android.Launcher;
import com.etisbew.eatz.android.Login;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

public class MyAccount extends BaseActivity {

	ScrollView SvMyAccount;
	LinearLayout LyChangePwd;
	TextView txtUser, txtlocation, txtPointsEarned, txtPointPending,
			txtMyAccount, txtChangePwd, txtOldPwd, txtNewPwd, txtConfirmPwd,
			txtFirstName, txtlastName, txtEmail, txtPhone, txtAddress,
			txtArrowLeft;
	ImageView imgLogo, imgUser, back;

	EditText edtOldPwd, edtNewPwd, edtConfirmPwd, edtFirstName, edtLastName,
			edtEmail1, edtPhone, edtAddress1;
	Button btnUpdate, btnUpdate1, btnAccount;

	String strFName, strLName, strEmail, strPhone, strAddress;
	String strOldPwd, strNewPwd, strConfirmPwd;

	private QuickAction quickAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account);

		SvMyAccount = (ScrollView) findViewById(R.id.SVMyAccount);
		LyChangePwd = (LinearLayout) findViewById(R.id.LYChangePwd);

		ImageView options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});

		txtUser = (TextView) findViewById(R.id.txtUser);
		txtUser.setText(Html.fromHtml(PreferenceUtils.getUserName1()));

		txtlocation = (TextView) findViewById(R.id.txtlocation);

		txtPointsEarned = (TextView) findViewById(R.id.txtPointsEarned);
		txtPointsEarned.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),
						MyPoints.class));
			}
		});

		try {
			Login.strPointsEarned = Launcher.accountDetailsArray
					.getJSONObject(0).getString("points_earned").toString();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		txtPointsEarned.setText(Html.fromHtml("<font color=\"#3d3d3d\">"
				+ "Points earned : " + "</font>" + "<font color=\"#d42a2b\">"
				+ PreferenceUtils.getUserPoints() + "</font>"));

		txtPointPending = (TextView) findViewById(R.id.txtPointPending);
		// txtPointPending.setTypeface(Localsecrets.Titillium_Bold);
		try {
			Login.strPointsPending = Launcher.accountDetailsArray
					.getJSONObject(0).getString("points_pending").toString();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		txtPointPending.setText(Html.fromHtml("<font color=\"#3d3d3d\">"
				+ "Points pending : " + "</font>" + "<font color=\"#d42a2b\">"
				+ PreferenceUtils.getUserPointsPending() + "</font>"));

		txtMyAccount = (TextView) findViewById(R.id.txtMyAccount);
		txtChangePwd = (TextView) findViewById(R.id.txtChangePwd);
		txtOldPwd = (TextView) findViewById(R.id.txtOldPwd);
		txtNewPwd = (TextView) findViewById(R.id.txtNewPwd);
		txtConfirmPwd = (TextView) findViewById(R.id.txtConfirmPwd);
		txtFirstName = (TextView) findViewById(R.id.txtFirstName);
		txtlastName = (TextView) findViewById(R.id.txtlastName);
		txtEmail = (TextView) findViewById(R.id.txtEmail);
		txtPhone = (TextView) findViewById(R.id.txtPhone);
		txtAddress = (TextView) findViewById(R.id.txtAddress);

		edtOldPwd = (EditText) findViewById(R.id.edtOldPwd);
		edtNewPwd = (EditText) findViewById(R.id.edtNewPwd);
		edtConfirmPwd = (EditText) findViewById(R.id.edtConfirmPwd);
		edtFirstName = (EditText) findViewById(R.id.edtFirstName);
		edtLastName = (EditText) findViewById(R.id.edtLastName);
		edtEmail1 = (EditText) findViewById(R.id.edtEmail1);
		edtPhone = (EditText) findViewById(R.id.edtPhone);
		edtAddress1 = (EditText) findViewById(R.id.edtAddress1);
		
         back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				setResult(10116);
//				MyAccount.this.finish();
				startActivity(new Intent(getApplicationContext(),	Launcher.class));
				
				
			}
		});

		imgUser = (ImageView) findViewById(R.id.imgReviewPerson);
		Picasso.with(MyAccount.this)
				.load(PreferenceUtils.getUserProfilePic().replace(" ", "%20"))
				.resize(100, 100).into(imgUser);

		try {

			strFName = Launcher.accountDetailsArray.getJSONObject(0)
					.getString("firstname").toString();
			if (strFName.equalsIgnoreCase("null")) {
				strFName = "";
			}
			edtFirstName.setText(Html.fromHtml(strFName));

			strLName = Launcher.accountDetailsArray.getJSONObject(0)
					.getString("lastname").toString();
			if (strLName.equalsIgnoreCase("null")) {
				strLName = "";
			}
			edtLastName.setText(Html.fromHtml(strLName));

			strEmail = Launcher.accountDetailsArray.getJSONObject(0)
					.getString("email").toString();

			edtEmail1.setText(Html.fromHtml(strEmail));

			strPhone = Launcher.accountDetailsArray.getJSONObject(0)
					.getString("phone").toString();
			if (strPhone.equalsIgnoreCase("null")) {
				strPhone = "";
			}
			edtPhone.setText(Html.fromHtml(strPhone));

			strAddress = Launcher.accountDetailsArray.getJSONObject(0)
					.getString("address").toString();
			if (strAddress.equalsIgnoreCase("null")) {
				strAddress = "";
			}
			edtAddress1.setText(Html.fromHtml(strAddress));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		txtMyAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtMyAccount.setBackgroundColor(Color.WHITE);
				txtChangePwd.setBackgroundColor(Color.parseColor("#ebebeb"));
				SvMyAccount.setVisibility(View.VISIBLE);
				LyChangePwd.setVisibility(View.INVISIBLE);
			}
		});

		txtChangePwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtChangePwd.setBackgroundColor(Color.WHITE);
				txtMyAccount.setBackgroundColor(Color.parseColor("#ebebeb"));
				SvMyAccount.setVisibility(View.INVISIBLE);
				LyChangePwd.setVisibility(View.VISIBLE);
			}
		});

		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strFName = edtFirstName.getText().toString();
				strLName = edtLastName.getText().toString();
				strPhone = edtPhone.getText().toString();
				strAddress = edtAddress1.getText().toString();

				if (TextUtils.isEmpty(strFName)) {
					showDialogMsg(MyAccount.this, "Please enter first name");
				} else if (TextUtils.isEmpty(strLName)) {
					showDialogMsg(MyAccount.this, "Please enter last name");
				} else if (TextUtils.isEmpty(strPhone)) {
					showDialogMsg(MyAccount.this, "Please enter phone number ");
				} else if (TextUtils.isEmpty(strAddress)) {
					showDialogMsg(MyAccount.this, "Please enter address");
				} else {
					if (strPhone.length() >= 10) {
						if (ConnectivityReceiver.checkInternetConnection(MyAccount.this)) {

							new UpdateAccount().execute();

						} else {
							ConnectivityReceiver.showCustomDialog(MyAccount.this);
						}
						
//						new UpdateAccount().execute();
					} else {
						showDialogMsg(v.getContext(),
								"Please enter valid phone number");
					}
				}
			}
		});

		btnUpdate1 = (Button) findViewById(R.id.btnUpdate1);
		btnUpdate1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strOldPwd = edtOldPwd.getText().toString();
				strNewPwd = edtNewPwd.getText().toString();
				strConfirmPwd = edtConfirmPwd.getText().toString();

				if (TextUtils.isEmpty(strOldPwd)) {
					showDialogMsg(MyAccount.this, "Please enter your password");
				} else if (TextUtils.isEmpty(strNewPwd)) {
					showDialogMsg(MyAccount.this, "Please enter new password");
				} else if (TextUtils.isEmpty(strConfirmPwd)) {
					showDialogMsg(MyAccount.this,
							"Please enter confirm password");
				} else if (strNewPwd.equals(strConfirmPwd)) { 

					if (ConnectivityReceiver
							.checkInternetConnection(MyAccount.this)) {
						new UpdatePassword().execute();
					} else {
						ConnectivityReceiver.showCustomDialog(MyAccount.this);
					}

				} else {
					showDialogMsg(MyAccount.this,
							"New password and confirm passwords are mismatch");
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		quickAction = new QuickAction(this, QuickAction.VERTICAL);
		if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
			quickAction.addActionItem(new ActionItem(Explore.ID_LOGIN, "Login",
					getResources().getDrawable(R.drawable.iocn_myaccount)));
		} else {

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
							startActivity(new Intent(MyAccount.this,Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(MyAccount.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(MyAccount.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(MyAccount.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(MyAccount.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(MyAccount.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(MyAccount.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							finish();
							startActivity(new Intent(MyAccount.this,
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 10116) {
			setResult(10116);
			finish();
		}
	}

	class UpdateAccount extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyAccount.this);
			pDialog.setMessage("We are updating your account details. Please wait ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "updateAccount/"
					+ PreferenceUtils.getUserSession()
					+ "/"
					+ strFName.replace(" ", "%20").trim()
					+ "/"
					+ strLName.replace(" ", "%20").trim()
					+ "/"
					+ strPhone
					+ "/"
					+ strAddress.replace(" ", "%20").replace("\n", "%20")
							.trim());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {

				showDialogMsg(MyAccount.this, "No data found from Server!!!");

			} else {

				try {

					JSONObject jObject = new JSONObject(result);

					String strErrorCode = jObject.optString("errorCode")
							.toString();
					String strErrorMsg = jObject.optString("errorMessage")
							.toString();

					if (Integer.parseInt(strErrorCode) == 0) {
						PreferenceUtils.setUserPhone1(strPhone);
						showDialogMsg2(strErrorMsg);
					} else {
						showDialogMsg(MyAccount.this, strErrorMsg);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}

	}

	private void showDialogMsg2(String strErrorMsg) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(strErrorMsg).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						MyAccount.this.finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	void showDialogMsg1(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MyAccount.this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						MyAccount.this.finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	class UpdatePassword extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyAccount.this);
			pDialog.setMessage("We are updating your password. Please wait ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "changePassword/" + PreferenceUtils.getUserSession()
					+ "/" + strOldPwd + "/" + strNewPwd + "/" + strConfirmPwd);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {

				showDialogMsg(MyAccount.this, "No data found from Server!!!");

			} else {

				try {

					JSONObject jObject = new JSONObject(result);

					String strErrorCode = jObject.optString("errorCode")
							.toString();
					String strErrorMsg = jObject.optString("errorMessage")
							.toString();

					if (Integer.parseInt(strErrorCode) == 0) {
						showDialogMsg(strErrorMsg);
					} else {
						showDialogMsg(MyAccount.this, strErrorMsg);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(),	Launcher.class));
	}
}
