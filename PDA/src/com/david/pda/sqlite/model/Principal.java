package com.david.pda.sqlite.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.david.pda.sqlite.model.base.Model;

public class Principal extends Model {
	public static final String TABLE_NAME = "principal";
	public final static Uri CONTENT_URI = Uri
			.parse("content://com.david.pda.model.principal");
	public final static String _ID = "_id";
	public final static String TITLE = "title";
	public final static String REMARKS = "remarks";
	public final static String CYCLETYPE = "cycleType";
	public final static String CYCLECONTENT = "cycleContent";

	private Long _id;
	private String title;
	private String remarks;
	private Long cycleType;
	private Long cycleContent;
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
		if (cycleType != null) {
			cv.put(CYCLETYPE, cycleType);
		}
		if (cycleContent != null) {
			cv.put(CYCLECONTENT, cycleContent);
		}
		return cv;
	}

	public Principal(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.title = c.getString(c.getColumnIndex(TITLE));
		this.remarks = c.getString(c.getColumnIndex(REMARKS));
		this.cycleType = c.getLong(c.getColumnIndex(CYCLETYPE));
		this.cycleContent = c.getLong(c.getColumnIndex(CYCLECONTENT));
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

	public Long getCycleType() {
		return cycleType;
	}

	public void setCycleType(Long cycleType) {
		this.cycleType = cycleType;
	}

	public Long getCycleContent() {
		return cycleContent;
	}

	public void setCycleContent(Long cycleContent) {
		this.cycleContent = cycleContent;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

}
