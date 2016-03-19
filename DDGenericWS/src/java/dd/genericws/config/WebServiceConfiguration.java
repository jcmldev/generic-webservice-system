
package dd.genericws.config;

import dd.Configuration;
import dd.output.LoggingContext;
import org.w3c.dom.Element;

public class WebServiceConfiguration 
{

    private static WebServiceConfiguration           s_instance;
    
    private String                                   m_serviceName;
    private MethodList                               m_methods;
    
    
    private WebServiceConfiguration(LoggingContext log, String contextPath)
    {
        loadConfigurationFromXml(log, contextPath);
    }
    
    public static void loadConfiguration(LoggingContext log, String contextPath)
    {
        s_instance = new WebServiceConfiguration(log, contextPath);
    }
    
    public static WebServiceConfiguration getInstance()
    {
        return s_instance;
    }
    
    public String getServiceName()
    {
        return m_serviceName;
    }
    
    public MethodList getMethods()
    {
        return m_methods;
    }
    
    private void loadConfigurationFromXml(LoggingContext log, String serviceName)
    {
        Element             serviceElement = Configuration.getServiceConfigurationOnLocalNode(log, serviceName);
        
        
        m_serviceName = serviceElement.getAttribute("name");
        m_methods = new MethodList(serviceElement);
    }
}