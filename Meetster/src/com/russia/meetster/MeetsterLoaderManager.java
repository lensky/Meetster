package com.russia.meetster;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CursorAdapter;

public class MeetsterLoaderManager implements LoaderCallbacks<Cursor> {

	private CursorAdapter mCursorAdapter;
	private String mSelection;
	private String[] mSelectionArgs;
	private Uri mUri;
	private Context mContext;
	private String[] mProjection;
	private String mSortOrder;
	
	public MeetsterLoaderManager(Context context, CursorAdapter ca, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		mCursorAdapter = ca;
		mSelection = selection;
		mSelectionArgs = selectionArgs;
		mUri = uri;
		mContext = context;
		mProjection = projection;
		mSortOrder = sortOrder;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {			
		return new CursorLoader(mContext, mUri, mProjection, mSelection, mSelectionArgs, mSortOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mCursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursorAdapter.swapCursor(null);
	}

}
