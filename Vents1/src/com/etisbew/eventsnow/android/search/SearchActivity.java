package com.etisbew.eventsnow.android.search;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eventsnow.android.Details;
import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.MainActivity;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.beans.CategoriesBean;
import com.etisbew.eventsnow.android.beans.EventsBean;
import com.etisbew.eventsnow.android.util.Utility;

@SuppressLint("ClickableViewAccessibility")
public class SearchActivity extends Activity implements OnClickListener {
	Button search;
	EventBean event;
	EditText category, search_keyword;
	TextView back, event_title, ccategory, cevent;
	Utility util;
	ArrayList<CategoriesBean> categories_details;
	 ArrayList<EventsBean> events_upcoming;
	int sel_id;
	ArrayList<String> item1,item2,item3;
	ListView lv, lv1;
	RelativeLayout rl, rl1;
	CustomAdapter adapter;
	CustomAdapter1 adapter1;
	CustomAdapter2 adapter2;

	ArrayList<Integer> category_id;
	ArrayList<String> category_name;
	ArrayList<Integer> event_ids;
	 String title1, description, startdate, enddate, category1, venue1, thumbnail1,
		city, region, cperson, web, email, phoneno, gps,
		facebook1, twitter1,event_type,ext_link;
		String url3;
		int event_id;
int flag=1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

		category_name = new ArrayList<String>();
		category_id = new ArrayList<Integer>();
		category_name.add("All categories");
		category_id.add(0);
		 
		event_ids = new ArrayList<Integer>();
		 
		event = (EventBean) getApplicationContext();
		
		events_upcoming = event.getObject();
		util = new Utility(SearchActivity.this);
		categories_details = event.getCategories();

		for (int i = 0; i < categories_details.size(); i++) {
			CategoriesBean category = categories_details.get(i);
			System.out.println("id value" + category.categoryId);
			if (!category_name.contains(category.categoryName))
				category_name.add(category.categoryName);
				category_id.add(category.categoryId);
		}

		item1 = new ArrayList<String>();
		item2 = new ArrayList<String>();
		item3 = new ArrayList<String>();
		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		search = (Button) findViewById(R.id.search);

		lv = (ListView) findViewById(R.id.listView1);
		lv1 = (ListView) findViewById(R.id.listView2);

		category = (EditText) findViewById(R.id.category);
		search_keyword = (EditText) findViewById(R.id.search_keyword);

		event_title.setTypeface(event.getTextBold());
		search.setTypeface(event.getTextBold());
		category.setTypeface(event.getTextNormal());
		search_keyword.setTypeface(event.getTextNormal());

		ccategory = (TextView) findViewById(R.id.textView2);
		cevent = (TextView) findViewById(R.id.textView3);

		back.setOnClickListener(this);
		event_title.setOnClickListener(this);
		search.setOnClickListener(this);

		ccategory.setOnClickListener(this);
		cevent.setOnClickListener(this);
		

		rl = (RelativeLayout) findViewById(R.id.selectcategory);
		rl1 = (RelativeLayout) findViewById(R.id.selectkeyword);

