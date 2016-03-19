
package dd.genericws.config;

import org.w3c.dom.Element;

public class MethodStep {

    private String                  m_targetServiceName;
    private int                     m_targetMethod;
    private int                     m_endDelayDistributionStart;
    private int                     m_endDelayDistributionEnd;


    public MethodStep(Element methodStepElement)
    {
        m_targetServiceName = methodStepElement.getAttribute("targetService");
        m_targetMethod = Integer.parseInt(methodStepElement.getAttribute("targetMethod"));
        m_endDelayDistributionStart = Integer.parseInt(methodStepElement.getAttribute("endDelayDistributionStart"));
        m_endDelayDistributionEnd = Integer.parseInt(methodStepElement.getAttribute("endDelayDistributionEnd"));
    }
    
    public String getTargetServiceName()
    {
        return m_targetServiceName;
    }

    public int getTargetMethod()
    {
        return m_targetMethod;
    }

    public int getEndDelayDistributionStart()
    {
        return m_endDelayDistributionStart;
    }
    
    public int getEndDelayDistributionEnd()
    {
        return m_endDelayDistributionEnd;
    }   
}