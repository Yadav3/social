package com.etisbew.eventsnow.android;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
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
	private TabsAdapter mTabsAdapter;
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

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.mainfragment, container, false);
		/*
		 * ViewFlipper vf = (ViewFlipper)v.findViewById(R.id.flipper1);
		 * vf.startFlipping();
		 */
		screenWidth = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth();

		event = (EventBean) getActivity().getApplicationContext();
		/*
		 * categories = event.getCategories();
		 * 
		 * TITLES = new String[categories.size() + 1];
		 * 
		 * TITLES[0] = "All Categories";
		 * 
		 * category_id = new ArrayList<Integer>(); for (int i = 0; i <
		 * categories.size(); i++) { final CategoriesBean lb =
		 * categories.get(i); TITLES[i + 1] = lb.categoryName;
		 * category_id.add(lb.categoryId); }
		 */ 

		mTabHost = (TabHost) v.findViewById(android.R.id.tabhost);
		
		hs = (HorizontalScrollView) v.findViewById(R.id.horizontalScrollView1);
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		//mTabHost.setup();
 
	
		
		/*mTabsAdapter = new TabsAdapter(getActivity(), mTabHost, mViewPager);
		mTabsAdapter.addTab( 
				mTabHost.newTabSpec("All Categories").setIndicator(
						"All Categories"), AllEventsFragment.class, null);*/
		categories = event.getCategories();
		System.out.println("categories" + categories);
		events_upcoming = event.getObject();
		category_id = new ArrayList<Integer>();
		category_name = new ArrayList<String>();
		/*for (int i = 0; i < categories.size(); i++) {
			final CategoriesBean lb = categories.get(i);

			category_id.add(lb.categoryId);
			category_name.add(lb.categoryName);
			if(i==0){
					mTabsAdapter.addTab(mTabHost.newTabSpec(lb.categoryName)
							.setIndicator(lb.categoryName),
							ArtTheatreFragment.class, null);
				}else if(i==1){
					mTabsAdapter.addTab(mTabHost.newTabSpec(lb.categoryName)
							.setIndicator(lb.categoryName),
							ExhibitionsFragment.class, null);
				}else if(i==2){
					mTabsAdapter.addTab(mTabHost.newTabSpec(lb.categoryName)
							.setIndicator(lb.categoryName),
							KidsFamilyFragment.class, null);
				}else if(i==3){
					mTabsAdapter.addTab(mTabHost.newTabSpec(lb.categoryName)
							.setIndicator(lb.categoryName),
							MusicConcertsFragment.class, null); 
				}else if(i==4){
					mTabsAdapter.addTab(mTabHost.newTabSpec(lb.categoryName)
							.setIndicator(lb.categoryName),
							ProfessionalFragment.class, null);
				}else if(i==5){
					mTabsAdapter.addTab(mTabHost.newTabSpec(lb.categoryName)
							.setIndicator(lb.categoryName),
							SportsOutdoorFragment.class, null);
				}else if(i==6){
					mTabsAdapter.addTab(mTabHost.newTabSpec(lb.categoryName)
							.setIndicator(lb.categoryName),
							WorkshopFragment.class, null);
				}else{
					mTabsAdapter.addTab(mTabHost.newTabSpec(lb.categoryName)
							.setIndicator(lb.categoryName),
							ArtTheatreFragment.class, null);
				}
			System.out.println("catgoryName" + lb.categoryName + lb.categoryId); 
		}*/
		/*event.setFragmentId(category_id.get(0));
		 
				
		mViewPager.setOffscreenPageLimit(-1);
		mViewPager.setAdapter(mTabsAdapter);
		// mViewPager.setOffscreenPageLimit(category_id.size());
		
		TabWidget widget = mTabHost.getTabWidget();

		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title);
			tv.setTextColor(Color.parseColor("#ffffff"));
			tv.setTextSize(14);
			tv.setTypeface(event.getTextBold());
			//tv.setEllipsize(TextUtils.TruncateAt.END);
			tv.setAllCaps(false);

			if (isTabletDevice(getResources()) == true) {
				System.out.println("istablet");
				// //widget.getLayoutParams().width;
				mTabHost.getTabWidget().getChildAt(i).getLayoutParams().width = 210;
				// mTabHost.getTabWidget().getChildAt(0).setLayoutParams(new
				// LinearLayout.LayoutParams(200,50));
			} else { //
				System.out.println("isphone");
				mTabHost.getTabWidget().getChildAt(i).getLayoutParams().width = 230;
				// mTabHost.getTabWidget().getChildAt(0).setLayoutParams(new
				// LinearLayout.LayoutParams(0,50));
   
			} //
		//	mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.line1); // //selector xml
																// for selected
			mTabHost.getTabWidget().setStripEnabled(false);
		//	mTabHost.getTabWidget().setRightStripDrawable(R.drawable.line1);
		//	mTabHost.getTabWidget().setLeftStripDrawable(R.drawable.line1);
			View v1 = widget.getChildAt(i);

			// v1.setPadding(0,0,0,0);
			v1.setBackgroundResource(R.drawable.tab_selector_drawable);

		} 
*/
		 
		initialiseTabHost();
		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getFragmentManager(), fragments);
		//mViewPager.setOffscreenPageLimit(1);
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
			tv.setAllCaps(false);
			View v1 = widget.getChildAt(i);

			// v1.setPadding(0,0,0,0);
			v1.setBackgroundResource(R.drawable.tab_selector_drawable);	
		}
	//	mViewPager.setOnPageChangeListener(mTabsAdapter);  
		//mViewPager.setOnPageChangeListener(getActivity());
		/*
		 * tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs); pager =
		 * (ViewPager) v.findViewById(R.id.pager); adapter = new
		 * MyPagerAdapter(getFragmentManager());
		 * 
		 * pager.setAdapter(adapter);
		 * 
		 * final int pageMargin = (int) TypedValue.applyDimension(
		 * TypedValue.COMPLEX_UNIT_DIP, 4, getResources() .getDisplayMetrics());
		 * pager.setPageMargin(pageMargin);
		 * 
		 * tabs.setViewPager(pager);
		 */
		/*
		 * mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
		 * 
		 * @Override public void onPageSelected(int arg0) { // TODO
		 * Auto-generated method stub if (arg0 == category_id.size() - 1 || arg0
		 * == 0) {
		 * 
		 * arg0++; System.out.println("in if"+arg0); //
		 * mTabHost.setCurrentTab(arg0);
		 * 
		 * } else { arg0 = mViewPager.getCurrentItem();
		 * 
		 * arg0++; System.out.println("in else"+arg0);
		 * mTabHost.setCurrentTab(arg0);
		 * 
		 * } }
		 * 
		 * @Override public void onPageScrolled(int arg0, float arg1, int arg2)
		 * { }
		 * 
		 * @Override public void onPageScrollStateChanged(int arg0) { // TODO
		 * Auto-generated method stub } });
		 */
		return v;
	} 
	private void initialiseTabHost() {
		//mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
  
		// TODO Put here your Tabs
		AddTab(getActivity(),mTabHost,mTabHost.newTabSpec("All Categories").setIndicator("All Categories"));
		for (int i = 0; i < categories.size(); i++) {
			final CategoriesBean lb = categories.get(i);

			category_id.add(lb.categoryId);
			/*if(i==0){
					mTabsAdapter.addTab(mTabHost.newTabSpec(lb.categoryName)
							.setIndicator(lb.categoryName),
							ArtTheatreFragment.class, null);*/
			AddTab(getActivity(),mTabHost,mTabHost.newTabSpec(lb.categoryName).setIndicator(lb.categoryName));
						}
		/*AddTab(getActivity(),mTabHost,mTabHost.newTabSpec("Tab2").setIndicator("Tab2"));
		AddTab(getActivity(),mTabHost,mTabHost.newTabSpec("Tab3").setIndicator("Tab3"));
		AddTab(getActivity(),mTabHost,mTabHost.newTabSpec("Tab4").setIndicator("Tab4"));*/
		/*AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Tab2").setIndicator("Tab2"));
		AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Tab3").setIndicator("Tab3"));*/

		mTabHost.setOnTabChangedListener(this);
	}
	private static void AddTab(Context mcontext, TabHost tabHost,
			TabHost.TabSpec tabSpec) {
		tabSpec.setContent(new MyTabFactory(mcontext));
		tabHost.addTab(tabSpec);
	}
	// Manages the Tab changes, synchronizing it with Pages
		public void onTabChanged(String tag) {
			int pos = this.mTabHost.getCurrentTab();
			//System.out.println("in tab changed"+pos+category_id.get(pos)+pageAdapter.getItem(mViewPager.getCurrentItem()));
			/*if(pos>=2){
			
			//event.setFragmentId(category_id.get(pos));
		
		}*/	centerTabItem(pos);  
			this.mViewPager.setCurrentItem(pos); 
			//pageAdapter.getItem(mViewPager.getCurrentItem());
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		// Manages the Page changes, synchronizing it with Tabs
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			int pos = this.mViewPager.getCurrentItem();
		//	System.out.println("in mViewPager changed"+pos+category_id.get(pos)+arg1+arg2);
			this.mTabHost.setCurrentTab(pos);
		}

		public void onPageSelected(int arg0) {
		}

		private List<Fragment> getFragments() {
			List<Fragment> fList = new ArrayList<Fragment>();

			// TODO Put here your Fragments
			/*AllEventsFragment f1 = AllEventsFragment.newInstance("Sample Fragment 1");
			
			fList.add(f1); 
			*/
			AllEventsFragment f1 = AllEventsFragment.newInstance("");
			fList.add(f1);
			for (int i = 0; i < categories.size(); i++) {
				final CategoriesBean lb = categories.get(i);
				
				//AddTab(getActivity(),mTabHost,mTabHost.newTabSpec(lb.categoryName).setIndicator(lb.categoryName));
				ArtTheatreFragment categoryName= ArtTheatreFragment.newInstance(""+lb.categoryId);
				fList.add(categoryName);
							}
			System.out.println("flistsize"+fList.size());

			return fList;
		}

	public static class TabsAdapter extends FragmentStatePagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final TabHost mTabHost;
		private final ViewPager mViewPager;
		int flag_clicked = 0; 
			private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}
		
		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
			mTabHost.getTabWidget().setStripEnabled(true);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
		
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			event.setCheck_id(mViewPager.getCurrentItem());
			//position=mViewPager.getCurrentItem();
			/*
			 * if (category_id.size() - 1 == position) { int pos = position - 1;
			 * if (flag == 1) { pos = position - 1; flag = 2; } else if (flag ==
			 * 2) { pos = position - 1; flag = 1; } TabInfo info; if
			 * (category_id.size() > 4) { info = mTabs.get(pos-1); } else { info
			 * = mTabs.get(pos+1); } // System.out.println("if block"+position);
			 * try { System.out.println("categories" + category_id);
			 * 
			 * System.out.println("in getItem if" + pos + category_id.get(pos));
			 * event.setFragmentId(category_id.get(pos)); } catch (Exception e)
			 * { e.printStackTrace(); } return Fragment.instantiate(mContext,
			 * info.clss.getName(), info.args); } else {
			 */
			// System.out.println("else block"+position);
			TabInfo info = null; 
			try {
				
				info = mTabs.get(position);
				
				System.out.println("positionsgggggggggggggg"+ mViewPager.getCurrentItem() + position);
				if (flag_clicked == 1) {
					System.out.println("flag_checked pos" + position);
					if (position == 2) {
						if (mViewPager.getCurrentItem() != position) {
							System.out.println("position 2"+ mViewPager.getCurrentItem() + position
									+ category_id.get(position - 1));
							event.setFragmentId(category_id.get(position - 1));
							flag_clicked = 0;
						} else {

							System.out.println("position 2 else block"+ mViewPager.getCurrentItem() + position
									+ category_id.get(position - 1));
							event.setFragmentId(category_id.get(position - 1));

							flag_clicked = 1;

						}
					} else if (position > 2) {
						if (mViewPager.getCurrentItem() != position) {

							System.out.println("inside  position >2"
									+ mViewPager.getCurrentItem() + position
									+ category_id.get(position - 2));
							event.setFragmentId(category_id.get(position - 2));
							flag_clicked = 0;
						}
						event.setFragmentId(category_id.get(position - 1));
					} else {

						System.out.println("inside  position >2 else"
								+ mViewPager.getCurrentItem() + position
								+ category_id.get(position));
						event.setFragmentId(category_id.get(position));

						flag_clicked = 1;

					}
				} else if (flag_clicked == 0) {
					if (mViewPager.getCurrentItem() != position) {

						System.out.println("inside if"
								+ mViewPager.getCurrentItem() + position
								+ category_id.get(position - 1));
						event.setFragmentId(category_id.get(position - 1));

					} else {
						if (flag2 == 1) {
							System.out.println("inside else"
									+ mViewPager.getCurrentItem() + position
									+ category_id.get(position - 2));
							event.setFragmentId(category_id.get(position - 2));
							flag2 = 1;
						}
					}

				}
				// }

			} catch (Exception e) {
				info = mTabs.get(position);
				e.printStackTrace();
			}
			System.out.println("details"+mContext+info.clss.getName()+info.args+mViewPager.getCurrentItem()+position);
			/*if(mViewPager.getCurrentItem()== position){
				System.out.println("inside loop"+mViewPager.getCurrentItem()+position);
				event.setFragmentId(category_id.get(position - 1));
			}*/
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
			
		}

		/*
		 * TabInfo info = mTabs.get(position); return
		 * Fragment.instantiate(mContext, info.clss.getName(), info.args);
		 */
		// }

		/*
		 * public Fragment getItemPosition(){ return Position.NONE; }
		 */

		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			
			System.out.println("positions" + mViewPager.getCurrentItem()
					+ position);
			try {
				if (mViewPager.getCurrentItem() == position) { 
					mViewPager.setCurrentItem(position);
					flag_clicked = 1;
					// event.setFragmentId(category_id.get(position));
					centerTabItem(position);
				} else {

					// mViewPager.setCurrentItem(position);
					flag_clicked = 1;
					event.setCheck_id(position);
					mViewPager.setCurrentItem(position);
					/*
					 * System.out.println("in jjjjjjjj" + position +
					 * category_id.get(position -1) + tabId);
					 */
					// if(category_id.get(position).equals(o))
					// event.setFragmentId(category_id.get(position-1));
					centerTabItem(position);

				}
			} catch (Exception e) {
				centerTabItem(position);
				e.printStackTrace();
			}
			// event.setFragmentId(category_id.get(position));

		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void onPageSelected(int position) {
			// Unfortunately when TabHost changes the current tab, it kindly
			// also takes care of putting focus on it when not in touch mode.
			// The jerk.
			// This hack tries to prevent this from pulling focus out of our
			// ViewPager.
			// if(position>=1){
			/*
			 * try { System.out.println("categories" + category_id);
			 * 
			 * int pos = position; System.out.println("in getItem" + pos +
			 * category_id.get(pos)); event.setFragmentId(category_id.get(pos));
			 * } catch (Exception e) { e.printStackTrace(); }
			 */
			// }
			/* 
			 * Bundle bundle = new Bundle(); bundle.putString("category",
			 * category); bundle.putString("keyword", keyword);
			 */
			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

			mTabHost.setCurrentTab(position);
			// widget.setDescendantFocusability(oldFocusability);
			System.out.println("pos" + position);

			/*
			 * if (position == category_id.size() - 1 || position == 0) {
			 * mTabHost.setCurrentTab(position); //position++;
			 * System.out.println("in if"+position);
			 * 
			 * try { System.out.println("categories" + category_id);
			 * 
			 * int pos = position; System.out.println("in getItem if" + pos +
			 * category_id.get(pos)); event.setFragmentId(category_id.get(pos));
			 * } catch (Exception e) { e.printStackTrace(); }
			 * 
			 * } else { //position = mViewPager.getCurrentItem();
			 * mTabHost.setCurrentTab(position); //position++;
			 *  
			 * System.out.println("in else"+position);
			 * 
			 * try { System.out.println("categories" + category_id);
			 * 
			 * int pos = position; System.out.println("in getItem else" + pos +
			 * category_id.get(pos)); event.setFragmentId(category_id.get(pos));
			 * } catch (Exception e) { e.printStackTrace(); }
			 * 
			 * }
			 */

		}
		
		public void centerTabItem(int position) {
			
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

		public void onPageScrollStateChanged(int state) {
		}

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
		boolean isScreenXlarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE);
		return (isScreenLarge || isScreenXlarge);
	}

	

}