package com.etisbew.eventsnow.android.contact;

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
	String url, desc_string = "";
	RelativeLayout menuLayout;
	int status;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		event = (EventBean) getApplicationContext();

		util = new Utility(Contact.this);
		//

		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		cheader = (TextView) findViewById(R.id.cheader);
		caddress = (TextView) findViewById(R.id.caddress);
		cphone = (TextView) findViewById(R.id.cphone);
		cphone1 = (TextView) findViewById(R.id.cphone1);
		cemail1 = (TextView) findViewById(R.id.cemail1);
		cemail = (TextView) findViewById(R.id.cemail);
		cfax = (TextView) findViewById(R.id.cfax);
		cfax1 = (TextView) findViewById(R.id.cfax1);

		menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);

		fname = (EditText) findViewById(R.id.fname);
		lname = (EditText) findViewById(R.id.lname);
		email = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phone);
		address = (EditText) findViewById(R.id.address);

		submit = (Button) findViewById(R.id.submit);

		event_title.setTypeface(event.getTextBold());
		submit.setTypeface(event.getTextBold());
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

		fname.setText(event.getUserName());
		email.setText(event.getEmail());
		phone.setText(event.getPhone());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title
				|| v.getId() == R.id.menuLayout) {
			finish();
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
		} else if (v.getId() == R.id.submit) {
			if (fname.getText().toString().length() == 0
					|| lname.getText().toString().length() == 0
					|| email.getText().toString().length() == 0
					|| phone.getText().toString().length() == 0
					|| address.getText().toString().length() == 0) {
				util.dialogExample1("All fields are mandatory");
			} else {

				if (!util.validEmail(email.getText().toString())) {
					util.dialogExample1("Please enter a valid email address");

				} else {
					String address1 = address.getText().toString().trim();
					address1 = address1.replace("\n", "%20");
					address1.replace(" ", "%20");

					url = util.setContact(fname.getText().toString().trim().replace(" ", "%20"),
							lname.getText().toString().trim().replace(" ", "%20"), "city",
							"country", email.getText().toString().trim(),
							"male", phone.getText().toString().trim(),
							"requesttype", address1.replace(" ", "%20"));

					Boolean status = util.IsNetConnected(Contact.this);
					if (status) {
						new Contact_Us().execute();

					} else {
						util.showAlertNoInternetService(Contact.this);
					}
				}
			}
		}

	}

	private class Contact_Us extends AsyncTask<String, Void, String> {
		private static final String FEEDBACKRESPONSE = "FeedbackResponse";
		private static final String RESPONSE = "Response";
		private static final String SUCCESS = "success";

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
							if (name.equalsIgnoreCase(FEEDBACKRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(SUCCESS)) {
									status = Integer
											.parseInt(parser.nextText());
								}
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(FEEDBACKRESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				update1();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	public void update1() {
		if (status == 1) {
			util.dialogExample1("Thanks for contacting us! We will get in touch with you shortly");
		} else {
			util.dialogExample1("There was a problem with your submission");
		}
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
}