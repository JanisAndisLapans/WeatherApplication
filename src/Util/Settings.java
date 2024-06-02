package Util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Settings {

	public static String temperatureSymbol = "°C";
	public static String timeFormat = "24H";
	public static String windSpeedSymbol = "km/h";
	public static String precipitationSymbol = "%";


	/**
	 * Save the settings to a file
	 * 
	 * @return status code
	 */

    public static int saveState() {
        // create a file and save the settings
            try {
                // save in home dir 
                FileWriter writer = new FileWriter(System.getProperty("user.home") + "/weatherapp.properties");
                writer.write("temperatureSymbol=" + temperatureSymbol + "\n");
                writer.write("timeFormat=" + timeFormat + "\n");
                writer.write("windSpeedSymbol=" + windSpeedSymbol + "\n");
                writer.write("precipitationSymbol=" + precipitationSymbol + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
				return 1;
            }
            return 0;
		}


	/**
	 * Load the settings from the file
	 * 
	 * @return status code
	 */

	public static int loadState() {
        try {
            FileReader reader = new FileReader(System.getProperty("user.home") + "/weatherapp.properties");
            Properties properties = new Properties();
            properties.load(reader);
			if (properties.getProperty("temperatureSymbol") == null) {
				return 101;
			}
	
			if (properties.getProperty("timeFormat") == null) {
				return 102;
			}
			if (properties.getProperty("windSpeedSymbol") == null) {
				return 103;
			}
            if (properties.getProperty("precipitationSymbol") == null){
                return 104;
            }

            temperatureSymbol = properties.getProperty("temperatureSymbol", "°C");
            timeFormat = properties.getProperty("timeFormat", "24H");
            windSpeedSymbol = properties.getProperty("windSpeedSymbol", "km/h");
            precipitationSymbol = properties.getProperty("precipitationSymbol", "%");
            reader.close();
        }
        catch (IOException e2) {
            e2.printStackTrace();
            return 1;
        }
        return 0;
    }
}
	
	// TODO: Add other settings that can be accessed globally
