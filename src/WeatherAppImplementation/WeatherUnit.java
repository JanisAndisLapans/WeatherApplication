package WeatherAppImplementation;

public abstract class WeatherUnit {
	protected double temperature;
	protected double windSpeed;
	protected double precipitaion;
	
	public WeatherUnit(double temperature, double windSpeed, long precipitaion) {
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
	
	public double getPrecipitaion() {
		return precipitaion;
	}

	public void setPrecipitaion(long precipitaion) {
		this.precipitaion = precipitaion;
	}
}
