
package dd.genericws.config;

import dd.genericws.config.MethodPlan;
import org.w3c.dom.Element;

public class Method {

    private int             m_id;
    private MethodPlan      m_plan;
    

    public Method(Element methodElement)
    {
        //m_id = Integer.parseInt(methodElement.getAttribute("id"));
        m_plan = new MethodPlan(methodElement);
    }
    
    /*
    public int getId()
    {
        return m_id;
    }
    */
    
    public MethodPlan getPlan()
    {
        return m_plan;
    }   
}