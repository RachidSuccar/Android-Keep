package com.funnel.keep.base;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class BaseDialogFragment extends DialogFragment {

	@Override
	public void show(FragmentManager manager, String tag) {
		try {
			if (!isAdded())
				super.show(manager, tag);
			else
				Log.e("BaseDialogFragment", "error added twice");

		} catch (IllegalStateException ex) {
			Log.e("BaseDialogFragment", "error displaying dialog");
		}
	}

	@Override
	public int show(FragmentTransaction transaction, String tag) {
		try {
			if (!isAdded())
				return super.show(transaction, tag);
			else {
				Log.e("BaseDialogFragment", "error added twice");
				return 0;
			}

		} catch (IllegalStateException ex) {

			return 0;
		}
	}

}
