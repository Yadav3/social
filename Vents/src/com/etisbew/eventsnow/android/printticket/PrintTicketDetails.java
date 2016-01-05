package com.etisbew.eventsnow.android.printticket;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.PdfView;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.util.Utility;

public class PrintTicketDetails extends Activity implements OnClickListener {
	TextView back, event_title;
	EventBean event;
	int status;
	String email1;
	ArrayList<String> ticket_id;
	ArrayList<String> f_name;
	ArrayList<String> e_title;
	ArrayList<String> t_title;
	ArrayList<String> pdf_url;
	ListView list;
	String booking_id;
	String send_email_url;
	int send_ticket_id;
	Utility util;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.printticket_details);

		ticket_id = new ArrayList<String>();
		f_name = new ArrayList<String>();
		e_title = new ArrayList<String>();
		t_title = new ArrayList<String>();
		pdf_url = new ArrayList<String>();
		
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {
			status = extras.getInt("status", 0);
			ticket_id = extras.getStringArrayList("ticket_id");
			f_name = extras.getStringArrayList("f_name");
			e_title = extras.getStringArrayList("e_title");
			t_title = extras.getStringArrayList("t_title");
			pdf_url = extras.getStringArrayList("pdf_url");
			email1 = extras.getString("email1");
			booking_id=extras.getString("booking_id");
			
		}
		
		event = (EventBean) getApplicationContext();
		util = new Utility(PrintTicketDetails.this);
	
		event_title = (TextView) findViewById(R.id.event_title); 
		list = (ListView) findViewById(R.id.listView1);
		back = (TextView) findViewById(R.id.textView1);

		event_title.setTypeface(event.getTextBold());


		back.setOnClickListener(this);
		event_title.setOnClickListener(this);

		final CustomAdapter1 cus = new CustomAdapter1(getBaseContext());
		cus.notifyDataSetChanged();
		list.setAdapter(cus);
		
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title
				|| v.getId() == R.id.menuLayout) {
			PrintTicketDetails.this.finish();
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
		}
		if (v.getId() == R.id.update) {

		}
		
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onBackPressed() {
		PrintTicketDetails.this.finish();
	
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

	public class CustomAdapter1 extends BaseAdapter {

		LayoutInflater mInflater;

		public CustomAdapter1(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			return ticket_id.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("unused")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			UserHolder1 holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.custom_print_layout,
						parent, false);
				holder = new UserHolder1();

				holder.event1 = (TextView)convertView.findViewById(R.id.event);
			    holder.eventid = (TextView)convertView.findViewById(R.id.eventid);
				holder.tickettype = (TextView)convertView.findViewById(R.id.tickettype);
				holder.tickettypeid = (TextView)convertView.findViewById(R.id.tickettypeid);
				holder.name = (TextView)convertView.findViewById(R.id.name);
				holder.name1 = (TextView)convertView.findViewById(R.id.name1);
				holder.ticket = (TextView)convertView.findViewById(R.id.ticket);
				holder.ticketid = (TextView)convertView.findViewById(R.id.ticketid);

				holder.viewticket = (Button)convertView.findViewById(R.id.viewticket);
				holder.sendemail = (Button)convertView.findViewById(R.id.sendemail);
				convertView.setTag(holder);
			} else {
				holder = (UserHolder1) convertView.getTag();
			}

			holder.event1.setTypeface(event.getTextBold());
			holder.tickettype.setTypeface(event.getTextBold());
			holder.name.setTypeface(event.getTextBold());
			holder.ticket.setTypeface(event.getTextBold());

			holder.eventid.setTypeface(event.getTextNormal());
			holder.tickettypeid.setTypeface(event.getTextNormal());
			holder.name1.setTypeface(event.getTextNormal());
			holder.ticketid.setTypeface(event.getTextNormal());
 
//			  
			try{
			String e_title1=e_title.get(position);
			holder.eventid.setText(Html.fromHtml(e_title1));
			String t_title1=t_title.get(position);
			holder.tickettypeid.setText(Html.fromHtml(t_title1));
			String name=f_name.get(position);
			if (name.length() == 0 && name == null && name.equalsIgnoreCase("null")) {

			} else {
				holder.name1.setText(""+name);
			} 
			String id=ticket_id.get(position);
			holder.ticketid.setText(""+id);
			final int pos=position;
			holder.viewticket.setOnClickListener(new View.OnClickListener() {

				@Override 
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent in = new Intent(PrintTicketDetails.this, PdfView.class);
					in.putExtra("pdfURL", pdf_url.get(pos));
					startActivity(in);
					overridePendingTransition(R.anim.trans_left_in,
							R.anim.trans_left_out);
				}
			});
			holder.sendemail.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					send_ticket_id=Integer.parseInt(ticket_id.get(pos));
				
					   Boolean status = util.IsNetConnected(PrintTicketDetails.this);
						if (status) {
							new SendEmail().execute();

						} else {
							util.showAlertNoInternetService(PrintTicketDetails.this);
						}
				}
			});
			}catch(Exception e){
				e.printStackTrace(); 
			}

			return convertView;
		}

	}

	static class UserHolder1 {
		TextView ticket, ticketid, event1, eventid, tickettype, tickettypeid,
				name, name1;
		Button viewticket, sendemail;
	}
	private class SendEmail extends AsyncTask<String, Void, String> {
		
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(PrintTicketDetails.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}
 
		@Override
		protected String doInBackground(String... args) {
			send_email_url=util.sendEmail(booking_id,send_ticket_id);
			return util.getXmlFromUrl(send_email_url);

		}  

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null == result || result.length() == 0
					|| TextUtils.isEmpty(result)) {

				util.dialogExample();

			} else {
				try {
					
					 
					JSONObject mainJson = new JSONObject(result);
					status=Integer.parseInt(mainJson.getString("status"));
					if(status ==1){
						util.dialogExample1("Mail sent successfully");
					}else{
						util.dialogExample1("Mail not sent");
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}
}
