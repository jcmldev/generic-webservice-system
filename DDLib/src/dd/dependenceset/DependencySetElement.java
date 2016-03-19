
package dd.dependenceset;

import dd.graph.DependenceGraph;
import dd.output.LoggingContext;


public abstract class DependencySetElement 
{
    protected DependenceGraph.FlStatus                m_flStatusEX;
    protected DependenceGraph.FlStatus                m_flStatusTO;
    protected long                                    m_lastEXTimestamp;
    protected long                                    m_firstTOTimestamp;

    
    public DependencySetElement(
            DependenceGraph.FlStatus flStatusEX,
            DependenceGraph.FlStatus flStatusTO,
            long lastEXTimestamp,
            long firstTOTimestamp)
    {
        m_flStatusEX = flStatusEX;
        m_flStatusTO = flStatusTO;
        m_lastEXTimestamp = lastEXTimestamp;
        m_firstTOTimestamp = firstTOTimestamp;
    }    
    
    public DependenceGraph.FlStatus getFlStatusEX()
    {
        return m_flStatusEX;
    }

    public void setFlStatusEX(DependenceGraph.FlStatus status)
    {
        m_flStatusEX = status;
    }

    public DependenceGraph.FlStatus getFlStatusTO()
    {
        return m_flStatusTO;
    }

    public void setFlStatusTO(DependenceGraph.FlStatus status)
    {
        m_flStatusTO = status;
    }

    public long getLastEXTimestamp()
    {
        return m_lastEXTimestamp;
    }

    public void setLastEXTimestamp(long timestamp)
    {
        m_lastEXTimestamp = timestamp;
    }

    public long getFirstTOTimestamp()
    {
        return m_firstTOTimestamp;
    }

    public void setFirstTOTimestamp(long timestamp)
    {
        m_firstTOTimestamp = timestamp;
    }
    
    public abstract void writeOut(LoggingContext log);
    
    public abstract String getService();
}
