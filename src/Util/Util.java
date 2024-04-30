package Util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Util {
	public static Calendar truncateDate(Calendar date) {
		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		
		return date;
	}
	
	public static Calendar minDate(Calendar date1, Calendar date2) {
		if(date1.after(date2)) {
			return date2;
		}
		return date1;
	}
	

	public static Calendar maxDate(Calendar date1, Calendar date2) {
		if(date1.before(date2)) {
			return date2;
		}
		return date1;
	}
	
	public static long daysBetween(Calendar date1, Calendar date2) {
		long diff = date2.getTime().getTime() - date1.getTime().getTime();
		
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
}
