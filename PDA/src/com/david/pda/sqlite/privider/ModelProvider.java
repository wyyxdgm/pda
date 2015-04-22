package com.david.pda.sqlite.privider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.david.pda.sqlite.model.Alarm;
import com.david.pda.sqlite.model.Countdown;
import com.david.pda.sqlite.model.CycleDetails;
import com.david.pda.sqlite.model.CycleDetailsForAlarm;
import com.david.pda.sqlite.model.CycleDetailsForPlan;
import com.david.pda.sqlite.model.CycleDetailsForPrinciple;
import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.Memo;
import com.david.pda.sqlite.model.Plan;
import com.david.pda.sqlite.model.Principal;
import com.david.pda.sqlite.model.Target;
import com.david.pda.sqlite.model.util.DBHelper;

public class ModelProvider extends ContentProvider {
	public static final String AUTHORITY = "com.david.pda.model";
	private DBHelper dbHelper;
	// private static final int ALARM = 0;
	// private static final int COUNTDOWN = 1;
	// private static final int CYCLECONGTENT = 2;
	// private static final int CYCLETYPE = 3;
	// private static final int MEMO = 4;
	// private static final int PLAN = 5;
	// private static final int PRINCIPAL = 6;
	// private static final int TARGET = 7;
	private static final List<String> TABLES = new ArrayList<String>();
	public static final Map<String, String> CREATE_TABLE = new HashMap<String, String>();
	private static final UriMatcher URI_MATHER = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		TABLES.add(Alarm.TABLE_NAME);
		TABLES.add(Countdown.TABLE_NAME);
		TABLES.add(CycleDetailsForAlarm.TABLE_NAME);
		TABLES.add(CycleDetailsForPlan.TABLE_NAME);
		TABLES.add(CycleDetailsForPrinciple.TABLE_NAME);
		TABLES.add(CycleType.TABLE_NAME);
		TABLES.add(Memo.TABLE_NAME);
		TABLES.add(Plan.TABLE_NAME);
		TABLES.add(Principal.TABLE_NAME);
		TABLES.add(Target.TABLE_NAME);

		for (int i = 0; i < TABLES.size(); i++) {
			if (TABLES.get(i).indexOf("for") != -1) {
				URI_MATHER.addURI(AUTHORITY + "/" + CycleDetails.TABLE_NAME,
						TABLES.get(i), i);
			} else
				URI_MATHER.addURI(AUTHORITY, TABLES.get(i), i);
		}
		CREATE_TABLE.put(Alarm.TABLE_NAME, Alarm.CREATE_TABLE());
		CREATE_TABLE.put(Countdown.TABLE_NAME, Countdown.CREATE_TABLE());

		CREATE_TABLE.put(CycleDetailsForAlarm.TABLE_NAME,
				CycleDetailsForAlarm.CREATE_TABLE());
		CREATE_TABLE.put(CycleDetailsForPlan.TABLE_NAME,
				CycleDetailsForPlan.CREATE_TABLE());
		CREATE_TABLE.put(CycleDetailsForPrinciple.TABLE_NAME,
				CycleDetailsForPrinciple.CREATE_TABLE());
		CREATE_TABLE.put(CycleType.TABLE_NAME, CycleType.CREATE_TABLE());
		CREATE_TABLE.put(Memo.TABLE_NAME, Memo.CREATE_TABLE());
		CREATE_TABLE.put(Plan.TABLE_NAME, Plan.CREATE_TABLE());
		CREATE_TABLE.put(Principal.TABLE_NAME, Principal.CREATE_TABLE());
		CREATE_TABLE.put(Target.TABLE_NAME, Target.CREATE_TABLE());
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String groupBy = null;
		String having = null;
		String table = getTableNameByUri(uri);
		if (table != null) {
			qb.setTables(table);
			return qb.query(db, projection, selection, selectionArgs, groupBy,
					having, sortOrder);
		}
		return null;
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String table = getTableNameByUri(uri);
		if (table != null) {
			long rId = db.insert(table, null, values);
			return ContentUris.withAppendedId(uri, rId);
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(getContext());
		return false;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		return updateOrInsert(db, uri, values, selection, selectionArgs);
	}

	private int updateOrInsert(SQLiteDatabase db, Uri uri,
			ContentValues values, String selection, String[] selectionArgs) {
		int count;
		String table = getTableNameByUri(uri);
		if (table != null) {
			count = db.update(table, values, selection, selectionArgs);
			if (count == 0) {
				return Integer.parseInt(insert(uri, values)
						.getLastPathSegment());
			}
		}
		return 0;
	}

	private String getTableNameByUri(Uri uri) {
		int match = URI_MATHER.match(uri);
		if (match >= 0 && match < TABLES.size()) {
			return TABLES.get(match);
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String table = getTableNameByUri(uri);
		if (getTableNameByUri(uri) != null) {
			return db.delete(table, selection, selectionArgs);
		}
		return -1;
	}

}
