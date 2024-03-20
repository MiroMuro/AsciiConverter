package AsciiWriter.Controllers;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.madgag.gif.fmsware.GifDecoder;

import AsciiWriter.Writer.Writer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GifController {

	@FXML
	private ImageView gifView;
	@FXML
	public TextArea gifTextArea;
	@FXML
	private Button gifButton;
	@FXML
	public int frameIndex = 0;
	@FXML
	private Slider verticalDensitySlider;
	@FXML
	private Slider horizontalDensitySlider;
	@FXML
	private Slider playbackSpeedSlider;
	@FXML
	private Slider fontSizeSlider;
	private boolean flag = false;
	private String fileName;

	public void initialize() {
		// Initialize and add listeners to the ascii density value sliders.
		verticalDensitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Vertical Slider value changed: " + newValue);
		});
		horizontalDensitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Horizontal Slider value changed: " + newValue);
		});
		playbackSpeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Playback Speed Slider value changed: " + newValue);
		});
		// Add a listener to the font size slider.
		fontSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			gifTextArea.setStyle("-fx-font-size: " + newValue + "px;");
			System.out.println(gifTextArea.getStyle());
			System.out.println("Font Size Slider value changed: " + newValue);
		});

	}

	@FXML
	public void setFontSize(int fontSize) {
		gifTextArea.setStyle("-fx-font-size: " + fontSize + "px;");
		System.out.println(gifTextArea.getStyle());
	}
	public void displayGif(Image gif, String gifName) throws InterruptedException {
		gifView.setImage(gif);
		System.out.println("Gif Path: " + gifName);
		fileName = gifName;

	}
	@FXML
	public void paintGifToAscii(ActionEvent event) throws InterruptedException {
		flag = true;
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				GifDecoder gifDecoder = new GifDecoder();
				gifDecoder.read("src/main/resources/gifs/" + fileName);
				int frameCount = gifDecoder.getFrameCount();
				int playbackSpeed = (int) playbackSpeedSlider.getValue();
				int verticalDensity = (int) verticalDensitySlider.getValue();
				int horizontalDensity = (int) horizontalDensitySlider.getValue();

				while (flag) {
					for (frameIndex = 0; frameIndex < frameCount && flag; frameIndex++) {
						BufferedImage gifFrame = gifDecoder.getFrame(frameIndex);
						renderGifFrameAsAscii(gifFrame, verticalDensity, horizontalDensity);
						Thread.sleep(playbackSpeed);
						clearGifTextArea();
					}
				}
				return null;
			}

		};

		new Thread(task).start();
	}

	private void clearGifTextArea() {
		// Gui updates must be handled in the JavaFX Application Thread.
		// Platform.runLater uses the JavaFX Application Thread.
		Platform.runLater(gifTextArea::clear);
	}

	private void renderGifFrameAsAscii(BufferedImage gifFrame, int verticalDensity, int horizontalDensity) {
		Platform.runLater(() -> {
			// Write the current gif frame as ascii to the file.
			Writer.writeAsciiImageToFileNoOpeningPropmt(gifFrame, verticalDensity, horizontalDensity);
			appendAsciiFrameFromFile();
		});

	}

	private void appendAsciiFrameFromFile() {
		// Read the current gif ascii frame from the file and append it to the text
		// area.
		try {
			File myObj = new File("src/main/resources/myFile.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				gifTextArea.appendText(data + "\n");
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred. File was not found.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	};

	@FXML
	public void clickGifButton(ActionEvent event) throws InterruptedException {
		flag = true;
		// startPainting();
		paintGifToAscii(event);

	}

	@FXML
	public void stopPaintingGif(ActionEvent event) {
		flag = false;

	}
	/*
	 * @FXML public void startPainting() throws InterruptedException { // Create a
	 * new Task (that runs on separate thread from the JavaFX) to handle // the gif
	 * painting. // The GUI must be updated in the JavaFX Application Thread, and //
	 * In this case, that thread is accessed through the Platform.runLater method.
	 * Task<Void> task = new Task<Void>() { // The interval between frame rendering
	 * is handled by // Thread.sleep(playbackSpeed). // But we cannot sleep the
	 * JavaFX Application Thread or else the app will // freeze, so we use a Task //
	 * to encapsulate the Platform.runLater method, and sleep the Task thread //
	 * instead. This ensures that the gif renders, unrenders and the GUI is updated
	 * // at the same time.
	 *
	 * @Override protected Void call() throws Exception { flag = true; GifDecoder
	 * gifDecoder = new GifDecoder(); gifDecoder.read("src/main/resources/gifs/" +
	 * fileName); int n = gifDecoder.getFrameCount(); int playbackSpeed = (int)
	 * playbackSpeedSlider.getValue(); int verticalDensity = (int)
	 * verticalDensitySlider.getValue(); int horizontalDensity = (int)
	 * horizontalDensitySlider.getValue(); while (flag) { frameIndex = 0; // This
	 * while loop writes the gif as ascii art to the gifTextArea. while (frameIndex
	 * < n - 1) { System.out.println(frameIndex + " " + n); // This updates the GUI
	 * in the JavaFX Application Thread. Platform.runLater(new Runnable() {
	 *
	 * @Override public void run() { BufferedImage frame =
	 * gifDecoder.getFrame(frameIndex);
	 * Writer.writeAsciiImageToFileNoOpeningPropmt(frame, verticalDensity,
	 * horizontalDensity); try { File myObj = new
	 * File("src/main/resources/myFile.txt"); Scanner myReader = new Scanner(myObj);
	 *
	 * while (myReader.hasNextLine()) { String data = myReader.nextLine();
	 * gifTextArea.appendText(data + "\n"); } myReader.close();
	 *
	 * } catch (FileNotFoundException e) { System.out.println("An error occurred.");
	 * e.printStackTrace(); } frameIndex++;
	 *
	 * } }); // This sleeps the Task thread, not the JavaFX Application Thread.
	 * Thread.sleep(playbackSpeed); // Clear the screen for the next frame. As an
	 * GUI update must be handled in the // JavaFX Application Thread, which
	 * PlatForm.runLater uses. Platform.runLater(new Runnable() {
	 *
	 * @Override public void run() { gifTextArea.clear(); } }); } }
	 *
	 *
	 * return null; }
	 *
	 * }; new Thread(task).start();
	 *
	 *
	 * };
	 */

}
