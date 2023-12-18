package com.epa.erb;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class LevelBasedFileHandler extends FileHandler {
	public LevelBasedFileHandler(final Level level) throws IOException, SecurityException {
		super();
		super.setLevel(level);
		super.setFormatter(new SimpleFormatter());
	}

	public LevelBasedFileHandler(final String s, final Level level) throws IOException, SecurityException {
		super(s);
		super.setLevel(level);
		super.setFormatter(new SimpleFormatter());
	}

	public LevelBasedFileHandler(final String s, final boolean b, final Level level)
			throws IOException, SecurityException {
		super(s, b);
		super.setLevel(level);
		super.setFormatter(new SimpleFormatter());
	}

	public LevelBasedFileHandler(final String s, final int i, final int i1, final Level level)
			throws IOException, SecurityException {
		super(s, i, i1);
		super.setLevel(level);
		super.setFormatter(new SimpleFormatter());
	}

	public LevelBasedFileHandler(final String s, final int i, final int i1, final boolean b, final Level level)
			throws IOException, SecurityException {
		super(s, i, i1, b);
		super.setLevel(level);
		super.setFormatter(new SimpleFormatter());
	}

	public void setLevel() {
		throw new UnsupportedOperationException("Can't change after construction!");
	}

	// This is the important part that makes it work
	// it also breaks the contract in the JavaDoc for FileHandler.setLevel()
	@Override
	public void publish(final LogRecord logRecord) {
		if (logRecord.getLevel().equals(super.getLevel())) {
			super.publish(logRecord);
		}
	}
}
