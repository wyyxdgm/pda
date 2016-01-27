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
	T t;

	@SuppressWarnings("unchecked")
	public DemoDB(Model t) {
		this.t = (T) t;
	}

	@SuppressWarnings("unchecked")
	public T getModel(Cursor c) {
		return (T) t.getInstance(c);
	}

	public List<T> getList(Context context) {
		// String where = Model.DELFLAG + "= ?";
		Cursor c = context.getContentResolver().query(t.CONTEN_URI(), null,
				null, null, " _id desc");
		List<T> list = new ArrayList<T>();
		if (c != null) {
			while (c.moveToNext()) {
				list.add(getModel(c));
			}
			c.close();
		}
		return list;
	}

	public List<T> getList(Context context, String selections, String[] args,
			String orderBy) {
		Cursor c = context.getContentResolver().query(t.CONTEN_URI(), null,
				selections, args, orderBy == null ? " _id desc" : null);
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
		String where = Model._ID + "=?";
		int count = context.getContentResolver().update(t.CONTEN_URI(), values,
				where, new String[] { obj.get_id() + "" });
		return count > 0;
	}

	public Uri insert(Model obj, Context context) {
		return context.getContentResolver().insert(t.CONTEN_URI(),
				obj.toContentValues());
	}

	@SuppressWarnings("unchecked")
	public T get(String id, Context context) {
		Cursor c = context.getContentResolver().query(t.CONTEN_URI(), null,
				Model._ID + " = ?", new String[] { id }, null);
		T item = null;
		if (c != null) {
			if (c.moveToNext()) {
				item = (T) t.getInstance(c);
			}
		}
		c.close();
		return item;
	}

	public boolean remove(String id, Context context) throws JSONException {
		String where = Model._ID + "=?";
		T item = get(id, context);
		item.setDelFlag(Model.FLAG_DELETED);// delete
		ContentValues values = item.toContentValues();
		int count = context.getContentResolver().update(t.CONTEN_URI(), values,
				where, new String[] { id });
		return count > 0;
	}

	public boolean realRemove(String id, Context context) throws JSONException {
		String where = Model._ID + "=?";
		int count = context.getContentResolver().delete(t.CONTEN_URI(), where,
				new String[] { id });
		return count > 0;
	}

	public void clear(Context context) {
		ContentResolver mContentResolver = context.getApplicationContext()
				.getContentResolver();
		mContentResolver.delete(t.CONTEN_URI(), null, null);
	}
}
