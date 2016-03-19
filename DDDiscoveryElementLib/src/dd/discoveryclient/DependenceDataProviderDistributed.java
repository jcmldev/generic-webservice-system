
package dd.discoveryclient;
import dd.dependenceset.DependencySetElement;
import dd.discoveryElement.DependenceDataProvider;
import dd.output.LoggingContext;


public class DependenceDataProviderDistributed extends DependenceDataProvider 
{    
    private DependenceDiscoveryProtocolClient             m_ddpClient;
    
    
    public DependenceDataProviderDistributed(LoggingContext log)
    {
        super(log);
        m_ddpClient = new DependenceDiscoveryProtocolClient(m_log);
    }
    
    @Override
    public DependencySetElement[] getElementsOfService(
            int conversationId, 
            String serviceId, 
            long from, 
            long to) 
    { 
        return m_ddpClient.getElementsOfService(
                serviceId, 
                from, 
                to);
    }
    
    public void runDistributedQuery(
            int conversationId, 
            String[] outgoingDependencies, 
            long fromTimestamp, 
            long toTimestamp)
    {
        m_ddpClient.runDistributedQuery(
                conversationId, 
                outgoingDependencies, 
                fromTimestamp, 
                toTimestamp);
    }
}