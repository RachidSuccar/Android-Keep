package com.funnel.keep.fragments;

import java.util.ArrayList;
import java.util.List;

import com.funnel.keep.utilities.CommonUtilities;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.customDialogs.ConfirmationDialog;
import com.funnel.keep.customDialogs.DialogListOfOptions;
import com.funnel.keep.customDialogs.ConfirmationDialog.OnActionClickListener;
import com.funnel.keep.customDialogs.DialogListOfOptions.DialolListItem;
import com.funnel.keep.customDialogs.DialogListOfOptions.OnOptionsDialogClickListener;
import com.funnel.keep.decorator.Constants;
import com.funnel.keep.decorator.PasswordItem;

public final class PasswordDetailFragment extends BaseFragment {
	
	
	//--------VIEW--------
	private TextView mainTitle;
	private LinearLayout accountLayout;
	private TextView account;
	private TextView passAccount;
	private TextView passText;
	private TextView passwordText;
	private TextView userName;
	private TextView passUserName;
	private LinearLayout userNameLayout;
	private LinearLayout descriptionlayout;
	private TextView description;
	private TextView passDescription;
	private LinearLayout bcakLayout;
	private FrameLayout changePasswordVisibilityLayout;
	private ImageView eyeImage;
	private FrameLayout copyTextToClipBoard;
	private FrameLayout editPassLayout;

	//--------OTHERS--------
	private int passwordID;
	private boolean eyeIsClosed = true;
	private PasswordItem passDetail;
	private DialogListOfOptions edirPasswordDialog;
	private ConfirmationDialog deletePasswordConfirmDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getBundleInfo();
		View v = inflater.inflate(R.layout.password_detail_fragment, container,
				false);

		copyTextToClipBoard = (FrameLayout) v
				.findViewById(R.id.FrameLayout_copy);
		bcakLayout = (LinearLayout) v.findViewById(R.id.back_layout);
		changePasswordVisibilityLayout = (FrameLayout) v
				.findViewById(R.id.FrameLayout_eye);
		eyeImage = (ImageView) v.findViewById(R.id.ImageView_eye);
		editPassLayout = (FrameLayout) v.findViewById(R.id.FrameLayout_edit);

		mainTitle = (TextView) v.findViewById(R.id.title_main);

		accountLayout = (LinearLayout) v
				.findViewById(R.id.LinearLayout_account);
		account = (TextView) v.findViewById(R.id.textView_account);
		passAccount = (TextView) v.findViewById(R.id.textView_passAccount);

		passText = (TextView) v.findViewById(R.id.textView_password);
		passwordText = (TextView) v.findViewById(R.id.editText_pass);
		passwordText.setTransformationMethod(PasswordTransformationMethod
				.getInstance());

		userName = (TextView) v.findViewById(R.id.textView_userName);
		passUserName = (TextView) v.findViewById(R.id.textView_passUserName);
		userNameLayout = (LinearLayout) v
				.findViewById(R.id.LinearLayout_username);

		descriptionlayout = (LinearLayout) v
				.findViewById(R.id.LinearLayout_description);
		description = (TextView) v.findViewById(R.id.textView_description);
		passDescription = (TextView) v
				.findViewById(R.id.textView_passDescription);

		List<DialolListItem> itemsOne = new ArrayList<DialolListItem>();
		itemsOne.add(new DialolListItem(R.drawable.edit_icon, getResources()
				.getString(R.string.edit_pass)));
		itemsOne.add(new DialolListItem(R.drawable.delete, getResources()
				.getString(R.string.delete_pass)));
		edirPasswordDialog = new DialogListOfOptions(main, itemsOne);

