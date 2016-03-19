
package dd.genericws;

import com.sun.grizzly.http.HttpWorkerThread;
import com.sun.grizzly.http.ProcessorTask;
import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.util.buf.MessageBytes;
import com.sun.xml.ws.developer.WSBindingProvider;
import dd.ExperimentConfiguration;
import dd.Network;
import dd.conversation.Addressing;
import dd.conversation.MessageDependenceList;
import dd.gwswrapper.InvokeMethodResponse;
import dd.monitor.ServerMonitor;
import dd.output.LoggingContext;
import dd.output.ServiceGtTrace;
import dd.serviceregistry.discoveryclient.ServiceDiscoveryClientHelper;
import fl.ServiceFaultGtTrace;
import java.net.URL;
import java.util.concurrent.TimeoutException;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;


/* Metro
 * In mobile network, the conversations (even the ones consisting of single message exchage)
 * might take longer than timeout period.
 * It seems to be due to some characteristics of the mobile environment.
 * When analyzing faults arriving after timeout period, it seems that the problems arrising from inabiliyt to connect 
 * or to connect but not beeing able to read any data prolonges to conversations above timeout limits.
 * Thus the used tools (metro/glassfish) are not suitable for this task.
 * Nevertheless, these are the only available right now.
 * 
 * This class addresses this problem by wrapping the GWS service and doing the timeout measurement and canceling of the conversation
 * on its own. 
 * 
 * It sends request as asynchronous but behaves as sychronous, it thus has space to do the measurements for timeout. 
 * 
 */


public class GWSWrapperForMobileNetwork 
{
    private LoggingContext          m_log;
    private String                  m_targetServiceUrl;
    private MessageDependenceList   m_resultMessageDependenceList = null;
    private boolean                 m_wasRequestSuccessful = false;
    private boolean                 m_isDone = false;
    private java.lang.Exception     m_invocationException;
    //private Object                  m_waitingObject;
    
    
    public GWSWrapperForMobileNetwork(LoggingContext log) throws java.lang.Exception
    {
        m_log = log;
    }
    
    private void discoverTargetService(
            int conversationId, 
            String sourceId,
            String targetService) throws java.lang.Exception
    {
        m_targetServiceUrl = ServiceDiscoveryClientHelper.getTargetServiceUrl(m_log, targetService);
        
        if (m_targetServiceUrl == null)
        {
            Exception ex = new Exception("Not found any reachable node hosting service: " + targetService);
            
            
            ServiceFaultGtTrace.recordServiceException(
                    m_log, 
                    conversationId, 
                    sourceId, 
                    ex);
                        
            throw(ex);
        }
    }
    
    public MessageDependenceList invokeMethod(
            String serviceName,
            String targetServiceName,
            int targetMethodId, 
            int conversationId, 
            String sourceId,
            MessageDependenceList messageDependenceList) throws java.lang.Exception    
    {
        invokeMethodAsynch(
                serviceName,
                targetServiceName,
                targetMethodId, 
                conversationId, 
                sourceId, 
                messageDependenceList);
        
        waitTillEndOfConversation();
        
        ServiceGtTrace.serviceReceivedResponse(
            m_log,
            serviceName,
            conversationId,
            sourceId, 
            m_targetServiceUrl, 
            targetMethodId);
        
        if (m_wasRequestSuccessful)
        {
            m_log.log("Response received from: " + m_targetServiceUrl + " (on " + sourceId + ") with result:");
            m_resultMessageDependenceList.writeOut(m_log);
            
            return m_resultMessageDependenceList;
        }
        else
        {
            m_log.log("Service invocation exception - source: " + sourceId + " - target: " + m_targetServiceUrl);
            m_log.log(m_invocationException);
            
            ServiceFaultGtTrace.recordServiceNetworkException(
                    m_log, 
                    conversationId, 
                    sourceId, 
                    m_targetServiceUrl, 
                    dd.gwswrapper.Exception_Exception.class, 
                    m_invocationException);

            
            throw m_invocationException;
        }
    }
    
