package com.david.pda.sqlite.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.david.pda.sqlite.model.base.Model;
import com.david.pda.sqlite.privider.ModelProvider;

public class Memo extends Model {
	public static final String TABLE_NAME = "memo";
	public final static Uri CONTENT_URI = Uri.parse("content://"
			+ ModelProvider.AUTHORITY + "/" + TABLE_NAME);
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

	public static String CREATE_TABLE() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append(TABLE_NAME).append("(");
		sb.append(_ID).append(" INTEGER primary key autoincrement,");
		sb.append(TITLE).append(" TEXT,");
		sb.append(CONTENT).append(" TEXT,");
		sb.append(FLAG).append(" INT,");
		sb.append(CREATETIME).append(" LONG,");
		sb.append(DELFLAG).append(" INT)");
		return sb.toString();
	}

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

	public Memo() {
	}

	public Memo(String title, String content, Integer flag, Long createTime,
			Integer delFlag) {
		this.title = title;
		this.content = content;
		this.flag = flag;
		this.createTime = createTime;
		this.delFlag = delFlag;
	}

	public Memo(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(_ID));
		this.title = c.getString(c.getColumnIndex(TITLE));
		this.createTime = c.getLong(c.getColumnIndex(CREATETIME));
		this.content = c.getString(c.getColumnIndex(CONTENT));
		this.flag = c.getInt(c.getColumnIndex(FLAG));
		this.delFlag = c.getInt(c.getColumnIndex(DELFLAG));
	}

	public Bundle toBundle() {
		Bundle b = new Bundle();
		b.putLong(_ID, this._id);
		b.putString(TITLE, this.title);
		b.putLong(CREATETIME, this.createTime);
		b.putString(CONTENT, this.content);
		b.putInt(FLAG, this.flag);
		b.putInt(DELFLAG, this.delFlag);
		return b;
	}

	public Memo(Bundle b) {
		super();
		this._id = b.getLong(_ID);
		this.title = b.getString(TITLE);
		this.createTime = b.getLong(CREATETIME);
		this.content = b.getString(CONTENT);
		this.flag = b.getInt(FLAG);
		this.delFlag = b.getInt(DELFLAG);
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

	@Override
	public Uri CONTEN_URI() {
		return CONTENT_URI;
	}

	@Override
	public Model getInstance(Cursor c) {
		return new Memo(c);
	}
}
