package com.etisbew.eventsnow.android.mytickets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import com.etisbew.eventsnow.android.DirectionsJSONParser;
import com.etisbew.eventsnow.android.EventBean;
import com.etisbew.eventsnow.android.GPSTracker;
import com.etisbew.eventsnow.android.R;
import com.etisbew.eventsnow.android.beans.TicketsBean;
import com.etisbew.eventsnow.android.readmore.ReadMoreActivity;
import com.etisbew.eventsnow.android.util.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

@SuppressLint("NewApi")
public class MyTicketDetails extends FragmentActivity implements
		OnClickListener, OnMarkerClickListener, OnMarkerDragListener {
	Button buyticket, readmore;
	int user_id;
	TextView back, event_title, event_date, event_time, event_desc, event_day,
			event_full_desc, address, contactinfo, name, phone, contact,
			website, facebook, twitter, button1, button2, button3,category1;
	EventBean event;
	int event_id, success_id, del_id;
	ImageView image;
	Utility util;
	String url, url1, url2, url3, url4;
	AQuery androidAQuery;
	private GoogleMap googleMap;
	FragmentManager mapFrag;
	Button zoombtn;
	RelativeLayout menuLayout;
	ArrayList<Integer> id_list;
	LinearLayout addtocalendar, addtofavorite;
	String title, description, startdate, enddate, category, venue, thumbnail,
			city, region, lat, lon, cperson, web, email, phoneno, gps,
			facebook1, twitter1, event_type, pdf_link;
	ArrayList<LatLng> markerPoints;
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
	ArrayList<TicketsBean> tickets_details;
	GPSTracker gps1;
	Double latitude, longitude;   
	int mMode=0;
    final int MODE_DRIVING=0;
    final int MODE_BICYCLING=1;
    final int MODE_WALKING=2;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytickets_view);
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {
			System.out.println("gps" + gps);
			event_id = extras.getInt("event_id", 0);
			title = extras.getString("title");
			description = extras.getString("description");
			startdate = extras.getString("startdate");
			enddate = extras.getString("enddate");
			category = extras.getString("category");
			venue = extras.getString("venue");
			thumbnail = extras.getString("thumbnail");
			city = extras.getString("city");
			region = extras.getString("region");
			gps = extras.getString("gps");
			event_type = extras.getString("event_type");

			cperson = extras.getString("cperson");
			web = extras.getString("web");
			email = extras.getString("email");
			phoneno = extras.getString("phoneno");
			facebook1 = extras.getString("facebook1");
			twitter1 = extras.getString("twitter1");
			pdf_link = extras.getString("pdf_link"); 

		}
		
		event = (EventBean) getApplicationContext();
		util = new Utility(MyTicketDetails.this);
		androidAQuery = new AQuery(MyTicketDetails.this);
		event_id = event.getEventId();
		url = util.setEventsDetails(event_id);
		user_id = event.getUserId();

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

		tickets_details = new ArrayList<TicketsBean>();

		back = (TextView) findViewById(R.id.textView1);
		event_title = (TextView) findViewById(R.id.event_title);
		event_desc = (TextView) findViewById(R.id.event_desc);
		event_day = (TextView) findViewById(R.id.event_day);
		contactinfo = (TextView) findViewById(R.id.contactinfo);
		event_full_desc = (TextView) findViewById(R.id.event_full_desc);
		address = (TextView) findViewById(R.id.address);
		buyticket = (Button) findViewById(R.id.buyticket);
		readmore = (Button) findViewById(R.id.readmore);
		button1 = (TextView) findViewById(R.id.button1);
		button2 = (TextView) findViewById(R.id.button2);
		button3 = (TextView) findViewById(R.id.button3);

		id_list = new ArrayList<Integer>();
		event_title.setText(title);
		image = (ImageView) findViewById(R.id.item_icon);

		zoombtn = (Button) findViewById(R.id.zoombtn);

		addtocalendar = (LinearLayout) findViewById(R.id.adddtocalendar);
		addtofavorite = (LinearLayout) findViewById(R.id.adddtofavorite);
		menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);

		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		contact = (TextView) findViewById(R.id.contact);
		website = (TextView) findViewById(R.id.website);
		facebook = (TextView) findViewById(R.id.facebook);
		twitter = (TextView) findViewById(R.id.twitter);
		category1= (TextView) findViewById(R.id.category);

		event_title.setTypeface(event.getTextBold());
		buyticket.setTypeface(event.getTextBold());
		readmore.setTypeface(event.getTextBold());
		button1.setTypeface(event.getTextBold());
		button2.setTypeface(event.getTextBold());
		category1.setTypeface(event.getTextBold());
		
		event_desc.setTypeface(event.getTextBold());
		event_day.setTypeface(event.getTextBold());
		event_full_desc.setTypeface(event.getTextNormal());
		address.setTypeface(event.getTextNormal());
		contactinfo.setTypeface(event.getTextBold());
		address.setTypeface(event.getTextNormal());
		name.setTypeface(event.getTextNormal());
		phone.setTypeface(event.getTextNormal());
		contact.setTypeface(event.getTextNormal());
		website.setTypeface(event.getTextNormal());
		facebook.setTypeface(event.getTextNormal());
		twitter.setTypeface(event.getTextNormal());

		buyticket.setOnClickListener(this);
		readmore.setOnClickListener(this);
		zoombtn.setOnClickListener(this);
		facebook.setOnClickListener(this);
		twitter.setOnClickListener(this);
		addtocalendar.setOnClickListener(this);
		addtofavorite.setOnClickListener(this);
		phone.setOnClickListener(this);
		contact.setOnClickListener(this);
		website.setOnClickListener(this);
		facebook.setOnClickListener(this);
		twitter.setOnClickListener(this);
		back.setOnClickListener(this);
		event_title.setOnClickListener(this);
		menuLayout.setOnClickListener(this);
		// iv.setOnClickListener(this);
		gps1 = new GPSTracker(getBaseContext()) {
		};
		if (gps1.canGetLocation()) {
			latitude = gps1.getLatitude();
			longitude = gps1.getLongitude();
		}
		update();
	}

	@Override
	public void onBackPressed() {
	//	super.onBackPressed();
		MyTicketDetails.this.finish();
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.readmore:

			Intent n = new Intent(MyTicketDetails.this, ReadMoreActivity.class);
			n.putExtra("title", title);
			n.putExtra("date", util.dateConvert(startdate, enddate));
			n.putExtra("desc", description);
			startActivity(n);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;
		case R.id.buyticket:
			System.out.println("event_id" + event_id);
			url4 = util.setTickets(event_id);
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_link));
			startActivity(intent);

			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;
		case R.id.zoombtn:
			Intent intent12 = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?   saddr="
							+ latitude + "," + longitude + "&daddr=" + lat
							+ "," + lon));
			intent12.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent12.addCategory(Intent.CATEGORY_LAUNCHER);
			intent12.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			startActivity(intent12);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;

		case R.id.facebook:
			Intent intent11 = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook1));
			startActivity(intent11);

			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);

			break;
		case R.id.twitter:
			Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter1));
			startActivity(intent1);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);

			break;

		
		case R.id.website:
			String url = web;
			Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent3);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;

		case R.id.phone:
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:+" + phoneno));
			startActivity(callIntent);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;

		case R.id.contact:
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.setType("plain/text");
			// sendIntent.setData(Uri.parse("test@gmail.com"));
			sendIntent.setClassName("com.google.android.gm",
					"com.google.android.gm.ComposeActivityGmail");
			sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, "test");
			sendIntent.putExtra(Intent.EXTRA_TEXT,
					"hello. this is a message sent from my demo app :-)");
			startActivity(sendIntent);
			overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			break;

		case R.id.textView1:
			finish();
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
			break;
		case R.id.event_title:

			finish();
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
			break;
		case R.id.menuLayout:

			finish();
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
			break;

		}

	}

	private void parsingMapData() {
		try {
			googleMap.clear();
			/*
			 * if(map_flg==2) {
			 */
			if (googleMap != null) {

				// marker using custom image
				System.out.println("latlon in " + lat + lon);
				LatLng WALL_STREET = new LatLng(Double.parseDouble(lat),
						Double.parseDouble(lon));
				googleMap
						.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
								.position(WALL_STREET)
								.title("")
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

			}
			
			LatLng centerCoord = new LatLng(17.3660, 78.4760);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerCoord,
					10));

		} catch (Exception e) {
		}
	}

	private void addGoogleMap() {
		// check if we have got the googleMap already
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.setOnMarkerClickListener(this);
			googleMap.setOnMarkerDragListener(this);
		}

	}

	public void update() {
		String[] gps_array = gps.split(",");
		lat = gps_array[0];
		lon = gps_array[1];
		addGoogleMap();
		parsingMapData();
		event_day.setText(util.dateConvert(startdate, enddate));
		event_full_desc.setText(Html.fromHtml(description));
		address.setText(venue);
		name.setText(cperson);
		phone.setText(phoneno);
		category1.setText(category);
		LatLng WALL_STREET = new LatLng(
				Double.parseDouble(lat),
				Double.parseDouble(lon));
		
		  LatLng userLocation = new LatLng(latitude,longitude);
		// Adding new item to the ArrayList
		markerPoints.add(userLocation);
        markerPoints.add(WALL_STREET); 
        
        if(markerPoints.size() >= 2){         
        	
        	
            
//            LatLng origin = markerPoints.get(0);
//            LatLng dest = markerPoints.get(1);
            
            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(userLocation, WALL_STREET);                
            
            DownloadTask downloadTask = new DownloadTask();
             
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
            
            drawStartStopMarkers();   
        }
        
		LatLng centerCoord = new LatLng(
				Double.parseDouble(lat),
				Double.parseDouble(lon));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerCoord,
				12));

