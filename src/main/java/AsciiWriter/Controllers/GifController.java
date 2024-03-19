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
	private boolean flag = false;
	public void displayGif(Image gif) throws InterruptedException {
		gifView.setImage(gif);
		Writer.writeAsciiGifToFile();

	}



	@FXML
	public void startPainting() throws InterruptedException {

		Task<Void> task = new Task<Void>() {

			@Override

			protected Void call() throws Exception {

				// TODO Auto-generated method stub
				GifDecoder gifDecoder = new GifDecoder();
				gifDecoder.read("src/main/resources/gifs/ds.gif");
				int n = gifDecoder.getFrameCount();
				while (frameIndex < n - 1) {
					System.out.println(frameIndex + " " + n);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							BufferedImage frame = gifDecoder.getFrame(frameIndex);
							Writer.writeAsciiImageToFileNoOpeningPropmt(frame, 2, 1);
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
					Thread.sleep(100);
					gifTextArea.clear();
				}

				return null;
			}

		};
		new Thread(task).start();

	};



	public void stopAids(ActionEvent event) {
		flag = true;
		/*
		 * if (frameIndex <= n) { gifTextArea.clear(); BufferedImage frame =
		 * gifDecoder.getFrame(frameIndex);
		 * Writer.writeAsciiImageToFileNoOpeningPropmt(frame, 2, 1); try { File myObj =
		 * new File("src/main/resources/myFile.txt"); Scanner myReader = new
		 * Scanner(myObj);
		 *
		 * while (myReader.hasNextLine()) { String data = myReader.nextLine();
		 * gifTextArea.appendText(data + "\n"); } myReader.close();
		 *
		 * } catch (FileNotFoundException e) { System.out.println("An error occurred.");
		 * e.printStackTrace(); } frameIndex++; } else {
		 *
		 * }
		 */
	}
}
