package com.david.pda.util.io;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.david.pda.weather.model.util.L;

public class SharePreferencesUtil {

	/**
	 * 默认sp是传入的ctx的全路径，由此放回Editor
	 * 
	 * @param ctx
	 * @param mode
	 * @return
	 */
	public static Editor getEditor(Context ctx, int mode) {
		SharedPreferences sp = ctx.getSharedPreferences(ctx.getClass()
				.getName(), Context.MODE_MULTI_PROCESS);
		Log.i(L.t, ctx.getClass().getName());
		// 存入数据
		return sp.edit();
	}

	@SuppressWarnings("unchecked")
	public void save(Editor editor, String key, Object val) {
		if (val != null) {
			if (val instanceof String) {
				editor.putString(key, (String) val);
			} else if (val instanceof Boolean) {
				editor.putBoolean(key, (Boolean) val);
			} else if (val instanceof Boolean) {
				editor.putFloat(key, (Float) val);
			} else if (val instanceof Boolean) {
				editor.putInt(key, (Integer) val);
			} else if (val instanceof Boolean) {
				editor.putLong(key, (Long) val);
			} else if (val instanceof Boolean) {
				editor.putStringSet(key, (Set<String>) val);
			}
			editor.commit();
		}
		if (val != null)
			Log.d(L.t, key + ":" + val.toString());
	}

}
