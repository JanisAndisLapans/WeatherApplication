package Util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Util {
	public static Calendar truncateDate(Calendar date) {
		Calendar truncDate = (Calendar) date.clone();
		
		truncDate.set(Calendar.HOUR_OF_DAY, 0);
		truncDate.set(Calendar.AM_PM, 0);
		truncDate.set(Calendar.HOUR, 0);
		truncDate.set(Calendar.MINUTE, 0);
		truncDate.set(Calendar.SECOND, 0);
		truncDate.set(Calendar.MILLISECOND, 0);
		return truncDate;
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

	public static double convertCelsiusToFahrenheit(double temp) {
		return (temp * 9/5) + 32;
	}
	
	public static double convertKMHtoMS(double kmh) {
		return kmh * 5 / 18;
	}
}
