module com.epa.erb {
	requires pdfbox;
	requires java.xml;
	requires javafx.fxml;
	requires javafx.web;
	requires javafx.base;
	requires java.base;
	requires java.desktop;
	requires jdk.compiler;
	requires org.apache.commons.io;
	requires org.apache.logging.log4j;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	opens com.epa.erb to javafx.fxml;
	opens com.epa.erb.chapter to javafx.fxml;
	opens com.epa.erb.noteboard to javafx.fxml;
	opens com.epa.erb.worksheet to javafx.fxml;
	opens com.epa.erb.engagement_action to javafx.fxml;
	opens com.epa.erb.engagement_setup to javafx.fxml;
	opens com.epa.erb.erb_progress_tracker to javafx.fxml;
	opens com.epa.erb.activity_progress_tracker to javafx.fxml;
	exports com.epa.erb;
}