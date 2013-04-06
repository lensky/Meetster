package com.russia.meetster;

import android.provider.BaseColumns;

public interface BaseTableContract extends BaseColumns {
	public String[] getColumns();
	public String[] getColumnTypes();
	public String getTableName();
}
