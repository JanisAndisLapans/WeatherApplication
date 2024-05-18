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
	private JButton timeFormat;
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
		
		btnSaveSettings = new JButton("Save settings");
		btnSaveSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fetchWeatherData();
			}
		});
		btnSaveSettings.setBounds(64, 711, 200, 27);
		panel.add(btnSaveSettings);

		//text label for time format
		JLabel lblTimeFormat = new JLabel("Time format");
		lblTimeFormat.setForeground(Color.WHITE);
		lblTimeFormat.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblTimeFormat.setBounds(64, 621, 121, 17);
		panel.add(lblTimeFormat);

		timeFormat = new JButton("24H");
		timeFormat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(timeFormat.getText().equals("24H")) {
					timeFormat.setText("12H");
					fetchWeatherData();
				}
				else {
					timeFormat.setText("24H");
					fetchWeatherData();
				}
				

			}
		});
		timeFormat.setBounds(64, 650, 200, 27);
		panel.add(timeFormat);

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


	

	
	private void fetchWeatherData() {
		var location = (Location)locationField.getSelectedItem();
		currentWeatherData = new WeatherData(location.getLongitude(), location.getLatitude());
		
		var date = Calendar.getInstance();
		date.setTime(dateChooser.getDate());
		
		try {
			currentWeatherData.loadDaysWithHours(date, date, () -> {
				var day = currentWeatherData.getDailyData().get(0);
				
				// Temperature
				tempLabel.setText(String.format("%.0f %s", day.getTemperature(), Settings.temperatureSymbol));
				
				// TODO: Implement showing more fetched data in GUI
				
				// Display hour data
				
				hourPanel.removeAll();
				var dayHours = day.getHours();

				var is24H = timeFormat.getText().equals("12H") ? false : true;

				
				
				for(int dayHour =0 ; dayHour < 24; dayHour++) {

					var hour = dayHours.get(dayHour);
					var period = "";
				
					if(!is24H) {
						period = dayHour < 12 ? "AM" : "PM";
						hour.hour = hour.hour % 12;
					}

					JPanel hourDataElement = new JPanel();
					hourDataElement.setBackground(Color.WHITE);
					hourDataElement.setLayout(null);




					JLabel hourLabel = new JLabel((hour.hour < 10 ? "0" : "") + Integer.toString(hour.hour) + ":00" + period);
					hourLabel.setFont(new Font("Dialog", Font.BOLD, 20));
					hourLabel.setHorizontalAlignment(SwingConstants.CENTER);
					hourLabel.setBounds(0, 12, 110, 17);
					hourDataElement.add(hourLabel);
					
					JLabel dataLabel = new JLabel(String.format("%.0f°", hour.getTemperature()));
					dataLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
					dataLabel.setHorizontalAlignment(SwingConstants.CENTER);
					dataLabel.setBounds(0, 55, 110, 17);
					hourDataElement.add(dataLabel);
					
					// Make sure jPanel stays the same size in box layout and scrolling works properly by setting fixed size
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
