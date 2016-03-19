
package dd.discoveryclient;

import dd.dependenceset.DependencySet;
import dd.output.LoggingContext;
import java.util.ArrayList;

public class DependenceDiscoveryProtocolQuery 
{    
    private LoggingContext                                              m_log;
    private int                                                         m_conversationId; 
    private String[]                                                    m_outgoingDependencies;
    private long                                                        m_fromTimestamp;
    private long                                                        m_toTimestamp;
    private boolean                                                     m_useGossipProtocol;
    private boolean                                                     m_useDistributedKeyLookup;
    private ArrayList<DependenceDiscoveryProtocolRequest>               m_requests = new ArrayList<>();
    
    
    public DependenceDiscoveryProtocolQuery(
            LoggingContext  log,
            int conversationId,
            String[] outgoingDependencies, 
            long fromTimestamp, 
            long toTimestamp,
            boolean useGossipProtocol,
            boolean useDistributedKeyLookup)
    {
        m_log = log;
        m_conversationId = conversationId;
        m_outgoingDependencies = outgoingDependencies;
        m_fromTimestamp = fromTimestamp;
        m_toTimestamp = toTimestamp;
        m_useGossipProtocol = useGossipProtocol;
        m_useDistributedKeyLookup = useDistributedKeyLookup;
    }
    
    public DependencySet runDistributedQuery()
    {
        sendRequests();
        waitForAllResponses();  
        return getResponseDependencies();
    }
    
    private DependencySet getResponseDependencies()
    {
        DependencySet           response = new DependencySet();
        
        
        for(DependenceDiscoveryProtocolRequest request : m_requests)
        {
            response.addDependencySet(request.getResponseDependencies());
        }
        
        return response;
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
        for(DependenceDiscoveryProtocolRequest request : m_requests)
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
        DependenceDiscoveryProtocolRequest             request;
        
        
        for(String serviceId : m_outgoingDependencies)
        {
            request = DependenceDiscoveryProtocolRequest.startRequest(
                    m_log,
                    m_conversationId, 
                    serviceId, 
                    m_fromTimestamp, 
                    m_toTimestamp, 
                    m_useGossipProtocol,
                    m_useDistributedKeyLookup);
            /*
            try
            {
                Thread.sleep(100);
            }
            catch(Exception ex)
            {
                m_log.log(ex);
            }    
            */
            m_requests.add(request);
        }    
    }       
}