package com.etisbew.eatz.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HaveFun extends Activity {

	private WebView webview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.havefun);

		webview = (WebView)findViewById(R.id.webView1);
		webview.getSettings().setJavaScriptEnabled(true);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webview.loadUrl("http://mobile.localsecrets.in/hyderabad-events");
		webview.setWebViewClient(new MyWebViewClient());
	}

	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	view.loadUrl(url);
	        return true;
	    }
	}
}