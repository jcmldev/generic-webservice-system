
package dd.gpdklmanager.multihopgp;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.gpdklmanager.SynchronizationTarget;
import dd.output.LoggingContext;


public class MultihopGpTarget extends SynchronizationTarget
{
    private String[]           m_lastSuccessfulBackupData;
    
    
    public MultihopGpTarget(LoggingContext log, String host)
    {
        super(log, host);
    }
        
    @Override
    public String getServiceAddress()
    {
        return Network.serviceUrlSetIpInUrl(
                ExperimentConfiguration.getGossipProtocolServiceAddress(m_log), 
                m_hostIp);
    }

    @Override
    public int getServiceTimeout() {
        return ExperimentConfiguration.getGossipProtocolBackupUpdateTimeout(m_log);
    }
    
    public String[] getLastSuccessfulBackupData()
    {
        return m_lastSuccessfulBackupData;
    }
    
    public void setLastSuccessfulBackupData(String[] data)
    {
        m_lastSuccessfulBackupData = data;
    }
}
