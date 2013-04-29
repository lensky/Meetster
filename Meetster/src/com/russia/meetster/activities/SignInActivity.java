package com.russia.meetster.activities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.russia.meetster.MeetsterApplication;
import com.russia.meetster.R;
import com.russia.meetster.R.layout;
import com.russia.meetster.R.menu;
import com.russia.meetster.data.MeetsterFriend;
import com.russia.meetster.utils.NetworkUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends Activity {
	private Button mCreateAccount;
	private EditText mEditFirstName,mEditLastName,mEditEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
		mCreateAccount = (Button) this.findViewById(R.id.buttonCreateAccount);
		mEditFirstName = (EditText) this.findViewById(R.id.editTextFirstName);
		mEditLastName = (EditText) this.findViewById(R.id.editTextLastName);
		mEditEmail = (EditText) this.findViewById(R.id.editTextEmail);
		
		AccountManager am = AccountManager.get(this);
		Account[] accounts = am.getAccountsByType("com.google");
		
		if (accounts.length > 0) {
			String defaultEmail = accounts[0].name;
			mEditEmail.setText(defaultEmail);
		}
		
		mCreateAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCreateAccount.setEnabled(false);
				
				String firstName = mEditFirstName.getText().toString();
				String lastName = mEditLastName.getText().toString();
				String email = mEditEmail.getText().toString();
				
				final MeetsterFriend friend = new MeetsterFriend(firstName, lastName, email);
				new CreateRemoteUser().execute(friend);
			}
		});
	}
	
	private class CreateRemoteUser extends AsyncTask<MeetsterFriend,Void,Long> {
		@Override
		protected Long doInBackground(MeetsterFriend... params) {
			// TODO Auto-generated method stub
			long id = -1;
			try {
				id = NetworkUtils.createRemoteUser(params[0]);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return id;
		}
		
		@Override
		protected void onPostExecute(Long result) {
			((MeetsterApplication) getApplicationContext()).setCurrentUserId(result);
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in, menu);
		return true;
	}

}
