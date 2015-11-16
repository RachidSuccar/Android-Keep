package com.funnel.keep.activities;

import com.funnel.keep.utilities.CommonUtilities;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;

import com.funnel.keep.R;

import com.funnel.keep.decorator.Constants;

public class NotificationReceiverActivity extends FragmentActivity {

	public final static String EXTRA_NOTIFICATION_MESSAGE = "EXTRA_NOTIFICATION_MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_reveiver_view);

		String userInput = getIntent().getStringExtra(
				EXTRA_NOTIFICATION_MESSAGE);


		CommonUtilities.copyToClipBoard(userInput, this,
				getString(R.string.content_cop_clip));
		Vibrator vb = (Vibrator) getSystemService(LoginActivity.VIBRATOR_SERVICE);
		vb.vibrate(150);

		this.finish();
	}
}
