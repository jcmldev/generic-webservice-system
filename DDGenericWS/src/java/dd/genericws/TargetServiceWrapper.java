package dd.genericws;

import com.sun.xml.ws.developer.WSBindingProvider;
import dd.ExperimentConfiguration;
import dd.Network;
import dd.conversation.Addressing;
import dd.conversation.MessageDependenceList;
import dd.output.LoggingContext;
import dd.output.ServiceGtTrace;
import dd.serviceregistry.discoveryclient.ServiceDiscoveryClientHelper;
import fl.ServiceFaultGtTrace;
import java.net.URL;
import javax.xml.ws.BindingProvider;


public class TargetServiceWrapper 
{
    private LoggingContext              m_log;
    private String                      m_targetServiceUrl;
    
    
    public TargetServiceWrapper(LoggingContext log) 
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
        URL                                                   wsdlUrl = dd.Wsdl.getWsdlFilePathURLInGfDir(m_log, "GWS");
        dd.gwswrapper.GWS_Service                             gwsClient = new dd.gwswrapper.GWS_Service(wsdlUrl);
        dd.gwswrapper.GWS                                     port = gwsClient.getGWSPort();
        MessageDependenceList                                 resultMessageDependenceList;
        String                                                resultMessageDependenceListString;
        

        
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
        
        Addressing.setHeadersOnPort(
             (WSBindingProvider)port, 
             conversationId, 
             sourceId);
        
        try
        {
            resultMessageDependenceListString = port.invokeMethod(
                    targetMethodId, 
                    messageDependenceList.toString());
        }
        catch(java.lang.Exception ex)
        {
            m_log.log("Service invocation exception - source: " + sourceId + " - target: " + m_targetServiceUrl);
            m_log.log(ex);
            
            ServiceFaultGtTrace.recordServiceNetworkException(
                    m_log, 
                    conversationId, 
                    sourceId, 
                    m_targetServiceUrl, 
                    dd.gwswrapper.Exception_Exception.class, 
                    ex);
            
            throw ex;
        }
        finally
        {
            ServiceGtTrace.serviceReceivedResponse(
                    m_log,
                    serviceName,
                    conversationId,
                    sourceId, 
                    m_targetServiceUrl, 
                    targetMethodId);
        }
                
        resultMessageDependenceList = MessageDependenceList.fromString(
                    resultMessageDependenceListString);
        
        m_log.log("Response received from: " + m_targetServiceUrl + " (on " + sourceId + ") with result:");
        resultMessageDependenceList.writeOut(m_log);

        return resultMessageDependenceList;
    }
}
