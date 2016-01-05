package com.etisbew.eventsnow.android.readmore;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eventsnow.android.Details;
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
	int event_id,collectinfo;
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
	String title,start_date1,end_date1,venue,gps,ext_link;
	EditText email1;
	Dialog d;
	
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
			ext_link = extras.getString("ext_link");
			collectinfo=extras.getInt("collectinfo", 0);
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
			url4 = util.setTickets(event_id);
			if(Integer.parseInt(event_type)==3){
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ext_link));
				startActivity(intent);

				overridePendingTransition(R.anim.trans_left_in,
						R.anim.trans_left_out);
			}else{
			
			Boolean status = util.IsNetConnected(ReadMoreActivity.this);
			if (status) {
				new GettingTickets().execute();

			} else {
				util.showAlertNoInternetService(ReadMoreActivity.this);
			}
			}
			
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
			private static final String PROMOTION_ID = "promotion_id";
			private static final String DISCOUNT_CODE = "discount_code";
			private static final String CODE_MAX_ALLOWED = "code_max_allowed";
			private static final String P_START_DATE = "p_start_date";
			private static final String P_END_DATE = "p_end_date";
			private static final String TAX = "tax";
			private static final String DISCOUNT = "discount";
			private static final String DISCOUNT_TYPE = "discount_type";
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
						tickets_details.clear();
						JSONObject mainJson = new JSONObject(result);
						JSONArray jsonArray = mainJson.getJSONArray(TICKETRESPONSE);
						if (jsonArray.length() > 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								TicketsBean ticket = new TicketsBean();
								JSONObject objJson = jsonArray.getJSONObject(i);
								ticket.id = Integer.parseInt(objJson.getString(ID));
								ticket.title = objJson.getString(TITLE);
								ticket.description = objJson.getString(DESCRIPTION);
								ticket.ticket_total = Integer.parseInt(objJson
										.getString(TICKET_TOTAL));
								ticket.minimum_persons = Integer.parseInt(objJson
										.getString(MINIMUM_PERSONS));
								ticket.price_per_ticket = Integer.parseInt(objJson
										.getString(PRICE_PER_TICKET));
								ticket.start_date = objJson.getString(START_DATE);
								ticket.end_date = objJson.getString(END_DATE);
								ticket.ticket_buy_limit = Integer.parseInt(objJson
										.getString(TICKET_BUY_LIMIT));
								ticket.ticket_sold = Integer.parseInt(objJson
										.getString(TICKET_SOLD));
								ticket.alert_me = Integer.parseInt(objJson
										.getString(ALERT_ME));
								ticket.event_id = Integer.parseInt(objJson
										.getString(EVENT_ID));
								ticket.display_status = Integer.parseInt(objJson
										.getString(DISPLAY_STATUS));
								ticket.show_soldout_status = Integer
										.parseInt(objJson
												.getString(SHOW_SOLDOUT_STATUS));

								if (!TextUtils.isEmpty(objJson.getString(TAX))
										|| objJson.getString(TAX).equalsIgnoreCase(
												"null")) {
									ticket.tax = Double.parseDouble(objJson
											.getString(TAX));
								} else {
									ticket.tax = 0.0;
								}

								JSONArray jsonArray1 = objJson
										.getJSONArray("Promotions");
								if (jsonArray1.length() > 0) {
									for (int j = 0; j < jsonArray1.length(); j++) {
										JSONObject objJson2 = jsonArray1
												.getJSONObject(j);

										int promotion_id1 = Integer
												.parseInt(objJson2
														.getString("promotion_id"));
										ticket.promotion_id.add(promotion_id1);
										ticket.discount_code.add(objJson2
												.getString(DISCOUNT_CODE));
										if (!TextUtils.isEmpty(objJson2
												.getString("code_max_allowed"))
												|| objJson2.getString(
														"code_max_allowed")
														.equalsIgnoreCase("null")) {
											ticket.code_max_allowed
													.add(Integer.parseInt(objJson2
															.getString("code_max_allowed")));
										} else {
											ticket.code_max_allowed.add(0);
										}
										ticket.p_start_date.add(objJson2
												.getString(P_START_DATE));
										ticket.p_end_date.add(objJson2
												.getString(P_END_DATE));
										ticket.discount_type.add(objJson2
												.getString(DISCOUNT_TYPE));
										ticket.discount.add(Double
												.parseDouble(objJson2
														.getString(DISCOUNT)));

									}
								}
								tickets_details.add(ticket);

							}
						}
					} catch (Exception e) {

						progressDialog.dismiss();

					}
					Notify_Method();
				}

				if (null != progressDialog && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

			}

		}
	 public void Notify_Method() {
			if (Integer.parseInt(event_type) == 1 && tickets_details.size() == 0) {

				d = new Dialog(ReadMoreActivity.this); //
				d.setTitle("Booking not yet Open");

				d.setContentView(R.layout.notify);
				TextView title = (TextView) d.findViewById(R.id.title);
				title.setTypeface(event.getTextBold());
				email1 = (EditText) d.findViewById(R.id.email);
				Button submit = (Button) d.findViewById(R.id.submit);
				email1.setTypeface(event.getTextNormal());
				submit.setTypeface(event.getTextBold());

				submit.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!util.validEmail(email1.getText().toString())) {
							util.dialogExample1("Please enter valid email");

						} else {

							Boolean status = util.IsNetConnected(ReadMoreActivity.this);
							if (status) {
								new Notify_Service().execute();

							} else {
								util.showAlertNoInternetService(ReadMoreActivity.this);
							}
						}
					} 
				});
 
				d.show(); 

			} else if (Integer.parseInt(event_type) == 1
					&& tickets_details.size() > 0) {
				event.setTickets_details(null);
				event.setTickets_details(tickets_details);

				event.setTitle(title);
				event.setVenue(venue);
				event.setDate1(util.dateConvert(start_date1, end_date1));
				event.setGps(gps);
				event.setEventId(event_id);
				event.setCollectinfo(collectinfo);
				event.setDetails(null);
				startActivity(new Intent(ReadMoreActivity.this, BuyTickets.class));
				
				overridePendingTransition(R.anim.trans_left_in,
						R.anim.trans_left_out);
			} else { 
				util.dialogExample1("No tickets found for this event");
			}
 
		}

	 private class Notify_Service extends AsyncTask<String, Void, String> {
			
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
			String	notify=util.setNotifyme(event_id, email1.getText().toString());
			return util.getXmlFromUrl(notify);

			}  

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (null == result || result.length() == 0
						|| TextUtils.isEmpty(result)) {

					util.dialogExample();

				} else {
					String status;
					String message;
					try {
						JSONObject mainJson = new JSONObject(result);
						status=mainJson.getString("status");
						message=mainJson.getString("error");
						AlertDialog.Builder altDialog = new AlertDialog.Builder(ReadMoreActivity.this);
						altDialog.setMessage(message); // here add your message
	 					altDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								d.cancel();
							}
						});
						altDialog.show();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (null != progressDialog && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

			}

		}
		
}