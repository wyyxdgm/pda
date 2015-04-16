package com.david.pda;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.david.pda.sqlite.model.Memo;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.weather.model.util.L;

public class DbActivity extends Activity {
	Button add;
	Button query;
	TextView content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_test);
		add = (Button) findViewById(R.id.addMemo);
		query = (Button) findViewById(R.id.queryMemo);
		content = (TextView) findViewById(R.id.content);
		Log.i(L.t, "create");
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DemoDB<Memo> db = new DemoDB<Memo>(new Memo());
				Memo m = new Memo("test", "content", 1, new Date().getTime(),
						Model.FLAG_EXISTS);
				db.insert(m, DbActivity.this);
			}
		});
		query.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DemoDB<Memo> db = new DemoDB<Memo>(new Memo());
				List<Memo> mList = db.getList(DbActivity.this);
				StringBuffer sb = new StringBuffer();
				for (Memo memo : mList) {
					if (memo != null)
						sb.append(memo.toContentValues().toString());
				}
				content.setText(sb.toString());
			}
		});
	}
}
