package com.funnel.keep.customDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.funnel.keep.base.BaseDialogFragment;

import com.funnel.keep.R;

public class TwoOptionDialog extends BaseDialogFragment {

	private RelativeLayout optionOneLayout;
	private RelativeLayout optionTwoLayout;
	private TextView textViewOptionTwo;
	private String stringOptionTwo;
	private String optionOneTextString;
	private TextView optionOneTextView;

	private ImageView iconOne;
	private ImageView iconTwo;

	private int iconOneRes = -1;
	private int iconTwoRes = -1;

	public TwoOptionDialog(String _optionOneTextString, String _stringOptionTwo) {
		this.optionOneTextString = _optionOneTextString;
		this.stringOptionTwo = _stringOptionTwo;

	}

	public TwoOptionDialog(String _optionOneTextString,
			String _stringOptionTwo, int iconOne, int iconTwo) {
		this.optionOneTextString = _optionOneTextString;
		this.stringOptionTwo = _stringOptionTwo;
		this.iconOneRes = iconOne;
		this.iconTwoRes = iconTwo;

	}

	public interface OnOptionsClickedListener {
		public void OnOptionOneClicked();

		public void OnOptionTwoClicked();
	}

	private OnOptionsClickedListener onOptionOneClickedListener;

	public void setOneOptionOneClickListener(
			OnOptionsClickedListener _onOptionOneClickedListener) {
		this.onOptionOneClickedListener = _onOptionOneClickedListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.two_option_dialog);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);


		optionOneLayout = (RelativeLayout) dialog
				.findViewById(R.id.RelativeLayout_optionOne);
		optionTwoLayout = (RelativeLayout) dialog
				.findViewById(R.id.RelativeLayout_optionTwo);
		iconOne = (ImageView) dialog.findViewById(R.id.imageView_iconOne);
		iconTwo = (ImageView) dialog.findViewById(R.id.imageView_iconTwo);
		if (iconOneRes != -1 && iconTwoRes != -1) {
			iconOne.setImageResource(iconOneRes);
			iconTwo.setImageResource(iconTwoRes);
			iconOne.setVisibility(View.VISIBLE);
			iconTwo.setVisibility(View.VISIBLE);
		}
		optionTwoLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onOptionOneClickedListener != null) {
					onOptionOneClickedListener.OnOptionTwoClicked();
				}
				dismiss();

			}
		});

		optionOneTextView = (TextView) dialog
				.findViewById(R.id.CustomTextView_optionOne);

		optionOneTextView.setText(optionOneTextString);
		textViewOptionTwo = (TextView) dialog
				.findViewById(R.id.CustomTextView_optionTwo);
		textViewOptionTwo.setText(stringOptionTwo);

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
