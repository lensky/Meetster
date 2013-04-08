package com.russia.meetster;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import java.util.Date;

abstract class YLSQLiteOpenHelper extends SQLiteOpenHelper {
	
	protected void createTable(SQLiteDatabase db, BaseTableContract contract) {
		String SQLQuery = "create table " + contract.getTableName() + " ( ";
		SQLQuery += BaseColumns._ID + " integer primary key autoincrement, ";
		
		String[] columns = contract.getColumns();
		String[] columnTypes = contract.getColumnTypes();
		
		for (int i = 0; i < columns.length; i++) {
			SQLQuery += columns[i] + " " + columnTypes[i];
			if (i < columns.length - 1) {
				SQLQuery += ", ";
			}
		}
		for (String string : contract.getExtraConstraints()) {
			SQLQuery += ", " + string;
		}
		SQLQuery += " )";
		
		db.execSQL(SQLQuery);
	}
	
	protected BaseTableContract[] contracts;
	
	private Context currentContext;

	public YLSQLiteOpenHelper(Context context, String dbName, int version, BaseTableContract[] contracts) {
		super(context, dbName, null, version);
		this.contracts = contracts;
	}
	
	public void onCreate(SQLiteDatabase db) {
		for (BaseTableContract contract : contracts) {
			createTable(db, contract);
		}
		MeetsterCategory sports = new MeetsterCategory("Sports"); // 1
		MeetsterCategory food = new MeetsterCategory("Food"); // 2
		MeetsterCategory entertainment = new MeetsterCategory("Entertainment"); // 3
		MeetsterCategory work = new MeetsterCategory("Work"); // 4
		MeetsterCategory misc = new MeetsterCategory("Misc"); // 5
		
		sports.setId(db.insert(MeetsterContract.Categories.getTableName(), null, sports.toValues()));
		food.setId(db.insert(MeetsterContract.Categories.getTableName(), null, food.toValues()));
		entertainment.setId(db.insert(MeetsterContract.Categories.getTableName(), null, entertainment.toValues()));
		work.setId(db.insert(MeetsterContract.Categories.getTableName(), null, work.toValues()));
		misc.setId(db.insert(MeetsterContract.Categories.getTableName(), null, misc.toValues()));
		
		MeetsterFriend yuri = new MeetsterFriend(1, "Yuri", "Lensky");
		MeetsterFriend trond = new MeetsterFriend(2, "Trond", "Andersen");
		MeetsterFriend nicolai = new MeetsterFriend(3, "Nicolai", "Ludvigsen");
		MeetsterFriend michael = new MeetsterFriend(4, "Michael", "Rodriguez");
		
		db.insert(MeetsterContract.Friends.getTableName(), null, yuri.toValues());
		db.insert(MeetsterContract.Friends.getTableName(), null, trond.toValues());
		db.insert(MeetsterContract.Friends.getTableName(), null, nicolai.toValues());
		db.insert(MeetsterContract.Friends.getTableName(), null, michael.toValues());
		
		MeetsterEvent soccer = new MeetsterEvent(trond, "Kresge", sports, "Soccer", new Date(), new Date());
		MeetsterEvent dinner = new MeetsterEvent(nicolai, "Chi Phi", food, "Dinner", new Date(), new Date());
		MeetsterEvent pset = new MeetsterEvent(michael, "W20", work, "8.03 P-Set 3", new Date(), new Date());
		
		db.insert(MeetsterContract.Events.getTableName(), null, soccer.toValues());
		db.insert(MeetsterContract.Events.getTableName(), null, dinner.toValues());
		db.insert(MeetsterContract.Events.getTableName(), null, pset.toValues());
	}
}

class MeetsterDBOpenHelper extends YLSQLiteOpenHelper {
	private static final int VERSION = 1;
	
	public MeetsterDBOpenHelper(Context context) {
		super(context, "MeetsterDB", VERSION, new BaseTableContract[] {
				MeetsterContract.Events,
				MeetsterContract.Categories,
				MeetsterContract.Friends,
		});
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		return;
	}
}

public class MeetsterContentProvider extends ContentProvider {
	
	private void notifyURIChange(Uri uri) {
		getContext().getContentResolver().notifyChange(uri, null);
	}
	
	private static String addIdToWhere(String where, String _ID, Uri uri) {
		return (TextUtils.isEmpty(where) ? "" : where + " and ") + BaseColumns._ID + "=" + ContentUris.parseId(uri);
	}
	
	private Uri createTableURI(String tableName) {
		return Uri.withAppendedPath(Uri.parse("content://" + AUTHORITY), tableName);
	}
	
	private Uri createTableIdURI(String tableName, long id) {
		return ContentUris.withAppendedId(createTableURI(tableName), id);
	}

	// Path types
	private static final int EVENT = 1;
	private static final int EVENT_ID = 2;
	private static final int CATEGORY = 3;
	private static final int CATEGORY_ID = 4;
	private static final int FRIEND = 5;
	private static final int FRIEND_ID = 6;

	private final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	private void addMeetsterURI(String authority, String path, int tableCode, int tableIdCode) {
		uriMatcher.addURI(authority, path, tableCode);
		uriMatcher.addURI(authority, path + "/#", tableIdCode);
	}
	
	// Non-static variables initialized in onCreate
	private String AUTHORITY;
	private SQLiteOpenHelper sqliteHelper;

