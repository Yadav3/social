package com.etisbew.eatz.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.etisbew.eatz.android.Launcher;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.android.orderfood.OrderConfirm;

public class PurchasedItems extends Activity {

	ImageView back;
	public static String address = "";

	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webviewpurchase);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

				startActivity(new Intent(getApplicationContext(),
						Launcher.class));
			}
		});

		final WebView webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().getAllowFileAccess();
		String url = "";
		if (Appconstants.strPurchaseSubUrl.equalsIgnoreCase("deals/mobileapp/")) {
			if (Appconstants.user_flag == 2) {
				url = Appconstants.PURCHASED_ITEMS_HOST
						+ Appconstants.strPurchaseSubUrl
						+ Appconstants.strPurchaseId + "?address=" + address
						+ "&city=" + Appconstants.strCityName +"&postal_code="+Appconstants.strPostalcode+ "&country=IND";//

				address = "";
			} else {
				url = Appconstants.PURCHASED_ITEMS_HOST
						+ Appconstants.strPurchaseSubUrl
						+ Appconstants.strPurchaseId;
				address = "";
			}
		} else {
			url = Appconstants.PURCHASED_ITEMS_HOST
					+ Appconstants.strPurchaseSubUrl
					+ Appconstants.strPurchaseId;
			address = "";
		}
		webView.loadUrl(url);
		System.out.println("url"+url);

		webView.setWebViewClient(new HelloWebViewClient1());
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

		finish();
		startActivity(new Intent(getApplicationContext(), Launcher.class));
		return;
	}

	public class HelloWebViewClient1 extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			if (url.contains("success")) {
				view.setVisibility(View.INVISIBLE);
				/*
				 * if (Appconstants.user_flag == 2) {
				 * PurchasedItems.this.finish(); startActivity(new
				 * Intent(PurchasedItems.this, Launcher.class));
				 * 
				 * } else {
				 */
				PurchasedItems.this.finish();
				startActivity(new Intent(PurchasedItems.this,
						OrderConfirm.class));
				// }
			} else if (url.contains("http://eatz.com")) {
				PurchasedItems.this.finish();
				startActivity(new Intent(PurchasedItems.this, Launcher.class));
			}

			return true;
		}
	}
}
