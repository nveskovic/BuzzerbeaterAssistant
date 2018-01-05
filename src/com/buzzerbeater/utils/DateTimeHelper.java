package com.buzzerbeater.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelper {
	public static Date getDateFromString(String dateString, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(dateString);
	}

	public static int getNumberOfMinutesBetweenDates(Date earlierDate, Date laterDate) {
		if( earlierDate == null || laterDate == null ) return 0;

	    return (int)((laterDate.getTime()/60000) - (earlierDate.getTime()/60000));
	}
}
