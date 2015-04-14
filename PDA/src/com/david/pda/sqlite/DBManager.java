package com.david.pda.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 使用规则:需先设置数据库名字、表名、版本号信息
 * 
 * @author Administrator
 */
public class DBManager {
	public static DBManager DB;
	public static DBHelper helper;
	public static SQLiteDatabase db;
	private static String DATEBASE_NAME;
	private static int DATEBASE_VERSION;
	private static String TableName;
	private static String sql;
	private static SQLiteDatabase sqliteDatebase;

	/************* 设置数据库、表名字和版本 start ************/
	public static String getDATEBASE_NAME() {
		return DATEBASE_NAME;
	}

	public static void setDATEBASE_NAME(String dATEBASE_NAME) {
		DATEBASE_NAME = dATEBASE_NAME;
	}

	public static int getDATEBASE_VERSION() {
		return DATEBASE_VERSION;
	}

	public static void setDATEBASE_VERSION(int dATEBASE_VERSION) {
		DATEBASE_VERSION = dATEBASE_VERSION;
	}

	public static String getTableName() {
		return TableName;
	}

	public static void setTableName(String tableName) {
		TableName = tableName;
	}

	/**
	 * 初始化
	 * 
	 * @param DB_name
	 *            数据库名字
	 * @param DB_version
	 *            数据库版本
	 * @param TbaleName
	 *            表名
	 * @param CreateSql
	 *            表字段
	 */
	public static void InitDB(String DB_name, int DB_version, String TableName,
			KeyValueMap<String, String> CreateSql) {
		DBManager.setDATEBASE_NAME(DB_name);
		DBManager.setDATEBASE_VERSION(DB_version);
		DBManager.setTableName(TableName);
		DBManager.sqlTable(CreateSql);
	}

	/************ 设置数据库、表名字和版本 end ***********/
	private DBManager(Context context) {
		DBHelper.setDATEBASE_NAME(getDATEBASE_NAME());
		DBHelper.setDATEBASE_VERSION(getDATEBASE_VERSION());
		DBHelper.setTableName(getTableName());
		DBHelper.setSql(sql);
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	public static DBManager getIntance(Context context) {
		if (DB == null) {
			DB = new DBManager(context);
		}
		return DB;
	}

	/**
	 * 创建表的sql语句 该法方法id自增长
	 * 
	 * @param hashmap
	 *            字段和参数
	 */
	public static void sqlTable(KeyValueMap<String, String> hashmap) {
		StringBuffer sb = new StringBuffer();
		for (int index = 0; index < hashmap.getSize(); index++) {
			String key = hashmap.getKey(index);
			String value = hashmap.getValue(index);
			sb.append(value).append(" ").append(key).append(",");
		}
		String value = sb.toString();
		String result = value.substring(0, value.length() - 1);
		sql = "create table " + TableName
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + result + ")";
	}

	/**
	 * 创建表的sql语句
	 * 
	 * @param cpysql
	 *            建表的sql语句
	 */
	public static void sqlTabel(String cpysql) {
		sql = cpysql;
	}

	/**
	 * 添加数据
	 * 
	 * @param TabelName
	 *            表名
	 * @param hashmap
	 *            参数值
	 */
	public void AddDate(String TabelName, KeyValueMap<String, String> hashmap) {
		sqliteDatebase = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (int index = 0; index < hashmap.getSize(); index++) {
			values.put(hashmap.getKey(index), hashmap.getValue(index));
		}
		sqliteDatebase.insert(TabelName, null, values);
	}

	/**
	 * 删除数据
	 * 
	 * @param TabelName
	 *            第一个参数String：表名
	 * @param arg0
	 *            第二个参数String：查询条件
	 * @param arg1
	 *            第三参数String[]：查询条件的参数
	 */
	public void DelDate(String TabelName, String arg0, String[] arg1) {
		sqliteDatebase = helper.getWritableDatabase();
		sqliteDatebase.delete(TabelName, arg0, arg1);
	}

	/**
	 * 更新数据
	 * 
	 * @param TabelName
	 * @param map
	 *            更新的数据
	 * @param arg1
	 *            查询条件
	 * @param arg2
	 *            查询条件的参数
	 */
	public void Update(String TabelName, KeyValueMap<String, String> map,
			String arg1, String[] arg2) {
		sqliteDatebase = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (int index = 0; index < map.getSize(); index++) {
			values.put(map.getKey(index), map.getValue(index));
		}
		sqliteDatebase.update(TabelName, values, arg1, arg2);
	}

	/**
	 * 查询数据
	 * 
	 * @param TableName
	 *            第一个参数String：表名
	 * @param arg0
	 *            第二个参数String[]:要查询的列名
	 * @param arg1
	 *            第三个参数String：查询条件
	 * @param arg2
	 *            第四个参数String[]：查询条件的参数
	 * @param arg3
	 *            第五个参数String:对查询的结果进行分组
	 * @param arg4
	 *            第六个参数String：对分组的结果进行限制
	 * @param arg5
	 *            第七个参数String：对查询的结果进行排序
	 * @return
	 */
	public Cursor query(String TableName, String[] arg0, String arg1,
			String[] arg2, String arg3, String arg4, String arg5) {
		sqliteDatebase = helper.getReadableDatabase();
		Cursor cursor = sqliteDatebase.query(TableName, arg0, arg1, arg2, arg3,
				arg4, arg5);
		return cursor;
	}

}
