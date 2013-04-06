package com.russia.meetster;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

abstract class YLSQLiteOpenHelper extends SQLiteOpenHelper {
	
	// Pass in columns and types in two separate, same-length lists
	protected void createDB(SQLiteDatabase db, BaseTableContract contract) {
		String SQLQuery = "create table " + contract.getTableName() + " ( ";
		SQLQuery += contract._ID + " integer primary key autoincrement, ";
		
		String[] columns = contract.getColumnTypes();
		String[] columnTypes = contract.getColumnTypes();
		
		for (int i = 0; i < columns.length; i++) {
			SQLQuery += columns[i] + " " + columnTypes[i];
			if (i < columns.length - 1) {
				SQLQuery += ",";
			}
			SQLQuery += " ";
		}
		SQLQuery += ")";
		
		db.execSQL(SQLQuery);
	}
	
	protected BaseTableContract contract;

	public YLSQLiteOpenHelper(Context context, BaseTableContract contract, int version) {
		super(context, contract.getTableName(), null, version);
		this.contract = contract;
	}
	
	public void onCreate(SQLiteDatabase db) {
		createDB(db, this.contract);
	}
}

class EventsOpenHelper extends YLSQLiteOpenHelper {
	private final static int VERSION = 1;
	
	public EventsOpenHelper(Context context) {
		super(context, MeetsterContract.Events, VERSION);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Not upgrading as of now
		return;
	}
}

class FriendsOpenHelper extends YLSQLiteOpenHelper {
	private final static int VERSION = 1;
	
	public FriendsOpenHelper(Context context) {
		super(context, MeetsterContract.Friends, VERSION);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Not upgrading as of now
		return;
	}
}

class CategoriesOpenHelper extends YLSQLiteOpenHelper {
	private final static int VERSION = 1;
	
	public CategoriesOpenHelper(Context context) {
		super(context, MeetsterContract.Categories, VERSION);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Not upgrading as of now
		return;
	}
}

public class MeetsterContentProvider extends ContentProvider {

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
