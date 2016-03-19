
package fl.serverlog;

import java.util.ArrayList;
import java.util.List;

public class InMemoryLog 
{
    private static List<LogEntry>           m_entries = new ArrayList<>();
            
    
    public synchronized static void storeEntry (LogEntry entry)
    {
        m_entries.add(entry);
    }
    
    public static LogEntry[] getEntries()
    {
        return m_entries.toArray(new LogEntry[0]);
    }
}
