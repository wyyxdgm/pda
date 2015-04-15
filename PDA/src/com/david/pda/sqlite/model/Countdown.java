package com.david.pda.sqlite.model;

import com.david.pda.sqlite.model.base.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class Countdown extends Model {
	public final static String TABLE_NAME = "countdown";
	public final static Uri CONTENT_URI = Uri
			.parse("content://com.david.pda.model.countdown");
	public final static String _ID = "_id";
	public final static String TITLE = "title";
	public final static String REMARKS = "remarks";
	public final static String ENDTIME = "endTime";

	private Long _id;
	private String title;
	private String remarks;
	private Long endTime;
	private Integer delFlag;

	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		if (_id != null)
			cv.put(_ID, _id);
		if (delFlag != null)
			cv.put(_ID, _id);
		if (!TextUtils.isEmpty(title))
			cv.put(TITLE, title);
		if (!TextUtils.isEmpty(remarks))
			cv.put(REMARKS, remarks);
		if (endTime != null) {
			cv.put(ENDTIME, endTime);
		}
		return cv;
	}

	public Countdown(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.title = c.getString(c.getColumnIndex(TITLE));
		this.remarks = c.getString(c.getColumnIndex(REMARKS));
		this.endTime = c.getLong(c.getColumnIndex(ENDTIME));
		this.delFlag = c.getInt(c.getColumnIndex(DELFLAG));
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

}
