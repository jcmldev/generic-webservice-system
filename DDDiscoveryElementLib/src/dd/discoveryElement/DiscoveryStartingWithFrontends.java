
package dd.discoveryElement;

import dd.discoveryclient.DependenceDiscoveryClient;
import dd.graph.DependenceGraph;
import dd.graph.Edge;
import dd.output.DiscoveryTrace;
import dd.output.LoggingContext;
import fl.ServiceFaultGtTrace;

public class DiscoveryStartingWithFrontends 
{
    private LoggingContext      m_log;
    
    
    public DiscoveryStartingWithFrontends(LoggingContext log)
    {
        m_log = log;
    }
    
    public DependenceGraph runDiscoveryForSingleConversation(
        int conversationId,
        String clientId,
        long fromTimestamp,
        long toTimestamp,
        String frontEndServiceId,                
        Exception conversationException,
        Class classOfWSExceptionWrapper)
    {
        DependenceGraph.FlStatus        frontEndDependenceFlStatusEX = DependenceGraph.FlStatus.NoFault;
        DependenceGraph.FlStatus        frontEndDependenceFlStatusTO = DependenceGraph.FlStatus.NoFault;
        DependenceGraph                 dg;
        DependenceGraph.FlStatus        clientFlStatusEX;
        DependenceGraph.FlStatus        clientFlStatusTO;
        DependenceGraph.FlStatus[]      rootFlStatusEX;
        DependenceGraph.FlStatus[]      rootFlStatusTO;
        String[]                        rootDependencies;
        DependenceDiscoveryClient       ddClient;
        ServiceFaultGtTrace.FaultType   faultType;
        
        
        if (conversationException != null)
        {
            faultType = ServiceFaultGtTrace.ClientProcess_getFaultTypeFromException(
                    classOfWSExceptionWrapper, 
                    conversationException);

            if (faultType == ServiceFaultGtTrace.FaultType.Timeout)
            {
                frontEndDependenceFlStatusTO = DependenceGraph.FlStatus.ElementFault;
            }
            else
            {
                frontEndDependenceFlStatusEX = DependenceGraph.FlStatus.TransitiveFault;
            }
        }
        
        rootDependencies = new String[]{frontEndServiceId};
        clientFlStatusEX = frontEndDependenceFlStatusEX != DependenceGraph.FlStatus.NoFault ? DependenceGraph.FlStatus.TransitiveFault : DependenceGraph.FlStatus.NoFault;
        clientFlStatusTO = frontEndDependenceFlStatusTO;

        rootFlStatusEX = new DependenceGraph.FlStatus[]{frontEndDependenceFlStatusEX};
        rootFlStatusTO = new DependenceGraph.FlStatus[]{frontEndDependenceFlStatusTO};

        ddClient = new DependenceDiscoveryClient(
                conversationId,
                clientId, 
                clientFlStatusEX,
                clientFlStatusTO,
                fromTimestamp, 
                toTimestamp, 
                rootDependencies,
                rootFlStatusEX,
                rootFlStatusTO,
                m_log);

        dg = ddClient.runDiscovery();
        
        recordDiscoveryIntoTrace(conversationId, dg);
        
        return dg;
    }
    
    private synchronized void recordDiscoveryIntoTrace(int conversationId, DependenceGraph dg)
    {
        for(Edge edge : dg.getEdges())
        {   
            DiscoveryTrace.writeDgEdgeIntoOutput(
                    m_log, 
                    conversationId,                     
                    edge.getSource().getId(),
                    edge.getTarget().getId(),
                    edge.getFlStatusEX().name(),
                    edge.getFlStatusTO().name(),
                    edge.getLastEXTimestamp(),
                    edge.getFirstTOTimestamp(),
                    edge.getTarget().getReachabilityStatus().name(),
                    edge.getTarget().getFlStatusEX().name(),
                    edge.getTarget().getFlStatusTO().name(),
                    edge.getTarget().getLastEXTimestamp(),
                    edge.getTarget().getFirstTOTimestamp());
        }
    }

}
