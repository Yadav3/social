package com.etisbew.eventsnow.android.aboutus;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.util.Utility;

public class AboutUs extends Activity implements OnClickListener {
	Button buyticket;
	EventBean event;
	TextView back, event_title, desc;
	Utility util;
	String url,desc_string="";
	RelativeLayout menuLayout; 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);

		event = (EventBean) getApplicationContext();
		util = new Utility(AboutUs.this);
		url = util.setAbout();
		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		desc = (TextView) findViewById(R.id.desc);
		menuLayout = (RelativeLayout)findViewById(R.id.menuLayout);
		event_title.setTypeface(event.getTextBold());
		desc.setTypeface(event.getTextNormal());

		back.setOnClickListener(this);
		menuLayout.setOnClickListener(this);
		event_title.setOnClickListener(this);
		Boolean status = util.IsNetConnected(AboutUs.this);
		if (status) {
			new About_Us().execute();

		} else {
			util.showAlertNoInternetService(AboutUs.this);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title || v.getId() == R.id.menuLayout) {
			finish();
			 overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);   
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
	 private class About_Us extends AsyncTask<String, Void, String> {
			private static final String CONTENTPAGES = "ContentPages";
			private static final String DESCRIPTION = "Description";
		
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {

				super.onPreExecute();
				progressDialog = new ProgressDialog(AboutUs.this); 
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
								if (name.equalsIgnoreCase(CONTENTPAGES)) {
								} else {
									 if (name.equalsIgnoreCase(DESCRIPTION)) {
										
										desc_string=parser.nextText();
										
										System.out.println(desc_string);
										
									} 
								}

								break;
							case XmlPullParser.END_TAG:
								name = parser.getName();
								if (name.equalsIgnoreCase(CONTENTPAGES)) {

								}
								break;
							}
							eventType = parser.next();
						}
						desc.setText(Html.fromHtml(desc_string));

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
}