package dd.serviceregistry.discoveryclient;

import dd.output.LoggingContext;
import dd.serviceregistry.RoutingTableServiceSelector;
import dd.serviceregistry.ServiceSelector;

public class ServiceDiscoveryClientHelper 
{

    
    // xml file based service discovery
    public static String getTargetServiceUrl(LoggingContext log, String targetServiceName)
    {
        ServiceDiscoveryProvider        sdp = new ServiceDiscoveryProviderXmlFile(log);
        ServiceSelector                 serviceSelector = new RoutingTableServiceSelector(log);
        String                          serviceUrl;
        
        
        serviceUrl = sdp.discoverServiceUrl(targetServiceName, serviceSelector);
        
        log.log(
                "Requested service: " + 
                targetServiceName + 
                " found url: " + 
                serviceUrl);
        
        return serviceUrl;
    }
    

    /* !!!!!!!!!!!!!!!!!!!!!!!!! before the servicediscoveryfile.java was referenced from dsplib !!!
    // DSP based service discovery
    public static String getTargetServiceUrl(LoggingContext log, String targetServiceName)
    {
        ServiceDiscoveryProvider        sdp = new ServiceDiscoveryProviderDSP(log);
        ServiceSelector                 serviceSelector = new RoutingTableServiceSelector(log);
        String                          serviceUrl;
        
        
        serviceUrl = sdp.discoverServiceUrl(targetServiceName, serviceSelector);
        
        log.log(
                "(DSP)Requested service: " + 
                targetServiceName + 
                " found url: " + 
                serviceUrl);
        
        return serviceUrl;
    }
*/
}