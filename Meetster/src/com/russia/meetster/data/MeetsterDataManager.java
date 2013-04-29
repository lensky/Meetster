package com.russia.meetster.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
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
	
	public static void writeEvent(Context context, MeetsterEvent e) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.insert(MeetsterContract.Events.getUri(), e.toValues());
	}
}
