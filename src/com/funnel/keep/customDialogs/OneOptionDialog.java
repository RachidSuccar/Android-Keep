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

public class OneOptionDialog extends BaseDialogFragment {

	private RelativeLayout optionOneLayout;
	private String optionOneTextString;
	private TextView optionOneTextView;

	public OneOptionDialog(String _optionOneTextString) {
		this.optionOneTextString = _optionOneTextString;

	}

	public interface OnOptionOneClickedListener {
		public void OnOptionOneClicked();
	}

	private OnOptionOneClickedListener onOptionOneClickedListener;

	public void setOneOptionOneClickListener(
			OnOptionOneClickedListener _onOptionOneClickedListener) {
		this.onOptionOneClickedListener = _onOptionOneClickedListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.one_option_dialog);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);


		optionOneLayout = (RelativeLayout) dialog
				.findViewById(R.id.RelativeLayout_deleteComment);

		optionOneTextView = (TextView) dialog
				.findViewById(R.id.CustomTextView_optionOne);

		optionOneTextView.setText(optionOneTextString);

		optionOneLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onOptionOneClickedListener != null) {
					onOptionOneClickedListener.OnOptionOneClicked();
				}
				dismiss();

			}
		});

		return dialog;
	}

}
