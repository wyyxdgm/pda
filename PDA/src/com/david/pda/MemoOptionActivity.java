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
import com.david.pda.sqlite.model.util.DemoDB;
import com.david.pda.util.other.Bind;

public class MemoOptionActivity extends Activity {
	public static final int FLAG_UPDATE = 2;
	public static final int FLAG_ADD = 3;
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
		Intent intent = getIntent();
		if (FLAG_UPDATE == intent.getFlags()) {// update
			memo = new Memo(intent.getExtras());
			showMemoToView();
			yesButton.setOnClickListener(new UpdateListenr());
		} else if (FLAG_ADD == intent.getFlags()) {// add
			memo = new Memo();
			showMemoToView();
			yesButton.setOnClickListener(new AddListenr());
		}
		Bind.bindReturn(cancleButton, MemoOptionActivity.this,
				MemoActivity.class);
	}

	public void showMemoToView() {
		titleEditText.setText(memo.getTitle());
		contentEditText.setText(memo.getContent());
		flagCheckedTextView.setChecked(memo.getFlag() == 1);
	}

	public void FillMemoWidthView() {
		if (memo == null) {
			memo = new Memo();
		}
		memo.setContent(contentEditText.getText().toString());
		memo.setTitle(titleEditText.getText().toString());
		memo.setFlag(flagCheckedTextView.isChecked() ? 1 : 0);
	}

	class UpdateListenr implements OnClickListener {
		@SuppressLint("ShowToast")
		@Override
		public void onClick(View arg0) {
			FillMemoWidthView();
			DemoDB<Memo> db = new DemoDB<Memo>(memo);
			try {
				db.update(memo, MemoOptionActivity.this);
				Toast.makeText(MemoOptionActivity.this, "更新成功！",
						Toast.LENGTH_SHORT);
			} catch (JSONException e) {
				Toast.makeText(MemoOptionActivity.this, "更新时出错！",
						Toast.LENGTH_SHORT);
				e.printStackTrace();
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
			Toast.makeText(MemoOptionActivity.this, "填加成功！", Toast.LENGTH_SHORT);
		}
	}

}
