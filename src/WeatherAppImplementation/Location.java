package WeatherAppImplementation;

// Object representing a location where the weather data measurements are being fetched from (e.g. country, city)

public class Location {

	private String name;
	private double latitude;
	private double longitude;
	private String placeId;
	private boolean isCoordsInit;
	
	public Location(String name, double latitude, double longitude, String placeId, boolean isCoordsInit) {
		super();
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.placeId = placeId;
		this.isCoordsInit = isCoordsInit;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getPlaceId() {
		return placeId;
	}
	
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	
	public boolean getIsCoordsInit() {
		return isCoordsInit;
	}
	
	public void setIsCoordsInit(boolean isCoordsInit) {
		this.isCoordsInit = isCoordsInit;
	}

	@Override
	public String toString() {
		return name;
	}

}
