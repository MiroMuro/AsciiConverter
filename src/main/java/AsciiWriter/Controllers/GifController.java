package AsciiWriter.Controllers;


import java.util.Timer;
import java.util.TimerTask;

import AsciiWriter.Writer.Writer;
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
	private TextArea gifTextArea;
	@FXML
	private Button gifButton;
	private boolean flag = false;
	public void displayGif(Image gif) throws InterruptedException {
		gifView.setImage(gif);
		Writer.writeAsciiGifToFile();

	}

	@FXML
	public void setAids(ActionEvent event) throws InterruptedException {
		/*GifDecoder gifDecoder = new GifDecoder();
		gifDecoder.read("src/main/resources/gifs/ds.gif");
		int n = gifDecoder.getFrameCount();
		System.out.println("Number of frames in the gif: " + n);
		Charset utf8 = Charset.forName("UTF-8");
		// BufferedImage frame = gifDecoder.getFrame(0);

		for (int i = 0; i < n; i++) {
			Thread.sleep(100);
			BufferedImage frame = gifDecoder.getFrame(i);
			Writer.writeAsciiImageToFileNoOpeningPropmt(frame, 2, 1);

			try {
				File myObj = new File("src/main/resources/myFile.txt");
				Scanner myReader = new Scanner(myObj);
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
					gifTextArea.appendText(data + "\n");
				}
				myReader.close();
				gifTextArea.clear();
			} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}

		}*/
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				gifTextArea.appendText("Hello World");
			}

		};
		Timer timer = new Timer();
		long delay = 300L;


		timer.schedule(task, delay, 50);



	}

	public void stopAids(ActionEvent event) {
		flag = true;
	}
}
