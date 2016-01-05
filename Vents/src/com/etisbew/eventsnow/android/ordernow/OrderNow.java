package com.etisbew.eventsnow.android.ordernow;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.beans.StatesDetails;
import com.etisbew.eventsnow.android.database.EventsNowConnection;
import com.etisbew.eventsnow.android.util.Utility;

@SuppressLint("UseSparseArrays")
public class OrderNow extends Activity implements OnClickListener, OnItemSelectedListener, OnItemClickListener {
	TextView back, event_title, tfname, temail, tphone, payoptions,toggle_title,
			ticket_title,baddress;
	RelativeLayout menu_layout;
	EditText name, email, phone,address;
	EventBean event;
	Utility util;
	CheckBox accept, iam;
	ArrayList<Integer> my_list;
	ListView lv;
	Button next, ordernow;
	Map<Integer, String> e_name;
	Map<Integer, String> e_email;
	Map<Integer, String> e_phone;
	ToggleButton togglebutton;
	ScrollView scrollView1;
	Map<Integer, String> details;
	ArrayList<Integer> quantity;
	ArrayList<Integer> tickets_quantitiy;
	ArrayList<Integer> tickets_id;
	ArrayList<Integer> tickets_dummy_id;
	ArrayList<Integer> tickets_price;
	ArrayList<Double> tickets_tax;
	ArrayList<Integer> tickets_price1;
	ArrayList<Double> tickets_tax1;
	ArrayList<String> title;
	ArrayList<Integer> ticket_promo1;
	Map<Integer, Integer> key_index;
	Map<Integer, String> key_value;
	ArrayList<Integer> ticket_promo;
	ArrayList<Integer> code_max_allowed;
	JSONObject json = new JSONObject();
	int total;
	Double tax=0.0;
	Double discount=0.0; 
	int payment_type=1;
	EventsNowConnection enc;
	StatesDetails states;
	RelativeLayout relativeLayout3; 
	AutoCompleteTextView ccity, cstate;
	ArrayList<Integer> state_id;
	ArrayList<String> state_name;
	ArrayList<Integer> city_id;
	ArrayList<String> city_name;
	int state_given_id, city_given_id; 
	int select_flag;
	String cities_url;
	ArrayAdapter adapter, adapter1; 
	int i=0;
	SharedPreferences pref;
	Editor editor;
	@SuppressWarnings("unchecked") 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ordernow);
		Intent iin = getIntent(); 
		Bundle extras = iin.getExtras(); 
		event = (EventBean) getApplicationContext();
		
		pref = getApplicationContext().getSharedPreferences("MyPref",
				MODE_PRIVATE);
		editor = pref.edit();
		if(event.getUserId()>0){
			
		}else{
		if(pref.getInt("user_id", 0)>0){
			event.setUserId(pref.getInt("user_id", 0));
			event.setUserName(pref.getString("user_name", ""));
			event.setUser_image(pref.getString("user_image", ""));
			event.setEmail(pref.getString("email", ""));
			event.setPhone(pref.getString("phone", ""));
			event.setAddress(pref.getString("address", ""));
			event.setState_name(pref.getString("state", ""));
			event.setCity_name(pref.getString("city", ""));
			event.setState_id(pref.getInt("state_id", 0));
			event.setCity_id(pref.getInt("city_id", 0));
			 
			editor.commit();
		}
		}
		state_id = new ArrayList<Integer>();
		state_name = new ArrayList<String>();

		city_id = new ArrayList<Integer>();
		city_name = new ArrayList<String>();
		ccity = (AutoCompleteTextView) findViewById(R.id.ccity);
		cstate = (AutoCompleteTextView) findViewById(R.id.cstate);
		address = (EditText)findViewById(R.id.address);
		address.setText(event.getAddress().replace("%20", " "));
		ccity.setText(event.getCity_name());
		cstate.setText(event.getState_name());
		city_given_id=event.getCity_id();
		state_given_id=event.getState_id();
		enc = new EventsNowConnection(OrderNow.this.getApplicationContext());
		List<StatesDetails> allTags = enc.getAllItemDetails();
		for (StatesDetails tag : allTags) {
			state_id.add(tag.getState_id());
			state_name.add(tag.getState_name());

		}
		if (extras != null)
		/*	details = (HashMap<Integer, String>) iin
			.getSerializableExtra("details");*/
			
			 
	//	details = event.getDetails();	 
		System.out.println("details"+event.getDetails());
		Map<Integer, String> treeMap = new TreeMap<Integer, String>(
				new Comparator<Integer>() {

					@Override
					public int compare(Integer o1, Integer o2) {
						return o1.compareTo(o2);
					}

				});
		treeMap.putAll(event.getDetails());
		details = treeMap;

		
		util = new Utility(OrderNow.this);

		quantity = new ArrayList<Integer>();
		title = new ArrayList<String>();
		tickets_id = new ArrayList<Integer>();
		tickets_dummy_id = new ArrayList<Integer>();
		tickets_price = new ArrayList<Integer>(); 
		tickets_tax = new ArrayList<Double>();
		tickets_price1 = new ArrayList<Integer>();
		tickets_quantitiy = new ArrayList<Integer>();
		ticket_promo = new ArrayList<Integer>();
		tickets_tax1 = new ArrayList<Double>();
		ticket_promo1 = new ArrayList<Integer>();
		code_max_allowed = new ArrayList<Integer>();

		e_name = new HashMap<Integer, String>();
		e_email = new HashMap<Integer, String>();
		e_phone = new HashMap<Integer, String>();

		key_index = new HashMap<Integer, Integer>();
		key_value = new HashMap<Integer, String>();

		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);

		menu_layout = (RelativeLayout) findViewById(R.id.menuLayout);

		togglebutton = (ToggleButton) findViewById(R.id.togglebutton);

		scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
		
		relativeLayout3 = (RelativeLayout) findViewById(R.id.relativeLayout3);

		togglebutton.setText(null);
		togglebutton.setTextOn(null);
		togglebutton.setTextOff(null);
		togglebutton.setOnClickListener(new OnClickListener(){
		    @Override
		    public void onClick(View view){
		       
		        if(payment_type ==2){
		        	payment_type=1;
		        } else if(payment_type ==1){
		        	payment_type=2;
		        }
		    }
		});
 
		tfname = (TextView) findViewById(R.id.tfname);
		temail = (TextView) findViewById(R.id.temail);
		tphone = (TextView) findViewById(R.id.tphone);
		payoptions = (TextView) findViewById(R.id.payoptions);
		payoptions = (TextView) findViewById(R.id.payoptions);
		baddress = (TextView) findViewById(R.id.baddress);
		 
		toggle_title = (TextView) findViewById(R.id.toggle_title);
		
		toggle_title.setVisibility(View.GONE);
		togglebutton.setVisibility(View.GONE);
		relativeLayout3.setVisibility(View.GONE);

		name = (EditText) findViewById(R.id.fname);
		email = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phone);

	
		next = (Button) findViewById(R.id.next);
		ordernow = (Button) findViewById(R.id.ordernow);

		back.setOnClickListener(this);
		event_title.setOnClickListener(this);
		menu_layout.setOnClickListener(this);
		next.setOnClickListener(this);
		ordernow.setOnClickListener(this);
		
		
		event_title.setTypeface(event.getTextBold());
		payoptions.setTypeface(event.getTextBold()); 

		next.setTypeface(event.getTextBold());
		baddress.setTypeface(event.getTextBold());

		tfname.setTypeface(event.getTextNormal());
		temail.setTypeface(event.getTextNormal());
		tphone.setTypeface(event.getTextNormal());
	
		name.setTypeface(event.getTextNormal());
		email.setTypeface(event.getTextNormal());
		phone.setTypeface(event.getTextNormal());

		name.setText(event.getUserName());
		email.setText(event.getEmail()); 
		phone.setText(event.getPhone());
   
		for (Map.Entry<Integer, String> entry : details.entrySet()) {
			Integer key = entry.getKey();
			String thing = entry.getValue();
			String[] str = thing.split(":");
			quantity.add(Integer.parseInt(str[1]));
			title.add(str[0]);
			tickets_id.add(Integer.parseInt(str[4]));
			tickets_price.add(Integer.parseInt(str[2]));
			tickets_tax.add(Double.parseDouble(str[3]));
			int amount=Integer.parseInt(str[1])*Integer.parseInt(str[2]);
			total=total+amount;
			if(Double.parseDouble(str[3]) != 0.0){
			tax=tax+amount*Double.parseDouble(str[3])/100;
			}else{
				tax=tax+0.0;
			}
			if(str.length>6){
				ticket_promo.add(Integer.parseInt(str[5]));
				code_max_allowed.add(Integer.parseInt(str[7]));
				if(Integer.parseInt(str[8]) ==1){
			if(Integer.parseInt(str[1])<=Integer.parseInt(str[7])){
				discount=discount+((Integer.parseInt(str[1])*Integer.parseInt(str[2]))*Double.parseDouble(str[6]))/100;
				}else{
					discount=discount+Integer.parseInt(str[7])*Integer.parseInt(str[2])*Double.parseDouble(str[6])/100;	
				}
				}else{
					if(Integer.parseInt(str[1])<=Integer.parseInt(str[7])){
						discount=discount+(Integer.parseInt(str[1])*Integer.parseInt(str[8]));
						}else{
							discount=discount+Integer.parseInt(str[7])*Integer.parseInt(str[8]);	
						}
				}
				 
			}else{
				ticket_promo.add(0);
				code_max_allowed.add(0);
			}

		}

		my_list = new ArrayList<Integer>();

		int count = 0;
		for (int k = 0; k < quantity.size(); k++) {

			for (int j = 0; j < quantity.get(k); j++) {

				my_list.add(j);
				tickets_dummy_id.add(tickets_id.get(k));
				tickets_price1.add(tickets_price.get(k));
				tickets_tax1.add(tickets_tax.get(k));
				tickets_quantitiy.add(quantity.get(k));
				if(code_max_allowed.get(k)<=10){
				ticket_promo1.add(ticket_promo.get(k));
				}else{
					ticket_promo1.add(0);
				}

			}
			try {
				if (k == 0) {

					int ss = quantity.get(k);
					key_index.put(k, k);
					key_value.put(k, title.get(k));
				} else if (k >= 1) {

					int ss = quantity.get(k - 1);
					count = count + ss;
					key_index.put(count, count);
					key_value.put(count, title.get(k));
				}
			} catch (Exception e) {

			}

		}
		if(event.getCollectinfo() == 2){
			
		}else if(event.getCollectinfo() ==1){
			setNearby1();
		}
		
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
			adapter = new ArrayAdapter<String>(OrderNow.this,
				android.R.layout.simple_dropdown_item_1line, state_name);
		cstate.setThreshold(0);
		cstate.setAdapter(adapter);
		cstate.setOnItemSelectedListener(this);
		cstate.setOnItemClickListener(this);
	}

	public void setCategory1() {
	
		if(select_flag ==2){
		if(city_name.size()>0){
			adapter1 = new ArrayAdapter<String>(OrderNow.this,
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
		
			Boolean status = util.IsNetConnected(OrderNow.this);
			if (status) {
				new GetCities().execute();

			} else {
				util.showAlertNoInternetService(OrderNow.this);
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

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title
				|| v.getId() == R.id.menuLayout) {
			finish();

			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
		} else if (v.getId() == R.id.next) {
				
		if(event.getCollectinfo() ==2){
			try {
				String address1=address.getText().toString().trim();
				address1=address1.replace("\n","%20");
				address1.replace(" ", "%20");
				json.put("first_name", name.getText().toString());
				json.put("email", email.getText().toString()); 
				json.put("phone", phone.getText().toString());    
				json.put("event_id", event.getEventId());
				json.put("user_id", event.getUserId());   
				json.put("myticketd", my_list.size()); 
				json.put("total_amount", total);
				json.put("service_charge", tax);
				json.put("total_discount_amount", discount);
				json.put("payment_type", payment_type);
				json.put("address", address1);
				json.put("state_id", state_given_id);
				json.put("city_id", city_given_id);
				json.put("country_id", 110);
	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(event.getCollectinfo() ==1){
			try {
				String address1=address.getText().toString().trim();
				address1=address1.replace("\n","%20");
				address1.replace(" ", "%20");
				json.put("first_name", name.getText().toString());
				json.put("email", email.getText().toString()); 
				json.put("phone", phone.getText().toString());    
				json.put("event_id", event.getEventId());
				json.put("user_id", event.getUserId());   
				json.put("myticketd", my_list.size()); 
				json.put("total_amount", total);
				json.put("service_charge", tax);
				json.put("total_discount_amount", discount);
				json.put("payment_type", payment_type);
				json.put("address", address1);
				json.put("state_id", state_given_id);
				json.put("city_id", city_given_id);
				json.put("country_id", 110);
						int l=0;
						JSONArray jarray = new JSONArray(); 
						JSONObject json1 = new JSONObject();
				for (int i = 0; i < quantity.size(); i++) {
					
				for (int j = 0; j < quantity.get(i); j++) {
					JSONObject manJson = new JSONObject();
				 
					manJson.put("first_name", e_name.get(l));
					manJson.put("email", e_email.get(l));
					manJson.put("phone", e_phone.get(l));
					manJson.put("event_id", event.getEventId());
					manJson.put("ticket_id", tickets_dummy_id.get(l));
					manJson.put("ticket_price", tickets_price1.get(l));
					manJson.put("ticket_tax", tickets_tax1.get(l));
					manJson.put("ticket_quantity", tickets_quantitiy.get(l));
					manJson.put("user_id", event.getUserId());
					if(l<10){
						manJson.put("promotion_id", ticket_promo1.get(l));
						}else{
							manJson.put("promotion_id", "0");
						}
					
				
					jarray.put(manJson);

					
					json1.put(""+l, manJson);
					l=l+1;
					
					}
				json.put("Tickets" , json1);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
			System.out.println("json" + json);
		

			if(event.getCollectinfo() ==2){ 
				if (name.getText().toString().length() > 0
						&& email.getText().toString().length() > 0
						&& phone.getText().toString().length() > 0 
						&& address.getText().toString().length() > 0
						&& String.valueOf(state_given_id).length() > 1
						&& String.valueOf(city_given_id).length() > 1) {
					if (!util.validEmail(email.getText().toString())) {
						util.dialogExample1("Please enter a valid email address");

					}else if(phone.getText().toString().length()<10){
						util.dialogExample1("Please enter valid phone");
						
				}else{
					Intent intent = new Intent(OrderNow.this,
							PaymentOptions.class);
					event.setDetails(details);
					intent.putExtra("json", json.toString());
					startActivity(intent);
					overridePendingTransition(R.anim.trans_left_in,
							R.anim.trans_left_out);
					}
				} else {
					util.dialogExample1("All fields are mandatory"); 
				}
			
			}else if(event.getCollectinfo() ==1){
			if (my_list.size() == e_name.size()
					&& my_list.size() == e_email.size()
					&& my_list.size() == e_phone.size()
					&& name.getText().toString().length() > 0
					&& email.getText().toString().length() > 0
					&& phone.getText().toString().length() > 0
					&& address.getText().toString().length() > 0
					&& String.valueOf(state_given_id).length() > 1
					&& String.valueOf(city_given_id).length() > 1) {
				if (!util.validEmail(email.getText().toString())) {
					util.dialogExample1("Please enter a valid email address");

				}else if(phone.getText().toString().length()<10){
					util.dialogExample1("Please enter valid phone");
					
			} else{
				ArrayList<Integer>flag = new ArrayList<Integer>();
				ArrayList<Integer>flag1 = new ArrayList<Integer>();
				ArrayList<Integer>flag2 = new ArrayList<Integer>();
				for(int i=0;i<my_list.size();i++){
					String email=e_email.get(i); 
					if (!util.validEmail(email)) { 
					
						flag.add(2);
					} else {
						flag.add(1);
					}
					String name=e_name.get(i); 
					if (name.length()==0) {
						
						flag1.add(2);
					} else {
						flag1.add(1);
					}
					String phone=e_phone.get(i);
					if (phone.length()<10) {
					
						flag2.add(2);
					} else {
						flag2.add(1);
					}
				 
				}
				int flag_dup=Collections.frequency(flag, 1);
				int flag_dup1=Collections.frequency(flag1, 1);
				int flag_dup2=Collections.frequency(flag2, 1);
				
				if (flag_dup==flag.size() && flag_dup1==flag.size() && flag_dup2==flag.size() ) {
					Intent intent = new Intent(OrderNow.this,
							PaymentOptions.class);  
					event.setDetails(details);
					intent.putExtra("json", json.toString());
					startActivity(intent);
					overridePendingTransition(R.anim.trans_left_in,
							R.anim.trans_left_out);
				}else if(flag_dup!=flag.size()){
					util.dialogExample1("Please enter a valid email address");
				}else if(flag_dup1!=flag.size()){
					util.dialogExample1("Please enter valid name");
				}else if(flag_dup2!=flag.size()){
					util.dialogExample1("Please enter valid phone");
				}
				}
			} else {
				util.dialogExample1("All fields are mandatory"); 
		} 
			}
		} else if (v.getId() == R.id.ordernow) {
			Intent intent = new Intent(OrderNow.this, OrderSummary.class);
			System.out.println("check details"+details);
			intent.putExtra("details", (TreeMap) details);
			startActivity(intent);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
		}
	}
 
	@Override
	public void onBackPressed() {
		finish(); 

		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	class ViewHolder {
		EditText caption;
		EditText caption1;
		EditText caption2;
		CheckBox iam;
		LinearLayout main_layout;
		TextView ticket_title;
	}

	class ListItem {
		String caption;
	}

	@SuppressLint("NewApi")
	public void setNearby1() {
		LinearLayout mLinearListView = (LinearLayout) findViewById(R.id.linear_listview2);
		mLinearListView.removeAllViews();
		for (int i = 0; i < my_list.size(); i++) {
			final ViewHolder holder;   
			LayoutInflater inflater = null; 
			inflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View mLinearView = inflater
					.inflate(R.layout.custom_order_now, null);
			holder = new ViewHolder(); 
  
			holder.caption = (EditText) mLinearView.findViewById(R.id.fname1);
			holder.caption1 = (EditText) mLinearView.findViewById(R.id.email1);
			holder.caption2 = (EditText) mLinearView.findViewById(R.id.phone1);

			holder.ticket_title = (TextView) mLinearView
					.findViewById(R.id.ticket_title);

			holder.main_layout = (LinearLayout) mLinearView
					.findViewById(R.id.main_layout);
			try {
				if (i == key_index.get(i)) {

					holder.main_layout.setVisibility(View.VISIBLE);
					holder.ticket_title
							.setText(Html.fromHtml(key_value.get(i)));

				} else {
					holder.main_layout.setVisibility(View.GONE);
					if (Build.VERSION.SDK_INT > 11) {
					scrollView1.smoothScrollTo(0, (int) menu_layout.getY());
					}
				}
			} catch (Exception e) {
				holder.main_layout.setVisibility(View.GONE);
				if (Build.VERSION.SDK_INT > 11) {
				scrollView1.smoothScrollTo(0, (int) menu_layout.getY());
				}
			}
			holder.iam = (CheckBox) mLinearView.findViewById(R.id.iam);

			holder.iam
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub

							if (buttonView.isChecked()) {
								holder.caption.setText(event.getUserName());
								holder.caption1.setText(event.getEmail());
								holder.caption2.setText(event.getPhone());
							} else {
								holder.caption.setText("");
								holder.caption1.setText("");
								holder.caption2.setText("");
							}

						}
					});

			final int j = i;
			holder.caption.addTextChangedListener(new TextWatcher() {
				@SuppressLint("UseSparseArrays")
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					e_name.put(j, s.toString());
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});
			holder.caption1.addTextChangedListener(new TextWatcher() {
				@SuppressLint("UseSparseArrays")
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					e_email.put(j, s.toString());
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});
			holder.caption2.addTextChangedListener(new TextWatcher() {
				@SuppressLint("UseSparseArrays")
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					e_phone.put(j, s.toString());
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});

			mLinearListView.addView(mLinearView);
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
		if (select_flag == 1) {
			for (int i = 0; i < state_name.size(); i++) {
				String str = state_name.get(i);
				if (str.contains(cstate.getText().toString())) {
					state_given_id = state_id.get(i);
					city_id.clear();
					city_name.clear();
					cities_url = util.setCities(state_id.get(i));
					
					Boolean status = util.IsNetConnected(OrderNow.this);
					if (status) {
						new GetCities().execute();

					} else {
						util.showAlertNoInternetService(OrderNow.this);
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
	private class GetCities extends AsyncTask<String, Void, String> {
		private static final String CITYRESPONSE = "CityResponse";
		private static final String ID = "id";
		private static final String CITYNAME = "cityname";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(OrderNow.this);
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
}
