package com.david.pda.sqlite.model.base;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public abstract class Model implements IModel<Model>, Serializable {
	private static final long serialVersionUID = 2911321711627520545L;
	public static final Integer IS_YES = 1;
	public static final Integer IS_NO = 0;
	public static final int FLAG_DELETED = 1;
	public static final int FLAG_EXISTS = 2;
	public static final String DELFLAG = "delFlag";
	public static Integer IS_ON = 1;
	public static final Integer IS_OFF = 0;

	public abstract Uri CONTEN_URI();

	public static final String _ID = "_id";

	public Model(Cursor c) {
		super();
	}

	public Model() {
		super();
	}

	public abstract Model getInstance(Cursor c);

	@Override
	public List<Model> getList(JSONArray ja) throws JSONException {
		return null;
	}

	public abstract ContentValues toContentValues();

	public Long get_id() {
		return Long.valueOf(0);
	}

	@Override
	public void setDelFlag(Integer delFlag) {
	}

	@Override
	public Integer getDelFlag() {
		return 0;
	}
}
