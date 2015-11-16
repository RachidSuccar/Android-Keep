package com.funnel.keep.fragments;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.funnel.keep.requestObjects.DeviceInfoRequestObject;
import com.funnel.keep.requestObjects.RegiterSessionReqestObject;
import com.funnel.keep.responseObject.BaseResponseObject;
import com.funnel.keep.serverManager.DeviceRegistrationManager;
import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.activities.LoginActivity;
import com.funnel.keep.activities.QrScannerActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.funnel.keep.base.BaseItem;

import com.funnel.keep.R;

import com.funnel.keep.customDialogs.DialogListOfOptions;
import com.funnel.keep.customDialogs.LoadingDialog;
import com.funnel.keep.customDialogs.DialogListOfOptions.DialolListItem;
import com.funnel.keep.customDialogs.DialogListOfOptions.OnOptionsDialogClickListener;
import com.funnel.keep.decorator.Constants;

public final class WebTextsFragment extends BaseFragment {

	// --------VIEW--------
	private FrameLayout scanQRLayout, editLayout;
	private LinearLayout backLayout;
	private ListView listView;
	private RelativeLayout emptyLayout;

	// --------OTHERS--------
	private LoadingDialog loadingDialog;
	private DialogListOfOptions edirPasswordDialog;
	private WebTextsAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.web_texts_fragment, container, false);
		scanQRLayout = (FrameLayout) v.findViewById(R.id.scanQR);
		backLayout = (LinearLayout) v.findViewById(R.id.back_layout);
		editLayout = (FrameLayout) v.findViewById(R.id.FrameLayout_edit);
		listView = (ListView) v.findViewById(R.id.listView_webTexts);
		emptyLayout = (RelativeLayout) v.findViewById(R.id.emptyLayout);
		initPage();
		setListeners();
		getPageInfo();
		return v;
	}

	private void getPageInfo() {
		if (main.getDataSource() != null) {
			adapter.clear();
			List<BaseItem> items = main.getDataSource().getAllWebTexts();

			if (items != null) {
				if (items.size() == 0) {
					emptyLayout.setVisibility(View.VISIBLE);
				} else {
					emptyLayout.setVisibility(View.GONE);
				}
				adapter.addAll(items);
				adapter.notifyDataSetChanged();
			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}

	}

	private void unlinkBrowserForDEvice() {
		if (CommonUtilities.isNetworkAvailable(main)) {
			loadingDialog.ShowDialog();
			DeviceInfoRequestObject ob = new DeviceInfoRequestObject();
			ob.DeviceName = CommonUtilities.GetDeviceModel();
			ob.DeviceUniqueID = CommonUtilities.GetDeviceID(main);
			ob.OsVersion = CommonUtilities.GetDeviceOperationSystem();
			WebHandler handler = new WebHandler(WebTextsFragment.this,
					WebHandler.UNLINK_ALL_BROWSERS);
			DeviceRegistrationManager.sharedInstance()
					.unlinkAllBrowserForDevice(ob, handler);
		} else {
			CommonUtilities.handleError("Please connect to the internet!",
					getFragmentManager());
		}

	}

	private void setListeners() {

		edirPasswordDialog
				.setOneOptionClickListener(new OnOptionsDialogClickListener() {

					@Override
					public void OnOptionClicked(int optionPosition) {
						if (optionPosition == 1) {
							unlinkBrowserForDEvice();

						} else {
							if (main.getDataSource() != null) {
								main.getDataSource().deleteWebTexts();
								adapter.clear();
								adapter.notifyDataSetChanged();
								emptyLayout.setVisibility(View.VISIBLE);
							}
						}

					}
				});

		editLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edirPasswordDialog.show(getFragmentManager(), "");

			}
		});
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.getSupportFragmentManager().popBackStack();

			}
		});
		scanQRLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonUtilities.getAutoExitPref(main)) {
					CommonUtilities.setAutoExitPref(main, false);
					CommonUtilities.setShouldSetAutoExitToTrue(main, true);
				}
				Intent qrIntnet = new Intent(main, QrScannerActivity.class);
				startActivityForResult(qrIntnet,
						Constants.INTENT_QR_COD_SCANNER);

			}
		});

	}

	private void initPage() {
		adapter = new WebTextsAdapter(main);
		listView.setAdapter(adapter);
		loadingDialog = new LoadingDialog("Loading...", main);
		List<DialolListItem> itemsOne = new ArrayList<DialolListItem>();
		itemsOne.add(new DialolListItem(R.drawable.auto_ex_blue, getResources()
				.getString(R.string.logout_web)));
		itemsOne.add(new DialolListItem(R.drawable.delete, getResources()
				.getString(R.string.clea_web_text)));
		edirPasswordDialog = new DialogListOfOptions(main, itemsOne);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK
				&& requestCode == Constants.INTENT_QR_COD_SCANNER) {

			Bundle MBuddle = data.getExtras();
			String MMessage = MBuddle.getString(Constants.INTENT_WEB_ID);

			// CommonUtilities.handleError(MMessage , getFragmentManager());
			handleQrCode(MMessage);

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void handleQrCode(String browserFingerPrint) {
		if (CommonUtilities.isNetworkAvailable(main)) {
			vibrate();
			loadingDialog.ShowDialog();
			RegiterSessionReqestObject requestObject = new RegiterSessionReqestObject();
			requestObject.DeviceName = CommonUtilities.GetDeviceModel();
			requestObject.DeviceUniqueID = CommonUtilities.GetDeviceID(main);
			requestObject.BrowserFingerprint = browserFingerPrint;
			requestObject.OsVersion = CommonUtilities
					.GetDeviceOperationSystem();
			WebHandler handler = new WebHandler(WebTextsFragment.this,
					WebHandler.LINK_BROWSER_TO_DVICE, browserFingerPrint);
			DeviceRegistrationManager.sharedInstance().addBrowser(
					requestObject, handler, main);
		} else {
			CommonUtilities.handleError(
					"Please connect to the internet before scanning QR code",
					getFragmentManager());
		}

	}

	private void vibrate() {
		Vibrator vb = (Vibrator) main
				.getSystemService(LoginActivity.VIBRATOR_SERVICE);
		vb.vibrate(100);

	}

	private class WebTextsAdapter extends ArrayAdapter<BaseItem> {
		Context context;

		public WebTextsAdapter(Context context) {

			super(context, 0);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			BaseItem selectedItem = getItem(position);
			final String clipboard = selectedItem.itemText;
			WebListViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.web_list_row, parent, false);
				viewHolder = new WebListViewHolder();
				viewHolder.viewHolderWebTextView = (TextView) convertView
						.findViewById(R.id.webText);
				viewHolder.viewHolderDateTextView = (TextView) convertView
						.findViewById(R.id.webDate);
				viewHolder.viewHolderCopyToClipboradLayout = (RelativeLayout) convertView
						.findViewById(R.id.copyLayout);

				convertView.setTag(viewHolder);

			} else {
				viewHolder = (WebListViewHolder) convertView.getTag();
			}

			viewHolder.viewHolderWebTextView.setText(selectedItem.itemText);
			viewHolder.viewHolderDateTextView.setText(CommonUtilities
					.convertSecondsToTimeAgo(CommonUtilities.timeInSecond()
							- selectedItem.itemCreationDate));
			viewHolder.viewHolderCopyToClipboradLayout
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							CommonUtilities.copyToClipBoard(clipboard, context,
									"Content copied to clipboard");

						}
					});
			return convertView;
		}

		private class WebListViewHolder {

			public TextView viewHolderWebTextView;
			public TextView viewHolderDateTextView;
			public RelativeLayout viewHolderCopyToClipboradLayout;
		}

	}

	public static class WebHandler extends Handler {

		private final WeakReference<WebTextsFragment> fragmentRef;
		private int action;
		public static final int LINK_BROWSER_TO_DVICE = 1;
		public static final int UNLINK_ALL_BROWSERS = 2;

		public WebHandler(WebTextsFragment _fragment, int _action) {
			this.fragmentRef = new WeakReference<WebTextsFragment>(_fragment);
			this.action = _action;

		}

		public WebHandler(WebTextsFragment _fragment, int _action, String key) {
			this.fragmentRef = new WeakReference<WebTextsFragment>(_fragment);
			this.action = _action;

		}

		@Override
		public void handleMessage(Message msg) {

			WebTextsFragment fragment = fragmentRef.get();

			if (fragment != null && fragment.getView() != null) {

				switch (action) {
				case LINK_BROWSER_TO_DVICE:
					fragment.loadingDialog.HideDialog();
					if (msg.what == Constants.HANDLER_SUCCESS) {
						BaseResponseObject response = (BaseResponseObject) msg.obj;

						if (response.RequestStatus == Constants.STATUS_SUCCESS) {
							CommonUtilities
									.handleError(
											"Your device has been linked successfully, the website should reload automatically. Please refresh your browser otherwise",
											fragment.getActivity()
													.getSupportFragmentManager());
						} else if (response.RequestStatus == Constants.DUPLICATED_BROWSER_ID) {
							CommonUtilities
									.Toast(fragment.main,
											"Please refresh your browser and try again");
						}

						else {
							CommonUtilities.handleError(null, fragment
									.getActivity().getSupportFragmentManager());
						}
					}

					else if (msg.what == Constants.NO_VALID_GOOGLE_PLAY_SERVICS_APK_FOUND) {
						CommonUtilities.Toast(fragment.main,
								"No Valid Google Play Services APK Found");
					}

					else {

						CommonUtilities.Toast(fragment.main,
								"Weak internet connection");

					}
					break;

				case UNLINK_ALL_BROWSERS:
					fragment.loadingDialog.HideDialog();
					if (msg.what == Constants.HANDLER_SUCCESS) {
						BaseResponseObject response = (BaseResponseObject) msg.obj;

						if (response.RequestStatus == Constants.STATUS_SUCCESS) {
							CommonUtilities.handleError(response.ExtraString,
									fragment.getActivity()
											.getSupportFragmentManager());
						}

						else {
							CommonUtilities.handleError(null, fragment
									.getActivity().getSupportFragmentManager());
						}
					}

					else {

						CommonUtilities.Toast(fragment.main,
								"Weak internet connection");

					}
					break;

				}
			}
		}
	}

	@Override
	public void onStart() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.BROADCAST_KEY_RELOAD_WEB_TEXTS);
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
			if (intent.getAction().equals(
					Constants.BROADCAST_KEY_RELOAD_WEB_TEXTS)) {
				getPageInfo();
			}

		}
	};

}
