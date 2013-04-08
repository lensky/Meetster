package com.russia.meetster;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class MeetsterFriend extends YLSQLRow {
	private long id;
	private String firstName;
	private String lastName;
	
	public static MeetsterFriend getFromId(Context c, long id) {
		Cursor cursor = c.getContentResolver().query(ContentUris.withAppendedId(MeetsterContract.Friends.getUri(), id), 
				MeetsterContract.Friends.getClassProjection(), null, null, null);
		return new MeetsterFriend(cursor);
	}
	
	public MeetsterFriend(Cursor cursor) {
		this.id = getCursorLong(cursor, MeetsterContract.Friends._ID);
		this.firstName = getCursorString(cursor, MeetsterContract.Friends.FIRST_NAME);
		this.lastName = getCursorString(cursor, MeetsterContract.Friends.LAST_NAME);
	}
	
	public MeetsterFriend(long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public ContentValues toValues() {
		ContentValues vals = new ContentValues();
		
		vals.put(MeetsterContract.Friends.FIRST_NAME, this.firstName);
		vals.put(MeetsterContract.Friends.LAST_NAME, this.lastName);
		
		return vals;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
