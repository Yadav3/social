package com.etisbew.eventsnow.android.ordernow;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.etisbew.eventsnow.android.MainActivity;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.util.Utility;

@SuppressLint({ "ClickableViewAccessibility", "SetJavaScriptEnabled" })
public class PaymentConfirmation extends Activity{
	WebView web;
	static String mAccessToken;  
	static String sPathUrl1; 
Utility util; 
int transaction_id;
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.facebook_details);
		web =(WebView)findViewById(R.id.webView1); 
		web.getSettings().setJavaScriptEnabled(true);  
		util = new Utility(PaymentConfirmation.this);  
		Intent iin = getIntent();
		Bundle extras = iin.getExtras(); 
		
		if (extras != null) {
			transaction_id = extras.getInt("orderid");
		
		}
		System.out.println("ud"+transaction_id+util.setAuthenticate(transaction_id));
		web.loadUrl(util.setAuthenticate(transaction_id));
		web.setWebViewClient(new HelloWebViewClient1());

	
	 
	}   
	
	public void get(){
		web.setWebViewClient(new HelloWebViewClient1());
		System.out.println("mAccessToken"+mAccessToken);
	} 
    @Override
    public void onBackPressed() { 
     }
    @Override
	public void onResume(){
	    super.onResume();
	
	}
   /*
    public class HelloWebViewClient extends WebViewClient {

    	public boolean shouldOverrideUrlLoading(WebView view, String url) { 
     		System.out.println("url"+url);
    		view.loadUrl(url);
    		view.setWebViewClient(new HelloWebViewClient1());
    		
    		return true; 
    	}

    	 
    }*/
    public class HelloWebViewClient1 extends WebViewClient {

    	public boolean shouldOverrideUrlLoading(WebView view, String url) { 
    			System.out.println("url2"+url);
    				if(url.contains("success")){
    					view.loadUrl(url);
    		    		view.setWebViewClient(new HelloWebViewClient1());
    				}else if (url.contains("?mobile=shutoff")) {
    				Intent in =new Intent(PaymentConfirmation.this,MainActivity.class);
    				in.putExtra("frompayment","frompayment");
    	    		startActivity(in);
    	    		PaymentConfirmation.this.finish();
    	    		overridePendingTransition(R.anim.trans_left_in,R.anim.trans_left_out);
    	    		
    	    		}else if(url.contains("http://eventsnow.com/")){
    	    			Intent in =new Intent(PaymentConfirmation.this,MainActivity.class);
    	    			startActivity(in);
    	    			overridePendingTransition(R.anim.trans_left_in,R.anim.trans_left_out);
    	    		}
    				view.loadUrl(url);
		    		view.setWebViewClient(new HelloWebViewClient1());
    	return true;
    	}

    	
    }

}