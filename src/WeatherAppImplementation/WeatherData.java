package WeatherAppImplementation;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import Util.APIQuery;
import Util.Messages;
import Util.Util;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

// Class for raw weather data fetched from API

public class WeatherData {
	private final String APIUrlForecast = "https://api.open-meteo.com/v1";
	private final String APIUrlArchive = "https://archive-api.open-meteo.com/v1";
	private final String HistoricalData = "era5";
	private final String Forecast = "forecast";
	private final SimpleDateFormat APIDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final SimpleDateFormat APIDateFormatTime = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
	
	
	private List<DailyWeather> days;
	private double longtitude;
	private double latitude;
	
	private boolean loaded1, loaded2, loaded3;
	
	private boolean loaded = false;
	
	private DailyWeather aggregateDaily(List<HourlyWeather> hourly, Calendar date) {
		int count = 0;
		
		float tempSum = 0;
		float windSpeedSum = 0;
		float precipitationSum = 0;
		
		for (var h : hourly) {			
			tempSum += h.getTemperature();
			windSpeedSum += h.getWindSpeed();
			precipitationSum += h.getPrecipitaion();
			count++;
		}
		
		var day = new DailyWeather(tempSum/count, windSpeedSum/count, precipitationSum/count, date, hourly);
	
		for (var h : hourly) {			
			h.setDay(day);
		}
		
		return day;
	} 
	
	private List<DailyWeather> getDataFromJson(BufferedReader jsonReader) throws Exception {
		var json = (JSONObject) new JSONParser().parse(jsonReader);
		
		var hourly = (JSONObject) json.get("hourly");
		
		var time = (JSONArray) hourly.get("time");
		var temps = (JSONArray) hourly.get("temperature_2m");
		var windSpeeds = (JSONArray) hourly.get("wind_speed_10m");
		var precipitationProbabilities = (JSONArray) hourly.get("precipitation_probability"); 
		
		var dailyData = new ArrayList<DailyWeather>();
		
		if(time.isEmpty()) {
			return dailyData;
		}
		
		var hourlyData = new ArrayList<HourlyWeather>();
		Calendar currentDay = null;
		
		var timeIter = time.iterator();
		var tempIter = temps.iterator();
		var windSpeedIter = windSpeeds.iterator();
		var precipitaionIter = precipitationProbabilities.iterator();
		
		while(timeIter.hasNext()) {
			var date = Calendar.getInstance();
			date.setTime(APIDateFormatTime.parse((String) timeIter.next()));
			
			var temp = (double) tempIter.next();
			var windSpeed = (double) windSpeedIter.next();
			var precipitaion = (double) precipitaionIter.next();
			
			if(currentDay == null) { // first
				currentDay = Util.truncateDate(date);
			} else if(currentDay.before(Util.truncateDate(date))) {				
				dailyData.add(aggregateDaily(hourlyData, currentDay));
				currentDay = Util.truncateDate(date);
				hourlyData = new ArrayList<HourlyWeather>();
			}
			
			hourlyData.add(new HourlyWeather(temp, windSpeed, precipitaion, date.get(Calendar.HOUR_OF_DAY), null));
		}
		
		dailyData.add(aggregateDaily(hourlyData, currentDay)); // Add last day since loop is already done
		
		return dailyData;
	}
	
	public WeatherData(double d, double e) {
		this.longtitude = d;
		this.latitude = e;
	}
	
