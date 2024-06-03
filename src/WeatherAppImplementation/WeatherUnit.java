package WeatherAppImplementation;

// Abstract class for a weather data in a specific time period (day, hour etc.)

public abstract class WeatherUnit {
	protected double temperature;
	protected double windSpeed;
	protected Double precipitaion;
	
	public WeatherUnit(double temperature, double windSpeed, Double precipitaion) {
		this.temperature = temperature;
		this.windSpeed = windSpeed;
		this.precipitaion = precipitaion;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	
	public Double getPrecipitaion() {
		return precipitaion;
	}

	public void setPrecipitaion(Double precipitaion) {
		this.precipitaion = precipitaion;
	}
}
