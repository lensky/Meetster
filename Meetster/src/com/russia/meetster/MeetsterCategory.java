package com.russia.meetster;

import android.content.ContentValues;

public class MeetsterCategory extends YLSQLRow {
	private Long id;
	private String description;

	
	public MeetsterCategory(String description) {
		this.setDescription(description);
		this.setId(null);
	}
	
	public long getId() {
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
		vals.put(MeetsterContract.Categories.DESCRIPTION, this.getDescription());
		
		return vals;
	}
}
