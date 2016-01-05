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
		System.out.println("ud"+transaction_id);
		System.out.println("url"+util.setAuthenticate(transaction_id));
		web.loadUrl(util.setAuthenticate(transaction_id));
			get();
	
	
	}  
	
	public void get(){
		web.setWebViewClient(new HelloWebViewClient());
		System.out.println("mAccessToken"+mAccessToken);
		//new GettingEvents().execute();
	}
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
      //  overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);   
    }
    @Override
	public void onResume(){
	    super.onResume();
	
	}
    private class GettingToken extends AsyncTask<String, Void, String> {

		
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(PaymentConfirmation.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			//return util.getXmlFromUrl(url);
			String str1 = null;
			HttpClient hc = new DefaultHttpClient();
			HttpPost post = new HttpPost(sPathUrl1);
			try {
				HttpResponse rp = hc.execute(post);
				if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					str1 = EntityUtils.toString(rp.getEntity());

				
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return str1;
 
		} 

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("result"+result);
			String name[]=result.split("=");
			System.out.println("name"+name);
			util.dialogExample1(name[1]);
			
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}  
   
	} 
    public class HelloWebViewClient extends WebViewClient {

    	public boolean shouldOverrideUrlLoading(WebView view, String url) { 
    		// getWindow().requestFeature(Window.FEATURE_PROGRESS);
    		System.out.println("url"+url);
    		view.loadUrl(url);
    		view.setWebViewClient(new HelloWebViewClient1());
    		
    		return true;
    	}

    	 
    }
    public class HelloWebViewClient1 extends WebViewClient {

    	public boolean shouldOverrideUrlLoading(WebView view, String url) { 
    		// getWindow().requestFeature(Window.FEATURE_PROGRESS);
    		System.out.println("url2"+url);
    				if(url.contains("success")){
    					view.loadUrl(url);
    		    		view.setWebViewClient(new HelloWebViewClient1());
    				}else if (url.contains("?mobile=shutoff")) {
    				Intent in =new Intent(PaymentConfirmation.this,MainActivity.class);
    				in.putExtra("frompayment","frompayment");
    	    		startActivity(in);
    	    		PaymentConfirmation.this.finish();
    	    		
    	    		}
    				view.loadUrl(url);
		    		view.setWebViewClient(new HelloWebViewClient1());
    	return true;
    	}

    	
    }

}