package com.russia.meetster;

import java.sql.SQLXML;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.location.Location;
import android.sax.StartElementListener;

public class MeetsterEvent extends YLSQLRow {
	private Long id;
	private Long creatorId;
	
	// We also plan to allow users to just enter a colloquial location description
	private Location location;
	private String locationDescription;
	
	private MeetsterCategory category;
	private String description;
	
	private Date creationTime;
	
	// Length 2 array: {start, end}.
	private Date[] timeRange;
	
	private ArrayList<MeetsterFriend> invitees;
	
	public MeetsterEvent(Long id, Long creatorId, Location location,
			String locationDescription, MeetsterCategory category,
			String description, Date creationTime, Date[] timeRange,
			ArrayList<MeetsterFriend> invitees) {
		this.id = id;
		this.creatorId = creatorId;
		this.location = location;
		this.locationDescription = locationDescription;
		this.category = category;
		this.description = description;
		this.creationTime = creationTime;
		this.timeRange = timeRange;
		this.invitees = invitees;
	}
	
	public ContentValues toValues() {
		ContentValues vals = new ContentValues();
		
		vals.put(MeetsterContract.Events.CREATORID, this.creatorId);
		vals.put(MeetsterContract.Events.CREATION_TIME, dateToSQLTimestamp(creationTime));
		vals.put(MeetsterContract.Events.CATEGORY, this.category.getId());
		vals.put(MeetsterContract.Events.INVITEE_IDS, this.creatorId);
		vals.put(MeetsterContract.Events.DESCRIPTION, this.creatorId);
		vals.put(MeetsterContract.Events.START_TIME, dateToSQLTimestamp(getStartTime()));
		vals.put(MeetsterContract.Events.END_TIME, dateToSQLTimestamp(getEndTime()));
		vals.put(MeetsterContract.Events.LATITUDE, getLatitude());
		vals.put(MeetsterContract.Events.LONGITUDE, getLongitude());
		vals.put(MeetsterContract.Events.LOCATION_DESCRIPTION, this.locationDescription);

		return vals;
	}

	public ArrayList<MeetsterFriend> getInvitees() {
		return invitees;
	}
	
	public void setInvitees(ArrayList<MeetsterFriend> invitees) {
		this.invitees = invitees;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public double getLatitude() {
		return this.location.getLatitude();
	}
	
	public double getLongitude() {
		return this.location.getLongitude();
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public MeetsterCategory getCategory() {
		return category;
	}

	public void setCategory(MeetsterCategory category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date[] getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(Date[] timeRange) {
		this.timeRange = timeRange;
	}
	
	public Date getStartTime() {
		return timeRange[0];
	}
	
	public Date getEndTime() {
		return timeRange[1];
	}
}
