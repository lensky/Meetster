package com.russia.meetster;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MeetsterContract {
	
	private static String addForeignKeyConstraint(String localCol, String remoteTable, String remoteCol) {
		return "foreign key(" + localCol + ") references " + remoteTable + "(" + remoteCol + ")";
	}
	
	private final static class TABLES {
		public final static String EVENTS = "events";
		public final static String CATEGORIES = "categories";
		public final static String FRIENDS = "friends";
	}
	
	final static class EventsContract extends BaseTableContract {
		public final static String	CREATORID				= "events_creatorid";
		public final static String	CREATION_TIME			= "events_creationtime";
		public final static String	CATEGORY				= "events_category";
		public final static String	INVITEE_IDS				= "events_inviteeids";
		public final static String	DESCRIPTION				= "events_description";
		public final static String	START_TIME				= "events_starttime";
		public final static String	END_TIME				= "events_endtime";
		public final static String	LATITUDE				= "events_latitude";
		public final static String	LONGITUDE				= "events_longitude";
		public final static String	MAX_RADIUS				= "events_maxradius";
		public final static String	LOCATION_DESCRIPTION	= "events_locationdescription";
		public final static String  MATCHINGID  	        = "m_id";
				
		public String[] getColumns() {
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
		
		public String[] getClassProjection() {
			return new String[] {
					_ID,
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
		
		public String[] getColumnTypes() {
			return new String[] {
					"integer",
					"datetime default current_timestamp",
					"integer",
					"text", // NOTE: THIS MEANS SERIALIZING DATA
					"text",
					"datetime",
					"datetime",
					"real",
					"real",
					"real",
					"text",
			};
		}
		
		public String[] getExtraConstraints() {
			return new String[] {
					addForeignKeyConstraint(CREATORID, Friends.getTableName(), BaseColumns._ID),
					addForeignKeyConstraint(CATEGORY, Categories.getTableName(), BaseColumns._ID),
			};
		}
		
		public String getTableName() {
			return TABLES.EVENTS;
		}
		
		public Uri getMatchingUserURI(Long id) {
			return ContentUris.withAppendedId(Uri.withAppendedPath(this.getUri(), "matching_user"), id);
		}
	}
	
	final static class CategoriesContract extends BaseTableContract {
		public final static String DESCRIPTION = "categories_description";
		
		public String[] getColumns() {
			return new String[] {
					DESCRIPTION,
			};
		}
		
		public String[] getColumnTypes() {
			return new String[] {
					"text",
			};
		}
		
		public String[] getClassProjection() {
			return new String[] {
					_ID,
					DESCRIPTION,
			};
		}
		
		public String getTableName() {
			return TABLES.CATEGORIES;
		}
		
		public String[] getExtraConstraints() {
			return new String[] {};
		}
	}
	
	final static class FriendsContract extends BaseTableContract {
		public final static String FIRST_NAME = "friends_firstname";
		public final static String LAST_NAME = "friends_lastname";
		
		public String[] getColumns() {
			return new String[] {
					FIRST_NAME,
					LAST_NAME,
			};
		}
		
		public String[] getClassProjection() {
			return new String[] {
					_ID,
					FIRST_NAME,
					LAST_NAME,
			};
		}
		
		public String[] getColumnTypes() {
			return new String[] {
					"text",
					"text",
			};
		}
		
		public String getTableName() {
			return TABLES.FRIENDS;
		}
		
		public String[] getExtraConstraints() {
			return new String[] {};
		}
	}
	
	public final static FriendsContract Friends = new FriendsContract();
	public final static CategoriesContract Categories = new CategoriesContract();
	public final static EventsContract Events = new EventsContract();
}
