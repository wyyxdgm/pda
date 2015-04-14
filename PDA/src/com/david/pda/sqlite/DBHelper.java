package com.david.pda.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库和表
 * 
 * @author Administrator
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	private static String DATEBASE_NAME;
	private static int DATEBASE_VERSION;
	private static String TableName;
	private static String sql; // 建表sql语句

	/************* 设置数据库、表名字和版本 start ************/

	public static void setDATEBASE_NAME(String dATEBASE_NAME) {
		DATEBASE_NAME = dATEBASE_NAME;
	}

	public static void setSql(String sql) {
		DBHelper.sql = sql;
	}

	public static void setDATEBASE_VERSION(int dATEBASE_VERSION) {
		DATEBASE_VERSION = dATEBASE_VERSION;
	}

	public static void setTableName(String tableName) {
		TableName = tableName;
	}

	/************ 设置数据库、表名字和版本 end ***********/
	public DBHelper(Context context) {
		super(context, DATEBASE_NAME, null, DATEBASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("ALTER TABLE " + TableName + " ADD COLUMN other STRING");
	}
}
