package com.etisbew.eatz.android.orderfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eatz.android.Launcher;
import com.etisbew.eatz.android.Localsecrets;
import com.etisbew.eatz.android.Login;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.android.SignUp;
import com.etisbew.eatz.android.dropdownlist.MyDeals;
import com.etisbew.eatz.android.dropdownlist.MyOrders;
import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.common.WebViewLoader;

public class OrderConfirm extends Activity {

	TextView tv_backtohome, mailrec, note, tv_checkStatus, txtThanks,message,tv_login;

	String text = "<font color=#000000>Confirmation has been emailed to </font> <font color=#1979bd>"
			+ PreferenceUtils.getUserEmail1() + "</font>";
	String text1 = "<font color=#FF0040>Note:</font> <font color=#000000> Pay for your order when you once delivered.</font>";
	RelativeLayout know_more_earn_poings;
	String text2 = "<font color=#FF0040>Note:</font> <font color=#000000> Pay for your order when you pick it up.</font>";
	
	WebView webivew_points,webivew_points1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_confirm);

		txtThanks = (TextView) findViewById(R.id.tv_thanks);

		tv_backtohome = (TextView) findViewById(R.id.tv_backtohome);

		tv_checkStatus = (TextView) findViewById(R.id.tv_checkStatus); 
		
		webivew_points = (WebView) findViewById(R.id.webivew_points);
		webivew_points1 = (WebView) findViewById(R.id.webivew_points1);
				
		message = (TextView) findViewById(R.id.message);
		tv_login = (TextView) findViewById(R.id.tv_login);  
		 
		 
		/*String str = "<html><body><p>Your order number is <b><font color=\"black\"> " 
		+Appconstants.strPurchaseId+"</font></b></br>"
		+"<p>We have sent you the order details to your registered mobile  <b><font color=\"black\"> "
		+OrderDetails_2.phone +"</font></b> &amp; also to your email <b><font color=\"black\"> " + 
				OrderDetails_2.email +"</font></b>.</br>"
					+"</body></html>"; */
		String str = "<html><body><p>Your order number is <b><font color=\"black\"> " 
				+Appconstants.strPurchaseId+"</font></b></br>";
				if(Appconstants.user_flag ==2){
					str=str+"<p>We have sent you the order details to your mobile  <b><font color=\"black\"> ";
				}else{
					str=str+"<p>We have sent you the order details to your registered mobile  <b><font color=\"black\"> ";
				}
				
				
				str=str+OrderDetails_2.phone +"</font></b> &amp; also to your email <b><font color=\"black\"> " + 
						OrderDetails_2.email +"</font></b>.</br>"
							+"</body></html>"; 
		
				 
		/*String str1 = "<html><body><p>If you have questions about this order contact us @ <b><font color=\"black\"> "
				+OrderDetails_2.eatznumber +"</b>  or write to us at  <b><font color=\"black\"> " 
				+OrderDetails_2.eatz_email +"</font></b></br>"
				+"<p>Your order for Rs<b><font color=\"black\"> " 
				+OrderDetails_2.total +"/-</b> earns you <b><font color=\"black\"> " 
				+OrderDetails_2.points +"</font></b> Eatz rewards points. Earning points is easy and simple on Eatz, as soon as you have" +
						"  <b>2000</b> points you get a voucher for free meal at your favourite restaurant.</br>"
						
				+"</body></html>"; */
		String str1 = "<html><body><p>If you have any queries or clarifications please contact us at <b><font color=\"black\"> "
				+OrderDetails_2.eatznumber +"</b> or email us at <b><font color=\"black\"> " 
				+OrderDetails_2.eatz_email +"</font></b></br>"
				+"<p>Your order for Rs<b><font color=\"black\"> " 
				+OrderDetails_2.total +"/-</b> earns you <b><font color=\"black\"> " 
				+OrderDetails_2.points +"</font></b> Eatz rewards points. Earning points is easy and simple on Eatz, as soon as you have" +
						"  <b>2000</b> points you get a voucher for free meal at your favourite restaurant.</br>"
						
				+"</body></html>"; 
	 
		 
		know_more_earn_poings = (RelativeLayout) findViewById(R.id.know_more_earn_poings);
		
		know_more_earn_poings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(OrderConfirm.this, RewardPoints.class));
				OrderConfirm.this.finish();
				
			}
		}); 

		mailrec = (TextView) findViewById(R.id.tv_mailrecieve);
		mailrec.setText(Html.fromHtml(text));

		note = (TextView) findViewById(R.id.tv_note);
		if (Appconstants.strPurchaseSubUrl
				.contains("torders/mobileapp/")) {
		if (Localsecrets.flg == 1) {
		if (OrderDetails_2.selectWay.equalsIgnoreCase("1")) {
		note.setText(Html.fromHtml(text1));
		}else{ 
			note.setVisibility(View.GONE);
		}
		} else if(Localsecrets.flg == 0){
			if (OrderDetails_2.selectWay.equalsIgnoreCase("1")) {
				note.setText(Html.fromHtml(text2));
				}else{
					note.setVisibility(View.GONE);
				}
		} else if(Localsecrets.flg == 2){
			note.setVisibility(View.GONE);
		} 
		}
		View view1 = findViewById(R.id.view1); 

		if (Appconstants.strPurchaseSubUrl.contains("deals/mobileapp/")) {
			txtThanks.setText("Thank you for purchasing deal");
			
				tv_checkStatus.setText("View deal details");
			
			
			note.setVisibility(View.GONE);
			view1.setVisibility(View.GONE);
			know_more_earn_poings.setVisibility(View.GONE);
		} else if (Appconstants.strPurchaseSubUrl
				.contains("torders/mobileapp/")) {
			webivew_points.loadDataWithBaseURL("file:///android_asset/",str.toString(), "text/html", "UTF-8", "");
			webivew_points1.loadDataWithBaseURL("file:///android_asset/",str1.toString(), "text/html", "UTF-8", "");
			txtThanks.setText("Thank you for placing order");
			if(Appconstants.user_flag ==2){
				tv_checkStatus.setText("View Invoice");
				know_more_earn_poings.setVisibility(View.GONE);
			}else{
				tv_checkStatus.setText("View order details");
				know_more_earn_poings.setVisibility(View.VISIBLE);
			}
			view1.setVisibility(View.VISIBLE);
			//note.setVisibility(View.VISIBLE);
		}
		tv_login.setOnClickListener(new View.OnClickListener() {
			
			@Override 
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OrderConfirm.this.finish();
				Intent in = new Intent(OrderConfirm.this,Login.class);
				if(Appconstants.user_flag ==2){
					Appconstants.user_flag=1;
					Appconstants.sessionId="";
					//PreferenceUtils.setUserSession(strSession);

					
					Appconstants.userId="";
					//PreferenceUtils.setUserId(strUserId);

				
					Appconstants.firstname="";  
					
					Appconstants.phone=""; 
					Appconstants.email_="";
					
				}
				Appconstants.user_flag=1;
				if(tv_login.getText().toString().equalsIgnoreCase("Login")){
				in.putExtra("guest_flag", "1");
				startActivity(in);
				}else{
					startActivity(new Intent(OrderConfirm.this,SignUp.class));	
				}
			} 
		});
		if(Appconstants.user_flag ==2){
			if (Appconstants.strPurchaseSubUrl.contains("torders/mobileapp/")) {
			if(OrderDetails_2.userstatus ==1){
			message.setText("Already your email address is registered with us. Login to check your order status.");
			tv_login.setText("Login");
			}else if(OrderDetails_2.userstatus ==0){ 
				message.setText("Only registered users are eligible for eatz rewards points.");
				tv_login.setText("Register me");
			}
			}else if(Appconstants.strPurchaseSubUrl
				.contains("deals/mobileapp/")){
				message.setVisibility(View.GONE);
				tv_login.setVisibility(View.GONE);
				/*if(OrderDetails_2.userstatus ==1){
					message.setText("Already your email address is registered with us. Login to view your Deal voucher.");
					tv_login.setText("Login");
					}else if(OrderDetails_2.userstatus ==0){ 
						message.setText("Only registered users are eligible for eatz rewards points.");
						tv_login.setText("Register ");
					}*/
			}
		}else{
			message.setVisibility(View.GONE);
			tv_login.setVisibility(View.GONE);
			
		}
		

		tv_backtohome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),
						Launcher.class));
			}
		});

		ImageView imgBack = (ImageView) findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				startActivity(new Intent(getApplicationContext(),
						Launcher.class));
			}
		});

		tv_checkStatus.setOnClickListener(new OnClickListener() { 

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (Appconstants.strPurchaseSubUrl.contains("deals/mobileapp/")) {
					if(Appconstants.user_flag ==2){
						startActivity(new Intent(getApplicationContext(),
								WebViewLoader.class));
						}else{
					startActivity(new Intent(getApplicationContext(),
							MyDeals.class));
						}
				} else if (Appconstants.strPurchaseSubUrl 
						.contains("torders/mobileapp/")) { 
					if(Appconstants.user_flag ==2){
						startActivity(new Intent(getApplicationContext(),
								WebViewLoader.class));
						}else{
							startActivity(new Intent(getApplicationContext(),
									MyOrders.class));
						} 
					
				}
			}
		});
	}
	@Override
	public void onBackPressed() {
		finish(); 

		startActivity(new Intent(getApplicationContext(),
				Launcher.class));
	}	
}
