package com.etisbew.eatz.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.facebook.AsyncFacebookRunner;
import com.etisbew.eatz.facebook.BaseRequestListener;
import com.etisbew.eatz.facebook.Facebook;
import com.etisbew.eatz.facebook.FacebookError;
import com.etisbew.eatz.facebook.LoginButton;
import com.etisbew.eatz.facebook.SessionEvents;
import com.etisbew.eatz.facebook.SessionEvents.AuthListener;
import com.etisbew.eatz.facebook.SessionStore;
import com.etisbew.eatz.facebook.Util;
import com.etisbew.eatz.objects.detailsDO;
import com.etisbew.eatz.objects.fbsuccess;
import com.etisbew.eatz.utils.Utils;

public class Login extends BaseActivity implements fbsuccess {

	private ImageView ivBack = null;
	private TextView tvRegister = null, tvLogin = null,
			tvForgotPassword = null, tvGmailLogin = null, tvContinue_lable;
	private String strUser, strPwd;
	private EditText edtusername, edtPwd = null;

	public static LoginButton mLoginButton;
	public static Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;

	// private PendingAction pendingAction = PendingAction.NONE;
	EditText et_guest_name, et_guest_mail, et_guest_phone, et_guest_Password;
	CheckBox register_check;
	Button btn_guest_continue;
	LinearLayout guest_login, main_layout, guest_or;
	String g_username = "", g_email = "", g_phone = "", g_password = "";
	boolean flag = false;
	int show = 1;
	public static int redirect_flag = 0;
	ScrollView scrollView1;
	RelativeLayout main_top;
	// private GraphUser user;
	int guest_flag = 0;
	int register;

	// private enum PendingAction {
	// NONE, POST_PHOTO, POST_STATUS_UPDATE
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();

		if (extras != null) {
			guest_flag = Integer.parseInt(extras.getString("guest_flag"));

		}

		hideKeyboard();

		mFacebook = new Facebook(Appconstants.APP_ID);
		scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
		main_top = (RelativeLayout) findViewById(R.id.main_top);

		main_layout = (LinearLayout) findViewById(R.id.main_layout);
		guest_or = (LinearLayout) findViewById(R.id.guest_or);

		fbSuccess = this;

		ivBack = (ImageView) findViewById(R.id.back);
		tvRegister = (TextView) findViewById(R.id.tvRegister);

		edtusername = (EditText) findViewById(R.id.etEmail);

		edtPwd = (EditText) findViewById(R.id.etPassword);

		mLoginButton = (LoginButton) findViewById(R.id.tvFBLogin);

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		et_guest_name = (EditText) findViewById(R.id.et_guest_name);
		et_guest_mail = (EditText) findViewById(R.id.et_guest_mail);
		et_guest_phone = (EditText) findViewById(R.id.et_guest_phone);
		et_guest_Password = (EditText) findViewById(R.id.et_guest_Password);

