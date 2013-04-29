package com.russia.meetster;

import java.util.Date;

import com.russia.meetster.utils.YLSQLRow;

import android.app.Application;
import android.content.SharedPreferences;

public class MeetsterApplication extends Application {
	private static final String PREFS_USERIDKEY = "userid";
	private static final String PREFS_LASTSYNC = "lastsync";
	public final static String PREFS_NAME="MeetsterPreferences";
	private long mUserId = -1;
	private SharedPreferences mPrefs;
	
	private SharedPreferences getPrefs() {
		if (mPrefs == null) {
			mPrefs = getSharedPreferences(PREFS_NAME, 0);
		}
		return mPrefs;
	}
	
	public Long getCurrentUserId() {
		if (mUserId == -1) {
			mUserId = getPrefs().getLong(PREFS_USERIDKEY, -1);
		}
		return mUserId;
	}

	public boolean isLoggedIn() {
		return this.getCurrentUserId() != -1;
	}
	
	public void setCurrentUserId(long id) {
		SharedPreferences.Editor editor = getPrefs().edit();
		
		editor.putLong(PREFS_USERIDKEY, id);
		this.mUserId = id;
		
		editor.commit();
	}
	
	public String getLastSyncTime() {
		return getPrefs().getString(PREFS_LASTSYNC, "1970-01-01 00:00:00");
	}
	
	public void setLastSyncTime(String lastSyncTime) {
		SharedPreferences.Editor editor = getPrefs().edit();
		
		editor.putString(PREFS_LASTSYNC, lastSyncTime);
		editor.commit();
	}
	
	public void setLastSyncTime(Date lastSyncTime) {
		setLastSyncTime(YLSQLRow.dateToSQLTimestamp(lastSyncTime));
	}
}
