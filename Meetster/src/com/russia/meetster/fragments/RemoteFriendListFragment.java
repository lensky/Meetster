package com.russia.meetster.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.russia.meetster.R;
import com.russia.meetster.data.MeetsterFriend;
import com.russia.meetster.utils.NetworkUtils;

import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class RemoteFriendListFragment extends ListFragment {
	
	private Context mContext;
	private EditText mEditSearch;
	private LayoutInflater mInflater;
	private ArrayAdapter<MeetsterFriend> mAdapter;
	
	private class GetRemoteUsers extends AsyncTask<String,Void,List<MeetsterFriend>> {
		
		@Override
		protected List<MeetsterFriend> doInBackground(String... params) {
			List<MeetsterFriend> matchedUsers = new ArrayList<MeetsterFriend>();
			try {
				matchedUsers = NetworkUtils.searchForUsersByEmail(params[0]);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return matchedUsers;
		}
		
		@Override
		protected void onPostExecute(List<MeetsterFriend> result) {
			mAdapter.clear();
			mAdapter.addAll(result);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this.getActivity();
		mAdapter = new ArrayAdapter<MeetsterFriend>(mContext, android.R.layout.simple_list_item_1) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null, false);
				}
				
				MeetsterFriend user = this.getItem(position);
				
				TextView txtName = (TextView) convertView.findViewById(android.R.id.text1);
				
				txtName.setText(user.getFirstName() + " " + user.getLastName());
				
				return convertView;
			}
		};
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mInflater = inflater;
		return inflater.inflate(R.layout.remote_friend_list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mEditSearch = (EditText) this.getView().findViewById(R.id.editTextSearch);
		mEditSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count > 3) {
					String searchString = mEditSearch.getText().toString();
					new GetRemoteUsers().execute(searchString);
				}
			}
		});
	}
}
