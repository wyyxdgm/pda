package com.david.pda.util.other;

import android.graphics.Paint;

public class DrawUtil {
	public static int a = 100;

	public static Paint[] paints = null;
	static {
		paints = new Paint[6];
		paints[0] = new Paint();
		paints[1] = new Paint();
		paints[2] = new Paint();
		paints[3] = new Paint();
		paints[4] = new Paint();
		paints[5] = new Paint();
		paints[0].setARGB(a, 255, 0, 255);
		paints[0].setStyle(Paint.Style.FILL_AND_STROKE);
		paints[1].setARGB(a, 255, 255, 0);
		paints[1].setStyle(Paint.Style.FILL_AND_STROKE);
		paints[2].setARGB(a, 215, 33, 215);
		paints[2].setStyle(Paint.Style.FILL_AND_STROKE);
		paints[3].setARGB(a, 25, 66, 255);
		paints[3].setStyle(Paint.Style.FILL_AND_STROKE);
		paints[4].setARGB(a, 205, 255, 55);
		paints[4].setStyle(Paint.Style.FILL_AND_STROKE);
		paints[5].setARGB(a, 0, 0, 0);
		paints[5].setStyle(Paint.Style.FILL_AND_STROKE);
	}

	public static Paint getPaint(int i, boolean gap) {
		return gap ? paints[4] : paints[i % 4];
	}

	public static Paint getPaint(int i) {
		return paints[i % 4];
	}

	public static Paint getBlank() {
		return paints[5];
	}
}
