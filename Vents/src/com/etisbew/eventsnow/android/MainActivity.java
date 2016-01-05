package com.etisbew.eventsnow.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.etisbew.eventsnow.android.aboutus.AboutUs;
import com.etisbew.eventsnow.android.beans.EventsBean;
import com.etisbew.eventsnow.android.beans.LocationsBean;
import com.etisbew.eventsnow.android.contact.Contact;
import com.etisbew.eventsnow.android.login.LoginActivity;
import com.etisbew.eventsnow.android.myfavorites.MyFavorites;
import com.etisbew.eventsnow.android.printticket.PrintTicket;
import com.etisbew.eventsnow.android.search.SearchActivity;
import com.etisbew.eventsnow.android.util.Utility;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity implements OnClickListener {
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private RelativeLayout mDrawerList;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	ScrollView scr;
	RelativeLayout rl_account;
	EventBean event;
	Utility util;
	String url;
	int visibility = 0;
	Button home_main, my_account, my_tickets, about_us, contact, my_favorites,
			login, locations, print_ticket;
	Typeface opensans_bold, opensans_regular;
	String category = "", keyword = "";
	String frompayment = "";
	ArrayList<EventsBean> events_upcoming;
	LinearLayout linear; 

	ArrayList<LocationsBean> categories_details;
	// ImageView iv;
	AQuery androidAQuery;
	// QuickAction quickAction;
	FragmentManager fragmentManager1;
	android.support.v7.app.ActionBar mActionBar;
	
	String open="not opened";
	Fragment fragment;
	SharedPreferences pref;
	Editor editor;
	public static final String MyPREFERENCES = "MyPrefs";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 

		
		util = new Utility(MainActivity.this);
		events_upcoming = new ArrayList<EventsBean>();
		categories_details = new ArrayList<LocationsBean>();
		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		if (extras != null) {
			category = extras.getString("category");
			keyword = extras.getString("keyword");
			frompayment = extras.getString("frompayment");
			open=extras.getString("open");
		}
		androidAQuery = new AQuery(MainActivity.this);

		opensans_bold = Typeface.createFromAsset(this.getAssets(),"OpenSans-Bold.ttf");
		opensans_regular = Typeface.createFromAsset(this.getAssets(),"OpenSans-Regular.ttf");

		event = (EventBean) getApplicationContext();
		pref = getApplicationContext().getSharedPreferences("MyPref",
				MODE_PRIVATE);
		editor = pref.edit();
		if(pref.getInt("user_id", 0)>0){
			event.setUserId(pref.getInt("user_id", 0));
			event.setUserName(pref.getString("user_name", ""));
			event.setUser_image(pref.getString("user_image", ""));
			event.setEmail(pref.getString("email", ""));
			event.setPhone(pref.getString("phone", ""));
			event.setAddress(pref.getString("address", ""));
			event.setState_name(pref.getString("state", ""));
			event.setCity_name(pref.getString("city", ""));
			event.setState_id(pref.getInt("state_id", 0));
			event.setCity_id(pref.getInt("city_id", 0));
			
			editor.commit();
		}
		event.setTextNormal(opensans_regular);
		event.setTextBold(opensans_bold);

		slidemenu_init();

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (RelativeLayout) findViewById(R.id.left_drawer);
		// enable ActionBar app icon to behave as action to toggle nav drawer 
		mActionBar = getSupportActionBar();
		

		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

		TextView location_name = (TextView) mCustomView
				.findViewById(R.id.location_name);
		Button button1 = (Button) mCustomView.findViewById(R.id.button1);

		RelativeLayout relative3 = (RelativeLayout) mCustomView
				.findViewById(R.id.relativeLayout3);
		RelativeLayout relative4 = (RelativeLayout) mCustomView
				.findViewById(R.id.relativeLayout4);

		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
					mDrawerLayout.closeDrawer(mDrawerList);
				} else {

					mDrawerLayout.openDrawer(mDrawerList);

				}

			}
		});
		relative4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						SearchActivity.class));
			}
		});
		relative3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
					mDrawerLayout.closeDrawer(mDrawerList);
				} else {

					mDrawerLayout.openDrawer(mDrawerList);
					if (Build.VERSION.SDK_INT >= 11) {
						scr.fullScroll(View.FOCUS_DOWN);
					}
					linear.setVisibility(View.VISIBLE);
				}

			}
		});
		try{
		ArrayList<LocationsBean> locations = event.getLocations();
		for (int i = 0; i < locations.size(); i++) {
			final LocationsBean lb = locations.get(i);

	
			if (isTabletDevice(getResources()) == true) {
				if (event.getLocationId() == lb.locationId) {

					location_name.setText(lb.locationName);
				}
			} else {
				if (event.getLocationId() == lb.locationId) {

					String str = "";
					if (lb.locationName.length() > 4) {
						str = lb.locationName.substring(0, 3);
						str = str + "...";
							location_name.setText(str); 
					} else {
						location_name.setText(lb.locationName);
					}
				}

			}

		}
		}catch(Exception e){
			
		}

		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, // host Activity
				mDrawerLayout, // DrawerLayout object
				R.drawable.logo_list, // nav drawer image to replace 'Up' image
				R.string.app_name, // "open drawer" description for
									// accessibility
				R.string.app_name // "close drawer" description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				// mTitleTextView.setText(mTitle);
				mActionBar.setTitle(mTitle);
				// getActionBar().set
				supportInvalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				// mTitleTextView.setText(mDrawerTitle);
				mActionBar.setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			Fragment newContent = new MainFragment();
			if (event.city_status == 0) {
				try {
					if (frompayment.equalsIgnoreCase("frompayment")) {
						Fragment newContent1 = new MyTicketsFragment();
						switchFragment(newContent1);
						
					} else if(frompayment.equalsIgnoreCase("MyAccount")){
						Fragment newContent1 = new MyAccountFragment();
						switchFragment(newContent1);
						
					}else if(frompayment.equalsIgnoreCase("MyTickets")){
						Fragment newContent1 = new MyTicketsFragment();
						switchFragment(newContent1);
						
					}else {
						switchFragment(newContent);
						  
					}
				} catch (Exception e) {
					switchFragment(newContent);
				}
			} else {
				
				if (Build.VERSION.SDK_INT >= 11) {
					try {
						
					
						if (event.city_status == 1) {
							Fragment newContent1 = new LocationNotMatching(); 
							switchFragment(newContent1);
						}else{
							try {
								if (frompayment.equalsIgnoreCase("frompayment")) {
									Fragment newContent1 = new MyTicketsFragment();
									switchFragment(newContent1);
								}else if(frompayment.equalsIgnoreCase("MyAccount")){
									Fragment newContent1 = new MyAccountFragment();
									switchFragment(newContent1);
									
								}else if(frompayment.equalsIgnoreCase("MyTickets")){
									Fragment newContent1 = new MyTicketsFragment();
									switchFragment(newContent1);
									
								} else {
									switchFragment(newContent);
								}
							} catch (Exception e) {
								
								switchFragment(newContent);
							}
						
						}
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
			}

		}
	}

	private void slidemenu_init() {
		// TODO Auto-generated method stub
		// try {
		my_account = (Button) findViewById(R.id.my_account);

		TextView person_name = (TextView) findViewById(R.id.personname);
		person_name.setText(event.getUserName());
		person_name.setTypeface(event.getTextBold());

		my_tickets = (Button) findViewById(R.id.my_tickets);
		home_main = (Button) findViewById(R.id.home_main);
		about_us = (Button) findViewById(R.id.about_us);
		contact = (Button) findViewById(R.id.contact);
		my_favorites = (Button) findViewById(R.id.my_favorites);
		print_ticket = (Button) findViewById(R.id.print_ticket);
		login = (Button) findViewById(R.id.login);
		locations = (Button) findViewById(R.id.locations);
		// iv = (ImageView) findViewById(R.id.imageView1);
		scr = (ScrollView) findViewById(R.id.scrollView2);

		rl_account = (RelativeLayout) findViewById(R.id.account);
			if (event.getUserId() > 0) {
			login.setText(R.string.logout);
			rl_account.setVisibility(View.VISIBLE);
			
		} else {
			rl_account.setVisibility(View.GONE);
		}

		linear = (LinearLayout) findViewById(R.id.locationLayout);


		home_main.setTypeface(event.getTextBold());
		my_account.setTypeface(event.getTextBold());
		my_tickets.setTypeface(event.getTextBold());
		about_us.setTypeface(event.getTextBold());
		my_favorites.setTypeface(event.getTextBold());
		contact.setTypeface(event.getTextBold());
		login.setTypeface(event.getTextBold());
		locations.setTypeface(event.getTextBold());
		print_ticket.setTypeface(event.getTextBold());

		home_main.setOnClickListener(this);
		my_account.setOnClickListener(this);
		my_tickets.setOnClickListener(this);
		about_us.setOnClickListener(this);
		contact.setOnClickListener(this);
		my_favorites.setOnClickListener(this);
		login.setOnClickListener(this);
		locations.setOnClickListener(this);
		print_ticket.setOnClickListener(this);
		
		if (Build.VERSION.SDK_INT >= 11) { 
			try { 
				 
				ArrayList<LocationsBean> locations1 = event.getLocations();
				
					for (int i = 0; i < locations1.size(); i++) {
					final LocationsBean lb = locations1.get(i);
					
					Button btn = new Button(MainActivity.this);
					btn.setText(lb.locationName);
					// btn.setLeft(10);
					btn.setTypeface(event.getTextBold());
					btn.setPadding(10, 0, 0, 0);
					View v = new View(MainActivity.this); 
					v.setPadding(0, 0, 0, 0);
					btn.setBackgroundColor(getResources()
							.getColor(R.color.hash));

					btn.setBackgroundResource(R.color.slide_bg);
					if (event.getLocationId() == lb.locationId) {
						btn.setTextColor(getResources().getColor(R.color.red));
					} else {
						btn.setTextColor(getResources().getColor( 
								R.color.menu_text));
					}
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT);

					layoutParams.gravity = RelativeLayout.ALIGN_PARENT_LEFT;
					LinearLayout.LayoutParams ll_params = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT, 1);
					v.setLayoutParams(ll_params);
					LinearLayout ll = new LinearLayout(MainActivity.this);
					ll.setId(lb.locationId);
					ll.addView(btn);
					ll.addView(v);
					ll.setPadding(110, 0, 0, 0);

					// ll.setBackgroundResource(R.color.menu_text);
					ll.setGravity(Gravity.LEFT);
					linear.addView(ll);

					btn.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							// do something when the button is clicked
							if (event.getLocationId() != lb.locationId) {
								event.setCity_status(0);
								event.setLocationId(lb.locationId);
								mDrawerLayout.closeDrawer(mDrawerList);
								startActivity(new Intent(MainActivity.this,
										SplashActivity.class));
								overridePendingTransition(
										R.anim.trans_right_in,
										R.anim.trans_right_out);
							}
						}
					});
				}

			} catch (Exception e) {
				e.printStackTrace();
				startActivity(new Intent(MainActivity.this,
						SplashActivity.class));
				overridePendingTransition(R.anim.trans_right_in,
						R.anim.trans_right_out);
			}
			} else {
			try {
				
				ArrayList<LocationsBean> locations = event.getLocations();
				for (int i = 0; i < locations.size(); i++) {
					final LocationsBean lb = locations.get(i);
					
					Button btn = new Button(MainActivity.this);
					btn.setText(lb.locationName);
					// btn.setLeft(10);
					btn.setTypeface(event.getTextBold());
					btn.setPadding(50, 0, 0, 0);
				

					btn.setBackgroundResource(R.color.slide_bg);
					if (event.getLocationId() == lb.locationId) {
						btn.setTextColor(getResources().getColor(R.color.red));
					} else {
						btn.setTextColor(getResources().getColor(
								R.color.menu_text));
					}
					linear.addView(btn, i);
					btn.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							// do something when the button is clicked
							event.setLocationId(lb.locationId);
							mDrawerLayout.closeDrawer(mDrawerList);
							startActivity(new Intent(MainActivity.this,
									SplashActivity.class));
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_main:
			mDrawerLayout.closeDrawer(mDrawerList);
			startActivity(new Intent(MainActivity.this, SplashActivity.class));
			break;
		case R.id.my_account:
			mDrawerLayout.closeDrawer(mDrawerList);

			if (event.getUserId() > 0) {

				Fragment newContent1 = new MyAccountFragment();
				switchFragment(newContent1);

			} else {
				Intent in = new Intent(MainActivity.this, LoginActivity.class);
			//	in.putExtra("activity","com.etisbew.eventsnow.android.MainActivity");
				in.putExtra("target", "MyAccount");
				startActivity(in);
				overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
			}
			break;
 
		case R.id.my_tickets:
			mDrawerLayout.closeDrawer(mDrawerList);
			if (event.getUserId() > 0) {

				Fragment newContent1 = new MyTicketsFragment();
				switchFragment(newContent1);
			} else {
				Intent in = new Intent(MainActivity.this, LoginActivity.class);
			//	in.putExtra("activity","com.etisbew.eventsnow.android.MainActivity");
				in.putExtra("target", "MyTickets");
				startActivity(in);
				overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
			}
			break;
		case R.id.about_us:
			mDrawerLayout.closeDrawer(mDrawerList);
			startActivity(new Intent(MainActivity.this, AboutUs.class));
			break;
		case R.id.print_ticket:
			mDrawerLayout.closeDrawer(mDrawerList);
			startActivity(new Intent(MainActivity.this, PrintTicket.class));
			break;
		case R.id.contact:
			mDrawerLayout.closeDrawer(mDrawerList);
			startActivity(new Intent(MainActivity.this, Contact.class));
			break;
		case R.id.my_favorites:

			mDrawerLayout.closeDrawer(mDrawerList);
			if (event.getUserId() > 0) {
				startActivity(new Intent(MainActivity.this, MyFavorites.class));
				overridePendingTransition(R.anim.trans_right_in,
						R.anim.trans_right_out);
			} else {
				Intent in = new Intent(MainActivity.this, LoginActivity.class);
			//	in.putExtra("activity","com.etisbew.eventsnow.android.myfavorites.MyFavorites");
				in.putExtra("target", "MyFavorites");
				startActivity(in);
				overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
			}
			break;
		case R.id.login:
			mDrawerLayout.closeDrawer(mDrawerList);
			if (login.getText().toString().equalsIgnoreCase("login")) {
				Intent in = new Intent(MainActivity.this, LoginActivity.class);
				//in.putExtra("activity","com.etisbew.eventsnow.android.MainActivity");
				in.putExtra("target", "MainActivity");
				startActivity(in);
				overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
			} else if (login.getText().toString().equalsIgnoreCase("Logout")) {
				editor.clear();
				editor.commit();
				
				event.setUserId(0);
				event.setUserName("");
				event.setUser_image("");
				event.setEmail("");
				event.setPhone("");
							

				Intent in = new Intent(MainActivity.this, LoginActivity.class);
				//in.putExtra("activity","com.etisbew.eventsnow.android.MainActivity");
				startActivity(in);
				overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
			}
			break;
		case R.id.locations:
			scr.fullScroll(ScrollView.FOCUS_UP);
			if (Build.VERSION.SDK_INT >= 11) {
				scr.smoothScrollTo(0, (int) rl_account.getY());
			}
			if (visibility == 0) {
				linear.setVisibility(View.VISIBLE);
				visibility = 1;
			} else if (visibility == 1) {

				linear.setVisibility(View.GONE);
				// linear.setBackgroundResource(R.drawable.slide_bg);
				visibility = 0;
			}
			break;

		}
		overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.search_category) {
	
			startActivity(new Intent(MainActivity.this, SearchActivity.class));

		} else if (item.getItemId() == R.id.search_category1) {

			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {

				mDrawerLayout.openDrawer(mDrawerList);
				if (Build.VERSION.SDK_INT >= 11) {
					scr.fullScroll(View.FOCUS_DOWN);
				}
				linear.setVisibility(View.VISIBLE);
			}

		 
		}
		return true;
	}

	private void switchFragment(Fragment fragment) {

		this.fragment=fragment;
			mDrawerLayout.closeDrawer(mDrawerList);
	
		fragmentManager1 = MainActivity.this.getSupportFragmentManager();
		fragmentManager1.beginTransaction().setCustomAnimations(R.anim.trans_right_in,R.anim.trans_right_out).replace(R.id.events_now, fragment).commit();

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = "";
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public ScaleDrawable LengthConversion(int id) {
		Drawable drawable3 = getResources().getDrawable(id);
		drawable3.setBounds(0, 0, (int) (drawable3.getIntrinsicWidth() * 0.9),
				(int) (drawable3.getIntrinsicHeight() * 0.9));
		ScaleDrawable sd3 = new ScaleDrawable(drawable3, 0, 100, 100);
		return sd3;
	}

	@Override 
	public void onBackPressed() {
		/*super.onBackPressed();
		
		}*/ 

	}

	@Override
	public void onResume() {
		//slidemenu_init(); 
		super.onResume();
	//	rl_account = (RelativeLayout) findViewById(R.id.account);
		if (event.getUserId() > 0) {
		login.setText(R.string.logout);
		rl_account.setVisibility(View.VISIBLE);
	
	} else {
		login.setText(R.string.login);
		rl_account.setVisibility(View.GONE);
	}
		
	}

	public boolean isTabletDevice(Resources resources) {
		int screenLayout = resources.getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		boolean isScreenLarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE);
		if (Build.VERSION.SDK_INT > 11) {
		boolean isScreenXlarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE);
		return (isScreenLarge || isScreenXlarge);
		}else{
			return (isScreenLarge );
		}
		
	}

}
