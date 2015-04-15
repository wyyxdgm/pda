package com.david.pda.sqlite.model;

import com.david.pda.sqlite.model.base.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class Memo extends Model {
	public static final String TABLE_NAME = "memo";
	public final static Uri CONTENT_URI = Uri.parse("content://com.david.pda.model.memo");
	public final static String _ID = "_id";
	public final static String TITLE = "title";
	public final static String CONTENT = "content";
	public final static String FLAG = "flag";
	public final static String CREATETIME = "createTime";

	private Long _id;
	private String title;
	private String content;
	private Integer flag;
	private Long createTime;
	private Integer delFlag;

	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		if (_id != null)
			cv.put(_ID, _id);
		if (delFlag != null)
			cv.put(_ID, _id);
		if (!TextUtils.isEmpty(title))
			cv.put(TITLE, title);
		if (!TextUtils.isEmpty(content))
			cv.put(CONTENT, content);
		if (flag != null) {
			cv.put(FLAG, flag);
		}
		if (createTime != null) {
			cv.put(CREATETIME, createTime);
		}
		return cv;
	}

	public Memo(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.title = c.getString(c.getColumnIndex(TITLE));
		this.createTime = c.getLong(c.getColumnIndex(CREATETIME));
		this.content = c.getString(c.getColumnIndex(CONTENT));
		this.flag = c.getInt(c.getColumnIndex(FLAG));
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

}
