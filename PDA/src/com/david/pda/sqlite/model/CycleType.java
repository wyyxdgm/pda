package com.david.pda.sqlite.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class CycleType extends Model {
	public final static Uri CONTENT_URI = Uri.parse("cycleType");
	public final static String _ID = "_id";
	public final static String NAME = "name";
	private Long _id;
	private String name;
	private Integer delFlag;

	public CycleType(JSONObject jo) throws JSONException {
		if (jo.has("_id"))
			this._id = jo.getLong("_id");
		if (jo.has("delFlag")) {
			this.setDelFlag(jo.getInt(DELFLAG));
		}
		if (jo.has("name"))
			this.name = jo.getString("name");
	}

	public static List<CycleType> getList(JSONArray ja) throws JSONException {
		List<CycleType> li = new ArrayList<CycleType>();
		for (Integer i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			li.add(new CycleType(jo));
		}
		return li;
	}

	public CycleType(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.name = c.getString(c.getColumnIndex(NAME));
		this.delFlag = c.getInt(c.getColumnIndex(DELFLAG));
	}

	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		if (_id > 0)
			cv.put(_ID, _id);
		if (!TextUtils.isEmpty(name))
			cv.put(NAME, name);
		cv.put(DELFLAG, delFlag);
		return cv;
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jo = new JSONObject();
		if (_id > 0)
			jo.put(_ID, _id);
		if (!TextUtils.isEmpty(name))
			jo.put(NAME, name);
		jo.put(DELFLAG, delFlag);
		return jo;
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

}
