
package dd.gpdklmanager.dkl;

import dd.DistributedKeyLookup;
import dd.Network;
import dd.gpdklmanager.BackupOnLocalHost;
import dd.gpdklmanager.BackupOnRemoteHost;
import dd.output.LoggingContext;
import java.util.List;
import java.util.TreeMap;

public class DklManager 
{
    private LoggingContext                          m_log;
    private TreeMap<String, DklTarget >             m_dependentIps = new TreeMap<>();
    private BackupOnLocalHost              m_localGpAndDkl;
            

    public DklManager(LoggingContext log)
    {
        m_log = log;
        m_localGpAndDkl = new BackupOnLocalHost(log);
    }
    
    public void updateDklNodes()
    {
        m_log.log("Distributed key lookup (DKL) - node update started ...");

        loadNewDependentIps();
        
        m_log.log("DKL - list of targets ...");

        for (DklTarget dklTarget : m_dependentIps.values())
        {
          m_log.log(
                  "   dependent ip: " + 
                  dklTarget.getDependentIp() + 
                  " antecedent service: " + 
                  dklTarget.getAntecedentService());
        }   

        for (DklTarget dklTarget : m_dependentIps.values())
        {
          updateDklNode(dklTarget);
        }   
    }
    
    private void updateDklNode(DklTarget dklTarget)
    {
        BackupOnRemoteHost           gpsRemote;
        
        
        if (dklTarget.getLastSuccessfulSynchronizationTimestamp() == 0)
        {
            gpsRemote = new BackupOnRemoteHost(m_log, dklTarget);
            gpsRemote.DKL_storeIpDependence(dklTarget.getDependentIp(), dklTarget.getAntecedentService());
        }
    }
    
    private void loadNewDependentIps()
    {
        List<String>            dependentIps = m_localGpAndDkl.GP_getDependentHosts();
        List<String>            antecedentServices;
        String[]                backupHosts;
        DklTarget               dklTarget;
        
        
        m_log.log("DKL - number of dependent hosts: " + dependentIps.size());
        
        m_log.log("DKL - number of available dkl sevice nodes: " + 
                DistributedKeyLookup.getNodesWithDklService(m_log).length);

        for (String ip : dependentIps)
        {
            if (!isIpLocal(ip))
            {
                backupHosts = DistributedKeyLookup.getNodesToStoreIpDependencies(m_log, ip);
                
                for (String host : backupHosts)
                {
                    antecedentServices = m_localGpAndDkl.DKL_getAntecedentServicesOfDependentIp(ip);

                    for (String antecedentService : antecedentServices)
                    {
                        dklTarget = new DklTarget(m_log, host, ip, antecedentService);

                        if (!this.m_dependentIps.containsKey(dklTarget.getId()))
                        {
                            m_dependentIps.put(dklTarget.getId(), dklTarget);

                            m_log.log(
                                "DKL - Added new depedent ip: " + 
                                ip + 
                                " - dependent on service: " +
                                antecedentService +
                                " - with lookup node: " + 
                                host);
                        }
                    }            
                }
            }
        }
    }

    private boolean isIpLocal(String ip)
    {
        if ("127.0.0.1".equals(ip)) return true;
        if (Network.getLocalHostIp(m_log).equals(ip)) return true;
        return false;
    }
}
