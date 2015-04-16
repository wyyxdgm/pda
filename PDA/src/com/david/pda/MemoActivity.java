package com.david.pda;

import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.david.pda.sqlite.model.Memo;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;

public class MemoActivity extends Activity {
	Button add;
	GridView memoGridView;
	ImageButton backward;
	List<Memo> memoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_some_tools_memo);
		memoGridView = (GridView) findViewById(R.id.main_some_tools_memo_grid);
		add = (Button) findViewById(R.id.addMemo);
		backward = (ImageButton) findViewById(R.id.main_some_tools_memo_topbar_backward);
		Bind.bindReturn(backward, this, MainActivity.class,
				MainActivity.POSTION_SOME_TOOLS);
		initGrid();
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MemoActivity.this,
						MemoOptionActivity.class);
				intent.setFlags(MemoOptionActivity.FLAG_ADD);
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
			if (from.equals(MemoOptionActivity.class.getName())) {
				if (intent.getFlags() == MemoOptionActivity.ADD_SUCCESS) {
					Toast.makeText(MemoActivity.this, "填加成功！",
							Toast.LENGTH_SHORT).show();
				} else if (intent.getFlags() == MemoOptionActivity.UPDATE_SUCCESS) {
					Toast.makeText(MemoActivity.this, "更新成功！",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MemoActivity.this, "操作失败！",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public void initGrid() {
		DemoDB<Memo> db = new DemoDB<Memo>(new Memo());
		memoList = db.getList(MemoActivity.this);
		memoGridView.setAdapter(new MemoGridAdapter(MemoActivity.this));
		memoGridView.setOnItemClickListener(new MemoGridItemClickListener());
		memoGridView
				.setOnItemLongClickListener(new MemoGridItemLongClickListener());
	}

	class MemoGridItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int index,
				long rowIndex) {
			Memo m = memoList.get(index);
			Intent intent = new Intent(MemoActivity.this,
					MemoOptionActivity.class);
			intent.putExtras(m.toBundle());
			intent.setFlags(MemoOptionActivity.FLAG_UPDATE);
			startActivity(intent);
		}

	}

	class MemoGridItemLongClickListener implements OnItemLongClickListener {

		@SuppressLint("ShowToast")
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int index, long arg3) {
			DemoDB<Memo> db = new DemoDB<Memo>(new Memo());
			try {
				db.realRemove(memoList.get(index).get_id() + "",
						MemoActivity.this);
				Toast.makeText(MemoActivity.this,
						"删除《" + memoList.get(index).getTitle() + "》成功！",
						Toast.LENGTH_SHORT).show();
				memoList.remove(index);
				initGrid();
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(MemoActivity.this, "删除失败！", Toast.LENGTH_SHORT)
						.show();
			}
			return false;
		}
	}

	public List<Memo> getData() {
		return memoList;
	}
}
