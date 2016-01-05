package com.etisbew.eatz.android;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.etisbew.eatz.android.orderfood.OrderConfirm;

public class HelloWebViewClient extends WebViewClient {

	Context context;

	public HelloWebViewClient(Context con) {
		// TODO Auto-generated constructor stub
		context = con;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		if (url.contains("success")) {
			context.startActivity(new Intent(context, OrderConfirm.class));
		}

		return true;
	}
}