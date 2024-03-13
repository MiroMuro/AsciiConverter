package AsciiWriter.Desktop;

import AsciiWriter.Controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
	static MainController controller;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Load the main.fxml file that contains the main layout of the application.
		Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));

		primaryStage.setTitle("AsciiWriter");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

	}

}