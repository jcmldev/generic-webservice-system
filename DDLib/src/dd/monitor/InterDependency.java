package dd.monitor;

import dd.Timestamp;
import dd.monitor.dependencestorage.DependenceStorage;
import dd.monitor.dependencestorage.DependenceStorageFactory;
import dd.monitor.multihopgp.TimeSlotSet;
import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.BitSet;

public class InterDependency implements Comparable {
    
    private String m_target;
    private DependenceStorage                 m_occurrenceStorage;
    
    
    public InterDependency(String target)
    {
        m_occurrenceStorage = DependenceStorageFactory.getDependenceStorageFromConfiguration(
                LoggingContext.getContext(this, ""));
        m_target = target;
    }
     
    public String getTarget()
    {
        return m_target;
    }
            
    public void addOccurrence()
    {
        m_occurrenceStorage.addOccurrence(Timestamp.now());
    }
            
    public void addOccurrence(long timestamp)
    {
        m_occurrenceStorage.addOccurrence(timestamp);
    }
    
    public boolean hasOccuredWithinTimewindow(long fromTimestamp, long toTimestamp)
    {
        return m_occurrenceStorage.hasOccuredWithinTimewindow(fromTimestamp, toTimestamp);
    }

    @Override
    public int compareTo(Object o) {
        
        InterDependency second = (InterDependency)o;
        
        
        return m_target.compareTo(second.m_target);
    }
    
    public void writeOut(LoggingContext log)
    {
        log.log("    Inter dependency - target: " + m_target);
        m_occurrenceStorage.writeOut(log);
    }
    
    public ArrayList<String> getBackupData(
            long fromTimestamp, 
            long toTimestamp, 
            int timeSlotSize)
    {
        return BackupDataHelper.getBackupDataFromStorage(
                m_target, 
                m_occurrenceStorage, 
                fromTimestamp, 
                toTimestamp, 
                timeSlotSize);
    }
    
    public TimeSlotSet getBackupDataForMultihopGp(
            long fromTimestamp, 
            long toTimestamp, 
            int timeSlotSize)
    {
        return BackupDataHelper.createBackupDataSlotSet(
                m_occurrenceStorage, 
                fromTimestamp, 
                toTimestamp, 
                timeSlotSize);
    }
}
