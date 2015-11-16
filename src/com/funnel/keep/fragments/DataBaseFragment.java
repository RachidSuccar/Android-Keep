package com.funnel.keep.fragments;

import com.funnel.keep.utilities.CommonUtilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnel.keep.R;

public final class DataBaseFragment extends BaseFragment {
	
	//--------VIEW--------
	private LinearLayout backLayout;
	private RelativeLayout dataBackupLayout;
	private RelativeLayout dataRestoreLayout;
	private TextView lastBackup;
	private TextView lastRestore;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.database_fragment, container, false);
		backLayout = (LinearLayout) v.findViewById(R.id.back_layout);
		dataBackupLayout = (RelativeLayout) v
				.findViewById(R.id.relativeLayout_backup);
		dataRestoreLayout = (RelativeLayout) v
				.findViewById(R.id.relativeLayout_restore);
		lastRestore = (TextView) v.findViewById(R.id.LastRestore);
		lastBackup = (TextView) v.findViewById(R.id.LastBackup);
		if (CommonUtilities.getIUpdateBit(main)) {
			lastBackup.setText(CommonUtilities.getLastUpdateDATE(main));
			lastBackup.setVisibility(View.VISIBLE);
		}
		if (CommonUtilities.getIRestoreBit(main)) {
			lastRestore.setText(CommonUtilities.getLastRestoreDATE(main));
			lastRestore.setVisibility(View.VISIBLE);
		}

		setlisteners();
		return v;
	}

	private void setlisteners() {

		dataRestoreLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.replaceFragment(SyncFragment
						.getInstanceOfSyncFragment(false));

			}
		});

		dataBackupLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.replaceFragment(SyncFragment
						.getInstanceOfSyncFragment(true));

			}
		});

		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.getSupportFragmentManager().popBackStack();

			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {

		super.onResume();
	}

}
