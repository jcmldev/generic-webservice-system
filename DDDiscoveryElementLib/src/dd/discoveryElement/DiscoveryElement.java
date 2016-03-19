
package dd.discoveryElement;

import dd.graph.DependenceGraph;
import dd.output.LoggingContext;


public class DiscoveryElement {
    
    private LoggingContext                  m_log;
    private DependenceDataProvider          m_dependenceDataProvider;
    
    
    public DiscoveryElement(LoggingContext log, DependenceDataProvider dependenceDataProvider)
    {
        m_log = log;
        m_dependenceDataProvider = dependenceDataProvider;
    }
    
    public DependenceGraph discoverInterDependenciesForService(
            int conversationId, 
            String rootServiceId, 
            String[] rootServiceOutDependencies,
            long fromTimestamp, 
            long toTimestamp)
    {
        DependenceGraphBuilder dgBuilder = new DependenceGraphBuilderBreadthFirst(
                m_log, 
                m_dependenceDataProvider);
        
        
        return dgBuilder.discoverInterDependencies(
                conversationId,
                rootServiceId, 
                rootServiceOutDependencies, 
                fromTimestamp, 
                toTimestamp);
    }
}