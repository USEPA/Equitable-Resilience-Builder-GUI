module erb {
	requires transitive javafx.fxml;
	requires transitive javafx.controls;
	opens erb to javafx.fxml;
	exports erb;
}