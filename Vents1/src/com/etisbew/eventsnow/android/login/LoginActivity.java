package com.etisbew.eventsnow.android.login;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.etisbew.eventsnow.android.signup.Signup;
import com.etisbew.eventsnow.android.util.Utility;

public class LoginActivity extends Activity implements OnClickListener {
	Button buyticket, signin,gmailLogin;
	TextView back, forgot, new_here, or, event_title;
	EditText email, pwd;
	EventBean event;
	Utility util;
	String url,fb_url;
	String target_activity;
	
	String user_image, user_name,email1,phone;
	String error_message;
	RelativeLayout menuLayout;
	
	ArrayList<Integer> id_list;
	public static Facebook mFacebook;
	LoginButton facebook;
	int user_id;
	String fb_access_token,fb_user_name,fb_email;
	private AsyncFacebookRunner mAsyncRunner;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {
			target_activity = extras.getString("activity");

		}
		event = (EventBean) getApplicationContext();
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
		mFacebook = new Facebook("100624390079389");
		
		
			
	
		SessionStore.restore(mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener());
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		if(event.getUserId()>0){
		util.dialogExample1("already logged in");
		}else{
			facebook.init(this, mFacebook);
		}  
	} 
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title
				|| v.getId() == R.id.menuLayout) {
			LoginActivity.this.finish();
			startActivity(new Intent(LoginActivity.this,MainActivity.class)); 
			overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
		} else if (v.getId() == R.id.signin) {
			if (email.getText().toString().length() >= 0
					&& pwd.getText().toString().length() >= 0) {
				
				if (!util.validEmail(email.getText().toString())) {
					util.dialogExample1("Please Enter valid Email");

				} else {
					url = util.setLogin();
						new LoginUser().execute();
				}
			 
			} else {   
  
			}
		} else if (v.getId() == R.id.forgot) {
			startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
		} else if (v.getId() == R.id.new_here) {
					startActivity(new Intent(LoginActivity.this, Signup.class));
					LoginActivity.this.finish(); 
		}else if (v.getId() == R.id.gmailLogin) {
				 Intent in =new Intent().setClass(v.getContext(),OAuthActivity.class);
			 in.putExtra("activity", target_activity);
			 startActivity(in);
			 
			}
		overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
	}

	@Override
	public void onBackPressed() {
		LoginActivity.this.finish();
		startActivity(new Intent(LoginActivity.this,MainActivity.class)); 
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}
	public class SampleAuthListener implements AuthListener {

		@Override
		public void onAuthSucceed() {

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
	public class SampleRequestListener extends BaseRequestListener {

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			try {
				String[] convert_=response.split("::");
						
				JSONObject json = Util.parseJson(convert_[0]);
					System.out.println("json "+convert_[1]+convert_[0]);
				fb_access_token=convert_[1]; 
				fb_user_name=json.getString("username");
				fb_email=json.getString("email");
				if(fb_access_token.length()>0 && fb_user_name.length()>0 && fb_email.length()>0){
					FbCall();
				}
				
				
//				strFbFName = json.getString("first_name");
//				strFbLName = json.getString("last_name");
//				// strFbUserName = json.getString("username");
//				strFbId = json.getString("id");
//				strFbEmail = json.getString("email");
//				
//				strFBFirstName = strFbFName;			
//				PreferenceUtils.setUserFirstName(strFBFirstName);

			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}
	@SuppressLint("HandlerLeak")
	public void FbCall(){
		
		runOnUiThread(new Runnable() 
		{
		   public void run() 
		   {
			   new FbLogin().execute();   
		   }
		}); 
	
	}
 
	private class LoginUser extends AsyncTask<String, Void, String> {
		private static final String RESULT = "Result";
		private static final String LOGINRESPONSE = "LoginResponse";
		private static final String ID = "ID";
		private static final String USERNAME = "username";
		private static final String PROFILEPIC = "Profilepic";
		private static final String EMAIL = "Email";
		private static final String PHONE = "Phone";
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
		 	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("username", email.getText().toString()));
			        nameValuePairs.add(new BasicNameValuePair("password", pwd.getText().toString()));
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
									user_id = Integer.parseInt(parser.nextText());
								} else if (name.equalsIgnoreCase(USERNAME)) {
									user_name = parser.nextText();
								} else if (name.equalsIgnoreCase(PROFILEPIC)) {
									user_image = parser.nextText();
								}else if (name.equalsIgnoreCase(EMAIL)) {
									email1 = parser.nextText();
								}else if (name.equalsIgnoreCase(PHONE)) {
									phone = parser.nextText();
								}else if (name.equalsIgnoreCase(ERRORMESSAGE)) {
									error_message = parser.nextText();
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
			fb_url=util.setFbEmail(fb_access_token, fb_user_name, fb_email);
			return util.getXmlFromUrl(fb_url);

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
							if (name.equalsIgnoreCase(FBRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									user_id = Integer.parseInt(parser.nextText());
								} else if (name.equalsIgnoreCase(USERNAME)) {
									user_name = parser.nextText();
								} else if (name.equalsIgnoreCase(PROFILEPIC)) {
									user_image = parser.nextText();
								}else if (name.equalsIgnoreCase(EMAIL)) {
									email1 = parser.nextText();
								}else if (name.equalsIgnoreCase(PHONE)) {
									phone = parser.nextText();
								}else if (name.equalsIgnoreCase(ERRORMESSAGE)) {
									error_message = parser.nextText();
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
		System.out.println("id" + user_id);
		if (user_id > 0) {
			System.out.println("user_id" + user_id);
			event.setUserId(user_id);
			event.setUserName(user_name);
			event.setUser_image(user_image);
			event.setEmail(email1);
			event.setPhone(phone);
			String myClass = target_activity;
			Class<?> myClass1 = null;
			try {
				myClass1 = Class.forName(myClass);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Activity obj = (Activity) myClass1.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent in = new Intent(LoginActivity.this, myClass1);
			in.putExtra("class", "LoginActivity");
			LoginActivity.this.finish();
			startActivity(in);
		/*	InputMethodManager inputManager = (InputMethodManager)
					LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.toggleSoftInput(0, 0);*/
					overridePendingTransition(R.anim.trans_left_in,
							R.anim.trans_left_out);
					//imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
			} else {
				util.dialogExample1(error_message);
		}
		
	}
	@Override
	public void onResume() {
		super.onResume();

	}
	

}