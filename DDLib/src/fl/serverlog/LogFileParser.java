
package fl.serverlog;

import dd.output.LoggingContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class LogFileParser 
{
    private static final String             RECORD_DELIMITER = "\\[\\#\\|";
    
    private LoggingContext          m_log;
    private Scanner                 m_logScanner;
    
    
    public LogFileParser (LoggingContext log, String logFilePath)
    {
        File logFile = new File(logFilePath);

        
        m_log = log;
        log.log("Opening server log file: " + logFilePath);
        
        try 
        {
            m_logScanner = new Scanner(logFile);
            m_logScanner.useDelimiter(RECORD_DELIMITER);
        } 
        catch (FileNotFoundException ex) 
        {
            log.log("Exception while opening server log file");
            log.log(ex);
        }
    }
    
    public LogRecordParser getNextRecord()
    {
        LogRecordParser     recordParser = null;
        String              record;
        
        
        if (m_logScanner.hasNext())
        {
            record = m_logScanner.next();
            recordParser = new LogRecordParser(m_log, record);
        }
    
        return recordParser;
    }
}
