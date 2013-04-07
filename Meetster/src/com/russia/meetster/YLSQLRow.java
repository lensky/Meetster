package com.russia.meetster;

import java.sql.Timestamp;
import java.util.Date;

public class YLSQLRow {
	protected static String dateToSQLTimestamp(Date d) {
		if (d == null)
			return null;
		return new Timestamp(d.getTime()).toString();
	}
}
