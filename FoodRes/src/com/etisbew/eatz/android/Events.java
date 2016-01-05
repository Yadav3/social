package com.etisbew.eatz.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.TextView;

import com.etisbew.eatz.android.Explore.AccountDetails;
import com.etisbew.eatz.android.dropdownlist.MyDeals;
import com.etisbew.eatz.android.dropdownlist.MyFav;
import com.etisbew.eatz.android.dropdownlist.MyOrders;
import com.etisbew.eatz.android.dropdownlist.MyReservations;
import com.etisbew.eatz.android.dropdownlist.MyReviews;
import com.etisbew.eatz.android.dropdownlist.RedemptionHistory;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.EventsDO;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.squareup.picasso.Picasso;

@SuppressLint("ViewHolder")
public class Events extends BaseActivity {

	private ListView lvResults = null;
	private SearchAdapter adapter = null;
	private ArrayList<EventsDO> allEvents = null;
	private ImageView ivBack = null, options = null;
	private String result;
	private TextView etFind;
	public static JSONObject jobjDeals;

	private QuickAction quickAction;
	TextView title_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.deals);

		hideKeyboard();

		result = getIntent().getStringExtra("result");

		title_txt = (TextView) findViewById(R.id.title_txt);
		title_txt.setText("Events");
		lvResults = (ListView) findViewById(R.id.listView1);
		etFind = (TextView) findViewById(R.id.etFind);
		if (ConnectivityReceiver.checkInternetConnection(Events.this)) {
			new DisplayEvents(result).execute();
		} else {
			ConnectivityReceiver.showCustomDialog(Events.this);
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

				Intent in = new Intent(v.getContext(), EventDetails.class)
						.putExtra("item", ((EventsDO) v.getTag()));
				startActivityForResult(in, 5);

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
							startActivity(new Intent(Events.this, Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							if (ConnectivityReceiver.checkInternetConnection(Events.this)) {
							new AccountDetails(Events.this).execute();
							} else {
								ConnectivityReceiver.showCustomDialog(Events.this);
							}
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(Events.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(Events.this,
									MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(Events.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(Events.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(Events.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(Events.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(Events.this, Login.class));
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
			Collections.sort(allEvents, new CustomComparatorAlpabet());
			adapter.refresh(allEvents);
		}

		if (requestCode == 1 && resultCode == 10116) {
			finish();
		}
	}

	public class CustomComparatorAlpabet implements Comparator<EventsDO> {
		@Override
		public int compare(EventsDO o1, EventsDO o2) {
			return o1.title.compareTo(o2.title);
		}
	}

	private LinearLayout llSort = null;

	public class SearchAdapter extends BaseAdapter {

		private Context mContext;
		ArrayList<EventsDO> array;

		public SearchAdapter(Context context, ArrayList<EventsDO> allEvents) {
			super();
			mContext = context;
			this.array = allEvents;

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
		@SuppressLint("InflateParams")
		public View getView(int position, View view, ViewGroup parent) {
			// inflate the layout for each item of listView
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.events_row, null);
			view.setTag(array.get(position));

			TextView tvItem = (TextView) view.findViewById(R.id.tvItemName);
			tvItem.setText(array.get(position).title);
			ImageView ivItem = (ImageView) view.findViewById(R.id.ivItemPhoto);
			Picasso.with(Events.this).load(array.get(position).thumb_image)
					.into(ivItem);
			TextView txtDescription = (TextView) view
					.findViewById(R.id.txtDescription);
			txtDescription.setText(array.get(position).description);
			TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
			tvDate.setText(array.get(position).eventDate + " "
					+ array.get(position).eventTime);
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

		public void refresh(ArrayList<EventsDO> array) {
			this.array = array;
			this.notifyDataSetChanged();
		}
	}

	private class DisplayEvents extends
			AsyncTask<String, ArrayList<EventsDO>, ArrayList<EventsDO>> {
		String result;

		public DisplayEvents(String result) {
			this.result = result;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoader();
		}

		@Override
		protected ArrayList<EventsDO> doInBackground(String... params) {

			allEvents = new ArrayList<EventsDO>();
			try {

				JSONObject totalObject = new JSONObject(result);
				JSONArray jsonArrayRestaurants = totalObject
						.getJSONArray("Events");
				for (int i = 0; i < jsonArrayRestaurants.length(); i++) {

					JSONObject objJson = jsonArrayRestaurants.getJSONObject(i);

					EventsDO objItem = new EventsDO();

					if (!objJson.isNull("eventid")) {
						objItem.eventid = objJson.getString("eventid");
					}
					if (!objJson.isNull("title")) {
						objItem.title = objJson.getString("title");
					}
					if (!objJson.isNull("description")) {
						objItem.description = objJson.getString("description");
					}
					if (!objJson.isNull("thumb_image")) {
						objItem.thumb_image = objJson.getString("thumb_image");
					}
					if (!objJson.isNull("address")) {
						objItem.location = objJson.getString("address");
					}
					if (!objJson.isNull("big_image")) {
						objItem.largeImage = objJson.getString("big_image");
					}
					if (!objJson.isNull("event_link")) {
						objItem.eventsLink = objJson.getString("event_link");
					}
					if (!objJson.isNull("eventdate")) {
						objItem.eventDate = objJson.getString("eventdate");
					}
					if (!objJson.isNull("eventstart_date")) {
						objItem.eventStartDate = objJson
								.getString("eventstart_date");
					}
					if (!objJson.isNull("eventend_date")) {
						objItem.eventEndDate = objJson
								.getString("eventend_date");
					}
					if (!objJson.isNull("eventtime")) {
						objItem.eventTime = objJson.getString("eventtime");
					}
					if (!objJson.isNull("latitude")) {
						objItem.lat = objJson.getString("latitude");
					}
					if (!objJson.isNull("longitude")) {
						objItem.lang = objJson.getString("longitude");
					}

					allEvents.add(objItem);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return allEvents;
		}

		@Override
		protected void onPostExecute(ArrayList<EventsDO> allDeals) {
			super.onPostExecute(allEvents);

			adapter = new SearchAdapter(Events.this, allEvents);
			lvResults.setAdapter(adapter);

			if (result.contains("errorMessage")) {
				AlertDialog.Builder alert = new AlertDialog.Builder(Events.this);
				alert.setTitle("eatz");
				// Create TextView
				final TextView txtMsg = new TextView(Events.this);
				txtMsg.setTextColor(Color.WHITE);
				txtMsg.setPadding(10, 0, 0, 0);
				txtMsg.setTypeface(null, Typeface.BOLD);
				txtMsg.setText("No events found");
				alert.setView(txtMsg);

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
								Intent intent = new Intent(
										getApplicationContext(), Launcher.class);
								startActivity(intent);
								finish();
							}
						});

				alert.show();
			} else {

			}

			hideLoader();
		}
	}
}
