

package fl.serverlog;

import fl.ServiceFaultGtTrace;
import java.util.Date;
import java.util.logging.Logger;


public class ServerLogWriter 
{
    public static void recordFault(
            ServiceFaultGtTrace.FaultType faultType, 
            String source, 
            String target)
    {
        LogEntry                            logEntry;
        

        logEntry = new LogEntry(new Date(), faultType, source, target);
        
        reportFault(logEntry);
    }

    public static void reportFault(LogEntry entry)
    {
        Logger.getLogger(entry.getRecordClassTypeName()).severe(entry.getContent());
        
        InMemoryLog.storeEntry(entry);
    }
}
