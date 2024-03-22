package AsciiWriter.Controllers;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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
	public void selectNewGif(ActionEvent event) {
		flag = false;
		FileChooser fc = new FileChooser();
		// Add filter for gif files.
		fc.getExtensionFilters().add(new ExtensionFilter("GIF files", "*.gif"));
		// Show the file chooser dialog.
		File file = fc.showOpenDialog(null);
		// If a file is seleted, copy it to the resources folder.
		if (file != null) {
			try {
				File destDir = new File("src/main/resources/gifs");
				if (!destDir.exists()) {
					destDir.mkdir();
					System.out.println("Directory created: " + destDir.getAbsolutePath());
				} else {
					System.out.println("Directory already exists: " + destDir.getAbsolutePath());
				}
				// Define the destination file path
				String destinationFilePath = destDir.getAbsolutePath() + File.separator + file.getName();

				// Input and output streams for file copying.
				FileInputStream fis = new FileInputStream(file);
				FileOutputStream fos = new FileOutputStream(destinationFilePath);
				// Copy the file byte by byte.
				byte[] buffer = new byte[1024];
				int length;
				// Reads up to buffer.length bytes of data from this inputstream into an array
				// of bytes.
				// the total number of bytes read into the buffer,
				// or -1 if there is no more data because the end of the file has been reached.
				while ((length = fis.read(buffer)) > 0) {
					fos.write(buffer, 0, length);
				}
				fis.close();
				fos.close();
				System.out.println("File copied to: " + destinationFilePath);
				// If the gif was copeid succesfully, open it in a new window.
				File newFile = new File(destinationFilePath);
				if (newFile.exists()) {
					System.out.println("Gif file loaded succesfully.");
					displayGif(new Image(newFile.toURI().toString()), newFile.getName());

				} else {

					System.out.println("File does not exist: " + newFile.getAbsolutePath());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

}
