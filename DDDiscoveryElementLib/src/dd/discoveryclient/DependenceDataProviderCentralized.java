
package dd.discoveryclient;
import dd.ExperimentConfiguration;
import dd.Network;
import dd.dependenceset.DependencySet;
import dd.dependenceset.DependencySetElement;
import dd.discoveryElement.DependenceDataProvider;
import dd.gossipprotocol.MultihopGossipProtocolClient;
import dd.graph.DependenceGraph;
import dd.output.LoggingContext;


public class DependenceDataProviderCentralized extends DependenceDataProvider 
{
    public DependenceDataProviderCentralized(LoggingContext log)
    {
        super(log);
    }

    @Override
    public DependencySetElement[] getElementsOfService(
            int conversationId, 
            String serviceId, 
            long from, 
            long to) 
    { 
        DependencySet                           result = null;
        DependenceGraph.ReachabilityStatus      reachabilityStatus = DependenceGraph.ReachabilityStatus.Unreachable;
        boolean                                 isServiceLocal;
        
        // here is returend also the dependency which leads to these dependencies
        // this is in order to return the status of discovery 
        // - the dg construction has to look for this dependency in the result and update it in dg

        
        //* //debug
        isServiceLocal = (Network.getHostIpFromUrl(m_log, serviceId).equals(Network.getLocalHostIp(m_log)));
        
        if (isServiceLocal || ExperimentConfiguration.getDependenceDiscoveryUseDirectHarvesting(m_log))
        {    
            result = getInterDependenciesFromDependenceDataProvider(
                    conversationId, 
                    serviceId, 
                    from, 
                    to);
            
            reachabilityStatus = DependenceGraph.ReachabilityStatus.Directly;
        }
        //*/
        
        /*
         //debug!!!
        if (serviceId.equals("http://192.168.1.3:8080/WS1/GWS"))
        {
            result = null;
        }
         */
                /*
        // use gossip protocol
        if (ExperimentConfiguration.getDependenceDiscoveryUseGossipProtocol(m_log) && result == null)
        {
            result = getInterDependenciesFromGossipProtocol(serviceId, from, to);
            reachabilityStatus = DependenceGraph.ReachabilityStatus.GP;
        }
        */
        
        // use multihop gossip protocol
        if (ExperimentConfiguration.getDependenceDiscoveryUseMultihopGossipProtocol(m_log) && result == null)
        {
            result = getInterDependenciesFromMultihopGossipProtocol(serviceId, from, to);
            reachabilityStatus = DependenceGraph.ReachabilityStatus.GP; 
        }
        
        // use multihop gossip protocol - for antecedent
        if (ExperimentConfiguration.getDependenceDiscoveryUseMultihopGossipProtocolWithAntecedent(m_log) && result == null)
        {
            result = getInterDependenciesFromMultihopGossipProtocolAntecedent(serviceId, from, to);
            reachabilityStatus = DependenceGraph.ReachabilityStatus.GPAntecedent;
        }
        
        /*
        // use distributed key lookup
        if (ExperimentConfiguration.getDependenceDiscoveryUseDistributedKeyLookup(m_log) && result == null)
        {
            result = getInterDependenciesFromDkl(serviceId);
            reachabilityStatus = DependenceGraph.ReachabilityStatus.DKL;
        }
*/
        // we did what we could - now report service data unreachable
        if (result == null)
        {
            reachabilityStatus = DependenceGraph.ReachabilityStatus.Unreachable;
            result = new DependencySet();
        }
        
        result.addServiceInformation(
                serviceId, 
                reachabilityStatus, 
                DependenceGraph.FlStatus.Unknown,
                DependenceGraph.FlStatus.Unknown,
                0,
                0);
        
        return result.getAllElements();
    }
    
