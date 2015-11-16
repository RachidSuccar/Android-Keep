
package com.funnel.keep.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.ExifUtils;
import com.funnel.keep.utilities.GridViewImageAdapter;
import com.funnel.keep.utilities.GridViewImageAdapter.GridViewItem;
import com.funnel.keep.utilities.ImageUtil;
import com.funnel.keep.activities.ImageViewPagerActivity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.customDialogs.DialogListOfOptions;
import com.funnel.keep.customDialogs.DialogListOfOptions.DialolListItem;
import com.funnel.keep.customDialogs.DialogListOfOptions.OnOptionsDialogClickListener;
import com.funnel.keep.decorator.Constants;

public final class SpecificPicCategoryFragment extends BaseFragment implements
		OnItemClickListener {
	
	//--------VIEW--------
	private TextView categoryTitleTextView;
	private GridView imageGridView;
	private FrameLayout doneLayout;
	private LinearLayout backLayout;
	private FrameLayout pageLoading;
	private RelativeLayout emptyPageLayout;

	//--------OTHERS--------
	private int albumID;
	private String albumTitleString;
	private GridViewImageAdapter adapter;
	public static List<GridViewItem> listOfALbumImage = new ArrayList<GridViewItem>();
	private final int SHOULD_RESTORE = 0;
	private final int DELETE_ONLY = 1;
	private DialogListOfOptions onPickLongClik;
	private int selectedImage = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.specific_album_fragment, container,
				false);
		getBundleInfo();
		pageLoading = (FrameLayout) v.findViewById(R.id.loading);
		categoryTitleTextView = (TextView) v.findViewById(R.id.title);
		categoryTitleTextView.setText(albumTitleString);
		imageGridView = (GridView) v.findViewById(R.id.imageGridview);
		doneLayout = (FrameLayout) v.findViewById(R.id.done_layout);
		backLayout = (LinearLayout) v.findViewById(R.id.back_layout);
		emptyPageLayout = (RelativeLayout) v.findViewById(R.id.emptyPageLayout);
		adapter = new GridViewImageAdapter(main);
		imageGridView.setColumnWidth(ImageUtil
				.getGridViewCellWidth(main, false));
		imageGridView.setAdapter(adapter);
		imageGridView.setOnItemClickListener(this);
		List<DialolListItem> itemsOne = new ArrayList<DialolListItem>();
		itemsOne.add(new DialolListItem(R.drawable.restore, getResources()
				.getString(R.string.restore_pic)));
		itemsOne.add(new DialolListItem(R.drawable.delete, getResources()
				.getString(R.string.delete_pic)));
		onPickLongClik = new DialogListOfOptions(main, itemsOne);
		setlisteners();
		getImage(false);
		// loadImageFromStorage();

		return v;
	}

	private void getBundleInfo() {
		this.albumTitleString = getArguments().getString(
				Constants.FRAGMENT_NEW_INSTANCE_ALBUM_TITLE);
		this.albumID = getArguments().getInt(
				Constants.FRAGMENT_NEW_INSTANCE_ALBUM_ID);

	}

	private void setlisteners() {
		onPickLongClik
				.setOneOptionClickListener(new OnOptionsDialogClickListener() {

					@Override
					public void OnOptionClicked(int optionPosition) {
						switch (optionPosition) {
						case 1:
							pageLoading.setVisibility(View.VISIBLE);
							new DeleteOrRestore().execute(selectedImage,
									SHOULD_RESTORE);
							break;

						default:

							pageLoading.setVisibility(View.VISIBLE);
							new DeleteOrRestore().execute(selectedImage,
									DELETE_ONLY);
							break;
						}

					}
				});

		imageGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				selectedImage = position;
				onPickLongClik.show(getFragmentManager(), "");

				return true;
			}
		});

		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.getSupportFragmentManager().popBackStack();

			}
		});

		doneLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startImageGalleryIntent();

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

	public static SpecificPicCategoryFragment newInstanceOfSpecificAlbumFragment(
			String categoryTitle, int albumID, int albumSize) {
		SpecificPicCategoryFragment myFragment = new SpecificPicCategoryFragment();
		Bundle args = new Bundle();
		args.putString(Constants.FRAGMENT_NEW_INSTANCE_ALBUM_TITLE,
				categoryTitle);
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_ALBUM_ID, albumID);
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_ALBUM_SIZE, albumSize);
		myFragment.setArguments(args);

		return myFragment;
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == Constants.INTENT_SELECT_PICTURE) {

			Uri selectedImageUri = data.getData();
			// Environment.getExternalStorageDirectory();
			pageLoading.setVisibility(View.VISIBLE);
			new SaveImage().execute(selectedImageUri);

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startImageGalleryIntent() {
		if (CommonUtilities.getAutoExitPref(main)) {
			CommonUtilities.setAutoExitPref(main, false);
			CommonUtilities.setShouldSetAutoExitToTrue(main, true);
		}

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				Constants.INTENT_SELECT_PICTURE);

	}

	private void getImage(boolean shouldScrolGridView) {
		new LoadImage(shouldScrolGridView).execute();

	}

	private List<GridViewItem> loadImageFromStorage() {
		if (main.getDataSource() != null) {
			listOfALbumImage.clear();
			List<String> pictures = main.getDataSource()
					.getPicturesTitlesForCategory(albumID);
			List<GridViewItem> picItem = new ArrayList<GridViewItem>();

			File mainAlbumDirectory = new File(
					Environment.getExternalStorageDirectory()
							+ ImageUtil.DIRECTORY_FILE_ALBUM);
			File AlbumName = new File(mainAlbumDirectory, albumTitleString);
			if (pictures != null) {
				for (int i = 0; i < pictures.size(); i++) {
					GridViewItem item = new GridViewItem();
					GridViewItem displayitem = new GridViewItem();
					item.imageName = pictures.get(i);
					item.imageUri = Uri.fromFile(new File(AlbumName.getPath(),
							pictures.get(i) + ImageUtil.thumbnailExtention
									+ ImageUtil.PNGextention));
					displayitem.imageUri = (Uri.fromFile(new File(AlbumName
							.getPath(), pictures.get(i)
							+ ImageUtil.PNGextention)));
					displayitem.imageName = pictures.get(i);
					listOfALbumImage.add(displayitem);
					picItem.add(item);
				}
				return picItem;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private class LoadImage extends AsyncTask<Void, Void, List<GridViewItem>> {

		public LoadImage(Boolean _shouldScrolGridView) {
			super();
		}

		@Override
		protected List<GridViewItem> doInBackground(Void... params) {
			List<GridViewItem> test = loadImageFromStorage();
			return test;
		}

		@Override
		protected void onPostExecute(List<GridViewItem> result) {
			if (adapter != null && result != null) {
				if (result.isEmpty()) {
					emptyPageLayout.setVisibility(View.VISIBLE);
				} else {
					emptyPageLayout.setVisibility(View.GONE);

				}
				adapter.setBitmaps(result);
			}
			pageLoading.setVisibility(View.GONE);
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	private class SaveImage extends AsyncTask<Uri, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Uri... uri) {
			return addNewPic(uri[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result) {
				getImage(true);
			} else {
				CommonUtilities.handleError("", getFragmentManager());
				pageLoading.setVisibility(View.GONE);
			}

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	private class DeleteOrRestore extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... postion) {

			return deleteOrRestoreImage(postion[0],
					postion[1] == SHOULD_RESTORE);
		}

		@Override
		protected void onPostExecute(Integer imageMovedSuccessfully) {

			handleMoveImageToSource((int) imageMovedSuccessfully);

		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	private void addNewPicStartBroadcast() {
		main.startBroadcast(Constants.BROADCAST_KEY_RELOAD_PICTURES_CATEGORIES_PAGE);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (CommonUtilities.getAutoExitPref(main)) {
			CommonUtilities.setAutoExitPref(main, false);
			CommonUtilities.setShouldSetAutoExitToTrue(main, true);
		}
		Intent intent = new Intent(main, ImageViewPagerActivity.class);
		intent.putExtra(Constants.INTENT_VIEW_PAGER_START_POSITION, position);
		startActivity(intent);

	}

	private boolean addNewPic(Uri uri) {
		boolean success = false;
		String newPicName = CommonUtilities.generateUniqueID();
		String path = ImageUtil.getPath(main, uri);
		File originGalleryImage = new File(path);
		Bitmap displayBitmap = ImageUtil.decodeScaledBitmapFromSdCard(path,
				ImageUtil.getDisplayWidthInDp(main),
				ImageUtil.getDisplayHeightInDp(main));
		displayBitmap.getWidth();
		displayBitmap.getHeight();
		File albumsMainDirectory = new File(
				Environment.getExternalStorageDirectory()
						+ ImageUtil.DIRECTORY_FILE_ALBUM);
		albumsMainDirectory.mkdirs();
		// for invisible folder
		new File(albumsMainDirectory, ".nomedia");
		File imageAlbumName = new File(albumsMainDirectory, albumTitleString);
		imageAlbumName.mkdir();
		if (imageAlbumName.isDirectory()) {
			File displayImage = new File(imageAlbumName, newPicName
					+ ImageUtil.PNGextention);
			File imageThumbnail = new File(imageAlbumName, (newPicName
					+ ImageUtil.thumbnailExtention + ImageUtil.PNGextention));
			displayBitmap = ExifUtils.rotateBitmap(path, displayBitmap);
			if (copyMainImageToAppRepo(imageAlbumName, new File(path),
					newPicName)) {

				if (saveDisplayImageToAppRepo(displayImage, displayBitmap)) {

					if (saveThumbImageToAppRepo(imageThumbnail, displayBitmap)) {
						if (originGalleryImage.exists()) {

							if (CommonUtilities
									.getIfShouldMovePicFromGallery(main)) {
								deleteFileFromMediaStore(
										main.getContentResolver(),
										originGalleryImage);
								startGalleryBroadcast(path);
							}
							if (main.getDataSource() != null) {
								if (main.getDataSource().addNewPicture(
										newPicName, albumID) != -1) {
									addNewPicStartBroadcast();

									success = true;
								}
							}
						}
					}
				}
			}
		}

		return success;

	}

	/**
	 * @param imageFile
	 * @param displayBitmap
	 * @return
	 */
	private boolean saveDisplayImageToAppRepo(File imageFile,
			Bitmap displayBitmap) {

		FileOutputStream outputStream;
		try {

			// if (displayBitmap.getWidth() >
			// ImageUtil.getDisplayWidthInDp(main))
			// displayBitmap = ThumbnailUtils.extractThumbnail(displayBitmap,
			// ImageUtil.getDisplayWidthInDp(main),
			// ImageUtil.getDisplayHeightInDp(main));
			if (displayBitmap.getWidth() > ImageUtil.getDisplayWidthInDp(main)
					&& displayBitmap.getHeight() > ImageUtil
							.getDisplayHeightInDp(main)) {
				double percW = (((ImageUtil.getDisplayWidthInDp(main) * 100) / displayBitmap
						.getWidth()) / 100.0);
				double percH = (((ImageUtil.getDisplayHeightInDp(main) * 100) / displayBitmap
						.getHeight()) / 100.0);
				double scale;
				if (percW > percH){
					scale = percW;
				}else {
					scale = percH;
				}
				displayBitmap = Bitmap.createScaledBitmap(displayBitmap,
						(int) (displayBitmap.getWidth() * scale),
						(int) (displayBitmap.getHeight() * scale), true);
			}

			outputStream = new FileOutputStream(imageFile);
			displayBitmap
					.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

			outputStream.close();
			return true;
		} catch (Exception e) {

			return false;
		}

	}

	private boolean saveThumbImageToAppRepo(File imageFile, Bitmap thumbBitmap) {
		int cellSize = ImageUtil.getGridViewCellWidth(main, true);
		FileOutputStream outputStream;
		try {

			outputStream = new FileOutputStream(imageFile);
			thumbBitmap = ThumbnailUtils.extractThumbnail(thumbBitmap,
					cellSize, cellSize);

			thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
			outputStream.close();
			return true;
		} catch (Exception e) {

			return false;
		}

	}

	private void startGalleryBroadcast(String path) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		if (isKitKat) {
			// gallery broadcast
			MediaScannerConnection.scanFile(main, new String[] {

			path },

			null, new MediaScannerConnection.OnScanCompletedListener() {

				public void onScanCompleted(String path, Uri uri)

				{

				}

			});

		} else {
			main.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
					.parse("file://"
							+ Environment.getExternalStorageDirectory())));
		}
	}

	private boolean copyMainImageToAppRepo(File albumsDirectory,
			File imageToAdd, String imageName) {
		// if (!CommonUtilities.getIfShouldMovePicFromGallery(main))
		// return true;
		File mainImage = new File(albumsDirectory, imageName
				+ ImageUtil.mainExtention + ImageUtil.PNGextention);
		// InputStream in;
		// OutputStream out;
		FileChannel src;
		FileChannel dst;
		try {
			// in = new FileInputStream(imageToAdd);
			// out = new FileOutputStream(mainImage);

			src = new FileInputStream(imageToAdd).getChannel();
			dst = new FileOutputStream(mainImage).getChannel();
			dst.transferFrom(src, 0, src.size());
			src.close();
			dst.close();

			// byte[] buf = new byte[1024];
			// int len;
			//
			// while ((len = in.read(buf)) > 0) {
			// out.write(buf, 0, len);
			// }

			// in.close();
			// out.close();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	private boolean transferFile(File source, File destination,
			boolean shouldRestore) {
		if (!shouldRestore)
			return true;
		FileChannel src;
		FileChannel dst;
		try {

			src = new FileInputStream(source).getChannel();
			dst = new FileOutputStream(destination).getChannel();
			dst.transferFrom(src, 0, src.size());
			src.close();
			dst.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private int deleteOrRestoreImage(int position, boolean shouldRestore) {
		int result = Constants.LOCAL_STATUS_FAILURE;
		String imageName = adapter.getImageNameForPostion(position);

		// KeepSafe app folders
		File albumsMainDirectory = new File(
				Environment.getExternalStorageDirectory()
						+ ImageUtil.DIRECTORY_FILE_ALBUM);
		File imageAlbumName = new File(albumsMainDirectory, albumTitleString);

		// public storage
		File publicAlbum = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				ImageUtil.DIRECTORY_FILE_PUBLIC_ALBUM);
		publicAlbum.mkdirs();

		File destinationFile = new File(publicAlbum, imageName
				+ ImageUtil.PNGextention);
		if (publicAlbum.isDirectory()) {

			File sourceFile = new File(imageAlbumName, (imageName
					+ ImageUtil.mainExtention + ImageUtil.PNGextention));

			if (sourceFile.exists()) {
				if (transferFile(sourceFile, destinationFile, shouldRestore)) {
					File thumbPic = new File(imageAlbumName, imageName
							+ ImageUtil.thumbnailExtention
							+ ImageUtil.PNGextention);
					if (thumbPic.exists()) {
						if (thumbPic.delete()) {
							File showPic = new File(imageAlbumName, imageName
									+ ImageUtil.PNGextention);

							if (showPic.exists()) {
								if (showPic.delete()) {
									if (sourceFile.delete()) {
										if (main.getDataSource(false) != null) {
											if (main.getDataSource(false)
													.deletePictureByName(
															imageName) != 0) {
												startGalleryBroadcast(destinationFile
														.getAbsolutePath());
												addNewPicStartBroadcast();
												// deleteDisplayImageFromStaticList(imageName);
												result = Constants.LOCAL_STATUS_SUCCESS;
											}
										}
									}
								}
							}
						}
					}

				}
			} else {
				result = Constants.LOCAL_STATUS_MAIN_IMAGE_DOES_NOT_EXSIT;
			}
		}
		return result;
	}

	public void deleteFileFromMediaStore(ContentResolver contentResolver,
			File file) {
		String canonicalPath;
		try {
			canonicalPath = file.getCanonicalPath();
		} catch (IOException e) {
			canonicalPath = file.getAbsolutePath();
		}
		Uri uri = MediaStore.Files.getContentUri("external");
		int result = contentResolver.delete(uri,
				MediaStore.Files.FileColumns.DATA + "=?",
				new String[] { canonicalPath });
		if (result == 0) {
			final String absolutePath = file.getAbsolutePath();
			if (!absolutePath.equals(canonicalPath)) {
				contentResolver.delete(uri, MediaStore.Files.FileColumns.DATA
						+ "=?", new String[] { absolutePath });
			}
		}
	}

	private void handleMoveImageToSource(int result) {
		if (result == Constants.LOCAL_STATUS_SUCCESS) {

			getImage(false);
		} else {
			CommonUtilities.handleError("", getFragmentManager());
			pageLoading.setVisibility(View.GONE);
		}
	}
}
