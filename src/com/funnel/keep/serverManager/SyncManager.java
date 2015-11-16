package com.funnel.keep.serverManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.funnel.keep.requestObjects.BackupRequestObject;
import com.funnel.keep.requestObjects.RestoreRequestObject;
import com.funnel.keep.responseObject.BaseResponseObject;
import com.funnel.keep.responseObject.RestoreResponseObject;
import android.os.Handler;
import com.funnel.keep.decorator.Constants;

public class SyncManager extends BaseManager {
	private static SyncManager instance;

	public static SyncManager sharedInstance() {
		if (instance == null)
			instance = new SyncManager();

		return instance;
	}

	private SyncManager() {
		super();
	}

	private final static String RESTORE_USER_PASSWORDS = "restoreUserData";
	private final static String BACKUP_USER_PASSORDS = "backupUserData";

	public void restoreUserPasswords(final RestoreRequestObject object,
			final Handler handler) {
		new Thread() {

			public void run() {
				String Url = String.format("%s/%s", Constants.getServerUrl(),
						RESTORE_USER_PASSWORDS);
				HttpHeaders requestHeaders = getRequestHeaders();
				requestHeaders.set("Connection", "Close");

				HttpEntity<RestoreRequestObject> requestEntity = new HttpEntity<RestoreRequestObject>(
						object, requestHeaders);

				try {
					ResponseEntity<RestoreResponseObject> response = _manager
							.exchange(Url, HttpMethod.POST, requestEntity,
									RestoreResponseObject.class);
					HttpStatus status = response.getStatusCode();
					if (status == HttpStatus.OK) {
						RestoreResponseObject userResponse = response.getBody();
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

	public void backupUserPasswords(final BackupRequestObject object,
			final Handler handler) {
		new Thread() {

			public void run() {
				String Url = String.format("%s/%s", Constants.getServerUrl(),
						BACKUP_USER_PASSORDS);
				HttpHeaders requestHeaders = getRequestHeaders();
				requestHeaders.set("Connection", "Close");

				HttpEntity<BackupRequestObject> requestEntity = new HttpEntity<BackupRequestObject>(
						object, requestHeaders);

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
