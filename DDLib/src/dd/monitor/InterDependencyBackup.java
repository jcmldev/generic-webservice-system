
package dd.monitor;

import dd.monitor.dependencestorage.BackupContainer;
import dd.monitor.dependencestorage.BitArrayBasedContainer;


public class InterDependencyBackup extends InterDependency
{
    private BackupContainer         m_backupContainer = new BackupContainer();
    
    
    public InterDependencyBackup(String target)
    {
        super(target);
    }
    
    @Override
    public boolean hasOccuredWithinTimewindow(long fromTimestamp, long toTimestamp)
    {
        return m_backupContainer.hasOccuredWithinTimewindow(fromTimestamp, toTimestamp);
    }
    
    public void addBackupData(
            long fromTimestamp, 
            long toTimestamp, 
            int timeSlotSize, 
            String backupData)
    {
        BitArrayBasedContainer              container;

       
        container = BackupDataHelper.getBackupDataFromString(
                fromTimestamp, 
                toTimestamp, 
                timeSlotSize, 
                backupData);
         
        m_backupContainer.addBackupContainer(container);
    }
}
