package com.david.pda;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.david.pda.adapter.CycleDetailsArrayAdapter;
import com.david.pda.sqlite.model.Alarm;
import com.david.pda.sqlite.model.CycleDetails;
import com.david.pda.sqlite.model.CycleDetailsForAlarm;
import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;
import com.david.pda.util.other.DateUtil;
import com.david.pda.weather.model.util.L;

public class SomeToolsAlarmOptionActivity extends Activity {
	public final static int FLAG_ADD = 1;
	public final static int FLAG_UPDATE = 2;
	private final String OPTION_UPDATE = "update";
	private final String OPTION_ADD = "add";
	private final String OPTION_DELETE = "delete";
	ImageButton backward;
	private Alarm alarm;
	private List<CycleDetailsForAlarm> details = new ArrayList<CycleDetailsForAlarm>();
	private ListView detailsListView;
	EditText titleEditText;
	EditText contentEditText;
	CheckBox checkBox;
	Button yesButton;
	Spinner cycleTypes;
	Button cancleButton;
	Button addforList;
	RelativeLayout topBar;
	CycleDetailsArrayAdapter mAdapter = null;
	private PopupWindow popupWindow;
	private CycleDetails currentDetail;
	private List<CycleType> cycleTypeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_some_tools_alarm_option);
		Intent intent = super.getIntent();
		backward = (ImageButton) findViewById(R.id.main_some_tools_alarm_option_topbar_backward);
		topBar = (RelativeLayout) findViewById(R.id.main_some_tools_alarm_option_topbar);
		Bind.bindReturn(backward, SomeToolsAlarmOptionActivity.this,
				MainActivity.class, MainActivity.POSTION_SOME_TOOLS);
		titleEditText = (EditText) findViewById(R.id.main_some_tools_alarm_option_title);
		addforList = (Button) findViewById(R.id.main_some_tools_alarm_option_addforlist);
		contentEditText = (EditText) findViewById(R.id.main_some_tools_alarm_option_content);
		checkBox = (CheckBox) findViewById(R.id.main_some_tools_alarm_option_flag_checkbox);
		yesButton = (Button) findViewById(R.id.main_some_tools_alarm_option_yes_button);
		cancleButton = (Button) findViewById(R.id.main_some_tools_alarm_option_cancle_button);
		detailsListView = (ListView) findViewById(R.id.main_some_tools_alarm_option_details_list);
		cycleTypes = (Spinner) findViewById(R.id.main_some_tools_alarm_option_cycle_type_spinner);
		addforList.setOnClickListener(new AddListItemListener());
		cancleButton.setOnClickListener(new OnCancelListener());
		detailsListView.setAdapter(mAdapter);
		Log.i(L.t, "intent" + intent);
		Intent i = intent;
		if (FLAG_UPDATE == intent.getFlags()) {// update
			alarm = (Alarm) intent.getSerializableExtra("alarm");
			Log.i(L.t, alarm + "");
			getDetailsByAlarm();
			mAdapter = new CycleDetailsArrayAdapter(this,
					R.id.list_item_cycle_details_resource, details);
			yesButton.setOnClickListener(new UpdateListenr());
		} else if (FLAG_ADD == intent.getFlags()) {// add
			yesButton.setOnClickListener(new AddListenr());
			mAdapter = new CycleDetailsArrayAdapter(this,
					R.id.list_item_cycle_details_resource, details);
		}
		cycleTypes
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_dropdown_item,
						getCycleTypes()));
		showModelToView();
	}

	private int selectionOfCycleTyp = 0;

	private String[] getCycleTypes() {
		if (cycleTypeList == null) {
			DemoDB<CycleType> db = new DemoDB<CycleType>(new CycleType());
			cycleTypeList = db.getList(this);
		}
		String[] s = new String[cycleTypeList.size()];
		int i = 0;
		for (CycleType c : cycleTypeList) {
			s[i++] = c.getDescription();
			if (alarm != null && alarm.get_id() != null) {
				if (c.get_id().intValue() == alarm.getCycleType().intValue()) {
					selectionOfCycleTyp = i-1;
				}
			}
		}
		return s;
	}

	// 为List添加item
	class AddListItemListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Log.i(L.t, "为List添加item");
			showWindowForAdd();
		}
	}

	// update Alarm的时候，获取之前的Details数据
	void getDetailsByAlarm() {
		// detailsListView.setAdapter();
		details = new DemoDB<CycleDetailsForAlarm>(new CycleDetailsForAlarm())
				.getList(SomeToolsAlarmOptionActivity.this, " "
						+ CycleDetailsForAlarm.CYLEFOR + "=?",
						new String[] { alarm.get_id() + "" }, null);
	}

	// 点击更新按钮。更新除了List外的数据
	class UpdateListenr implements OnClickListener {

		@Override
		public void onClick(View arg0) {

		}

	}

	// 点击添加按钮，新建，这时候需要将list数据同步更新到数据库
	class AddListenr implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			Log.i(L.t, "add Alarm");
			getAlarmFromView();
			DemoDB<Alarm> db = new DemoDB<Alarm>(new Alarm());
			Uri uri = db.insert(alarm, SomeToolsAlarmOptionActivity.this);
			Long id = Long.valueOf(uri.getLastPathSegment());
			Log.i(L.t, "" + id);
			DemoDB<CycleDetailsForAlarm> db2 = new DemoDB<CycleDetailsForAlarm>(
					new CycleDetailsForAlarm());
			for (CycleDetailsForAlarm c : details) {
				c.setCycleFor(id);
				db2.insert(c, SomeToolsAlarmOptionActivity.this);
			}
			Toast.makeText(SomeToolsAlarmOptionActivity.this, "添加成功!",
					Toast.LENGTH_SHORT).show();
			Intent i = new Intent(SomeToolsAlarmOptionActivity.this,
					SomeToolsAlarmActivity.class);
			SomeToolsAlarmOptionActivity.this.startActivity(i);
			SomeToolsAlarmOptionActivity.this.finish();
		}

	}

	public void getAlarmFromView() {
		if (alarm == null) {
			alarm = new Alarm();
		}
		alarm.setTitle(titleEditText.getText().toString());
		alarm.setRemarks(content.getText().toString());
		alarm.setIsOn(checkBox.isChecked() ? Model.IS_ON : Model.IS_OFF);
		CycleType c = cycleTypeList.get(cycleTypes.getSelectedItemPosition());
		alarm.setCycleType(c.get_id());
	}

	// 更新的时候，将数据显示到视图上
	public void showModelToView() {
		this.checkBox.setChecked(alarm.getIsOn() == Model.IS_ON);
		this.contentEditText.setText(alarm.getRemarks());
		this.titleEditText.setText(alarm.getTitle());

		this.cycleTypes.setSelection(selectionOfCycleTyp);
		if (details == null) {
			details = new ArrayList<CycleDetailsForAlarm>();
		}
		if (details.size() == 0) {
			CycleDetailsForAlarm c = null;
			for (int i = 0; i < 10; i++) {
				c = CycleDetailsForAlarm.getTest(i);
				details.add(c);
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	private void showWindowForUpdate() {
		if (alarm == null || currentDetail == null) {
			return;
		}
		initWindow();
		initUpdateLayoutContent();
		showWindow();
	}

	private void showWindowForAdd() {
		initWindow();
		initAddLayoutContent();
		showWindow();
	}

	// 取消
	class OnCancelListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(SomeToolsAlarmOptionActivity.this,
					SomeToolsAlarmActivity.class);
			SomeToolsAlarmOptionActivity.this.startActivity(i);
			SomeToolsAlarmOptionActivity.this.finish();
		}

	}

	// add layout

	private void initAddLayoutContent() {
		popupButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideWindow();
			}
		});
		popupButtonConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {// add
				Log.i(L.t, "add");
				getCycleDetailFromPopup();
				SomeToolsAlarmOptionActivity.this.mAdapter.add(currentDetail);
				SomeToolsAlarmOptionActivity.this.mAdapter
						.notifyDataSetChanged();
				SomeToolsAlarmOptionActivity.this.hideWindow();
			}
		});
	}

	// update layout

	private void initUpdateLayoutContent() {
		popupButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideWindow();
			}
		});
		popupButtonConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {// update
				Log.i(L.t, "update");
			}
		});
	}

	View popupView;

	private void initWindow() {
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			popupView = layoutInflater.inflate(R.layout.popup_cycle_details,
					null);
			// 创建一个PopuWidow对象
			Point size = new Point();
			getWindowManager().getDefaultDisplay().getSize(size);
			popupWindow = new PopupWindow(popupView, size.x - 20, 450);
			initLayout();
		}
	}

	EditText sday;
	EditText eday;
	EditText shour;
	EditText ehour;
	EditText sminute;
	EditText eminute;
	CheckBox isahead;
	CheckBox istip;
	EditText content;
	EditText aheadtime;
	EditText weatherSensitive;
	Button popupButtonCancel;
	Button popupButtonConfirm;

	private void initLayout() {
		sday = (EditText) popupView
				.findViewById(R.id.popup_cycle_details_starttime_day);
		eday = (EditText) popupView
				.findViewById(R.id.popup_cycle_details_endtime_day);
		shour = (EditText) popupView
				.findViewById(R.id.popup_cycle_details_starttime_hour);
		ehour = (EditText) popupView
				.findViewById(R.id.popup_cycle_details_endtime_hour);
		sminute = (EditText) popupView
				.findViewById(R.id.popup_cycle_details_starttime_minute);
		eminute = (EditText) popupView
				.findViewById(R.id.popup_cycle_details_endtime_minute);
		isahead = (CheckBox) popupView
				.findViewById(R.id.popup_cycle_details_isaheadtime);
		istip = (CheckBox) popupView
				.findViewById(R.id.popup_cycle_details_istip);
		content = (EditText) popupView
				.findViewById(R.id.popup_cycle_details_content);
		aheadtime = (EditText) popupView
				.findViewById(R.id.popup_cycle_details_ahead_time);
		weatherSensitive = (EditText) popupView
				.findViewById(R.id.popup_cycle_details_weather_sensitive);
		popupButtonCancel = (Button) popupView
				.findViewById(R.id.popup_cycle_deails_button_cancel);
		popupButtonConfirm = (Button) popupView
				.findViewById(R.id.popup_cycle_deails_button_confirm);
	}

	private void showWindow() {
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int xPos = size.x / 2 - popupWindow.getWidth() / 2;
		popupWindow.showAsDropDown(topBar, xPos, 20);
	}

	private void hideWindow() {
		if (popupWindow != null) {
			popupWindow.dismiss();
		}
	}

	private void getCycleDetailFromPopup() {
		TextView sday = (TextView) popupView
				.findViewById(R.id.popup_cycle_details_starttime_day);
		TextView eday = (TextView) popupView
				.findViewById(R.id.popup_cycle_details_endtime_day);
		TextView shour = (TextView) popupView
				.findViewById(R.id.popup_cycle_details_starttime_hour);
		TextView ehour = (TextView) popupView
				.findViewById(R.id.popup_cycle_details_endtime_hour);
		TextView sminute = (TextView) popupView
				.findViewById(R.id.popup_cycle_details_starttime_minute);
		TextView eminute = (TextView) popupView
				.findViewById(R.id.popup_cycle_details_endtime_minute);

		CheckBox isahead = (CheckBox) popupView
				.findViewById(R.id.popup_cycle_details_isaheadtime);
		CheckBox istip = (CheckBox) popupView
				.findViewById(R.id.popup_cycle_details_istip);
		TextView content = (TextView) popupView
				.findViewById(R.id.popup_cycle_details_content);
		TextView aheadtime = (TextView) popupView
				.findViewById(R.id.popup_cycle_details_ahead_time);
		TextView weatherSensitive = (TextView) popupView
				.findViewById(R.id.popup_cycle_details_weather_sensitive);

		CycleDetails detail = new CycleDetailsForAlarm();
		int ssday = (int) getNumber(sday.getText().toString());
		int esday = (int) getNumber(eday.getText().toString());
		int sshour = (int) getNumber(shour.getText().toString());
		int eshour = (int) getNumber(ehour.getText().toString());

		int ssminute = (int) getNumber(sminute.getText().toString());
		int esminute = (int) getNumber(eminute.getText().toString());

		detail.set_id(null);
		detail.setStartTime(DateUtil.getLongByDHM(ssday, sshour, ssminute));
		detail.setEndTime(DateUtil.getLongByDHM(esday, eshour, esminute));
		detail.setIsAhead(isahead.isChecked() ? Model.IS_YES : Model.IS_NO);
		detail.setIsTip(istip.isChecked() ? Model.IS_YES : Model.IS_NO);
		detail.setDiscription(content.getText().toString());
		detail.setAheadTime(getNumber(aheadtime.getText().toString()));
		detail.setWeatherSensitivity(weatherSensitive.getText().toString());
		this.currentDetail = detail;
	}

	private long getNumber(String s) {
		if (s != null && !s.equals("") && TextUtils.isDigitsOnly(s)) {
			return Long.valueOf("0" + s);
		} else
			return 0;
	}
}
