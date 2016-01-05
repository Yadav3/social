package com.etisbew.eventsnow.android;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.etisbew.eventsnow.android.beans.StatesDetails;
import com.etisbew.eventsnow.android.database.EventsNowConnection;
import com.etisbew.eventsnow.android.util.Utility;

public class MyAccountFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener, OnItemClickListener {
	Button update, update1;
	TextView tfname, tcname, tphone, temail, taddress, topwd, tnpwd, tcpwd,
			tccity, tcstate;
	Button b1, b2, b3, b4;
	EventBean event;
	RelativeLayout r1, r2;
	Utility util;
	int response_id;
	String url, url1;
	int user_id;
	EditText fname, cname, email, phone, address, opwd, npwd, cpwd;
	AutoCompleteTextView ccity, cstate;
	ImageView iv;
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
	int i=0;
	String u_name,u_phone,u_address;
	SharedPreferences pref;
	Editor editor;
	public static final String MyPREFERENCES = "MyPrefs";
//	ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void onResume(){
		System.out.println("id"+event.getUserId());
		if(event.getUserId()==0)
		{
			startActivity(new Intent(getActivity(),MainActivity.class));
		}
		super.onResume();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// return inflater.inflate(R.layout.page_one, container, false);
		View my_account = inflater.inflate(R.layout.my_account, container,
				false);

		event = (EventBean) getActivity().getApplicationContext();
		pref = this.getActivity().getApplicationContext().getSharedPreferences("MyPref",
				Context.MODE_PRIVATE);
		editor = pref.edit();
		util = new Utility(getActivity());

		b1 = (Button) my_account.findViewById(R.id.button1);
		b2 = (Button) my_account.findViewById(R.id.button2);
		b3 = (Button) my_account.findViewById(R.id.button3);
		b4 = (Button) my_account.findViewById(R.id.button4);
		update = (Button) my_account.findViewById(R.id.update);
		update1 = (Button) my_account.findViewById(R.id.update1);

		state_id = new ArrayList<Integer>();
		state_name = new ArrayList<String>();

		city_id = new ArrayList<Integer>();
		city_name = new ArrayList<String>();

		fname = (EditText) my_account.findViewById(R.id.fname);
		enc = new EventsNowConnection(getActivity().getApplicationContext());
		List<StatesDetails> allTags = enc.getAllItemDetails();
		for (StatesDetails tag : allTags) {
			state_id.add(tag.getState_id());
			state_name.add(tag.getState_name());

		}
		cname = (EditText) my_account.findViewById(R.id.cname);
		ccity = (AutoCompleteTextView) my_account.findViewById(R.id.ccity);
		cstate = (AutoCompleteTextView) my_account.findViewById(R.id.cstate);
		email = (EditText) my_account.findViewById(R.id.email);
		phone = (EditText) my_account.findViewById(R.id.phone);
		address = (EditText) my_account.findViewById(R.id.address);
		opwd = (EditText) my_account.findViewById(R.id.opwd);
		npwd = (EditText) my_account.findViewById(R.id.npwd);
		cpwd = (EditText) my_account.findViewById(R.id.cpwd);

		iv = (ImageView) my_account.findViewById(R.id.imageView1);

		androidAQuery = new AQuery(getActivity());
		email.setKeyListener(null);

		r1 = (RelativeLayout) my_account.findViewById(R.id.relativeLayout2);
		r2 = (RelativeLayout) my_account.findViewById(R.id.relativeLayout3);

		b1.setTypeface(event.getTextBold());
		b2.setTypeface(event.getTextBold());
	

		fname.setTypeface(event.getTextNormal());
		cname.setTypeface(event.getTextNormal());
		ccity.setTypeface(event.getTextNormal());
		cstate.setTypeface(event.getTextNormal());
		email.setTypeface(event.getTextNormal());
		phone.setTypeface(event.getTextNormal());
		address.setTypeface(event.getTextNormal());
		opwd.setTypeface(event.getTextNormal());
		npwd.setTypeface(event.getTextNormal());
		cpwd.setTypeface(event.getTextNormal());

		b1.setTypeface(event.getTextBold());
		b2.setTypeface(event.getTextBold());
		b2.setTextColor(getResources().getColor(R.color.hash));
		update.setTypeface(event.getTextBold());
		update1.setTypeface(event.getTextBold());

		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		update.setOnClickListener(this);
		update1.setOnClickListener(this);
		url1 = util.setMyAccount(event.getUserId());
		
		
		

	
		Boolean status = util.IsNetConnected(getActivity());
		if (status) {
			new MyAccountDetails().execute();

		} else {
			util.showAlertNoInternetService(getActivity());
		}
		androidAQuery.id(iv).image(event.getUser_image(), true, true);

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

