package com.david.pda.sqlite.model.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.text.format.DateUtils;

public class Account {
	public static Account EmptyAccount = new Account();
	//
	public long _id;

	//
	public String identity_id;

	// 电话
	public String phone;

	//
	public String password;

	//
	public String paypassword;

	//
	public String token;

	//
	public String nickname;

	// 姓名
	public String realname;

	// 身份证
	public String idcard;

	// 电子邮件
	public String email;

	// 学校
	public String school;

	// 入学年份
	public String year;

	// 院系
	public String department;

	// 专业
	public String major;

	// 学号
	public String student_id;

	// 班级
	public String student_class;

	// 身份证照片
	public String idcard_photo;

	// 学生证照片1
	public String stucard_photo1;

	// 学生证照片2
	public String stucard_photo2;

	// 学生证照片3
	public String stucard_photo3;

	// 验证视频
	public String video;

	// 总额度
	public double limitamount;

	// 剩余额度
	public double spent;

	// 用户状态
	public int status;

	// 邀请人userid
	public long inviteuserid;

	// 用户注册渠道
	public String type;

	// 补充信息
	public String supp;

	// 补充信息2
	public String supp2;

	// 创建时间
	public Date create_time;

	public Account() {
	}

	public Account(JSONObject jo) throws JSONException {
		if (jo.has("_id"))
			this._id = jo.getLong("_id");
		if (jo.has("identity_id"))
			this.identity_id = jo.getString("identity_id");
		if (jo.has("phone"))
			this.phone = jo.getString("phone");
		if (jo.has("password"))
			this.password = jo.getString("password");
		if (jo.has("paypassword"))
			this.paypassword = jo.getString("paypassword");
		if (jo.has("token"))
			this.token = jo.getString("token");
		if (jo.has("nickname"))
			this.nickname = jo.getString("nickname");
		if (jo.has("realname"))
			this.realname = jo.getString("realname");
		if (jo.has("idcard"))
			this.idcard = jo.getString("idcard");
		if (jo.has("email"))
			this.email = jo.getString("email");
		if (jo.has("school"))
			this.school = jo.getString("school");
		if (jo.has("year"))
			this.year = jo.getString("year");
		if (jo.has("department"))
			this.department = jo.getString("department");
		if (jo.has("major"))
			this.major = jo.getString("major");
		if (jo.has("student_id"))
			this.student_id = jo.getString("student_id");
		if (jo.has("student_class"))
			this.student_class = jo.getString("student_class");
		if (jo.has("idcard_photo"))
			this.idcard_photo = jo.getString("idcard_photo");
		if (jo.has("stucard_photo1"))
			this.stucard_photo1 = jo.getString("stucard_photo1");
		if (jo.has("stucard_photo2"))
			this.stucard_photo2 = jo.getString("stucard_photo2");
		if (jo.has("stucard_photo3"))
			this.stucard_photo3 = jo.getString("stucard_photo3");
		if (jo.has("video"))
			this.video = jo.getString("video");
		if (jo.has("limitamount"))
			this.limitamount = jo.getDouble("limitamount");
		if (jo.has("spent"))
			this.spent = jo.getDouble("spent");
		if (jo.has("status"))
			this.status = jo.getInt("status");
		if (jo.has("inviteuserid"))
			this.inviteuserid = jo.getLong("inviteuserid");
		if (jo.has("type"))
			this.type = jo.getString("type");
		if (jo.has("supp"))
			this.supp = jo.getString("supp");
		if (jo.has("supp2"))
			this.supp2 = jo.getString("supp2");
		if (jo.has("create_time"))
			this.create_time = DateUtils.parseDate(jo.getString("create_time"));
	}

