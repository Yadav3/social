package com.etisbew.eatz.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
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
import com.etisbew.eatz.objects.DealsDO;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

@SuppressLint("ViewHolder")
public class Deals extends BaseActivity {

	private ListView lvResults = null;
	private SearchAdapter adapter = null;
	private ArrayList<DealsDO> allDeals = null;
	private ImageView ivBack = null, options = null;
	private String result;
	private TextView etFind;
	public static JSONObject jobjDeals;
	public static String lik = "", hig = "", tnc = "", pay = "";
	ArrayList<String> links = new ArrayList<String>();
	ArrayList<String> highl = new ArrayList<String>();
	ArrayList<String> pay_now = new ArrayList<String>();
	ArrayList<String> trc = new ArrayList<String>();
	private QuickAction quickAction;
	static String strDealId;
	RelativeLayout no_deals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.deals);

		hideKeyboard();

		result = getIntent().getStringExtra("result");
		no_deals = (RelativeLayout) findViewById(R.id.no_deals);

		lvResults = (ListView) findViewById(R.id.listView1);
		etFind = (TextView) findViewById(R.id.etFind);
		if (ConnectivityReceiver.checkInternetConnection(Deals.this)) {
			new DisplayDeals(result).execute();
		} else {
			ConnectivityReceiver.showCustomDialog(Deals.this);
		}
		ivBack = (ImageView) findViewById(R.id.back);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});

		etFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(v.getContext(), Search.class));
			}
		});

		lvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View v,
					int position, long id) {
				lik = links.get(position);
				hig = highl.get(position);
				tnc = trc.get(position);
				pay = pay_now.get(position);
				// pay = "11";
				String redirectUrl = Appconstants.url2 + "viewDealdetails/";
				strDealId = ((DealsDO) v.getTag()).dealid;
				System.out.println("url is" + redirectUrl
						+ ((DealsDO) v.getTag()).dealid);
				if (ConnectivityReceiver.checkInternetConnection(Deals.this)) {
					new ConnectToWEB(redirectUrl, ((DealsDO) v.getTag()).dealid)
							.execute();
				} else {
					ConnectivityReceiver.showCustomDialog(Deals.this);
				}

			}
		});

		llSort = (LinearLayout) findViewById(R.id.llSort);
		llSort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent in = new Intent(v.getContext(), Sort.class);
				startActivityForResult(in, 0);

			}
		});

		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				quickAction.show(v);
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
							startActivity(new Intent(Deals.this, Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							if (ConnectivityReceiver
									.checkInternetConnection(Deals.this)) {
								new AccountDetails(Deals.this).execute();
							} else {
								ConnectivityReceiver
										.showCustomDialog(Deals.this);
							}
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(Deals.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(Deals.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(Deals.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(Deals.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(Deals.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(Deals.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(Deals.this, Login.class));
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

		if (requestCode == 0 && resultCode == 1011) {
			Collections.sort(allDeals, new CustomComparatorAlpabet());
			adapter.refresh(allDeals);
		}

		if (requestCode == 1 && resultCode == 10116) {
			finish();
		}
	}

	public class CustomComparatorAlpabet implements Comparator<DealsDO> {
		@Override
		public int compare(DealsDO o1, DealsDO o2) {
			return o1.venuename.compareTo(o2.venuename);
		}
	}

	private LinearLayout llSort = null;

	public class SearchAdapter extends BaseAdapter {

		private Context mContext;
		ArrayList<DealsDO> array;

		public SearchAdapter(Context context, ArrayList<DealsDO> array) {
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
		@SuppressLint({ "InflateParams", "NewApi" })
		public View getView(int position, View view, ViewGroup parent) {
			// inflate the layout for each item of listView
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.deals_row, null);
			view.setTag(array.get(position));

			TextView tvItem = (TextView) view.findViewById(R.id.tvItemName);
			// TextView tvprice = (TextView) view.findViewById(R.id.tvprice);
			/*
			 * TextView tvLocation = (TextView)
			 * view.findViewById(R.id.tvLocation);
			 */
			TextView tvLsPrice = (TextView) view.findViewById(R.id.tvLsPrice);
			ImageView ivItem = (ImageView) view.findViewById(R.id.ivItemPhoto);
			TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

			TextView lablePN = (TextView) view.findViewById(R.id.lablePN);
			TextView lableLS = (TextView) view.findViewById(R.id.lableLS);

			TextView tvPayNow = (TextView) view.findViewById(R.id.tvPayNow);

			tvItem.setText(array.get(position).venuename);
			/* tvLocation.setText(array.get(position).location); */
			tvTitle.setText(array.get(position).title);

			if (!array.get(position).product_price.equalsIgnoreCase("")) {
				tvLsPrice.setText(getResources().getString(R.string.oneRupee)
						+ array.get(position).product_price);
			} else {
				tvLsPrice.setVisibility(View.GONE);
				lableLS.setVisibility(View.GONE);
			}

			if (!array.get(position).ls_price.equalsIgnoreCase("")) {
				tvPayNow.setText(getResources().getString(R.string.oneRupee)
						+ array.get(position).ls_price);
			} else {
				tvPayNow.setVisibility(View.GONE);
				lablePN.setVisibility(View.GONE);
			}
			/*
			 * if (!array.get(position).paynow.equalsIgnoreCase("0")) {
			 * tvprice.setText(getResources().getString(R.string.oneRupee) +
			 * array.get(position).paynow); } else {
			 * tvprice.setVisibility(View.GONE);
			 * 
			 * }
			 */

			if (array.get(position).product_price.equalsIgnoreCase("")
					&& array.get(position).ls_price.equalsIgnoreCase("")) {
				tvLsPrice.setText("Free");// \nvoucher
				tvLsPrice.setVisibility(View.VISIBLE);
				lableLS.setVisibility(View.GONE);
				tvPayNow.setVisibility(View.GONE);
				lablePN.setVisibility(View.GONE);

			}

			if (pay_now.get(position).length() > 0) {
				// ||pay_now.get(position).equalsIgnoreCase("0")

				if (array.get(position).ls_price.length() > 0) {
					lableLS.setVisibility(View.VISIBLE);
					lableLS.setText("LS Price");
					tvLsPrice.setText("" + array.get(position).ls_price);// \nvoucher
					tvLsPrice.setVisibility(View.VISIBLE);
				} else {
					tvLsPrice.setVisibility(View.GONE);
					lableLS.setVisibility(View.GONE);
				}
				if (pay_now.get(position).equalsIgnoreCase("0")) {
					// tvPayNow.setVisibility(View.VISIBLE);
					// tvPayNow.setText(""+pay_now.get(position));
					// lablePN.setVisibility(View.VISIBLE);

					tvLsPrice.setText("Free");// \nvoucher
					tvLsPrice.setVisibility(View.VISIBLE);
					lableLS.setVisibility(View.GONE);
					tvPayNow.setVisibility(View.GONE);
					lablePN.setVisibility(View.GONE);
				} else {
					tvPayNow.setVisibility(View.VISIBLE);
					tvPayNow.setText("" + pay_now.get(position));
					lablePN.setVisibility(View.VISIBLE);
				}

			} else {
				tvLsPrice.setText("Free");
				tvLsPrice.setVisibility(View.VISIBLE);
				lableLS.setVisibility(View.GONE);
				tvPayNow.setVisibility(View.GONE);
				lablePN.setVisibility(View.GONE);
			}

			Picasso.with(Deals.this)
					.load((array.get(position).deal_image).replace(" ", "%20"))
					.placeholder(R.drawable.loadingimg) //
					.error(R.drawable.no_image_available1).fit().into(ivItem);

			/*
			 * ivItem.setImageAlpha(R.drawable.text_bg); ivItem.setAlpha(225);
			 */

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

		public void refresh(ArrayList<DealsDO> array) {
			this.array = array;
			this.notifyDataSetChanged();
		}
	}

	private class DisplayDeals extends
			AsyncTask<String, ArrayList<DealsDO>, ArrayList<DealsDO>> {
		String result;

		public DisplayDeals(String result) {
			this.result = result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			showLoader();

		}

		@Override
		protected ArrayList<DealsDO> doInBackground(String... params) {

			allDeals = new ArrayList<DealsDO>();

			try {
				JSONObject totalObject = new JSONObject(result);
				JSONArray jsonArrayRestaurants = totalObject
						.getJSONArray("Deals");
				links.clear();
				highl.clear();
				trc.clear();
				pay_now.clear();
				for (int i = 0; i < jsonArrayRestaurants.length(); i++) {

					JSONObject objJson = jsonArrayRestaurants.getJSONObject(i);

					DealsDO objItem = new DealsDO();

					if (!objJson.isNull("dealid")) {
						objItem.dealid = objJson.getString("dealid");
					}
					if (!objJson.isNull("location")) {
						objItem.location = objJson.getString("location");
					}
					if (!objJson.isNull("title")) {
						objItem.title = objJson.getString("title");
					}
					if (!objJson.isNull("deal_image")) {
						objItem.deal_image = objJson.getString("deal_image");
					}
					if (!objJson.isNull("ls_price")) {
						objItem.ls_price = objJson.getString("ls_price");
					}
					if (!objJson.isNull("paynow")) {
						objItem.itemPayNow = objJson.getString("paynow");
						pay_now.add(objJson.getString("paynow"));
					}

					if (!objJson.isNull("venuename")) {
						objItem.venuename = objJson.getString("venuename");
					}
					if (!objJson.isNull("address")) {
						objItem.address = objJson.getString("address");
					}
					if (!objJson.isNull("Phone")) {
						objItem.phone = objJson.getString("Phone");
					}

					if (!objJson.isNull("highlights")) {
						objItem.highlights = objJson.getString("highlights");
						highl.add(objJson.getString("highlights"));
					}

					if (!objJson.isNull("termsconditions")) {
						objItem.terms = objJson.getString("termsconditions");
						trc.add(objJson.getString("termsconditions"));
					}
					if (!objJson.isNull("product_price")) {
						objItem.product_price = objJson
								.getString("product_price");
					}
					if (!objJson.isNull("link")) {
						objItem.link = objJson.getString("link");

					}
					if (!objJson.isNull("paynow")) {
						objItem.paynow = objJson.getString("paynow");

					}

					if (!objJson.isNull("dealmenu")) {
						objItem.deal_menu = objJson.getString("dealmenu");

						links.add(objJson.getString("dealmenu"));
					}
					allDeals.add(objItem);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return allDeals;
		}

		@Override
		protected void onPostExecute(ArrayList<DealsDO> allDeals) {
			super.onPostExecute(allDeals);

			adapter = new SearchAdapter(Deals.this, allDeals);
			lvResults.setAdapter(adapter);
			if (allDeals.size() > 0) {
				lvResults.setVisibility(View.VISIBLE);
				no_deals.setVisibility(View.GONE);
			} else {
				lvResults.setVisibility(View.GONE);
				no_deals.setVisibility(View.VISIBLE);
			}
			hideLoader();

		}

	}

	private class ConnectToWEB extends AsyncTask<String, String, String> {
		String result, link;

		public ConnectToWEB(String result, String link) {
			this.result = result;
			this.link = link;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			showLoader();

		}

		@Override
		protected String doInBackground(String... params) {

			return new WebServiceCalls().urlGet(result + link);

		}

		@Override
		protected void onPostExecute(String allDeals) {
			super.onPostExecute(allDeals);
			System.out.println("dealsmessage" + allDeals);

			try {
				jobjDeals = new JSONObject(allDeals);
				if (allDeals.contains("errorCode")) {
					showDialogMsg(Deals.this,
							jobjDeals.optString("errorMessage"));
				} else {
					startActivity(new Intent(getApplicationContext(),
							DealsDetails.class));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				hideLoader();
				e.printStackTrace();
			}
			hideLoader();

		}

	}

}
