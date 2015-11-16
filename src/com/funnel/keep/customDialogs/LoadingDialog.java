package com.funnel.keep.customDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.funnel.keep.R;

public class LoadingDialog {

	public String LoadDialogMessage;
	private TextView Message;
	private Dialog dialog;

	public LoadingDialog(String _LoadDialogMessage, Context _context) {

		this.LoadDialogMessage = _LoadDialogMessage;

		dialog = new Dialog(_context);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);

		dialog.setContentView(R.layout.loading_dialog);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		Message = (TextView) dialog.findViewById(R.id.TextView_loading);
		Message.setText(LoadDialogMessage);

	}

	public void ShowDialog() {
		dialog.show();
	}

	public void HideDialog() {
		dialog.dismiss();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			return true;
		} else {
			return false;
		}
	}

}
