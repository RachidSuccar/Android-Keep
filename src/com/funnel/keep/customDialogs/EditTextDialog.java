package com.funnel.keep.customDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.funnel.keep.base.BaseDialogFragment;

import com.funnel.keep.R;

public class EditTextDialog extends BaseDialogFragment {

	public String dialogMessage;
	public String dialogDismissButtonName;
	public String dialogActionButtonName;
	public String dialogEditTextHint;
	private String dialogEditTextString = "";
	

	private TextView message;
	private TextView dismissText;
	private TextView actionText;
	private RelativeLayout actionLayout;
	private RelativeLayout dismissLayout;
	private EditText editTextDialog;

	public EditTextDialog(String dialogMessage, String dialogDismissButtonName,
			String dialogActionButtonName, String _DialogEditTextHint) {

		this.dialogMessage = dialogMessage;
		this.dialogDismissButtonName = dialogDismissButtonName;
		this.dialogActionButtonName = dialogActionButtonName;
		this.dialogEditTextHint = _DialogEditTextHint;
	}

	public interface OnEditTextDialogListener {
		public abstract boolean onActionClicked(String editTextString);

		public abstract boolean onDismiss();
	}
	
	private OnEditTextDialogListener onActionClickListener;

	//

	public OnEditTextDialogListener getOnActionClickListener() {
		return onActionClickListener;
	}

	public void setOnEditTextDialogClickListener(
			OnEditTextDialogListener onActionClickListener) {
		this.onActionClickListener = onActionClickListener;
	}

	public void setDialogEditText(String text) {
		dialogEditTextString = text;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		new OnEditTextDialogListener() {
			
			@Override
			public boolean onDismiss() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onActionClicked(String editTextString) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.edit_text_dialog_view);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		message = (TextView) dialog.findViewById(R.id.TextView_dialoMessage);
		dismissText = (TextView) dialog.findViewById(R.id.TextView_Cancel);
		actionText = (TextView) dialog.findViewById(R.id.TextView_ActionT);
		message.setText(dialogMessage);
		dismissText.setText(dialogDismissButtonName);
		editTextDialog = (EditText) dialog
				.findViewById(R.id.editText_categoryTitle);
		editTextDialog.setText(dialogEditTextString);
		editTextDialog.setHint(dialogEditTextHint);
		editTextDialog.setSelection(editTextDialog.getText().length());
		actionText.setText(dialogActionButtonName);
		dismissLayout = (RelativeLayout) dialog
				.findViewById(R.id.RelativeLayout_dismissB);
		actionLayout = (RelativeLayout) dialog
				.findViewById(R.id.RelativeLayout_Action);
		dismissLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();
				if (onActionClickListener != null) {
					if (onActionClickListener.onDismiss()) {
						dismiss();
					}
				}

			}
		});

		actionLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (onActionClickListener != null) {
					if (onActionClickListener.onActionClicked(editTextDialog
							.getText().toString())) {
						dismiss();
					}
				}

			}
		});

		return dialog;
	}

}
