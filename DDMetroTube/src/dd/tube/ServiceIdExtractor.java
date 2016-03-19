
package dd.tube;

import com.sun.grizzly.http.HttpWorkerThread;
import com.sun.grizzly.http.ProcessorTask;
import com.sun.grizzly.tcp.ActionCode;
import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.util.buf.MessageBytes;
import com.sun.xml.ws.api.message.Packet;
import dd.ExperimentConfiguration;
import dd.Network;
import dd.output.LoggingContext;
import java.net.URL;



public class ServiceIdExtractor {

    private LoggingContext              m_log;
    private Packet                      m_packet;
    
    
    public ServiceIdExtractor(LoggingContext log, Packet packet)
    {
        m_log = log;
        m_packet = packet;
    }
    
    public boolean getIsRunningOnServer()
    {
        return (Thread.currentThread() instanceof HttpWorkerThread);
    }
    
    public String getIncomingRequestMessageSourceHostIp()
    {
        
        HttpWorkerThread        pHttpWorkerThread;
        ProcessorTask           pProcessorTask;
        Request                 pRequest;
        String                  host = null;


        try
        {   
            pHttpWorkerThread = (HttpWorkerThread) Thread.currentThread();
            pProcessorTask = pHttpWorkerThread.getProcessorTask();
            pRequest = pProcessorTask.getRequest();
            
            pRequest.action(ActionCode.ACTION_REQ_HOST_ADDR_ATTRIBUTE, null); 
            host = pRequest.remoteAddr().toString();
        }
        catch(Exception e)
        {
            m_log.log("getIncomingRequestMessageSourceHostIp");
            m_log.log(e);        
        }
               
        return host;
    }
    
    // this applies to both incoming and outgoing requests
    public String getServerRequestMessageTargetService()
    {
        HttpWorkerThread        pHttpWorkerThread;
        ProcessorTask           pProcessorTask;
        Request                 pRequest;
        MessageBytes            pMessageBytes;
        String                  componentId;


        try
        {   
            pHttpWorkerThread = (HttpWorkerThread) Thread.currentThread();
            pProcessorTask = pHttpWorkerThread.getProcessorTask();
            pRequest = pProcessorTask.getRequest();
            pMessageBytes = pRequest.unparsedURI();
            componentId = pMessageBytes.toString();         
        }
        catch(Exception e)
        {
            m_log.log("getServerRequestMessageTargetService");
            m_log.log(e);        
            componentId = "Exception thrown while extracting source address";
        }

        return componentId;    
    } 
    
    public String getIncomingRequestMessageTargetUrl()
    {
        String      url = ExperimentConfiguration.getTubeIncomingRequestMessageTargetUrlTemplate(m_log);
        
        
        url = Network.serviceUrlSetIpInUrl(url, Network.getLocalHostIp(m_log));
        url = Network.serviceUrlSetServiceNameInUrl(url, getServerRequestMessageTargetService());
        
        return url;
    }
    
    public String getOutgoingRequestMessageClientApplicationProcessId()
    {
        return Network.getClientApplicationProcessId(m_log);
    }
    
    public String getOutgoingRequestMessageTargetUrl()
    {   	
        //The endpoint address to which this message is sent to. 
        //Must not be null for a request message on the client. Otherwise it's null. 
    	String endpointAddress = null;
                
                
        try
        {
            endpointAddress = m_packet.endpointAddress.toString();
        }
        catch(Exception e)
        {
            m_log.log("getOutgoingRequestMessageTargetUrl");
            m_log.log(e);
            //endpointAddress = "Exception thrown while extracting target address";
        }
        
        return endpointAddress;
    }
    
    public String getOutgoingRequestMessageTargetHostIp()
    {
        URL             endpointAddress = m_packet.endpointAddress.getURL();
        String          host = null;
    
        
        try
        {
            host = endpointAddress.getHost();
        }
        catch(Exception e)
        {
            m_log.log("getOutgoingRequestMessageTargetHostIp");            
            m_log.log(e);
        }
        
        return host;
    }
    
    public String getOutgoingSourceId()
    {
        if (getIsRunningOnServer())
        {
            return getServerRequestMessageTargetService();
        }
        else
        {
            return getOutgoingRequestMessageClientApplicationProcessId();
        }
        /*
        // check if is running in server
        if (Thread.currentThread() instanceof HttpWorkerThread)
        {
            return getClientServerComponentId();
        }
        else
        { // or in client application
            return getClientApplicationProcessId();
        }
        **/
    }

    public String getOutgoingTargetId()
    {
        return getOutgoingRequestMessageTargetUrl();
    }
}
