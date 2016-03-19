
package dd.wsatube;

import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.ServerTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.TubelineAssemblyContext;
import dd.Timestamp;
import dd.output.LoggingContext;
import dd.output.WsaSymptomGtTrace;
import dd.tube.timeoutdetector.ResponseTimeoutDetector;
import fl.messagemonitor.SymptomMonitor;


public class WsaSymptomMonitorTube extends WsaMonitorTube 
{
    private LoggingContext                     m_log;

    
    public WsaSymptomMonitorTube(Tube tube, ProcessingFlowDirection flowDirection, TubelineAssemblyContext context) 
    {
        super(tube, flowDirection, context);
        m_log = LoggingContext.getContext(this, "");
    }

    private WsaSymptomMonitorTube(WsaMonitorTube original, TubeCloner cloner, ProcessingFlowDirection flowDirection, TubelineAssemblyContext context) 
    {
        super(original, cloner, flowDirection, context);
        m_log = LoggingContext.getContext(this, "");
    }
    
    @Override
    public AbstractTubeImpl copy(TubeCloner tc) 
    {
        return new WsaSymptomMonitorTube(this, tc, m_flowDirection, m_context);
    }
    
    
    
    @Override
    protected void clientProcessRequest(ClientTubelineAssemblyContext context, WsaMessageFieldReader wsaFieldReader) 
    {
        if (ConfigRecordInterOutbound())
        {
            ResponseTimeoutDetector.getInstance().interRequestObserved(
                    SymptomMonitor.ProcessingFlowDirection.Outbound, 
                    wsaFieldReader.getFrom(), 
                    wsaFieldReader.getTo(), 
                    wsaFieldReader.getMessageId(),
                    wsaFieldReader.getConversationId());
        }
        
        if (ConfigRecordIntra())
        {
            String conversationSource = IntraDependenceConversationMonitor.getIntraDependenceSource(
                wsaFieldReader);

            ResponseTimeoutDetector.getInstance().intraRequestObserved(
                    conversationSource,
                    wsaFieldReader.getFrom(),
                    wsaFieldReader.getTo(),
                    wsaFieldReader.getMessageId());
        }
    }

    @Override
    protected void serverProcessRequest(ServerTubelineAssemblyContext context, WsaMessageFieldReader wsaFieldReader) 
    {        
        if (ConfigRecordInterInbound())
        {
            ResponseTimeoutDetector.getInstance().interRequestObserved(
                    SymptomMonitor.ProcessingFlowDirection.Inbound, 
                    wsaFieldReader.getFrom(), 
                    wsaFieldReader.getTo(), 
                    wsaFieldReader.getMessageId(),
                    wsaFieldReader.getConversationId());
        }
        
        // duplicated from dependence tube, but is has to be here to ensure that we have the conversation id recorded
        if (ConfigRecordIntra())
        {
            IntraDependenceConversationMonitor.recordIntraDependenceSource(
                    wsaFieldReader);
        }
    }

    @Override
    protected void clientProcessResponse(
            ClientTubelineAssemblyContext context, 
            WsaMessageFieldReader wsaFieldReader,
            WsaMessageFieldReader contextRequestMessage) 
    {
        if (wsaFieldReader.isMessageTransitiveFault())
        {
            WsaSymptomGtTrace.recordFault(
                    m_log, 
                    SymptomMonitor.ProcessingFlowDirection.Outbound, 
                    contextRequestMessage.getConversationId(), 
                    contextRequestMessage.getFrom(), 
                    contextRequestMessage.getTo(), 
                    SymptomMonitor.SymptomType.EX, 
                    Timestamp.now());
            
            if (ConfigRecordInterOutbound())
            {

                SymptomMonitor.getInstance().recordInterDependenceFault(
                    SymptomMonitor.ProcessingFlowDirection.Outbound, 
                    contextRequestMessage.getFrom(), 
                    contextRequestMessage.getTo(), 
                    SymptomMonitor.SymptomType.EX);        
            }

            if (ConfigRecordIntra())
            {
                String conversationSource = IntraDependenceConversationMonitor.getIntraDependenceSource(
                        contextRequestMessage);

                SymptomMonitor.getInstance().recordIntraDependenceFault(
                        conversationSource, 
                        contextRequestMessage.getFrom(), 
                        contextRequestMessage.getTo(), 
                        SymptomMonitor.SymptomType.EX);
            }
        }
        
        ResponseTimeoutDetector.getInstance().responseObserved(wsaFieldReader.getRelatesToId());
    }