    private void invokeMethodAsynch(
            String serviceName,
            String targetServiceName,
            int targetMethodId, 
            int conversationId, 
            String sourceId,
            MessageDependenceList messageDependenceList) throws java.lang.Exception
    {
        URL                                     wsdlUrl = dd.Wsdl.getWsdlFilePathURLInGfDir(m_log, "GWS");
        dd.gwswrapper.GWS_Service               gwsService = new dd.gwswrapper.GWS_Service(wsdlUrl);
        dd.gwswrapper.GWS                       port = gwsService.getGWSPort();
        
        
        discoverTargetService(
                conversationId, 
                sourceId, 
                targetServiceName);

        m_log.log(
                    "Request sent to: " + m_targetServiceUrl + 
                    " method id: " + targetMethodId + 
                    " conversation id: " + conversationId);
        
        messageDependenceList.addDependency(
                sourceId, 
                m_targetServiceUrl, 
                targetMethodId);

        Network.setServiceUrl(
                (BindingProvider)port, 
                m_targetServiceUrl,
                ExperimentConfiguration.getServiceSystemMessageExchangeTimeout(m_log));

        ServiceGtTrace.serviceSentRequest(
                m_log,
                serviceName,
                conversationId,
                sourceId, 
                m_targetServiceUrl, 
                targetMethodId);
        
        
        //!!! Because the request is sent asynchronously the monitor can not pickup the requesting service (this one) 
        // this is due to new thread beeing created and used to sent the message, and this thread does not have the service context
        // thus for now, the monitoring method is executed from here
        recordDependency(serviceName, m_targetServiceUrl);

        try
        {
            AsyncHandler<InvokeMethodResponse> asyncHandler = 
                new AsyncHandler<InvokeMethodResponse>() 
            {

                @Override
                public void handleResponse(Response<InvokeMethodResponse> response) 
                {
                    try 
                    {
                        String resultMessageDependenceListString = 
                                response.get().getReturn();
                        
                        m_resultMessageDependenceList = MessageDependenceList.fromString(
                            resultMessageDependenceListString);

                        m_wasRequestSuccessful = true;
                    } 
                    catch (java.lang.Exception ex) 
                    {
                        m_invocationException = ex;
                    }
                    
                    m_isDone = true;
                   // m_waitingObject.notifyAll();
                }
            };
            
            Addressing.setHeadersOnPort(
                    (WSBindingProvider)port, 
                    conversationId, 
                    sourceId);
            
            java.util.concurrent.Future<? extends Object> result = port.invokeMethodAsync(
                    targetMethodId,
                    messageDependenceList.toString(), 
                    asyncHandler);
        } 
        catch (java.lang.Exception ex) 
        {
            m_invocationException = ex;
            m_isDone = true;
        }
    }
    
    // this method waits till either
    // - response or exception arrives
    // - or timeout expires
    private synchronized void waitTillEndOfConversation() throws InterruptedException
    {
        int configurationTimeout = ExperimentConfiguration.getServiceSystemMessageExchangeTimeout(m_log);
        int waitingStepSize = 1;
        int waitingCounter = 0;
        
        
        // not to interfere with some inner delays
        configurationTimeout += ExperimentConfiguration.getServiceSystemMessageExchangeTimeoutAdditionalWraperTime(m_log);
        
        // debug
        //configurationTimeout = 20000;
        
        // !!! the notify/notifyall does not seem to be workin within the asynch method
        //m_waitingObject = this;
        //m_waitingObject.wait(configurationTimeout);
        
        while(true)
        {
            this.wait(waitingStepSize);
            waitingCounter += waitingStepSize;
            if (m_isDone) { break; }
            if (waitingCounter >= configurationTimeout) 
            {
                m_invocationException = new TimeoutException("Timeout caught by service wrapper");
                return;
            }
        }
    }

    
    private void recordDependency(String sourceServiceName, String outgoingDependency)
    {
        ServerMonitor           serverMonitor = ServerMonitor.getInstance();
        
        
        sourceServiceName = "/" + sourceServiceName + "/GWS";
        
        serverMonitor.addInterDependencyOccurrence(
                    sourceServiceName, 
                    outgoingDependency);
    }
    
    /*
    public String getContextServiceName()
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
    */
}
