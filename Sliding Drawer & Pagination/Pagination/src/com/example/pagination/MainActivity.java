package com.example.pagination;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.slidemenu.SlideMenu;
import com.example.slidemenu.SlideMenuInterface.OnSlideMenuItemClickListener;
import com.viewpagerindicator.CirclePageIndicator;

public class MainActivity extends FragmentActivity implements
		OnSlideMenuItemClickListener, Page1Fragment.OnPageListener {

	private SlideMenu slidemenu;
	ImageButton b;

	List<Fragment> fragments = new Vector<Fragment>();
	// page adapter between fragment list and view pager
	private PagerAdapter mPagerAdapter;
	// view pager
	private ViewPager mPager;
	public String p2text, p3text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		b = (ImageButton) findViewById(R.id.menu);
		slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
		slidemenu.init(this, R.menu.slide, this, 333);
		// this can set the menu to initially shown instead of hidden
		// slidemenu.setAsShown();

		// set optional header image
		// slidemenu.setHeaderImage(getResources().getDrawable(
		// R.drawable.blogger));
		//
		// SlideMenuItem item = new SlideMenuItem();
		// item.icon = getResources().getDrawable(R.drawable.blogger);
		// item.label = "Blogger";
		// slidemenu.addMenuItem(item);

		// creating fragments and adding to list
		fragments
				.add(Fragment.instantiate(this, Page1Fragment.class.getName()));
		fragments
				.add(Fragment.instantiate(this, Page2Fragment.class.getName()));
		// fragments.add(Fragment.instantiate(this,Page3Fragment.class.getName()));

		// creating adapter and linking to view pager
		this.mPagerAdapter = new PagerAdapter(
				super.getSupportFragmentManager(), fragments);
		mPager = (ViewPager) super.findViewById(R.id.pager);
		mPager.setAdapter(this.mPagerAdapter);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		// mIndicator = indicator;
		indicator.setViewPager(mPager);

		final float density = getResources().getDisplayMetrics().density;
		// indicator.setBackgroundColor(0xFFCCCCCC);
		indicator.setRadius(9 * density);
		indicator.setPageColor(0xff888888);
		// indicator.setFillColor(0xFF888888);
		indicator.setStrokeColor(0xFF000000);
		indicator.setStrokeWidth(1 * density);

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				slidemenu.show();
			}
		});

	}

	// page 1 fragment listener implementation
	@Override
	public void onPage1(String s) {

		// set activity data with received string
		p2text = new String(s + " 2");
		p3text = new String(s + " 3");

		// page 2 fragment update
		Page2Fragment f2 = (Page2Fragment) fragments.get(1);
		f2.ptext = p2text;
		// if page 2 view is already created, update
		View v2 = f2.getView();
		if (v2 != null) {
			f2.setText(p2text + " direct");
		}

	}

	@Override
	public void onSlideMenuItemClick(int itemId) {
		// TODO Auto-generated method stub
		switch (itemId) {
		case R.id.blogger:
			Toast.makeText(this, "Login with Blogger", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.digg:
			Toast.makeText(this, "Login with Digg", Toast.LENGTH_SHORT).show();
			break;
		case R.id.facebook:
			Toast.makeText(this, "Login with Facebook", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.flicker:
			Toast.makeText(this, "Login with flicker", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.google:
			Toast.makeText(this, "Login with Google", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.lastfm:
			Toast.makeText(this, "Login with Lastfm", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.linkedin:
			Toast.makeText(this, "Login with Linkedin", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.live:
			Toast.makeText(this, "Login with Live", Toast.LENGTH_SHORT).show();
			break;
		case R.id.orkut:
			Toast.makeText(this, "Login with Orkut", Toast.LENGTH_SHORT).show();
			break;
		case R.id.rss:
			Toast.makeText(this, "Login with RSS", Toast.LENGTH_SHORT).show();
			break;
		case R.id.technorati:
			Toast.makeText(this, "Login with Technorati", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.twitter:
			Toast.makeText(this, "Login with Twitter", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.wordpress:
			Toast.makeText(this, "Login with Wordpress", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.yahoo:
			Toast.makeText(this, "Login with Yahoo", Toast.LENGTH_SHORT).show();
			break;
		case R.id.youtube:
			Toast.makeText(this, "Login with Youtube", Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slidemenu.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
