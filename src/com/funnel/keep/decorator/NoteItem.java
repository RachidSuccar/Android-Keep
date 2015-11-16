package com.funnel.keep.decorator;

import java.util.List;

import com.funnel.keep.base.BaseItem;

public class NoteItem extends BaseItem {

	// note bg color
	public static final int NOTE_ICON_BG_COLOR_ONE = 1;
	public static final int NOTE_ICON_BG_COLOR_TWO = 2;
	public static final int NOTE_ICON_BG_COLOR_THREE = 3;

	// note type
	public static final int NOTE_TYPE_NORMAL = 0;
	public static final int NOTE_TYPE_CHECKLIST = 1;

	// note checklist status
	public static final int CHECKLIST_STATUS_DONE = 1;
	public static final int CHECKLIST_STATUS_NOT_DONE = 0;

	public String noteMiniText;
	public int noteType;
	public List<ChecklistItem> noteChecklist;

	public NoteItem(String _itemTitle, int _itemBackgroundColor) {
		super(_itemTitle, _itemBackgroundColor);
	}

	public NoteItem() {

	}

	public static class ChecklistItem {

		public int status;
		public String text;
		public int ID;

		public ChecklistItem() {

		}

		public ChecklistItem(int _status, String _text) {
			this.status = _status;
			this.text = _text;
		}
	}

}
