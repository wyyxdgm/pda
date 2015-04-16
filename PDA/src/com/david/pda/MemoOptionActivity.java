package com.david.pda;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.david.pda.sqlite.model.Memo;
import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;

public class MemoOptionActivity extends Activity {
	public static final int FLAG_UPDATE = 2;
	public static final int FLAG_ADD = 3;
	public static final int UPDATE_SUCCESS = 4;
	public static final int ADD_SUCCESS = 5;
	ImageButton backward;
	private Memo memo;
	EditText titleEditText;
	EditText contentEditText;
	CheckedTextView flagCheckedTextView;
	Button yesButton;
	Button cancleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_some_tools_memo_option);
		backward = (ImageButton) findViewById(R.id.main_some_tools_memo_option_topbar_backward);
		Bind.bindReturn(backward, MemoOptionActivity.this, MainActivity.class,
				MainActivity.POSTION_SOME_TOOLS);
		titleEditText = (EditText) findViewById(R.id.main_some_tools_memo_option_title);
		contentEditText = (EditText) findViewById(R.id.main_some_tools_memo_option_content);
		flagCheckedTextView = (CheckedTextView) findViewById(R.id.main_some_tools_memo_option_flag_checkbox);
		yesButton = (Button) findViewById(R.id.main_some_tools_memo_option_yes_button);
		cancleButton = (Button) findViewById(R.id.main_some_tools_memo_option_cancle_button);
		flagCheckedTextView
				.setOnClickListener(new FlagCheckedClickedListener());
		Intent intent = getIntent();
		if (FLAG_UPDATE == intent.getFlags()) {// update
			memo = new Memo(intent.getExtras());
			showMemoToView();
			yesButton.setOnClickListener(new UpdateListenr());
		} else if (FLAG_ADD == intent.getFlags()) {// add
			showMemoToView();
			yesButton.setOnClickListener(new AddListenr());
		}
		Bind.bindReturn(cancleButton, MemoOptionActivity.this,
				MemoActivity.class);
	}

	public void showMemoToView() {
		if (memo != null) {
			titleEditText.setText(memo.getTitle());
			contentEditText.setText(memo.getContent());
			boolean checked = memo.getFlag() != null && memo.getFlag() == 1;
			flagCheckedTextView.setChecked(checked);
			flagCheckedTextView
					.setCheckMarkDrawable(checked ? R.drawable.flag_mark_red
							: R.drawable.flag_mark_gray);
		}
	}

	public void FillMemoWidthView() {
		if (memo == null) {
			memo = new Memo();
		}
		memo.setContent(contentEditText.getText().toString());
		memo.setTitle(titleEditText.getText().toString());
		memo.setFlag(flagCheckedTextView.isChecked() ? 1 : 0);
		memo.setDelFlag(Model.FLAG_EXISTS);
		memo.setCreateTime(System.currentTimeMillis());
	}

	class UpdateListenr implements OnClickListener {
		@SuppressLint("ShowToast")
		@Override
		public void onClick(View arg0) {
			FillMemoWidthView();
			DemoDB<Memo> db = new DemoDB<Memo>(memo);
			try {
				db.update(memo, MemoOptionActivity.this);
				goBack(UPDATE_SUCCESS);
			} catch (JSONException e) {
				e.printStackTrace();
				goBack(-1);
			}
		}

	}

	class FlagCheckedClickedListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			CheckedTextView cv = (CheckedTextView) arg0;
			cv.toggle();
			if (!cv.isChecked()) {
				cv.setCheckMarkDrawable(R.drawable.flag_mark_gray);
				cv.setText("取消该红旗:");
			} else {
				cv.setCheckMarkDrawable(R.drawable.flag_mark_red);
				cv.setText("标记为红旗:");
			}
		}
	}

	class AddListenr implements OnClickListener {

		@SuppressLint("ShowToast")
		@Override
		public void onClick(View arg0) {
			FillMemoWidthView();
			DemoDB<Memo> db = new DemoDB<Memo>(memo);
			db.insert(memo, MemoOptionActivity.this);
			goBack(ADD_SUCCESS);
		}
	}

	private void goBack(int result) {
		Intent i = new Intent(MemoOptionActivity.this, MemoActivity.class);
		i.setFlags(result);
		i.putExtra("from", MemoOptionActivity.class.getName());
		startActivity(i);
		MemoOptionActivity.this.finish();
	}

}
