package com.funnel.keep.fragments;

import java.util.ArrayList;
import java.util.List;

import com.funnel.keep.utilities.CommonUtilities;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.customDialogs.ConfirmationDialog;
import com.funnel.keep.customDialogs.DialogListOfOptions;
import com.funnel.keep.customDialogs.ConfirmationDialog.OnActionClickListener;
import com.funnel.keep.customDialogs.DialogListOfOptions.DialolListItem;
import com.funnel.keep.customDialogs.DialogListOfOptions.OnOptionsDialogClickListener;
import com.funnel.keep.decorator.Constants;
import com.funnel.keep.decorator.NoteItem;

public final class SpecificNoteCategoryFragment extends BaseFragment {
	
	//--------VIEW--------
	private TextView categoryTitleTextView;
	private RelativeLayout emptyPageLayout;
	private FrameLayout doneLayout;
	private LinearLayout backLayout;
	private ListView notesListView;

	//--------OTHERS--------
	private ConfirmationDialog deleteNoteConfirmDialog;
	private DialogListOfOptions dialogListOfOptions;
	private DialogListOfOptions dialogNoteType;

	private String categoryTitleString;
	private int categoryID;
	private NoteListAdapter adapter;
	private int longClickedItemID;
	private int longClickedNoteyType;

	private final int NOTE = 0;
	private final int CHECKLIST = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		adapter = new NoteListAdapter(getActivity(), 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.specific_note_category_fragment,
				container, false);
		getBundleInfo();

		categoryTitleTextView = (TextView) v.findViewById(R.id.title);
		emptyPageLayout = (RelativeLayout) v.findViewById(R.id.emptyPageLayout);
		categoryTitleTextView.setText(categoryTitleString);
		notesListView = (ListView) v
				.findViewById(R.id.listView_passwordListView);
		notesListView.setAdapter(adapter);

		doneLayout = (FrameLayout) v.findViewById(R.id.done_layout);
		backLayout = (LinearLayout) v.findViewById(R.id.back_layout);
		setDialogs();

		setlisteners();
			getAllNotes();

