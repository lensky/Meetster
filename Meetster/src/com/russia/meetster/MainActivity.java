package com.russia.meetster;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
		Intent intent = new Intent(this, CreateEventActivity.class);
		EditText editText = (EditText) findViewById(R.id.editText1);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
	    // Do something in response to button
	}
	public void confirmScreen(View view) {
		Intent intent = new Intent(this, ConfirmationscreenActivity.class);
		EditText editText1 = (EditText) findViewById(R.id.editText1);
		String message = editText1.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		EditText editText2 = (EditText) findViewById(R.id.editText2);
		String message2 = editText2.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message2);
		EditText editText3 = (EditText) findViewById(R.id.editText3);
		String message3 = editText3.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message3);
	    // Do something in response to button
	}
	public void settings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		EditText editText = (EditText) findViewById(R.id.editText1);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
	    // Do something in response to button
	}
}
