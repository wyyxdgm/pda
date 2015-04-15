package com.david.pda.sqlite.model.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.david.pda.sqlite.model.base.Model;

public class DemoDB<T extends Model> {
	@SuppressWarnings("rawtypes")
	Class tClass;

	public DemoDB(Class<T> tClass) {
		this.tClass = tClass.getClass();
	}

	@SuppressWarnings("unchecked")
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

	public Uri insert(Model obj, Context context) throws JSONException {
		return context.getContentResolver().insert(T.CONTENT_URI,
				obj.toContentValues());
	}

	@SuppressWarnings("unchecked")
	public T get(String id, Context context) {
		Cursor c = context.getContentResolver().query(T.CONTENT_URI, null,
				T._ID + " = ? ", new String[] { id }, null);
		T item = null;
		try {
			if (c != null) {
				if (c.moveToNext()) {
					Object o;
					o = tClass.getClass().newInstance();
					item = (T) ((T) o).getInstance(c);
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
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

	public boolean realRemove(String id, Context context) throws JSONException {
		String where = T._ID + "= '" + id + "'";
		int count = context.getContentResolver().delete(T.CONTENT_URI, where,
				new String[] { T.DELFLAG });
		return count > 0;
	}

	public void clear(Context context) {
		ContentResolver mContentResolver = context.getApplicationContext()
				.getContentResolver();
		mContentResolver.delete(T.CONTENT_URI, null, null);
	}
}
