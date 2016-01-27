package com.david.pda.sqlite.model.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.DateType;
import com.david.pda.sqlite.privider.ModelProvider;

public class DBHelper extends SQLiteOpenHelper {
	Context c;

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
		String insertPre = "insert into " + CycleType.TABLE_NAME + "("
				+ CycleType.NAME + "," + CycleType.CYCLELENGTH + ","
				+ CycleType.DATETYPE + ")values";
		db.execSQL(insertPre + "('每年','1'," + DateType.YEAR.getIndex() + ")");
		db.execSQL(insertPre + "('每季度','1'," + DateType.QUARTER.getIndex()
				+ ")");
		db.execSQL(insertPre + "('每月','1'," + DateType.MONTH.getIndex() + ")");
		db.execSQL(insertPre + "('每周','1'," + DateType.WEEK.getIndex() + ")");
		db.execSQL(insertPre + "('每天','1'," + DateType.DAY.getIndex() + ")");
		db.execSQL(insertPre + "('每小时','1'," + DateType.HOUR.getIndex() + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
