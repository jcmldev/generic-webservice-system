
package dd.discoveryclient;

import dd.dependenceset.DependencySet;
import dd.dependenceset.DependencySetDependency;
import dd.dependenceset.DependencySetElement;
import dd.dependenceset.DependencySetService;
import dd.gossipprotocol.MultihopGossipProtocolClient;
import dd.graph.DependenceGraph;
import dd.output.DiscoveryTrace;
import dd.output.LoggingContext;


public class DependenceDiscoverySystemWideDg 
{
    public static DependencySet getSystemDg(
            int conversationId,
            long from, 
            long to)
    {
        LoggingContext          log = LoggingContext.getContext(new DependenceDiscoverySystemWideDg(), Integer.toString(conversationId));
        MultihopGossipProtocolClient                        multihopGPClient = new MultihopGossipProtocolClient(log);
        String[]                                            result = null;
        DependencySet                                       resultSet = null;
        
        
        log.log("Using multihop gossip protocol for system wide dg");
        
        try
        {
            result = multihopGPClient.getSystemDg(from, to);
        }
        catch(Exception ex)
        {
            log.log(ex, false);
        }
        
        if (result == null) 
        {
            log.log("Cenralized multihop gossip protocol could not reach data for system wide dg");
        }
        else
        {
            log.log("Cenralized multihop gossip received data - size: " + result.length);
            resultSet = DependencySet.fromStringArray(result);
            resultSet.writeOut(log);
                    
            recordSystemWideDgIntoTrace(
                    log, 
                    conversationId, 
                    resultSet);
        }
        
        return resultSet;
    }
    
    // using discovery trace designed for single conversation dg - so it gets bit nasty
    private static synchronized void recordSystemWideDgIntoTrace(
            LoggingContext log, 
            int conversationId,
            DependencySet ds)
    {
        DependencySetDependency             dependency;
        DependencySetService                service;
        
        
        for(DependencySetElement element : ds.getAllElements())
        {   
            if (element instanceof DependencySetDependency)
            {
                dependency = (DependencySetDependency)element;
                
                DiscoveryTrace.writeDgEdgeIntoOutput(
                        log, 
                        conversationId,  
                        dependency.getDependent(),
                        dependency.getAntecedent(),
                        dependency.getFlStatusEX().name(),
                        dependency.getFlStatusTO().name(),
                        dependency.getLastEXTimestamp(),
                        dependency.getFirstTOTimestamp(),
                        DependenceGraph.ReachabilityStatus.Unknown.name(),
                        DependenceGraph.FlStatus.Unknown.name(),
                        DependenceGraph.FlStatus.Unknown.name(),
                        0,
                        0);
            }
            else
            {
                service = (DependencySetService)element;
                
                DiscoveryTrace.writeDgEdgeIntoOutput(
                        log, 
                        conversationId,  
                        service.getService(),
                        "",
                        service.getFlStatusEX().name(),
                        service.getFlStatusTO().name(),
                        service.getLastEXTimestamp(),
                        service.getFirstTOTimestamp(),
                        service.getReachabilityStatus().name(),
                        DependenceGraph.FlStatus.Unknown.name(),
                        DependenceGraph.FlStatus.Unknown.name(),
                        0,
                        0);
            }
        }
    }

}
