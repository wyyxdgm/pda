package com.david.pda;

import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.DateType;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;
import com.david.pda.weather.model.util.L;

public class CycleTypeOptionActivity extends Activity {
	ImageButton backward;
	Button cancelButton;
	Button yesButton;
	TextView name;
	TextView cycleLength;
	Spinner spinner;
	private Long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_system_setting_cycle_type_option);
		backward = (ImageButton) findViewById(R.id.main_system_setting_option_topbar_backward);
		cancelButton = (Button) findViewById(R.id.main_system_setting_option_cancle_button);
		yesButton = (Button) findViewById(R.id.main_system_setting_option_yes_button);
		name = (TextView) findViewById(R.id.main_system_setting_option_name);
		cycleLength = (TextView) findViewById(R.id.main_system_setting_option_cycle_length);
		spinner = (Spinner) findViewById(R.id.main_system_setting_option_spinner);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.i(L.t, "position:" + arg2);
				Log.i(L.t, "text:" + ((TextView) arg1).getText().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Log.i(L.t, "nothing");
			}
		});
		Bind.bindReturn(backward, CycleTypeOptionActivity.this,
				MainActivity.class, MainActivity.POSTION_SYSTEM_SETTIONG);
		Bind.bindReturn(cancelButton, CycleTypeOptionActivity.this,
				MainActivity.class, MainActivity.POSTION_SYSTEM_SETTIONG);
		Intent i = getIntent();
		if (i.hasExtra("opt")) {
			DemoDB<CycleType> db = new DemoDB<CycleType>(new CycleType());
			List<CycleType> list = db.getList(this);
			int position = i.getIntExtra("position", -1);
			if (position == -1)
				return;
			CycleType ct = list.get(position);
			id = ct.get_id();
			spinner.setSelection(ct.getDateType().getIndex(), false);
			name.setText(ct.getName());
			cycleLength.setText(ct.getCycleLength() + "");
			// update
			yesButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					DemoDB<CycleType> db = new DemoDB<CycleType>(
							new CycleType());
					CycleType ct = new CycleType();
					ct.setCycleLength(Long.valueOf(cycleLength.getText()
							.toString()));
					ct.setDateType(DateType.getType(((TextView) spinner
							.getSelectedView()).getText().toString()));
					ct.setName(name.getText().toString());
					ct.setDelFlag(Model.FLAG_EXISTS);
					ct.set_id(CycleTypeOptionActivity.this.id);
					try {
						db.update(ct, CycleTypeOptionActivity.this);
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(CycleTypeOptionActivity.this, "操作失败！",
								Toast.LENGTH_SHORT).show();
						return;
					}
					Toast.makeText(CycleTypeOptionActivity.this, "操作成功！",
							Toast.LENGTH_SHORT).show();
					Intent i = new Intent(CycleTypeOptionActivity.this,
							MainActivity.class);
					i.setFlags(MainActivity.POSTION_SYSTEM_SETTIONG);
					CycleTypeOptionActivity.this.startActivity(i);
					CycleTypeOptionActivity.this.finish();

				}
			});
		} else {// add
			yesButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					DemoDB<CycleType> db = new DemoDB<CycleType>(
							new CycleType());
					CycleType ct = new CycleType();
					ct.setCycleLength(Long.valueOf(cycleLength.getText()
							.toString()));
					ct.setDateType(DateType.getType(((TextView) spinner
							.getSelectedView()).getText().toString()));
					ct.setName(name.getText().toString());
					ct.setDelFlag(Model.FLAG_EXISTS);
					db.insert(ct, CycleTypeOptionActivity.this);
					Toast.makeText(CycleTypeOptionActivity.this, "操作成功！",
							Toast.LENGTH_SHORT).show();
					Intent i = new Intent(CycleTypeOptionActivity.this,
							MainActivity.class);
					i.setFlags(MainActivity.POSTION_SYSTEM_SETTIONG);
					CycleTypeOptionActivity.this.startActivity(i);
					CycleTypeOptionActivity.this.finish();

				}
			});
		}

	}
}
