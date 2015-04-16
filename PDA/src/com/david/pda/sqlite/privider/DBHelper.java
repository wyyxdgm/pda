package com.david.pda.sqlite.privider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DBHelper(Context context) {
		super(context, "pda", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String sql : ModelProvider.CREATE_TABLE.values()) {
			db.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
