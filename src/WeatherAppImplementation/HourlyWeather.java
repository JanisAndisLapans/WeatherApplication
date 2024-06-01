package WeatherAppImplementation;

public class HourlyWeather extends WeatherUnit {
	private DailyWeather day;
	public int hour;
	
	public HourlyWeather(double temperature, double windSpeed, double precipitaion, int hour, DailyWeather day) {
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
