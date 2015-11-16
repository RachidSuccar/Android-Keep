package com.funnel.keep.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.funnel.keep.utilities.CategoriesListAdapter;
import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.ImageUtil;
import com.funnel.keep.utilities.Utilities;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.customDialogs.ConfirmationDialog;
import com.funnel.keep.customDialogs.DialogListOfOptions;
import com.funnel.keep.customDialogs.EditTextDialog;
import com.funnel.keep.customDialogs.ConfirmationDialog.OnActionClickListener;
import com.funnel.keep.customDialogs.DialogListOfOptions.DialolListItem;
import com.funnel.keep.customDialogs.DialogListOfOptions.OnOptionsDialogClickListener;
import com.funnel.keep.customDialogs.EditTextDialog.OnEditTextDialogListener;
import com.funnel.keep.decorator.CategoryItems;
import com.funnel.keep.decorator.Constants;
import com.funnel.keep.floatingButton.FloatingActionButton;

public final class CategoriesFragment extends BaseFragment implements
		OnItemClickListener, OnOptionsDialogClickListener {
	
	//--------CATEGORIES--------
	public static final int PASSWORD_CATEGORIES = 1;
	public static final int PICTURE_CATEGORIES = 2;
	public static final int NOTE_CATEGORIES = 3;

	//--------OPERATION TYPES--------
	private enum OperationType {
		pageInfo, listClickListener, broadcastReceiver, setEmptyPagePic, deleteCategory, editCategoryname
	}

	//--------VIEW--------
	private ListView categListView;
	private FloatingActionButton addNewCate;
	private RelativeLayout emptyPage;
	private ImageView emptyPagePic;
	private TextView emptyPageTitle;

	//--------OTHERS--------
	private DialogListOfOptions optionsDialog;
	private ConfirmationDialog deleteCategoryConfirmDialog;
	private EditTextDialog editCategoryTitleDialog;
	private CategoriesListAdapter adapter;
	private int categoryID;
	private int selectedCategoryPostion;
	private String selectedCategoryName;
	private boolean isVirgin = true;
	private boolean pageIdEmpty;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		adapter = new CategoriesListAdapter(getActivity(), 0);
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(
				R.layout.categories_fragment, container, false);
		getBundleInfo();
		addNewCate = (FloatingActionButton) fragmentView
				.findViewById(R.id.floatingActionButton);
		addNewCate.setColorNormal(main.getResources().getColor(
				R.color.app_color));
		emptyPageTitle = (TextView) fragmentView
				.findViewById(R.id.emptyPageTtitle);
		emptyPagePic = (ImageView) fragmentView
				.findViewById(R.id.emptyPagePic);
		addNewCate.setColorPressed(main.getResources().getColor(
				R.color.app_color));
		emptyPage = (RelativeLayout) fragmentView
				.findViewById(R.id.emptyPageLayout);
		addNewCate.setType(FloatingActionButton.TYPE_MINI);
		categListView = (ListView) fragmentView
				.findViewById(R.id.listView_categoriesListView);
		addNewCate.attachToListView(categListView);
		categListView.setAdapter(adapter);
		categListView.setOnItemClickListener(this);
		setDialogs();
		setListener();
		getOperationDone(OperationType.setEmptyPagePic);
		if (isVirgin) {
			isVirgin = false;
			getOperationDone(OperationType.pageInfo);
		}
		checkIfPageIsEmpty();
		return fragmentView;
	}

	private void checkIfPageIsEmpty() {
		if (pageIdEmpty) {
			emptyPage.setVisibility(View.VISIBLE);
		} else {
			emptyPage.setVisibility(View.GONE);

		}

	}

	private void setListener() {

		editCategoryTitleDialog
				.setOnEditTextDialogClickListener(new OnEditTextDialogListener() {

					@Override
					public boolean onDismiss() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean onActionClicked(String editTextString) {
						editTextString = Utilities
								.removeSpacesFromText(editTextString);
						getOperationDone(OperationType.editCategoryname,
								Utilities.removeSpacesFromText(editTextString));
						return true;
					}
				});

		deleteCategoryConfirmDialog
				.setOnActionClickListener(new OnActionClickListener() {

					@Override
					public boolean onDismiss() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void onActionClicked() {
						getOperationDone(OperationType.deleteCategory);

					}
				});

		addNewCate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToAddNewCategoryFragment();

			}
		});

		categListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				selectedCategoryPostion = pos;
				selectedCategoryName = adapter.getItem(pos).title;
				optionsDialog.show(main.getSupportFragmentManager(), "");
				return true;
			}
		});
	};

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			getOperationDone(OperationType.broadcastReceiver,
					intent.getAction());

		}
	};

	@Override
	public void onStart() {
		
		// BROADCAST
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(Constants.BROADCAST_KEY_RELOAD_NOTES_CATEGORIES_PAGE);
		intentFilter
				.addAction(Constants.BROADCAST_KEY_RELOAD_PASSWORDS_CATEGORIES_PAGE);

		intentFilter.addAction(Constants.BROADCAST_KEY_RELOAD_NOTES);
		intentFilter
				.addAction(Constants.BROADCAST_KEY_RELOAD_PICTURES_CATEGORIES_PAGE);
		intentFilter.addAction(Constants.BROADCAST_KEY_RELOAD_PASSWORDS);
		intentFilter.addAction(Constants.BROADCAST_KEY_RESTORE_DATA);

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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		getOperationDone(OperationType.listClickListener, position);

	}

	public static CategoriesFragment getInstanceOfCategFrag(int categoryID) {
		CategoriesFragment fragment = new CategoriesFragment();
		Bundle args = new Bundle();
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID, categoryID);

		fragment.setArguments(args);
		return fragment;
	}

	private void getBundleInfo() {
		this.categoryID = getArguments().getInt(
				Constants.FRAGMENT_NEW_INSTANCE_CATEGORY_ID);

	}

	private void setDialogs() {
		// OPTIONS DIALOG
		List<DialolListItem> items = new ArrayList<DialolListItem>();
		items.add(new DialolListItem(R.drawable.edit_icon, getResources()
				.getString(R.string.edit_title)));
		items.add(new DialolListItem(R.drawable.delete, getResources()
				.getString(R.string.delete)));
		optionsDialog = new DialogListOfOptions(main, items);
		optionsDialog.setOneOptionClickListener(this);
		// CONFIRMATION DIALOG
		deleteCategoryConfirmDialog = new ConfirmationDialog(getActivity()
				.getResources().getString(R.string.are_you_sure_delete_cate),
				getActivity().getResources().getString(R.string.cancel),
				getActivity().getResources().getString(R.string.yes));
		// EDIT CATEGORY TITLE DIALOG
		editCategoryTitleDialog = new EditTextDialog(main.getResources()
				.getString(R.string.edit_title), main.getResources().getString(
				R.string.cancel), main.getResources().getString(R.string.done),
				main.getResources().getString(R.string.edit_title_hint));
	}

	// NO ADDITIONAL INFO
	private void getOperationDone(OperationType perationType) {
		getOperationDone(perationType, 0, null);
	}

	// STRING ADDITIONAL INFO
	private void getOperationDone(OperationType perationType,
			String stringAddInfo) {
		getOperationDone(perationType, 0, stringAddInfo);
	}

	// INT ADDITIONAL INFO
	private void getOperationDone(OperationType perationType, int additionalInfo) {
		getOperationDone(perationType, additionalInfo, null);
	}

	private void getOperationDone(OperationType perationType,
			int intAdditionalInfo, String stringAdditionalInfo) {
		switch (categoryID) {
		// PASSWORD CATEGORIES>>>>>>>>>>>>>>>>>>>>>
		case PASSWORD_CATEGORIES:
			switch (perationType) {
			case pageInfo:
				getPassCategoriesPageInfo();
				break;
			case listClickListener:
				onPassCategoryListClickListener(intAdditionalInfo);
				break;
			case broadcastReceiver:
				passCategoriesBroadcastReceiver(stringAdditionalInfo);
				break;
			case setEmptyPagePic:
				setEmptyPageImage(R.drawable.empty_pass,
						main.getString(R.string.empty));
				break;

			case deleteCategory:
				deletePassCategory();
				break;

			case editCategoryname:
				editPassCategoryName(stringAdditionalInfo);
				break;

			}

			break;
		// PICTURE CATEGORIES>>>>>>>>>>>>>>>>>>>>>
		case PICTURE_CATEGORIES:
			switch (perationType) {
			case pageInfo:
				getPicCategoriesPageInfo();
				break;

			case listClickListener:
				onPicCategoryListClickListener(intAdditionalInfo);

				break;
			case broadcastReceiver:
				picCategoriesBroadcastReceiver(stringAdditionalInfo);
				break;

			case setEmptyPagePic:
				setEmptyPageImage(R.drawable.empty_album,
						main.getString(R.string.empty));
				break;

			case deleteCategory:
				deletePicCategoryName();
				break;

			case editCategoryname:
				editPicCategoryName(stringAdditionalInfo);
				break;

			}

			break;
		// NOTE CATEGORIES>>>>>>>>>>>>>>>>>>>>>
		case NOTE_CATEGORIES:
			switch (perationType) {
			case pageInfo:
				getNotesCategoriesPageInfo();
				break;

			case listClickListener:
				onNoteCategoryListClickListener(intAdditionalInfo);
				break;
			case broadcastReceiver:
				notesCategoriesBroadcastReceiver(stringAdditionalInfo);
				break;

			case setEmptyPagePic:
				setEmptyPageImage(R.drawable.empty_notes,
						main.getString(R.string.empty));
				break;

			case deleteCategory:
				deleteNoteCategory();
				break;

			case editCategoryname:
				editNoteCategoryName(stringAdditionalInfo);
				break;

			}

		}

	}

	
	private void getPicCategoriesPageInfo() {
		if (main.getDataSource() != null) {
			adapter.clear();
			List<CategoryItems> items = main.getDataSource()
					.getPicturesCategories(R.drawable.albums);

			if (items != null) {
				if (items.size() == 0) {
					pageIdEmpty = true;
				} else {
					pageIdEmpty = false;
				}
				adapter.addAll(items);
				adapter.notifyDataSetChanged();
			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}
		checkIfPageIsEmpty();
	}

	private void onPicCategoryListClickListener(int postion) {
		main.replaceFragment(SpecificPicCategoryFragment
				.newInstanceOfSpecificAlbumFragment(
						adapter.getItem(postion).title,
						adapter.getItem(postion).id,
						adapter.getItem(postion).count));

	}

	private void picCategoriesBroadcastReceiver(String intentAction) {
		if (intentAction
				.equals(Constants.BROADCAST_KEY_RELOAD_PICTURES_CATEGORIES_PAGE)) {

			getOperationDone(OperationType.pageInfo);
		}

	}

	private void editPicCategoryName(String title) {

		File albumsMainDirectory = new File(
				Environment.getExternalStorageDirectory()
						+ ImageUtil.DIRECTORY_FILE_ALBUM);
		albumsMainDirectory.mkdirs();
		File imageAlbumName = new File(albumsMainDirectory,
				selectedCategoryName);

		imageAlbumName.mkdir();
		if (imageAlbumName.isDirectory()) {
			File newFileName = new File(albumsMainDirectory, title);
			if (imageAlbumName.renameTo(newFileName)) {
				if (main.getDataSource() != null) {
					if (main.getDataSource().changePicturesCategoryName(
							adapter.getItem(selectedCategoryPostion).id, title) != 0) {
						getOperationDone(OperationType.pageInfo);
					} else {
						CommonUtilities.handleError("", getFragmentManager());
					}
				}

			}

		}

	}

	private class DeleteFile extends AsyncTask<File, Void, Void> {

		@Override
		protected Void doInBackground(File... postion) {
			// try {
			// Thread.sleep(10000);
			// } catch (InterruptedException e) {
			//
			// }
			CommonUtilities.deleteDirectory(postion[0]);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (adapter != null) {
				main.getDataSource().deletePicturesCategory(
						adapter.getItem(selectedCategoryPostion).id);
				getOperationDone(OperationType.pageInfo);
			}

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	private void deletePicCategoryName() {

		File albumsMainDirectory = new File(
				Environment.getExternalStorageDirectory()
						+ ImageUtil.DIRECTORY_FILE_ALBUM);
		albumsMainDirectory.mkdirs();
		File imageAlbumName = new File(albumsMainDirectory,
				selectedCategoryName);

		imageAlbumName.mkdir();

		if (main.getDataSource() != null) {
			new DeleteFile().execute(imageAlbumName);
		} else {
			CommonUtilities.handleError("", getFragmentManager());
		}

	}

	// PASSWORD CATEGORIES>>>>>>>>>>>>>>>>>>>>>
	private void getPassCategoriesPageInfo() {
		if (main.getDataSource() != null) {
			adapter.clear();
			List<CategoryItems> items = main.getDataSource()
					.getAllPasswordsCategories();
			if (items != null) {
				if (items.size() == 0) {
					pageIdEmpty = true;
				} else {
					pageIdEmpty = false;
					if (adapter.getCount() > 0) {
						adapter.clear();
					}
					adapter.addAll(items);
					adapter.notifyDataSetChanged();
				}

			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}
		checkIfPageIsEmpty();
	}

	private void onPassCategoryListClickListener(int position) {
		CategoryItems item = adapter.getItem(position);
		SpecificPassCategoryFragment fragment = SpecificPassCategoryFragment
				.newInstanceOfSpecificCategoryFragment(item.title, item.id);
		main.replaceFragment(fragment);
	}

	private void onNoteCategoryListClickListener(int position) {
		CategoryItems item = adapter.getItem(position);
		SpecificNoteCategoryFragment fragment = SpecificNoteCategoryFragment
				.getNewInstance(item.title, item.id);
		main.replaceFragment(fragment);
	}

	private void passCategoriesBroadcastReceiver(String intentAction) {
		if (intentAction
				.equals(Constants.BROADCAST_KEY_RELOAD_PASSWORDS_CATEGORIES_PAGE)
				|| intentAction
						.equals(Constants.BROADCAST_KEY_RELOAD_PASSWORDS)
				|| intentAction.equals(Constants.BROADCAST_KEY_RESTORE_DATA)) {
			getOperationDone(OperationType.pageInfo);

		}
	}

	private void deletePassCategory() {
		if (main.getDataSource() != null) {
			if (main.getDataSource().deletePasswordsCategory(
					adapter.getItem(selectedCategoryPostion).id)) {
				getOperationDone(OperationType.pageInfo);
			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}

	}

	private void deleteNoteCategory() {
		if (main.getDataSource() != null) {
			if (main.getDataSource().deleteNoteCategory(
					adapter.getItem(selectedCategoryPostion).id)) {
				getOperationDone(OperationType.pageInfo);
			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}

	}

	private void editPassCategoryName(String title) {
		if (main.getDataSource() != null) {
			if (main.getDataSource().changePasswordsCategoryName(
					adapter.getItem(selectedCategoryPostion).id, title) != 0) {
				getOperationDone(OperationType.pageInfo);

			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}

	}

	// NOTE CATEGORIES>>>>>>>>>>>>>>>>>>>>>

	private void getNotesCategoriesPageInfo() {
		if (main.getDataSource() != null) {
			adapter.clear();
			List<CategoryItems> items = main.getDataSource()
					.getNotesCategories(R.drawable.notes);

			if (items != null) {
				if (items.size() == 0) {
					pageIdEmpty = true;
				} else {
					pageIdEmpty = false;
				}
				adapter.addAll(items);
				adapter.notifyDataSetChanged();
			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}
		checkIfPageIsEmpty();
	}

	private void editNoteCategoryName(String title) {
		if (main.getDataSource() != null) {
			if (main.getDataSource().changeNoteCategoryName(
					adapter.getItem(selectedCategoryPostion).id, title) != 0) {
				getOperationDone(OperationType.pageInfo);

			} else {
				CommonUtilities.handleError("", getFragmentManager());
			}
		}

	}

	private void notesCategoriesBroadcastReceiver(String intentAction) {
		if (intentAction
				.equals(Constants.BROADCAST_KEY_RELOAD_NOTES_CATEGORIES_PAGE)
				|| intentAction.equals(Constants.BROADCAST_KEY_RELOAD_NOTES)
				|| intentAction.equals(Constants.BROADCAST_KEY_RESTORE_DATA))
			getOperationDone(OperationType.pageInfo);

	}

	private void setEmptyPageImage(int emptyPicIcon, String emptyPageText) {
		emptyPagePic.setImageResource(emptyPicIcon);
		emptyPageTitle.setText(emptyPageText);
	}

	private void goToAddNewCategoryFragment() {
		main.replaceFragment(AddNewCategoryFragment
				.getInstanceOfCategFrag(categoryID));
	}

	@Override
	public void OnOptionClicked(int optionPosition) {
		switch (optionPosition) {
		case 1:
			editCategoryTitleDialog.setDialogEditText(selectedCategoryName);
			editCategoryTitleDialog
					.show(main.getSupportFragmentManager(), null);
			break;

		default:
			deleteCategoryConfirmDialog.show(main.getSupportFragmentManager(),
					null);

			break;
		}

	}

}
