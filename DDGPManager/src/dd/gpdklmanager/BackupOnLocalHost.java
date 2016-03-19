
package dd.gpdklmanager;

import dd.Network;
import dd.Wsdl;
import dd.bck.Backup;
import dd.bck.Backup_Service;
import dd.gpdklmanager.gp.GpTarget;
import dd.output.LoggingContext;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.xml.ws.BindingProvider;


public class BackupOnLocalHost 
{
    private LoggingContext                              m_log;
    private Backup                             m_port;
    private static Backup_Service              m_service;
    
    
    public BackupOnLocalHost(LoggingContext log)
    {
        m_log = log;
        m_port = getPort();
    }
    
    private Backup_Service getService()
    {
        URL                                   wsdlUrl;
            
        
        if (m_service == null)
        {
            wsdlUrl = Wsdl.getWsdlFilePathURLInGfDir(m_log, "Backup");
            m_service = new Backup_Service(wsdlUrl);
        }

        return m_service;
    }
    
    private Backup getPort()
    {
        Backup                   port = getService().getBackupPort();
        String                            serviceUrl;
        GpTarget                          localHostTarget = new GpTarget(m_log, "127.0.0.1");
        
        
        serviceUrl = localHostTarget.getServiceAddress();

        Network.setServiceUrl(
                (BindingProvider) port, 
                serviceUrl, 
                localHostTarget.getServiceTimeout());
        
        return port;
    }
    
    public String[] GP_getBackupData(
            long fromTimestamp, 
            long toTimestamp, 
            int timeSlotSize) 
    {
        List<String>                            backupData = m_port.gpGetBackupData(fromTimestamp, toTimestamp, timeSlotSize);

        
        return backupData.toArray(new String[0]);
    }

    public void GP_addTargetOfNode(String nodeId, String targetId)
    {
        m_port.gpAddTargetOfNode(nodeId, targetId);
    }
    
    public List<String> GP_getMetadata()
    {
        return m_port.gpGetMetadata();
    }
     
    public List<String> GP_getDependentHosts()
    {
        return m_port.gpGetDependentHosts();
    }
     
    public List<String> DKL_getAntecedentServicesOfDependentIp(String dependentIp)
    {
        return m_port.dklGetAntecedentServicesOfDependentIp(dependentIp);
    }
    
    public String[] GP_Multihop_getBackupData(
            String[] lastSuccessfulyTransferedBackupData) 
    {
        List<String>                   lastBackup = null;      
        List<String>                   backupData;

        
        if (lastSuccessfulyTransferedBackupData != null)
        {
            lastBackup = Arrays.asList(lastSuccessfulyTransferedBackupData);
        }
        
        backupData = m_port.gpMultihopGetBackupData(lastBackup);
        
        return backupData.toArray(new String[0]);
    }

}