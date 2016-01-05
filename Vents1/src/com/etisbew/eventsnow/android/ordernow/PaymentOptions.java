package com.etisbew.eventsnow.android.ordernow;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.util.Utility;

public class PaymentOptions extends Activity implements OnClickListener {
	EventBean event;
	RelativeLayout menuLayout;
	TextView back, event_title, odetails, onumber, txt_id, txt_id2, txt_id3,
			txt_id11, txt_id12, txt_id21, txt_id22, txt_id31, txt_id32,
			txt_id71, txt_id72;
	Button placeorder;
	ListView listView1;
	Map<Integer, String> details;
	ArrayList<Integer> quantity;
	ArrayList<String> title;
	ArrayList<Integer> amount;
	Utility util;
	String json;
	int st;
	Double tax = 0.0;
	Double discount = 0.0;
	int orderid, resultcode;
	String message;
	LinearLayout linearLayout3, linearLayout5;
	View view51, view71;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_options);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		event = (EventBean) getApplicationContext();
		quantity = new ArrayList<Integer>();
		title = new ArrayList<String>();
		amount = new ArrayList<Integer>();
		util = new Utility(PaymentOptions.this);
		if (extras != null) {

			json = extras.getString("json");
		}
		details = event.getDetails();
		System.out.println("details" + details);
		Map<Integer, String> treeMap = new TreeMap<Integer, String>(
				new Comparator<Integer>() {

					@Override
					public int compare(Integer o1, Integer o2) {
						return o1.compareTo(o2);
					}

				});
		treeMap.putAll(details);
		details = treeMap;
		for (Map.Entry<Integer, String> entry : details.entrySet()) {
			Integer key = entry.getKey();
			String thing = entry.getValue();
			System.out.println("values" + key + thing);
			String[] str = thing.split(":");
			title.add(str[0]);
			quantity.add(Integer.parseInt(str[1]));
			st = st + Integer.parseInt(str[1]) * Integer.parseInt(str[2]);

			if (Double.parseDouble(str[3]) != 0.0) {
				System.out.println("amm" + amount + Double.parseDouble(str[3]));
				tax = tax + Integer.parseInt(str[1]) * Integer.parseInt(str[2])
						* Double.parseDouble(str[3]) / 100;
			} else {
				tax = tax + 0.0;
			}
			try {
				if (str.length > 6) {
					if (Integer.parseInt(str[8]) == 1) {
						if (Double.parseDouble(str[6]) != 0.0) {
							// System.out.println("amm"+amount+Double.parseDouble(str[3]));
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
						// System.out.println("amm"+amount+Double.parseDouble(str[3]));
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
		System.out.println("values in options" + title + quantity + amount);

		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		odetails = (TextView) findViewById(R.id.odetails);
		onumber = (TextView) findViewById(R.id.onumber);
		txt_id = (TextView) findViewById(R.id.txt_id);
		txt_id2 = (TextView) findViewById(R.id.txt_id2);
		txt_id3 = (TextView) findViewById(R.id.txt_id3);

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

		placeorder = (Button) findViewById(R.id.placeorder);

		listView1 = (ListView) findViewById(R.id.listView1);

		menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);

		back.setOnClickListener(this);
		placeorder.setOnClickListener(this);
		event_title.setOnClickListener(this);
		menuLayout.setOnClickListener(this);

		event_title.setTypeface(event.getTextBold());
		odetails.setTypeface(event.getTextBold());
		onumber.setTypeface(event.getTextBold());
		txt_id.setTypeface(event.getTextBold());
		txt_id2.setTypeface(event.getTextBold());
		txt_id3.setTypeface(event.getTextBold());

		placeorder.setTypeface(event.getTextBold());

		txt_id11.setTypeface(event.getTextNormal());
		txt_id12.setTypeface(event.getTextNormal());
		txt_id21.setTypeface(event.getTextNormal());
		txt_id22.setTypeface(event.getTextNormal());
		txt_id32.setTypeface(event.getTextBold());
		txt_id31.setTypeface(event.getTextBold());
		if (tax == 0) {
			txt_id12.setText("" + st);
			// txt_id22.setText(""+tax);
			// Double total_=st+tax;
			linearLayout3.setVisibility(View.GONE);
			view71.setVisibility(View.GONE);
			Double total_ = st - discount;
			txt_id72.setText("" + discount);
			txt_id32.setText("" + total_);
		} else if (discount == 0) {

			txt_id72.setVisibility(View.GONE);
			txt_id71.setVisibility(View.GONE);
			view51.setVisibility(View.GONE);
			// Double tot_=total;
			txt_id22.setText("" + tax);
			Double total_ = (st + tax);
			txt_id32.setText("" + total_);
		} else {
			txt_id12.setText("" + st);
			txt_id22.setText("" + tax);
			txt_id72.setText("" + discount);
			Double total_ = (st + tax) - discount;
			txt_id32.setText("" + total_);
		}

		CustomAdapter1 adapter = new CustomAdapter1(PaymentOptions.this);
		adapter.notifyDataSetChanged();
		listView1.setAdapter(adapter);

	}

	public class CustomAdapter1 extends BaseAdapter {

		LayoutInflater mInflater;

		// TextView add,price;

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
		} else if (v.getId() == R.id.placeorder) {
			new Webservice().execute();
		}
	}

	@Override
	public void onBackPressed() {
		finish();

		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

	private class Webservice extends AsyncTask<String, Void, String> {

		private static final String ORDERRESPONSE = "OrderResponse";
		private static final String ORDERID = "Orderid";
		private static final String RESULTCODE = "ResultCode";
		private static final String ERRORMESSAGE = "ErrorMessage";

		private ProgressDialog progressDialog;

		// <OrderResponse><Orderid>155539</Orderid><ResultCode>1</ResultCode><ErrorMessage>Success</ErrorMessage></OrderResponse></ns0:Response>

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(PaymentOptions.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(util.setService());
			String result = null;
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("data", json
						.toString()));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, HTTP.UTF_8).trim();

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			return result;

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
							if (name.equalsIgnoreCase(ORDERRESPONSE)) {
							} else {
								if (name.equalsIgnoreCase(ORDERID)) {
									orderid = Integer.parseInt(parser
											.nextText());
								} else if (name.equalsIgnoreCase(RESULTCODE)) {
									resultcode = Integer.parseInt(parser
											.nextText());
								} else if (name.equalsIgnoreCase(ERRORMESSAGE)) {
									message = parser.nextText();
								}
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(ORDERRESPONSE)) {

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

		if (resultcode == 1) {
			Intent in = new Intent(PaymentOptions.this,
					PaymentConfirmation.class);
			System.out.println("order id" + orderid);
			in.putExtra("orderid", orderid);
			startActivity(in);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			PaymentOptions.this.finish();
			// util.dialogExample1(message);
		} else {
			util.dialogExample1(message);
		}

	}
}
