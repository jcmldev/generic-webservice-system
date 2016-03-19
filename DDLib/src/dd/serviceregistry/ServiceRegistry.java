
package dd.serviceregistry;

public interface ServiceRegistry 
{
    public ServiceRecord getServiceRecord (String interfaceName, ServiceSelector serviceSelector);
}