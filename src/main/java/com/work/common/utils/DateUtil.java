package com.work.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public abstract class DateUtil {

	/**
	 * 日期格式化字符串->全数字表示
	 */
	public static final String TIME_FORMAT_DIGITAL = "yyyyMMddHHmmss";

	/**
	 * 日期格式化字符串 到秒
	 */
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期格式化字符串 到毫秒
	 */
	public static final String TIME_FORMAT_MSEL = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/**
	 * 日期格式化字符串 到毫秒->全数字表示
	 */
	public static final String TIME_FORMAT_MSEL_DIGITAL = "yyyyMMddHHmmssSSS";

	/**
	 * oracle中日期格式化字符串
	 */
	public static final String TIME_FORMAT_ORACLE = "yyyy-mm-dd hh24:mi:ss";

	/**
	 * yyyyMMdd
	 */
	public static final String YYYYMMDD = "yyyyMMdd";
	/**
	 * YYYY-MM-DD
	 */
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	
	/** HH **/
	public static final String HH = "HH";

	/** MM **/
	public static final String MM = "MM";

	/** SS **/
	public static final String SS = "SS";

	/**
	 * 获取服务器当前日期
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 获取服务器当前时间的字符串 ,格式 ：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentDateStr() {
		return convertDateToStr(getCurrentDate(), TIME_FORMAT);
	}

	/**
	 * 获取服务器当前时间的字符串到毫秒 ,格式 ：yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @return
	 */
	public static String getCurrentTimeStr() {
		return convertDateToStr(getCurrentDate(), TIME_FORMAT_MSEL);
	}

	public static String getOrderDateStr() {
		return convertDateToStr(new Date(), YYYYMMDD);
	}

	/**
	 * 增加天数
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDate(Date date, int days) {
		if (date == null) {
			return date;
		}

		Locale loc = Locale.getDefault();
		GregorianCalendar cal = new GregorianCalendar(loc);
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		cal.set(year, month, day + days);
		return cal.getTime();
	}

	/**
	 * 将字符串转换为日期格式
	 * 
	 * @param dateStr
	 * @param dateFormat
	 * @return
	 */
	public static Date convertStrToDate(String dateStr, String dateFormat) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(dateStr);
		} catch (Exception e) {
			throw new RuntimeException("DateUtil.convertStrToDate():" + e.getMessage());
		}
	}

	/**
	 * 将日期转换为字符串格式
	 * 
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String convertDateToStr(Date date, String dateFormat) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	/**
	 * 将日期转换为字符串格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String convertDateToStr(Date date) {
		return convertDateToStr(date, TIME_FORMAT);
	}

	/**
	 * 给一日期增加一时间
	 * 
	 * @param timePart
	 *            HH,mm,ss
	 * @param number
	 *            要增加的时间
	 * @param date
	 *            日期对象
	 * @return
	 */
	public static Date addTime(String timePart, int number, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (HH.equals(timePart)) {
			cal.add(Calendar.HOUR, number);
		} else if (MM.equals(timePart)) {
			cal.add(Calendar.MINUTE, number);
		} else if (SS.equals(timePart)) {
			cal.add(Calendar.SECOND, number);
		} else {
			throw new IllegalArgumentException("DateUtil.addDate()方法非法参数值：" + timePart);
		}
		return cal.getTime();
	}

	/**
	 * 
	 * 清除指定的时间
	 * 
	 * @param date
	 * @param timePart
	 *            HH,mm,ss
	 * @return
	 */
	public static Date clearTime(Date date, String timePart) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (HH.equals(timePart)) {
			cal.set(Calendar.HOUR_OF_DAY, 0);
		} else if (MM.equals(timePart)) {
			cal.clear(Calendar.MINUTE);
		} else if (SS.equals(timePart)) {
			cal.clear(Calendar.SECOND);
		} else {
			throw new IllegalArgumentException("DateUtil.addDate()方法非法参数值：" + timePart);
		}
		return cal.getTime();
	}

	/**
	 * 时间格式转化
	 * 
	 * @param dateStr
	 * @param formatStyle
	 * @return Date
	 * @author:sunjiaxiao
	 * @throws ParseException 
	 * @date:2013-3-29
	 */
	public static Date getDate(String dateStr, String formatStyle) throws ParseException {
		DateFormat df = new SimpleDateFormat(formatStyle);
		return df.parse(dateStr);
	}

	/**
	 * 字符串格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateStr
	 * @return Date
	 * @throws ParseException 
	 * @date:2013-8-7
	 */
	public static Date getDate(String dateStr) throws ParseException {
		DateFormat df = new SimpleDateFormat(TIME_FORMAT);
		return df.parse(dateStr);
	}

	/**
	 * 获取date格式化后的字符串
	 * 
	 * @param formatStyle
	 * @return String
	 * @author:sunjiaxiao
	 * @date:2013-4-25
	 */
	public static String getDateStr(String formatStyle) {
		DateFormat df = new SimpleDateFormat(formatStyle);
		return df.format(new Date());
	}
	
	public static List<Date> findDates(Date dBegin, Date dEnd) {
		List<Date> lDate = new ArrayList<>();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}
	
	public static List<String> findDateStrs(Date dBegin, Date dEnd,String formatStyle) {
		List<String> dateStrs = new ArrayList<>();
		dateStrs.add(convertDateToStr(dBegin,formatStyle));
		Calendar calendar = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calendar.setTime(dBegin);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calendar.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			dateStrs.add(convertDateToStr(calendar.getTime(),formatStyle));
		}
		return dateStrs;
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtil.convertDateToStr(new Date(), "yyyyMMddHHmmssSSS"));
		List<String> dates = findDateStrs(convertStrToDate("2004-09-18",YYYY_MM_DD), convertStrToDate("2014-09-03",YYYY_MM_DD), YYYY_MM_DD);
		for (String string : dates) {
			System.out.println(string);
		}
	}
}
