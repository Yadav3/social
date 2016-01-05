package com.etisbew.eventsnow.android.buyticket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.MainActivity;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.SplashActivity;
import com.etisbew.eventsnow.android.beans.TicketsBean;
import com.etisbew.eventsnow.android.login.LoginActivity;
import com.etisbew.eventsnow.android.ordernow.OrderNow;
import com.etisbew.eventsnow.android.util.Utility;

public class BuyTickets extends Activity implements OnClickListener,
		OnValueChangeListener { 
	Button ordernow;
	TextView back, event_title, title, date, address, txt_id, txt_id1, 
	txt_id2,txt_id11,txt_id12,txt_id21,txt_id22,txt_id31,txt_id32,txt_id72,txt_id71,
			txt_id3,txt_id_promo;
	EventBean event;
	EditText promo;
	Button apply;
	ArrayList<Integer> list;
	ListView lv;
	String class_name = "";
	int ticket_count;  
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
	ArrayList<Double> p_discount;
	ArrayList<Integer> promotion_id;
	ArrayList<String> discount_code;
	ArrayList<Integer> code_max_allowed;
	ArrayList<String> p_start_date;
	ArrayList<String> p_end_date;
	RelativeLayout menu_layout,promotional;
	ArrayList<TicketsBean> tickets_details;
	Utility util;
	Map<Integer, String> details;
	Map<Integer, String> details1;
	Map<Integer, Integer> hold_id;
    LinearLayout linearLayout5,linearLayout3;
    View view71,view51;
	public static boolean ASC = true;
	public static boolean DESC = false;
	int user_id;
	int st;
	int select;
	Double tax1=0.0; 
	Double discount=0.0; 
	@SuppressWarnings("unchecked")
	@SuppressLint("UseSparseArrays")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.etisbew.eventsnow.android.R.layout.buy_tickets);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();  
		event = (EventBean) getApplicationContext();
		if (extras != null) {
			class_name = extras.getString("class");
			select = extras.getInt("select", 0);
		} 
		try { 
			details1 = event.getDetails();
			
			hold_id = new HashMap<Integer, Integer>();

			Map<Integer, String> treeMap = new TreeMap<Integer, String>(
					new Comparator<Integer>() {

						@Override
						public int compare(Integer o1, Integer o2) {
							return o1.compareTo(o2);
						}

					});
			treeMap.putAll(details1);
			for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
				Integer key = entry.getKey();

				String thing = entry.getValue();
				System.out.println("values" + key + thing);
				String[] str = thing.split(":");
				hold_id.put(key, key);
 
			} 

			printMap(treeMap);
			 
		} catch (Exception e) {
 
		}
		System.out.println("intent" + class_name);
		System.out.println("details1" + details1);
		
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
		show_soldout_status = new ArrayList<Integer>();
		tax = new ArrayList<Double>();
		promotion_id = new ArrayList<Integer>();
		discount_code = new ArrayList<String>();
		code_max_allowed = new ArrayList<Integer>();
		p_start_date = new ArrayList<String>();
		p_end_date = new ArrayList<String>();
		p_discount = new ArrayList<Double>();
		
		tickets_details = event.getTickets_details();
		System.out.println("tickets_details" + tickets_details);
		try {
			for (int i = 0; i < tickets_details.size(); i++) {
				TicketsBean ticket = tickets_details.get(i);
				id.add(ticket.id);
				title1.add(ticket.title);
				description1.add(ticket.description);
				start_date.add(ticket.start_date);
				end_date.add(ticket.end_date);
				ticket_total.add(ticket.ticket_total);
				minimum_persons.add(ticket.minimum_persons);
				price_per_ticket.add(ticket.price_per_ticket);
				ticket_buy_limit.add(ticket.ticket_buy_limit);
				ticket_sold.add(ticket.ticket_sold);
				alert_me.add(ticket.alert_me);
				event_id1.add(ticket.event_id);
				display_status.add(ticket.display_status);
				show_soldout_status.add(ticket.show_soldout_status);
				tax.add(ticket.tax);
				ArrayList<Integer> promotions = new ArrayList<Integer>();
				promotions=ticket.promotion_id;
				System.out.println("details"+promotions+promotions.size());
				if(promotions.size()>0){
					promotion_id.add(promotions.get(0));
				}
				/*promotion_id.add(ticket.promotion_id.get);
				discount_code.add(ticket.discount_code);
			    code_max_allowed.add(ticket.code_max_allowed);
			    p_start_date.add(ticket.p_start_date);
			    p_end_date.add(ticket.p_end_date);
			    p_discount.add(ticket.discount);*/
			}
		} catch (Exception e) {
			startActivity(new Intent(BuyTickets.this, SplashActivity.class));
		}

		details = new HashMap<Integer, String>();
		user_id = event.getUserId();
		util = new Utility(BuyTickets.this);
		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		title = (TextView) findViewById(R.id.title);
		date = (TextView) findViewById(R.id.date);
		address = (TextView) findViewById(R.id.address);
		txt_id = (TextView) findViewById(R.id.txt_id);
		txt_id1 = (TextView) findViewById(R.id.txt_id1);
		txt_id2 = (TextView) findViewById(R.id.txt_id2);
		txt_id3 = (TextView) findViewById(R.id.txt_id3);
		ordernow = (Button) findViewById(R.id.ordernow);
		
		promo = (EditText) findViewById(R.id.promo);
		apply = (Button) findViewById(R.id.apply);
		
		view71 = (View) findViewById(R.id.view71);
		view51 = (View) findViewById(R.id.view51);
		
		txt_id11 = (TextView)findViewById(R.id.txt_id11);
		txt_id12 = (TextView)findViewById(R.id.txt_id12);
		txt_id21 = (TextView)findViewById(R.id.txt_id21);
		txt_id22 = (TextView)findViewById(R.id.txt_id22);
		txt_id31 = (TextView)findViewById(R.id.txt_id31);
		txt_id32 = (TextView)findViewById(R.id.txt_id32);
		txt_id71 = (TextView)findViewById(R.id.txt_id71);
		txt_id72 = (TextView)findViewById(R.id.txt_id72);
		
		txt_id_promo= (TextView)findViewById(R.id.txt_id_promo);
		
		
		menu_layout = (RelativeLayout) findViewById(R.id.menuLayout);
		promotional = (RelativeLayout) findViewById(R.id.promotional);
		
		linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
		linearLayout5 = (LinearLayout) findViewById(R.id.linearLayout5);

		event_title.setTypeface(event.getTextBold());
		title.setTypeface(event.getTextBold()); 
		date.setTypeface(event.getTextNormal());
		address.setTypeface(event.getTextNormal());
		txt_id.setTypeface(event.getTextBold());
		txt_id1.setTypeface(event.getTextBold());
		txt_id2.setTypeface(event.getTextBold());
		txt_id3.setTypeface(event.getTextBold());
		ordernow.setTypeface(event.getTextBold());
		
		txt_id11.setTypeface(event.getTextNormal());
		txt_id12.setTypeface(event.getTextNormal());
		txt_id21.setTypeface(event.getTextNormal());
		txt_id22.setTypeface(event.getTextNormal());
		txt_id71.setTypeface(event.getTextNormal());
		txt_id72.setTypeface(event.getTextNormal());
		txt_id32.setTypeface(event.getTextBold());
		txt_id31.setTypeface(event.getTextBold());
		txt_id_promo.setTypeface(event.getTextNormal());
		promo.setTypeface(event.getTextNormal());
		apply.setTypeface(event.getTextBold());
		
		if(id.size() == promotion_id.size()){
			if(select>=0){ 
				
					Update_display(); 
				
			} 
		}else{
			Update_display(); 
			if(id.size()>0){
			promotional.setVisibility(View.GONE);
			}
		}

		lv = (ListView) findViewById(R.id.listView1);
		 CustomAdapter1 adapter = new CustomAdapter1(BuyTickets.this);
			adapter.notifyDataSetChanged();
			lv.setAdapter(adapter);
		/*runOnUiThread(new Runnable() 
		{
		   public void run() 
		   {
			   CustomAdapter1 adapter = new CustomAdapter1(BuyTickets.this);
				adapter.notifyDataSetChanged();
				lv.setAdapter(adapter);
				
		   } 
		});*/
		
		
		back.setOnClickListener(this);
		event_title.setOnClickListener(this);
		menu_layout.setOnClickListener(this);
		ordernow.setOnClickListener(this);
		
		apply.setOnClickListener(this);

		title.setText(event.getTitle());
		address.setText(event.getVenue());
		date.setText(event.getDate1());
		
	}

	private static Map<Integer, String> sortByComparator(
			Map<Integer, String> sortMap, final boolean order) {

		List<Entry<Integer, String>> list = new LinkedList<Entry<Integer, String>>(
				sortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<Integer, String>>() {
			public int compare(Entry<Integer, String> o1,
					Entry<Integer, String> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());

				}
			}

			/*
			 * @Override public int compare(Entry<Integer, String> lhs,
			 * Entry<Integer, String> rhs) { // TODO Auto-generated method stub
			 * return 0; }
			 */

		});

		// Maintaining insertion order with the help of LinkedList
		Map<Integer, String> sortedMap = new LinkedHashMap<Integer, String>();
		for (Entry<Integer, String> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public static void printMap(Map<Integer, String> map) {
		for (Entry<Integer, String> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}
	}

	public class CustomAdapter1 extends BaseAdapter {

		LayoutInflater mInflater;

		// TextView add,price;

		public CustomAdapter1(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			return title1.size();

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
			if (display_status.get(position) == 1) {

				convertView = mInflater.inflate(
						R.layout.buy_tickets_custom_design, parent, false);

				TextView title = (TextView) convertView
						.findViewById(R.id.title);
				TextView date = (TextView) convertView.findViewById(R.id.date);

				TextView price = (TextView) convertView
						.findViewById(R.id.price);
				final TextView add = (TextView) convertView
						.findViewById(R.id.add);
				TextView desc = (TextView) convertView.findViewById(R.id.desc);
				final TextView total = (TextView) convertView
						.findViewById(R.id.total);

				title.setTypeface(event.getTextBold());
				date.setTypeface(event.getTextNormal());
				price.setTypeface(event.getTextNormal());
				add.setTypeface(event.getTextNormal());
				desc.setTypeface(event.getTextNormal());

				title.setText(Html.fromHtml(title1.get(position)));

				price.setText(String.valueOf("Rs. "
						+ price_per_ticket.get(position)));

				try {
					System.out.println("hold id" + hold_id);
					System.out.println("hold id position"
							+ hold_id.get(position) + position);
					if (hold_id.get(position) == position) {

						System.out.println("true;" + hold_id.get(position)
								+ position);
						// System.out.println("details of all"+details1.get(position));

						String[] ll = details1.get(position).split(":");
						add.setText(String.valueOf(Integer.parseInt(ll[1])));
						// 12-08 10:51:32.509: I/System.out(25428): details of
						// all<B>Men - Singles</B>:3:500:336
						System.out.println("string" + ll);
						int value1 = Integer.parseInt(ll[1])
								* Integer.parseInt(ll[2]);
						System.out.println("price" + Integer.parseInt(ll[1])
								+ Integer.parseInt(ll[2]));
						total.setText("Rs. " + String.valueOf(value1));
						/*
						 * details.put(position, (ll[0]+ ":" +
						 * Integer.parseInt(ll[1]) + ":" +
						 * Integer.parseInt(ll[2]) + ":" +
						 * Integer.parseInt(ll[3])));
						 */
					} else {
						total.setText(String.valueOf("Rs. 0"));
						System.out.println("false;" + hold_id.get(position)
								+ position);
					}
					details = details1;
					if(details.size()>0){
						Update_display();
					}
				} catch(NullPointerException n){
					total.setText(String.valueOf("Rs. 0"));
				}catch (Exception e) {
					System.out.println("error message");
					e.printStackTrace();
					
				}
				String[] dates = end_date.get(position).split(" ");
				date.setText("Lastday: " + util.dateConvert1(dates[0]));
				if (description1.get(position).length() > 0) {
					desc.setText(description1.get(position));
				} else {
					desc.setVisibility(View.GONE);
				}

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:s", Locale.US);
				String currentDateandTime = sdf.format(new Date());
				System.out.println("times in buy checking" + currentDateandTime
						+ end_date.get((position)));
				if (util.CheckDates(currentDateandTime, 
							start_date.get((position)))) {   
						System.out.println("true"); 
						add.setText(String.valueOf("Coming soon")); 
						add.setTextColor(getResources().getColor(R.color.red));
						add.setBackgroundColor(Color.TRANSPARENT);
				}else 	if (util.CheckDates(currentDateandTime,
								end_date.get((position)))) {
							System.out.println("true"); 
							add.setText(String.valueOf("Tickets Closed"));
							add.setTextColor(getResources().getColor(R.color.red));
							add.setBackgroundColor(Color.TRANSPARENT);
					  
				} else if (show_soldout_status.get(position) == 1) {
					add.setText(String.valueOf("Sold Out")); 
					add.setTextColor(getResources().getColor(R.color.red)); 
					add.setBackgroundColor(Color.TRANSPARENT); 
				} else {
					add.setOnClickListener(new OnClickListener() {
 
						@Override  
						public void onClick(View v) { 
							// TODO Auto-generated method stub

							final Dialog dialog = new Dialog(BuyTickets.this);
							dialog.setContentView(R.layout.dialog);
							dialog.setTitle("Number of Tickets");
							ListView listView = (ListView) dialog
									.findViewById(R.id.myListView);
							list = new ArrayList<Integer>();

							for (int i = minimum_persons.get(position); i <= ticket_buy_limit
									.get(position); i++) {
								list.add(i);
							}

							// ArrayAdapter<String> adapter = new
							// ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
							// android.R.id.text1, dummies);
							ArrayAdapter<Integer> ad = new ArrayAdapter<Integer>(
									BuyTickets.this,
									R.layout.single_item_layout,
									R.id.singleItem, list);
							ad.notifyDataSetChanged();
							listView.setAdapter(ad);
							listView.setOnItemClickListener(new OnItemClickListener() {
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// do something on click
									System.out.println("values are" + arg0
											+ arg1 + arg2 + arg3);
									ticket_count = list.get(arg2);
									Log.i("getting value  is", ""
											+ ticket_count);
									add.setText(String.valueOf(ticket_count));
									final int value1 = ticket_count
											* price_per_ticket.get(position);
									System.out.println("value1" + value1);
									total.setText("Rs. "
											+ String.valueOf(value1));
									
									
									// details.remove(id.get(position));
									System.out.println("size of details"
											+ details.size());
									/* details.notify(); */
									
									details.put(position,
											(title1.get(position) + ":"+ ticket_count + ":" + price_per_ticket.get(position))+ ":"+ tax.get(position)+ ":"+ id.get(position));
									Intent in = new Intent(BuyTickets.this, BuyTickets.class);
									System.out.println("details size" + details.get(0));
									in.putExtra("select", position);
									BuyTickets.this.finish();
									event.setDetails(details);
									startActivity(in);
									overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
									 /* try {
								            BuyTickets.this.Update_display();
								        } catch (Exception e) {
								            // TODO Auto-generated catch block
								            e.printStackTrace();                    
								        }          */ 
									//   
									/*runOnUiThread(new Runnable() 
									{ 
									   public void run() 
									   {
										   add.setText(String.valueOf(ticket_count));
											
											total.setText("Rs. "
													+ String.valueOf(value1));
										  
									   } 
									});  */
									/*if(st==1){
										add.setText(String.valueOf(ticket_count));
										
										total.setText("Rs. "
												+ String.valueOf(value1));
									}*/
									
									dialog.dismiss(); 
								}
							
							});
						
							dialog.show();

							/*
							 * final Dialog d = new Dialog(BuyTickets.this); //
							 * d.setTitle("Number of Tickets");
							 * d.setContentView(R.layout.dialog); Button b1 =
							 * (Button) d.findViewById(R.id.button1); Button b2
							 * = (Button) d.findViewById(R.id.button2); final
							 * NumberPicker np = (NumberPicker)
							 * d.findViewById(R.id.numberPicker1);
							 * np.setMaxValue(100); np.setMinValue(0);
							 * np.setValue(in); np.setSelected(true);
							 * np.setWrapSelectorWheel(true);
							 * np.setOnClickListener(this);
							 * b1.setOnClickListener(new OnClickListener() {
							 * 
							 * @Override public void onClick(View v) {
							 * d.dismiss(); update(price,add,np.getValue()); }
							 * }); b2.setOnClickListener(new OnClickListener() {
							 * 
							 * @Override public void onClick(View v) {
							 * d.dismiss(); } }); d.show();
							 */
							
						}
						
					});
				
				}
			} else {

			}
			return convertView;

		}

	}
