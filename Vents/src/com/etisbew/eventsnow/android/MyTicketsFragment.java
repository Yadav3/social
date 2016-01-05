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
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
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
	ArrayList<String> pdf_links;
	ListView list; 
	Utility util;
	String url;
	int del_id; 
	String remove_title;
	int result_code;
	String error_message;
	boolean status_menu=false;
	 String title1, description, startdate, enddate, category, venue1, thumbnail1,
		city, region, cperson, web, email, phoneno, gps,
		facebook1, twitter1,pdf_link;
		int event_id;
		RelativeLayout no_ticket;
		TextView back_home_button;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
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
		pdf_links = new ArrayList<String>();
		url1 = new ArrayList<String>();
		
 		Boolean status = util.IsNetConnected(getActivity());
	 		if (status) {
				new GettingEvents().execute();
	
			} else {
				util.showAlertNoInternetService(getActivity());
			}
 		list = (ListView) my_tickets.findViewById(R.id.listView1); 
 		no_ticket = (RelativeLayout) my_tickets.findViewById(R.id.no_ticket); 
 		back_home_button = (TextView) my_tickets.findViewById(R.id.back_home_button); 
 		back_home_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), MainActivity.class);
				
				if (Build.VERSION.SDK_INT > 11) {
					Bundle  bundle = ActivityOptions.makeCustomAnimation(getActivity(),
						R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
				getActivity().startActivity(intent, bundle);
				}else{
					startActivity(intent);
				}
			}
		});
 	
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
		private static final String PDF_LINK = "pdf_link";
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
			System.out.println("result"+result);
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
								} /*else if (name.equalsIgnoreCase(CATEGORYID)) {
									categoryId.add(Integer.parseInt(parser.nextText()));
								}*/ else if (name.equalsIgnoreCase(CATEGORY)) {
									category1.add(parser.nextText());
								} else if (name.equalsIgnoreCase(VENU)) {
									venue.add(parser.nextText());
								} else if (name.equalsIgnoreCase(THUMBNAIL)) {
									thumbnail.add(parser.nextText());
								} else if (name.equalsIgnoreCase(PDF_LINK)) {
									pdf_links.add(parser.nextText());
								}else if (name.equalsIgnoreCase(TICKETID)) {
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
		System.out.println("length" + id+uniqueName+title+pdf_links.size());
		if(result_code ==1){
		System.out.println("length" + id+uniqueName+title);
		final CustomAdapter1 cus = new CustomAdapter1(getActivity());
		list.setAdapter(cus);
		}else{
			list.setVisibility(View.GONE);
			no_ticket.setVisibility(View.VISIBLE);
		}
		
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
		final int pos=position;
		System.out.println("positons"+pos+position+category1.size());
		try{
		holder.category.setText(""+category1.get(pos));
		holder.title.setText(""+title.get(pos));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		holder.view.setOnClickListener(new View.OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				System.out.println("id list"+id+id.get(pos));
            	int id1=id.get(pos); 
               	event.setEventId(id1);
                    	Intent intent = new Intent(getActivity(), PdfView.class); 
        		
        		intent.putExtra("pdfURL", pdf_links.get(pos));
        	 	startActivity(intent);
        	 	if (Build.VERSION.SDK_INT > 11) {
            	Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        		getActivity().startActivity(intent, bundle);
    			startActivity(intent);
    			
        	 	}else{
        	 		startActivity(intent);
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
