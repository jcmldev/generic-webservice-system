
package dd.discoveryclient;

import dd.dependenceset.DependencySet;
import dd.dkl.DistributedKeyLookupClient;
import dd.gossipprotocol.GossipProtocolClient;
import dd.graph.DependenceGraph;
import dd.output.LoggingContext;

public class DependenceDiscoveryProtocolRequest implements Runnable
{
    private LoggingContext          m_log;
    private int                     m_conversationId; 
    private String                  m_serviceId;
    private long                    m_fromTimestamp;
    private long                    m_toTimestamp;
    private boolean                 m_isDone = false;
    private boolean                 m_useGossipProtocol;
    private boolean                 m_useDistributedKeyLookup;
    private DependencySet           m_responseDependencies;
    
    
    private DependenceDiscoveryProtocolRequest(
            LoggingContext log,
            int conversationId, 
            String serviceId, 
            long fromTimestamp, 
            long toTimestamp, 
            boolean useGossipProtocol,
            boolean useDistributedKeyLookup)
    {
        m_log = log;
        m_conversationId = conversationId; 
        m_serviceId = serviceId;
        m_fromTimestamp = fromTimestamp;
        m_toTimestamp = toTimestamp;
        m_useGossipProtocol = useGossipProtocol;
        m_useDistributedKeyLookup = useDistributedKeyLookup;
    }
    
    public static DependenceDiscoveryProtocolRequest startRequest(
            LoggingContext log,
            int conversationId, 
            String serviceId, 
            long fromTimestamp, 
            long toTimestamp, 
            boolean useGossipProtocol,
            boolean useDistributedKeyLookup)
    {
        DependenceDiscoveryProtocolRequest          request;
        
        
        request = new DependenceDiscoveryProtocolRequest(
                log,
                conversationId,
                serviceId, 
                fromTimestamp, 
                toTimestamp, 
                useGossipProtocol,
                useDistributedKeyLookup);
        
        (new Thread(request)).start();

        return request;
    }
    
    public String getAntecdentServiceId()
    {
        return m_serviceId;
    }
    
    public boolean getIsDone()
    {
        return m_isDone;
    }
    
    public DependencySet getResponseDependencies()
    {
        return m_responseDependencies;
    }
    
    @Override
    public void run() 
    {
        DependencySet            result;
        
        
        result = sendRequestToDDProvider();
        
        
        /*
         //debug!!!
        if (m_serviceId.equals("http://192.168.1.3:8080/WS1/GWS"))
        {
            result = null;
        }
        */
        
        if (result != null)
        {
            m_responseDependencies = result;
            m_isDone = true;
            return;
        }
        
        if (m_useGossipProtocol)
        {
            result = useGossipProtocolInsteadOfDDProvider();
            
            if (result != null)
            {
                m_responseDependencies = result;
                m_isDone = true;
                return;
            }
        }
        
        if (m_useDistributedKeyLookup)
        {
            result = useDistributedKeyLookupInsteadOfDDProvider();
            
            if (result != null)
            {
                m_responseDependencies = result;
                m_isDone = true;
                return;
            }
        }        
        
        m_isDone = true;
        
        result = new DependencySet();
        result.addServiceInformation(
                m_serviceId, 
                DependenceGraph.ReachabilityStatus.Unreachable, 
                DependenceGraph.FlStatus.Unknown,
                DependenceGraph.FlStatus.Unknown,
                0,
                0);
    }
    
    private DependencySet sendRequestToDDProvider()
    {
        String[]                                            result = null;
        DependenceDataProviderWrapper                       ddpWrapper;
        DependencySet                                       dependencySet = null;
        
        
        m_log.log("Dependence discovery protocol sent request for dependencies of service:" + m_serviceId);
        
        ddpWrapper = DependenceDataProviderWrapper.getWrapperForService(m_log, m_serviceId);
        
        try
        {
            result = ddpWrapper.getInterDependenciesForTimeWindowDistributed(
                    m_conversationId,
                    m_serviceId, 
                    m_fromTimestamp, 
                    m_toTimestamp);
        }
        catch(Exception ex)
        {
            m_log.log("Request to service: " + m_serviceId + " - exception");
            m_log.log(ex);
        }
        
        if (result == null) 
        {
            m_log.log("Dependence discovery protocol could not reach data of service:" + m_serviceId);
        }        
        else
        {
            dependencySet = DependencySet.fromStringArray(result);
            dependencySet.addServiceInformation(
                    m_serviceId, 
                    DependenceGraph.ReachabilityStatus.Directly, 
                    DependenceGraph.FlStatus.Unknown,
                    DependenceGraph.FlStatus.Unknown,
                    0,
                    0);
        }
        
        return dependencySet;
    }
    
