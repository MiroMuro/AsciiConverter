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
		fontSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Font Size Slider value changed: " + newValue);
		});

		// 1000 / 50 = 20 frames per second
		// 1000 / 25 = 40 frames per second
		// 1000 / 100 = 10 frames per second
		// 1000 / 200 = 5 frames per second
		// 1000 / 300 = 3.33 frames per second
		// Thread.sleep(300); = 3.33 frames per second.
		// Thread.sleep(200); = 5 frames per second.
		// Thread.sleep(100); = 10 frames per second.
		// Thread.sleep(50); = 20 frames per second.
		// Thread.sleep(25); = 40 frames per second.
	}

	public void setFontSize(int fontSize) {
		gifTextArea.setStyle("-fx-font-size: " + fontSize + "px;");
	}
	public void displayGif(Image gif, String gifName) throws InterruptedException {
		gifView.setImage(gif);
		Writer.writeAsciiGifToFile(gifName);
		System.out.println("Gif Path: " + gifName);
		fileName = gifName;

	}

	@FXML
	public void startPainting() throws InterruptedException {

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				flag = true;
				GifDecoder gifDecoder = new GifDecoder();
				gifDecoder.read("src/main/resources/gifs/" + fileName);
				int n = gifDecoder.getFrameCount();
				int playbackSpeed = (int) playbackSpeedSlider.getValue();
				int verticalDensity = (int) verticalDensitySlider.getValue();
				int horizontalDensity = (int) horizontalDensitySlider.getValue();
				while (flag) {
					frameIndex = 0;
					while (frameIndex < n - 1) {
						System.out.println(frameIndex + " " + n);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								BufferedImage frame = gifDecoder.getFrame(frameIndex);
								Writer.writeAsciiImageToFileNoOpeningPropmt(frame, verticalDensity, horizontalDensity);
								try {
									File myObj = new File("src/main/resources/myFile.txt");
									Scanner myReader = new Scanner(myObj);

									while (myReader.hasNextLine()) {
										String data = myReader.nextLine();
										gifTextArea.appendText(data + "\n");
									}
									myReader.close();

								} catch (FileNotFoundException e) {
									System.out.println("An error occurred.");
									e.printStackTrace();
								}
								frameIndex++;

							}
						});
						Thread.sleep(playbackSpeed);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								gifTextArea.clear();
							}
						});
					}
				}


				return null;
			}

		};
		new Thread(task).start();


	};

	@FXML
	public void clickGifButton(ActionEvent event) throws InterruptedException {
		flag = true;
		while (flag) {
			startPainting();
		}
	}

	@FXML
	public void stopPaintingGif(ActionEvent event) {
		flag = false;

	}
}
