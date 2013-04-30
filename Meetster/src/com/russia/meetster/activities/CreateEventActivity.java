package com.russia.meetster.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.russia.meetster.MeetsterApplication;
import com.russia.meetster.R;
import com.russia.meetster.R.menu;
import com.russia.meetster.fragments.CreateEventFragment;
import com.russia.meetster.fragments.FriendSelectionListFragment;
import com.russia.meetster.fragments.CreateEventFragment.CreateEventFragmentListener;
import com.russia.meetster.fragments.FriendSelectionListFragment.OnFriendsSelectedListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class CreateEventActivity extends Activity implements OnFriendsSelectedListener,CreateEventFragmentListener {
	
	private static final String INVITEEFRAGMENT = "inviteeFragment";
	
	Date startTime;
	Date endTime;
	
	Fragment mCreateEventFragment;
	Fragment mFriendSelectionFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (((MeetsterApplication) this.getApplicationContext()).noFriends()) {
			Intent i = new Intent(this, AddFriendActivity.class);
			startActivity(i);
		} else {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			
			mCreateEventFragment = new CreateEventFragment();
			ft.add(android.R.id.content, mCreateEventFragment);
			ft.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_event, menu);
		return true;
	}
	
	@Override
	public void onFriendSelected(long[] friendIds) {
		List<Long> inviteeIds = new ArrayList<Long>(friendIds.length);
		for (long id : friendIds) {
			inviteeIds.add(id);
		}
		
		((CreateEventFragment) mCreateEventFragment).setInviteeIds(inviteeIds);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(android.R.id.content, mCreateEventFragment);
		ft.commit();
	}

	@Override
	public void onAskForInvitees() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if (mFriendSelectionFragment == null) {
			mFriendSelectionFragment = new FriendSelectionListFragment();
		}
		ft.replace(android.R.id.content, mFriendSelectionFragment, INVITEEFRAGMENT);
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void onEventCreated() {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}

}
