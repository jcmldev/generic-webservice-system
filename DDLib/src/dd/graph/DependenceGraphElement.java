
package dd.graph;


public abstract class DependenceGraphElement 
{

    protected DependenceGraph.FlStatus                    m_flStatusEX;
    protected DependenceGraph.FlStatus                    m_flStatusTO;
    protected long                                        m_lastEXTimestamp;
    protected long                                        m_firstTOTimestamp;
    
    
    public DependenceGraphElement()
    {
        m_flStatusEX = DependenceGraph.FlStatus.Unknown;
        m_flStatusTO = DependenceGraph.FlStatus.Unknown;
        m_lastEXTimestamp = 0;
        m_firstTOTimestamp = 0;
    }

    public DependenceGraph.FlStatus getFlStatusEX()
    {
        return m_flStatusEX;
    }
    
    public void setFlStatusEX(DependenceGraph.FlStatus flStatus)
    {
        m_flStatusEX = flStatus;
    }

    public DependenceGraph.FlStatus getFlStatusTO()
    {
        return m_flStatusTO;
    }
    
    public void setFlStatusTO(DependenceGraph.FlStatus flStatus)
    {
        m_flStatusTO = flStatus;
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
    
    protected void upgradeStatuses(DependenceGraphElement element)
    {
        upgradeStatuses(
                element.m_flStatusEX, 
                element.m_flStatusTO, 
                element.m_lastEXTimestamp, 
                element.m_firstTOTimestamp);
    }
    
    public void upgradeStatuses(
            DependenceGraph.FlStatus flStatusEX,
            DependenceGraph.FlStatus flStatusTO,
            long lastEXTimestamp,
            long firstTOTimestamp)
    {
        m_flStatusEX = DependenceGraphMerger.joinFlStatus(this.m_flStatusEX, flStatusEX);
        m_flStatusTO = DependenceGraphMerger.joinFlStatus(this.m_flStatusTO, flStatusTO);
        m_lastEXTimestamp = m_lastEXTimestamp < lastEXTimestamp ? lastEXTimestamp : m_lastEXTimestamp;
        m_firstTOTimestamp = m_firstTOTimestamp == 0 ? firstTOTimestamp : m_firstTOTimestamp;
        m_firstTOTimestamp = m_firstTOTimestamp > firstTOTimestamp ? firstTOTimestamp : m_firstTOTimestamp;
    }
    
    
}
