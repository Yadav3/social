package com.etisbew.eatz.android;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.EditText;
import android.widget.ImageView;
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

public class SignUp extends BaseActivity implements fbsuccess {

	private ImageView ivBack = null;
	private TextView tvMember = null, tvSignup = null, tvGmailLogin = null;
	EditText edtName, edtEmail, edtPwd, etPhone;
	String strName, strEmail, strPwd, strPhone;

	public static LoginButton mLoginButton;
	public static Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;
	static fbsuccess fbSuccess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.register_activity);
		hideKeyboard();

		fbSuccess = this;
		mFacebook = new Facebook(Appconstants.APP_ID);

		ivBack = (ImageView) findViewById(R.id.back);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		//
		edtName = (EditText) findViewById(R.id.etName);

		etPhone = (EditText) findViewById(R.id.etMobile);

		edtPwd = (EditText) findViewById(R.id.etPassword);

		edtEmail = (EditText) findViewById(R.id.etEmail);

		mLoginButton = (LoginButton) findViewById(R.id.tvFBLogin);

		TextView tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
		tvForgotPassword
				.setText("By signing up, I agree to Terms & Conditions");
		tvForgotPassword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),
						TermsAndConditions.class));
			}
		});

		tvMember = (TextView) findViewById(R.id.tvMember);
		
		Spanned text = Html.fromHtml("Already an eatz member <b>Login</b> now");
		tvMember.setText(text);  
		tvMember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent inExplore = new Intent(v.getContext(), Login.class)
						.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				
				inExplore.putExtra("guest_flag", "1");
			
				startActivity(inExplore); 
			}
		});
		tvSignup = (TextView) findViewById(R.id.tvSignup);
		tvSignup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
 
				// TODO Auto-generated method stub

				strName = edtName.getText().toString().trim();
				strPhone = etPhone.getText().toString().trim();
				strEmail = edtEmail.getText().toString().trim();
				strPwd = edtPwd.getText().toString().trim();

				// Signup.this.finish();
				// Intent i = new Intent(getApplicationContext(),
				// MainScreen.class);
				// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(i);
				// // overridePendingTransition(R.anim.pull_in_right, 0);
				// overridePendingTransition(R.anim.pull_in_right,
				// R.anim.push_out_left);
 
				if (TextUtils.isEmpty(strName)) {

					showDialogMsg(v.getContext(), "Please enter your name");
				} else if (TextUtils.isEmpty(strPhone)) {
					showDialogMsg(v.getContext(),
							"Please enter your phone number");
				} else if (TextUtils.isEmpty(strEmail)) {
					showDialogMsg(v.getContext(), "Please enter email address");
				} else if (TextUtils.isEmpty(strPwd)) {
					showDialogMsg(v.getContext(), "Please enter password");
				} else {
					if (strPhone.length() >= 10) {
						Boolean bool = eMailValidation(strEmail);

						if (bool.equals(true)) {

							if (checkInternetConnection(SignUp.this)) {

								new SignUpTask().execute();
							} else {
								ConnectivityReceiver.showCustomDialog(SignUp.this);
							}

						} else {
							showDialogMsg(v.getContext(),
									"Please enter valid email address");
						}
					} else {
						showDialogMsg(v.getContext(),
								"Please enter valid phone number");
					}

				}

			}
		});

		tvGmailLogin = (TextView) findViewById(R.id.tvGmailLogin);
		tvGmailLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

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
			mUseFine = savedInstanceState.getBoolean(Login.KEY_FINE);
			mUseBoth = savedInstanceState.getBoolean(Login.KEY_BOTH);
		} else {
			mUseFine = false;
			mUseBoth = false;
		}

	}
	@Override
	public void onResume(){
		super.onResume();
		if(PreferenceUtils.getUserId().length()>5){
			if(PreferenceUtils.getUserId().equalsIgnoreCase("none")){
			SignUp.this.finish();
			}
		}else if(Appconstants.user_flag==2){
			SignUp.this.finish();
		}
	}

	boolean mUseFine;
	boolean mUseBoth;

	class SignUpTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(SignUp.this);
			pDialog.setMessage("Creating user account. Please wait ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return Utils.getJSONString(Appconstants.MAIN_HOST + "signUp/"
					+ strName + "/" + strEmail + "/" + strPwd + "/" + strPhone);
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
						showDialogMsg1("User account created successfully. Account confirmation email has been sent to your email address. Please activate it");
					} else {
						showDialogMsg(errmsg);
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
						SignUp.this.finish();
						/*
						 * Intent i = new
						 * Intent(getApplicationContext(),Launcher.class);
						 * i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						 * startActivity(i);
						 * overridePendingTransition(R.anim.pull_in_right
						 * ,R.anim.push_out_left);
						 */

					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

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

			Toast.makeText(getApplicationContext(), "Login Failed: ",
					Toast.LENGTH_LONG).show();
		}
	}

	// static String strFbFName, strFbLName, strFbId, strFbEmail;

	public class SampleRequestListener extends BaseRequestListener {

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			try {
				// process the response here: executed in background thread
				Log.d("Facebook-Example", "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				Login.strFbFName = json.getString("first_name");
				Login.strFbLName = json.getString("last_name");
				// strFbUserName = json.getString("username");
				Login.strFbId = json.getString("id");
				Login.strFbEmail = json.getString("email");

				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				SignUp.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// Toast.makeText(getApplicationContext(),
						// "Hello there, " + email + "!", 400).show();
						
//						new FbSignupTask(SignUp.this).execute();
						if (ConnectivityReceiver.checkInternetConnection(SignUp.this)) {

							new FbSignupTask(SignUp.this).execute();

						} else {
							ConnectivityReceiver.showCustomDialog(SignUp.this);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mFacebook.authorizeCallback(requestCode, resultCode, data);
	}

	public static class FbSignupTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		String strFbSession;
		Context con;

		public FbSignupTask(Context context) {
			con = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(con);
			pDialog.setMessage("Connecting Facebook With eatz. Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			return Utils.getJSONString(Appconstants.MAIN_HOST
					+ "facebookLogin/" + mFacebook.getAccessToken() + "/"
					+ Login.strFbEmail + "/" + Login.strFbFName + "/"
					+ Login.strFbLName + "/" + Login.strFbId);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
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
						PreferenceUtils.setUserSession(Login.strSession);
						Login.strUserId = jObject.optString("userId");
						Login.strUserName = jObject.optString("username");
						Login.strPointsEarned = jObject
								.optString("points_earned");
						Login.strPointsPending = jObject
								.optString("points_pending");
						Login.strProfileImg = jObject.optString("profilePic");

						fbSuccess.details(null);
						// con.startActivity(new Intent(con, Launcher.class));
						// overridePendingTransition(R.anim.stay,
						// R.anim.bottomtotop);
					}

				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		}
	}

	@Override
	public void details(detailsDO details) {
		// TODO Auto-generated method stub
		if (details == null)
			finish();
	}

}
