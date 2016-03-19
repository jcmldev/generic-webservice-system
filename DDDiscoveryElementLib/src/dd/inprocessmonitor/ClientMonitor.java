
package dd.inprocessmonitor;

import dd.Network;
import dd.gossipprotocol.MultihopGossipProtocolClient;
import dd.output.LoggingContext;
import fl.ServiceFaultGtTrace;


/*
 This component eliminates need for monitor in dll attached to metro
 - it has the same functionality in terms of monitoring messages
 - also it reports faults / which was not yet implemented in the dll monitor and would require somewhat more work
 (thus saving time here)
 */

public class ClientMonitor 
{
    public static void reportDependence(String frontEndServiceId, long timestamp)
    {
        LoggingContext                      log = LoggingContext.getContext(new ClientMonitor(),"");
        MultihopGossipProtocolClient        multihopGPClient = new MultihopGossipProtocolClient(log);
        
        
        //log.log("reportDependence");
        
        try
        {
            multihopGPClient.monitorRecordClientDependence(
                    Network.getClientApplicationProcessId(log),
                    frontEndServiceId, 
                    timestamp);
        }
        catch(Exception ex)
        {
            log.log(ex, false);
        }
    }
    
    public static void reportFault(
            int conversationId,
            String frontEndServiceId,
            long faultTimestamp,
            Class classOfWSExceptionWrapper,
            Exception ex)
    
    {
        LoggingContext                      log = LoggingContext.getContext(new ClientMonitor(),"");
        MultihopGossipProtocolClient        multihopGPClient = new MultihopGossipProtocolClient(log);
        ServiceFaultGtTrace.FaultType       faultType;
        String                              exceptionType;
        String                              exceptionMessage;
        String                              exceptionCauseType = "";
        String                              exceptionCauseMessage = "";
        
        //log.log("reportFault");
        
        faultType = ServiceFaultGtTrace.ClientProcess_getFaultTypeFromException(classOfWSExceptionWrapper, ex);
        exceptionType = ex.getClass().getSimpleName();
        exceptionMessage = ex.getMessage();
        
        if (ex.getCause() != null)
        {
            exceptionCauseType = ex.getCause().getClass().getSimpleName();
            exceptionCauseMessage = ex.getCause().getMessage();
        }
        
        try
        {
            multihopGPClient.monitorRecordClientConversationFault(
                    conversationId,
                    Network.getClientApplicationProcessId(log),
                    frontEndServiceId,
                    faultTimestamp,
                    faultType,
                    exceptionType,
                    exceptionMessage,
                    exceptionCauseType,
                    exceptionCauseMessage);
        }
        catch(Exception ex1)
        {
            log.log(ex1, false);
        }
    }
    
}
