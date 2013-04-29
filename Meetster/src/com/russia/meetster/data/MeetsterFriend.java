package com.russia.meetster.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.russia.meetster.utils.YLSQLRow;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class MeetsterFriend extends YLSQLRow {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	
	public static MeetsterFriend getFromId(Context c, long id) {
		Cursor cursor = c.getContentResolver().query(ContentUris.withAppendedId(MeetsterContract.Friends.getUri(), id), 
				MeetsterContract.Friends.getClassProjection(), null, null, null);
		cursor.moveToFirst();
		return new MeetsterFriend(cursor);
	}
	
	public MeetsterFriend(Cursor cursor) {
		this.id = getCursorLong(cursor, MeetsterContract.Friends._ID);
		this.firstName = getCursorString(cursor, MeetsterContract.Friends.FIRST_NAME);
		this.lastName = getCursorString(cursor, MeetsterContract.Friends.LAST_NAME);
		this.email = getCursorString(cursor, MeetsterContract.Friends.EMAIL);
	}
	
	public MeetsterFriend(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public ContentValues toValues() {
		ContentValues vals = new ContentValues();
		
		vals.put(MeetsterContract.Friends.FIRST_NAME, this.firstName);
		vals.put(MeetsterContract.Friends.LAST_NAME, this.lastName);
		vals.put(MeetsterContract.Friends.EMAIL, this.email);
		
		return vals;
	}
	
	public JSONObject toJSON() {
		JSONObject friend = new JSONObject();
		try {
			friend.put("first_name", this.getFirstName());
			friend.put("last_name", this.getLastName());
			friend.put("email", this.getEmail());
		} catch (JSONException e) {}
		return friend;
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
	public String getEmail() {
		return this.email;
	}
}
