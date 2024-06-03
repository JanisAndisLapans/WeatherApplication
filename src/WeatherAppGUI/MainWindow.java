package WeatherAppGUI;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Util.Messages;
import Util.Util;
import WeatherAppImplementation.WeatherData;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.Color;
import java.awt.Font;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import Util.Settings;


public class MainWindow {

	private JFrame frame;

	/**
	 * Create the window.
	 */
	public MainWindow() {
		initialize();
		loadSettings();
		fetchWeatherData();
	}

	/**
	 * Launch the window.
	 */
	public void open() {
		try {
			MainWindow window = new MainWindow();
			window.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JButton btnSaveSettings;
	private JComboBox<String> timeFormat;
	private JComboBox<String> speedUnit;
	private JComboBox<String> tempUnit;
	private JLabel tempLabel;
	private JLabel windSpeedLabel;
	private JLabel precipitaionLabel;
	private LocationField locationField;
	private JDateChooser dateChooser;
	private JPanel hourPanel;
	private String type = "t"; // god forgive me
	private JLabel headingLocation;
	private JLabel headingDate;

	WeatherData currentWeatherData;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Main window of the program
		frame = new JFrame();
		frame.getContentPane().setForeground(Color.WHITE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(0, 0, 1300, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		/**
		 * #########################################
		 *  Settings sidebar content of the program
		 * #########################################
		 */

		// Settings sidebar
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(0, 0, 356, 700);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		// Heading "Settings"
		JLabel lblSettings = new JLabel("SETTINGS");
		lblSettings.setForeground(Color.WHITE);
		lblSettings.setBounds(64, 25, 200, 69);
		lblSettings.setFont(new Font("Dialog", Font.BOLD, 32));
		panel.add(lblSettings);

		// Label for changing date
		JLabel lblDate = new JLabel("Date");
		lblDate.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblDate.setForeground(Color.WHITE);
		lblDate.setBounds(64, 106, 60, 17);
		panel.add(lblDate);

		// Get current day
		var today = Util.truncateDate(Calendar.getInstance());
		var fifteenDaysAhead = (Calendar) today.clone();
		fifteenDaysAhead.add(Calendar.DAY_OF_YEAR, 15);

		// Date field
		dateChooser = new JDateChooser();
		dateChooser.setBounds(64, 135, 200, 26);
		dateChooser.setDate(today.getTime());

		dateChooser.setMaxSelectableDate(fifteenDaysAhead.getTime());
		Calendar minDate = Calendar.getInstance();
		minDate.set(1940, 0, 1);		
		dateChooser.setMinSelectableDate(minDate.getTime());
		panel.add(dateChooser);

		// Label for changing location
		JLabel lblLocation = new JLabel("Location");
		lblLocation.setForeground(Color.WHITE);
		lblLocation.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblLocation.setBounds(64, 180, 121, 17);
		panel.add(lblLocation);

		// Location field
		locationField = new LocationField();
		locationField.setBounds(64, 209, 200, 26);
		panel.add(locationField);
		
		// Label for changing temperature format
		JLabel lblTemperatureMetric = new JLabel("Temperature Metric");
		lblTemperatureMetric.setForeground(Color.WHITE);
		lblTemperatureMetric.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblTemperatureMetric.setBounds(64, 258, 200, 17);
		panel.add(lblTemperatureMetric);

		// Temperature format field
		tempUnit = new JComboBox<String>();
		tempUnit.addItem("Celsius");
		tempUnit.addItem("Fahrenheit");
		tempUnit.setBounds(64, 287, 200, 27);
		panel.add(tempUnit);
		
		// Label for changing speed format
		JLabel lblSpeedMetric = new JLabel("Speed Metric");
		lblSpeedMetric.setForeground(Color.WHITE);
		lblSpeedMetric.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblSpeedMetric.setBounds(64, 336, 200, 17);
		panel.add(lblSpeedMetric);

		// Wind speed format field
		speedUnit = new JComboBox<String>();
		speedUnit.addItem("km/h");
		speedUnit.addItem("m/s");
		tempUnit.setBounds(64, 287, 200, 27);
		speedUnit.setBounds(64, 365, 200, 27);
		panel.add(speedUnit);
		
		// when pressed tempUnit drop down
		// change the temperature symbol in the settings
		tempUnit.addActionListener(e -> {
			Settings.temperatureSymbol = tempUnit.getSelectedIndex() == 0 ? "°C" : "°F";
		});
		
		// when pressed speedUnit drop down
		// change the wind speed symbol in the settings
		speedUnit.addActionListener(e -> {
			Settings.windSpeedSymbol = speedUnit.getSelectedIndex() == 0 ? "km/h" : "m/s";
		});
		
		// Label for changing time format
		JLabel lblTimeMetric = new JLabel("Time Format");
		lblTimeMetric.setForeground(Color.WHITE);
		lblTimeMetric.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblTimeMetric.setBounds(64, 414, 200, 17);
		panel.add(lblTimeMetric);

		// time format 24/12h drop down
		timeFormat = new JComboBox<String>();
		timeFormat.addItem("24H");
		timeFormat.addItem("12H");
		timeFormat.setBounds(64, 443, 200, 27);
		panel.add(timeFormat);

		// when pressed timeFormat drop down
		// change the time format in the settings
		timeFormat.addActionListener(e -> {
			if(is12HFormat()){
				Settings.timeFormat = "12H";
			}else{
				Settings.timeFormat = "24H";
			}
		});

		// reset all to defaults button
		JButton reset = new JButton("Reset settings");
		reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dateChooser.setDate(today.getTime());
				locationField.setSelectedIndex(0);
				timeFormat.setSelectedIndex(0);
				tempUnit.setSelectedIndex(0);
				speedUnit.setSelectedIndex(0);
				fetchWeatherData();
			}
		});
		reset.setBounds(64, 502, 200, 27);
		panel.add(reset);
		
		// Button to apply settings
		btnSaveSettings = new JButton("Apply");
		btnSaveSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fetchWeatherData();
			}
		});
		btnSaveSettings.setBounds(64, 566, 200, 27);
		panel.add(btnSaveSettings);
		
		/**
		 * #############################
		 *  Main content of the program
		 * #############################
		 */
				
		// Heading text current date
		headingDate = new JLabel("Date");
		headingDate.setForeground(Color.BLACK);
		headingDate.setBounds(390, 28, 509, 51);
		headingDate.setFont(new Font("Arial", Font.BOLD, 50));
		frame.getContentPane().add(headingDate);
		
		// Heading text current location
		headingLocation = new JLabel("Location");
		headingLocation.setForeground(Color.BLACK);
		headingLocation.setBounds(390, 73, 877, 59);
		headingLocation.setFont(new Font("Arial", Font.PLAIN, 20));
		frame.getContentPane().add(headingLocation);

		// Heading text "Average"
		JLabel headingAverage = new JLabel("Average");
		headingAverage.setForeground(Color.BLACK);
		headingAverage.setBounds(391, 144, 188, 43);
		headingAverage.setFont(new Font("Arial", Font.BOLD, 30));
		frame.getContentPane().add(headingAverage);
		
		// Temperature icon
		JLabel tempIcon = new JLabel("");
		tempIcon.setIcon(new ImageIcon(MainWindow.class.getResource("/Images/Temperature-Icon.png")));
		tempIcon.setBounds(391, 179, 152, 149);
		frame.getContentPane().add(tempIcon);
		
		// Temperature information
		tempLabel = new JLabel("...");
		tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tempLabel.setFont(new Font("Dialog", Font.BOLD, 30));
		tempLabel.setBounds(438, 179, 174, 149);
		frame.getContentPane().add(tempLabel);
		
		// Wind speed icon
		JLabel windIcon = new JLabel("");
		windIcon.setIcon(new ImageIcon(MainWindow.class.getResource("/Images/Wind-Icon.png")));
		windIcon.setBounds(624, 179, 152, 149);
		frame.getContentPane().add(windIcon);
		
		// Wind speed information
		windSpeedLabel = new JLabel("...");
		windSpeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		windSpeedLabel.setFont(new Font("Dialog", Font.BOLD, 30));
		windSpeedLabel.setBounds(660, 179, 262, 149);
		frame.getContentPane().add(windSpeedLabel);
		
		// Precipitation icon
		JLabel precipitationIcon = new JLabel("");
		precipitationIcon.setIcon(new ImageIcon(MainWindow.class.getResource("/Images/Precipitation-Icon.png")));
		precipitationIcon.setBounds(901, 179, 152, 149);
		frame.getContentPane().add(precipitationIcon);
		
		// Precipitation information
		precipitaionLabel = new JLabel("...");
		precipitaionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		precipitaionLabel.setFont(new Font("Dialog", Font.BOLD, 30));
		precipitaionLabel.setBounds(951, 179, 174, 149);
		frame.getContentPane().add(precipitaionLabel);

		// Horizontal line between average and hourly information
		JPanel horizontalLine = new JPanel();
		horizontalLine.setBounds(391, 340, 875, 1);
		horizontalLine.setBackground(new Color(0, 0, 0));
		frame.getContentPane().add(horizontalLine);
		
		// Heading text "Hourly"
		JLabel headingHourly = new JLabel("Hourly");
		headingHourly.setForeground(Color.BLACK);
		headingHourly.setBounds(391, 369, 188, 43);
		headingHourly.setFont(new Font("Arial", Font.BOLD, 30));
		frame.getContentPane().add(headingHourly);
		
		// Scroll panel with hourly data
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(391, 424, 877, 99);
		frame.getContentPane().add(scrollPane);

		hourPanel = new JPanel();
		hourPanel.setForeground(new Color(255, 255, 255));
		hourPanel.setBorder(null);
		hourPanel.setBackground(new Color(255, 255, 255));
		scrollPane.setViewportView(hourPanel);
		hourPanel.setLayout(new BoxLayout(hourPanel, BoxLayout.X_AXIS));
		
		/**
		 * Buttons to change shown information type on hourly scroll panel
		 * and actions that are taken after clicking
		 */
		JButton btnTemp = new JButton("Temp");
		btnTemp.setBounds(437, 549, 175, 45);
		frame.getContentPane().add(btnTemp);
		
		JButton btnWind = new JButton("Wind");
		btnWind.setBounds(742, 549, 175, 45);
		frame.getContentPane().add(btnWind);
		
		JButton btnPrecipitation = new JButton("Precipitation");
		btnPrecipitation.setBounds(1051, 549, 175, 45);
		frame.getContentPane().add(btnPrecipitation);
		
		btnPrecipitation.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				type = "p";
				fetchWeatherData();
			}
		});
		btnWind.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				type = "w";
				fetchWeatherData();
			}
		});
		btnTemp.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				type = "t";
				fetchWeatherData();
			}
		});		
	}
	
	private void loadSettings () {
		//load the state of the settings from previous uses of application
		switch (Settings.loadState()){
			case 0:
				timeFormat.setSelectedItem(Settings.timeFormat);
				tempUnit.setSelectedItem(Settings.temperatureSymbol.equals("°C") ? "Celsius" : "Fahrenheit");
				speedUnit.setSelectedItem(Settings.windSpeedSymbol.equals("km/h") ? "km/h" : "m/s");
				break;
			case 1:
				// No settings to load
				break;
			case 101:
				Messages.showError("temperatureSymbol not found in file");
				break;
			case 102:
				Messages.showError("timeFormat not found in file");
				break;
			case 103:
				Messages.showError("windSpeedSymbol not found in file");
				break;
			case 104:
				Messages.showError("precipitationSymbol not found in file");
				break;
			default:
				break;
		}
	}
	
	private void saveSettings() {
		switch (Settings.saveState()) {
		case 1:
			Messages.showError("Error saving settings");
			break;
		default:
			break;
		}
	}

	private boolean is12HFormat() {
		// read form timeformat dropdown
		return timeFormat.getSelectedIndex() == 1;
	}
	private String formatHourText(int hour, String period) {
		String hourText = hour < 10 ? "0" : "";
		hourText += Integer.toString(hour);
		hourText += ":00" + period;
		return hourText;
	}

	private void fetchWeatherData() {

		saveSettings();
		
		var location = (Location) locationField.getSelectedItem();
		headingLocation.setText(location.toString());
		currentWeatherData = new WeatherData(location.getLongitude(), location.getLatitude());

		var date = Calendar.getInstance();
		final var dateFormat = new SimpleDateFormat(dateChooser.getDateFormatString());
		headingDate.setText(dateFormat.format(dateChooser.getDate()));
		date.setTime(dateChooser.getDate());

		try {
			currentWeatherData.loadDaysWithHours(date, date, () -> {
				var day = currentWeatherData.getDailyData().get(0);
				var temp = day.getTemperature();
				var dayHours = day.getHours();
				var speed = day.getWindSpeed();

				if (Settings.temperatureSymbol.equals("°F")) {
					temp = Util.convertCelsiusToFahrenheit(temp);
				}
				
				if (Settings.windSpeedSymbol.equals("m/s")) {
					speed = Util.convertKMHtoMS(speed);
				}
				
				// Temperature
				tempLabel.setText(String.format("%.0f %s", temp, Settings.temperatureSymbol));
				// Wind speed
				windSpeedLabel.setText(String.format("%.0f %s", speed, Settings.windSpeedSymbol));
				// Precipitation
				
				if(day.getPrecipitaion() == null) {
					precipitaionLabel.setText(String.format("N/A"));
				}
				else {
					precipitaionLabel.setText(String.format("%.0f %s", day.getPrecipitaion(), Settings.precipitationSymbol));
				}
				// TODO: Implement showing more fetched data in GUI

				// Display hour data
				hourPanel.removeAll();

				Double value = Double.valueOf(0);
				String symbol = null;

				for (int dayHour = 0; dayHour < 24; dayHour++) {
					var hour = dayHours.get(dayHour);
					var period = "";
					var hourTemp = hour.getTemperature();
					var hourSpeed = hour.getWindSpeed();

					if(Settings.temperatureSymbol.equals("°F")){
						hourTemp = Util.convertCelsiusToFahrenheit(hourTemp);
					}
					
					if (Settings.windSpeedSymbol.equals("m/s")) {
						hourSpeed = Util.convertKMHtoMS(hourSpeed);
					}
					
					if (type == "t") {
						value = hourTemp;
						symbol = "%.0f" + Settings.temperatureSymbol;
					}
					else if(type == "w") {
						value = hourSpeed;
						symbol = "%.0f" + Settings.windSpeedSymbol;
					}
					else if(type == "p") {
						value = hour.getPrecipitaion();
						symbol = "%.0f%" + Settings.precipitationSymbol;
					}
					
					// API returns 0 as 12th hour
					if (hour.hour == 0 && dayHour == 12) {
						hour.hour = 12;
					}
					// Format period
					if (is12HFormat()) {
						period = dayHour < 12 ? "AM" : "PM";
						if (hour.hour == 0 || hour.hour == 12) {
							hour.hour = 12;
						} else {
						hour.hour = hour.hour % 12;
						}
					}

					// Format hour text
					String hourText = formatHourText(hour.hour, period);


					JPanel hourDataElement = new JPanel();
					hourDataElement.setBackground(Color.WHITE);
					hourDataElement.setLayout(null);

					JLabel hourLabel = new JLabel(hourText);
					hourLabel.setFont(new Font("Dialog", Font.BOLD, 20));
					hourLabel.setHorizontalAlignment(SwingConstants.CENTER);
					hourLabel.setBounds(0, 12, 110, 17);
					hourDataElement.add(hourLabel);

					JLabel dataLabel;
					if(value == null) {
						dataLabel = new JLabel("N/A");
					}
					else {
						dataLabel = new JLabel(String.format(symbol, value));
					}
					
					dataLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
					dataLabel.setHorizontalAlignment(SwingConstants.CENTER);
					dataLabel.setBounds(0, 55, 110, 17);
					hourDataElement.add(dataLabel);

					// Make sure jPanel stays the same size in box layout and scrolling works
					// properly by setting fixed size
					hourDataElement.setMaximumSize(new Dimension(110, 190));
					hourDataElement.setMinimumSize(new Dimension(110, 190));
					hourDataElement.setPreferredSize(new Dimension(110, 0));

					hourPanel.add(hourDataElement);
				}

				hourPanel.revalidate();
				// TODO: Implement more different data besides temp
			});
		} catch (Exception e) {
			Messages.showError(e.getMessage());
		}
	}
}
