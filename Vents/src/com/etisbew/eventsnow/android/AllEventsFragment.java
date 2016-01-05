package com.etisbew.eventsnow.android;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.etisbew.eventsnow.android.beans.EventsBean;
import com.etisbew.eventsnow.android.util.Utility;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class AllEventsFragment extends Fragment { 
	EventBean event; 
	ListView lv;    
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<Integer> id_list = new ArrayList<Integer>();
	ArrayList<Integer> id_position = new ArrayList<Integer>(); 
	ArrayList<String> list1 = new ArrayList<String>();
	ArrayList<String> title1 = new ArrayList<String>();
	ArrayList<EventsBean> events_upcoming; 
	AQuery androidAQuery;  
	Utility util;    
	String title, description, startdate, enddate, category, venue, thumbnail,
			city, region, lat, lon, cperson, web, email, phoneno, gps,
			facebook1, twitter1, event_type, ext_link;
	String url;
	int event_id,collectinfo;
	Gallery gallery;
	Button month;
	GalleryImageAdapter13 galImageAdapter;
	ArrayList<String> calendar_dates = new ArrayList<String>();
	ArrayList<String> default_dates = new ArrayList<String>();
	private int selectedImagePosition = 0;
	String todate = ""; 
	String month1; 
	int current_month_id,current_month_id1;
	private TextView selectedImageView;
	Date date = new Date(); 
	String[] MenuItems1 = null;
	Integer[] Menu_pos = null;
	RelativeLayout no_ticket;
	TextView back_home_button;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static final AllEventsFragment newInstance(String sampleText) {
		AllEventsFragment f = new AllEventsFragment();

		Bundle b = new Bundle();
		b.putString("bString", sampleText);
		f.setArguments(b);

		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View home = inflater.inflate(R.layout.page_one, container, false);
		Bundle bundle = this.getArguments();
		
		event = (EventBean) getActivity().getApplicationContext();
		util = new Utility(getActivity());
		androidAQuery = new AQuery(getActivity());
		events_upcoming = event.getObject();
		list.clear();
		id_list.clear();
		calendar_dates.clear();
		default_dates.clear();
		title1.clear();
		id_position.clear();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Date date = new Date();
		String current_date = dateFormat.format(date);
		DateFormat format2 = new SimpleDateFormat("MMM");
		String[] convert_date = current_date.split("-");
		current_month_id = Integer.parseInt(convert_date[1]);
		month1 = String.valueOf(convert_date[1]);
		no_ticket = (RelativeLayout) home.findViewById(R.id.no_ticket); 
 		back_home_button = (TextView) home.findViewById(R.id.back_home_button); 
 		back_home_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), MainActivity.class);
				
				if (Build.VERSION.SDK_INT > 16) {
					Bundle  bundle = ActivityOptions.makeCustomAnimation(getActivity(),
						R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
				getActivity().startActivity(intent, bundle);
				}else{
					startActivity(intent);
				}
			}
		});
		
		if (month1.length() == 1) {
			month1 = "0" + month1;
		}
		Date dt1 = null;
		try {
			dt1 = dateFormat.parse(current_date);
		} catch (java.text.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final String[] MenuItems = { "ALL", "JAN", "FEB", "MAR", "APR", "MAY",
				"JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
	
		String finalDay = format2.format(dt1).toUpperCase();
			try {
			
			for (int i = 0; i < events_upcoming.size(); i++) {
				EventsBean eve = events_upcoming.get(i);
				String[] given_dates = eve.startDate.split("T");
				String[] ss = eve.startDate.split("T");
				String[] month_values = given_dates[0].split("-");

				//if (month1.equalsIgnoreCase(month_values[1])) {
					if (!calendar_dates.contains(ss[0])) {
						calendar_dates.add(ss[0]);

					}
					list.add(eve.category); 
					id_list.add(eve.id);
					title1.add(eve.title);
					id_position.add(i);
				//}

			}
			String conversion = "";
			int count = 0;
			int total_size = 0;
			for (int j = 0; j < 13; j++) {
				String mm;
				for (int i = 0; i < events_upcoming.size(); i++) {
					EventsBean eve = events_upcoming.get(i);
					String[] given_dates = eve.startDate.split("T");
					String[] month_values = given_dates[0].split("-");

					mm = String.valueOf(j);
					if (mm.length() == 1) {
						mm = "0" + mm;
					}

					if (mm.equalsIgnoreCase(month_values[1])) {
						count = count + 1;
					}
				}
				//MenuItems1[j] = MenuItems[j] + "  (" + count + ") ";
				if(count>0){
				conversion=conversion+count+":"+j+",";
				}
				total_size = total_size + count;
				count = 0;
			}
			
			
			String[] ss=conversion.split(",");
			MenuItems1 = new String[ss.length+1];
			Menu_pos = new Integer[ss.length+1];
			int len=ss.length;
			MenuItems1[0]="";
			Menu_pos[0]=0;
			for(int k=0;k<len;k++){ 
				String[] val=ss[k].split(":");
					MenuItems1[k+1] = MenuItems[Integer.parseInt(val[1])] + "  (" + Integer.parseInt(val[0]) + ") ";
				Menu_pos[k+1]=Integer.parseInt(val[1]);
			}
			MenuItems1[0] = MenuItems[0] + "  (" + total_size + ") ";
			for(int l=0;l<Menu_pos.length;l++){
				if(Menu_pos[l].equals(current_month_id)){
						current_month_id1=l;
				}
			}
		} catch (Exception e) {
			startActivity(new Intent(getActivity(), SplashActivity.class));
		}
		try{
			if(event.getDetails().size()>0){
				event.getDetails().clear();
			}
		} catch (Exception e) {
			
		}
		
		gallery = (Gallery) home.findViewById(R.id.gallery1);
		month = (Button) home.findViewById(R.id.button1);

		month.setText(finalDay);
		// month.setBackgroundResource(R.drawable.month_arrows);
		month.setOnClickListener(new View.OnClickListener() {

			@Override 
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setCancelable(true).setSingleChoiceItems(
						MenuItems1, current_month_id1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog1, int pos) {
								// TODO Auto-generated method stub
								
								
										current_month_id=Menu_pos[pos];
										current_month_id1=pos;
										
							
								month.setText(MenuItems[current_month_id]);
								month1 = String.valueOf(current_month_id);
								//current_month_id=pos;
								if (month1.length() == 1) {
									month1 = "0" + month1;
								}
								if(current_month_id1!=0){
									Date date = new Date();
									
									String[] date_conversion=todate.split("-");
								Calendar cal = Calendar.getInstance();
								DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
								todate = dateFormat1.format(date); 
								cal.set(Calendar.MONTH, current_month_id-1);
								//cal.set(Calendar.YEAR, Integer.parseInt(date_conversion[0]));
								int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
								
								default_dates.clear();
								for (int i = 0; i < maxDay; i++) {
									cal.set(Calendar.DAY_OF_MONTH, i + 1);
									
									default_dates.add(dateFormat1.format(cal.getTime()));
								}
								}
								setUserSelected();
								dialog1.cancel();
							}
						});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		
		 
		todate = dateFormat1.format(date); 
		String[] date_conversion=todate.split("-");
		cal.set(Calendar.MONTH, Integer.parseInt(date_conversion[1])-1);
		cal.set(Calendar.YEAR, Integer.parseInt(date_conversion[0]));
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		for (int i = 0; i < maxDay; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i + 1);
			
			default_dates.add(dateFormat1.format(cal.getTime()));
		}
		galImageAdapter = new GalleryImageAdapter13(getActivity(),
				R.layout.item, calendar_dates,default_dates);

		gallery.setAdapter(galImageAdapter);
		// gallery.setSelection(5);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				selectedImagePosition = pos;
				if(calendar_dates.contains(default_dates.get(pos))){
					Toast.makeText(getActivity(), "pos"+pos+default_dates.get(pos), Toast.LENGTH_LONG).show();
					try {
						list.clear();
						id_list.clear();
						title1.clear();
						id_position.clear();
						for (int i = 0; i < events_upcoming.size(); i++) {
							EventsBean eve = events_upcoming.get(i);
							String[] given_dates = eve.startDate.split("T");
							String[] ss = eve.startDate.split("T");
							String[] month_values = given_dates[0].split("-");

							if (default_dates.get(pos).equalsIgnoreCase(
									given_dates[0])) {
								list.add(eve.category);
								id_list.add(eve.id);
								title1.add(eve.title);
								id_position.add(i);
							}

						}
						CustomAdapter adapter = new CustomAdapter(getActivity(),
								list);
						lv.setAdapter(adapter);
					} catch (Exception e) {
						startActivity(new Intent(getActivity(),
								SplashActivity.class));
					}
					changeBorderForSelectedImage(selectedImagePosition);
					setSelectedImage(selectedImagePosition);
				}
				

				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		lv = (ListView) home.findViewById(R.id.listView1);
		CustomAdapter adapter = new CustomAdapter(getActivity(), list);
		lv.setAdapter(adapter);
		try {
			String str = (String) this.getActivity().getIntent().getExtras()
					.get("category");
			String str1 = (String) this.getActivity().getIntent().getExtras()
					.get("keyword");
			list1.clear();
			
			if (str.equalsIgnoreCase("All categories") && str1.length() == 0) {
				lv = (ListView) home.findViewById(R.id.listView1);
				CustomAdapter adapter1 = new CustomAdapter(getActivity(), list);
				lv.setAdapter(adapter1);
			} else {
				if (str.length() > 0 && str1.length() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String string = list.get(i).toLowerCase(Locale.US);
						String string1 = title1.get(i).toLowerCase(Locale.US);
						System.out.println("values are "+str+str1+"::"+string+string1);
						if (string.contains(str.toLowerCase(Locale.US)) && string1.contains(str1.toLowerCase(Locale.US))) {
							if (!list1.contains(string))
								list1.add(String.valueOf(i));
 
						}else if(str.equalsIgnoreCase("All categories")){
							if (string1.contains(str1.toLowerCase(Locale.US))) {
								if (!list1.contains(string))
									list1.add(String.valueOf(i));

							}
						}
					}
					if(list1.size()>0){
					lv = (ListView) home.findViewById(R.id.listView1);
					CustomAdapter1 adapter1 = new CustomAdapter1(getActivity(),
							list, list1);
					lv.setAdapter(adapter1);
					}else{
						lv.setVisibility(View.GONE);
						no_ticket.setVisibility(View.VISIBLE);
					}

				} else if (str.length() > 0 && str1.length() == 0) {
					for (int i = 0; i < list.size(); i++) {
						String string1 = list.get(i).toLowerCase(Locale.US);
						if (string1.contains(str.toLowerCase(Locale.US))) {
							list1.add(String.valueOf(i));
						}
					}
					if(list1.size()>0){
					lv = (ListView) home.findViewById(R.id.listView1);
					CustomAdapter1 adapter1 = new CustomAdapter1(getActivity(),
							list, list1);
					lv.setAdapter(adapter1);
					}else{
						lv.setVisibility(View.GONE);
						no_ticket.setVisibility(View.VISIBLE);
					}
				} else if (str.length() == 0 && str1.length() > 0) {
					for (int i = 0; i < title1.size(); i++) {
						String string1 = title1.get(i).toLowerCase(Locale.US);
						if (string1.contains(str1.toLowerCase(Locale.US))) {
							list1.add(String.valueOf(i));
						}
					}
					if(list1.size()>0){
					lv = (ListView) home.findViewById(R.id.listView1);

					CustomAdapter1 adapter1 = new CustomAdapter1(getActivity(),
							list, list1);
					lv.setAdapter(adapter1);
				}else{
					lv.setVisibility(View.GONE);
					no_ticket.setVisibility(View.VISIBLE);
				}
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@SuppressLint("CommitTransaction")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int id1;
				if (list1.size() > 0) {
					id1 = id_list.get(Integer.parseInt(list1.get(position)));
				} else {
					id1 = id_list.get(position);
				}
				event.setEventId(id1);
				url = util.setEventsDetails(id1);
				Boolean status = util.IsNetConnected(getActivity());
				if (status) {
					new GettingEventsDetails().execute();
				} else {
					util.showAlertNoInternetService(getActivity());
				}

			}
		});
		

		return home;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	public class CustomAdapter extends BaseAdapter {

		ArrayList<String> titles;

		LayoutInflater mInflater;

		public CustomAdapter(Context context, ArrayList<String> titles) {
			mInflater = LayoutInflater.from(context);

			this.titles = titles;

		}

		@Override
		public int getCount() {
			return titles.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			UserHolder1 holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_layout, parent,
						false);
				holder = new UserHolder1();

				holder.category = (TextView) convertView
						.findViewById(R.id.category);
				holder.event_title = (TextView) convertView
						.findViewById(R.id.event_title);
				holder.event_date = (TextView) convertView
						.findViewById(R.id.event_date);
				holder.event_desc = (TextView) convertView
						.findViewById(R.id.event_desc);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.item_icon);

				convertView.setTag(holder);
			} else {
				holder = (UserHolder1) convertView.getTag();
			}
			holder.event_title.setTypeface(event.getTextBold());
			holder.event_date.setTypeface(event.getTextBold());
			holder.event_desc.setTypeface(event.getTextNormal());
			int pos = id_position.get(position);
			EventsBean eve1 = events_upcoming.get(pos);

			holder.event_date.setText(util.dateConvert(eve1.startDate,
					eve1.endDate));
			holder.category.setText(titles.get(position));
			holder.event_title.setText(eve1.title);
			holder.event_desc.setText(eve1.venue);
			androidAQuery.id(holder.iv).image(eve1.thumbnail, true, true);
			return convertView;
		}

	}

	public class CustomAdapter1 extends BaseAdapter {

		ArrayList<String> titles;
		ArrayList<String> list1;

		LayoutInflater mInflater;

		public CustomAdapter1(Context context, ArrayList<String> titles,
				ArrayList<String> list1) {
			mInflater = LayoutInflater.from(context);

			this.titles = titles;
			this.list1 = list1;

		}

		@Override
		public int getCount() {
			return list1.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			int pos = Integer.parseInt(list1.get(position));
			UserHolder1 holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_layout, parent,
						false);
				holder = new UserHolder1();

				holder.category = (TextView) convertView
						.findViewById(R.id.category);
				holder.event_title = (TextView) convertView
						.findViewById(R.id.event_title);
				holder.event_date = (TextView) convertView
						.findViewById(R.id.event_date);
				holder.event_desc = (TextView) convertView
						.findViewById(R.id.event_desc);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.item_icon);

				convertView.setTag(holder);
			} else {
				holder = (UserHolder1) convertView.getTag();
			}

			holder.event_title.setTypeface(event.getTextBold());
			holder.event_date.setTypeface(event.getTextBold());
			holder.event_desc.setTypeface(event.getTextNormal());

			EventsBean eve1 = events_upcoming.get(pos);

			holder.event_date.setText(util.dateConvert(eve1.startDate,
					eve1.endDate));

			holder.category.setText(titles.get(pos));
			holder.event_title.setText(eve1.title);

			holder.event_desc.setText(eve1.venue);
			androidAQuery.id(holder.iv).image(eve1.thumbnail, true, true,
					AQuery.FADE_IN, pos, null, 0, 0);
			return convertView;

		}

	}

	static class UserHolder1 {
		TextView category;
		TextView event_title;
		TextView event_date;
		TextView event_time;
		TextView event_desc;
		ImageView iv;
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
		private static final String COLLECTINFO = "CollectInfo";
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
								} else if (name.equalsIgnoreCase(TITLE)) {
									if (i == 0) {
										title = parser.nextText();
									}
									i++;
								} else if (name.equalsIgnoreCase(STARTDATE)) {
									startdate = parser.nextText();
								} else if (name.equalsIgnoreCase(ENDDATE)) {
									enddate = parser.nextText();
								} else if (name.equalsIgnoreCase(DESCRIPTION)) {
									description = parser.nextText();
								} else if (name.equalsIgnoreCase(CATEGORY)) {
									category = parser.nextText();
								} else if (name.equalsIgnoreCase(VENU)) {
									venue = parser.nextText();
								} else if (name.equalsIgnoreCase(THUMBNAIL)) {
									thumbnail = parser.nextText();
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
								} else if (name.equalsIgnoreCase(EVENTTYPE)) {
									event_type = parser.nextText();
								} else if (name.equalsIgnoreCase(EXTLINK)) {
									ext_link = parser.nextText();
								}else if (name.equalsIgnoreCase(COLLECTINFO)) {
									collectinfo = Integer.parseInt(parser.nextText());
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
				update();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	public void update() {

		Intent intent = new Intent(getActivity(), Details.class);
		intent.putExtra("event_id", event_id);
		intent.putExtra("title", title);
		intent.putExtra("startdate", String.valueOf(startdate));
		intent.putExtra("enddate", String.valueOf(enddate));
		intent.putExtra("description", description);
		intent.putExtra("category", category);
		intent.putExtra("venue", venue); 
		intent.putExtra("thumbnail", thumbnail);
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
		intent.putExtra("collectinfo", collectinfo);
		/*Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(),
				R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
		getActivity().startActivity(intent, bundle);*/
		if (Build.VERSION.SDK_INT > 16) {
			Bundle  bundle = ActivityOptions.makeCustomAnimation(getActivity(),
				R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
		getActivity().startActivity(intent, bundle);
		}else{
			startActivity(intent);
		}
	}

	private void setSelectedImage(int selectedImagePosition) {

		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:dd");
			Date dt1 = format1.parse(default_dates.get(selectedImagePosition));
			DateFormat format2 = new SimpleDateFormat("EEE");
			String finalDay = format2.format(dt1);

			String[] parts = default_dates.get(selectedImagePosition).split(
					":");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
			Date date1 = sdf.parse(todate);
			Date date2 = sdf.parse(default_dates.get(selectedImagePosition));

	
			if (date2.compareTo(date1) > 0 || date2.compareTo(date1) == 0) {
				selectedImageView.setText(Html
						.fromHtml("<html><body><font color=\"black\">"
								+ finalDay
								+ "</font><br><b><font color=\"red\">"
								+ parts[2] + "</font></b><br>"
								+ "<font color=\"black\">"
								+ getMonthForInt(Integer.parseInt(parts[1]))
								+ "</font>" + "</body></html>"));
				// b.setPadding(3, 3, 3, 3);

			} else if (date2.compareTo(date1) < 0) {
					selectedImageView.setText(Html
						.fromHtml("<html><body><font color=\"black\">"
								+ finalDay
								+ "</font><br><b><font color=\"black\">"
								+ parts[2] + "</font></b><br>"
								+ "<font color=\"black\">"
								+ getMonthForInt(Integer.parseInt(parts[1]))
								+ "</font>" + "</body></html>"));
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	private void changeBorderForSelectedImage(int selectedItemPos) {
		int count = gallery.getChildCount();

		for (int i = 0; i < count; i++) {

			TextView txt = (TextView) gallery.getChildAt(i);
			txt.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button_color));
			// txt.setPadding(3, 3, 3, 3);
		}
		TextView txt = (TextView) gallery.getSelectedView();
		try {
			txt.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.select_textview_bg));
		} catch (Exception e) {

		}
		// txt.setBackgroundResource(R.drawable.line3);
		// txt.setPadding(3, 3, 3, 3);
	}

	String getMonthForInt(int num) {
		String monthString = "wrong";

		switch (num) {
		case 1:
			monthString = "Jan";
			break;
		case 2:
			monthString = "Feb";
			break;
		case 3:
			monthString = "Mar";
			break;
		case 4:
			monthString = "Apr";
			break;
		case 5:
			monthString = "May";
			break;
		case 6:
			monthString = "Jun";
			break;
		case 7:
			monthString = "Jul";
			break;
		case 8:
			monthString = "Aug";
			break;
		case 9:
			monthString = "Sep";
			break;
		case 10:
			monthString = "Oct";
			break;
		case 11:
			monthString = "Nov";
			break;
		case 12:
			monthString = "Dec";
			break;
		default:
			monthString = "Invalid month";
			break;
		}
		return monthString;
	}

	public void setUserSelected() {
		try {
			calendar_dates.clear();
			list.clear();
			id_list.clear();
			title1.clear();
			id_position.clear();
			for (int i = 0; i < events_upcoming.size(); i++) {
				EventsBean eve = events_upcoming.get(i);
				String[] given_dates = eve.startDate.split("T");
				String[] month_values = given_dates[0].split("-");
				if (month1.equalsIgnoreCase("00")) {
					if (!calendar_dates.contains(given_dates[0])) {
						calendar_dates.add(given_dates[0]);
					}
					list.add(eve.category);
					id_list.add(eve.id); 
					title1.add(eve.title);
					id_position.add(i);
				} else if (month1.equalsIgnoreCase(month_values[1])) {
					if (!calendar_dates.contains(given_dates[0])) {
						calendar_dates.add(given_dates[0]);

					}
					list.add(eve.category);
					id_list.add(eve.id);
					title1.add(eve.title);
					id_position.add(i);
				}

			}
			
			if (month1.equalsIgnoreCase("00")) {
				ArrayList<String> default_dates1 = new ArrayList<String>();
				galImageAdapter = new GalleryImageAdapter13(getActivity(),
						R.layout.item, calendar_dates,default_dates1);
				gallery.setAdapter(galImageAdapter);
				//gallery.removeAllViews();

			} else {
				galImageAdapter = new GalleryImageAdapter13(getActivity(),
						R.layout.item, calendar_dates,default_dates);
				gallery.setAdapter(galImageAdapter);
			}
			CustomAdapter adapter1 = new CustomAdapter(getActivity(), list);
			lv.setAdapter(adapter1);
		} catch (Exception e) {
			startActivity(new Intent(getActivity(), SplashActivity.class));
		}
	}
}