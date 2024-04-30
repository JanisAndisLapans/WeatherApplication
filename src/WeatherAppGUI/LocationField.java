package WeatherAppGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import Util.Messages;

public class LocationField extends JComboBox<Location> {

	private static final long serialVersionUID = -110676774074115141L;

	private JTextComponent editor;

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
		// TODO: Implement setting current ip location 
	
		// Temporary test data:
		this.addItem(new Location("Latvia, Riga", 56.94889, 24.10639));
	}
	
	private void fetchLocations() {
		if(editor.getText().length() > 3) {
			// TODO: Implement fetch
		}
	}
	
}
