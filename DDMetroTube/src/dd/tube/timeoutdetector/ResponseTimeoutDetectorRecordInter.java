
package dd.tube.timeoutdetector;

import fl.messagemonitor.SymptomMonitor;


public class ResponseTimeoutDetectorRecordInter extends ResponseTimeoutDetectorRecord
{
    public SymptomMonitor.ProcessingFlowDirection   m_flowDirection; 
    public String                                   m_source;
    public String                                   m_target; 
    public String                                   m_conversationId; 
            
    
    public ResponseTimeoutDetectorRecordInter(
            SymptomMonitor.ProcessingFlowDirection flowDirection, 
            String source, 
            String target, 
            String requestMessageId,
            String conversationId)
    {
        super(requestMessageId);
        
        m_flowDirection = flowDirection;
        m_source = source;
        m_target = target;
        m_conversationId = conversationId;
    }
    
    public SymptomMonitor.ProcessingFlowDirection getFlowDirection()
    {
        return m_flowDirection;
    }
    
    public String getSource()
    {
        return m_source;
    }
    
    public String getTarget()
    {
        return m_target;
    }
    
    public String getConversationId()
    {
        return m_conversationId;
    }
}
