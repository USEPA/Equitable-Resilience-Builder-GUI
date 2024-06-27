package com.epa.erb.finalReport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import com.epa.erb.App;
import com.epa.erb.utility.FileHandler;

public class FinalReportImage {
	
	private App app;
	private String fileName;
	private FileHandler fileHandler;
	
	public FinalReportImage(String fileName, App app) {
		this.fileName = fileName;
		this.app = app;
		fileHandler = new FileHandler();
	}
	
	private File getDiagram() {
		File uploadsDirectory = fileHandler.getMyUploadsDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
		if (uploadsDirectory != null && uploadsDirectory.exists()) {
			for (File uploadedDir : uploadsDirectory.listFiles()) {
				for(File uploadFile: uploadedDir.listFiles()) {
					if(uploadFile.getName().contains(fileName)) {
						return uploadFile;
					}
				}
			}
		}
		return null;
	}
	
	public void addImageToRun(XWPFRun contentRun) {
		try {
			File diagramFile = getDiagram();
			FileInputStream imageData = new FileInputStream(diagramFile);
			int imageType = XWPFDocument.PICTURE_TYPE_JPEG; 
			String imageFileName = diagramFile.getName(); 
			int width = 500; 
			int height = 250; 
			contentRun.addPicture(imageData, imageType, imageFileName,Units.toEMU(width), Units.toEMU(height));
		} catch (IOException | InvalidFormatException e) {
			e.printStackTrace();
		} 
	}

}
