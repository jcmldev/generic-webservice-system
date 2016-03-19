
package dd.tube.timeoutdetector;

import dd.ExperimentConfiguration;
import dd.output.LoggingContext;


public abstract class ResponseTimeoutDetectorRecord 
{
    private LoggingContext          m_log;
    private long                    m_requestTimestamp;
    private String                  m_requestMessageId;
    
    
    public ResponseTimeoutDetectorRecord(String requestMessageId)
    {
        m_log = LoggingContext.getContext(this);
        m_requestTimestamp = dd.Timestamp.now();
    }
    
    private int getTimeoutLength()
    {
        return ExperimentConfiguration.getServiceSystemMessageExchangeTimeoutSymptomMonitor(m_log);
    }
    
    public boolean hasTimeoutExpired()
    {
        long        currentDuration = dd.Timestamp.now() - m_requestTimestamp;
        
        
        return (currentDuration > getTimeoutLength());
    }
    
    public long getTimeoutTimestamp()
    {
        return m_requestTimestamp + getTimeoutLength();
    }
    
    public String getRequestMessageId()
    {
        return m_requestMessageId;
    }
    
}