    private DependencySet useGossipProtocolInsteadOfDDProvider()
    {
        String[]            outgoingDependencies;
        DependencySet       antecedentDependencies;
        DependencySet       result = new DependencySet();
        
        
        outgoingDependencies = sendRequestToGossipProtocol();
        
        if (outgoingDependencies == null) return null;

        result.addServiceInformation(
                m_serviceId, 
                DependenceGraph.ReachabilityStatus.GP, 
                DependenceGraph.FlStatus.Unknown,
                DependenceGraph.FlStatus.Unknown,
                0,
                0);

        result.addDependenciesOfService(
                m_serviceId, 
                outgoingDependencies);

        if (outgoingDependencies.length > 0)
        {
            antecedentDependencies = sendRequestToAntecedentDDProviders(outgoingDependencies);
            result.addDependencySet(antecedentDependencies);
        }
        
        return result;
    }

    private String[] sendRequestToGossipProtocol()
    {
        GossipProtocolClient                                gpClient = new GossipProtocolClient(m_log, m_serviceId);
        String[]                                            result;
        
        
        m_log.log("Using gossip protocol for service: " + m_serviceId);
        
        result = gpClient.getInterDependenciesForTimeWindow(m_fromTimestamp, m_toTimestamp);
        
        if (result == null) 
        {
            m_log.log("Gossip protocol could not reach data of service:" + m_serviceId);
        }
        else
        {
            m_log.log("Gossip protocol received data - size: " + result.length);
        }
        
        return result;
    }
    
    // combine the gp and dkl
    private DependencySet useDistributedKeyLookupInsteadOfDDProvider()
    {
        String[]            outgoingDependencies;
        DependencySet       antecedentDependencies;
        DependencySet       result = new DependencySet();
        
        
        outgoingDependencies = sendRequestToDistributedKeyLookup();
        
        if (outgoingDependencies == null) return null;

        result.addServiceInformation(
                m_serviceId, 
                DependenceGraph.ReachabilityStatus.DKL, 
                DependenceGraph.FlStatus.Unknown,
                DependenceGraph.FlStatus.Unknown,
                0,
                0);

        result.addDependenciesOfService(
                m_serviceId, 
                outgoingDependencies);

        if (outgoingDependencies.length > 0)
        {
            antecedentDependencies = sendRequestToAntecedentDDProviders(outgoingDependencies);
            result.addDependencySet(antecedentDependencies);
        }
        
        return result;
    }
    
    private String[] sendRequestToDistributedKeyLookup()
    {
        DistributedKeyLookupClient                          dklClient = new DistributedKeyLookupClient(m_log, m_serviceId);
        String[]                                            result;
        
        
        m_log.log("Using distributed key lookup for service: " + m_serviceId);
        
        result = dklClient.getAntecedentServicesOfDependentIp();
        
        if (result == null) 
        {
            m_log.log("Distributed key lookup could not reach data of service:" + m_serviceId);
        }
        else
        {
            m_log.log("Distributed key lookup received data - size: " + result.length);
        }
        
        return result;
    }
        
    private DependencySet sendRequestToAntecedentDDProviders(String[] outgoingDependencies)
    {
        DependenceDiscoveryProtocolQuery            query;
        DependencySet                               response;
        
        
        m_log.log("Using sub query for antecedent dependencies of unavailable ddprovider");
        
        query = new DependenceDiscoveryProtocolQuery(
                m_log,
                m_conversationId,
                outgoingDependencies, 
                m_fromTimestamp, 
                m_toTimestamp, 
                m_useGossipProtocol,
                m_useDistributedKeyLookup);
    
        response = query.runDistributedQuery();
        
        m_log.log("Sub query result - size: " + response.size());
        
        return response;
    }
}