package AsciiWriter.Controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import AsciiWriter.Writer.Writer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController {
	@FXML
	private Label singleFileLabel;
	@FXML
	private Label myMessage;
	@FXML
	private ImageView myImage;

	@FXML
	public void generateRandom(ActionEvent event) {
		Random random = new Random();
		int myrand = random.nextInt(10) + 1;
		myMessage.setText(Integer.toString(myrand));
		// System.out.println(Integer.toString(myrand));
	}

	@FXML
	public void generateAsciiTextFile(ActionEvent event) {
		BufferedImage img = Writer.loadImage();
		Image image = SwingFXUtils.toFXImage(img, null);
		System.out.println(img.toString());
		myImage.setImage(image);
	}

	@FXML
	public void singleFileChooser(ActionEvent event) {
		FileChooser fc = new FileChooser();
		// Filter accepted image formats.
		fc.getExtensionFilters().add(new ExtensionFilter("Image files", "*.jpg", "*.png", "*.jpeg"));
		// Show the file chooser dialog.
		File file = fc.showOpenDialog(null);
		// If a file is seleted, copy it to the resources folder.
		if (file != null) {
			singleFileLabel.setText(file.getAbsolutePath());
			try {
				// Define the destination directory
				File destDir = new File("src/main/resources/images");
				if (!destDir.exists()) {
					destDir.mkdirs();
					System.out.println("Directory created " + destDir.getAbsolutePath());
				} else {
					System.out.println("Directory already exists " + destDir.getAbsolutePath());
				}

				// Define the destination file path
				String destinationFilePath = destDir.getAbsolutePath() + File.separator + file.getName();
				System.out.println("Destination file path: " + destinationFilePath);

				// Input and output streams for file copying.
				FileInputStream fis = new FileInputStream(file);
				FileOutputStream fos = new FileOutputStream(destinationFilePath);
				System.out.println("File input stream: " + fis);
				// Copy the file byte by byte.
				byte[] buffer = new byte[1024];
				int length;
				while ((length = fis.read(buffer)) > 0) {
					fos.write(buffer, 0, length);
				}

				fis.close();
				fos.close();

				System.out.println("File copied to: " + destinationFilePath);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

};
