package com.funnel.keep.base;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.funnel.keep.requestObjects.DeviceInfoRequestObject;
import com.funnel.keep.serverManager.DeviceRegistrationManager;
import com.funnel.keep.utilities.CommonUtilities;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class BaseNotificationActivity extends FragmentActivity {

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_OS_VERSION = "osVersion";
	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	public static String SENDER_ID = "137123191789";
	// 137123191789
	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "GCMDemo";
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Context context;

	String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		if (CommonUtilities.checkPlayServices(this)) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);

			if (regid.length() == 0) {
				registerInBackground();
			} else {
				sendDeviceInfo(regid);
			}
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.length() == 0) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}

		// Check if OS is updated
		String registeredOS = prefs.getString(PROPERTY_OS_VERSION, "");
		String currentOS = CommonUtilities.GetDeviceOperationSystem();

		if (registeredOS.length() == 0 || !registeredOS.equals(currentOS)) {
			Log.i(TAG, "Os version changed.");
			return "";
		}

		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(
				BaseNotificationActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
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

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over
					// HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your
					// app.
					// The request to your server should be authenticated if
					// your app
					// is using accounts.
					sendRegistrationIdToBackend(regid);

					// For this demo: we don't need to send it because the
					// device
					// will send upstream messages to a server that echo back
					// the
					// message using the 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {

			}
		}.execute(null, null, null);

	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend(String regid) {
		Log.e("REGID", regid);
		sendDeviceInfo(regid);

	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.putString(PROPERTY_OS_VERSION,
				CommonUtilities.GetDeviceOperationSystem());
		editor.commit();
	}

	public void sendDeviceInfo(String notificationToken) {
		DeviceInfoRequestObject ob = new DeviceInfoRequestObject();
		ob.DeviceName = CommonUtilities.GetDeviceModel();
		ob.DeviceUniqueID = CommonUtilities.GetDeviceID(this);
		ob.DeviceNotificationToken = notificationToken;
		ob.OsVersion = CommonUtilities.GetDeviceOperationSystem();

		DeviceRegistrationManager.sharedInstance().UpdateDeviceInfo(ob);

	}

}
