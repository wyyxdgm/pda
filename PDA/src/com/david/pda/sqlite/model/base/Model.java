package com.david.pda.sqlite.model.base;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public abstract class Model implements IModel<Model> {
	public static final Uri CONTENT_URI = Uri
			.parse("content://com.david.pda.model.model");

	public static final String _ID = "_id";

	public Model(Cursor c) {
		super();
	}

	public Model() {
		super();
	}

	@Override
	public Model getInstance(Cursor c) {
		return null;
	}

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
