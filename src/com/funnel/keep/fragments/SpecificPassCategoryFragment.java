package com.funnel.keep.fragments;

import java.util.ArrayList;
import java.util.List;

import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.PasswordListAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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
import com.funnel.keep.decorator.PasswordItem;

public final class SpecificPassCategoryFragment extends BaseFragment implements
		OnItemClickListener {
	// view
	private TextView categoryTitleTextView;
	private RelativeLayout emptyPageLayout;
	private FrameLayout doneLayout;
	private LinearLayout backLayout;
	private ListView passwordsListView;

	// other
	private String categoryTitleString;
	private int categoryID;
	private PasswordListAdapter adapter;
	private int passwordPositionToDelete;
	private ConfirmationDialog deletePasswordConfirmDialog;
	private DialogListOfOptions dialogListOfOptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		adapter = new PasswordListAdapter(getActivity(), 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.specific_category_fragment,
				container, false);
		getBundleInfo();
		emptyPageLayout = (RelativeLayout) v.findViewById(R.id.emptyPageLayout);
		categoryTitleTextView = (TextView) v.findViewById(R.id.title);
		categoryTitleTextView.setText(categoryTitleString);
		passwordsListView = (ListView) v
				.findViewById(R.id.listView_passwordListView);
		passwordsListView.setAdapter(adapter);

		doneLayout = (FrameLayout) v.findViewById(R.id.done_layout);
		backLayout = (LinearLayout) v.findViewById(R.id.back_layout);
		setDialogs();

		setlisteners();
		getAllPassword();

		return v;
	}

	private void setDialogs() {
		List<DialolListItem> items = new ArrayList<DialolListItem>();
		items.add(new DialolListItem(R.drawable.delete, getResources()
				.getString(R.string.delete)));
		dialogListOfOptions = new DialogListOfOptions(main, items);
		deletePasswordConfirmDialog = new ConfirmationDialog(getActivity()
				.getResources().getString(R.string.are_you_sure_delete),
				getActivity().getResources().getString(R.string.cancel),
				getActivity().getResources().getString(R.string.yes));

	}

	private void getAllPassword() {
		if (main.getDataSource() != null) {

			List<PasswordItem> items = main.getDataSource()
					.gePasswordsForCategory(categoryID);
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

		passwordsListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int pos, long id) {
						passwordPositionToDelete = pos;
						dialogListOfOptions.show(getActivity()
								.getSupportFragmentManager(), null);

						return true;
					}
				});

		passwordsListView.setOnItemClickListener(this);

		deletePasswordConfirmDialog
				.setOnActionClickListener(new OnActionClickListener() {

					@Override
					public boolean onDismiss() {
						return false;

					}

					@Override
					public void onActionClicked() {
						if (main.getDataSource() != null) {
							if (main.getDataSource()
									.deletePassword(
											adapter.getItem(passwordPositionToDelete).itemID) != 0) {
								adapter.remove(adapter
										.getItem(passwordPositionToDelete));
								adapter.notifyDataSetChanged();
								if (adapter.isEmpty()) {
									emptyPageLayout.setVisibility(View.VISIBLE);
								} else {
									emptyPageLayout.setVisibility(View.GONE);
								}
								main.startBroadcast(Constants.BROADCAST_KEY_RELOAD_PASSWORDS_CATEGORIES_PAGE);
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
							deletePasswordConfirmDialog.show(getActivity()
									.getSupportFragmentManager(), null);
							break;

						default:

							break;
						}

					}
				});
		doneLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AddNewPasswordFragment fragment = AddNewPasswordFragment
						.newInstanceOfAddNewPasswordFragment(categoryID, true);
				main.replaceFragment(fragment);
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

	public static SpecificPassCategoryFragment newInstanceOfSpecificCategoryFragment(
			String categoryTitle, int categoryID) {
		SpecificPassCategoryFragment myFragment = new SpecificPassCategoryFragment();
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
		intentFilter.addAction(Constants.BROADCAST_KEY_RELOAD_PASSWORDS);
		intentFilter
				.addAction(Constants.BROADCAST_KEY_RELOAD_SPECIFIC_PASSWORD_CATEGORIES_PAGE);

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
					Constants.BROADCAST_KEY_RELOAD_PASSWORDS)
					|| intent
							.getAction()
							.equals(Constants.BROADCAST_KEY_RELOAD_SPECIFIC_PASSWORD_CATEGORIES_PAGE)) {

				adapter.clear();
				getAllPassword();

			}

		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		PasswordItem item = adapter.getItem(arg2);
		main.replaceFragment(PasswordDetailFragment
				.getInstanceOfPasswordDetailFragment(item.itemID));

	}
}
