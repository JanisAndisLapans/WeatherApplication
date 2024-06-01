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
import javax.swing.JTextArea;

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
	private JButton timeFormat;
	private JLabel tempLabel;
	private JLabel windSpeedLabel;
	private JLabel precipitaionLabel;
	private LocationField locationField;
	private JDateChooser dateChooser;
	private JPanel hourPanel;
	private String type = "t"; // god forgive me

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

		btnSaveSettings = new JButton("Save settings");
		btnSaveSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fetchWeatherData();
			}
		});
		btnSaveSettings.setBounds(64, 711, 200, 27);
		panel.add(btnSaveSettings);

		// text label for time format
		JLabel lblTimeFormat = new JLabel("Time format");
		lblTimeFormat.setForeground(Color.WHITE);
		lblTimeFormat.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblTimeFormat.setBounds(64, 621, 121, 17);
		panel.add(lblTimeFormat);


		//reset all to default
		JButton reset = new JButton("Reset");
		reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dateChooser.setDate(today.getTime());
				locationField.setSelectedIndex(0);
				timeFormat.setText("24H");
				fetchWeatherData();
			}
		});
		reset.setBounds(64, 690, 200, 27);
		panel.add(reset);

		timeFormat = new JButton("24H");
		timeFormat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (timeFormat.getText().equals("24H")) {
					timeFormat.setText("12H");
					fetchWeatherData();
				} else {
					timeFormat.setText("24H");
					fetchWeatherData();
				}

			}
		});
		timeFormat.setBounds(64, 650, 200, 27);
		panel.add(timeFormat);
		
		// Heading text current location
		JLabel headingLocation = new JLabel("Location");
		headingLocation.setText(today.getTime().toString());
		headingLocation.setForeground(Color.BLACK);
		headingLocation.setBounds(391, 26, 361, 59);
		headingLocation.setFont(new Font("Arial", Font.BOLD, 50));
		frame.getContentPane().add(headingLocation);
		
		// Heading text current date
		JLabel headingDate = new JLabel("Date");
		headingDate.setText(today.getTime().toString());
		headingDate.setForeground(Color.BLACK);
		headingDate.setBounds(391, 81, 334, 43);
		headingDate.setFont(new Font("Arial", Font.PLAIN, 20));
		frame.getContentPane().add(headingDate);
		
		// Heading text "Average"
		JLabel headingAverage = new JLabel("Average");
		headingAverage.setForeground(Color.BLACK);
		headingAverage.setBounds(391, 144, 188, 43);
		headingAverage.setFont(new Font("Arial", Font.BOLD, 30));
		frame.getContentPane().add(headingAverage);
		
		// Heading text "Hourly"
		JLabel headingHourly = new JLabel("Hourly");
		headingHourly.setForeground(Color.BLACK);
		headingHourly.setBounds(391, 402, 188, 43);
		headingHourly.setFont(new Font("Arial", Font.BOLD, 30));
		frame.getContentPane().add(headingHourly);

		JLabel tempIcon = new JLabel("");
		tempIcon.setIcon(new ImageIcon(MainWindow.class.getResource("/Images/Temperature-Icon.png")));
		tempIcon.setBounds(391, 179, 152, 149);
		frame.getContentPane().add(tempIcon);
		
		JLabel windIcon = new JLabel("");
		windIcon.setIcon(new ImageIcon(MainWindow.class.getResource("/Images/Wind-Icon.png")));
		windIcon.setBounds(600, 179, 152, 149);
		frame.getContentPane().add(windIcon);
		
		JLabel precipitationIcon = new JLabel("");
		precipitationIcon.setIcon(new ImageIcon(MainWindow.class.getResource("/Images/Precipitation-Icon.png")));
		precipitationIcon.setBounds(854, 179, 152, 149);
		frame.getContentPane().add(precipitationIcon);

		tempLabel = new JLabel("...");
		tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tempLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		tempLabel.setBounds(425, 179, 174, 149);
		frame.getContentPane().add(tempLabel);
		
		windSpeedLabel = new JLabel("...");
		windSpeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		windSpeedLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		windSpeedLabel.setBounds(668, 179, 174, 149);
		frame.getContentPane().add(windSpeedLabel);
		
		precipitaionLabel = new JLabel("...");
		precipitaionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		precipitaionLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		precipitaionLabel.setBounds(909, 179, 174, 149);
		frame.getContentPane().add(precipitaionLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(391, 457, 768, 99);
		frame.getContentPane().add(scrollPane);

		hourPanel = new JPanel();
		hourPanel.setForeground(new Color(255, 255, 255));
		hourPanel.setBorder(null);
		hourPanel.setBackground(new Color(255, 255, 255));
		scrollPane.setViewportView(hourPanel);
		hourPanel.setLayout(new BoxLayout(hourPanel, BoxLayout.X_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(391, 338, 790, 1);
		panel_1.setBackground(new Color(0, 0, 0));
		frame.getContentPane().add(panel_1);
		
		JButton btnTemp = new JButton("Temp");
		btnTemp.setBounds(438, 616, 175, 45);
		frame.getContentPane().add(btnTemp);
		
		JButton btnWind = new JButton("Wind");
		btnWind.setBounds(668, 616, 175, 45);
		frame.getContentPane().add(btnWind);
		
		JButton btnPrecipitation = new JButton("Precipitation");
		btnPrecipitation.setBounds(908, 616, 175, 45);
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

	private boolean is12H() {
		return timeFormat.getText().equals("12H");
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

				// Temperature
				tempLabel.setText(String.format("%.0f %s", day.getTemperature(), Settings.temperatureSymbol));
				windSpeedLabel.setText(String.format("%.0f %s", day.getWindSpeed(), Settings.windSpeedSymbol));
				precipitaionLabel.setText(String.format("%.0f %s", day.getPrecipitaion(), Settings.precipitationSymbol));

				// TODO: Implement showing more fetched data in GUI

				// Display hour data

				hourPanel.removeAll();
				var dayHours = day.getHours();
				double value = 0;
				String symbol = null;
				for (int dayHour = 0; dayHour < 24; dayHour++) {

					var hour = dayHours.get(dayHour);
					var period = "";

					if (type == "t") {
						value = hour.getTemperature();
						symbol = "%.0f°";
					}
					else if(type == "w") {
						value = hour.getWindSpeed();
						symbol = "%.0fkm/h";
					}
					else if(type == "p") {
						value = hour.getPrecipitaion();
						symbol = "%.0f%%";
					}
					
					
					// API returns 0 as 12th hour
					if (hour.hour == 0 && dayHour == 12) {
						hour.hour = 12;
					}
					// Format period
					if (is12H()) {
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

					JLabel dataLabel = new JLabel(String.format(symbol, value)); //šeit jamaina
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
