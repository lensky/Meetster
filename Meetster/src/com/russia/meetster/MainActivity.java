package com.russia.meetster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private ListView friendsEventsView;
	private EventDisplayCursorAdapter friendsEventsCursorAdapter;
	
	class EventDisplayCursorAdapter extends CursorAdapter {

		public EventDisplayCursorAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView description = (TextView) view.findViewById(R.id.descriptionTextView);
			TextView timeRange = (TextView) view.findViewById(R.id.timeRangeTextView);
			TextView creator = (TextView) view.findViewById(R.id.creatorTextView);
			
			MeetsterEvent event = new MeetsterEvent(cursor);
			
			description.setText(event.getDescription());
			timeRange.setText(event.formatTimeRange());
			creator.setText(event.formatCreator());
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.event_list_item, parent, false);
			return v;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Initialize the content loader
		getSupportLoaderManager().initLoader(0, null, this);
		
		// Getting views on the page
		friendsEventsView = (ListView) findViewById(R.id.friendsEventsList);
		
		friendsEventsCursorAdapter = new EventDisplayCursorAdapter(this.getBaseContext(), null, 0);
		
		friendsEventsView.setAdapter(friendsEventsCursorAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.game_main, menu);
		return true;
	}
	/** Called when the user clicks the Send button */


	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		List<String> projection = new ArrayList<String>(Arrays.asList(MeetsterContract.Events.getClassProjection()));
		List<String> friendsColumns = new ArrayList<String>(Arrays.asList(MeetsterContract.Friends.getColumns()));
		List<String> categoriesColumns = new ArrayList<String>(Arrays.asList(MeetsterContract.Categories.getColumns()));
		projection.addAll(friendsColumns);
		projection.addAll(categoriesColumns);
		String[] projectionString = projection.toArray(new String[0]);
		return new CursorLoader(getBaseContext(), MeetsterContract.Events.getUri(), projectionString, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		friendsEventsCursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		friendsEventsCursorAdapter.swapCursor(null);
	}

}
