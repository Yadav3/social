/* 
 * Copyright (C) 2011 iMellon
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.etisbew.eatz.android;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.facebook.SharedPreferencesCredentialStore;
import com.etisbew.eatz.utils.Utils;
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

/**
 * @author Christos Papazafeiropoulos, Dimitris Makris
 * 
 *         This class starts a webview to let the user either insert credentials
 *         and accept/deny the application or accept/deny directly the
 *         application. The procedure is based on the OAuth 2.0 dance which is
 *         handled by the Google libraries.
 * 
 */

public class OAuthActivity extends Activity {

	private SharedPreferences prefs;
	String strCode,strUserEmail,strUserAddress;
	private Person profile;
	WebView webview;
	boolean flag = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

		// new OAuthRequestTokenTask(this).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
		webview = new WebView(this);
		webview.setVisibility(View.VISIBLE);
		webview.getSettings().setJavaScriptEnabled(true);
		setContentView(webview);

		String googleAuthorizationRequestUrl = new GoogleAuthorizationRequestUrl(
				SharedPreferencesCredentialStore.CLIENT_ID,
				SharedPreferencesCredentialStore.REDIRECT_URI,
				SharedPreferencesCredentialStore.SCOPE).build();

		webview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				
				if (url.startsWith(SharedPreferencesCredentialStore.REDIRECT_URI)) {
					if (url.indexOf("code=") != -1) {
						// Url is like
						// http://localhost/?code=4/Z5DgC1IxNL-muPsrE2Sjy9zQn2pF
						strCode = url.substring(
								SharedPreferencesCredentialStore.REDIRECT_URI
										.length() + 7, url.length());

						// startActivity(new Intent(
						// OAuthActivity.this,
						// GooglePlusActivity.class));
						// finish();

						if (!flag) {
							flag = true;
							view.setVisibility(View.GONE);
							if (ConnectivityReceiver.checkInternetConnection(OAuthActivity.this)) {	
							new MyTask().execute();

						} else {
							ConnectivityReceiver.showCustomDialog(OAuthActivity.this);
						}
						}

					} else if (url.indexOf("error=") != -1) {
						view.setVisibility(View.INVISIBLE);
						SharedPreferencesCredentialStore.getInstance(prefs)
								.clearCredentials();
						OAuthActivity.this.finish();
					}
				}
			}
		});

		webview.loadUrl(googleAuthorizationRequestUrl);
	}

	class MyTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		AccessTokenResponse accessTokenResponse;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(OAuthActivity.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			try {
				accessTokenResponse = new GoogleAuthorizationCodeGrant(
						new NetHttpTransport(), new JacksonFactory(),
						SharedPreferencesCredentialStore.CLIENT_ID,
						SharedPreferencesCredentialStore.CLIENT_SECRET,
						strCode, SharedPreferencesCredentialStore.REDIRECT_URI)
						.execute();

				SharedPreferencesCredentialStore credentialStore = SharedPreferencesCredentialStore
						.getInstance(prefs);
				credentialStore.write(accessTokenResponse);

				JsonFactory jsonFactory = new JacksonFactory();
				HttpTransport transport = new NetHttpTransport();

				AccessTokenResponse accessTokenResponse = credentialStore
						.read();

				GoogleAccessProtectedResource accessProtectedResource = new GoogleAccessProtectedResource(
						accessTokenResponse.accessToken, transport,
						jsonFactory,
						SharedPreferencesCredentialStore.CLIENT_ID,
						SharedPreferencesCredentialStore.CLIENT_SECRET,
						accessTokenResponse.refreshToken);

				Builder b = Plus.builder(transport, jsonFactory)
						.setApplicationName("Simple-Google-Plus/1.0");
				b.setHttpRequestInitializer(accessProtectedResource);
				Plus plus = b.build();
				profile = plus.people().get("me").execute();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			webview.setVisibility(View.INVISIBLE);

			String strResponse = profile.getEmails().toString().replace("[", "").replace("]", "");
			
			try {
				JSONObject jObj = new JSONObject(strResponse);
				strUserEmail = jObj.optString("value");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (ConnectivityReceiver.checkInternetConnection(OAuthActivity.this)) {
			new GpLoginTask(accessTokenResponse.accessToken, profile.getName()
					.getFamilyName(), profile.getName().getGivenName(),
					profile.getId()).execute();
			} else {
				ConnectivityReceiver.showCustomDialog(OAuthActivity.this);
			}
			
			Login.strFirstName = profile.getName().getGivenName();			
			PreferenceUtils.setUserFirstName(Login.strFirstName);
			
			Login.strLastName = profile.getName().getFamilyName();			
			PreferenceUtils.setUserLastName(Login.strLastName);
			
//			for (int i = 0; i < profile.getEmails().size(); i++) {
				Login.strEmailId = profile.getEmails().get(0).getValue().toString();	
				PreferenceUtils.setUserEmail1(Login.strEmailId);
//			}
			

		}
	}

	class GpLoginTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		String strAccesstoken, strUsername, strLastname, strGoogleUID;

		public GpLoginTask(String accessToken, String personName,
				String Lastname, String strId) {
			strAccesstoken = accessToken;
			strUsername = personName;
			strLastname = Lastname;
			strGoogleUID = strId;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(OAuthActivity.this);
			pDialog.setMessage("Connecting Google With eatz. Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			return Utils.getJSONString(Appconstants.MAIN_HOST
					+ "facebookLogin/" + strAccesstoken + "/"
					+ strUserEmail + "/" + strUsername + "/"
					+ strLastname + "/" + strGoogleUID);
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
					
					Log.e("Response : ", result);

					if (result.contains("errorMessage")) {
						@SuppressWarnings("unused")
						String errmsg = jObject.optString("errorMessage");
					} else {

						Login.strSession = jObject.optString("sessionId");
						PreferenceUtils.setUserSession(Login.strSession);
						Login.strUserId = jObject.optString("userId");
						PreferenceUtils.setUserId(Login.strUserId);
						Login.strUserName = jObject.optString("username");
						PreferenceUtils.setUserName1(Login.strUserName);
						Login.strPointsEarned = jObject
								.optString("points_earned");
						PreferenceUtils.setUserPoints(Login.strPointsEarned);
						Login.strPointsPending = jObject
								.optString("points_pending");
						PreferenceUtils.setUserPointsPending(Login.strPointsPending);
						Login.strProfileImg = jObject.optString("profilePic");
						PreferenceUtils.setUserProfilePic(Login.strProfileImg);
						if (!jObject.isNull("address")) {
							strUserAddress = jObject.optString("address");
							PreferenceUtils.setUserAddress1(strUserAddress);
							}else{
								PreferenceUtils.setUserAddress1("");
							}
						if (!jObject.isNull("phone")) {
							PreferenceUtils.setUserPhone1(jObject
									.optString("phone"));
							}
						if(Login.redirect_flag ==1){
							Login.redirect_flag=0; 
						finish();
						 startActivity(new
						 Intent(getApplicationContext(),Launcher.class));
						}else{
							finish();
						}
					
						overridePendingTransition(R.anim.stay,
								R.anim.toptobottom);
						/*OAuthActivity.this.finish();
						startActivity(new Intent(getApplicationContext(),
								Launcher.class));*/
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() >= 0) {
			onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		OAuthActivity.this.finish();

	startActivity(new Intent(OAuthActivity.this,Login.class));
		return;
	}
}
