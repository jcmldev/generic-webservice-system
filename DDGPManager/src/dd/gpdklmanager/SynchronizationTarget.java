
package dd.gpdklmanager;

import dd.output.LoggingContext;


public abstract class SynchronizationTarget 
{
    protected LoggingContext                m_log;
    protected String                        m_hostIp;
    private long                            m_lastSuccessfulSynchronizationTimestamp = 0L;
    
    
    public SynchronizationTarget(LoggingContext log, String hostIp)
    {
        m_log = log;
        m_hostIp = hostIp;
    }
    
    public String getHost()
    {
        return m_hostIp;
    }
    
    public abstract String getServiceAddress();
    
    public abstract int getServiceTimeout();
    
    public long getLastSuccessfulSynchronizationTimestamp()
    {
        return m_lastSuccessfulSynchronizationTimestamp;
    }
    
    public void setLastSuccessfulSynchronizationTimestamp(long timestamp)
    {
        if (timestamp > m_lastSuccessfulSynchronizationTimestamp)
        {
            m_lastSuccessfulSynchronizationTimestamp = timestamp;
        }
    }
    
    
}
