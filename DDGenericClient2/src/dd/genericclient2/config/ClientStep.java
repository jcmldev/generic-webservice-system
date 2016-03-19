
package dd.genericclient2.config;

import org.w3c.dom.Element;

public class ClientStep {

    private String          m_targetService;
    private int             m_targetMethod;
    

    public ClientStep(Element stepElement)
    {
        m_targetService = stepElement.getAttribute("targetService");
        m_targetMethod = Integer.parseInt(stepElement.getAttribute("targetMethod"));
    }

    public String getTargetService()
    {
        return m_targetService;
    }

    public int getTargetMethod()
    {
        return m_targetMethod;
    }
}