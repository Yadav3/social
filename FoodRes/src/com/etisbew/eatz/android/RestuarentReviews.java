package com.etisbew.eatz.android;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.common.ConnectivityReceiver;
import com.etisbew.eatz.common.PreferenceUtils;
import com.etisbew.eatz.objects.reviewDO;
import com.etisbew.eatz.web.WebServiceCalls;

public class RestuarentReviews extends BaseActivity{
	ListView lvResults;
	private SearchAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.restaurent_reviews);
		
		ImageView ivBack = (ImageView) findViewById(R.id.back);

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setResult(1012);
				finish();

			}
		});
		
		
		ImageView writeReview = (ImageView) findViewById(R.id.writeReview);

		writeReview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (PreferenceUtils.getUserSession().equalsIgnoreCase("none")) {
					if (Appconstants.user_flag == 2) {
						SearchDetails.getWriteReview(v.getContext());
					} else {
						startActivity(new Intent(v.getContext(), Login.class));
					}
				} else {
					SearchDetails.getWriteReview(v.getContext());
				}
				
			}
		});
		
		TextView etFind = (TextView) findViewById(R.id.etFind);
//		etFind.setText(SearchDetails.restDO.name+"-Ratings & Reviews ("+SearchDetails.restDO.review_count+")");
		etFind.setText(SearchDetails.str_restaurant_name+"-Ratings & Reviews ("+SearchDetails.str_reviews_count+")");

		
		lvResults = (ListView) findViewById(R.id.listView1);
		adapter = new SearchAdapter(RestuarentReviews.this, null);
		lvResults.setAdapter(adapter);
		
		
		if (ConnectivityReceiver.checkInternetConnection(RestuarentReviews.this)) {

			new DisplayReviews().execute();

		} else {
			ConnectivityReceiver.showCustomDialog(RestuarentReviews.this);
		}
		
		lvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				startActivity(new Intent(view.getContext(), ReviewDetails.class).putExtra("review", (reviewDO)view.getTag()));
				
			}
		});
		
		
		
	}
	
	public class SearchAdapter extends BaseAdapter {

		private Context mContext;
		ArrayList<reviewDO> array;

		public SearchAdapter(Context context, ArrayList<reviewDO> array) {
			super();
			mContext = context;
			this.array = array;

		}

		@Override
		public int getCount() {
			// return the number of records in cursor
			if (array == null)
				return 0;
			return array.size();
		}

		// getView method is called for each item of ListView
		@Override
		@SuppressLint({ "ViewHolder", "InflateParams" })
		public View getView(final int i, View view, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.review_row, null);
			view.setTag(array.get(i));
			
			TextView tvName = (TextView) view.findViewById(R.id.tvName);
			RatingBar rating = (RatingBar) view
					.findViewById(R.id.rtReviewRating);
			TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
			TextView tvDisc = (TextView) view.findViewById(R.id.tvDisc);

			tvName.setText(array.get(i).firstname + " "
					+ array.get(i).lastname);
			tvTime.setText(array.get(i).datecreated);
			tvDisc.setText(array.get(i).comment);

			int rates = Integer.parseInt(array.get(i).rating);
			rating.setRating(rates);
			
			
			return view;

		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void refresh(ArrayList<reviewDO> array) {

			this.array = array;
			this.notifyDataSetChanged();
		}
	}
	
	private ArrayList<reviewDO> reviews = null;

	private class DisplayReviews extends AsyncTask<String, ArrayList<reviewDO>, ArrayList<reviewDO>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoader();
		}

		@Override
		protected ArrayList<reviewDO> doInBackground(String... params) {

			reviews = new ArrayList<reviewDO>();

			String result = WebServiceCalls.getJSONString(Appconstants.MAIN_HOST + "restaurantReviews/"+Explore.strVenueId );//SearchDetails.restDO.id

			if (result == null) {
				
			} else {

				JSONObject mainJson;
				try {
					mainJson = new JSONObject(result);
					if (result.contains("errorMessage")) {
						String strErrorMsg = mainJson.optString("errorMessage");
						showDialogMsg(RestuarentReviews.this, strErrorMsg);
					} else {
						JSONArray reviewsArray = mainJson.getJSONArray("Reviews");
						for (int i = 0; i < reviewsArray.length(); i++) {
							
							reviewDO reviewsObj = new reviewDO();
							JSONObject joObj = reviewsArray.getJSONObject(i);
							
							reviewsObj.id = joObj.getString("id");
							if (!joObj.isNull("title")) {
								reviewsObj.title = joObj.getString("title");
							}
							if (!joObj.isNull("comment")) {
								reviewsObj.comment = joObj.getString("comment");
							}
							if (!joObj.isNull("rating")) {
								reviewsObj.rating = joObj.getString("rating").replace("null", "0");
							}
							if (!joObj.isNull("datecreated")) {
								reviewsObj.datecreated = joObj.getString("datecreated");
							}
							if (!joObj.isNull("firstname")) {
								reviewsObj.firstname = joObj.getString("firstname");
							}
							if (!joObj.isNull("lastname")) {
								reviewsObj.lastname = joObj.getString("lastname");
							}
							
							reviews.add(reviewsObj);
						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			return reviews;

		}

		@Override
		protected void onPostExecute(ArrayList<reviewDO> result) {
			super.onPostExecute(result);
			hideLoader();

			adapter.refresh(result);

		}

	}

}
