package com.JAC.stubank.provider;

import com.JAC.stubank.provider.DatabaseConstant.AccountConstant;
import com.JAC.stubank.model.Account;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AccountUtils {

	public static List<Account> getList(Context context) {
		Cursor c = context.getContentResolver().query(
				AccountConstant.CONTENT_URI, null, null, null, null);
		List<Account> list = new ArrayList<Account>();
		if (c != null) {
			while (c.moveToNext()) {
				list.add(new Account(c));
			}
			c.close();
		}
		return list;
	}

	public static List<Account> getListDesc(Context context) {
		Cursor c = context.getContentResolver().query(
				AccountConstant.CONTENT_URI, null, null,
				new String[] { AuthUtils.getUserId(context) },
				AccountConstant.CREATE_TIME + " desc");
		List<Account> list = new ArrayList<Account>();
		if (c != null) {
			while (c.moveToNext()) {
				list.add(new Account(c));
			}
			c.close();
		}
		return list;
	}

	public static Account get(Context context) {
		Cursor c = context.getContentResolver().query(
				AccountConstant.CONTENT_URI, null, null, null, null);
		Account item = null;
		if (c != null) {
			if (c.moveToNext()) {
				item = new Account(c);
			}
			c.close();
		}
		return item;
	}

	public static boolean save(Account obj, Context context)
			throws JSONException {
		ContentValues values = obj.toContentValues();
		values.put(AccountConstant.PRESENT, 1);
		String where = AccountConstant._ID + "= '" + obj._id + "'";
		int count = context.getContentResolver().update(
				AccountConstant.CONTENT_URI, values, where, null);
		return count > 0;
	}

	public static boolean saveList(List<Account> li, Context context)
			throws JSONException {
		for (int i = 0; i < li.size(); i++) {
			save(li.get(i), context);
		}
		return true;
	}

	public static Account get(String id, Context context) {
		Cursor c = context.getContentResolver().query(
				AccountConstant.CONTENT_URI, null,
				AccountConstant._ID + " = ? ", new String[] { id }, null);
		Account item = null;
		if (c != null) {
			if (c.moveToNext()) {
				item = new Account(c);
			}
			c.close();
		}
		return item;
	}

	public static boolean remove(String id, Context context)
			throws JSONException {
		String where = AccountConstant._ID + "= '" + id + "'";
		int count = context.getContentResolver().delete(
				AccountConstant.CONTENT_URI, where, null);
		return count > 0;
	}

	public static void clear(Context context) {
		ContentResolver mContentResolver = context.getApplicationContext()
				.getContentResolver();
		mContentResolver.delete(AccountConstant.CONTENT_URI, null, null);
	}

}
