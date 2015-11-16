package com.funnel.keep.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.funnel.keep.R;

public class KeepTermsFragment extends BaseFragment {

	private LinearLayout backLayout;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.keep_terms, container, false);
		backLayout = (LinearLayout) view.findViewById(R.id.backLayout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.getSupportFragmentManager().popBackStack();

			}
		});
		return view;
	}

}
