package com.funnel.keep.activities;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import com.funnel.keep.sqlDataBaseManager.BaseDataSource;
import com.funnel.keep.sqlDataBaseManager.DataSource;
import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.Cryptography;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import com.funnel.keep.base.BaseNotificationActivity;

import com.funnel.keep.R;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import com.funnel.keep.decorator.Constants;
import com.funnel.keep.fragments.CategoriesFragment;
import com.funnel.keep.fragments.SettingsFragment;

public class ViewPagerActivity extends BaseNotificationActivity {
	// --------VIEW--------
	private ViewPager mPager;

	// --------OTHERS--------
	private FragmentPagerAdapter adapter;
	private BaseDataSource dataSource;
	private SecretKeySpec secretKeySpec;
	private boolean checkIfShouldFinish = false;
	
	// MENU ITEMS
	private List<MenuItem> menuItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_activity);
		if (getIntentInfo()) {
			initPage();
		}

	}

	private void initPage() {

		dataSource = new DataSource(this, secretKeySpec);
		
		// Set application main menu items
		setMenu();
		
		mPager = (ViewPager) findViewById(R.id.pager);
		adapter = new KeepSafeAdapter(getSupportFragmentManager());
		mPager.setAdapter(adapter);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);

		indicator.setViewPager(mPager);
	}

	public BaseDataSource getDataSource(boolean noErrorHandling) {
		if (noErrorHandling) {
			return dataSource;
		} else {
			return getDataSource();
		}

	}

	public BaseDataSource getDataSource() {
		if (dataSource != null) {
			if (!dataSource.isDbOpen())
				dataSource.openDataBAse();
			return dataSource;
		} else {
			CommonUtilities.handleError(
					getString(R.string.error_db_connection_closed),
					getSupportFragmentManager());
			return null;
		}
	}

	private boolean getIntentInfo() {
		if (getIntent().hasExtra(Constants.INTENT_ENCRYPTION_KEY)) {
			String key = getIntent().getStringExtra(
					Constants.INTENT_ENCRYPTION_KEY);
			secretKeySpec = Cryptography.getMySecretKey128Bits(key);
			return true;
		} else {

			this.finish();
			return false;
		}

	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					Constants.BROADCAST_NOTIFICATION_TOAST)) {
				String input = intent
						.getStringExtra(Constants.BROADCAST_TOAST_EXTRA_KEY);
				Vibrator vb = (Vibrator) ViewPagerActivity.this
						.getSystemService(LoginActivity.VIBRATOR_SERVICE);
				vb.vibrate(70);
				CommonUtilities.copyToClipBoard(input, ViewPagerActivity.this,
						getString(R.string.content_cop_clip));
			}

		}
	};

	@Override
	protected void onStart() {
		// Cancel shown notifications
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.BROADCAST_NOTIFICATION_TOAST);
		registerReceiver(this.broadcastReceiver, intentFilter);
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		if (dataSource != null && dataSource.isDbOpen()) {
			dataSource.closeDatabaBase();
		}
		try {
			unregisterReceiver(this.broadcastReceiver);
		} catch (IllegalArgumentException ex) {
			Log.e("Broadcast unregister", ex.getMessage());
		}
		super.onDestroy();
	}

	
	private class KeepSafeAdapter extends FragmentPagerAdapter implements
			IconPagerAdapter {
		public KeepSafeAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return menuItems.get(position).itemFragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return menuItems.get(position).itemTitle;
		}

		@Override
		public int getIconResId(int index) {
			return menuItems.get(index).itemIcon;
		}

		@Override
		public int getCount() {
			return menuItems.size();
		}
	}

	@Override
	protected void onPause() {
		KeepApplicationContext.getKeepApplicationContext().activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		KeepApplicationContext.getKeepApplicationContext().activityResumed();
		// Check if should finish the application in case auto exist is true
		if (checkIfShouldFinish
				&& (CommonUtilities.getAutoExitPref(this) || dataSource == null)) {
			finish();
			Intent newItent = new Intent(ViewPagerActivity.this,
					LoginActivity.class);
			startActivity(newItent);
		}
		checkIfShouldFinish = true;
		if (CommonUtilities.ifShouldSetAutoExitToTrue(this)) {
			CommonUtilities.setAutoExitPref(this, true);
			CommonUtilities.setShouldSetAutoExitToTrue(this, false);
		}
		if (!dataSource.isDbOpen()) {
			dataSource.openDataBAse();
		}
		super.onResume();
	}

	public void replaceFragment(Fragment fragment) {
		FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
		FT.replace(R.id.main_view, fragment).addToBackStack(null);

		FT.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void setMenu() {

		if (menuItems == null)
			menuItems = new ArrayList<MenuItem>();
		
		// ADD PASSWORDS ITEM
		menuItems
				.add(new MenuItem(
						R.drawable.perm_group_passwords,
						getResources().getString(R.string.passwods),
						CategoriesFragment
								.getInstanceOfCategFrag(CategoriesFragment.PASSWORD_CATEGORIES)));
		// ADD PICTURES ITEM
		menuItems
				.add(new MenuItem(
						R.drawable.perm_group_pictures,
						getResources().getString(R.string.pictures),
						CategoriesFragment
								.getInstanceOfCategFrag(CategoriesFragment.PICTURE_CATEGORIES)));
		// ADD NOTES ITEM
		menuItems.add(new MenuItem(R.drawable.perm_group_notes, getResources()
				.getString(R.string.notes), CategoriesFragment
				.getInstanceOfCategFrag(CategoriesFragment.NOTE_CATEGORIES)));
		// ADD SETTINGS ITEM
		menuItems.add(new MenuItem(R.drawable.perm_group_settings,
				getResources().getString(R.string.settings),
				new SettingsFragment()));

	}

	private class MenuItem {
		public int itemIcon;
		public String itemTitle;
		public Fragment itemFragment;

		public MenuItem(int itemIcon, String itemTitle, Fragment itemFragment) {
			this.itemIcon = itemIcon;
			this.itemTitle = itemTitle;
			this.itemFragment = itemFragment;
		}

	}

	public void onBackClicked(View v) {
		this.getSupportFragmentManager().popBackStack();

	}

	public void startBroadcast(String broadcastKey) {
		Intent broadcastIntent = new Intent();

		broadcastIntent.setAction(broadcastKey);
		this.sendBroadcast(broadcastIntent);

	}
}
