
package dd.serviceregistry.discoveryclient;

import dd.serviceregistry.ServiceSelector;

public interface ServiceDiscoveryProvider 
{
    public String discoverServiceUrl(String targetServiceName, ServiceSelector serviceSelector);               
}