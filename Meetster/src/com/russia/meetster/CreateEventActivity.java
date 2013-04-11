package com.russia.meetster;

import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class CreateEventActivity extends Activity {
	
	EditText descriptionText;
	EditText locationText;
	
	ToggleButton foodToggle,businessToggle,entertainmentToggle,sportsToggle,miscToggle;
	
	Date startTime;
	Date endTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_event);
		
		descriptionText = (EditText) findViewById(R.id.titleofEvent);
		locationText = (EditText) findViewById(R.id.locationtext);
		foodToggle = (ToggleButton) findViewById(R.id.knife);
		businessToggle = (ToggleButton) findViewById(R.id.briefcase);
		entertainmentToggle = (ToggleButton) findViewById(R.id.tv_icon);
		sportsToggle = (ToggleButton) findViewById(R.id.basketball);
		miscToggle = (ToggleButton) findViewById(R.id.misc);
		
		startTime = new Date();
		endTime = new Date();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_event, menu);
		return true;
	}
	
	private void setTogglesToFalse() {
		foodToggle.setChecked(false);
		businessToggle.setChecked(false);
		entertainmentToggle.setChecked(false);
		sportsToggle.setChecked(false);
		miscToggle.setChecked(false);
	}
	
	public void setCategory(View view) {
		setTogglesToFalse();
		ToggleButton current = (ToggleButton) view;
		current.setChecked(true);
	}
	
	public void createEvent(View view) {
		String description = descriptionText.getText().toString();
		String locationDescription = locationText.getText().toString();
		
		MeetsterFriend creator = MeetsterFriend.getFromId(this, 1);
		
		long creatorId;
		if (foodToggle.isChecked()) {
			creatorId = 2;
		} else if (businessToggle.isChecked()) {
			creatorId = 4;
		} else if (entertainmentToggle.isChecked()) {
			creatorId = 3;
		} else if (sportsToggle.isChecked()) {
			creatorId = 1;
		} else {
			creatorId = 5;
		}
		MeetsterCategory category = MeetsterCategory.getFromId(this, creatorId);
		
		MeetsterEvent newEvent = new MeetsterEvent(creator, locationDescription, category, description, startTime, endTime);
		getContentResolver().insert(MeetsterContract.Events.getUri(), newEvent.toValues());
		
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
	
	public void showTimeDialog(final Date dateObj, String text) {
        //set up dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.maindialog);
        dialog.setTitle(text);
        dialog.setCancelable(true);

        Button button = (Button) dialog.findViewById(R.id.Button01);
        final TimePicker timepicker = (TimePicker) dialog.findViewById(R.id.timePicker1);
        final DatePicker datepicker = (DatePicker) dialog.findViewById(R.id.datePicker1);
        
        button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
        	int hour = timepicker.getCurrentHour();
        	int minute = timepicker.getCurrentMinute();
        	
        	int dayOfMonth = datepicker.getDayOfMonth();
        	int month = datepicker.getMonth();
        	int year = datepicker.getYear();
        	
        	dateObj.setYear(year);
        	dateObj.setMonth(month);
        	dateObj.setDate(dayOfMonth);
        	dateObj.setHours(hour);
        	dateObj.setMinutes(minute);
        	        	
        	dialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it    
        dialog.show();
	}

	
	public void showStartTimeDialog(View view) {
		//set up dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.maindialog);
        dialog.setTitle("Select start and end times.");
        dialog.setCancelable(true);

        Button button = (Button) dialog.findViewById(R.id.Button01);
        final TimePicker timepicker1 = (TimePicker) dialog.findViewById(R.id.timePicker1);
        final DatePicker datepicker1 = (DatePicker) dialog.findViewById(R.id.datePicker1);
        final TimePicker timepicker2 = (TimePicker) dialog.findViewById(R.id.timePicker2);
        final DatePicker datepicker2 = (DatePicker) dialog.findViewById(R.id.datePicker2);
        
        button.setOnClickListener(new OnClickListener() {
        	private void setDate(Date dateObj, TimePicker timepicker, DatePicker datepicker) {
            	int hour = timepicker.getCurrentHour();
            	int minute = timepicker.getCurrentMinute();
            	
            	int dayOfMonth = datepicker.getDayOfMonth();
            	int month = datepicker.getMonth();
            	int year = datepicker.getYear();
            	
            	dateObj.setYear(year);
            	dateObj.setMonth(month);
            	dateObj.setDate(dayOfMonth);
            	dateObj.setHours(hour);
            	dateObj.setMinutes(minute);
        	}
        @Override
            public void onClick(View v) {
        	setDate(startTime, timepicker1, datepicker1);
        	setDate(endTime, timepicker2, datepicker2);
        	dialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it    
        dialog.show();
	}

}
