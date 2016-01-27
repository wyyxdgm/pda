package com.david.pda;

import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.david.pda.adapter.AlarmListAdapter;
import com.david.pda.application.SysApplication;
import com.david.pda.sqlite.model.Alarm;
import com.david.pda.sqlite.model.CycleDetailsForAlarm;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;
import com.david.pda.weather.model.util.L;

public class SomeToolsAlarmActivity extends Activity {
	Button addButton;
	ListView listView;
	ImageButton backward;
	List<Alarm> alarms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.main_some_tools_alarm);
		SysApplication.getInstance().addActivity(this);
		addButton = (Button) findViewById(R.id.addAlarm);
		listView = (ListView) findViewById(R.id.main_some_tools_alarm_listview);
		backward = (ImageButton) findViewById(R.id.main_some_tools_alarm_topbar_backward);
		Bind.bindReturn(backward, this, MainActivity.class,
				MainActivity.POSTION_SOME_TOOLS);
		initView();
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SomeToolsAlarmActivity.this,
						SomeToolsAlarmOptionActivity.class);
				intent.setFlags(SomeToolsAlarmOptionActivity.FLAG_ADD);
				startActivity(intent);
				SomeToolsAlarmActivity.this.finish();
			}
		});
	}

	private void initView() {
		getAlarms();
		listView.setAdapter(new AlarmListAdapter(this, this.alarms));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(SomeToolsAlarmActivity.this,
						SomeToolsAlarmOptionActivity.class);
				Alarm a = alarms.get(arg2);
				i.putExtra("alarm", a);
				i.setFlags(SomeToolsAlarmOptionActivity.FLAG_UPDATE);
				SomeToolsAlarmActivity.this.startActivity(i);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int index, long arg3) {
				AlertDialog.Builder builder = new Builder(
						SomeToolsAlarmActivity.this);
				builder.setMessage("确认删除吗？");
				builder.setTitle("提示");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								DemoDB<Alarm> db = new DemoDB<Alarm>(
										new Alarm());
								try {
									DemoDB<CycleDetailsForAlarm> db2 = new DemoDB<CycleDetailsForAlarm>(
											new CycleDetailsForAlarm());
									List<CycleDetailsForAlarm> details = db2
											.getList(
													SomeToolsAlarmActivity.this,
													CycleDetailsForAlarm.CYLEFOR
															+ "=?",
													new String[] { alarms.get(
															index).get_id()
															+ "" }, null);
									for (CycleDetailsForAlarm d : details) {
										db2.realRemove(d.get_id() + "",
												SomeToolsAlarmActivity.this);
									}
									db.realRemove(alarms.get(index).get_id()
											+ "", SomeToolsAlarmActivity.this);
									Toast.makeText(
											SomeToolsAlarmActivity.this,
											"删除《"
													+ alarms.get(index)
															.getTitle()
													+ "》成功！",
											Toast.LENGTH_SHORT).show();
									// alarms.remove(index);
									initView();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
				return true;
			}
		});
	}

	private void getAlarms() {
		this.alarms = new DemoDB<Alarm>(new Alarm()).getList(this);
		Log.i(L.t, "alarmSize:" + alarms.size());
	}

}
