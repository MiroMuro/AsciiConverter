module AsciiWriter.Desktop {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.fxml;
	requires javafx.swing;

	opens AsciiWriter.Desktop to javafx.fxml;
	opens AsciiWriter.Controllers to javafx.fxml;
	exports AsciiWriter.Desktop;
	exports AsciiWriter.Controllers;
}
