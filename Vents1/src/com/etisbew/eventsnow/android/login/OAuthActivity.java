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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.MainActivity;
import com.etisbew.eventsnow.android.util.Utility;
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
public class OAuthActivity extends Activity {


	public static final String CLIENT_ID = "510598577647-0soro1g9arpg93pqavt70jv3hqcf90vs.apps.googleusercontent.com";
	
	public static final String CLIENT_SECRET = "2C2B2Fl683OeMxCuPO-FAeMY";
	
	
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(tp);
		}
		event = (EventBean) getApplicationContext();
		util = new Utility(OAuthActivity.this);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {
			target_activity = extras.getString("activity");

		}	
	}

	@Override
	protected void onResume() {
		super.onResume();
		WebView webview = new WebView(this);
		webview.setVisibility(View.VISIBLE);
		webview.getSettings().setJavaScriptEnabled(true);
		setContentView(webview);
		
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

						System.out.println("response"+accessTokenResponse);
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
						
					/*	new GpLoginTask(accessTokenResponse.accessToken, profile.getName()
								.getFamilyName(), profile.getName().getGivenName(),
								profile.getId()).execute();
						
						Login.strFirstName = profile.getName().getGivenName();			
						PreferenceUtils.setUserFirstName(Login.strFirstName);
						
						Login.strLastName = profile.getName().getFamilyName();			
						PreferenceUtils.setUserLastName(Login.strLastName);
						
//						for (int i = 0; i < profile.getEmails().size(); i++) {
							Login.strEmailId = profile.getEmails().get(0).getValue().toString();	
							PreferenceUtils.setUserEmail1(Login.strEmailId);
//						} 
*/
						 
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
	  new GoogleLogin().execute();   
	
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
			System.out.println("target"+target_activity);
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
			Intent in = new Intent(OAuthActivity.this, myClass1);
			in.putExtra("class", "LoginActivity");
			OAuthActivity.this.finish();
			startActivity(in);
			} else {
				util.dialogExample1(error_message);
		}
	}
}