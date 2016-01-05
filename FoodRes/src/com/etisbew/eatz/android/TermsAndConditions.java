package com.etisbew.eatz.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class TermsAndConditions extends Activity {
	private TextView etRestName;
private ImageView back, iv_close;
WebView termsAndConditions_web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.terms_conditions);
		
		etRestName = (TextView) findViewById(R.id.etRestName);
		etRestName.setText(Explore.strRestaurantTitle);
		
		back=(ImageView)findViewById(R.id.back);
		iv_close=(ImageView)findViewById(R.id.iv_close);
		
		termsAndConditions_web = (WebView) findViewById(R.id.termsAndConditions_web);
		termsAndConditions_web.getSettings().setJavaScriptEnabled(true);
		
	//	termsAndConditions_web.loadDataWithBaseURL("file:///android_asset/");
		
		termsAndConditions_web.loadUrl("file:///android_asset/terms.html");
//		loadUrl("file:///android_assets/www/webpage.html");
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TermsAndConditions.this.finish();
				onBackPressed();
			}
		});
		
		iv_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent i=new Intent(getApplicationContext(),OrderDetails_2.class);
//				startActivity(i);
			}
		});
		
	}
	
	

}
