package com.etisbew.eatz.android.dropdownlist;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eatz.android.BaseActivity;
import com.etisbew.eatz.android.Explore;
import com.etisbew.eatz.android.Explore.AccountDetails;
import com.etisbew.eatz.android.Launcher;
import com.etisbew.eatz.android.Localsecrets;
import com.etisbew.eatz.android.Login;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.android.SearchDetails;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.RestaurentDO;
import com.etisbew.eatz.objects.reviewDO;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.utils.Utils;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

public class MyFav extends BaseActivity {

	private QuickAction quickAction;
	private TextView txtUser, txtPointsEarned, txtPointPending;// txtlocation
	private ListView lvMyFavList;
	private SearchAdapter adapter = null;
	ArrayList<String> id_list = new ArrayList<String>();
	RelativeLayout no_favorites;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_fav);

		ImageView ivBack = (ImageView) findViewById(R.id.back);
		
		no_favorites = (RelativeLayout) findViewById(R.id.no_favorites);
		
		
		
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setResult(10116);
//				finish();
				
					startActivity(new Intent(getApplicationContext(),	Launcher.class));
			
			}
		});

		ImageView options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				quickAction.show(v);
			}
		});

		txtUser = (TextView) findViewById(R.id.txtUser);
		txtUser.setText(Html.fromHtml(PreferenceUtils.getUserName1()));
		// txtlocation = (TextView) findViewById(R.id.txtlocation);
		txtPointsEarned = (TextView) findViewById(R.id.txtPointsEarned);
		txtPointsEarned.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),
						MyPoints.class));
			}
		});

		try {
			Login.strPointsEarned = Launcher.accountDetailsArray
					.getJSONObject(0).getString("points_earned").toString();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		txtPointsEarned.setText(Html.fromHtml("<font color=\"#3d3d3d\">"
				+ "Points earned : " + "</font>" + "<font color=\"#d42a2b\">"
				+ PreferenceUtils.getUserPoints() + "</font>"));

		/*
		 * try { Login.strPointsEarned = Launcher.accountDetailsArray
		 * .getJSONObject(0).getString("points_earned").toString(); } catch
		 * (JSONException e1) { e1.printStackTrace(); } catch
		 * (NullPointerException e) { e.printStackTrace(); }
		 */

		txtPointPending = (TextView) findViewById(R.id.txtPointPending);
		/*
		 * try { Login.strPointsPending = Launcher.accountDetailsArray
		 * .getJSONObject(0).getString("points_pending").toString(); } catch
		 * (JSONException e1) { e1.printStackTrace(); } catch
		 * (NullPointerException e) { e.printStackTrace(); }
		 */
		try {
			Login.strPointsPending = Launcher.accountDetailsArray
					.getJSONObject(0).getString("points_pending").toString();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		txtPointPending.setText(Html.fromHtml("<font color=\"#3d3d3d\">"
				+ "Points pending : " + "</font>" + "<font color=\"#d42a2b\">"
				+ PreferenceUtils.getUserPointsPending() + "</font>"));

		imgUser = (ImageView) findViewById(R.id.imgReviewPerson);
		Picasso.with(MyFav.this)
				.load(PreferenceUtils.getUserProfilePic().replace(" ", "%20"))
				.resize(100, 150).into(imgUser);

		lvMyFavList = (ListView) findViewById(R.id.listView1);
		adapter = new SearchAdapter(MyFav.this, null);
		lvMyFavList.setAdapter(adapter);
 
		lvMyFavList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
//				RestaurentDO objItem = new RestaurentDO();
				
				Localsecrets.det_fl = Appconstants.MAIN_HOST + "restaurantDetails/"
						+ id_list.get(position);
				if (ConnectivityReceiver.checkInternetConnection(MyFav.this)) {

					new DeatilsTasks().execute();
				} else {
					ConnectivityReceiver.showCustomDialog(MyFav.this);
				}
				
//				new DeatilsTasks().execute();
			}
		});

		if (ConnectivityReceiver.checkInternetConnection(MyFav.this)) {

			new DisplayRestaurents().execute();
		} else {
			ConnectivityReceiver.showCustomDialog(MyFav.this);
		}
		
