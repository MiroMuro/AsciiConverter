package AsciiWriter.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GifController {
	@FXML
	private Label testLabel;

	public void displayMessage(String message) {
		testLabel.setText("Swag: " + message);
	}
}
