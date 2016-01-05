package com.etisbew.eatz.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Sort extends Activity implements OnClickListener{
	
	TextView tvAlbha,tvCancel,tvRating,tvDistence,tvPrice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sortby);
		
		tvAlbha = (TextView)findViewById(R.id.tvAlbha);
		tvCancel= (TextView)findViewById(R.id.tvCancel);
		tvRating= (TextView)findViewById(R.id.tvRating);
		tvDistence = (TextView)findViewById(R.id.tvDistence);
		tvPrice = (TextView)findViewById(R.id.tvPrice);
		
		tvAlbha.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
		tvRating.setOnClickListener(this);
		tvDistence.setOnClickListener(this);
		tvPrice.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.tvAlbha:
			
			setResult(1011);
			finish();
			
			break;
		case R.id.tvCancel:
			
			finish();
			
			break;
		case R.id.tvRating:
			
			setResult(1012);
			finish();
			
			break;
			
		case R.id.tvDistence:
			
			setResult(1013);
			finish();
			
			break;
		case R.id.tvPrice:
			
			setResult(1014);
			finish();
			
			break;

		default:
			break;
		}
		
		
	}
	
	
	
	

}
