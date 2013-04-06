package com.russia.meetster;

import java.util.ArrayList;
import java.util.Date;

import android.location.Location;

public class MeetsterEvent {
	private long id;
	private long creatorId;
	
	// We also plan to allow users to just enter a colloquial location description
	private Location location;
	private String locationDescription;
	
	private MeetsterCategory category;
	private String description;
	
	private Date creationTime;
	
	// Length 2 array: {start, end}.
	private Date[] timeRange;
	
	private ArrayList<Long> inviteeIds;
	
	public MeetsterEvent(long id, long creatorId, Location location,
			String locationDescription, MeetsterCategory category,
			String description, Date creationTime, Date[] timeRange,
			ArrayList<Long> inviteeIds) {
		this.id = id;
		this.creatorId = creatorId;
		this.location = location;
		this.locationDescription = locationDescription;
		this.category = category;
		this.description = description;
		this.creationTime = creationTime;
		this.timeRange = timeRange;
		this.inviteeIds = inviteeIds;
	}

	public ArrayList<Long> getInviteeIds() {
		return inviteeIds;
	}
	
	public void setInviteeIds(ArrayList<Long> inviteeIds) {
		this.inviteeIds = inviteeIds;
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
