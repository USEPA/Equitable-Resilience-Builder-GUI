package com.epa.erb;

import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.epa.erb.utility.Constants;

public class MyLogger extends Logger{
		
	 public MyLogger(final String className) {
		 super(className, null);
		 initLogger();
	}
	
	private void initLogger() {
		Constants constants = new Constants();
		File pathToLogDir = new File(constants.getPathToERBLogsDirectory());
		if (pathToLogDir != null) {
			try {
				addHandler(new LevelBasedFileHandler(pathToLogDir + File.separator + "severe.log",true, Level.SEVERE));
				addHandler(new LevelBasedFileHandler(pathToLogDir + File.separator + "warn.log", true, Level.WARNING));
				addHandler(new LevelBasedFileHandler(pathToLogDir + File.separator + "info.log", true, Level.INFO));
			} catch (SecurityException | IOException e) {
				System.out.println("Logger init exception. Cannot use logger.");
			}
		}
	}
	
	public void closeHandlers() {
		for(Handler handler: getHandlers()) {
			handler.close();
		}
	}
	
}
