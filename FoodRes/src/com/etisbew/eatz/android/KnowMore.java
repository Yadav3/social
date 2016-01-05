package com.etisbew.eatz.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;

public class KnowMore extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		RelativeLayout ordernow;
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.know_more);
		ordernow = (RelativeLayout) findViewById(R.id.bottom_layout);
		ordernow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Appconstants.strMenuflag = "FastLane";
				Appconstants.filters_flg="2";
//				String s5 = "";
				try {
					Launcher.userLocation = Appconstants.ltDo.name;
					Localsecrets.str_titles = "FastLane Restaurants";

//					if (Appconstants.ltDo.id.length() > 0) {
//						s5 = Appconstants.ltDo.id;
//					} else {
//						s5 = "null"; 
//	 
//					} 
					Localsecrets.flg = 2;
					Explore.type=3;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/3/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";

				} catch (NullPointerException e) {
					Localsecrets.flg = 2;
					Explore.type=3;
					Explore.id1=2;
					Launcher.getRestaurent = Appconstants.MAIN_HOST
							+ "restaurantslisting/3/" + Appconstants.strCityId +"/"
							+ Appconstants.LATTITUDE + "/" + Appconstants.LANGITUDE+"/1";

				} 

				Explore.search_string="";
				if (ConnectivityReceiver.checkInternetConnection(KnowMore.this)) {
				new RestaurantsList(KnowMore.this, Launcher.getRestaurent).execute();
				} else {
					ConnectivityReceiver.showCustomDialog(KnowMore.this);
				}
			}
		});
	}
}
