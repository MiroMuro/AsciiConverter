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
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController {
	@FXML
	private Label myMessage;
	@FXML
	private ImageView myImage;
	@FXML
	private Image currentImage;
	@FXML
	private Slider verticalDensitySlider;
	@FXML
	private Slider horizontalDensitySlider;

	public void initialize() {
		verticalDensitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Vertical Slider value changed: " + newValue);
		});
		horizontalDensitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("Horizontal Slider value changed: " + newValue);
		});
	}

	@FXML
	public void generateRandom(ActionEvent event) {
		Random random = new Random();
		int myrand = random.nextInt(10) + 1;
		myMessage.setText(Integer.toString(myrand));
		// System.out.println(Integer.toString(myrand));
	}

	@FXML
	public void loadExamplePhoto(ActionEvent event) {
		BufferedImage img = Writer.loadDefaultImage();
		Image image = SwingFXUtils.toFXImage(img, null);
		myImage.setImage(image);
		currentImage = myImage.getImage();
	}

	@FXML
	public void loadUneditedPhoto(ActionEvent event) {
		if (currentImage != null) {
			myImage.setImage(currentImage);
		} else {
			myMessage.setText("Error. No original image loaded.");
		}

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
				// Copy the file byte by byte.
				byte[] buffer = new byte[1024];
				int length;
				while ((length = fis.read(buffer)) > 0) {
					fos.write(buffer, 0, length);
				}

				fis.close();
				fos.close();
				System.out.println("File copied to: " + destinationFilePath);

				File newFile = new File(destinationFilePath);
				if (newFile.exists()) {
					myImage.setImage(new Image(newFile.toURI().toString()));
					currentImage = myImage.getImage();
					System.out.println("Curent image set to: " + currentImage.toString());
				} else {
					System.out.println("File does not exist: " + newFile.getAbsolutePath());
				}



			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void convertImageToMonohrome(ActionEvent event) {
		// Convert the current image to BufferedImage and then to monochrome.
		// Then convert the monochrome bufferedImage back to an image and set it to the
		// image view.
		if (currentImage == null) {
			myMessage.setText("Error. No image loaded.");
		} else {
			BufferedImage img = Writer.convertImageToMonochrome(SwingFXUtils.fromFXImage(currentImage, null));
			myImage.setImage(SwingFXUtils.toFXImage(img, null));
			System.out.println("Image converted to monochrome." + currentImage.toString());
		}

	}

	@FXML
	public void convertImageToGrayScale(ActionEvent event) {
		if (currentImage == null) {
			myMessage.setText("Error. No image loaded.");
		} else {
			BufferedImage img = Writer.transferImageToGreyScale(SwingFXUtils.fromFXImage(currentImage, null));
			// Then convert the grayscale bufferedImage back to an image and set it to the
			// image view.
			myImage.setImage(SwingFXUtils.toFXImage(img, null));
			System.out.println("Image converted to grayscale." + currentImage.toString());
		}
		// Convert the current image to BufferedImage and then to grayscale.

	}

	@FXML
	public void printImageAsAsciiToConsole(ActionEvent event) {
		if (currentImage == null) {
			myMessage.setText("Error. No image loaded.");
		} else {
			// Transfer the current image to a buffered image and then print it to the
			// console.
			BufferedImage image = SwingFXUtils.fromFXImage(currentImage, null);
			Writer.writeAsciiImageToConsole(image, (int) verticalDensitySlider.getValue(),
					(int) horizontalDensitySlider.getValue());
		}
	}

	@FXML
	public void printImageAsAsciiToFile(ActionEvent event) {
		if (currentImage == null) {
			myMessage.setText("Error. No image loaded.");
		} else {
			// Transfer the current displayed image to a buffered image and then print it to
			// the
			// console.
			BufferedImage image = SwingFXUtils.fromFXImage(currentImage, null);
			Writer.writeAsciiImageToFile(image, (int) verticalDensitySlider.getValue(),
					(int) horizontalDensitySlider.getValue());
		}
	}

	@FXML
	public void test(ActionEvent event) {
		System.out.println("Slider value test");
	}


};
