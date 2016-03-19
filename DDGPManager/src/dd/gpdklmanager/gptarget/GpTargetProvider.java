package dd.gpdklmanager.gptarget;

import dd.gpdklmanager.BackupOnLocalHost;
import dd.output.LoggingContext;
import java.util.List;

public abstract class GpTargetProvider 
{
    protected LoggingContext                  m_log; 
    protected List<String>                    m_nodesWithGossipProtocol; 
    protected BackupOnLocalHost               m_localGpAndDkl;
    protected int                               m_maxMetricInRoutingTable;
    
            
    public GpTargetProvider(
            LoggingContext log, 
            List<String> nodesWithGossipProtocol, 
            BackupOnLocalHost localGpAndDkl,
            int maxMetricInRoutingTable)
    {
        m_log = log;
        m_nodesWithGossipProtocol = nodesWithGossipProtocol;
        m_localGpAndDkl = localGpAndDkl;
        m_maxMetricInRoutingTable = maxMetricInRoutingTable;
    }
    
    public abstract String[] getTargets();
}
