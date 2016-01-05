package com.etisbew.eatz.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.etisbew.eatz.android.R;

public class WebViewLoader extends Activity {

	private WebView webView;
	private ProgressDialog progDailog;
	String GoogleDocs = "http://docs.google.com/gview?embedded=true&url=";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webviewload);

		progDailog = ProgressDialog.show(WebViewLoader.this, "Loading",
				"Please wait...", true);
		progDailog.setCancelable(false);
		webView = (WebView) findViewById(R.id.webViewLoad); 
		 
			webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				progDailog.show();
				view.loadUrl(url);

				return true;
			}
 
			@Override
			public void onPageFinished(WebView view, final String url) {
				progDailog.dismiss();
			}
		});

		 String doc="<iframe src='http://docs.google.com/viewer?url="+Appconstants.strWebviewUrl+"&embedded=true'width='100%' height='100%'style='border: none;'></iframe>";
	
		//webView.loadUrl(GoogleDocs + Appconstants.strWebviewUrl);
		//webView.loadUrl(doc);
		 webView.loadDataWithBaseURL("file:///android_asset/", 
				 doc.toString(), "text/html", "UTF-8", "");
	}
}
