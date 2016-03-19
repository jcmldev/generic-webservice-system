
package dd.monitor.dependencestorage;

import dd.output.LoggingContext;

public abstract class DependenceContainer 
{
    protected long              m_containerStart;
    //protected long              m_containerPeriod;
    protected long              m_containerEnd;
    
 
    public DependenceContainer (long containerStart, long containerPeriod)
    {
        m_containerStart = containerStart;
        //m_containerPeriod = containerPeriod;
        m_containerEnd = m_containerStart + containerPeriod - 1;
    }
        
    public long getStart()
    {
        return m_containerStart;
    }
    
    public long getEnd()
    {
        return m_containerEnd;
    }
    
    public abstract void addOccurrence(long timestamp);
    
    public abstract boolean hasOccuredWithinTimewindow(long fromTimestamp, long toTimestamp);
    
    public abstract void writeOut(LoggingContext log);
}
