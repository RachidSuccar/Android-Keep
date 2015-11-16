package com.funnel.keep.fragments;

import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.LinedEditText;
import com.funnel.keep.utilities.Utilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.customDialogs.NotesPickColorDialog;
import com.funnel.keep.customDialogs.NotesPickColorDialog.OnColorPickerListener;
import com.funnel.keep.decorator.Constants;
import com.funnel.keep.decorator.NoteItem;

public class AddNewNoteFragment extends BaseFragment {

	//--------VIEW--------
	private FrameLayout changeNotColor;
	private RelativeLayout mainLayout;
	private LinedEditText noteEditText;
	private EditText notTitle;
	private FrameLayout doneLayout;
	private TextView noteCreationDate;
	private TextView mainTitle;
	private TextView doneText;
	
	//--------OTHERS--------
	private NotesPickColorDialog notrColorDialog;
	private int notBackgroundColor = NoteItem.NOTE_ICON_BG_COLOR_ONE;
	private int noteCategoryID;
	private int noteID = Constants.NONEXISTENT_NOTE_ID;
	private long noteDate = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.add_new_note_fragment,
				container, false);
		changeNotColor = (FrameLayout) fragmentView
				.findViewById(R.id.brush_layout);
		mainLayout = (RelativeLayout) fragmentView
				.findViewById(R.id.addnoteMainLayout);
		mainTitle = (TextView) fragmentView.findViewById(R.id.title_main);
		noteCreationDate = (TextView) fragmentView
				.findViewById(R.id.noteCreationDate);
		doneText = (TextView) fragmentView.findViewById(R.id.textView_done);
		doneLayout = (FrameLayout) fragmentView.findViewById(R.id.done_layout);
		notTitle = (EditText) fragmentView.findViewById(R.id.noteTitle);
		noteEditText = (LinedEditText) fragmentView
				.findViewById(R.id.noteEditText);
		notrColorDialog = new NotesPickColorDialog();

		getBundleInfo();
		setListeners();
		checkIfNewNote();
		return fragmentView;
	}

	private void checkIfNewNote() {

		if (noteID == Constants.NONEXISTENT_NOTE_ID) {
			// new note
			notTitle.requestFocus();
			CommonUtilities.showKeyBoard(main);
			noteCreationDate.setVisibility(View.GONE);
			noteEditText.setEnabled(true);

		} else {
			// show note
			mainTitle.setText(getString(R.string.edit_note));
			doneText.setText(getString(R.string.done));
			noteEditText.setMinLines(0);
			getNoteDetail(noteID);
		}

	}

	private void getNoteDetail(int noteID) {

		if (main.getDataSource() != null) {
			NoteItem note = main.getDataSource().getNoteDetails(noteID);
			if (note != null) {
				noteDate = note.itemCreationDate;
				if (note.itemTitle != null && !note.itemTitle.isEmpty()) {
					notTitle.setVisibility(View.VISIBLE);
					notTitle.setText(note.itemTitle);
				} else {
					notTitle.setVisibility(View.GONE);
				}
				noteCreationDate.setVisibility(View.VISIBLE);
				noteCreationDate.setText(Utilities
						.getDate(note.itemCreationDate));
				noteEditText.setText(note.itemText);
				switch (note.itemBackgroundColor) {
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
			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}

	}

	@Override
	public void onPause() {
		CommonUtilities.hideKeyboard(main, noteEditText);
		super.onPause();
	}

	private void setListeners() {

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
				addNewNote();

			}
		});
	}

	private void addNewNote() {

		long date = (noteID == Constants.NONEXISTENT_NOTE_ID) ? CommonUtilities
				.timeInSecond() : noteDate;
		if (!noteEditText.getText().toString().isEmpty()) {
			if (main.getDataSource() != null) {
				String textFirst100Characters = noteEditText.getText()
						.toString().length() > 101 ? noteEditText.getText()
						.toString().substring(0, 100) : noteEditText.getText()
						.toString();
				if (main.getDataSource().creatNewNoteWithoutEncry(
						notTitle.getText().toString(),
						noteEditText.getText().toString(),
						textFirst100Characters, notBackgroundColor, date,
						NoteItem.NOTE_TYPE_NORMAL, noteCategoryID, noteID) != -1) {
					if (noteID == Constants.NONEXISTENT_NOTE_ID) {
						main.startBroadcast(

						Constants.BROADCAST_KEY_RELOAD_NOTES);

					} else {
						main.startBroadcast(

						Constants.BROADCAST_KEY_RELOAD_SPECIFIC_NOTE_CATEGORIES_PAGE);
					}

					getActivity().getSupportFragmentManager().popBackStack();
				} else {
					CommonUtilities.handleError("", getFragmentManager());
				}
			}
		} else {
			CommonUtilities.Toast(main, "Note shoud not be empty");
		}

	}

	private void getBundleInfo() {
		this.noteCategoryID = getArguments().getInt(
				Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID, 0);
		this.noteID = getArguments().getInt(
				Constants.FRAGMENT_NEW_INSTANCE_NOTE_ID, 0);

	}

	public static AddNewNoteFragment getInstanceOfAddNewNoteFragment(
			int categoryID, int noteID) {
		AddNewNoteFragment fragment = new AddNewNoteFragment();
		Bundle args = new Bundle();
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID, categoryID);
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_NOTE_ID, noteID);
		fragment.setArguments(args);
		return fragment;
	}

}
