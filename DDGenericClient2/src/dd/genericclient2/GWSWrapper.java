package dd.genericclient2;

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
import javax.xml.ws.BindingProvider;


public class GWSWrapper 
{
    private LoggingContext      m_log;
    private String              m_targetServiceUrl;
    
    
    public GWSWrapper(LoggingContext log, String targetService) throws java.lang.Exception
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
            MessageDependenceList messageDependenceList, 
            boolean writeOut) throws java.lang.Exception    
    {
        URL wsdlUrl = dd.Wsdl.getWsdlFilePathURLInGfDir(m_log, "GWS");
        GWS_Service                             gwsService = new GWS_Service(wsdlUrl);
        GWS                                     port = gwsService.getGWSPort();
        MessageDependenceList                   resultMessageDependenceList;
        String                                  resultMessageDependenceListString;
        
        
        if (writeOut)
        {
            m_log.log(
                    "Request sent to: " + m_targetServiceUrl + 
                    " method id: " + methodId + 
                    " conversation id: " + conversationId);
        }
        
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
        
       Addressing.setHeadersOnPort(
                    (WSBindingProvider)port, 
                    conversationId, 
                    sourceId);
     
        try
        {
            resultMessageDependenceListString = port.invokeMethod(
                    methodId,
                    messageDependenceList.toString());
        }
        catch(java.lang.Exception ex)
        {
            m_log.log("Service invocation exception - source: " + sourceId + " - target: " + m_targetServiceUrl);
            
            
            ClientMonitor.reportFault(
                    conversationId, 
                    m_targetServiceUrl, 
                    Timestamp.now(), 
                    Exception_Exception.class, 
                    ex);
            
            throw ex;
        }
                
        resultMessageDependenceList = MessageDependenceList.fromString(
                    resultMessageDependenceListString);
        
        if (writeOut)
        {
            m_log.log("Response received from: " + m_targetServiceUrl + " (on " + sourceId + ") with result:");
            resultMessageDependenceList.writeOut(m_log);
        }
        
        return resultMessageDependenceList;
    }
}