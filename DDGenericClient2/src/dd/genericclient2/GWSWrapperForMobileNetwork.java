
package dd.genericclient2;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.developer.WSBindingProvider;
import dd.ExperimentConfiguration;
import dd.Network;
import dd.Timestamp;
import dd.conversation.Addressing;
import dd.conversation.MessageDependenceList;
import dd.inprocessmonitor.ClientMonitor;
import dd.output.LoggingContext;
import dd.serviceregistry.discoveryclient.ServiceDiscoveryClientHelper;
import java.net.URL;
import java.util.concurrent.TimeoutException;
import javax.xml.namespace.QName;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;


/* Metro
 * In mobile network, the conversations (even the ones consisting of single message exchage)
 * might take longer than timeout period.
 * It seems to be due to some characteristics of the mobile environment.
 * When analyzing faults arriving after timeout period, it seems that the problems arrising from inability to connect 
 * or to connect but not beeing able to read any data prolongs the conversations above timeout limits.
 * Thus the used tools (metro/glassfish) are well suited for this task.
 * Nevertheless, these are the only available right now.
 * 
 * This class addresses this problem by wrapping the GWS service and doing the timeout measurement and canceling of the conversation
 * on its own. 
 * 
 * It sends request as asynchronous but behaves as sychronous, it thus has the capacity to do the measurements of timeout. 
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
    
    
    public GWSWrapperForMobileNetwork(LoggingContext log, String targetService) throws java.lang.Exception
    {
        m_log = log;
        m_targetServiceUrl = ServiceDiscoveryClientHelper.getTargetServiceUrl(log, targetService);
        
        if (m_targetServiceUrl == null)
        {
            throw(new java.lang.Exception("Not found any reachable node hosting service: " + targetService));
        }
    }
    
    public String getTargetServiceUrl()
    {
        return m_targetServiceUrl;
    }

    public MessageDependenceList invokeMethod(
            int methodId, 
            int conversationId, 
            String sourceId,
            MessageDependenceList messageDependenceList) throws java.lang.Exception    
    {
        invokeMethodAsynch(
                methodId, 
                conversationId, 
                sourceId, 
                messageDependenceList);
        
        waitTillEndOfConversation();
        
        
        if (m_wasRequestSuccessful)
        {
            m_log.log("Response received from: " + m_targetServiceUrl + " (on " + sourceId + ") with result:");
            m_resultMessageDependenceList.writeOut(m_log);
            
            return m_resultMessageDependenceList;
        }
        else
        {
            m_log.log("Service invocation exception - source: " + sourceId + " - target: " + m_targetServiceUrl);
            //m_log.log(m_invocationException);
                    
            ClientMonitor.reportFault(
                    conversationId, 
                    m_targetServiceUrl, 
                    Timestamp.now(), 
                    Exception_Exception.class, 
                    m_invocationException);
            
            throw m_invocationException;
        }
    }
    
    private void invokeMethodAsynch(
            int methodId, 
            int conversationId, 
            String sourceId,
            MessageDependenceList messageDependenceList)
    {
        URL                                     wsdlUrl = dd.Wsdl.getWsdlFilePathURLInGfDir(m_log, "GWS");
        GWS_Service                             gwsService = new GWS_Service(wsdlUrl);
        GWS                                     port = gwsService.getGWSPort();
        
        
        m_log.log(
                "Request sent to: " + m_targetServiceUrl + 
                " method id: " + methodId + 
                " conversation id: " + conversationId);
        
        messageDependenceList.addDependency(
                sourceId, 
                m_targetServiceUrl, 
                methodId);

        Network.setServiceUrl(
                (BindingProvider)port, 
                m_targetServiceUrl,
                ExperimentConfiguration.getServiceSystemMessageExchangeTimeout(m_log));
        
        ClientMonitor.reportDependence(
                m_targetServiceUrl, 
                Timestamp.now());

        
        WSBindingProvider bp = (WSBindingProvider)port;
        bp.setOutboundHeaders(
  // simple string value as a header, like <simpleHeader>stringValue</simpleHeader>
  
                new Header[]{
                Headers.create(new QName("simpleHeader"),"stringValue"),
  // create a header from JAXB object
  Headers.create(new QName("simpleHeader2"),"stringValue2")}
);
        
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
                    methodId,
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

}
