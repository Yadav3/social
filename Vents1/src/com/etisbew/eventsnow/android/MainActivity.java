package com.etisbew.eventsnow.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
public class MainActivity extends FragmentActivity implements OnClickListener {
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
	Button home, my_account, my_tickets, about_us, contact, my_favorites,
			login, locations,print_ticket;
	Typeface opensans_bold, opensans_regular;
	String category = "", keyword = "";
	String frompayment = "";
	ArrayList<EventsBean> events_upcoming;
	LinearLayout linear;
	
	ArrayList<LocationsBean> categories_details;
	ImageView iv;
	AQuery androidAQuery;
	QuickAction quickAction;
	FragmentManager fragmentManager1;
	@SuppressLint("NewApi") 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent iin = getIntent();
		Bundle extras = iin.getExtras();
		util = new Utility(MainActivity.this);
		events_upcoming = new ArrayList<EventsBean>();
		categories_details = new ArrayList<LocationsBean>();
		if (extras != null) {
			category = extras.getString("category");
			keyword = extras.getString("keyword");
			frompayment = extras.getString("frompayment");
		}
		androidAQuery = new AQuery(MainActivity.this);
				

		opensans_bold = Typeface.createFromAsset(this.getAssets(),
				"OpenSans-Bold.ttf");
		opensans_regular = Typeface.createFromAsset(this.getAssets(),
				"OpenSans-Regular.ttf");

		event = (EventBean) getApplicationContext();
		event.setTextNormal(opensans_regular);
		event.setTextBold(opensans_bold);

