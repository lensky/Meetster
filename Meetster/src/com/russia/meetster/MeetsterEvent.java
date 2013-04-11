package com.russia.meetster;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;

public class MeetsterEvent extends YLSQLRow {
	private Long id;
	
	private MeetsterFriend creator;
	
	// We also plan to allow users to just enter a colloquial location description
	private Location location;
	private String locationDescription;
	
	private MeetsterCategory category;
	private String description;
	
	private Date creationTime;
	
	// Length 2 array: {start, end}.
	private Date[] timeRange;
	
	private ArrayList<Long> inviteeIds;
	
	// NOTE: Get rid of this one!!!!
	public MeetsterEvent(MeetsterFriend creator, String locationDescription, MeetsterCategory category,
			String description, Date startTime, Date endTime) {
		this.creator = creator;
		this.locationDescription = locationDescription;
		this.category = category;
		this.description = description;
		this.timeRange = new Date[] { startTime, endTime };
	}
	
	public MeetsterEvent(Cursor c) {		
		this.id = getCursorLong(c, MeetsterContract.Events._ID);
		this.creator = new MeetsterFriend(getCursorLong(c, MeetsterContract.Events.CREATORID),
				getCursorString(c, MeetsterContract.Friends.FIRST_NAME),
				getCursorString(c, MeetsterContract.Friends.LAST_NAME));
				
		Long longitude = getCursorLong(c, MeetsterContract.Events.LONGITUDE);
		Long latitude = getCursorLong(c, MeetsterContract.Events.LATITUDE);
		
		if ((longitude != null) && (latitude != null)) {
			this.location = new Location("Meetster");
			this.location.setLatitude(latitude);
			this.location.setLongitude(longitude);
		} else { this.location = null; }
		
		this.category = new MeetsterCategory(c);
		
		try {
			this.timeRange = new Date[] {
					sqlTimestampStringToDate(getCursorString(c, MeetsterContract.Events.START_TIME)),
					sqlTimestampStringToDate(getCursorString(c, MeetsterContract.Events.END_TIME)),
			};
		} catch (ParseException e) {
			this.timeRange = null;
		}
		
		try {
			this.creationTime = sqlTimestampStringToDate(getCursorString(c, MeetsterContract.Events.CREATION_TIME));
		} catch (ParseException e) { 
			this.creationTime = null;
		}
		
		String inviteeIdsString = getCursorString(c, MeetsterContract.Events.INVITEE_IDS);
		if (inviteeIdsString != null) {
			String[] strings = inviteeIdsString.split(",");
			this.inviteeIds = new ArrayList<Long>();
			for (String string : strings) {
				this.inviteeIds.add(Long.parseLong(string));
			}
		} else { this.inviteeIds = null; }
		
		this.locationDescription = getCursorString(c, MeetsterContract.Events.LOCATION_DESCRIPTION);
		this.description = getCursorString(c, MeetsterContract.Events.DESCRIPTION);
	}
	
	public String formatTimeRange() {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		DateFormat dateFormat = new SimpleDateFormat("EEE");
		Date startTime = getStartTime();
		Date endTime = getEndTime();
		String t = "From " + timeFormat.format(startTime) + " on " + dateFormat.format(startTime) + 
				" to " + timeFormat.format(endTime) + " on " + dateFormat.format(endTime);
		
		return t;
	}
	
	public String formatCreator() {
		return this.creator.getFirstName();
	}
	
	private String inviteesToString() {
		if (this.inviteeIds == null) {
			return null;
		}
		String inviteesString = "";
		for (int i = 0; i < this.inviteeIds.size(); ++i) {
			inviteesString += this.inviteeIds.get(i);
			if (i < this.inviteeIds.size() - 1) {
				inviteesString += ",";
			}
		}
		return inviteesString;
	}
	
	private Long getCategoryId() {
		return this.category.getId();
	}
	
	public ContentValues toValues() {
		ContentValues vals = new ContentValues();
		
		vals.put(MeetsterContract.Events.CREATORID, this.creator.getId());
		vals.put(MeetsterContract.Events.CATEGORY, this.getCategoryId());
		vals.put(MeetsterContract.Events.INVITEE_IDS, inviteesToString());
		vals.put(MeetsterContract.Events.DESCRIPTION, this.description);
		vals.put(MeetsterContract.Events.START_TIME, dateToSQLTimestamp(getStartTime()));
		vals.put(MeetsterContract.Events.END_TIME, dateToSQLTimestamp(getEndTime()));
		if (hasLocation()) {
			vals.put(MeetsterContract.Events.LATITUDE, getLatitude());
			vals.put(MeetsterContract.Events.LONGITUDE, getLongitude());
		}
		vals.put(MeetsterContract.Events.LOCATION_DESCRIPTION, this.locationDescription);

		return vals;
	}

	public ArrayList<MeetsterFriend> getInvitees(Context context) {
		ArrayList<MeetsterFriend> invitees = new ArrayList<MeetsterFriend>();
		for (Long inviteeId : this.inviteeIds) {
			invitees.add(MeetsterFriend.getFromId(context, inviteeId));
		}
		return invitees;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreatorId() {
		return creator.getId();
	}
	
	public MeetsterFriend getCreator(Context c) {
		return this.creator;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public boolean hasLocation() {
		return (this.location != null);
	}
	
	public Double getLatitude() {
		return (this.location == null) ? null : this.location.getLatitude();
	}
	
	public Double getLongitude() {
		return (this.location == null) ? null : this.location.getLongitude();
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public MeetsterCategory getCategory(Context context) {
		return this.category;
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
		return (timeRange == null) ? null : timeRange[0];
	}
	
	public Date getEndTime() {
		return (timeRange == null) ? null : timeRange[1];
	}
}
