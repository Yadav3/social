package com.etisbew.eventsnow.android.forgot;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.util.Utility;

public class ForgotPassword extends Activity implements OnClickListener {
	Button submit;
	EventBean event;
	TextView back, event_title;
	EditText email;
	Utility util;
	String url;
	String response;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot);
		event = (EventBean) getApplicationContext();

		util = new Utility(ForgotPassword.this);
		//

		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		email = (EditText) findViewById(R.id.email);
		submit = (Button) findViewById(R.id.submit);
		
		event_title.setTypeface(event.getTextBold());
		email.setTypeface(event.getTextNormal());
		submit.setTypeface(event.getTextBold());
		
		back.setOnClickListener(this);
		event_title.setOnClickListener(this);
		submit.setOnClickListener(this);
		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.textView1:
			finish();
			overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
			break;
		case R.id.event_title:
			finish();
			overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
			break;
		case R.id.submit:
			if (!util.validEmail(email.getText().toString())) {
				util.dialogExample1("Please enter a valid email address");

			} else {
				url=util.setForgot(email.getText().toString());
			
				Boolean status = util.IsNetConnected(ForgotPassword.this);
				if (status) {
					new ForgotPw().execute();

				} else {
					util.showAlertNoInternetService(ForgotPassword.this);
				}
				
			}
			overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
		}
	}
	private class ForgotPw extends AsyncTask<String, Void, String> {
		private static final String RESPONSE  = "Response ";
		private static final String ERRORMESSAGE = "ErrorMessage";
		
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(ForgotPassword.this);
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
				if (null == result || result.length() == 0
					|| TextUtils.isEmpty(result)) {

				util.dialogExample();

			} else {
				try {
					XmlPullParserFactory factory = XmlPullParserFactory
							.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser parser = factory.newPullParser();

					parser.setInput(new StringReader(result));

					int eventType = parser.getEventType();

					while (eventType != XmlPullParser.END_DOCUMENT) {
						String name = null;
						switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(RESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ERRORMESSAGE)) {
									response = parser.nextText();
								}
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(RESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				update();
			} 

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}
	public void update(){
		util.dialogExample1(response);
	}
	
}