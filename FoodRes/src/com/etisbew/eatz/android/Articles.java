package com.etisbew.eatz.android;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.controlls.ImageLoader;
import com.etisbew.eatz.web.WebServiceCalls;
import com.squareup.picasso.Picasso;

public class Articles extends Activity {

	ListviewListAdapter adapter;
	public ListView list;

	ArrayList<HashMap<String, String>> arraylist; 
	static String TITLE = "title";
	static String DESCRIPTION = "description";
	static String IMAGE = "thumb_image";     
	static String ARTICLEID = "articleid"; 
   
	@Override   
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.articles); 

		TextView etFind = (TextView) findViewById(R.id.etFind);

		ImageView ivBack = (ImageView) findViewById(R.id.back);

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		}); 
 
		etFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(v.getContext(), Search.class));
			}
		});

		list = (ListView) findViewById(R.id.articlelistview);
		
		
		
		if (ConnectivityReceiver.checkInternetConnection(Articles.this)) {

			new getArticleTask().execute();

		} else {
			ConnectivityReceiver.showCustomDialog(Articles.this);
		}
		

		list.setAdapter(adapter);
	}
	

	class getArticleTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;
		String strarticlesId1, jsonString;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Articles.this);
			pDialog.setMessage("Loading ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			return WebServiceCalls.getJSONString(Appconstants.MAIN_HOST
					+ "articles");
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
				ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
				JSONArray categories = mainObject.getJSONArray("Articles");
				for (int i = 0; i < categories.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					mainObject = categories.getJSONObject(i);

					map.put("title", mainObject.getString("title"));
					map.put("description", mainObject.getString("description"));
					map.put("thumb_image", mainObject.getString("thumb_image"));
					map.put("articleid", mainObject.getString("articleid"));

					arraylist.add(map);

				}

				adapter = new ListviewListAdapter(Articles.this, arraylist);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.setAdapter(adapter);

		}
	}

	class ListviewListAdapter extends BaseAdapter {

		Context context;
		LayoutInflater inflater;
		ArrayList<HashMap<String, String>> data;
		ImageLoader imageLoader;
		HashMap<String, String> resultp = new HashMap<String, String>();

		public ListviewListAdapter(Context context,
				ArrayList<HashMap<String, String>> arraylist) {
			// TODO Auto-generated constructor stub
			this.context = context;
			data = arraylist;
			imageLoader = new ImageLoader(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;

			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.article_row, null);
			}

			// TODO Auto-generated method stub
			TextView tvtitle, tvDescription;
			ImageView image;

			// Get the position
			resultp = data.get(position);

			// Locate the TextViews in listview_item.xml
			tvtitle = (TextView) view.findViewById(R.id.tvItemName);
			tvDescription = (TextView) view.findViewById(R.id.tvTitle);

			// Locate the ImageView in listview_item.xml
			image = (ImageView) view.findViewById(R.id.ivItemPhoto);

			// Capture position and set results to the TextViews
			tvtitle.setText(Html.fromHtml(resultp.get(Articles.TITLE)));
			tvDescription.setText(Html.fromHtml(resultp
					.get(Articles.DESCRIPTION)));
			Picasso.with(Articles.this)
					.load(Appconstants.ARTICLE_IMG_BASE_URL
							+ resultp.get(Articles.IMAGE).replace(" ", "%20"))
					.into(image);

			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					// Get the position
					resultp = data.get(position);
					ARTICLEID = resultp.get(Articles.ARTICLEID);
					startActivity(new Intent(Articles.this,
							SingleArticleItemView.class));
					// intent.putExtra("thumb_image",
					// resultp.get(Articles.IMAGE));
					// intent.putExtra("articleid",
					// resultp.get(Articles.ARTICLEID));
					// Start SingleItemView Class
					// startActivity(intent);

				}
			});

			return view;
		}

	}

}
