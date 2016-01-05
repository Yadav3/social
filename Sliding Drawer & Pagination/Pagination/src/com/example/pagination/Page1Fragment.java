package com.example.pagination;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Page1Fragment extends Fragment {

	ImageButton blogger, digg, facebook, flicker,
			google, fastfm, linkedin, live, orkut;

	// Button btnWrite;
	public String ptext = "..PAGE 1..";

	// activity listener interface
	@SuppressWarnings("unused")
	private OnPageListener pageListener;

	public interface OnPageListener {
		public void onPage1(String s);
	}

	// onAttach : set activity listener
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// if implemented by activity, set listener
		if (activity instanceof OnPageListener) {
			pageListener = (OnPageListener) activity;
		}
		// else create local listener (code never executed in this example)
		else
			pageListener = new OnPageListener() {
				@Override
				public void onPage1(String s) {
					Log.d("PAG1", "Button event from page 1 : " + s);
				}
			};
	}

	// onCreateView :
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// fragment not when container null
		if (container == null) {
			return null;
		}
		// inflate view from layout
		View view = (LinearLayout) inflater.inflate(R.layout.page1, container,false);
		// get button reference
		
		blogger = (ImageButton) view.findViewById(R.id.blogger);
		digg = (ImageButton) view.findViewById(R.id.digg);
		facebook = (ImageButton) view.findViewById(R.id.facebook);
		flicker = (ImageButton) view.findViewById(R.id.flicker);
		google = (ImageButton) view.findViewById(R.id.google);
		fastfm = (ImageButton) view.findViewById(R.id.lastfm);
		linkedin = (ImageButton) view.findViewById(R.id.linkedin);
		live = (ImageButton) view.findViewById(R.id.live);
		orkut = (ImageButton) view.findViewById(R.id.orkut);

		blogger.setOnClickListener(onClickListener);
		digg.setOnClickListener(onClickListener);
		facebook.setOnClickListener(onClickListener);
		flicker.setOnClickListener(onClickListener);
		google.setOnClickListener(onClickListener);
		fastfm.setOnClickListener(onClickListener);
		linkedin.setOnClickListener(onClickListener);
		live.setOnClickListener(onClickListener);
		orkut.setOnClickListener(onClickListener);

		return view;
	}

	// set text helper function
	public void setText(String item) {
		// TextView view = (TextView) getView().findViewById(R.id.tvText1);
		// view.setText(item);
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.blogger:
				Toast.makeText(getActivity(), "Blogger", Toast.LENGTH_LONG).show();
				break;
			case R.id.digg:
				Toast.makeText(getActivity(), "Digg", Toast.LENGTH_LONG).show();
				break;
			case R.id.facebook:
				Toast.makeText(getActivity(), "Facebook", Toast.LENGTH_LONG).show();
				break;
				
			case R.id.flicker:
				Toast.makeText(getActivity(), "Flicker", Toast.LENGTH_LONG).show();
				break;
			case R.id.google:
				Toast.makeText(getActivity(), "Google", Toast.LENGTH_LONG).show();
				break;
			case R.id.lastfm:
				Toast.makeText(getActivity(), "Lastfm", Toast.LENGTH_LONG).show();
				break;
				
			case R.id.linkedin:
				Toast.makeText(getActivity(), "Linkedin", Toast.LENGTH_LONG).show();
				break;
			case R.id.live:
				Toast.makeText(getActivity(), "Live", Toast.LENGTH_LONG).show();
				break;
			case R.id.orkut:
				Toast.makeText(getActivity(), "Orkut", Toast.LENGTH_LONG).show();
				break;
			}

		}
	};

}