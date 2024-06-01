package WeatherAppGUI;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Calendar;

import Util.Messages;
import Util.Util;
import WeatherAppImplementation.WeatherData;

import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.Color;
import java.awt.Font;
import com.toedter.components.JLocaleChooser;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import java.util.Calendar;
import Util.Settings;
import javax.swing.ScrollPaneConstants;
import java.awt.FlowLayout;
import javax.swing.SpringLayout;


public class MainWindow {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Create the window.
	 */
	public MainWindow() {
		initialize();
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
	private JLabel tempLabel;
	private LocationField locationField;
	private JDateChooser dateChooser;
	private JPanel hourPanel;

	WeatherData currentWeatherData;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(Color.WHITE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(0, 0, 1200, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setResizable(false);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(0, 0, 356, 766);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblSettings = new JLabel("SETTINGS");
		lblSettings.setForeground(Color.WHITE);
		lblSettings.setBounds(64, 25, 200, 69);
		lblSettings.setFont(new Font("Dialog", Font.BOLD, 32));
		panel.add(lblSettings);

		JLabel lblDate = new JLabel("Date");
		lblDate.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblDate.setForeground(Color.WHITE);
		lblDate.setBounds(64, 106, 60, 17);
		panel.add(lblDate);

		var today = Util.truncateDate(Calendar.getInstance());
		var sixteenDaysAhead = (Calendar) today.clone();
		sixteenDaysAhead.add(Calendar.DAY_OF_YEAR, 16);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(64, 135, 200, 26);
		dateChooser.setDate(today.getTime());

		dateChooser.setMaxSelectableDate(sixteenDaysAhead.getTime());
		panel.add(dateChooser);

		JLabel lblLocation = new JLabel("Location");
		lblLocation.setForeground(Color.WHITE);
		lblLocation.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblLocation.setBounds(64, 180, 121, 17);
		panel.add(lblLocation);

		locationField = new LocationField();
		locationField.setBounds(64, 209, 200, 26);
		panel.add(locationField);

		btnSaveSettings = new JButton("Apply");
		btnSaveSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fetchWeatherData();
			}
		});
		btnSaveSettings.setBounds(64, 711, 200, 27);
		panel.add(btnSaveSettings);

		//add celsisu/fahrenheit drop down
		JComboBox<String> tempUnit = new JComboBox<String>();
		tempUnit.addItem("Celsius");
		tempUnit.addItem("Fahrenheit");
		tempUnit.setBounds(64, 280, 200, 27);
		panel.add(tempUnit);

		//when pressed tempUnit drop down
		//change the temperature symbol in the settings
		tempUnit.addActionListener(e -> {
			Settings.temperatureSymbol = tempUnit.getSelectedIndex() == 0 ? "°C" : "°F";
		});



		// time format 24/12h drop down
		timeFormat = new JComboBox<String>();
		timeFormat.addItem("24H");
		timeFormat.addItem("12H");
		timeFormat.setBounds(64, 250, 200, 27);
		panel.add(timeFormat);

		//when pressed timeFormat drop down
		//change the time format in the settings
		timeFormat.addActionListener(e -> {
			if(is12HFormat()){
				Settings.timeFormat = "12H";
			}else{
				Settings.timeFormat = "24H";
			}
		});

		//reset all to default6
		JButton reset = new JButton("Reset");
		reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dateChooser.setDate(today.getTime());
				locationField.setSelectedIndex(0);
				timeFormat.setSelectedIndex(0);
				tempUnit.setSelectedIndex(0);
				fetchWeatherData();
			}
		});
		reset.setBounds(64, 320, 200, 27);
		panel.add(reset);



		//add save state button
		JButton btnSaveState = new JButton("Save State");
		btnSaveState.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				switch (Settings.saveState()){
					case 0:
						Messages.showError("Settings saved successfully");
						break;
					case 1:
						Messages.showError("Error saving settings");
						break;
					case 2:
						Messages.showError("No file selected");
						break;
					default:
						break;
				}

			}
	});
		btnSaveState.setBounds(64, 650, 200, 27);
		panel.add(btnSaveState);

		//add load state button
		JButton btnLoadState = new JButton("Load State");
		btnLoadState.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//load the state of the settings
				switch (Settings.loadState()){
					case 0:
						timeFormat.setSelectedItem(Settings.timeFormat);
						tempUnit.setSelectedItem(Settings.temperatureSymbol.equals("°C") ? "Celsius" : "Fahrenheit");
						fetchWeatherData();
						break;
					case 1:
						Messages.showError("Error loading settings");
						break;
					case 2:
						Messages.showError("No file selected");
						break;
					case 101:
						Messages.showError("temperatureSymbol not found in file");
						break;
					case 102:
						Messages.showError("timeFormat not found in file");
						break;
					default:
						break;
				}
			}
		});
		btnLoadState.setBounds(64, 620, 200, 27);
		panel.add(btnLoadState);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(MainWindow.class.getResource("/Images/temperature-icon.png")));
		label.setBounds(356, 199, 152, 149);
		frame.getContentPane().add(label);

		tempLabel = new JLabel("...");
		tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tempLabel.setFont(new Font("Dialog", Font.BOLD, 57));
		tempLabel.setBounds(492, 199, 174, 149);
		frame.getContentPane().add(tempLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(368, 381, 810, 99);
		frame.getContentPane().add(scrollPane);

		hourPanel = new JPanel();
		hourPanel.setForeground(new Color(255, 255, 255));
		hourPanel.setBorder(null);
		hourPanel.setBackground(new Color(255, 255, 255));
		scrollPane.setViewportView(hourPanel);
		hourPanel.setLayout(new BoxLayout(hourPanel, BoxLayout.X_AXIS));
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
		var location = (Location) locationField.getSelectedItem();
		currentWeatherData = new WeatherData(location.getLongitude(), location.getLatitude());

		var date = Calendar.getInstance();
		date.setTime(dateChooser.getDate());

		try {
			currentWeatherData.loadDaysWithHours(date, date, () -> {
				var day = currentWeatherData.getDailyData().get(0);
				var temp = day.getTemperature();
				var dayHours = day.getHours();
				



				// Temperature
				tempLabel.setText(String.format("%.0f %s", temp, Settings.temperatureSymbol));

				// TODO: Implement showing more fetched data in GUI

				// Display hour data
				hourPanel.removeAll();


				for (int dayHour = 0; dayHour < 24; dayHour++) {

					var hour = dayHours.get(dayHour);
					var period = "";

					if(Settings.temperatureSymbol.equals("°F")){
						temp = convertCelsiusToFahrenheit(temp);
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

					JLabel dataLabel = new JLabel(String.format("%.0f°", day.getTemperature()));
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

	private double convertCelsiusToFahrenheit(double temp) {
		// TODO Auto-generated method stub
		return (temp * 9/5) + 32;
	}

}
