package com.funnel.keep.fragments;

import com.funnel.keep.activities.ViewPagerActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	protected ViewPagerActivity main;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (getActivity() instanceof ViewPagerActivity) {

			main = (ViewPagerActivity) getActivity();
		}

		super.onCreate(savedInstanceState);
	}

}
