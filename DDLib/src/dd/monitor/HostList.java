
package dd.monitor;

import dd.output.LoggingContext;
import java.util.HashSet;

public class HostList 
{
    private HashSet<String>         m_hosts = new HashSet<>();
    
    
    public HostList()
    {}
    
    public void add(String host)
    {
        if (host != null)
        {
            m_hosts.add(host);
        }
    }

    public String[] getHosts()
    {
        return m_hosts.toArray(new String[0]);
    }
    
    public void writeOut(LoggingContext log)
    {
        for(String host : m_hosts)
        {
            log.log("    Host: " + host);
        }
    }
}
