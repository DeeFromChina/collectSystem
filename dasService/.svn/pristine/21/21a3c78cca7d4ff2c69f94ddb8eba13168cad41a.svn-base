package com.golead.dasService.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static final SimpleDateFormat longFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final SimpleDateFormat shortFormat = new SimpleDateFormat("yyyyMMdd");

	public static String lFormat(Date date) {
		if (date == null)
			return "";

		return longFormat.format(date);
	}

	public static String sFormat(Date date) {
		if (date == null)
			return "";

		return shortFormat.format(date);
	}
	
	public static Date lParse(String dateStr) {
		if(dateStr == null || dateStr.trim().length() == 0)
			return null;
		
		try {
			return longFormat.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date sParse(String dateStr) {
		if(dateStr == null || dateStr.trim().length() == 0)
			return null;
		
		try {
			return shortFormat.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String getTimeStamp() {
		Date date = Calendar.getInstance().getTime();
		return longFormat.format(date);
	}

}
