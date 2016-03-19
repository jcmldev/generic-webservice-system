
package dd.dependenceset;

import dd.graph.DependenceGraph;
import dd.output.LoggingContext;


public class DependencySetDependency extends DependencySetElement
{
    public static final String                      STRING_PREFIX = "D-";
    private String                                  m_dependent;
    private String                                  m_antecedent;
    
    
    public DependencySetDependency(
            String dependent, 
            String antecedent,
            DependenceGraph.FlStatus flStatusEX,
            DependenceGraph.FlStatus flStatusTO,
            long lastEXTimestamp,
            long firstTOTimestamp)
    {
        super(flStatusEX, flStatusTO, lastEXTimestamp, firstTOTimestamp);
        m_dependent = dependent;
        m_antecedent = antecedent;
    }
    
    @Override
    public String getService()
    {
        return m_dependent;
    }
    
    public String getDependent()
    {
        return m_dependent;
    }
    
    public String getAntecedent()
    {
        return m_antecedent;
    }

    @Override
    public String toString()
    {
        return 
                STRING_PREFIX +
                DependencySet.STRING_ATTRIBUTES_DELIMITER +
                m_dependent + 
                DependencySet.STRING_ATTRIBUTES_DELIMITER + 
                m_antecedent + 
                DependencySet.STRING_ATTRIBUTES_DELIMITER +
                m_flStatusEX.name() +
                DependencySet.STRING_ATTRIBUTES_DELIMITER +
                m_flStatusTO.name() + 
                DependencySet.STRING_ATTRIBUTES_DELIMITER +
                m_lastEXTimestamp + 
                DependencySet.STRING_ATTRIBUTES_DELIMITER +
                m_firstTOTimestamp;

    }
    
    public static DependencySetDependency fromString(String string)
    {
        String[]            attrs = string.split(DependencySet.STRING_ATTRIBUTES_DELIMITER);
        
        
        return new DependencySetDependency(
                attrs[1], 
                attrs[2], 
                DependenceGraph.FlStatus.valueOf(attrs[3]),
                DependenceGraph.FlStatus.valueOf(attrs[4]),
                Long.valueOf(attrs[5]),
                Long.valueOf(attrs[6]));
    }
    
    @Override
    public void writeOut(LoggingContext log)
    {
        log.log("   Dependency: " + toString());
    }
}
