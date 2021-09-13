package com.tilmohr.marioparty.formatting;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimpleLogFormatter extends Formatter {

	public String format(LogRecord logRecord) {
		return "[" + new SimpleDateFormat("HH:mm:ss").format(new Date(logRecord.getMillis())) + " "
				+ logRecord.getLevel() + "] " + logRecord.getMessage() + "\n";
	}

}
