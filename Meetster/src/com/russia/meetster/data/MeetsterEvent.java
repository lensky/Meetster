package com.russia.meetster.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.russia.meetster.utils.YLSQLRow;
import com.russia.meetster.utils.YLUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;

public class MeetsterEvent extends YLSQLRow {
	
	private Context mContext;
	
	private Long id;
	
	private Long creatorId;
	private MeetsterFriend creator;
	
	// We also plan to allow users to just enter a colloquial location description
	private Location location;
	private String locationDescription;
	
	private MeetsterCategory category;
	private Long categoryId;
	
	private String description;
	
	private Date creationTime;
	
	// Length 2 array: {start, end}.
	private Date[] timeRange;
	
	private List<Long> inviteeIds;
	
	public MeetsterEvent(Context c, long creatorId, String locationDescription, long categoryId,
			String description, Date startTime, Date endTime, List<Long> inviteeIds) {
		this.mContext = c;
		this.setCreator(creatorId);
		this.locationDescription = locationDescription;
		this.setCategory(categoryId);
		this.description = description;
		this.timeRange = new Date[] { startTime, endTime };
		this.inviteeIds = inviteeIds;
	}
	
	public MeetsterEvent(Context context, Cursor c) {		
		this.mContext = context;
		
		this.id = getCursorLong(c, MeetsterContract.Events._ID);
		this.creatorId = getCursorLong(c, MeetsterContract.Events.CREATORID);
				
		Long longitude = getCursorLong(c, MeetsterContract.Events.LONGITUDE);
		Long latitude = getCursorLong(c, MeetsterContract.Events.LATITUDE);
		
		if ((longitude != null) && (latitude != null)) {
			this.location = new Location("Meetster");
			this.location.setLatitude(latitude);
			this.location.setLongitude(longitude);
		} else { this.location = null; }
		
		this.categoryId = getCursorLong(c, MeetsterContract.Events.CATEGORY);
		
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
	
	public void setCreator(MeetsterFriend creator) {
		this.creatorId = creator.getId();
		this.creator = creator;
	}

	public void setCreator(Long creatorId) {
		if (creatorId != this.creatorId) {
			this.creatorId = creatorId;
			this.creator = null;
		}
	}

	public MeetsterFriend getCreator() {
		if (this.creator == null) {
			this.creator = MeetsterFriend.getFromId(mContext, this.creatorId);
		}
		return this.creator;
	}

	public long getCreatorId() {
		return this.getCreator().getId();
	}

	public void setCategory(MeetsterCategory category) {
		this.category = category;
		this.categoryId = category.getId();
	}

	public void setCategory(Long categoryId) {
		if (categoryId != this.categoryId) {
			this.categoryId = categoryId;
			this.category = null;
		}
	}

	public Long getCategoryId() {
		return this.getCategory().getId();
	}

	public MeetsterCategory getCategory() {
		if (this.category == null) {
			this.category = MeetsterCategory.getFromId(mContext, this.categoryId);
		}
		return this.category;
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
		return this.getCreator().getFirstName();
	}
	
	private String inviteeIdsToString() {
		if (this.inviteeIds == null)
			return null;
		else
			return YLUtils.join(",", inviteeIds);
	}
	
	public ContentValues toValues() {
		ContentValues vals = new ContentValues();
		
		vals.put(MeetsterContract.Events.CREATORID, this.getCreatorId());
		vals.put(MeetsterContract.Events.CATEGORY, this.getCategoryId());
		vals.put(MeetsterContract.Events.INVITEE_IDS, inviteeIdsToString());
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

	public ArrayList<MeetsterFriend> getInvitees() {
		ArrayList<MeetsterFriend> invitees = new ArrayList<MeetsterFriend>();
		for (Long inviteeId : this.inviteeIds) {
			invitees.add(MeetsterFriend.getFromId(mContext, inviteeId));
		}
		return invitees;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
