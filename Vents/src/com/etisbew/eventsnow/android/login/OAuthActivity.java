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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.MainActivity;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.myfavorites.MyFavorites;
import com.etisbew.eventsnow.android.ordernow.OrderNow;
import com.etisbew.eventsnow.android.util.Utility;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAuthorizationRequestUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.Plus.Builder;
import com.google.api.services.plus.model.Person;
@SuppressLint("NewApi")
public class OAuthActivity extends Activity {


		public static final String CLIENT_ID = "648368762330-0vd7g5vndqloq1meimo186lgsiu4op5p.apps.googleusercontent.com";
	public static final String CLIENT_SECRET = "3O233hwBwXDDpndFdG07GMbb";
		
	public static final String SCOPE = " https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";
	public static final String REDIRECT_URI = "http://localhost";
	private Person profile;
	Utility util; 
	EventBean event;   
	int user_id;
	String google_access_token,google_user_name,google_email;
	String user_image, user_name,email1,phone;
	String error_message;
	String google_url;
	String target_activity;
	String address,state,city;
	int state_id,city_id;
	SharedPreferences pref;
	 Editor editor;
	int pref_user_id;
	private PlusClient mPlusClient;
	String pref_user_name;
	String pref_user_image;
	String pref_email,pref_phone;
	String pref_address,state_name,city_name;
	public static final String MyPREFERENCES = "MyPrefs" ;
	Map<Integer, String> details;
	@SuppressLint("NewApi") 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(tp); 
		}
		pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
	    editor = pref.edit();
		event = (EventBean) getApplicationContext(); 
		util = new Utility(OAuthActivity.this);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {
			target_activity = extras.getString("target");
			details = (HashMap<Integer, String>) iin
					.getSerializableExtra("details");
		}	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	
		WebView webview = new WebView(this);
	
		webview.setVisibility(View.VISIBLE);
		webview.getSettings().setJavaScriptEnabled(true);
		setContentView(webview);
	
		  CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(OAuthActivity.this);
		    CookieManager cookieManager = CookieManager.getInstance();
		    cookieManager.removeAllCookie();
		
		String googleAuthorizationRequestUrl = new GoogleAuthorizationRequestUrl(
				CLIENT_ID,
				REDIRECT_URI,
				SCOPE).build(); 
		webview.setWebViewClient(new WebViewClient() { 
			
			@Override
			public void onPageFinished(WebView view, String url) {

				if (url.startsWith(REDIRECT_URI)) {
					try {
						if (url.indexOf("code=") != -1) {
							view.setVisibility(View.INVISIBLE);
							String code = url.substring(
									REDIRECT_URI.length() + 7, 
									url.length()); 
							
							AccessTokenResponse accessTokenResponse = new GoogleAuthorizationCodeGrant(
									new NetHttpTransport(),
									new JacksonFactory(),
									CLIENT_ID, 
									CLIENT_SECRET,  
									code, REDIRECT_URI)
									.execute();
						JsonFactory jsonFactory = new JacksonFactory();
						HttpTransport transport = new NetHttpTransport();
						GoogleAccessProtectedResource accessProtectedResource = new GoogleAccessProtectedResource(
								accessTokenResponse.accessToken, transport, jsonFactory,
								CLIENT_ID,
								CLIENT_SECRET,
								accessTokenResponse.refreshToken);
						

						Builder b = Plus.builder(transport, jsonFactory)
								.setApplicationName("Simple-Google-Plus/1.0");
					
						b.setHttpRequestInitializer(accessProtectedResource);
						Plus plus = b.build();
						profile = plus.people().get("me").execute();
						String strResponse = profile.getEmails().toString().replace("[", "").replace("]", "");
						String email = null;
						try {
							JSONObject jObj = new JSONObject(strResponse);
							email = jObj.optString("value");
						} catch (JSONException e) { 
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						google_access_token=accessTokenResponse.accessToken;
						google_user_name=profile.getDisplayName();
						google_email=email;
					
						if(google_access_token.length()>0 && google_user_name.length()>0 && google_email.length()>0){
							
							GoogleCall();
						}
						
	
						 
								} else if (url.indexOf("error=") != -1) {
							view.setVisibility(View.INVISIBLE);
								startActivity(new Intent(
									OAuthActivity.this,
									MainActivity.class));
							finish();
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		webview.loadUrl(googleAuthorizationRequestUrl);
	}
public void GoogleCall(){
	
	  Boolean status = util.IsNetConnected(OAuthActivity.this);
		if (status) {
			  new GoogleLogin().execute();  
		} else {
			util.showAlertNoInternetService(OAuthActivity.this);
		}
	
	}
public PlusClient getMplusClient() {
    return mPlusClient;
}
	private class GoogleLogin extends AsyncTask<String, Void, String> {
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
			progressDialog = new ProgressDialog(OAuthActivity.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
				 HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost(util.setGmailEmail());
			    String result = null; 
			    try {
			        // Add your data
			    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("name", google_user_name));
			        nameValuePairs.add(new BasicNameValuePair("email", google_email));
			        nameValuePairs.add(new BasicNameValuePair("google_id", google_access_token));
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
								}else if (name.equalsIgnoreCase(CITY)) {
									city = parser.nextText();
								}else if (name.equalsIgnoreCase(STATE)) {
									state = parser.nextText();
								}else if (name.equalsIgnoreCase(ADDRESS)) {
									address = parser.nextText();
								}else if (name.equalsIgnoreCase(CITY_ID)) {
									city_id = Integer.parseInt(parser.nextText());
								}else if (name.equalsIgnoreCase(STATE_ID)) {
									state_id = Integer.parseInt(parser.nextText());
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
			 editor.putString("user_image", user_image);
			 editor.putString("email", email1);
			 editor.putString("phone", phone);
			 editor.putString("address", address);
			 editor.putString("state", state);
			 editor.putString("city", city);
			 editor.commit();
			 if (TextUtils.isEmpty(target_activity)
					|| target_activity.equalsIgnoreCase("null")) {
				 Intent in = new Intent(OAuthActivity.this, MainActivity.class);
					OAuthActivity.this.finish();
					startActivity(in);
					overridePendingTransition(R.anim.trans_left_in,
							R.anim.trans_left_out);
			 }else if(target_activity.equalsIgnoreCase("MainActivity")){
					Intent in = new Intent(OAuthActivity.this, MainActivity.class);
					OAuthActivity.this.finish();
					startActivity(in);
					overridePendingTransition(R.anim.trans_left_in,
							R.anim.trans_left_out);
					
			 }	 else if (target_activity.equalsIgnoreCase("MyAccount")) {
							if (!((Activity) OAuthActivity.this).isFinishing()) {
								Intent in=new Intent(OAuthActivity.this, MainActivity.class);
								in.putExtra("frompayment", "MyAccount");
								OAuthActivity.this.startActivity(in);
							}
							overridePendingTransition(R.anim.trans_left_in,
									R.anim.trans_left_out);
				}	 else if (target_activity.equalsIgnoreCase("MyTickets")) {
									if (!((Activity) OAuthActivity.this).isFinishing()) {
										Intent in=new Intent(OAuthActivity.this, MainActivity.class);
										in.putExtra("frompayment", "MyTickets");
										OAuthActivity.this.startActivity(in);
									}
									overridePendingTransition(R.anim.trans_left_in,
											R.anim.trans_left_out);
			
			 }	else if (target_activity.equalsIgnoreCase("Details")) {
						if (!((Activity) OAuthActivity.this).isFinishing()) {
							OAuthActivity.this.finish();
						}
						overridePendingTransition(R.anim.trans_left_in,
								R.anim.trans_left_out);
				}else if(target_activity.equalsIgnoreCase("MyFavorites")){
					Intent in = new Intent(OAuthActivity.this, MyFavorites.class);
					OAuthActivity.this.finish();
					startActivity(in);
					overridePendingTransition(R.anim.trans_left_in,
							R.anim.trans_left_out);
				} else if(target_activity.equalsIgnoreCase("OrderNow")){
					try{
						event.setDetails(details);
						}catch(Exception e){
							
						}
					Intent in = new Intent(OAuthActivity.this, OrderNow.class);
					OAuthActivity.this.finish();
					startActivity(in);
					overridePendingTransition(R.anim.trans_left_in,
							R.anim.trans_left_out);
				}

			} else {
				util.dialogExample1(error_message);
		}
	}
}