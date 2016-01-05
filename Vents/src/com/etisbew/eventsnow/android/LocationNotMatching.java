package com.etisbew.eventsnow.android;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etisbew.eventsnow.android.beans.LocationsBean;

public class LocationNotMatching extends Fragment{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	TextView back_home_button;
	EventBean event;
	String Locations[];
	Integer Location_id[];
	int pos;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View home = inflater.inflate(R.layout.location_not_matching, container, false);
		back_home_button = (TextView) home.findViewById(R.id.back_home_button); 
 		event = (EventBean) getActivity().getApplicationContext();
 		back_home_button.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				Intent intent = new Intent(getActivity(), MainActivity.class);
//				intent.putExtra("open", "open");
//				
				/*Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(),
						R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
				getActivity().startActivity(intent, bundle);*/
				/*if (Build.VERSION.SDK_INT > 11) {
					Bundle  bundle = ActivityOptions.makeCustomAnimation(getActivity(),
						R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
				getActivity().startActivity(intent, bundle);
				}else{ 
					startActivity(intent);
				} */
				try{
					ArrayList<LocationsBean> locations = event.getLocations();
					Locations = new String[locations.size()];
					Location_id = new Integer[locations.size()];
				System.out.println("locations"+locations.size()); 
					for (int i = 0; i < locations.size(); i++) {
						final LocationsBean lb = locations.get(i);
		
					

								Locations[i]=lb.locationName;
								Location_id[i]=lb.locationId;
								if (event.getLocationId() == lb.locationId) {
									pos=i;
								} else {
									
								}
						}

					
					}catch(Exception e){
						
					}
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setCancelable(true).setSingleChoiceItems(
						Locations, pos,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog1, int pos) {
								// TODO Auto-generated method stub
								
								event.setCity_status(0);
								event.setLocationId(Location_id[pos]);
								Intent intent = new Intent(getActivity(), SplashActivity.class);
								if (Build.VERSION.SDK_INT > 11) {
									Bundle  bundle = ActivityOptions.makeCustomAnimation(getActivity(),
										R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
								getActivity().startActivity(intent, bundle);
								}else{ 
									startActivity(intent);
								}
							}
						});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});
 	
	return home;
}

@Override
public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
}

@Override
public void onAttach(Activity activity) {
	super.onAttach(activity);
}

@Override
public void onStart() {
	super.onStart();
}

@Override
public void onResume() {
	super.onResume();
}
}
