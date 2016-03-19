
package dd.ws;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.discoveryclient.DependenceDiscoveryProtocolQuery;
import dd.dependenceset.DependencySet;
import dd.monitor.ServerMonitor;
import dd.output.LoggingContext;

public class RequestForDistributedDependencies 
{
    private LoggingContext          m_log;
    
  
    RequestForDistributedDependencies(LoggingContext log)
    {
        m_log = log;
    }
    
    // if ddprovider is unreachable - then gp is queried for dependencies instead
    // next, distributed query for all dependencies provided by gp is sent
    // and again, some of those might not respond thus gp might need to be queried for any of those and then send distributed query as well

    
    public String[] getInterDependenciesForTimeWindow(
            int conversationId,
            String serviceId, 
            long fromTimestamp, 
            long toTimestamp)
    {
        DependencySet           resultSet;
        String                  serviceName; 
        String[]                antecedentDependencies; 
        DependencySet           antecedentSet;
        
        
        
        m_log.log(
                "RequestForDistributedDependencies.getInterDependenciesForTimeWindow - serviceId: " + 
                serviceId + 
                " from: " + 
                fromTimestamp + 
                " to: " + 
                toTimestamp);

        serviceName = Network.getServiceNameFromUrl(m_log, serviceId);

        m_log.log(
                "Service name: " + 
                serviceName + 
                " ..............................");

        antecedentDependencies = ServerMonitor.getInstance().getInterDependenciesForTimeWindow(
                serviceName, 
                fromTimestamp, 
                toTimestamp);
        
        resultSet = ServerMonitor.loadLocalDependencySet(
                m_log,
                serviceId, 
                antecedentDependencies, 
                fromTimestamp, 
                toTimestamp);

        antecedentSet = runDistributedQuery(
                conversationId,
                antecedentDependencies, 
                fromTimestamp, 
                toTimestamp);

        resultSet.addDependencySet(antecedentSet);

        m_log.log(
                "Response of service: " + 
                serviceId + 
                "...............................................");

        resultSet.writeOut(m_log);

        return resultSet.toStringArray();
    }
    
    private DependencySet runDistributedQuery(
            int conversationId, 
            String[] outgoingDependencies, 
            long fromTimestamp, 
            long toTimestamp)
    {
        DependenceDiscoveryProtocolQuery            distributedQuery;
        
        
        distributedQuery = new DependenceDiscoveryProtocolQuery(
                m_log,
                conversationId,
                outgoingDependencies, 
                fromTimestamp, 
                toTimestamp,
                ExperimentConfiguration.getDependenceDiscoveryUseGossipProtocol(m_log),
                ExperimentConfiguration.getDependenceDiscoveryUseDistributedKeyLookup(m_log));

        return distributedQuery.runDistributedQuery();        
    }
}