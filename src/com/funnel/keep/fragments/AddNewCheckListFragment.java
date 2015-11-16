package com.funnel.keep.fragments;

import java.util.ArrayList;
import java.util.List;

import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.Utilities;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.customDialogs.NotesPickColorDialog;
import com.funnel.keep.customDialogs.NotesPickColorDialog.OnColorPickerListener;
import com.funnel.keep.decorator.Constants;
import com.funnel.keep.decorator.NoteItem;
import com.funnel.keep.decorator.NoteItem.ChecklistItem;

public class AddNewCheckListFragment extends BaseFragment {

	//--------VIEW--------
	private FrameLayout changeNotColor;
	private RelativeLayout mainLayout;
	private RelativeLayout addChecklistLayout;
	private FrameLayout doneLayout;
	private LinearLayout checklistParent;
	private EditText noteTitle;
	private TextView noteCreationDate;
	private TextView titleMain;
	private TextView textDone;

	//--------OTHERS--------
	private NotesPickColorDialog notrColorDialog;
	private int notBackgroundColor = NoteItem.NOTE_ICON_BG_COLOR_ONE;
	private int noteCategoryID;
	private int checklitID = Constants.NONEXISTENT_NOTE_ID;
	private long noteDate = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(
				R.layout.add_new_checklist_fragment, container, false);

		changeNotColor = (FrameLayout) fragmentView
				.findViewById(R.id.brush_layout);
		mainLayout = (RelativeLayout) fragmentView
				.findViewById(R.id.addnoteMainLayout);
		addChecklistLayout = (RelativeLayout) fragmentView
				.findViewById(R.id.addChecklistLayout);
		noteCreationDate = (TextView) fragmentView
				.findViewById(R.id.noteCreationDate);
		titleMain = (TextView) fragmentView.findViewById(R.id.title_main);

		textDone = (TextView) fragmentView.findViewById(R.id.textView_done);
		checklistParent = (LinearLayout) fragmentView
				.findViewById(R.id.checklistParent);
		noteTitle = (EditText) fragmentView.findViewById(R.id.noteTitle);
		doneLayout = (FrameLayout) fragmentView.findViewById(R.id.done_layout);
		notrColorDialog = new NotesPickColorDialog();

