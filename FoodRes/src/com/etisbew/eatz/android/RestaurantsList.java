package com.etisbew.eatz.android;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.web.WebServiceCalls;

public class RestaurantsList extends AsyncTask<String, Void, String> {

	ProgressDialog pDialog;
	Context context; 
	String strUrl;

	public RestaurantsList(Context con, String strUrl1) {
		// TODO Auto-generated constructor stub

		context = con;
		strUrl = strUrl1;
	}
 
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		BaseActivity.showLoader1(context, "fetching results...");

		/*pDialog = new ProgressDialog(context);
		pDialog.setMessage("fetching results...");
		pDialog.setCancelable(false);
		pDialog.show();*/
	}

	@Override
	protected String doInBackground(String... params) {
		String result = "";
		Appconstants.staticURL = strUrl;
		result = WebServiceCalls.getJSONString(strUrl);
		return result;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (null == result || result.length() == 0) {
		} else {

			if (result.contains("errormsg")) {

				try {
					Launcher.strErrorMsg = new JSONObject(result)
							.optString("errormsg");
					Launcher.userLocation = "Hyderabad";

					Launcher.restJsonDetails = new JSONObject(result); 
					Explore.map_flag=1;
					((Activity) context).startActivityForResult(new Intent(
							context, Explore.class), 0);
				} catch (Exception e) { 
					e.printStackTrace();
				}
			} else {
				try {
					Launcher.restJsonDetails = new JSONObject(result);
					Explore.map_flag=1;
					// ((Activity) context).finish();
					((Activity) context).startActivityForResult(new Intent(
							context, Explore.class), 0);
					/*if (null != pDialog && pDialog.isShowing()) {
						pDialog.dismiss();
					}*/
					
				
				} catch (Exception e) { 
					e.printStackTrace();
				}
			}

		}

	}
}
