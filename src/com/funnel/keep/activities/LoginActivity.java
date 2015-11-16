package com.funnel.keep.activities;

import io.fabric.sdk.android.Fabric;
import android.animation.ObjectAnimator;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.crashlytics.android.Crashlytics;
import com.funnel.keep.R;
import com.funnel.keep.decorator.Constants;
import com.funnel.keep.sqlDataBaseManager.DataSource;
import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.Cryptography;
import com.funnel.keep.utilities.Utilities;

public class LoginActivity extends FragmentActivity {

	// --------VIEW--------
	private EditText pass;
	private EditText newPass;
	private EditText confirmPass;
	private LinearLayout enterPassLayout;
	private LinearLayout loginLayout;
	private LinearLayout mainLayout;
	private ScrollView mainScrollView;

	// --------OTHERS--------
	private DataSource dataSource;
	private Drawable lockLightGray;
	private Drawable lockRed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Crashlytics
		Fabric.with(this, new Crashlytics());

		setContentView(R.layout.login_activity);
		initPage();

	}

	private void initPage() {
		pass = (EditText) findViewById(R.id.editText_pass);
		newPass = (EditText) findViewById(R.id.new_pass);
		confirmPass = (EditText) findViewById(R.id.confirm_pass);
		lockLightGray = getResources().getDrawable(R.drawable.lock_l_g);
		lockRed = getResources().getDrawable(R.drawable.lock_red);
		loginLayout = (LinearLayout) findViewById(R.id.login_layout);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		enterPassLayout = (LinearLayout) findViewById(R.id.login_enterPassLayout);
		mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
		mainScrollView.fullScroll(View.FOCUS_DOWN);
		dataSource = new DataSource(this);
		setListeners();

	}

	private void setListeners() {
		newPass.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				changeEditTextLeftIcon(false, newPass);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		confirmPass.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().equals(newPass.getText().toString())) {
					changeEditTextLeftIcon(false, confirmPass);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void onPause() {
		// To know how to handle data sent from web-keep.com
		KeepApplicationContext.getKeepApplicationContext().activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// To know how to handle data sent from web-keep.com
		KeepApplicationContext.getKeepApplicationContext().activityResumed();
		if (!dataSource.isDbOpen()) {
			dataSource.openDataBAse();
		}

		checkIfRegistered();

		super.onResume();
	}

	@Override
	public void onStart() {
		// Cancel shown notifications
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.BROADCAST_NOTIFICATION_TOAST);
		registerReceiver(this.broadcastReceiver, intentFilter);
		super.onStart();
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					Constants.BROADCAST_NOTIFICATION_TOAST)) {
				String input = intent
						.getStringExtra(Constants.BROADCAST_TOAST_EXTRA_KEY);
				Vibrator vb = (Vibrator) LoginActivity.this
						.getSystemService(LoginActivity.VIBRATOR_SERVICE);
				vb.vibrate(70);
				CommonUtilities.copyToClipBoard(input, LoginActivity.this,
						getString(R.string.content_cop_clip));
			}

		}
	};

	public void proceed(View v) {

		String hashedPassword = dataSource.getPassword();
		if (hashedPassword != null && hashedPassword.length() > 0) {
			if (Cryptography.hashMD5(pass.getText().toString()).equals(
					dataSource.getPassword())) {
				// The right password
				proceedToViewPagerActivity(pass.getText().toString());
			} else {

				// Wrong password
				CommonUtilities.Toast(this, getString(R.string.wrong_pass));
				vibrate();
			}
		}
	}

	private void vibrate() {
		Vibrator vb = (Vibrator) getSystemService(LoginActivity.VIBRATOR_SERVICE);
		vb.vibrate(150);
		ObjectAnimator
				.ofFloat(mainLayout, "translationX", 0, 25, -25, 25, -25, 15,
						-15, 6, -6, 0).setDuration(150).start();
	}

	private void proceedToViewPagerActivity(String userPassord) {
		// userPassord is used for encryption
		Intent intent = new Intent(LoginActivity.this, ViewPagerActivity.class);
		intent.putExtra(Constants.INTENT_ENCRYPTION_KEY, userPassord);
		startActivity(intent);
		this.finish();

	}

	public void saveNewPass(View v) {
		if (shouldSaveNewPass()) {
			dataSource.setMainAppPassword(Cryptography.hashMD5(newPass
					.getText().toString()));
			proceedToViewPagerActivity(newPass.getText().toString());
		}
	}

	@Override
	protected void onDestroy() {
		if (dataSource != null) {
			dataSource.closeDatabaBase();
		}

		CommonUtilities.hideKeyboard(this, pass);
		try {
			unregisterReceiver(this.broadcastReceiver);
		} catch (IllegalArgumentException ex) {
			Log.e("Broadcast unregister", ex.getMessage());
		}
		super.onDestroy();
	}

	private boolean shouldSaveNewPass() {
		boolean shouldSave;

		if (newPass.getText().toString().length() >= Constants.PASSWORD_MIN_LENGHT) {

			if (Utilities.checkIfStringCharacters(newPass.getText().toString())) {

				if (Utilities.checkIfStringContainNumber(newPass.getText()
						.toString())) {
					if (newPass.getText().toString()
							.equals(confirmPass.getText().toString())) {
						shouldSave = true;
					} else {
						shouldSave = false;
						changeEditTextLeftIcon(true, confirmPass);
						CommonUtilities.Toast(this,
								getResources()
										.getString(R.string.pass_not_same));
					}

				} else {
					shouldSave = false;
					changeEditTextLeftIcon(true, newPass);
					CommonUtilities.Toast(this,
							getResources()
									.getString(R.string.pass_contain_numb));
				}

			} else {
				shouldSave = false;
				changeEditTextLeftIcon(true, newPass);
				CommonUtilities.Toast(
						this,
						getResources().getString(
								R.string.pass_contain_character));

			}

		} else {
			shouldSave = false;
			changeEditTextLeftIcon(true, newPass);
			CommonUtilities.Toast(this,
					getResources().getString(R.string.pass_min_eight_char));
		}
		return shouldSave;
	}

	private void changeEditTextLeftIcon(Boolean IsRed, EditText editText) {
		if (IsRed) {
			editText.setCompoundDrawablesWithIntrinsicBounds(lockRed, null,
					null, null);
		} else {
			editText.setCompoundDrawablesWithIntrinsicBounds(lockLightGray,
					null, null, null);
		}
	}

	// Check if user already set his app main password or not.
	private void checkIfRegistered() {
		String pass = dataSource.getPassword();
		if (pass != null && !pass.isEmpty()) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			enterPassLayout.setVisibility(View.GONE);
			loginLayout.setVisibility(View.VISIBLE);

		} else {
			CommonUtilities.handleError(getString(R.string.pass_tip),
					getSupportFragmentManager());
			enterPassLayout.setVisibility(View.VISIBLE);
			loginLayout.setVisibility(View.GONE);
		}
	}

}