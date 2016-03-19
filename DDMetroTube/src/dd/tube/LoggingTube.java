package dd.tube;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.ws.assembler.dev.ServerTubelineAssemblyContext;
import com.sun.xml.ws.message.FaultMessage;
import dd.ExperimentConfiguration;
import dd.output.LoggingContext;




/*Points to explore
 *  - where in the chain of tubes the LogTube is
 *  - if there is transitive EX but not previous EX root - than it is a node ex root 
 * 
 * 
 * 
 * 
 */


/*
 * FL2
 * 1) - in TO (and maybe EX) the symptons included in FPM should be more time limited
 * -- in TO, the inclusion of TO symptoms is following the time flow of symptoms
 * -- that is, TO symptoms can have only antecedent newer TO symptoms
 * -- the reveresed is valid for EX
 * 
 * 
 * 
 */



public final class LoggingTube extends AbstractFilterTubeImpl {
    
    

    private ClientTubelineAssemblyContext       m_clientContext;
    private ServerTubelineAssemblyContext       m_serverContext;
    
    private Packet  m_contextPacket;
    
    private LoggingContext                     m_log;


    public LoggingTube(Tube original, ClientTubelineAssemblyContext clientContext) {
        super(original);
        m_log = LoggingContext.getContext(this, "");
        m_clientContext = clientContext;
        
        m_log.log("new tube");
        m_log.log("client");
        m_log.log("getAddress", clientContext.getAddress().toString());
        m_log.log("getService", clientContext.getService().toString());
        m_log.log("getBinding", clientContext.getBinding().toString());
        m_log.log("getPortInfo", clientContext.getPortInfo().toString());
    }

    public LoggingTube(Tube original, ServerTubelineAssemblyContext serverContext) {
        super(original);
        m_log = LoggingContext.getContext(this, "");
        m_serverContext = serverContext;
        
        m_log.log("new tube");
        m_log.log("server");
        m_log.log("getEndpoint", serverContext.getEndpoint().toString());
        m_log.log("getServiceName", serverContext.getEndpoint().getServiceName().toString());

    }

    private LoggingTube(LoggingTube original, TubeCloner cloner, ClientTubelineAssemblyContext clientContext) {
        super(original, cloner);
        m_log = LoggingContext.getContext(this, "");
        m_clientContext = clientContext;
    }

    private LoggingTube(LoggingTube original, TubeCloner cloner, ServerTubelineAssemblyContext serverContext) {
        super(original, cloner);
        m_log = LoggingContext.getContext(this, "");
        m_serverContext = serverContext;
    }

    @Override
    public LoggingTube copy(TubeCloner cloner) {
        if (m_clientContext != null)
        {
            return new LoggingTube(this, cloner, m_clientContext);
        }        
        else
        {
            return new LoggingTube(this, cloner, m_serverContext);
        }
    }

    @Override
    public NextAction processRequest(Packet request) 
    {
        ServiceIdExtractor      serviceIdExtractor = new ServiceIdExtractor(m_log, request);
        
        
        m_contextPacket = request;
    
        if (isMessageTargetMatchingGWSPattern(serviceIdExtractor))
        {

            m_log.log("processRequest");
            m_log.log(m_clientContext != null ? "client" : "server");


            if (m_clientContext != null) 
            {
                m_log.log("getOutgoingSourceId", serviceIdExtractor.getOutgoingSourceId());
                m_log.log("getOutgoingTargetId", serviceIdExtractor.getOutgoingTargetId());
            }
            else
            {
                m_log.log("getIncomingRequestMessageSourceHostIp", serviceIdExtractor.getIncomingRequestMessageSourceHostIp());
                m_log.log("getIncomingRequestMessageTargetUrl", serviceIdExtractor.getIncomingRequestMessageTargetUrl());
            }
        }

        return super.processRequest(request);
    }
    
        private boolean isMessageTargetMatchingGWSPattern(ServiceIdExtractor serviceIdExtractor)
    {
        final String        pattern =  ExperimentConfiguration.getTubeServiceNamePatternOfOutogingServicesToRecord(m_log);
        String              url;
        
        
        //m_log.log(serviceIdExtractor.getOutogingTargetId() + " matches pattern: " + 
        //        serviceIdExtractor.getOutogingTargetId().matches(pattern));

        if (m_clientContext != null)
        {
            url = serviceIdExtractor.getOutgoingTargetId();
        }
        else
        {
            url = serviceIdExtractor.getIncomingRequestMessageTargetUrl();
        }

        
        return url.matches(pattern);
    }
        
    @Override
    public NextAction processResponse(Packet response) {        
        
        ServiceIdExtractor      serviceIdExtractor = new ServiceIdExtractor(m_log, response);
    
        
        m_contextPacket = response;

        if (isMessageTargetMatchingGWSPattern(serviceIdExtractor))
        {

            m_log.log("processResponse");
            m_log.log(m_clientContext != null ? "client" : "server");

            if (response.getMessage() instanceof FaultMessage)
            {
                m_log.log("EX - transitive");
            }
            

            if (m_clientContext != null) 
            {
                m_log.log("getOutgoingSourceId", serviceIdExtractor.getOutgoingSourceId());
                m_log.log("getOutgoingTargetId", serviceIdExtractor.getOutgoingTargetId());
            }
            else
            {
                m_log.log("getIncomingRequestMessageSourceHostIp", serviceIdExtractor.getIncomingRequestMessageSourceHostIp());
                m_log.log("getIncomingRequestMessageTargetUrl", serviceIdExtractor.getIncomingRequestMessageTargetUrl());
            }
        }
        
        return super.processResponse(response);
    }

    @Override
    public NextAction processException(Throwable throwable) {
        
        m_log.log("processException");
        m_log.log(m_clientContext != null ? "client" : "server");
        m_log.log("exception", throwable.getMessage());        
        m_log.log("class", throwable.getClass().toString());
        m_log.log("context packet is null:", (m_contextPacket == null) ? "true" : "false" );
        
        
        
        
        if (throwable instanceof java.net.SocketTimeoutException)
        {
            m_log.log("TO - ???????");
        }
        else
        {       
            m_log.log("EX - root cause");
        }
        
        ServiceIdExtractor      serviceIdExtractor = new ServiceIdExtractor(m_log, m_contextPacket);
        m_log.log("getOutgoingSourceId", serviceIdExtractor.getOutgoingSourceId());
        m_log.log("getOutgoingTargetId", serviceIdExtractor.getOutgoingTargetId());

        return super.processException(throwable);
    }

    @Override
    public void preDestroy() {
        super.preDestroy();
    }
}