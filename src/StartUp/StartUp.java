package StartUp;

import java.awt.EventQueue;
import java.util.Calendar;

import WeatherAppGUI.MainWindow;
import WeatherAppImplementation.WeatherData;

import javax.swing.JOptionPane;
import Util.Messages;
import Util.Util;

public class StartUp {

	
	//Application startup
	public static void main(String[] args) {
		var mainWindow = new MainWindow();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainWindow.open();
		
					/* Fetch test TODO: remove
					
					WeatherData wd = new WeatherData(13.41, 52.52);
					
					Calendar today = Util.truncateDate(Calendar.getInstance());
					Calendar from = (Calendar) today.clone();
					Calendar to = (Calendar) today.clone();
					from.add(Calendar.DAY_OF_MONTH, -2);
					to.add(Calendar.DAY_OF_MONTH, 2);
					
					wd.loadDaysWithHours(from, to, () -> {
						Messages.showDebugMessage(wd.getDailyData().get(0).getDate().toString());
						Messages.showDebugMessage(Double.toString(wd.getDailyData().get(0).getTemperature()));
					});
					*/
				} catch (Exception e) {
					Messages.showError(e.getMessage());
				}
			}
		});
	}
}
