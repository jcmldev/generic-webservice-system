    
package dd.serviceregistry.discoveryclient;

import dd.Network;
import dd.output.LoggingContext;
import dd.serviceregistry.ServiceRecord;
import dd.serviceregistry.ServiceSelector;
import dd.serviceregistry.XmlFileServiceRegistry;

public class ServiceDiscoveryProviderXmlFile implements ServiceDiscoveryProvider
{
    private LoggingContext                  m_log;
    private XmlFileServiceRegistry          m_serviceRegistry;

    
    public ServiceDiscoveryProviderXmlFile (LoggingContext log)
    {
        m_log = log;
        m_serviceRegistry = new XmlFileServiceRegistry(log);
    }
    
    @Override
    public String discoverServiceUrl(String targetServiceName, ServiceSelector serviceSelector) 
    {
        ServiceRecord                   serviceRecord;
        String                          serviceUrl = null;
        
        
        serviceRecord = m_serviceRegistry.getServiceRecord(
                targetServiceName, 
                serviceSelector);
        
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