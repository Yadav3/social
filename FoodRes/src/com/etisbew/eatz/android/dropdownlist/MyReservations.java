package com.etisbew.eatz.android.dropdownlist;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.etisbew.eatz.android.BaseActivity;
import com.etisbew.eatz.android.Explore;
import com.etisbew.eatz.android.Explore.AccountDetails;
import com.etisbew.eatz.android.Launcher;
import com.etisbew.eatz.android.Login;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.android.RowAdapter;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.Item;
import com.etisbew.eatz.options.ActionItem;
import com.etisbew.eatz.options.QuickAction;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

public class MyReservations extends BaseActivity {

	Button btnAccount;
	Dialog dialog, dialog1, dialog2, dialog3, dialog4;
	List<Item> arrayOfList;
	ListView lvReservations;
	RowAdapter objAdapter;
	RadioGroup rg;
	RadioButton rb1;
	LinearLayout ly;
	String Img_paths="",encStr="";
	ImageView imgLogo, imgUser, imgBack;
	TextView txtUser, txtlocation, txtPointsEarned, txtPointPending,
			txtReservationTitle, txtArrowLeft;
	JSONArray jsonArray;
	static JSONArray reservationDetailsArray;
	static String strReservationId;
	EditText edtWRTitle, edtReviewComments;
	String strOverAllRating, strValueRating, strAtmosRating, strFoodRating,
			strServRating, strSize, strReason, strRaiseDispute, strVenueId;
	EditText edtRaiseDisputeMsg;
	private QuickAction quickAction;
	ImageView options;
	TextView txtRaiseDispute,file_choosen;
//	private Uri mImageCaptureUri;
	static Bitmap photo;
//	private static final int PICK_FROM_CAMERA = 1;
//	private static final int CROP_FROM_CAMERA = 2;
//	private static final int PICK_FROM_FILE = 3;
	
	private static final int REQUEST_CAMERA = 0;
	private static final int SELECT_FILE = 1;
	SharedPreferences settings;
	HttpResponse response;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myreservation);
		settings = getSharedPreferences("prefs", 0);
		txtUser = (TextView) findViewById(R.id.txtUser);

		txtUser.setText(Html.fromHtml(PreferenceUtils.getUserName1()));

		// txtUser.setTypeface(Localsecrets.Titillium_Bold);

		txtlocation = (TextView) findViewById(R.id.txtlocation);
		// txtlocation.setTypeface(Localsecrets.Titillium_Bold);

		txtPointsEarned = (TextView) findViewById(R.id.txtPointsEarned);
		txtPointsEarned.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),
						MyPoints.class));
			}
		});
		txtPointPending = (TextView) findViewById(R.id.txtPointPending);
		// txtPointPending.setTypeface(Localsecrets.Titillium_Bold);

		// btnAccount = (Button) findViewById(R.id.btnAccount);
		// btnAccount.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // quickAction.show(v);
		// }
		// });

		// txtArrowLeft = (TextView) findViewById(R.id.txtArrowLeft);
		// txtArrowLeft.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// MyReservations.this.finish();
		// }
		// });

		imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(MyReservations.this, Launcher.class));
				// MyReservations.this.finish();
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
		Picasso.with(MyReservations.this)
				.load(PreferenceUtils.getUserProfilePic().replace(" ", "%20"))
				// .resize(100, 150).into(imgUser);
				.resize(100, 100).into(imgUser);
		txtReservationTitle = (TextView) findViewById(R.id.txtReservationTitle);
		// txtReservationTitle.setTypeface(Localsecrets.Titillium_Bold);

		lvReservations = (ListView) findViewById(R.id.lvReservations);
		arrayOfList = new ArrayList<Item>();

		lvReservations
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						// TODO Auto-generated method stub
						// showReservationDetails();

						try {
							strReservationId = jsonArray.getJSONObject(pos)
									.getString("ReservationId").toString();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (ConnectivityReceiver.checkInternetConnection(MyReservations.this)) {

							new ReservationDetailsTask().execute();

						} else {
							ConnectivityReceiver.showCustomDialog(MyReservations.this);
						}
						
						
//						new ReservationDetailsTask().execute();
					}
				});
		
		if (ConnectivityReceiver.checkInternetConnection(MyReservations.this)) {

			new MyTask().execute();

		} else {
			ConnectivityReceiver.showCustomDialog(MyReservations.this);
		}

