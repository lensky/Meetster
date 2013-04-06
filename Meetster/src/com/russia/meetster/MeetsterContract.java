package com.russia.meetster;

import android.provider.BaseColumns;

public class MeetsterContract {
	private final static class TABLES {
		public final static String EVENTS = "events.db";
		public final static String CATEGORIES = "categories.db";
	}
	
	public final static class Events implements BaseColumns {
		public final static String CREATORID = "creatorid";
		public final static String CREATION_TIME = "creationtime";
		
		public final static String CATEGORY = "category";
		public final static String INVITEE_IDS = "inviteeids";
		public final static String DESCRIPTION = "description";
		
		public final static String START_TIME = "starttime";
		public final static String END_TIME = "endtime";
		
		public final static String LATITUDE = "latitude";
		public final static String LONGITUDE = "longitude";
		public final static String MAX_RADIUS = "maxradius";
		public final static String LOCATION_DESCRIPTION = "locationdescription";
		

		public static String[] getColumns() {
			return new String[] {
					CREATORID,
					CREATION_TIME,
					CATEGORY,
					INVITEE_IDS,
					DESCRIPTION,
					START_TIME,
					END_TIME,
					LATITUDE,
					LONGITUDE,
					MAX_RADIUS,
					LOCATION_DESCRIPTION,
			};
		}
		
		public static String getTableName() {
			return TABLES.EVENTS;
		}
	}
	
	public final static class Categories implements BaseColumns {
		public final static String DESCRIPTION = "description";
		
		public static String[] getColumns() {
			return new String[] {
					DESCRIPTION,
			};
		}
		
		public static String getTableName() {
			return TABLES.CATEGORIES;
		}
	}
}