		slidemenu_init(); 

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (RelativeLayout) findViewById(R.id.left_drawer);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#ffffff")));
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
		// hiding the logo when we set from manifest file
		// getActionBar().setDisplayUseLogoEnabled(false);
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
				getActionBar().setTitle(mTitle);
				// getActionBar().set
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				// mTitleTextView.setText(mDrawerTitle);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			Fragment newContent = new MainFragment();
			System.out.println("city status" + event.city_status);
			if (event.city_status == 0) {
				try{
				if (frompayment.equalsIgnoreCase("frompayment")) {
					Fragment newContent1 = new MyTicketsFragment();
					switchFragment(newContent1);
				} else {
					switchFragment(newContent);
				}
				}catch(Exception e){
					switchFragment(newContent);
				}
			} else {
				// System.out.println("categories"+event.getCategories()+event.getCategories().size());
				try{
				if (frompayment.equalsIgnoreCase("frompayment")) {
					Fragment newContent1 = new MyTicketsFragment();
					switchFragment(newContent1);
				} else {
					switchFragment(newContent);
				}
				}catch(Exception e){
					switchFragment(newContent);
				}
				//util.dialogExample1("Oops there are no events listed for your location, Please check back again or select another location.");
				
				            new AlertDialog.Builder( this )
				             .setMessage( "Oops there are no events listed for your location, Please check back again or select another location." )
				            .setPositiveButton( "Choose location", new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int which) {
				                    Log.d( "AlertDialog", "Positive" );
				                    mDrawerLayout.openDrawer(mDrawerList);
				        			linear.setVisibility(View.VISIBLE);
				                }
				            })
				            .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int which) {
				                    Log.d( "AlertDialog", "Negative" );
				                }
				            } )
				            .show();
				        }
				 
		
		}
	}
	private void showStatusPopup(final Activity context, Point p) {

		   // Inflate the popup_layout.xml
		 //  LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.llStatusChangePopup);
		   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   View layout = layoutInflater.inflate(R.layout.popup_layout, null);

		   // Creating the PopupWindow
		   PopupWindow   changeStatusPopUp = new PopupWindow(context);
		   changeStatusPopUp.setContentView(layout); 
		   changeStatusPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
		   changeStatusPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		   changeStatusPopUp.setFocusable(true);

		   // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
		   int OFFSET_X = 120;
		   int OFFSET_Y = 250; 

		   //Clear the default translucent background
		   changeStatusPopUp.setBackgroundDrawable(new BitmapDrawable());

		   // Displaying the popup at the specified location, + offsets.
		  changeStatusPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
		}
	private void slidemenu_init() {
		// TODO Auto-generated method stub
		my_account = (Button) findViewById(R.id.my_account);

		TextView person_name = (TextView) findViewById(R.id.personname);
		person_name.setText(event.getUserName());
		person_name.setTypeface(event.getTextBold());

		my_tickets = (Button) findViewById(R.id.my_tickets);
		home = (Button) findViewById(R.id.home);
		about_us = (Button) findViewById(R.id.about_us);
		contact = (Button) findViewById(R.id.contact);
		my_favorites = (Button) findViewById(R.id.my_favorites);
		print_ticket = (Button) findViewById(R.id.print_ticket);
		login = (Button) findViewById(R.id.login);
		locations = (Button) findViewById(R.id.locations);
		iv = (ImageView) findViewById(R.id.imageView1);
		scr = (ScrollView) findViewById(R.id.scrollView2);

		rl_account = (RelativeLayout) findViewById(R.id.account);
		System.out.println("user_id" + event.getUserId());
		if (event.getUserId() > 0) {
			login.setText(R.string.logout);
			rl_account.setVisibility(View.VISIBLE);
			androidAQuery.id(iv).image(event.getUser_image(), true, true);
		} else {
			rl_account.setVisibility(View.GONE);
		}

		linear = (LinearLayout) findViewById(R.id.locationLayout);

		ScaleDrawable sd1 = LengthConversion(R.drawable.note);
		my_account.setCompoundDrawables(sd1.getDrawable(), null, null, null);

		ScaleDrawable sd2 = LengthConversion(R.drawable.view3);
		my_tickets.setCompoundDrawables(sd2.getDrawable(), null, null, null);

		ScaleDrawable sd3 = LengthConversion(R.drawable.contact3);
		about_us.setCompoundDrawables(sd3.getDrawable(), null, null, null);

		ScaleDrawable sd4 = LengthConversion(R.drawable.view4);
		contact.setCompoundDrawables(sd4.getDrawable(), null, null, null);

		ScaleDrawable sd5 = LengthConversion(R.drawable.home1);
		home.setCompoundDrawables(sd5.getDrawable(), null, null, null);

		ScaleDrawable sd6 = LengthConversion(R.drawable.favorite);
		my_favorites.setCompoundDrawables(sd6.getDrawable(), null, null, null);

		ScaleDrawable sd7 = LengthConversion(R.drawable.logout);
		login.setCompoundDrawables(sd7.getDrawable(), null, null, null);

		ScaleDrawable sd8 = LengthConversion(R.drawable.location);
		locations.setCompoundDrawables(sd8.getDrawable(), null, null, null);
		
		ScaleDrawable sd9 = LengthConversion(R.drawable.print);
		print_ticket.setCompoundDrawables(sd9.getDrawable(), null, null, null);

		home.setTypeface(event.getTextBold());
		my_account.setTypeface(event.getTextBold());
		my_tickets.setTypeface(event.getTextBold());
		about_us.setTypeface(event.getTextBold());
		my_favorites.setTypeface(event.getTextBold());
		contact.setTypeface(event.getTextBold());
		login.setTypeface(event.getTextBold()); 
		locations.setTypeface(event.getTextBold());
		print_ticket.setTypeface(event.getTextBold());

		home.setOnClickListener(this);
		my_account.setOnClickListener(this);
		my_tickets.setOnClickListener(this);
		about_us.setOnClickListener(this);
		contact.setOnClickListener(this);
		my_favorites.setOnClickListener(this);
		login.setOnClickListener(this);
		locations.setOnClickListener(this); 
		print_ticket.setOnClickListener(this); 
		try { 

			ArrayList<LocationsBean> locations = event.getLocations();
			System.out.println("locations" + locations.size());
				for (int i = 0; i < locations.size(); i++) {
				final LocationsBean lb = locations.get(i);
				/*
				 * LinearLayout.LayoutParams leftMarginParams = new
				 * LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT,
				 * LayoutParams.WRAP_CONTENT); leftMarginParams.leftMargin =
				 * 350; leftMarginParams.leftMargin=100;
				 */
				System.out.println("name" + lb.locationName);
				Button btn = new Button(MainActivity.this);
				btn.setText(lb.locationName);
				//btn.setLeft(10);
				btn.setTypeface(event.getTextBold());
				btn.setPadding(10, 0, 0, 0);
				View v = new View(MainActivity.this);
				v.setPadding(0, 0, 0, 0);
				  btn.setBackgroundColor(getResources().getColor(R.color.hash));
			
				btn.setBackgroundResource(R.color.slide_bg);
				if (event.getLocationId() == lb.locationId) {
					btn.setTextColor(getResources().getColor(R.color.red));
				} else {
					btn.setTextColor(getResources().getColor(R.color.menu_text));
				}   
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			            LinearLayout.LayoutParams.MATCH_PARENT,
			            LinearLayout.LayoutParams.MATCH_PARENT); 

			    layoutParams.gravity = RelativeLayout.ALIGN_PARENT_LEFT;
			       LinearLayout.LayoutParams ll_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                       1);
			       v.setLayoutParams(ll_params);
			  LinearLayout ll = new LinearLayout(MainActivity.this);
	              ll.setId(lb.locationId);
	              ll.addView(btn);
	              ll.addView(v); 
	              ll.setPadding(110, 0, 0, 0);   
	            
	              //ll.setBackgroundResource(R.color.menu_text);
	              ll.setGravity(Gravity.LEFT);
	              linear.addView(ll);  
	             
				btn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// do something when the button is clicked
						if (event.getLocationId() != lb.locationId) {
							event.setLocationId(lb.locationId);
							mDrawerLayout.closeDrawer(mDrawerList);
							startActivity(new Intent(MainActivity.this,
									SplashActivity.class));
						}
					}
				});
			} 
			
		
			
		} catch (Exception e) {
			startActivity(new Intent(MainActivity.this, SplashActivity.class));
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_right_out);
		}
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		// Bundle bundle = new Bundle();
		System.out.println("view is" + v.getId());
		switch (v.getId()) {
		case R.id.home:
			mDrawerLayout.closeDrawer(mDrawerList);
			//startActivity(new Intent(MainActivity.this, MainActivity.class));
			startActivity(new Intent(MainActivity.this,SplashActivity.class));
			break;
		case R.id.my_account:
			mDrawerLayout.closeDrawer(mDrawerList);
			
			if (event.getUserId() > 0) {
				
				Fragment newContent1 = new MyAccountFragment();
				switchFragment(newContent1);

			} else {
				Intent in = new Intent(MainActivity.this, LoginActivity.class);
				in.putExtra("activity",
						"com.etisbew.eventsnow.android.MainActivity");
				startActivity(in);
				overridePendingTransition(R.anim.trans_right_in,
						R.anim.trans_right_out);
			}
			break;

		case R.id.my_tickets:
			mDrawerLayout.closeDrawer(mDrawerList);
			if (event.getUserId() > 0) {
				
				Fragment newContent1 = new MyTicketsFragment();
				switchFragment(newContent1);
			} else {
				Intent in = new Intent(MainActivity.this, LoginActivity.class);
				in.putExtra("activity",
						"com.etisbew.eventsnow.android.MainActivity");
				startActivity(in);
				overridePendingTransition(R.anim.trans_right_in,
						R.anim.trans_right_out);
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
			// startActivity(new Intent(MainActivity.this, MyTickets.class));
			if (event.getUserId() > 0) {
				startActivity(new Intent(MainActivity.this, MyFavorites.class));
				overridePendingTransition(R.anim.trans_right_in,
						R.anim.trans_right_out);
			} else {
				Intent in = new Intent(MainActivity.this, LoginActivity.class);
				in.putExtra("activity",
						"com.etisbew.eventsnow.android.myfavorites.MyFavorites");
				startActivity(in);
				overridePendingTransition(R.anim.trans_right_in,
						R.anim.trans_right_out);
			}
			break;
		case R.id.login:
			mDrawerLayout.closeDrawer(mDrawerList);
			if (login.getText().toString().equalsIgnoreCase("login")) {
				Intent in = new Intent(MainActivity.this, LoginActivity.class);
				in.putExtra("activity",
						"com.etisbew.eventsnow.android.MainActivity");
				startActivity(in);
				overridePendingTransition(R.anim.trans_right_in,
						R.anim.trans_right_out);
			} else if (login.getText().toString().equalsIgnoreCase("Logout")) {

				event.setUserId(0);
				event.setUserName("");
				event.setUser_image("");

				Intent in = new Intent(MainActivity.this, LoginActivity.class);
				in.putExtra("activity",
						"com.etisbew.eventsnow.android.MainActivity");
				startActivity(in);
				overridePendingTransition(R.anim.trans_right_in,
						R.anim.trans_right_out);
			}
			break;
		case R.id.locations:
			scr.fullScroll(ScrollView.FOCUS_UP);
			scr.smoothScrollTo(0, (int) rl_account.getY());
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
		inflater.inflate(R.menu.splash, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		System.out.println("in search method" + item.getItemId());
		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}

		} else if (item.getItemId() == R.id.search_category) {
			System.out.println("in search method");

			startActivity(new Intent(MainActivity.this, SearchActivity.class));

		} else if(item.getItemId() == R.id.search_category1){
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
				linear.setVisibility(View.VISIBLE);
			}
			
			
			/*test(); 
			quickAction.show(home);*/
		}
		return true;
	}
