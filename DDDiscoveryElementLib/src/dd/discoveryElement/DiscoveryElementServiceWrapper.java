
package dd.discoveryElement;

import dd.Network;
import dd.Wsdl;
import dd.output.LoggingContext;
import java.net.URL;
import java.util.Arrays;
import javax.xml.ws.BindingProvider;


public class DiscoveryElementServiceWrapper 
{
    LoggingContext              m_log;
    private String             m_nodeIp;
    
    
    public DiscoveryElementServiceWrapper(LoggingContext log, String nodeIp)
    {
        m_log = log;
        m_nodeIp = nodeIp;
    }    
    
    private String getServiceAddress()
    {
        return null; // in configuration
        /*return Network.getWebServiceUrl(
                m_log,
                m_nodeIp, 
                "/DDWSProvider/DiscoveryElementService");
                * */
    }
    
    public static DiscoveryElementServiceWrapper getWrapperForService(LoggingContext log, String nodeIp)
    {        
        return new DiscoveryElementServiceWrapper(log, nodeIp);
    }
    
    private dd.desWrapper.DiscoveryElementService getPort()
    {
        URL                                                    wsdlUrl = Wsdl.getWsdlFilePathURLInGfDir(m_log, "DiscoveryElementService");
        dd.desWrapper.DiscoveryElementService_Service          service = new dd.desWrapper.DiscoveryElementService_Service(wsdlUrl);
        dd.desWrapper.DiscoveryElementService                  port = service.getDiscoveryElementServicePort();
        
        
        Network.setServiceUrl(
                (BindingProvider)port, 
                getServiceAddress(),
                0); // take timeout from configuraiton
        // this timeout is set to default as any other service - only the discovery itself uses the timeout from configuration
        
        return port;
    }
    
    public boolean runDiscoveryStartingWithFrontends(
                    int conversationId,
                    String clientId,
                    String[] frontendUrls,
                    long fromTimestamp,
                    long toTimestamp)
    {
        boolean                     success;

        
        try
        {        
            getPort().runDiscoveryStartingWithFrontends(
                    conversationId, 
                    clientId, 
                    Arrays.asList(frontendUrls), 
                    fromTimestamp, 
                    toTimestamp);
 
            success = true;
        }
        catch(java.lang.Exception ex)
        {
            m_log.log("Request to runDiscoveryStartingWithFrontends failed with exception ...");
            m_log.log(ex);
            
            success = false;
        }
        
        return success;    
    }
}
