package com.david.pda.util.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.david.pda.sqlite.model.CycleDetails;
import com.david.pda.sqlite.model.CycleDetailsForPlan;
import com.david.pda.sqlite.model.CycleType;
import com.david.pda.sqlite.model.DateType;
import com.david.pda.sqlite.model.Plan;
import com.david.pda.sqlite.model.base.Model;

/**
 * 根据给定的时间区间，和周期中的某个时间片，给出所有关于这个定义好的周期时间片的所有具体时间段列表，列表为空表示在给定区间内没有轮到该给定时间片
 * relative time to absolute time
 * 
 * @author Administrator
 */
public class PlanCycleUtil {
	private long currentEndTime;
	private long currentStartTime;
	private List<CycleDetailsForPlan> details = new ArrayList<CycleDetailsForPlan>();
	private CycleDetailsForPlan curDetail = null;
	private long realMin;
	private long realMax;
	private long hashTipCycle = 0l;
	private long gapFromNext;
	/**
	 * absolute time:left
	 */
	private long leftTime;
	/**
	 * absolute time:right
	 */
	private long rightTime;
	private int cycleLength;
	/**
	 * 表示当前在第几个周期, 一个周期的信息由cycleType的cyleLength和DateType决定
	 * cycleLength决定一个周期有几个最小粒度
	 */
	private long cycleNumber;
	/**
	 * DateType决定周期时间的最小粒度单位，同一个单位表示的时间含义在不同时间可能不同
	 * 比如DateType=month时，只能通过month+1获取下一个时间的刻度
	 */
	private DateType dateType;
	/**
	 * 记录周期计算的时间起点，实际时间
	 */
	private long startTime;
	/**
	 * 记录周期计算的结束，实际时间
	 */
	private long endTime;
	/**
	 * 记录每个周期中的某个时间段，其中所保留的startTime和endTime一般不得超出周期长度，否则将它处理为往后的某个周期中某个时间段
	 */
	private CycleDetailsForPlan detail;
	Calendar c = Calendar.getInstance();

	public PlanCycleUtil(long startTime, long endTime, long leftTime,
			long rightTime, DateType dateType, int cycleLength,
			CycleDetailsForPlan detail, Long hashTipcycle) {
		super();
		this.startTime = startTime;// plan startTime
		this.endTime = endTime;// plan endTime
		this.dateType = dateType;
		this.cycleLength = cycleLength;
		this.detail = detail;// detail
		this.leftTime = leftTime;
		this.hashTipCycle = hashTipcycle;
		this.rightTime = rightTime;
		// init
		this.cycleNumber = 1;// 第一个周期
		this.currentStartTime = startTime + detail.getStartTime();// first start
																	// time
																	// after
																	// plan
		this.currentEndTime = startTime + detail.getEndTime();// first end time
																// after plan
		c.setTimeInMillis(this.currentStartTime);
	}

	public PlanCycleUtil(long leftTime, long rightTime, Plan m,
			CycleType cycle, CycleDetailsForPlan details, Long hashTipcycle) {
		this(m.getStartTime(), m.getEndTime(), leftTime, rightTime, cycle
				.getDateType(), cycle.getCycleLength().intValue(), details,
				hashTipcycle);
	}

	public PlanCycleUtil(long leftTime, long rightTime, CycleType cycle,
			CycleDetailsForPlan details, long hashTipCycle) {
		this(leftTime, rightTime, leftTime, rightTime, cycle.getDateType(),
				cycle.getCycleLength().intValue(), details, hashTipCycle);
	}

	public List<CycleDetailsForPlan> getTimes() {// get absolute time list
		// 1指定的周期求取区间在事务执行期间的两侧，时间上没有交集
		if (leftTime > endTime || rightTime < startTime) {// L--R--start---end
			return details;
		}
		this.realMin = leftTime > startTime ? leftTime : startTime;
		this.realMax = rightTime < endTime ? rightTime : endTime;
		return getDetailsBettweenMaxAndMin();
	}

	private List<CycleDetailsForPlan> getDetailsBettweenMaxAndMin() {
		while (currentEndTime < realMin) {
			nextCycle();
		}
		fixWhenRightBLMin();
		return details;
	}

	private void fixWhenRightBLMin() {
		if (currentStartTime >= realMax) {// --R--cs1
			return;
		}
		// cs1---R
		if (currentStartTime < realMax && currentEndTime > realMax) {// 4
			AddCurrentToDetails(currentStartTime, realMax);
			return;
		} else if (currentStartTime > realMin && currentEndTime < realMax) {// 3
			AddCurrentToDetails(currentStartTime, currentEndTime);
			nextCycle();
			// start add at next,按周期加，如果过end<Max，+1
			while (currentEndTime < realMax) {
				AddCurrentToDetails();
				nextCycle();
			}
			if (currentStartTime < realMax) {
				AddCurrentToDetails(currentStartTime, realMax);
			}
		} else if (currentStartTime < realMin && currentEndTime > realMin) {// 2
			AddCurrentToDetails(realMin, currentEndTime);
			nextCycle();
			while (currentEndTime < realMax) {
				AddCurrentToDetails();
				nextCycle();
			}
			if (currentStartTime < realMax) {
				AddCurrentToDetails(currentStartTime, realMax);
			}
		} else if (currentStartTime < realMin && currentEndTime > realMax) {// contain
			AddCurrentToDetails(realMin, realMax);
		}
	}