public void test(){
	final ArrayList<LocationsBean> locations = event.getLocations();
	quickAction = new QuickAction(this, QuickAction.VERTICAL);
	ActionItem  nextItem;
	for (int i = 0; i < locations.size(); i++) {
		final LocationsBean lb = locations.get(i);
		
	nextItem 	= new ActionItem(lb.locationId, lb.locationName,getResources().getDrawable(R.drawable.location));
	
	
	//add action items into QuickAction
    quickAction.addActionItem(nextItem);
   
    
	
	}
	quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
		@Override
		public void onItemClick(QuickAction source, int pos, int actionId) {				
			ActionItem actionItem = quickAction.getActionItem(pos);
             
			//here we can filter which action item was clicked with pos or actionId parameter
				//Toast.makeText(getApplicationContext(), "Let's do some search action"+pos, Toast.LENGTH_SHORT).show();
				LocationsBean lb = locations.get(pos);
				event.setLocationId(lb.locationId);
				mDrawerLayout.closeDrawer(mDrawerList);
				startActivity(new Intent(MainActivity.this,
						SplashActivity.class));
			
		}
	});
	
	//set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
	//by clicking the area outside the dialog.
	quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {			
		@Override
		public void onDismiss() {
			Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
		} 
	});

}
	private void switchFragment(Fragment fragment) {

			mDrawerLayout.closeDrawer(mDrawerList);
		fragmentManager1 = MainActivity.this
				.getSupportFragmentManager();
		fragmentManager1
				.beginTransaction()
				.setCustomAnimations(R.anim.trans_right_in,
						R.anim.trans_right_out)
				.replace(R.id.events_now, fragment).commit();

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
		 Fragment prev_fragment = fragmentManager1.findFragmentByTag("MainFragment");
		
		   // String str=fragmentManager1.getFragment(arg0, backEntry);
		  //  Fragment fragment=getFragmentManager().findFragmentByTag(str);
		  /*  FragmentManager.BackStackEntry backEntry=getFragmentManager().getBackStackEntryAt(getActivity().getFragmentManager().getBackStackEntryCount()-1);
		    String str=backEntry.getName();
		    Fragment fragment=getFragmentManager().findFragmentByTag(str);*/
		 Toast.makeText(getApplicationContext(), "back"+prev_fragment, Toast.LENGTH_LONG).show();
		/*
		 * super.onBackPressed();
		 * overridePendingTransition(R.anim.trans_right_in,
		 * R.anim.trans_right_out); 
		 */
	}

	@Override
	public void onResume() {
		super.onResume();
		// put your code here...

	}
}
