package com.russia.meetster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventsListFragment extends ListFragment {
	
	private Context mContext;
	
	class EventDisplayCursorAdapter extends CursorAdapter {

		public EventDisplayCursorAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView description = (TextView) view.findViewById(R.id.descriptionTextView);
			TextView timeRange = (TextView) view.findViewById(R.id.timeRangeTextView);
			TextView creator = (TextView) view.findViewById(R.id.creatorTextView);
			TextView locationTextView = (TextView) view.findViewById(R.id.locationTextView);
			
			MeetsterEvent event = new MeetsterEvent(cursor);
			
			description.setText(event.getDescription());
			timeRange.setText(event.formatTimeRange());
			creator.setText(event.formatCreator());
			locationTextView.setText(event.getLocationDescription());
			
			// De-emphasize entry if created by self
			if (event.getCreatorId() == ((MeetsterApplication) context.getApplicationContext()).getCurrentUserId()) {
				description.setTypeface(null, Typeface.ITALIC);
				timeRange.setTypeface(null, Typeface.ITALIC);
				creator.setTypeface(null, Typeface.ITALIC);
				locationTextView.setTypeface(null, Typeface.ITALIC);
				description.setTextColor(Color.LTGRAY);
				timeRange.setTextColor(Color.LTGRAY);
				creator.setTextColor(Color.LTGRAY);
				locationTextView.setTextColor(Color.LTGRAY);
			}
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.event_list_item, parent, false);
			return v;
		}
		
	}
	
	class EventLoaderManager implements LoaderManager.LoaderCallbacks<Cursor> {
		
		private CursorAdapter mCursorAdapter;
		
		public EventLoaderManager(CursorAdapter ca) {
			mCursorAdapter = ca;
		}

		@Override
		public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
			List<String> projection = new ArrayList<String>(Arrays.asList(MeetsterContract.Events.getClassProjection()));
			List<String> friendsColumns = new ArrayList<String>(Arrays.asList(MeetsterContract.Friends.getColumns()));
			List<String> categoriesColumns = new ArrayList<String>(Arrays.asList(MeetsterContract.Categories.getColumns()));
			projection.addAll(friendsColumns);
			projection.addAll(categoriesColumns);
			String[] projectionString = projection.toArray(new String[0]);
			return new CursorLoader(mContext, MeetsterContract.Events.getUri(), projectionString, null, null, null);
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this.getActivity();
		EventDisplayCursorAdapter eventsCursorAdapter = 
				new EventDisplayCursorAdapter(mContext, null, 0);
		
		// Initialize the content loader
		this.getLoaderManager().initLoader(0, null, 
				new EventLoaderManager(eventsCursorAdapter));
		
		this.setListAdapter(eventsCursorAdapter);
	}
}