		return my_account;
	}

	public void setCategory() {
		System.out.println("state_names" + state_name.size() + state_name);

		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, state_name);
		cstate.setThreshold(0);
		cstate.setAdapter(adapter);
		cstate.setOnItemSelectedListener(this);
		cstate.setOnItemClickListener(this);
	}

	public void setCategory1() {
		System.out.println("state_names" + state_name.size() + state_name);
		
		if(select_flag ==2){
		if(city_name.size()>0){
			adapter1 = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_dropdown_item_1line, city_name);
			ccity.setThreshold(0);
			ccity.setAdapter(adapter1);
			ccity.setOnItemSelectedListener(this);
			ccity.setOnItemClickListener(this);
		}else{
			if(state_given_id!=0){
			cities_url = util.setCities(state_given_id);
			System.out.println("given url"+cities_url);
			city_id.clear();
			city_name.clear();
			
			Boolean status = util.IsNetConnected(getActivity());
			if (status) {
				new GetCities().execute(); 

			} else {
				util.showAlertNoInternetService(getActivity());
			}
			}else {
				if(i%2==0){
					util.dialogExample1("Select state first");
					}
					i=i+1;
			}
		}
		}
	}

	@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 
			if (v.getId() == R.id.button1) {
				
				r1.setVisibility(View.VISIBLE);
				r2.setVisibility(View.GONE);
				
				b1.setTypeface(event.getTextBold());
				b2.setTypeface(event.getTextBold());
				b1.setTextColor(getResources().getColor(R.color.white));
				b2.setTextColor(getResources().getColor(R.color.hash));
				b3.setBackgroundColor(getResources().getColor(R.color.red));
				b4.setBackgroundColor(getResources().getColor(R.color.black));
			}
			if (v.getId() == R.id.button2) {
				r1.setVisibility(View.GONE);
				r2.setVisibility(View.VISIBLE);
				b1.setTypeface(event.getTextBold());
				b2.setTypeface(event.getTextBold());
				b2.setTextColor(getResources().getColor(R.color.white));
				b1.setTextColor(getResources().getColor(R.color.hash));
				b3.setBackgroundColor(getResources().getColor(R.color.black));
				b4.setBackgroundColor(getResources().getColor(R.color.red));
			}
			if (v.getId() == R.id.update1) {
				if (opwd.getText().toString().length() > 0
						&& npwd.getText().toString().length() > 0
						&& cpwd.getText().toString().length() > 0) {
					if (npwd.getText().toString().equals(cpwd.getText().toString())) {
						url = util.setChangePwd(event.getUserId(), opwd.getText()
								.toString(), npwd.getText().toString());
					
						Boolean status = util.IsNetConnected(getActivity());
						if (status) {
							new ChangePwd().execute();

						} else {
							util.showAlertNoInternetService(getActivity());
						}
					} else {
						util.dialogExample1("New password & confirm password doesn't match ");
					}
				} else { 
					util.dialogExample1("All fields are mandatory "); 
				}
			}
			if(v.getId() == R.id.update){
				String address1=address.getText().toString().trim();
				address1=address1.replace("\n","%20"); 
				address1.replace(" ", "%20");  
				u_name=fname.getText().toString();  
				u_phone=phone.getText().toString();
				u_address=address1.replace(" ", "%20");
				if (fname.getText().toString().length() > 0 && cname.getText().toString().length()>0 && address.getText().toString().length()>0 && phone.getText().toString().length()>0 && String.valueOf(state_given_id).length() > 0
						&& String.valueOf(city_given_id).length() > 0){
					int len=phone.getText().toString().length();
			  
			if(len<10){
					util.dialogExample1("Please enter valid phone");
			}else{
				update_profile=util.setUpdateMyAccount(event.getUserId(), fname.getText().toString().replace(" ", "%20"), cname.getText().toString().replace(" ", "%20"), state_given_id, city_given_id, address1.replace(" ", "%20"), phone.getText().toString());
				System.out.println("profile url"+update_profile);
				Boolean status = util.IsNetConnected(getActivity());
				if (status) {
					new UpdateProfile().execute();

				} else {
					util.showAlertNoInternetService(getActivity()); 
				}
			}
			}else{ 
				util.dialogExample1("All fields are mandatory ");
			}
			
			}
	}

		private class ChangePwd extends AsyncTask<String, Void, String> {
			private static final String ACCOUNTRESPONSE = "AccountResponse";
			private static final String RESPONSE = "Response";
			private static final String RESULTCODE = "ResultCode";
			private static final String ERRORMESSAGE = "ErrorMessage";
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() { 

				super.onPreExecute();
				progressDialog = new ProgressDialog(getActivity());
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
				System.out.println("result" + result);

			
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
								if (name.equalsIgnoreCase(ACCOUNTRESPONSE)) {
								} else {
									if (name.equalsIgnoreCase(RESULTCODE)) {
										response_id = Integer.parseInt(parser
												.nextText());
										System.out.println("in if" + response_id);

									}
								}

								break;
							case XmlPullParser.END_TAG:
								name = parser.getName();
								if (name.equalsIgnoreCase(ACCOUNTRESPONSE)) {

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
		if (response_id > 0) {
			util.dialogExample1("Password changed successfully");
		} else {
			util.dialogExample1("Password changed request failure");
		}
	}

	private class MyAccountDetails extends AsyncTask<String, Void, String> {
		private static final String RESPONSE = "Response";
		private static final String USER = "User";
		private static final String ID = "ID";
		private static final String EMAIL = "email";
		private static final String USERNAME = "username";
		private static final String COMPANYNAME = "Companyname";
		private static final String STATE = "State";
		private static final String CITY = "City";
		private static final String STATE_ID = "State_id";
		private static final String CITY_ID = "City_id";
		private static final String PHONENUMBER = "Phonenumber";
		private static final String ADDRESS = "Address";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(url1);

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
							if (name.equalsIgnoreCase(RESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									user_id = Integer.parseInt(parser
											.nextText());
								} else if (name.equalsIgnoreCase(EMAIL)) {
									email.setText(parser.nextText());
								} else if (name.equalsIgnoreCase(COMPANYNAME)) {
									cname.setText(parser.nextText());
								} else if (name.equalsIgnoreCase(USERNAME)) {
									fname.setText(parser.nextText());
								} else if (name.equalsIgnoreCase(STATE)) {
									given_state = parser.nextText();
									cstate.setText(given_state);
								} else if (name.equalsIgnoreCase(CITY)) {
									String city = parser.nextText();
									ccity.setText(city);

								} else if (name.equalsIgnoreCase(PHONENUMBER)) {
									phone.setText(parser.nextText());
								} else if (name.equalsIgnoreCase(ADDRESS)) {
									address.setText(parser.nextText());
								} else if (name.equalsIgnoreCase(STATE_ID)) {
									state_given_id = Integer.parseInt(parser
											.nextText());
								} else if (name.equalsIgnoreCase(CITY_ID)) {
									city_given_id = Integer.parseInt(parser
											.nextText());
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

			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		System.out.println("new " + state_given_id + city_given_id);
		if (select_flag == 1) {
			System.out.println("position" + position
					+ cstate.getText().toString());
			for (int i = 0; i < state_name.size(); i++) {
				String str = state_name.get(i);
				if (str.contains(cstate.getText().toString())) {
					System.out.println("id" + state_id.get(i));
					state_given_id = state_id.get(i);
				
					city_id.clear();
					city_name.clear();
					cities_url = util.setCities(state_id.get(i));
					
					Boolean status = util.IsNetConnected(getActivity());
					if (status) {
						new GetCities().execute();

					} else {
						util.showAlertNoInternetService(getActivity());
					}
					// }
				}
			}
		} else if (select_flag == 2) {
			for (int i = 0; i < city_name.size(); i++) {
				String str = city_name.get(i);
				if (str.contains(ccity.getText().toString())) {
					System.out.println("id" + city_id.get(i));
					city_given_id = city_id.get(i);

				}
			}
		}

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
			progressDialog = new ProgressDialog(getActivity());
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
			System.out.println("result" + result);

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
			progressDialog = new ProgressDialog(getActivity());
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
			System.out.println("result" + result);

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
			event.setUserName(u_name);
			event.setPhone(u_phone);
			event.setAddress(u_address);
			editor.putString("user_name", u_name);
			editor.putString("phone", u_phone);
			editor.putString("address", u_address);
			editor.commit();
			util.dialogExample1("Update successfully");
		
		} else {
			util.dialogExample1(update_msg);
		}
	}

}
