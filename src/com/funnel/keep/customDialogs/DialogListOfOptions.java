package com.funnel.keep.customDialogs;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.funnel.keep.base.BaseDialogFragment;

import com.funnel.keep.R;

public class DialogListOfOptions extends BaseDialogFragment {

	private ListView dialogListView;
	private List<DialolListItem> listOfOptions;
	private SimpleDialogListAdapter adapter;
	private Context context;

	public DialogListOfOptions(Context context,
			List<DialolListItem> _listOfOptions) {
		this.listOfOptions = _listOfOptions;
		this.context = context;
	}

	private OnOptionsDialogClickListener onOptionClickedListener;

	public void setOneOptionClickListener(
			OnOptionsDialogClickListener _onOptionClickedListener) {
		this.onOptionClickedListener = _onOptionClickedListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.dialog_list_of_option_view);
		adapter = new SimpleDialogListAdapter(context);
		adapter.addAll(listOfOptions);
		dialogListView = (ListView) dialog.findViewById(R.id.dialogListView);
		dialogListView.setAdapter(adapter);

		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));

		dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		

		return dialog;
	}

	private class SimpleDialogListAdapter extends ArrayAdapter<DialolListItem> {

		public SimpleDialogListAdapter(Context context) {
			super(context, 0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int clickedpostion = position;
			DialolListItem selectedItem = getItem(position);
			DialogLisyViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.dialog_list_of_option_row, parent, false);
				viewHolder = new DialogLisyViewHolder();
				viewHolder.viewHolderRowIcon = (ImageView) convertView
						.findViewById(R.id.rowIcon);
				viewHolder.viewHolderTextView = (TextView) convertView
						.findViewById(R.id.rowTitle);
				viewHolder.viewHolderDivider = (View) convertView
						.findViewById(R.id.rowDivider);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (DialogLisyViewHolder) convertView.getTag();
			}
			if (selectedItem.rowIcon == 0) {
				viewHolder.viewHolderRowIcon.setVisibility(View.GONE);
			} else {
				viewHolder.viewHolderRowIcon
						.setImageResource(selectedItem.rowIcon);
				viewHolder.viewHolderRowIcon.setVisibility(View.VISIBLE);
			}
			if (position == getCount() - 1) {
				viewHolder.viewHolderDivider.setVisibility(View.GONE);
			} else {
				viewHolder.viewHolderDivider.setVisibility(View.VISIBLE);
			}

			viewHolder.viewHolderTextView.setText(selectedItem.rowTitle);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (onOptionClickedListener != null) {
						onOptionClickedListener
								.OnOptionClicked(clickedpostion + 1);
						dismiss();
					}

				}
			});
			return convertView;
		}

		private class DialogLisyViewHolder {
			public ImageView viewHolderRowIcon;
			public TextView viewHolderTextView;
			public View viewHolderDivider;
		}

	}

	public static class DialolListItem {
		public int rowIcon;
		public String rowTitle;

		public DialolListItem(int rowIcon, String rowTitle) {
			this.rowIcon = rowIcon;
			this.rowTitle = rowTitle;
		}

	}

	public interface OnOptionsDialogClickListener {
		public void OnOptionClicked(int optionPosition);
	}

}
