
package dd.discoveryclient;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.Wsdl;
import dd.output.LoggingContext;
import java.net.URL;
import java.util.List;
import javax.xml.ws.BindingProvider;


public class DependenceDataProviderWrapper 
{
    private String                  m_nodeIp;
    private LoggingContext          m_log;
    
    public DependenceDataProviderWrapper(LoggingContext log, String nodeIp)
    {
        m_nodeIp = nodeIp;
        m_log = log;
    }    
    
    public static DependenceDataProviderWrapper getWrapperForService(LoggingContext log, String serviceUrl)
    {
        String              nodeIp;
        
        
        nodeIp = Network.getHostIpFromUrl(log, serviceUrl);
        
        return new DependenceDataProviderWrapper(log, nodeIp);
    }
    
    private dd.ddpwrapper.DependenceDataProvider getPort()
    {
        URL                                                       wsdlUrl = Wsdl.getWsdlFilePathURLInGfDir(m_log, "DependenceDataProvider");
        dd.ddpwrapper.DependenceDataProvider_Service              service = new dd.ddpwrapper.DependenceDataProvider_Service(wsdlUrl);
        dd.ddpwrapper.DependenceDataProvider                      port = service.getDependenceDataProviderPort();
        String                                                    serviceUrl;
        
        
        serviceUrl = Network.serviceUrlSetIpInUrl(
                ExperimentConfiguration.getDependenceDiscoveryServiceAddress(m_log), 
                m_nodeIp);
        
        Network.setServiceUrl(
                (BindingProvider)port, 
                serviceUrl,
                ExperimentConfiguration.getDependenceDiscoveryRequestTimeout(m_log));
        
        return port;
    }
    
    public String[] getInterDependenciesForTimeWindow(int conversationId, String serviceId, long from, long to)
    {
        List<String>                                        resultList;
        String[]                                            result = null;
        
        
        try
        {        
            resultList = getPort().getInterDependenciesForTimeWindow(
                    conversationId,
                    serviceId, 
                    from, 
                    to);
            
            result = resultList.toArray(new String[0]);
        }
        catch(Exception ex)
        {
            m_log.log("Request to DependenceDataProvider.getInterDependenciesForTimeWindow: " + 
                    serviceId + " failed with exception ...");
            m_log.log(ex, false);
        }
        
        return result;    
    }
    
    public String[] getInterDependenciesForTimeWindowDistributed(
            int conversationId, 
            String serviceId, 
            long from, 
            long to)
    {
        List<String>                                        resultList;
        String[]                                            result = null;
        
        
        try
        {        
            resultList = getPort().getInterDependenciesForTimeWindowDistributed(
                    conversationId,
                    serviceId, 
                    from, 
                    to);
            
            result = resultList.toArray(new String[0]);
        }
        catch(Exception ex)
        {
            m_log.log("Request to DependenceDataProvider.getInterDependenciesForTimeWindowDistributed: " + 
                    serviceId + " failed with exception ...");
            m_log.log(ex);
        }
        
        return result;    
    }
}