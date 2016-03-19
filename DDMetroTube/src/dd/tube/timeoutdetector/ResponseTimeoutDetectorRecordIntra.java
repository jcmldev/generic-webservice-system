
package dd.tube.timeoutdetector;


public class ResponseTimeoutDetectorRecordIntra extends ResponseTimeoutDetectorRecord
{
    public String                                   m_source;
    public String                                   m_middleService;
    public String                                   m_target; 
            
    
    public ResponseTimeoutDetectorRecordIntra(
            String source,
            String middleService,
            String target, 
            String requestMessageId)
    {
        super(requestMessageId);
        
        m_source = source;
        m_middleService = middleService;
        m_target = target;
    }
    
    public String getSource()
    {
        return m_source;
    }
    
    public String getMiddleService()
    {
        return m_middleService;
    }
    
    public String getTarget()
    {
        return m_target;
    }
}
