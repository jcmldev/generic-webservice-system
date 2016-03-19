
package dd.genericws.config;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MethodPlan 
{
    private int                     m_startDelayDistributionStart;
    private int                     m_startDelayDistributionEnd;
    private int                     m_endDelayDistributionStart;
    private int                     m_endDelayDistributionEnd;
    private MethodStep[]            m_steps;
    
    
    public MethodPlan(Element methodElement)
    {
        m_startDelayDistributionStart = Integer.parseInt(methodElement.getAttribute("startDelayDistributionStart"));
        m_startDelayDistributionEnd = Integer.parseInt(methodElement.getAttribute("startDelayDistributionEnd"));
        m_endDelayDistributionStart = Integer.parseInt(methodElement.getAttribute("endDelayDistributionStart"));
        m_endDelayDistributionEnd = Integer.parseInt(methodElement.getAttribute("endDelayDistributionEnd"));
        loadSteps(methodElement);
    }
    
    public int getStartDelayDistributionStart()
    {
        return m_startDelayDistributionStart;
    }
    
    public int getStartDelayDistributionEnd()
    {
        return m_startDelayDistributionEnd;
    }
    
    public int getEndDelayDistributionStart()
    {
        return m_endDelayDistributionStart;
    }
    
    public int getEndDelayDistributionEnd()
    {
        return m_endDelayDistributionEnd;
    }
    
    public MethodStep[] getSteps()
    {
        return m_steps;
    }
    
    private void loadSteps(Element methodElement)
    {
        NodeList                stepElements = methodElement.getElementsByTagName("step");
        Element                 stepElement;
        MethodStep              step;
        ArrayList<MethodStep>   steps = new ArrayList<MethodStep>();
        
        
        for(int i = 0; i<stepElements.getLength(); i++)
        {
            stepElement = (Element) stepElements.item(i);
            step = new MethodStep(stepElement);
            steps.add(step);
        }
        
        m_steps = steps.toArray(new MethodStep[0]);
    }
}