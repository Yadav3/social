package com.etisbew.eatz.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.reviewDO;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

public class ReviewDetails extends BaseActivity {

	reviewDO rvdo;
	Button btnYes, btnReports;
	RatingBar ratingServ, ratingFood, ratingAtmos, ratingVal;
	TextView txtHelpfulMsg, txtProblemReview;
	EditText edtReasons;
	CheckBox chkCopyMsg;
	ImageView imgReviewPerson;

	String strReports[] = { "Wrong", "Incomplete", "Listed multiple times",
			"The category is incorrect", "Shut down/closed", "Wrongly named",
			"In the wrong place on the map", "Not real", "Abusive/offensive",
			"Other" };
	String strReportMsg = "Wrong", strCopyEmail = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.review_details);

		rvdo = (reviewDO) getIntent().getSerializableExtra("review");

		TextView tvUser = (TextView) findViewById(R.id.txtUser);
		tvUser.setText(rvdo.firstname.replace("null", ""));

		TextView tvDate = (TextView) findViewById(R.id.tvDate);
		tvDate.setText(rvdo.datecreated);

		imgReviewPerson = (ImageView) findViewById(R.id.imgReviewPerson);
		ImageView ivBack = (ImageView) findViewById(R.id.back);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});

		ImageView writeReview = (ImageView) findViewById(R.id.writeReview);

		writeReview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
					if (Appconstants.user_flag == 2) {
						SearchDetails.getWriteReview(v.getContext());
					} else {
						startActivity(new Intent(v.getContext(), Login.class));
					}
				} else {
					SearchDetails.getWriteReview(v.getContext());
				}
			}
		});

		// set the rating.
		RatingBar rating = (RatingBar) findViewById(R.id.userRating);
		int rate = Integer.parseInt(rvdo.rating);
		rating.setRating(rate);

		ratingServ = (RatingBar) findViewById(R.id.serveRating);

		ratingFood = (RatingBar) findViewById(R.id.foodRating);

		ratingAtmos = (RatingBar) findViewById(R.id.atmosphereRating);

		ratingVal = (RatingBar) findViewById(R.id.valueRating);

		TextView txtReviewTitle = (TextView) findViewById(R.id.txtReviewTitle);
		txtReviewTitle.setText(rvdo.title);

		TextView tvReviewDesc = (TextView) findViewById(R.id.tvReviewDesc);
		tvReviewDesc.setText(rvdo.comment);

		TextView etFind = (TextView) findViewById(R.id.etFind);
		if (!TextUtils.isEmpty(SearchDetails.str_restaurant_name)) {
			etFind.setText(SearchDetails.str_restaurant_name);// restDetails.name
		}
		txtHelpfulMsg = (TextView) findViewById(R.id.txtHelpfulMsg);
		btnYes = (Button) findViewById(R.id.btnYes);

		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
					if (Appconstants.user_flag == 2) {
						new ReviewHelpFulTask().execute();
					} else {
						startActivity(new Intent(v.getContext(), Login.class));
					}
				} else {
					new ReviewHelpFulTask().execute();
				}

				// if
				// (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				// startActivity(new Intent(getApplicationContext(),
				// Login.class));
				// } else {
				// new ReviewHelpFulTask().execute();
				// }

			}
		});

		txtProblemReview = (TextView) findViewById(R.id.txtProblemReview);
		txtProblemReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
					if (Appconstants.user_flag == 2) {
						ReportDialog();
					} else {
						startActivity(new Intent(v.getContext(), Login.class));
					}
				} else {
					ReportDialog();
				}

				// if
				// (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				// startActivity(new Intent(v.getContext(), Login.class));
				// return;
				// }
				//
			}
		});

		if (ConnectivityReceiver.checkInternetConnection(ReviewDetails.this)) {
			new ReviewDetailsTask().execute();

		} else {

			ConnectivityReceiver.showCustomDialog(ReviewDetails.this);
		}

	}

	public void ReportDialog() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(ReviewDetails.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.problemreview);

		TextView txtProblemReviewMsg = (TextView) dialog
				.findViewById(R.id.txtProblemReviewMsg);
		txtProblemReviewMsg.setText(Html
				.fromHtml("This report is about <font><b>" + rvdo.comment
						+ "</b></font>"));

		TextView txtReportRestaurant = (TextView) dialog
				.findViewById(R.id.txtReportRestaurant);
		if (!TextUtils.isEmpty(SearchDetails.str_restaurant_name)) {
			txtReportRestaurant.setText("@ "
					+ SearchDetails.str_restaurant_name);
		} else {
			txtReportRestaurant.setVisibility(View.GONE);
		}

		edtReasons = (EditText) dialog.findViewById(R.id.edtReasons);
		EditText edtUsername = (EditText) dialog.findViewById(R.id.edtUsername);
		edtUsername.setText(PreferenceUtils.getUserFirstName());

		EditText edtUserEmail = (EditText) dialog
				.findViewById(R.id.edtUserEmail);
		edtUserEmail.setText(PreferenceUtils.getUserEmail1());

		chkCopyMsg = (CheckBox) dialog.findViewById(R.id.chkCopyMsg);

		btnReports = (Button) dialog.findViewById(R.id.btnReports);
		btnReports.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(
						ReviewDetails.this);
				builderSingle.setTitle("Select Type");

				builderSingle.setItems(strReports,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									final int position) {

								try {
									btnReports.setText(strReports[position]);
									strReportMsg = strReports[position];

								} catch (Exception e) {
									// TODO: handle exception
								}

							}

						});

				builderSingle.show();
			}
		});

		Button btnSend = (Button) dialog.findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (chkCopyMsg.isChecked()) {
					strCopyEmail = "1";
				} else {
					strCopyEmail = "0";
				}
				if (!TextUtils.isEmpty(edtReasons.getText().toString())) {
					if (ConnectivityReceiver
							.checkInternetConnection(ReviewDetails.this)) {
						dialog.dismiss();

						new SendReportReviewTask().execute();

					} else {

						ConnectivityReceiver
								.showCustomDialog(ReviewDetails.this);
					}

				} else {
					showDialogMsg(ReviewDetails.this, "Please enter specifics");

				}

			}
		});

		dialog.show();

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
					+ "reviewDetails/" + rvdo.id);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// if (null != pDialog && pDialog.isShowing()) {
			// pDialog.dismiss();
			// }

			if (null == result || result.length() == 0) {
				BaseActivity.showDialogMsg(ReviewDetails.this,
						"No data found from web!!!");

			} else {

				if (result.contains("errorMessage")) {
					try {
						String strErrMsg = new JSONObject(result)
								.optString("errorMessage");
						BaseActivity.showDialogMsg(ReviewDetails.this,
								strErrMsg);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {

					try {
						JSONObject ReviewJson = new JSONObject(result);

						if (ReviewJson.optString("service_rating")
								.equalsIgnoreCase("null")) {
							ratingServ.setRating(0);
						} else {
							ratingServ.setRating(Integer.parseInt(ReviewJson
									.optString("service_rating")));
						}

						if (ReviewJson.optString("food_rating")
								.equalsIgnoreCase("null")) {
							ratingFood.setRating(0);
						} else {
							ratingFood.setRating(Integer.parseInt(ReviewJson
									.optString("food_rating")));
						}

						if (ReviewJson.optString("atmosphere_rating")
								.equalsIgnoreCase("null")) {
							ratingAtmos.setRating(0);
						} else {
							ratingAtmos.setRating(Integer.parseInt(ReviewJson
									.optString("atmosphere_rating")));
						}

						if (ReviewJson.has("profilepic")) {

							Picasso.with(ReviewDetails.this)
									.load(ReviewJson.optString("profilepic")
											.replace(" ", "%20"))
									.into(imgReviewPerson);
						}

						if (ReviewJson.optString("value_rating")
								.equalsIgnoreCase("null")) {
							ratingVal.setRating(0);
						} else {
							ratingVal.setRating(Integer.parseInt(ReviewJson
									.optString("value_rating")));
						}

						String strHelpStatus = ReviewJson
								.optString("user_helpful");
						if (strHelpStatus.equalsIgnoreCase("1")) {
							txtHelpfulMsg
									.setText("You found this review helpful.");
							btnYes.setVisibility(View.GONE);
						} else {

							txtHelpfulMsg.setText("Was this review helpful ?");
							btnYes.setVisibility(View.VISIBLE);
						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}
	}

	class ReviewHelpFulTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(ReviewDetails.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			String usersession = null;

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				if (Appconstants.user_flag == 2) {
					usersession = Appconstants.sessionId;
				} else {
					startActivity(new Intent(ReviewDetails.this, Login.class));
				}

			} else {
				usersession = PreferenceUtils.getUserSession();
			}

			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "reviewHelpful/" + usersession + "/" + rvdo.id);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				BaseActivity.showDialogMsg(ReviewDetails.this,
						"No data found from web!!!");

			} else {

				if (result.contains("errorMessage")) {
					try {
						String strErrMsg = new JSONObject(result)
								.optString("errorMessage");
						BaseActivity.showDialogMsg(ReviewDetails.this,
								strErrMsg);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					ReviewDetails.this.finish();
				}

			}
		}
	}

	class SendReportReviewTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(ReviewDetails.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String usersession = null;

			if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
				if (Appconstants.user_flag == 2) {
					usersession = Appconstants.sessionId;
				} else {
					startActivity(new Intent(ReviewDetails.this, Login.class));
				}

			} else {
				usersession = PreferenceUtils.getUserSession();
			}

			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "reportReview/" + usersession + "/" + rvdo.id + "/"
					+ Explore.strVenueId + "/"
					+ btnReports.getText().toString().replace(" ", "%20") + "/"
					+ edtReasons.getText().toString().replace(" ", "%20") + "/"
					+ strCopyEmail);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				BaseActivity.showDialogMsg(ReviewDetails.this,
						"No data found from web!!!");

			} else {

				if (result.contains("errorMessage")) {
					try {
						String strErrMsg = new JSONObject(result)
								.optString("errorMessage");
						BaseActivity.showDialogMsg(ReviewDetails.this,
								strErrMsg);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					ReviewDetails.this.finish();
				}

			}
		}
	}
}
