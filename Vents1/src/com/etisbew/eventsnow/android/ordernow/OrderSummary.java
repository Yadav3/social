package com.etisbew.eventsnow.android.ordernow;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.GPSTracker;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.buyticket.BuyTickets;

public class OrderSummary extends Activity implements OnClickListener {
	EventBean event;
	RelativeLayout menuLayout;
	TextView back, event_title, dt, datetime, datetime1, datetime2, address,
			venue, not, not1, st, st1, sc, sc1, ta, ta1, dc, dc1;
	Button viewonmap, editorder;
	Map<Integer, String> details;
	int ticket_count;
	int total;
	Double tax = 0.0;
	Double discount = 0.0;
	GPSTracker gps1;
	Double latitude, longitude;
	View view3, view6;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_summary);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();

		if (extras != null)
			details = (HashMap<Integer, String>) iin
					.getSerializableExtra("details");

		for (Map.Entry<Integer, String> entry : details.entrySet()) {
			Integer key = entry.getKey();
			String thing = entry.getValue();
			System.out.println("values" + key + thing);
			String[] str = thing.split(":");
			ticket_count = ticket_count + Integer.parseInt(str[1]);
			int amount = Integer.parseInt(str[1]) * Integer.parseInt(str[2]);
			total = total + amount;
			if (Double.parseDouble(str[3]) != 0.0) {
				System.out.println("amm" + amount + Double.parseDouble(str[3]));
				tax = tax + amount * Double.parseDouble(str[3]) / 100;
			} else {
				tax = tax + 0.0;
			}
			try {
				if (str.length > 6) {
					if (Integer.parseInt(str[8]) == 1) {
						if (Double.parseDouble(str[6]) != 0.0) {
							System.out.println("amm" + amount
									+ Double.parseDouble(str[3]));
							if (Integer.parseInt(str[1]) <= Integer
									.parseInt(str[7])) {
								discount = discount + Integer.parseInt(str[1])
										* Integer.parseInt(str[2])
										* Double.parseDouble(str[6]) / 100;
							} else {
								discount = discount + Integer.parseInt(str[7])
										* Integer.parseInt(str[2])
										* Double.parseDouble(str[6]) / 100;
							}
						} else {
							discount = discount + 0.0;
						}
					}
				} else {
					if (Double.parseDouble(str[6]) != 0.0) {
						System.out.println("amm" + amount
								+ Double.parseDouble(str[3]));
						if (Integer.parseInt(str[1]) <= Integer
								.parseInt(str[7])) {
							discount = discount + Integer.parseInt(str[1])
									* Integer.parseInt(str[8])
									;
						} else {
							discount = discount + Integer.parseInt(str[7])
									* Integer.parseInt(str[8]);
									
						}
					} else {
						discount = discount + 0.0;
					}
				}
			} catch (Exception e) {

			}

		}

		event = (EventBean) getApplicationContext();

		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		datetime = (TextView) findViewById(R.id.datetime);
		datetime1 = (TextView) findViewById(R.id.datetime1);
		datetime2 = (TextView) findViewById(R.id.datetime2);
		dt = (TextView) findViewById(R.id.dt);
		venue = (TextView) findViewById(R.id.venue);
		address = (TextView) findViewById(R.id.address);

		not = (TextView) findViewById(R.id.not);
		not1 = (TextView) findViewById(R.id.not1);
		st = (TextView) findViewById(R.id.st);
		st1 = (TextView) findViewById(R.id.st1);
		sc = (TextView) findViewById(R.id.sc);
		sc1 = (TextView) findViewById(R.id.sc1);
		ta = (TextView) findViewById(R.id.ta);
		ta1 = (TextView) findViewById(R.id.ta1);
		dc = (TextView) findViewById(R.id.dc);
		dc1 = (TextView) findViewById(R.id.dc1);

		view3 = (View) findViewById(R.id.view3);
		view6 = (View) findViewById(R.id.view6);

		viewonmap = (Button) findViewById(R.id.viewonmap);
		editorder = (Button) findViewById(R.id.editorder);

		menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);

		event_title.setTypeface(event.getTextBold());
		datetime.setTypeface(event.getTextNormal());
		datetime1.setTypeface(event.getTextNormal());
		datetime2.setTypeface(event.getTextNormal());
		dt.setTypeface(event.getTextNormal());
		venue.setTypeface(event.getTextNormal());
		address.setTypeface(event.getTextNormal());
		viewonmap.setTypeface(event.getTextBold());
		editorder.setTypeface(event.getTextBold());

		not.setTypeface(event.getTextNormal());
		not1.setTypeface(event.getTextBold());
		st.setTypeface(event.getTextNormal());
		st1.setTypeface(event.getTextBold());
		sc.setTypeface(event.getTextNormal());
		sc1.setTypeface(event.getTextBold());
		ta.setTypeface(event.getTextBold());
		ta1.setTypeface(event.getTextBold());
		dc.setTypeface(event.getTextNormal());
		dc1.setTypeface(event.getTextBold());

		not1.setText("" + ticket_count);
		st1.setText("Rs. " + total);
		if (tax == 0) {
			System.out.println("if block" + discount);
			sc1.setVisibility(View.GONE);
			sc.setVisibility(View.GONE);
			view3.setVisibility(View.GONE);
			// Double tot_=total;
			Double tot_ = total - discount;
			ta1.setText("Rs. " + tot_);
			dc1.setText("Rs. " + discount);
		} else if (discount == 0) {
			System.out.println("else if block" + discount);
			dc1.setVisibility(View.GONE);
			dc.setVisibility(View.GONE); 
			view6.setVisibility(View.GONE);
			Double tot_ = total + tax;
			sc1.setText("Rs. " + tax);
			ta1.setText("Rs. " + tot_);
		} else {
			System.out.println("else block" + discount);
			sc1.setText("Rs. " + tax);
			dc1.setText("Rs. " + discount);
			Double tot_ = (total + tax) - discount;
			ta1.setText("Rs. " + tot_);
		}

		back.setOnClickListener(this);
		event_title.setOnClickListener(this);
		menuLayout.setOnClickListener(this);
		viewonmap.setOnClickListener(this);
		editorder.setOnClickListener(this);

		datetime.setText(event.getDate1());
		address.setText(event.getVenue());
		String[] times_record = event.getDate1().split("To");
		if (times_record.length == 2) {
			String[] from_time = times_record[0].split(":");
			datetime.setText("" + from_time[0] + ":" + from_time[1]);
			datetime1.setText("To");
			String[] to_time = times_record[1].split(":");
			datetime2.setText("" + to_time[0] + ":" + to_time[1]);
		} else {
			String[] from_time = times_record[0].split(":");
			datetime.setText("" + from_time[0] + ":" + from_time[1]);
			datetime1.setVisibility(View.GONE);
			datetime2.setVisibility(View.GONE);
		}
		gps1 = new GPSTracker(getBaseContext()) {
		};
		if (gps1.canGetLocation()) {
			latitude = gps1.getLatitude();
			longitude = gps1.getLongitude();
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
		} else if (v.getId() == R.id.viewonmap) {
			String[] gps_array = event.getGps().split(",");

			/*
			 * Intent in = new Intent(OrderSummary.this,GenralMap.class);
			 * 
			 * in.putExtra("lat", gps_array[0]);
			 * in.putExtra("lon",gps_array[1]); startActivity(in);
			 * overridePendingTransition
			 * (R.anim.trans_right_in,R.anim.trans_right_out);
			 */
			Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?   saddr="
							+ latitude + "," + longitude + "&daddr="
							+ gps_array[0] + "," + gps_array[1]));
			intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent1.addCategory(Intent.CATEGORY_LAUNCHER);
			intent1.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			startActivity(intent1);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
		} else if (v.getId() == R.id.editorder) {
			OrderSummary.this.finish();
			Intent in1 = new Intent(OrderSummary.this, BuyTickets.class);
			// in1.putExtra("details",(HashMap)details);
			event.setDetails(details);
			in1.putExtra("class", "OrderSummary");
			startActivity(in1);

			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
		}
	}

	@Override
	public void onBackPressed() {
		finish();

		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

}
