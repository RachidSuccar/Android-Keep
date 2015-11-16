package com.funnel.keep.activities;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import com.funnel.keep.utilities.CameraPreview;
import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.QrCrypto;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funnel.keep.R;

import com.funnel.keep.decorator.Constants;

public class QrScannerActivity extends Activity {

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;

	ImageScanner scanner;
	private TextView info;
	private boolean barcodeScanned = false;
	private boolean previewing = true;
	private LinearLayout backLayout;

	static {
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.qr_view);
		info = (TextView) findViewById(R.id.info);
		String text = "<font color=#999999>Visit </font> <strong><font color=#727272>web-keep.com </font></strong> <font color=#999999>on your computer and scan the QR code</font>";
		info.setText(Html.fromHtml(text));
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED);
				QrScannerActivity.this.finish();

			}
		});
		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();

		/* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
		preview.addView(mPreview);

		// scanButton.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// if (barcodeScanned) {
		// barcodeScanned = false;
		// scanText.setText("Scanning...");
		// mCamera.setPreviewCallback(previewCb);
		// mCamera.startPreview();
		// previewing = true;
		// mCamera.autoFocus(autoFocusCB);
		// }
		// }
		// });
	}

	@Override
	protected void onResume() {
		KeepApplicationContext.getKeepApplicationContext().activityResumed();
		super.onResume();
	}

	public void onPause() {
		KeepApplicationContext.getKeepApplicationContext().activityPaused();
		super.onPause();
		releaseCamera();
		QrScannerActivity.this.finish();
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
			int o = 9;
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				// previewing = false;
				// mCamera.setPreviewCallback(null);
				// mCamera.stopPreview();
				String resulto = "";
				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					resulto = sym.getData();

					barcodeScanned = true;
				}

				String decryptedQrCode = null;
				try {
					decryptedQrCode = QrCrypto.decrypt(resulto, "K!|&$@FS%%");
				} catch (KeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidAlgorithmParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GeneralSecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (decryptedQrCode != null) {
					Intent returnIntent = new Intent();
					setResult(Activity.RESULT_OK, returnIntent);
					returnIntent.putExtra(Constants.INTENT_WEB_ID,
							CommonUtilities.getBrowserFingerprint(
									decryptedQrCode, resulto));
					try {
						QrScannerActivity.this.finish();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

}