//		new MyTask().execute();
	}

	class MyTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyReservations.this);
			pDialog.setMessage("Loading your reservation details. Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String url = "";

			url = Appconstants.MAIN_HOST + "myReservations/"
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
				showDialogMsg(MyReservations.this, "No data found from web!!!");
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					if (!mainJson.isNull("points_earned")) {
						Login.strPointsEarned = mainJson.optString(
								"points_earned").toString();
						txtPointsEarned.setText(Html
								.fromHtml("<font color=\"#3d3d3d\">"
										+ "Points earned : " + "</font>"
										+ "<font color=\"#d42a2b\">"
										+ PreferenceUtils.getUserPoints() + "</font>"));
					}

					if (!mainJson.isNull("points_pending")) {
						Login.strPointsPending = mainJson.optString(
								"points_pending").toString();
						txtPointPending.setText(Html
								.fromHtml("<font color=\"#3d3d3d\">"
										+ "Points pending : " + "</font>"
										+ "<font color=\"#d42a2b\">"
										+ PreferenceUtils.getUserPointsPending() + "</font>"));
					}
					jsonArray = mainJson.getJSONArray("reservations");
					for (int i = 0; i < jsonArray.length(); i++) {
						// for (int i = 0; i < 20; i++) {
						JSONObject objJson = jsonArray.getJSONObject(i);

						Item objItem = new Item();

						objItem.setReservationId(objJson
								.getString("ReservationId"));
						objItem.setBookedDate(objJson.getString("BookedDate"));
						objItem.setVenue(objJson.getString("Venue"));
						objItem.setSession(objJson.getString("Session"));
						objItem.setPoints(objJson.getString("Points"));
						objItem.setPartySize(objJson.getString("PartySize"));
						objItem.setStatus(objJson.getString("Status"));
						objItem.setMadeon(objJson.getString("Madeon"));
						arrayOfList.add(objItem);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				setAdapterToListview();

			}

		}
	}

	// // @Override
	// // public void onItemClick(AdapterView<?> parent, View view, int
	// position,
	// // long id) {
	// // showReservationDetails();
	// // }
	//
	// // private void showReservationDetails() {
	// // // TODO Auto-generated method stub
	// //
	// // dialog = new Dialog(MyReservations.this);
	// // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// // dialog.setContentView(R.layout.reservationdetails);
	// //
	// // TextView txtReservationDetailsTitle = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsTitle);
	// // txtReservationDetailsTitle.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsDate = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsDate);
	// // txtReservationDetailsDate.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsDate1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsDate1);
	// //
	// txtReservationDetailsDate1.setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsVenue = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsVenue);
	// // txtReservationDetailsVenue.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsVenue1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsVenue1);
	// // txtReservationDetailsVenue1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsSession = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsSession);
	// // txtReservationDetailsSession.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsSession1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsSession1);
	// // txtReservationDetailsSession1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsPartySize = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsPartySize);
	// //
	// txtReservationDetailsPartySize.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsPartySize1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsPartySize1);
	// // txtReservationDetailsPartySize1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsConfirmationNo = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsConfirmationNo);
	// // txtReservationDetailsConfirmationNo
	// // .setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsConfirmationNo1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsConfirmationNo1);
	// // txtReservationDetailsConfirmationNo1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsStatus = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsStatus);
	// // txtReservationDetailsStatus.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsStatus1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsStatus1);
	// // txtReservationDetailsStatus1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsPoints = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsPoints);
	// // txtReservationDetailsPoints.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsPoints1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsPoints1);
	// // txtReservationDetailsPoints1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // TextView txtReservationDetailsMadeOn = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsMadeOn);
	// // txtReservationDetailsMadeOn.setTypeface(Localsecrets.Titillium_Bold);
	// //
	// // TextView txtReservationDetailsMadeOn1 = (TextView) dialog
	// // .findViewById(R.id.txtReservationDetailsMadeOn1);
	// // txtReservationDetailsMadeOn1
	// // .setTypeface(Localsecrets.Titillium_Semibold);
	// //
	// // Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
	// //
	// // btnClose.setOnClickListener(new OnClickListener() {
	// // @Override
	// // public void onClick(View v) {
	// // dialog.dismiss();
	// // }
	// // });
	// //
	// // dialog.show();
	// // }

	public void setAdapterToListview() {
		objAdapter = new RowAdapter(MyReservations.this,
				R.layout.reservationlist1, arrayOfList);
		lvReservations.setAdapter(objAdapter);

	}

	class ReservationDetailsTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyReservations.this);
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "reservationDetails/" + PreferenceUtils.getUserSession()
					+ "/" + strReservationId);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyReservations.this,
						"No data found from Server!!!");

			} else {

				try {

					JSONObject jObject = new JSONObject(result);
					reservationDetailsArray = jObject
							.getJSONArray("getReservations");
					if (result.contains("ErrorMessage")) {
						String errmsg = reservationDetailsArray
								.getJSONObject(0).getString("ErrorMessage")
								.toString();
						BaseActivity.showDialogMsg(MyReservations.this, errmsg);
					} else {

						dialog = new Dialog(MyReservations.this);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.reservationdetails);

						// TextView txtReservationDetailsTitle = (TextView)
						// dialog
						// .findViewById(R.id.txtReservationDetailsTitle);
						// txtReservationDetailsTitle
						// .setTypeface(Localsecrets.Titillium_Bold);

						// TextView txtReservationDetailsDate = (TextView)
						// dialog
						// .findViewById(R.id.txtReservationDetailsDate);
						// txtReservationDetailsDate
						// .setTypeface(Localsecrets.Titillium_Bold);

						TextView txtReservationDetailsDate1 = (TextView) dialog
								.findViewById(R.id.txtReservationDetailsDate1);
						// txtReservationDetailsDate1
						// .setTypeface(Localsecrets.Titillium_Semibold);
						txtReservationDetailsDate1.setText(Html
								.fromHtml(reservationDetailsArray
										.getJSONObject(0)
										.getString("reservation_date")
										.toString()));

						txtRaiseDispute = (TextView) dialog
								.findViewById(R.id.txtRaiseDispute);
						if (Integer.parseInt(reservationDetailsArray
								.getJSONObject(0).getString("Raise Dispute")
								.toString()) == 1) {
							txtRaiseDispute.setVisibility(View.VISIBLE);
						} else {
							txtRaiseDispute.setVisibility(View.GONE);
						}
						boolean firstRun = settings.getBoolean("message_alert",
								false);
						if (firstRun == false)// if running for first time
						// Splash will load for first time
						{
							SharedPreferences.Editor editor = settings.edit();
							editor.putBoolean("message_alert", false);
							editor.commit();

						} else {
							txtRaiseDispute.setVisibility(View.GONE);
						}
						txtRaiseDispute
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										ShowRaiseDisputeAlertBox();
									}
								});
						// txtReservationDetailsVenue
						// .setTypeface(Localsecrets.Titillium_Bold);

						TextView txtReservationDetailsVenue1 = (TextView) dialog
								.findViewById(R.id.txtReservationDetailsVenue1);
						// txtReservationDetailsVenue1
						// .setTypeface(Localsecrets.Titillium_Semibold);
						txtReservationDetailsVenue1.setText(Html
								.fromHtml(reservationDetailsArray
										.getJSONObject(0)
										.getString("Venuename").toString()));

						// TextView txtReservationDetailsSession = (TextView)
						// dialog
						// .findViewById(R.id.txtReservationDetailsSession);
						// txtReservationDetailsSession
						// .setTypeface(Localsecrets.Titillium_Bold);

						TextView txtReservationDetailsSession1 = (TextView) dialog
								.findViewById(R.id.txtReservationDetailsSession1);
						// txtReservationDetailsSession1
						// .setTypeface(Localsecrets.Titillium_Semibold);
						strVenueId = reservationDetailsArray.getJSONObject(0)
								.getString("Venueid").toString();
						txtReservationDetailsSession1.setText(Html
								.fromHtml(reservationDetailsArray
										.getJSONObject(0).getString("session")
										.toString()));

						// TextView txtReservationDetailsPartySize = (TextView)
						// dialog
						// .findViewById(R.id.txtReservationDetailsPartySize);
						// txtReservationDetailsPartySize
						// .setTypeface(Localsecrets.Titillium_Bold);

						TextView txtReservationDetailsPartySize1 = (TextView) dialog
								.findViewById(R.id.txtReservationDetailsPartySize1);
						// txtReservationDetailsPartySize1
						// .setTypeface(Localsecrets.Titillium_Semibold);
						txtReservationDetailsPartySize1.setText(Html
								.fromHtml(reservationDetailsArray
										.getJSONObject(0)
										.getString("party_size").toString()));

						// TextView txtReservationDetailsConfirmationNo =
						// (TextView) dialog
						// .findViewById(R.id.txtReservationDetailsConfirmationNo);
						// txtReservationDetailsConfirmationNo
						// .setTypeface(Localsecrets.Titillium_Bold);

						TextView txtReservationDetailsConfirmationNo1 = (TextView) dialog
								.findViewById(R.id.txtReservationDetailsConfirmationNo1);
						// txtReservationDetailsConfirmationNo1
						// .setTypeface(Localsecrets.Titillium_Semibold);
						txtReservationDetailsConfirmationNo1.setText(Html
								.fromHtml(reservationDetailsArray
										.getJSONObject(0)
										.getString("confirmation_number")
										.toString()));

						// TextView txtReservationDetailsStatus = (TextView)
						// dialog
						// .findViewById(R.id.txtReservationDetailsStatus);
						// txtReservationDetailsStatus
						// .setTypeface(Localsecrets.Titillium_Bold);

						TextView txtReservationDetailsStatus1 = (TextView) dialog
								.findViewById(R.id.txtReservationDetailsStatus1);
						// txtReservationDetailsStatus1
						// .setTypeface(Localsecrets.Titillium_Semibold);
						txtReservationDetailsStatus1.setText(Html
								.fromHtml(reservationDetailsArray
										.getJSONObject(0).getString("status")
										.toString()));

						// TextView txtReservationDetailsPoints = (TextView)
						// dialog
						// .findViewById(R.id.txtReservationDetailsPoints);
						// txtReservationDetailsPoints
						// .setTypeface(Localsecrets.Titillium_Bold);

						TextView txtReservationDetailsPoints1 = (TextView) dialog
								.findViewById(R.id.txtReservationDetailsPoints1);
						// txtReservationDetailsPoints1
						// .setTypeface(Localsecrets.Titillium_Semibold);
						txtReservationDetailsPoints1.setText(Html
								.fromHtml(reservationDetailsArray
										.getJSONObject(0).getString("points")
										.toString()));

						// TextView txtReservationDetailsMadeOn = (TextView)
						// dialog
						// .findViewById(R.id.txtReservationDetailsMadeOn);
						// txtReservationDetailsMadeOn
						// .setTypeface(Localsecrets.Titillium_Bold);

						TextView txtReservationDetailsMadeOn1 = (TextView) dialog
								.findViewById(R.id.txtReservationDetailsMadeOn1);
						// txtReservationDetailsMadeOn1
						// .setTypeface(Localsecrets.Titillium_Semibold);
						txtReservationDetailsMadeOn1.setText(Html
								.fromHtml(reservationDetailsArray
										.getJSONObject(0)
										.getString("reservation_madeon")
										.toString()));

						Button btnClose = (Button) dialog
								.findViewById(R.id.btnClose);

						btnClose.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});

						TextView txtCancel = (TextView) dialog
								.findViewById(R.id.txtCancel);
						// txtCancel.setTypeface(Localsecrets.Titillium_Semibold);
						if (Integer.parseInt(reservationDetailsArray
								.getJSONObject(0).getString("Cancel")
								.toString()) == 1) {
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

						TextView txtRateVenue = (TextView) dialog
								.findViewById(R.id.txtRateVenue);
						// txtRateVenue
						// .setTypeface(Localsecrets.Titillium_Semibold);
						if (Integer.parseInt(reservationDetailsArray
								.getJSONObject(0).getString("Rate this venue")
								.toString()) == 1) {
							txtRateVenue.setVisibility(View.VISIBLE);
						} else {
							txtRateVenue.setVisibility(View.GONE);
						}
						txtRateVenue.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								RateVenue();
							}

						});

						TextView txtClaimPoints = (TextView) dialog
								.findViewById(R.id.txtClaimPoints);
						// txtClaimPoints
						// .setTypeface(Localsecrets.Titillium_Semibold);
						if (Integer.parseInt(reservationDetailsArray
								.getJSONObject(0)
								.getString("Claim your points").toString()) == 1) {
							txtClaimPoints.setVisibility(View.VISIBLE);
						} else {
							txtClaimPoints.setVisibility(View.GONE);
						}
						txtClaimPoints
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										ShowClaimPointsAlert();
									}
								});

						dialog.show();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}

	}

	private void ShowRaiseDisputeAlertBox() {

		dialog4 = new Dialog(MyReservations.this);
		dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog4.setContentView(R.layout.raisedispute);

		try {
			TextView txtReservationDetailsDate1 = (TextView) dialog4
					.findViewById(R.id.txtReservationDetailsDate1);
			txtReservationDetailsDate1.setText(Html
					.fromHtml(reservationDetailsArray.getJSONObject(0)
							.getString("reservation_date").toString()));

			edtRaiseDisputeMsg = (EditText) dialog4
					.findViewById(R.id.edtRaiseDisputeMsg);

			TextView txtSubmit = (TextView) dialog4 
					.findViewById(R.id.txtSubmit);
			
			file_choosen = (TextView) dialog4 
					.findViewById(R.id.textView1);

			txtSubmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					strRaiseDispute = edtRaiseDisputeMsg.getText().toString();
					
					if (ConnectivityReceiver.checkInternetConnection(MyReservations.this)) {

						new RaiseDisputeTask().execute();

					} else {
						ConnectivityReceiver.showCustomDialog(MyReservations.this);
					}
					
//					new RaiseDisputeTask().execute();
				}
			});
			TextView btnClose1 = (TextView) dialog4.findViewById(R.id.btnClose1);
			btnClose1.setOnClickListener(new OnClickListener() {
				@Override 
				public void onClick(View v) {
				
					dialog4.dismiss();
				}
			});

			TextView btnChooseFile1 = (TextView) dialog4.findViewById(R.id.btnChooseFile1);
			btnChooseFile1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "clicked"+v.getId(), 5).show();
					selectImage();
					/*final String[] items = new String[] { "Take from camera",
							"Select from gallery" };
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							MyReservations.this,
							android.R.layout.select_dialog_item, items);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MyReservations.this);
 
					builder.setTitle("Select Image");
					builder.setAdapter(adapter,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) { // pick from camera
									if (item == 0) {
										Intent intent = new Intent(
												MediaStore.ACTION_IMAGE_CAPTURE);

										mImageCaptureUri = Uri.fromFile(new File(
												Environment
														.getExternalStorageDirectory(),
												"tmp_avatar_"
														+ String.valueOf(System
																.currentTimeMillis())
														+ ".jpg"));

										intent.putExtra(
												android.provider.MediaStore.EXTRA_OUTPUT,
												mImageCaptureUri);

										try {
											intent.putExtra("return-data", true);

											startActivityForResult(intent,
													PICK_FROM_CAMERA);
										} catch (ActivityNotFoundException e) {
											e.printStackTrace();
										}
									} else {
										Intent intent = new Intent();

										intent.setType("image/*");
										intent.setAction(Intent.ACTION_GET_CONTENT);

										startActivityForResult(
												Intent.createChooser(intent,
														"Complete action using"),
												PICK_FROM_FILE);
									}
								}
							});

					final AlertDialog dialog5 = builder.create();
					dialog5.show();
					*/
				}
			});

			TextView txtReservationDetailsVenue1 = (TextView) dialog4
					.findViewById(R.id.txtReservationDetailsVenue1);
			txtReservationDetailsVenue1.setText(Html
					.fromHtml(reservationDetailsArray.getJSONObject(0)
							.getString("Venuename").toString()));

			TextView txtReservationDetailsSession1 = (TextView) dialog4
					.findViewById(R.id.txtReservationDetailsSession1);
			txtReservationDetailsSession1.setText(Html
					.fromHtml(reservationDetailsArray.getJSONObject(0)
							.getString("session").toString()));

			TextView txtReservationDetailsPartySize1 = (TextView) dialog4
					.findViewById(R.id.txtReservationDetailsPartySize1);
			txtReservationDetailsPartySize1.setText(Html
					.fromHtml(reservationDetailsArray.getJSONObject(0)
							.getString("party_size").toString()));

			TextView txtReservationDetailsConfirmationNo1 = (TextView) dialog4
					.findViewById(R.id.txtReservationDetailsConfirmationNo1);
			txtReservationDetailsConfirmationNo1.setText(Html
					.fromHtml(reservationDetailsArray.getJSONObject(0)
							.getString("confirmation_number").toString()));

			TextView txtReservationDetailsStatus1 = (TextView) dialog4
					.findViewById(R.id.txtReservationDetailsStatus1);
			txtReservationDetailsStatus1.setText(Html
					.fromHtml(reservationDetailsArray.getJSONObject(0)
							.getString("status").toString()));

			TextView txtReservationDetailsPoints1 = (TextView) dialog4
					.findViewById(R.id.txtReservationDetailsPoints1);
			txtReservationDetailsPoints1.setText(Html
					.fromHtml(reservationDetailsArray.getJSONObject(0)
							.getString("points").toString()));

			TextView txtReservationDetailsMadeOn1 = (TextView) dialog4
					.findViewById(R.id.txtReservationDetailsMadeOn1);
			txtReservationDetailsMadeOn1.setText(Html
					.fromHtml(reservationDetailsArray.getJSONObject(0)
							.getString("reservation_madeon").toString()));

			


		} catch (Exception e) {
			// TODO: handle exception
		}
 
		dialog4.show();
	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Gallery",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(MyReservations.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Gallery")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				try {
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

					photo = BitmapFactory.decodeFile(f.getAbsolutePath(),
							btmapOptions);
					photo = Bitmap.createScaledBitmap(photo, 90, 100, true);
					new ConvertString().execute();
					String path = android.os.Environment
							.getExternalStorageDirectory()
							+ File.separator
							+ "Phoenix" + File.separator + "default";
				
					f.delete();
//					OutputStream fOut = null;
					File file = new File(path, String.valueOf(System
							.currentTimeMillis()) + ".jpg");
					Img_paths=""+file;
//					et_uploadphoto.setText(Img_paths);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				String tempPath = getPath(selectedImageUri, MyReservations.this);
				Img_paths=tempPath;
//				et_uploadphoto.setText(Img_paths);
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				photo = BitmapFactory.decodeFile(tempPath, btmapOptions);
				photo = Bitmap.createScaledBitmap(photo, 90, 100, true);
				new ConvertString().execute();
			}
		}
		if(requestCode==2){
			//Create an instance of bundle and get the returned data
			Bundle extras = data.getExtras();
			photo = extras.getParcelable("data");
			photo = Bitmap.createScaledBitmap(photo, 90, 100, true);
			new ConvertString().execute();
		}
	}
	//create helping method cropCapturedImage(Uri picUri)
			public void cropCapturedImage(Uri picUri){
				//call the standard crop action intent 
				Intent cropIntent = new Intent("com.android.camera.action.CROP");
				//indicate image type and Uri of image
				cropIntent.setDataAndType(picUri, "image/*");
				//set crop properties
				cropIntent.putExtra("crop", "true");
				//indicate aspect of desired crop
				cropIntent.putExtra("aspectX", 1);
				cropIntent.putExtra("aspectY", 1);
				//indicate output X and Y
				cropIntent.putExtra("outputX", 256);
				cropIntent.putExtra("outputY", 256);
				//retrieve data on return
				cropIntent.putExtra("return-data", true);
				//start the activity - we handle returning in onActivityResult
				startActivityForResult(cropIntent, 2);
			}

		public String getPath(Uri uri, Activity activity) {
			String[] projection = { MediaColumns.DATA };
			@SuppressWarnings("deprecation")
			Cursor cursor = activity
					.managedQuery(uri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		
		public String BitMapToString(Bitmap bitmap){
		     ByteArrayOutputStream baos=new  ByteArrayOutputStream();
		     bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
		     byte [] b=baos.toByteArray();
		     String temp=Base64.encodeToString(b, Base64.DEFAULT);
		     return temp;
		}
		
		private class ConvertString extends AsyncTask<Object, Void, Boolean> {

			ProgressDialog myProgressDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				myProgressDialog = new ProgressDialog(MyReservations.this);
				myProgressDialog.setMessage("please wait...");
				myProgressDialog.setCancelable(false);
				myProgressDialog.show();
				
				
			}

			@Override
			protected Boolean doInBackground(Object... params) {

				try {

//					String res=BitMapToString(bm);
					
					 ByteArrayOutputStream baos=new  ByteArrayOutputStream();
				     photo.compress(Bitmap.CompressFormat.PNG,100, baos);
				     byte [] b=baos.toByteArray();
//				     encStr= bytesToStringUTFNIO(b);
				     encStr=Base64.encodeToString(b, Base64.DEFAULT); 
				     if(!TextUtils.isEmpty(encStr)){
				     file_choosen.setVisibility(View.GONE);
				     }
				     /*try {
							File myFile = new File("/sdcard/mysdfile.txt");
							myFile.createNewFile();
							FileOutputStream fOut = new FileOutputStream(myFile);
							OutputStreamWriter myOutWriter = 
													new OutputStreamWriter(fOut);
							myOutWriter.append(encStr);
							myOutWriter.close();
							fOut.close();
							Toast.makeText(getBaseContext(),
									"Done writing SD 'mysdfile.txt'",
									Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							Toast.makeText(getBaseContext(), e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}*/
				     
				} catch (Exception e) {
					
				}

				return null;
			}

			@Override
			protected void onPostExecute(Boolean success) {
				super.onPostExecute(success);

				try {
				
//					 Toast.makeText(getApplicationContext(), encStr, Toast.LENGTH_LONG).show();
						
						myProgressDialog.dismiss();
					 
				} catch (Exception e) {
					// TODO: handle exception
					myProgressDialog.dismiss();
				}

			}
		}
		
		/**
		 * @param encodedString
		 * @return bitmap (from given string)
		 */
		public Bitmap StringToBitMap(String encodedString){
		   try {
		      byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
		      Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
		      return bitmap;
		   } catch(Exception e) {
		      e.getMessage();
		      return null;
		   }
		}


		public static String bytesToStringUTFNIO(byte[] bytes) {
			 CharBuffer cBuffer = ByteBuffer.wrap(bytes).asCharBuffer();
			 return cBuffer.toString();
			}
		

	class RaiseDisputeTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyReservations.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
		//	sfsdfs
			try{
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Appconstants.MAIN_HOST
					+ "raiseDispute/");
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("reservation_id", strReservationId));
			params1.add(new BasicNameValuePair("venue_id", strVenueId));
			params1.add(new BasicNameValuePair("sessionid", PreferenceUtils.getUserSession()));
			params1.add(new BasicNameValuePair("description", strRaiseDispute));
			if(!TextUtils.isEmpty(encStr)){
			params1.add(new BasicNameValuePair("disputeimage", encStr));
			}
			
			post.setEntity(new UrlEncodedFormEntity(params1));
			response = client.execute(post);
		/*	return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "raiseDispute/" + strReservationId + "/" + strVenueId
					+ "/" + PreferenceUtils.getUserSession() + "/"
					+ strRaiseDispute.replace(" ", "%20").trim());*/
			}catch(Exception e){
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) { 
				pDialog.dismiss();
			}

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				try {
					
					String re = EntityUtils.toString(entity, HTTP.UTF_8);

					if (null == re || re.length() == 0) {
						showDialogMsg(MyReservations.this, "No data found from web!!!");

					} else {

						try {
							JSONObject mainJson = new JSONObject(re);
							String strErrorCode = mainJson.optString("error");
							if (Integer.parseInt(strErrorCode) == 0) {
								if (dialog.isShowing()) {
									dialog.dismiss();
								}
								if (dialog4.isShowing()) {
									dialog4.dismiss();
								}

							}
							boolean firstRun = settings.getBoolean("message_alert",
									false);
							if (firstRun == false)// if running for first time
							// Splash will load for first time
							{
								SharedPreferences.Editor editor = settings.edit();
								editor.putBoolean("message_alert", true);
								editor.commit();
								String strErrorMsg = mainJson.optString("errorMessage");
								showDialogMsg(MyReservations.this, strErrorMsg);

							} else {
								txtRaiseDispute.setVisibility(View.GONE);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
					
			}
			/*if (null == result || result.length() == 0) {
				showDialogMsg(MyReservations.this, "No data found from web!!!");

			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					String strErrorCode = mainJson.optString("error");
					if (Integer.parseInt(strErrorCode) == 0) {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						if (dialog4.isShowing()) {
							dialog4.dismiss();
						}

					}
					boolean firstRun = settings.getBoolean("message_alert",
							false);
					if (firstRun == false)// if running for first time
					// Splash will load for first time
					{
						SharedPreferences.Editor editor = settings.edit();
						editor.putBoolean("message_alert", true);
						editor.commit();
						String strErrorMsg = mainJson.optString("errorMessage");
						showDialogMsg(MyReservations.this, strErrorMsg);

					} else {
						txtRaiseDispute.setVisibility(View.GONE);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}*/
	}
	}
	private void ShowCancelAlert() {
		// TODO Auto-generated method stub

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MyReservations.this);
		alertDialogBuilder.setTitle("Reason for cancellation ");

		// LayoutParams layoutParams = new
		// LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT);
		// layoutParams.setMargins(10, 10, 10, 10);

		final EditText edt = new EditText(MyReservations.this);
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

						strReason = edt.getText().toString();
						if (!TextUtils.isEmpty(strReason)) {
							if (ConnectivityReceiver.checkInternetConnection(MyReservations.this)) {

								new CancelReservationTask().execute();

							} else {
								ConnectivityReceiver.showCustomDialog(MyReservations.this);
							}
							
							
							
//							new CancelReservationTask().execute();
						} else {
							showDialogMsg(MyReservations.this,
									"Please enter reason");
						}

					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	class CancelReservationTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyReservations.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "cancelReservation/" + PreferenceUtils.getUserSession()
					+ "/" + strReservationId + "/"
					+ strReason.replace(" ", "%20").trim());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyReservations.this, "No data found from web!!!");

			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					String strErrorCode = mainJson.optString("errorCode");
					if (Integer.parseInt(strErrorCode) == 0) {
						dialog.dismiss();
					}
					String strErrorMsg = mainJson.optString("errorMessage");
					showDialogMsg(MyReservations.this, strErrorMsg);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void RateVenue() {

		dialog1 = new Dialog(MyReservations.this);
		dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog1.setContentView(R.layout.writereview);

		// TextView txtWriteReviewTitle = (TextView) dialog1
		// .findViewById(R.id.txtWriteReviewTitle);
		// txtWriteReviewTitle.setTypeface(Localsecrets.Titillium_Bold);

		TextView txtBeTheFirstReview = (TextView) dialog1
				.findViewById(R.id.txtBeTheFirstReview);
		// txtBeTheFirstReview.setTypeface(Localsecrets.Titillium_Semibold);
		try {
			txtBeTheFirstReview.setText("Rate "
					+ Html.fromHtml(reservationDetailsArray.getJSONObject(0)
							.getString("Venuename").toString()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TextView txtService = (TextView)
		// dialog1.findViewById(R.id.txtService);
		// txtService.setTypeface(Localsecrets.Titillium_Semibold);

		// TextView txtFood = (TextView) dialog1.findViewById(R.id.txtFood);
		// txtFood.setTypeface(Localsecrets.Titillium_Semibold);

		// TextView txtAtmos = (TextView) dialog1.findViewById(R.id.txtAtmos);
		// txtAtmos.setTypeface(Localsecrets.Titillium_Semibold);

		// TextView txtValue = (TextView) dialog1.findViewById(R.id.txtValue);
		// txtValue.setTypeface(Localsecrets.Titillium_Semibold);

		// TextView txtOverAll = (TextView)
		// dialog1.findViewById(R.id.txtOverAll);
		// txtOverAll.setTypeface(Localsecrets.Titillium_Semibold);

		// TextView txtLimitMsg = (TextView) dialog1
		// .findViewById(R.id.txtLimitMsg);
		// txtLimitMsg.setTypeface(Localsecrets.Titillium_Regular);

		final TextView txtMsgCount = (TextView) dialog1
				.findViewById(R.id.txtMsgCount);
		txtMsgCount.setVisibility(View.VISIBLE);
		// txtMsgCount.setTypeface(Localsecrets.Titillium_Regular);

		Button btnReview = (Button) dialog1.findViewById(R.id.btnReviewCount);
		btnReview.setVisibility(View.GONE);

		edtWRTitle = (EditText) dialog1.findViewById(R.id.edtWRTitle);
		// edtWRTitle.setTypeface(Localsecrets.Titillium_Semibold);

		edtReviewComments = (EditText) dialog1
				.findViewById(R.id.edtReviewComments);
		// edtReviewComments.setTypeface(Localsecrets.Titillium_Semibold);
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

		RatingBar rbService = (RatingBar) dialog1.findViewById(R.id.RBService);
		rbService.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {

				strServRating = String.valueOf(rating);

			}
		});
		RatingBar rbFood = (RatingBar) dialog1.findViewById(R.id.RBFood);
		rbFood.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strFoodRating = String.valueOf(rating);

			}
		});
		RatingBar rbAtmos = (RatingBar) dialog1.findViewById(R.id.RBAtmos);
		rbAtmos.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strAtmosRating = String.valueOf(rating);

			}
		});
		RatingBar rbValue = (RatingBar) dialog1.findViewById(R.id.RBValue);
		rbValue.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				// TODO Auto-generated method stub
				strValueRating = String.valueOf(rating);

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
		// btnWriteReviewSubmit.setTypeface(Localsecrets.Titillium_Semibold);

		btnWriteReviewSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(edtWRTitle.getText().toString())) {
					showDialogMsg(MyReservations.this,
							"Please enter review title");
				} else if (TextUtils.isEmpty(edtReviewComments.getText()
						.toString())) {
					showDialogMsg(MyReservations.this,
							"Please enter review comments");
				} else if (TextUtils.isEmpty(strServRating)) {
					showDialogMsg(MyReservations.this,
							"Please enter service rating for the venue");
				} else if (TextUtils.isEmpty(strFoodRating)) {
					showDialogMsg(MyReservations.this,
							"Please enter food rating for the venue");
				} else if (TextUtils.isEmpty(strAtmosRating)) {
					showDialogMsg(MyReservations.this,
							"Please enter atmosphere rating for the venue");
				} else if (TextUtils.isEmpty(strValueRating)) {
					showDialogMsg(MyReservations.this,
							"Please enter value rating for the venue");
				} else if (TextUtils.isEmpty(strOverAllRating)) {
					showDialogMsg(MyReservations.this,
							"Please enter overall rating for the venue");
				} else if (!TextUtils.isEmpty(edtWRTitle.getText().toString())
						&& !TextUtils.isEmpty(edtReviewComments.getText()
								.toString())
						&& !TextUtils.isEmpty(strServRating)
						&& !TextUtils.isEmpty(strFoodRating)
						&& !TextUtils.isEmpty(strAtmosRating)
						&& !TextUtils.isEmpty(strValueRating)
						&& !TextUtils.isEmpty(strOverAllRating)) {
					if (ConnectivityReceiver
							.checkInternetConnection(MyReservations.this)) {
						new RateThisVenueTask().execute();
					} else {
						BaseActivity.showDialogMsg(MyReservations.this,
								"Sorry, Network is not available. Please try again later");
					}
				}

			}
		});

		dialog1.show();
	}

	class RateThisVenueTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyReservations.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "rateThisVenue/"
					+ PreferenceUtils.getUserSession()
					+ "/"
					+ strReservationId
					+ "/"
					+ edtWRTitle.getText().toString().replace(" ", "%20")
							.trim()
					+ "/"
					+ edtReviewComments.getText().toString()
							.replace(" ", "%20").trim() + "/"
					+ strOverAllRating + "/" + strServRating + "/"
					+ strValueRating + "/" + strAtmosRating + "/"
					+ strFoodRating);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyReservations.this, "No data found from web!!!");
				// // MyReservations.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					String strErrorCode = mainJson.optString("errorCode");
					if (Integer.parseInt(strErrorCode) == 0) {
						dialog1.dismiss();
						if (dialog.isShowing()) {
							dialog.cancel();
						}
					} else {

					}
					String strErrorMsg = mainJson.optString("errorMessage")
							.replace("<p>", "").replace("</p>", "");
					showDialogMsg(MyReservations.this, strErrorMsg);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void ShowClaimPointsAlert() {
		// TODO Auto-generated method stub
		dialog3 = new Dialog(MyReservations.this);
		// dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog3.setContentView(R.layout.claimdialog);

		dialog3.setTitle("Claim your points");

		TextView txtGuestSize = (TextView) dialog3
				.findViewById(R.id.txtGuestSize);
		// txtGuestSize.setTypeface(Localsecrets.Titillium_Bold);
		try {
			strSize = MyReservations.reservationDetailsArray.getJSONObject(0)
					.getString("party_size").toString();
			txtGuestSize.setText("Your guests size is "
					+ MyReservations.reservationDetailsArray.getJSONObject(0)
							.getString("party_size").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}

		// TextView txtConfirmation = (TextView) dialog3
		// .findViewById(R.id.txtConfirmation);
		// txtConfirmation.setTypeface(Localsecrets.Titillium_Regular);

		rg = (RadioGroup) dialog3.findViewById(R.id.radioGroup1);

		ly = (LinearLayout) dialog3.findViewById(R.id.ly);

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				rb1 = (RadioButton) dialog3.findViewById(rg
						.getCheckedRadioButtonId());
				String name = rb1.getText().toString();

				if (name.equalsIgnoreCase("No")) {
					ly.setVisibility(View.VISIBLE);
				} else {
					ly.setVisibility(View.GONE);
				}

			}
		});

		// TextView txtChkGuests = (TextView)
		// dialog3.findViewById(R.id.textView1);
		// txtChkGuests.setTypeface(Localsecrets.Titillium_Regular);

		EditText edtChkGuest = (EditText) dialog3
				.findViewById(R.id.edtChkinGuests);

		if (!TextUtils.isEmpty(edtChkGuest.getText().toString())) {
			strSize = edtChkGuest.getText().toString();
		}

		Button dialogButton = (Button) dialog3.findViewById(R.id.btnSubmit);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ConnectivityReceiver.checkInternetConnection(MyReservations.this)) {

					new ClaimYourPointsTask().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(MyReservations.this);
				}

