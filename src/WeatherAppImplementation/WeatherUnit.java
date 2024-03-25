package WeatherAppImplementation;

public abstract class WeatherUnit {
	protected double temperature;
	
	public WeatherUnit(double temperature) {
		this.temperature = temperature;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
}
