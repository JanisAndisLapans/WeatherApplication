package WeatherAppGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;

import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Util.APIQuery;
import Util.Messages;

public class LocationField extends JComboBox<Location> {

	private static final long serialVersionUID = -110676774074115141L;

	private JTextComponent editor;
	
	private final String APIUrlPlace = "https://maps.googleapis.com/maps/api/place";
	private final String Autocomplete = "autocomplete/json";
	private final String Details = "details/json";
    private final String APIKey = "AIzaSyDCWGTdf7HIrKAdSzIUX8B_Zb0RPn87Rn8";
    
    private boolean isLoaded;

	public LocationField() {
		super();
		this.setEditable(true);
	
		editor = (JTextComponent) this.getEditor().getEditorComponent();
		
		editor.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				fetchLocations();

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				fetchLocations();				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				fetchLocations();				
			}
			
		});		
		
		
		chooseCurrentLocation();
	}
	
	public void chooseCurrentLocation () {
		GetLocation currLoc = new GetLocation();
		this.addItem(new Location(currLoc.getCountry() + ", " + currLoc.getCity(), currLoc.getLat(), currLoc.getLon(), null, true));
	}
	
	private void fetchLocations() {
		var input = editor.getText();
		isLoaded = false;
		if(editor.getText().length() > 3) {
			try {
		        new APIQuery(APIUrlPlace)
				.function(Autocomplete)
				.addParam("input", input)
	            .addParam("key", APIKey)
				.exec(new APIQuery.APICallback() {
					public void run(int responseCode, BufferedReader jsonResult, String errorMessage) {
						try {
							if(responseCode != APIQuery.OK) {
								throw new RuntimeException(errorMessage);
							}
						
							fillAutocompleteDataFromJson(jsonResult);
							
						} catch (Exception e) {
							Messages.showError(e.getMessage());
						}
					}
				});
	        
			} catch (Exception e) {
				Messages.showError(e.getMessage());
			}
		}
	}
	
	private void fillAutocompleteDataFromJson(BufferedReader jsonReader) throws Exception {
		var json = (JSONObject) new JSONParser().parse(jsonReader);
		
		var predictions = (JSONArray) json.get("predictions");
		for (int i = 0; i < predictions.size(); i++) {
			var prediction = (JSONObject) predictions.get(i);
            
			var name = (String) prediction.get("description");
            var placeId = (String) prediction.get("place_id");
            this.addItem(new Location(name, 0, 0, null, false));
        }
	}
	
}
