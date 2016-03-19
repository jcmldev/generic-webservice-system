
package fl.serverlog;

import dd.ExperimentConfiguration;
import dd.graph.DependenceGraph;
import dd.output.LoggingContext;
import fl.ServiceFaultGtTrace;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServerLogReader 
{
    private LogEntry[]          m_logEntries;
    
    
    public ServerLogReader (LoggingContext log, String logFilePath)
    {
        if (ExperimentConfiguration.getFlUseInMemoryLog(log))
        {
            m_logEntries = InMemoryLog.getEntries();
        }
        else
        {
            m_logEntries = LogEntryLoader.loadFromFile(log, logFilePath);
        }
    }
    
    public LogEntry[] getEntriesForServiceInTimeWindow(String service, Date from, Date to)
    {
        List<LogEntry>          entries = new ArrayList<>();
        
        
        for(LogEntry entry : m_logEntries)
        {
            
            // is it the service we are looking for?
            if (entry.getSource().equals(service))
            {
                // is the entry within the time window?
                if (!entry.getTimestamp().before(from) && !entry.getTimestamp().after(to))
                {
                    entries.add(entry);
                }
            }
        }
    
        return entries.toArray(new LogEntry[0]);
    }
}
