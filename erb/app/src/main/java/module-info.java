module erb {
	requires transitive javafx.fxml;
	requires transitive javafx.controls;
	requires javafx.graphics;
	requires transitive json.simple;
	opens erb to javafx.fxml;
	exports erb;
}