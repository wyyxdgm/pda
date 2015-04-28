package com.david.pda;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.david.pda.sqlite.model.CycleDetailsForPlan;
import com.david.pda.sqlite.model.Plan;
import com.david.pda.sqlite.model.Target;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;
import com.david.pda.util.other.DateUtil;

public class AffairPlanOptionActivity extends Activity {
	private Plan plan = new Plan();
	ImageButton backward;
	public final static int FLAG_ADD = 1;
	public final static int FLAG_UPDATE = 2;
	Spinner targetListSP;
	EditText startDP;
	EditText startTP;
	EditText endDP;
	EditText endTP;
	CheckBox jjCB;
	CheckBox zyCB;
	EditText afterSuccessET;
	EditText title;

	EditText contentET;
	EditText startDay;
	EditText startHour;
	EditText startMinute;
	EditText endDay;
	EditText endHour;
	EditText endMinute;
	CheckBox isTip;
	CheckBox isAhead;
	EditText weatherSensitive;
	EditText aheadTime;

	Button cancleBT;
	Button confirmBT;
	private List<Target> targets;
	private int targetIndex;
	private CycleDetailsForPlan detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_affair_plan_option);
		backward = (ImageButton) findViewById(R.id.main_affair_plan_option_topbar_backward);
		Bind.bindReturn(backward, AffairPlanOptionActivity.this,
				MainActivity.class, MainActivity.POSTION_AFFAIR_PLAN);
		targetListSP = (Spinner) findViewById(R.id.main_affair_plan_option_target_list);
		title = (EditText) findViewById(R.id.main_affair_plan_option_title);
		startDP = (EditText) findViewById(R.id.main_affair_plan_option_target_start_date_et);
		startTP = (EditText) findViewById(R.id.main_affair_plan_option_target_start_time_et);
		endDP = (EditText) findViewById(R.id.main_affair_plan_option_target_end_date_et);
		endTP = (EditText) findViewById(R.id.main_affair_plan_option_target_end_time_et);
		jjCB = (CheckBox) findViewById(R.id.main_affair_plan_option_target_jj);
		zyCB = (CheckBox) findViewById(R.id.main_affair_plan_option_target_zy);
		afterSuccessET = (EditText) findViewById(R.id.main_affair_plan_option_after_success);
		contentET = (EditText) findViewById(R.id.main_affair_plan_option_content);
		startDay = (EditText) findViewById(R.id.main_affair_plan_option_starttime_day);
		startHour = (EditText) findViewById(R.id.main_affair_plan_option_starttime_hour);
		startMinute = (EditText) findViewById(R.id.main_affair_plan_option_starttime_minute);
		endDay = (EditText) findViewById(R.id.main_affair_plan_option_endtime_day);
		endHour = (EditText) findViewById(R.id.main_affair_plan_option_endtime_hour);
		endMinute = (EditText) findViewById(R.id.main_affair_plan_option_endtime_minute);
		isTip = (CheckBox) findViewById(R.id.main_affair_plan_option_istip);
		isAhead = (CheckBox) findViewById(R.id.main_affair_plan_option_isaheadtime);
		weatherSensitive = (EditText) findViewById(R.id.main_affair_plan_option_weather_sensitive);
		aheadTime = (EditText) findViewById(R.id.main_affair_plan_option_ahead_time);
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
		cancleBT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AffairPlanOptionActivity.this,
						MainActivity.class);
				AffairPlanOptionActivity.this.startActivity(intent);
				AffairPlanOptionActivity.this.finish();
			}
		});
		if (getIntent().getFlags() == FLAG_ADD) {// add option
			confirmBT.setOnClickListener(new AddPlanListener());
		} else if (getIntent().getFlags() == FLAG_UPDATE) {// update
			this.plan = (Plan) getIntent().getSerializableExtra("plan");
			initViewWithModelWhenUpdate();
			confirmBT.setOnClickListener(new UpdatePlanListener());
		}
	}

	public void getModelByViewWhenUpdate() {
		if (detail == null) {
			getDetailFromDB();
		}
		fillViewFromDetail();
		if (plan == null || plan.get_id() == null) {
			Toast.makeText(AffairPlanOptionActivity.this, "数据异常！",
					Toast.LENGTH_SHORT).show();
		} else {
			getPlanFromView();
		}
	}

	class UpdatePlanListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			getModelByViewWhenUpdate();
			if (detail.get_id() != null) {

			}
		}

	}

	class AddPlanListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			getModelFromViewWhenAdd();
			DemoDB<Plan> db = new DemoDB<Plan>(new Plan());
			Uri u = db.insert(plan, AffairPlanOptionActivity.this);
			String id = u.getLastPathSegment();
			DemoDB<CycleDetailsForPlan> db2 = new DemoDB<CycleDetailsForPlan>(
					new CycleDetailsForPlan());
			detail.setCycleFor(Long.valueOf(id));
			db2.insert(detail, AffairPlanOptionActivity.this);
			Toast.makeText(AffairPlanOptionActivity.this, "添加成功！",
					Toast.LENGTH_SHORT).show();
			goBack();
		}

	}

	public void goBack() {
		Intent intent = new Intent(AffairPlanOptionActivity.this,
				MainActivity.class);
		intent.setFlags(MainActivity.POSTION_AFFAIR_PLAN);
		startActivity(intent);
		AffairPlanOptionActivity.this.finish();
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
			if (plan != null && plan.getTarget() != null) {
				if (t.get_id().intValue() == plan.getTarget().intValue()) {
					targetIndex = i;
				}
			} else {
				targetIndex = 0;
			}
			i++;
		}
		return names;
	}

	private void initViewWithModelWhenUpdate() {
		if (plan != null) {
			fillCommonAttrViewFromPlan();
			if (detail == null) {
				getDetailFromDB();
				fillViewFromDetail();
			}
		}

	}

	public void getDetailFromDB() {
		DemoDB<CycleDetailsForPlan> db = new DemoDB<CycleDetailsForPlan>(
				new CycleDetailsForPlan());
		List<CycleDetailsForPlan> details = db.getList(this,
				CycleDetailsForPlan.CYLEFOR + "=?",
				new String[] { "" + plan.get_id() }, null);
		if (details.size() > 0) {
			detail = details.get(0);
		} else {
			detail = new CycleDetailsForPlan();
		}
	}

	public void fillViewFromDetail() {
		int s[] = DateUtil.getDHM(detail.getStartTime());
		int e[] = DateUtil.getDHM(detail.getEndTime());
		startDay.setText(s[0] + "");
		startHour.setText(s[1] + "");
		startMinute.setText(s[2] + "");
		endDay.setText(e[0] + "");
		endHour.setText(e[1] + "");
		endMinute.setText(e[2] + "");
		isAhead.setChecked(detail.getIsAhead() != null
				&& detail.getIsAhead() == Model.IS_YES);
		isTip.setChecked(detail.getIsTip() != null
				&& detail.getIsTip() == Model.IS_YES);
		contentET.setText(detail.getDiscription() == null ? "" : detail
				.getDiscription());
		aheadTime.setText(detail.getAheadTime() != null ? detail.getAheadTime()
				+ "" : "");
		weatherSensitive
				.setText(detail.getWeatherSensitivity() != null ? detail
						.getWeatherSensitivity() : "");
	}

	private void getModelFromViewWhenAdd() {
		if (this.detail == null) {
			detail = new CycleDetailsForPlan();
		}
		getDetailFromView();
		if (this.plan == null) {
			this.plan = new Plan();
		}
		getPlanFromView();
	}

	private void getDetailFromView() {
		int ssday = (int) getNumber(startDay.getText().toString());
		int esday = (int) getNumber(endDay.getText().toString());
		int sshour = (int) getNumber(startHour.getText().toString());
		int eshour = (int) getNumber(endHour.getText().toString());

		int ssminute = (int) getNumber(startMinute.getText().toString());
		int esminute = (int) getNumber(endMinute.getText().toString());

		detail.setStartTime(DateUtil.getLongByDHM(ssday, sshour, ssminute));
		detail.setEndTime(DateUtil.getLongByDHM(esday, eshour, esminute));
		detail.setIsAhead(isAhead.isChecked() ? Model.IS_YES : Model.IS_NO);
		detail.setIsTip(isTip.isChecked() ? Model.IS_YES : Model.IS_NO);
		detail.setDiscription(contentET.getText().toString());
		detail.setAheadTime(getNumber(aheadTime.getText().toString()));
		detail.setWeatherSensitivity(weatherSensitive.getText().toString());
		this.detail = (CycleDetailsForPlan) detail;
	}

	private void fillCommonAttrViewFromPlan() {
		targetListSP.setSelection(targetIndex);
		title.setText(plan.getTitle());
		startDP.setText(DateUtil.format(DateUtil.yyyy_MM_dd,
				plan.getStartTime()));
		startTP.setText(DateUtil.format(DateUtil.HH_mm, plan.getStartTime()));
		endDP.setText(DateUtil.format(DateUtil.yyyy_MM_dd, plan.getEndTime()));
		endTP.setText(DateUtil.format(DateUtil.HH_mm, plan.getEndTime()));
		jjCB.setText(Plan.urgency(plan.getUrgencyimportant()));
		zyCB.setText(Plan.important(plan.getUrgencyimportant()));
		afterSuccessET.setText(plan.getDoAfterSuccess());
	}

	public void getPlanFromView() {
		// if (TextUtils.isEmpty(endTP.getText().toString())
		// || TextUtils.isEmpty(endDP.getText().toString())
		// || TextUtils.isEmpty(startTP.getText().toString())
		// || TextUtils.isEmpty(startDP.getText().toString())) {
		// Toast.makeText(AffairPlanOptionActivity.this, "日期不能为空！",
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		targetIndex = targetListSP.getSelectedItemPosition();
		plan.setStartTime(DateUtil.parse(DateUtil.yyyy_MM_dd_HH_mm, startDP
				.getText().toString() + " " + startTP.getText().toString()));
		plan.setEndTime(DateUtil.parse(DateUtil.yyyy_MM_dd_HH_mm, endDP
				.getText().toString() + " " + endTP.getText().toString()));
		plan.setUrgencyimportant((jjCB.isChecked() ? 1 * 2 : 0 * 2)
				+ (zyCB.isChecked() ? 1 * 1 : 0 * 1));
		plan.setDoAfterSuccess(afterSuccessET.getText().toString());
		plan.setCreateTime(System.currentTimeMillis());
		plan.setDelFlag(Model.FLAG_EXISTS);
		plan.setTitle(title.getText().toString());
	}

	private long getNumber(String s) {
		if (s != null && !s.equals("") && TextUtils.isDigitsOnly(s)) {
			return Long.valueOf("" + s);
		} else
			return 0;
	}
}
