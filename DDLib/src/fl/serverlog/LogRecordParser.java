package fl.serverlog;

import dd.output.LoggingContext;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LogRecordParser 
{
    private static final String             RECORD_PART_DELIMITER = "\\|";
    private static final String             DATE_PARSE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSSSZ";
        
    private String[]        m_recordParts;
    private Date            m_timestamp ;
    
    
    public LogRecordParser(LoggingContext log, String record)
    {
        m_recordParts = record.split(RECORD_PART_DELIMITER);
        parseTimestamp(log);
    }
    
    private void parseTimestamp(LoggingContext log)
    {
        String datePart = getPart(0);
        SimpleDateFormat sdf;
        
        
        try 
        {
            sdf = new SimpleDateFormat(DATE_PARSE_FORMAT);
            m_timestamp = sdf.parse(datePart);
        } 
        catch (Exception ex) 
        {
            log.log("Exception while parsing server log record - timestamp");
            log.log(ex);
        }
    }
    
    private String getPart(int index)
    {
        return m_recordParts[index];
    }
    
    public Date getTimestamp()
    {
        return m_timestamp;
    }
    
    public String getRecordClass()
    {
        return getPart(3);
    }
    
    public String getContent()
    {
        return getPart(5);
    }
}
