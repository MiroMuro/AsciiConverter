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
		// TODO Auto-generated method stub
		// Main.main(null);
		/*
		 * Button printAsciiToTextFile = new
		 * Button("Print this image as ASCII to a text file.");
		 *
		 * printAsciiToTextFile.setOnAction((event) -> {
		 * System.out.println("Print this image as ASCII to a text file.");
		 * System.out.println("Event: " + event.toString()); });
		 *
		 * Button lambdaTest = new Button("Lambda test");
		 *
		 * lambdaTest.setOnAction((event) -> { System.out.println("Lambda test"); });
		 *
		 * Button btn = new Button("Say 'Hello World'");
		 *
		 * btn.setOnAction(new EventHandler<ActionEvent>() {
		 *
		 * @Override public void handle(ActionEvent event) {
		 * System.out.println("Hello World!"); } });
		 *
		 * Button exitButton = new Button("Close application"); exitButton.setOnAction(e
		 * -> { System.out.println("Exit button pressed. Closing application.");
		 *
		 * System.exit(0); });
		 *
		 * primaryStage.setTitle("AsciiWriter"); VBox root = new VBox(); //
		 * root.getChildren().add(btn); root.getChildren().addAll(btn,
		 * printAsciiToTextFile, exitButton); // root.getChildren().add(lambdaTest);
		 */
		// FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));

		Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
		// controller = (MainController) loader.getController();

		primaryStage.setTitle("AsciiWriter");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

	}

}