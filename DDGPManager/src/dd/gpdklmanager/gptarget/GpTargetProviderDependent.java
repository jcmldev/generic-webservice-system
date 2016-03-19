
package dd.gpdklmanager.gptarget;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.gpdklmanager.BackupOnLocalHost;
import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.List;


public class GpTargetProviderDependent extends GpTargetProvider
{
    public GpTargetProviderDependent(
                        LoggingContext log, 
                        List<String> nodesWithGossipProtocol, 
                        BackupOnLocalHost localGpAndDkl,
                        int maxMetricInRoutingTable)
    {
        super(log, nodesWithGossipProtocol, localGpAndDkl, maxMetricInRoutingTable);
    }
    
    @Override
    public String[] getTargets() 
    {
        List<String>                    dependentHosts = m_localGpAndDkl.GP_getDependentHosts();
        List<String>                    targets = new ArrayList<>();
        boolean                         useDebugCode = ExperimentConfiguration.getUseSingleMachineDebugCode(m_log);
        
        
        for(String host : dependentHosts)
        {
            if (m_nodesWithGossipProtocol.contains(host) )
            {
                if (!useDebugCode && host.equals(Network.getLocalHostIp(m_log)))
                {
                    break;
                }
                else
                {
                    if (useDebugCode && host.equals(Network.getLocalHostIp(m_log)))
                    {
                        m_log.log("DEBUG! using local ip in GP backup selection");
                    }
                }

                targets.add(host);
            }
        }
        
        return targets.toArray(new String[0]);
    }
}
