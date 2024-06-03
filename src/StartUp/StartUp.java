package StartUp;

import java.awt.EventQueue;
import WeatherAppGUI.MainWindow;
import Util.Messages;

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
