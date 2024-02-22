package com.epa.erb.finalReport;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import com.epa.erb.App;
import com.epa.erb.utility.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FinalReportSelectionController implements Initializable{

	private App app;
	public FinalReportSelectionController(App app) {
		this.app = app;
	}
	
	FileHandler fileHandler ;
	private int reportFontSize = 16;
	
	@FXML
	public void generateFinalReportButtonAction() {
		createFinalReport();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fileHandler = new FileHandler(app);
	}
	
	private void createFinalReport() {
		File goalDir = fileHandler.getGoalDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
		try {
			XWPFDocument reportDoc = new XWPFDocument();
			
			//1
			XWPFParagraph paragraph1 = reportDoc.createParagraph();
			String titleText1 = "Key Takeaways: Assess";
			createNewTitleRun(paragraph1,titleText1);
			XWPFRun contentRun1 = createNewContentRun(paragraph1, null);
			addNewLines(getAssessKeyTakeawaysText(), contentRun1);
			//2
			XWPFParagraph paragraph2 = reportDoc.createParagraph();
			String titleText2 = "Strategy Chart";
			createNewTitleRun(paragraph2, titleText2);
			createStrategyChart(reportDoc, goalDir);

			//3
			XWPFParagraph paragraph3 = reportDoc.createParagraph();
			String titleText3 = "Reflection Diary";
			createNewTitleRun(paragraph3, titleText3);
			XWPFRun contentRun3 = createNewContentRun(paragraph3, null);
			addNewLines(getReflectionDiaryText(goalDir), contentRun3);
			
			//4
			XWPFParagraph paragraph4 = reportDoc.createParagraph();
			String titleText4 = "Quadrant Sorting";
			createNewTitleRun(paragraph4, titleText4);
			XWPFRun contentRun4 = createNewContentRun(paragraph4, null);
			addQuadrantDiagram(contentRun4);
			
			
			FileOutputStream out = new FileOutputStream(new File(goalDir + File.separator + "finalReport.docx"));
			reportDoc.write(out);
			reportDoc.close();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private XWPFRun createNewTitleRun(XWPFParagraph paragraph, String runText) {
		XWPFRun run = paragraph.createRun();
		run.addBreak();
		if(runText != null) run.setText(runText);
		run.addBreak();
		run.setUnderline(UnderlinePatterns.SINGLE);
		run.setBold(true);
		run.setFontSize(reportFontSize);
		return run;
	}
	
	private XWPFRun createNewContentRun(XWPFParagraph paragraph, String runText) {
		XWPFRun run = paragraph.createRun();
		if(runText != null) run.setText(runText);
		run.addBreak();
		run.setFontSize(reportFontSize);
		return run;
	}
	
	private void addNewLines(String data,XWPFRun run ) {
		if (data.contains("\n")) {
            String[] lines = data.split("\n");
            run.setText(lines[0], 0);
            for(int i=1;i<lines.length;i++){
                run.addBreak();
                run.setText(lines[i]);
            }
        } else {
            run.setText(data, 0);
        }
	}
	
	private String getReflectionDiaryText(File goalDir) {
		File fileToSearch = new File(goalDir.getPath() +  File.separator + "Supporting_DOC" + File.separator + "Reflection_Diary.docx");
		FinalReportParagraph fRP = new FinalReportParagraph(fileToSearch, "Plan Your Project Questions", "What to Do Next");
		String text = fRP.readWordParagraph();
		return text;
	}
	
	
	private void createStrategyChart(XWPFDocument reportDoc, File goalDir) {
		File fileToSearch = new File(goalDir.getPath() + File.separator + "Supporting_DOC" + File.separator +"Strategy_Chart.docx");
		FinalReportTable ft = new FinalReportTable(fileToSearch, "TableToRead", reportDoc);
		ft.createTableCopy();
	}
	
	
	private String getAssessKeyTakeawaysText() {
		File uploadsDirectory = fileHandler.getMyUploadsDirectory(app.getSelectedProject(), app.getEngagementActionController().getCurrentGoal());
		if (uploadsDirectory != null && uploadsDirectory.exists()) {
			for (File uploadedDir : uploadsDirectory.listFiles()) {
				for (File uploadFile : uploadedDir.listFiles()) {
					if (uploadFile.getName().contentEquals("Key_Takeaways__Assess.txt")) {
						FinalReportKeyTakeaways fRKT = new FinalReportKeyTakeaways(uploadFile);
						return fRKT.getKeyTakeawaysFormattedText();
					}
				}
			}
		}
		return null;
	}
	
	private void addQuadrantDiagram(XWPFRun contentRun) {
		FinalReportImage fRI = new FinalReportImage("SortingSnapshot", app);
		fRI.addImageToRun(contentRun);
	}

}
