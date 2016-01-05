package com.etisbew.eatz.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.etisbew.eatz.android.Explore.AccountDetails;
import com.etisbew.eatz.android.dropdownlist.MyDeals;
import com.etisbew.eatz.android.dropdownlist.MyFav;
import com.etisbew.eatz.android.dropdownlist.MyOrders;
import com.etisbew.eatz.android.dropdownlist.MyReservations;
import com.etisbew.eatz.android.dropdownlist.MyReviews;
import com.etisbew.eatz.android.dropdownlist.RedemptionHistory;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;

public class More extends BaseActivity {

	TextView tvStarProperty, tvVegRestaurants, tvGooutBuffet,
			tvRestaurantServingBuffet, tvMoreTitle, tvSunday, tvArticles,tvEvents;
	ImageView imgBack;
	private QuickAction quickAction;
	ImageView options;
	static String userLocation = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more);

		tvMoreTitle = (TextView) findViewById(R.id.tvMoreTitle);
		tvMoreTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				More.this.finish();
			}
		});

		imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				More.this.finish();
			}
		});

		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				quickAction.show(v);

			}
		});

		tvStarProperty = (TextView) findViewById(R.id.tvStar);
		tvVegRestaurants = (TextView) findViewById(R.id.tvVegRestaurants);
		tvGooutBuffet = (TextView) findViewById(R.id.tvGoOut);
		tvRestaurantServingBuffet = (TextView) findViewById(R.id.tvBuffet);
		tvSunday = (TextView) findViewById(R.id.tvSunday);
		tvArticles = (TextView) findViewById(R.id.tvArticles);
		tvEvents = (TextView) findViewById(R.id.tvEvents);
		
		tvEvents.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Appconstants.strMenuflag = "events";
				userLocation = Appconstants.ltDo.name;

				showLoader();
				new Thread(new Runnable() {
					@Override
					public void run() {

						/*final String result = new WebServiceCalls()
								.urlPost(Appconstants.MAIN_HOST
										+ "restaurantEvents");*/
						
						final String result = new WebServiceCalls()
						.urlPost(Appconstants.MAIN_HOST
								+ "restaurantslisting/16/" + Appconstants.strCityId +"/"
								+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE);

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								hideLoader();

								if (result != null) {
									Intent inDeals = new Intent(getApplicationContext(),
											Events.class)
											.putExtra("result", result);
									startActivity(inDeals);
									// hideLoader();
								}

							}
						});

					}
				}).start();	
				
				
				
				
				
				
				
			}
		});
		
		
		

		tvStarProperty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "Star";
				Localsecrets.str_titles = "Star properties";
				try {
					Explore.type=19;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/19/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "morelinks/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/1";
*/
				} catch (NullPointerException e) {
					Explore.type=19;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/19/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "morelinks/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/1";*/

				}
				if (ConnectivityReceiver.checkInternetConnection(More.this)) {

				new RestaurantsList(v.getContext(), Launcher.getRestaurent)
						.execute();
			} else {
				ConnectivityReceiver.showCustomDialog(More.this);
			}
			}
		});

		tvVegRestaurants.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "Explore";
				Localsecrets.str_titles = "Vegetarian restaurants";
				try {
					Explore.type=11;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/11/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "morelinks/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/2";*/

				} catch (NullPointerException e) {
					Explore.type=11;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/11/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "morelinks/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/2";
*/
				}

			
				if (ConnectivityReceiver.checkInternetConnection(More.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
					.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(More.this);
				}
			}
		});

		tvGooutBuffet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "HappyHrs";
				Localsecrets.str_titles = "Drinks served";
				try {
					Explore.type=12;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/12/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
				/*	Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "morelinks/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/3";*/

				} catch (NullPointerException e) {

					Explore.type=12;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/12/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "morelinks/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/3";*/
				}

				
				if (ConnectivityReceiver.checkInternetConnection(More.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
					.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(More.this);
				}
			}
		});

		tvRestaurantServingBuffet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "BuffetPlace";
				Localsecrets.str_titles = "Buffet restaurants";
				try {
					Explore.type=13;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/13/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
				/*	Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "morelinks/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/4";*/

				} catch (NullPointerException e) {
					Explore.type=13;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/13/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "morelinks/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/4";*/
				}

			
				if (ConnectivityReceiver.checkInternetConnection(More.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
					.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(More.this);
				}
			}
		});

		tvSunday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "Sunday";
				Localsecrets.str_titles = "Sunday brunches";
				try {
					Explore.type=14;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/14/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "morelinks/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/5";*/

				} catch (NullPointerException e) {
					Explore.type=14;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/14/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "morelinks/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/5";*/
				}
				if (ConnectivityReceiver.checkInternetConnection(More.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
					.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(More.this);
				}
			}
		});

		tvArticles.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(getApplicationContext(),
						Articles.class));
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		quickAction = new QuickAction(this, QuickAction.VERTICAL);
		if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
			quickAction.addActionItem(new ActionItem(Explore.ID_LOGIN, "Login",
					getResources().getDrawable(R.drawable.iocn_myaccount)));
		} else {
			quickAction.addActionItem(new ActionItem(Explore.ID_MYACCOUNT,
					"My Account", getResources().getDrawable(
							R.drawable.my_account)));
			quickAction.addActionItem(new ActionItem(Explore.ID_RES,
					" My Reservations", getResources().getDrawable(
							R.drawable.my_reservations)));
			quickAction.addActionItem(new ActionItem(Explore.ID_ORDERS,
					" My Orders", getResources().getDrawable(
							R.drawable.my_orders)));
			quickAction.addActionItem(new ActionItem(Explore.ID_DEALS,
					" My Deals", getResources()
							.getDrawable(R.drawable.my_deals)));
			quickAction.addActionItem(new ActionItem(Explore.ID_REV,
					"My Reviews", getResources().getDrawable(
							R.drawable.my_reviews)));
			quickAction.addActionItem(new ActionItem(Explore.ID_REDEMPTION,
					" Redemption History", getResources().getDrawable(
							R.drawable.redemption_history)));
			quickAction.addActionItem(new ActionItem(Explore.ID_FAV,
					" My Favourites", getResources().getDrawable(
							R.drawable.favs)));
			quickAction
					.addActionItem(new ActionItem(Explore.ID_LOGOUT, "Logout",
							getResources().getDrawable(R.drawable.my_logout)));
		}
		// Set listener for action item clicked
		quickAction
				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {

						quickAction.dismiss();
						if (actionId == Explore.ID_LOGOUT) {
							PreferenceUtils.removeUserName();
							startActivity(new Intent(More.this, Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							if (ConnectivityReceiver.checkInternetConnection(More.this)) {
							new AccountDetails(More.this).execute();

							} else {
								ConnectivityReceiver.showCustomDialog(More.this);
							}
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(More.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(More.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(More.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(More.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(More.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(More.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(More.this, Login.class));
						}

					}
				});

		// actions
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {

			}
		});
	}
}
