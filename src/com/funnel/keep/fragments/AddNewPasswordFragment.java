package com.funnel.keep.fragments;

import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.Utilities;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.customDialogs.PickColorDialog;
import com.funnel.keep.customDialogs.PickColorDialog.OnColorPickerListener;
import com.funnel.keep.decorator.Constants;
import com.funnel.keep.decorator.PasswordItem;

public final class AddNewPasswordFragment extends BaseFragment implements
		OnColorPickerListener {
	
	//--------VIEW--------
	private LinearLayout backLayout;
	private EditText passwordDescription;
	private EditText passTitle;
	private FrameLayout eyeLayout;
	private ImageView eyeImage;
	private EditText passwordText;
	private FrameLayout brushLayout;
	private PickColorDialog colorDialog;
	private TextView title;
	private TextView account;
	private TextView password;
	private TextView username;
	private TextView description;
	private ImageView titleWarning;
	private ImageView passwordWarning;
	private FrameLayout doneLayout;
	private ScrollView mainScrollView;
	private LinearLayout MainScrollViewLinearLayou;
	private EditText passwordAccount;
	private EditText passwordUserName;
	private TextView doneTextView;
	private TextView mainTitle;
	private TextView passwordRequiredMark;
	private TextView titleRequiredMark;
	
	//--------OTHERS--------
	private boolean eyeIsClosed = true;
	private int iconBgColor = PasswordItem.PASSWORD_ICON_BG_COLOR_NORMAL;
	private String passwTitle;
	private String passwAccount;
	private String passwText;
	private String passwUserName;
	private String passwDescription;
	private boolean passwIsNew = true;
	private int passwID;
	private int passwCategoryID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.add_new_password_fragment,
				container, false);
		getBundleInfo();
		passwordDescription = (EditText) fragmentView
				.findViewById(R.id.editText_description);
		passwordUserName = (EditText) fragmentView.findViewById(R.id.editText_userName);
		passwordAccount = (EditText) fragmentView.findViewById(R.id.editText_passAccount);
		backLayout = (LinearLayout) fragmentView.findViewById(R.id.back_layout);
		passwordRequiredMark = (TextView) fragmentView
				.findViewById(R.id.textView_PasswordRequired);
		titleRequiredMark = (TextView) fragmentView
				.findViewById(R.id.textView_TitleRequired);
		passTitle = (EditText) fragmentView.findViewById(R.id.editText_passTitle);
		eyeLayout = (FrameLayout) fragmentView.findViewById(R.id.FrameLayout_eye);
		eyeImage = (ImageView) fragmentView.findViewById(R.id.ImageView_eye);
		passwordText = (EditText) fragmentView.findViewById(R.id.editText_pass);
		brushLayout = (FrameLayout) fragmentView.findViewById(R.id.brush_layout);
		title = (TextView) fragmentView.findViewById(R.id.textView_title);
		account = (TextView) fragmentView.findViewById(R.id.textView_account);
		password = (TextView) fragmentView.findViewById(R.id.textView_password);
		username = (TextView) fragmentView.findViewById(R.id.textView_userName);
		description = (TextView) fragmentView.findViewById(R.id.textView_descrption);
		titleWarning = (ImageView) fragmentView.findViewById(R.id.ImageView_TitileWarning);
		passwordWarning = (ImageView) fragmentView
				.findViewById(R.id.ImageView_PasswordWarning);
		doneTextView = (TextView) fragmentView.findViewById(R.id.textView_done);
		doneLayout = (FrameLayout) fragmentView.findViewById(R.id.done_layout);
		mainScrollView = (ScrollView) fragmentView.findViewById(R.id.ScrollView_main);
		MainScrollViewLinearLayou = (LinearLayout) fragmentView
				.findViewById(R.id.LinearLayout_main);
		mainTitle = (TextView) fragmentView.findViewById(R.id.title_main);
		

		colorDialog = new PickColorDialog();
		colorDialog.setOnColorPickerListener(this);

		setListeners();
		checkIfPasswordIsNew();
		return fragmentView;
	}

	private void checkIfPasswordIsNew() {
		if (!this.passwIsNew) {
			mainTitle.setText(passwTitle);
			passTitle.setText(passwTitle);
			passwordAccount.setText(passwAccount);
			passwordText.setText(passwText);
			passwordUserName.setText(passwUserName);
			passwordDescription.setText(passwDescription);

			switch (iconBgColor) {
			case PasswordItem.PASSWORD_ICON_BG_COLOR_BLUE:
				setTextViewcolor(main.getResources().getColor(
						R.color.icon_bg_blue));
				break;

			case PasswordItem.PASSWORD_ICON_BG_COLOR_NORMAL:
				setTextViewcolor(main.getResources().getColor(
						R.color.icon_bg_default_color));
				break;

			case PasswordItem.PASSWORD_ICON_BG_COLOR_RED:
				setTextViewcolor(main.getResources().getColor(
						R.color.icon_bg_red));
				break;
			}

			doneTextView.setText(main.getResources().getString(R.string.done));

		}

	}

	private void setListeners() {

		passTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (passTitle.getText().toString().trim().length() > 0) {
					titleRequiredMark.setVisibility(View.INVISIBLE);
				} else {
					titleRequiredMark.setVisibility(View.VISIBLE);
				}

				titleWarning.setVisibility(View.GONE);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		passwordText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (passwordText.getText().length() > 0) {
					passwordRequiredMark.setVisibility(View.INVISIBLE);
				} else {
					passwordRequiredMark.setVisibility(View.VISIBLE);
				}
				passwordWarning.setVisibility(View.GONE);

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
				if (passwIsNew) {
					onDonePressed();
				} else {

					updatePass();

				}

			}

			private void updatePass() {
				if (shouldPassOnDonePressed() && main.getDataSource() != null) {

					if (main.getDataSource().updatePassword(
							passwID,
							Utilities.removeSpacesFromText(passTitle.getText()
									.toString()),
							Utilities.removeSpacesFromText(passwordAccount
									.getText().toString()),
							Utilities.removeSpacesFromText(passwordText
									.getText().toString()),
							Utilities.removeSpacesFromText(passwordUserName
									.getText().toString()),
							Utilities.removeSpacesFromText(passwordDescription
									.getText().toString()), iconBgColor) != 0) {
						main.startBroadcast(Constants.BROADCAST_KEY_RELOAD_SPECIFIC_PASSWORD_CATEGORIES_PAGE);

						getActivity().getSupportFragmentManager()
								.popBackStack();
					} else {
						CommonUtilities.handleError("", getFragmentManager());
					}

				}

			}
		});

		brushLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				colorDialog.show(getChildFragmentManager(), null);
			}
		});

		eyeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (eyeIsClosed) {
					eyeImage.setImageResource(R.drawable.eye_open);
					passwordText
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
					passwordText.setSelection(passwordText.getText().length());
					eyeIsClosed = false;
				} else {
					eyeImage.setImageResource(R.drawable.eye_close);

					passwordText
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
					passwordText.setSelection(passwordText.getText().length());
					eyeIsClosed = true;
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

	@Override
	public void onPause() {
		CommonUtilities.hideKeyboard(getActivity(), passTitle);
		super.onPause();
	}

	private void setTextViewcolor(int color) {
		title.setTextColor(color);
		account.setTextColor(color);
		password.setTextColor(color);
		username.setTextColor(color);
		description.setTextColor(color);
	}

	@Override
	public void onDefaultColorClicked(int defaultColor) {
		setTextViewcolor(defaultColor);
		iconBgColor = PasswordItem.PASSWORD_ICON_BG_COLOR_NORMAL;

	}

	@Override
	public void onBlueColorClicked(int blueColor) {
		setTextViewcolor(blueColor);
		iconBgColor = PasswordItem.PASSWORD_ICON_BG_COLOR_BLUE;

	}

	@Override
	public void onRedColorClicked(int redColor) {
		setTextViewcolor(redColor);
		iconBgColor = PasswordItem.PASSWORD_ICON_BG_COLOR_RED;

	}

	@Override
	public void onResume() {

		super.onResume();
	}

	private boolean shouldPassOnDonePressed() {

		boolean shouldPass;
		if (passTitle.getText().toString() != null
				&& passTitle.getText().toString().trim().length() > 0) {
			if (passwordText.getText().toString() != null
					&& passwordText.getText().toString().length() > 0) {

				shouldPass = true;

			} else {
				shouldPass = false;
				mainScrollView.scrollTo(0, passwordText.getBottom());
				passwordText.requestFocus();
				passwordWarning.setVisibility(View.VISIBLE);
				CommonUtilities.Toast(
						getActivity(),
						getActivity().getResources().getString(
								R.string.password_should_not_be_empty));

			}

		} else {
			shouldPass = false;
			mainScrollView.scrollTo(0, MainScrollViewLinearLayou.getTop());
			passTitle.requestFocus();
			titleWarning.setVisibility(View.VISIBLE);
			CommonUtilities.Toast(getActivity(), getActivity().getResources()
					.getString(R.string.title_should_not_be_empty));
		}

		return shouldPass;

	}

	private void onDonePressed() {

		if (shouldPassOnDonePressed()) {
			if (main.getDataSource() != null) {
				if (main.getDataSource().creatNewPassword(
						Utilities.removeSpacesFromText(passTitle.getText()
								.toString()),
						Utilities.removeSpacesFromText(passwordAccount
								.getText().toString()),
						Utilities.removeSpacesFromText(passwordText.getText()
								.toString()),
						Utilities.removeSpacesFromText(passwordUserName
								.getText().toString()),
						Utilities.removeSpacesFromText(passwordDescription
								.getText().toString()), iconBgColor,
						CommonUtilities.timeInSecond(), passwCategoryID) != -1) {
					main.startBroadcast(Constants.BROADCAST_KEY_RELOAD_PASSWORDS);
					getActivity().getSupportFragmentManager().popBackStack();
				} else {
					CommonUtilities.handleError("", getFragmentManager());
				}
			}
		}

	}

	public static AddNewPasswordFragment newInstanceOfAddNewPasswordFragment(
			int categoryID, boolean passIsNew) {
		AddNewPasswordFragment myFragment = new AddNewPasswordFragment();
		Bundle args = new Bundle();

		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID, categoryID);
		args.putBoolean(Constants.FRAGMENT_NEW_INSTANCE_NEW_PASSWORD, passIsNew);
		myFragment.setArguments(args);

		return myFragment;
	}

	private void getBundleInfo() {
		this.passwCategoryID = getArguments().getInt(
				Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID, 0);
		this.passwIsNew = getArguments().getBoolean(
				Constants.FRAGMENT_NEW_INSTANCE_NEW_PASSWORD);
		if (!this.passwIsNew) {

			this.passwAccount = getArguments().getString(
					Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_ACCOUNT);
			this.passwTitle = getArguments().getString(
					Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_TITLE);
			this.passwText = getArguments().getString(
					Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_TEXT);

			this.passwUserName = getArguments().getString(
					Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_USERNAME);

			this.passwDescription = getArguments().getString(
					Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_DESCRIPTION);
			this.iconBgColor = getArguments().getInt(
					Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_BG_COLOR);
			this.passwID = getArguments().getInt(
					Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_ID);
		}

	}

	public static AddNewPasswordFragment getInstanceOfAddNewPasswordFragment(
			String title, String account, String password, String userName,
			String description, int passwordThem, boolean newPass, int passID) {
		AddNewPasswordFragment fragment = new AddNewPasswordFragment();
		Bundle args = new Bundle();
		args.putString(Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_TITLE, title);
		args.putString(Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_ACCOUNT,
				account);
		args.putString(Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_TEXT, password);
		args.putString(Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_USERNAME,
				userName);
		args.putString(Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_DESCRIPTION,
				description);
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_BG_COLOR,
				passwordThem);
		args.putBoolean(Constants.FRAGMENT_NEW_INSTANCE_NEW_PASSWORD, newPass);

		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_ID, passID);

		fragment.setArguments(args);
		return fragment;
	}

}
