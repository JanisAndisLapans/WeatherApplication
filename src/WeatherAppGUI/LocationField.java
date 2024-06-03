package WeatherAppGUI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
		
		this.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && isLoaded) {
                    var selectedItem = (Location) LocationField.this.getSelectedItem();
                    for (int i = 0; i < LocationField.this.getItemCount(); i++) {
                        Location item = LocationField.this.getItemAt(i);
                        if(item.getName().equals(selectedItem.getName())) {
                        	selectedItem.setPlaceId(item.getPlaceId());
                        	continue;
                        }
                    }
                    if (selectedItem.getPlaceId() != null && !selectedItem.getIsCoordsInit()) {
                    	fillCoordsForLocation(selectedItem);
                    }
                }
            }
        });
	}
	
	public void chooseCurrentLocation () {
		GetLocation currLoc = new GetLocation();
		this.addItem(new Location(currLoc.getCountry() + ", " + currLoc.getCity(), currLoc.getLat(), currLoc.getLon(), null, true));
	}
	
	private void fetchLocations() {
		var input = editor.getText();
		handleSelectedItem(input);
		try {
            Thread.sleep(15);
        } catch (InterruptedException e) {};
		isLoaded = false;
		if(this.isDisplayable() && editor.getText().length() > 3) {
			this.setPopupVisible(false);
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
		    this.setPopupVisible(true);
			} catch (Exception e) {
				Messages.showError(e.getMessage());
			}
		}
		isLoaded = true;
	}
	
	private void fillAutocompleteDataFromJson(BufferedReader jsonReader) throws Exception {
		var json = (JSONObject) new JSONParser().parse(jsonReader);
		
		var predictions = (JSONArray) json.get("predictions");
		for (int i = 0; i < predictions.size(); i++) {
			var prediction = (JSONObject) predictions.get(i);
            
			var name = (String) prediction.get("description");
            var placeId = (String) prediction.get("place_id");
            this.addItem(new Location(name, 0, 0, placeId, false));
        }
	}
	
	private void handleSelectedItem(String input) {
		var selectedItem = (Location) this.getSelectedItem();
		
		if (!selectedItem.getName().equals(input)) {
			this.setSelectedItem(new Location(input, 0, 0, null, false));
			selectedItem = (Location) this.getSelectedItem();
		};
		
		for (int i = 0; i < this.getItemCount(); i++) {
            Object item = this.getItemAt(i);
            if (!item.equals(selectedItem)) {
                this.removeItemAt(i);
            }
        }
	
	}
	
	private void fillCoordsForLocation(Location loc) {
		if(loc.getIsCoordsInit()) return;
		
		try {
	        new APIQuery(APIUrlPlace)
			.function(Details)
			.addParam("place_id", loc.getPlaceId())
            .addParam("key", APIKey)
			.exec(new APIQuery.APICallback() {
				public void run(int responseCode, BufferedReader jsonResult, String errorMessage) {
					try {
						if(responseCode != APIQuery.OK) {
							throw new RuntimeException(errorMessage);
						}
					
						var json = (JSONObject) new JSONParser().parse(jsonResult);
						var result = (JSONObject) json.get("result");
						var geometry = (JSONObject) result.get("geometry");
						var location = (JSONObject) geometry.get("location");
						
						loc.setLatitude((double) location.get("lat"));
						loc.setLongitude((double) location.get("lng"));
						loc.setIsCoordsInit(true);
						
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
