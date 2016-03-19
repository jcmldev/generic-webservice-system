
package dd.gpdklmanager.multihopgp;

import dd.output.LoggingContext;
import java.util.Map;
import java.util.TreeMap;

public class MultihopGpTargetList 
{
    private LoggingContext                                  m_log;
    private Map<String, MultihopGpTarget>                   m_targets = new TreeMap<>();
    

    public MultihopGpTargetList(LoggingContext log)
    {
        m_log = log;
    }
    
    public MultihopGpTarget getTarget(String host)
    {
        return m_targets.get(host);
    }
    
    public boolean containesTarget(String host)
    {
        return m_targets.containsKey(host);
    }
    
    public void add(MultihopGpTarget target)
    {
        m_targets.put(target.getHost(), target);
    }
    
    public void addTarget(String host)
    {
        if (!m_targets.containsKey(host))
        {
            m_targets.put(host, new MultihopGpTarget(m_log, host));
        }
    }
    
    public int size()
    {
        return m_targets.size();
    }
    
    public MultihopGpTarget[] getTargets()
    {
        return m_targets.values().toArray(new MultihopGpTarget[0]);
    }
}