//		new DisplayRestaurents().execute();
		
		

	}
	
	class DeatilsTasks extends AsyncTask<String, String, String> {

		ProgressDialog myProgressDialog;
		ArrayList<reviewDO> reviews = new ArrayList<reviewDO>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			myProgressDialog = new ProgressDialog(MyFav.this);
			myProgressDialog.setMessage("Loading...");
			myProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Localsecrets.det_fl);
		}

		@SuppressLint("InflateParams")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject jObject = new JSONObject(result);
				if (jObject.has("id")) {
					Intent in = new Intent(getApplicationContext(),
							SearchDetails.class);
					in.putExtra("result", result);
					startActivityForResult(in, 5);
					// OrderFood.this.finish();
				}

				myProgressDialog.dismiss();
			} catch (Exception e) {
				myProgressDialog.dismiss();
				Log.e("", "" + e);
			}

		}
	}

	ImageView imgLogo, imgUser;

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
							startActivity(new Intent(MyFav.this, Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							new AccountDetails(MyFav.this).execute();
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(MyFav.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(MyFav.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(MyFav.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(MyFav.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(MyFav.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							finish();
							startActivity(new Intent(MyFav.this, Login.class));
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 10116) {
			setResult(10116);
			finish();
		}
	}

	public class SearchAdapter extends BaseAdapter {

		private Context mContext;
		ArrayList<RestaurentDO> array;

		public SearchAdapter(Context context, ArrayList<RestaurentDO> array) {
			super();
			mContext = context;
			this.array = array;

		}

		@Override
		public int getCount() {
			// return the number of records in cursor
			if (array == null)
				return 0;
			return array.size();
		}

		// getView method is called for each item of ListView
		@Override
		@SuppressLint({ "InflateParams", "ViewHolder" })
		public View getView(final int position, View view, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.favorates_row, null);

			TextView tvItem = (TextView) view
					.findViewById(R.id.tvRestaurentTitle);
			TextView tvAddressLine = (TextView) view
					.findViewById(R.id.tvAddressLine1);
			TextView tvCusineList = (TextView) view
					.findViewById(R.id.tvCusinelsit);
			// TextView tvDistance = (TextView)
			// view.findViewById(R.id.tvDistance);
			TextView priceRate = (TextView) view.findViewById(R.id.priceRate);
			RatingBar rating = (RatingBar) view.findViewById(R.id.rating);
			TextView btnPhoneNumber = (TextView) view
					.findViewById(R.id.btnPhoneNumber);
			ImageView openOrClose = (ImageView) view
					.findViewById(R.id.openOrClose);
			ImageView ivHasDeals = (ImageView) view
					.findViewById(R.id.ivHasDeals);

			tvItem.setText(array.get(position).name);
			tvAddressLine.setText(array.get(position).addressLine1);
			tvCusineList.setText(array.get(position).cuisines_list);

			view.setTag(array.get(position));

			// if(array.get(position).currentdistance>1000)
			// {
			// tvDistance.setText(""+(array.get(position).currentdistance)/1000+"\nkm");
			// }else
			// tvDistance.setText(""+array.get(position).currentdistance+"\nm");

			// set the rating.
			int rate = Integer.parseInt(array.get(position).review_rating);
			rating.setRating(rate);

			if (rate == 0) {
				rating.setVisibility(View.GONE);
			}

			TextView btnRemove = (TextView) view.findViewById(R.id.btnRemove);
			btnRemove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// need to write
					showPromptDialog(array.get(position).favoriteid, position);

				}
			});

			btnPhoneNumber.setText(array.get(position).phone);
			btnPhoneNumber.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						Intent callIntent = new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:" + array.get(position).phone));
						startActivity(callIntent);
					} catch (ActivityNotFoundException e) {
					}
				}
			});

			LinearLayout llDirections = (LinearLayout) view
					.findViewById(R.id.llDirections);
			llDirections.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(
							android.content.Intent.ACTION_VIEW, Uri
									.parse("http://maps.google.com/maps?saddr="
											+ Appconstants.LATTITUDE + ","
											+ Appconstants.LANGITUDE
											+ "&daddr="
											+ array.get(position).lat + ","
											+ array.get(position).lang));
					startActivity(intent);
				}
			});

			// set the price.
			int price = 0;
			try {
				price = Integer.parseInt(array.get(position).price);
			} catch (Exception e) {
				price = 0;
			}
			String str = "";

			for (int i = 0; i < price; i++) {

				str = str + getResources().getString(R.string.oneRupee);
			}
			priceRate.setText(str);

			if (array.get(position).check_open.contains("OPEN")) {
				openOrClose.setVisibility(View.VISIBLE);
			} else {
				openOrClose.setImageResource(R.drawable.close);
				openOrClose.setVisibility(View.VISIBLE);
			}

			if (array.get(position).has_deals) {
				ivHasDeals.setVisibility(View.VISIBLE);
			} else
				ivHasDeals.setVisibility(View.GONE);

			return view;

		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void refresh(ArrayList<RestaurentDO> array) {

			this.array = array;
			this.notifyDataSetChanged();
		}
	}

	private void showPromptDialog(final String favId, final int position) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyFav.this);
		alertDialog.setTitle("Confirm Delete...");
		alertDialog.setMessage("Are you sure you want to remove this venue from favourites?");
		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if (ConnectivityReceiver.checkInternetConnection(MyFav.this)) {

							new RemoveFromFavs(favId, position).execute();

						} else {
							ConnectivityReceiver.showCustomDialog(MyFav.this);
						}