	// from - first date (max = to) 
	// to - last date (max = today + 16 days)
	// callbackOnLoad - optional callback
	public void loadDaysWithHours(Calendar from, Calendar to, Runnable callbackOnLoad) throws Exception {
		final List<String> propertiesToFetch = new ArrayList<String>(){private static final long serialVersionUID = 7279058049570501796L;

		{  
			add("temperature_2m");
			add("wind_speed_10m");
			add("precipitation_probability");// TODO: fetch more data
		}};
		
		// Remove time info for comparisons
		to = Util.truncateDate(to);
		from = Util.truncateDate(from);
		
		loaded = false;
		
		// Validation
		if(to.before(from)) {
			throw new IllegalArgumentException("from must be before to");
		}
		
		var today = Util.truncateDate(Calendar.getInstance());
		var sixteenDaysAhead = (Calendar) today.clone();
		sixteenDaysAhead.add(Calendar.DAY_OF_YEAR, 16);
		
		if(from.after(sixteenDaysAhead)) {
			throw new IllegalArgumentException("Period must not be more than 16 days in the future");
		}
		
		// Fetch data
		this.days = new ArrayList<DailyWeather>();
		
		var forecastMinDate = (Calendar) today.clone();
		forecastMinDate.add(Calendar.DAY_OF_YEAR, -95);
		
		// Fetch historical
		if(from.before(forecastMinDate)) {				
			loaded1 = false;
			
			new APIQuery(APIUrlArchive)
			.function(HistoricalData)
			.addParam("longitude", Double.toString(this.longtitude))
			.addParam("latitude", Double.toString(this.latitude))
			.addParam("start_date", APIDateFormat.format(from.getTime()))
			.addParam("end_date", APIDateFormat.format(Util.minDate(to, forecastMinDate).getTime()))
			.addParam("hourly", String.join(",", propertiesToFetch))
			.exec(new APIQuery.APICallback() {
				public void run(int responseCode, BufferedReader jsonResult, String errorMessage) {
					try {
						if(responseCode != APIQuery.OK) {
							throw new RuntimeException(errorMessage);
						}
						
						days.addAll(getDataFromJson(jsonResult));
					
						loaded1 = true;
						if(loaded2) {
							loaded = true;
							if(callbackOnLoad != null) {
								callbackOnLoad.run();
							}
						}
					} catch (Exception e) {
						Messages.showError(e.getMessage());
					}
				}
			});
		} else {
			loaded1 = true;
		}
		
		
		// Fetch forecast
		if(!to.before(forecastMinDate)) {
			loaded2 = false;
			
			forecastMinDate.add(Calendar.DAY_OF_YEAR, 1);
			var fetchDataFrom = Util.maxDate(forecastMinDate, from);
			var daysAhead = Util.daysBetween(today, to) + 1;
			if(daysAhead < 0 ) {
				daysAhead = 0;
			}
			var daysAgo = Util.daysBetween(fetchDataFrom, today);
			if(daysAgo < 0) {
				daysAgo = 0;
			}
			
			var toLocal = to; // for use in response to avoid error
			
			new APIQuery(APIUrlForecast)
			.function(Forecast)
			.addParam("longitude", Double.toString(this.longtitude))
			.addParam("latitude", Double.toString(this.latitude))
			.addParam("forecast_days", Long.toString(daysAhead))
			.addParam("past_days", Long.toString(daysAgo))
			.addParam("hourly",  String.join(",", propertiesToFetch))
			.exec(new APIQuery.APICallback() {
				public void run(int responseCode, BufferedReader jsonResult, String errorMessage) {
					try {
						if(responseCode != APIQuery.OK) {
							throw new RuntimeException(errorMessage);
						}
					
						for(var day : getDataFromJson(jsonResult))
						{
							if(!day.getDate().before(fetchDataFrom) && !day.getDate().after(toLocal)) {
								days.add(day);
							}
						}
												
						loaded2 = true;
						if(loaded1) {
							loaded = true;
							if(callbackOnLoad != null) {
								callbackOnLoad.run();
							}
						}
					} catch (Exception e) {
						Messages.showError(e.getMessage());
					}
				}
			});
	
		} else {
			loaded2 = true;
		}	
	} 
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public List<DailyWeather> getDailyData() throws RuntimeException {
		if(!loaded) {
			throw new RuntimeException("Data not yet loaded");
		}
		
		return days;
	}
	
	//TODO: create filtered data return methods as needed
}
