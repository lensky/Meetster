package com.russia.meetster;

import android.app.Activity;
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
		
		public TabListener(Class<T> fc, Context c) {
			mFragmentClass = fc;
			mContext = c;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// Do nothing on purpose
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (mFragment == null) {
				mFragment = Fragment.instantiate(mContext, mFragmentClass.getName());
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
	
	private <T extends Fragment> Tab addTab(Class<T> fragmentClass, String text) {
		if (mActionBar == null) {
			mActionBar = getActionBar();
		}
		
		Tab tab = mActionBar.newTab().setText(text);
		tab.setTabListener(new TabListener<T>(fragmentClass, this));
		
		mActionBar.addTab(tab);
		
		return tab;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActionBar = getActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		addTab(EventsListFragment.class, "Matching");
		addTab(EventsListFragment.class, "Invited");
		addTab(EventsListFragment.class, "Mine");
		addTab(EventsListFragment.class, "All");
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
		case R.id.addEventMenuItem:
			Intent i = new Intent(this, CreateEventActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
