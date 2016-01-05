package com.etisbew.eatz.android.dropdownlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.etisbew.eatz.android.BaseActivity;
import com.etisbew.eatz.android.Explore;
import com.etisbew.eatz.android.Explore.AccountDetails;
import com.etisbew.eatz.android.Launcher;
import com.etisbew.eatz.android.Login;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.android.RowAdapter;
import com.etisbew.eatz.android.orderfood.DataWrapper;
import com.etisbew.eatz.android.orderfood.OrderDetails;
import com.etisbew.eatz.android.orderfood.OrderDetails_1;
import com.etisbew.eatz.android.orderfood.OrderFood;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.common.WebViewLoader;
import com.etisbew.eatz.objects.Item;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

public class MyOrders extends BaseActivity {

	Button btnAccount;
	Dialog dialog, dialog1;
	List<Item> arrayOfList, arrayofItems;
	ListView lvOrders, lvItems;
	RowAdapter objAdapter, objAdapter1;
	JSONArray OrderItemsList;
	JSONObject ordersObject;
	EditText edtWRTitle, edtReviewComments;
	String strOverAllRating, strFoodQuantityRating, strFoodQualityRating,
			strPkgRating, strDeliveryTimeRating, strTotalItemCost, strVenueId,
			strInvoicePdf;

	ImageView imgLogo, imgUser;
	TextView txtUser, txtlocation, txtPointsEarned, txtPointPending,
			txtOrderTitle, txtArrowLeft;
	JSONArray ordersArray, statusArray;
	String strOrderId, strReason, strCancel, strClaim, strVenue, strClaimTitle,
			ordertype;

	static String[] status_ids = { "" };
	public static String[] status_names = { "" };

	public static ArrayList<String> status_id = new ArrayList<String>();
	ArrayList<String> status_name = new ArrayList<String>();

	private QuickAction quickAction;
	ImageView options;

