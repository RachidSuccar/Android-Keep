package com.funnel.keep.gcm;

import com.funnel.keep.sqlDataBaseManager.BaseDataSource;
import com.funnel.keep.sqlDataBaseManager.DataSource;
import com.funnel.keep.activities.KeepApplicationContext;
import com.funnel.keep.activities.NotificationReceiverActivity;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.funnel.keep.R;

import com.funnel.keep.decorator.Constants;

public class GcmIntentService extends IntentService {

	public final static String EXTRA_NOTIFICATION_MESSAGE = "EXTRA_NOTIFICATION_MESSAGE";
	public static final String knotificationNumber = "notificationNumber";

	public static final int NOTIFICATION_ID_TEXT = 198057;
	public static final int NOTIFICATION_ID_ALARM = 198058;
	public static final String TAG = "GMC service";
	private Drawable notificationLargeIcon;
	private Bitmap bitmapicon;
	private NotificationManager mNotificationManager;
	private static BaseDataSource dataSource;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				// sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				// sendNotification("Deleted messages on server: " +
				// extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				// This loop represents the service doing some work.
				// Post notification of received message.

				try {
					String title = "KeepWeb";

					final String input = extras.get("data").toString();
					// Copy text to your phone clipboard
					String message;
					if (input.length() > 30) {
						message = input.substring(0,
								Math.min(input.length(), 30))
								+ "...";
					} else {
						message = input.substring(0,
								Math.min(input.length(), 30));
					}
					boolean isSound = false;
					// if (input.equals("s")) {
					// CommonUtilities.setAralmOff();
					// CommonUtilities.setSoundOn(this, manager);
					//
					// message = "Tap to stop sound";
					// isSound = true;
					// }
					if (dataSource == null)
						dataSource = new DataSource(this);
					try {
						if (!dataSource.isDbOpen())
							dataSource.openDataBAse();
						dataSource.addNewWebText(input);
						if (dataSource != null && dataSource.isDbOpen())
							dataSource.closeDatabaBase();
					} catch (Exception e) {

					}
					startBroadcast();
					if (KeepApplicationContext.getKeepApplicationContext()
							.isActivityVisible()) {
						startToastBroadcast(input);

					} else {
						sendNotification(message, title, input, isSound);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void startToastBroadcast(String input) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.putExtra(Constants.BROADCAST_TOAST_EXTRA_KEY, input);
		broadcastIntent.setAction(Constants.BROADCAST_NOTIFICATION_TOAST);
		this.sendBroadcast(broadcastIntent);

	}

	public void startBroadcast() {
		Intent broadcastIntent = new Intent();

		broadcastIntent.setAction(Constants.BROADCAST_KEY_RELOAD_WEB_TEXTS);
		this.sendBroadcast(broadcastIntent);

	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.

	@SuppressLint("InlinedApi")
	private void sendNotification(String msg, String title, String input,
			boolean isSound) {

		notificationLargeIcon = getResources().getDrawable(
				R.drawable.ic_launcher);
		bitmapicon = convertToBitmap(notificationLargeIcon, 100, 100);
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Uri notificationSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		Intent notificationIntent;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			notificationIntent = new Intent(this,
					NotificationReceiverActivity.class);
			;
		} else {
			notificationIntent = new Intent(this,
					NotificationReceiverActivity.class);

		}
		notificationIntent.putExtra(EXTRA_NOTIFICATION_MESSAGE, input);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		int smallIcon = !isSound ? R.drawable.copy : R.drawable.sound_on;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(smallIcon).setAutoCancel(true)
				.setContentTitle(title).setLargeIcon(bitmapicon)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setSound(notificationSound).setContentText(msg);
		int notificationID = !isSound ? NOTIFICATION_ID_TEXT
				: NOTIFICATION_ID_ALARM;
		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(notificationID, mBuilder.build());

	}

	public Bitmap convertToBitmap(Drawable drawable, int widthPixels,
			int heightPixels) {
		Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mutableBitmap);
		drawable.setBounds(0, 0, widthPixels, heightPixels);
		drawable.draw(canvas);

		return mutableBitmap;
	}

}
