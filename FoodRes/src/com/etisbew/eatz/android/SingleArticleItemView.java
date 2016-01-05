package com.etisbew.eatz.android;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.imageloader.ImageLoader;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

public class SingleArticleItemView extends Activity {

//	String thumb_image, strArticleId;
	String position;
	ImageLoader imageLoader = new ImageLoader(this);

	TextView tvtitle, tvprevious, tvnext, tvshort_headline, tvdescription;
	String strPrev, strnext;

//	ArrayList<HashMap<String, String>> data;
//	HashMap<String, String> resultp = new HashMap<String, String>();
//	String TITLE = "title";
//	String PREVIOUS = "previous";
//	String NEXT = "next";
//	String HEADLINE = "short_headline";
//	String DESCRIPTION = "description";
	View viewLine;
	ImageView imgMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.article_magazines);

		imgMain = (ImageView) findViewById(R.id.ivarticleimage);
//		Intent i = getIntent();

//		thumb_image = i.getStringExtra("thumb_image");
//		strArticleId = i.getStringExtra("articleid");

		tvtitle = (TextView) findViewById(R.id.tvArticleTitle);
		tvprevious = (TextView) findViewById(R.id.tvPrevArticle);
		tvnext = (TextView) findViewById(R.id.tvNextArticle);
		tvshort_headline = (TextView) findViewById(R.id.tvimagedate);
		tvdescription = (TextView) findViewById(R.id.tvdiscription);
		
		viewLine = findViewById(R.id.viewLine);

		tvprevious.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Articles.ARTICLEID = strPrev;
//				new getArticleTask1().execute();
				if (ConnectivityReceiver.checkInternetConnection(SingleArticleItemView.this)) {

					new getArticleTask1().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(SingleArticleItemView.this);
				}
			}
		});
		tvnext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Articles.ARTICLEID = strnext;
//				new getArticleTask1().execute();
				if (ConnectivityReceiver.checkInternetConnection(SingleArticleItemView.this)) {

					new getArticleTask1().execute();

				} else {
					ConnectivityReceiver.showCustomDialog(SingleArticleItemView.this);
				}
			}
		});

		ImageView imgback = (ImageView) findViewById(R.id.back);
		imgback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SingleArticleItemView.this.finish();
			}
		});
//		new getArticleTask1().execute();
		if (ConnectivityReceiver.checkInternetConnection(SingleArticleItemView.this)) {

			new getArticleTask1().execute();

		} else {
			ConnectivityReceiver.showCustomDialog(SingleArticleItemView.this);
		}
	}

	class getArticleTask1 extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		String strarticlesId1, jsonString;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(SingleArticleItemView.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "articles/" + Articles.ARTICLEID);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}
			if (result == null || "".equals(result)) {
				return;
			}

			try {
				JSONObject mainObject = new JSONObject(result);
				JSONArray categories = mainObject.getJSONArray("Articles");
				// for (int i = 0; i < categories.length(); i++) {
				mainObject = categories.getJSONObject(0);

				tvtitle.setText(Html.fromHtml(mainObject.getString("title")));
				if (!mainObject.getString("previous").equalsIgnoreCase("null")) {
					tvprevious.setVisibility(View.VISIBLE);
					viewLine.setVisibility(View.VISIBLE);
					strPrev = mainObject.getString("previous");
				} else {
					viewLine.setVisibility(View.GONE);
					tvprevious.setVisibility(View.GONE);
				}
				if (!mainObject.getString("next").equalsIgnoreCase("null")) {
					tvnext.setVisibility(View.VISIBLE);
					strnext = mainObject.getString("next");
				} else {
					viewLine.setVisibility(View.GONE);
					tvnext.setVisibility(View.GONE);
				}
				// tvprevious.setText(Html.fromHtml(mainObject
				// .getString("previous")));
				// tvnext.setText(Html.fromHtml(mainObject.getString("next")));
				tvshort_headline.setText(Html.fromHtml(mainObject
						.getString("short_headline")));
				tvdescription.setText(Html.fromHtml(mainObject
						.getString("description")));
				
				
				Picasso.with(SingleArticleItemView.this)
				.load(Appconstants.ARTICLE_IMG_BASE_URL
						+ mainObject.getString("thumb_image").replace(" ", "%20")).into(imgMain);
				// }

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
