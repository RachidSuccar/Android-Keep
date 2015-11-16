package com.funnel.keep.fragments;

import com.funnel.keep.utilities.CommonUtilities;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnel.keep.R;

public final class SettingsFragment extends BaseFragment {

	//--------View--------
	private RelativeLayout autoExitLayout;
	private ImageView checkBox;
	private RelativeLayout syncLayout;
	private ImageView imageMovePic;
	private ImageView imageCopyPic;
	private RelativeLayout movePic;
	private RelativeLayout copoyPic;
	private RelativeLayout aboutLayout;
	private RelativeLayout PrivacyPolicy;
	private RelativeLayout termsOfServiceLayout;
	private RelativeLayout keepWebLayout;
	private TextView webInfo;
	
	//--------OTHERS--------
	private boolean autoExitBool;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings_fragment, container,
				false);
		webInfo = (TextView) view.findViewById(R.id.web_info);
		String text = "<font color=#999999>Visit </font> <strong><font color=#727272>web-keep.com </font></strong> <font color=#999999>on your computer and copy texts from pc to your phone clipboard</font>";
		webInfo.setText(Html.fromHtml(text));

		movePic = (RelativeLayout) view.findViewById(R.id.relativeLayout_move);
		copoyPic = (RelativeLayout) view.findViewById(R.id.relativeLayout_copy);
		imageMovePic = (ImageView) view.findViewById(R.id.imageView_Move);
		keepWebLayout = (RelativeLayout) view
				.findViewById(R.id.relativeLayout_keepWeb);
		imageCopyPic = (ImageView) view.findViewById(R.id.imageView_Copy);
		aboutLayout = (RelativeLayout) view
				.findViewById(R.id.relativeLayout_about);
		PrivacyPolicy = (RelativeLayout) view
				.findViewById(R.id.relativeLayout_privacy_policy);
		termsOfServiceLayout = (RelativeLayout) view
				.findViewById(R.id.relativeLayout_term_of_service);

		autoExitLayout = (RelativeLayout) view
				.findViewById(R.id.relativeLayout_autoExist);
		checkBox = (ImageView) view.findViewById(R.id.imageView_checkBox);
		syncLayout = (RelativeLayout) view
				.findViewById(R.id.relativeLayout_sync);
		autoExitBool = CommonUtilities.getAutoExitPref(main);
		imageTransferCheck();
		checkBox();
		setListeners();

		return view;
	}

	private void imageTransferCheck() {
		if (CommonUtilities.getIfShouldMovePicFromGallery(main)) {
			imageMovePic.setImageResource(R.drawable.picker_filled);
			imageCopyPic
					.setImageResource(R.drawable.auto_exit_check_box_not_checked);
		} else {
			imageMovePic
					.setImageResource(R.drawable.auto_exit_check_box_not_checked);
			imageCopyPic.setImageResource(R.drawable.picker_filled);
		}
	}

	private void checkBox() {
		if (autoExitBool) {
			checkBox.setImageResource(R.drawable.square_checked);
		} else {
			checkBox.setImageResource(R.drawable.square_unchecked);
		}

	}

	private void toggleCheckBox() {
		if (autoExitBool) {
			checkBox.setImageResource(R.drawable.square_unchecked);
		} else {
			checkBox.setImageResource(R.drawable.square_checked);

		}
		autoExitBool = !autoExitBool;
		CommonUtilities.setAutoExitPref(main, autoExitBool);

	}

	private void setListeners() {
		keepWebLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				main.replaceFragment(new WebTextsFragment());

			}
		});
		termsOfServiceLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.replaceFragment(new KeepTermsFragment());
			}
		});
		PrivacyPolicy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.replaceFragment(new KeepPrivacyPolicyFragment());
			}
		});

		aboutLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.replaceFragment(new KeepAboutFragment());
			}
		});
		movePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtilities.setIfShouldMovePicFromGallery(main, true);
				imageTransferCheck();
			}
		});

		copoyPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtilities.setIfShouldMovePicFromGallery(main, false);
				imageTransferCheck();
			}
		});

		syncLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				main.replaceFragment(new DataBaseFragment());

			}
		});

		autoExitLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleCheckBox();

			}
		});

	}

}
