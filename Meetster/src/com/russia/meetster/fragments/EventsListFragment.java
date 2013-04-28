package com.russia.meetster.fragments;

import com.russia.meetster.MeetsterApplication;
import com.russia.meetster.MeetsterLoaderManager;
import com.russia.meetster.R;
import com.russia.meetster.R.id;
import com.russia.meetster.R.layout;
import com.russia.meetster.data.MeetsterContract;
import com.russia.meetster.data.MeetsterEvent;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
	
	// Some useful default bundles
	public static Bundle DEFAULT_BUNDLE(Context c) {
		return new Bundle();
	}
	
	public static Bundle MY_EVENTS_BUNDLE(Context c) {
		Bundle b = new Bundle();
		b.putString("selection", MeetsterContract.Events.CREATORID + "=" + ((MeetsterApplication) c.getApplicationContext()).getCurrentUserId());
		
		return b;
	}
	
	public static Bundle INVITED_BUNDLE(Context c) {
		Bundle b = new Bundle();
		b.putString("selection", MeetsterContract.Events.CREATORID + "!=" + ((MeetsterApplication) c.getApplicationContext()).getCurrentUserId());
		
		return b;
	}
	
	public static Bundle MATCHING_BUNDLE(Context c) {
		Bundle b = new Bundle();
		b.putParcelable("uri", MeetsterContract.Events.getMatchingUserURI(((MeetsterApplication) c.getApplicationContext()).getCurrentUserId()));
		return b;
	}
	
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
			
			MeetsterEvent event = new MeetsterEvent(mContext, cursor);
			
			description.setText(event.getDescription());
			timeRange.setText(event.formatTimeRange());
			creator.setText(event.formatCreator());
			locationTextView.setText(event.getLocationDescription());
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.event_list_item, parent, false);
			return v;
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String selection = this.getArguments().getString("selection");
		String[] selectionArgs = this.getArguments().getStringArray("selectionArgs");
		Uri uri = (Uri) this.getArguments().getParcelable("uri");
		if (uri == null)
			uri = MeetsterContract.Events.getUri();
		
		mContext = this.getActivity();
		
		EventDisplayCursorAdapter eventsCursorAdapter = 
				new EventDisplayCursorAdapter(mContext, null, 0);
		
		// Initialize the content loader
		this.getLoaderManager().initLoader(0, null, 
				new MeetsterLoaderManager(mContext, eventsCursorAdapter, uri, MeetsterContract.Events.getClassProjection(), 
						selection, selectionArgs, "datetime(" + MeetsterContract.Events.START_TIME + ")"));
				
		this.setListAdapter(eventsCursorAdapter);
	}
}
