
package dd.discoveryclient;

import dd.dependenceset.DependencySetElement;
import dd.dependenceset.DependencySet;
import dd.ExperimentConfiguration;
import dd.output.LoggingContext;

public class DependenceDiscoveryProtocolClient 
{
    private LoggingContext                  m_log;
    private DependencySet                   m_responseDependencies;
    
    
    public DependenceDiscoveryProtocolClient(LoggingContext log)
    {
        m_log = log;
    }
    
    public void runDistributedQuery(
            int conversationId, 
            String[] outgoingDependencies, 
            long fromTimestamp, 
            long toTimestamp)
    {
        DependenceDiscoveryProtocolQuery                        distributedQuery;
        
        
        distributedQuery = new DependenceDiscoveryProtocolQuery(
                m_log,
                conversationId,
                outgoingDependencies, 
                fromTimestamp, 
                toTimestamp,
                ExperimentConfiguration.getDependenceDiscoveryUseGossipProtocol(m_log),
                ExperimentConfiguration.getDependenceDiscoveryUseDistributedKeyLookup(m_log));

        m_responseDependencies = distributedQuery.runDistributedQuery();        
    }
    
    public DependencySetElement[] getElementsOfService(String serviceId, long from, long to)
    {        
        return m_responseDependencies.getElementsOfService(serviceId);
    }   
}