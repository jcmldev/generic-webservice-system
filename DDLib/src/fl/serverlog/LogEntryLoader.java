
package fl.serverlog;

import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.List;

public class LogEntryLoader 
{

    public static LogEntry[] loadFromFile (LoggingContext log, String logFilePath)
    {
        LogFileParser       fileParser = new LogFileParser(log, logFilePath);
        LogRecordParser     recordParser;
        LogEntry            logEntry;
        List<LogEntry>      logEntries = new ArrayList<>();
        
        
        while((recordParser = fileParser.getNextRecord()) != null)
        {
            
            if (LogEntry.isRecordClassLogEntry(recordParser.getRecordClass()))
            {
                logEntry = new LogEntry(
                        recordParser.getTimestamp(), 
                        recordParser.getRecordClass(), 
                        recordParser.getContent());

                logEntries.add(logEntry);
            }
        }

        return logEntries.toArray(new LogEntry[0]);
    }
}
