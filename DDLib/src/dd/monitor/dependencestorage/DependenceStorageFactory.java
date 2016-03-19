
package dd.monitor.dependencestorage;

import dd.ExperimentConfiguration;
import dd.output.LoggingContext;


public class DependenceStorageFactory {
    
    public static DependenceStorage getDependenceStorageFromConfiguration(LoggingContext log)
    {
        DependenceContainerFactory              containerFactory = null;
        DependenceStorage                       storage;
        String                                  storageType = ExperimentConfiguration.getMonitorStorageType(log);
        int                                     maintainedHistoryPeriod = ExperimentConfiguration.getMonitorMaintainedHistoryPeriod(log);
        int                                     numberOfContainers = ExperimentConfiguration.getMonitorNumberOfContainers(log);
        
        
        switch(storageType)
        {
            case "timestamp":
                containerFactory = getTimestampBasedFactory(log);
                break;
                
            case "bitarray":
                containerFactory = getBitArrayBasedFactory(log);
                break;
                
            case "bloomfilter":
                containerFactory = getBloomFilterBasedFactory(log);
                break;
        }
    
        storage = new DependenceStorage(
                maintainedHistoryPeriod, 
                numberOfContainers, 
                containerFactory);
        
        return storage;
    }
    
    private static DependenceContainerFactory getTimestampBasedFactory(LoggingContext log)
    {
        return new TimestampBasedContainerFactory();
    }
    
    private static DependenceContainerFactory getBitArrayBasedFactory(LoggingContext log)
    {
        return new BitArrayBasedContainerFactory(
                ExperimentConfiguration.getMonitorBitArrayTimeSlotSize(log));
    }

    private static DependenceContainerFactory getBloomFilterBasedFactory(LoggingContext log)
    {
        return new BloomFilterBasedContainerFactory(
                ExperimentConfiguration.getMonitorBloomFilterTimeSlotSize(log),
                ExperimentConfiguration.getMonitorBloomFilterFalsePositiveProbability(log));
    }
}
