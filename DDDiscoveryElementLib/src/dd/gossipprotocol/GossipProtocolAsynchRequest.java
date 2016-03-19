
package dd.gossipprotocol;

import dd.output.LoggingContext;
import java.util.List;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

public class GossipProtocolAsynchRequest 
{
    private LoggingContext          m_log;
    private String                  m_backupNodeIp;
    private String                  m_nodeId;
    private String                  m_serviceId;
    private long                    m_fromTimestamp;
    private long                    m_toTimestamp;
    private boolean                 m_isDone = false;
    private boolean                 m_wasRequestSuccessful = false;
    private List<String>            m_responseDependencies;
    
    
    public GossipProtocolAsynchRequest(
            LoggingContext log,
            String backupNodeIp,
            String nodeId, 
            String serviceId, 
            long fromTimestamp, 
            long toTimestamp)
    {
        m_log = log;
        m_backupNodeIp = backupNodeIp;
        m_nodeId = nodeId;
        m_serviceId = serviceId;
        m_fromTimestamp = fromTimestamp;
        m_toTimestamp = toTimestamp;
    }
    
    public boolean getIsDone()
    {
        return m_isDone;
    }
    
    public boolean getWasRequestSuccessful()
    {
        return m_wasRequestSuccessful;
    }
    
    public List<String> getResponseDependencies()
    {
        return m_responseDependencies;
    }
    
    public void sendRequest()
    {
        GossipProtocolServiceWrapper               gpWrapper;
        
        
        gpWrapper = new GossipProtocolServiceWrapper(m_log, m_backupNodeIp);
        
        m_log.log(
                "Gossip protocol: Asynch request sent to node: " + 
                m_backupNodeIp + 
                " for dependencies of service: " + 
                m_nodeId + 
                " - " + 
                m_serviceId);
        
        try 
        {    
            AsyncHandler<dd.bck.GPGetInterDependenciesForTimeWindowResponse> asyncHandler = 
                    new AsyncHandler<dd.bck.GPGetInterDependenciesForTimeWindowResponse>() 
            {

                @Override
                public void handleResponse(Response<dd.bck.GPGetInterDependenciesForTimeWindowResponse> response) 
                {
                    try 
                    {
                        m_responseDependencies = response.get().getReturn();
                        m_wasRequestSuccessful = true;
                        
                        m_log.log(
                                "Gossip protocol: Received response from node: " + 
                                m_backupNodeIp + 
                                " for dependencies of service: " + 
                                m_nodeId + 
                                " - " + 
                                m_serviceId);
                        
                        for(String dependence : m_responseDependencies)
                        {
                            m_log.log("     dependence: " + dependence);
                        }
                    } 
                    catch (Exception ex) 
                    {
                        m_log.log(
                                "Gossip protocol: Request for dependencies of: " + 
                                m_nodeId + " - " + m_serviceId +
                                " sent to backup node: " + m_backupNodeIp +
                                " failed with exception ...");
                        m_log.log(ex);
                    }
                    
                    m_isDone = true;
                }
            };
            
            java.util.concurrent.Future<? extends Object> result = gpWrapper.getPort().gpGetInterDependenciesForTimeWindowAsync(
                    m_nodeId,
                    m_serviceId, 
                    m_fromTimestamp, 
                    m_toTimestamp, 
                    asyncHandler);
        } 
        catch (Exception ex) 
        {
            m_log.log(
                    "Gossip protocol: Method of request for dependencies of: " + 
                    m_nodeId + " - " + m_serviceId +
                    " sent to backup node: " + m_backupNodeIp +
                    " failed with exception ...");
        }
    }    
}