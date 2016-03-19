
package dd.dependenceset;

import dd.graph.DependenceGraph;
import dd.output.LoggingContext;

public class DependencySetService extends DependencySetElement
{
    public static final String                      STRING_PREFIX = "S-";
    private String                                  m_service;
    private DependenceGraph.ReachabilityStatus      m_reachabilityStatus;

    
    public DependencySetService(
            String service, 
            DependenceGraph.ReachabilityStatus reachabilityStatus,
            DependenceGraph.FlStatus flStatusEX,
            DependenceGraph.FlStatus flStatusTO,
            long lastEXTimestamp,
            long firstTOTimestamp)
    {
        super(flStatusEX, flStatusTO, lastEXTimestamp, firstTOTimestamp);
        m_service = service;
        m_reachabilityStatus = reachabilityStatus;
    }    
    
    @Override
    public String getService()
    {
        return m_service;
    }

    @Override
    public String toString()
    {
        return 
                STRING_PREFIX +
                DependencySet.STRING_ATTRIBUTES_DELIMITER +
                m_service + 
                DependencySet.STRING_ATTRIBUTES_DELIMITER + 
                m_reachabilityStatus.name() +
                DependencySet.STRING_ATTRIBUTES_DELIMITER +
                m_flStatusEX.name() +
                DependencySet.STRING_ATTRIBUTES_DELIMITER +
                m_flStatusTO.name() + 
                DependencySet.STRING_ATTRIBUTES_DELIMITER +
                m_lastEXTimestamp + 
                DependencySet.STRING_ATTRIBUTES_DELIMITER +
                m_firstTOTimestamp;
    }
        
    public DependenceGraph.ReachabilityStatus getReachabilityStatus()
    {
        return m_reachabilityStatus;
    }

    public void setReachabilityStatus(DependenceGraph.ReachabilityStatus status)
    {
        m_reachabilityStatus = status;
    }
    
    public static DependencySetService fromString(String string)
    {
        String[]            attrs = string.split(DependencySet.STRING_ATTRIBUTES_DELIMITER);
        
        
        return new DependencySetService(
                attrs[1], 
                DependenceGraph.ReachabilityStatus.valueOf(attrs[2]),
                DependenceGraph.FlStatus.valueOf(attrs[3]),
                DependenceGraph.FlStatus.valueOf(attrs[4]),
                Long.valueOf(attrs[5]),
                Long.valueOf(attrs[6]));
    }
    
    @Override
    public void writeOut(LoggingContext log)
    {
        log.log("   Service: " + toString());
    }
}
