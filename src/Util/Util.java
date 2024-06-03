package Util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

// General utility methods

public class Util {
	
	/***
	 * Remove time from date
	 * Input:
	 * Calendar date - the date you want to remove time from
	 * Return Calendar object equal to date without time 
	***/
	
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
	
	/***
	 * Get earliest date of two
	 * Input:
	 * Calendar date1 - a date
	 * Calendar date2 - a date
	 * Return Calendar object, earliest of the two dates
	***/
	
	public static Calendar minDate(Calendar date1, Calendar date2) {
		if(date1.after(date2)) {
			return date2;
		}
		return date1;
	}
	
	/***
	 * Get latest date of two
	 * Input:
	 * Calendar date1 - a date
	 * Calendar date2 - a date
	 * Return Calendar object, latest of the two dates
	***/

	public static Calendar maxDate(Calendar date1, Calendar date2) {
		if(date1.before(date2)) {
			return date2;
		}
		return date1;
	}
	
	/***
	 * Get days between two dates
	 * Input:
	 * Calendar date1 - start date
	 * Calendar date2 - end date
	 * Return long number of full days passed between the two dates
	***/
	
	public static long daysBetween(Calendar date1, Calendar date2) {
		long diff = date2.getTime().getTime() - date1.getTime().getTime();
		
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}	

	/***
	 * Convert Celsius degrees to Fahreinheit
	 * Input:
	 * double temp - a Celsius temperature
	 * Return double - Fahrenheit temperature calculated from the input
	***/
	
	public static double convertCelsiusToFahrenheit(double temp) {
		return (temp * 9/5) + 32;
	}
	
	/***
	 * Convert km/h to m/s
	 * Input:
	 * double kmh - km/h speed
	 * Return double - m/s speed calculated from the input
	***/
	
	public static double convertKMHtoMS(double kmh) {
		return kmh * 5 / 18;
	}
}
