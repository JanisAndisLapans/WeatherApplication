package Util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;

public class Settings {

	public static String temperatureSymbol = "°C";
	public static String timeFormat = "24H";


	/**
	 * Save the settings to a file
	 * 
	 * @return status code
	 */

    public static int saveState() {
        // create a file and save the settings
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                FileWriter writer = new FileWriter(fileChooser.getSelectedFile().getAbsolutePath());
                writer.write("temperatureSymbol=" + temperatureSymbol + "\n");
                writer.write("timeFormat=" + timeFormat + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
				return 1;
            }
        }else{
			return 2;
		}
		return 0;
    }


	/**
	 * Load the settings from the file
	 * 
	 * @return status code
	 */

	public static int loadState() {
    JFileChooser fileChooser = new JFileChooser();
    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        try {
            FileReader reader = new FileReader(fileChooser.getSelectedFile().getAbsolutePath());
            Properties properties = new Properties();
            properties.load(reader);
			if (properties.getProperty("temperatureSymbol") == null) {
				return 101;
			}
	
			if (properties.getProperty("timeFormat") == null) {
				return 102;
			}

            temperatureSymbol = properties.getProperty("temperatureSymbol", "°C");
            timeFormat = properties.getProperty("timeFormat", "24H");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }else{
		return 2;
	}
    return 0;
}
	
	// TODO: Add other settings that can be accessed globally
}
