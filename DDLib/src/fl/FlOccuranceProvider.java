
package fl;

import dd.output.LoggingContext;
import fl.serverlog.LogEntry;
import fl.serverlog.ServerLogReader;
import java.util.Date;


public class FlOccuranceProvider 
{
    private ServerLogReader     m_serverLogReader;
    private LoggingContext      m_log;
    private String              m_serviceId;
    
    
    public FlOccuranceProvider(
            LoggingContext log,
            String serviceId)
    {
        m_log = log;
        m_serviceId = serviceId;
    }
    
    private ServerLogReader getServerLogReader()
    {
        if (m_serverLogReader == null)
        {
            m_serverLogReader = new ServerLogReader(
                    m_log, 
                    fl.Configuration.getServerLogPath());
        }
        
        return m_serverLogReader;
    }
    
    public boolean getOccuranceOfServiceSymptom(
            long fromTimestamp, 
            long toTimestamp,
            ServiceFaultGtTrace.FaultType faultType)
    {
        Date            fromTimestampEX = new Date(fromTimestamp);
        Date            toTimestampEX = new Date(toTimestamp);
        LogEntry[]      entries;
        
        
        entries = getServerLogReader().getEntriesForServiceInTimeWindow(
                    m_serviceId, 
                    fromTimestampEX, 
                    toTimestampEX);  
        
        
        for(LogEntry entry : entries)
        {
            if (entry.getFaultType() == faultType)
            {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean getOccuranceOfDependenceSymptom(
            String antecedentServiceId,
            long fromTimestamp, 
            long toTimestamp,
            ServiceFaultGtTrace.FaultType faultType)
    {
        Date            fromTimestampEX = new Date(fromTimestamp);
        Date            toTimestampEX = new Date(toTimestamp);
        LogEntry[]      entries;
        
        
        entries = getServerLogReader().getEntriesForServiceInTimeWindow(
                    m_serviceId, 
                    fromTimestampEX, 
                    toTimestampEX);  
        
        
        for(LogEntry entry : entries)
        {
            if (entry.getTarget().equals(antecedentServiceId))
            {
                if (entry.getFaultType() == faultType)
                {
                    return true;
                }
            }
        }
        
        return false;
    }
}