    private DependencySet getInterDependenciesFromDependenceDataProvider(
            int conversationId, 
            String serviceId, 
            long from, 
            long to)
    {
        String[]                                            result = null;
        DependenceDataProviderWrapper                       ddpWrapper;
        DependencySet                                       resultSet = null;
        
        
        m_log.log("Cenralized dependence discovery sent request for dependencies of service:" + serviceId);
        
        ddpWrapper = DependenceDataProviderWrapper.getWrapperForService(m_log, serviceId);
        
        try
        {
            result = ddpWrapper.getInterDependenciesForTimeWindow(
                    conversationId,
                    serviceId, 
                    from, 
                    to);
        }
        catch(java.lang.Exception ex)
        {
            m_log.log(ex);
        }
        
        if (result == null) 
        {
            m_log.log("Cenralized dependence discovery could not reach data of service:" + serviceId);
        }
        else
        {
            m_log.log("Cenralized discovery received data - size: " + result.length);
            resultSet = DependencySet.fromStringArray(result);
        }
        
        return resultSet;
    }
    /*
    private DependencySet getInterDependenciesFromGossipProtocol(
            String serviceId, 
            long from, 
            long to)
    {
        GossipProtocolClient                                gpClient = new GossipProtocolClient(m_log, serviceId);
        String[]                                            result = null;
        DependencySet                                       resultSet = null;
        
        
        m_log.log("Using gossip protocol for service: " + serviceId);
        
        try
        {
            result = gpClient.getInterDependenciesForTimeWindow(from, to);
        }
        catch(Exception ex)
        {
            m_log.log(ex, false);
        }
        
        if (result == null) 
        {
            m_log.log("Cenralized gossip protocol could not reach data of service:" + serviceId);
        }
        else
        {
            m_log.log("Cenralized gossip received data - size: " + result.length);
            resultSet = new DependencySet();
            resultSet.addDependenciesOfService(serviceId, result);
        }
        
        return resultSet;
    }
    * */
    
    private DependencySet getInterDependenciesFromMultihopGossipProtocol(
            String serviceId, 
            long from, 
            long to)
    {
        MultihopGossipProtocolClient                        multihopGPClient = new MultihopGossipProtocolClient(m_log);
        String[]                                            result = null;
        DependencySet                                       resultSet = null;
        
        
        m_log.log("Using multihop gossip protocol for service: " + serviceId);
        
        try
        {
            result = multihopGPClient.getInterDependenciesForTimeWindow(serviceId, from, to);
        }
        catch(Exception ex)
        {
            m_log.log(ex, false);
        }
        
        if (result == null) 
        {
            m_log.log("Cenralized multihop gossip protocol could not reach data of service:" + serviceId);
        }
        else
        {
            m_log.log("Cenralized multihop gossip received data - size: " + result.length);
            resultSet = DependencySet.fromStringArray(result);
        }
        
        return resultSet;
    }
    
    // here, instead of asking for outgoing dependencies of services
    // we ask for all dependencies onall nodes which have dependent ip same as ip of the dependent service
    private DependencySet getInterDependenciesFromMultihopGossipProtocolAntecedent(
            String serviceId, 
            long from, 
            long to)
    {
        MultihopGossipProtocolClient                        multihopGPClient = new MultihopGossipProtocolClient(m_log);
        String[]                                            result = null;
        DependencySet                                       resultSet = null;
        
        
        m_log.log("Using multihop gossip protocol (antecedent) for service: " + serviceId);
        
        try
        {
            result = multihopGPClient.getInterDependenciesForTimeWindowAntecedent(serviceId, from, to);
        }
        catch(Exception ex)
        {
            m_log.log(ex, false);
        }
        
        if (result == null) 
        {
            m_log.log("Cenralized multihop gossip protocol (antecedent) could not reach data of service:" + serviceId);
        }
        else
        {
            m_log.log("Cenralized multihop gossip (antecedent) received data - size: " + result.length);
            resultSet = DependencySet.fromStringArray(result);
        }
        
        return resultSet;
    }
    
    /*
    private DependencySet getInterDependenciesFromDkl(String serviceId)
    {
        DistributedKeyLookupClient                          dklClient = new DistributedKeyLookupClient(m_log, serviceId);
        String[]                                            result = null;
        DependencySet                                       resultSet = null;
        
        
        m_log.log("Using distributed key lookup for service: " + serviceId);

        try
        {
            result = dklClient.getAntecedentServicesOfDependentIp();
        }
        catch(Exception ex)
        {
            m_log.log(ex, false);
        }
        
        if (result == null) 
        {
            m_log.log("Cenralized distributed key lookup could not reach data of service:" + serviceId);
        }
        else
        {
            m_log.log("Cenralized distributed key lookup received data - size: " + result.length);
            resultSet = new DependencySet();
            resultSet.addDependenciesOfService(serviceId, result);
        }
        
        return resultSet;
    }
    */ 
}