package com.funnel.keep.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.decorator.PasswordItem;

public class PasswordListAdapter extends ArrayAdapter<PasswordItem> {

	private ViewHolder viewHolder;
	private PasswordItem item;

	public PasswordListAdapter(Context context, int resource) {

		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		item = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.password_list_row, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.ViewHolderIconTitleTextView = (TextView) convertView
					.findViewById(R.id.icon_title);
			viewHolder.ViewHolderImageViewIcon = (ImageView) convertView
					.findViewById(R.id.raw_icon);
			viewHolder.ViewHolderPasswordTitleTextView = (TextView) convertView
					.findViewById(R.id.TextView_passwordTitle);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.ViewHolderIconTitleTextView.setText(item.getIconName());
		viewHolder.ViewHolderPasswordTitleTextView.setText(item.itemTitle);

		switch (item.itemBackgroundColor) {
		case PasswordItem.PASSWORD_ICON_BG_COLOR_BLUE:
			viewHolder.ViewHolderImageViewIcon
					.setImageResource(R.drawable.pass_icon_bg_blue);
			break;

		case PasswordItem.PASSWORD_ICON_BG_COLOR_NORMAL:
			viewHolder.ViewHolderImageViewIcon
					.setImageResource(R.drawable.pass_icon_bg);
			break;

		case PasswordItem.PASSWORD_ICON_BG_COLOR_RED:
			viewHolder.ViewHolderImageViewIcon
					.setImageResource(R.drawable.pass_icon_bg_red);
			break;
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView ViewHolderPasswordTitleTextView;
		private TextView ViewHolderIconTitleTextView;
		private ImageView ViewHolderImageViewIcon;

	}

}
