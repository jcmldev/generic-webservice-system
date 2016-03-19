
package dd.serviceregistry;

import dd.Configuration;
import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlFileServiceRegistry implements ServiceRegistry 
{
    private ArrayList<XmlFileServiceRecord>         m_records;
    private LoggingContext                          m_log;
    
    
    public XmlFileServiceRegistry(LoggingContext log)
    {
        m_log = log;
        loadConfigurationFromXml();
    }

    @Override
    public ServiceRecord getServiceRecord(String serviceName, ServiceSelector serviceSelector) 
    {
        HashMap<String, XmlFileServiceRecord>       services = new HashMap<>();
        String[]                                    hosts;
        String                                      selectedHost;
        XmlFileServiceRecord                        selectedRecord = null;
        
        
        for(XmlFileServiceRecord record: m_records)
        {
            if (record.getServiceName().equals(serviceName))
            {
                services.put(record.getHostAddress(), record);
            }
        }
        
        if (!services.isEmpty())
        {
            hosts = services.keySet().toArray(new String[0]);
            selectedHost = serviceSelector.selectService(hosts);
            
            if (selectedHost != null)
            {
                selectedRecord = services.get(selectedHost);
            }
        }
        
        return selectedRecord;
    }
    
    private void loadConfigurationFromXml()
    {
        Element             rootElement = Configuration.getServiceRegistryConfiguration(m_log);
        
        
        loadRecords(rootElement);
    }
    
    private void loadRecords(Element rootElement)
    {
        NodeList                nodeElements = rootElement.getElementsByTagName("node");
        Element                 nodeElement;
        NodeList                serviceElements;
        Element                 serviceElement;
        XmlFileServiceRecord    record;
        
        
        m_records = new ArrayList<>();
        
        for(int i = 0; i<nodeElements.getLength(); i++)
        {
            nodeElement = (Element) nodeElements.item(i);
            serviceElements = nodeElement.getElementsByTagName("service");
            
            for(int y = 0; y<serviceElements.getLength(); y++)
            {
                serviceElement = (Element) serviceElements.item(y);
                record = new XmlFileServiceRecord(nodeElement, serviceElement);
                m_records.add(record);
            }
        }
    }
}