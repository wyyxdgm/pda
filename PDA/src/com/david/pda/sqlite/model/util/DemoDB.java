package com.david.pda.sqlite.model.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.david.pda.sqlite.model.Alarm;
import com.david.pda.sqlite.model.Model;

public class DemoDB<T extends Model> {
	Class tClass;

	public DemoDB(Class<T> tz) {
		this.tClass = tz.getClass();
	}

	public T getModel(Cursor c) {
		Object o = null;
		try {
			o = tClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return (T) ((T) o).getInstance(c);
	}

	public List<T> getList(Context context) {
		Cursor c = context.getContentResolver().query(T.CONTENT_URI, null,
				null, null, null);
		List<T> list = new ArrayList<T>();
		if (c != null) {
			while (c.moveToNext()) {
				list.add(getModel(c));
			}
			c.close();
		}
		return list;
	}

	public boolean update(Model obj, Context context) throws JSONException {
		ContentValues values = obj.toContentValues();
		String where = T._ID + "= '" + obj.get_id() + "'";
		int count = context.getContentResolver().update(T.CONTENT_URI, values,
				where, null);
		return count > 0;
	}

	@SuppressWarnings("unchecked")
	public T get(String id, Context context) {
		Cursor c = context.getContentResolver().query(T.CONTENT_URI, null,
				T._ID + " = ? ", new String[] { id }, null);
		T item = null;
		if (c != null) {
			if (c.moveToNext()) {
				item = (T) new Model(c);
			}
			c.close();
		}
		return item;
	}

	public boolean remove(String id, Context context) throws JSONException {
		String where = T._ID + "= '" + id + "'";
		T item = get(id, context);
		item.setDelFlag(0);// delete
		ContentValues values = item.toContentValues();
		int count = context.getContentResolver().update(T.CONTENT_URI, values,
				where, new String[] { T.DELFLAG });
		return count > 0;
	}

	public void clear(Context context) {
		ContentResolver mContentResolver = context.getApplicationContext()
				.getContentResolver();
		mContentResolver.delete(T.CONTENT_URI, null, null);
	}
}
