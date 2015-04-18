package com.david.pda;

import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.david.pda.adapter.CountdownGridAdapter;
import com.david.pda.sqlite.model.Countdown;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;

public class SomeToolsCountdownActivity extends Activity {
	Button add;
	GridView countdownGridView;
	ImageButton backward;
	List<Countdown> countdownList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_some_tools_countdown);
		countdownGridView = (GridView) findViewById(R.id.main_some_tools_countdown_grid);
		add = (Button) findViewById(R.id.addCountdown);
		backward = (ImageButton) findViewById(R.id.main_some_tools_countdown_topbar_backward);
		Bind.bindReturn(backward, this, MainActivity.class,
				MainActivity.POSTION_SOME_TOOLS);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SomeToolsCountdownActivity.this,
						SomeToolsCountdownOptionActivity.class);
				intent.setFlags(SomeToolsCountdownOptionActivity.FLAG_ADD);
				startActivity(intent);
			}
		});
		resolveIntent();
		initGrid();
	}

	@SuppressLint("ShowToast")
	private void resolveIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			String from = intent.getStringExtra("from");// work with option
														// activity
			if (from != null) {
				if (from.equals(SomeToolsCountdownOptionActivity.class
						.getName())) {
					if (intent.getFlags() == SomeToolsCountdownOptionActivity.ADD_SUCCESS) {
						Toast.makeText(SomeToolsCountdownActivity.this,
								"填加成功！", Toast.LENGTH_SHORT).show();
					} else if (intent.getFlags() == SomeToolsCountdownOptionActivity.UPDATE_SUCCESS) {
						Toast.makeText(SomeToolsCountdownActivity.this,
								"更新成功！", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(SomeToolsCountdownActivity.this,
								"操作失败！", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}

	}

	public void initGrid() {
		DemoDB<Countdown> db = new DemoDB<Countdown>(new Countdown());
		countdownList = db.getList(SomeToolsCountdownActivity.this);
		countdownGridView.setAdapter(new CountdownGridAdapter(
				SomeToolsCountdownActivity.this));
		countdownGridView
				.setOnItemClickListener(new CountdownGridItemClickListener());
		countdownGridView
				.setOnItemLongClickListener(new CountdownGridItemLongClickListener());
	}

	class CountdownGridItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int index,
				long rowIndex) {
			Countdown m = countdownList.get(index);
			Intent intent = new Intent(SomeToolsCountdownActivity.this,
					SomeToolsCountdownOptionActivity.class);
			intent.putExtras(m.toBundle());
			intent.setFlags(SomeToolsCountdownOptionActivity.FLAG_UPDATE);
			startActivity(intent);
		}

	}

	class CountdownGridItemLongClickListener implements OnItemLongClickListener {

		@SuppressLint("ShowToast")
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int index, long arg3) {
			AlertDialog.Builder builder = new Builder(
					SomeToolsCountdownActivity.this);
			builder.setMessage("确认删除吗？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							DemoDB<Countdown> db = new DemoDB<Countdown>(
									new Countdown());
							try {
								db.realRemove(countdownList.get(index).get_id()
										+ "", SomeToolsCountdownActivity.this);

								Toast.makeText(
										SomeToolsCountdownActivity.this,
										"删除《"
												+ countdownList.get(index)
														.getTitle() + "》成功！",
										Toast.LENGTH_SHORT).show();
								countdownList.remove(index);
								initGrid();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			return false;
		}
	}

	public List<Countdown> getData() {
		return countdownList;
	}
}
