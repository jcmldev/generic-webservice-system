
package dd.ws;

import dd.discoveryElement.DiscoveryStartingWithFrontends;
import dd.output.LoggingContext;

public class RequestForDiscoveryStartingWithFrontends 
{
    private LoggingContext      m_log;
    
    
    public RequestForDiscoveryStartingWithFrontends(LoggingContext log)
    {
        m_log = log;
    }
    
    public void runDiscovery(
        int conversationId,
        String clientId,
        String[] rootDependencies,
        long fromTimestamp, 
        long toTimestamp)
    {
        /*
        DiscoveryStartingWithFrontends      dswf = new DiscoveryStartingWithFrontends(m_log);
        
        
        dswf.runDiscovery(
                conversationId, 
                clientId, 
                rootDependencies, 
                fromTimestamp, 
                toTimestamp);
                */ 
    }
}
