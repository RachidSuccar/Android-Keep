package com.funnel.keep.utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.funnel.keep.base.BaseNotificationActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.funnel.keep.R;
import com.funnel.keep.customDialogs.InfoDialog;
import com.funnel.keep.decorator.Constants;

public class CommonUtilities {
	public static final String ERROR_GENERALE = "Oups something went wrong,  please try again later";

	public static MediaPlayer mMediaPlayer;

	public static String getBrowserFingerprint(String hashed, String encrypted) {
		hashed = hashed.substring(0, Math.min(hashed.length(), 15));
		encrypted = encrypted.substring(0,
				Math.min(encrypted.length(), encrypted.length() - 1));
		return ("R" + encrypted + hashed + "W");

	}

	public static void setSoundOn(Context context,
			final NotificationManager manager) {
		AudioManager amanager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = amanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		amanager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
		if (mMediaPlayer == null)
			mMediaPlayer = new MediaPlayer();
		mMediaPlayer = MediaPlayer.create(context, R.raw.sound);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setLooping(true);
		mMediaPlayer.start();
		mMediaPlayer.setVolume(maxVolume, maxVolume);

	}

	public static void setAralmOff() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer.release();

		}

	}

	public static void copyToClipBoard(String text, Context context) {

		copyToClipBoard(text, context,
				context.getResources().getString(R.string.text_cpied_clip));
	}

	public static void copyToClipBoard(String text, Context context,
			String taostLable) {

		ClipboardManager clipboard = (ClipboardManager) context
				.getSystemService(Activity.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("label", text);
		clipboard.setPrimaryClip(clip);
		CommonUtilities.Toast((Activity) context, taostLable);
	}

	public static void setAutoExitPref(Context context, boolean bit) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEdit = prefs.edit();
		prefsEdit.putBoolean(
				Constants.KEY_SHARED_PREFERENCE_AUTO_EXIT, bit);

		prefsEdit.commit();
	}

	public static boolean getIfShouldMovePicFromGallery(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(
				Constants.KEY_SHARED_PREFERENCE_IMAGE, true);
	}

	public static void setIfShouldMovePicFromGallery(Context context,
			boolean bit) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEdit = prefs.edit();
		prefsEdit.putBoolean(Constants.KEY_SHARED_PREFERENCE_IMAGE,
				bit);

		prefsEdit.commit();
	}

	public static boolean getAutoExitPref(Context context) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(
				Constants.KEY_SHARED_PREFERENCE_AUTO_EXIT, true);

	}

	public static void setShouldSetAutoExitToTrue(Context context, boolean bit) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEdit = prefs.edit();
		prefsEdit
				.putBoolean(
						Constants.KEY_SHARED_PREFERENCE_SHOULD_TOGGLE_AUTO_EXIT,
						bit);

		prefsEdit.commit();
	}

	public static boolean ifShouldSetAutoExitToTrue(Context context) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs
				.getBoolean(
						Constants.KEY_SHARED_PREFERENCE_SHOULD_TOGGLE_AUTO_EXIT,
						false);

	}

	public static boolean getIRestoreBit(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(
				Constants.KEY_SHARED_PREFERENCE_RESTORE_BIT, false);
	}

	public static boolean getIUpdateBit(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(
				Constants.KEY_SHARED_PREFERENCE_UPDATE_BIT, false);
	}

	public static void setRestoreBit(Context context, boolean bit) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEdit = prefs.edit();
		prefsEdit.putBoolean(
				Constants.KEY_SHARED_PREFERENCE_RESTORE_BIT, bit);

		prefsEdit.commit();
	}

	public static void setUpdateBit(Context context, boolean bit) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEdit = prefs.edit();
		prefsEdit.putBoolean(
				Constants.KEY_SHARED_PREFERENCE_UPDATE_BIT, bit);

		prefsEdit.commit();
	}

	public static void setEmail(Context context, String email) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEdit = prefs.edit();
		prefsEdit.putString(Constants.KEY_SHARED_PREFERENCE_EMAIL,
				email);

		prefsEdit.commit();
	}

	public static void setLastUpdateDATE(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEdit = prefs.edit();
		prefsEdit.putLong(
				Constants.KEY_SHARED_PREFERENCE_LAST_UPDATE_DATE,
				timeInSecond());

		prefsEdit.commit();
	}

	public static void setRestoreDATE(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefsEdit = prefs.edit();
		prefsEdit.putLong(
				Constants.KEY_SHARED_PREFERENCE_LAST_RESTORE_DATE,
				timeInSecond());

		prefsEdit.commit();
	}

	public static String getLastRestoreDATE(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return "(Last resotre "
				+ convertSecondsToTimeAgo(timeInSecond()
						- prefs.getLong(
								Constants.KEY_SHARED_PREFERENCE_LAST_RESTORE_DATE,
								0)) + ")";
	}

	public static String getLastUpdateDATE(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return "(Last backup "
				+ convertSecondsToTimeAgo(timeInSecond()
						- prefs.getLong(
								Constants.KEY_SHARED_PREFERENCE_LAST_UPDATE_DATE,
								0)) + ")";
	}

	public static String getEmail(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(Constants.KEY_SHARED_PREFERENCE_EMAIL, "");
	}

	public static void Toast(final Activity context, final String text) {

		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
				// toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 30);
				toast.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
		});

	}

	public static void showKeyBoard(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getApplicationContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);

		if (imm != null) {
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static void hideKeyboard(Context context, EditText searchView) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

	}

	public static String convertSecondsToTimeAgo(double Time) {
		String TimeAgo = null;

		Time = Time / 60;
		if (Time < 1.5) {

			TimeAgo = "Just Now";
		} else if (Time >= 1 && Time < 59.5) {
			int finalValue = (int) Math.round(Time);
			TimeAgo = String.format(Locale.getDefault(), "%d%s%s", finalValue,
					" ", "minutes ago");
		} else if (Time >= 59.5 && Time < 1440) {
			int finalValue = (int) Math.round(Time / 60);
			{
				if (finalValue == 1) {
					TimeAgo = String.format(Locale.getDefault(), "%d%s%s",
							finalValue, " ", "hour ago");

				} else if (finalValue == 24) {

					TimeAgo = "1 day ago";
				} else {
					TimeAgo = String.format(Locale.getDefault(), "%d%s%s",
							finalValue, " ", "hours ago");
				}
			}

		}

		else if (Time >= 1440 && Time < 10080) {
			int finalValue = (int) Math.round(Time / 1440);
			{
				if (finalValue == 1) {
					TimeAgo = String.format(Locale.getDefault(), "%d%s%s",
							finalValue, " ", "day ago");

				} else if (finalValue == 168) {
					TimeAgo = "1 week ago";

				}

				else {
					TimeAgo = String.format(Locale.getDefault(), "%d%s%s",
							finalValue, " ", "days ago");
				}
			}

		}

		else if (Time >= 10080 && Time < 40320) {
			int finalValue = (int) Math.round(Time / 10080);
			{
				if (finalValue == 1) {
					TimeAgo = String.format(Locale.getDefault(), "%d%s%s",
							finalValue, " ", "week ago");

				}

				else if (finalValue == 4) {
					TimeAgo = "1 month ago";

				} else {
					TimeAgo = String.format(Locale.getDefault(), "%d%s%s",
							finalValue, " ", "weeks ago");
				}
			}

		}

		else if (Time >= 40320 && Time < 483840) {
			int finalValue = (int) Math.round(Time / 40320);
			{
				if (finalValue == 1) {
					TimeAgo = String.format(Locale.getDefault(), "%d%s%s",
							finalValue, " ", "month ago");

				} else if (finalValue == 12) {
					TimeAgo = "1 year ago";

				}

				else {
					TimeAgo = String.format(Locale.getDefault(), "%d%s%s",
							finalValue, " ", "months ago");
				}
			}

		}

		else if (Time >= 483840) {
			int finalValue = (int) Math.round(Time / 483840);
			{
				if (finalValue == 1) {
					TimeAgo = String.format(Locale.getDefault(), "%d%s%s",
							finalValue, " ", "year ago");
				} else {
					TimeAgo = String.format(Locale.getDefault(), "%d%s%s",
							finalValue, " ", "years ago");
				}

			}

		}
		return TimeAgo;

	}

	public static long timeInSecond() {
		return (System.currentTimeMillis() / 1000);
	}

	public static String GetDeviceID(Context context) {
		TelephonyManager mngr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return mngr.getDeviceId();
	}

	public static String GetDeviceModel() {
		return android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
	}

	public static String GetDeviceOperationSystem() {
		return android.os.Build.VERSION.RELEASE;
	}

	public static String generateUniqueID() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.US);
		return sdf.format(cal.getTime());

	}

	public static void handleError(String errorTitle, FragmentManager manager) {
		InfoDialog dialog = InfoDialog.getInfoInoDialogInstance();
		if (errorTitle == null || errorTitle.equals("")) {
			dialog.setMessage(ERROR_GENERALE);
		} else {
			dialog.setMessage(errorTitle);
		}

		dialog.show(manager, "");
	}

	public static void deleteDirectory(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
			file.delete();
		}
	}

	public static boolean checkPlayServices(Context context) {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			return false;
		}
		return true;
	}

	public static String getRegistrationId(Context context) {

		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(
				BaseNotificationActivity.PROPERTY_REG_ID, "");
		if (registrationId.length() == 0) {

			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(
				BaseNotificationActivity.PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {

			return "";
		}

		// Check if OS is updated
		String registeredOS = prefs.getString(
				BaseNotificationActivity.PROPERTY_OS_VERSION, "");
		String currentOS = CommonUtilities.GetDeviceOperationSystem();

		if (registeredOS.length() == 0 || !registeredOS.equals(currentOS)) {

			return "";
		}

		return registrationId;
	}

	public static SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(
				BaseNotificationActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			return 0;
			// throw new RuntimeException("Could not get package name: " + e);
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
}
