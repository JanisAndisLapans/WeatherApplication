package Util;

import javax.swing.JOptionPane;

// Static methods to show messages to the user or for debugging purposes

public class Messages {
	static void showMessage(String text, String title, int type) {
		JOptionPane.showMessageDialog(null, text, title, type);
	}
		
	public static void showError(String text) {
		showMessage(text, "Unhandled Exception Occourred", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showDebugMessage(String text) {
		showMessage(text, "Debug", JOptionPane.INFORMATION_MESSAGE);
	}
}
