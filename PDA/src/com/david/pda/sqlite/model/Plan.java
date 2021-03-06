package com.david.pda.sqlite.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.privider.ModelProvider;

public class Plan extends Model {
	private static final long serialVersionUID = -2583549883517195823L;
	public static final String TABLE_NAME = "plan";
	public final static Uri CONTENT_URI = Uri.parse("content://"
			+ ModelProvider.AUTHORITY + "/" + TABLE_NAME);
	public final static String _ID = "_id";
	public final static String TITLE = "title";
	public final static String URGENCYIMPORTANT = "urgencyimportant";
	public final static String CREATETIME = "createTime";
	public final static String TARGET = "target";
	public final static String ISTIP = "isTip";
	public final static String CYCLETYPE = "cycleType";
	public final static String WEATHERSENSITIVITY = "weatherSensitivity";
	public final static String TOTALTIME = "totalTime";
	public final static String TOTALUSED = "totalUsed";
	public final static String STARTTIME = "startTime";
	public final static String ENDTIME = "endTime";
	public final static String DOAFTERSUCCESS = "doAfterSuccess";
	public final static String HASHTIPCYCLE = "hashTipCycle";
	private CycleDetailsForPlan detail;
	private CycleType cycleTypeObj;
	private Long hashTipCycle;

	public Long getHashTipCycle() {
		return hashTipCycle;
	}

	public void setHashTipCycle(Long hashTipCycle) {
		this.hashTipCycle = hashTipCycle;
	}

	public CycleType getCycleTypeObj() {
		return cycleTypeObj;
	}

	public void setCycleTypeObj(CycleType cycleTypeObj) {
		this.cycleTypeObj = cycleTypeObj;
	}

	public void setDetail(CycleDetailsForPlan detail) {
		this.detail = detail;
	}

	public CycleDetailsForPlan getDetail() {
		return this.detail;
	}

	public static int urgency(int urgencyimportant) {// 00,01,10,11
		return urgencyimportant >> 1;
	}

	public static int important(int urgencyimportant) {
		return urgencyimportant % 2;
	}

	public static String CREATE_TABLE() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(TABLE_NAME).append("(");
		sb.append(_ID).append(" INTEGER primary key autoincrement,");
		sb.append(TITLE).append(" TEXT,");
		sb.append(URGENCYIMPORTANT).append(" INT,");
		sb.append(CREATETIME).append(" TEXT,");
		sb.append(TARGET).append(" INT,");
		sb.append(ISTIP).append(" INT,");
		sb.append(WEATHERSENSITIVITY).append(" INT,");
		sb.append(TOTALTIME).append(" TEXT,");
		sb.append(TOTALUSED).append(" TEXT,");
		sb.append(CYCLETYPE).append(" INT,");
		sb.append(STARTTIME).append(" TEXT,");
		sb.append(ENDTIME).append(" TEXT,");
		sb.append(DOAFTERSUCCESS).append(" TEXT,");
		sb.append(HASHTIPCYCLE).append(" TEXT,");
		sb.append(DELFLAG).append(" INT)");
		return sb.toString();
	}

	private Long _id;
	private String title;
	private Integer urgencyimportant;
	private Long createTime;
	private Integer target;
	private Integer isTip;
	private String weatherSensitivity;
	private Long totalTime;
	private Long totalUsed;
	private Long cycleType;
	private Long startTime;
	private Long endTime;
	private String doAfterSuccess;
	private Integer delFlag;

	@Override
	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		if (_id != null)
			cv.put(_ID, _id);
		if (!TextUtils.isEmpty(title))
			cv.put(TITLE, title);
		if (urgencyimportant != null)
			cv.put(URGENCYIMPORTANT, urgencyimportant);
		if (createTime != null) {
			cv.put(CREATETIME, createTime);
		}
		if (target != null) {
			cv.put(TARGET, target);
		}
		if (isTip != null) {
			cv.put(ISTIP, isTip);
		}
		if (weatherSensitivity != null) {
			cv.put(WEATHERSENSITIVITY, weatherSensitivity);
		}
		if (totalTime != null) {
			cv.put(TOTALTIME, totalTime);
		}
		if (totalUsed != null) {
			cv.put(TOTALUSED, totalUsed);
		}
		if (cycleType != null) {
			cv.put(CYCLETYPE, cycleType);
		}
		if (startTime != null) {
			cv.put(STARTTIME, startTime);
		}
		if (endTime != null) {
			cv.put(ENDTIME, endTime);
		}
		if (hashTipCycle != null) {
			cv.put(HASHTIPCYCLE, hashTipCycle);
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
		this.target = c.getInt(c.getColumnIndex(TARGET));
		this.isTip = c.getInt(c.getColumnIndex(ISTIP));
		this.weatherSensitivity = c.getString(c
				.getColumnIndex(WEATHERSENSITIVITY));
		this.totalTime = c.getLong(c.getColumnIndex(TOTALTIME));
		this.totalUsed = c.getLong(c.getColumnIndex(TOTALUSED));
		this.startTime = c.getLong(c.getColumnIndex(STARTTIME));
		this.cycleType = c.getLong(c.getColumnIndex(CYCLETYPE));
		this.endTime = c.getLong(c.getColumnIndex(ENDTIME));
		this.doAfterSuccess = c.getString(c.getColumnIndex(DOAFTERSUCCESS));
		this.delFlag = c.getInt(c.getColumnIndex(DELFLAG));
		this.hashTipCycle = c.getLong(c.getColumnIndex(HASHTIPCYCLE));
	}

	public Plan() {
		super();
	}

	@Override
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

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

	public Integer getIsTip() {
		return isTip;
	}

	public void setIsTip(Integer isTip) {
		this.isTip = isTip;
	}

	public String getWeatherSensitivity() {
		return weatherSensitivity;
	}

	public void setWeatherSensitivity(String weatherSensitivity) {
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
		return new Plan(c);
	}

	public Long getCycleType() {
		return cycleType;
	}

	public void setCycleType(Long cycleType) {
		this.cycleType = cycleType;
	}
}
