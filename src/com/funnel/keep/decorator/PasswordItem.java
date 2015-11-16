package com.funnel.keep.decorator;

import java.util.Locale;

import com.funnel.keep.base.BaseItem;

public class PasswordItem extends BaseItem {
	public static final int PASSWORD_ICON_BG_COLOR_NORMAL = 1;
	public static final int PASSWORD_ICON_BG_COLOR_BLUE = 2;
	public static final int PASSWORD_ICON_BG_COLOR_RED = 3;

	public String passwordAccount;
	public String passwordUsername;
	public String passwordDescription;

	public PasswordItem(String _itemTitle, int _itemBackgroundColor) {
		super(_itemTitle, _itemBackgroundColor);
	}

	public PasswordItem() {

	}

	public String getIconName() {

		String iconTitle = null;
		if (itemTitle != null && itemTitle.length() > 0) {
			if (itemTitle.length() == 1) {
				iconTitle = Character.toString(itemTitle.charAt(0));
			} else {
				iconTitle = Character.toString(itemTitle.charAt(0))
						+ Character.toString(itemTitle.charAt(1));
			}

		}
		return iconTitle.toUpperCase(Locale.getDefault());
	}

}
