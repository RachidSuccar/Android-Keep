package com.funnel.keep.serverManager;

import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.funnel.keep.requestObjects.DeviceInfoRequestObject;
import com.funnel.keep.requestObjects.RegiterSessionReqestObject;
import com.funnel.keep.responseObject.BaseResponseObject;
import com.funnel.keep.utilities.CommonUtilities;
import android.content.Context;
import android.os.Handler;
import com.funnel.keep.base.BaseNotificationActivity;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.funnel.keep.decorator.Constants;

public class DeviceRegistrationManager extends BaseManager {
	private final static String UPDATE_DEVICE_INFO = "updateDeviceInfo";
	private final static String ADD_BROWSER = "addBrowser";
	private final static String UNLINK_BROWSER_FOR_DEVICE = "unlinkAllBrowserForDevice";

	private static DeviceRegistrationManager deviceRegistrationManager;

	public static DeviceRegistrationManager sharedInstance() {
		if (deviceRegistrationManager == null) {
			deviceRegistrationManager = new DeviceRegistrationManager();
		}
		return deviceRegistrationManager;
	}

	public DeviceRegistrationManager() {
		super();
	}

	public void UpdateDeviceInfo(final DeviceInfoRequestObject object) {
		new Thread() {

			@Override
			public void run() {
				String Url = String.format("%s/%s", Constants.getServerUrl(),
						UPDATE_DEVICE_INFO);
				HttpHeaders requestHeaders = getRequestHeaders();
				requestHeaders.set("Connection", "Close");

				HttpEntity<DeviceInfoRequestObject> requestEntity = new HttpEntity<DeviceInfoRequestObject>(
						object, requestHeaders);

				try {
					_manager.exchange(Url, HttpMethod.POST, requestEntity,
							BaseResponseObject.class);

				} catch (Exception ex) {

				}
			}

		}.start();
	}

	public void addBrowser(final RegiterSessionReqestObject requestObject,
			final Handler handler, final Context context) {
		new Thread() {

			public void run() {
				boolean NoValidGooglePlayServicesAPKFound = false;
				String regid = CommonUtilities.getRegistrationId(context);
				if (regid == null || regid.length() == 0) {
					if (CommonUtilities.checkPlayServices(context)) {

						GoogleCloudMessaging gcm = GoogleCloudMessaging
								.getInstance(context);
						try {
							regid = gcm
									.register(BaseNotificationActivity.SENDER_ID);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						NoValidGooglePlayServicesAPKFound = true;
					}

				}

				if (regid != null && regid.length() > 0) {
					requestObject.DeviceNotificationToken = regid;
					String Url = String.format("%s/%s",
							Constants.getServerUrl(), ADD_BROWSER);
					HttpHeaders requestHeaders = getRequestHeaders();
					requestHeaders.set("Connection", "Close");

					HttpEntity<RegiterSessionReqestObject> requestEntity = new HttpEntity<RegiterSessionReqestObject>(
							requestObject, requestHeaders);

					try {
						ResponseEntity<BaseResponseObject> response = _manager
								.exchange(Url, HttpMethod.POST, requestEntity,
										BaseResponseObject.class);
						HttpStatus status = response.getStatusCode();
						if (status == HttpStatus.OK) {
							BaseResponseObject userResponse = response
									.getBody();
							handler.sendMessage(handler.obtainMessage(
									Constants.HANDLER_SUCCESS, userResponse));
						} else {
							throw new Exception(status.toString());
						}

					} catch (Exception ex) {
						handler.sendMessage(handler.obtainMessage(
								Constants.HANDLER_FAILURE, ex));
					}

				} else {
					if (NoValidGooglePlayServicesAPKFound) {
						handler.sendMessage(handler
								.obtainMessage(Constants.NO_VALID_GOOGLE_PLAY_SERVICS_APK_FOUND));
					} else {
						handler.sendMessage(handler
								.obtainMessage(Constants.HANDLER_FAILURE));
					}

				}

			}
		}.start();

	}

	public void unlinkAllBrowserForDevice(
			final DeviceInfoRequestObject requestObject, final Handler handler) {
		new Thread() {

			public void run() {

				String Url = String.format("%s/%s", Constants.getServerUrl(),
						UNLINK_BROWSER_FOR_DEVICE);
				HttpHeaders requestHeaders = getRequestHeaders();
				requestHeaders.set("Connection", "Close");

				HttpEntity<DeviceInfoRequestObject> requestEntity = new HttpEntity<DeviceInfoRequestObject>(
						requestObject, requestHeaders);

				try {
					ResponseEntity<BaseResponseObject> response = _manager
							.exchange(Url, HttpMethod.POST, requestEntity,
									BaseResponseObject.class);
					HttpStatus status = response.getStatusCode();
					if (status == HttpStatus.OK) {
						BaseResponseObject userResponse = response.getBody();
						handler.sendMessage(handler.obtainMessage(
								Constants.HANDLER_SUCCESS, userResponse));
					} else {
						throw new Exception(status.toString());
					}

				} catch (Exception ex) {
					handler.sendMessage(handler.obtainMessage(
							Constants.HANDLER_FAILURE, ex));
				}

			}
		}.start();

	}
}
