package com.epa.erb.finalReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epa.erb.App;

public class FinalReportKeyTakeaways {

	private Logger logger;
	
	private App app;
	private File keyTakeawaysFile;
	public FinalReportKeyTakeaways(File keyTakeawaysFile, App app) {
		this.app = app;
		this.keyTakeawaysFile = keyTakeawaysFile;
		
		logger = app.getLogger();
	}
	
	public LinkedHashMap<String, ArrayList<String>> getKeyTakeawaysQRMap() {
		try {

			Pattern qPattern = Pattern.compile("^([0-9]+.(.*?)+)$");
			String question = null;
			ArrayList<String> answers = new ArrayList<>();
			Scanner scanner = new Scanner(keyTakeawaysFile);
			LinkedHashMap<String, ArrayList<String>> qHashMap = new LinkedHashMap<String, ArrayList<String>>();
			
			boolean startStoring = false;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				Matcher qMatcher = qPattern.matcher(line);
				if (qMatcher.matches()) {
					if(question != null) {
						qHashMap.put(question, answers);
					}
					startStoring = true;
					answers = new ArrayList<>();
					question = qMatcher.group(0);
				} else {
					if (startStoring && line.trim().length() > 0 && (!line.contains("complete after"))) {
						answers.add(line);
					}
				}
			}
			scanner.close();
			return qHashMap;
		} catch (FileNotFoundException e) {
			logger.log(Level.FINE, "Failed to get KeyTakeaways QR Map.");
			logger.log(Level.FINER, "Failed to get KeyTakeaways QR Map: " + e.getStackTrace());
			return null;
		}
	}
	
	public String getKeyTakeawaysFormattedText() {
		HashMap<String, ArrayList<String>> map = getKeyTakeawaysQRMap();
		StringBuilder stringBuilder = new StringBuilder();
		int keyNum =0;
		for(String keyQ: map.keySet()) {
			if(keyNum > 0) {
				stringBuilder.append("\n\n" + keyQ);

			}else {
				stringBuilder.append("\n" + keyQ);

			}
			for(String ans: map.get(keyQ)) {
				stringBuilder.append("\n" + ans);
			}
			keyNum++;
		}
		String keyTakeawaysText = stringBuilder.toString();
		keyTakeawaysText = keyTakeawaysText.replaceAll("\\[(.*?)\\]", "");
		keyTakeawaysText = keyTakeawaysText.replaceAll("\n", "\r\n");

		return keyTakeawaysText;
	}

	public App getApp() {
		return app;
	}

	
}
