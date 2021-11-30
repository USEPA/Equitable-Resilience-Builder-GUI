module erb {
	requires transitive javafx.fxml;
	requires transitive javafx.controls;
	requires javafx.graphics;
	requires org.json;
	requires json.simple;
	requires javafx.base;
	opens erb to javafx.fxml;
	exports erb;
}