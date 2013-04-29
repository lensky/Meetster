package com.russia.meetster;

import android.app.Application;
import android.content.SharedPreferences;

public class MeetsterApplication extends Application {
	private static final String PREFS_USERIDKEY = "userid";
	public final static String PREFS_NAME="MeetsterPreferences";
	private long mUserId = -1;
	
	public Long getCurrentUserId() {
		if (mUserId == -1) {
			SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
			mUserId = prefs.getLong(PREFS_USERIDKEY, -1);
		}
		return mUserId;
	}

	public boolean isLoggedIn() {
		return this.getCurrentUserId() != -1;
	}
	
	public void setCurrentUserId(long id) {
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putLong(PREFS_USERIDKEY, id);
		this.mUserId = id;
		
		editor.commit();
	}
}
