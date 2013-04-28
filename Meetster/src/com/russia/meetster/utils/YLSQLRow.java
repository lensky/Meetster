package com.russia.meetster.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.Cursor;

public class YLSQLRow {
	protected static String dateToSQLTimestamp(Date d) {
		if (d == null)
			return null;
		return new Timestamp(d.getTime()).toString();
	}
		
	protected static Long getCursorLong(Cursor c, String columnName) {
		int i = c.getColumnIndex(columnName);
		if (i == -1)
			return null;
		else
			return c.getLong(i);
	}
	
	protected static String getCursorString(Cursor c, String columnName) {
		int i = c.getColumnIndex(columnName);
		if (i == -1)
			return null;
		else
			return c.getString(i);
	}
	
	protected static Date sqlTimestampStringToDate(String dateString) throws ParseException {
		if (dateString == null)
			return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.parse(dateString);
	}
}
