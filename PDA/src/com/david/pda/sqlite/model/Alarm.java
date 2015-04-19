package com.david.pda.sqlite.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.privider.ModelProvider;

public class Alarm extends Model {
	public final static String TABLE_NAME = "alarm";
	public final static Uri CONTENT_URI = Uri.parse("content://"
			+ ModelProvider.AUTHORITY + "/" + TABLE_NAME);
	public final static String _ID = "_id";
	public final static String TITLE = "title";
	public final static String REMARKS = "remarks";
	public final static String ENDTIME = "endTime";
	public final static String CYCLETYPE = "cycleType";
	public final static String CYCLECONTENT = "cycleContent";

	public static String CREATE_TABLE() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(TABLE_NAME).append("(");
		sb.append(_ID);
		sb.append(" INTEGER primary key autoincrement,");
		sb.append(TITLE).append(" TEXT,");
		sb.append(REMARKS).append(" TEXT,");
		sb.append(ENDTIME).append(" TEXT,");
		sb.append(CYCLETYPE).append(" INT,");
		sb.append(CYCLECONTENT).append(" TEXT,");
		sb.append(DELFLAG).append(" INT)");
		return sb.toString();
	}

	private Long _id;
	private String title;
	private String remarks;
	private Long endTime;
	private Long cycleType;
	private Long cycleContent;
	private Integer delFlag;

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
		if (cycleType != null) {
			cv.put(CYCLETYPE, cycleType);
		}
		if (cycleContent != null) {
			cv.put(CYCLECONTENT, cycleContent);
		}
		return cv;
	}

	public Alarm() {
	};

	public Alarm(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.title = c.getString(c.getColumnIndex(TITLE));
		this.remarks = c.getString(c.getColumnIndex(REMARKS));
		this.endTime = c.getLong(c.getColumnIndex(ENDTIME));
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

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
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

	@Override
	public Uri CONTEN_URI() {
		return Alarm.CONTENT_URI;
	}

	@Override
	public Model getInstance(Cursor c) {
		return new Alarm(c);
	}
}
