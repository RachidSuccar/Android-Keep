package com.funnel.keep.customDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.funnel.keep.base.BaseDialogFragment;

import com.funnel.keep.R;

public class InfoDialog extends BaseDialogFragment {

	public static InfoDialog instanceOfInfoDialog;

	public static InfoDialog getInfoInoDialogInstance() {
		if (instanceOfInfoDialog == null) {
			instanceOfInfoDialog = new InfoDialog();
		}
		return instanceOfInfoDialog;
	}

	public String dialogMessage;

	private TextView messageTextView;

	private RelativeLayout DismissLayout;

	public InfoDialog() {
	}

	//
	public void setMessage(String dialogMessage) {
		this.dialogMessage = dialogMessage;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.info_dialog_view);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		messageTextView = (TextView) dialog
				.findViewById(R.id.TextView_dialoMessage);
		messageTextView.setText(dialogMessage);
		DismissLayout = (RelativeLayout) dialog
				.findViewById(R.id.RelativeLayout_dismissB);
		DismissLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();

			}
		});

		return dialog;
	}

}
