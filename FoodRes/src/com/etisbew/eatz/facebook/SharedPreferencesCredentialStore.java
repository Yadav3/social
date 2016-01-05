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

package com.etisbew.eatz.facebook;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;

/**
 * @author Christos Papazafeiropoulos, Dimitris Makris
 * 
 * This class keeps the authentication tokens and the ID + SECRET
 * of the Google application.
 * 
 */

public class SharedPreferencesCredentialStore{
	
	/* 
	 * Get access to OAuth 2 credentials in the <a
	 * href="https://code.google.com/apis/console">Google apis console</a>
	 * 
	 */
	
//	1072883967093-qp4njs52ref5uhkg3h7k61giq4qlki6b.apps.googleusercontent.com
	
	//-old === 510598577647-0soro1g9arpg93pqavt70jv3hqcf90vs.apps.googleusercontent.com
	public static final String CLIENT_ID = "208162942894-gjgvkqjijbnroo81nvqo2c2ktpdc42fl.apps.googleusercontent.com";
	
	public static final String CLIENT_SECRET = "MdJS6wnw_WkjdZppTb0ZhtDx";
	/*public static final String CLIENT_ID = "510598577647-0soro1g9arpg93pqavt70jv3hqcf90vs.apps.googleusercontent.com";
	
	public static final String CLIENT_SECRET = "2C2B2Fl683OeMxCuPO-FAeMY";*/
	
	
	public static final String SCOPE = " https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";
	public static final String REDIRECT_URI = "http://localhost";

	private static final String ACCESS_TOKEN = "access_token";
	private static final String EXPIRATION_TIME = "token_expiration_perion";
	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String SCOPE_STRING = "scope";

	private SharedPreferences prefs;

	private static SharedPreferencesCredentialStore store;

	private SharedPreferencesCredentialStore(SharedPreferences prefs) {
		this.prefs = prefs;
	}

	public static SharedPreferencesCredentialStore getInstance(SharedPreferences prefs) {
		if (store == null)
			store = new SharedPreferencesCredentialStore(prefs);

		return store;
	}

	public AccessTokenResponse read() {
		AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
		accessTokenResponse.accessToken = prefs.getString(ACCESS_TOKEN, "");
		accessTokenResponse.expiresIn = prefs.getLong(EXPIRATION_TIME, 0);
		accessTokenResponse.refreshToken = prefs.getString(REFRESH_TOKEN, "");
		accessTokenResponse.scope = prefs.getString(SCOPE_STRING, "");
		return accessTokenResponse;
	}

	public void write(AccessTokenResponse accessTokenResponse) {
		Editor editor = prefs.edit();
		editor.putString(ACCESS_TOKEN, accessTokenResponse.accessToken);
		editor.putLong(EXPIRATION_TIME, accessTokenResponse.expiresIn);
		editor.putString(REFRESH_TOKEN, accessTokenResponse.refreshToken);
		editor.putString(SCOPE_STRING, accessTokenResponse.scope);
		editor.commit();
	}

	public void clearCredentials() {
		Editor editor = prefs.edit();
		editor.remove(ACCESS_TOKEN);
		editor.remove(EXPIRATION_TIME);
		editor.remove(REFRESH_TOKEN);
		editor.remove(SCOPE_STRING);
		editor.commit();
	}
}
