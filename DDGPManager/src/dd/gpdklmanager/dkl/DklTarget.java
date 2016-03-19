
package dd.gpdklmanager.dkl;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.gpdklmanager.SynchronizationTarget;
import dd.output.LoggingContext;

public class DklTarget extends SynchronizationTarget
{
    private String              m_dependentIp;
    private String              m_antecedentService;
    
    
    public DklTarget(
            LoggingContext log, 
            String hostIp, 
            String dependentIp, 
            String antecedentService)
    {
        super(log, hostIp);
        m_dependentIp = dependentIp;
        m_antecedentService = antecedentService;
    }
    
    public String getDependentIp()
    {
        return m_dependentIp;
    }
    
    public String getAntecedentService()
    {
        return m_antecedentService;
    }
    
    public String getId()
    {
        return getDependentIp() + getAntecedentService();
    }

    @Override
    public String getServiceAddress() 
    {
        return Network.serviceUrlSetIpInUrl(ExperimentConfiguration.getDistributedKeyLookupServiceAddress(m_log), m_hostIp);
    }

    @Override
    public int getServiceTimeout() 
    {
        return ExperimentConfiguration.getDistributedKeyLookupBackupUpdateTimeout(m_log);
    }
}
