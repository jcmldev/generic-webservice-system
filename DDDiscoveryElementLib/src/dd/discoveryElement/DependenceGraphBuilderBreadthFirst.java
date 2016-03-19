
package dd.discoveryElement;

import dd.dependenceset.DependencySetDependency;
import dd.dependenceset.DependencySetElement;
import dd.dependenceset.DependencySetService;
import dd.graph.DependenceGraph;
import dd.graph.DependenceGraphMerger;
import dd.graph.Edge;
import dd.graph.Node;
import dd.output.LoggingContext;

public class DependenceGraphBuilderBreadthFirst extends DependenceGraphBuilder {

    private DependenceGraph                 m_dg;
    private int                             m_conversationId; 
    private long                            m_fromTimestamp;
    private long                            m_toTimestamp;
    
    
    public DependenceGraphBuilderBreadthFirst(
            LoggingContext log,
            DependenceDataProvider dependenceDataProvider) 
    {
        super(log, dependenceDataProvider);
    }

    // here we should add the fl status of root?
    @Override
    public DependenceGraph discoverInterDependencies(
            int conversationId, 
            String rootServiceId, 
            String[] rootServiceOutDependencies, 
            long fromTimestamp, 
            long totTimestamp) 
    {      
        m_conversationId = conversationId;
        m_fromTimestamp = fromTimestamp;
        m_toTimestamp = totTimestamp;
        m_dg = new DependenceGraph(rootServiceId);        
        
        addNodeEdges(
                m_dg.getRoot(), 
                rootServiceOutDependencies);
        
        addNodesBreadthFirst();
        
        return m_dg;
    }
    
    private void addNodesBreadthFirst()
    {
        int                         nodeIndex = 1;    // skip root node since its dependencies are added before 
                                                      // - thus we start with first level subnodes
        Node                        node;
        Node[]                      dgNodes;
                                
        
        while (true)
        {
            dgNodes = m_dg.getNodes();
            
            if (dgNodes.length <= nodeIndex) 
            {
                break;
            }
            
            node = m_dg.getNodes()[nodeIndex];    
            addElementsOfNode(node);
            nodeIndex++;
        }
    }
    
    private void addElementsOfNode(Node node)
    {
        DependencySetElement[]      nodeElements = getInterDependenciesForNode(node);
    
        
        for(DependencySetElement element : nodeElements)
        {
            if (element instanceof DependencySetService)
            {
                processElementService((DependencySetService)element);
            }
            if (element instanceof DependencySetDependency)
            {
                processElementDependency((DependencySetDependency)element);
            }
        }
    }
    
    private void processElementService(DependencySetService service)
    {
        DependenceGraph.ReachabilityStatus          reachabilityStatus;
        Node                                        node = m_dg.addNode(service.getService()); 

        
        node.upgradeStatuses(
                service.getFlStatusEX(), 
                service.getFlStatusTO(), 
                service.getLastEXTimestamp(), 
                service.getFirstTOTimestamp());
                
        reachabilityStatus = DependenceGraphMerger.joinReachabilityStatus(
            node.getReachabilityStatus(), 
            service.getReachabilityStatus());

        node.setReachabilityStatus(reachabilityStatus);
    }

    private void processElementDependency(DependencySetDependency dependency)
    {
        Edge                                        edge;
        
        
        edge = m_dg.addEdge(
                dependency.getDependent(), 
                dependency.getAntecedent());
        
        edge.upgradeStatuses(
                dependency.getFlStatusEX(), 
                dependency.getFlStatusTO(), 
                dependency.getLastEXTimestamp(), 
                dependency.getFirstTOTimestamp());
    }

    private void addNodeEdges (Node node, String[] serviceOutDependencies)
    {
        for (String outDependency : serviceOutDependencies)
        {
            m_dg.addEdge(node.getId(), outDependency);
        }
    }
    
    private DependencySetElement[] getInterDependenciesForNode(Node node)
    {
        return m_dependenceDataProvider.getElementsOfService(
                m_conversationId,
                node.getId(), 
                m_fromTimestamp, 
                m_toTimestamp);
    }
}
