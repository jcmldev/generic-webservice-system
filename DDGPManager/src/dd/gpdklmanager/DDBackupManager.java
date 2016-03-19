
package dd.gpdklmanager;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.RuntimeHelper;
import dd.gpdklmanager.gp.GpBackupManager;
import dd.gpdklmanager.gp.GpTargetList;
import dd.gpdklmanager.multihopgp.MultihopGpBackupManager;
import dd.gpdklmanager.multihopgp.MultihopGpTargetList;
import dd.output.LoggingContext;
import java.util.Date;

public class DDBackupManager 
{

    private static GpTargetList                 m_gpTargetList;
    private static MultihopGpTargetList         m_multihopGpTargetList;
    
    
    public static void main(String[] args) 
    {
        try
        {
            runSynchronizationCycle();
        }
        catch(Exception ex)
        {
            LoggingContext.getContext(new Exception(), "").log(ex);
        }
    }
    
    private static void runSynchronizationCycle()
    {
        LoggingContext              log = LoggingContext.getContext(new DDBackupManager(), null);
        int                         synchronizationFrequency = ExperimentConfiguration.getGossipProtocolBackupSynchronizationFrequency(log);
        int                         cycleIndex = 0;
        boolean                     runGpManager = ExperimentConfiguration.getSynchronizationManagerRunGossipProtocol(log);
        boolean                     runMultihopGpManager = ExperimentConfiguration.getSynchronizationManagerRunMultihopGossipProtocol(log);
                
        // with new log - with prefix of cycle !
                
        
        log.log("Gossip protocol and Distributed key lookup - manager started ...");
        
        runInitialDelay(log);
        
        log.log("Synchronization frequency: " + synchronizationFrequency + "ms");
        log.log("Run Gossip protocol synchronization: " + runGpManager);
        log.log("Run Multihop Gossip protocol synchronization: " + runMultihopGpManager);
        
        while(true)
        {
            cycleIndex++;
            log = LoggingContext.getContext(new DDBackupManager(), "Cycle " + cycleIndex);
            
            //runTest(synchronizationCounter);
            
            log.log("---------------------------------------------------------");
            
            log.log("Synchronization running on node: " + 
                    Network.getLocalHostIp(log) + 
                    " - cycle#" + 
                    cycleIndex + 
                    " at " + 
                    new Date());
            
            if (runGpManager)
            {
                runGpSynchronization(log);
            }
            
            if (runMultihopGpManager)
            {
                runMultihopGpSynchronization(log, cycleIndex);
            }
            
            log.log(" -- Next cycle will run in " + synchronizationFrequency + "ms");
            RuntimeHelper.waitDelay(log, synchronizationFrequency);
        }    
    }
    
    private static void runMultihopGpSynchronization(LoggingContext log, int cycleIndex)
    {
        if (m_multihopGpTargetList == null)
        {
            m_multihopGpTargetList = new MultihopGpTargetList(
                    LoggingContext.getContext(
                        new DDBackupManager(), ""));
        }
        
        SynchronizationManager.runSynchronization(
                new MultihopGpBackupManager(
                    log, 
                    m_multihopGpTargetList,
                    cycleIndex));
    }
    
    private static void runGpSynchronization(LoggingContext log)
    {
        if (m_gpTargetList == null)
        {
            m_gpTargetList = new GpTargetList(
                    LoggingContext.getContext(
                        new DDBackupManager(), ""));
        }
        
        SynchronizationManager.runSynchronization(
                new GpBackupManager(
                    log, 
                    m_gpTargetList));
    }
    
    private static void runInitialDelay(LoggingContext log)
    {
        int                 workloadDelay;
        
        
        workloadDelay = ExperimentConfiguration.getClientWorloadStartDelayDistributionStart(log);
     
        workloadDelay += 
             ((ExperimentConfiguration.getClientWorloadStartDelayDistributionEnd(log) - 
                 ExperimentConfiguration.getClientWorloadStartDelayDistributionStart(log)) *
             Math.random());

        log.log("   Starting with workload delay: " + workloadDelay + "ms");
        
        RuntimeHelper.waitDelay(log, workloadDelay);
        
        log.log("   Done waiting, synchronization is starting now ...");
    }
        
}
