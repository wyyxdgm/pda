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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.david.pda.application.SysApplication;
import com.david.pda.sqlite.model.Countdown;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;
import com.david.pda.util.other.DateUtil;

public class SomeToolsCountdownOptionActivity extends Activity {
	public static final int FLAG_UPDATE = 2;
	public static final int FLAG_ADD = 3;
	public static final int UPDATE_SUCCESS = 4;
	public static final int ADD_SUCCESS = 5;
	private static final int OPTION_FAILED = -1;
	ImageButton backward;
	private Countdown countdown;
	private int flag;
	EditText titleEditText;
	EditText remarksEditText;
	ToggleButton flagToggleButton;
	Button yesButton;
	Button cancleButton;
	EditText endDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_some_tools_countdown_option);
		SysApplication.getInstance().addActivity(this);
		backward = (ImageButton) findViewById(R.id.main_some_tools_countdown_option_topbar_backward);
		Bind.bindReturn(backward, SomeToolsCountdownOptionActivity.this,
				MainActivity.class, MainActivity.POSTION_SOME_TOOLS);
		titleEditText = (EditText) findViewById(R.id.main_some_tools_countdown_option_title);
		remarksEditText = (EditText) findViewById(R.id.main_some_tools_countdown_option_remarks);
		flagToggleButton = (ToggleButton) findViewById(R.id.main_some_tools_countdown_option_isOn);
		yesButton = (Button) findViewById(R.id.main_some_tools_countdown_option_yes_button);
		cancleButton = (Button) findViewById(R.id.main_some_tools_countdown_option_cancle_button);
		endDate = (EditText) findViewById(R.id.main_some_tools_countdown_option_endTime);
		endDate.setOnClickListener(new GetDateListener());
		resolveIntent();
		Bind.bindReturn(cancleButton, SomeToolsCountdownOptionActivity.this,
				SomeToolsCountdownActivity.class);
	}

	private void resolveIntent() {
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		if (b.containsKey("from")) {
			String from = intent.getStringExtra("from");
			if (DateTimePicker.class.getName().equals(from)) {
				Bundle oldB = b.getBundle(DateTimePicker.OLDBUNDLE);
				this.flag = oldB.getInt("flag");
				this.countdown = new Countdown(oldB.getBundle("countdown"));
				this.countdown.setEndTime(intent.getLongExtra(
						DateTimePicker.TIME, System.currentTimeMillis()));
				showCountdownToView();
				if (FLAG_UPDATE == flag) {// update
					yesButton.setOnClickListener(new UpdateListenr());
				} else if (FLAG_ADD == flag) {// add
					yesButton.setOnClickListener(new AddListenr());
				}
			} else if (SomeToolsCountdownActivity.class.getName().equals(from)) {// from
				flag = intent.getFlags();
				if (FLAG_UPDATE == flag) {// update
					countdown = new Countdown(intent.getExtras());
					showCountdownToView();
					yesButton.setOnClickListener(new UpdateListenr());
				} else if (FLAG_ADD == flag) {// add
					countdown = new Countdown();
					yesButton.setOnClickListener(new AddListenr());
				}
			}
		}
	}

	public void showCountdownToView() {
		if (countdown != null) {
			titleEditText.setText(countdown.getTitle());
			remarksEditText.setText(countdown.getRemarks());
			boolean checked = countdown.getIsOn() != null
					&& countdown.getIsOn() == Model.IS_YES;
			flagToggleButton.setChecked(checked);
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(countdown.getEndTime() != null ? countdown
					.getEndTime() : System.currentTimeMillis());
			endDate.setText(DateUtil.formatyyyy_MM_dd_HH_mm(countdown
					.getEndTime()));
		} else {
			this.countdown = new Countdown();
		}
	}

	public void FillCountdownWidthView() {
		if (countdown == null) {
			countdown = new Countdown();
		}
		countdown.setRemarks(remarksEditText.getText().toString());
		countdown.setTitle(titleEditText.getText().toString());
		countdown.setIsOn(flagToggleButton.isChecked() ? Model.IS_YES
				: Model.IS_NO);
		countdown.setDelFlag(Model.FLAG_EXISTS);
		if (!endDate.getText().toString().equals(""))
			countdown.setEndTime(DateUtil.parsePT(DateUtil.yyyy_MM_dd_HH_mm,
					endDate.getText().toString()));
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

	class GetDateListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Intent i = DateTimePicker.buildIntentContainsBundle(
					SomeToolsCountdownOptionActivity.this, true,
					DateUtil.yyyy_MM_dd_HH_mm);
			Bundle b = new Bundle();
			FillCountdownWidthView();
			b.putBundle("countdown",
					SomeToolsCountdownOptionActivity.this.countdown.toBundle());// 等待传回来
			b.putInt("flag", flag);
			i.putExtra("oldBundle", b);
			// oldBundle-countdown
			// oldBundle-flags
			SomeToolsCountdownOptionActivity.this.startActivity(i);
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
		SomeToolsCountdownOptionActivity.this.startActivity(i);
		SomeToolsCountdownOptionActivity.this.finish();
	}

}