	@Override
	public boolean onCreate() {
		// Defining the path structure
		AUTHORITY = getContext().getString(R.string.meetster_authority);

		addMeetsterURI(AUTHORITY, MeetsterContract.Events.getTableName(), EVENT, EVENT_ID);
		addMeetsterURI(AUTHORITY, MeetsterContract.Friends.getTableName(), FRIEND, FRIEND_ID);
		addMeetsterURI(AUTHORITY, MeetsterContract.Categories.getTableName(), CATEGORY, CATEGORY_ID);
		
		// Setting non-static variables
		sqliteHelper = new MeetsterDBOpenHelper(getContext());
		return true;
	}

	private String constructRelatedTableJoin(BaseTableContract contract1, BaseTableContract contract2, String col1, String col2) {
		String[] contract1Cols = contract1.getClassProjection();
		String[] contract2Cols = contract2.getColumns();
		
		String contract1Table = contract1.getTableName();
		String contract2Table = contract2.getTableName();

		String selectColumns = "";

		for (int i = 0; i < contract1Cols.length; ++i) {
			selectColumns += contract1Table + "." + contract1Cols[i] + ",";
		}
		selectColumns += contract2Table + "." + contract2Cols[0];
		for (int i = 1; i < contract2Cols.length; ++i) {
			selectColumns += "," + contract2Table +  "." + contract2Cols[1];
		}

		return "(SELECT " + selectColumns +
				" FROM (" + contract1Table +  " INNER JOIN " +
				contract2Table +
				" ON " + contract1Table + "." + MeetsterContract.Events.CATEGORY +
				"=" + contract2Table + "." + MeetsterContract.Categories._ID +
				"))";
	}
	
	private String constructRelatedTableTripleJoin(BaseTableContract contract1, BaseTableContract contract2, BaseTableContract contract3, String col2, String col3) {
		String[] contract1Cols = contract1.getClassProjection();
		String[] contract2Cols = contract2.getColumns();
		String[] contract3Cols = contract3.getColumns();
		
		String contract1Table = contract1.getTableName();
		String contract2Table = contract2.getTableName();
		String contract3Table = contract3.getTableName();

		String selectColumns = "";

		for (int i = 0; i < contract1Cols.length; ++i) {
			selectColumns += contract1Table + "." + contract1Cols[i] + ",";
		}
		selectColumns += contract2Table + "." + contract2Cols[0];
		for (int i = 1; i < contract2Cols.length; ++i) {
			selectColumns += "," + contract2Table +  "." + contract2Cols[i];
		}
		for (int i = 0; i < contract3Cols.length; ++i) {
			selectColumns += "," + contract3Table +  "." + contract3Cols[i];
		}


		return "(SELECT " + selectColumns +
				" FROM (" + contract1Table +  " INNER JOIN " + contract2Table +
				" ON " + contract1Table + "." + col2 +
				"=" + contract2Table + "." + contract2._ID +
				" INNER JOIN " + contract3Table + " ON " +
				contract1Table + "." + col3 + 
				"=" + contract3Table + "." + contract3._ID
				+ "))";
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qB = new SQLiteQueryBuilder();
		
		switch (uriMatcher.match(uri)) {
		case EVENT:
			qB.setTables(constructRelatedTableTripleJoin(MeetsterContract.Events, MeetsterContract.Categories, MeetsterContract.Friends,
					MeetsterContract.Events.CATEGORY, MeetsterContract.Events.CREATORID));
			break;
		case EVENT_ID:
			qB.setTables(constructRelatedTableTripleJoin(MeetsterContract.Events, MeetsterContract.Categories, MeetsterContract.Friends,
					MeetsterContract.Events.CATEGORY, MeetsterContract.Events.CREATORID));
			qB.appendWhere(BaseColumns._ID + "=" + ContentUris.parseId(uri));
			break;
		case UriMatcher.NO_MATCH:
			throw new IllegalArgumentException("Invalid URI for Meetster query: " + uri);
		default:
			List<String> pathSegments = uri.getPathSegments();
			qB.setTables(pathSegments.get(0));
			if (pathSegments.size() > 1) {
				qB.appendWhere(BaseColumns._ID + "=" + ContentUris.parseId(uri));
			}
			break;
		}
		
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		String testQuery = qB.buildQuery(projection, selection, selectionArgs, null, null, null, null);
		Cursor c = qB.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String tableName = uri.getPathSegments().get(0);
		
		switch (uriMatcher.match(uri)) {
		case EVENT:
			break;
		case CATEGORY:
			break;
		case FRIEND:
			break;
		default:
			throw new IllegalArgumentException("Invalid URI for Meetster insertion: " + uri);
		}
		
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		long newid = db.insert(tableName, null, values);
		notifyURIChange(uri);
		return createTableIdURI(tableName, newid);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		String tableName;
		
		switch(uriMatcher.match(uri)) {
		case UriMatcher.NO_MATCH:
			throw new IllegalArgumentException("Invalid URI for Meetster update: " + uri);
		default:
			List<String> pathSegments = uri.getPathSegments();
			tableName = pathSegments.get(0);
			if (pathSegments.size() > 1) {
				addIdToWhere(selection, BaseColumns._ID, uri);
			}
			break;
		}
		
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		int updatedRows = db.update(tableName, values, selection, selectionArgs);
		
		notifyURIChange(uri);
		
		return updatedRows;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String tableName;

		switch(uriMatcher.match(uri)) {
		case UriMatcher.NO_MATCH:
			throw new IllegalArgumentException("Invalid URI for Meetster update: " + uri);
		default:
			List<String> pathSegments = uri.getPathSegments();
			tableName = pathSegments.get(0);
			if (pathSegments.size() > 1) {
				addIdToWhere(selection, BaseColumns._ID, uri);
			}
			break;
		}

		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		int deletedRows = db.delete(tableName, selection, selectionArgs);
				
		notifyURIChange(uri);

		return deletedRows;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

}
