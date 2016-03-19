
package dd.serviceregistry.discoveryclient;

import dd.serviceregistry.ServiceRecord;



public class DSPServiceRecord implements ServiceRecord
{
    private String          m_serviceName;
    private String          m_hostAddress;
    
    
    public DSPServiceRecord(String nodeIp, String serviceName)
    {
        m_serviceName = serviceName;
        m_hostAddress = nodeIp;
    }
    
    @Override
    public String getServiceName ()
    {
        return m_serviceName;
    }
    
    @Override
    public String getHostAddress ()
    {
        return m_hostAddress;
    }
}
