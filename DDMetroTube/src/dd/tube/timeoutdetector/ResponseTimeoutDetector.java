
package dd.tube.timeoutdetector;

import dd.Timestamp;
import dd.output.LoggingContext;
import dd.output.WsaSymptomGtTrace;
import fl.messagemonitor.SymptomMonitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ResponseTimeoutDetector 
{
    
    private HashMap<String, ResponseTimeoutDetectorRecord>               m_records = new HashMap<>();
    private LoggingContext                                               m_log;
        
    private static ResponseTimeoutDetector                               m_intance;
    
    
    private ResponseTimeoutDetector()
    {
        m_log = LoggingContext.getContext(this);
    }
    
    public static ResponseTimeoutDetector getInstance()
    {
        if (m_intance == null)
        {
            m_intance = new ResponseTimeoutDetector();
        }
        
        return m_intance;
    }
    
    public synchronized void interRequestObserved(
            SymptomMonitor.ProcessingFlowDirection flowDirection, 
            String source, 
            String target, 
            String requestMessageId,
            String conversationId)
    {
        ResponseTimeoutDetectorRecord record = new ResponseTimeoutDetectorRecordInter(
                flowDirection, 
                source, 
                target, 
                requestMessageId,
                conversationId);
        
        m_records.put(requestMessageId, record);
    }
    
    public synchronized void intraRequestObserved(
            String source, 
            String middleService,
            String target, 
            String requestMessageId)
    {    
        ResponseTimeoutDetectorRecord record = new ResponseTimeoutDetectorRecordIntra(
                source, 
                middleService,
                target, 
                requestMessageId);
        
        m_records.put(requestMessageId, record);
    }
    
    public synchronized void responseObserved(String requestMessageId)
    {
        // first detect timeouts, the response might have arrived too late
        detectTimeouts();
        
        if (m_records.containsKey(requestMessageId))
        {
            m_records.remove(requestMessageId);
        }
    }
    
    public synchronized void detectTimeouts()
    {
        List<ResponseTimeoutDetectorRecord> expiredList = new ArrayList<>();

        
        for(ResponseTimeoutDetectorRecord record : m_records.values())
        {
            if (record.hasTimeoutExpired())
            {
                expiredList.add(record);
            }
        }
        
        for(ResponseTimeoutDetectorRecord record : expiredList)
        {
            reportTimeout(record);
            m_records.remove(record.getRequestMessageId());
        }    
    }
    
    private void reportTimeout(ResponseTimeoutDetectorRecord record)
    {
        if (record instanceof ResponseTimeoutDetectorRecordInter)
        {
            ResponseTimeoutDetectorRecordInter inter = (ResponseTimeoutDetectorRecordInter)record;


            WsaSymptomGtTrace.recordFault(
                m_log, 
                inter.getFlowDirection(), 
                inter.getConversationId(), 
                inter.getSource(),
                inter.getTarget(),
                SymptomMonitor.SymptomType.TO, 
                inter.getTimeoutTimestamp());

            SymptomMonitor.getInstance().recordInterDependenceFault(
                    inter.getFlowDirection(), 
                    inter.getSource(), 
                    inter.getTarget(), 
                    SymptomMonitor.SymptomType.TO,
                    inter.getTimeoutTimestamp());
        }
        else
        {
            ResponseTimeoutDetectorRecordIntra intra = (ResponseTimeoutDetectorRecordIntra)record;
            
            SymptomMonitor.getInstance().recordIntraDependenceFault(
                    intra.getSource(), 
                    intra.getMiddleService(), 
                    intra.getTarget(),
                    SymptomMonitor.SymptomType.TO,
                    intra.getTimeoutTimestamp());        
        }
    }
}
