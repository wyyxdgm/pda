package com.david.pda.sqlite.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public interface IModel<T> {
	public static final Integer IS_YES = 1;
	public static final Integer IS_NO = 0;
	public static final Integer FLAG_DELETED = 0;
	public static final Integer FLAG_EXISTS = 1;
	public static final String DELFLAG = "delFlag";
	public static final Uri CONTENT_URI = Uri.parse("model");

	T getInstance(Cursor c);

	List<T> getList(JSONArray ja) throws JSONException;

	public ContentValues toContentValues();

	public Long get_id();

	public void setDelFlag(Integer delFlag);

	public Integer getDelFlag();
}
