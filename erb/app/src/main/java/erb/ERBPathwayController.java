package erb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class ERBPathwayController implements Initializable{

	@FXML
	VBox vBox;
	@FXML
	WebView webView;
	
	File htmlFile;
	String pathToRadialData = "C:\\Users\\AWILKE06\\OneDrive - Environmental Protection Agency (EPA)\\Documents\\Projects\\Metro-CERI\\FY22\\ERB\\Javascript\\Radial";
	
	private Logger logger = LogManager.getLogger(ERBPathwayController.class);
	
	Activity activity;
	public ERBPathwayController(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		createHTMLFileForActivity();
		createDataJSFileForActivity();
		loadWebViewContent();
		vBox.setId(activity.getGUID());
	}
	
	public void loadWebViewContent() {
		if (htmlFile != null && htmlFile.exists()) {
			webView.getEngine().load(htmlFile.toURI().toString());
		} else {
			logger.error(htmlFile.getPath() + " is either null or does not exist.");
		}
	}
	
	/**
	 * Creates an HTML file with a specified activity GUID.
	 */
	public void createHTMLFileForActivity() {
		File sourceIndexHTMLFile = new File(pathToRadialData + "\\index.html");
		if (sourceIndexHTMLFile.exists()) {
			htmlFile = new File(pathToRadialData + "\\index-" + activity.getGUID() + ".html");
			if (!htmlFile.exists()) {
				try {
					boolean fileCopied = copyFile(sourceIndexHTMLFile, htmlFile);
					if (fileCopied) updateDataLinkInHTML();
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			} else {
				logger.error(htmlFile.getPath() + " already exisits.");
			}
		} else {
			logger.error(sourceIndexHTMLFile.getPath() + " does not exist.");
		}
	}
	
	/**
	 * Updates the link to the data JS in the HTML by referencing the data with the activity GUID.
	 */
	public void updateDataLinkInHTML() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(htmlFile));
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append("\n");
			}
			bufferedReader.close();
			StringBuilder finalStringBuilder = new StringBuilder();
			String [] lines = stringBuilder.toString().split("\n");
			for(String l: lines) {
				if(l.contentEquals("<script src=\"data.js\"></script>")) {
					l = "<script src=\"data-" + activity.getGUID() + ".js\"></script>";
				}
				finalStringBuilder.append(l);
			}
			PrintWriter printWriter = new PrintWriter(htmlFile);
			printWriter.write(finalStringBuilder.toString());
			printWriter.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Creates data JS file using activity GUID containing data from the specified activity.
	 */
	public void createDataJSFileForActivity() {
		File dataJSFile = new File(pathToRadialData + "\\data-" + activity.getGUID() + ".js");
		if (!dataJSFile.exists()) {
			try {
				String quote = "\"";
				PrintWriter printWriter = new PrintWriter(dataJSFile);
				printWriter.println("var activityData = [");
				ArrayList<ActivityAttribute> listOfAttributes = activity.getActivityAttributes();
				for(ActivityAttribute activityAttribute : listOfAttributes) {
					printWriter.println("{");
					printWriter.println(quote + "Attribute" + quote + ": " + quote + activityAttribute.getShortName() + quote + ",");
					printWriter.println(quote + "Value" + quote + ": " + quote + activityAttribute.getValue() + quote + ",");
					printWriter.println(quote + "Data" + quote + ": " + quote + activityAttribute.getData() + quote + ",");
					printWriter.println(quote + "Color" + quote + ": " + quote + "#" + activityAttribute.getColor() + quote);
					printWriter.println("},");
				}
				printWriter.println("]");
				printWriter.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(dataJSFile.getPath() + " already exists.");
		}
	}
	
	/**
	 * Copies contents of file from sourceFile to destFile.
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @return
	 */
	public boolean copyFile(File sourceFile, File destFile) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(sourceFile);
			os = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			is.close();
			os.close();
			return true;
		} catch (Exception e) {
			logger.error("Copy File Failed: " + e.getMessage());
			return false;
		}
	}
}
