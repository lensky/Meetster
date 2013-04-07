package com.russia.meetster;

import java.sql.Timestamp;
import java.util.Date;

public class YLSQLRow {
	protected static String dateToSQLTimestamp(Date d) {
		return new Timestamp(d.getTime()).toString();
	}
}
