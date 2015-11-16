package com.funnel.keep.customDialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.funnel.keep.base.BaseDialogFragment;

import com.funnel.keep.R;

public class PickColorDialog extends BaseDialogFragment {

	private RelativeLayout DismissLayout;
	private FrameLayout defaultColor;
	private FrameLayout blueColor;
	private FrameLayout redColor;

	public PickColorDialog() {
	}

	public interface OnColorPickerListener {
		public void onDefaultColorClicked(int defaultColor);

		public void onBlueColorClicked(int blueColor);

		public void onRedColorClicked(int redColor);
	}

	private OnColorPickerListener onColorPickerListener;

	public void setOnColorPickerListener(
			OnColorPickerListener _onColorPickerListener) {
		this.onColorPickerListener = _onColorPickerListener;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.dialog_pick_color);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);


		defaultColor = (FrameLayout) dialog
				.findViewById(R.id.frameLayout_defaultColor);
		blueColor = (FrameLayout) dialog.findViewById(R.id.frameLayout_blue);
		redColor = (FrameLayout) dialog.findViewById(R.id.frameLayout_red);
		redColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onColorPickerListener != null) {
					onColorPickerListener.onRedColorClicked(getActivity()
							.getResources().getColor(R.color.icon_bg_red));
				}
				dismiss();

			}
		});

		blueColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onColorPickerListener != null) {
					onColorPickerListener.onBlueColorClicked(getActivity()
							.getResources().getColor(R.color.icon_bg_blue));
				}
				dismiss();

			}
		});

		defaultColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onColorPickerListener != null) {
					onColorPickerListener.onDefaultColorClicked(getActivity()
							.getResources().getColor(
									R.color.icon_bg_default_color));
				}
				dismiss();

			}
		});

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