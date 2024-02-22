package com.epa.erb.finalReport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class FinalReportParagraph {
	
	private File file;
	private String startReading;
	private String stopReading;
	public FinalReportParagraph(File file, String startReading, String stopReading) {
		this.file= file;
		this.startReading = startReading;
		this.stopReading = stopReading;
	}
	
	public String readWordParagraph() {
		try (XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(file.getPath())))) {
            // output the same as 8.1
            List<XWPFParagraph> list = doc.getParagraphs();
            StringBuilder stringBuilder = new StringBuilder();
            boolean add = false;
            for (XWPFParagraph paragraph : list) {
            	String text = paragraph.getText();
            	if(text.contentEquals(startReading)) {
            		add = true;	
            	} else if (text.contentEquals(stopReading)) {
            		add = false;
            	}
            	if(add == true) stringBuilder.append(text + "\n");
            }
            return stringBuilder.toString();
        } catch (IOException e) {
			e.printStackTrace();
		}
		return null;
}

}
