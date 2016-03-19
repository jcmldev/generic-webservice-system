
package dd.graph;

import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.List;


public class Node extends DependenceGraphElement
{
  

    private String                                      m_id;
    private List<Edge>                                  m_inEdges = new ArrayList<>();
    private List<Edge>                                  m_outEdges = new ArrayList<>();
    private DependenceGraph.ReachabilityStatus          m_reachabilityStatus;
    
    
    public Node(String id)
    {
        m_id = id;
        m_reachabilityStatus = DependenceGraph.ReachabilityStatus.Unknown;
    }
    
    public String getId()
    {
        return m_id;
    }
    
    public DependenceGraph.ReachabilityStatus getReachabilityStatus()
    {
        return m_reachabilityStatus;
    }
    
    public void setReachabilityStatus(DependenceGraph.ReachabilityStatus reachabilityStatus)
    {
        m_reachabilityStatus = reachabilityStatus;
    }

    public List<Edge> getInEdges()
    {
        return m_inEdges;
    }

    public List<Edge> getOutEdges()
    {
        return m_outEdges;
    }
    
    public void upgradeStatuses(Node node)
    {
        if (node == null) return;
        
        m_reachabilityStatus = DependenceGraphMerger.joinReachabilityStatus(
                m_reachabilityStatus, 
                node.m_reachabilityStatus);
        
        super.upgradeStatuses(node);
    }

    public void writeOut(LoggingContext log)
    {
        log.log(
                "    Node{id=" + 
                m_id + 
                ", reachability=" + 
                m_reachabilityStatus.name() + 
                ", ex=" + 
                m_flStatusEX.name() + 
                ", to=" + 
                m_flStatusTO.name() + 
                ", lastEX=" +
                m_lastEXTimestamp +
                ", firstTO=" +
                m_firstTOTimestamp +
                "}");
        
        for(Edge edge : m_outEdges)
        {
            edge.writeOut(log);
        }
    }
}
