
package dd.gossipprotocol;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.output.LoggingContext;
import java.util.LinkedList;
import java.util.List;

public class GossipProtocolClient 
{
    private LoggingContext                  m_log;
    private String                          m_serviceId;
    private GossipProtocolServiceWrapper    m_localGps;
    
    
    public GossipProtocolClient (LoggingContext log, String serviceId)
    {
        m_log = log;
        m_serviceId = serviceId;
    }
    
    /*
     * Response with no dependencies from gossip protocol may mean that the dependencies did not occure 
     * but also that the dependence data are not available (were not synchronized) also the server may have only part of the required data.
     * Therefore, request for dependencies should be send to all backup nodes and the result should be unique sum of the responses
     */
    
    public String[] getInterDependenciesForTimeWindow(
            long fromTimestamp, 
            long toTimestamp)
    {
        List<String>                            backupTargets;
        GossipProtocolDistributedQuery          gossipProtocolDistributedQuery;
        String[]                                dependencies = null;
        
        
        m_log.log(
                "Gossip protocol - request for data of ip: " + 
                getNodeIpOfService() + 
                " - time window size: " + 
                (toTimestamp - fromTimestamp) + "ms");
        
        backupTargets = getBackupTargets();
        
        if (!backupTargets.isEmpty())
        {
            gossipProtocolDistributedQuery = new GossipProtocolDistributedQuery(
                    m_log,
                    backupTargets.toArray(new String[0]), 
                    getNodeIpOfService(), 
                    getNameOfService(),  
                    fromTimestamp, 
                    toTimestamp);

            dependencies = gossipProtocolDistributedQuery.runDistributedQuery();
        }
        
        return dependencies;
    }
    
    private String getNodeIpOfService()
    {
        return Network.getHostIpFromUrl(m_log, m_serviceId);
    }
    
    private String getNameOfService()
    {
        return Network.getServiceNameFromUrl(m_log, m_serviceId);
    }
    
    private GossipProtocolServiceWrapper getLocalPort()
    {   
        if (m_localGps == null)
        {
            m_localGps = new GossipProtocolServiceWrapper(m_log, "127.0.0.1");
        }
        
        return m_localGps;
    }
    
    private GossipProtocolServiceWrapper getRemotePort(String nodeIp)
    {
        return new GossipProtocolServiceWrapper(m_log, nodeIp);
    }
    
    private List<String> getBackupTargets()
    {
        List<String>                targets;
        
        
        targets = getLocalPort().getBackupTargetsOfNode(getNodeIpOfService());
    
        if (targets == null)
        {
            targets = new LinkedList<>();
        }
        
        m_log.log(
                "Backup targets found on localhost for ip: " + 
                getNodeIpOfService() + 
                " - size: " + 
                targets.size());
        
        for(String target : targets)
        {
            m_log.log("Target: " + target);
        }
        
        return targets;
    }   
}