	private void nextCycle() {
		switch (this.dateType) {
		case DAY:
			c.add(Calendar.DAY_OF_MONTH, cycleLength);
			break;
		case MONTH:
			c.add(Calendar.MONTH, cycleLength);
			break;
		case QUARTER:
			c.add(Calendar.MONTH, (cycleLength * 3));
			break;
		case HOUR:
			c.add(Calendar.HOUR_OF_DAY, cycleLength);
			break;
		case WEEK:
			c.add(Calendar.DATE, 7 * cycleLength);
			break;
		case YEAR:
			c.add(Calendar.YEAR, cycleLength);
			break;

		default:
			System.err.println("Error DateType!");
			break;
		}
		this.cycleNumber++;
		this.currentStartTime = c.getTimeInMillis();
		this.currentEndTime = this.currentStartTime
				+ (this.detail.getEndTime() - this.detail.getStartTime());
	}

	private void AddCurrentToDetails() {
		if (detail.getIsTip() == null
				|| detail.getIsTip().intValue() == Model.IS_NO) {// 直接过滤掉不提示的
			return;
		}
		curDetail = (CycleDetailsForPlan) detail.clone();
		if (cycleNumber == hashTipCycle) {// she wei bu tishi
			curDetail.setIsTip(Model.IS_NO);
		}
		curDetail.setCycleNumber(this.cycleNumber);
		curDetail.setStartTime(currentStartTime);
		curDetail.setEndTime(currentEndTime);
		details.add(curDetail);
		curDetail = null;
	}

	private void AddCurrentToDetails(long startTime, long endTime) {
		if (detail.getIsTip() == null
				|| detail.getIsTip().intValue() == Model.IS_NO) {// 直接过滤掉不提示的
			return;
		}
		curDetail = (CycleDetailsForPlan) detail.clone();
		if (cycleNumber == hashTipCycle) {// she wei bu tishi
			curDetail.setIsTip(Model.IS_NO);
		}
		curDetail.setStartTime(startTime);
		curDetail.setCycleNumber(this.cycleNumber);
		curDetail.setEndTime(endTime);
		details.add(curDetail);
		curDetail = null;
	}

	public long getCurrentEndTime() {
		return currentEndTime;
	}

	public void setCurrentEndTime(long currentEndTime) {
		this.currentEndTime = currentEndTime;
	}

	public long getCurrentStartTime() {
		return currentStartTime;
	}

	public void setCurrentStartTime(long currentStartTime) {
		this.currentStartTime = currentStartTime;
	}

	public List<CycleDetailsForPlan> getDetails() {
		return details;
	}

	public void setDetails(List<CycleDetailsForPlan> details) {
		this.details = details;
	}

	public CycleDetails getCurDetail() {
		return curDetail;
	}

	public void setCurDetail(CycleDetailsForPlan curDetail) {
		this.curDetail = curDetail;
	}

	public long getRealMin() {
		return realMin;
	}

	public void setRealMin(long realMin) {
		this.realMin = realMin;
	}

	public long getRealMax() {
		return realMax;
	}

	public void setRealMax(long realMax) {
		this.realMax = realMax;
	}

	public long getGapFromNext() {
		return gapFromNext;
	}

	public void setGapFromNext(long gapFromNext) {
		this.gapFromNext = gapFromNext;
	}

	public long getLeftTime() {
		return leftTime;
	}

	public void setLeftTime(long leftTime) {
		this.leftTime = leftTime;
	}

	public long getRightTime() {
		return rightTime;
	}

	public void setRightTime(long rightTime) {
		this.rightTime = rightTime;
	}

	public int getCycleLength() {
		return cycleLength;
	}

	public void setCycleLength(int cycleLength) {
		this.cycleLength = cycleLength;
	}

	public long getCycleNumber() {
		return cycleNumber;
	}

	public void setCycleNumber(long cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	public DateType getDateType() {
		return dateType;
	}

	public void setDateType(DateType dateType) {
		this.dateType = dateType;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public CycleDetailsForPlan getDetail() {
		return detail;
	}

	public void setDetail(CycleDetailsForPlan detail) {
		this.detail = detail;
	}

	public Calendar getC() {
		return c;
	}

	public void setC(Calendar c) {
		this.c = c;
	}

	public long getHashTipCycle() {
		return hashTipCycle;
	}

	public void setHashTipCycle(long hashTipCycle) {
		this.hashTipCycle = hashTipCycle;
	}

}
