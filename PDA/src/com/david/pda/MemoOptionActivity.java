package com.david.pda;

import com.david.pda.util.other.Bind;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

public class MemoOptionActivity extends Activity {
	ImageButton backward;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		backward = (ImageButton) findViewById(R.id.main_some_tools_memo_option_topbar_backward);
		Bind.bindReturn(backward, this, MainActivity.class,
				MainActivity.POSTION_SOME_TOOLS);
	}
}
