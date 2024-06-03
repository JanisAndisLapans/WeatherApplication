package WeatherAppImplementation;

import java.util.Calendar;
import java.util.List;

// Class to store weather data for a specific calendar day 

public class DailyWeather extends WeatherUnit {
	private List<HourlyWeather> hours;
	Calendar date;
	
	public DailyWeather(double temperature, double windSpeed, Double precipitaion, Calendar date, List<HourlyWeather> hours) {
		super(temperature, windSpeed, precipitaion);
		this.date = date;
		this.hours = hours;
	}

	public List<HourlyWeather> getHours() {
		return hours;
	}

	public Calendar getDate() {
		return date;
	}

}