    @Override
    protected void serverProcessResponse(
            ServerTubelineAssemblyContext context, 
            WsaMessageFieldReader wsaFieldReader, 
            WsaMessageFieldReader contextRequestMessage) 
    {
        if (wsaFieldReader.isMessageTransitiveFault())
        {
            WsaSymptomGtTrace.recordFault(
                    m_log, 
                    SymptomMonitor.ProcessingFlowDirection.Inbound, 
                    contextRequestMessage.getConversationId(), 
                    contextRequestMessage.getFrom(), 
                    contextRequestMessage.getTo(), 
                    SymptomMonitor.SymptomType.EX, 
                    Timestamp.now());

            if (ConfigRecordInterInbound())
            {
                SymptomMonitor.getInstance().recordInterDependenceFault(
                    SymptomMonitor.ProcessingFlowDirection.Inbound, 
                    contextRequestMessage.getFrom(), 
                    contextRequestMessage.getTo(), 
                    SymptomMonitor.SymptomType.EX);        
            }        
        }
        
        ResponseTimeoutDetector.getInstance().responseObserved(wsaFieldReader.getRelatesToId());
    }

    @Override
    protected void clientProcessException(
            ClientTubelineAssemblyContext context, 
            WsaMessageFieldReader exceptionContextMessage, 
            Throwable throwable) 
    {
        WsaSymptomGtTrace.recordFault(
            m_log, 
            SymptomMonitor.ProcessingFlowDirection.Outbound, 
            exceptionContextMessage.getConversationId(), 
            exceptionContextMessage.getFrom(), 
            exceptionContextMessage.getTo(), 
            SymptomMonitor.SymptomType.SENDF, 
            Timestamp.now());

        if (ConfigRecordInterOutbound())
        {
            SymptomMonitor.getInstance().recordInterDependenceFault(
                    SymptomMonitor.ProcessingFlowDirection.Outbound, 
                    exceptionContextMessage.getFrom(), 
                    exceptionContextMessage.getTo(), 
                    SymptomMonitor.SymptomType.SENDF);        
        }
        
        if (ConfigRecordIntra())
        {
            String conversationSource = IntraDependenceConversationMonitor.getIntraDependenceSource(
                    exceptionContextMessage);

            SymptomMonitor.getInstance().recordIntraDependenceFault(
                    conversationSource, 
                    exceptionContextMessage.getFrom(), 
                    exceptionContextMessage.getTo(), 
                    SymptomMonitor.SymptomType.SENDF);
        }

        ResponseTimeoutDetector.getInstance().responseObserved(exceptionContextMessage.getMessageId());
    }

    @Override
    protected void serverProcessException(
            ServerTubelineAssemblyContext context, 
            WsaMessageFieldReader exceptionContextMessage, 
            Throwable throwable) 
    {
        WsaSymptomGtTrace.recordFault(
            m_log, 
            SymptomMonitor.ProcessingFlowDirection.Inbound, 
            exceptionContextMessage.getConversationId(), 
            exceptionContextMessage.getFrom(), 
            exceptionContextMessage.getTo(), 
            SymptomMonitor.SymptomType.SENDF, 
            Timestamp.now());

        if (ConfigRecordInterInbound())
        {
            SymptomMonitor.getInstance().recordInterDependenceFault(
                    SymptomMonitor.ProcessingFlowDirection.Inbound, 
                    exceptionContextMessage.getFrom(), 
                    exceptionContextMessage.getTo(), 
                    SymptomMonitor.SymptomType.SENDF);
        }
        
        ResponseTimeoutDetector.getInstance().responseObserved(exceptionContextMessage.getMessageId());
    }    
    
    private boolean ConfigRecordInterOutbound()
    {
        return true;
    }

    private boolean ConfigRecordInterInbound()
    {
        return true;
    }
        
    private boolean ConfigRecordIntra()
    {
        return true;
    }
}
