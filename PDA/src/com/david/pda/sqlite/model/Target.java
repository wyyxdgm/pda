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

import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.privider.ModelProvider;

public class Target extends Model {
	private static final long serialVersionUID = 117894922636906754L;
	public static final String TABLE_NAME = "target";
	public final static Uri CONTENT_URI = Uri.parse("content://"
			+ ModelProvider.AUTHORITY + "/" + TABLE_NAME);
	public final static String _ID = "_id";
	public final static String NAME = "name";
	public final static String SCALE = "scale";
	private Long _id;
	private String name;
	private Integer scale;
	private Integer delFlag;

	public Target() {
	}

	public Target(String name, Integer scale) {
		this.name = name;
		this.scale = scale;
		this.delFlag = Model.FLAG_EXISTS;
	}

	public Target(Long _id, String name, Integer scale) {
		this._id = _id;
		this.name = name;
		this.scale = scale;
		this.delFlag = Model.FLAG_EXISTS;
	}

	@Override
	public Target clone() {
		Target t = new Target();
		t.set_id(this._id);
		t.setDelFlag(this.delFlag);
		t.setName(this.name);
		t.setScale(this.scale);
		return t;
	}

	public static String CREATE_TABLE() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(TABLE_NAME).append("(");
		sb.append(_ID).append(" INTEGER primary key autoincrement,");
		sb.append(NAME).append(" TEXT,");
		sb.append(SCALE).append(" INT,");
		sb.append(DELFLAG).append(" INT)");
		return sb.toString();
	}

	public Target(JSONObject jo) throws JSONException {
		if (jo.has("_id"))
			this._id = jo.getLong("_id");
		if (jo.has("delFlag")) {
			this.setDelFlag(jo.getInt(DELFLAG));
		}
		if (jo.has("name"))
			this.name = jo.getString("name");
		if (jo.has("scale"))
			this.scale = jo.getInt("scale");
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getList(JSONArray ja) throws JSONException {
		List<Target> li = new ArrayList<Target>();
		for (Integer i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			li.add(new Target(jo));
		}
		return li;
	}

	public Target(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.name = c.getString(c.getColumnIndex(NAME));
		this.scale = c.getInt(c.getColumnIndex(SCALE));
		this.delFlag = c.getInt(c.getColumnIndex(DELFLAG));
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		if (_id != null)
			cv.put(_ID, _id);
		if (!TextUtils.isEmpty(name))
			cv.put(NAME, name);
		if (scale != null)
			cv.put(SCALE, scale);
		if (delFlag != null)
			cv.put(DELFLAG, delFlag);
		return cv;
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jo = new JSONObject();
		if (_id > 0)
			jo.put(_ID, _id);
		if (!TextUtils.isEmpty(name))
			jo.put(NAME, name);
		if (scale > 0)
			jo.put(SCALE, scale);
		jo.put(DELFLAG, delFlag);
		return jo;
	}

	@Override
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

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	@Override
	public Integer getDelFlag() {
		return delFlag;
	}

	@Override
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public Uri CONTEN_URI() {
		return CONTENT_URI;
	}

	@Override
	public Model getInstance(Cursor c) {
		return new Target(c);
	}
}
