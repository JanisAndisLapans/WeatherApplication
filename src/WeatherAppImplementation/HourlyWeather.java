package WeatherAppImplementation;

// Class to store weather data about a specific hour in a day (dependent on a DailyWeather object)

public class HourlyWeather extends WeatherUnit {
	private DailyWeather day;
	public int hour;
	
	public HourlyWeather(double temperature, double windSpeed, Double precipitaion, int hour, DailyWeather day) {
		super(temperature, windSpeed, precipitaion);
		this.hour = hour;
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public DailyWeather getDay() {
		return day;
	}

	public void setDay(DailyWeather day) {
		this.day = day;
	}

}
