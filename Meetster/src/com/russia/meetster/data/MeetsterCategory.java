package com.russia.meetster.data;

import com.russia.meetster.utils.YLSQLRow;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class MeetsterCategory extends YLSQLRow {
	private Long id;
	private String description;

	
	public MeetsterCategory(String description) {
		this.setDescription(description);
		this.setId(null);
	}
	
	public static MeetsterCategory getFromId(Context context, long id) {
		Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(MeetsterContract.Categories.getUri(), id), 
				MeetsterContract.Categories.getClassProjection(), null, null, null);
		cursor.moveToFirst();
		return new MeetsterCategory(cursor);
	}
	
	public MeetsterCategory(Cursor cursor) {
		this.id = getCursorLong(cursor, MeetsterContract.Categories._ID);
		this.description = getCursorString(cursor, MeetsterContract.Categories.DESCRIPTION);
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean hasId() {
		return (id != null);
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public ContentValues toValues() {
		ContentValues vals = new ContentValues();
		vals.put(MeetsterContract.Categories._ID, this.getId());
		vals.put(MeetsterContract.Categories.DESCRIPTION, this.getDescription());
		
		return vals;
	}
}
