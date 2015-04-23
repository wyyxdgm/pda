package com.david.pda.sqlite.model;

public enum DateType {
	HOUR("小时", 0), DAY("天", 1), WEEK("周", 2), MONTH("月", 3), QUARTER("季度", 4), YEAR(
			"年", 5);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private DateType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	// 普通方法
	public static String getName(int index) {
		for (DateType d : DateType.values()) {
			if (d.getIndex() == index) {
				return d.name;
			}
		}
		return null;
	}

	// 普通方法
	public static DateType getType(String value) {
		for (DateType d : DateType.values()) {
			if (d.getName().equals(value)) {
				return d;
			}
		}
		return null;
	}

	// get set 方法
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
