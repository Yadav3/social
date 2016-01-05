package com.etisbew.eatz.android.orderfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etisbew.eatz.android.BaseActivity;
import com.etisbew.eatz.android.Launcher;
import com.etisbew.eatz.android.R;
import com.etisbew.eatz.common.Appconstants;
 
public class OrderFailure extends BaseActivity{
	
	RelativeLayout rltop;
	TextView txtErrorMsg,tv_checkStatus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.paytminvalid);
		
		rltop=(RelativeLayout)findViewById(R.id.RLTop);
		tv_checkStatus=(TextView)findViewById(R.id.tv_checkStatus);
		txtErrorMsg=(TextView)findViewById(R.id.txtErrorMsg);
		
		rltop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(OrderFailure.this,Launcher.class));
			}
		});
		tv_checkStatus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startActivity(new Intent(OrderFailure.this,PayTm.class));
				
					OrderFailure.this.finish();			
				Intent in = new Intent(OrderFailure.this,OrderDetails_2.class);
				in.putExtra("name", Appconstants.name);
				in.putExtra("Strdatetime", Appconstants.Strdatetime);
				// secondIntent.putExtra("name1", name1);
				// secondIntent.putExtra("name2", name2);
				in.putExtra("orders", Appconstants.orders);
				in.putExtra("eachidprices", Appconstants.eachidqty);
				in.putExtra("eachidqty", Appconstants.eachidprices);
				startActivity(in);
				
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
