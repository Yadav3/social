package com.etisbew.eatz.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.etisbew.eatz.common.PreferenceUtils;

public class BaseActivity extends FragmentActivity {

	static ProgressDialog pDialog = null;
	int width, height;
	PreferenceUtils pref;
	 
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// CalligraphyConfig.initDefault("Roboto-ThinItalic.ttf");

		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;

		pref = new PreferenceUtils(this); 

	}

	public void showLoader() {
		if (pDialog == null)
			pDialog = ProgressDialog.show(this, "", "Loading..");
	}
	public void showLoader(String msg) {
		if (pDialog == null)
			pDialog = ProgressDialog.show(this, "", msg);
	}
	public static void showLoader1(Context con,String msg) {
		if (pDialog == null)
			pDialog = ProgressDialog.show(con, "", msg);
	}

	public void hideLoader() {
		if (pDialog != null && pDialog.isShowing()) {
			try {
				pDialog.cancel();
				pDialog = null;
			} catch (Exception e) {
			}
		}
	}
	
	public static void hideLoader1() {
		if (pDialog != null && pDialog.isShowing()) {
			try {
				pDialog.cancel();
				pDialog = null;
			} catch (Exception e) {
			}
		}
	}

	public void hideKeyboard() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public static void showDialogMsg(Context con, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(con);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void showDialogMsg(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static boolean checkInternetConnection(Context context) {

		ConnectivityManager con_manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (con_manager == null) {
			return false;
		} else {
			NetworkInfo[] info = con_manager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean eMailValidation(String emailstring) {
		Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		return emailMatcher.matches();
	}

}
