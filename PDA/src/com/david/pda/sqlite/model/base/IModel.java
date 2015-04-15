package com.david.pda.sqlite.model.base;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.database.Cursor;

public interface IModel<T> {
	public static final Integer IS_YES = 1;
	public static final Integer IS_NO = 0;
	public static final Integer FLAG_DELETED = 0;
	public static final Integer FLAG_EXISTS = 1;
	public static final String DELFLAG = "delFlag";
	T getInstance(Cursor c);

	List<T> getList(JSONArray ja) throws JSONException;

	public ContentValues toContentValues();

	public Long get_id();

	public void setDelFlag(Integer delFlag);

	public Integer getDelFlag();
}
