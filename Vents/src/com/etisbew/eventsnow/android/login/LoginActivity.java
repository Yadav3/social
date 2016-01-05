package com.etisbew.eventsnow.android.login;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.MainActivity;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.facebook.AsyncFacebookRunner;
import com.etisbew.eventsnow.android.facebook.BaseRequestListener;
import com.etisbew.eventsnow.android.facebook.Facebook;
import com.etisbew.eventsnow.android.facebook.FacebookError;
import com.etisbew.eventsnow.android.facebook.LoginButton;
import com.etisbew.eventsnow.android.facebook.SessionEvents;
import com.etisbew.eventsnow.android.facebook.SessionEvents.AuthListener;
import com.etisbew.eventsnow.android.facebook.SessionStore;
import com.etisbew.eventsnow.android.facebook.Util;
import com.etisbew.eventsnow.android.forgot.ForgotPassword;
import com.etisbew.eventsnow.android.myfavorites.MyFavorites;
import com.etisbew.eventsnow.android.ordernow.OrderNow;
import com.etisbew.eventsnow.android.signup.Signup;
import com.etisbew.eventsnow.android.util.Utility;

public class LoginActivity extends Activity implements OnClickListener {
	Button buyticket, signin, gmailLogin;
	TextView back, forgot, new_here, or, event_title;
	EditText email, pwd;
	EventBean event;
	Utility util;
	String url, fb_url;
	String target_activity="MainActivity";

	String user_image, user_name, email1, phone;
	String error_message;
	RelativeLayout menuLayout;

	ArrayList<Integer> id_list;
	public static Facebook mFacebook;
	LoginButton facebook;
	int user_id;
	String fb_access_token, fb_user_name, fb_email;
	private AsyncFacebookRunner mAsyncRunner;
	String address, state, city;
	int state_id, city_id;

	SharedPreferences pref;
	Editor editor;
	public static final String MyPREFERENCES = "MyPrefs";
	int pref_user_id;

	String pref_user_name;
	String pref_user_image;
	String pref_email, pref_phone;
	String pref_address, state_name, city_name;
	Map<Integer, String> details;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {

			target_activity = extras.getString("target");

			details = (HashMap<Integer, String>) iin
					.getSerializableExtra("details");
		}

		
		pref = getApplicationContext().getSharedPreferences("MyPref",
				MODE_PRIVATE);
		editor = pref.edit();

		event = (EventBean) getApplicationContext();
		if(event.getUserId()>0){
			editor.clear();
			editor.commit();
			
			event.setUserId(0);
			event.setUserName("");
			event.setUser_image("");
			event.setEmail("");
			event.setPhone("");
		}
		util = new Utility(LoginActivity.this);
		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		forgot = (TextView) findViewById(R.id.forgot);
		new_here = (TextView) findViewById(R.id.new_here);
		or = (TextView) findViewById(R.id.or);
		menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
		signin = (Button) findViewById(R.id.signin);
		facebook = (LoginButton) findViewById(R.id.facebook);
		gmailLogin = (Button) findViewById(R.id.gmailLogin);
		email = (EditText) findViewById(R.id.email);
		pwd = (EditText) findViewById(R.id.pwd);

		event_title.setTypeface(event.getTextBold());
		signin.setTypeface(event.getTextBold());
		forgot.setTypeface(event.getTextBold());
		new_here.setTypeface(event.getTextBold());
		or.setTypeface(event.getTextNormal());
		email.setTypeface(event.getTextNormal());
		pwd.setTypeface(event.getTextNormal());
		facebook.setTypeface(event.getTextBold());
		gmailLogin.setTypeface(event.getTextBold());

		back.setOnClickListener(this);
		event_title.setOnClickListener(this);
		signin.setOnClickListener(this);
		forgot.setOnClickListener(this);
		menuLayout.setOnClickListener(this);

		new_here.setOnClickListener(this);
		gmailLogin.setOnClickListener(this);
		id_list = new ArrayList<Integer>();
		mFacebook = new Facebook("375235435981067");
		// 100624390079389 375235435981067

