package com.etisbew.eatz.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.EventsDO;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

public class EventDetails extends BaseActivity {

	private EventsDO eventDetails = null;
	private TextView txtEventTitle, txtDesc, txtEvntLoc, txtEvntDate,
			txtEvntTime;
	private ImageView imgEventDetails, back, imgMap;
	Button btnAddToCalender, btnAddToFav, btnViewDetails;
	String strFavActionMsg = "add";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eventdetails);

		eventDetails = (EventsDO) getIntent().getSerializableExtra("item");

		txtEventTitle = (TextView) findViewById(R.id.txtEventTitle);
		txtEventTitle.setText(eventDetails.title);

		txtDesc = (TextView) findViewById(R.id.txtDesc);
		txtDesc.setText(eventDetails.description);

		txtEvntLoc = (TextView) findViewById(R.id.txtEvntLoc);
		txtEvntLoc.setText("Venue : " + eventDetails.location);

		txtEvntDate = (TextView) findViewById(R.id.txtEvntDate);
		txtEvntDate.setText(eventDetails.eventDate);

		txtEvntTime = (TextView) findViewById(R.id.txtEvntTime);
		txtEvntTime.setText(eventDetails.eventTime);

		imgMap = (ImageView) findViewById(R.id.imgMap);
		imgMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse("http://maps.google.com/maps?saddr="
								+ Appconstants.LATTITUDE + ","
								+ Appconstants.LANGITUDE + "&daddr="
								+ eventDetails.lat + "," + eventDetails.lang)));
			}
		});

		imgEventDetails = (ImageView) findViewById(R.id.imgEventDetails);
		Picasso.with(EventDetails.this).load(eventDetails.largeImage)
				.into(imgEventDetails);

		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

		btnViewDetails = (Button) findViewById(R.id.btnViewDetails);
		btnViewDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(eventDetails.eventsLink)));
			}
		});

		btnAddToCalender = (Button) findViewById(R.id.btnAddToCalender);
		btnAddToCalender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra("beginTime", eventDetails.eventStartDate);
				intent.putExtra("endTime", eventDetails.eventEndDate);
				intent.putExtra("title", eventDetails.title);
				startActivity(intent);
			}
		});

		btnAddToFav = (Button) findViewById(R.id.btnAddToFav);
		btnAddToFav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
					startActivity(new Intent(getApplicationContext(),
							Login.class));
				} else {
					if (ConnectivityReceiver.checkInternetConnection(EventDetails.this)) {
					new FavouriteTask().execute();
					} else {
						ConnectivityReceiver.showCustomDialog(EventDetails.this);
					}
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	class FavouriteTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(EventDetails.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "venueFavorites/" + PreferenceUtils.getUserSession()
					+ "/" + eventDetails.eventid + "/" + strFavActionMsg);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg("No data found from web!!!");

			} else {

				// if (result.contains("errorMessage")) {
				try {
					String strErrMsg = new JSONObject(result)
							.optString("errorMessage");
					showDialogMsg(strErrMsg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