		return v;
	}

	private void setDialogs() {
		// add note types
		List<DialolListItem> itemsOne = new ArrayList<DialolListItem>();
		itemsOne.add(new DialolListItem(R.drawable.note_blue, getResources()
				.getString(R.string.noramle_note)));
		itemsOne.add(new DialolListItem(R.drawable.check_list, getResources()
				.getString(R.string.checklist_note)));
		dialogNoteType = new DialogListOfOptions(main, itemsOne);
		// delete not confirmation dialog
		deleteNoteConfirmDialog = new ConfirmationDialog(getActivity()
				.getResources().getString(R.string.are_you_sure_delete),
				getActivity().getResources().getString(R.string.cancel),
				getActivity().getResources().getString(R.string.yes));
		// long click dialog (edit , delete) note
		List<DialolListItem> itemsTwo = new ArrayList<DialolListItem>();
		itemsTwo.add(new DialolListItem(R.drawable.edit_icon, getResources()
				.getString(R.string.edit)));
		itemsTwo.add(new DialolListItem(R.drawable.delete, getResources()
				.getString(R.string.delete)));
		dialogListOfOptions = new DialogListOfOptions(main, itemsTwo);

	}

	private void getAllNotes() {
		if (main.getDataSource() != null) {

			List<NoteItem> items = main.getDataSource().geNotessForCategory(
					categoryID);
			if (items != null) {
				if (!items.isEmpty()) {
					emptyPageLayout.setVisibility(View.GONE);
					adapter.clear();
					adapter.addAll(items);
					adapter.notifyDataSetChanged();

				} else {
					emptyPageLayout.setVisibility(View.VISIBLE);
				}

			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}

	}

	private void getBundleInfo() {
		this.categoryID = getArguments().getInt(
				Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID, 0);
		this.categoryTitleString = getArguments().getString(
				Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_TITLE);
	}

	private void setlisteners() {

		dialogNoteType
				.setOneOptionClickListener(new OnOptionsDialogClickListener() {

					@Override
					public void OnOptionClicked(int optionPosition) {
						switch (optionPosition) {
						case 1:
							AddNewNoteFragment fragment = AddNewNoteFragment
									.getInstanceOfAddNewNoteFragment(
											categoryID,
											Constants.NONEXISTENT_NOTE_ID);
							main.replaceFragment(fragment);
							break;

						default:
							main.replaceFragment(AddNewCheckListFragment
									.getInstanceOfAddNewCheckListFragment(
											categoryID,
											Constants.NONEXISTENT_NOTE_ID));

							break;
						}

					}
				});

		deleteNoteConfirmDialog
				.setOnActionClickListener(new OnActionClickListener() {

					@Override
					public boolean onDismiss() {
						return false;

					}

					@Override
					public void onActionClicked() {
						if (main.getDataSource() != null) {
							if (main.getDataSource().deleteNote(
									longClickedItemID) != 0) {
								adapter.clear();
								getAllNotes();
								adapter.notifyDataSetChanged();
								main.startBroadcast(Constants.BROADCAST_KEY_RELOAD_NOTES_CATEGORIES_PAGE);
							} else {
								CommonUtilities.handleError("",
										getFragmentManager());
							}

						}
					}
				});

		dialogListOfOptions
				.setOneOptionClickListener(new OnOptionsDialogClickListener() {

					@Override
					public void OnOptionClicked(int optionPosition) {
						switch (optionPosition) {
						case 1:
							if (longClickedNoteyType == NOTE) {
								main.replaceFragment(AddNewNoteFragment
										.getInstanceOfAddNewNoteFragment(
												categoryID, longClickedItemID));
							} else {
								main.replaceFragment(AddNewCheckListFragment
										.getInstanceOfAddNewCheckListFragment(
												categoryID, longClickedItemID));
							}
							break;

						default:
							deleteNoteConfirmDialog.show(getActivity()
									.getSupportFragmentManager(), null);
							break;
						}

					}
				});
		doneLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogNoteType.show(main.getSupportFragmentManager(), "");
			}
		});
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.getSupportFragmentManager().popBackStack();

			}
		});

	}

	@Override
	public void onPause() {

		super.onPause();

	}

	@Override
	public void onResume() {

		super.onResume();
	}

	public static SpecificNoteCategoryFragment getNewInstance(
			String categoryTitle, int categoryID) {
		SpecificNoteCategoryFragment myFragment = new SpecificNoteCategoryFragment();
		Bundle args = new Bundle();
		args.putString(Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_TITLE,
				categoryTitle);
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID, categoryID);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public void onStart() {
		IntentFilter intentFilter = new IntentFilter();

		intentFilter
				.addAction(Constants.BROADCAST_KEY_RELOAD_SPECIFIC_NOTE_CATEGORIES_PAGE);
		intentFilter.addAction(Constants.BROADCAST_KEY_RELOAD_NOTES);

		main.registerReceiver(this.broadcastReceiver, intentFilter);
		super.onStart();
	}

	@Override
	public void onDestroy() {
		try {
			getActivity().unregisterReceiver(this.broadcastReceiver);
		} catch (IllegalArgumentException ex) {
			Log.e("Broadcast unregister", ex.getMessage());
		}
		super.onDestroyView();
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent
					.getAction()
					.equals(Constants.BROADCAST_KEY_RELOAD_SPECIFIC_NOTE_CATEGORIES_PAGE)
					|| intent.getAction().equals(
							Constants.BROADCAST_KEY_RELOAD_NOTES)) {

				adapter.clear();
				getAllNotes();

			}

		}
	};

	private class NoteListAdapter extends ArrayAdapter<NoteItem> {

		public NoteListAdapter(Context context, int resource) {

			super(context, resource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public int getItemViewType(int position) {

			NoteItem item = getItem(position);
			if (item.noteType == NoteItem.NOTE_TYPE_NORMAL) {
				return NOTE;
			} else {
				return CHECKLIST;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NoteItem item = getItem(position);
			final int itemID = item.itemID;
			ViewHolder viewHolder = new ViewHolder();
			final int type = getItemViewType(position);
			if (convertView == null) {

				if (type == NOTE) {
					convertView = LayoutInflater.from(getContext()).inflate(
							R.layout.note_list_row, parent, false);
					viewHolder.ViewHolderNoteMiniText = (TextView) convertView
							.findViewById(R.id.noteMiniText);
				} else {
					convertView = LayoutInflater.from(getContext()).inflate(
							R.layout.checklist_list_row_adapter, parent, false);
				}

				viewHolder.ViewHolderNoteTitleTextView = (TextView) convertView
						.findViewById(R.id.noteTitle);

				viewHolder.ViewHolderMainLayou = (RelativeLayout) convertView
						.findViewById(R.id.mainLayout);
				viewHolder.ViewHolderNoteCreationDate = (TextView) convertView
						.findViewById(R.id.noteCreationDate);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (type == NOTE) {
				String miniText = item.noteMiniText.length() > 99 ? (item.noteMiniText + "...")
						: (item.noteMiniText);
				viewHolder.ViewHolderNoteMiniText.setText(miniText);

			}
			if (item.itemTitle != null && !item.itemTitle.isEmpty()) {
				viewHolder.ViewHolderNoteTitleTextView
						.setVisibility(View.VISIBLE);
				viewHolder.ViewHolderNoteTitleTextView.setText(item.itemTitle);
			} else {
				viewHolder.ViewHolderNoteTitleTextView.setVisibility(View.GONE);

			}

			viewHolder.ViewHolderNoteCreationDate.setText(CommonUtilities
					.convertSecondsToTimeAgo(CommonUtilities.timeInSecond()
							- item.itemCreationDate));

			switch (item.itemBackgroundColor) {
			case Constants.NOTE_BG_COLOR_ONE:
				viewHolder.ViewHolderMainLayou.setBackground(getResources()
						.getDrawable(R.drawable.note_bg_color_one));
				break;

			case Constants.NOTE_BG_COLOR_TWO:
				viewHolder.ViewHolderMainLayou.setBackground(getResources()
						.getDrawable(R.drawable.note_bg_color_two));
				break;

			case Constants.NOTE_BG_COLOR_THREE:
				viewHolder.ViewHolderMainLayou.setBackground(getResources()
						.getDrawable(R.drawable.note_bg_color_three));
				break;

			}
			viewHolder.ViewHolderMainLayou
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (type == NOTE) {
								main.replaceFragment(AddNewNoteFragment
										.getInstanceOfAddNewNoteFragment(
												categoryID, itemID));
							} else {
								main.replaceFragment(AddNewCheckListFragment
										.getInstanceOfAddNewCheckListFragment(
												categoryID, itemID));
							}

						}
					});

			viewHolder.ViewHolderMainLayou
					.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							longClickedItemID = itemID;
							longClickedNoteyType = type;
							dialogListOfOptions.show(getActivity()
									.getSupportFragmentManager(), null);

							return false;
						}
					});

			return convertView;
		}

		private class ViewHolder {
			private TextView ViewHolderNoteTitleTextView;
			private TextView ViewHolderNoteMiniText;
			private TextView ViewHolderNoteCreationDate;
			private RelativeLayout ViewHolderMainLayou;

		}

	}
}
