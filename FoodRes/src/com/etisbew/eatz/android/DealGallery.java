package com.etisbew.eatz.android;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DealGallery extends BaseActivity {
	ViewPager viewPager;
	ViewPagerAdapterSS adapter;
	TextView previes_btn, next_btn, t1, t2, txt_done, txtTitle;
	TouchImageView imgflag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu_details);
		viewPager = (ViewPager) findViewById(R.id.galleryViewPager);
		previes_btn = (TextView) findViewById(R.id.previous);
		next_btn = (TextView) findViewById(R.id.next);
		t1 = (TextView) findViewById(R.id.firstTxt);
		t2 = (TextView) findViewById(R.id.thirdTxt);

		adapter = new ViewPagerAdapterSS(DealGallery.this);
		viewPager.setAdapter(adapter);
		// viewPager.setCurrentItem(0);

		t1.setText("1");
		t2.setText("" +  DealsDetails.images_path.size());
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

				// Setting the title, image and image position
				if (arg0 == DealsDetails.images_path.size() - 1 || arg0 == 0) {

					// txtTitle.setText("" +
					// Multimedia.sub_media_Titles.get(arg0));

					arg0++;
					t1.setText("" + arg0);

				} else {
					arg0 = viewPager.getCurrentItem();

					// txtTitle.setText("" +
					// Multimedia.sub_media_Titles.get(arg0));

					arg0++;
					t1.setText("" + arg0);
				}

				if (viewPager.getCurrentItem() == 0) {

					previes_btn.setCompoundDrawablesWithIntrinsicBounds(null,
							null, getApplicationContext().getResources()
									.getDrawable(R.drawable.back_arrow_dis),
							null);
				} else {

					previes_btn.setCompoundDrawablesWithIntrinsicBounds(
							getApplicationContext().getResources().getDrawable(
									R.drawable.back_arrows), null, null, null);
				}

				if (viewPager.getCurrentItem() == DealsDetails.images_path.size() - 1) {

					next_btn.setCompoundDrawablesWithIntrinsicBounds(null,
							null, getApplicationContext().getResources()
									.getDrawable(R.drawable.front_arrow_dis),
							null);
				} else {

					next_btn.setCompoundDrawablesWithIntrinsicBounds(null,
							null, getApplicationContext().getResources()
									.getDrawable(R.drawable.front_arrows), null);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		// Setting the previous image, title and position on UI
		previes_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int current = viewPager.getCurrentItem();
				viewPager.setCurrentItem(current - 1);

				// txtTitle.setText(""
				// + Multimedia.sub_media_Titles.get(viewPager
				// .getCurrentItem()));

				if (viewPager.getCurrentItem() == DealsDetails.images_path.size() - 1) {

				}

				if (viewPager.getCurrentItem() == 0) {

					previes_btn.setCompoundDrawablesWithIntrinsicBounds(null,
							null, getApplicationContext().getResources()
									.getDrawable(R.drawable.back_arrow_dis),
							null);
				} else {

					previes_btn.setCompoundDrawablesWithIntrinsicBounds(
							getApplicationContext().getResources().getDrawable(
									R.drawable.back_arrows), null, null, null);
				}

				if (viewPager.getCurrentItem() == DealsDetails.images_path.size() - 1) {

					next_btn.setCompoundDrawablesWithIntrinsicBounds(null,
							null, getApplicationContext().getResources()
									.getDrawable(R.drawable.front_arrow_dis),
							null);
				} else {

					next_btn.setCompoundDrawablesWithIntrinsicBounds(null,
							null, getApplicationContext().getResources()
									.getDrawable(R.drawable.front_arrows), null);
				}

			}
		});
		// Setting the next image, title and position on UI
				next_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (viewPager.getCurrentItem() < DealsDetails.images_path.size() - 1) {
							//imgCount++;
							// txtTitle.setText(""
							// + ""
							// + Multimedia.sub_media_Titles.get(viewPager
							// .getCurrentItem() + 1));
							viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
							int next = viewPager.getCurrentItem();
							next++;
							t1.setText("" + next++);

						} else {

						}

						if (viewPager.getCurrentItem() == DealsDetails.images_path.size() - 1) {

							next_btn.setCompoundDrawablesWithIntrinsicBounds(null,
									null, getApplicationContext().getResources()
											.getDrawable(R.drawable.front_arrow_dis),
									null);

							previes_btn.setCompoundDrawablesWithIntrinsicBounds(
									getApplicationContext().getResources().getDrawable(
											R.drawable.back_arrows), null, null, null);
						} else {

							next_btn.setCompoundDrawablesWithIntrinsicBounds(null,
									null, getApplicationContext().getResources()
											.getDrawable(R.drawable.front_arrows), null);
						}
					}
				});
	}

	public class ViewPagerAdapterSS extends PagerAdapter {

		// Variables for saving/restoring instance state
		Context context;
		LayoutInflater inflater;

		public ViewPagerAdapterSS(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;

		}

		@Override
		public int getCount() {
			return DealsDetails.images_path.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((RelativeLayout) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			/*inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ImageView imageView = new ImageView(context);
			new ImageLoadTask(DealsDetails.images_path.get(position), imageView).execute();
			
			//imageView.setBackground(DealsDetails.images_path.get(position));
			
			 * Picasso.with(DealGallery.this)
			 * .load((DealsDetails.images_path.get(position)).replace(" ",
			 * "%20")) .placeholder(R.drawable.loadingimg) //
			 * .error(R.drawable.no_image_available1).fit().into(imageView);
			 
*/
			 
			 inflater = (LayoutInflater) context
			 .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			 View itemView = inflater.inflate(R.layout.viewpager_item, container, false); 
			// Creating objects 
			 imgflag = (TouchImageView)
			
			 itemView.findViewById(R.id.view_image); imgflag.setMaxZoom(8); 
			 
			 Picasso.with(DealGallery.this)
			 .load(DealsDetails.images_path.get(position)) .placeholder(R.drawable.loadingimg)
			 .error(R.drawable.no_image_available1).fit().into(imgflag);

			// Add viewpager_item.xml to ViewPager
			((ViewPager) container).addView(itemView);

			return itemView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// Remove viewpager_item.xml from ViewPager
			((ViewPager) container).removeView((RelativeLayout) object);
		}
	}
	public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

	    private String url;
	    private ImageView imageView;

	    public ImageLoadTask(String url, ImageView imageView) {
	        this.url = url;
	        this.imageView = imageView;
	    }

	    @Override
	    protected Bitmap doInBackground(Void... params) {
	        try {
	            URL urlConnection = new URL(url);
	            HttpURLConnection connection = (HttpURLConnection) urlConnection
	                    .openConnection();
	            connection.setDoInput(true);
	            connection.connect();
	            InputStream input = connection.getInputStream();
	            Bitmap myBitmap = BitmapFactory.decodeStream(input);
	            return myBitmap;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    @Override
	    protected void onPostExecute(Bitmap result) {
	        super.onPostExecute(result);
	        imageView.setImageBitmap(result);
	    }

	}
}
