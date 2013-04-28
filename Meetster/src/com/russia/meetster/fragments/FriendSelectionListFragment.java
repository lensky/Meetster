package com.russia.meetster.fragments;

import com.russia.meetster.MeetsterLoaderManager;
import com.russia.meetster.R;
import com.russia.meetster.R.id;
import com.russia.meetster.R.layout;
import com.russia.meetster.data.MeetsterContract;
import com.russia.meetster.data.MeetsterFriend;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.CheckedTextView;

public class FriendSelectionListFragment extends ListFragment {
	private Context mContext;
	private OnFriendsSelectedListener mFriendsCallback;
	private Button mAcceptFriendChoice;
	private ListView mListView;
	
	class FriendsSelectionListAdapter extends CursorAdapter {
		public FriendsSelectionListAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			MeetsterFriend friend = new MeetsterFriend(cursor);
			
			((CheckedTextView) view).setText(friend.getFirstName() + " " + friend.getLastName());
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
			return v;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this.getActivity();
		
		FriendsSelectionListAdapter friendsCursorAdapter = 
				new FriendsSelectionListAdapter(mContext, null, 0);
		
		// Initialize the content loader
		this.getLoaderManager().initLoader(0, null, 
				new MeetsterLoaderManager(mContext, friendsCursorAdapter, MeetsterContract.Friends.getUri(), 
						MeetsterContract.Friends.getClassProjection(), 
						null, null, null));
				
		this.setListAdapter(friendsCursorAdapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.friend_selection_list_layout, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mListView = this.getListView();
		mAcceptFriendChoice = (Button) this.getView().findViewById(R.id.acceptFriendChoice);
		
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		mAcceptFriendChoice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mFriendsCallback.onFriendSelected(mListView.getCheckedItemIds());
			}
		});
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (l.getCheckedItemCount() > 0) {
			mAcceptFriendChoice.setVisibility(View.VISIBLE);
		} else {
			mAcceptFriendChoice.setVisibility(View.GONE);
		}
	}
	
	public interface OnFriendsSelectedListener {
		public void onFriendSelected(long[] friendIds);
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		mFriendsCallback = (OnFriendsSelectedListener) activity;
	}
}