		if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
			if (!PreferenceUtils.getUserName1().equalsIgnoreCase("none")) {
				et_guest_name.setText(PreferenceUtils.getUserName1());
			}
			if (!PreferenceUtils.getUserName1().equalsIgnoreCase("none")) {
				et_guest_mail.setText(PreferenceUtils.getUserEmail1());
			}
			if (!PreferenceUtils.getUserName1().equalsIgnoreCase("none")) {
				et_guest_phone.setText(PreferenceUtils.getUserPhone1());
			}

		}

		tvContinue_lable = (TextView) findViewById(R.id.tvContinue_lable);

		if (guest_flag == 1) {
			tvContinue_lable.setVisibility(View.GONE);
			guest_or.setVisibility(View.GONE);
		} else {
			tvContinue_lable.setVisibility(View.VISIBLE);
			guest_or.setVisibility(View.VISIBLE);
		}

		guest_login = (LinearLayout) findViewById(R.id.guest_login);

		register_check = (CheckBox) findViewById(R.id.register_check);
		btn_guest_continue = (Button) findViewById(R.id.btn_guest_continue);

		tvContinue_lable.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (show == 1) {
					guest_login.setVisibility(View.VISIBLE);
					scrollView1.post(new Runnable() {
						@Override
						public void run() {

							scrollView1.scrollTo(0, scrollView1.getBottom());
							main_layout.setVisibility(View.GONE);

						}
					});
					show = 2;
				} else {

					guest_login.setVisibility(View.GONE);
					main_layout.setVisibility(View.VISIBLE);
					show = 1;
				}
			}
		});

		register_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					flag = true;
					et_guest_Password.setVisibility(View.VISIBLE);
				} else {
					flag = false;
					et_guest_Password.setVisibility(View.GONE);
				}
			}
		});
		btn_guest_continue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				g_username = et_guest_name.getText().toString().trim();
				g_email = et_guest_mail.getText().toString().trim();
				g_phone = et_guest_phone.getText().toString().trim();
				g_password = et_guest_Password.getText().toString().trim();

				if (TextUtils.isEmpty(g_username) && TextUtils.isEmpty(g_email)
						&& TextUtils.isEmpty(g_phone)) {
					showDialogMsg("Please enter name,email and phone");
				} else {

					if (eMailValidation(g_email)) {
						if (g_phone.length() == 10) {
							if (flag == true) {
								if (g_password.length() > 0) {

									if (checkInternetConnection(Login.this)) {

										new GuestLogin().execute();
									} else {
										ConnectivityReceiver
												.showCustomDialog(Login.this);
									}
								} else {
									showDialogMsg("Please enter password");
								}
							} else {
								if (checkInternetConnection(Login.this)) {

									new GuestLogin().execute();
								} else {
									ConnectivityReceiver
											.showCustomDialog(Login.this);
								}
							}
						} else {
							showDialogMsg("Please enter valid phone");
						}
					} else {
						showDialogMsg("Please enter valid email address");
					}
				}
			}
		});

		tvRegister = (TextView) findViewById(R.id.tvRegister);

		Spanned text = Html
				.fromHtml("Don\'t have an eatz account? <b>Sign Up</b> here");
		tvRegister.setText(text);
		tvRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent inExplore = new Intent(v.getContext(), SignUp.class)
						.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(inExplore);
			}
		});

		tvLogin = (TextView) findViewById(R.id.tvLogin);
		tvLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				strUser = edtusername.getText().toString().trim();
				strPwd = edtPwd.getText().toString().trim();

				PreferenceUtils.setUserEmail1(edtusername.getText().toString());
				PreferenceUtils.getUserEmail1();

				if (TextUtils.isEmpty(strUser)) {
					showDialogMsg(Login.this, "Please enter email address");
				} else if (TextUtils.isEmpty(strPwd)) {
					showDialogMsg(Login.this, "Please enter password");
				} else {
					if (checkInternetConnection(Login.this)) {

						new LoginTask().execute();
					} else {
						ConnectivityReceiver.showCustomDialog(Login.this);
					}
				}
			}
		});

		tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
		tvForgotPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strUser = edtusername.getText().toString().trim();
				if (TextUtils.isEmpty(strUser)) {
					showDialogMsg("Please enter email id");
				} else {

					if (eMailValidation(strUser)) {

						if (checkInternetConnection(Login.this)) {

							new ForgotPwdTask().execute();
						} else {
							ConnectivityReceiver.showCustomDialog(Login.this);
						}
					} else {
						showDialogMsg("Please enter valid email address");
					}
				}
			}
		});

		// tvGmailLogin
		tvGmailLogin = (TextView) findViewById(R.id.tvGmailLogin);
		tvGmailLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Login.this.finish();
				startActivity(new Intent().setClass(v.getContext(),
						OAuthActivity.class));

			}
		});

		SessionStore.restore(mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener());
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		mLoginButton.init(this, mFacebook);

		if (Appconstants.APP_ID == null) {
			Util.showAlert(this, "Warning", "Facebook Applicaton ID must be "
					+ "specified before running this example: see Example.java");
		}

		if (savedInstanceState != null) {
			mUseFine = savedInstanceState.getBoolean(KEY_FINE);
			mUseBoth = savedInstanceState.getBoolean(KEY_BOTH);
		} else {
			mUseFine = false;
			mUseBoth = false;
		}

		// mLoginButton.setUserInfoChangedCallback(new
		// LoginButton.UserInfoChangedCallback() {
		// @Override
		// public void onUserInfoFetched(GraphUser user) {
		// Login.this.user = user;
		// updateUI();
		// // It's possible that we were waiting for this.user to be populated
		// in order to post a
		// // status update.
		// handlePendingAction();
		// }
		// });

	}

	// private void onSessionStateChange(Session session, SessionState state,
	// Exception exception) {
	// if (pendingAction != PendingAction.NONE &&
	// (exception instanceof FacebookOperationCanceledException ||
	// exception instanceof FacebookAuthorizationException)) {
	// new AlertDialog.Builder(Login.this)
	// .setTitle("Cancel")
	// .setMessage("Permission Granted")
	// .setPositiveButton("Ok", null)
	// .show();
	// pendingAction = PendingAction.NONE;
	// } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
	// handlePendingAction();
	// }
	// updateUI();
	// }

	// private void updateUI() {
	// Session session = Session.getActiveSession();
	// boolean enableButtons = (session != null && session.isOpened());
	//
	// // postStatusUpdateButton.setEnabled(enableButtons ||
	// canPresentShareDialog);
	// // postPhotoButton.setEnabled(enableButtons ||
	// canPresentShareDialogWithPhotos);
	// // pickFriendsButton.setEnabled(enableButtons);
	// // pickPlaceButton.setEnabled(enableButtons);
	//
	// if (enableButtons && user != null) {
	// // profilePictureView.setProfileId(user.getId());
	// // greeting.setText(getString(R.string.hello_user, user.getFirstName()));
	// } else {
	// // profilePictureView.setProfileId(null);
	// // greeting.setText(null);
	// }
	// }

	// @SuppressWarnings("incomplete-switch")
	// private void handlePendingAction() {
	// PendingAction previouslyPendingAction = pendingAction;
	// // These actions may re-set pendingAction if they are still pending, but
	// we assume they
	// // will succeed.
	// pendingAction = PendingAction.NONE;
	//
	// switch (previouslyPendingAction) {
	// case POST_PHOTO:
	// // postPhoto();
	// break;
	// case POST_STATUS_UPDATE:
	// // postStatusUpdate();
	// break;
	// }
	// }
	/*
	 * public void onResume(){ super.onResume();
	 * if(PreferenceUtils.getUserId().length()>0){ Login.this.finish(); } }
	 * public void onResume(){ super.onResume();
	 * if(PreferenceUtils.getUserId().length()>5){
	 * if(PreferenceUtils.getUserId().equalsIgnoreCase("none")){
	 * Login.this.finish(); } }else if(Appconstants.user_flag==2){
	 * Login.this.finish(); } }
	 */
	public class SampleAuthListener implements AuthListener {

		@Override
		public void onAuthSucceed() {

			Toast.makeText(getApplicationContext(), "You have logged in! ",
					Toast.LENGTH_LONG).show();
			mAsyncRunner.request("me", new SampleRequestListener());
			// new AsyncFacebookRunner(mFacebook).request("me",
			// new SampleRequestListener());

		}

		@Override
		public void onAuthFail(String error) {

			Toast.makeText(getApplicationContext(), "Login Failed:",
					Toast.LENGTH_LONG).show();
		}
	}

	static String strFbFName, strFbLName, strFbId, strFbEmail;

	public class SampleRequestListener extends BaseRequestListener {

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			try {
				JSONObject json = Util.parseJson(response);
				strFbFName = json.getString("first_name");
				strFbLName = json.getString("last_name");
				// strFbUserName = json.getString("username");
				strFbId = json.getString("id");
				strFbEmail = json.getString("email");

				strFBFirstName = strFbFName;
				PreferenceUtils.setUserFirstName(strFBFirstName);

				strFBLastName = strFbLName;
				PreferenceUtils.setUserLastName(strFBLastName);

				// for (int i = 0; i < profile.getEmails().size(); i++) {
				strFBEmailId = strFbEmail;
				PreferenceUtils.setUserEmail1(strFBEmailId);
				PreferenceUtils.setUserAddress1("");
				// }

				Login.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (checkInternetConnection(Login.this)) {
							new FbLoginTask(Login.this).execute();
						} else {
							ConnectivityReceiver.showCustomDialog(Login.this);
						}
					}
				});
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}

	public static class FbLoginTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		String strFbSession;
		Context con;

		public FbLoginTask(Context context) {
			con = context;
			pDialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog.setMessage("Connecting Facebook With eatz. Please wait...");
			pDialog.setCancelable(false);

			if (!((Activity) con).isFinishing()) {
				// show dialog
				pDialog.show();
			}

			// if (pDialog != null && !pDialog.isShowing()) {
			// pDialog.show();
			// }
		}

		@Override
		protected String doInBackground(String... params) {

			return Utils.getJSONString(Appconstants.MAIN_HOST
					+ "facebookLogin/" + mFacebook.getAccessToken() + "/"
					+ strFbEmail + "/" + strFbFName + "/" + strFbLName + "/"
					+ strFbId);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (!((Activity) con).isFinishing()) {
				if (null != pDialog && pDialog.isShowing()) {
					pDialog.dismiss();
				}
			}
			if (null == result || result.length() == 0) {
				// Login.this.finish();
			} else {

				try {

					JSONObject jObject = new JSONObject(result);

					if (result.contains("errorMessage")) {
						@SuppressWarnings("unused")
						String errmsg = jObject.optString("errorMessage");
					} else {

						Login.strSession = jObject.optString("sessionId");
						PreferenceUtils.setUserSession(strSession);
						Login.strUserId = jObject.optString("userId");
						PreferenceUtils.setUserId(strUserId);
						Login.strUserName = jObject.optString("username");
						PreferenceUtils.setUserName1(strUserName);
						Login.strPointsEarned = jObject
								.optString("points_earned");
						PreferenceUtils.setUserPoints(strPointsEarned);
						Login.strPointsPending = jObject
								.optString("points_pending");
						PreferenceUtils.setUserPointsPending(strPointsPending);
						Login.strProfileImg = jObject.optString("profilePic");
						PreferenceUtils.setUserProfilePic(Login.strProfileImg);
						if (!jObject.isNull("address")) {
							strUserAddress = jObject.optString("address");
							PreferenceUtils.setUserAddress1(strUserAddress);
						} else {
							PreferenceUtils.setUserAddress1("");
						}
						if (!jObject.isNull("phone")) {
							PreferenceUtils.setUserPhone1(jObject
									.optString("phone"));
						}

						fbSuccess.details(null);

						if (!((Activity) con).isFinishing()) {

							if (redirect_flag == 1) {
								redirect_flag = 0;
								((Activity) con).finish();
								con.startActivity(new Intent(con,
										Launcher.class));
							} else {
								((Activity) con).finish();
							}
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		}
	}

	static fbsuccess fbSuccess;
	// Keys for maintaining UI states after rotation.
	static final String KEY_FINE = "use_fine";
	static final String KEY_BOTH = "use_both";
	boolean mUseFine;
	boolean mUseBoth;

	public static String strSession = "null", strUserId, strPointsEarned = "",
			strUserPhone, strUserAddress, strUserEmail, strPointsPending = "",
			strUserName = "", strProfileImg = "", strLocation = "",
			strFirstName = "", strLastName = "", strEmailId = "",
			strFBFirstName = "", strFBLastName = "", strFBEmailId = "";
	public static JSONObject jObject;

	class LoginTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Signing in...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return Utils.getJSONString(Appconstants.MAIN_HOST + "userLogin/"
					+ strUser + "/" + strPwd);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Log.e("Result : ", result);
			// Toast.makeText(getApplicationContext(), result, 200).show();
			if (null == result || result.length() == 0) {
				if (null != pDialog && pDialog.isShowing()) {
					pDialog.dismiss();
				}
				showDialogMsg(Login.this, "No data found from Server!!!");
				// Login.this.finish();
			} else {

				try {

					JSONObject jObject = new JSONObject(result);

					if (result.contains("errorMessage")) {
						if (null != pDialog && pDialog.isShowing()) {
							pDialog.dismiss();
						}
						String errmsg = jObject.optString("errorMessage");
						showDialogMsg(Login.this, errmsg);
					} else {

						strSession = jObject.optString("sessionId");
						PreferenceUtils.setUserSession(strSession);

						strUserId = jObject.optString("userId");
						PreferenceUtils.setUserId(strUserId);

						strUserName = jObject.optString("username");
						PreferenceUtils.setUserName1(strUserName);

						// strFirstName= jObject.optString("firstname");
						PreferenceUtils.setUserFirstName(jObject
								.optString("firstname"));

						// strUserName = jObject.optString("lastname");
						PreferenceUtils.setUserLastName(jObject
								.optString("lastname"));

						// strUserName = jObject.optString("username");
						PreferenceUtils.setUserEmail1(jObject
								.optString("email"));

						// strUserEmail = jObject.optString("email");
						// PreferenceUtils.setUserName1(strUserEmail);

						strPointsEarned = jObject.optString("points_earned");
						PreferenceUtils.setUserPoints(strPointsEarned);

						strPointsPending = jObject.optString("points_pending");
						PreferenceUtils.setUserPointsPending(strPointsPending);
						strProfileImg = jObject.optString("profilePic");
						PreferenceUtils.setUserProfilePic(strProfileImg);

						strUserPhone = jObject.optString("phone");
						PreferenceUtils.setUserPhone1(strUserPhone);

						if (!jObject.isNull("phone")) {
							PreferenceUtils.setUserPhone1(jObject
									.optString("phone"));
						}
						if (!jObject.isNull("address")) {
							strUserAddress = jObject.optString("address");
							PreferenceUtils.setUserAddress1(strUserAddress);
						} else {
							PreferenceUtils.setUserAddress1("");
						}
						PreferenceUtils.setUserEmail1(edtusername.getText()
								.toString());
						PreferenceUtils.getUserEmail1();

						// Login.this.finish();
						if (null != pDialog && pDialog.isShowing()) {
							pDialog.dismiss();
						}

						if (redirect_flag == 1) {
							redirect_flag = 0;
							finish();
							startActivity(new Intent(getApplicationContext(),
									Launcher.class));
						} else {
							finish();
						}
						overridePendingTransition(R.anim.stay,
								R.anim.toptobottom);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}

	class ForgotPwdTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Sending password reset details to your registered mail. Please wait ....");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return Utils.getJSONString(Appconstants.MAIN_HOST
					+ "forgotPassword/" + strUser);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg("No data found from Server!!!");
			} else {
				try {
					JSONObject jObject = new JSONObject(result);
					String strErrorCode = jObject.optString("errorCode");
					String errmsg = jObject.optString("errorMessage");
					if (Integer.parseInt(strErrorCode) == 0) {
						showDialogMsg1(errmsg);
					} else {
						showDialogMsg(errmsg);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}

	class GuestLogin extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Login.this);
			if (flag == true) {
				pDialog.setMessage("Please wait ...");
			} else {
				pDialog.setMessage("Loading ....");
			}
			pDialog.setCancelable(false);
			pDialog.show();
		}

		// http://dev.eatz.com/WebServices/eatzGuest/nagababu/nagababu.bommidi@etisbew.com/8977898619/nagababu
		@Override
		protected String doInBackground(String... params) {
			String url=Appconstants.MAIN_HOST + "eatzGuest/"
					+ g_username + "/" + g_email + "/" + g_phone + "/"
					+ g_password;
			System.out.println("url"+url);
			return Utils.getJSONString(url);

		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			// Log.e("Result : ", result);
			// Toast.makeText(getApplicationContext(), result, 200).show();
			if (null == result || result.length() == 0) {
				if (null != pDialog && pDialog.isShowing()) {
					pDialog.dismiss();
				}
				showDialogMsg(Login.this, "No data found from Server!!!");
				// Login.this.finish();
			} else {

				try {

					JSONObject jObject = new JSONObject(result);

					if (result.contains("errorMessage")) {
						if (null != pDialog && pDialog.isShowing()) {
							pDialog.dismiss();
						}
						String errmsg = jObject.optString("errorMessage");
						showDialogMsg(Login.this, errmsg);
					} else {

						strSession = jObject.optString("sessionId");
						Appconstants.user_flag = 2;
						Appconstants.sessionId = strSession;
						// PreferenceUtils.setUserSession(strSession);

						strUserId = jObject.optString("userId");
						Appconstants.userId = strUserId;
						// PreferenceUtils.setUserId(strUserId);

						strUserName = jObject.optString("firstname");
						Appconstants.firstname = strUserName;

						Appconstants.phone = jObject.optString("phone");
						Appconstants.email_ = jObject.optString("email");

						PreferenceUtils.setUserName1(jObject
								.optString("firstname"));
						if(jObject.has("is_register")){
							register=Integer.parseInt(jObject.optString("is_register"));
						}

						// strFirstName= jObject.optString("firstname");
						// PreferenceUtils.setUserFirstName(jObject/.optString("firstname"));

						// strUserName = jObject.optString("lastname");
						// PreferenceUtils.setUserLastName("");

						// strUserName = jObject.optString("username");
						PreferenceUtils.setUserEmail1(jObject
								.optString("email"));
						PreferenceUtils.setUserPhone1(jObject
								.optString("phone"));
						/*
						 * if (!jObject.isNull("address")) { strUserAddress =
						 * jObject.optString("address");
						 * PreferenceUtils.setUserAddress1(strUserAddress);
						 * }else{ PreferenceUtils.setUserAddress1(""); } if
						 * (!jObject.isNull("phone")) {
						 * PreferenceUtils.setUserPhone1(jObject
						 * .optString("phone")); }
						 */
						// strUserEmail = jObject.optString("email");
						// PreferenceUtils.setUserName1(strUserEmail);

						// Login.this.finish();
						if (null != pDialog && pDialog.isShowing()) {
							pDialog.dismiss();
						}
						// startActivity(new
						// Intent(getApplicationContext(),Launcher.class));
						// if (Integer.parseInt(strErrorCode) == 0) {
						if (flag == true) {
							// showDialogMsg1("User account created successfully. Account confirmation email has been sent to your email address. Please activate it");
							AlertDialog alertDialog = new AlertDialog.Builder(
									Login.this).create();

							// Setting Dialog Title
							// alertDialog.setTitle("Alert Dialog");

							// Setting Dialog Message
							if(register==1){
								alertDialog
								.setMessage("You're already an eatz registered member with this email id! ");
						
							}else{
							alertDialog
									.setMessage("User account created successfully. Account confirmation email has been sent to your email address. Please activate it");
							}
							// Setting Icon to Dialog
							// alertDialog.setIcon(R.drawable.tick);

							// Setting OK Button
							alertDialog.setButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// Write your code here to execute
											// after dialog closed
											finish();
											overridePendingTransition(
													R.anim.stay,
													R.anim.toptobottom);
										}
									});
							/*
							 * alertDialog.setButton("Cancel", new
							 * DialogInterface.OnClickListener() { public void
							 * onClick(DialogInterface dialog, int which) { //
							 * Write your code here to execute after dialog
							 * closed finish();
							 * overridePendingTransition(R.anim.stay,
							 * R.anim.toptobottom); } });
							 */

							// Showing Alert Message
							alertDialog.show();

						} else {
							finish();
							overridePendingTransition(R.anim.stay,
									R.anim.toptobottom);
						}

					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}
	}

	private void showDialogMsg1(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						Login.this.finish();
						startActivity(new Intent(getApplicationContext(),
								Launcher.class)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						overridePendingTransition(R.anim.pull_in_right,
								R.anim.push_out_left);
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mFacebook.authorizeCallback(requestCode, resultCode, data);
	}

	@Override
	public void details(detailsDO details) {
		if (details == null) {
			finish();
		}
	}

	// class FBLoginTask extends AsyncTask<String, Void, String> {
	//
	// ProgressDialog pDialog;
	// String strAccesstoken, strUsername, strLastname, strGoogleUID;
	//
	// public FBLoginTask(String accessToken, String personName,
	// String Lastname, String strId) {
	// strAccesstoken = accessToken;
	// strUsername = personName;
	// strLastname = Lastname;
	// strGoogleUID = strId;
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	//
	// pDialog = new ProgressDialog(Login.this);
	// pDialog.setMessage("Connecting Facebook With Localsecrets. Please wait...");
	// pDialog.setCancelable(false);
	// pDialog.show();
	// }
	//
	// @Override
	// protected String doInBackground(String... params) {
	//
	// return Utils.getJSONString(Appconstants.MAIN_HOST
	// + "facebookLogin/" + strAccesstoken + "/"
	// + strUserEmail + "/" + strUsername + "/"
	// + strLastname + "/" + strGoogleUID);
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
	// // Login.this.finish();
	// } else {
	//
	// // try {
	// //
	// // JSONObject jObject = new JSONObject(result);
	// //
	// // Log.e("Response : ", result);
	// //
	// // if (result.contains("errorMessage")) {
	// // @SuppressWarnings("unused")
	// // String errmsg = jObject.optString("errorMessage");
	// // } else {
	// //
	// // Login.strSession = jObject.optString("sessionId");
	// // PreferenceUtils.setUserSession(Login.strSession);
	// // Login.strUserId = jObject.optString("userId");
	// // PreferenceUtils.setUserId(Login.strUserId);
	// // Login.strUserName = jObject.optString("username");
	// // PreferenceUtils.setUserName1(Login.strUserName);
	// // Login.strPointsEarned = jObject
	// // .optString("points_earned");
	// // PreferenceUtils.setUserPoints(Login.strPointsEarned);
	// // Login.strPointsPending = jObject
	// // .optString("points_pending");
	// // Login.strProfileImg = jObject.optString("profilePic");
	// // PreferenceUtils.setUserProfilePic(Login.strProfileImg);
	// //
	// // OAuthActivity.this.finish();
	// // startActivity(new Intent(getApplicationContext(),
	// // Launcher.class));
	// // // con.startActivity(new Intent(con, Launcher.class));
	// // // overridePendingTransition(R.anim.stay,
	// // // R.anim.bottomtotop);
	// // }
	// //
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // // TODO: handle exception
	// // }
	// }
	// }
	// }
}
