package com.etisbew.eventsnow.android.contact;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.util.Utility;

public class Contact extends Activity implements OnClickListener {
	Button submit;
	EventBean event;
	TextView back, event_title, tfname, tlname, tphone, temail1, tmsg, cheader,
			caddress, cphone, cphone1, cemail, cemail1, cfax, cfax1;
	EditText fname, lname, email, phone, address;
	Utility util;
	String url,desc_string="";
	RelativeLayout menuLayout;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);  
		event = (EventBean) getApplicationContext(); 
		
		util = new Utility(Contact.this);
//		
 
		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		tfname = (TextView) findViewById(R.id.tfname);
		tlname = (TextView) findViewById(R.id.tlname);
		tphone = (TextView) findViewById(R.id.tphone);
		temail1 = (TextView) findViewById(R.id.temail1);
		tmsg = (TextView) findViewById(R.id.tmsg);
		cheader = (TextView) findViewById(R.id.cheader);
		caddress = (TextView) findViewById(R.id.caddress);
		cphone = (TextView) findViewById(R.id.cphone);
		cphone1 = (TextView) findViewById(R.id.cphone1);
		cemail1 = (TextView) findViewById(R.id.cemail1);
		cemail = (TextView) findViewById(R.id.cemail);
		cfax = (TextView) findViewById(R.id.cfax);
		cfax1 = (TextView) findViewById(R.id.cfax1);
		
		menuLayout = (RelativeLayout)findViewById(R.id.menuLayout);
		
		fname = (EditText) findViewById(R.id.fname);
		lname = (EditText) findViewById(R.id.lname);
		email = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phone);
		address = (EditText) findViewById(R.id.address);

		submit = (Button) findViewById(R.id.submit);

		event_title.setTypeface(event.getTextBold());
		submit.setTypeface(event.getTextBold());
		tfname.setTypeface(event.getTextNormal());
		tlname.setTypeface(event.getTextNormal());
		tphone.setTypeface(event.getTextNormal());
		temail1.setTypeface(event.getTextNormal());
		tmsg.setTypeface(event.getTextNormal());
		cheader.setTypeface(event.getTextBold());
		caddress.setTypeface(event.getTextNormal());
		cphone.setTypeface(event.getTextBold());
		cphone1.setTypeface(event.getTextNormal());
		cemail1.setTypeface(event.getTextNormal());
		cemail.setTypeface(event.getTextBold());
		cfax.setTypeface(event.getTextBold());
		cfax1.setTypeface(event.getTextNormal());

		fname.setTypeface(event.getTextNormal());
		lname.setTypeface(event.getTextNormal());
		email.setTypeface(event.getTextNormal());
		phone.setTypeface(event.getTextNormal());
		address.setTypeface(event.getTextNormal());
		back.setOnClickListener(this);
		event_title.setOnClickListener(this);
		submit.setOnClickListener(this);
		menuLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title || v.getId() == R.id.menuLayout) {
			finish();
			overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);  
		}
		else if (v.getId() == R.id.submit) {
			if(fname.getText().toString().length() == 0 || lname.getText().toString().length() == 0|| email.getText().toString().length() == 0|| phone.getText().toString().length() == 0|| address.getText().toString().length() == 0){
				util.dialogExample1("Please enter all fields");
			}else{
				
				if (!util.validEmail(email.getText().toString())) {
					util.dialogExample1("Enter valid e-mail!");

				} else {
					url = util.setContact(fname.getText().toString().trim(),
							lname.getText().toString().trim(), "city",
							"country", email.getText().toString().trim(),
							"male", phone.getText().toString().trim(),
							"requesttype", address.getText().toString().trim());
					         new Contact_Us().execute();
				}



				
		}
		}
		
	}
	



	 private class Contact_Us extends AsyncTask<String, Void, String> {
			private static final String CONTENTPAGES = "ContentPages";
			private static final String DESCRIPTION = "Description";
		
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {

				super.onPreExecute();
				progressDialog = new ProgressDialog(Contact.this);
				progressDialog.setMessage("Loading ...");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();

			}

			@Override
			protected String doInBackground(String... args) {
				return util.getXmlFromUrl(url);

			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				System.out.println("result1" + result);
				if (null == result || result.length() == 0
						|| TextUtils.isEmpty(result)) {

					util.dialogExample();

				} else {
					try {
						System.out.println("my result"+result);
					} catch (Exception e) {

						progressDialog.dismiss();
	  
					}
					//update1(); 
				}

				if (null != progressDialog && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

			}

		}
	
	 @Override
	    public void onBackPressed() {
	        super.onBackPressed();
	        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);   
	    }
	 @Override
		public void onResume(){
		    super.onResume();
		
		}
}