package com.funnel.keep.fragments;

import java.lang.ref.WeakReference;

import javax.crypto.spec.SecretKeySpec;

import com.funnel.keep.requestObjects.BackupRequestObject;
import com.funnel.keep.requestObjects.RestoreRequestObject;
import com.funnel.keep.responseObject.BaseResponseObject;
import com.funnel.keep.responseObject.RestoreResponseObject;
import com.funnel.keep.serverManager.SyncManager;
import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.Cryptography;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.customDialogs.LoadingDialog;
import com.funnel.keep.decorator.Constants;

public final class SyncFragment extends BaseFragment {
	
	//--------VIEW--------
	private LinearLayout backLayout;
	private TextView mainTitle;
	private FrameLayout donelayout;
	private EditText userMail;
	private EditText userPassword;
	private TextView pageHint;
	
	//--------OTHERS--------
	private boolean backUpFragment = true;
	private Drawable emailRed;
	private Drawable emailGray;
	private Drawable passRed;
	private Drawable passGray;
	private LoadingDialog loadingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.sync_fragment, container, false);
		getBundleInfo();
		backLayout = (LinearLayout) v.findViewById(R.id.back_layout);
		mainTitle = (TextView) v.findViewById(R.id.title_main);
		donelayout = (FrameLayout) v.findViewById(R.id.done_layout);
		userMail = (EditText) v.findViewById(R.id.editText_email);
		pageHint = (TextView) v.findViewById(R.id.pageHint);
		userPassword = (EditText) v.findViewById(R.id.editText_Pass);
		if (backUpFragment) {
			userPassword.setHint(getString(R.string.app_password));
			mainTitle.setText(getString(R.string.backup));
			pageHint.setText(getString(R.string.backup_page_hint));
		} else {
			userPassword.setHint(getString(R.string.backup_password));
			mainTitle.setText(getString(R.string.restore));
			pageHint.setText(getString(R.string.restore_page_hint));
		}
		CommonUtilities.showKeyBoard(main);

		emailRed = getActivity().getResources().getDrawable(
				R.drawable.email_red);
		emailGray = getActivity().getResources().getDrawable(
				R.drawable.email_l_g);

		passRed = getActivity().getResources().getDrawable(R.drawable.lock_red);
		passGray = getActivity().getResources()
				.getDrawable(R.drawable.lock_l_g);
		userMail.setText(CommonUtilities.getEmail(main));
		userMail.setSelection(userMail.getText().length());
		loadingDialog = new LoadingDialog("Loading...", main);
		setlisteners();
		return v;
	}

	private void getBundleInfo() {
		this.backUpFragment = getArguments().getBoolean(
				Constants.FRAGMENT_NEW_INSTANCE_BACKUP);
	}

	private void setlisteners() {

		userMail.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				userMail.setCompoundDrawablesWithIntrinsicBounds(emailGray,
						null, null, null);

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

		userPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				userPassword.setCompoundDrawablesWithIntrinsicBounds(passGray,
						null, null, null);

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

		donelayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (backUpFragment) {

					backUpDataBase();

				} else {
					restoreDatabase();
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
		CommonUtilities.hideKeyboard(getActivity(), userMail);
		super.onPause();
	}

	@Override
	public void onResume() {

		super.onResume();
	}

	private void backUpDataBase() {
		if (main.getDataSource() != null && checkBeforeDoneIsPressed()) {
			CommonUtilities.hideKeyboard(getActivity(), userMail);
			loadingDialog.ShowDialog();
			SyncHandler handler = new SyncHandler(this,
					SyncHandler.BACKUP_DATABASE);
			BackupRequestObject backupObject = new BackupRequestObject();
			backupObject.HashedPassword = Cryptography.hashMD5(userPassword
					.getText().toString());
			backupObject.PassCategories = main.getDataSource()
					.getUserPasswords();
			backupObject.NoteCategories = main.getDataSource()
					.geNotesForCategory();
			backupObject.UniqueIdentifier = CommonUtilities.GetDeviceID(main);
			backupObject.DeviceName = CommonUtilities.GetDeviceModel();
			backupObject.OsVersion = CommonUtilities.GetDeviceOperationSystem();
			backupObject.Email = userMail.getText().toString();
			if ((backupObject.PassCategories != null && backupObject.PassCategories
					.size() > 0)
					|| (backupObject.NoteCategories != null && backupObject.NoteCategories
							.size() > 0)) {
				SyncManager.sharedInstance().backupUserPasswords(backupObject,
						handler);
			} else {
				CommonUtilities.handleError(
						getString(R.string.wrong_application_password),
						getFragmentManager());
				loadingDialog.HideDialog();
			}

		}

	}

	private void restoreDatabase() {

		loadingDialog.ShowDialog();
		CommonUtilities.hideKeyboard(getActivity(), userMail);
		SyncHandler handler = new SyncHandler(this,
				SyncHandler.RESTORE_DATABASE, userPassword.getText().toString());
		RestoreRequestObject objectToSend = new RestoreRequestObject();
		objectToSend.HashedPassword = Cryptography.hashMD5(userPassword
				.getText().toString());
		objectToSend.Email = userMail.getText().toString();
		SyncManager.sharedInstance()
				.restoreUserPasswords(objectToSend, handler);

	}

	public static SyncFragment getInstanceOfSyncFragment(boolean backUp) {
		SyncFragment fragment = new SyncFragment();
		Bundle args = new Bundle();
		args.putBoolean(Constants.FRAGMENT_NEW_INSTANCE_BACKUP, backUp);

		fragment.setArguments(args);
		return fragment;
	}

	private boolean checkBeforeDoneIsPressed() {
		if (userMail.getText().toString() != null
				&& userMail.getText().toString().length() > 0) {

			if (userPassword.getText().toString() != null
					&& userPassword.getText().toString().length() > 0) {

				if (Cryptography.hashMD5(userPassword.getText().toString())
						.equals(main.getDataSource().getPassword())) {
					return true;
				} else {
					CommonUtilities.Toast(
							main,
							main.getResources().getString(
									R.string.password_wrong));
					userPassword.setCompoundDrawablesWithIntrinsicBounds(
							passRed, null, null, null);
					return false;
				}

			} else {

				CommonUtilities.Toast(
						main,
						main.getResources().getString(
								R.string.password_should_not_be_empty));
				userPassword.setCompoundDrawablesWithIntrinsicBounds(passRed,
						null, null, null);
				return false;

			}

		} else {

			CommonUtilities.Toast(
					main,
					main.getResources().getString(
							R.string.email_should_not_be_empty));
			userMail.setCompoundDrawablesWithIntrinsicBounds(emailRed, null,
					null, null);
			return false;
		}

	}

	private void handleBackupSuccess() {
		CommonUtilities.Toast(main,
				main.getString(R.string.update_successfully));
		main.getSupportFragmentManager().popBackStack();
		CommonUtilities.setEmail(main, userMail.getText().toString());
		CommonUtilities.setLastUpdateDATE(main);
		CommonUtilities.setUpdateBit(main, true);
	}

	private void handleWrongEmailFormat() {
		userMail.setCompoundDrawablesWithIntrinsicBounds(emailRed, null, null,
				null);
		CommonUtilities.Toast(main, "wrong email format");
	}

	private void handleWrongEmailOrPassword() {

		userPassword.setCompoundDrawablesWithIntrinsicBounds(passRed, null,
				null, null);
		CommonUtilities.Toast(main, "wrong email or password");
	}

	private void handleRestoreInfo(RestoreResponseObject restoreResponseObject,
			String key) {

		SecretKeySpec secretKeySpec = Cryptography.getMySecretKey128Bits(key);

		if (main.getDataSource() != null) {
			main.getDataSource().restoreUserInfos(restoreResponseObject,
					secretKeySpec);
			CommonUtilities.setEmail(main, userMail.getText().toString());
			main.startBroadcast(Constants.BROADCAST_KEY_RESTORE_DATA);
			CommonUtilities.setRestoreDATE(main);
			CommonUtilities.setRestoreBit(main, true);
			CommonUtilities.Toast(main,
					main.getString(R.string.restore_successfully));
			main.getSupportFragmentManager().popBackStack();
		}

	}

	public static class SyncHandler extends Handler {

		private final WeakReference<SyncFragment> fragmentRef;
		private int action;
		public static final int BACKUP_DATABASE = 1;
		public static final int RESTORE_DATABASE = 2;
		private String key;

		public SyncHandler(SyncFragment _fragment, int _action) {
			this.fragmentRef = new WeakReference<SyncFragment>(_fragment);
			this.action = _action;

		}

		public SyncHandler(SyncFragment _fragment, int _action, String key) {
			this.fragmentRef = new WeakReference<SyncFragment>(_fragment);
			this.action = _action;
			this.key = key;

		}

		@Override
		public void handleMessage(Message msg) {

			SyncFragment fragment = fragmentRef.get();

			if (fragment != null && fragment.getView() != null) {

				switch (action) {
				case BACKUP_DATABASE:
					fragment.loadingDialog.HideDialog();
					if (msg.what == Constants.HANDLER_SUCCESS) {
						BaseResponseObject response = (BaseResponseObject) msg.obj;

						if (response.RequestStatus == Constants.STATUS_SUCCESS) {

							fragment.handleBackupSuccess();

						}

						else if (response.RequestStatus == Constants.WRONG_EMAIL_FORMAT) {
							fragment.handleWrongEmailFormat();
						}

						else if (response.RequestStatus == Constants.WRONG_EMAIL_OR_DISPLAYNAME) {
							fragment.handleWrongEmailOrPassword();
						} else {
							CommonUtilities.handleError(null, fragment
									.getActivity().getSupportFragmentManager());
						}
					} else {

						CommonUtilities.Toast(fragment.main,
								"Weak internet connection");

					}
					break;

				case RESTORE_DATABASE:
					fragment.loadingDialog.HideDialog();
					if (msg.what == Constants.HANDLER_SUCCESS) {
						RestoreResponseObject response = (RestoreResponseObject) msg.obj;

						if (response.RequestStatus == Constants.STATUS_SUCCESS) {
							fragment.handleRestoreInfo(response, key);

						}

						else if (response.RequestStatus == Constants.WRONG_EMAIL_OR_DISPLAYNAME) {
							fragment.handleWrongEmailOrPassword();

						} else {
							CommonUtilities.handleError(null, fragment
									.getActivity().getSupportFragmentManager());
						}

					} else {

						CommonUtilities.Toast(fragment.main,
								"Weak internet connection");

					}
					break;

				}
			}
		}
	}
}
