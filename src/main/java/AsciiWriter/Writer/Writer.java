package AsciiWriter.Writer;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

			// Order is important here because the image being converted
			// is the same image the whole time. The ascii write methods
			// will modify the colored image for best results.
			// Write the image to the console.
			// writeAsciiImageToConsole(resizedImage);
			// Write the image to a text file and open it.
			// writeAsciiImageToFile(resizedImage);

			// Display the image in greyscale.
			// createImage(transferImageToGreyScale(resizedImage));
			// Display the image in monochrome.
			// createImage(convertImageToMonochrome(resizedImage));
			return resizedImage;
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return null;

	}


	public static void main(String[] args) {

		try {

			// Read the image from the file.
			BufferedImage originalImage = ImageIO.read(Writer.class.getResource("/monalisa.jpg"));
			// BufferedImage originalImage =
			// ImageIO.read(Main.class.getResource("src/app/monalisa.jpg"));
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();
			// Calculate the ratio of the image and scale it down a bit
			double ratio = (double) width / height;
			int newWidth = (int) (width * (1 - ratio));
			int newHeight = (int) (height * (1 - ratio));
			int type = originalImage.getType();

			// Create a new buffered image with scaled dimensions.
			BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
			// Create a graphics2D object from the resized image and draw the original image
			// to it.
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
			g.dispose();

			// Order is important here because the image being converted
			// is the same image the whole time. The ascii write methods
			// will modify the colored image for best results.
			// Write the image to the console.
			writeAsciiImageToConsole(resizedImage);
			// Write the image to a text file and open it.
			writeAsciiImageToFile(resizedImage);

			// Display the image in greyscale.
			createImage(transferImageToGreyScale(resizedImage));
			// Display the image in monochrome.
			createImage(convertImageToMonochrome(resizedImage));

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	};


	public static BufferedImage transferImageToGreyScale(BufferedImage image) {
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
				if (colorAtPixel.getRed() + colorAtPixel.getGreen() + colorAtPixel.getBlue() > THRESHOLD) {
					image.setRGB(x, y, Color.WHITE.getRGB());
				} else {
					image.setRGB(x, y, Color.BLACK.getRGB());
				}

			}
		}
		return image;
	}

	public static void createImage(BufferedImage image) {
		// Create a new frame and panel to display the image.
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel() {
			@Override
			// Paint the image to the panel.
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};
		// Add the panel to the frame.
		frame.add(panel);
		frame.setSize(image.getWidth(), image.getHeight());
		frame.setVisible(true);
	};

	public static void writeAsciiImageToFile(BufferedImage image) {
		try {
			// Instantiate a new file object and a file writer object.
			File file = new File("src/main/resources/myFile.txt");
			FileWriter myWriter = new FileWriter("src/main/resources/myFile.txt");
			myWriter.write("This is a test.");
			myWriter.write("\n");
			myWriter.write("This is another test.");

			// Control the density of ascii characters printed to the text file.
			// Denser prints will result in a more detailed and larger image, but the file
			// will take up more memory. e.g, per how many pixels to print a character.

			int densityOfRow = 1;
			int density = 1;
			for (int y = 0; y < image.getHeight(); y += densityOfRow) {
				// Create a new line for each row of pixels.
				myWriter.write("\n");
				for (int x = 0; x < image.getWidth(); x += density) {
					Color pixelColor = new Color(image.getRGB(x, y), true);
					// Create a grayscale pixel using the luminosity method described here:
					// http://www.johndcook.com/blog/2009/08/24/algorithms-convert-color-grayscale/
					// and get its luminosity value as an float between 0 and 1.
					double luminosityOfPixel = ((0.21 * pixelColor.getRed()) + (0.72 * pixelColor.getGreen())
							+ (0.07 * pixelColor.getBlue())) / 255;
					// Get the corresponding character for the luminosity value and write it to the
					// myFile.txt.
					myWriter.write(getAsciiCharacter(luminosityOfPixel));

				}
			}
			System.out.println("Successfully wrote to the file.");
			myWriter.close();
			// Open the file in the default text editor.
			if (!Desktop.isDesktopSupported()) {
				System.out.println("Desktop is not supported");
			}
			Desktop desktop = Desktop.getDesktop();

			if (file.exists()) {
				desktop.open(file);
				double bytes = Files.size(file.toPath());
				// System.out.println("File size in megabytesbytes: " + file.getTotalSpace());
				// System.out.println(Files.size(file.toPath()) + " bytes");
				System.out.println(String.format("ASCII file size in bytes: %f ", bytes));
				System.out.println(String.format("ASCII file size in kilobytes: %f", bytes / 1024));
				System.out.println(String.format("ASCII file size in megabytes: %f", bytes / 1024 / 1024));
			}
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static void writeAsciiImageToConsole(BufferedImage image) {
		// Control the density of ascii characters printed to the console.
		// Denser prints will result in a more detailed image. But will take longer to
		// print and consume more space in the console.
		int density = 5;
		for (int y = 0; y < image.getHeight(); y += density) {
			System.out.println("\n");
			for (int x = 0; x < image.getWidth(); x += density) {
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

	public static char getAsciiCharacter(double luminosityValue) {
		// Get the corresponding character for the luminosity value.
		// Darker values are represented by thicker characters and vice versa.
		if (luminosityValue >= 0.9) {
			return '.';
		}
		if (luminosityValue >= 0.8) {
			return '~';
		}
		if (luminosityValue >= 0.7) {
			return 'z';
		}
		if (luminosityValue >= 0.6) {
			return '<';
		}
		if (luminosityValue >= 0.5) {
			return '§';
		}
		if (luminosityValue >= 0.4) {
			return '+';
		}
		if (luminosityValue >= 0.3) {
			return '€';
		}
		if (luminosityValue >= 0.2) {
			return '*';
		}
		if (luminosityValue >= 0.1) {
			return '@';
		}
		return '#';
	}
}

