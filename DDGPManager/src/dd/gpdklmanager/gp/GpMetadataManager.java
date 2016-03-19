
package dd.gpdklmanager.gp;

import dd.gpdklmanager.gptarget.GpTargetProviderDependent;
import dd.Network;
import dd.Timestamp;
import dd.gpdklmanager.BackupOnLocalHost;
import dd.gpdklmanager.BackupOnRemoteHost;
import dd.output.LoggingContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class GpMetadataManager 
{
    private LoggingContext                          m_log;
    private HashMap<String, GpTarget>               m_metadataTargets = new HashMap<>();
    private List<String>                            m_lastMetadataUpdateState;
    private long                                    m_lastMetadataUpdateTimestamp;
    private List<String>                            m_nodesWithGossipProtocolService;
    private BackupOnLocalHost              m_localGpAndDkl;
    
    
    public GpMetadataManager(LoggingContext log, List<String> nodesWithGossipProtocolService)
    {
        m_log = log;
        m_localGpAndDkl = new BackupOnLocalHost(log);
        m_nodesWithGossipProtocolService = nodesWithGossipProtocolService;
    }

    public void addTargetOfLocalHost(String target)
    {
        m_localGpAndDkl.GP_addTargetOfNode(Network.getLocalHostIp(m_log), target);
    }
        
    public void propagateMetadata()
    {
        m_log.log("*** Step 2 - Propagates metadata to all dependent nodes (cumulative list of node to node dependencies)");
        
        long                                        updateTimestamp = Timestamp.now();
        BackupOnRemoteHost                 remoteGps;
        List<String>                                metadata = m_localGpAndDkl.GP_getMetadata();
        
        
        // metadata are sent to targets only if there are some new data to be sent (or there was no synch before)
        
        loadMetadataTargets();
        
        //m_log.log("Metadata to synchronize:", metadata.toArray(new String[0]));
        m_log.log("Metadata to synchronize: size=" + metadata.size());
        
        for(GpTarget metadataTarget : m_metadataTargets.values())
        {
            if (shouldMetadataBeUpdatedOnHost(metadata, metadataTarget))
            {
                remoteGps = new BackupOnRemoteHost(m_log, metadataTarget);
                remoteGps.GP_addMetadata(metadata, updateTimestamp);
            }            
        }
        
        if(wereMetadataChanged(metadata))
        {
            m_lastMetadataUpdateTimestamp = updateTimestamp;
            m_lastMetadataUpdateState = metadata;
        }
    }
    
    private void loadMetadataTargets()
    {
        /*
        GpTargetProviderDependent       dependentTargets = new GpTargetProviderDependent(m_log, m_nodesWithGossipProtocolService, m_localGpAndDkl);
        String[]                        hosts = dependentTargets.getTargets();
        
        
        for(String host : hosts)
        {
            if (!m_metadataTargets.containsKey(host))
            {
                m_metadataTargets.put(host, new GpTarget(m_log, host));
                m_log.log("Gossip protocol - metadata target added: " + host);
            }
        }
        
        m_log.log(
                "GP - List of metadata targets - dependent nodes ...", 
                m_metadataTargets.keySet().toArray(new String[0]));
                */ 
    }
    
    private boolean wereMetadataChanged(List<String> metadata)
    {
        if(m_lastMetadataUpdateState == null)
            return true;

        // items are only added
        return m_lastMetadataUpdateState.size() != metadata.size();
    }
    
    private boolean shouldMetadataBeUpdatedOnHost(List<String> metadata, GpTarget metadataTarget)
    {
        if (metadataTarget.getLastSuccessfulSynchronizationTimestamp() == 0L) 
            return true;
                
        if (metadataTarget.getLastSuccessfulSynchronizationTimestamp() < m_lastMetadataUpdateTimestamp) 
            return true;
        
        if (wereMetadataChanged(metadata))
            return true;
        
        return false;
    }
}