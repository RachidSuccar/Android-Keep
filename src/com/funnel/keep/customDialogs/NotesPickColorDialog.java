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

public class NotesPickColorDialog extends BaseDialogFragment {

	private RelativeLayout dismissLayout;
	private FrameLayout colorOne;
	private FrameLayout colorTwo;
	private FrameLayout colorThree;

	public NotesPickColorDialog() {
	}

	public interface OnColorPickerListener {
		public void onColorOneClicked(int colorOne);

		public void onColorTwoClicked(int colorTwo);

		public void onColorThreeClicked(int colorthree);
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

		dialog.setContentView(R.layout.notes_dialog_pick_color);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);


		colorOne = (FrameLayout) dialog
				.findViewById(R.id.frameLayout_defaultColor);
		colorTwo = (FrameLayout) dialog.findViewById(R.id.frameLayout_blue);
		colorThree = (FrameLayout) dialog.findViewById(R.id.frameLayout_red);
		colorThree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onColorPickerListener != null) {
					onColorPickerListener.onColorThreeClicked(getActivity()
							.getResources().getColor(R.color.note_color_three));
				}
				dismiss();

			}
		});

		colorTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onColorPickerListener != null) {
					onColorPickerListener.onColorTwoClicked(getActivity()
							.getResources().getColor(R.color.note_color_two));
				}
				dismiss();

			}
		});

		colorOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onColorPickerListener != null) {
					onColorPickerListener.onColorOneClicked(getActivity()
							.getResources().getColor(R.color.note_color_one));
				}
				dismiss();

			}
		});

		dismissLayout = (RelativeLayout) dialog
				.findViewById(R.id.RelativeLayout_dismissB);
		dismissLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dismiss();

			}
		});

		return dialog;
	}

}