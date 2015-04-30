package com.david.pda.sqlite.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.privider.ModelProvider;

public abstract class CycleDetails extends Model implements
		Comparable<CycleDetails> {
	public static final int DETAIL_FOR_PLAN = 1;
	public static final int DETAIL_FOR_PRINCIPLE = 2;
	public static final int DETAIL_FOR_ALARM = 3;

	protected static final long serialVersionUID = 36361417733489344L;
	public static final String TABLE_NAME = "cycledetails";
	public final static Uri CONTENT_URI = Uri.parse("content://"
			+ ModelProvider.AUTHORITY + "/" + TABLE_NAME);
	public final static String _ID = "_id";
	public final static String CYLEFOR = "cycleFor";
	public final static String STARTTIME = "startTime";
	public final static String ENDTIME = "endTime";
	public final static String ISTIP = "isTip";
	public final static String WEATHERSENSITIVITY = "weatherSensitivity";
	public final static String ISAHEAD = "isAhead";
	public final static String AHEADTIME = "aheadTime";
	public final static String DISCRIPTION = "discription";
	protected Model father;

	public Model getFather() {
		return father;
	}

	public void setFather(Model father) {
		this.father = father;
	}

	public CycleDetails() {
	}

	public abstract CycleDetails clone();

	public static String CREATE_TABLE() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(TABLE_NAME).append("(");
		sb.append(_ID).append(" INTEGER primary key autoincrement,");
		sb.append(CYLEFOR).append(" INTEGER,");
		sb.append(ISTIP).append(" INT,");
		sb.append(ISAHEAD).append(" INT,");
		sb.append(AHEADTIME).append(" TEXT,");
		sb.append(DISCRIPTION).append(" TEXT,");
		sb.append(WEATHERSENSITIVITY).append(" TEXT,");
		sb.append(STARTTIME).append(" LONG,");
		sb.append(ENDTIME).append(" LONG,");
		sb.append(DELFLAG).append(" INT)");
		return sb.toString();
	}

	protected Long _id;
	protected Long cycleFor;
	protected Integer isTip;
	protected Integer isAhead;
	protected Long aheadTime;
	protected String discription;
	protected String weatherSensitivity;
	protected Long startTime;
	protected Long endTime;
	protected Integer delFlag;

	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		if (_id != null)
			cv.put(_ID, _id);
		if (cycleFor != null)
			cv.put(CYLEFOR, cycleFor);
		if (isTip != null) {
			cv.put(ISTIP, isTip);
		}
		if (isAhead != null)
			cv.put(ISAHEAD, isAhead);
		if (aheadTime != null) {
			cv.put(AHEADTIME, aheadTime);
		}
		if (weatherSensitivity != null) {
			cv.put(WEATHERSENSITIVITY, weatherSensitivity);
		}
		if (startTime != null) {
			cv.put(STARTTIME, startTime);
		}
		if (endTime != null) {
			cv.put(ENDTIME, endTime);
		}
		if (!TextUtils.isEmpty(discription))
			cv.put(DISCRIPTION, discription);
		if (delFlag != null)
			cv.put(DELFLAG, delFlag);
		return cv;
	}

	public CycleDetails(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.isAhead = c.getInt(c.getColumnIndex(ISAHEAD));
		this.cycleFor = c.getLong(c.getColumnIndex(CYLEFOR));
		this.isTip = c.getInt(c.getColumnIndex(ISTIP));
		this.weatherSensitivity = c.getString(c
				.getColumnIndex(WEATHERSENSITIVITY));
		this.aheadTime = c.getLong(c.getColumnIndex(AHEADTIME));
		this.startTime = c.getLong(c.getColumnIndex(STARTTIME));
		this.endTime = c.getLong(c.getColumnIndex(ENDTIME));
		this.discription = c.getString(c.getColumnIndex(DISCRIPTION));
		this.delFlag = c.getInt(c.getColumnIndex(DELFLAG));
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public Long getCycleFor() {
		return cycleFor;
	}

	public void setCycleFor(Long cycleFor) {
		this.cycleFor = cycleFor;
	}

	public Integer getIsTip() {
		return isTip;
	}

	public void setIsTip(Integer isTip) {
		this.isTip = isTip;
	}

	public Integer getIsAhead() {
		return isAhead;
	}

	public void setIsAhead(Integer isAhead) {
		this.isAhead = isAhead;
	}

	public Long getAheadTime() {
		return aheadTime;
	}

	public void setAheadTime(Long aheadTime) {
		this.aheadTime = aheadTime;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getWeatherSensitivity() {
		return weatherSensitivity;
	}

	public void setWeatherSensitivity(String weatherSensitivity) {
		this.weatherSensitivity = weatherSensitivity;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
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
	public int compareTo(CycleDetails other) {
		if (other != null) {
			CycleDetails c2 = other;
			if (this.getStartTime() < c2.getStartTime()) {
				return -1;
			} else
				return 1;
		}
		return 0;
	}
}
