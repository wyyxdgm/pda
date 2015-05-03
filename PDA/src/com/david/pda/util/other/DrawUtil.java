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
		paints[2].setARGB(a, 215, 33, 115);
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

	public static Paint getGap() {
		return paints[4];
	}

	public static Paint getVisualPaint() {
		Paint p = new Paint();
		p.setARGB(40, 23, 55, 180);
		p.setStyle(Paint.Style.STROKE);
		return p;
	}

	public static Paint getNomalPaint() {
		Paint p = new Paint();
		p.setARGB(100, 180, 51, 150);
		p.setStyle(Paint.Style.FILL_AND_STROKE);
		return p;
	}

	public static Paint getPaintByAndgle(int angle) {
		Paint p = new Paint();
		if (angle > 360) {
			p.setARGB(80, 5, 55, 5);
			p.setStyle(Paint.Style.FILL_AND_STROKE);
		} else if (angle > 180) {
			p.setARGB(130, 0, 50, 255);
			p.setStyle(Paint.Style.FILL_AND_STROKE);
		} else if (angle > 90) {
			p.setARGB(150, 250, 215, 0);
			p.setStyle(Paint.Style.FILL_AND_STROKE);
		} else if (angle > 50) {
			p.setARGB(150, 205, 50, 50);
			p.setStyle(Paint.Style.FILL_AND_STROKE);
		} else if (angle > 20) {
			p.setARGB(200, 250, 10, 10);
			p.setStyle(Paint.Style.FILL_AND_STROKE);
		} else if (angle >= 0) {
			p.setARGB(255, 255, 0, 0);
			p.setStyle(Paint.Style.FILL_AND_STROKE);
		} else {// 比当前早，kill it shuld be
			p.setARGB(40, 0, 0, 0);
			p.setStyle(Paint.Style.STROKE);
		}
		return p;
	}

	public static int parseAngleByDayTime(long startTime, long endTime,
			long time) {
		if (time > endTime)
			return 360;
		if (time < startTime)
			return 0;
		return (int) (360.0 * (time - startTime) / (endTime - startTime));
	}
}
