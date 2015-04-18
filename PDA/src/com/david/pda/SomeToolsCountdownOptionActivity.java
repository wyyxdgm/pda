package com.david.pda;

import java.util.Calendar;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.david.pda.sqlite.model.Countdown;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;

public class SomeToolsCountdownOptionActivity extends Activity {
	public static final int FLAG_UPDATE = 2;
	public static final int FLAG_ADD = 3;
	public static final int UPDATE_SUCCESS = 4;
	public static final int ADD_SUCCESS = 5;
	private static final int OPTION_FAILED = -1;
	ImageButton backward;
	private Countdown countdown;
	EditText titleEditText;
	EditText remarksEditText;
	ToggleButton flagToggleButton;
	Button yesButton;
	Button cancleButton;
	DatePicker endDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_some_tools_countdown_option);
		backward = (ImageButton) findViewById(R.id.main_some_tools_countdown_option_topbar_backward);
		Bind.bindReturn(backward, SomeToolsCountdownOptionActivity.this,
				MainActivity.class, MainActivity.POSTION_SOME_TOOLS);
		titleEditText = (EditText) findViewById(R.id.main_some_tools_countdown_option_title);
		remarksEditText = (EditText) findViewById(R.id.main_some_tools_countdown_option_remarks);
		flagToggleButton = (ToggleButton) findViewById(R.id.main_some_tools_countdown_option_isOn);
		yesButton = (Button) findViewById(R.id.main_some_tools_countdown_option_yes_button);
		cancleButton = (Button) findViewById(R.id.main_some_tools_countdown_option_cancle_button);
		endDate = (DatePicker) findViewById(R.id.main_some_tools_countdown_option_endTime);
		Intent intent = getIntent();
		if (FLAG_UPDATE == intent.getFlags()) {// update
			countdown = new Countdown(intent.getExtras());
			showCountdownToView();
			yesButton.setOnClickListener(new UpdateListenr());
		} else if (FLAG_ADD == intent.getFlags()) {// add
			showCountdownToView();
			yesButton.setOnClickListener(new AddListenr());
		}
		Bind.bindReturn(cancleButton, SomeToolsCountdownOptionActivity.this,
				SomeToolsCountdownActivity.class);
	}

	public void showCountdownToView() {
		if (countdown != null) {
			titleEditText.setText(countdown.getTitle());
			remarksEditText.setText(countdown.getRemarks());
			boolean checked = countdown.getIsOn() != null
					&& countdown.getIsOn() == Model.IS_NO;
			flagToggleButton.setChecked(checked);
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(countdown.getEndTime() != null ? countdown
					.getEndTime() : System.currentTimeMillis());
			endDate.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH + 1),
					c.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							Toast.makeText(
									SomeToolsCountdownOptionActivity.this,
									year + ";" + monthOfYear + ";" + dayOfMonth,
									Toast.LENGTH_SHORT).show();
						}

					});

		}
	}

	public void FillCountdownWidthView() {
		if (countdown == null) {
			countdown = new Countdown();
		}
		countdown.setRemarks(remarksEditText.getText().toString());
		countdown.setTitle(titleEditText.getText().toString());
		countdown.setIsOn(flagToggleButton.isChecked() ? Model.IS_ON
				: Model.IS_OFF);
		countdown.setDelFlag(Model.FLAG_EXISTS);
		countdown.setEndTime(endDate.getDrawingTime());
	}

	class UpdateListenr implements OnClickListener {

		@SuppressLint("ShowToast")
		@Override
		public void onClick(View arg0) {
			FillCountdownWidthView();
			DemoDB<Countdown> db = new DemoDB<Countdown>(countdown);
			try {
				db.update(countdown, SomeToolsCountdownOptionActivity.this);
				goBack(UPDATE_SUCCESS);
			} catch (JSONException e) {
				e.printStackTrace();
				goBack(OPTION_FAILED);
			}
		}

	}

	class AddListenr implements OnClickListener {

		@SuppressLint("ShowToast")
		@Override
		public void onClick(View arg0) {
			FillCountdownWidthView();
			DemoDB<Countdown> db = new DemoDB<Countdown>(countdown);
			db.insert(countdown, SomeToolsCountdownOptionActivity.this);
			goBack(ADD_SUCCESS);
		}
	}

	private void goBack(int result) {
		Intent i = new Intent(SomeToolsCountdownOptionActivity.this,
				SomeToolsCountdownActivity.class);
		i.setFlags(result);
		i.putExtra("from", SomeToolsCountdownOptionActivity.class.getName());
		startActivity(i);
		SomeToolsCountdownOptionActivity.this.finish();
	}

}
