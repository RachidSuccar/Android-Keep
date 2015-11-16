package com.funnel.keep.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.decorator.CategoryItems;

public class CategoriesListAdapter extends ArrayAdapter<CategoryItems> {

	private ViewHolder viewHolder;
	private CategoryItems item;

	public CategoriesListAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		item = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.category_list_row, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.ViewHolderCategoryTitleTextView = (TextView) convertView
					.findViewById(R.id.TextView_categoryTitle);
			viewHolder.ViewHolderCategoryLenghtTextView = (TextView) convertView
					.findViewById(R.id.TextView_categoryContentCount);
			viewHolder.ViewHolderCategoryLenghtLayout = (RelativeLayout) convertView
					.findViewById(R.id.categoryLenghtLayout);
			viewHolder.ViewHolderCategoryMainIcon = (ImageView) convertView
					.findViewById(R.id.raw_icon);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.ViewHolderCategoryTitleTextView.setText(item.title);

		if (item.icon != -1) {
			viewHolder.ViewHolderCategoryMainIcon.setImageResource(item.icon);
		} 
		if (item.count == 0) {
			viewHolder.ViewHolderCategoryLenghtLayout.setVisibility(View.GONE);
		} else {
			viewHolder.ViewHolderCategoryLenghtTextView.setText(String
					.valueOf(item.count));
			viewHolder.ViewHolderCategoryLenghtLayout
					.setVisibility(View.VISIBLE);

		}

		return convertView;
	}

	private class ViewHolder {
		private TextView ViewHolderCategoryTitleTextView;
		private TextView ViewHolderCategoryLenghtTextView;
		private RelativeLayout ViewHolderCategoryLenghtLayout;
		private ImageView ViewHolderCategoryMainIcon;

	}

}
