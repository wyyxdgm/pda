package com.david.pda.sqlite.model;

import com.david.pda.sqlite.model.base.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class Plan extends Model {
	public static final String TABLE_NAME = "plan";
	public final static Uri CONTENT_URI = Uri
			.parse("content://com.david.pda.model.plan");
	public final static String _ID = "_id";
	public final static String TITLE = "title";
	public final static String URGENCYIMPORTANT = "urgencyimportant";
	public final static String CREATETIME = "createTime";
	public final static String FIRSTTYPE = "firstType";
	public final static String SECONDTYPE = "secondType";
	public final static String WEATHERSENSITIVITY = "weatherSensitivity";
	public final static String TOTALTIME = "totalTime";
	public final static String TOTALUSED = "totalUsed";
	public final static String STARTTIME = "startTime";
	public final static String ENDTIME = "endTime";
	public final static String DOAFTERSUCCESS = "doAfterSuccess";

	private Long _id;
	private String title;
	private Integer urgencyimportant;
	private Long createTime;
	private Integer firstType;
	private Integer secondType;
	private Integer weatherSensitivity;
	private Long totalTime;
	private Long totalUsed;
	private Long startTime;
	private Long endTime;
	private String doAfterSuccess;
	private Integer delFlag;

	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		if (_id > 0)
			cv.put(_ID, _id);
		if (!TextUtils.isEmpty(title))
			cv.put(TITLE, title);
		if (urgencyimportant > 0)
			cv.put(URGENCYIMPORTANT, urgencyimportant);
		if (createTime > 0) {
			cv.put(CREATETIME, createTime);
		}
		if (firstType > 0) {
			cv.put(FIRSTTYPE, firstType);
		}
		if (secondType > 0) {
			cv.put(SECONDTYPE, secondType);
		}
		if (weatherSensitivity > 0) {
			cv.put(WEATHERSENSITIVITY, weatherSensitivity);
		}
		if (totalTime > 0) {
			cv.put(TOTALTIME, totalTime);
		}
		if (totalUsed > 0) {
			cv.put(TOTALUSED, totalUsed);
		}
		if (startTime > 0) {
			cv.put(STARTTIME, startTime);
		}
		if (endTime > 0) {
			cv.put(ENDTIME, endTime);
		}
		if (!TextUtils.isEmpty(doAfterSuccess))
			cv.put(DOAFTERSUCCESS, doAfterSuccess);
		cv.put(DELFLAG, delFlag);
		return cv;
	}

	public Plan(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.title = c.getString(c.getColumnIndex(TITLE));
		this.urgencyimportant = c.getInt(c.getColumnIndex(URGENCYIMPORTANT));
		this.createTime = c.getLong(c.getColumnIndex(CREATETIME));
		this.firstType = c.getInt(c.getColumnIndex(FIRSTTYPE));
		this.secondType = c.getInt(c.getColumnIndex(SECONDTYPE));
		this.weatherSensitivity = c
				.getInt(c.getColumnIndex(WEATHERSENSITIVITY));
		this.totalTime = c.getLong(c.getColumnIndex(TOTALTIME));
		this.totalUsed = c.getLong(c.getColumnIndex(TOTALUSED));
		this.startTime = c.getLong(c.getColumnIndex(STARTTIME));
		this.endTime = c.getLong(c.getColumnIndex(ENDTIME));
		this.doAfterSuccess = c.getString(c.getColumnIndex(DOAFTERSUCCESS));
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

	public Integer getUrgencyimportant() {
		return urgencyimportant;
	}

	public void setUrgencyimportant(Integer urgencyimportant) {
		this.urgencyimportant = urgencyimportant;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getFirstType() {
		return firstType;
	}

	public void setFirstType(Integer firstType) {
		this.firstType = firstType;
	}

	public Integer getSecondType() {
		return secondType;
	}

	public void setSecondType(Integer secondType) {
		this.secondType = secondType;
	}

	public Integer getWeatherSensitivity() {
		return weatherSensitivity;
	}

	public void setWeatherSensitivity(Integer weatherSensitivity) {
		this.weatherSensitivity = weatherSensitivity;
	}

	public Long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}

	public Long getTotalUsed() {
		return totalUsed;
	}

	public void setTotalUsed(Long totalUsed) {
		this.totalUsed = totalUsed;
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

	public String getDoAfterSuccess() {
		return doAfterSuccess;
	}

	public void setDoAfterSuccess(String doAfterSuccess) {
		this.doAfterSuccess = doAfterSuccess;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

}
