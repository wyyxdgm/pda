package com.david.pda;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageButton;

import com.david.pda.util.other.Bind;

public class SomeToolsCalendarActivity extends Activity {
	ImageButton backward;
	CalendarView calendarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_some_tools_calendar);
		backward = (ImageButton) findViewById(R.id.main_some_tools_calendar_topbar_backward);
		calendarView = (CalendarView) findViewById(R.id.main_some_tools_calendar_calendarView);
		Bind.bindReturn(backward, SomeToolsCalendarActivity.this,
				MainActivity.class, MainActivity.POSTION_SOME_TOOLS);
	}
}
