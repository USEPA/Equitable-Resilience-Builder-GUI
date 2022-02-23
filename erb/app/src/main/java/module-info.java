module erb {
	requires java.xml;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.controls;
	opens erb to javafx.fxml;
	opens erb_chapter1 to javafx.fxml;
	opens erb_chapter2 to javafx.fxml;
	opens erb_chapter3 to javafx.fxml;
	opens erb_chapter4 to javafx.fxml;
	opens erb_chapter5 to javafx.fxml;
	exports erb;
}