		category.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				rl.setVisibility(View.VISIBLE);
				rl1.setVisibility(View.INVISIBLE);
				ccategory.setVisibility(View.INVISIBLE);
				cevent.setVisibility(View.GONE);
				System.out.println("ddd"+event);
				flag=1;
				item3.clear();
				adapter = new CustomAdapter(SearchActivity.this, category_name);
				adapter.notifyDataSetChanged();
				lv.setAdapter(adapter);
				search_keyword.setText("");
				return false; 
			}
		});
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View view, int i, long l) {
				 
				if(item2.size()>0){
					int pos=Integer.parseInt(item2.get(i));
					/*Toast.makeText(SearchActivity.this, "myPos " + category_id.get(pos)+category_name.get(pos),
							Toast.LENGTH_LONG).show();*/
					category.setText(category_name.get(pos));
					sel_id=category_id.get(pos);
				}else{
					/*Toast.makeText(SearchActivity.this, "myPos " + category_id.get(i)+category_name.get(i),
							Toast.LENGTH_LONG).show();*/
					category.setText(category_name.get(i));
					sel_id=category_id.get(i);
				}
				
			}
		});
		lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View view, int i, long l) {
				int id1;
				if(item3.size()>0){
					int pos=Integer.parseInt(item3.get(i));
					id1=event_ids.get(pos);
					/*Toast.makeText(SearchActivity.this, "myPos " + event_ids.get(pos)+item1.get(pos),
							Toast.LENGTH_LONG).show();*/
		
				}else{
					id1=event_ids.get(i);
				
				}
				url3 = util.setEventsDetails(id1);
				Boolean status = util.IsNetConnected(SearchActivity.this);
				if (status) {
					new GettingEventsDetails().execute();
				} else {
					util.showAlertNoInternetService(SearchActivity.this);
				}
            
				/*
				 * Intent in = new Intent(Articles.this, ArticlesInner.class);
				 * if (searchString.size() > 0) { int pos =
				 * Integer.parseInt(searchString.get(i)); in.putExtra("id",
				 * articleId.get(pos)); } else { in.putExtra("id",
				 * articleId.get(i)); }
				 * 
				 * startActivity(in);
				 */
			}
		});
		category.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				item2.clear();
				item3.clear();
				ccategory.setVisibility(View.VISIBLE);
				cevent.setVisibility(View.INVISIBLE);
				for (int i = 0; i < category_name.size(); i++) { 
					String string = category_name.get(i).toLowerCase(Locale.US);
					if (string.contains(category.getText().toString().toLowerCase(Locale.US))) {
						if (!item2.contains(String.valueOf(i)))
							item2.add(String.valueOf(i));
					}
				}
				if (item2.size() > 0) {
					lv.setVisibility(View.VISIBLE);
					adapter1 = new CustomAdapter1(SearchActivity.this, item2);
					adapter1.notifyDataSetChanged();
					lv.setAdapter(adapter1);
				}else{
					lv.setVisibility(View.INVISIBLE);
					util.dialogExample1("No categories found");
				}
			}
		});

		search_keyword.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				rl1.setVisibility(View.VISIBLE);
				rl.setVisibility(View.INVISIBLE);
				ccategory.setVisibility(View.INVISIBLE);
				cevent.setVisibility(View.INVISIBLE);
				flag=2;
				
				if (category.getText().toString().length() > 1 && sel_id!=0) {
					item1.clear();
					event_ids.clear();
					for (int i = 0; i < events_upcoming.size(); i++) {
						EventsBean eve = events_upcoming.get(i);
						if (eve.categoryId==sel_id){
							item1.add(eve.title); 
							event_ids.add(eve.id);
						} 
					}
					System.out.println("if block" + item1 + item1.size()+event_ids);
					adapter = new CustomAdapter(SearchActivity.this, item1);
					adapter.notifyDataSetChanged();
					lv1.setAdapter(adapter);

				} else if(sel_id == 0) {
					item1.clear();
					event_ids.clear();
					// util.dialogExample1("Select Category");
					for (int i = 0; i < events_upcoming.size(); i++) {
						EventsBean eve = events_upcoming.get(i);
						item1.add(eve.title);
						event_ids.add(eve.id);
					}
					System.out.println("else if block" + item1 + item1.size()+event_ids);
					adapter = new CustomAdapter(SearchActivity.this, item1);
					adapter.notifyDataSetChanged();
					lv1.setAdapter(adapter);
				} else{
					item1.clear();
					event_ids.clear();
						for (int i = 0; i < events_upcoming.size(); i++) {
						EventsBean eve = events_upcoming.get(i); 
						System.out.println("on touch else id value" + eve.id);
						item1.add(eve.title);
						event_ids.add(eve.id);
					}
					System.out.println("else block" + item1 + item1.size()+event_ids);
					adapter = new CustomAdapter(SearchActivity.this, item1);
					adapter.notifyDataSetChanged();
					lv1.setAdapter(adapter);
				}

				return false;
			}
		});
		search_keyword.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				ccategory.setVisibility(View.INVISIBLE);
				if(flag==1){
				cevent.setVisibility(View.INVISIBLE);
				}else if(flag==2){
					cevent.setVisibility(View.VISIBLE);
				}
				System.out.println("sel_id"+sel_id+search_keyword.getText().toString().toLowerCase(Locale.US)+item1.size());
				if (category.getText().toString().length() > 1 && sel_id!=0) {
					item3.clear();
					for (int i = 0; i < item1.size(); i++) {
						String str=item1.get(i);
							if (str.toLowerCase(Locale.US).contains(search_keyword.getText().toString().toLowerCase(Locale.US)))
							item3.add(String.valueOf(i));
					}
					System.out.println("item3 if" + item3 + item3.size()+item1.size()+item1+event_ids.size()+event_ids);
					adapter2 = new CustomAdapter2(SearchActivity.this, item3);
					adapter2.notifyDataSetChanged();
					lv1.setAdapter(adapter2);
				} else if(sel_id == 0) {
					item3.clear();
					for (int i = 0; i < item1.size(); i++) {
						String str=item1.get(i);
						if (str.toLowerCase(Locale.US).contains(search_keyword.getText().toString().toLowerCase(Locale.US)))

							item3.add(String.valueOf(i));
					}
					System.out.println("item3 else if" + item3 + item3.size()+item1.size()+item1+event_ids.size()+event_ids);
					adapter2 = new CustomAdapter2(SearchActivity.this, item3);
					adapter2.notifyDataSetChanged();
					lv1.setAdapter(adapter2);
					
				} else {
					item3.clear();
					for (int i = 0; i < item1.size(); i++) {
						String str=item1.get(i);
						if (str.toLowerCase(Locale.US).contains(search_keyword.getText().toString().toLowerCase(Locale.US)))

							item3.add(String.valueOf(i));
					}
					System.out.println("item3 else else" + item3 + item3.size()+item1.size()+item1+event_ids.size()+event_ids);
					adapter2 = new CustomAdapter2(SearchActivity.this, item3);
					adapter2.notifyDataSetChanged();
					lv1.setAdapter(adapter2);
				/*	item1.clear();
					// util.dialogExample1("Select Category");
					for (int i = 0; i < events_upcoming.size(); i++) {
						EventsBean eve = events_upcoming.get(i);
						System.out.println("on text changed else id value"
								+ eve.id);
						if (eve.title.toLowerCase(Locale.US).contains(
								search_keyword.getText().toString()
										.toLowerCase(Locale.US))) {
							item1.add(eve.title);
						}
					}
					System.out.println("item1" + item1 + item1.size());
					adapter1 = new CustomAdapter(SearchActivity.this, item1);
					adapter1.notifyDataSetChanged();
					lv1.setAdapter(adapter1);*/
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title) {
			finish();
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
		} else if (v.getId() == R.id.search) {
			Intent in = new Intent(SearchActivity.this, MainActivity.class);
			in.putExtra("category", category.getText().toString());
			in.putExtra("keyword", search_keyword.getText().toString());
			System.out.println("values" + category.getText().toString() + search_keyword.getText().toString());
			startActivity(in);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
		} else if (v.getId() == R.id.textView2) {
			category.setText("");
			search_keyword.setText("");
		} else if (v.getId() == R.id.textView3) {
			search_keyword.setText("");
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

	public class CustomAdapter extends BaseAdapter {

		LayoutInflater mInflater;
		ArrayList<String> search;

		public CustomAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		public CustomAdapter(Context context, ArrayList<String> search) {
			mInflater = LayoutInflater.from(context);
			this.search = search;
		}

		@Override
		public int getCount() {

			return search.size();

		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.search_custom_layout,
						parent, false);
				holder = new ViewHolder();
				holder.tv = (TextView) convertView
						.findViewById(R.id.singleItem);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(search.get(position));
			holder.tv.setTypeface(event.getTextBold());

			return convertView;

		}

	}

		public class CustomAdapter1 extends BaseAdapter {

		LayoutInflater mInflater;
		ArrayList<String> search;

		public CustomAdapter1(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		public CustomAdapter1(Context context, ArrayList<String> search) {
			mInflater = LayoutInflater.from(context);
			this.search = search;
		}

		@Override
		public int getCount() {

			return search.size();

		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			int pos=Integer.parseInt(search.get(position));
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.search_custom_layout,
						parent, false);
				holder = new ViewHolder();
				holder.tv = (TextView) convertView.findViewById(R.id.singleItem);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(category_name.get(pos));
			holder.tv.setTypeface(event.getTextBold());

			return convertView;

		}

	}
		public class CustomAdapter2 extends BaseAdapter {

			LayoutInflater mInflater;
			ArrayList<String> search;

			public CustomAdapter2(Context context) {
				mInflater = LayoutInflater.from(context);

			}

			public CustomAdapter2(Context context, ArrayList<String> search) {
				mInflater = LayoutInflater.from(context);
				this.search = search;
			}

			@Override
			public int getCount() {

				return search.size();

			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder;
				int pos=Integer.parseInt(item3.get(position));
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.search_custom_layout,parent, false);
					holder = new ViewHolder();
					holder.tv = (TextView) convertView.findViewById(R.id.singleItem);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.tv.setText(item1.get(pos));
				holder.tv.setTypeface(event.getTextBold());

				return convertView;

			}

		}

	static class ViewHolder {

		TextView tv;

	}
	private class GettingEventsDetails extends AsyncTask<String, Void, String> {

		private static final String EVENTDETAILRESPONSE = "EventDetailResponse";
		private static final String EVENTDETAIL = "EventDetail";
		private static final String ID = "ID";
		private static final String UNIQUENAME = "UniqueName";
		private static final String DESCRIPTION = "Description";
		private static final String TITLE = "Title";
		private static final String STARTDATE = "StartDate";
		private static final String ENDDATE = "EndDate";
		private static final String CATEGORYID = "CategoryID";
		private static final String CATEGORY = "Category";
		private static final String VENU = "Venu";
		private static final String THUMBNAIL = "Thumbnail";
		private static final String URL = "Url";
		private static final String CITY = "City";
		private static final String REGION = "Region";
		private static final String GPSLOCATION = "GPSLocation";
		private static final String CONTACTINFO = "ContactInfo";
		private static final String WEBSITE = "Website";
		private static final String EMAIL = "Email";
		private static final String PHONE = "Phone";
		private static final String MOBILE = "Mobile";
		private static final String FAX = "Fax";
		private static final String FACEBOOK = "Facebook";
		private static final String TWITTER = "Twitter";
		private static final String EVENTTYPE = "EventType";
		private static final String EXTLINK = "ExtLink";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(SearchActivity.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
 
		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(url3);

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
					int i = 0;
					while (eventType != XmlPullParser.END_DOCUMENT) {
						String name = null;
						switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(EVENTDETAILRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									event_id = Integer.parseInt(parser
											.nextText());
								} else if (name.equalsIgnoreCase(UNIQUENAME)) {
									// uniqueName.add(parser.nextText());
								} else if (name.equalsIgnoreCase(TITLE)) {
									if (i == 0) {
										title1 = parser.nextText();
									}
									i++;
								} else if (name.equalsIgnoreCase(STARTDATE)) {
									startdate = parser.nextText();
								} else if (name.equalsIgnoreCase(ENDDATE)) {
									enddate = parser.nextText();
								} else if (name.equalsIgnoreCase(DESCRIPTION)) {
									description = parser.nextText();
								} else if (name.equalsIgnoreCase(CATEGORY)) {
									category1 = parser.nextText();
								} else if (name.equalsIgnoreCase(VENU)) {
									venue1 = parser.nextText();
								} else if (name.equalsIgnoreCase(THUMBNAIL)) {
									thumbnail1 = parser.nextText();
								} else if (name.equalsIgnoreCase(CITY)) {
									city = parser.nextText();
								} else if (name.equalsIgnoreCase(REGION)) {
									region = parser.nextText();
								} else if (name.equalsIgnoreCase(GPSLOCATION)) {
									gps = parser.nextText();
								} else if (name.equalsIgnoreCase(CONTACTINFO)) {
									cperson = parser.nextText();
								} else if (name.equalsIgnoreCase(WEBSITE)) {
									web = parser.nextText();
								} else if (name.equalsIgnoreCase(EMAIL)) {
									email = parser.nextText();
								} else if (name.equalsIgnoreCase(PHONE)) {
									phoneno = parser.nextText();
								} else if (name.equalsIgnoreCase(FACEBOOK)) {
									facebook1 = parser.nextText();
								} else if (name.equalsIgnoreCase(TWITTER)) {
									twitter1 = parser.nextText();
								} else if (name.equalsIgnoreCase(URL)) {
									// url1.add(parser.nextText());
								}else if (name.equalsIgnoreCase(EVENTTYPE)) {
									event_type=parser.nextText();
								}else if (name.equalsIgnoreCase(EXTLINK)) {
									ext_link=parser.nextText();
								}
							}

							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(EVENTDETAILRESPONSE)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				update2();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
  
		}  
 
	} 
public void update2(){
		
		System.out.println("dates"+startdate+enddate+gps+event_id);
		Intent intent = new Intent(SearchActivity.this, Details.class); 
		intent.putExtra("event_id", event_id);
		intent.putExtra("title", title1);
		intent.putExtra("startdate", String.valueOf(startdate));
		intent.putExtra("enddate", String.valueOf(enddate));
		intent.putExtra("description", description);
		intent.putExtra("category", category1);
		intent.putExtra("venue", venue1);
		intent.putExtra("thumbnail", thumbnail1);
		intent.putExtra("city", city);
		intent.putExtra("region", region);
		intent.putExtra("gps", gps);
		intent.putExtra("cperson", cperson);
		intent.putExtra("web", web);
		intent.putExtra("email", email);
		intent.putExtra("phoneno", phoneno);
		intent.putExtra("twitter1", twitter1);
		intent.putExtra("facebook1", facebook1);
		intent.putExtra("event_type", event_type);
		intent.putExtra("ext_link", ext_link);
	 	startActivity(intent);
    	overridePendingTransition(R.anim.trans_left_in,R.anim.trans_left_out);
		/*Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
		getActivity().startActivity(intent, bundle);*/
	}

}