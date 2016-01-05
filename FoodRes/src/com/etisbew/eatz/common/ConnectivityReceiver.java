package com.etisbew.eatz.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver {

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
	public static void showCustomDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		//builder.setMessage("Network is not available. Please check network connection and try again.").setCancelable(false)
		builder.setMessage("Sorry, Network is not available. Please try again later").setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
		
	}
}