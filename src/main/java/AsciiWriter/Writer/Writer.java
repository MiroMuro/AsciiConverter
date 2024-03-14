package AsciiWriter.Writer;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Writer {
	// The threshold value for a pixel to be considered white or black.
	public final static int THRESHOLD = 280;

	public static BufferedImage loadDefaultImage() {
		try {
			int newWidth;
			int newHeight;
			// Read the image from the file.
			BufferedImage originalImage = ImageIO.read(Writer.class.getResource("/monalisa.jpg"));
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();
			// Calculate the ratio of the image.
			double ratio = (double) width / height;
			// If the width is greater than the height, scale the width down.
			// For landscape photos.
			if (ratio > 1.0) {
				newWidth = (int) (width * (ratio - 1));
				newHeight = (int) (height * (ratio - 1));
			} else {
				// If the height is greater than the width, scale the height down.
				// For portrait photos.
				newWidth = (int) (width * (1 - ratio));
				newHeight = (int) (height * (1 - ratio));
			}


			int type = originalImage.getType();

			// Create a new buffered image with scaled dimensions.
			BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
			// Create a graphics2D object from the resized image and draw the original image
			// to it.
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
			g.dispose();

			return resizedImage;
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;

	}





	public static BufferedImage convertImageToGrayScale(BufferedImage image) {
		// Loop through all the pixels in the image and convert them to grayscale.
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				// Get the color of the pixel at x, y.
				Color color = new Color(image.getRGB(x, y), true);

				// Create a grayscale pixel using the luminosity method described here:
				// http://www.johndcook.com/blog/2009/08/24/algorithms-convert-color-grayscale/
				int grayscalePixel = (int) ((0.21 * color.getRed()) + (0.71 * color.getGreen())
						+ (0.07 * color.getBlue()));

				// Invert the colors.
				// color = new Color(255- color.getRed(),255-
				// color.getGreen(),255-color.getBlue());

				// Create a new fade of gray with the grayscale pixel.
				Color grayScaleColor = new Color(grayscalePixel, grayscalePixel, grayscalePixel);

				// Set the new grayscale color to the pixel.
				image.setRGB(x, y, grayScaleColor.getRGB());
			}
		}

		return image;
	};

	public static BufferedImage convertImageToMonochrome(BufferedImage image) {
		// Loop through all the pixels in the image and convert them to monochrome.

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color colorAtPixel = new Color(image.getRGB(x, y), true);
				// If the sum of the red, green, and blue values of the pixel is greater than
				// the threshold, set the pixel to white. Otherwise, set it to black.
				// Threshold is completely arbitrary and can be set to any value.
				if (colorAtPixel.getRed() + colorAtPixel.getGreen() + colorAtPixel.getBlue() > THRESHOLD) {
					image.setRGB(x, y, Color.WHITE.getRGB());
				} else {
					image.setRGB(x, y, Color.BLACK.getRGB());
				}

			}
		}
		return image;
	}



	public static void writeAsciiImageToFile(BufferedImage image, int verticalDensity, int horizontalDensity) {
		// Transfer the image to grayscale for more accurate ascii representation.
		BufferedImage newImage = convertImageToGrayScale(image);
		// Instantiate a new file object with the desired file path and the utf8
		// charset.
		Charset utf8 = Charset.forName("UTF-8");
		File file = new File("src/main/resources/myFile.txt");
		// Set the buffer size to 16kb. Dense prints can be quite large.
		int bufferSize = 16 * 1024;
		// Create a buffered writer that writes the characters to an OutputStreamwriter
		// that transfers the characters to a into utf8 copmliant bytes and
		// writes these bytes to the fileOutPutStream that writes to the file.

		try (BufferedWriter buffWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), utf8), bufferSize)) {

			for (int y = 0; y < newImage.getHeight(); y += verticalDensity) {
				// Create a new line for each row of pixels.
				buffWriter.newLine();
				for (int x = 0; x < newImage.getWidth(); x += horizontalDensity) {
					Color pixelColor = new Color(newImage.getRGB(x, y), true);
					// Create a grayscale pixel using the luminosity method described here:
					// http://www.johndcook.com/blog/2009/08/24/algorithms-convert-color-grayscale/
					// and get its luminosity value as an float between 0 and 1.
					double luminosityOfPixel = ((0.21 * pixelColor.getRed()) + (0.72 * pixelColor.getGreen())
							+ (0.07 * pixelColor.getBlue())) / 255;
					// Get the corresponding character for the luminosity value and write it to the
					// bufferered writer.
					buffWriter.write(getAsciiCharacter(luminosityOfPixel));

				}
			}
			System.out.println("Successfully wrote to the file.");
			// just to make sure the buffer is flushed and the file is closed.
			buffWriter.close();
			// Open the file if it exists.
			openNewAsciiFileIfPresent(file);

		} catch (IOException ioe) {
			System.out.println("Printing to file failed: ");
			ioe.printStackTrace();
		}

	}

	public static void writeAsciiEmojiImageToFile(BufferedImage image, int verticalDensity, int horizontalDensity) {

		// Instantiate a new file object with the desired file path and the utf8
		// charset.
		Charset utf8 = Charset.forName("UTF-8");
		File file = new File("src/main/resources/myFile.txt");
		// Set the buffer size to 16kb. Dense prints can be quite large.
		int bufferSize = 16 * 1024;
		// Create a buffered writer that writes the characters to an OutputStreamwriter
		// that transfers the characters to a into utf8 copmliant bytes and
		// writes these bytes to the fileOutPutStream that writes to the file.

		try (BufferedWriter buffWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), utf8), bufferSize)) {

			for (int y = 0; y < image.getHeight(); y += verticalDensity) {
				// Create a new line for each row of pixels.
				buffWriter.newLine();
				for (int x = 0; x < image.getWidth(); x += horizontalDensity) {
					Color pixelColor = new Color(image.getRGB(x, y), true);
					// Create a grayscale pixel using the luminosity method described here:
					// http://www.johndcook.com/blog/2009/08/24/algorithms-convert-color-grayscale/
					// and get its luminosity value as an float between 0 and 1.
					double luminosityOfPixel = ((0.21 * pixelColor.getRed()) + (0.72 * pixelColor.getGreen())
							+ (0.07 * pixelColor.getBlue())) / 255;
					// Get the corresponding character for the luminosity value and write it to the
					// bufferered writer.
					String emoji = getEmojiCharacter(luminosityOfPixel);
					buffWriter.write(emoji, 0, emoji.length());

				}
			}
			System.out.println("Successfully wrote to the file.");
			// just to make sure the buffer is flushed and the file is closed.
			buffWriter.close();
			// Open the file if it exists.
			openNewAsciiFileIfPresent(file);

		} catch (IOException ioe) {
			System.out.println("Printing to file failed: ");
			ioe.printStackTrace();
		}

	}

	public static void writeAsciiImageToConsole(BufferedImage image, int verticalDensity, int horizontalDensity) {
		// Control the density of ascii characters printed to the console.
		// Denser prints will result in a more detailed image. But will take longer to
		// print and consume more space in the console.
		for (int y = 0; y < image.getHeight(); y += verticalDensity) {
			System.out.println("\n");
			for (int x = 0; x < image.getWidth(); x += horizontalDensity) {
				Color pixelColor = new Color(image.getRGB(x, y), true);
				// Create a grayscale pixel using the luminosity method described here:
				// http://www.johndcook.com/blog/2009/08/24/algorithms-convert-color-grayscale/
				// and get its luminosity value as an float between 0 and 1.
				double luminosityOfPixel = ((0.21 * pixelColor.getRed()) + (0.72 * pixelColor.getGreen())
						+ (0.07 * pixelColor.getBlue())) / 255;
				// Get the corresponding character for the luminosity value and write it to the
				// console
				System.out.print(getAsciiCharacter(luminosityOfPixel));

			}
		}
	}

	public static void openNewAsciiFileIfPresent(File newAsciiFile) {
		// Check that desktop is available and the file exists.
		if (!Desktop.isDesktopSupported()) {
			System.out.println("Desktop is not supported, cannot open file.");
			return;
		}
		if (!newAsciiFile.exists()) {
			System.out.println("File does not exist");
			return;
		}

		try {
			// Get the size of the file in bytes.
			double bytes = Files.size(newAsciiFile.toPath());
			showConfirmationAlert(newAsciiFile, bytes);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void openFile(File file) {
		// Open the file with the default desktop application.
		try {
			Desktop desktop = Desktop.getDesktop();
			desktop.open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	};

	private static void showConfirmationAlert(File newAsciiFile, double bytes) {
		// Show a confirmation dialog that asks if the user wants to open the new ascii
		// file.
		// If the user clicks OK, the file will be opened.
		// Also show the size of the file in bytes, kilobytes and megabytes, formatted
		// to
		// two decimal places.
		DecimalFormat f = new DecimalFormat("##.00");
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("File created");
		alert.setHeaderText("An ASCII file was created at " + newAsciiFile.getAbsolutePath());
		alert.setContentText("Do you want to open the file? \n\n" + "ASCII file size in bytes: " + bytes + " B \n"
				+ "ASCII file size in kilobytes: " + f.format(bytes / 1024) + " KB \n"
				+ "ASCII file size in megabytes: " + f.format(bytes / 1024 / 1024) + " MB \n");
		Optional<ButtonType> result = alert.showAndWait();
		// If the user clicks OK, open the file.
		// If the user clicks cancel, do nothing.
		if (result.isPresent() && result.get() == ButtonType.OK) {
			openFile(newAsciiFile);
		} else {
			System.out.println("User cancelled the operation.");
		}

	}

	public static char getAsciiCharacter(double luminosityValue) {
		// Get the corresponding character for the luminosity value.
		// Darker values are represented by thicker characters as luminosityValue
		// approaches 0.
		if (luminosityValue >= 0.95) {
			return '.';
		}
		if (luminosityValue >= 0.9) {
			return '+';
		}
		if (luminosityValue >= 0.85) {
			return ',';
		}
		if (luminosityValue >= 0.8) {
			return '"';
		}
		if (luminosityValue >= 0.75) {
			return ':';
		}
		if (luminosityValue >= 0.7) {
			return ';';
		}
		if (luminosityValue >= 0.65) {
			return '?';
		}
		if (luminosityValue >= 0.6) {
			return '!';
		}
		if (luminosityValue >= 0.55) {
			return '^';
		}
		if (luminosityValue >= 0.5) {
			return '`';
		}
		if (luminosityValue >= 0.45) {
			return '\'';
		}
		if (luminosityValue >= 0.4) {
			return ',';
		}
		if (luminosityValue >= 0.35) {
			return '&';
		}
		if (luminosityValue >= 0.3) {
			return '=';
		}
		if (luminosityValue >= 0.25) {
			return '*';
		}
		if (luminosityValue >= 0.2) {
			return '#';
		}
		if (luminosityValue >= 0.15) {
			return '~';
		}
		if (luminosityValue >= 0.1) {
			return 'z';
		}
		if (luminosityValue >= 0.05) {
			return '-';
		}
		return '@';
	}

	public static String getEmojiCharacter(double luminosityValue) {
		if (luminosityValue >= 0.9) {
			String s = "🚗";
			return s;
		}
		if (luminosityValue >= 0.8) {
			String s = "🚓";
			return s;
		}
		if (luminosityValue >= 0.7) {
			String s = "🚕";
			return s;
		}
		if (luminosityValue >= 0.6) {
			String s = "🚙";
			return s;
		}
		if (luminosityValue >= 0.5) {
			String s = "🚐";
			return s;
		}
		if (luminosityValue >= 0.4) {
			String s = "🚚";
			return s;
		}
		if (luminosityValue >= 0.3) {
			String s = "🚑";
			return s;
		}
		if (luminosityValue >= 0.2) {
			String s = "🚒";
			return s;
		}
		if (luminosityValue >= 0.1) {
			String s = "🚌";
			return s;
		}
		if (luminosityValue <= 0.1 && luminosityValue >= 0.08) {
			String s = "🚝";
			return s;
		}
		if (luminosityValue <= 0.08 && luminosityValue >= 0.06) {
			String s = "🚠";
			return s;
		}
		if (luminosityValue <= 0.06 && luminosityValue >= 0.04) {
			String s = "🚋";
			return s;
		}
		if (luminosityValue <= 0.04 && luminosityValue >= 0.02) {
			String s = "🚂";
			return s;
		}
		String s = "🚎";
		return s;
	}
}


