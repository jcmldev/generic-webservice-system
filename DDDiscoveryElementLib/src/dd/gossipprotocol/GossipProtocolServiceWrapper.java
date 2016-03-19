package dd.gossipprotocol;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.Wsdl;
import dd.bck.Backup;
import dd.bck.Backup_Service;
import dd.output.LoggingContext;
import java.net.URL;
import java.util.List;
import javax.xml.ws.BindingProvider;

public class GossipProtocolServiceWrapper 
{
    private LoggingContext                  m_log;
    private String                          m_nodeIp;
    private static Backup_Service           m_service;
    
    
    public GossipProtocolServiceWrapper (LoggingContext log, String nodeIp)
    {
        m_log = log;
        m_nodeIp = nodeIp;
    }
    
    
    private Backup_Service getService()
    {
        URL                          wsdlUrl;

        
        if (m_service == null)
        {
            wsdlUrl = Wsdl.getWsdlFilePathURLInGfDir(m_log, "Backup");
            m_service = new Backup_Service(wsdlUrl);
        }
        
        return m_service;
    }

    public Backup getPort()
    {
        Backup                       port = getService().getBackupPort();
        String                       serviceUrl;
        
        
                
        serviceUrl = Network.serviceUrlSetIpInUrl(
                ExperimentConfiguration.getGossipProtocolServiceAddress(m_log), 
                m_nodeIp);
        
        Network.setServiceUrl(
                (BindingProvider)port, 
                serviceUrl,
                ExperimentConfiguration.getGossipProtocolQueryTimeout(m_log));
        
        return port;
    }
    
    public List<String> getBackupTargetsOfNode(String nodeIp)
    {
        return getPort().gpGetBackupTargetsOfNode(nodeIp);
    }
}