	public static List<Account> getList(JSONArray ja) throws JSONException {
		List<Account> li = new ArrayList<Account>();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			li.add(new Account(jo));
		}
		return li;
	}

	public Account(Cursor c) {
		this._id = c.getLong(c.getColumnIndex(AccountConstant._ID));
		this.identity_id = c.getString(c
				.getColumnIndex(AccountConstant.IDENTITY_ID));
		this.phone = c.getString(c.getColumnIndex(AccountConstant.PHONE));
		this.password = c.getString(c.getColumnIndex(AccountConstant.PASSWORD));
		this.paypassword = c.getString(c
				.getColumnIndex(AccountConstant.PAYPASSWORD));
		this.token = c.getString(c.getColumnIndex(AccountConstant.TOKEN));
		this.nickname = c.getString(c.getColumnIndex(AccountConstant.NICKNAME));
		this.realname = c.getString(c.getColumnIndex(AccountConstant.REALNAME));
		this.idcard = c.getString(c.getColumnIndex(AccountConstant.IDCARD));
		this.email = c.getString(c.getColumnIndex(AccountConstant.EMAIL));
		this.school = c.getString(c.getColumnIndex(AccountConstant.SCHOOL));
		this.year = c.getString(c.getColumnIndex(AccountConstant.YEAR));
		this.department = c.getString(c
				.getColumnIndex(AccountConstant.DEPARTMENT));
		this.major = c.getString(c.getColumnIndex(AccountConstant.MAJOR));
		this.student_id = c.getString(c
				.getColumnIndex(AccountConstant.STUDENT_ID));
		this.student_class = c.getString(c
				.getColumnIndex(AccountConstant.STUDENT_CLASS));
		this.idcard_photo = c.getString(c
				.getColumnIndex(AccountConstant.IDCARD_PHOTO));
		this.stucard_photo1 = c.getString(c
				.getColumnIndex(AccountConstant.STUCARD_PHOTO1));
		this.stucard_photo2 = c.getString(c
				.getColumnIndex(AccountConstant.STUCARD_PHOTO2));
		this.stucard_photo3 = c.getString(c
				.getColumnIndex(AccountConstant.STUCARD_PHOTO3));
		this.video = c.getString(c.getColumnIndex(AccountConstant.VIDEO));
		this.limitamount = c.getDouble(c
				.getColumnIndex(AccountConstant.LIMITAMOUNT));
		this.spent = c.getDouble(c.getColumnIndex(AccountConstant.SPENT));
		this.status = c.getInt(c.getColumnIndex(AccountConstant.STATUS));
		this.inviteuserid = c.getLong(c
				.getColumnIndex(AccountConstant.INVITEUSERID));
		this.type = c.getString(c.getColumnIndex(AccountConstant.TYPE));
		this.supp = c.getString(c.getColumnIndex(AccountConstant.SUPP));
		this.supp2 = c.getString(c.getColumnIndex(AccountConstant.SUPP2));
		this.create_time = DateUtils.parseDate(c.getString(c
				.getColumnIndex(AccountConstant.CREATE_TIME)));

	}

	public void set_id(String str) {
		this._id = Long.parseLong(str);
	}

	public void setIdentity_id(String str) {
		this.identity_id = str;
	}

	public void setPhone(String str) {
		this.phone = str;
	}

	public void setPassword(String str) {
		this.password = str;
	}

	public void setPaypassword(String str) {
		this.paypassword = str;
	}

	public void setToken(String str) {
		this.token = str;
	}

	public void setNickname(String str) {
		this.nickname = str;
	}

	public void setRealname(String str) {
		this.realname = str;
	}

	public void setIdcard(String str) {
		this.idcard = str;
	}

	public void setEmail(String str) {
		this.email = str;
	}

	public void setSchool(String str) {
		this.school = str;
	}

	public void setYear(String str) {
		this.year = str;
	}

	public void setDepartment(String str) {
		this.department = str;
	}

	public void setMajor(String str) {
		this.major = str;
	}

	public void setStudent_id(String str) {
		this.student_id = str;
	}

	public void setStudent_class(String str) {
		this.student_class = str;
	}

	public void setIdcard_photo(String str) {
		this.idcard_photo = str;
	}

	public String getPathIdcard_photo(Context context) {
		return API.serverURI + "/api/account/idcard_photo/" + this.idcard_photo;
	}

	public void setStucard_photo1(String str) {
		this.stucard_photo1 = str;
	}

	public String getPathStucard_photo1(Context context) {
		return API.serverURI + "/api/account/stucard_photo1/"
				+ this.stucard_photo1;
	}

	public void setStucard_photo2(String str) {
		this.stucard_photo2 = str;
	}

	public String getPathStucard_photo2(Context context) {
		return API.serverURI + "/api/account/stucard_photo2/"
				+ this.stucard_photo2;
	}

	public void setStucard_photo3(String str) {
		this.stucard_photo3 = str;
	}

	public String getPathStucard_photo3(Context context) {
		return API.serverURI + "/api/account/stucard_photo3/"
				+ this.stucard_photo3;
	}

	public void setVideo(String str) {
		this.video = str;
	}

	public void setLimitamount(String str) {
		this.limitamount = Double.parseDouble(str);
	}

	public void setSpent(String str) {
		this.spent = Double.parseDouble(str);
	}

	public void setStatus(String str) {
		this.status = Integer.parseInt(str);
	}

	public void setInviteuserid(String str) {
		this.inviteuserid = Long.parseLong(str);
	}

	public void setType(String str) {
		this.type = str;
	}

	public void setSupp(String str) {
		this.supp = str;
	}

	public void setSupp2(String str) {
		this.supp2 = str;
	}

	public void setCreate_time(String str) {
		this.create_time = DateUtils.parseDate(str);
	}

	public boolean isNull() {
		return _id != 0;
	}

	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		if (_id > 0)
			cv.put("_id", _id);
		if (!TextUtils.isEmpty(identity_id))
			cv.put("identity_id", identity_id);
		if (!TextUtils.isEmpty(phone))
			cv.put("phone", phone);
		if (!TextUtils.isEmpty(password))
			cv.put("password", password);
		if (!TextUtils.isEmpty(paypassword))
			cv.put("paypassword", paypassword);
		if (!TextUtils.isEmpty(token))
			cv.put("token", token);
		if (!TextUtils.isEmpty(nickname))
			cv.put("nickname", nickname);
		if (!TextUtils.isEmpty(realname))
			cv.put("realname", realname);
		if (!TextUtils.isEmpty(idcard))
			cv.put("idcard", idcard);
		if (!TextUtils.isEmpty(email))
			cv.put("email", email);
		if (!TextUtils.isEmpty(school))
			cv.put("school", school);
		if (!TextUtils.isEmpty(year))
			cv.put("year", year);
		if (!TextUtils.isEmpty(department))
			cv.put("department", department);
		if (!TextUtils.isEmpty(major))
			cv.put("major", major);
		if (!TextUtils.isEmpty(student_id))
			cv.put("student_id", student_id);
		if (!TextUtils.isEmpty(student_class))
			cv.put("student_class", student_class);
		if (!TextUtils.isEmpty(idcard_photo))
			cv.put("idcard_photo", idcard_photo);
		if (!TextUtils.isEmpty(stucard_photo1))
			cv.put("stucard_photo1", stucard_photo1);
		if (!TextUtils.isEmpty(stucard_photo2))
			cv.put("stucard_photo2", stucard_photo2);
		if (!TextUtils.isEmpty(stucard_photo3))
			cv.put("stucard_photo3", stucard_photo3);
		if (!TextUtils.isEmpty(video))
			cv.put("video", video);
		if (limitamount > 0)
			cv.put("limitamount", limitamount);
		if (spent > 0)
			cv.put("spent", spent);
		if (status > 0)
			cv.put("status", status);
		if (inviteuserid > 0)
			cv.put("inviteuserid", inviteuserid);
		if (!TextUtils.isEmpty(type))
			cv.put("type", type);
		if (!TextUtils.isEmpty(supp))
			cv.put("supp", supp);
		if (!TextUtils.isEmpty(supp2))
			cv.put("supp2", supp2);
		if (create_time != null)
			cv.put("create_time", DateUtils.getString(create_time));
		return cv;
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jo = new JSONObject();
		if (_id > 0)
			jo.put("_id", _id);
		if (!TextUtils.isEmpty(identity_id))
			jo.put("identity_id", identity_id);
		if (!TextUtils.isEmpty(phone))
			jo.put("phone", phone);
		if (!TextUtils.isEmpty(password))
			jo.put("password", password);
		if (!TextUtils.isEmpty(paypassword))
			jo.put("paypassword", paypassword);
		if (!TextUtils.isEmpty(token))
			jo.put("token", token);
		if (!TextUtils.isEmpty(nickname))
			jo.put("nickname", nickname);
		if (!TextUtils.isEmpty(realname))
			jo.put("realname", realname);
		if (!TextUtils.isEmpty(idcard))
			jo.put("idcard", idcard);
		if (!TextUtils.isEmpty(email))
			jo.put("email", email);
		if (!TextUtils.isEmpty(school))
			jo.put("school", school);
		if (!TextUtils.isEmpty(year))
			jo.put("year", year);
		if (!TextUtils.isEmpty(department))
			jo.put("department", department);
		if (!TextUtils.isEmpty(major))
			jo.put("major", major);
		if (!TextUtils.isEmpty(student_id))
			jo.put("student_id", student_id);
		if (!TextUtils.isEmpty(student_class))
			jo.put("student_class", student_class);
		if (!TextUtils.isEmpty(idcard_photo))
			jo.put("idcard_photo", idcard_photo);
		if (!TextUtils.isEmpty(stucard_photo1))
			jo.put("stucard_photo1", stucard_photo1);
		if (!TextUtils.isEmpty(stucard_photo2))
			jo.put("stucard_photo2", stucard_photo2);
		if (!TextUtils.isEmpty(stucard_photo3))
			jo.put("stucard_photo3", stucard_photo3);
		if (!TextUtils.isEmpty(video))
			jo.put("video", video);
		if (limitamount > 0)
			jo.put("limitamount", limitamount);
		if (spent > 0)
			jo.put("spent", spent);
		if (status > 0)
			jo.put("status", status);
		if (inviteuserid > 0)
			jo.put("inviteuserid", inviteuserid);
		if (!TextUtils.isEmpty(type))
			jo.put("type", type);
		if (!TextUtils.isEmpty(supp))
			jo.put("supp", supp);
		if (!TextUtils.isEmpty(supp2))
			jo.put("supp2", supp2);
		if (create_time != null)
			jo.put("create_time", DateUtils.getString(create_time));
		return jo;
	}

	public static Account generateTest(String fix) {
		Account test = new Account();
		test.identity_id = fix + "identity_id";
		test.phone = fix + "phone";
		test.password = fix + "password";
		test.paypassword = fix + "paypassword";
		test.token = fix + "token";
		test.nickname = fix + "nickname";
		test.realname = fix + "realname";
		test.idcard = fix + "idcard";
		test.email = fix + "email";
		test.school = fix + "school";
		test.year = fix + "year";
		test.department = fix + "department";
		test.major = fix + "major";
		test.student_id = fix + "student_id";
		test.student_class = fix + "student_class";
		test.idcard_photo = fix + "idcard_photo";
		test.stucard_photo1 = fix + "stucard_photo1";
		test.stucard_photo2 = fix + "stucard_photo2";
		test.stucard_photo3 = fix + "stucard_photo3";
		test.video = fix + "video";
		test.limitamount = 1;
		test.spent = 1;
		test.status = 1;
		test.inviteuserid = 1;
		test.type = fix + "type";
		test.supp = fix + "supp";
		test.supp2 = fix + "supp2";
		return test;

	}
}