		SessionStore.restore(mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener());
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		facebook.init(this, mFacebook);
	 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title
				|| v.getId() == R.id.menuLayout) {
			
			finish();
			
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
		} else if (v.getId() == R.id.signin) {
			if (email.getText().toString().length() >= 0
					&& pwd.getText().toString().length() >= 0) {

				if (!util.validEmail(email.getText().toString())) {
					util.dialogExample1("Please enter a valid email address");

				} else {
					url = util.setLogin();
					InputMethodManager inputManager = (InputMethodManager) LoginActivity.this
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(pwd.getWindowToken(),
							0);
					Boolean status = util.IsNetConnected(LoginActivity.this);
					if (status) {
						new LoginUser().execute();

					} else {
						util.showAlertNoInternetService(LoginActivity.this);
					}
				}

			} else {

			}
		} else if (v.getId() == R.id.forgot) {
			startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
		} else if (v.getId() == R.id.new_here) {
			startActivity(new Intent(LoginActivity.this, Signup.class));
			LoginActivity.this.finish();
		} else if (v.getId() == R.id.gmailLogin) { 
			Intent in = new Intent().setClass(v.getContext(),
					OAuthActivity.class);
			in.putExtra("target", target_activity);
			in.putExtra("details", (HashMap) details);
			startActivity(in);
			LoginActivity.this.finish();
 
		}
		overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
	}

	@Override 
	public void onBackPressed() {
		if(event.getUserId()>0){
			editor.clear();
			editor.commit();
			
			event.setUserId(0);
			event.setUserName("");
			event.setUser_image("");
			event.setEmail("");
			event.setPhone("");
		}
		finish();
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

	public class SampleAuthListener implements AuthListener {

		@Override
		public void onAuthSucceed() {

			mAsyncRunner.request("me", new SampleRequestListener());
	
		}

		@Override
		public void onAuthFail(String error) {

			Toast.makeText(getApplicationContext(), "Login Failed:",
					Toast.LENGTH_LONG).show();

		}
	}

	public class SampleRequestListener extends BaseRequestListener {

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			try {
				String[] convert_ = response.split("::");

				JSONObject json = Util.parseJson(convert_[0]);
				fb_access_token = convert_[1];
				fb_user_name = json.getString("name");
				fb_email = json.getString("email");
				if (fb_access_token.length() > 0 && fb_user_name.length() > 0
						&& fb_email.length() > 0) {
					// FbCall();
					Boolean status = util.IsNetConnected(LoginActivity.this);
					if (status) {

						LoginActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								new FbLogin().execute();
							}
						});

					} else {
						util.showAlertNoInternetService(LoginActivity.this);
					}
				}

	
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}

	
	private class LoginUser extends AsyncTask<String, Void, String> {
		private static final String RESULT = "Result";
		private static final String LOGINRESPONSE = "LoginResponse";
		private static final String ID = "ID";
		private static final String USERNAME = "username";
		private static final String PROFILEPIC = "Profilepic";
		private static final String EMAIL = "Email";
		private static final String PHONE = "Phone";
		private static final String CITY = "City";
		private static final String STATE = "State";
		private static final String ADDRESS = "Address";

		private static final String STATE_ID = "State_id";
		private static final String CITY_ID = "City_id";

		private static final String ERRORMESSAGE = "ErrorMessage";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(LoginActivity.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(util.setLogin());
			String result = null;
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("username", email
						.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("password", pwd
						.getText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, HTTP.UTF_8).trim();

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("result1" + result);
			if (null == result || result.length() == 0
					|| TextUtils.isEmpty(result)) {

				util.dialogExample();

			} else {
				try {
					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser parser = factory.newPullParser();

					parser.setInput(new StringReader(result));

					int eventType = parser.getEventType();

					while (eventType != XmlPullParser.END_DOCUMENT) {
						String name = null;
						switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(LOGINRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									user_id = Integer.parseInt(parser
											.nextText());
								} else if (name.equalsIgnoreCase(USERNAME)) {
									user_name = parser.nextText();
								} else if (name.equalsIgnoreCase(PROFILEPIC)) {
									user_image = parser.nextText();
								} else if (name.equalsIgnoreCase(EMAIL)) {
									email1 = parser.nextText();
								} else if (name.equalsIgnoreCase(PHONE)) {
									phone = parser.nextText();
								} else if (name.equalsIgnoreCase(ERRORMESSAGE)) {
									error_message = parser.nextText();
								} else if (name.equalsIgnoreCase(CITY)) {
									city = parser.nextText();
								} else if (name.equalsIgnoreCase(STATE)) {
									state = parser.nextText();
								} else if (name.equalsIgnoreCase(ADDRESS)) {
									address = parser.nextText();
								} else if (name.equalsIgnoreCase(CITY_ID)) {
									city_id = Integer.parseInt(parser
											.nextText());
								} else if (name.equalsIgnoreCase(STATE_ID)) {
									state_id = Integer.parseInt(parser
											.nextText());
								}
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(LOGINRESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				update();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	private class FbLogin extends AsyncTask<String, Void, String> {
		private static final String RESULT = "Result";
		private static final String FBRESPONSE = "FbResponse";
		private static final String ID = "ID";
		private static final String USERNAME = "username";
		private static final String PROFILEPIC = "Profilepic";
		private static final String EMAIL = "Email";
		private static final String PHONE = "Phone";
		private static final String ERRORMESSAGE = "ErrorMessage";
		private static final String CITY = "City";
		private static final String STATE = "State";
		private static final String ADDRESS = "Address";

		private static final String STATE_ID = "State_id";
		private static final String CITY_ID = "City_id";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(LoginActivity.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			if (!((Activity) LoginActivity.this).isFinishing()) {
				// show dialog
				progressDialog.show();
			}

		}

		@Override
		protected String doInBackground(String... args) {
			fb_url = util.setFbEmail(fb_access_token,
					fb_user_name.replace(" ", "%20"), fb_email);
			return util.getXmlFromUrl(fb_url);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (!((Activity) LoginActivity.this).isFinishing()) {
				if (null != progressDialog && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
			System.out.println("result1" + result);
			if (null == result || result.length() == 0
					|| TextUtils.isEmpty(result)) {

				util.dialogExample();

			} else {
				try {
					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser parser = factory.newPullParser();

					parser.setInput(new StringReader(result));

					int eventType = parser.getEventType();

					while (eventType != XmlPullParser.END_DOCUMENT) {
						String name = null;
						switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(FBRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									user_id = Integer.parseInt(parser
											.nextText());
								} else if (name.equalsIgnoreCase(USERNAME)) {
									user_name = parser.nextText();
								} else if (name.equalsIgnoreCase(PROFILEPIC)) {
									user_image = parser.nextText();
								} else if (name.equalsIgnoreCase(EMAIL)) {
									email1 = parser.nextText();
								} else if (name.equalsIgnoreCase(PHONE)) {
									phone = parser.nextText();
								} else if (name.equalsIgnoreCase(ERRORMESSAGE)) {
									error_message = parser.nextText();
								} else if (name.equalsIgnoreCase(CITY)) {
									city = parser.nextText();
								} else if (name.equalsIgnoreCase(STATE)) {
									state = parser.nextText();
								} else if (name.equalsIgnoreCase(ADDRESS)) {
									address = parser.nextText();
								} else if (name.equalsIgnoreCase(CITY_ID)) {
									city_id = Integer.parseInt(parser
											.nextText());
								} else if (name.equalsIgnoreCase(STATE_ID)) {
									state_id = Integer.parseInt(parser
											.nextText());
								}
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(FBRESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				update();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	public void update() {
		if (user_id > 0) {
			editor.putInt("state_id", state_id);
			editor.putInt("user_id", user_id);
			editor.putInt("city_id", city_id);

			editor.putString("user_name", user_name);
			editor.putString("phone", phone);
			editor.putString("user_image", user_image);
			editor.putString("email", email1);

			editor.putString("address", address);
			editor.putString("state", state);
			editor.putString("city", city);
			editor.commit();
			
			getIntent().removeExtra("target");
			if (TextUtils.isEmpty(target_activity)
					|| target_activity.equalsIgnoreCase("null")) {
 
				if (!((Activity) LoginActivity.this).isFinishing()) {
					LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
				}
				overridePendingTransition(R.anim.trans_left_in,R.anim.trans_left_out);
			} else if (target_activity.equalsIgnoreCase("MainActivity")) {
				if (!((Activity) LoginActivity.this).isFinishing()) {
					LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
				}
				overridePendingTransition(R.anim.trans_left_in,R.anim.trans_left_out);
			}	 else if (target_activity.equalsIgnoreCase("MyAccount")) {
						if (!((Activity) LoginActivity.this).isFinishing()) {
							Intent in=new Intent(LoginActivity.this, MainActivity.class);
							in.putExtra("frompayment", "MyAccount");
							LoginActivity.this.startActivity(in);
						}
						overridePendingTransition(R.anim.trans_left_in,
								R.anim.trans_left_out);
			}	 else if (target_activity.equalsIgnoreCase("MyTickets")) {
								if (!((Activity) LoginActivity.this).isFinishing()) {
									Intent in=new Intent(LoginActivity.this, MainActivity.class);
									in.putExtra("frompayment", "MyTickets");
									LoginActivity.this.startActivity(in);
								}
								overridePendingTransition(R.anim.trans_left_in,
										R.anim.trans_left_out);
			}	else if (target_activity.equalsIgnoreCase("Details")) {
					if (!((Activity) LoginActivity.this).isFinishing()) {
						LoginActivity.this.finish();
					}
					overridePendingTransition(R.anim.trans_left_in,
							R.anim.trans_left_out);
			} else if (target_activity.equalsIgnoreCase("MyFavorites")) {
				if (!((Activity) LoginActivity.this).isFinishing()) {
					LoginActivity.this.startActivity(new Intent(LoginActivity.this, MyFavorites.class));
				}
				overridePendingTransition(R.anim.trans_left_in,
						R.anim.trans_left_out);
			} else if (target_activity.equalsIgnoreCase("OrderNow")) {
				try {
					event.setDetails(details);
				} catch (Exception e) {

				}
				if (!((Activity) LoginActivity.this).isFinishing()) {
					LoginActivity.this.startActivity(new Intent(LoginActivity.this, OrderNow.class));
				}
				overridePendingTransition(R.anim.trans_left_in,
						R.anim.trans_left_out);
			}

	
		} else {
			util.dialogExample1(error_message);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

}