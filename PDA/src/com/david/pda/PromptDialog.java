package com.david.pda;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PromptDialog extends AlertDialog {
	Context context;

	public PromptDialog(Context context) {
		super(context);
	}

	public PromptDialog(Context context, boolean cancelable,
			DialogInterface.OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

}
