package com.russia.meetster;

import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
		OnTimeSetListener {
	
	private TimePickerListener mListener;
	
	public interface TimePickerListener {
		public void onTimePicked(int timePickerId, int hourOfDay, int minute);
	}
	
	static TimePickerFragment newInstance(int ID, Date date) {
		TimePickerFragment f = new TimePickerFragment();
		
		Bundle b = new Bundle();
		b.putSerializable("date", date);
		b.putInt("id", ID);
		
		f.setArguments(b);
		return f;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Date d = (Date) this.getArguments().getSerializable("date");
		
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}
	
	public void setListener(TimePickerListener listener) {
		mListener = listener;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		mListener.onTimePicked(this.getArguments().getInt("id"), hourOfDay, minute);
	}

}
