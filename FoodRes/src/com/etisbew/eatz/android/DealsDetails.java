package com.etisbew.eatz.android;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

public class DealsDetails extends BaseActivity {

	private TextView etRestName, tvAddressLine1, tvTitle, viewMenu,
			tvGetFreeVoucher, tvTermsConditionsDetails, tvDealTime;
	private ImageView ivPhoto, back, ivCall;
	private QuickAction quickAction;
	ImageView options;
	WebView tvHighlightsDetails_wb;
	static ArrayList<String> images_path = new ArrayList<String>();
	public static String item = "";
	Spinner spinner;
	int flag_clicked = 0;
	static int paytm_flag = 0;
	String strDealTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.deals_details);

		etRestName = (TextView) findViewById(R.id.etRestName);
		etRestName.setText(Deals.jobjDeals.optString("venuename"));
		tvAddressLine1 = (TextView) findViewById(R.id.tvAddressLine1);
		tvAddressLine1.setText(Deals.jobjDeals.optString("address1") + ", "
				+ Deals.jobjDeals.optString("address2") + ", "
				+ Deals.jobjDeals.optString("location"));
		Deals.hig = Deals.jobjDeals.optString("highlights").replace(":&", ":");
		Deals.tnc = Deals.jobjDeals.optString("termsconditions").replace(":&",":");

		tvDealTime = (TextView) findViewById(R.id.txtDealTime);
		strDealTime = "";
		if (Deals.jobjDeals.has("dealtime")) {
			if (!TextUtils.isEmpty(Deals.jobjDeals.optString("dealtime"))) {
				strDealTime = Deals.jobjDeals.optString("dealtime");
			}
		}

		if (!TextUtils.isEmpty(strDealTime))
			tvDealTime.setText("Time : " + strDealTime);
		else
			tvDealTime.setVisibility(View.GONE);

		if (Deals.jobjDeals.has("pgstatus")) {
			paytm_flag = Integer
					.parseInt(Deals.jobjDeals.optString("pgstatus"));

		}
		if (Deals.jobjDeals.has("dealmenu")) {

			try {

				images_path.clear();

				JSONArray json = Deals.jobjDeals.getJSONArray("dealmenu");
				for (int i = 0; i < json.length(); i++) {
					images_path.add("" + json.get(i));

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		spinner = (Spinner) findViewById(R.id.spinner1);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int i, long l) {
				item = spinner.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			}
		});

		viewMenu = (TextView) findViewById(R.id.viewMenu);
		if (images_path.size() > 0) {
			viewMenu.setVisibility(View.VISIBLE);
		} else {
			viewMenu.setVisibility(View.GONE);
		}
		viewMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("url is" + Appconstants.strWebviewUrl
						+ images_path.size());
				if (images_path.size() > 0) {
					/*
					 * Appconstants.strWebviewUrl = Deals.lik.replace(" ",
					 * "%20"); // Appconstants.strWebviewUrl =
					 * "http://www.geology.19thcenturyscience.org/books/1887-Geikie-ScenScot/%20%20%20acrobathyperlinks.pdf"
					 * ; Intent intent = new Intent(DealsDetails.this,
					 * PdfActivity.class);
					 * intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME,
					 * Appconstants.strWebviewUrl); startActivity(intent);
					 */
					Intent intent = new Intent(DealsDetails.this,
							DealGallery.class);
					startActivity(intent);

				}

				/*
				 * startActivity(new Intent(getApplicationContext(),
				 * WebViewLoader.class));
				 */
			}
		});

		ivPhoto = (ImageView) findViewById(R.id.ivItemPhoto);
		Picasso.with(DealsDetails.this)
				.load(Deals.jobjDeals.optString("deal_image")
						.replace("", "%20")).into(ivPhoto);
		back = (ImageView) findViewById(R.id.back);
		ivCall = (ImageView) findViewById(R.id.ivCall);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(Deals.jobjDeals.optString("title"));
		tvGetFreeVoucher = (TextView) findViewById(R.id.tvGetFreeVoucher);
		if (Deals.pay.length() > 0) {
			if (Deals.pay.equalsIgnoreCase("0")) {
				tvGetFreeVoucher.setText("Get free voucher");
			} else {
				tvGetFreeVoucher.setText("Buy now");
			}
		} else {
			tvGetFreeVoucher.setText("Get free voucher");
		}

		tvGetFreeVoucher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
					startActivity(new Intent(getApplicationContext(),
							GetVoucher.class));
				} else if (Appconstants.user_flag == 2) {
					startActivity(new Intent(getApplicationContext(),
							GetVoucher.class));

				} else {
					flag_clicked = 2;
					startActivity(new Intent(DealsDetails.this, Login.class));
				}

			}
		});

		tvHighlightsDetails_wb = (WebView) findViewById(R.id.txtHighlightsDetails_web);
		tvHighlightsDetails_wb.getSettings().setJavaScriptEnabled(true);
		tvTermsConditionsDetails = (TextView) findViewById(R.id.txtTermsConditionsDetails);
		tvTermsConditionsDetails.setVisibility(View.GONE);
		String final_string = "";
		String final_string1 = "";

		if (Deals.hig.contains("\r\n")) {
			String[] ss = Deals.hig.split("\r\n");
			for (int i = 0; i < ss.length; i++) {
				if (i == 0) {
					final_string = final_string + "<ul>";
				} else {
					if (ss[i].length() > 0) {
						final_string = final_string + "<li>" + ss[i] + "</li>";
					}
					if (i == ss.length - 1) {
						final_string = final_string + "</ul>\r\n";
					}
				}
			}
		} else {
			final_string = Deals.hig;
		}
		if (Deals.tnc.contains("\r\n")) {
			String[] ss1 = Deals.tnc.split("\r\n");
			for (int i = 0; i < ss1.length; i++) {
				if (i == 0) {
					final_string1 = final_string1 + "<ul>";
				} else {
					if (ss1[i].length() > 0) {
						final_string1 = final_string1 + "<li>" + ss1[i]
								+ "</li>";
					}
					if (i == ss1.length - 1) {
						final_string1 = final_string1 + "</ul>\r\n";
					}
				}
			}
		} else {
			final_string1 = Deals.tnc;
		}

		String str = "<html><body><br><b><font color=\"black\"> Highlights</font></b></br>"
				+ final_string.replace("&lt;", "<").replace("&gt;", ">")
						.replace("&gt;", ">").replace("&nbsp;", "")
						.replace("&amp;rsquo;", "’").replace("ndash;", "")
						.replace("&amp;amp;", "").trim()
				+ "<br><b><font color=\"black\"> "
				+ "Fine print</font></b></br>"
				+ final_string1.replace("&lt;", "<").replace("&gt;", ">")
						.replace("&gt;", ">").replace("&amp;", "")
						.replace("amp;", "").replace("&amp;rsquo;", "’")
						.replace("nbsp;", "").trim() + "</body></html>";

		System.out.println("string" + str);

		tvHighlightsDetails_wb.loadDataWithBaseURL("file:///android_asset/",
				str.toString(), "text/html", "UTF-8", "");

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

		ivCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					if (!TextUtils.isEmpty(Deals.jobjDeals.optString("phone"))) {
						startActivity(new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:"
										+ Deals.jobjDeals.optString("phone"))));
					}

				} catch (ActivityNotFoundException e) {
				}
			}
		});

		options = (ImageView) findViewById(R.id.options);
		options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// quickAction.show(v);
				final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);

				emailIntent.setType("text/html");
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"My Allergy Journal");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, ""); // Html.fromHtml("<small>"+sb.toString()+"</small>")
				startActivity(Intent.createChooser(emailIntent, "Email:"));

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (flag_clicked == 2) {
			flag_clicked = 0;
			if (!PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				startActivity(new Intent(getApplicationContext(),
						GetVoucher.class));
			} else if (Appconstants.user_flag == 2) {
				startActivity(new Intent(getApplicationContext(),
						GetVoucher.class));

			}

		}

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
							startActivity(new Intent(DealsDetails.this,
									Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							if (ConnectivityReceiver
									.checkInternetConnection(DealsDetails.this)) {
								new AccountDetails(DealsDetails.this).execute();
							} else {
								ConnectivityReceiver
										.showCustomDialog(DealsDetails.this);
							}
						} else if (actionId == Explore.ID_RES) {
							startActivityForResult(new Intent(
									DealsDetails.this, MyReservations.class), 1);
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(
									DealsDetails.this, MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(
									DealsDetails.this, MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(
									DealsDetails.this, MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(
									new Intent(DealsDetails.this,
											RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(
									DealsDetails.this, MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(DealsDetails.this,
									Login.class));
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
