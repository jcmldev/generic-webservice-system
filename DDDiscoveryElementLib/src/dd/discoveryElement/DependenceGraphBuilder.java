
package dd.discoveryElement;

import dd.graph.DependenceGraph;
import dd.output.LoggingContext;


public abstract class DependenceGraphBuilder {
    
    protected LoggingContext                  m_log;
    protected DependenceDataProvider          m_dependenceDataProvider;
    
    
    public DependenceGraphBuilder(
            LoggingContext log,
            DependenceDataProvider dependenceDataProvider)
    {
        m_log = log;
        m_dependenceDataProvider = dependenceDataProvider;
    }
   
    public abstract DependenceGraph discoverInterDependencies(
            int conversationId, 
            String rootServiceId, 
            String[] rootServiceOutDependencies,
            long fromTimestamp, 
            long totTimestamp);
    
}
