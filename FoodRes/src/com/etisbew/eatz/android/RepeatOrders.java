package com.etisbew.eatz.android;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
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

import com.etisbew.eatz.android.orderfood.OrderFood;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.RestaurentDO;
import com.etisbew.eatz.web.WebServiceCalls;

public class RepeatOrders extends BaseActivity implements OnClickListener {
	RelativeLayout back;
	private SearchAdapter adapter = null;
	ListView listView1;
	int repeat_order;
	String orderid;
	static JSONObject restJsonDetails;
	private ArrayList<RestaurentDO> restrent = null;
	public static ArrayList<String> vid = new ArrayList<String>();
	public static ArrayList<String> order_id = new ArrayList<String>();
	public static ArrayList<String> opncls = new ArrayList<String>();
	public static ArrayList<String> venueNames = new ArrayList<String>();
	public static ArrayList<Double> latitude = new ArrayList<Double>();
	public static ArrayList<Double> longitude = new ArrayList<Double>();
	public static ArrayList<String> phone = new ArrayList<String>();
	public static ArrayList<String> address = new ArrayList<String>();
	RestaurentDO objItem;
	static JSONObject restJsonDetails1;
	static JSONArray jsonArrayRestaurants1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.repeatorder);
		listView1 = (ListView) findViewById(R.id.listView1);
		back = (RelativeLayout) findViewById(R.id.back);
		back.setOnClickListener(this);

		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Explore.strVenueId = restrent.get(position).id;
				Explore.strRestaurantTitle = restrent.get(position).name;
				// new getSessionsSlotsTask(strVenueId).execute();
				// startActivity(new
				// Intent(getApplicationContext(),BookTable.class));

				Intent in = new Intent(getApplicationContext(), OrderFood.class);
				repeat_order = 2;
				orderid = order_id.get(position);
				in.putExtra("myorder", "2");
				in.putExtra("order_id", order_id.get(position));

				// startActivity(in);
				// new getOrderCategoriesTask.ex
				if (ConnectivityReceiver
						.checkInternetConnection(RepeatOrders.this)) {

					new getOrderCategoriesTask().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(RepeatOrders.this);
				}

			}
		});
		new DisplayRestaurents().execute();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			RepeatOrders.this.finish();
			// startActivity(new Intent(RepeatOrders.this,OrderList.class));
		}
	}

	public class DisplayRestaurents extends
			AsyncTask<String, ArrayList<RestaurentDO>, ArrayList<RestaurentDO>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoader();
		}

		@Override
		protected ArrayList<RestaurentDO> doInBackground(String... params) {

			restrent = new ArrayList<RestaurentDO>();

			try {

				if (OrderList.restJsonDetails.has("Restaurants")) {

					Launcher.jsonArrayRestaurants = OrderList.restJsonDetails
							.getJSONArray("Restaurants");
					vid.clear();
					opncls.clear();
					venueNames.clear();
					order_id.clear();
					for (int i = 0; i < Launcher.jsonArrayRestaurants.length(); i++) {

						JSONObject objJson = Launcher.jsonArrayRestaurants
								.getJSONObject(i);

						objItem = new RestaurentDO();

						if (!objJson.isNull("Venueid")) {
							objItem.id = objJson.getString("Venueid");
							vid.add(objJson.getString("Venueid"));
						}
						if (!objJson.isNull("VenueName")) {
							objItem.name = objJson.getString("VenueName");
							venueNames.add(objJson.getString("VenueName"));
						}
						if (!objJson.isNull("address1")
								&& !objJson.isNull("address2")) {
							objItem.addressLine1 = objJson
									.getString("address1")
									+ ","
									+ objJson.getString("address2");

							address.add(objJson.getString("address1") + ","
									+ objJson.getString("address2"));
						}
						if (!objJson.isNull("Location")) {
							objItem.location = objJson.getString("Location");
						}
						if (!objJson.isNull("cuisines_list")) {
							objItem.cuisines_list = objJson
									.getString("cuisines_list");
						}
						if (!objJson.isNull("latitude")) {
							objItem.latitude = objJson.getString("latitude");

							latitude.add(Double.parseDouble(objJson
									.getString("latitude")));
						}
						if (!objJson.isNull("longitude")) {
							objItem.longitude = objJson.getString("longitude");
							longitude.add(Double.parseDouble(objJson
									.getString("longitude")));
						}
						if (!objJson.isNull("keyword")) {
							objItem.keyword = objJson.getString("keyword");

						}

						if (!objJson.isNull("review_rating")) {
							objItem.review_rating = objJson
									.getString("review_rating");
						}
						if (!objJson.isNull("currentdistance")) {

							// String distence = "0";
							float distencef = Float.parseFloat(objJson
									.getString("currentdistance"));
							int k = (int) (distencef * 1000);

							objItem.currentdistance = k;
						}
						if (!objJson.isNull("phone")) {
							objItem.phone = objJson.getString("phone").replace(
									"- Ext: ", ",");
						}
						if (!objJson.isNull("hasdeals")) {
							objItem.has_deals = objJson.getBoolean("hasdeals");
						}
						if (!objJson.isNull("userorder")) {
							JSONObject objJson1 = objJson
									.getJSONObject("userorder");
							if (!objJson1.isNull("order_id")) {
								System.out.println("values are"
										+ objJson1.getString("order_id"));
								order_id.add(objJson1.getString("order_id"));
							}
							if (!objJson1.isNull("ordertype")) {
								objItem.ordertype = objJson1
										.getString("ordertype");
							}
							if (!objJson1.isNull("total_price")) {
								objItem.orderamount = objJson1
										.getString("total_price");
							}
							if (!objJson1.isNull("date_order")) {
								objItem.orderdate = objJson1
										.getString("date_order");
							}
						}
						// if (!objJson.isNull("booktable")) {
						// objItem.has_booktable =
						// objJson.getBoolean("booktable");
						// }
						// if (!objJson.isNull("orderfood")) {
						// objItem.has_orderfood =
						// objJson.getBoolean("orderfood");
						// }
						if (!objJson.isNull("averagemealpertwo")) {
							objItem.price = objJson
									.getString("averagemealpertwo");
						}
						if (!objJson.isNull("latitude")) {
							objItem.lat = objJson.getString("latitude");
						}
						if (!objJson.isNull("longitude")) {
							objItem.lang = objJson.getString("longitude");
						}

						if (!objJson.isNull("openstatus")) {
							objItem.check_open = objJson
									.getString("openstatus");
							opncls.add(objJson.getString("openstatus"));
						}

						if (!objJson.isNull("next_sunday_brunch")) {
							objItem.next_sunday_brunch = objJson
									.getString("next_sunday_brunch");
						}

						if (!objJson.isNull("brunch_timings")) {
							objItem.brunch_timings = objJson
									.getString("brunch_timings");
						}

						if (!objJson.isNull("happy_hours")) {
							objItem.happy_hours = objJson
									.getBoolean("happy_hours");
							if (objItem.happy_hours == true) {
								objItem.happy_hours_start = objJson
										.getString("happy_hours_start");
								objItem.happy_hours_end = objJson
										.getString("happy_hours_end");
							}
						}

						if (!objJson.isNull("has_features")) {
							objItem.has_features = objJson
									.getString("has_features");
						}

						if (!objJson.isNull("serving_items")) {
							objItem.serving_items = objJson
									.getString("serving_items");
						}

						if (!objJson.isNull("existing_buffets")) {
							String str = objJson.getString("existing_buffets");
							String values = "";
							if (str.length() > 0 && str.startsWith("[")) {
								try {
									JSONArray jarray = new JSONArray(str);
									for (int j = 0; j < jarray.length(); j++) {
										JSONObject jObject = jarray
												.getJSONObject(j);
										values = values
												+ jObject.getString("name")
												+ ":"
												+ jObject
														.getString("serving_timings")
												+ "("
												+ jObject.getString("opened")
												+ ")";

										if (j != jarray.length() - 1) {
											values = values + "\n";
										}

									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							objItem.existing_buffets = values;
						}

						restrent.add(objItem);

					}

					/*
					 * if (Launcher.jsonArrayRestaurants.length() == 1) {
					 * llSort.setVisibility(View.GONE);
					 * llFilter.setVisibility(View.GONE);
					 * LinearLayout.LayoutParams params1 = new
					 * LinearLayout.LayoutParams( LayoutParams.WRAP_CONTENT,
					 * LayoutParams.MATCH_PARENT); params1.weight = 3.0f;
					 * llMap.setLayoutParams(params1); }
					 */

					/*
					 * Launcher.features = restJsonDetails
					 * .getJSONArray("features"); Launcher.pricelist =
					 * restJsonDetails .getString("pricelist");
					 */

					/*
					 * if (Launcher.userLocation.equalsIgnoreCase("Hyderabad"))
					 * { if (Launcher.restJsonDetails.has("errMsg")) { errorMsg
					 * = Launcher.restJsonDetails .getString("errMsg"); }
					 * 
					 * }
					 */
				} else {
					/*
					 * llExplore.setVisibility(View.GONE);
					 * llNoResult.setVisibility(View.VISIBLE);
					 */
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}
			/*
			 * System.out.println("size" + restrent.size()); if (restrent.size()
			 * > 9) { flag = 1; } else { flag = 2; }
			 */
			return restrent;
		}

		@Override
		protected void onPostExecute(ArrayList<RestaurentDO> result) {
			super.onPostExecute(result);

			hideLoader();
			// dummyRestrent = (ArrayList<RestaurentDO>) result.clone();
			/*
			 * ViewGroup.LayoutParams listViewParams = (ViewGroup.LayoutParams)
			 * listView1 .getLayoutParams(); if(restrent.size()>1){
			 * listViewParams.height = 480; }else{ listViewParams.height = 240;
			 * }
			 */

			listView1.requestLayout();
			adapter = new SearchAdapter(RepeatOrders.this);
			listView1.setAdapter(adapter);

			/*
			 * if (!errorMsg.equalsIgnoreCase("")) {
			 * 
			 * rlError = (RelativeLayout) findViewById(R.id.rlErrorMsg);
			 * rlError.setVisibility(View.VISIBLE);
			 * 
			 * 
			 * TextView tvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
			 * tvErrorMsg.setText(errorMsg);
			 * 
			 * ImageView ivCross = (ImageView) findViewById(R.id.ivCross);
			 * ivCross.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * 
			 * // rlError.setVisibility(View.GONE);
			 * 
			 * } }); }
			 */
		}

	}

	public class SearchAdapter extends BaseAdapter {

		private Context mContext;
		ArrayList<RestaurentDO> array;

		public SearchAdapter(Context context) {
			super();
			mContext = context;
			this.array = restrent;

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
		public View getView(final int position, View view, ViewGroup parent) {
			// inflate the layout for each item of listView
			// if (view == null) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.repeat_custom, null);
			view.setTag(array.get(position));
			/*
			 * if (position == getCount() - 1) { if (flag == 1) { new
			 * getRestaurantsList1(Explore.this).execute(); id1 = id1 + 1; } }
			 */

			// } else {
			// view.setTag(array.get(position));
			// }

			TextView tvItem = (TextView) view
					.findViewById(R.id.tvRestaurentTitle);
			TextView tvAddressLine = (TextView) view
					.findViewById(R.id.tvAddressLine1);
			TextView tvCusineList = (TextView) view
					.findViewById(R.id.tvCusinelsit);
			TextView tvDistance = (TextView) view.findViewById(R.id.tvDistance);
			TextView priceRate = (TextView) view.findViewById(R.id.priceRate);
			TextView keyword = (TextView) view.findViewById(R.id.tvkeyword);

			LinearLayout llStars = (LinearLayout) view
					.findViewById(R.id.llStars);

			TextView btnPhoneNumber = (TextView) view
					.findViewById(R.id.btnPhoneNumber);
			RatingBar rating = (RatingBar) view.findViewById(R.id.rating);
			// RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
			LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
			stars.getDrawable(2).setColorFilter(Color.RED,
					PorterDuff.Mode.SRC_ATOP);

			ImageView ivHasDeals = (ImageView) view
					.findViewById(R.id.ivHasDeals);
			ImageView openOrClose = (ImageView) view
					.findViewById(R.id.openOrClose);
			TextView tvOthers = (TextView) view.findViewById(R.id.tvOthers);
			TextView tvBrunchTimings = (TextView) view
					.findViewById(R.id.tvBrunchTimings);
			TextView tvHas = (TextView) view.findViewById(R.id.tvHas);
			TextView tvService = (TextView) view.findViewById(R.id.tvService);
			TextView tvExistingBuffet = (TextView) view
					.findViewById(R.id.tvExistingBuffet);

			TextView tv_orderdate = (TextView) view
					.findViewById(R.id.tv_orderdate);
			TextView tv_orderamount = (TextView) view
					.findViewById(R.id.tv_orderamount);
			TextView tv_ordertype = (TextView) view
					.findViewById(R.id.tv_ordertype);

			LinearLayout llDirections = (LinearLayout) view
					.findViewById(R.id.llDirections);

			LinearLayout llPhone = (LinearLayout) view
					.findViewById(R.id.llPhone);

			TextView txtOrderFood = (TextView) view
					.findViewById(R.id.txtOrderFoodClick);
			txtOrderFood.setText("Repeat Order");
			txtOrderFood.setTag(view.getTag());
			txtOrderFood.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Explore.strVenueId = array.get(position).id;
					Explore.strRestaurantTitle = array.get(position).name;
					// new getSessionsSlotsTask(strVenueId).execute();
					// startActivity(new
					// Intent(getApplicationContext(),BookTable.class));
					/*
					 * Intent in = new Intent(getApplicationContext(),
					 * OrderFood.class); in.putExtra("myorder","2");
					 * in.putExtra("order_id",order_id.get(position));
					 * 
					 * startActivity(in);
					 */
					repeat_order = 2;
					orderid = order_id.get(position);
					if (ConnectivityReceiver
							.checkInternetConnection(RepeatOrders.this)) {

						new getOrderCategoriesTask().execute();

					} else {
						ConnectivityReceiver
								.showCustomDialog(RepeatOrders.this);
					}
				}
			});

			tvItem.setText(array.get(position).name);
			tvCusineList.setText(array.get(position).cuisines_list + "");

			if (array.get(position).cuisines_list.equalsIgnoreCase("")) {
				tvCusineList.setVisibility(View.GONE);
			} else {
				tvCusineList.setVisibility(View.VISIBLE);
			}

			tvAddressLine.setText(array.get(position).location);

			if (array.get(position).phone != null
					&& array.get(position).phone.length() > 2) {

				if (array.get(position).phone.contains(",")) {
					String s[] = array.get(position).phone.split(",");

					btnPhoneNumber.setText("Call    Ext No : " + s[1]);
				} else {
					btnPhoneNumber.setText("call");
				}

				llPhone.setVisibility(View.VISIBLE);
			} else {
				llPhone.setVisibility(View.GONE);
			}
			// System.out.println("position"+position+array.get(position)+array.get(position).name+array.get(position).cuisines_list+array.get(position).location);

			btnPhoneNumber.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						if (array.get(position).phone.contains(",")) {
							String s[] = array.get(position).phone.split(",");

							// Intent callIntent = new
							// Intent(Intent.ACTION_CALL,
							// Uri.parse("tel:" + s[0]+""
							// + PhoneNumberUtils.PAUSE + "#"
							// + s[1]));

							Intent callIntent = new Intent(Intent.ACTION_CALL);

							callIntent.setData(Uri.parse("tel:" + s[0] + ""
									+ PhoneNumberUtils.PAUSE + "#" + s[1]));
							startActivity(callIntent);
						} else {

							Intent callIntent = new Intent(Intent.ACTION_CALL,
									Uri.parse("tel:"
											+ array.get(position).phone));
							startActivity(callIntent);
						}

					} catch (ActivityNotFoundException e) {
					}
				}
			});

			if (array.get(position).lat != null) {
				llDirections.setVisibility(View.VISIBLE);
			} else {
				llDirections.setVisibility(View.GONE);
			}

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

			// if (array.get(position).currentdistance >= 1000) {

			DecimalFormat twoDForm = new DecimalFormat("#.##");

			tvDistance
					.setText(""
							+ twoDForm.format((array.get(position).currentdistance) / 1000)
							+ "\nkm");
			// }

			// else
			// tvDistance.setText("" + array.get(position).currentdistance
			// + "\nm");

			if (array.get(position).currentdistance == 0) {
				tvDistance.setText("Near\nby");
			}
			// set the price.
			int price = 0;
			try {
				price = Integer.parseInt(array.get(position).price);
			} catch (Exception e) {
				price = 0;
			}
			System.out.println("str" + array.get(position));
			String str = "";

			for (int i = 0; i < price; i++) {
				str = str + getResources().getString(R.string.oneRupee);
			}
			priceRate.setText(str);

			if (TextUtils.isEmpty(str)
					&& array.get(position).review_rating.equalsIgnoreCase("0")
					&& !array.get(position).has_deals) {
				llStars.setVisibility(View.GONE);
			}
			// set the rating.
			if (!array.get(position).review_rating.equalsIgnoreCase("0")) {
				int rate = Integer.parseInt(array.get(position).review_rating);
				llStars.setVisibility(View.VISIBLE);
				rating.setRating(rate);
			} else {
				rating.setVisibility(View.GONE);
			}

			// check has deals or not.
			if (array.get(position).has_deals) {
				llStars.setVisibility(View.VISIBLE);
				ivHasDeals.setVisibility(View.VISIBLE);
			} else {
				ivHasDeals.setVisibility(View.GONE);
			}
			// opncls
			/*
			 * if ((array.get(position).check_open).equalsIgnoreCase("0")) {
			 * openOrClose.setVisibility(View.VISIBLE);
			 * openOrClose.setImageResource(R.drawable.close);
			 * 
			 * } else { openOrClose.setImageResource(R.drawable.open);
			 * openOrClose.setVisibility(View.VISIBLE); }
			 */

			if (opncls.get(position).equalsIgnoreCase("0")) {
				openOrClose.setVisibility(View.VISIBLE);
				openOrClose.setImageResource(R.drawable.close);

			} else {
				openOrClose.setImageResource(R.drawable.open);
				openOrClose.setVisibility(View.VISIBLE);
			}

			if (!array.get(position).next_sunday_brunch.equalsIgnoreCase("")) {
				if (!Appconstants.strMenuflag.equalsIgnoreCase("HappyHrs")) {
					tvOthers.setVisibility(View.VISIBLE);
					tvOthers.setText("Next Sunday Brunch-"
							+ array.get(position).next_sunday_brunch);
				}

			} else {
				tvOthers.setVisibility(View.GONE);
			}

			if (!array.get(position).orderamount.equalsIgnoreCase("")) {
				tv_orderamount.setVisibility(View.VISIBLE);
				tv_orderamount.setText("Order Amount: "
						+ getString(R.string.oneRupee) + " "
						+ array.get(position).orderamount);
			} else {
				tv_orderamount.setVisibility(View.GONE);
			}
			if (!array.get(position).orderdate.equalsIgnoreCase("")) {
				tv_orderdate.setVisibility(View.VISIBLE);
				SimpleDateFormat format1 = new SimpleDateFormat(
						"yyyy-MM-DD HH:mm:ss");
				SimpleDateFormat format2 = new SimpleDateFormat(
						"dd/MM/yyyy HH:mm");
				Date date = null;
				try {
					try {
						date = format1.parse(array.get(position).orderdate);
					} catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String dateString = format2.format(date);
				tv_orderdate.setText("Order Date: " + dateString);
			} else {
				tv_orderdate.setVisibility(View.GONE);
			}
			if (!array.get(position).ordertype.equalsIgnoreCase("")) {
				tv_ordertype.setVisibility(View.VISIBLE);
				String message = "";
				if (Integer.parseInt(array.get(position).ordertype) == 0) {
					message = "Home Delivery";
				} else if (Integer.parseInt(array.get(position).ordertype) == 1) {
					message = "Pickup Order";
				} else if (Integer.parseInt(array.get(position).ordertype) == 2) {
					message = "Fastlane Order";
				}
				tv_ordertype.setText("Order Type: " + message);
			} else {
				tv_ordertype.setVisibility(View.GONE);
			}

			if (!array.get(position).brunch_timings.equalsIgnoreCase("")) {
				if (!Appconstants.strMenuflag.equalsIgnoreCase("HappyHrs")) {
					tvBrunchTimings.setVisibility(View.VISIBLE);
					tvBrunchTimings.setText("Lunch-"
							+ array.get(position).brunch_timings);
				}

			} else {
				tvBrunchTimings.setVisibility(View.GONE);
			}
			if (array.get(position).happy_hours == true) {
				if (Appconstants.strMenuflag.equalsIgnoreCase("HappyHrs")) {
					tvOthers.setVisibility(View.VISIBLE);
					tvOthers.setText("Happy Hours-"
							+ array.get(position).happy_hours_start + " TO "
							+ array.get(position).happy_hours_end);
				}
			} else {
				tvOthers.setVisibility(View.GONE);
			}

			if (!array.get(position).has_features.equalsIgnoreCase("")) {
				tvHas.setVisibility(View.VISIBLE);
				tvHas.setText("Has: " + array.get(position).has_features);
			} else {
				tvHas.setVisibility(View.GONE);
			}

			if (!array.get(position).serving_items.equalsIgnoreCase("")) {
				tvService.setVisibility(View.VISIBLE);
				tvService.setText("Serving: "
						+ array.get(position).serving_items);
			} else {
				tvService.setVisibility(View.GONE);
			}
			if (!array.get(position).keyword.equalsIgnoreCase("")) {
				keyword.setVisibility(View.VISIBLE);
				keyword.setText("Keyword: " + array.get(position).keyword);
			} else {
				keyword.setVisibility(View.GONE);
			}

			if (!array.get(position).existing_buffets.equalsIgnoreCase("")) {
				tvExistingBuffet.setVisibility(View.VISIBLE);

				tvExistingBuffet.setText(array.get(position).existing_buffets);
				Localsecrets.str_buffet_time = tvExistingBuffet.getText()
						.toString();
				System.out.println("buffet timings"
						+ array.get(position).existing_buffets);

			} else {
				tvExistingBuffet.setVisibility(View.GONE);
			}

			priceRate.setVisibility(View.VISIBLE);
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

	class getOrderCategoriesTask extends AsyncTask<String, Void, String> {

//		ProgressDialog pDialog;
		String strVenueId1, jsonString, StrCat_Id;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

//			pDialog = new ProgressDialog(RepeatOrders.this);
//			pDialog.setMessage("Loading ...");
//			pDialog.setCancelable(false);
//			pDialog.show();
			showLoader();
		}

		@Override
		protected String doInBackground(String... params) {
			String service_url = "";

			ArrayList<String> order_type = new ArrayList<String>();
			ArrayList<String> order_type1 = new ArrayList<String>();
			System.out.println("pos" + PreferenceUtils.getOrderSession()
					+ PreferenceUtils.getOrderTypes());
			String[] ss = PreferenceUtils.getOrderSession().split(",");
			String[] ss1 = PreferenceUtils.getOrderTypes().split(",");
			for (int i = 0; i < ss.length; i++) {
				order_type.add("" + ss[i]);
				System.out.println("order" + ss[i]);
				order_type1.add("" + ss1[i]);
				System.out.println("types" + ss1[i]);
			}
			System.out.println("pos" + ss + ss1);
			int pos = order_type.indexOf("" + orderid);
			String type = order_type1.get(pos);
			System.out.println("pos" + pos + type);
			System.out.println("val" + pos + order_type + order_type1 + type);

			service_url = Appconstants.MAIN_HOST + "orderFoodCategories2/"
					+ Explore.strVenueId + "/0/0/" + orderid + "/" + type;
			int repeat_flag = Integer.parseInt(type);
			/*
			 * if(repeat_flag ==1){ Localsecrets.flg = 0; }else if(repeat_flag
			 * ==0){ Localsecrets.flg = 1; }else{
			 */
			Localsecrets.flg = repeat_flag;
			// }

			System.out.println("url is " + service_url);
			return WebServiceCalls.getJSONString(service_url);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

//			if (null != pDialog && pDialog.isShowing()) {
//				pDialog.dismiss();
//			}
			
			hideLoader();
			if (result == null || "".equals(result)) {
				return;
			}
			try {
				JSONObject mainObject = new JSONObject(result);
				JSONArray jsonArray = mainObject.getJSONArray("settings");
				if (jsonArray.length() > 0) {
					Launcher.restOrderDetails = mainObject;
					Intent in = new Intent(getApplicationContext(),
							OrderFood.class);
					in.putExtra("myorder", "2");
					in.putExtra("order_id", orderid);

					startActivity(in);

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}
}
