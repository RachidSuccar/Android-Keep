package com.funnel.keep.activities;

import com.funnel.keep.fragments.SpecificPicCategoryFragment;
import io.fabric.sdk.android.Fabric;

import com.funnel.keep.utilities.HackyViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.funnel.keep.R;
import com.funnel.keep.decorator.Constants;
import com.funnel.keep.fragments.ImageShowFragment;

public class ImageViewPagerActivity extends FragmentActivity {

	//--------VIEW--------
	private HackyViewPager mPager;
	//--------OTHERS--------
	private int postion;
	private PagerAdapter mPagerAdapter;
	private boolean fullScreenMode = true;

	
	
	@Override
	protected void onResume() {
		KeepApplicationContext.getKeepApplicationContext().activityResumed();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		KeepApplicationContext.getKeepApplicationContext().activityPaused();
		super.onPause();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.image_view_pager);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			hideSystemUI();
		}

		getIntent().getExtras().getInt(
				Constants.INTENT_VIEW_PAGER_START_POSITION);
		mPager = (HackyViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		postion = getIntent().getExtras().getInt(
				Constants.INTENT_VIEW_PAGER_START_POSITION);

		mPager.setCurrentItem(postion);

	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ImageShowFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return SpecificPicCategoryFragment.listOfALbumImage.size();
		}

	}

	// This snippet hides the system bars.
	public void hideSystemUI() {
		// Set the IMMERSIVE flag.
		// Set the content to appear under the system bars so that the content
		// doesn't resize when the system bars hide and show.
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
						| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
						| View.SYSTEM_UI_FLAG_IMMERSIVE);
		fullScreenMode = true;
	}

	// This snippet shows the system bars. It does this by removing all the
	// flags
	// except for the ones that make the content appear under the system bars.
	private void showSystemUI() {

		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		fullScreenMode = false;
	}

	public boolean toggleFullScreen() {
		if (fullScreenMode) {
			showSystemUI();
		} else {
			hideSystemUI();
		}

		return fullScreenMode;
	}

	public void setViewPagerLocked(boolean lock) {
		if (isViewPagerActive()) {
			((HackyViewPager) mPager).setLocked(lock);
		}

	}

	private boolean isViewPagerActive() {
		return (mPager != null && mPager instanceof HackyViewPager);
	}

}
