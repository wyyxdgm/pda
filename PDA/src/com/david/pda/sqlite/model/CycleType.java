package com.david.pda.sqlite.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.privider.ModelProvider;

public class CycleType extends Model {
	private static final long serialVersionUID = -9000521813963145217L;
	public static final String TABLE_NAME = "cycletype";
	public final static Uri CONTENT_URI = Uri.parse("content://"
			+ ModelProvider.AUTHORITY + "/" + TABLE_NAME);
	public final static String _ID = "_id";
	public final static String NAME = "name";
	public final static String CYCLELENGTH = "cycleLength";
	private Long _id;
	private String name;
	private Integer delFlag;
	private Long cycleLength;

	public CycleType() {
		super();
	}

	public static String CREATE_TABLE() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(TABLE_NAME).append("(");
		sb.append(_ID).append(" INTEGER primary key autoincrement,");
		sb.append(NAME).append(" TEXT,");
		sb.append(CYCLELENGTH).append(" TEXT,");
		sb.append(DELFLAG).append(" INT)");
		return sb.toString();
	}

	public CycleType(JSONObject jo) throws JSONException {
		if (jo.has(_ID))
			this._id = jo.getLong(_ID);
		if (jo.has(DELFLAG)) {
			this.setDelFlag(jo.getInt(DELFLAG));
		}
		if (jo.has(CYCLELENGTH)) {
			this.setCycleLength(jo.getLong(CYCLELENGTH));
		}
		if (jo.has(NAME))
			this.name = jo.getString("name");
	}

	public CycleType(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.name = c.getString(c.getColumnIndex(NAME));
		this.cycleLength = c.getLong(c.getColumnIndex(CYCLELENGTH));
		this.delFlag = c.getInt(c.getColumnIndex(DELFLAG));
	}

	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		if (_id != null)
			cv.put(_ID, _id);
		if (!TextUtils.isEmpty(name))
			cv.put(NAME, name);
		if (delFlag != null)
			cv.put(DELFLAG, delFlag);
		if (cycleLength != null)
			cv.put(CYCLELENGTH, cycleLength);
		return cv;
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jo = new JSONObject();
		if (_id != null)
			jo.put(_ID, _id);
		if (cycleLength != null)
			jo.put(CYCLELENGTH, cycleLength);
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

	public Long getCycleLength() {
		return cycleLength;
	}

	public void setCycleLength(Long cycleLength) {
		this.cycleLength = cycleLength;
	}

	@Override
	public Uri CONTEN_URI() {
		return CONTENT_URI;
	}

	@Override
	public Model getInstance(Cursor c) {
		return new CycleType(c);
	}
}
