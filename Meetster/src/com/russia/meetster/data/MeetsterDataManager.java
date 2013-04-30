package com.russia.meetster.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;

public class MeetsterDataManager {
	public static List<MeetsterEvent> getUnsyncedEvents(Context context) {
		final List<MeetsterEvent> result = new ArrayList<MeetsterEvent>();
		
		final ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(MeetsterContract.Events.getUri(), MeetsterContract.Events.getClassProjection(), 
				MeetsterContract.Events.SYNCED + "=0", null, null);
		
		while (cursor.moveToNext()) {
			MeetsterEvent e = new MeetsterEvent(context, cursor);
			result.add(e);
		}
		cursor.close();
		
		return result;
	}
	
	public static List<Long> getUnsyncedInviterIds(Context context) {
		List<Long> result = new ArrayList<Long>();
		
		final ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(MeetsterContract.Events.getUnsyncedInvitersURI(), new String[] { MeetsterContract.Events.CREATORID }, null, null, null);
		
		while (cursor.moveToNext()) {
			result.add(cursor.getLong(cursor.getColumnIndex(MeetsterContract.Events.CREATORID)));
		}
		
		return result;
	}
	
	public static void writeUser(Context context, MeetsterFriend f) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.insert(MeetsterContract.Friends.getUri(), f.toValues());
	}
	
	public static void writeEvent(Context context, MeetsterEvent e) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.insert(MeetsterContract.Events.getUri(), e.toValues());
	}
	
	public static void updateEvent(Context context, MeetsterEvent e) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.update(ContentUris.withAppendedId(MeetsterContract.Events.getUri(), e.getId()), e.toValues(), null, null);
	}
}
