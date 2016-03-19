package dd.dkl;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.Wsdl;
import dd.bck.Backup;
import dd.bck.Backup_Service;
import dd.output.LoggingContext;
import java.net.URL;
import javax.xml.ws.BindingProvider;

public class DistributedKeyLookupServiceWrapper 
{
    private LoggingContext                  m_log;
    private String                          m_nodeIp;
    
    
    public DistributedKeyLookupServiceWrapper (LoggingContext log, String nodeIp)
    {
        m_log = log;
        m_nodeIp = nodeIp;
    }

    public Backup getPort()
    {
        URL                                   wsdlUrl = Wsdl.getWsdlFilePathURLInGfDir(m_log, "Backup");
        Backup_Service               service = new Backup_Service(wsdlUrl);
        Backup                       port = service.getBackupPort();
        String                                serviceUrl;
        
        
        serviceUrl = Network.serviceUrlSetIpInUrl(
                ExperimentConfiguration.getDistributedKeyLookupServiceAddress(m_log), 
                m_nodeIp);
        
        Network.setServiceUrl(
                (BindingProvider)port, 
                serviceUrl,
                ExperimentConfiguration.getDistributedKeyLookupQueryTimeout(m_log));
        
        return port;
    }
}
