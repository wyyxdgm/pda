package com.david.pda;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.david.pda.sqlite.model.Plan;
import com.david.pda.sqlite.model.Target;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;

public class AffairPlanOptionActivity extends Activity {
	private Plan plan = new Plan();
	ImageButton backward;
	public final static int FLAG_ADD = 1;
	public final static int FLAG_UPDATE = 2;
	private int flagOption = FLAG_ADD;
	Spinner targetListSP;
	EditText startDP;
	EditText startTP;
	EditText endDP;
	EditText endTP;
	CheckBox jjCB;
	CheckBox zyCB;
	EditText contentET;
	EditText afterSuccessET;
	Button cancleBT;
	Button confirmBT;
	private List<Target> targets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_affair_plan_option);
		backward = (ImageButton) findViewById(R.id.main_affair_plan_option_topbar_backward);
		Bind.bindReturn(backward, AffairPlanOptionActivity.this,
				MainActivity.class, MainActivity.POSTION_AFFAIR_PLAN);
		targetListSP = (Spinner) findViewById(R.id.main_affair_plan_option_target_list);
		startDP = (EditText) findViewById(R.id.main_affair_plan_option_target_start_date_et);
		startTP = (EditText) findViewById(R.id.main_affair_plan_option_target_start_time_et);
		endDP = (EditText) findViewById(R.id.main_affair_plan_option_target_end_date_et);
		endTP = (EditText) findViewById(R.id.main_affair_plan_option_target_end_time_et);
		jjCB = (CheckBox) findViewById(R.id.main_affair_plan_option_target_jj);
		zyCB = (CheckBox) findViewById(R.id.main_affair_plan_option_target_zy);
		contentET = (EditText) findViewById(R.id.main_affair_plan_option_content);
		afterSuccessET = (EditText) findViewById(R.id.main_affair_plan_option_after_success);
		cancleBT = (Button) findViewById(R.id.main_affair_plan_option_cancle_button);
		confirmBT = (Button) findViewById(R.id.main_affair_plan_option_yes_button);
		final Calendar c = Calendar.getInstance();
		targetListSP
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_dropdown_item,
						getTargetList()));
		startDP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new DatePickerDialog(AffairPlanOptionActivity.this,
						new OnMyDateSetListener(startDP), c.get(Calendar.YEAR),
						c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
						.show();
			}
		});
		startTP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new TimePickerDialog(AffairPlanOptionActivity.this,
						new OnMyTimeSetListener(startTP), c
								.get(Calendar.HOUR_OF_DAY), c
								.get(Calendar.MINUTE), true).show();
			}
		});
		endTP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new TimePickerDialog(AffairPlanOptionActivity.this,
						new OnMyTimeSetListener(endTP), c
								.get(Calendar.HOUR_OF_DAY), c
								.get(Calendar.MINUTE), true).show();
			}
		});
		endDP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new DatePickerDialog(AffairPlanOptionActivity.this,
						new OnMyDateSetListener(endDP), c.get(Calendar.YEAR), c
								.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
	}

	class OnMyDateSetListener implements OnDateSetListener {
		EditText et;

		public OnMyDateSetListener(EditText startDP) {
			this.et = startDP;
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String ymd = "" + year + "-" + monthOfYear + 1 + "-" + dayOfMonth;
			Toast.makeText(AffairPlanOptionActivity.this, ymd,
					Toast.LENGTH_SHORT).show();
			this.et.setText(ymd);
		}

	}

	class OnMyTimeSetListener implements OnTimeSetListener {
		EditText et;

		public OnMyTimeSetListener(EditText startDP) {
			this.et = startDP;
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			String hm = hourOfDay + ":" + minute;
			Toast.makeText(AffairPlanOptionActivity.this, hm,
					Toast.LENGTH_SHORT).show();
			this.et.setText(hm);
		}
	}

	private String[] getTargetList() {
		DemoDB<Target> db = new DemoDB<Target>(new Target());
		targets = db.getList(this);
		String[] names = new String[targets.size()];
		int i = 0;
		for (Target t : targets) {
			names[i] = t.getName();
			i++;
		}
		return names;
	}

	private void initViewWithModel() {

	}
}
