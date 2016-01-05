package com.etisbew.eventsnow.android.ordernow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
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
import com.etisbew.eventsnow.android.util.Utility;

@SuppressLint("UseSparseArrays")
public class OrderNow extends Activity implements OnClickListener {
	TextView back, event_title, tfname, temail, tphone, payoptions,toggle_title,
			ticket_title;
	RelativeLayout menu_layout;
	EditText name, email, phone;
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
	Map<Integer, Integer> key_index;
	Map<Integer, String> key_value;
	JSONObject json = new JSONObject();
	int total;
	Double tax=0.0;
	int payment_type=2;
	RelativeLayout relativeLayout3;
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ordernow);
		Intent iin = getIntent(); 
		Bundle extras = iin.getExtras();
		event = (EventBean) getApplicationContext();
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
		System.out.println("intent" + details);

		
		util = new Utility(OrderNow.this);

		quantity = new ArrayList<Integer>();
		title = new ArrayList<String>();
		tickets_id = new ArrayList<Integer>();
		tickets_dummy_id = new ArrayList<Integer>();
		tickets_price = new ArrayList<Integer>();
		tickets_tax = new ArrayList<Double>();
		tickets_price1 = new ArrayList<Integer>();
		tickets_quantitiy = new ArrayList<Integer>();
		tickets_tax1 = new ArrayList<Double>();


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
		        	// Toast.makeText(getApplicationContext(), "Click!"+payment_type, Toast.LENGTH_SHORT).show();
		    }
		});
 
		tfname = (TextView) findViewById(R.id.tfname);
		temail = (TextView) findViewById(R.id.temail);
		tphone = (TextView) findViewById(R.id.tphone);
		payoptions = (TextView) findViewById(R.id.payoptions);
		payoptions = (TextView) findViewById(R.id.payoptions);
		
		toggle_title = (TextView) findViewById(R.id.toggle_title);
		
		toggle_title.setVisibility(View.GONE);
		togglebutton.setVisibility(View.GONE);
		relativeLayout3.setVisibility(View.GONE);

		name = (EditText) findViewById(R.id.fname);
		email = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phone);

		// accept = (CheckBox) findViewById(R.id.accept);

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

		tfname.setTypeface(event.getTextNormal());
		temail.setTypeface(event.getTextNormal());
		tphone.setTypeface(event.getTextNormal());
		// accept.setTypeface(event.getTextNormal());

		name.setTypeface(event.getTextNormal());
		email.setTypeface(event.getTextNormal());
		phone.setTypeface(event.getTextNormal());

		name.setText(event.getUserName());
		email.setText(event.getEmail());
		phone.setText(event.getPhone());

		for (Map.Entry<Integer, String> entry : details.entrySet()) {
			Integer key = entry.getKey();
			String thing = entry.getValue();
			System.out.println("values" + key + thing);
			String[] str = thing.split(":");
			quantity.add(Integer.parseInt(str[1]));
			title.add(str[0]);
			tickets_id.add(Integer.parseInt(str[4]));
			tickets_price.add(Integer.parseInt(str[2]));
			tickets_tax.add(Double.parseDouble(str[3]));
			int amount=Integer.parseInt(str[1])*Integer.parseInt(str[2]);
			total=total+amount;
			if(Double.parseDouble(str[3]) != 0.0){
			System.out.println("amm"+amount+Double.parseDouble(str[3]));
			tax=tax+amount*Double.parseDouble(str[3])/100;
			}else{
				tax=tax+0.0;
			}

		}

		System.out.println("title+quantity" + title + quantity);
		my_list = new ArrayList<Integer>();

		int count = 0;
		for (int k = 0; k < quantity.size(); k++) {

			for (int j = 0; j < quantity.get(k); j++) {

				my_list.add(j);
				tickets_dummy_id.add(tickets_id.get(k));
				tickets_price1.add(tickets_price.get(k));
				tickets_tax1.add(tickets_tax.get(k));
				tickets_quantitiy.add(quantity.get(k));

			}
			try {
				if (k == 0) {

					int ss = quantity.get(k);
					key_index.put(k, k);
					key_value.put(k, title.get(k));
				} else if (k >= 1) {

					int ss = quantity.get(k - 1);
					count = count + ss;
					System.out.println("else if" + k + ss + title.get(k));
					key_index.put(count, count);
					key_value.put(count, title.get(k));
				}
			} catch (Exception e) {

			}

		}
		System.out.println("types" + key_index + key_value);
		/*
		 * my_list.add(1);
		 * 
		 * lv = (ListView) findViewById(R.id.listView1); MyAdapter adapter = new
		 * MyAdapter(); adapter.notifyDataSetChanged(); lv.setAdapter(adapter);
		 */
		setNearby1();

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title
				|| v.getId() == R.id.menuLayout) {
			finish();

			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
		} else if (v.getId() == R.id.next) {
			
			int size = my_list.size() * 3;
			String[] result_array = new String[size + 7];
			result_array[0] = "[_method] => POST";
			result_array[1] = "[first_name] => " + name.getText().toString();
			result_array[2] = "[email] => " + email.getText().toString();
			result_array[3] = "[phone] => " + phone.getText().toString();
			result_array[4] = "[event_id] => 83";
			result_array[5] = "[myticketd] => 1";
			result_array[6] = "[data] => POST";

			System.out.println("my_array" + Arrays.deepToString(result_array));
			
			
		try {
				json.put("first_name", name.getText().toString());
				json.put("email", email.getText().toString()); 
				json.put("phone", phone.getText().toString());    
				json.put("event_id", event.getEventId());
				json.put("user_id", event.getUserId());   
				json.put("myticketd", my_list.size()); 
				json.put("total_amount", total);
				json.put("service_charge", tax);
				json.put("payment_type", payment_type);
				System.out.println("mylist" + tickets_id + tickets_id.size()
						+ e_name + key_index+my_list.size());
						int l=0;
						JSONArray jarray = new JSONArray(); 
						JSONObject json1 = new JSONObject();
				for (int i = 0; i < quantity.size(); i++) {
					
				for (int j = 0; j < quantity.get(i); j++) {
				System.out.println("l value"+l);
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
			
			System.out.println("json" + json);
		
			

/*	try {
				json.put("first_name", name.getText().toString());
				json.put("email", email.getText().toString());
				json.put("phone", phone.getText().toString());
				json.put("event_id", event.getEventId());
				json.put("total_amount", total);
				json.put("service_charge", tax);
				json.put("myticketd", my_list.size());
				JSONArray jarray;
				JSONObject new_json = new JSONObject();
				System.out.println("mylist" + tickets_id + tickets_id.size()
						+ e_name + key_index+my_list.size());
				for (int i = 0; i < my_list.size(); i++) {
					JSONObject manJson = new JSONObject();
					JSONObject manJson1 = new JSONObject();
					
					jarray = new JSONArray(); 
					// jarray.put(55);

					manJson.put("first_name", e_name.get(i));
					manJson.put("email", e_email.get(i));
					manJson.put("phone", e_phone.get(i)); 

					manJson.put("event_id", event.getEventId());
					manJson.put("ticket_id", tickets_dummy_id.get(i));
					manJson.put("user_id", event.getUserId());
				
					jarray.put(manJson);
	 

					json.put(""+i, jarray);
					//json.put(""+tickets_dummy_id.get(i), new_json);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("json" + json);*/
			if (my_list.size() == e_name.size()
					&& my_list.size() == e_email.size()
					&& my_list.size() == e_phone.size()
					&& name.getText().toString().length() > 0
					&& email.getText().toString().length() > 0
					&& phone.getText().toString().length() > 0) {
				int flag = 1,flag1=1,flag2=1;
				for(int i=0;i<my_list.size();i++){
				/*for (Map.Entry<Integer, String> entry : e_email.entrySet()) {
					Integer key = entry.getKey();
					String thing = entry.getValue();
					System.out.println("values" + key + thing);
					if (!util.validEmail(thing)) {
						flag = 2;
						util.dialogExample1("Please Enter valid Email");

					} else {
						flag = 1;
					}
				}*/
					String email=e_email.get(i);
					if (!util.validEmail(email)) {
						flag = 2;
					} else {
						flag = 1;
					}
					String name=e_name.get(i);
					if (name.length()==0) {
						flag1 = 2;
					} else {
						flag1 = 1;
					}
					String phone=e_phone.get(i);
					if (phone.length() ==9) {
						flag2 = 2;
					} else {
						flag2 = 1;
					}
				
				}
				
				if (flag == 1 && flag1 == 1 && flag2 == 1) {
					Intent intent = new Intent(OrderNow.this,
							PaymentOptions.class);
					event.setDetails(details);
					intent.putExtra("json", json.toString());
					startActivity(intent);
					overridePendingTransition(R.anim.trans_left_in,
							R.anim.trans_left_out);
					//new Webservice().execute(); 
				}else if(flag == 2){
					util.dialogExample1("Please Enter valid Email");
				}else if(flag1 == 2){
					util.dialogExample1("Please Enter valid Name");
				}else if(flag1 == 2){
					util.dialogExample1("Please Enter valid Phone");
				}

			} else {
				util.dialogExample1(" * Fields are mandatory"); 
				/* 
				 * Intent intent = new Intent(OrderNow.this,
				 * PaymentOptions.class); intent.putExtra("details", (TreeMap)
				 * details); startActivity(intent);
				 * overridePendingTransition(R.anim.trans_right_in,
				 * R.anim.trans_right_out); 
				 */ 
			}
		} else if (v.getId() == R.id.ordernow) {
			Intent intent = new Intent(OrderNow.this, OrderSummary.class);
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

	/*
	 * public class CustomAdapter1 extends BaseAdapter {
	 * 
	 * LayoutInflater mInflater;
	 * 
	 * // TextView add,price;
	 * 
	 * public CustomAdapter1(Context context) { mInflater =
	 * LayoutInflater.from(context);
	 * 
	 * }
	 * 
	 * @Override public int getCount() { return my_list.size();
	 * 
	 * }
	 * 
	 * @Override public Object getItem(int position) { return position; }
	 * 
	 * @Override public long getItemId(int position) { return position; }
	 * 
	 * @Override public View getView(final int position, View convertView,
	 * ViewGroup parent) {
	 * 
	 * 
	 * convertView = mInflater.inflate(R.layout.custom_order_now, parent,
	 * false);
	 * 
	 * EditText ed1 = (EditText)findViewById(R.id.editText1); EditText ed2 =
	 * (EditText)findViewById(R.id.editText2); EditText ed3 =
	 * (EditText)findViewById(R.id.editText3);
	 * 
	 * ed1.setText(position); ed2.setText(position); ed3.setText(position);
	 * 
	 * 
	 * 
	 * return convertView;
	 * 
	 * }
	 * 
	 * }
	 */
	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		// public ArrayList myItems = new ArrayList();

		public MyAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			/*
			 * for (int i = 0; i < 20; i++) { ListItem listItem = new
			 * ListItem(); listItem.caption = "Caption" + i;
			 * myItems.add(listItem); }
			 */
			notifyDataSetChanged();
		}

		public int getCount() {
			return my_list.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater
						.inflate(R.layout.custom_order_now, null);
				holder.caption = (EditText) convertView
						.findViewById(R.id.fname1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// Fill EditText with the value you have in data source
			// holder.caption.setText(my_list.get(position));
			holder.caption.setId(position);
			holder.caption.setText("hello worlld" + position);

			// we need to update adapter once we finish with editing
			holder.caption
					.setOnFocusChangeListener(new OnFocusChangeListener() {
						public void onFocusChange(View v, boolean hasFocus) {
							if (!hasFocus) {
								final int position = v.getId();
								final EditText Caption = (EditText) v;
								System.out
										.println(Caption.getText().toString());
							}
						}
					});

			return convertView;
		}
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

	public void setNearby1() {
		// TODO Auto-generated method stub
		/*
		 * HorizontalScrollView sv = (HorizontalScrollView)
		 * findViewById(R.id.hscrollview); sv.scrollTo(0, 10);
		 */
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

			/*   
			 * holder.caption .setCursorVisible(true); holder.caption1
			 * .setCursorVisible(true); holder.caption2 .setCursorVisible(true);
			 * holder.caption.setFocusable(true);
			 * holder.caption1.setFocusable(true);
			 * holder.caption2.setFocusable(true);
			 */
			holder.ticket_title = (TextView) mLinearView
					.findViewById(R.id.ticket_title);

			holder.main_layout = (LinearLayout) mLinearView
					.findViewById(R.id.main_layout);
			/*
			 * for (Map.Entry<Integer, String> entry : key_index.entrySet()) {
			 * int key = entry.getKey();
			 */
			try {
				System.out.println("values check" + i + key_index.get(i));
				if (i == key_index.get(i)) {

					holder.main_layout.setVisibility(View.VISIBLE);
					holder.ticket_title
							.setText(Html.fromHtml(key_value.get(i)));

				} else {
					System.out.println("title" + i + key_index.get(i));
					//
					holder.main_layout.setVisibility(View.GONE);
					scrollView1.smoothScrollTo(0, (int) menu_layout.getY());
				}
			} catch (Exception e) {
				holder.main_layout.setVisibility(View.GONE);
				scrollView1.smoothScrollTo(0, (int) menu_layout.getY());
			}
			/*
			 * if (i==key_index.get(i)) {
			 * 
			 * holder.main_layout.setVisibility(View.VISIBLE);
			 * holder.ticket_title.setText(Html.fromHtml(key_value.get(i)));
			 * 
			 * }else{ System.out.println("title"+i+key_index.get(i)); //
			 * holder.main_layout.setVisibility(View.GONE);
			 * scrollView1.smoothScrollTo(0, (int) menu_layout.getY()); }
			 */
			// }

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
					/*
					 * Model element = (Model) viewHolder.scores.getTag();
					 * element.setScore(s.toString());
					 */
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
					/*
					 * HashMap<Integer, String> h2 = new HashMap<Integer,
					 * String>(); System.out.println("caption" + j +
					 * s.toString()); h2.put(j, s.toString());
					 */
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
					/*
					 * HashMap<Integer, String> h3 = new HashMap<Integer,
					 * String>(); System.out.println("caption" + j +
					 * s.toString()); h3.put(j, s.toString()); nn.put(j,
					 * s.toString());
					 */
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
	private class Webservice extends AsyncTask<String, Void, String> {
		private static final String RESULT = "Result";
		private static final String LOGINRESPONSE = "LoginResponse";
		private static final String ID = "ID";
		private static final String USERNAME = "username";
		private static final String PROFILEPIC = "Profilepic";
		private static final String EMAIL = "Email";
		private static final String PHONE = "Phone";
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
			 HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost(util.setService());
			    String result = null; 
			    try {
			        // Add your data
		 	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("data",json.toString()));
			      
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
			        // Execute HTTP Post Request
			        HttpResponse response = httpclient.execute(httppost);  
			        HttpEntity entity = response.getEntity();
					result = EntityUtils.toString(entity, HTTP.UTF_8).trim();
			          
			    } catch (ClientProtocolException e) {
			        // TODO Auto-generated catch block
			    } catch (IOException e) {
			        // TODO Auto-generated catch block
			    }
			    return result;

		}   

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("result1" + result);
			

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}
}
