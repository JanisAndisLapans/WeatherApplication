package WeatherAppImplementation;

import java.io.BufferedReader;
//import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Class to fetch data about IP location

public class GetLocation {
	
	private String ip_url = "http://ip-api.com/json/?fields=status,country,region,regionName,city,zip,query,lon,lat";
	private String APIstatus;
	private String country;
	private String regionName;
	private String city;
	private String zip;
	private String pub_ip;
	private double lon;
	private double lat;
	
	public GetLocation() {
		// If any errors occur, location parameters default to Riga
		try {
			URL url =  new URL(ip_url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "curl/7.81.0");
			int status = con.getResponseCode();
			
			if (status == HttpURLConnection.HTTP_OK) {
				 con.connect();
				 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	             String inputLine;
	             StringBuilder response = new StringBuilder();

	             while ((inputLine = in.readLine()) != null) {
	                 response.append(inputLine);
	             }
	             in.close();		          
	             
	             // Parse JSON response
	             JSONParser parse = new JSONParser();
	             JSONObject data_obj = (JSONObject) parse.parse(response.toString());
	             
	             // Get JSON values
	             country = (String) data_obj.get("country");
	             APIstatus = (String) data_obj.get("status");
	             regionName = (String) data_obj.get("regionName");
	             city = (String) data_obj.get("city");
	             zip = (String) data_obj.get("zip");
	             lon = (double) data_obj.get("lon");
	             lat = (double) data_obj.get("lat");
	             pub_ip = (String) data_obj.get("query");
	             
	             //Close connection
	             con.disconnect();
			}
			else {
				 throw new RuntimeException("HttpResponseCode: " + status);
			}
		} catch (Exception e) {
			country = "Latvia";
            APIstatus = "Failed";
            regionName = "Riga";
            city = "Riga";
            zip = "LV-1000";
            lon = 24.0978;
            lat = 56.9496;
            pub_ip = "87.246.186.242";
		}
	}
	
	
	public String getCountry() {
		return country;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getRegion() {
		return regionName;
	}
	
	public String getZip() {
		return zip;
	}
	
	public String getIp() {
		return pub_ip;
	}
	
	public String getAPIstatus() {
		return APIstatus;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLon() {
		return lon;
	}
}
