package com.epa.erb;

import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger extends Logger{
		
	 public MyLogger(final String className) {
		 super(className, null);
		 initLogger();
	}
	
	private void initLogger() {
		File pathToLogDir = getTestLogDir();
		//File pathToLogDir = getPackagedLogDir();
		if (pathToLogDir != null) {
			try {
				addHandler(new LevelBasedFileHandler(pathToLogDir + "\\severe.log",true, Level.SEVERE));
				addHandler(new LevelBasedFileHandler(pathToLogDir + "\\warn.log", true, Level.WARNING));
				addHandler(new LevelBasedFileHandler(pathToLogDir + "\\info.log", true, Level.INFO));
				addHandler(new LevelBasedFileHandler(pathToLogDir + "\\error.log", true, Level.FINE));
				addHandler(new LevelBasedFileHandler(pathToLogDir + "\\error_details.log", true, Level.FINER));
			} catch (SecurityException | IOException e) {
				System.out.println("Logger init exception. Cannot use logger.");
				e.printStackTrace();
			}
		} else {
			System.out.println("Path to log dir is null. Cannot use logger");
		}
	}
	
	private File getTestLogDir() {
		File vistooldstSupportingDocsDir = new File(System.getProperty("user.dir") + "/../.." + "/erb_supporting_docs/");
		if (vistooldstSupportingDocsDir.exists()) {
			File logsDir = new File(vistooldstSupportingDocsDir + "/Logs");
			if (!logsDir.exists()) logsDir.mkdir();
			return logsDir;
		}
		return null;
	}
	
	private File getPackagedLogDir() {
		File erbDir = new File(System.getProperty("user.dir") + "/lib/ERB");
		if (erbDir.exists()) {
			File logsDir = new File(erbDir + "/Logs");
			if (!logsDir.exists()) logsDir.mkdir();
			return logsDir;
		}
		return null;
	}
	
	public void closeHandlers() {
		for(Handler handler: getHandlers()) {
			handler.close();
		}
	}

}
