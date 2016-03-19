
package dd.wsatube;

import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.ServerTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.TubelineAssemblyContext;
import dd.monitor.ServerMonitor;
import dd.output.LoggingContext;
import dd.output.WsaMessageGtTrace;


public class WsaDependenceMonitorTube extends WsaMonitorTube 
{
    private LoggingContext                     m_log;

    
    public WsaDependenceMonitorTube(Tube tube, ProcessingFlowDirection flowDirection, TubelineAssemblyContext context) 
    {
        super(tube, flowDirection, context);
        m_log = LoggingContext.getContext(this, "");
    }

    private WsaDependenceMonitorTube(WsaMonitorTube original, TubeCloner cloner, ProcessingFlowDirection flowDirection, TubelineAssemblyContext context) 
    {
        super(original, cloner, flowDirection, context);
        m_log = LoggingContext.getContext(this, "");
    }
    
    @Override
    public AbstractTubeImpl copy(TubeCloner tc) 
    {
        return new WsaDependenceMonitorTube(this, tc, m_flowDirection, m_context);
    }
    
    
    
    @Override
    protected void clientProcessRequest(ClientTubelineAssemblyContext context, WsaMessageFieldReader wsaFieldReader) 
    {
        WsaMessageGtTrace.sentRequest(
            m_log, 
            wsaFieldReader.getConversationId(), 
            wsaFieldReader.getFrom(), 
            wsaFieldReader.getTo());

        try
        {                    
            if (ConfigRecordInterOutbound())
            {
                getServerMonitor().addInterDependencyOccurrence(
                                wsaFieldReader.getFrom(), 
                                wsaFieldReader.getTo());
            }                    

            if (ConfigRecordIntra())
            {
                String conversationSource = IntraDependenceConversationMonitor.getIntraDependenceSource(
                        wsaFieldReader);

                getServerMonitor().addIntraDependencyOccurrence(
                        conversationSource, 
                        wsaFieldReader.getFrom(), 
                        wsaFieldReader.getTo());
            }
        }
        catch(Exception ex)
        {
            m_log.log("clientProcessRequest");
            m_log.log(ex);
        }
    }

    @Override
    protected void serverProcessRequest(ServerTubelineAssemblyContext context, WsaMessageFieldReader wsaFieldReader) 
    {        
        WsaMessageGtTrace.receivedRequest(
                m_log, 
                wsaFieldReader.getConversationId(), 
                wsaFieldReader.getFrom(), 
                wsaFieldReader.getTo());

        try
        {
            if (ConfigRecordInterInboundSourceIp())
            {
                getServerMonitor().addInterDependencyOccurrence(
                                WsaMessageFieldReader.getIncomingRequestMessageSourceHostIp(m_log), 
                                wsaFieldReader.getTo());
            }                    

            if (ConfigRecordIntra())
            {
                IntraDependenceConversationMonitor.recordIntraDependenceSource(
                        wsaFieldReader);
            }
        }
        catch(Exception ex)
        {
            m_log.log("serverProcessRequest");
            m_log.log(ex);
        }
    }
    
    @Override
    protected void clientProcessResponse(
            ClientTubelineAssemblyContext context, 
            WsaMessageFieldReader wsaFieldReader,
            WsaMessageFieldReader contextRequestMessage) 
    {
        WsaMessageGtTrace.receivedResponse(
                m_log, 
                contextRequestMessage.getConversationId(), 
                contextRequestMessage.getFrom(), 
                contextRequestMessage.getTo());
    }

    @Override
    protected void serverProcessResponse(
            ServerTubelineAssemblyContext context, 
            WsaMessageFieldReader wsaFieldReader, 
            WsaMessageFieldReader contextRequestMessage) 
    {
        WsaMessageGtTrace.sentResponse(
            m_log, 
            contextRequestMessage.getConversationId(), 
            contextRequestMessage.getFrom(), 
            contextRequestMessage.getTo());
    }

    @Override
    protected void clientProcessException(
            ClientTubelineAssemblyContext context, 
            WsaMessageFieldReader exceptionContextMessage, 
            Throwable throwable) 
    {
        
    }

    @Override
    protected void serverProcessException(
            ServerTubelineAssemblyContext context, 
            WsaMessageFieldReader exceptionContextMessage, 
            Throwable throwable) 
    {
        
    }    
    
    private ServerMonitor getServerMonitor()
    {
        return ServerMonitor.getInstance();
    }
    
    private boolean ConfigRecordInterOutbound()
    {
        return true;
    }
    
    private boolean ConfigRecordIntra()
    {
        return true;
    }
    
    private boolean ConfigRecordInterInboundSourceIp()
    {
        return true;
    }
}
