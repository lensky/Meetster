package com.russia.meetster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	
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
			TextView locationTextView = (TextView) view.findViewById(R.id.locationTextView);
			
			MeetsterEvent event = new MeetsterEvent(cursor);
			
			description.setText(event.getDescription());
			timeRange.setText(event.formatTimeRange());
			creator.setText(event.formatCreator());
			locationTextView.setText(event.getLocationDescription());
			
			if (event.getCreatorId() == 1) {
				description.setTypeface(null, Typeface.ITALIC);
				timeRange.setTypeface(null, Typeface.ITALIC);
				creator.setTypeface(null, Typeface.ITALIC);
				locationTextView.setTypeface(null, Typeface.ITALIC);
				description.setTextColor(Color.LTGRAY);
				timeRange.setTextColor(Color.LTGRAY);
				creator.setTextColor(Color.LTGRAY);
				locationTextView.setTextColor(Color.LTGRAY);
			}
			
			if (event.getId() == 3) {
				description.setTypeface(null, Typeface.BOLD);
				timeRange.setTypeface(null, Typeface.BOLD);
				creator.setTypeface(null, Typeface.BOLD);
				locationTextView.setTypeface(null, Typeface.BOLD);
			}
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
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		EventsView eventsView = new EventsView();
		fragmentTransaction.add(R.id.mainRootLayout, eventsView);
		fragmentTransaction.commit();
		
		friendsEventsCursorAdapter = new EventDisplayCursorAdapter(this.getBaseContext(), null, 0);
		eventsView.setListAdapter(friendsEventsCursorAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.addEventMenuItem:
			Intent i = new Intent(this, CreateEventActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

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