		getBundleInfo();
		setListeners();
		checkIfNewNote();
		return fragmentView;
	}
	

	private void checkIfNewNote() {

		if (checklitID == Constants.NONEXISTENT_NOTE_ID) {
			titleMain.setText(main.getString(R.string.new_checklist));
			textDone.setText(main.getString(R.string.add));
			titleMain.setFocusable(true);
			titleMain.requestFocus();
			CommonUtilities.showKeyBoard(main);

		} else {
			// show note
			titleMain.setFocusable(false);
			titleMain.setText(main.getString(R.string.edit_checklist));
			textDone.setText(main.getString(R.string.done));
			getNoteDetail(checklitID);
		}

	}

	private void getNoteDetail(int noteID) {
		if (main.getDataSource() != null) {
			NoteItem checklist = main.getDataSource().getChecklistDetails(
					noteID);
			noteDate = checklist.itemCreationDate;
			if (checklist != null) {
				if (checklist.itemTitle != null
						&& !checklist.itemTitle.isEmpty()) {
					noteTitle.setVisibility(View.VISIBLE);
					noteTitle.setText(checklist.itemTitle);
				} else {
					noteTitle.setVisibility(View.GONE);
				}
				noteCreationDate.setVisibility(View.VISIBLE);
				noteCreationDate.setText(Utilities
						.getDate(checklist.itemCreationDate));
				addChecklistItems(checklist.noteChecklist);
				switch (checklist.itemBackgroundColor) {
				case NoteItem.NOTE_ICON_BG_COLOR_ONE:
					mainLayout.setBackgroundColor(main.getResources().getColor(
							R.color.note_color_one));
					notBackgroundColor = NoteItem.NOTE_ICON_BG_COLOR_ONE;
					break;

				case NoteItem.NOTE_ICON_BG_COLOR_TWO:

					mainLayout.setBackgroundColor(main.getResources().getColor(
							R.color.note_color_two));
					notBackgroundColor = NoteItem.NOTE_ICON_BG_COLOR_TWO;

					break;
				case NoteItem.NOTE_ICON_BG_COLOR_THREE:

					mainLayout.setBackgroundColor(main.getResources().getColor(
							R.color.note_color_three));
					notBackgroundColor = NoteItem.NOTE_ICON_BG_COLOR_THREE;

					break;
				}
			}

		}
	}

	private void addChecklistItems(List<ChecklistItem> items) {
		for (ChecklistItem item : items) {
			addNewItem(item);
		}
	}

	@Override
	public void onPause() {
		CommonUtilities.hideKeyboard(main, noteTitle);
		super.onPause();
	}

	private List<ChecklistItem> getChecklistItems() {
		List<ChecklistItem> items = new ArrayList<ChecklistItem>();
		for (int i = checklistParent.getChildCount() - 1; i > -1; i--) {
			ChecklistItem item = (ChecklistItem) checklistParent.getChildAt(i)
					.getTag();
			if (item != null && item.text != null && !item.text.isEmpty())
				items.add(item);
		}
		return items;
	}

	private void addNewItem(ChecklistItem _item) {
		// checkIfKeyboardIsShown();
		final ChecklistItem item;

		final View cheklistView = View.inflate(main,
				R.layout.checklist_list_row, null);
		RelativeLayout imageLayout = (RelativeLayout) cheklistView
				.findViewById(R.id.checkLayout);
		RelativeLayout closeLayout = (RelativeLayout) cheklistView
				.findViewById(R.id.close);
		final EditText checkListTitle = (EditText) cheklistView
				.findViewById(R.id.checkListTitle);

		final ImageView checkIcon = (ImageView) cheklistView
				.findViewById(R.id.checkBox);
		if (_item != null) {
			item = _item;
			checkListTitle.setText(item.text);
			if (item.status == NoteItem.CHECKLIST_STATUS_DONE) {
				checkIcon.setImageResource(R.drawable.square_checked);
				checkListTitle.setPaintFlags(checkListTitle.getPaintFlags()
						| Paint.STRIKE_THRU_TEXT_FLAG);
				imageLayout.setTag("true");

			} else {
				checkIcon.setImageResource(R.drawable.square_unchecked);
				checkListTitle.setPaintFlags(checkListTitle.getPaintFlags()
						& (~Paint.STRIKE_THRU_TEXT_FLAG));
				imageLayout.setTag("false");

			}
		} else {
			item = new ChecklistItem();
			checkListTitle.requestFocus();

			// if (!isKeyboardShown)
			// CommonUtilities.showKeyBoard(main);

		}

		// checkListTitle.setOnKeyListener(new OnKeyListener() {
		//
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		//
		// if (keyCode == KeyEvent.KEYCODE_ENTER
		// && event.getAction() == KeyEvent.ACTION_DOWN) {
		// addNewItem(null);
		// }
		// return false;
		// }
		// });

		checkListTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				item.text = checkListTitle.getText().toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// if (s.charAt(s.length() - 1) == '\n') {
				// addNewItem(null);
				// }
			}
		});

		closeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checklistParent.removeView(cheklistView);
				item.text = "";

			}
		});
		imageLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (v.getTag().equals("false")) {
					checkIcon.setImageResource(R.drawable.square_checked);
					checkListTitle.setPaintFlags(checkListTitle.getPaintFlags()
							| Paint.STRIKE_THRU_TEXT_FLAG);
					v.setTag("true");
					item.status = NoteItem.CHECKLIST_STATUS_DONE;
				} else {
					checkIcon.setImageResource(R.drawable.square_unchecked);
					checkListTitle.setPaintFlags(checkListTitle.getPaintFlags()
							& (~Paint.STRIKE_THRU_TEXT_FLAG));
					v.setTag("false");
					item.status = NoteItem.CHECKLIST_STATUS_NOT_DONE;
				}

			}
		});
		item.status = (imageLayout.getTag().equals("true")) ? NoteItem.CHECKLIST_STATUS_DONE
				: NoteItem.CHECKLIST_STATUS_NOT_DONE;
		item.text = checkListTitle.getText().toString();
		cheklistView.setTag(item);

		checklistParent.addView(cheklistView,
				(checklistParent.getChildCount() - 1));
	}

	private void setListeners() {

		addChecklistLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addNewItem(null);

			}
		});

		notrColorDialog.setOnColorPickerListener(new OnColorPickerListener() {

			@Override
			public void onColorOneClicked(int colorOne) {
				notBackgroundColor = NoteItem.NOTE_ICON_BG_COLOR_ONE;
				mainLayout.setBackgroundColor(colorOne);

			}

			@Override
			public void onColorTwoClicked(int colorTwo) {
				notBackgroundColor = NoteItem.NOTE_ICON_BG_COLOR_TWO;
				mainLayout.setBackgroundColor(colorTwo);

			}

			@Override
			public void onColorThreeClicked(int colorthree) {
				notBackgroundColor = NoteItem.NOTE_ICON_BG_COLOR_THREE;
				mainLayout.setBackgroundColor(colorthree);

			}
		});
		changeNotColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				notrColorDialog.show(getFragmentManager(), "");

			}
		});
		doneLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addNewChecklist();

			}
		});
	}

	private void addNewChecklist() {
		long date = (checklitID == Constants.NONEXISTENT_NOTE_ID) ? CommonUtilities
				.timeInSecond() : noteDate;

		if (!noteTitle.getText().toString().isEmpty()) {
			if (main.getDataSource() != null) {
				List<ChecklistItem> items = getChecklistItems();
				if (items != null && !items.isEmpty()) {
					main.getDataSource().creatNewChecklistWithoutEncry(
							noteTitle.getText().toString(), notBackgroundColor,
							date, NoteItem.NOTE_TYPE_CHECKLIST, noteCategoryID,
							getChecklistItems(), checklitID);
					if (checklitID == Constants.NONEXISTENT_NOTE_ID) {
						main.startBroadcast(

						Constants.BROADCAST_KEY_RELOAD_NOTES);

					} else {
						main.startBroadcast(

						Constants.BROADCAST_KEY_RELOAD_SPECIFIC_NOTE_CATEGORIES_PAGE);
					}

					getActivity().getSupportFragmentManager().popBackStack();
				} else {
					CommonUtilities.Toast(main,
							main.getString(R.string.note_should_not_be_empty));
				}

			}
		} else {
			CommonUtilities.Toast(main,
					main.getString(R.string.title_should_not_be_empty));
		}
	}

	private void getBundleInfo() {
		this.noteCategoryID = getArguments().getInt(
				Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID, 0);
		this.checklitID = getArguments().getInt(
				Constants.FRAGMENT_NEW_INSTANCE_NOTE_ID, 0);

	}

	public static AddNewCheckListFragment getInstanceOfAddNewCheckListFragment(
			int categoryID, int noteID) {
		AddNewCheckListFragment fragment = new AddNewCheckListFragment();
		Bundle args = new Bundle();
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID, categoryID);
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_NOTE_ID, noteID);
		fragment.setArguments(args);
		return fragment;
	}

}
