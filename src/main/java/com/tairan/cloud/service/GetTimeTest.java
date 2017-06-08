package com.tairan.cloud.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * hzmpj 2017年5月12日
 */
public class GetTimeTest {

	// public static void main(String[] args) throws ParseException {
	// String strTime = "2017-4-8";
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	// Date date = sdf.parse(strTime);
	//
	// DateUtils.ceiling(date, field)
	//
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(date);
	// calendar.set(Calendar.DAY_OF_MONTH,
	// calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
	// System.out.println(sdf.format(calendar.getTime()));
	// }

	public static void main(String[] args) throws ParseException {
		Date date = new Date();
		// add method
		// This method deprecated
		// System.out.println(DateUtils.add(date, Calendar.MONTH, 1));
		// Use addYears addWeeks addMonths addDays addHours addMinutes
		// addSeconds replace it
		// System.out.println(DateUtils.addDays(date, 1));
		// System.out.println(DateUtils.addHours(date, 1));
		// System.out.println(DateUtils.addMinutes(date, 1));
		// System.out.println(DateUtils.addSeconds(date, 1));
		// // ceiling 取上限
		// System.out.println(DateUtils.ceiling(date, Calendar.HOUR));
		// System.out.println(DateUtils.ceiling(date, Calendar.MINUTE));
		// truncate 类似Oracle SQL语句中的truncate函数
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(DateUtils.truncate(sdf.parse("2017-4-10"), Calendar.MONTH)));
		// truncatedEquals truncate之后进行比较

		// toCalendar方法将Date装换成Calendar (java.util.GregorianCalendar)
		System.out.println((DateUtils.toCalendar(date)).getClass());
		// parseDate方法
		try {
			System.out.println(DateUtils.parseDateStrictly("10-22-2010", new String[] { "yyyy-MM-dd", "MM-dd-yyyy" }));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// DateUtils 是对Calendar和SimpleDateFormat方法的补充
	}
}
