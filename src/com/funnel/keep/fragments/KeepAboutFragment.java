package com.funnel.keep.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.funnel.keep.R;

public class KeepAboutFragment extends BaseFragment {

	private TextView appVersion;
	private FrameLayout backLayout;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.keep_about_fragment, container,
				false);
		appVersion = (TextView) view.findViewById(R.id.appVersion);
		backLayout = (FrameLayout) view.findViewById(R.id.backLayout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.getSupportFragmentManager().popBackStack();

			}
		});
		PackageManager manager = main.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(main.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String appVersion = info != null ? "Version " + info.versionName
				: "Version";
		this.appVersion.setText(appVersion);
		return view;
	}

}
