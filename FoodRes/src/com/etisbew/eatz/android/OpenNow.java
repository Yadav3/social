package com.etisbew.eatz.android;

import java.util.Calendar;

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

public class OpenNow extends BaseActivity {

	TextView tvRestaurant, tvQuickBites, tvHappyHrs, tvLunchBuffet,
			tvPubAndBar, tvDeliveryNow, tvDinnerBuffet, tvOpenNowTitle;
	ImageView imgBack;
	String strOpenId;
	private QuickAction quickAction;
	ImageView options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.opennowmenu);

		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				quickAction.show(v);

			}
		});

		tvOpenNowTitle = (TextView) findViewById(R.id.tvOpenNowTitle);
		tvOpenNowTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OpenNow.this.finish();
			}
		});

		imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OpenNow.this.finish();
			}
		});

		tvRestaurant = (TextView) findViewById(R.id.tvRestaurant);
		tvHappyHrs = (TextView) findViewById(R.id.tvHappyHrs);
		tvLunchBuffet = (TextView) findViewById(R.id.tvLunchBuffet);
		tvDinnerBuffet = (TextView) findViewById(R.id.tvDinnerBuffet);
		tvPubAndBar = (TextView) findViewById(R.id.tvPubAndBar);
		tvDeliveryNow = (TextView) findViewById(R.id.tvDeliveryNow);

		Calendar c = Calendar.getInstance();

		int Hr24 = c.get(Calendar.HOUR_OF_DAY);
		if (Hr24 > 12 && Hr24 < 15) {
			tvLunchBuffet.setVisibility(View.VISIBLE);
		} else {
			tvLunchBuffet.setVisibility(View.GONE);
		}
		if (Hr24 > 12 && Hr24 < 23) {
			tvHappyHrs.setVisibility(View.VISIBLE);
			tvPubAndBar.setVisibility(View.VISIBLE);
		} else {
			tvHappyHrs.setVisibility(View.GONE);
			tvPubAndBar.setVisibility(View.GONE);
		}
		if (Hr24 > 19 && Hr24 < 23) {
			tvDinnerBuffet.setVisibility(View.VISIBLE);
		} else {
			tvDinnerBuffet.setVisibility(View.GONE);
		}

		tvRestaurant.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "Explore";
				Localsecrets.str_titles = "Restaurants open";
				try {

					Explore.type=5;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/5/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
				} catch (Exception e) {
					// TODO: handle exception
				
					Explore.type=5;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/5/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
				}
				
				if (ConnectivityReceiver.checkInternetConnection(OpenNow.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
					.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(OpenNow.this);
				}
			}
		});

		tvHappyHrs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "HappyHrs";
				Localsecrets.str_titles = "Happy hours";
				try {
				/*	Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "opennow/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/2/1";*/
					Explore.type=6;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/6/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
				} catch (Exception e) {
					// TODO: handle exception
					Explore.type=6;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/6/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "opennow/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/2/1";*/
				}

				if (ConnectivityReceiver.checkInternetConnection(OpenNow.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
					.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(OpenNow.this);
				}
			}
		});

		tvLunchBuffet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "Explore";
				Localsecrets.str_titles = "Lunch buffet";
				try {
					Explore.type=7;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/7/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "opennow/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/3/1";*/
				} catch (Exception e) {
					// TODO: handle exception
					Explore.type=7;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/7/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "opennow/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/3/1";*/
				}

				if (ConnectivityReceiver.checkInternetConnection(OpenNow.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
					.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(OpenNow.this);
				}
			}
		});

		tvDinnerBuffet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "Explore";
				Localsecrets.str_titles = "Dinner buffet";
				try {
					Explore.type=8;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/8/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
				/*	Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "opennow/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/4/1";*/
				} catch (Exception e) {
					// TODO: handle exception
					Explore.type=8;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/8/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "opennow/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/4/1";*/
				}

				if (ConnectivityReceiver.checkInternetConnection(OpenNow.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
					.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(OpenNow.this);
				}
			}
		});

		tvPubAndBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "PubBar";
				Localsecrets.str_titles = "Happy hours";
				try {
					Explore.type=9;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/9/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "opennow/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/5/1";*/
				} catch (Exception e) {
					// TODO: handle exception
					Explore.type=9;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/9/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "opennow/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/5/1";*/
				}

				if (ConnectivityReceiver.checkInternetConnection(OpenNow.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
					.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(OpenNow.this);
				}
			}
		});

		tvDeliveryNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "DeliveryNow";
				Localsecrets.str_titles = "Restaurants that deliver";
				try {
					Explore.type=10;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/10/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "opennow/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/6/1";*/
				} catch (Exception e) {
					// TODO: handle exception
					Explore.type=10;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/10/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";
					/*Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "opennow/" + Appconstants.LATTITUDE + "/"
							+ Appconstants.LANGITUDE + "/"
							+ Appconstants.strCityId + "/6/1";*/
				}

				
				if (ConnectivityReceiver.checkInternetConnection(OpenNow.this)) {

					new RestaurantsList(v.getContext(), Launcher.getRestaurent)
					.execute();

				} else {
					ConnectivityReceiver.showCustomDialog(OpenNow.this);
				}
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
							startActivity(new Intent(OpenNow.this, Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							
							
							if (ConnectivityReceiver.checkInternetConnection(OpenNow.this)) {

								new AccountDetails(OpenNow.this).execute();

							} else {
								ConnectivityReceiver.showCustomDialog(OpenNow.this);
							}
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(OpenNow.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(OpenNow.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(OpenNow.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(OpenNow.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(OpenNow.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(OpenNow.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(OpenNow.this, Login.class));
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
