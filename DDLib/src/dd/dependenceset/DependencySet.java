
package dd.dependenceset;

import dd.graph.DependenceGraph;
import dd.graph.DependenceGraphMerger;
import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.List;


/*
 * Allows to transfer information about dependencies, discovery and symptoms from node to node
 * Contains following information:
 * - List of dependencies discovered
 * - For each dependence if the antecedent service was reachable
 * - For each service contains symptom status 
 * - For each dependence contains symptom status
 * 
 * In order to contain all this info the set contains two types of records
 * - Dependencies - added by dependent service monitor
 * -- contains information about dependence occurance, if and how the antecedent service was reachable, and symptom status
 * - Services - added by monitor of given service
 * -- contains information about symptom status of the single service
 */



public class DependencySet 
{
    public static final String                          STRING_ATTRIBUTES_DELIMITER = "<=>";
    
    private List<DependencySetElement>                  m_elements = new ArrayList<>();
    
    
    public DependencySet()
    {}

    private DependencySet(List<DependencySetElement> elements)
    {
        m_elements = elements;
    }

    public DependencySetElement[] getElementsOfService(String service)
    {
        List<DependencySetElement>                  elements = new ArrayList<>();
    
        
        for(DependencySetElement element : m_elements)
        {
            if (element.getService().equals(service))
            {
                elements.add(element);
            }
        }
        
        return elements.toArray(new DependencySetElement[0]);
    }
    
    public DependencySetService getServiceElement(String serviceId)
    {
        DependencySetService        service;
        
        
        for(DependencySetElement element : m_elements)
        {
            if (element instanceof DependencySetService)
            {
                service = (DependencySetService)element;
                if (service.getService().equals(serviceId))
                {
                    return service;
                }
            }
        }

        return null;
    }
    
    public DependencySetDependency[] getDependencyElements()
    {
        List<DependencySetDependency>                  elements = new ArrayList<>();
    
        
        for(DependencySetElement element : m_elements)
        {
            if (element instanceof DependencySetDependency)
            {            
                elements.add((DependencySetDependency)element);
            }
        }
        
        return elements.toArray(new DependencySetDependency[0]);    
    }
    
    public DependencySetElement[] getAllElements()
    {
        return m_elements.toArray(new DependencySetElement[0]);
    }

    public int size()
    {
        return m_elements.size();
    }
    
    private DependencySetService getService(String serviceId)
    {
        DependencySetService     service;
        
        
        for(DependencySetElement element : m_elements)
        {
            if (element instanceof DependencySetService)
            {
                service = (DependencySetService)element;
                
                if (service.getService().equals(serviceId))
                { 
                    return service;
                }
            }
        }
    
        return null;
    }
    
    private DependencySetDependency getDependency(
            String dependent, 
            String antecedent)
    {
        DependencySetDependency     dependency;

        
        for(DependencySetElement element : m_elements)
        {
            if (element instanceof DependencySetDependency)
            {
                dependency = (DependencySetDependency)element;
            
                if ((dependency.getDependent().equals(dependent)) && 
                        (dependency.getAntecedent().equals(antecedent)))
                {
                    return dependency;
                }
            }
        }    
        
        return null;
    }
    
    private void addElementInformation(
            DependencySetElement element,
            DependenceGraph.FlStatus flStatusEX,
            DependenceGraph.FlStatus flStatusTO,
            long lastEXTimestamp,
            long firstTOTimestamp)
    {
        element.setFlStatusEX(
                DependenceGraphMerger.joinFlStatus(
                    element.getFlStatusEX(), 
                            flStatusEX));

        element.setFlStatusTO(
                DependenceGraphMerger.joinFlStatus(
                    element.getFlStatusTO(), 
                            flStatusTO));

        element.setLastEXTimestamp(
                lastEXTimestamp < element.getLastEXTimestamp() ? 
                element.getLastEXTimestamp() : 
                lastEXTimestamp);

        element.setFirstTOTimestamp(
                firstTOTimestamp < element.getFirstTOTimestamp() ? 
                element.getFirstTOTimestamp() : 
                firstTOTimestamp);
    
    }
    
