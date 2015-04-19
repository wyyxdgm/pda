package com.david.pda.sqlite.model;

import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.privider.ModelProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class CycleContent extends Model {
	private static final long serialVersionUID = 36361417733489344L;
	public static final String TABLE_NAME = "cyclecontent";
	public final static Uri CONTENT_URI = Uri.parse("content://"
			+ ModelProvider.AUTHORITY + "/" + TABLE_NAME);
	public final static String _ID = "_id";
	public final static String PRINCIPAL = "principal";
	public final static String STARTTIME = "startTime";
	public final static String ENDTIME = "endTime";
	public final static String ISON = "isOn";
	public final static String ISTIP = "isTip";
	public final static String WEATHERSENSITIVITY = "weatherSensitivity";
	public final static String ISAHEAD = "isAhead";
	public final static String AHEADTIME = "aheadTime";
	public final static String DISCRIPTION = "discription";

	public static String CREATE_TABLE() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(TABLE_NAME).append("(");
		sb.append(_ID).append(" INTEGER primary key autoincrement,");
		sb.append(PRINCIPAL).append(" LONG,");
		sb.append(STARTTIME).append(" LONG,");
		sb.append(ENDTIME).append(" LONG,");
		sb.append(ISON).append(" INT,");
		sb.append(ISTIP).append(" INT,");
		sb.append(WEATHERSENSITIVITY).append(" INT,");
		sb.append(ISAHEAD).append(" INT,");
		sb.append(AHEADTIME).append(" LONG,");
		sb.append(DISCRIPTION).append(" TEXT,");
		sb.append(DELFLAG).append(" INT)");
		return sb.toString();
	}

	private Long _id;
	private Long principal;
	private Long startTime;
	private Long endTime;
	private Integer isOn;
	private Integer isTip;
	private Integer weatherSensitivity;
	private Integer isAhead;
	private Long aheadTime;
	private String discription;
	private Integer delFlag;

	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		if (_id > 0)
			cv.put(_ID, _id);
		if (isAhead > 0)
			cv.put(ISAHEAD, isAhead);
		if (principal > 0) {
			cv.put(PRINCIPAL, principal);
		}
		if (isOn > 0) {
			cv.put(ISON, isOn);
		}
		if (isTip > 0) {
			cv.put(ISTIP, isTip);
		}
		if (weatherSensitivity > 0) {
			cv.put(WEATHERSENSITIVITY, weatherSensitivity);
		}
		if (aheadTime > 0) {
			cv.put(AHEADTIME, aheadTime);
		}
		if (startTime > 0) {
			cv.put(STARTTIME, startTime);
		}
		if (endTime > 0) {
			cv.put(ENDTIME, endTime);
		}
		if (!TextUtils.isEmpty(discription))
			cv.put(DISCRIPTION, discription);
		cv.put(DELFLAG, delFlag);
		return cv;
	}

	public CycleContent(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.isAhead = c.getInt(c.getColumnIndex(ISAHEAD));
		this.principal = c.getLong(c.getColumnIndex(PRINCIPAL));
		this.isOn = c.getInt(c.getColumnIndex(ISON));
		this.isTip = c.getInt(c.getColumnIndex(ISTIP));
		this.weatherSensitivity = c
				.getInt(c.getColumnIndex(WEATHERSENSITIVITY));
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

	public Long getPrincipal() {
		return principal;
	}

	public void setPrincipal(Long principal) {
		this.principal = principal;
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

	public Integer getIsOn() {
		return isOn;
	}

	public void setIsOn(Integer isOn) {
		this.isOn = isOn;
	}

	public Integer getIsTip() {
		return isTip;
	}

	public void setIsTip(Integer isTip) {
		this.isTip = isTip;
	}

	public Integer getWeatherSensitivity() {
		return weatherSensitivity;
	}

	public void setWeatherSensitivity(Integer weatherSensitivity) {
		this.weatherSensitivity = weatherSensitivity;
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
		return new CycleContent(c);
	}
}
