package com.etisbew.eatz.android.orderfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.etisbew.eatz.android.R;

public class RewardPoints extends Activity{ 
	RelativeLayout top_layout,got_it;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reward_points);
		
		top_layout = (RelativeLayout) findViewById(R.id.top_layout);
		got_it = (RelativeLayout) findViewById(R.id.got_it);
		top_layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RewardPoints.this, OrderConfirm.class));
				RewardPoints.this.finish();
			}
		});
		got_it.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RewardPoints.this, OrderConfirm.class));
				RewardPoints.this.finish();
			}
		});
		
		
	}
	
@Override
public void onResume(){
	super.onResume();
}
}
