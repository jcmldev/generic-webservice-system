
package dd.gpdklmanager.multihopgp;

import dd.ExperimentConfiguration;
import dd.Timestamp;
import dd.gpdklmanager.BackupOnLocalHost;
import dd.gpdklmanager.BackupOnRemoteHost;
import dd.gpdklmanager.SynchronizationManager;
import dd.gpdklmanager.gptarget.GpTargetProvider;
import dd.gpdklmanager.gptarget.GpTargetProviderFactory;
import dd.gpdklmanager.gptarget.GpTargetProviderRoutingTable;
import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MultihopGpBackupManager extends SynchronizationManager
{
    private MultihopGpTargetList            m_backupTargetList;
    private MultihopGpTargetList            m_backupTargetsToSynch;
    private GpTargetProvider                m_backupTargetProvider;
    private BackupOnLocalHost               m_localBackup;
    private int                             m_maxNodesToSynch;
    private int                             m_maxMetricInRoutingTable;
            
    
    public MultihopGpBackupManager(LoggingContext log, MultihopGpTargetList targetList, int cycleIndex)
    {
        super(log, cycleIndex);
        m_localBackup = new BackupOnLocalHost(log);
        m_backupTargetList = targetList;
        //m_backupTargetProvider = GpTargetProviderFactory.getTargetProviderDependent(log, m_localBackup);
        m_maxNodesToSynch = ExperimentConfiguration.getGossipProtocolNumberOfBackupNodes(m_log);
        m_maxMetricInRoutingTable = ExperimentConfiguration.getGossipProtocolRoutingTableMaximumMetric(m_log);
        m_backupTargetProvider = GpTargetProviderFactory.getTargetProviderFromConfiguration(log, m_localBackup, m_maxMetricInRoutingTable);
    }

    @Override
    protected void runSynchronization() 
    {
        try
        {
            m_log.log("Multihop GP - synchronization started ...");

            try
            {
                loadAllPossibleTargets();
            }
            catch(Exception ex)
            {
                m_log.log("Multihop GP - Loading targets exception: " + ex.getMessage());
                m_log.log(ex);
            }

            selectTargetsToSynchWith();
            
            for(MultihopGpTarget target : m_backupTargetsToSynch.getTargets())
            {
                runBackupIterationForTarget(target);
            }                
        }
        catch(Exception ex)
        {
            m_log.log("Multihop GP exception: " + ex.getMessage());
            m_log.log(ex);
        }
    }
    
    private void selectTargetsToSynchWith()
    {
        MultihopGpTarget            target;
        int                         i = 0;
        String[]                    targetIps;
        
        
        if (m_maxNodesToSynch == 0)
        {
            m_log.log("Multihop GP - There is no limit on number of targets to synch with");
            m_backupTargetsToSynch = m_backupTargetList;
        }
        else
        {
            m_log.log("Multihop GP - Max number of targets to synch with: " + m_maxNodesToSynch + " - selection is based on hop count");
            m_log.log("Multihop GP - Max metric in routing table:" + m_maxMetricInRoutingTable);
            
            m_backupTargetsToSynch = new MultihopGpTargetList(m_log);

            m_log.log("Multihop GP - List of targets to synchronize with ...");

            if (m_backupTargetProvider instanceof GpTargetProviderRoutingTable)
            {
                targetIps = getTargetsToSynchWithRandomlyAtHop1();
            }
            else
            {
                targetIps = m_backupTargetProvider.getTargets();
            }
            
            for (String targetIp : targetIps)
            {
                i++;
                target = m_backupTargetList.getTarget(targetIp);
                m_backupTargetsToSynch.add(target);
                
                m_log.log(
                    "     " + 
                    i +
                    " - " + 
                    target.getHost() + 
                    " - last successful synchronization: " + 
                    target.getLastSuccessfulSynchronizationTimestamp());
                
                if (i == m_maxNodesToSynch) break;
            }
        }
    }
    
    private String[] getTargetsToSynchWithRandomlyAtHop1()
    {
        GpTargetProviderRoutingTable        provider = (GpTargetProviderRoutingTable)m_backupTargetProvider;
        String[]                            hop1Targets = provider.getTargetsAtDistance(1);
        Random                              r = new Random(Timestamp.now());
        int                                 randomTargetIndex;
        String                              randomTarget;
        List<String>                        targets = new ArrayList<>();
        
        
        // if there is more than upper limit at hop 1, the randomizing makes sence
        if (hop1Targets.length > m_maxNodesToSynch)
        {
            while(targets.size() < hop1Targets.length)
            {
                randomTargetIndex = r.nextInt(hop1Targets.length);
                randomTarget = hop1Targets[randomTargetIndex];
                if (!targets.contains(randomTarget))
                {
                    targets.add(randomTarget);
                }
            }            
            
            return targets.toArray(new String[0]);
        }
        // otherwise just take hop 1 and rest in default order
        else
        {
            return provider.getTargets();
        }
    }
    
    private void runBackupIterationForTarget(MultihopGpTarget target)
    {
        String[]                       backupData;
        BackupOnRemoteHost             remoteGps = new BackupOnRemoteHost(m_log, target);
                
        
        m_log.log("Multihop GP - synchronizing with target: " + target.getHost());
        
        try
        {
            backupData = m_localBackup.GP_Multihop_getBackupData(
                    target.getLastSuccessfulBackupData());
            
            m_log.log("Backup data", backupData, true);

            remoteGps.GP_Multihop_storeBackupData(
                    Timestamp.now(),
                    backupData,
                    target.getLastSuccessfulBackupData(),
                    m_cycleIndex);
        }
        catch(Exception ex)
        {
            m_log.log(ex, true);
        }
    }

    private void loadAllPossibleTargets()
    {
        String[]            hosts = m_backupTargetProvider.getTargets();
        
        
        for(String host : hosts)
        {       
            if (!m_backupTargetList.containesTarget(host))
            {
                m_backupTargetList.addTarget(host);
                
                m_log.log(
                        "Added new target: " + host + 
                        " - current number of targets: " +
                        m_backupTargetList.size());              
            }
        }
    }    
}
