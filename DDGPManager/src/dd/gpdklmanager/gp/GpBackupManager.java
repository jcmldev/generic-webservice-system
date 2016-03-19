
package dd.gpdklmanager.gp;

import dd.gpdklmanager.gptarget.GpTargetProviderFactory;
import dd.gpdklmanager.gptarget.GpTargetProvider;
import dd.ExperimentConfiguration;
import dd.Network;
import dd.Timestamp;
import dd.gpdklmanager.BackupOnLocalHost;
import dd.gpdklmanager.BackupOnRemoteHost;
import dd.gpdklmanager.SynchronizationManager;
import dd.output.LoggingContext;

public class GpBackupManager extends SynchronizationManager
{
    private GpTargetList                    m_backupTargetList;
    private GpTargetProvider                m_backupTargetProvider;
    private GpMetadataManager               m_metadataManager;
    private BackupOnLocalHost      m_localGpAndDkl;
            
    
    public GpBackupManager(LoggingContext log, GpTargetList targetList)
    {
        super(log, 0);
        m_localGpAndDkl = new BackupOnLocalHost(log);
        m_backupTargetList = targetList;
        //m_backupTargetProvider = GpTargetProviderFactory.getTargetProviderFromConfiguration(log, m_localGpAndDkl);
        m_metadataManager = new GpMetadataManager(m_log, GpTargetProviderFactory.getNodesWithGossipProtocol(log));
    }
    
    @Override
    protected void runSynchronization() 
    {
        try
        {
            m_log.log("Gossip protocol (GP) - synchronization started ...");

            m_log.log("*** Step 1 - Update of data on backup nodes - info about antecedent dependencies of local services");
            try
            {
                loadTargets();
            }
            catch(Exception ex)
            {
                m_log.log("GP - Loading targets exception: " + ex.getMessage());
                m_log.log(ex);
            }


            m_log.log("GP - List of targets to synchronize with ...");

            for(GpTarget target : m_backupTargetList.getTargets())
            {
                m_log.log(
                        "     " + 
                        target.getHost() + 
                        " - last successful synchronization: " + 
                        target.getLastSuccessfulSynchronizationTimestamp());
            }

            for(GpTarget target : m_backupTargetList.getTargets())
            {
                runBackupIterationForTarget(target);
            }

            m_metadataManager.propagateMetadata();

                
        }
        catch(Exception ex)
        {
            m_log.log("GP exception: " + ex.getMessage());
            m_log.log(ex);
        }
    }
    
    private void runBackupIterationForTarget(GpTarget target)
    {
        long                                    fromTimestamp;
        long                                    toTimestamp = Timestamp.now();
        String[]                                backupData;
        BackupOnRemoteHost             remoteGps = new BackupOnRemoteHost(m_log, target);
        int                                     backupTimeSlotSize = ExperimentConfiguration.getGossipProtocolBackupTimeSlotSize(m_log);
        long                                    maximumHistoryTimestamp; 
                
        
        m_log.log("GP - synchronizing with target: " + target.getHost());
        
        maximumHistoryTimestamp = toTimestamp - ExperimentConfiguration.getGossipProtocolMaximumHistoryPeriodToSynch(m_log);

        // in first backup synch the timewindow of backup data is set to maximum allowed history
        // in subsequent backups the timewindow of backup data is based on the timestamp of last sucessful synchronization
        fromTimestamp = target.getLastSuccessfulSynchronizationTimestamp();

        // but it cant be too old data
        if (fromTimestamp < maximumHistoryTimestamp)
        {
            fromTimestamp = maximumHistoryTimestamp;
        }
        
        try
        {
            backupData = m_localGpAndDkl.GP_getBackupData(
                    fromTimestamp, 
                    toTimestamp, 
                    backupTimeSlotSize);

            m_log.log("Backup data", backupData);

            
            // do not send message if there are no data
            if (backupData.length == 0) return;
                            
            remoteGps.GP_storeBackupData(
                    Network.getLocalHostIp(m_log), 
                    fromTimestamp, 
                    toTimestamp, 
                    backupTimeSlotSize, 
                    backupData);
        }
        catch(Exception ex)
        {
            m_log.log(ex, false);
        }
    }
    
    private void loadTargets()
    {
        String[]            hosts = m_backupTargetProvider.getTargets();
        int                 targetNumberOfBackupNodes = ExperimentConfiguration.getGossipProtocolNumberOfBackupNodes(m_log);
        
        
        if (m_backupTargetList.size() >= targetNumberOfBackupNodes) return;
        
        for(String host : hosts)
        {
            
            if (!m_backupTargetList.containesTarget(host))
            {
                m_metadataManager.addTargetOfLocalHost(host);

                m_backupTargetList.addTarget(host);
                
                m_log.log(
                        "Added new target: " + host + 
                        " - current number of targets: " + m_backupTargetList.size() + 
                        " - out of aim: " + targetNumberOfBackupNodes);
            
                if (m_backupTargetList.size() >= targetNumberOfBackupNodes) return;
            }
        }
    }    
}
