package com.etisbew.eventsnow.android.printticket;

import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button; 
import android.widget.EditText;
import android.widget.TextView; 

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.MainActivity;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.util.Utility;

public class PrintTicket extends Activity implements OnClickListener{
	TextView back, event_title,ticket,temail;
	EventBean event;
	Utility util;
	EditText ticketid,email;
	Button update;
	String url;
	int status,ticket_id; 
	String f_name,e_title,t_title,pdf_url,email1;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.print_ticket);
		
		event = (EventBean) getApplicationContext();

		util = new Utility(PrintTicket.this);

		event_title = (TextView) findViewById(R.id.event_title);
		back = (TextView) findViewById(R.id.textView1);
		ticket = (TextView) findViewById(R.id.ticket);
		temail = (TextView) findViewById(R.id.temail);
		
		ticketid = (EditText) findViewById(R.id.ticketid);
		email = (EditText) findViewById(R.id.email);
		
		update = (Button) findViewById(R.id.update);
		
		event_title.setTypeface(event.getTextBold());
		ticket.setTypeface(event.getTextBold());
		temail.setTypeface(event.getTextBold());
		email.setTypeface(event.getTextNormal());
		ticketid.setTypeface(event.getTextNormal());
		
		back.setOnClickListener(this);
		update.setOnClickListener(this);
		event_title.setOnClickListener(this); 
		
		 
		
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title
				|| v.getId() == R.id.menuLayout) {
			PrintTicket.this.finish();
			//startActivity(new Intent(Signup.this, LoginActivity.class));
			overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
		}
		if (v.getId() == R.id.update) {
			if (email.getText().toString().length() >= 0
					&& ticketid.getText().toString().length() >= 0) {
				
				if (!util.validEmail(email.getText().toString())) {
					util.dialogExample1("Please Enter valid Email");

				} else {
					url = util.setLogin();
					email1=email.getText().toString();
						new PrintTicketsDetails().execute();
				}
			 
			} else {   
				util.dialogExample1("* fields mandatory");
			}
		}
		
	}
	private class PrintTicketsDetails extends AsyncTask<String, Void, String> {
		
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(PrintTicket.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}
 
		@Override
		protected String doInBackground(String... args) {
			url=util.setPrintTicket(ticketid.getText().toString(), email.getText().toString());
			return util.getXmlFromUrl(url);

		}  

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("result1" + result);
			if (null == result || result.length() == 0
					|| TextUtils.isEmpty(result)) {

				util.dialogExample();

			} else {
				try {
					JSONObject mainJson = new JSONObject(result);
					status=Integer.parseInt(mainJson.getString("status"));
					JSONArray	jsonArray = mainJson.getJSONArray("Tickets");
					System.out.println("jsonArray"+jsonArray+jsonArray.length()); 
					if(jsonArray.length()>0) 
					{
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject objJson = jsonArray.getJSONObject(i);
							System.out.println("ddd"+objJson);
							ticket_id=Integer.parseInt(objJson.getString("TicketId"));
							f_name=objJson.getString("FirstName");
							e_title=objJson.getString("EventTitle");
							t_title=objJson.getString("TicketTitle");
							pdf_url=objJson.getString("PdfUrl");
							//String f_name,e_title,t_title,pdf_url;
						}
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
		
		System.out.println("values are"+status+ticket_id+f_name+e_title+t_title+pdf_url);
		if(status==1){
		Intent in = new Intent(PrintTicket.this,PrintTicketDetails.class);
		in.putExtra("status", status);
		in.putExtra("ticket_id", ticket_id);
		in.putExtra("f_name", f_name);
		in.putExtra("e_title", e_title);
		in.putExtra("t_title", t_title);
		in.putExtra("pdf_url", pdf_url);
		in.putExtra("email1", email1);
		startActivity(in);
		} 
	}
	@Override
	public void onResume() {
		super.onResume();

	}
	@Override
	public void onBackPressed() {
		PrintTicket.this.finish();
		startActivity(new Intent(PrintTicket.this, MainActivity.class));
		
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}


}
