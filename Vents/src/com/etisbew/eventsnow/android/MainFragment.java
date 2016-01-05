package com.etisbew.eventsnow.android;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.etisbew.eventsnow.android.beans.CategoriesBean;
import com.etisbew.eventsnow.android.beans.EventsBean;

public class MainFragment extends Fragment implements OnTabChangeListener, OnPageChangeListener {

	private TabHost mTabHost;
	private ViewPager mViewPager;
	private GestureDetectorCompat gDetector;
	static HorizontalScrollView hs;
	static int screenWidth;

	private ViewPager pager;
	// private MyPagerAdapter adapter;
	static String category = "";
	static String keyword = ""; 
	static EventBean event;
	static ArrayList<Integer> category_id;
	static ArrayList<String> category_name;
	ArrayList<CategoriesBean> categories;
	static int flag = 1;
	static int flag2 = 1;
	private String[] TITLES;

	ArrayList<EventsBean> events_upcoming;

	MyPageAdapter pageAdapter;
	public MainFragment() {
	}

	@Override
	public void onCreate(Bundle instance) {
		super.onCreate(instance);

	} 
	public void onResumeFragment() {
		super.onResume();
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.mainfragment, container, false);
		
		screenWidth = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth();

		event = (EventBean) getActivity().getApplicationContext();

		mTabHost = (TabHost) v.findViewById(android.R.id.tabhost);
		
		hs = (HorizontalScrollView) v.findViewById(R.id.horizontalScrollView1);
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		categories = event.getCategories();
		events_upcoming = event.getObject();
		category_id = new ArrayList<Integer>();
		category_name = new ArrayList<String>();
	
		 
		initialiseTabHost();
		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getFragmentManager(), fragments);
		mViewPager.setOffscreenPageLimit(1);  
		mViewPager.setAdapter(pageAdapter);
	
		mViewPager.setOnPageChangeListener(this);
		TabWidget widget = mTabHost.getTabWidget();
		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title);
			tv.setTextColor(Color.parseColor("#ffffff"));
			if (isTabletDevice(getResources()) == true) {
			tv.setTextSize(17);
			}else{
			tv.setTextSize(14);
			} 
			tv.setTypeface(event.getTextBold());
			//tv.setEllipsize(TextUtils.TruncateAt.END);
			if (Build.VERSION.SDK_INT > 11) {
			tv.setAllCaps(false);
			}
			View v1 = widget.getChildAt(i); 

			// v1.setPadding(0,0,0,0);
			v1.setBackgroundResource(R.drawable.tab_selector_drawable);	
		}
 
		return v;    
	} 
	@SuppressLint("NewApi")
	private void initialiseTabHost() {
		mTabHost.setup();
  
		// TODO Put here your Tabs
		AddTab(getActivity(),mTabHost,mTabHost.newTabSpec("All Categories").setIndicator("All Categories"));
		try {
		for (int i = 0; i < categories.size(); i++) {
			final CategoriesBean lb = categories.get(i);

			category_id.add(lb.categoryId);
			AddTab(getActivity(),mTabHost,mTabHost.newTabSpec(lb.categoryName).setIndicator(lb.categoryName));
						}
		} catch (Exception e) {
			e.printStackTrace();
			Intent intent = new Intent(new Intent(getActivity(),
					SplashActivity.class));
			
			if (Build.VERSION.SDK_INT > 11) {
				Bundle  bundle = ActivityOptions.makeCustomAnimation(getActivity(),
					R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
			getActivity().startActivity(intent, bundle);
			}else{
				startActivity(intent);
			}
		}
			mTabHost.setOnTabChangedListener(this);
	}
	private static void AddTab(Context mcontext, TabHost tabHost,
			TabHost.TabSpec tabSpec) {
		tabSpec.setContent(new MyTabFactory(mcontext));
		tabHost.addTab(tabSpec);
	}
		public void onTabChanged(String tag) {
			int pos = this.mTabHost.getCurrentTab();
			centerTabItem(pos);  
			this.mViewPager.setCurrentItem(pos); 
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		// Manages the Page changes, synchronizing it with Tabs
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			int pos = this.mViewPager.getCurrentItem();
			this.mTabHost.setCurrentTab(pos);
		}

		public void onPageSelected(int arg0) {
		}

		@SuppressLint("NewApi")
		private List<Fragment> getFragments() {
			List<Fragment> fList = new ArrayList<Fragment>();

			AllEventsFragment f1 = AllEventsFragment.newInstance("");
			fList.add(f1);
			try {
			for (int i = 0; i < categories.size(); i++) {
				final CategoriesBean lb = categories.get(i);
				
				ArtTheatreFragment categoryName= ArtTheatreFragment.newInstance(""+lb.categoryId);
				fList.add(categoryName);
							}
			} catch (Exception e) {
				e.printStackTrace();
				Intent intent = new Intent(new Intent(getActivity(),
						SplashActivity.class));
				
				if (Build.VERSION.SDK_INT > 11) {
					Bundle  bundle = ActivityOptions.makeCustomAnimation(getActivity(),
						R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
				getActivity().startActivity(intent, bundle);
				}else{
					startActivity(intent);
				}
			}
			return fList;
		}
 
	 
	public void centerTabItem(int position) { 
		System.out.println("position " + position);
		mTabHost.setCurrentTab(position);
		final TabWidget tabWidget = mTabHost.getTabWidget();

		final int leftX = tabWidget.getChildAt(position).getLeft();
		int newX = 0;
 
		newX = leftX + (tabWidget.getChildAt(position).getWidth() / 2)
				- (screenWidth / 2); 
		if (newX < 0) {
			newX = 0;
		}  
		hs.scrollTo(newX, 0);
	}
 
	

	public boolean isTabletDevice(Resources resources) {
		int screenLayout = resources.getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		boolean isScreenLarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE);
		if (Build.VERSION.SDK_INT > 11) {
		boolean isScreenXlarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE);
		return (isScreenLarge || isScreenXlarge);
		}else{
			return (isScreenLarge );
		}
	}

	

}