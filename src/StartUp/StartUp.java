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
				} catch (Exception e) {
					Messages.showError(e.getMessage());
				}
			}
		});
	}
}
