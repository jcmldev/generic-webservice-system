package dd.tube;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import dd.ExperimentConfiguration;
import dd.monitor.ServerMonitor;
import dd.output.LoggingContext;


public final class MonitorTube extends AbstractFilterTubeImpl {
    
    public enum RequestMessageDirection
    {
        Incoming,
        Outgoing
    };
    
    private LoggingContext                     m_log;
    private RequestMessageDirection            m_messageDirection;

    
    private MonitorTube(MonitorTube original, TubeCloner cloner, RequestMessageDirection messageDirection) {
        super(original, cloner);
        m_log = LoggingContext.getContext(this, "");
        m_messageDirection = messageDirection;
    }

    @Override
    public MonitorTube copy(TubeCloner cloner) {
        return new MonitorTube(this, cloner, m_messageDirection);
    }

    MonitorTube(Tube tube, RequestMessageDirection messageDirection) {
        super(tube);
        m_log = LoggingContext.getContext(this, "");
        m_messageDirection = messageDirection;
    }

    @Override
    public NextAction processRequest(Packet request) 
    {
        ServiceIdExtractor      serviceIdExtractor = new ServiceIdExtractor(m_log, request);
        
              
        recordDependency(serviceIdExtractor);
        recordHostDependence(serviceIdExtractor);
        //recordDklIpDependency(serviceIdExtractor);
        
        //ServerMonitor.getInstance().writeOut();
        
        return super.processRequest(request);
    }
    
    private void recordDependency(ServiceIdExtractor serviceIdExtractor)
    {
        ServerMonitor           serverMonitor = ServerMonitor.getInstance();
        
        /*
        
        m_log.log("Request: " + this.m_messageDirection);
    
        try
        {
            m_log.log("source: " + serviceIdExtractor.getOutgoingSourceId());
            m_log.log("target: " + serviceIdExtractor.getOutogingTargetId());        
        }
        catch(Exception ex)
        {
            m_log.log(ex);
        }
        */
        // for now, record only server dependencies
        if (!serviceIdExtractor.getIsRunningOnServer())
        {
            return;
        }
            
        if (this.m_messageDirection == RequestMessageDirection.Outgoing)
        {
            if (isMessageTargetMatchingGWSPattern(serviceIdExtractor))
            {
                serverMonitor.addInterDependencyOccurrence(
                        serviceIdExtractor.getOutgoingSourceId(), 
                        serviceIdExtractor.getOutgoingTargetId());
                
                //serverMonitor.writeOut(m_log);
            }
        }
        else
        {
            // incoming dependencies are recorded only if requested in cofig
            if (ExperimentConfiguration.getDependenceDiscoveryUseMultihopGossipProtocolWithAntecedent(m_log))
            {
                if (isMessageTargetMatchingGWSPattern(serviceIdExtractor))
                {
                    serverMonitor.addInterDependencyOccurrence(
                            serviceIdExtractor.getIncomingRequestMessageSourceHostIp(), 
                            serviceIdExtractor.getIncomingRequestMessageTargetUrl());

                    //serverMonitor.writeOut(m_log);
                }
            }
        }
    }
    
    private boolean isMessageTargetMatchingGWSPattern(ServiceIdExtractor serviceIdExtractor)
    {
        final String        pattern =  ExperimentConfiguration.getTubeServiceNamePatternOfOutogingServicesToRecord(m_log);
        String              url;
        
        
        //m_log.log(serviceIdExtractor.getOutogingTargetId() + " matches pattern: " + 
        //        serviceIdExtractor.getOutogingTargetId().matches(pattern));

        if (this.m_messageDirection == RequestMessageDirection.Outgoing)
        {
            url = serviceIdExtractor.getOutgoingTargetId();
        }
        else
        {
            url = serviceIdExtractor.getIncomingRequestMessageTargetUrl();
        }

        
        return url.matches(pattern);
    }
    /*
    private void recordDklIpDependency(ServiceIdExtractor serviceIdExtractor)
    {
        ServerMonitor           serverMonitor = ServerMonitor.getInstance();
        String                  dependentHostIp;
        String                  antecedentService;
        
        
        if (m_messageDirection == RequestMessageDirection.Incoming)
        {
            dependentHostIp = serviceIdExtractor.getIncomingRequestMessageSourceHostIp();
            antecedentService = serviceIdExtractor.getIncomingRequestMessageTargetUrl();
                    
            serverMonitor.getDklDependencies().add(dependentHostIp, antecedentService);
        }
    }
    */
    // the Ip dependencies recorded are based on messages exchanged between 
    // generic service system services only - thus excluding all other services
    private void recordHostDependence(ServiceIdExtractor serviceIdExtractor)
    {
        ServerMonitor           serverMonitor = ServerMonitor.getInstance();
        String                  host;
        
        
        if (isMessageTargetMatchingGWSPattern(serviceIdExtractor))
        {
            switch(m_messageDirection)
            {
                case Incoming:
                    host = serviceIdExtractor.getIncomingRequestMessageSourceHostIp();
                    serverMonitor.getDepedentHosts().add(host);
                    break;

                case Outgoing:
                    host = serviceIdExtractor.getOutgoingRequestMessageTargetHostIp();
                    serverMonitor.getAntecedentHosts().add(host);
                    break;
            }
        }
    }

    
    @Override
    public NextAction processResponse(Packet response) {        
        
        /*
        m_log.log("response: " + this.m_messageDirection);
    
        try
        {

            ServiceIdExtractor      serviceIdExtractor = new ServiceIdExtractor(m_log, response);

                    
            m_log.log("source: " + serviceIdExtractor.getOutgoingSourceId());
            m_log.log("target: " + serviceIdExtractor.getOutogingTargetId());        
        }
        catch(Exception ex)
        {
            m_log.log(ex);
        }

        */
        return super.processResponse(response);
    }

    @Override
    public NextAction processException(Throwable throwable) {
        //m_log.log("exception", throwable.getMessage());
        return super.processException(throwable);
    }

    @Override
    public void preDestroy() {
        super.preDestroy();
    }
}