
package dd.graph;

import dd.output.LoggingContext;

public class Edge extends DependenceGraphElement
{
        
    private Node          m_source;
    private Node          m_target;
    
    
    public Edge(Node source, Node target)
    {
        m_source = source;
        m_target = target;
    }
    
    public String getId()
    {
        return m_source.getId() + m_target.getId();
    }
    
    public Node getSource()
    {
        return m_source;
    }
    
    public Node getTarget()
    {
        return m_target;
    }
    
    public void upgradeStatuses(Edge edge)
    {
        if (edge == null) return;
        
        super.upgradeStatuses(edge);
    }
    
    public void writeOut(LoggingContext log)
    {
        log.log(
                "        Edge{target=" + 
                m_target.getId() + 
                ", ex=" + 
                m_flStatusEX.name() + 
                ", to=" + 
                m_flStatusTO.name() + 
                ", lastEX=" +
                m_lastEXTimestamp +
                ", firstTO=" +
                m_firstTOTimestamp +
                "}");
        
    }
}
