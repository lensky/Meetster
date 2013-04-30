package com.russia.meetster;

import java.util.Date;

import com.russia.meetster.utils.YLSQLRow;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.SharedPreferences;

public class MeetsterApplication extends Application {
	private static final String PREFS_USERIDKEY = "userid";
	private static final String PREFS_LASTSYNC = "lastsync";
	private static final String PREFS_HASFRIEND = "hasfriend";
	
	public final static String PREFS_NAME="MeetsterPreferences";
	private long mUserId = -1;
	private Boolean mHasFriend = null;
	
	private SharedPreferences mPrefs;
	private Account mAccount;
	
	public Account getAccount() {
		if (mAccount == null) {
			AccountManager am = AccountManager.get(this);
			Account[] accounts = am.getAccountsByType("com.google");
			if (accounts.length > 0)
				mAccount = accounts[0];
		}
		return mAccount;
	}
	
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
	
	public Long getLastSyncTime() {
		return getPrefs().getLong(PREFS_LASTSYNC, 0);
	}
	
	public void setLastSyncTime(Long lastSyncTime) {
		SharedPreferences.Editor editor = getPrefs().edit();
		
		editor.putLong(PREFS_LASTSYNC, lastSyncTime);
		editor.commit();
	}
	
	public void setLastSyncTime(Date lastSyncTime) {
		setLastSyncTime(lastSyncTime.getTime());
	}
	
	public boolean noFriends() {
		if (mHasFriend == null) {
			mHasFriend = getPrefs().getBoolean(PREFS_HASFRIEND, false);
		}
		return !mHasFriend;
	}
	
	public void setHasFriend() {
		if (this.noFriends()) {
			SharedPreferences.Editor editor = getPrefs().edit();

			editor.putBoolean(PREFS_HASFRIEND, true);
			editor.commit();

			mHasFriend = true;
		}
	}
}
