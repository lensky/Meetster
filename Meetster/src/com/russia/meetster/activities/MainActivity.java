package com.russia.meetster.activities;

import com.russia.meetster.MeetsterApplication;
import com.russia.meetster.R;
import com.russia.meetster.R.id;
import com.russia.meetster.R.menu;
import com.russia.meetster.data.MeetsterContentProvider;
import com.russia.meetster.fragments.EventsListFragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	private ActionBar mActionBar;
	
	class TabListener<T extends Fragment> implements ActionBar.TabListener {
		
		private Fragment mFragment;
		private Class<T> mFragmentClass;
		private Context mContext;
		private Bundle mBundle;
		
		public TabListener(Class<T> fc, Bundle b, Context c) {
			mFragmentClass = fc;
			mContext = c;
			mBundle = b;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// Do nothing on purpose
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (mFragment == null) {
				mFragment = Fragment.instantiate(mContext, mFragmentClass.getName(), mBundle);
				ft.add(android.R.id.content, mFragment);
			} else {
				ft.attach(mFragment);
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null)
				ft.detach(mFragment);
		}
		
	}
	
	private <T extends Fragment> Tab addTab(Class<T> fragmentClass, Bundle b, String text) {
		if (mActionBar == null) {
			mActionBar = getActionBar();
		}
		
		Tab tab = mActionBar.newTab().setText(text);
		tab.setTabListener(new TabListener<T>(fragmentClass, b, this));
		
		mActionBar.addTab(tab);
		
		return tab;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!((MeetsterApplication)this.getApplicationContext()).isLoggedIn()) {
			Intent i = new Intent(this, SignInActivity.class);
			startActivity(i);
		} else {
			mActionBar = getActionBar();
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			addTab(EventsListFragment.class, EventsListFragment.MATCHING_BUNDLE(this), "Matching");
			addTab(EventsListFragment.class, EventsListFragment.INVITED_BUNDLE(this), "Invited");
			addTab(EventsListFragment.class, EventsListFragment.MY_EVENTS_BUNDLE(this), "Mine");
			addTab(EventsListFragment.class, EventsListFragment.DEFAULT_BUNDLE(this), "All");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menuItemAddEvent:
			Intent i = new Intent(this, CreateEventActivity.class);
			startActivity(i);
			return true;
		case R.id.menuItemRefresh:
			ContentResolver.requestSync(((MeetsterApplication) this.getApplicationContext()).getAccount(), 
					MeetsterContentProvider.AUTHORITY, new Bundle());
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
