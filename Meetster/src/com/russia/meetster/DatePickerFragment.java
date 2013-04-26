package com.russia.meetster;

import java.util.Calendar;
import java.util.Date;

import com.russia.meetster.TimePickerFragment.TimePickerListener;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener {

	private DatePickerListener mListener;

	public interface DatePickerListener {
		public void onDatePicked(int datePickerId, int year, int monthOfYear, int dayOfMonth);
	}

	static DatePickerFragment newInstance(int ID, Date date) {
		DatePickerFragment f = new DatePickerFragment();

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

		int year = c.get(Calendar.YEAR);
		int monthOfYear = c.get(Calendar.MONTH);
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(this.getActivity(), this, year, monthOfYear, dayOfMonth);
	}

	public void setListener(DatePickerListener listener) {
		mListener = listener;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mListener.onDatePicked(this.getArguments().getInt("id"), year, monthOfYear, dayOfMonth);
	}

}
