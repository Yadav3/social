package com.etisbew.eatz.android.dropdownlist;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.etisbew.eatz.android.BaseActivity;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.utils.Utils;

public class MyReviewDetails extends BaseActivity {


	Button btnOrderFood, btnBookTable, btnMore;
	TextView txtTitle, txtWriteAReview;
	TextView txtReviewBy, txtWriteReview, txtReviewName, txtReviewDate,
			txtReview, txtServ, txtAtms, txtFood1, txtVal, txtDeleteReview;

	ImageView imgProfile;
	LinearLayout LYMore, LYMoreDropDown;
	Bitmap bimg;
	RatingBar servRating, FoodRating, atmosRating, valueRating, ratingReview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myreviewdetails);

		
		txtTitle = (TextView) findViewById(R.id.etFind);
		ImageView back = (ImageView)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		
		txtReviewName = (TextView) findViewById(R.id.txtReview);
//		txtReviewName.setTypeface(Localsecrets.Titillium_Semibold);
		if (!TextUtils.isEmpty(MyReviews.MyReviewJson.optString("title"))
				&& !TextUtils.isEmpty(MyReviews.MyReviewJson
						.optString("comment"))) {
			txtReviewName.setText(MyReviews.MyReviewJson.optString("title")
					+ "\n\n" + MyReviews.MyReviewJson.optString("comment"));
		}

		
		txtReviewDate = (TextView) findViewById(R.id.txtReviewDate);
//		txtReviewDate.setTypeface(Localsecrets.Titillium_Regular);
		if (!TextUtils.isEmpty(MyReviews.MyReviewJson.optString("datecreated"))) {
			txtReviewDate.setText(MyReviews.MyReviewJson
					.optString("datecreated"));
		}

		// imgProfile = (ImageView) findViewById(R.id.imgReview);
		// BitmapDownloaderTask task = new BitmapDownloaderTask(imgProfile);
		// task.execute(MyReviews.MyReviewJson.optString("profilepic").replace(
		// " ", "%20"));

		txtServ = (TextView) findViewById(R.id.txtServ);
//		txtServ.setTypeface(Localsecrets.Titillium_Semibold);

		txtAtms = (TextView) findViewById(R.id.txtAtms);
//		txtAtms.setTypeface(Localsecrets.Titillium_Semibold);

		txtFood1 = (TextView) findViewById(R.id.txtFood1);
//		txtFood1.setTypeface(Localsecrets.Titillium_Semibold);

		txtVal = (TextView) findViewById(R.id.txtVal);
//		txtVal.setTypeface(Localsecrets.Titillium_Semibold);

		txtDeleteReview = (TextView) findViewById(R.id.txtDeleteReview);
//		txtDeleteReview.setTypeface(Localsecrets.Titillium_Regular);
		txtDeleteReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPromptDialog();
			}
		});

		servRating = (RatingBar) findViewById(R.id.serveRating);
		LayerDrawable stars = (LayerDrawable) servRating.getProgressDrawable();
		stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		if(!MyReviews.MyReviewJson.isNull("service_rating")){
		servRating.setRating(Float.parseFloat(MyReviews.MyReviewJson.optString("service_rating")));
		}
		FoodRating = (RatingBar) findViewById(R.id.foodRating);
		LayerDrawable stars1 = (LayerDrawable) FoodRating.getProgressDrawable();
		stars1.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		if(!MyReviews.MyReviewJson.isNull("food_rating")){
		FoodRating.setRating(Float.parseFloat(MyReviews.MyReviewJson
				.optString("food_rating")));
		}
		
		atmosRating = (RatingBar) findViewById(R.id.atmosphereRating);
		LayerDrawable stars3 = (LayerDrawable) atmosRating.getProgressDrawable();
		stars3.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		if(!MyReviews.MyReviewJson.isNull("atmosphere_rating")){
		atmosRating.setRating(Float.parseFloat(MyReviews.MyReviewJson
				.optString("atmosphere_rating")));
		}
		valueRating = (RatingBar) findViewById(R.id.valueRating);
		LayerDrawable stars4 = (LayerDrawable) valueRating.getProgressDrawable();
		stars4.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		if(!MyReviews.MyReviewJson.isNull("value_rating")){
		valueRating.setRating(Float.parseFloat(MyReviews.MyReviewJson
				.optString("value_rating")));
		}
		
		ratingReview = (RatingBar) findViewById(R.id.ratingbarRestaurant);
		LayerDrawable stars5 = (LayerDrawable) ratingReview.getProgressDrawable();
		stars5.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		if(!MyReviews.MyReviewJson.isNull("rating")){
		ratingReview.setRating(Float.parseFloat(MyReviews.MyReviewJson
				.optString("rating")));
		}
	}

	private void showPromptDialog() {
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyReviewDetails.this);
		alertDialog.setTitle("Confirm Delete...");
		alertDialog.setMessage("Are you sure you want delete this review ?");
		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if (ConnectivityReceiver.checkInternetConnection(MyReviewDetails.this)) {

							new DeleteReviewTask().execute();

						} else {
							ConnectivityReceiver.showCustomDialog(MyReviewDetails.this);
						}

//						new DeleteReviewTask().execute();
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

	class DeleteReviewTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyReviewDetails.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			String url=Appconstants.url2 + "deleteReview/"
					+ PreferenceUtils.getUserSession() + "/" + MyReviews.strReviewId;
			return Utils.getJSONStringGET(url);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyReviewDetails.this,
						"No data found from web!!!");
				// MyReservations.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					String strErrorCode = mainJson.optString("errorCode");
					String strErrorMsg = mainJson.optString("errorMessage");
					if (Integer.parseInt(strErrorCode) == 0) {
						// dialog1.dismiss();

						AlertDialog.Builder builder = new AlertDialog.Builder(
								MyReviewDetails.this);
						builder.setMessage(strErrorMsg)
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
												setResult(10121);
												finish();
												/*startActivity(new Intent(
														getApplicationContext(),
														MyReviews.class));*/
											}
										});
						AlertDialog alert = builder.create();
						alert.show();

					} else {
						showDialogMsg(MyReviewDetails.this, strErrorMsg);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() >= 0) {
			onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {

		MyReviewDetails.this.finish();
		startActivity(new Intent(getApplicationContext(), MyReviews.class));
		overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
		return;
	}
}
