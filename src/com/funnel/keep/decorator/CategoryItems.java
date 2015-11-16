package com.funnel.keep.decorator;

public class CategoryItems {

	public String title;
	public int icon;
	public int count;
	public int id;

	public CategoryItems() {

	}

	public CategoryItems(String _title, int _categoryContentCount) {
		this.id = -1;
		this.count = _categoryContentCount;
		this.title = _title;
	}

}
