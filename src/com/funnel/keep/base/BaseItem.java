package com.funnel.keep.base;

public class BaseItem {
	public int itemID;
	public String itemTitle;
	public String itemText;
	public int itemBackgroundColor;
	public int itemCategoryID;
	public long itemCreationDate;

	public BaseItem(String itemText, long itemCreationDate) {
		this.itemText = itemText;
		this.itemCreationDate = itemCreationDate;
	}

	protected BaseItem(String _itemTitle, int _itemBackgroundColor) {
		this.itemTitle = _itemTitle;
		this.itemBackgroundColor = _itemBackgroundColor;
	}

	public BaseItem() {
	}

	protected String getItemTitleFirstCharacter() {
		return Character.toString(itemTitle.charAt(0));
	}

}
