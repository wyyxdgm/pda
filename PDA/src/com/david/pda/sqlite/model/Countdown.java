package com.david.pda.sqlite.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.privider.ModelProvider;

public class Countdown extends Model {
	private static final long serialVersionUID = 169597650930460847L;
	public final static String TABLE_NAME = "countdown";
	public final static Uri CONTENT_URI = Uri.parse("content://"
			+ ModelProvider.AUTHORITY + "/" + TABLE_NAME);
	public final static String _ID = "_id";
	public final static String TITLE = "title";
	public final static String REMARKS = "remarks";
	public final static String ENDTIME = "endTime";
	public final static String ISON = "isOn";

	public Integer getIsOn() {
		return this.isOn;
	}

	public void setIsOn(Integer isOn) {
		this.isOn = isOn;
	}

	public static String CREATE_TABLE() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(TABLE_NAME).append("(");
		sb.append(_ID).append(" INTEGER primary key autoincrement,");
		sb.append(TITLE).append(" TEXT,");
		sb.append(REMARKS).append(" TEXT,");
		sb.append(ENDTIME).append(" TEXT,");
		sb.append(ISON).append(" INT,");
		sb.append(DELFLAG).append(" INT)");
		return sb.toString();
	}

	private Long _id;
	private String title;
	private String remarks;
	private Long endTime;
	private Integer delFlag=Model.FLAG_EXISTS;
	private Integer isOn=Model.IS_ON;

	public Countdown(Bundle b) {
		if (b.containsKey(_ID))
			this._id = b.getLong(_ID);
		if (b.containsKey(TITLE))
			this.title = b.getString(TITLE);
		if (b.containsKey(REMARKS))
			this.remarks = b.getString(REMARKS);
		if (b.containsKey(ENDTIME))
			this.endTime = b.getLong(ENDTIME);
		if (b.containsKey(DELFLAG))
			this.delFlag = b.getInt(DELFLAG);
		if (b.containsKey(ISON))
			this.isOn = b.getInt(ISON);
	}

	public Bundle toBundle() {
		Bundle b = new Bundle();
		if (_id != null)
			b.putLong(_ID, _id);
		if (title != null)
			b.putString(TITLE, title);
		if (remarks != null)
			b.putString(REMARKS, remarks);
		if (endTime != null)
			b.putLong(ENDTIME, endTime);
		if (delFlag != null)
			b.putInt(DELFLAG, delFlag);
		if (isOn != null)
			b.putInt(ISON, isOn);
		return b;
	}

	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		if (_id != null)
			cv.put(_ID, _id);
		if (delFlag != null)
			cv.put(DELFLAG, delFlag);
		if (!TextUtils.isEmpty(title))
			cv.put(TITLE, title);
		if (!TextUtils.isEmpty(remarks))
			cv.put(REMARKS, remarks);
		if (endTime != null) {
			cv.put(ENDTIME, endTime);
		}
		if (isOn != null) {
			cv.put(ISON, isOn);
		}
		return cv;
	}

	public Countdown(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.title = c.getString(c.getColumnIndex(TITLE));
		this.remarks = c.getString(c.getColumnIndex(REMARKS));
		this.endTime = c.getLong(c.getColumnIndex(ENDTIME));
		this.delFlag = c.getInt(c.getColumnIndex(DELFLAG));
		this.isOn = c.getInt(c.getColumnIndex(ISON));
	}

	public Countdown() {
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public Uri CONTEN_URI() {
		return CONTENT_URI;
	}

	@Override
	public Model getInstance(Cursor c) {
		return new Countdown(c);
	}

}
