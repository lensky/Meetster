package com.russia.meetster;

import android.net.Uri;
import android.provider.BaseColumns;

public abstract class BaseTableContract implements BaseColumns {
	public abstract String[] getColumns();
	public abstract String[] getColumnTypes();
	public abstract String getTableName();
	public abstract String[] getExtraConstraints();
	public Uri getUri() {
		return Uri.withAppendedPath(Uri.parse("content://" + MeetsterContentProvider.AUTHORITY), getTableName());
	}
	public String[] getClassProjection() {
		// TODO Auto-generated method stub
		return null;
	}
}
