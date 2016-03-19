
package dd.graph;

import dd.output.LoggingContext;
import java.util.LinkedHashMap;
import java.util.Map;

public class DependenceGraph 
{
    public enum ReachabilityStatus
    {
        Directly,
        GP,
        GPAntecedent,
        DKL,
        Unreachable,
        Unknown
    }
        
    public enum FlStatus
    {
        Unknown,
        NoFault,
        ElementFault,
        TransitiveFault,
        ElementAndTransitiveFault
    }
    
        
    private Node                    m_rootNode;      
    
    // here is preserved order of insertion of elements - thus allows iteration during graph construction
    private Map<String, Node>       m_nodes = new LinkedHashMap<>();
    private Map<String, Edge>       m_edges = new LinkedHashMap<>();
    
    
    public DependenceGraph(String rootId)
    {
        m_rootNode = new Node(rootId);
        m_nodes.put(rootId, m_rootNode);
    }
    
    public Node getRoot()
    {
        return m_rootNode;
    }
    /*
    public boolean containsEdge(Edge edge)
    {
        return m_edges.containsKey(edge.getId());
    }
*/
    public boolean containsNode(String nodeId)
    {
        return m_nodes.containsKey(nodeId);
    }
    
    public Node getNode(String nodeId)
    {
        return m_nodes.get(nodeId);
    }

    public Edge getEdge(Edge edge)
    {
        return m_edges.get(edge.getId());
    }
  
    public Node addNode(String nodeId)
    {
        return getNodeSafe(nodeId);
    }
        
    public Edge addEdge(String strSource, String strTarget)
    {
        Node        source = getNodeSafe(strSource);
        Node        target = getNodeSafe(strTarget);
        Edge        edge = new Edge(source, target);
        
        
        if (!m_edges.containsKey(edge.getId()))
        {
            m_edges.put(edge.getId(), edge);
            
            source.getOutEdges().add(edge);
            target.getInEdges().add(edge);
        }
        
        return edge;
    }
    
    public Node[] getNodes()
    {
        return m_nodes.values().toArray(new Node[0]);
    } 
    
    public Edge[] getEdges()
    {
        return m_edges.values().toArray(new Edge[0]);
    }
    
    private Node getNodeSafe(String id)
    {
        Node node = m_nodes.get(id);
        
        
        if (node == null)
        {
            node = new Node(id);
            m_nodes.put(id, node);
        }
        
        return node;
    }

    public void writeOut(LoggingContext log)
    {
        log.log(
                "Dependence graph - root: " + 
                m_rootNode.getId() + 
                " - contains unreachable nodes: " + 
                getContainsUnreachableNodes());
        
        for(Node node : m_nodes.values())
        {
            node.writeOut(log);
        }
        
        log.log(
                "Graph size: nodes: " + 
                m_nodes.size() + 
                ", edges: " + 
                m_edges.size());
    }
    
    public boolean getContainsUnreachableNodes()
    {
        for(Node node : m_nodes.values())
        {
            if (node.getReachabilityStatus() == ReachabilityStatus.Unreachable) 
            {
                return true;
            }
        }
        
        return false;
    }
}