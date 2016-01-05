package com.etisbew.eventsnow.android;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.widget.TextView;

import com.androidquery.AQuery;
import com.etisbew.eventsnow.android.AllEventsFragment.UserHolder1;
import com.etisbew.eventsnow.android.beans.EventsBean;
import com.etisbew.eventsnow.android.util.Utility;

@SuppressLint("NewApi")
public class ExhibitionsFragment extends Fragment {
	EventBean event;
	ArrayList<Integer> list;
	ArrayList<EventsBean> events_upcoming;
	ArrayList<Integer> id_list;
	AQuery androidAQuery;
	Utility util;
	String title, description, startdate, enddate, category, venue, thumbnail,
			city, region, lat, lon, cperson, web, email, phoneno, gps,
			facebook1, twitter1, event_type, ext_link;
	String url;
	int event_id;
	private int selectedImagePosition = 0;
	CustomAdapter adapter;
	ListView lv;
	static ArrayList<Integer> category_id;
	int cat_id;
	Gallery gallery;
	Button month;
	GalleryImageAdapter13 galImageAdapter;
	int current_month_id;
	static ArrayList<String> nxt_day = new ArrayList<String>();
	String month1;
	String todate = "";
	private TextView selectedImageView;
	int fragment_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public static final ExhibitionsFragment newInstance(String sampleText) {
		ExhibitionsFragment f = new ExhibitionsFragment();
 
		Bundle b = new Bundle();
		b.putString("bString", sampleText);
		f.setArguments(b);
		System.out.println("given string"+sampleText);

		return f;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// return inflater.inflate(R.layout.page_one, container, false);
		View home = inflater.inflate(R.layout.page_one, container, false);
		System.out.println("getting"+getArguments().getString("bString"));
		fragment_id=Integer.parseInt(getArguments().getString("bString"));
		list = new ArrayList<Integer>();
		id_list = new ArrayList<Integer>();
		util = new Utility(getActivity());
		event = (EventBean) getActivity().getApplicationContext();
		androidAQuery = new AQuery(getActivity());
		events_upcoming = event.getObject();
		list.clear();
		id_list.clear();
		nxt_day.clear();

		/*
		 * ArrayList<CategoriesBean> categories = event.getCategories();
		 * System.out.println("categories" + categories);
		 * 
		 * category_id = new ArrayList<Integer>(); for (int i = 0; i <
		 * categories.size(); i++) { final CategoriesBean lb =
		 * categories.get(i); category_id.add(lb.categoryId);
		 * 
		 * } try { System.out.println("get check_id" + event.getCheck_id() +
		 * category_id.get(event.getCheck_id() - 1) + category_id.size());
		 * cat_id = category_id.get(event.getCheck_id() - 1); } catch (Exception
		 * e) {
		 * 
		 * }
		 */
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		Date date = new Date();
		String current_date = dateFormat.format(date);
		DateFormat format2 = new SimpleDateFormat("MMM");
		String[] convert_date = current_date.split("-");
		current_month_id = Integer.parseInt(convert_date[1]);
		month1 = String.valueOf(convert_date[1]);
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
		String finalDay = format2.format(dt1).toUpperCase();
		//fragment_id=event.getFragmentId();
		final String[] MenuItems = { "ALL", "JAN", "FEB", "MAR", "APR",
				"MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
		final String[] MenuItems1 =new String[13];
		try{
			for (int i = 0; i < events_upcoming.size(); i++) {
				EventsBean eve = events_upcoming.get(i);
				
				if (eve.categoryId == fragment_id) {
					String[] given_dates = eve.startDate.split("T");
					String[] ss = eve.startDate.split("T");
					String[] month_values = given_dates[0].split("-");
					if (month1.equalsIgnoreCase(month_values[1])) {
						if (!nxt_day.contains(ss[0])) {
							nxt_day.add(ss[0]);

						}
						list.add(i);
						id_list.add(eve.id);
					}
				

				}
			}
			int count=0;
			int total_size=0;
			for(int j=0; j<13;j++){  
			String mm;
				for (int i = 0; i < events_upcoming.size(); i++) {
					EventsBean eve = events_upcoming.get(i); 
					String[] given_dates=eve.startDate.split("T");
					String[] month_values=given_dates[0].split("-");
					if (eve.categoryId == fragment_id) {
					mm=String.valueOf(j);
					if (mm.length() == 1) {
						mm = "0" + mm;
					}
					
					if(mm.equalsIgnoreCase(month_values[1])){
						count=count+1;
					}
					}
			} 
				MenuItems1[j]=MenuItems[j]+"  ("+count+") ";
				total_size=total_size+count;
				count=0;
			}
			MenuItems1[0]=MenuItems[0]+"  ("+total_size+") ";
			}catch(Exception e){
				
			}
			
		
		System.out.println("size of nxt_day"+nxt_day.size()+nxt_day+event.getFragmentId());
		gallery = (Gallery) home.findViewById(R.id.gallery1);
		month = (Button) home.findViewById(R.id.button1);
		month.setBackground(null);
		month.setText(finalDay);
		month.setOnClickListener(new View.OnClickListener() { 

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setCancelable(true).setSingleChoiceItems(
						MenuItems1, current_month_id,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog1, int pos) {
								// TODO Auto-generated method stub

								month.setText(MenuItems[pos]);
								month1 = String.valueOf(pos);
								if (month1.length() == 1) {
									month1 = "0" + month1;
								}
								current_month_id = pos;
								/*Toast.makeText(getActivity(),
										MenuItems[pos] + month1,
										Toast.LENGTH_LONG).show();*/
								setUserSelected();
								dialog1.cancel();
							}
						});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});
		//galImageAdapter = new GalleryImageAdapter1(getActivity(), R.layout.item,nxt_day); 

