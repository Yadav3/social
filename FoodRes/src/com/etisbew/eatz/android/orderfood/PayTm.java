package com.etisbew.eatz.android.orderfood;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.etisbew.eatz.android.Launcher;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

public class PayTm extends Activity {
	// private int randomInt = 0;
	private PaytmPGService Service = null;
	HttpResponse response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("LOG", "onCreate of MainActivity");
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_main);

		// Random randomGenerator = new Random();
		// randomInt = randomGenerator.nextInt(1000);

//		Service = PaytmPGService.getStagingService(); // for testing
		// environment
		 Service = PaytmPGService.getProductionService(); // for production
		// environment

		/*
		 * PaytmMerchant constructor takes two parameters 1) Checksum generation
		 * url 2) Checksum verification url Merchant should replace the below
		 * values with his values
		 */

		PaytmMerchant Merchant = new PaytmMerchant(
				"http://dev.eatz.com/sdkpaytm/generateChecksum.php",
				"http://dev.eatz.com/sdkpaytm/verifyChecksum.php");

		// below parameter map is required to construct PaytmOrder object,
		// Merchant should replace below map values with his own values

		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("REQUEST_TYPE", "DEFAULT");
		paramMap.put("ORDER_ID", Appconstants.strPurchaseId);
		paramMap.put("MID", "eatzco16461972056965");
		if (Appconstants.user_flag == 2) {
			paramMap.put("CUST_ID", Appconstants.userId);
		} else {
			paramMap.put("CUST_ID", PreferenceUtils.getUserId());

		}
		// paramMap.put("CUST_ID", );
		paramMap.put("CHANNEL_ID", "WAP");
		paramMap.put("INDUSTRY_TYPE_ID", "Retail");
		paramMap.put("WEBSITE", "eatzcomapp");
		paramMap.put("TXN_AMOUNT", Appconstants.strFinalAmount);
		// paramMap.put("is_mobile", "1");
		System.out.println("param" + paramMap);

		PaytmOrder Order = new PaytmOrder(paramMap);

		Service.initialize(Order, Merchant, null);
		Service.startPaymentTransaction(this, true, false,
				new PaytmPaymentTransactionCallback() {

					@Override
					public void clientAuthenticationFailed(String arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(),
								"clientAuthenticationFailed", Toast.LENGTH_LONG)
								.show();
					}

					@Override
					public void networkNotAvailable() {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(),
								"networkNotAvailable", Toast.LENGTH_LONG)
								.show();
					}

					@Override
					public void onErrorLoadingWebPage(int arg0, String arg1,
							String arg2) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(),
								"onErrorLoadingWebPage", Toast.LENGTH_LONG)
								.show();
					}

					@Override
					public void onTransactionFailure(String arg1, Bundle arg0) {
						// TODO Auto-generated method stub
						// Toast.makeText(getApplicationContext(),
						// "onTransactionFailure", Toast.LENGTH_LONG)
						// .show();
						// System.out.println("failure"+arg0.toString()+":"+arg1);
						PayTm.this.finish();

						if (arg1.equalsIgnoreCase("Transaction cancelled by user.")) {
							startActivity(new Intent(PayTm.this, Launcher.class));
						} else {
							startActivity(new Intent(PayTm.this,
									OrderFailure.class));
						}
					}

					@Override
					public void onTransactionSuccess(Bundle arg0) {
						// TODO Auto-generated method stub
						// JSONObject js;
						// try {
						// js = new JSONObject(arg0.toString());
						//
						// } catch (JSONException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }
						// Log.e("Srikanth", "Srikanth" + arg0.toString());

						if (Appconstants.Strdatetime.length() > 0) {
							Appconstants.name = "";
							Appconstants.Strdatetime = "";
							Appconstants.orders = "";
							Appconstants.eachidprices = "";
							Appconstants.eachidqty = "";
							Appconstants.address = "";
						}

						// Bundle[{GATEWAYNAME=ICICI, TXNDATE=2015-04-02
						// 14:38:21.0, PAYMENTMODE=CC, STATUS=TXN_SUCCESS,
						// REFUNDAMT=,
						// MID=eatzco16461972056965, TXNTYPE=, ORDERID=3377,
						// CURRENCY=INR, TXNID=102981, TXNAMOUNT=136.26,
						// IS_CHECKSUM_VALID=Y, BANKTXNID=201504022541705,
						// BANKNAME=HDFC Bank, RESPMSG=Txn Successful.,
						// RESPCODE=01,
						// CHECKSUMHASH=m8CE1SkJZcVcVB8tpC3cskppofSvW4Oq2rLFxvK8HMashpMuEkwlkwiqt5w077jyaDpM58iD3wElxuZgKvDm/Qtfs4OxbF1aCSgRJqVsaUs=}]
						JSONObject jsonObj = new JSONObject();
						try {

							jsonObj.put("GATEWAYNAME",
									arg0.getString("GATEWAYNAME"));
							jsonObj.put("TXNDATE", arg0.getString("TXNDATE"));
							jsonObj.put("PAYMENTMODE",
									arg0.getString("PAYMENTMODE"));
							jsonObj.put("STATUS", arg0.getString("STATUS"));
							jsonObj.put("REFUNDAMT",
									arg0.getString("REFUNDAMT"));
							jsonObj.put("MID", arg0.getString("MID"));
							jsonObj.put("TXNTYPE", arg0.getString("TXNTYPE"));
							jsonObj.put("ORDERID", arg0.getString("ORDERID"));
							jsonObj.put("CURRENCY", arg0.getString("CURRENCY"));
							jsonObj.put("TXNID", arg0.getString("TXNID"));
							jsonObj.put("TXNAMOUNT",
									arg0.getString("TXNAMOUNT"));
							jsonObj.put("IS_CHECKSUM_VALID",
									arg0.getString("IS_CHECKSUM_VALID"));
							jsonObj.put("BANKTXNID",
									arg0.getString("BANKTXNID"));
							jsonObj.put("BANKNAME", arg0.getString("BANKNAME"));
							jsonObj.put("RESPMSG", arg0.getString("RESPMSG"));
							jsonObj.put("RESPCODE", arg0.getString("RESPCODE"));
							jsonObj.put("CHECKSUMHASH",
									arg0.getString("CHECKSUMHASH"));

						} catch (Exception e) {

						}

						System.out.println("values are"
								+ arg0.getString("GATEWAYNAME"));
						// String str=arg0.toString();
						// System.out.println("Hi");
						// Toast.makeText(getApplicationContext(), "Success",
						// Toast.LENGTH_LONG).show();
						// moveTaskToBack(true);
						String type = jsonObj.toString();

						System.out.println("json" + type);
						// new PostingData(arg0.toString()).execute();
						if (ConnectivityReceiver
								.checkInternetConnection(PayTm.this)) {

							new PostingData(type).execute();

						} else {
							ConnectivityReceiver.showCustomDialog(PayTm.this);
						}
					}

					@Override
					public void someUIErrorOccurred(String arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(),
								"someUIErrorOccurred", Toast.LENGTH_LONG)
								.show();
					}

				});
	}

	private class PostingData extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;

		String strresponse, re;

		public PostingData(String strbundle) {
			// TODO Auto-generated constructor stub
			strresponse = strbundle;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(PayTm.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance

			try {

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(Appconstants.PURCHASED_ITEMS_HOST
						+ "ordersuccess/");

				List<NameValuePair> params1 = new ArrayList<NameValuePair>();

				params1.add(new BasicNameValuePair("response", strresponse));
				params1.add(new BasicNameValuePair("is_mobile", "1"));
				post.setEntity(new UrlEncodedFormEntity(params1));
				response = client.execute(post);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();

					try {
						re = EntityUtils.toString(entity, HTTP.UTF_8);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					System.out.println("respone" + re);

				}

			} catch (Exception e) {
				Log.e("exception in doinbackground", "" + e.getMessage());
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (pDialog.isShowing())
				pDialog.dismiss();

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				System.out.println("respone" + re);

				String orderid = null;
				try {
					JSONObject json = new JSONObject(re);
					orderid = json.getString("orderid");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("order id" + orderid);
				if (!TextUtils.isEmpty(orderid)) {
					if (orderid.length() > 0) {
						PayTm.this.finish();
						startActivity(new Intent(PayTm.this, OrderConfirm.class));
					}
				}

			}
		}
	}
}
