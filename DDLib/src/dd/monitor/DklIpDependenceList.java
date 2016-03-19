
package dd.monitor;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DklIpDependenceList 
{
    // maps dependent nodes to antecedent services
    
    // hostIp , set of antecedent services
    private Map<String, Set<String> >             m_dependencies = new TreeMap<>();

    
    public DklIpDependenceList()
    {
    
    }
    
    public void add(String dependentHostIp, String antecedentService)
    {
        Set<String>         services;
        
        
        if (!m_dependencies.containsKey(dependentHostIp))
        {
            m_dependencies.put(dependentHostIp, new TreeSet<String>());
        }
        
        services = m_dependencies.get(dependentHostIp);
        services.add(antecedentService);
    }
    
    public Map<String, Set<String> > getIpDependencies()
    {
        return m_dependencies;
    }
}