		gallery.setAdapter(galImageAdapter);
		// gallery.setSelection(3, false);
		
		
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				selectedImagePosition = pos;
				// util.dialogExample1(""+nxt_day.get(pos));
				/*System.out.println("before pos in on itemselected"+pos+nxt_day+nxt_day.get(pos));
				for (int i = 0; i < events_upcoming.size(); i++) {
					EventsBean eve = events_upcoming.get(i);
				if (eve.categoryId == fragment_id) {
					String[] given_dates = eve.startDate.split("T");
					String[] ss = eve.startDate.split("T");
					String[] month_values = given_dates[0].split("-");
					if (month1.equalsIgnoreCase(month_values[1])) {
						if (!nxt_day.contains(ss[0])) {
						nxt_day.add(ss[0]);
						}
			
					}
				}
				}*/
				System.out.println("after pos in on itemselected"+pos+nxt_day+nxt_day.get(pos));
					try {

					list.clear();
					id_list.clear();
					for (int i = 0; i < events_upcoming.size(); i++) {
						EventsBean eve = events_upcoming.get(i);
						
						if (eve.categoryId == fragment_id) {
							System.out.println("categories check"+eve.categoryId+fragment_id);
						String[] given_dates = eve.startDate.split("T");
						String[] ss = eve.startDate.split("T");
						String[] month_values = given_dates[0].split("-");
						
						if (nxt_day.get(pos).equalsIgnoreCase(given_dates[0]) ) {
							System.out.println("month_values" +nxt_day.get(pos)
									+ month1 + given_dates[0] + eve.title);

							id_list.add(eve.id);
							list.add(i);
						}
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

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		lv = (ListView) home.findViewById(R.id.listView1);
		adapter = new CustomAdapter(getActivity(), list);
		adapter.notifyDataSetChanged();
		lv.setAdapter(adapter);
		refresh();
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int id1 = id_list.get(position);
				event.setEventId(id1);
				/*
				 * Fragment fragment = new DetailsFragment();
				 * android.support.v4.app.FragmentManager fragmentManager =
				 * getFragmentManager(); fragmentManager.beginTransaction()
				 * .replace(R.id.events_now, fragment).commit();
				 */
				// startActivity(new Intent(getActivity(),Details.class));
				/*
				 * Intent intent = new Intent(getActivity(), Details.class);
				 * Bundle bundle =
				 * ActivityOptions.makeCustomAnimation(getActivity(),
				 * R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
				 * getActivity().startActivity(intent, bundle);
				 */

				url = util.setEventsDetails(id1);
				Boolean status = util.IsNetConnected(getActivity());
				if (status) {
					new GettingEventsDetails().execute();
				} else {
					util.showAlertNoInternetService(getActivity());
				}

				// Toast.makeText(getActivity(), "You Clicked at " +items[+
				// position], Toast.LENGTH_SHORT).show();
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

	public void refresh() {
		adapter = new CustomAdapter(getActivity(), list);
		adapter.notifyDataSetChanged();
		lv.setAdapter(adapter);
	}

	public class CustomAdapter extends BaseAdapter {

		ArrayList<Integer> list;

		LayoutInflater mInflater;

		public CustomAdapter(Context context, ArrayList<Integer> list) {
			mInflater = LayoutInflater.from(context);

			this.list = list;

		}

		@Override
		public int getCount() {
			return list.size();
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

			int pos = list.get(position);
			System.out.println("list size" + list.size() + list + pos);
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
				// holder.event_time = (TextView)
				// convertView.findViewById(R.id.event_time);
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
			// holder.event_time.setTypeface(event.getTextNormal());
			holder.event_desc.setTypeface(event.getTextNormal());

			EventsBean eve1 = events_upcoming.get(pos);

			holder.category.setText(eve1.category);
			holder.event_title.setText(eve1.title);
			System.out.println("eve category title" + eve1.category
					+ eve1.title);
			// holder.event_time.setText(eve1.endDate.replace("T", " "));
			holder.event_date.setText(util.dateConvert(eve1.startDate,
					eve1.endDate));
			holder.event_desc.setText(eve1.venue);
			// holder.iv.setBackgroundDrawable(eve1.thumbnail);
			androidAQuery.id(holder.iv).image(eve1.thumbnail, true, true,
					AQuery.FADE_IN, pos, null, 0, 0);

			return convertView;
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
									// uniqueName.add(parser.nextText());
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

		System.out.println("dates" + startdate + enddate + gps);
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
		Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(),
				R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
		getActivity().startActivity(intent, bundle);
	}

	private void setSelectedImage(int selectedImagePosition) {
System.out.println("selectedImagePosition"+selectedImagePosition);
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:dd");
			Date dt1 = format1.parse(nxt_day.get(selectedImagePosition));
			DateFormat format2 = new SimpleDateFormat("EEE");
			String finalDay = format2.format(dt1);

			String[] parts = nxt_day.get(selectedImagePosition).split(":");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
			Date date1 = sdf.parse(todate);
			Date date2 = sdf.parse(nxt_day.get(selectedImagePosition));

			System.out.println(sdf.format(date1));
			System.out.println(sdf.format(date2));

			if (date2.compareTo(date1) > 0 || date2.compareTo(date1) == 0) {
				System.out.println("Date1 is after Date2");
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
				System.out.println("Date1 is before Date2");
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
		// ImageView imageView = (ImageView) gallery.getSelectedView();
		try{
			txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_textview_bg));
			}catch(Exception e){
				
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
		// System.out.println(monthString);
		return monthString;
	}

	public void setUserSelected() {
		try {
			nxt_day.clear();
			list.clear();
			id_list.clear();

			for (int i = 0; i < events_upcoming.size(); i++) {
				EventsBean eve = events_upcoming.get(i);
				String[] given_dates = eve.startDate.split("T");
				String[] month_values = given_dates[0].split("-");
				System.out.println("live data" + month1 + month_values[1]+fragment_id);
				
					
				if (month1.equalsIgnoreCase("00")) {
					if (eve.categoryId == fragment_id) {
						System.out.println("categories with 00:"+eve.categoryId+fragment_id+given_dates[0]);
					if (!nxt_day.contains(given_dates[0])) {
						nxt_day.add(given_dates[0]);

					}
					id_list.add(eve.id);
					list.add(i);
					}
				} else if (month1.equalsIgnoreCase(month_values[1])) {
					if (eve.categoryId == fragment_id) {
						System.out.println("categories with else"+eve.categoryId+fragment_id+given_dates[0]);
					if (!nxt_day.contains(given_dates[0])) {
						nxt_day.add(given_dates[0]);

					}
					id_list.add(eve.id);
					list.add(i);
				}
				}

			}
			System.out.println("nxt_day size"+nxt_day);
			/*galImageAdapter = new GalleryImageAdapter1(getActivity(),
					R.layout.item, nxt_day);
*/
			gallery.setAdapter(galImageAdapter);
			CustomAdapter adapter1 = new CustomAdapter(getActivity(), list);
			lv.setAdapter(adapter1);
		} catch (Exception e) {
			startActivity(new Intent(getActivity(), SplashActivity.class));
		}
	}
}