public int Update_display(){
	
	int success=1;
	st=0;
	tax1=0.0;
	discount=0.0; 
	for (Map.Entry<Integer, String> entry : details.entrySet()) { 
		Integer key = entry.getKey();
		String thing = entry.getValue();
		String[] str = thing.split(":");
	 	st=st+Integer.parseInt(str[1])*Integer.parseInt(str[2]);
	 	try{
			if(str.length>6){
				if(Integer.parseInt(str[8]) ==1){
			if(Integer.parseInt(str[1])<=Integer.parseInt(str[7])){
				discount=discount+((Integer.parseInt(str[1])*Integer.parseInt(str[2]))*Double.parseDouble(str[6]))/100;
				}else{
					discount=discount+Integer.parseInt(str[7])*Integer.parseInt(str[2])*Double.parseDouble(str[6])/100;	
				}
				System.out.println("discount"+(Integer.parseInt(str[1])*Integer.parseInt(str[2]))*Double.parseDouble(str[6])+discount);
				}else{
					if(Integer.parseInt(str[1])<=Integer.parseInt(str[7])){
						discount=discount+(Integer.parseInt(str[1])*Integer.parseInt(str[8]));
						}else{
							discount=discount+Integer.parseInt(str[7])*Integer.parseInt(str[8]);	
						}
				}
				
			}
		}catch(Exception e){
			
		}
		
						
	} 
	double total;
	if(tax1==0.0 && discount ==0.0){
		linearLayout3.setVisibility(View.GONE);
		view71.setVisibility(View.GONE);
		linearLayout5.setVisibility(View.GONE);
		view51.setVisibility(View.GONE);
	total=st;
	txt_id12.setText(""+st);
	txt_id32.setText(""+total);
	}else if(discount ==0.0){
		linearLayout5.setVisibility(View.VISIBLE);
		view51.setVisibility(View.VISIBLE);
	total=st+tax1;
	txt_id12.setText(""+st);
	txt_id32.setText(""+total);
	 
	}else{ 
		if(discount !=0.0){
			linearLayout5.setVisibility(View.VISIBLE);
			view51.setVisibility(View.VISIBLE);
		}
		total=(st+tax1)-discount;
		txt_id22.setText(""+tax1);
		txt_id12.setText(""+st);
		txt_id72.setText(""+discount);
		txt_id32.setText(""+total);
	}
	/*txt_id12.setText(""+st);
	txt_id72.setText(""+discount);
	txt_id32.setText(""+total);*/
	return success;
}
public void Update_display1(){
	
	st=0;
	tax1=0.0;
	discount=0.0; 
	for (Map.Entry<Integer, String> entry : details.entrySet()) { 
		Integer key = entry.getKey(); 
		String thing = entry.getValue();
		System.out.println("valuesvalues" + key + thing); 
		String[] str = thing.split(":");
	 
		st=st+Integer.parseInt(str[1])*Integer.parseInt(str[2]); 
		try{
			if(str.length>6){
				if(Integer.parseInt(str[8]) ==1){
			if(Integer.parseInt(str[1])<=Integer.parseInt(str[7])){
				discount=discount+((Integer.parseInt(str[1])*Integer.parseInt(str[2]))*Double.parseDouble(str[6]))/100;
				}else{
					discount=discount+Integer.parseInt(str[7])*Integer.parseInt(str[2])*Double.parseDouble(str[6])/100;	
				}
				System.out.println("discount"+(Integer.parseInt(str[1])*Integer.parseInt(str[2]))*Double.parseDouble(str[6])+discount);
				}else{
					if(Integer.parseInt(str[1])<=Integer.parseInt(str[7])){
						discount=discount+(Integer.parseInt(str[1])*Integer.parseInt(str[8]));
						}else{
							discount=discount+Integer.parseInt(str[7])*Integer.parseInt(str[8]);	
						}
				}
				
			}
		}catch(Exception e){
			
		}
		
		if(Double.parseDouble(str[3]) != 0.0){
			tax1=tax1+Integer.parseInt(str[1])*Integer.parseInt(str[2])*Double.parseDouble(str[3])/100;
			}else{
				tax1=tax1+0.0; 
			} 
	
					
	} 
	double total;
	if(tax1==0.0 && discount ==0.0){
		linearLayout3.setVisibility(View.VISIBLE);
		view71.setVisibility(View.VISIBLE);
		linearLayout5.setVisibility(View.VISIBLE);
		view51.setVisibility(View.VISIBLE);
	total=st;
	txt_id12.setText(""+st);

	txt_id32.setText(""+total); 
	}else if(discount ==0.0){
		linearLayout5.setVisibility(View.VISIBLE);
		view51.setVisibility(View.VISIBLE);
	total=st+tax1;
	txt_id12.setText(""+st);
	txt_id32.setText(""+total);
	}else{
		linearLayout5.setVisibility(View.VISIBLE);
		view51.setVisibility(View.VISIBLE);
		total=(st+tax1)-discount;
		txt_id22.setText(""+tax1);
		txt_id12.setText(""+st);
		txt_id72.setText(""+discount);
		txt_id32.setText(""+total);
		System.out.println("after setting values"); 
	}
	/*txt_id12.setText(""+st);
	txt_id72.setText(""+discount);
	txt_id32.setText(""+total);*/

}
	@SuppressWarnings("rawtypes")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textView1 || v.getId() == R.id.event_title
				|| v.getId() == R.id.menuLayout) {
			BuyTickets.this.finish();
			startActivity(new Intent(BuyTickets.this, MainActivity.class));
		
			/*if (class_name.equalsIgnoreCase("LoginActivity")) {
				startActivity(new Intent(BuyTickets.this, MainActivity.class));
			}*/
			overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
		} else if (v.getId() == R.id.ordernow) {
			if (details.size() >= 1) {
				/*Intent in = new Intent(BuyTickets.this, OrderNow.class);
				System.out.println("details size" + details.get(0));
				in.putExtra("details", (HashMap) details);
				startActivity(in);
				overridePendingTransition(R.anim.trans_left_in,
						R.anim.trans_left_out);*/
				
				if (event.getUserId() > 0) {
					Intent in = new Intent(BuyTickets.this, OrderNow.class);
					System.out.println("details size" + details.get(0));
					//in.putExtra("details", (HashMap) details);
					event.setDetails(details);
					startActivity(in);
				} else {
					Intent in = new Intent(BuyTickets.this, LoginActivity.class);
					event.setDetails(details);
					in.putExtra("activity",
							"com.etisbew.eventsnow.android.ordernow.OrderNow");
					startActivity(in);
					overridePendingTransition(R.anim.trans_left_in,
							R.anim.trans_left_out);
				}
				
			} else {
				util.dialogExample1("Please select ticket quantity");
			}
		} else if (v.getId() == R.id.apply) {
			
			String s=promo.getText().toString();
			System.out.println("details size"+details.size()+details);
			for (Map.Entry<Integer, String> entry : details.entrySet()) {  
				int flag=0;
				Integer key = entry.getKey();
				String thing = entry.getValue();
				String[] things = thing.split(":");
				String thing1 = "";
				for(int l=0;l<5;l++){
					if(l==4){
						thing1=thing1+things[l];
					}else{
						thing1=thing1+things[l]+":";
					}
					
				} 
				if(key == select){
				TicketsBean ticket = tickets_details.get(key);
				
				ArrayList<Integer> l_promotion_id = new ArrayList<Integer>();
				ArrayList<String> l_discount_code = new ArrayList<String>();
				ArrayList<Integer> l_code_max_allowed= new ArrayList<Integer>();
				ArrayList<String> l_p_start_date= new ArrayList<String>();
				ArrayList<String> l_p_end_date= new ArrayList<String>();
				ArrayList<String> l_discount_type= new ArrayList<String>();
				ArrayList<Double> l_discount= new ArrayList<Double>();
				
				l_promotion_id=ticket.promotion_id; 
				l_discount_code=ticket.discount_code;
				l_code_max_allowed=ticket.code_max_allowed;
				l_p_start_date=ticket.p_start_date;
				l_p_end_date=ticket.p_end_date;
				l_discount_type=ticket.discount_type;
				l_discount=ticket.discount;
				
				//Toast.makeText(getApplicationContext(), "position :"+s+key+thing+l_promotion_id.size(),Toast.LENGTH_LONG).show();
				try{
				//	System.out.println("l_promotion_id"+l_promotion_id.size());
					for(int k=0;k<l_promotion_id.size();k++){
						if(l_discount_code.get(k).equalsIgnoreCase(s)){ 
							SimpleDateFormat sdf = new SimpleDateFormat( 
									"yyyy-MM-dd HH:mm:s", Locale.US); 
							String currentDateandTime = sdf.format(new Date());
								System.out.println("sss"+util.dateComparison2(currentDateandTime,l_p_start_date.get(k))+util.dateComparison1(currentDateandTime,l_p_end_date.get(k)));
							
							if (util.dateComparison2(currentDateandTime,l_p_start_date.get(k))==true && util.dateComparison1(currentDateandTime,l_p_end_date.get(k))==false) {
						//		System.out.println("value true");
								details.put(key,
										thing1+ ":"+ l_promotion_id.get(k)+ ":"+ l_discount.get(k)+":"+l_code_max_allowed.get(k)+":"+l_discount_type.get(k));
								flag=1;
							}
							 
						//	Toast.makeText(getApplicationContext(), "code matched :"+details,Toast.LENGTH_LONG).show();
							
							
						}else{ 
							
							//flag=2;
						}
					}
				}catch (Exception e){
					e.printStackTrace();
					
				}
				
				//
				/*details.put(position,
						(title1.get(position) + ":"+ ticket_count + ":" + price_per_ticket.get(position))+ ":"+ tax.get(position)+ ":"+ id.get(position));
		*/
				
				
						
			} 
				if(flag==1){
					Update_display1(); 
					flag=2;
				}else if(flag==2){
					Toast.makeText(getApplicationContext(), "code notmatched :"+s+details,Toast.LENGTH_LONG).show();
				}
			}
		}

	}

	@Override
	public void onBackPressed() {
		BuyTickets.this.finish();
		startActivity(new Intent(BuyTickets.this, MainActivity.class));
		
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		// TODO Auto-generated method stub
		Log.i("value is", "" + newVal);
	}

	@Override
	public void onResume() {
		
		super.onResume();

	}
}