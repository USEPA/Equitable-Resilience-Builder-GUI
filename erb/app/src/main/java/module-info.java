module erb {
	requires javafx.base;
	requires java.xml;
	requires transitive javafx.fxml;
	requires transitive javafx.controls;
	requires transitive javafx.graphics;
	opens erb to javafx.fxml;
	opens erb_chapter1 to javafx.fxml;
	opens erb_chapter2 to javafx.fxml;
	opens erb_chapter3 to javafx.fxml;
	opens erb_chapter4 to javafx.fxml;
	opens erb_chapter5 to javafx.fxml;
	exports erb;
}