    
package dd.serviceregistry.discoveryclient;

import dd.Network;
import dd.output.LoggingContext;
import dd.serviceregistry.ServiceRecord;
import dd.serviceregistry.ServiceSelector;

public class ServiceDiscoveryProviderDSP implements ServiceDiscoveryProvider
{
    private LoggingContext                  m_log;
    //private XmlFileServiceRegistry          m_serviceRegistry;

    
    public ServiceDiscoveryProviderDSP (LoggingContext log)
    {
        m_log = log;
        //m_serviceRegistry = new XmlFileServiceRegistry(log);        
    }
    
    @Override
    public String discoverServiceUrl(String targetServiceName, ServiceSelector serviceSelector) 
    {
        int                             serviceId;
        String[]                        hosts;
        ServiceRecord                   serviceRecord = null;
        String                          serviceUrl = null;
        String                          selectedHost;
     
        
        serviceId = Integer.parseInt(targetServiceName.substring(2));
        
        hosts = ServiceDiscoveryFile.getNodesHostingService(serviceId);
        
        
        if (hosts != null)
        {
            selectedHost = serviceSelector.selectService(hosts);
            
            if (selectedHost != null)
            {
                serviceRecord = new DSPServiceRecord(selectedHost, targetServiceName);
            }
        }

        if (serviceRecord == null)
        {
            m_log.log(
                "Not found any node hosting requested service! - targetServiceName: " + 
                targetServiceName);
        }
        else
        {
            serviceUrl = Network.getGenericWsUrl(
                    m_log,
                    serviceRecord.getHostAddress(), 
                    targetServiceName);
        }
        
        return serviceUrl;
    }
}