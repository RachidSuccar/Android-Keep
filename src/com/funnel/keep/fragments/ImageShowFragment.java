package com.funnel.keep.fragments;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.activities.ImageViewPagerActivity;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.funnel.keep.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import com.funnel.keep.decorator.Constants;

public class ImageShowFragment extends Fragment {

	// OTHERS
	private int uriImagePostion;

	// VIEW
	private ImageView mainImage;
	private PhotoViewAttacher mAttacher;
	private ImageViewPagerActivity main;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		uriImagePostion = getArguments() != null ? getArguments().getInt(
				Constants.FRAGMENT_NEW_INSTANCE_URI_IMAGE_POSITION) : -1;
		View view = inflater.inflate(R.layout.image_show, container, false);
		main = (ImageViewPagerActivity) getActivity();
		mainImage = (ImageView) view.findViewById(R.id.imageView_main);
		Callback imageLoadedCallback = new Callback() {

			@Override
			public void onSuccess() {
				if (mAttacher != null) {
					mAttacher.update();
				} else {
					mAttacher = new PhotoViewAttacher(mainImage);
					mAttacher.onDrag(2, 2);
					mAttacher
							.setOnMatrixChangeListener(new OnMatrixChangedListener() {

								@Override
								public void onMatrixChanged(RectF arg0) {
									if (mAttacher.getScale() > 1f) {
										main.setViewPagerLocked(true);
									} else {
										main.setViewPagerLocked(false);
									}

								}
							});
					mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

						@Override
						public void onPhotoTap(View arg0, float arg1, float arg2) {
							main.toggleFullScreen();
							// if (!main.toggleFullScreen()) {
							// waitAndHidSystemUI();
							// } else {
							// if (runnable != null)
							// timerHandler.removeCallbacks(runnable);
							// }

						}
					});

				}
			}

			@Override
			public void onError() {
				CommonUtilities.Toast(getActivity(),
						"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
		};

		Picasso.with(main)
				.load(SpecificPicCategoryFragment.listOfALbumImage
						.get(uriImagePostion).imageUri)

				.into(mainImage, imageLoadedCallback);

		return view;
	}

	@Override
	public void onPause() {
		Picasso.with(main).cancelRequest(mainImage);
		super.onPause();
	}

	public static ImageShowFragment newInstance(int uriPostion) {
		final ImageShowFragment fragment = new ImageShowFragment();
		final Bundle args = new Bundle();
		args.putInt(Constants.FRAGMENT_NEW_INSTANCE_URI_IMAGE_POSITION,
				uriPostion);
		fragment.setArguments(args);
		return fragment;
	}

}
