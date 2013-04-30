package com.russia.meetster.activities;

import com.russia.meetster.fragments.RemoteFriendListFragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class AddFriendActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
		ft.add(android.R.id.content, new RemoteFriendListFragment());
		ft.commit();
	}
}