//						new RemoveFromFavs(favId, position).execute();
					}
				});

		alertDialog.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
					}
				});

		alertDialog.show();
	}

	private ArrayList<RestaurentDO> restrent = null;

	String pointsEarned = "0", pendingPoints = "0";

	private class DisplayRestaurents extends
			AsyncTask<String, ArrayList<RestaurentDO>, ArrayList<RestaurentDO>> {
		JSONObject mainJson;
		public DisplayRestaurents() {

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoader();
		}

		@Override
		protected ArrayList<RestaurentDO> doInBackground(String... params) {

			restrent = new ArrayList<RestaurentDO>();

			String result = Utils.getJSONStringGET(Appconstants.url2
					+ "myFavorites/" + PreferenceUtils.getUserSession());

			if (result == null) {
				return null;
			} else {
				try {
					mainJson = new JSONObject(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return restrent;

		}

		@Override
		protected void onPostExecute(ArrayList<RestaurentDO> result) {
			super.onPostExecute(result); 
			
			try {
				id_list.clear();
				if (result.contains("errorMessage")) {
					String strErrorMsg = mainJson.optString("errorMessage");
					showDialogMsg(MyFav.this, strErrorMsg);
				} else {
					JSONArray favs = mainJson.getJSONArray("favorites");
					for (int i = 0; i < favs.length(); i++) {
						JSONObject jos = favs.getJSONObject(i);
						RestaurentDO objItem = new RestaurentDO();
						if (!jos.isNull("id")) {
							id_list.add(jos.getString("id"));
							objItem.id = jos.getString("id");
						}
						if (!jos.isNull("favoriteid")) {
							objItem.favoriteid = jos
									.getString("favoriteid");
						}
						if (!jos.isNull("Address")) {
							objItem.addressLine1 = jos.getString("Address");
						}
						if (!jos.isNull("RestaurantName")) {
							objItem.name = jos.getString("RestaurantName");
						}

						if (!jos.isNull("Phone")) {
							objItem.phone = jos.getString("Phone");
						}

						if (!jos.isNull("cuisines_list")) {
							objItem.cuisines_list = jos
									.getString("cuisines_list");
						}

						if (!jos.isNull("has_deals")) {
							if (jos.getString("has_deals")
									.equalsIgnoreCase("1"))
								objItem.has_deals = true;
						}

						if (!jos.isNull("overall_rating")) {
							objItem.review_rating = jos
									.getString("overall_rating");
						}
						if (!jos.isNull("currentdistance")) {

							// String distence = "0";
							float distencef = Float.parseFloat(jos
									.getString("currentdistance"));
							int k = (int) (distencef * 1000);

							objItem.currentdistance = k;
						}

						if (!jos.isNull("costpertwo")) {
							objItem.price = jos.getString("costpertwo");
						}

						objItem.lat = jos.getString("Latitude");
						objItem.lang = jos.getString("Longitude");

						if (!jos.isNull("check_open")) {
							objItem.check_open = jos
									.getString("check_open");
						}

						restrent.add(objItem);
					}

				}

				if (!mainJson.isNull("points_earned")) {
					PreferenceUtils.setUserPoints(mainJson.getString("points_earned"));
					pointsEarned = mainJson.getString("points_earned");
				}

				if (!mainJson.isNull("points_pending")) {
					PreferenceUtils.setUserPointsPending(mainJson.getString("points_pending"));
					pendingPoints = mainJson.getString("points_pending");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			hideLoader();

			adapter.refresh(result);
			Update();

		/*	txtPointsEarned.setText(Html.fromHtml("<font color=\"#3d3d3d\">"
					+ "Points earned : " + "</font>"
					+ "<font color=\"#d42a2b\">" + pointsEarned + "</font>"));

			txtPointPending.setText(Html.fromHtml("<font color=\"#3d3d3d\">"
					+ "Points pending : " + "</font>"
					+ "<font color=\"#d42a2b\">" + pendingPoints + "</font>"));*/

		}

	}
	
	public void Update(){
		if(restrent.size()>0){
			lvMyFavList.setVisibility(View.VISIBLE);
			no_favorites.setVisibility(View.GONE);
		}else{
			lvMyFavList.setVisibility(View.GONE);
			no_favorites.setVisibility(View.VISIBLE);
		}
	}

	private class RemoveFromFavs extends AsyncTask<String, String, String> {

		String favId;
		int position;

		public RemoveFromFavs(String favId, int position) {
			this.favId = favId;
			this.position = position; 
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoader();
		}

		@Override
		protected String doInBackground(String... params) {

			String url = Appconstants.url2 + "removeFavorite/"
					+ PreferenceUtils.getUserSession() + "/" + favId;

			return Utils.getJSONStringGET(url);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			hideLoader();
			if (result.contains("Successfully")) {
				restrent.remove(position);
				Update();
				adapter.refresh(restrent);
			}

		}

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(),	Launcher.class));
	}

}
