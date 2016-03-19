
package dd.gossipprotocol;

import dd.Network;
import dd.output.LoggingContext;
import fl.ServiceFaultGtTrace;
import java.util.List;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;


public class MultihopGossipProtocolClient 
{
    private LoggingContext                  m_log;
    private GossipProtocolServiceWrapper    m_localGps;

    
    public MultihopGossipProtocolClient (LoggingContext log)
    {
        m_log = log;
        m_localGps = new GossipProtocolServiceWrapper(log, "127.0.0.1");
    }

    public String[] getInterDependenciesForTimeWindow(
            String dependentServiceId,
            long fromTimestamp, 
            long toTimestamp)
    {
        List<String>                                antecedents;
        
        
        antecedents = m_localGps.getPort().gpMultihopGetInterDependenciesForTimeWindow(
                dependentServiceId, 
                fromTimestamp, 
                toTimestamp);
        
        return antecedents.toArray(new String[0]);
    }
    
    public String[] getInterDependenciesForTimeWindowAntecedent(
            String dependentServiceId,
            long fromTimestamp, 
            long toTimestamp)
    {
        List<String>                                antecedents;
        
        
        antecedents = m_localGps.getPort().gpMultihopGetInterDependenciesForTimeWindowAntecedent(
                dependentServiceId, 
                fromTimestamp, 
                toTimestamp);
        
        return antecedents.toArray(new String[0]);
    }
    
    public String[] getSystemDg(
            long fromTimestamp, 
            long toTimestamp)
    {
        List<String>                                systemDg;
        
        
        systemDg = m_localGps.getPort().gpMultihopGetSystemDG( 
                fromTimestamp, 
                toTimestamp);
        
        return systemDg.toArray(new String[0]);        
    }
    
    public void monitorRecordClientConversationFault(
            int conversationId,
            String clientProcessId,
            String frontEndServiceId,
            long faultTimestamp,
            ServiceFaultGtTrace.FaultType faultType,
            String exceptionType,
            String exceptionMessage,
            String exceptionCauseType,
            String exceptionCauseMessage)
    {
        AsyncHandler<dd.bck.MonitorRecordClientConversationFaultResponse> asyncHandler = 
            new AsyncHandler<dd.bck.MonitorRecordClientConversationFaultResponse>() 
        {
            @Override
            public void handleResponse(Response<dd.bck.MonitorRecordClientConversationFaultResponse> response) 
            {
            }
        };

        m_localGps.getPort().monitorRecordClientConversationFaultAsync(
                conversationId,
                clientProcessId,
                frontEndServiceId, 
                faultTimestamp, 
                faultType.name(), 
                exceptionType,
                exceptionMessage, 
                exceptionCauseType,
                exceptionCauseMessage,
                asyncHandler);
    }
    
    public void monitorRecordClientDependence(
            String clientProcessId,
            String frontEndServiceId,
            long faultTimestamp)
    {
        AsyncHandler<dd.bck.MonitorRecordClientDependenceResponse> asyncHandler = 
            new AsyncHandler<dd.bck.MonitorRecordClientDependenceResponse>() 
        {
            @Override
            public void handleResponse(Response<dd.bck.MonitorRecordClientDependenceResponse> response) 
            {
            }
        };

        m_localGps.getPort().monitorRecordClientDependenceAsync(
                clientProcessId, 
                frontEndServiceId, 
                faultTimestamp, 
                asyncHandler);
    }
}
