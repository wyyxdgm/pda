package com.david.pda.sqlite.model.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.david.pda.sqlite.model.Target;

public class TargetUtil {

	public static List<Target> getList(Context context) {
		Cursor c = context.getContentResolver().query(Target.CONTENT_URI, null,
				null, null, null);
		List<Target> list = new ArrayList<Target>();
		if (c != null) {
			while (c.moveToNext()) {
				list.add(new Target(c));
			}
			c.close();
		}
		return list;
	}

	public static boolean update(Target obj, Context context)
			throws JSONException {
		ContentValues values = obj.toContentValues();
		String where = Target._ID + "= '" + obj.get_id() + "'";
		int count = context.getContentResolver().update(Target.CONTENT_URI,
				values, where, null);
		return count > 0;
	}

	public static Target get(String id, Context context) {
		Cursor c = context.getContentResolver().query(Target.CONTENT_URI, null,
				Target._ID + " = ? ", new String[] { id }, null);
		Target item = null;
		if (c != null) {
			if (c.moveToNext()) {
				item = new Target(c);
			}
			c.close();
		}
		return item;
	}

	public static boolean remove(String id, Context context)
			throws JSONException {
		String where = Target._ID + "= '" + id + "'";
		Target item = get(id, context);
		item.setDelFlag(0);// delete
		ContentValues values = item.toContentValues();
		int count = context.getContentResolver().update(Target.CONTENT_URI,
				values, where, new String[] { Target.DELFLAG });
		return count > 0;
	}

	public static void clear(Context context) {
		ContentResolver mContentResolver = context.getApplicationContext()
				.getContentResolver();
		mContentResolver.delete(Target.CONTENT_URI, null, null);
	}
}
