package com.funnel.keep.utilities;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.funnel.keep.R;
import com.squareup.picasso.Picasso;

public class GridViewImageAdapter extends BaseAdapter {
	private Context mContext;
	private List<GridViewItem> items;
	private int cellSize;

	public GridViewImageAdapter(Context c) {
		this.mContext = c;
		cellSize = ImageUtil.getGridViewCellWidth(mContext, true);

	}

	public int getCount() {
		if (items != null) {
			return items.size();
		} else {
			return 0;
		}

	}

	public void setBitmaps(List<GridViewItem> Uri) {
		if (this.items != null && this.items.size() > 0) {
			this.items.clear();
		}
		this.items = Uri;
		this.notifyDataSetChanged();
	}

	public Object getItem(int position) {

		return items.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		GridViewItem item = (GridViewItem) getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.image_gridview_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.viewHolderImage = (ImageView) convertView
					.findViewById(R.id.imageView);

			// viewHolder.viewHolderImage
			// .setScaleType(ImageView.ScaleType.CENTER_CROP);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Drawable placeHolder = mContext.getResources().getDrawable(
				R.drawable.view_holder);
		Picasso.with(mContext).load(item.imageUri)
				.placeholder(placeHolder).noFade()

				.into(viewHolder.viewHolderImage);
		return convertView;
	}

	public static class ViewHolder {
		public ImageView viewHolderImage;
	}

	public static class GridViewItem {
		public Uri imageUri;
		public String imageName;

	}

	public String getImageNameForPostion(int postion) {
		return this.items.get(postion).imageName;
	}
}
