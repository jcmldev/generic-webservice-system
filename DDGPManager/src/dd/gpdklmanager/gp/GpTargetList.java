
package dd.gpdklmanager.gp;

import dd.output.LoggingContext;
import java.util.Map;
import java.util.TreeMap;

public class GpTargetList 
{
    private LoggingContext                          m_log;
    private Map<String, GpTarget>                   m_targets = new TreeMap<>();
    

    public GpTargetList(LoggingContext log)
    {
        m_log = log;
    }
    
    public boolean containesTarget(String host)
    {
        return m_targets.containsKey(host);
    }
    
    public void addTarget(String host)
    {
        if (!m_targets.containsKey(host))
        {
            m_targets.put(host, new GpTarget(m_log, host));
        }
    }
    
    public int size()
    {
        return m_targets.size();
    }
    
    public GpTarget[] getTargets()
    {
        return m_targets.values().toArray(new GpTarget[0]);
    }
}