//	}
	googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
		@Override
		public void onInfoWindowClick(Marker marker) {
		}
	});
		if (facebook1.length() == 1 || facebook1.length() == 0) {
			facebook.setVisibility(View.GONE);
		} else {
			facebook.setVisibility(View.VISIBLE);
			facebook.setText(R.string.facebook);
		}
		if (twitter1.length() == 1 || twitter1.length() == 0) {
			twitter.setVisibility(View.GONE);
		} else {
			twitter.setVisibility(View.VISIBLE);
			twitter.setText(R.string.twitter);
		}
		if (web.length() == 1 || web.length() == 0) {
			website.setVisibility(View.GONE);
		} else {
			website.setVisibility(View.VISIBLE);
			website.setText(R.string.viewwebsite);
		}
		if (email.length() == 1 || email.length() == 0) {
			contact.setVisibility(View.GONE);
		} else {
			contact.setVisibility(View.VISIBLE);
			contact.setText(R.string.organizer);
		}
		if (cperson.length() == 1 || cperson.length() == 0) {
			name.setVisibility(View.GONE);
		} else {
			name.setVisibility(View.VISIBLE);
		}
		if (phoneno.length() == 1 || phoneno.length() == 0) {
			phone.setVisibility(View.GONE);
		} else {
			phone.setVisibility(View.VISIBLE);
		}
		androidAQuery.id(image).image(thumbnail, true, true);
		try {
			if (event.getUserId() > 0) {
				url3 = util.setMyList(event.getUserId());
				new GettingEvents().execute();

			}
		} catch (Exception e) {

		}
	}

	private void drawStartStopMarkers(){
        
        for(int i=0;i<markerPoints.size();i++){
        
            // Creating MarkerOptions
            MarkerOptions options = new MarkerOptions();            
            
            
            // Setting the position of the marker
            options.position(markerPoints.get(i) );
            
            /** 
             * For the start location, the color of marker is GREEN and
             * for the end location, the color of marker is RED.
             */
            if(i==0){
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else if(i==1){
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }            
            
            // Add new marker to the Google Map Android API V2
            googleMap.addMarker(options);
        }        
    }
    
    
    private String getDirectionsUrl(LatLng origin,LatLng dest){
                    
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;    
                    
        // Sensor enabled
        String sensor = "sensor=false";            
        
        // Travelling Mode
        String mode = "mode=driving";    
        
//        if(rbDriving.isChecked()){
            mode = "mode=driving";
            mMode = 0 ;
//        }else if(rbBiCycling.isChecked()){
//            mode = "mode=bicycling";
//            mMode = 1;
//        }else if(rbWalking.isChecked()){
//            mode = "mode=walking";
//            mMode = 2;
//        }
        
                    
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;
                    
        // Output format
        String output = "json";
        
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        
        
        return url;
    }
    private class DownloadTask extends AsyncTask<String, Void, String>{            
        
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
                
            // For storing data from web service
            String data = "";
                    
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;        
        }
        
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {            
            super.onPostExecute(result);            
            
            ParserTask parserTask = new ParserTask();
            
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
                
        }        
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
        
        // Parsing the data in non-ui thread        
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            
            JSONObject jObject;    
            List<List<HashMap<String, String>>> routes = null;                       
            
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                
                // Starts parsing data
                routes = parser.parse(jObject);    
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
        
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                
                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);                    
                    
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);    
                    
                    points.add(position);                        
                }
                
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                
                // Changing the color polyline according to the mode
                if(mMode==MODE_DRIVING)
                    lineOptions.color(Color.RED);
                else if(mMode==MODE_BICYCLING)
                    lineOptions.color(Color.GREEN);
                else if(mMode==MODE_WALKING)
                    lineOptions.color(Color.BLUE);                
            }
            
            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline(lineOptions);
            
        }            
    }  
    
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }
@Override
public boolean onMarkerClick(Marker arg0) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public void onMarkerDrag(Marker arg0) {
	// TODO Auto-generated method stub

}

@Override
public void onMarkerDragEnd(Marker arg0) {
	// TODO Auto-generated method stub

}

@Override
public void onMarkerDragStart(Marker arg0) {
	// TODO Auto-generated method stub

}
	

	

	public void update3() {
		try {
			if (id_list.contains(event_id)) {
				button1.setText(getResources().getString(R.string.removefav));
				button3.setBackgroundResource(R.drawable.add_to_favorite2);
			}

		} catch (Exception e) {

		}
	}

	private class GettingEvents extends AsyncTask<String, Void, String> {

		private static final String EVENTSLIST = "EventsList";
		private static final String EVENT = "Event";
		private static final String ID = "ID";
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			progressDialog = new ProgressDialog(MyTicketDetails.this);
			progressDialog.setMessage("Loading ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			return util.getXmlFromUrl(url3);

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
							if (name.equalsIgnoreCase(EVENTSLIST)) {
							} else {
								if (name.equalsIgnoreCase(ID)) {
									id_list.add(Integer.parseInt(parser
											.nextText()));
								}
							}

							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(EVENTSLIST)) {

							}
							break;
						}
						eventType = parser.next();
					}

				} catch (Exception e) {

					progressDialog.dismiss();

				}
				update3();
			}

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

		}

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	
}
