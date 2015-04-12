package com.david.pda.weather.model;

import java.util.Map;

public class ImageUrlHelper {
	static Map<String, Integer> map;
	static {
		map.put("night_duoyun", 1);
	}

	public static void getId(String key) {
		map.get(key);
	}
}
