module erb {
	requires transitive javafx.fxml;
	requires transitive javafx.controls;
	requires javafx.graphics;
	opens erb to javafx.fxml;
	exports erb;
}