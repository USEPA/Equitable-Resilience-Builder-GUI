module com.epa.erb {
	requires java.xml;
	requires java.desktop;
	requires transitive javafx.graphics;
	requires javafx.fxml;
	requires transitive javafx.controls;
	requires org.apache.logging.log4j;
	requires javafx.web;
	requires javafx.base;
	requires java.base;
	opens com.epa.erb to javafx.fxml;
	opens com.epa.erb.noteboard to javafx.fxml;
	opens com.epa.erb.worksheet to javafx.fxml;
	opens com.epa.erb.engagement_action to javafx.fxml;
	opens com.epa.erb.engagement_setup to javafx.fxml;
	exports com.epa.erb;
}