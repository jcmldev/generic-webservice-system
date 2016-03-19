package dd.discoveryElement;

import dd.dependenceset.DependencySetElement;
import dd.output.LoggingContext;

public abstract class DependenceDataProvider 
{   
    protected LoggingContext            m_log;
    
    
    public DependenceDataProvider(LoggingContext log)
    {
        m_log = log;
    }
    
    public abstract DependencySetElement[] getElementsOfService(
            int conversationId, 
            String serviceUrl, 
            long fromTimestamp, 
            long toTimestamp);
}
