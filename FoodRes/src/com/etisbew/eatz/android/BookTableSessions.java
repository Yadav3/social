package com.etisbew.eatz.android;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.etisbew.eatz.android.Explore.getSessionsSlotsTask;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.web.WebServiceCalls;

public class BookTableSessions extends BaseActivity implements OnClickListener {

	Button btnSize, btnSessions, findAtable = null;
	EditText edtDate;
	String strDate, strDate1;
	private int mYear;
	private int mMonth;
	private int mDay;
	String strSize = "2";
	String strSessionName="";
	static final int DATE_PICKER_ID = 1111;

	ImageView imgback;
	TextView tvRestName, tvAddress;
	RatingBar ratingBar;

	static String userLocation = "";
	static String getRestaurent;
	
	ArrayAdapter<String> sessionArrayAdapter, partySizesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.booktable_sessions);

		imgback = (ImageView) findViewById(R.id.back);
		imgback.setOnClickListener(this);

		tvRestName = (TextView) findViewById(R.id.txtRestaurantName1);
		tvRestName.setText(Explore.strRestaurantTitle);
		tvRestName.setOnClickListener(this);

		tvAddress = (TextView) findViewById(R.id.txtRstAddress);
		tvAddress.setText(Explore.strRestaurantAddress);
		tvAddress.setOnClickListener(this);

		ratingBar = (RatingBar) findViewById(R.id.ratngBar);
		LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
		stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		
		
		int rate = Integer.parseInt(Explore.strRestaurantRating);
		if (rate > 0) {
			ratingBar.setRating(rate);
		} else {
			ratingBar.setVisibility(View.INVISIBLE);
		}
		ratingBar.setOnClickListener(this);

		edtDate = (EditText) findViewById(R.id.edtDate);
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		edtDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(mDay).append("-").append(mMonth + 1).append("-")
				.append(mYear).append(" "));
		strDate = (new StringBuilder().append(mDay).append("-")
				.append(mMonth + 1).append("-").append(mYear)).toString();
		strDate1 = (new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay)).toString();
		edtDate.setOnClickListener(this);

		findAtable = (Button) findViewById(R.id.btn_findTable);
		findAtable.setOnClickListener(this);

		btnSessions = (Button) findViewById(R.id.btnSessions);
		btnSessions.setText(Explore.strBookingSession);
		btnSessions.setOnClickListener(this);

		btnSize = (Button) findViewById(R.id.btnSize);
		btnSize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(
						BookTableSessions.this);
				builderSingle.setTitle("Select party size");
				partySizesAdapter = new ArrayAdapter<String>(
						BookTableSessions.this,
						android.R.layout.select_dialog_item);

				for (int i = 2; i < 40; i++) {
					partySizesAdapter.add(String.valueOf(i));
				}

				builderSingle.setAdapter(partySizesAdapter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int pos) {

								strSize = partySizesAdapter.getItem(pos)
										.toString();
								btnSize.setText(strSize + " People");
							}
						});
				builderSingle.show();
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:
			
			DatePickerDialog _date =   new DatePickerDialog(this, pickerListener, mYear,mMonth,
                    mDay){
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {   
                    if (year < mYear)
                        view.updateDate(mYear, mMonth, mDay);

                    if (monthOfYear < mMonth && year == mYear)
                        view.updateDate(mYear, mMonth, mDay);

                    if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                        view.updateDate(mYear, mMonth, mDay);

                }
            };

			return _date;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			mYear = selectedYear;
			mMonth = selectedMonth;
			mDay = selectedDay;

			// Show selected date
			edtDate.setText(new StringBuilder().append(mDay).append("-")
					.append(mMonth + 1).append("-").append(mYear).append(" "));

			strDate = (new StringBuilder().append(mDay).append("-")
					.append(mMonth + 1).append("-").append(mYear)).toString();
			/*strDate1 = (new StringBuilder().append(mYear).append("-")
					.append(mMonth + 1).append("-").append(mDay)).toString();*/
			strDate1 = edtDate.getText().toString().trim();
			
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.edtDate:

			showDialog(DATE_PICKER_ID);
			break;

		case R.id.back:
			BookTableSessions.this.finish();
		case R.id.btn_findTable:

			if (btnSessions.getText().toString().contains("Select")) {
				BaseActivity.showDialogMsg(BookTableSessions.this,
						"Please select session");
			} else {
				try { 

					DateFormat srcDf = new SimpleDateFormat("dd-MM-yyyy");
					Date date1 = srcDf.parse(strDate);
					DateFormat destDf = new SimpleDateFormat("dd MMM yyyy");
					strDate = destDf.format(date1);

				} catch (ParseException e) {
					e.printStackTrace();
				}

				Explore.strBookDateMsg = strDate + " - "
						+ btnSessions.getText().toString() + " - party of  "
						+ strSize;
				Explore.strBookMsg = Explore.strBookingSession
						+ " times available for " + strSize + " people on "
						+ strDate;
				System.out.println("message"+Explore.strBookMsg);

				Explore.strBookDate = strDate1;
				Explore.strPartySize = strSize;
				Explore.strSessionName = strSessionName;
				try{
				if (ConnectivityReceiver.checkInternetConnection(BookTableSessions.this)) {
				new getSessionsSlotsTask(BookTableSessions.this,Explore.strVenueId).execute();
			} else {
				ConnectivityReceiver.showCustomDialog(BookTableSessions.this);
			}
				}catch(Exception e){
					
				}
				
			}

			break;

		case R.id.btnSessions:
			if (ConnectivityReceiver.checkInternetConnection(BookTableSessions.this)) {
			new GetSessionsTask().execute();
			} else {
				ConnectivityReceiver.showCustomDialog(BookTableSessions.this);
			}
			break;
		}

	}

	class GetSessionsTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(BookTableSessions.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "getAvailableSessionsList/" + Explore.strVenueId + "/"
					+ edtDate.getText().toString().trim());

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

				if (result.contains("errorCode")) {

					try {
						JSONObject mainJson1 = new JSONObject(result);
						if (mainJson1.has("errorMessage")) {
							showDialogMsg(mainJson1.optString("errorMessage"));
						}

						String strErrorCode = new JSONObject(result)
								.optString("errorCode");
						if (Integer.parseInt(strErrorCode) == 0) {
							JSONObject mainJson = new JSONObject(result);
							String strSessions = mainJson.optString("sessions");

							AlertDialog.Builder builderSingle = new AlertDialog.Builder(
									BookTableSessions.this);
							builderSingle.setTitle("Select session");
							sessionArrayAdapter = new ArrayAdapter<String>(
									BookTableSessions.this,
									android.R.layout.select_dialog_item);

							if (strSessions.contains(",")) {
								String[] strSlots = strSessions.split(",");
								for (int i = 0; i < strSlots.length; i++) {
									sessionArrayAdapter.add(strSlots[i]);
								}
							} else {
								sessionArrayAdapter.add(strSessions);
							}

							builderSingle.setAdapter(sessionArrayAdapter,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog, int pos) {

											strSessionName = sessionArrayAdapter
													.getItem(pos).toString();
											Explore.strBookingSession=strSessionName;
											btnSessions.setText(strSessionName);
										}
									});
							builderSingle.show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}
	}

}