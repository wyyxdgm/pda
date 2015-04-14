package com.david.pda.sqlite.model;

public class Model {
	public static final Integer IS_YES = 1;
	public static final Integer IS_NO = 0;
	public static final Integer FLAG_DELETED = 0;
	public static final Integer FLAG_EXISTS = 1;
	public static final String DELFLAG = "delFlag";
	private Integer delFlag = 0;

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

}