		deletePasswordConfirmDialog = new ConfirmationDialog(getActivity()
				.getResources().getString(R.string.are_you_sure_delete),
				getActivity().getResources().getString(R.string.cancel),
				getActivity().getResources().getString(R.string.yes));
		getPassordDetail();
		setListeners();
		return v;
	}

	private void setListeners() {

		edirPasswordDialog
				.setOneOptionClickListener(new OnOptionsDialogClickListener() {

					@Override
					public void OnOptionClicked(int optionPosition) {
						if (optionPosition == 1) {
							main.replaceFragment(AddNewPasswordFragment
									.getInstanceOfAddNewPasswordFragment(
											passDetail.itemTitle,
											passDetail.passwordAccount,
											passDetail.itemText,
											passDetail.passwordUsername,
											passDetail.passwordDescription,
											passDetail.itemBackgroundColor,
											false, passwordID));

						} else {
							deletePasswordConfirmDialog.show(
									getFragmentManager(), "");
						}

					}
				});

		deletePasswordConfirmDialog
				.setOnActionClickListener(new OnActionClickListener() {

					@Override
					public boolean onDismiss() {
						return false;

					}

					@Override
					public void onActionClicked() {
						if (main.getDataSource() != null) {
							if (main.getDataSource().deletePassword(passwordID) != 0) {
								main.startBroadcast(Constants.BROADCAST_KEY_RELOAD_PASSWORDS);
								main.getSupportFragmentManager().popBackStack();

							} else {
								CommonUtilities.handleError("",
										getFragmentManager());
							}
						}

					}
				});

		editPassLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edirPasswordDialog.show(getFragmentManager(), "");

			}
		});

		copyTextToClipBoard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				copyToClipBoard(passwordText.getText().toString());

			}
		});

		changePasswordVisibilityLayout
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (eyeIsClosed) {
							eyeImage.setImageResource(R.drawable.eye_open);
							passwordText
									.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());

							eyeIsClosed = false;
						} else {
							eyeImage.setImageResource(R.drawable.eye_close);

							passwordText
									.setTransformationMethod(PasswordTransformationMethod
											.getInstance());

							eyeIsClosed = true;
						}

					}
				});

		bcakLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.getSupportFragmentManager().popBackStack();

			}
		});

	}

	private void getBundleInfo() {
		this.passwordID = getArguments().getInt(
				Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_ID);

	}

	@Override
	public void onPause() {

		super.onPause();
	}

	private void getPassordDetail() {
		if (main.getDataSource() != null) {
			passDetail = main.getDataSource().getPasswordDetails(passwordID);
			if (passDetail != null) {
				mainTitle.setText(passDetail.itemTitle);
				if (passDetail.passwordAccount != null
						&& passDetail.passwordAccount.length() > 0) {
					passAccount.setText(passDetail.passwordAccount);
				} else {
					accountLayout.setVisibility(View.GONE);
				}
				passwordText.setText(passDetail.itemText);

				if (passDetail.passwordUsername != null
						&& passDetail.passwordUsername.length() > 0) {
					passUserName.setText(passDetail.passwordUsername);
				} else {
					userNameLayout.setVisibility(View.GONE);
				}

				if (passDetail.passwordDescription != null
						&& passDetail.passwordDescription.length() > 0) {
					passDescription.setText(passDetail.passwordDescription);

				} else {
					descriptionlayout.setVisibility(View.GONE);
				}

				setPassColor(passDetail.itemBackgroundColor);
			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}

	}

	private void setPassColor(int color) {
		switch (color) {
		case PasswordItem.PASSWORD_ICON_BG_COLOR_NORMAL:
			color = getActivity().getResources().getColor(
					R.color.icon_bg_default_color);
			break;

		case PasswordItem.PASSWORD_ICON_BG_COLOR_BLUE:

			color = getActivity().getResources().getColor(R.color.icon_bg_blue);

			break;

		case PasswordItem.PASSWORD_ICON_BG_COLOR_RED:

			color = getActivity().getResources().getColor(R.color.icon_bg_red);

			break;
		}

		account.setTextColor(color);
		passText.setTextColor(color);
		userName.setTextColor(color);
		description.setTextColor(color);

	}

	public static PasswordDetailFragment getInstanceOfPasswordDetailFragment(
			int passwordID) {
		PasswordDetailFragment fragment = new PasswordDetailFragment();
		Bundle args = new Bundle();
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_PASSWORD_ID, passwordID);

		fragment.setArguments(args);
		return fragment;

	}

	private void copyToClipBoard(String text) {

		ClipboardManager clipboard = (ClipboardManager) getActivity()
				.getSystemService(Activity.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("label", text);
		clipboard.setPrimaryClip(clip);
		CommonUtilities.Toast(getActivity(),
				main.getResources().getString(R.string.text_cpied_clip));
	}


	

}
