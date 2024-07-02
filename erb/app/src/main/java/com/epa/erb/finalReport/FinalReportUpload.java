package com.epa.erb.finalReport;

import java.io.File;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;

public class FinalReportUpload {
	
	private File file;
	private String fileName;
	public FinalReportUpload(File file, String fileName) {
		this.file = file;
		this.fileName = fileName;
	}
	
	public XWPFHyperlinkRun createUploadRun(XWPFParagraph paragraph) {

		XWPFHyperlinkRun hyperlinkrun = createHyperlinkRun(paragraph, file.toURI().toString());
		hyperlinkrun.setUnderline(UnderlinePatterns.SINGLE);
		hyperlinkrun.setText(fileName);
		hyperlinkrun.setBold(false);
		hyperlinkrun.setFontSize(14);
		return hyperlinkrun;
	}
	
	XWPFHyperlinkRun createHyperlinkRun(XWPFParagraph paragraph, String uri) {
		String rId = paragraph.getDocument().getPackagePart()
				.addExternalRelationship(uri, XWPFRelation.HYPERLINK.getRelation()).getId();

		CTHyperlink cthyperLink = paragraph.getCTP().addNewHyperlink();
		cthyperLink.setId(rId);
		cthyperLink.addNewR();

		return new XWPFHyperlinkRun(cthyperLink, cthyperLink.getRArray(0), paragraph);
	}
	
	
	
	

}
