package com.etisbew.eventsnow.android.readmore;

import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.beans.TicketsBean;
import com.etisbew.eventsnow.android.buyticket.BuyTickets;
import com.etisbew.eventsnow.android.util.Utility;

public class ReadMoreActivity extends Activity implements OnClickListener {
	Button buyticket;
	TextView back, event_title, event_desc, event_day, event_full_desc;
	RelativeLayout menuLayout;
	String event_type;
	Utility util;
	String url4;
	int event_id;
	ArrayList<Integer> id;
	ArrayList<String> title1;
	ArrayList<String> description1;
	ArrayList<Integer> ticket_total;
	ArrayList<Integer> minimum_persons;
	ArrayList<Integer> price_per_ticket;
	ArrayList<String> start_date;
	ArrayList<String> end_date;
	ArrayList<Integer> ticket_buy_limit;
	ArrayList<Integer> ticket_sold;
	ArrayList<Integer> alert_me;
	ArrayList<Integer> event_id1;
	ArrayList<Integer> display_status;
	ArrayList<Integer> show_soldout_status;
	ArrayList<Double> tax;
	ArrayList<TicketsBean> tickets_details;
	int user_id; 
	EventBean event;
	String title,start_date1,end_date1,venue,gps;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_more_activity);
		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		event_desc = (TextView) findViewById(R.id.event_desc);
		event_day = (TextView) findViewById(R.id.event_day);
		event_full_desc = (TextView) findViewById(R.id.event_full_desc);
		
		menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
		buyticket = (Button) findViewById(R.id.buyticket);
		
		event = (EventBean) getApplicationContext();
		user_id = event.getUserId();
		util= new Utility(ReadMoreActivity.this);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {
			title=extras.getString("title");
			event_title.setText(extras.getString("title"));
			event_day.setText(extras.getString("date"));
			event_full_desc.setText(Html.fromHtml(extras.getString("desc")));
			event_type=extras.getString("event_type");
			event_id = extras.getInt("event_id", 0);
			start_date1=extras.getString("start_date");
			end_date1=extras.getString("end_date");
			gps=extras.getString("gps");
			venue=extras.getString("venue");
		}

		 event_title.setTypeface(event.getTextBold());
		 event_desc.setTypeface(event.getTextBold());
    	 event_day.setTypeface(event.getTextBold());
    	 buyticket.setTypeface(event.getTextBold());
    	 event_full_desc.setTypeface(event.getTextNormal());

		back.setOnClickListener(this);
		back.setOnClickListener(this);
		buyticket.setOnClickListener(this);
		menuLayout.setOnClickListener(this);
		
		id = new ArrayList<Integer>();
		title1 = new ArrayList<String>();
		description1 = new ArrayList<String>();
		start_date = new ArrayList<String>();
		end_date = new ArrayList<String>();
		ticket_total = new ArrayList<Integer>();
		minimum_persons = new ArrayList<Integer>();
		price_per_ticket = new ArrayList<Integer>();
		ticket_buy_limit = new ArrayList<Integer>();
		ticket_sold = new ArrayList<Integer>();
		alert_me = new ArrayList<Integer>();
		event_id1 = new ArrayList<Integer>();
		display_status = new ArrayList<Integer>();

		tax = new ArrayList<Double>();
		show_soldout_status = new ArrayList<Integer>();
		tickets_details = new ArrayList<TicketsBean>();
		
		if (Integer.parseInt(event_type) == 1) {

		} else if (Integer.parseInt(event_type) == 2) {

		} else if (Integer.parseInt(event_type) == 3) {
			buyticket.setText("Registeration Now");
		} else if (Integer.parseInt(event_type) == 4) {
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout7);
			rl.setVisibility(View.GONE);
			buyticket.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title || v.getId() == R.id.menuLayout) {
			finish();
			overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);  
		} else if(v.getId() == R.id.buyticket){
			System.out.println("event_id" + event_id);
			url4 = util.setTickets(event_id);
			new GettingTickets().execute();
			
		}
	}
	 @Override
	    public void onBackPressed() {
	        super.onBackPressed();
	        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);   
	    }
	 @Override
		public void onResume(){
		    super.onResume();
		
		}
	 private class GettingTickets extends AsyncTask<String, Void, String> {

			private static final String TICKETRESPONSE = "TicketResponse";
			private static final String ID = "id";
			private static final String TITLE = "title";
			private static final String DESCRIPTION = "description";
			private static final String TICKET_TOTAL = "ticket_total";
			private static final String MINIMUM_PERSONS = "minimum_persons";
			private static final String PRICE_PER_TICKET = "price_per_ticket";
			private static final String START_DATE = "start_date";
			private static final String END_DATE = "end_date";
			private static final String TICKET_BUY_LIMIT = "ticket_buy_limit";
			private static final String TICKET_SOLD = "ticket_sold";
			private static final String ALERT_ME = "alert_me";
			private static final String EVENT_ID = "event_id";
			private static final String DISPLAY_STATUS = "display_status";
			private static final String SHOW_SOLDOUT_STATUS = "show_soldout_status";
			private static final String TAX = "tax";
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {

				super.onPreExecute();
				progressDialog = new ProgressDialog(ReadMoreActivity.this);
				progressDialog.setMessage("Loading ...");
				progressDialog.setIndeterminate(false);
				progressDialog.setCancelable(false);
				progressDialog.show();

			}

			@Override
			protected String doInBackground(String... args) {
				return util.getXmlFromUrl(url4);

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
									} else if (name.equalsIgnoreCase(TITLE)) {
										title1.add(parser.nextText());
									} else if (name.equalsIgnoreCase(DESCRIPTION)) {
										description1.add(parser.nextText());
									} else if (name.equalsIgnoreCase(TICKET_TOTAL)) {
										ticket_total.add(Integer.parseInt(parser
												.nextText()));
									} else if (name
											.equalsIgnoreCase(MINIMUM_PERSONS)) {
										minimum_persons.add(Integer.parseInt(parser
												.nextText()));
									} else if (name
											.equalsIgnoreCase(PRICE_PER_TICKET)) {
										price_per_ticket.add(Integer
												.parseInt(parser.nextText()));
									} else if (name.equalsIgnoreCase(START_DATE)) {
										start_date.add(parser.nextText());
									} else if (name.equalsIgnoreCase(END_DATE)) {
										end_date.add(parser.nextText());
									} else if (name
											.equalsIgnoreCase(TICKET_BUY_LIMIT)) {
										ticket_buy_limit.add(Integer
												.parseInt(parser.nextText()));
									} else if (name.equalsIgnoreCase(TICKET_SOLD)) {
										ticket_sold.add(Integer.parseInt(parser
												.nextText()));
									} else if (name.equalsIgnoreCase(ALERT_ME)) {
										alert_me.add(Integer.parseInt(parser
												.nextText()));
									} else if (name.equalsIgnoreCase(EVENT_ID)) {
										event_id1.add(Integer.parseInt(parser
												.nextText()));
									} else if (name
											.equalsIgnoreCase(DISPLAY_STATUS)) {
										display_status.add(Integer.parseInt(parser
												.nextText()));
									} else if (name
											.equalsIgnoreCase(SHOW_SOLDOUT_STATUS)) {
										show_soldout_status.add(Integer
												.parseInt(parser.nextText()));
									} else if (name.equalsIgnoreCase(TAX)) {
										tax.add(Double.parseDouble(parser
												.nextText()));
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
					update4();
				}

				if (null != progressDialog && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

			}

		} 
	 public void update4() {
			System.out.println("titles" + title1 + id);
			System.out.println("titles"+tickets_details);
			for (int i = 0; i < id.size(); i++) {  
				TicketsBean ticket = new TicketsBean();

				ticket.id = id.get(i);
				ticket.title = title1.get(i);
				ticket.description = description1.get(i);
				ticket.ticket_total = ticket_total.get(i);
				ticket.minimum_persons = minimum_persons.get(i); 
				ticket.price_per_ticket = price_per_ticket.get(i);
				ticket.start_date = start_date.get(i);
				ticket.end_date = end_date.get(i);
				ticket.ticket_buy_limit = ticket_buy_limit.get(i);
				ticket.ticket_sold = ticket_sold.get(i);
				ticket.alert_me = alert_me.get(i); 
				ticket.event_id = event_id1.get(i);
				ticket.display_status = display_status.get(i);
				ticket.show_soldout_status = show_soldout_status.get(i);
				ticket.tax = tax.get(i);

				tickets_details.add(ticket);
			}
			System.out.println("tickets_details" + tickets_details.size()
					+ tickets_details);
			event.setTickets_details(tickets_details);
			
			event.setTitle(title); 
			event.setVenue(venue);
			event.setDate1(util.dateConvert(start_date1, end_date1));
			event.setGps(gps);
			event.setEventId(event_id);
			startActivity(new Intent(ReadMoreActivity.this, BuyTickets.class));
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			/*if (user_id > 0) {
				
			} else {
				Intent in = new Intent(ReadMoreActivity.this, LoginActivity.class);
				in.putExtra("activity",
						"com.etisbew.android.eventsnow.buyticket.BuyTickets");
				startActivity(in);
				overridePendingTransition(R.anim.trans_left_in,
						R.anim.trans_left_out);
			}*/

		}
}