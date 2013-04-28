package com.russia.meetster.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.russia.meetster.MeetsterApplication;
import com.russia.meetster.R;
import com.russia.meetster.R.id;
import com.russia.meetster.R.layout;
import com.russia.meetster.data.MeetsterContract;
import com.russia.meetster.data.MeetsterEvent;
import com.russia.meetster.fragments.DatePickerFragment.DatePickerListener;
import com.russia.meetster.fragments.TimePickerFragment.TimePickerListener;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class CreateEventFragment extends Fragment implements TimePickerListener,DatePickerListener {
	
	private CreateEventFragmentListener mCallback;
	
	private List<Long> mInviteeIds;
	
	private Date mStartTime, mEndTime;
	private DateFormat mTimeFormat, mDateFormat;
	
	private Button buttonStartTime,buttonEndTime,buttonStartDate,buttonEndDate;
	
	private static final int STARTTIMEID = 1;
	private static final int ENDTIMEID = 2;
	
	private View mView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mStartTime = new Date();
		mEndTime = changeHour(mStartTime, 1);
	}
	
	private Date changeHour(Date d, int delta) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.HOUR_OF_DAY, delta);
		
		return c.getTime();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_new_event, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mView = this.getView();
		
		Button buttonAskForInvitees = (Button) mView.findViewById(R.id.particfriends);
		buttonAskForInvitees.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.onAskForInvitees();
			}
		});
		
		Button buttonCreateEvent = (Button) mView.findViewById(R.id.createeventbutton);
		buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createEvent();
			}
		});
		
		buttonStartTime = (Button) mView.findViewById(R.id.editTextStartTime);
		buttonStartDate = (Button) mView.findViewById(R.id.editTextStartDate);
		
		buttonEndTime = (Button) mView.findViewById(R.id.editTextEndTime);
		buttonEndDate = (Button) mView.findViewById(R.id.editTextEndDate);
		
		mTimeFormat = SimpleDateFormat.getTimeInstance();
		mDateFormat = SimpleDateFormat.getDateInstance();
		
		updateDateTimeDisplay();
		
		buttonStartTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showTimePicker(STARTTIMEID, mStartTime);
			}
		});
		
		buttonEndTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showTimePicker(ENDTIMEID, mEndTime);
			}
		});
		
		buttonStartDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePicker(STARTTIMEID, mStartTime);
			}
		});
		
		buttonEndDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePicker(ENDTIMEID, mEndTime);
			}
		});
	}
	
	private void showDatePicker(int id, Date time) {
		DatePickerFragment datePicker = DatePickerFragment.newInstance(id, time);
		datePicker.setListener(this);
		datePicker.show(this.getActivity().getFragmentManager(), "datePickerDialog");
		
	}
	
	private void showTimePicker(int id, Date time) {
		TimePickerFragment timePicker = TimePickerFragment.newInstance(STARTTIMEID, time);
		timePicker.setListener(this);
		timePicker.show(this.getActivity().getFragmentManager(), "timePickerDialog");
	}
	
	private void createEvent() {
		long categoryId;
		RadioGroup categoryGroup = (RadioGroup) mView.findViewById(R.id.radioGroupCategory);
		switch (categoryGroup.getCheckedRadioButtonId()) {
		case R.id.radioButtonEntertainment:
			categoryId = 3;
			break;
		case R.id.radioButtonWork:
			categoryId = 4;
			break;
		case R.id.radioButtonSports:
			categoryId = 1;
			break;
		case R.id.radioButtonFood:
			categoryId = 2;
			break;
		case R.id.radioButtonMisc:
			categoryId = 5;
			break;
		default:
			categoryId = 5;
			break;
		}
		
		String description = ((EditText) mView.findViewById(R.id.editTextEventDescription)).getText().toString();
		String locationString = ((EditText) mView.findViewById(R.id.editTextLocationDescription)).getText().toString();
		
		long creatorId = ((MeetsterApplication) this.getActivity().getApplicationContext()).getCurrentUserId();
		
		MeetsterEvent event = new MeetsterEvent((Context) this.getActivity(), creatorId, locationString, categoryId, description, mStartTime, mEndTime, mInviteeIds);
		
		this.getActivity().getContentResolver().insert(MeetsterContract.Events.getUri(), event.toValues());
		
		mCallback.onEventCreated();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	public interface CreateEventFragmentListener {
		public void onAskForInvitees();
		public void onEventCreated();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		mCallback = (CreateEventFragmentListener) activity;
	}
	
	public void setInviteeIds(List<Long> inviteeIds) {
		this.mInviteeIds = inviteeIds;
	}

	
	private Date updateTime(Date d, int hourOfDay, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		
		return c.getTime();
	}
	
	private Date updateDate(Date d, int year, int monthOfYear, int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, monthOfYear);
		c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		
		return c.getTime();
	}
	
	private void updateDateTimeDisplay() {
		buttonStartTime.setText(mTimeFormat.format(mStartTime));
		buttonStartDate.setText(mDateFormat.format(mStartTime));
		
		buttonEndTime.setText(mTimeFormat.format(mEndTime));
		buttonEndDate.setText(mDateFormat.format(mEndTime));
	}
	
	@Override
	public void onTimePicked(int timePickerId, int hourOfDay, int minute) {
		switch (timePickerId) {
		case STARTTIMEID:
			mStartTime = updateTime(mStartTime, hourOfDay, minute);
			if (mStartTime.after(mEndTime)) {
				mEndTime = changeHour(mStartTime, 1);
			}
			break;
		case ENDTIMEID:
			mEndTime = updateTime(mEndTime, hourOfDay, minute);
			if (mEndTime.before(mStartTime)) {
				mStartTime = changeHour(mEndTime, -1);
			}
			break;
		}
		updateDateTimeDisplay();
	}

	@Override
	public void onDatePicked(int datePickerId, int year, int monthOfYear,
			int dayOfMonth) {
		switch (datePickerId) {
		case STARTTIMEID:
			mStartTime = updateDate(mStartTime, year, monthOfYear, dayOfMonth);
			if (mStartTime.after(mEndTime)) {
				mEndTime = changeHour(mStartTime, 1);				
			}
			break;
		case ENDTIMEID:
			mEndTime = updateDate(mEndTime, year, monthOfYear, dayOfMonth);
			if (mEndTime.before(mStartTime)) {
				mStartTime = changeHour(mEndTime, -1);
			}
			break;
		}
		updateDateTimeDisplay();
	}
	
}
