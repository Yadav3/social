package com.etisbew.eatz.android.dropdownlist;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.etisbew.eatz.android.BaseActivity;
import com.etisbew.eatz.android.Explore;
import com.etisbew.eatz.android.Explore.AccountDetails;
import com.etisbew.eatz.android.Launcher;
import com.etisbew.eatz.android.Login;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.android.RowAdapter;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.Item;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

public class MyReviews extends BaseActivity {

	Button btnAccount;
 
	List<Item> arrayOfList;
	ListView lvReviews1;
	RowAdapter objAdapter;
	Dialog dialog;
	static String strReviewId;
	JSONArray reviewsArray;
	TextView txtUser, txtlocation, txtPointsEarned, txtPointPending,
			txtReviewTitle, txtArrowLeft;
	ImageView imgLogo, imgUser;
	static JSONObject MyReviewJson;
	int posion = 0;

	private QuickAction quickAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myreviews);

		txtUser = (TextView) findViewById(R.id.txtUser);
		// txtUser.setTypeface(Localsecrets.Titillium_Bold);
		txtUser.setText(Html.fromHtml(PreferenceUtils.getUserName1()));

		ImageView imgLogo = (ImageView) findViewById(R.id.back);
		imgLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				setResult(1012);
				// finish();
//				MyReviews.this.finish();
				startActivity(new Intent(getApplicationContext(),	Launcher.class));	
				
			}
		});

		imgUser = (ImageView) findViewById(R.id.imgReviewPerson);
		Picasso.with(MyReviews.this)
				.load(PreferenceUtils.getUserProfilePic().replace(" ", "%20"))
				.resize(100, 100).into(imgUser);

		txtlocation = (TextView) findViewById(R.id.txtlocation);
		// txtlocation.setTypeface(Localsecrets.Titillium_Bold);
		/*
		 * if (!TextUtils.isEmpty(Login.strLocation)) { if (Login.strLocation !=
		 * null) { txtlocation.setText(Html.fromHtml(Login.strLocation)); }
		 * 
		 * }
		 */

		txtPointsEarned = (TextView) findViewById(R.id.txtPointsEarned);
		txtPointsEarned.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),
						MyPoints.class));
			}
		});
		// txtPointsEarned.setTypeface(Localsecrets.Titillium_Bold);

		txtPointPending = (TextView) findViewById(R.id.txtPointPending);
		// txtPointPending.setTypeface(Localsecrets.Titillium_Bold);

		txtReviewTitle = (TextView) findViewById(R.id.txtReviewTitle);
		// txtReviewTitle.setTypeface(Localsecrets.Titillium_Bold);

		lvReviews1 = (ListView) findViewById(R.id.lvReviews1);
		arrayOfList = new ArrayList<Item>();

		lvReviews1
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

						posion = arg2;

						try {
//							strReviewId = reviewsArray.getJSONObject(arg2)
//									.getString("id");

							Item item = arrayOfList.get(arg2);
							strReviewId = String.valueOf(item.getId());
							
							// MyReviews.this.finish();
							
							if (ConnectivityReceiver.checkInternetConnection(MyReviews.this)) {

								new ReviewDetailsTask().execute();

							} else {
								ConnectivityReceiver.showCustomDialog(MyReviews.this);
							}
							
							
							
//							new ReviewDetailsTask().execute();

						} catch (Exception e) {
							e.printStackTrace();
							// TODO: handle exception
						}
					}

				});

		ImageView options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});
		if (ConnectivityReceiver.checkInternetConnection(MyReviews.this)) {

			new MyTask().execute();

		} else {
			ConnectivityReceiver.showCustomDialog(MyReviews.this);
		}
		
//		new MyTask().execute();
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
							startActivity(new Intent(MyReviews.this,
									Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
if (ConnectivityReceiver.checkInternetConnection(MyReviews.this)) {

	new AccountDetails(MyReviews.this).execute();
} else {
	ConnectivityReceiver.showCustomDialog(MyReviews.this);
}
//							new AccountDetails(MyReviews.this).execute();
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(MyReviews.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(MyReviews.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(MyReviews.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(MyReviews.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(MyReviews.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(MyReviews.this,
									Login.class));
							finish();
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

	class MyTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyReviews.this);
			pDialog.setMessage("Loading your reviews. Please wait ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
 
		@Override
		protected String doInBackground(String... params) {
			/*
			 * return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST +
			 * "myReviews/" + PreferenceUtils.getUserSession());
			 */

			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "myReviews/" + PreferenceUtils.getUserSession());

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyReviews.this, "No data found from web!!!");

			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					if (result.contains("errorMessage")) {
						String strErrorMsg = mainJson.optString("errorMessage");
						showDialogMsg1(MyReviews.this, strErrorMsg);
					} else {
						reviewsArray = mainJson.getJSONArray("Reviews");

						for (int i = 0; i < reviewsArray.length(); i++) {
							JSONObject objJson = reviewsArray.getJSONObject(i);

							Item objItem = new Item();

							objItem.setRestaurantName(objJson
									.getString("venue_name"));
							objItem.setLocation(objJson.getString("location"));
							objItem.setRating(objJson.getString("rating"));
							objItem.setReviewDate(objJson
		 							.getString("datecreated"));
							objItem.setReviewDesc(objJson.getString("comment"));
							objItem.setId(objJson.getInt("id"));
							arrayOfList.add(objItem);

						}
					} 

					if (!mainJson.isNull("points_earned")) {
						
						PreferenceUtils.setUserPoints(mainJson.getString("points_earned"));
						txtPointsEarned.setText(Html
								.fromHtml("<font color=\"#3d3d3d\">"
										+ "Points earned : " + "</font>"
										+ "<font color=\"#d42a2b\">"
										+ mainJson.getString("points_earned")
										+ "</font>"));
					}

					if (!mainJson.isNull("points_pending")) {
						PreferenceUtils.setUserPointsPending(mainJson.getString("points_pending"));
						txtPointPending.setText(Html
								.fromHtml("<font color=\"#3d3d3d\">"
										+ "Points pending : " + "</font>"
										+ "<font color=\"#d42a2b\">"
										+ mainJson.getString("points_pending")
										+ "</font>"));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				setAdapterToListview();

			}

		}
	}

	public void showDialogMsg1(Context con, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(con);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void setAdapterToListview() {
		objAdapter = new RowAdapter(MyReviews.this, R.layout.reviews1,
				arrayOfList);
		lvReviews1.setAdapter(objAdapter);

	}

	class ReviewDetailsTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// pDialog = new ProgressDialog(Reviews.this);
			// pDialog.setMessage("Loading...");
			// pDialog.setCancelable(false);
			// pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "reviewDetails/" + strReviewId);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// if (null != pDialog && pDialog.isShowing()) {
			// pDialog.dismiss();
			// }

			if (null == result || result.length() == 0) {
				showDialogMsg(MyReviews.this, "No data found from web!!!");

			} else {

				if (result.contains("errorMessage")) {
					try {
						String strErrMsg = new JSONObject(result)
								.optString("errorMessage");
						showDialogMsg(MyReviews.this, strErrMsg);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {

					try {
						MyReviewJson = new JSONObject(result);
						startActivityForResult(
								new Intent(getApplicationContext(),
										MyReviewDetails.class), 10);
						overridePendingTransition(R.anim.pull_in_right,
								R.anim.push_out_left);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 10 && resultCode == 10121) {
			arrayOfList.remove(posion);
			objAdapter.notifyDataSetChanged();
		}

		if (requestCode == 1 && resultCode == 10116) {
			setResult(10116);
			finish();
		}

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(),	Launcher.class));
	}

}