//				new ClaimYourPointsTask().execute();

			}
		});

		dialog3.show();
	}

	class ClaimYourPointsTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MyReservations.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "claimYourPoints/" + PreferenceUtils.getUserSession()
					+ "/" + strReservationId + "/" + strSize.trim());

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showDialogMsg(MyReservations.this, "No data found from web!!!");
				// // MyReservations.this.finish();
			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					String strErrorCode = mainJson.optString("errorCode");
					if (Integer.parseInt(strErrorCode) == 0) {
						if (dialog.isShowing())
							dialog.cancel();
						if (dialog3.isShowing()) {
							dialog3.cancel();
						}
					}
					String strErrorMsg = mainJson.optString("errorMessage");
					showDialogMsg(MyReservations.this, strErrorMsg);

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
							startActivity(new Intent(MyReservations.this,
									Login.class)
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
							finish();
						} else if (actionId == Explore.ID_MYACCOUNT) {
							if (ConnectivityReceiver.checkInternetConnection(MyReservations.this)) {

								new AccountDetails(MyReservations.this).execute();

							} else {
								ConnectivityReceiver.showCustomDialog(MyReservations.this);
							}
							
//							new AccountDetails(MyReservations.this).execute();
						} else if (actionId == Explore.ID_ORDERS) {
							startActivityForResult(new Intent(
									MyReservations.this, MyOrders.class), 1);
						} else if (actionId == Explore.ID_DEALS) {
							startActivityForResult(new Intent(
									MyReservations.this, MyDeals.class), 1);
						} else if (actionId == Explore.ID_REV) {
							startActivityForResult(new Intent(
									MyReservations.this, MyReviews.class), 1);
						} else if (actionId == Explore.ID_REDEMPTION) {
							startActivityForResult(new Intent(
									MyReservations.this,
									RedemptionHistory.class), 1);
						} else if (actionId == Explore.ID_FAV) {
							startActivityForResult(new Intent(
									MyReservations.this, MyFav.class), 1);
						} else if (actionId == Explore.ID_LOGIN) {
							startActivity(new Intent(MyReservations.this,
									Login.class));
							finish();
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

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case PICK_FROM_CAMERA:
			doCrop();

			break;

		case PICK_FROM_FILE:
			mImageCaptureUri = data.getData();

			doCrop();

			break;

		case CROP_FROM_CAMERA:
			Bundle extras = data.getExtras();

			if (extras != null) {
				photo = extras.getParcelable("data");

			}

			File f = new File(mImageCaptureUri.getPath());

			if (f.exists())
				f.delete();

			break;
		}
	}
