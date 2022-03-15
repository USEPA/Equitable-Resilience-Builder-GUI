module erb {
	requires java.xml;
	requires java.desktop;
	requires transitive javafx.graphics;
	requires javafx.fxml;
	requires transitive javafx.controls;
	requires org.apache.logging.log4j;
	requires javafx.web;
	requires javafx.base;
	opens erb to javafx.fxml;
	exports erb;
}