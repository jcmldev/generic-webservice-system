
package dd.gpdklmanager.gp;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.gpdklmanager.SynchronizationTarget;
import dd.output.LoggingContext;


public class GpTarget extends SynchronizationTarget
{
    public GpTarget(LoggingContext log, String host)
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
    
}
