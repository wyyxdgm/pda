package com.david.pda.util.other;

import java.util.Random;

public class RandUtil {
	public static Random rd = new Random();

	public static int getInt(int max) {
		return rd.nextInt(max);
	}
}
