package com.funnel.keep.activities;

import android.app.Application;

public class KeepApplicationContext extends Application {

	public static KeepApplicationContext instance;
	private boolean applicationIsVisible = false;

	public KeepApplicationContext() {
		super();
		instance = this;
	}

	public static KeepApplicationContext getKeepApplicationContext() {
		if (instance == null) {
			instance = new KeepApplicationContext();

		}
		return instance;
	}

	public boolean isActivityVisible() {
		return applicationIsVisible;
	}

	public void activityResumed() {
		applicationIsVisible = true;
	}

	public void activityPaused() {
		applicationIsVisible = false;
	}

}
