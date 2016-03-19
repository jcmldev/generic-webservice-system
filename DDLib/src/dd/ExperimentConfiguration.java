package dd;

import dd.output.LoggingContext;
import org.w3c.dom.Element;

public class ExperimentConfiguration 
{
    private static Element                          m_configurationElement;
    

    private synchronized static String getAttribute(LoggingContext log, String attribute)
    {
        if (m_configurationElement == null)
        {
            m_configurationElement = Configuration.getExperimentElement(log);
        }
        
        if (!m_configurationElement.hasAttribute(attribute))
        {
            log.log("Configuraiton attribute is missing: " + attribute);
            return null;
        }
        
        if (m_configurationElement.getAttribute(attribute).length() == 0)
        {
            log.log("Configuraiton attribute is empty: " + attribute);
        }
        
        return m_configurationElement.getAttribute(attribute);
    }

    public static boolean getDependenceDiscoveryRunConversationDiscovery(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "dependenceDiscoveryRunConversationDiscovery"));
    }
                
    public static boolean getDependenceDiscoveryRunSystemWideDiscovery(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "dependenceDiscoveryRunSystemWideDiscovery"));
    }
                
    public static int getServiceSystemMessageExchangeTimeout(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "serviceSystemMessageExchangeTimeout"));
    }
    
    public static int getServiceSystemMessageExchangeTimeoutSymptomMonitor(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "serviceSystemMessageExchangeTimeoutSymptomMonitor"));
    }
    
    public static int getServiceSystemMessageExchangeTimeoutAdditionalWraperTime(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "serviceSystemMessageExchangeTimeoutAdditionalWraperTime"));
    }
    
    public static String getServiceSystemGenericServiceAddress(LoggingContext log)
    {
        return getAttribute(log, "serviceSystemGenericServiceAddress");
    }

    public static String getTubeIncomingRequestMessageTargetUrlTemplate(LoggingContext log)
    {
        return getAttribute(log, "tubeIncomingRequestMessageTargetUrlTemplate");
    }

    public static String getTubeServiceNamePatternOfOutogingServicesToRecord(LoggingContext log)
    {
        return getAttribute(log, "tubeServiceNamePatternOfOutogingServicesToRecord");
    }

    public static String getDependenceDiscoveryServiceAddress(LoggingContext log)
    {
        return getAttribute(log, "dependenceDiscoveryServiceAddress");
    }
    
    public static int getDependenceDiscoveryDelayBetweenAttempts(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "dependenceDiscoveryDelayBetweenAttempts"));
    }
    
    public static int getDependenceDiscoveryRequestTimeout(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "dependenceDiscoveryRequestTimeout"));
    }
    
    public static boolean getDependenceDiscoveryUseDependenceDiscoveryProtocol(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "dependenceDiscoveryUseDependenceDiscoveryProtocol"));
    }
    
    public static boolean getDependenceDiscoveryUseDirectHarvesting(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "dependenceDiscoveryUseDirectHarvesting"));
    }
    
    public static boolean getDependenceDiscoveryUseGossipProtocol(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "dependenceDiscoveryUseGossipProtocol"));
    }
    
    public static boolean getDependenceDiscoveryUseMultihopGossipProtocol(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "dependenceDiscoveryUseMultihopGossipProtocol"));
    }
    
    public static boolean getDependenceDiscoveryUseMultihopGossipProtocolWithAntecedent(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "dependenceDiscoveryUseMultihopGossipProtocolWithAntecedent"));
    }
            
    public static int getDependenceDiscoverySystemWideTimeWindowSize(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "dependenceDiscoverySystemWideTimeWindowSize"));
    }
    
    public static int getGossipProtocolQueryTimeout(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "gossipProtocolQueryTimeout"));
    }
    
    public static int getGossipProtocolNumberOfBackupNodes(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "gossipProtocolNumberOfBackupNodes"));
    }
    
    public static int getGossipProtocolBackupSynchronizationFrequency(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "gossipProtocolBackupSynchronizationFrequency"));
    }

    public static int getGossipProtocolBackupUpdateTimeout(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "gossipProtocolBackupUpdateTimeout"));
    }
        
    public static String getGossipProtocolServiceAddress(LoggingContext log)
    {
        return getAttribute(log, "gossipProtocolServiceAddress");
    }
        
    public static String getGossipProtocolBackupNodeSelectionAlgorithm(LoggingContext log)
    {
        return getAttribute(log, "gossipProtocolBackupNodeSelectionAlgorithm");
    }
        
    public static int getGossipProtocolBackupTimeSlotSize(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "gossipProtocolBackupTimeSlotSize"));
    }
    
    public static int getGossipProtocolMaximumHistoryPeriodToSynch(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "gossipProtocolMaximumHistoryPeriodToSynch"));
    }
    
    public static int getGossipProtocolMultihopMaximumNumberOfSlotsToSynch(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "gossipProtocolMultihopMaximumNumberOfSlotsToSynch"));
    }
    
    public static int getGossipProtocolMultihopBackupTimeSlotSize(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "gossipProtocolMultihopBackupTimeSlotSize"));
    }
    
    public static int getDistributedKeyLookupQueryTimeout(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "distributedKeyLookupQueryTimeout"));
    }
    
    public static int getDistributedKeyLookupNumberOfBackupNodes(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "gossipProtocolNumberOfBackupNodes"));
    }

    public static int getGossipProtocolRoutingTableMaximumMetric(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "gossipProtocolRoutingTableMaximumMetric"));
    }

    public static int getDistributedKeyLookupBackupUpdateTimeout(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "distributedKeyLookupBackupUpdateTimeout"));
    }

    public static String getDistributedKeyLookupServiceAddress(LoggingContext log)
    {
        return getAttribute(log, "distributedKeyLookupServiceAddress");
    }
     
    public static int getClientDependenceDiscoveryDelayAfterConversation(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "clientDependenceDiscoveryDelayAfterConversation"));
    }
     
    public static boolean getClientDependenceDiscoveryRunInParallelToWorkload(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "clientDependenceDiscoveryRunInParallelToWorkload"));
    }
    
    public static boolean getSynchronizationManagerRunGossipProtocol(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "synchronizationManagerRunGossipProtocol"));
    }
    
    public static boolean getSynchronizationManagerRunMultihopGossipProtocol(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "synchronizationManagerRunMultihopGossipProtocol"));
    }
    
    public static boolean getSynchronizationManagerRunDistributedKeyLookup(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "synchronizationManagerRunDistributedKeyLookup"));
    }   
    
    public static int getDependenceDiscoveryNumberOfAttempts(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "dependenceDiscoveryNumberOfAttempts"));
    }
    
    public static int getDependenceDiscoveryTimeWindowAdditionalTimeBefore(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "dependenceDiscoveryTimeWindowAdditionalTimeBefore"));
    }
    
    public static int getDependenceDiscoveryTimeWindowAdditionalTimeAfter(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "dependenceDiscoveryTimeWindowAdditionalTimeAfter"));
    }
    
    public static boolean getDependenceDiscoveryUseThirdParty(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "dependenceDiscoveryUseThirdParty"));
    }
        
    public static String getDependenceDiscoveryThirdPartyIp(LoggingContext log)
    {
        return getAttribute(log, "dependenceDiscoveryThirdPartyIp");
    }
    
    public static int getClientWorloadStartDelayDistributionStart(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "clientWorloadStartDelayDistributionStart"));
    }
     
    public static int getClientWorloadStartDelayDistributionEnd(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "clientWorloadStartDelayDistributionEnd"));
    }

    public static boolean getDependenceDiscoveryUseDistributedKeyLookup(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "dependenceDiscoveryUseDistributedKeyLookup"));
    }
    
    public static boolean getFlServerLogScanningOn(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "flServerLogScanningOn"));
    }
    
    public static boolean getFlUseInMemoryLog(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "flUseInMemoryLog"));
    }
    
    public static int getFlTimeoutSymptomsTimeWindowSize(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "flTimeoutSymptomsTimeWindowSize"));
    }

    private static int monitorMaintainedHistoryPeriod = 0;
    public static int getMonitorMaintainedHistoryPeriod(LoggingContext log)
    {
        if (monitorMaintainedHistoryPeriod == 0)
        {
            monitorMaintainedHistoryPeriod = Integer.parseInt(getAttribute(log, "monitorMaintainedHistoryPeriod"));
        }
        
        return monitorMaintainedHistoryPeriod;
    }

    private static int monitorNumberOfContainers = 0;
    public static int getMonitorNumberOfContainers(LoggingContext log)
    {
        if (monitorNumberOfContainers == 0)
        {
            monitorNumberOfContainers = Integer.parseInt(getAttribute(log, "monitorNumberOfContainers"));
        }
        return monitorNumberOfContainers;
    }

    public static int getMonitorBitArrayTimeSlotSize(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "monitorBitArrayTimeSlotSize"));
    }

    public static int getMonitorBloomFilterTimeSlotSize(LoggingContext log)
    {
        return Integer.parseInt(getAttribute(log, "monitorBloomFilterTimeSlotSize"));
    }

    public static double getMonitorBloomFilterFalsePositiveProbability(LoggingContext log)
    {
        return Double.parseDouble(getAttribute(log, "monitorBloomFilterFalsePositiveProbability"));
    }

    private static String monitorStorageType = null;
    public static String getMonitorStorageType(LoggingContext log)
    {
        if (monitorStorageType == null)
        {
            monitorStorageType = getAttribute(log, "monitorStorageType"); 
        }
        
        return monitorStorageType;
    }
    
    public static boolean getUseSingleMachineDebugCode(LoggingContext log)
    {
        return Boolean.valueOf(getAttribute(log, "useSingleMachineDebugCode"));
    }
}
