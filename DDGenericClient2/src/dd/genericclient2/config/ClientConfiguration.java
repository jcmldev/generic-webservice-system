package dd.genericclient2.config;

import dd.Configuration;
import dd.output.LoggingContext;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class ClientConfiguration 
{
    
    private int                                 m_stepDelayDistributionStart;
    private int                                 m_stepDelayDistributionEnd;
    private int                                 m_dependenceDiscoveryFrequency;
    private int                                 m_conversationIdSeed;
    private ArrayList<ClientStep>               m_steps;
    
    
    public ClientConfiguration(LoggingContext log)
    {
        loadConfigurationFromXml(log);
    }
    
    public int getStepDelayDistributionStart()
    {
        return m_stepDelayDistributionStart;
    }
    
    public int getStepDelayDistributionEnd()
    {
        return m_stepDelayDistributionEnd;
    }
    
    public int getConversationIdSeed()
    {
        return m_conversationIdSeed;
    }
    
    public int getDependenceDiscoveryFrequency()
    {
        return m_dependenceDiscoveryFrequency;
    }
    
    public ArrayList<ClientStep> getSteps()
    {
        return m_steps;
    }
    
    private void loadConfigurationFromXml(LoggingContext log)
    {
        Element             rootElement = Configuration.getGenericClientConfiguration(log);
        

        m_stepDelayDistributionStart = Integer.parseInt(rootElement.getAttribute("stepDelayDistributionStart"));
        m_stepDelayDistributionEnd = Integer.parseInt(rootElement.getAttribute("stepDelayDistributionEnd"));
        m_dependenceDiscoveryFrequency = Integer.parseInt(rootElement.getAttribute("dependenceDiscoveryFrequency"));
        m_conversationIdSeed = Integer.parseInt(rootElement.getAttribute("conversationIdSeed"));
        
        loadSteps(rootElement);
    }
    
    private void loadSteps(Element rootElement)
    {
        NodeList                stepElements = rootElement.getElementsByTagName("step");
        Element                 stepElement;
        ClientStep              step;
        
        
        m_steps = new ArrayList<>();
        
        for(int i = 0; i<stepElements.getLength(); i++)
        {
            stepElement = (Element) stepElements.item(i);
            step = new ClientStep(stepElement);
            m_steps.add(step);
        }
    }
}