*/
//	private void doCrop() {
//		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
//
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setType("image/*");
//
//		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
//				intent, 0);
//
//		int size = list.size();
//
//		if (size == 0) {
//			Login.showDialogMsg(MyReservations.this,
//					"Can not find image crop app");
//
//			return;
//		} else {
//			intent.setData(mImageCaptureUri);
//
//			intent.putExtra("outputX", 400);
//			intent.putExtra("outputY", 380);
//			intent.putExtra("aspectX", 200);
//			intent.putExtra("aspectY", 200);
//			intent.putExtra("scale", true);
//			intent.putExtra("return-data", true);
//
//			if (size == 1) {
//				Intent i = new Intent(intent);
//				ResolveInfo res = list.get(0);
//
//				i.setComponent(new ComponentName(res.activityInfo.packageName,
//						res.activityInfo.name));
//
//				startActivityForResult(i, CROP_FROM_CAMERA);
//			} else {
//				for (ResolveInfo res : list) {
//					final CropOption co = new CropOption();
//
//					co.title = getPackageManager().getApplicationLabel(
//							res.activityInfo.applicationInfo);
//					co.icon = getPackageManager().getApplicationIcon(
//							res.activityInfo.applicationInfo);
//					co.appIntent = new Intent(intent);
//
//					co.appIntent
//							.setComponent(new ComponentName(
//									res.activityInfo.packageName,
//									res.activityInfo.name));
//
//					cropOptions.add(co);
//				}
//
//				CropOptionAdapter adapter = new CropOptionAdapter(
//						getApplicationContext(), cropOptions);
//
//				AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				builder.setTitle("Choose Crop App");
//				builder.setAdapter(adapter,
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int item) {
//								startActivityForResult(
//										cropOptions.get(item).appIntent,
//										CROP_FROM_CAMERA);
//
//								// Intent i = new
//								// Intent(TakeSnap.this,FacebookLogin.class);
//								// startActivity(i);
//							}
//						});
//
//				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//					@Override
//					public void onCancel(DialogInterface dialog) {
//
//						if (mImageCaptureUri != null) {
//							getContentResolver().delete(mImageCaptureUri, null,
//									null);
//							mImageCaptureUri = null;
//						}
//					}
//				});
//
//				AlertDialog alert = builder.create();
//
//				alert.show();
//			}
//		}
//	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(), Launcher.class));
	}
}
