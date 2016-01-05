package com.etisbew.eventsnow.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;


public class PdfView  extends Activity {
String pdfURL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();  
	
		if (extras != null) {
		
			pdfURL = extras.getString("pdfURL");
		} 
		WebView webView = new WebView(PdfView.this);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);

		
		webView.setWebViewClient(new Callback());

		
		webView.loadUrl("http://docs.google.com/gview?embedded=true&url="
				+ pdfURL);

		setContentView(webView);
	}

	private class Callback extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return (false);
		}
	}
}
