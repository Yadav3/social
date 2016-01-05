package com.etisbew.eventsnow.android.myfavorites;

import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.etisbew.eventsnow.android.Details;
import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.util.Utility;

@SuppressLint("NewApi")
public class MyFavorites extends Activity implements OnClickListener {
	final int[] booksCoverPhotos = null;
	EventBean event;
	RelativeLayout menuLayout;
	AQuery androidAQuery;
	ArrayList<Integer> id;
	ArrayList<String> uniqueName;
	ArrayList<String> title;
	ArrayList<String> startDate;
	ArrayList<String> endDate;
	ArrayList<Integer> categoryId;
	ArrayList<String> category1;
	ArrayList<String> venue;
	ArrayList<String> thumbnail;
	ArrayList<String> url1;
	ListView list;
	Utility util;
	String url, url2;
	int del_id;
	String remove_title;
	TextView back, event_title;
	boolean status_menu = false;
	String title1, description, startdate, enddate, category, venue1,
			thumbnail1, city, region, cperson, web, email, phoneno, gps,
			facebook1, twitter1, event_type, ext_link;
	String url3;
	int event_id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_favorites);

		final TextView tv = (TextView) findViewById(R.id.TextView02);
		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		androidAQuery = new AQuery(MyFavorites.this);
		event = (EventBean) getApplicationContext();
		util = new Utility(MyFavorites.this);
		Boolean status = util.IsNetConnected(MyFavorites.this);
		url = util.setMyList(event.getUserId());
		menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
		id = new ArrayList<Integer>();
		uniqueName = new ArrayList<String>();
		title = new ArrayList<String>();
		startDate = new ArrayList<String>();
		endDate = new ArrayList<String>();
		categoryId = new ArrayList<Integer>();
		category1 = new ArrayList<String>();
		venue = new ArrayList<String>();
		thumbnail = new ArrayList<String>();
		url1 = new ArrayList<String>();

		event_title.setTypeface(event.getTextBold());
		tv.setTypeface(event.getTextBold());

		back.setOnClickListener(this);
		event_title.setOnClickListener(this);
		menuLayout.setOnClickListener(this);
		if (status) {
			new GettingEvents().execute();

		} else {
			util.showAlertNoInternetService(MyFavorites.this);
		}

		list = (ListView) findViewById(R.id.listView1);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View view, int i, long l) {
				System.out.println("id list" + id + id.get(i));
				int id1 = id.get(i);
				event.setEventId(id1);
				url3 = util.setEventsDetails(id1);
				Boolean status = util.IsNetConnected(MyFavorites.this);
				if (status) {
					new GettingEventsDetails().execute();
				} else {
					util.showAlertNoInternetService(MyFavorites.this);
				}

			}
		});

		tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String a = tv.getText().toString();
				if (a.equalsIgnoreCase("Edit")) {

					tv.setText("Done");
					tv.setTypeface(event.getTextBold());
					final CustomAdapter2 cus1 = new CustomAdapter2(
							getBaseContext(), a);
					list.setAdapter(cus1);

				} else if (a.equalsIgnoreCase("Done")) {

					tv.setText("Edit");
					tv.setTypeface(event.getTextBold());
					final CustomAdapter2 cus2 = new CustomAdapter2(
							getBaseContext(), a);
					list.setAdapter(cus2);
				}

			}
		});

	}

	public class CustomAdapter1 extends BaseAdapter {

		LayoutInflater mInflater;

		public CustomAdapter1(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			return id.size();
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
			UserHolder1 holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.myfavorites_custom_layout, parent, false);
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

			holder.category.setText(category1.get(position));
			holder.event_date.setText(util.dateConvert(startDate.get(position),
					endDate.get(position)));
			holder.event_title.setText(title.get(position));
			holder.event_desc.setText(venue.get(position));
			System.out.println("thumbnail" + thumbnail.get(position));
			androidAQuery.id(holder.iv).image(thumbnail.get(position), true,
					true);

			return convertView;
		}

	}

	public class CustomAdapter2 extends BaseAdapter {

		String status = null;
		LayoutInflater mInflater;

		public CustomAdapter2(Context context, String status) {
			mInflater = LayoutInflater.from(context);

			this.status = status;
		}

		@Override
		public int getCount() {
			return id.size();
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
				convertView = mInflater.inflate(
						R.layout.myfavorites_custom_layout, parent, false);
				holder = new UserHolder1();

				holder.category = (TextView) convertView
						.findViewById(R.id.category);
				holder.event_title = (TextView) convertView
						.findViewById(R.id.event_title);
				holder.event_date = (TextView) convertView
						.findViewById(R.id.event_date);
				holder.event_time = (TextView) convertView
						.findViewById(R.id.event_time);
				holder.event_desc = (TextView) convertView
						.findViewById(R.id.event_desc);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.item_icon);
				holder.iv1 = (ImageView) convertView
						.findViewById(R.id.imageView1);

				convertView.setTag(holder);
			} else {
				holder = (UserHolder1) convertView.getTag();
			}

			holder.event_title.setTypeface(event.getTextBold());
			holder.event_date.setTypeface(event.getTextBold());
			holder.event_desc.setTypeface(event.getTextNormal());

			boolean flag = util.dateComparison(
					startDate.get(position).replace("T", " "),
					endDate.get(position).replace("T", " "));
			System.out.println("flag" + flag);
			if (flag) {
				holder.event_date.setText(startDate.get(position).replace("T",
						" "));
			} else {
				holder.event_date.setText(startDate.get(position).replace("T",
						" ")
						+ " To " + endDate.get(position).replace("T", " "));
			}
			if (status.equalsIgnoreCase("Edit")) {
				holder.iv1.setVisibility(View.VISIBLE);
			} else if (status.equalsIgnoreCase("Done")) {
				holder.iv1.setVisibility(View.GONE);
			}
			holder.category.setText(category1.get(position));
			holder.event_title.setText(title.get(position));
			holder.event_desc.setText(venue.get(position));
			androidAQuery.id(holder.iv).image(thumbnail.get(position), true,
					true);
			holder.iv1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("position" + position);
					remove_title = title.get(position);
					title.remove(position);
					url2 = util.setDeleteList(event.getUserId(),
							id.get(position));
					id.remove(position);
					uniqueName.remove(position);
					startDate.remove(position);
					endDate.remove(position);
					categoryId.remove(position);
					category1.remove(position);
					venue.remove(position);
					thumbnail.remove(position);
					url1.remove(position);
					new DeleteList().execute();
					notifyDataSetChanged();
				}
			});
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
		ImageView iv1;
		// Button btnDelete;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	private class GettingEvents extends AsyncTask<String, Void, String> {

		private static final String EVENTSLIST = "EventsList";
		private static final String EVENT = "Event";
		private static final String ID = "ID";
		private static final String UNIQUENAME = "UniqueName";
		private static final String TITLE = "Title";
		private static final String STARTDATE = "StartDate";
		private static final String ENDDATE = "EndDate";
		private static final String CATEGORYID = "CategoryID";
		private static final String CATEGORY = "Category";
		private static final String VENU = "Venu";
		private static final String THUMBNAIL = "Thumbnail";
		private static final String URL = "Url";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(MyFavorites.this);
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
							if (name.equalsIgnoreCase(EVENTSLIST)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									id.add(Integer.parseInt(parser.nextText()));
								} else if (name.equalsIgnoreCase(UNIQUENAME)) {
									uniqueName.add(parser.nextText());
								} else if (name.equalsIgnoreCase(TITLE)) {
									title.add(parser.nextText());
								} else if (name.equalsIgnoreCase(STARTDATE)) {
									startDate.add(parser.nextText());
								} else if (name.equalsIgnoreCase(ENDDATE)) {
									endDate.add(parser.nextText());
								} else if (name.equalsIgnoreCase(CATEGORYID)) {
									categoryId.add(Integer.parseInt(parser
											.nextText()));
								} else if (name.equalsIgnoreCase(CATEGORY)) {
									category1.add(parser.nextText());
								} else if (name.equalsIgnoreCase(VENU)) {
									venue.add(parser.nextText());
								} else if (name.equalsIgnoreCase(THUMBNAIL)) {
									thumbnail.add(parser.nextText());
								} else if (name.equalsIgnoreCase(URL)) {
									url1.add(parser.nextText());
								}
							}

							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(EVENTSLIST)) {

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
		System.out.println("length" + id);
		final CustomAdapter1 cus = new CustomAdapter1(getBaseContext());
		list.setAdapter(cus);

	}

	public void update1() {
		if (del_id == 1) {
			util.dialogExample1(remove_title + " "
					+ getResources().getString(R.string.removed));
		} else {
		}

	}

	private class DeleteList extends AsyncTask<String, Void, String> {
		private static final String RESULT = "Result";
		private static final String DELETEFROMMYLISTRESPONSE = "DeleteFromMyListResponse";
		private static final String RESULTCODE = "ResultCode";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(MyFavorites.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(url2);

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
							if (name.equalsIgnoreCase(DELETEFROMMYLISTRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(RESULTCODE)) {
									del_id = Integer
											.parseInt(parser.nextText());
								}
							}

							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(DELETEFROMMYLISTRESPONSE)) {

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title
				|| v.getId() == R.id.menuLayout) {
			finish();
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
		}
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
			progressDialog = new ProgressDialog(MyFavorites.this);
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
									category = parser.nextText();
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
								} else if (name.equalsIgnoreCase(EVENTTYPE)) {
									event_type = parser.nextText();
								} else if (name.equalsIgnoreCase(EXTLINK)) {
									ext_link = parser.nextText();
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

	public void update2() {

		Intent intent = new Intent(MyFavorites.this, Details.class);
		intent.putExtra("event_id", event_id);
		intent.putExtra("title", title1);
		intent.putExtra("startdate", String.valueOf(startdate));
		intent.putExtra("enddate", String.valueOf(enddate));
		intent.putExtra("description", description);
		intent.putExtra("category", category);
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
		overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}
}
