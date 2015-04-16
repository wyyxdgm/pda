package com.david.pda;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;

import com.david.pda.sqlite.model.Memo;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;
import com.david.pda.util.other.DateUtil;

public class MemoActivity extends Activity {
	Button add;
	Button query;
	GridView memoGridView;
	ImageButton backward;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_some_tools_memo);
		memoGridView = (GridView) findViewById(R.id.main_some_tools_memo_grid);
		add = (Button) findViewById(R.id.addMemo);
		backward = (ImageButton) findViewById(R.id.main_some_tools_memo_topbar_backward);
		Bind.bindReturn(backward, this, MainActivity.class,
				MainActivity.POSTION_SOME_TOOLS);
		query = (Button) findViewById(R.id.queryMemo);
		initGrid();
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Memo m = new Memo("test", "content", 1, new Date().getTime(),
						Model.FLAG_EXISTS);
				DemoDB<Memo> db = new DemoDB<Memo>(m);
				db.insert(m, MemoActivity.this);
			}
		});
		query.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				initGrid();
			}
		});
	}

	public void initGrid() {
		DemoDB<Memo> db = new DemoDB<Memo>(new Memo());
		List<Memo> mList = db.getList(MemoActivity.this);
		List<Map<String, Object>> arryList = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = null;
		for (Memo memo : mList) {
			item = new HashMap<String, Object>();
			item.put("title", memo.getTitle());
			item.put("content", memo.getContent());
			item.put("createTime",
					DateUtil.formatYYYY_MM_DD(memo.getCreateTime()));
			item.put("flag", memo.getFlag() == 1);
			arryList.add(item);
		}
		SimpleAdapter memoGridItemAdapter = new SimpleAdapter(this, arryList,
				R.layout.grid_item_memo, new String[] { "title", "content",
						"createTime", "flag" }, new int[] {
						R.id.some_tools_memo_item_title,
						R.id.some_tools_memo_item_content,
						R.id.some_tools_memo_item_createTime,
						R.id.some_tools_memo_item_flag });
		memoGridView.setAdapter(memoGridItemAdapter);
		memoGridView.setOnItemClickListener(new MemoGridItemClickListener());
	}

	public class MemoGridItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int index,
				long rowIndex) {
		}

	}
}
