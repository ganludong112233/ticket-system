package com.workorder.ticket.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DateUtils {
	public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_YYYYMMDDHHMMSS2 = "yyyy-MM-dd_HH_mm_ss";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	private static Map<String, SimpleDateFormat> formats = new HashMap<String, SimpleDateFormat>();

	public synchronized static String format(Date date, String pattern) {
		SimpleDateFormat sdf = formats.get(pattern);
		if (sdf == null) {
			sdf = new SimpleDateFormat(pattern);
			formats.put(pattern, sdf);
		}
		return sdf.format(date);
	}

	public synchronized static Date parse(String date, String pattern) {
		SimpleDateFormat sdf = formats.get(pattern);
		if (sdf == null) {
			sdf = new SimpleDateFormat(pattern);
			formats.put(pattern, sdf);
		}
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取当前时间的字符串 格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentDateString() {
		return format(new Date(), DATE_FORMAT_YYYYMMDDHHMMSS);
	}

	/**
	 * get current host UTC datetime
	 * 
	 * @return
	 */
	public static Date getUTCDate() {
		Calendar cal = Calendar.getInstance();
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return cal.getTime();
	}

	public static Date calculateUTCDate(Date datetime, int timezone) {
		return calculateUTCDate(datetime, timezone, 0);
	}

	public static Date calculateLocalDate(int timezone) {
		return calculateLocalDate(timezone, 0);
	}

	/**
	 * calculate the UTC Date with given datetime and the timezone
	 * 
	 * @param datetime
	 *            the local datetime
	 * @param timezone
	 *            the local timezone
	 * @param dstMillseconds
	 *            the day saving time offset millseconds.
	 * @return
	 */
	public static Date calculateUTCDate(Date datetime, int timezone,
			int dstMillseconds) {
		if (datetime == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		int zoneOffset = timezone * 3600 * 1000;
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstMillseconds));
		return cal.getTime();
	}

	// 　把时间转换成指定时区的字符串
	public static String formatToLocalStr(Date datetime, String format,
			int timezone) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (timezone >= 0) {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+" + timezone));
		} else {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT" + timezone));
		}

		return sdf.format(datetime);
	}

	// 　计算指定时区在当时区到达指定时间(datetime)时的时间戳
	public static Date calculateLocalDate(String timStr, String format,
			int timezone) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (timezone >= 0) {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+" + timezone));
		} else {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT" + timezone));
		}

		try {
			return sdf.parse(timStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 　计算指定时区在当时区到达指定时间(datetime)时的时间戳
	public static Long calculateLocalTimestamp(String timStr, String format,
			int timezone) {
		Date date = calculateLocalDate(timStr, format, timezone);
		if (date != null) {
			return date.getTime();
		}
		return null;
	}

	/**
	 * calculate the specified timezone datetime
	 * 
	 * @param timezone
	 * @param dstMillseconds
	 *            the day saving time offset millseconds.
	 * @return
	 */
	public static Date calculateLocalDate(int timezone, int dstMillseconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getUTCDate());
		int zoneOffset = timezone * 3600 * 1000;
		cal.add(java.util.Calendar.MILLISECOND, zoneOffset + dstMillseconds);
		return cal.getTime();
	}

	public static Date getBeginOfDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.MILLISECOND, 000);
		return calendar.getTime();
	}

	public static Date getEndOfDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public static Date getBeginOfWeek(Date date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONDAY),
				calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return calendar.getTime();
	}

	public static Date getEndOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getBeginOfWeek(date));
		calendar.add(Calendar.DAY_OF_WEEK, 6);
		return calendar.getTime();
	}

	public static void main(String[] args) {
		System.out.println(getBeginOfWeek(new Date()));
		System.out.println(getEndOfWeek(new Date()));

		System.out.println(getBeginOfMonth(new Date()));
		System.out.println(getEndOfMonth(new Date()));
	}

	public static Date getBeginOfMonth(Date date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONDAY),
				calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static Date getEndOfMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONDAY),
				calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1);
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		return calendar.getTime();
	}

	/**
	 * 通过时间秒毫秒数判断两个时间的间隔
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDaysByMillisecond(Date date1, Date date2) {
		// 同一天
		if (getEndOfDay(date1).equals(getEndOfDay(date2))) {
			return 0;
		}
		int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
		return days;
	}

	/**
	 * @param date
	 * @param formatString
	 */
	public static String getStringFromDate(Date date, String formatString) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(date);
	}

	/**
	 * @param date
	 * @param formatString
	 * @return 0时区
	 */
	public static String get0StringFromDate(Date date, String formatString) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return format.format(date);
	}

	/**
	 * 获取两个时间时间间隔（秒）
	 */
	public static long getSecondsBetweenDate(Date date1, Date date2) {
		if (date1.before(date2)) {
			return (date2.getTime() - date1.getTime()) / 1000;
		} else {
			return (date1.getTime() - date2.getTime()) / 1000;
		}
	}

	/**
	 * @param dateString
	 * @param format
	 * @return 0时区
	 */
	public static Date get0DateFromString(String dateString, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		try {
			return sdf.parse(dateString);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date getAddedDate(Date date, int day) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, day);
			return calendar.getTime();
		}
		return date;
	}

	public static Date getFewerMinutes(Date date, int minute) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, minute);
			return calendar.getTime();
		}
		return date;
	}

	public static Date getDateFromString(String dateString, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(dateString);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getDayByTimestamp(long timestamp, TimeZone timezone) {
		Date datetime = new Date(timestamp);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setTimeZone(timezone);
		return format.format(datetime);
	}

	// 获取零时区当天的开始时间
	public static long getTodayStartTimestamp() {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setTimeZone(TimeZone.getTimeZone("GMT+0"));

		String dayStr = format.format(new Date());
		Date TodayStart;
		try {
			TodayStart = format.parse(dayStr);
			return TodayStart.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0L;
	}

	public static TimeZone getLocalTimeZone() {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeZone();
	}

	public static boolean isBetweenTime(Date begin, Date end, Date now) {
		if (begin.getTime() < now.getTime() && now.getTime() < end.getTime()) {
			return true;
		}
		return false;
	}

	public static boolean isAfterTime(Date end, Date now) {
		if (end.getTime() < now.getTime()) {
			return true;
		}
		return false;
	}

	public static boolean isBeforTime(Date begin, Date now) {
		if (begin.getTime() > now.getTime()) {
			return true;
		}
		return false;
	}

	public static boolean isRemind(Date begin, Date now) {
		if (begin.getTime() < now.getTime()) {
			return true;
		}
		return false;
	}

}
