package com.russia.meetster;

import android.content.ContentValues;

public class MeetsterFriend {
	private long id;
	private String firstName;
	private String lastName;
	
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
