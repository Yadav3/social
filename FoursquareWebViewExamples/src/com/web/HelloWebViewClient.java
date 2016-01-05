package com.web;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HelloWebViewClient extends WebViewClient {

	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// getWindow().requestFeature(Window.FEATURE_PROGRESS);
		/*
		 * if(url.contains("market")) { url="https://foursquare.com/signup/";
		 * view.getSettings().setJavaScriptEnabled(true);
		 * 
		 * }
		 */
		/*
		 * if(url.contains("/logout")) { url="https://foursquare.com/";
		 * //view.getSettings().setJavaScriptEnabled(true); }
		 */
		// view.loadUrl(url);
		if (url.contains("?code=")) {
			String[] exp = url.split("\\?");
			String[] exp1 = exp[1].split("=");
			String code = exp1[1];

			String sPathUrl1 = "https://foursquare.com/oauth2/access_token?client_id=0BTAHE0BSCCMWHGHEPJS25UFPWHQUVG2EVMPCH2FWV1NX4E3&client_secret=VQVXOCZMJ5YT4ULKQVA0FB1VL11Y3WK21CLSFBD1OGARL2H2&grant_type=authorization_code&redirect_uri=http://uat.myplatinumlife.com/newwebservice/Loading.html&code="
					+ code;
			String str1 = ""; // ,str2="";

			HttpClient hc = new DefaultHttpClient();
			HttpPost post = new HttpPost(sPathUrl1);

			try {
				HttpResponse rp = hc.execute(post);
				if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					str1 = EntityUtils.toString(rp.getEntity());

					JSONObject json = new JSONObject(str1);

					if (json.has("error")) {
						JSONObject error = json.getJSONObject("error");
					}
					if (json.has("access_token")) {
						WebViewExamples.mAccessToken = json
								.getString("access_token");
						
					}
					if (json.has("username")) {
						String name = json.getString("username");
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		// if(url.contains("success"))
		// {
		// Toast.makeText(con, text, duration)
		// }

		// sPathUrl = url;

		return true;
	}

	// private JSONObject executeHttpGet(String uri) throws Exception {
	// HttpGet req = new HttpGet(uri);
	//
	// HttpClient client = new DefaultHttpClient();
	// HttpResponse resLogin = client.execute(req);
	// BufferedReader r = new BufferedReader(new InputStreamReader(resLogin
	// .getEntity().getContent()));
	// StringBuilder sb = new StringBuilder();
	// String s = null;
	// while ((s = r.readLine()) != null) {
	// sb.append(s);
	// }
	// return new JSONObject(sb.toString());
	// }

	// public static JSONObject parseJson(String response) throws JSONException
	// {
	// JSONObject json = new JSONObject(response);
	//
	// if (json.has("error")) {
	// JSONObject error = json.getJSONObject("error");
	// }
	// if (json.has("access_token")) {
	// AccessToke = json.getString("access_token");
	// }
	// if (json.has("username")) {
	// name = json.getString("username");
	// }
	// return json;
	// }
}