	public HashMap<String, String> itemWithQuantity = new HashMap<String, String>();
	public HashMap<String, String> itemWithCatid = new HashMap<String, String>();
	public HashMap<String, String> itemWithPrice = new HashMap<String, String>();
	public HashMap<String, String> itemWithName = new HashMap<String, String>();
	public HashMap<String, String> itemWithIcon = new HashMap<String, String>();
	AlertDialog alertDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myorders);

		ImageView txtArrowLeft = (ImageView) findViewById(R.id.back);
		txtArrowLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// MyOrders.this.finish();

				startActivity(new Intent(getApplicationContext(),
						Launcher.class));

			}
		});

		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				quickAction.show(v);

			}
		});

		imgUser = (ImageView) findViewById(R.id.imgReviewPerson);
		Picasso.with(MyOrders.this)
				.load(PreferenceUtils.getUserProfilePic().replace(" ", "%20"))
				// .resize(100, 150).into(imgUser);
				.resize(100, 100).into(imgUser);

		txtUser = (TextView) findViewById(R.id.txtUser);
		txtUser.setText(Html.fromHtml(PreferenceUtils.getUserName1()));

		txtlocation = (TextView) findViewById(R.id.txtlocation);

		txtPointsEarned = (TextView) findViewById(R.id.txtPointsEarned);
		txtPointsEarned.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),
						MyPoints.class));
			}
		});

		/*
		 * try { Login.strPointsEarned = Launcher.accountDetailsArray
		 * .getJSONObject(0).getString("points_earned").toString(); } catch
		 * (JSONException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } catch (NullPointerException e) {
		 * e.printStackTrace(); }
		 * 
		 * txtPointsEarned.setText(Html.fromHtml("<font color=\"#3d3d3d\">" +
		 * "Points earned : " + "</font>" + "<font color=\"#d42a2b\">" +
		 * Login.strPointsEarned + "</font>"));
		 */

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

		txtPointPending = (TextView) findViewById(R.id.txtPointPending);

		/*
		 * try { Login.strPointsPending = Launcher.accountDetailsArray
		 * .getJSONObject(0).getString("points_pending").toString(); } catch
		 * (JSONException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } catch (NullPointerException e) {
		 * e.printStackTrace(); }
		 * txtPointPending.setText(Html.fromHtml("<font color=\"#3d3d3d\">" +
		 * "Points pending : " + "</font>" + "<font color=\"#d42a2b\">" +
		 * Login.strPointsPending + "</font>"));
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

		txtOrderTitle = (TextView) findViewById(R.id.txtOrderTitle);

		lvOrders = (ListView) findViewById(R.id.lvOrders);
		arrayOfList = new ArrayList<Item>();

		lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				try {
					strOrderId = ordersArray.getJSONObject(arg2)
							.getString("id");
					strCancel = ordersArray.getJSONObject(arg2).getString(
							"cancel_order");
					strClaim = ordersArray.getJSONObject(arg2)
							.getString("claim_points").toString();
					strVenue = ordersArray.getJSONObject(arg2)
							.getString("venuename").toString();
					strClaimTitle = "Rate " + strVenue;
					strVenueId = ordersArray.getJSONObject(arg2)
							.getString("venueid").toString();
					Appconstants.strOrderType = ordersArray.getJSONObject(arg2)
							.getString("ordertype").toString();
					strInvoicePdf = ordersArray.getJSONObject(arg2)
							.getString("pdf").toString();
					ordertype = ordersArray.getJSONObject(arg2)
							.getString("ordertype").toString();

					if (ConnectivityReceiver
							.checkInternetConnection(MyOrders.this)) {

						new OrderDetailsTask().execute();

					} else {
						ConnectivityReceiver.showCustomDialog(MyOrders.this);
					}

					// new OrderDetailsTask().execute();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});

		if (ConnectivityReceiver.checkInternetConnection(MyOrders.this)) {

			new OrdersTask().execute();

		} else {
			ConnectivityReceiver.showCustomDialog(MyOrders.this);
		}

		// new OrdersTask().execute();
	}

	class OrdersTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyOrders.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String url = Appconstants.MAIN_HOST + "myOrders/"
					+ PreferenceUtils.getUserSession();
			return WebServiceCalls.getJSONString(url);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyOrders.this, "No data found from web!!!");
				MyOrders.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					ordersArray = mainJson.getJSONArray("Orders");
					/*
					 * if (!mainJson.isNull("points_earned")) {
					 * Login.strPointsEarned = mainJson.optString(
					 * "points_earned").toString(); txtPointsEarned.setText(Html
					 * .fromHtml("<font color=\"#3d3d3d\">" + "Points earned : "
					 * + "</font>" + "<font color=\"#d42a2b\">" +
					 * Login.strPointsEarned + "</font>")); }
					 * 
					 * if (!mainJson.isNull("points_pending")) {
					 * Login.strPointsPending = mainJson.optString(
					 * "points_pending").toString();
					 * txtPointPending.setText(Html
					 * .fromHtml("<font color=\"#3d3d3d\">" +
					 * "Points pending : " + "</font>" +
					 * "<font color=\"#d42a2b\">" + Login.strPointsPending +
					 * "</font>")); }
					 */
					for (int i = 0; i < ordersArray.length(); i++) {
						JSONObject objJson = ordersArray.getJSONObject(i);

						Item objItem = new Item();

						objItem.setRestaurantId(objJson.getString("id"));
						objItem.setBookedDate(objJson.getString("date_order"));

						objItem.setRestaurantName(objJson
								.getString("venuename"));
						objItem.setOrderedDate(objJson
								.getString("date_delivery"));
						objItem.setVoucherValue(objJson.getString("ordervalue"));
						objItem.setOrderType(objJson.getString("ordertype"));
						objItem.setStatus(objJson.getString("status"));
						objItem.setPoints(objJson.getString("points"));

						arrayOfList.add(objItem);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// new StatusListTask().execute();
				setAdapterToListview();
			}

		}
	}

	class StatusListTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// pDialog = new ProgressDialog(MyOrders.this);
			// pDialog.setMessage("Loading...");
			// pDialog.setCancelable(false);
			// pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "orderStatusList");
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// if (null != pDialog && pDialog.isShowing()) {
			// pDialog.dismiss();
			// }

			if (null == result || result.length() == 0) {
				showDialogMsg(MyOrders.this, "No data found from web!!!");
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					statusArray = mainJson.getJSONArray("orderstatus");
					for (int i = 0; i < statusArray.length(); i++) {

						try {
							String strStatusid = statusArray.getJSONObject(i)
									.getString("status_id").toString();
							status_id.add(strStatusid);

							String strStatusName = statusArray.getJSONObject(i)
									.getString("status_name").toString();
							status_name.add(strStatusName);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					status_ids = status_id
							.toArray(new String[status_id.size()]);
					status_names = status_name.toArray(new String[status_name
							.size()]);

					objAdapter = new RowAdapter(MyOrders.this,
							R.layout.orders1, arrayOfList);
					lvOrders.setAdapter(objAdapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void showOrderDetails(String result) {
		// TODO Auto-generated method stub

		dialog = new Dialog(MyOrders.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.orderdetails);

		arrayofItems = new ArrayList<Item>();
		lvItems = (ListView) dialog.findViewById(R.id.listItems);

		try {

			ordersObject = new JSONObject(result);
			OrderItemsList = ordersObject.getJSONArray("Itemslist");
			for (int i = 0; i < OrderItemsList.length(); i++) {
				JSONObject objJson = OrderItemsList.getJSONObject(i);

				Item objItem = new Item();

				objItem.setProduct(objJson.getString("product"));
				objItem.setProductQty(objJson.getString("product_qty"));
				objItem.setProductPrice(objJson.getString("product_price"));
				objItem.setTotal(objJson.getString("total"));

				arrayofItems.add(objItem);

			}

			setItemsAdapter();

			TextView txtTax = (TextView) dialog.findViewById(R.id.txtTax);

			if (TextUtils.isEmpty(ordersObject.optString("tax_amount"))) {
				txtTax.setText("Service tax : " + getString(R.string.oneRupee)
						+ " 0");
			} else {
				txtTax.setText("Service tax : " + getString(R.string.oneRupee)
						+ " " + ordersObject.optString("tax_amount"));
			}

			TextView txtVat = (TextView) dialog.findViewById(R.id.txtVat);
			if (TextUtils.isEmpty(ordersObject.optString("vat_amount"))) {
				txtVat.setText("Vat : " + getString(R.string.oneRupee) + " 0");
			} else {
				txtVat.setText("Vat : " + getString(R.string.oneRupee) + " "
						+ ordersObject.optString("vat_amount"));
			}

			TextView txtDelivery = (TextView) dialog
					.findViewById(R.id.txtDelivery);
			if (ordertype.equalsIgnoreCase("Fastlane")) {
				txtDelivery.setVisibility(View.GONE);
			} else {
				if (TextUtils.isEmpty(ordersObject
						.optString("delivery_charges"))) {
					txtDelivery.setText("Delivery charges : "
							+ getString(R.string.oneRupee) + " 0");
				} else {
					txtDelivery.setText("Delivery charges : "
							+ getString(R.string.oneRupee) + " "
							+ ordersObject.optString("delivery_charges"));
				}
			}

			TextView txtTotal = (TextView) dialog.findViewById(R.id.txtTotal);
			// txtTotal.setTypeface(Localsecrets.Titillium_Bold);
			if (TextUtils.isEmpty(ordersObject.optString("ordertotal"))) {
				strTotalItemCost = "0";
				txtTotal.setText("Total : " + getString(R.string.oneRupee)
						+ " 0");
			} else {
				strTotalItemCost = ordersObject.optString("ordertotal");
				txtTotal.setText("Total : " + getString(R.string.oneRupee)
						+ " " + ordersObject.optString("ordertotal"));
			}

			TextView txtInvoice = (TextView) dialog
					.findViewById(R.id.txtInvoice);
			txtInvoice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (strInvoicePdf.length() > 2) {
						// startActivity(new Intent(Intent.ACTION_VIEW, Uri
						// .parse(strInvoicePdf)));

						Appconstants.strWebviewUrl = strInvoicePdf;
						startActivity(new Intent(getApplicationContext(),
								WebViewLoader.class));

					}

				}
			});

			Button btnClose = (Button) dialog.findViewById(R.id.btnClose);

			btnClose.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			TextView txtClaim = (TextView) dialog.findViewById(R.id.txtClaim);
			// txtClaim.setTypeface(Localsecrets.Titillium_Bold);

			if (Integer.parseInt(strClaim) == 0) {
				txtClaim.setVisibility(View.GONE);
			} else {
				txtClaim.setVisibility(View.VISIBLE);
			}

			txtClaim.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ShowClaimPointsAlert();
				}
			});

			TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
			// txtCancel.setTypeface(Localsecrets.Titillium_Bold);
			if (Integer.parseInt(strCancel) == 1) {

				txtCancel.setVisibility(View.VISIBLE);

			} else {
				txtCancel.setVisibility(View.GONE);
			}

			txtCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ShowCancelAlert();
				}
			});

			TextView txtRepeatOrder = (TextView) dialog
					.findViewById(R.id.txtRepeatOrder);
			// txtRepeatOrder.setTypeface(Localsecrets.Titillium_Bold);
			txtRepeatOrder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Explore.strRestaurantTitle = strVenue;
					Explore.strVenueId = strVenueId;
					if (ConnectivityReceiver
							.checkInternetConnection(MyOrders.this)) {

						new RepeatOrderTask().execute();

					} else {
						ConnectivityReceiver.showCustomDialog(MyOrders.this);
					}

					// new RepeatOrderTask().execute();
				}
			});

			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void setItemsAdapter() {

		objAdapter1 = new RowAdapter(MyOrders.this, R.layout.orderdetails1,
				arrayofItems);
		lvItems.setAdapter(objAdapter1);

	}

	public void setAdapterToListview() {

		if (ConnectivityReceiver.checkInternetConnection(MyOrders.this)) {

			new StatusListTask().execute();

		} else {
			ConnectivityReceiver.showCustomDialog(MyOrders.this);
		}

		// new StatusListTask().execute();

	}

	class OrderDetailsTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyOrders.this);
			pDialog.setMessage("Loading order details. Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String url = Appconstants.url2 + "viewOrderDetails/" + strOrderId;
			return WebServiceCalls.getJSONString(url);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyOrders.this, "No data found from web!!!");

			} else {

				if (result.contains("errorMessage")) {
					try {
						String strErrMsg = new JSONObject(result)
								.optString("errorMessage");
						showDialogMsg(MyOrders.this, strErrMsg);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					showOrderDetails(result);

				}

			}

		}
	}

	private void ShowClaimPointsAlert() {
		// TODO Auto-generated method stub
		dialog1 = new Dialog(MyOrders.this);
		dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog1.setContentView(R.layout.claimorderpoints);

		TextView txtClaimTitle = (TextView) dialog1
				.findViewById(R.id.txtClaimTitle);
		if (!TextUtils.isEmpty(strClaimTitle)) {
			txtClaimTitle.setText(strClaimTitle);
		}

		final TextView txtMsgCount = (TextView) dialog1
				.findViewById(R.id.txtMsgCount);

		edtWRTitle = (EditText) dialog1.findViewById(R.id.edtWRTitle);
		edtReviewComments = (EditText) dialog1
				.findViewById(R.id.edtReviewComments);
		edtReviewComments.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				txtMsgCount.setText(s.length() + " Characters");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		RatingBar rbDelivery = (RatingBar) dialog1
				.findViewById(R.id.RBDelivery);
		rbDelivery
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {

						strDeliveryTimeRating = String.valueOf(rating);

					}
				});
		RatingBar rbPkg = (RatingBar) dialog1.findViewById(R.id.RBPkg);
		rbPkg.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strPkgRating = String.valueOf(rating);

			}
		});
		RatingBar rbFoodQuality = (RatingBar) dialog1
				.findViewById(R.id.RBFoodQuality);
		rbFoodQuality
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						// TODO Auto-generated method stub
						strFoodQualityRating = String.valueOf(rating);

					}
				});
		RatingBar rbFoodQuantity = (RatingBar) dialog1
				.findViewById(R.id.RBFoodQuantity);
		rbFoodQuantity
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						// TODO Auto-generated method stub
						strFoodQuantityRating = String.valueOf(rating);

					}
				});

		RatingBar rbOverAll = (RatingBar) dialog1.findViewById(R.id.RBOverAll);
		rbOverAll.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strOverAllRating = String.valueOf(rating);

			}
		});

		Button btnWriteReviewSubmit = (Button) dialog1
				.findViewById(R.id.btnWriteReviewSubmit);

		btnWriteReviewSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(edtWRTitle.getText().toString())) {
					showDialogMsg(MyOrders.this, "Please enter review title");
				} else if (TextUtils.isEmpty(edtReviewComments.getText()
						.toString())) {
					showDialogMsg(MyOrders.this, "Please enter review comments");
				} else if (TextUtils.isEmpty(strDeliveryTimeRating)) {
					showDialogMsg(MyOrders.this,
							"Please enter rating for delivery timing");
				} else if (TextUtils.isEmpty(strPkgRating)) {
					showDialogMsg(MyOrders.this, "Please enter packing rating");
				} else if (TextUtils.isEmpty(strFoodQualityRating)) {
					showDialogMsg(MyOrders.this,
							"Please enter food quality rating");
				} else if (TextUtils.isEmpty(strFoodQuantityRating)) {
					showDialogMsg(MyOrders.this,
							"Please enter food quantity rating");
				} else if (TextUtils.isEmpty(strOverAllRating)) {
					showDialogMsg(MyOrders.this, "Please enter overall rating");
				} else if (!TextUtils.isEmpty(edtWRTitle.getText().toString())
						&& !TextUtils.isEmpty(edtReviewComments.getText()
								.toString())
						&& !TextUtils.isEmpty(strDeliveryTimeRating)
						&& !TextUtils.isEmpty(strPkgRating)
						&& !TextUtils.isEmpty(strFoodQualityRating)
						&& !TextUtils.isEmpty(strFoodQuantityRating)
						&& !TextUtils.isEmpty(strOverAllRating)) {
					if (ConnectivityReceiver
							.checkInternetConnection(MyOrders.this)) {
						new ClaimYourPointsTask().execute();
					} else {
						showDialogMsg(MyOrders.this,
								"Sorry, Network is not available. Please try again later");
					}

				}

			}
		});

		dialog1.show();
	}

	class ClaimYourPointsTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyOrders.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String url = Appconstants.url2
					+ "claimOrderPoints/"
					+ PreferenceUtils.getUserSession()
					+ "/"
					+ strOrderId
					+ "/"
					+ edtWRTitle.getText().toString().replace(" ", "%20")
							.trim()
					+ "/"
					+ edtReviewComments.getText().toString()
							.replace(" ", "%20").trim() + "/"
					+ strOverAllRating + "/" + strFoodQuantityRating + "/"
					+ strFoodQualityRating + "/" + strPkgRating + "/"
					+ strDeliveryTimeRating;

			return WebServiceCalls.getJSONString(url);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyOrders.this, "No data found from web!!!");
				// MyReservations.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					String strErrorCode = mainJson.optString("errorCode");
					if (Integer.parseInt(strErrorCode) == 0) {

						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						if (dialog1.isShowing()) {
							dialog1.dismiss();
						}
					}
					String strErrorMsg = mainJson.optString("errorMessage");
					showDialogMsg(MyOrders.this, strErrorMsg);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void ShowCancelAlert() {
		// TODO Auto-generated method stub

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MyOrders.this);
		alertDialogBuilder.setTitle("Reason for cancellation ");

		// LayoutParams layoutParams = new
		// LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT);
		// layoutParams.setMargins(10, 10, 10, 10);

		final EditText edt = new EditText(MyOrders.this);
		edt.setBackgroundColor(Color.WHITE);
		edt.setHint("Reason");
		edt.setLines(2);
		// edt.setLayoutParams(layoutParams);

		// LinearLayout layout = new LinearLayout(ReservationConfirmed.this);
		// layout.addView(edt);
		// alertDialogBuilder.setView(layout);
		alertDialogBuilder.setView(edt);

		alertDialogBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						strReason = edt.getText().toString()
								.replace(" ", "%20").trim();
						if (!TextUtils.isEmpty(strReason)) {
							if (ConnectivityReceiver
									.checkInternetConnection(MyOrders.this)) {

								new CancelOrdersTask().execute();

							} else {
								ConnectivityReceiver
										.showCustomDialog(MyOrders.this);
							}

							// new CancelOrdersTask().execute();
						} else {
							showDialogMsg(MyOrders.this, "Please enter reason");
						}

					}
				});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						alertDialog.dismiss();
					}
				});

		alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	class CancelOrdersTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyOrders.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			String url = Appconstants.url2 + "cancelOrderByUser/"
					+ PreferenceUtils.getUserSession() + "/" + strOrderId + "/"
					+ strReason.replace(" ", "%20");
			return WebServiceCalls.getJSONString(url);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyOrders.this, "No data found from web!!!");
				// MyReservations.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					String strErrorCode = mainJson.optString("errorCode");
					if (Integer.parseInt(strErrorCode) == 0) {
						/*
						 * if (dialog1.isShowing()) { dialog1.dismiss(); }
						 */
						String strErrorMsg = mainJson.optString("errorMessage");
						AlertDialog.Builder builder = new AlertDialog.Builder(
								MyOrders.this);
						builder.setMessage(strErrorMsg)
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int id) {
												MyOrders.this.finish();
												startActivity(new Intent(
														MyOrders.this,
														MyOrders.class));
											}
										});
						AlertDialog alert = builder.create();
						alert.show();

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	class RepeatOrderTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyOrders.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "repeatOrder/" + strOrderId + "/"
					+ PreferenceUtils.getUserSession());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyOrders.this, "No data found from web!!!");
				// MyReservations.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					if (!mainJson.has("errorCode")) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}

						JSONObject jObj = new JSONObject(result);
						JSONArray jArray = jObj.getJSONArray("productdetails");
						try {
							for (int i = 0; i < jArray.length(); i++) {
								JSONObject objJson = jArray.getJSONObject(i);

								itemWithPrice.put(objJson
										.getString("venuemenuproduct_id"),
										objJson.getString("product_price"));
								itemWithCatid
										.put(objJson
												.getString("venuemenuproduct_id"),
												objJson.getString("venuemenucategory_id"));
								itemWithName.put(objJson
										.getString("venuemenuproduct_id"),
										objJson.getString("Productname"));
								itemWithIcon.put(objJson
										.getString("venuemenuproduct_id"),
										objJson.getString("veg_type"));
								itemWithQuantity.put(objJson
										.getString("venuemenuproduct_id"),
										objJson.getString("product_qty"));
							}
						} catch (Exception e) {
							// TODO: handle exception
						}

						if (TextUtils.isEmpty(jObj.optString("order_vat"))) {
							OrderFood.TAG_VATCHARGES = "0";
						} else {
							OrderFood.TAG_VATCHARGES = jObj
									.optString("order_vat");
						}

						OrderFood.TAG_TOTAL_PRICE = jObj
								.optString("total_price");
						if (jObj.has("service_charges")) {
							if (TextUtils.isEmpty("service_charges")) {
								OrderFood.TAG_SERVICE_CHARGE = "0";
							} else {
								OrderFood.TAG_SERVICE_CHARGE = jObj
										.optString("service_charges");
							}

						} else {
							OrderFood.TAG_SERVICE_CHARGE = "0";
						}

						if (TextUtils.isEmpty(jObj
								.optString("delivery_charges"))) {
							OrderFood.TAG_DELIVERY_CHARGES = "0";
						} else {
							OrderFood.TAG_DELIVERY_CHARGES = jObj
									.optString("delivery_charges");
						}

						Intent secondIntent2 = new Intent(
								getApplicationContext(), OrderDetails.class);

						Iterator<Map.Entry<String, String>> iterator2 = itemWithQuantity
								.entrySet().iterator();
						ArrayList<OrderDetails_1> ordersList2 = new ArrayList<OrderDetails_1>();

						while (iterator2.hasNext()) {
							Map.Entry<String, String> pairs = iterator2.next();
							String itemID = pairs.getKey().trim();
							OrderDetails_1 item = new OrderDetails_1(itemID,
									itemWithName.get(itemID),
									itemWithQuantity.get(itemID),
									itemWithIcon.get(itemID),
									itemWithCatid.get(itemID));
							ordersList2.add(item);
							// it.remove(); // avoids a
							// ConcurrentModificationException
						}

						secondIntent2.putExtra("orders", new DataWrapper(
								ordersList2));
						secondIntent2.putExtra("eachidprices", itemWithPrice);
						secondIntent2.putExtra("eachidqty", itemWithQuantity);
						secondIntent2.putExtra("eachidicon", itemWithIcon);
						secondIntent2.putExtra("eachcatid", itemWithCatid);
						startActivity(secondIntent2);

					} else {
						String strErrorMsg = mainJson.optString("errorMessage");
						showDialogMsg(MyOrders.this, strErrorMsg);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
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
							startActivity(new Intent(MyOrders.this, Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							new AccountDetails(MyOrders.this).execute();
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(MyOrders.this,
									MyReservations.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(MyOrders.this,
									MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(MyOrders.this,
									MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(MyOrders.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(MyOrders.this,
									MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							finish();
							startActivity(new Intent(MyOrders.this, Login.class));
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(), Launcher.class));
	}

}
