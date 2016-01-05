package com.example.pagination;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Page2Fragment extends Fragment {

	ImageButton btnWrite, youtube, wordpress, yahoo, service, news_and_events,
			team;
	public String ptext = "..PAGE 2..";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// fragment not when container null
		if (container == null) {
			return null;
		}
		// inflate view from layout
		View view = (LinearLayout) inflater.inflate(R.layout.page2, container,
				false);

		youtube = (ImageButton) view.findViewById(R.id.youtube);
		wordpress = (ImageButton) view.findViewById(R.id.wordpress);
		yahoo = (ImageButton) view.findViewById(R.id.yahoo);
		service = (ImageButton) view.findViewById(R.id.rss);
		news_and_events = (ImageButton) view.findViewById(R.id.technorati);
		team = (ImageButton) view.findViewById(R.id.twitter);

		youtube.setOnClickListener(onClickListener);
		wordpress.setOnClickListener(onClickListener);
		yahoo.setOnClickListener(onClickListener);
		service.setOnClickListener(onClickListener);
		news_and_events.setOnClickListener(onClickListener);
		team.setOnClickListener(onClickListener);

		return view;
	}

	// set text helper function
	public void setText(String item) {

		// TextView view = (TextView) getView().findViewById(R.id.tvText2);
		// view.setVisibility(view.INVISIBLE);
		// view.setText(item);
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.youtube:
				Toast.makeText(getActivity(), "Youtube", Toast.LENGTH_LONG)
						.show();
				break;
			case R.id.wordpress:
				Toast.makeText(getActivity(), "Wordpress", Toast.LENGTH_LONG)
						.show();
				break;
			case R.id.yahoo:
				Toast.makeText(getActivity(), "Yahoo", Toast.LENGTH_LONG)
						.show();
				break;
			case R.id.rss:
				Toast.makeText(getActivity(), "Rss", Toast.LENGTH_LONG).show();
				break;
			case R.id.technorati:
				Toast.makeText(getActivity(), "Technorati", Toast.LENGTH_LONG)
						.show();
				break;
			case R.id.twitter:
				Toast.makeText(getActivity(), "Twitter", Toast.LENGTH_LONG)
						.show();
				break;
			}

		}
	};

}