package com.etisbew.eventsnow.android.ordernow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
			venue, not, not1, str, st1, sc, sc1, ta, ta1, dc, dc1,txt_id11, txt_id12, txt_id21, txt_id22, txt_id31, txt_id32,
			txt_id71, txt_id72;
	Button viewonmap, editorder;
	Map<Integer, String> details;
	int ticket_count;
	int total;
	Double tax = 0.0;
	Double discount = 0.0;
	GPSTracker gps1;
	Double latitude, longitude;
	View view3, view6;
	ListView listView1;
	ArrayList<Integer> quantity;
	ArrayList<String> title;
	ArrayList<Integer> amount;
	LinearLayout linearLayout3, linearLayout5;
	View view51, view71;
	Double st=0.0;
	Double display_tax = 0.0;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_summary);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		quantity = new ArrayList<Integer>();
		title = new ArrayList<String>();
		amount = new ArrayList<Integer>();
		if (extras != null)
			details = (HashMap<Integer, String>) iin
					.getSerializableExtra("details");

	
		for (Map.Entry<Integer, String> entry : details.entrySet()) {
			Integer key = entry.getKey();
			String thing = entry.getValue();
			String[] str = thing.split(":");
			title.add(str[0]);
			quantity.add(Integer.parseInt(str[1]));
			st = st + Integer.parseInt(str[1]) * Integer.parseInt(str[2]);

			if (Double.parseDouble(str[3]) != 0.0) {
				display_tax=Double.parseDouble(str[3]);
				tax = tax + Integer.parseInt(str[1]) * Integer.parseInt(str[2])
						* Double.parseDouble(str[3]) / 100;
			} else {
				tax = tax + 0.0;
			}
			try {
				if (str.length > 6) {
					if (Integer.parseInt(str[8]) == 1) {
						if (Double.parseDouble(str[6]) != 0.0) {
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
			amount.add(Integer.parseInt(str[1]) * Integer.parseInt(str[2]));

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
		txt_id11 = (TextView) findViewById(R.id.txt_id11);
		txt_id12 = (TextView) findViewById(R.id.txt_id12);
		txt_id21 = (TextView) findViewById(R.id.txt_id21);
		txt_id22 = (TextView) findViewById(R.id.txt_id22);
		txt_id31 = (TextView) findViewById(R.id.txt_id31);
		txt_id32 = (TextView) findViewById(R.id.txt_id32);
		txt_id71 = (TextView) findViewById(R.id.txt_id71);
		txt_id72 = (TextView) findViewById(R.id.txt_id72);

		view51 = (View) findViewById(R.id.view51);
		view71 = (View) findViewById(R.id.view71);

		linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
		linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);

		viewonmap = (Button) findViewById(R.id.viewonmap);
		editorder = (Button) findViewById(R.id.editorder);
		listView1 = (ListView) findViewById(R.id.listView1);
		menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);

		event_title.setTypeface(event.getTextBold());
		datetime.setTypeface(event.getTextNormal());
		datetime1.setTypeface(event.getTextNormal());
		datetime2.setTypeface(event.getTextNormal());
		dt.setTypeface(event.getTextBold()); 
		venue.setTypeface(event.getTextBold());
		address.setTypeface(event.getTextNormal());
		viewonmap.setTypeface(event.getTextBold());
		editorder.setTypeface(event.getTextBold());


		txt_id11.setTypeface(event.getTextNormal());
		txt_id12.setTypeface(event.getTextNormal());
		txt_id21.setTypeface(event.getTextNormal());
		txt_id22.setTypeface(event.getTextNormal());
		txt_id32.setTypeface(event.getTextBold());
		txt_id31.setTypeface(event.getTextBold());
		if (tax == 0.0) {
			txt_id12.setText("Rs." + (String.format("%.2f",st+0.00)));
			linearLayout3.setVisibility(View.GONE);
			view71.setVisibility(View.GONE);
			Double total_ = st - discount;
			txt_id72.setText("Rs." + String.format("%.2f",discount));
			txt_id32.setText("Rs." + String.format("%.2f",total_));
			if(discount == 0.0){
				linearLayout5.setVisibility(View.GONE);
				view51.setVisibility(View.GONE);
			
			} 
		} else if (discount == 0.0) { 

			linearLayout5.setVisibility(View.GONE);
			view51.setVisibility(View.GONE);
			txt_id21.setText("Service Charge ("+display_tax+" %)");
			txt_id12.setText("Rs." + String.format("%.2f",st));
			txt_id22.setText("Rs." + String.format("%.2f",tax)); 
			Double total_ = (st + tax);
			txt_id32.setText("Rs." + String.format("%.2f",total_));
		} else {
			txt_id21.setText("Service Charge ("+display_tax+" %)");
			txt_id12.setText("Rs." + String.format("%.2f",st));
			txt_id22.setText("Rs." + String.format("%.2f",tax));
			txt_id72.setText("Rs." + String.format("%.2f",discount));
			Double total_ = (st + tax) - discount;
			txt_id32.setText("Rs." + String.format("%.2f",total_));
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
		CustomAdapter1 adapter = new CustomAdapter1(OrderSummary.this);
		adapter.notifyDataSetChanged();
		listView1.setAdapter(adapter);
	}
	public class CustomAdapter1 extends BaseAdapter {

		LayoutInflater mInflater;

		public CustomAdapter1(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			return quantity.size();

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

			convertView = mInflater.inflate(R.layout.payment_options_custom,
					parent, false);

			TextView title1 = (TextView) convertView.findViewById(R.id.title);
			TextView qty = (TextView) convertView.findViewById(R.id.qty);
			TextView total = (TextView) convertView.findViewById(R.id.total);

			title1.setTypeface(event.getTextBold());
			qty.setTypeface(event.getTextNormal());
			total.setTypeface(event.getTextNormal());

			title1.setText(Html.fromHtml(title.get(position)));
			qty.setText("" + quantity.get(position));
			total.setText("" + amount.get(position));
			;

			return convertView;

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
	@Override
	public void onResume() {
		super.onResume();

	}
}
