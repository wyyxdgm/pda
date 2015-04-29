package com.david.pda.util.time;

import java.util.ArrayList;
import java.util.List;

import com.david.pda.sqlite.model.CycleDetails;
import com.david.pda.sqlite.model.DateType;

/**
 * 根据给定的时间区间，和周期中的某个时间片，给出所有关于这个定义好的周期时间片的所有具体时间段列表，列表为空表示在给定区间内没有轮到该给定时间片
 * relative time to absolute time
 * 
 * @author Administrator
 */
public class CycleEntity {
	private int currentCycleNumber = 0;

	public CycleEntity(long startTime, long endTime, long leftTime,
			long rightTime, DateType dateType, long cycleLength,
			CycleDetails detail) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.dateType = dateType;
		this.cycleLength = cycleLength;
		this.detail = detail;
		this.leftTime = leftTime;
		this.rightTime = rightTime;
	}

	public List<CycleDetails> details = new ArrayList<CycleDetails>();
	CycleDetails curDetail = null;

	public List<CycleDetails> getTimes() {// get absolute time list
		// 1指定的周期求取区间在事务执行期间的两侧，时间上没有交集
		if (leftTime > endTime || rightTime < endTime) {// L--R--start---end
			return details;
		}
		long realMin = leftTime > startTime ? leftTime : startTime;
		long realMax = rightTime < endTime ? rightTime : endTime;
		return getDetailsBettween(realMin, realMax);
	}

	private List<CycleDetails> getDetailsBettween(long realMin, long realMax) {
		long fst = getFirstStartTimeAfterStart();
		long fet = getFirstEndTimeAfterStart();
		if (fst >= realMax) {// --R--cs1
			return details;
		}
		// cs1---R
		if (fst < realMax && fet > realMax) {
			curDetail = detail.clone();
			curDetail.setStartTime(fst);
			curDetail.setEndTime(realMax);
			details.add(curDetail);
			curDetail = null;
			return details;
		} else if (fst > realMin && fet < realMax) {
			curDetail = detail.clone();
			curDetail.setStartTime(fst);
			curDetail.setEndTime(fet);
			details.add(curDetail);
			AddWhileEndLTMax();// start add at next
		} else if (fst < realMin && fet > realMin) {
			curDetail = detail.clone();
			curDetail.setStartTime(realMin);
			curDetail.setEndTime(fet);
			details.add(curDetail);
			AddWhileEndLTMax();// start add at next
		} else if (fet < realMin) {
			loopWhileEndLTMin();// when out it the currentCycleNumber shuld be
								// for the one next
			curDetail = detail.clone();
			curDetail.setStartTime(realMin);
			curDetail
					.setEndTime(getCurrentEndTime() > realMin ? getCurrentEndTime()
							: realMin);
			details.add(curDetail);
		} else if (fst < realMin && fet > realMax) {
			curDetail = detail.clone();
			curDetail.setStartTime(realMin);
			curDetail.setEndTime(realMax);
			details.add(curDetail);
		}
		return details;
	}

	private void loopWhileEndLTMin() {

	}

	private void AddWhileEndLTMax() {

	}

	private long getFirstStartTimeAfterStart() {
		return startTime + detail.getStartTime();
	}

	private long getFirstEndTimeAfterStart() {
		return startTime + detail.getEndTime();
	}

	private long getRealTimeAtNextCycle() {

	}

	private long getGapBettweenNextCycle(){
		if(dateType==DateType.HOUR){
			return 
		}
		for(int i=0;i< cycleLength;i++){
			
		}
	}

	long now = 0;

	private long getNow() {
		return now;
	}

	private long getRealTimeByRelative(long startTime, long relativeTime) {
		return startTime + relativeTime;
	}

	/**
	 * absolute time:left
	 */
	long leftTime;
	/**
	 * absolute time:right
	 */
	long rightTime;
	long cycleLength;
	/**
	 * 表示当前在第几个周期, 一个周期的信息由cycleType的cyleLength和DateType决定
	 * cycleLength决定一个周期有几个最小粒度
	 */
	long cycleNumber;
	/**
	 * DateType决定周期时间的最小粒度单位，同一个单位表示的时间含义在不同时间可能不同
	 * 比如DateType=month时，只能通过month+1获取下一个时间的刻度
	 */
	DateType dateType;
	/**
	 * 记录周期计算的时间起点，实际时间
	 */
	long startTime;
	/**
	 * 记录周期计算的结束，实际时间
	 */
	long endTime;
	/**
	 * 记录每个周期中的某个时间段，其中所保留的startTime和endTime一般不得超出周期长度，否则将它处理为往后的某个周期中某个时间段
	 */
	CycleDetails detail;
}
