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

public class ConfirmationDialog extends BaseDialogFragment {

	public String DialogMessage;
	public String DialogDismissButtonName;
	public String DialogActionButtonName;

	private TextView Message;
	private TextView DismissText;
	private TextView ActionText;
	private RelativeLayout ActionLayout;
	private RelativeLayout DismissLayout;

	public ConfirmationDialog(String dialogMessage,
			String dialogDismissButtonName, String dialogActionButtonName) {

		DialogMessage = dialogMessage;
		DialogDismissButtonName =dialogDismissButtonName ;
		DialogActionButtonName =  dialogActionButtonName  ;
	}

	public interface OnActionClickListener {
		public abstract void onActionClicked();
		public abstract boolean onDismiss();
	}

	private OnActionClickListener onActionClickListener;

	

	public OnActionClickListener getOnActionClickListener() {
		return onActionClickListener;
	}

	public void setOnActionClickListener(OnActionClickListener onActionClickListener) {
		this.onActionClickListener = onActionClickListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.confirmation_dialog_view);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		Message = (TextView) dialog.findViewById(R.id.TextView_dialoMessage);
		DismissText = (TextView) dialog.findViewById(R.id.TextView_Cancel);
		ActionText = (TextView) dialog.findViewById(R.id.TextView_ActionT);
		Message.setText(DialogMessage);
		DismissText.setText(DialogDismissButtonName);
		ActionText.setText(DialogActionButtonName);
		DismissLayout = (RelativeLayout) dialog
				.findViewById(R.id.RelativeLayout_dismissB);
		ActionLayout = (RelativeLayout) dialog
				.findViewById(R.id.RelativeLayout_Action);
		DismissLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();
				if (onActionClickListener != null) {
					if (onActionClickListener.onDismiss()){
						//dismiss
					}
				}

			}
		});

		ActionLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (onActionClickListener != null) {
					onActionClickListener.onActionClicked();
				}

			}
		});

		return dialog;
	}

}

