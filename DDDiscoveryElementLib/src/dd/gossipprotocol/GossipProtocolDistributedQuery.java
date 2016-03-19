
package dd.gossipprotocol;

import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GossipProtocolDistributedQuery 
{    
    private LoggingContext                                  m_log;
    private String[]                                        m_backupNodeIps;
    private String                                          m_nodeId;
    private String                                          m_serviceId;
    private long                                            m_fromTimestamp;
    private long                                            m_toTimestamp;
    private ArrayList<GossipProtocolAsynchRequest>          m_requests = new ArrayList<GossipProtocolAsynchRequest>();
    
    
    public GossipProtocolDistributedQuery( 
            LoggingContext log,
            String[] backupNodeIps,
            String nodeId, 
            String serviceId, 
            long fromTimestamp, 
            long toTimestamp)
    {
        m_log = log;
        m_backupNodeIps = backupNodeIps;
        m_nodeId = nodeId;
        m_serviceId = serviceId;
        m_fromTimestamp = fromTimestamp;
        m_toTimestamp = toTimestamp;
    }
    
    public String[] runDistributedQuery()
    {
        sendRequests();
        waitForAllResponses();  
        return getResponseDependencies();
    }
    
    private String[] getResponseDependencies()
    {
        Set<String>             responseDependencies = new TreeSet<String>();
        String[]                response = null;
        
        
        if (getWasAtLeastOneRequestSuccessful())
        {
            for(GossipProtocolAsynchRequest request : m_requests)
            {
                if (request.getWasRequestSuccessful())
                {
                    addDependenciesIntoList(
                            responseDependencies, 
                            request.getResponseDependencies());
                }
            }
            
            response = responseDependencies.toArray(new String[0]);
        }        
        
        m_log.log("Gossip protocol: found following dependencies ...");
        
        for (String dependence : responseDependencies)
        {
            m_log.log("     dependence: " + dependence);
        }
        
        return response;
    }
    
    private boolean getWasAtLeastOneRequestSuccessful()
    {
        for(GossipProtocolAsynchRequest request : m_requests)
        {
            if (request.getWasRequestSuccessful()) return true;
        }
        
        return false;
    }
    
    private void addDependenciesIntoList(Set<String> dependenciesList, List<String> dependenciesToAdd)
    {
        for(String dependency : dependenciesToAdd)
        {
            if (!dependenciesList.contains(dependency))
            {
                dependenciesList.add(dependency);
            }
        }
    }
    
    private void waitForAllResponses()
    {
        // wait untill all responses are received before continue on processing data
        try
        {
            while (!getAreAllRequestsDone()) 
            {
                Thread.sleep(10);
            }
        }
        catch(Exception ex)
        {
            m_log.log(ex);
        }    
    }
    
    private boolean getAreAllRequestsDone()
    {
        for(GossipProtocolAsynchRequest request : m_requests)
        {
            if (!request.getIsDone())
            {
                return false;
            }
        }
        
        return true;
    }
    
    private void sendRequests()
    {
        GossipProtocolAsynchRequest             request;
        
        
        for(String backupNodeIp : m_backupNodeIps)
        {
            request = new GossipProtocolAsynchRequest(
                    m_log,
                    backupNodeIp, 
                    m_nodeId, 
                    m_serviceId, 
                    m_fromTimestamp, 
                    m_toTimestamp);

            try
            {
                Thread.sleep(10);
            }
            catch(Exception ex)
            {
                m_log.log(ex);
            }    
            
            m_requests.add(request);
            request.sendRequest();
        }    
    }       
}