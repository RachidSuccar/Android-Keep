package com.funnel.keep.fragments;

import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.Utilities;
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
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.decorator.Constants;

public final class AddNewCategoryFragment extends BaseFragment {
	// --------VIEW--------
	private EditText categoryTitle;
	private FrameLayout doneLayout;
	private LinearLayout backLayout;
	private ImageView titleWarning;
	private TextView pagetitle;

	// --------OTHERS--------
	private int categoryID;

	private enum OperationType {
		setPageTilte, addNewCategory
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(
				R.layout.add_new_category_fragment, container, false);
		getBundleInfo();
		CommonUtilities.showKeyBoard(getActivity());
		
		
		pagetitle = (TextView) fragmentView.findViewById(R.id.title);
		categoryTitle = (EditText) fragmentView
				.findViewById(R.id.editText_categoryTitle);
		doneLayout = (FrameLayout) fragmentView.findViewById(R.id.done_layout);
		backLayout = (LinearLayout) fragmentView.findViewById(R.id.back_layout);
		titleWarning = (ImageView) fragmentView
				.findViewById(R.id.ImageView_TitileWarning);
		
		setlisteners();
		
		getOperationDone(OperationType.setPageTilte);
		return fragmentView;
	}


	private void setlisteners() {

		categoryTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				titleWarning.setVisibility(View.GONE);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		doneLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkOnDonePressed()) {
					getOperationDone(OperationType.addNewCategory);

				}

			}
		});
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.getSupportFragmentManager().popBackStack();

			}
		});

	}

	private boolean checkOnDonePressed() {
		boolean shouldProcced;
		if (categoryTitle.getText().toString() != null
				&& categoryTitle.getText().toString().trim().length() > 0) {
			shouldProcced = true;

		} else {
			shouldProcced = false;
			// titleWarning.setVisibility(View.VISIBLE);
			CommonUtilities.Toast(getActivity(), getActivity().getResources()
					.getString(R.string.title_should_not_be_empty));
		}
		return shouldProcced;
	}

	@Override
	public void onPause() {
		CommonUtilities.hideKeyboard(main, categoryTitle);
		super.onPause();

	}

	@Override
	public void onResume() {

		super.onResume();
	}

	public static AddNewCategoryFragment getInstanceOfCategFrag(int categoryID) {
		AddNewCategoryFragment fragment = new AddNewCategoryFragment();
		Bundle args = new Bundle();
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID, categoryID);

		fragment.setArguments(args);
		return fragment;
	}

	private void getBundleInfo() {
		this.categoryID = getArguments().getInt(
				Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID);

	}

	private void setPageTitle(String title) {
		pagetitle.setText(title);
	}

	private void addNewPassCategory() {
		if (main.getDataSource() != null) {
			if (main.getDataSource().createNewPasswordsCategory(
					Utilities.removeSpacesFromText(categoryTitle.getText()
							.toString())) != -1) {
				main.startBroadcast(Constants.BROADCAST_KEY_RELOAD_PASSWORDS_CATEGORIES_PAGE);
				main.getSupportFragmentManager().popBackStack();
			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}

	}

	private void addNewNoteCategory() {
		if (main.getDataSource() != null) {
			if (main.getDataSource().createNewNoteCategory(
					Utilities.removeSpacesFromText(categoryTitle.getText()
							.toString())) != -1) {
				main.startBroadcast(Constants.BROADCAST_KEY_RELOAD_NOTES_CATEGORIES_PAGE);
				main.getSupportFragmentManager().popBackStack();
			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}

	}

	private void addNewPicCategory() {
		if (main.getDataSource() != null) {
			int response = main.getDataSource().createNewPicturesCategories(
					Utilities.removeSpacesFromText(categoryTitle.getText()
							.toString()));
			if (response == -2) {

				CommonUtilities.Toast(main,
						getResources().getString(R.string.album_already_exist));

			} else if (response == -1) {
				CommonUtilities.handleError("", getChildFragmentManager());

			} else {

				main.startBroadcast(Constants.BROADCAST_KEY_RELOAD_PICTURES_CATEGORIES_PAGE);
				main.getSupportFragmentManager().popBackStack();
			}
		}

	}

	private void getOperationDone(OperationType operationType) {
		switch (categoryID) {
		case CategoriesFragment.PASSWORD_CATEGORIES:
			switch (operationType) {
			case setPageTilte:
				setPageTitle(main.getString(R.string.new_pass_category));
				break;

			case addNewCategory:
				addNewPassCategory();
				break;
			}
			break;
		case CategoriesFragment.PICTURE_CATEGORIES:
			switch (operationType) {
			case setPageTilte:
				setPageTitle(main.getString(R.string.new_pic_category));
				break;

			case addNewCategory:
				addNewPicCategory();
				break;
			}
			break;

		case CategoriesFragment.NOTE_CATEGORIES:
			switch (operationType) {
			case setPageTilte:
				setPageTitle(main.getString(R.string.new_note_category));
				break;

			case addNewCategory:
				addNewNoteCategory();
				break;
			}
			break;

		}

	}
}