    public void addServiceInformation(
            String serviceId, 
            DependenceGraph.ReachabilityStatus reachabilityStatus,
            DependenceGraph.FlStatus flStatusEX,
            DependenceGraph.FlStatus flStatusTO,
            long lastEXTimestamp,
            long firstTOTimestamp)
    {
        DependencySetService     service = getService(serviceId);
        
        
        if (service != null)
        {
            addElementInformation(
                    service, 
                    flStatusEX, 
                    flStatusTO, 
                    lastEXTimestamp, 
                    firstTOTimestamp);

            service.setReachabilityStatus(
                    DependenceGraphMerger.joinReachabilityStatus(
                        service.getReachabilityStatus(),
                        reachabilityStatus));
        }
        else
        {
            service = new DependencySetService(
                    serviceId, 
                    reachabilityStatus, 
                    flStatusEX,
                    flStatusTO,
                    lastEXTimestamp,
                    firstTOTimestamp);
            
            m_elements.add(service);
        }
    }
    
    public void addDependencyInformation(
            String dependent, 
            String antecedent,
            DependenceGraph.FlStatus flStatusEX,
            DependenceGraph.FlStatus flStatusTO,
            long lastEXTimestamp,
            long firstTOTimestamp)
    {
        DependencySetDependency     dependency = getDependency(dependent, antecedent);
        
        
        if (dependency != null)
        {
            addElementInformation(
                    dependency, 
                    flStatusEX, 
                    flStatusTO, 
                    lastEXTimestamp, 
                    firstTOTimestamp);
        }
        else
        {
            dependency = new DependencySetDependency(
                    dependent, 
                    antecedent, 
                    flStatusEX,
                    flStatusTO,
                    lastEXTimestamp,
                    firstTOTimestamp);
            
            m_elements.add(dependency);
        }
    }
    
    public void addDependenciesOfService(
            String serviceId,
            String[] antecedentServices)
    {
        for(String antecedent : antecedentServices)
        {
            addDependencyInformation(
                    serviceId, 
                    antecedent,
                    DependenceGraph.FlStatus.Unknown,
                    DependenceGraph.FlStatus.Unknown,
                    0,
                    0);
        }
    }
    
    private void addService(DependencySetService service)
    {
        addServiceInformation(
                service.getService(), 
                service.getReachabilityStatus(),
                service.getFlStatusEX(),
                service.getFlStatusTO(),
                service.getLastEXTimestamp(),
                service.getFirstTOTimestamp());
    }

    private void addDependency(DependencySetDependency dependency)
    {
        addDependencyInformation(
                dependency.getDependent(),
                dependency.getAntecedent(),
                dependency.getFlStatusEX(),
                dependency.getFlStatusTO(),
                dependency.getLastEXTimestamp(),
                dependency.getFirstTOTimestamp());
    }

    public void addDependencySet(DependencySet set)
    {
        if (set == null) return;
        
        for(DependencySetElement element : set.m_elements)
        {
            if (element instanceof DependencySetService)
            {
                addService((DependencySetService)element);
            }
            if (element instanceof DependencySetDependency)
            {
                addDependency((DependencySetDependency)element);
            }
        }
    }
    
    public String[] toStringArray()
    {
        ArrayList<String>               strings = new ArrayList<>();
        
        
        for(DependencySetElement element : m_elements)
        {
            strings.add(element.toString());
        }
        
        return strings.toArray(new String[0]);
    }
    
    public static DependencySet fromStringArray(String[] elements)
    {
        List<DependencySetElement>      setElements = new ArrayList<>();
        DependencySetElement            setElement;
        String                          firstAttribute;
        
        
        for(String element : elements)
        {
            // get first attributed to determine what type of element it is
            firstAttribute = element.split(STRING_ATTRIBUTES_DELIMITER)[0];
            
            switch(firstAttribute)
            {
                case DependencySetService.STRING_PREFIX:
                    setElement = DependencySetService.fromString(element);
                    break;

                case DependencySetDependency.STRING_PREFIX:
                    setElement = DependencySetDependency.fromString(element);
                    break;
                    
                default:
                    setElement = null;
            }
            
            setElements.add(setElement);
        }
        
        return new DependencySet(setElements);
    }
    
    public void writeOut(LoggingContext log)
    {
        log.log("Dependency set ...");
        
        for(DependencySetElement element : m_elements)
        {
            element.writeOut(log);
        }
    }
}