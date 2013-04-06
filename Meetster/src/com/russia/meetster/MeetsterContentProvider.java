package com.russia.meetster;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

abstract class YLSQLiteOpenHelper extends SQLiteOpenHelper {
	
	protected void createTable(SQLiteDatabase db, BaseTableContract contract) {
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
	
	protected BaseTableContract[] contracts;

	public YLSQLiteOpenHelper(Context context, String dbName, int version, BaseTableContract[] contracts) {
		super(context, dbName, null, version);
		this.contracts = contracts;
	}
	
	public void onCreate(SQLiteDatabase db) {
		for (BaseTableContract contract : contracts) {
			createTable(db, contract);
		}
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
