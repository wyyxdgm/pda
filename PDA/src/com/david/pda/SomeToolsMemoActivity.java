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

import com.david.pda.adapter.MemoGridAdapter;
import com.david.pda.application.SysApplication;
import com.david.pda.sqlite.model.Memo;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;

public class SomeToolsMemoActivity extends Activity {
	Button add;
	GridView memoGridView;
	ImageButton backward;
	List<Memo> memoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_some_tools_memo);
		SysApplication.getInstance().addActivity(this);
		memoGridView = (GridView) findViewById(R.id.main_some_tools_memo_grid);
		add = (Button) findViewById(R.id.addMemo);
		backward = (ImageButton) findViewById(R.id.main_some_tools_memo_topbar_backward);
		Bind.bindReturn(backward, this, MainActivity.class,
				MainActivity.POSTION_SOME_TOOLS);
		initGrid();
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SomeToolsMemoActivity.this,
						SomeToolsMemoOptionActivity.class);
				intent.setFlags(SomeToolsMemoOptionActivity.FLAG_ADD);
				startActivity(intent);
			}
		});
		resolveIntent();
	}

	@SuppressLint("ShowToast")
	private void resolveIntent() {
		Intent intent = getIntent();
		String from = intent.getStringExtra("from");
		if (from != null) {
			if (from.equals(SomeToolsMemoOptionActivity.class.getName())) {
				if (intent.getFlags() == SomeToolsMemoOptionActivity.ADD_SUCCESS) {
					Toast.makeText(SomeToolsMemoActivity.this, "填加成功！",
							Toast.LENGTH_SHORT).show();
				} else if (intent.getFlags() == SomeToolsMemoOptionActivity.UPDATE_SUCCESS) {
					Toast.makeText(SomeToolsMemoActivity.this, "更新成功！",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SomeToolsMemoActivity.this, "操作失败！",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public void initGrid() {
		DemoDB<Memo> db = new DemoDB<Memo>(new Memo());
		memoList = db.getList(SomeToolsMemoActivity.this);
		memoGridView
				.setAdapter(new MemoGridAdapter(SomeToolsMemoActivity.this));
		memoGridView.setOnItemClickListener(new MemoGridItemClickListener());
		memoGridView
				.setOnItemLongClickListener(new MemoGridItemLongClickListener());
	}

	class MemoGridItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int index,
				long rowIndex) {
			Memo m = memoList.get(index);
			Intent intent = new Intent(SomeToolsMemoActivity.this,
					SomeToolsMemoOptionActivity.class);
			intent.putExtras(m.toBundle());
			intent.setFlags(SomeToolsMemoOptionActivity.FLAG_UPDATE);
			startActivity(intent);
		}

	}

	class MemoGridItemLongClickListener implements OnItemLongClickListener {
		public int index;

		@SuppressLint("ShowToast")
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int index, long arg3) {
			AlertDialog.Builder builder = new Builder(
					SomeToolsMemoActivity.this);
			builder.setMessage("确认删除吗？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							DemoDB<Memo> db = new DemoDB<Memo>(new Memo());
							try {
								db.realRemove(
										memoList.get(index).get_id() + "",
										SomeToolsMemoActivity.this);
								Toast.makeText(
										SomeToolsMemoActivity.this,
										"删除《" + memoList.get(index).getTitle()
												+ "》成功！", Toast.LENGTH_SHORT)
										.show();
								memoList.remove(index);
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

	public List<Memo> getData() {
		return memoList;
	}
}
