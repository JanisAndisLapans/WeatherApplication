package WeatherAppImplementation;

public class HourlyWeather extends WeatherUnit {
	private DailyWeather day;
	private int hour;
	
	public HourlyWeather(double temperature, int hour, DailyWeather day) {
		super(temperature);
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
