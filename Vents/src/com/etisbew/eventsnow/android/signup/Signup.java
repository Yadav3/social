package com.etisbew.eventsnow.android.signup;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.beans.StatesDetails;
import com.etisbew.eventsnow.android.database.EventsNowConnection;
import com.etisbew.eventsnow.android.login.LoginActivity;
import com.etisbew.eventsnow.android.util.Utility;

public class Signup extends Activity implements OnClickListener,
		OnItemSelectedListener, OnItemClickListener {
	TextView back, event_title, tfname, tcname, tphone, temail,
			topwd, tnpwd, tcpwd, tccity, tcstate;
	EditText fname, cname, email, phone, pwd, cpwd;
	AutoCompleteTextView ccity, cstate;
	CheckBox accept;
	EventBean event;
	Utility util;
	AQuery androidAQuery;
	EventsNowConnection enc;
	StatesDetails states;
	ArrayList<Integer> state_id;
	ArrayList<String> state_name;
	ArrayAdapter adapter, adapter1;
	ArrayList<Integer> city_id;
	ArrayList<String> city_name;
	int state_given_id, city_given_id;
	int select_flag;
	String cities_url, update_profile;
	String given_state;
	int update_id;
	String update_msg;
	Button update;
	int i=0;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		event = (EventBean) getApplicationContext();

		util = new Utility(Signup.this); 

		event_title = (TextView) findViewById(R.id.event_title);
		back = (TextView) findViewById(R.id.textView1);
		tfname = (TextView) findViewById(R.id.tfname);
		tccity = (TextView) findViewById(R.id.tccity);
		tcname = (TextView) findViewById(R.id.tcname);
		tcstate = (TextView) findViewById(R.id.tcstate);
		tphone = (TextView) findViewById(R.id.tphone);
		temail = (TextView) findViewById(R.id.temail);
		accept = (CheckBox) findViewById(R.id.accept);

		tnpwd = (TextView) findViewById(R.id.tnpwd);
		tcpwd = (TextView) findViewById(R.id.tcpwd);

		update = (Button) findViewById(R.id.update);

		state_id = new ArrayList<Integer>();
		state_name = new ArrayList<String>();

		city_id = new ArrayList<Integer>(); 
		city_name = new ArrayList<String>(); 

		fname = (EditText) findViewById(R.id.fname);
		enc = new EventsNowConnection(getApplicationContext());
		List<StatesDetails> allTags = enc.getAllItemDetails();
		for (StatesDetails tag : allTags) {
			state_id.add(tag.getState_id());
			state_name.add(tag.getState_name());

		}

		cname = (EditText) findViewById(R.id.cname);
		ccity = (AutoCompleteTextView) findViewById(R.id.ccity);
		cstate = (AutoCompleteTextView) findViewById(R.id.cstate);
		email = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phone);
		pwd = (EditText) findViewById(R.id.pwd);
		cpwd = (EditText) findViewById(R.id.cpwd);

		androidAQuery = new AQuery(Signup.this);
		event_title.setTypeface(event.getTextBold());

		tfname.setTypeface(event.getTextNormal());
		tcname.setTypeface(event.getTextNormal());
		tccity.setTypeface(event.getTextNormal());
		tcstate.setTypeface(event.getTextNormal());
		tphone.setTypeface(event.getTextNormal());
		temail.setTypeface(event.getTextNormal());
			tnpwd.setTypeface(event.getTextNormal());
		tcpwd.setTypeface(event.getTextNormal());
		accept.setTypeface(event.getTextNormal());

		fname.setTypeface(event.getTextNormal());
		cname.setTypeface(event.getTextNormal());
		ccity.setTypeface(event.getTextNormal());
		cstate.setTypeface(event.getTextNormal());
		email.setTypeface(event.getTextNormal());
		phone.setTypeface(event.getTextNormal());
		pwd.setTypeface(event.getTextNormal());
		cpwd.setTypeface(event.getTextNormal());

		update.setTypeface(event.getTextBold());

		back.setOnClickListener(this);
		update.setOnClickListener(this);
		event_title.setOnClickListener(this);
		cstate.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				select_flag = 1;
				setCategory();
				return false;
			}
		});
		ccity.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				select_flag = 2;
				setCategory1();
				return false;
			}
		});

	}

	public void setCategory() {
	
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, state_name);
		cstate.setThreshold(0);
		cstate.setAdapter(adapter);
		cstate.setOnItemSelectedListener(this);
		cstate.setOnItemClickListener(this);
	}

	public void setCategory1() {

		if(select_flag ==2){
		if(city_name.size()>0){  
			adapter1 = new ArrayAdapter<String>(Signup.this,
					android.R.layout.simple_dropdown_item_1line, city_name);
			ccity.setThreshold(0);
			ccity.setAdapter(adapter1); 
			ccity.setOnItemSelectedListener(this);
			ccity.setOnItemClickListener(this);
		}else{
			
			if(state_given_id!=0){
			cities_url = util.setCities(state_given_id);
			city_id.clear();
			city_name.clear();
			
			Boolean status = util.IsNetConnected(Signup.this);
			if (status) {
				new GetCities().execute();

			} else {
				util.showAlertNoInternetService(Signup.this);
			}
			}else {
				
				if(i%2==0){
					util.dialogExample1("Please select state first");
				}
				i=i+1;
			}
		}
	
		}
	}
 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title
				|| v.getId() == R.id.menuLayout) {
			Signup.this.finish();
			startActivity(new Intent(Signup.this, LoginActivity.class));
			overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
		}
		if (v.getId() == R.id.update) {
			if (email.getText().toString().length() > 0
					&& pwd.getText().toString().length() > 0
					&& cpwd.getText().toString().length() > 0
					&& fname.getText().toString().length() > 0
					&& String.valueOf(state_given_id).length() > 0
					&& String.valueOf(city_given_id).length() > 0
					&& phone.getText().toString().length() > 0
					&& accept.isChecked()) {

				if (!util.validEmail(email.getText().toString())) {
					util.dialogExample1("Please enter a valid email address");
				}else if(phone.getText().toString().length()<10){
					util.dialogExample1("Please enter valid phone");
				}else{
				if (pwd.getText().toString().equals(cpwd.getText().toString())) {
					update_profile = util.setNewMyAccount(fname.getText()
							.toString().replace(" ", "%20"), email.getText().toString(), pwd
							.getText().toString(), cpwd.getText().toString(),
							cname.getText().toString().replace(" ", "%20"), phone.getText()
									.toString(), state_given_id, city_given_id);
					
					Boolean status = util.IsNetConnected(Signup.this);
					if (status) {
						new UpdateProfile().execute();

					} else {
						util.showAlertNoInternetService(Signup.this);
					}
				} else {
					util.dialogExample1("Password & confirm password doesn't match ");
				}
				}
			} else {
				util.dialogExample1("All fields marked(*) are mandatory");
			}
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (select_flag == 1) {
			for (int i = 0; i < state_name.size(); i++) {
				String str = state_name.get(i);
				if (str.contains(cstate.getText().toString())) {
					state_given_id = state_id.get(i);
					cities_url = util.setCities(state_id.get(i));
					city_id.clear();
					city_name.clear();
					
					Boolean status = util.IsNetConnected(Signup.this);
					if (status) {
						new GetCities().execute();

					} else {
						util.showAlertNoInternetService(Signup.this);
					}
				}
			}
		} else if (select_flag == 2) {
			for (int i = 0; i < city_name.size(); i++) {
				String str = city_name.get(i);
				if (str.contains(ccity.getText().toString())) {
					city_given_id = city_id.get(i);

				}
			}
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	private class GetCities extends AsyncTask<String, Void, String> {
		private static final String CITYRESPONSE = "CityResponse";
		private static final String RESPONSE = "Response";
		private static final String ID = "id";
		private static final String CITYNAME = "cityname";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(Signup.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(cities_url);

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
							if (name.equalsIgnoreCase(CITYRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									city_id.add(Integer.parseInt(parser
											.nextText()));
								} else if (name.equalsIgnoreCase(CITYNAME)) {
									city_name.add(parser.nextText());
								}
							}

							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(CITYRESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				setCategory1();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	private class UpdateProfile extends AsyncTask<String, Void, String> {
		private static final String CREATEUSERRESPONSE = "CreateUserResponse";
		private static final String RESPONSE = "Response";
		private static final String RESULTCODE = "ResultCode";
		private static final String ERRORMESSAGE = "ErrorMessage";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(Signup.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(update_profile);

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
							if (name.equalsIgnoreCase(CREATEUSERRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(RESULTCODE)) {
									update_id = Integer.parseInt(parser
											.nextText());
								} else if (name.equalsIgnoreCase(ERRORMESSAGE)) {
									update_msg = parser.nextText();
								}
							}

							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(CREATEUSERRESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				updateMsg();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	public void updateMsg() {
		if (update_id > 0) {
			util.dialogExample1(update_msg);
		} else {
			util.dialogExample1(update_msg);
		}
	}
	@Override
	public void onResume() {
		super.onResume();
	
	}
	@Override
	public void onBackPressed() {
		
		  super.onBackPressed();
		 overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
		 
	}
}
