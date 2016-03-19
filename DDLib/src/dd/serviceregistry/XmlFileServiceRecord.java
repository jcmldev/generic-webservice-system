
package dd.serviceregistry;

import org.w3c.dom.Element;

public class XmlFileServiceRecord  implements ServiceRecord
{    
    private String          m_serviceName;
    private String          m_hostAddress;
    
    
    public XmlFileServiceRecord(Element nodeElement, Element serviceElement)
    {
        m_serviceName = serviceElement.getAttribute("name");
        m_hostAddress = nodeElement.getAttribute("ip");
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