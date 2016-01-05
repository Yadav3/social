package com.etisbew.eventsnow.android;

import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.etisbew.eventsnow.android.mytickets.MyTicketDetails;
import com.etisbew.eventsnow.android.util.Utility;
@SuppressLint("NewApi")
public class MyTicketsFragment extends Fragment{
	EventBean event;
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
	ArrayList<Integer> ticket_id;
	ListView list;
	Utility util;
	String url,url3;
	int del_id;
	String remove_title;
	int result_code;
	String error_message;
	boolean status_menu=false;
	 String title1, description, startdate, enddate, category, venue1, thumbnail1,
		city, region, cperson, web, email, phoneno, gps,
		facebook1, twitter1,pdf_link;
		int event_id;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    } 
      
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
      //  return inflater.inflate(R.layout.page_one, container, false);
    	 View my_tickets = inflater.inflate(R.layout.my_tickets_fragment, container, false);
    	 androidAQuery = new AQuery(getActivity());
 		event = (EventBean) getActivity().getApplicationContext();
 		util = new Utility(getActivity());
 		url = util.setMyTickets(event.getUserId());
		
		id = new ArrayList<Integer>();
		ticket_id = new ArrayList<Integer>();
		uniqueName = new ArrayList<String>();
		title = new ArrayList<String>();
		startDate = new ArrayList<String>();
		endDate = new ArrayList<String>();
		categoryId = new ArrayList<Integer>();
		category1 = new ArrayList<String>();
		venue = new ArrayList<String>();
		thumbnail = new ArrayList<String>();
		url1 = new ArrayList<String>();
		/*id.add(1);
		title.add("new Event");
		category1.add("new category");
		id.add(1);
		title.add("new Event");
		category1.add("new category");*/
 		Boolean status = util.IsNetConnected(getActivity());
 		if (status) {
			new GettingEvents().execute();

		} else {
			util.showAlertNoInternetService(getActivity());
		}
 		list = (ListView) my_tickets.findViewById(R.id.listView1);
 		/*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
            	System.out.println("id list"+id+id.get(i));
            	int id1=id.get(i);
               	event.setEventId(id1);
               	url3 = util.setMyTicketDetails(id1, event.getUserId(), ticket_id.get(i));
				Boolean status = util.IsNetConnected(getActivity());
				if (status) {
					new GettingEventsDetails().execute();
				} else {
					util.showAlertNoInternetService(getActivity());
				}
            
		    	startActivity(new Intent(MyFavorites.this, Details.class));
		    	overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
            }
        }); */
    	 return my_tickets;
    }
    private class GettingEvents extends AsyncTask<String, Void, String> {

		private static final String TICKETRESPONSE = "TicketResponse";
		private static final String EVENT = "Event";
		private static final String ID = "ID";
		private static final String UNIQUENAME = "UniqueName";
		private static final String TITLE = "Title";
		private static final String STARTDATE = "StartDate";
		private static final String ENDDATE = "EndDate";
		private static final String CATEGORYID = "CategoryID";
		private static final String CATEGORY = "Category";
		private static final String VENU = "Venu";
		private static final String TICKETID = "TicketID";
		private static final String THUMBNAIL = "Thumbnail";
		private static final String URL = "Url";
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
							if (name.equalsIgnoreCase(TICKETRESPONSE)) {
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
									categoryId.add(Integer.parseInt(parser.nextText()));
								} else if (name.equalsIgnoreCase(CATEGORY)) {
									category1.add(parser.nextText());
								} else if (name.equalsIgnoreCase(VENU)) {
									venue.add(parser.nextText());
								} else if (name.equalsIgnoreCase(THUMBNAIL)) {
									thumbnail.add(parser.nextText());
								} else if (name.equalsIgnoreCase(TICKETID)) {
											ticket_id.add(Integer.parseInt(parser.nextText()));
								} else if (name.equalsIgnoreCase(URL)) {
									url1.add(parser.nextText());
								}else if (name.equalsIgnoreCase(RESULTCODE)) {
									result_code=Integer.parseInt(parser.nextText());
								}else if (name.equalsIgnoreCase(ERRORMESSAGE)) {
									error_message=parser.nextText();
								}
							} 

							break; 
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(TICKETRESPONSE)) {

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
		if(result_code ==1){
		System.out.println("length" + id);
		final CustomAdapter1 cus = new CustomAdapter1(getActivity());
		list.setAdapter(cus);
		}else{
			/*final CustomAdapter1 cus = new CustomAdapter1(getActivity());
			list.setAdapter(cus);*/
			util.dialogExample1(error_message);
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
		private static final String PDFLINK = "Pdflink";
		
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
								}else if (name.equalsIgnoreCase(PDFLINK)) {
									pdf_link=parser.nextText();
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
		
		System.out.println("dates"+startdate+enddate+gps+title1);
		Intent intent = new Intent(getActivity(), MyTicketDetails.class); 
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
		intent.putExtra("pdf_link", pdf_link);
	 	startActivity(intent);
    	Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
		getActivity().startActivity(intent, bundle);
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
			convertView = mInflater.inflate(R.layout.my_tickets_custom_row, parent, false);
			holder = new UserHolder1();
			
			holder.category = (TextView) convertView.findViewById(R.id.category);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			
			holder.view = (Button) convertView.findViewById(R.id.button1);
			
				convertView.setTag(holder);
		} else {
			holder = (UserHolder1) convertView.getTag();
		}
		
		holder.title.setTypeface(event.getTextBold());
	
		holder.category.setTypeface(event.getTextNormal());
		
		
		holder.category.setText(category1.get(position));
		holder.title.setText(title.get(position));
		final int pos=position;
		holder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				System.out.println("id list"+id+id.get(pos));
            	int id1=id.get(pos);
               	event.setEventId(id1);
               	url3 = util.setMyTicketDetails(id1, event.getUserId(), ticket_id.get(pos));
				Boolean status = util.IsNetConnected(getActivity());
				if (status) {
					new GettingEventsDetails().execute();
				} else {
					util.showAlertNoInternetService(getActivity());
				}
			}
		});
			
		
		
		return convertView;
	}

}

static class UserHolder1 {
	  TextView title;
	  TextView category;
	  Button view;
	 }
}
