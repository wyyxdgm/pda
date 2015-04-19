package com.david.pda;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.david.pda.sqlite.model.Countdown;
import com.david.pda.util.other.DateUtil;

public class DateTimePicker extends Activity {
	public static final String TIME = "time";
	public static final String FORMATE = "format";
	public static final String FROM = "from";
	public static final String OLDBUNDLE = "oldBundle";
	private EditText dateTimeEt = null;
	private Button confirmBtn;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private Bundle oldBundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_layout);
		dateTimeEt = (EditText) findViewById(R.id.dateTimeEt);
		confirmBtn = (Button) findViewById(R.id.date_time_confirm_btn);
		DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
		datePicker.setMinimumWidth(40);
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		datePicker.init(year, month, day, new OnDateChangedListener() {
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				DateTimePicker.this.year = year;
				DateTimePicker.this.month = monthOfYear;
				DateTimePicker.this.day = dayOfMonth;
				dateTimeEt.setText(getStr());
			}
		});
		oldBundle = getIntent().getExtras().getBundle("oldBundle");
		Countdown c = new Countdown(oldBundle.getBundle("countdown"));

		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				DateTimePicker.this.hour = hourOfDay;
				DateTimePicker.this.minute = minute;
				dateTimeEt.setText(getStr());
			}
		});
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				goBackWithResult();
			}
		});
	}

	public String getStr() {
		return year + "年" + (month + 1) + "月" + day + "日 " + hour + "时"
				+ minute + "分";
	}

	public static Intent buildIntentContainsBundle(Context context,
			boolean time, String format) {
		Intent i = new Intent(context, DateTimePicker.class);
		Bundle b = new Bundle();
		if (time)
			b.putString(TIME, "true");
		if (format != null && !format.equals(""))
			b.putString(FORMATE, format);
		b.putSerializable(FROM, context.getClass());
		i.putExtras(b);
		return i;
	}

	public void goBackWithResult() {
		Intent i = getIntent();
		Bundle b = i.getExtras();
		Class<?> target = null;
		if (b.containsKey(FROM)) {
			target = (Class<?>) b.getSerializable("from");
		} else {
			return;
		}
		Intent intent = new Intent(DateTimePicker.this, target);
		Bundle newB = new Bundle();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		if (b.containsKey("format")) {
			newB.putString(FORMATE,
					DateUtil.format(b.getString(FORMATE), c.getTimeInMillis()));
		}
		if (b.containsKey(TIME)) {
			newB.putLong(TIME, c.getTimeInMillis());
		}
		newB.putSerializable(FROM, DateTimePicker.class.getName());
		newB.putBundle(OLDBUNDLE, oldBundle);
		intent.putExtras(newB);
		DateTimePicker.this.startActivity(intent);
		DateTimePicker.this.finish();
	}
}
