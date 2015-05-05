package com.david.pda.sqlite.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.privider.ModelProvider;

public class Alarm extends Model {
	private static final long serialVersionUID = 1L;
	public final static String TABLE_NAME = "alarm";
	public final static Uri CONTENT_URI = Uri.parse("content://"
			+ ModelProvider.AUTHORITY + "/" + TABLE_NAME);
	public final static String _ID = "_id";
	public final static String TITLE = "title";
	public final static String REMARKS = "remarks";
	public final static String ISON = "ISON";
	public final static String CYCLETYPE = "cycleType";

	public static String CREATE_TABLE() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(TABLE_NAME).append("(");
		sb.append(_ID);
		sb.append(" INTEGER primary key autoincrement,");
		sb.append(TITLE).append(" TEXT,");
		sb.append(REMARKS).append(" TEXT,");
		sb.append(ISON).append(" INTEGER,");
		sb.append(CYCLETYPE).append(" INTEGER,");
		sb.append(DELFLAG).append(" INTEGER)");
		return sb.toString();
	}

	private Long _id;
	private String title;
	private String remarks;
	private Integer isOn;
	private Long cycleType;
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
		if (ISON != null) {
			cv.put(ISON, isOn);
		}
		if (cycleType != null) {
			cv.put(CYCLETYPE, cycleType);
		}
		return cv;
	}

	public Alarm() {
	};

	public Alarm(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.title = c.getString(c.getColumnIndex(TITLE));
		this.remarks = c.getString(c.getColumnIndex(REMARKS));
		this.isOn = c.getInt(c.getColumnIndex(ISON));
		this.cycleType = c.getLong(c.getColumnIndex(CYCLETYPE));
		this.delFlag = c.getInt(c.getColumnIndex(DELFLAG));
	}

	public Alarm(Bundle b) {
		this._id = b.getLong(_ID);
		this.title = b.getString(TITLE);
		this.remarks = b.getString(REMARKS);
		this.isOn = b.getInt(ISON);
		this.cycleType = b.getLong(CYCLETYPE);
		this.delFlag = b.getInt(DELFLAG);
	}

	public Bundle toBundle() {
		Bundle b = new Bundle();
		b.putLong(_ID, _id);
		b.putString(TITLE, title);
		b.putString(REMARKS, remarks);
		b.putInt(ISON, isOn);
		b.putLong(CYCLETYPE, cycleType);
		b.putInt(DELFLAG, delFlag);
		return b;
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

	public Integer getIsOn() {
		return isOn;
	}

	public void setIsOn(Integer isOn) {
		this.isOn = isOn;
	}

	public Long getCycleType() {
		return cycleType;
	}

	public void setCycleType(Long cycleType) {
		this.cycleType = cycleType;
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

	public List<CycleDetailsForAlarm> getDetails() {
		return details;
	}

	public void setDetails(List<CycleDetailsForAlarm> details) {
		this.details = details;
	}

	public CycleType getCycleTypeObj() {
		return cycleTypeObj;
	}

	public void setCycleTypeObj(CycleType cycleTypeObj) {
		this.cycleTypeObj = cycleTypeObj;
	}

	private List<CycleDetailsForAlarm> details;
	private CycleType cycleTypeObj;

	public void addDetail(CycleDetailsForAlarm c) {
		if (details == null) {
			details = new ArrayList<CycleDetailsForAlarm>();
		}
		details.add(c);
	}
}
