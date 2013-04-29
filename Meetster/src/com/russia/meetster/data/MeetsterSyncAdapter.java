package com.russia.meetster.data;

import java.util.List;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

public class MeetsterSyncAdapter extends AbstractThreadedSyncAdapter {

	public MeetsterSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		List<MeetsterEvent> unsyncedEvents;
		List<MeetsterEvent> updatedEvents;
		
		final String authtoken;
	}

}
