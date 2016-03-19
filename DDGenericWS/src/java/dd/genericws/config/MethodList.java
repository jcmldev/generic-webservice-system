
package dd.genericws.config;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MethodList 
{
    private Method[]            m_methods; 
    
    
    public MethodList(Element rootElement)
    {
        loadMethods(rootElement);
    }
    
    public Method getMethod(int methodId)
    {
        return m_methods[methodId];
    }
    
    private void loadMethods(Element applicationElement)
    {
        NodeList                methodElements = applicationElement.getElementsByTagName("method");
        Element                 methodElement;
        Method                  method;
        ArrayList<Method>       methods = new ArrayList<Method>();
        
        
        for(int i = 0; i<methodElements.getLength(); i++)
        {
            methodElement = (Element) methodElements.item(i);
            method = new Method(methodElement);
            methods.add(method);
        }
        
        m_methods = methods.toArray(new Method[0]);
    }
}