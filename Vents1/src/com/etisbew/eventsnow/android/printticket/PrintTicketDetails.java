package com.etisbew.eventsnow.android.printticket;

import java.util.ArrayList;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.MainActivity;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.beans.EventsBean;
import com.etisbew.eventsnow.android.beans.LocationsBean;
import com.etisbew.eventsnow.android.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PrintTicketDetails extends Activity implements OnClickListener {
	TextView back, event_title, ticket, ticketid, event1, eventid, tickettype,
			tickettypeid, name, name1;
	EventBean event;
	int status, ticket_id;
	String f_name, e_title, t_title, pdf_url, email1;
	Button viewticket, sendemail;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.printticket_details);

		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {
			status = extras.getInt("status", 0);
			ticket_id = extras.getInt("ticket_id", 0);
			f_name = extras.getString("f_name");
			e_title = extras.getString("e_title");
			t_title = extras.getString("t_title");
			pdf_url = extras.getString("pdf_url");
			email1 = extras.getString("email1");
		}

		event = (EventBean) getApplicationContext();

		event_title = (TextView) findViewById(R.id.event_title);
		back = (TextView) findViewById(R.id.textView1);
		event1 = (TextView) findViewById(R.id.event);
		eventid = (TextView) findViewById(R.id.eventid);
		tickettype = (TextView) findViewById(R.id.tickettype);
		tickettypeid = (TextView) findViewById(R.id.tickettypeid);
		name = (TextView) findViewById(R.id.name);
		name1 = (TextView) findViewById(R.id.name1);
		ticket = (TextView) findViewById(R.id.ticket);
		ticketid = (TextView) findViewById(R.id.ticketid);

		viewticket = (Button) findViewById(R.id.viewticket);
		sendemail = (Button) findViewById(R.id.sendemail);

		event_title.setTypeface(event.getTextBold());
		event1.setTypeface(event.getTextBold());
		tickettype.setTypeface(event.getTextBold());
		name.setTypeface(event.getTextBold());
		ticket.setTypeface(event.getTextBold());

		eventid.setTypeface(event.getTextNormal());
		tickettypeid.setTypeface(event.getTextNormal());
		name1.setTypeface(event.getTextNormal());
		ticketid.setTypeface(event.getTextNormal());
		
		System.out.println("f_name"+f_name);

		eventid.setText("" + e_title);
		tickettypeid.setText(Html.fromHtml("" + t_title));
		if (f_name.length() > 0 && f_name!=null && f_name!="null") {
			name1.setText("" + f_name);
		}
		ticketid.setText("" + ticket_id);
 
		back.setOnClickListener(this);
		// update.setOnClickListener(this); 
		event_title.setOnClickListener(this);

		viewticket.setOnClickListener(this);
		sendemail.setOnClickListener(this);
	} 

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title
				|| v.getId() == R.id.menuLayout) {
			PrintTicketDetails.this.finish();
			// startActivity(new Intent(Signup.this, LoginActivity.class));
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
		}
		if (v.getId() == R.id.update) {

		}
		if (v.getId() == R.id.viewticket) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
			startActivity(intent);

			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
		}
		if (v.getId() == R.id.sendemail) {
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.setType("plain/text");
			// sendIntent.setData(Uri.parse("test@gmail.com"));
			sendIntent.setClassName("com.google.android.gm",
					"com.google.android.gm.ComposeActivityGmail");
			sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email1 });
			sendIntent.putExtra(Intent.EXTRA_SUBJECT,e_title );
			sendIntent.putExtra(Intent.EXTRA_TEXT,
					pdf_url);
			startActivity(sendIntent);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out); 
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onBackPressed() {
		PrintTicketDetails.this.finish();
		// startActivity(new Intent(PrintTicketDetails.this,
		// PrintTicket.class));

		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

}
