package com.funnel.keep.utilities;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

public class CropUtil {

	private static final String SCHEME_FILE = "file";
	private static final String SCHEME_CONTENT = "content";

	public static void closeSilently(Closeable c) {
		if (c == null)
			return;
		try {
			c.close();
		} catch (Throwable t) {
			// Do nothing
		}
	}

	public static int getExifRotation(File imageFile) {
		if (imageFile == null)
			return 0;
		try {
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			// We only recognize a subset of orientation tag values
			switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED)) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			case ExifInterface.ORIENTATION_ROTATE_270:
				return 270;
			default:
				return ExifInterface.ORIENTATION_UNDEFINED;
			}
		} catch (IOException e) {
			return 0;
		}
	}

	public static File getFromMediaUri(ContentResolver resolver, Uri uri) {
		if (uri == null)
			return null;

		if (SCHEME_FILE.equals(uri.getScheme())) {
			return new File(uri.getPath());
		} else if (SCHEME_CONTENT.equals(uri.getScheme())) {
			final String[] filePathColumn = { MediaStore.MediaColumns.DATA,
					MediaStore.MediaColumns.DISPLAY_NAME };
			Cursor cursor = null;
			try {
				cursor = resolver.query(uri, filePathColumn, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					final int columnIndex = (uri.toString()
							.startsWith("content://com.google.android.gallery3d")) ? cursor
							.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
							: cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
					// Picasa image on newer devices with Honeycomb and up
					if (columnIndex != -1) {
						String filePath = cursor.getString(columnIndex);
						if (!TextUtils.isEmpty(filePath)) {
							return new File(filePath);
						}
					}
				}
			} catch (SecurityException ignored) {
				// Nothing we can do
			} finally {
				if (cursor != null)
					cursor.close();
			}
		}
		return null;
	}

}
