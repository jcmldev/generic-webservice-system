
package dd.wsatube;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.ServerTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.TubelineAssemblyContext;


public abstract class WsaMonitorTube extends AbstractFilterTubeImpl 
{
    
    public enum ProcessingFlowDirection
    {
        Outbound,
        Inbound
    };
    
    protected ProcessingFlowDirection                       m_flowDirection;
    protected TubelineAssemblyContext               m_context;
    private WsaMessageFieldReader                          m_contextRequestMessage;
    

    protected WsaMonitorTube(Tube tube, ProcessingFlowDirection flowDirection, TubelineAssemblyContext context) 
    {
        super(tube);
        m_flowDirection = flowDirection;
        m_context = context;
    }

    protected WsaMonitorTube(WsaMonitorTube original, TubeCloner cloner, ProcessingFlowDirection flowDirection, TubelineAssemblyContext context) 
    {
        super(original, cloner);
        m_flowDirection = flowDirection;
        m_context = context;
    }
    
    protected abstract void clientProcessRequest(ClientTubelineAssemblyContext context, WsaMessageFieldReader wsaFieldReader);
    protected abstract void serverProcessRequest(ServerTubelineAssemblyContext context, WsaMessageFieldReader wsaFieldReader);
    protected abstract void clientProcessResponse(ClientTubelineAssemblyContext context, WsaMessageFieldReader wsaFieldReader, WsaMessageFieldReader contextRequestMessage);
    protected abstract void serverProcessResponse(ServerTubelineAssemblyContext context, WsaMessageFieldReader wsaFieldReader, WsaMessageFieldReader contextRequestMessage);
    protected abstract void clientProcessException(ClientTubelineAssemblyContext context, WsaMessageFieldReader exceptionContextMessage, Throwable throwable);
    protected abstract void serverProcessException(ServerTubelineAssemblyContext context, WsaMessageFieldReader exceptionContextMessage, Throwable throwable);

    
    @Override
    public NextAction processRequest(Packet request) 
    {
        WsaMessageFieldReader          wsaFieldReader = new WsaMessageFieldReader(request);
        
        
        m_contextRequestMessage = wsaFieldReader;
        
        if (wsaFieldReader.isMessageWsa())
        {
            if (m_flowDirection == ProcessingFlowDirection.Outbound)
            {
                clientProcessRequest((ClientTubelineAssemblyContext)m_context, wsaFieldReader);
            }
            else
            {
                serverProcessRequest((ServerTubelineAssemblyContext)m_context, wsaFieldReader);            
            }
        }
        
        return super.processRequest(request);
    }

    @Override
    public NextAction processResponse(Packet response) 
    {        
        WsaMessageFieldReader          wsaFieldReader = new WsaMessageFieldReader(response);
        
          
        // leave there the request - fault response does not contain enought information
        //m_exceptionContextMessage = wsaFieldReader;
        
        if (wsaFieldReader.isMessageWsa())
        {
            if (m_flowDirection == ProcessingFlowDirection.Outbound)
            {
                clientProcessResponse(
                        (ClientTubelineAssemblyContext)m_context, 
                        wsaFieldReader, 
                        m_contextRequestMessage);
            }
            else
            {
                serverProcessResponse(
                        (ServerTubelineAssemblyContext)m_context, 
                        wsaFieldReader, 
                        m_contextRequestMessage);            
            }
        }
        
        return super.processResponse(response);
    }

    @Override
    public NextAction processException(Throwable throwable) 
    {    
        if (m_contextRequestMessage.isMessageWsa())
        {
            if (m_flowDirection == ProcessingFlowDirection.Outbound)
            {
                clientProcessException(
                        (ClientTubelineAssemblyContext)m_context, 
                        m_contextRequestMessage, 
                        throwable);
            }
            else
            {
                serverProcessException(
                        (ServerTubelineAssemblyContext)m_context, 
                        m_contextRequestMessage, 
                        throwable);            
            }
        }
        
        return super.processException(throwable);
    }

    @Override
    public void preDestroy() {
        super.preDestroy();
    }
}
