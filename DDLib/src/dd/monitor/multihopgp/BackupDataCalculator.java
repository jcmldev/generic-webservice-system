
package dd.monitor.multihopgp;

import dd.ExperimentConfiguration;
import dd.Timestamp;
import dd.output.LoggingContext;


public class BackupDataCalculator 
{
    private LoggingContext          m_log;
    private BackupDataSet           m_latestSuccessfulBackupSet;
    private int                     m_slotSize;
    private int                     m_maximumNumberOfSlotsToSynch;
    private long                    m_currentTimestampSlotIndex;
    private long                    m_smallestPossibleSlotToSynch;
    
    
    public BackupDataCalculator(
            LoggingContext log,
            String[] latestSuccessfulBackup)
    {
        m_log = log;
        m_slotSize = ExperimentConfiguration.getGossipProtocolMultihopBackupTimeSlotSize(m_log);
        m_maximumNumberOfSlotsToSynch = ExperimentConfiguration.getGossipProtocolMultihopMaximumNumberOfSlotsToSynch(m_log);
        
        m_latestSuccessfulBackupSet = BackupDataSet.loadRecordsFromLocalData(m_log, m_slotSize, latestSuccessfulBackup);
        m_currentTimestampSlotIndex = TimeSlotSet.getSlotIndexOfTimestamp(m_slotSize, Timestamp.now());
        m_smallestPossibleSlotToSynch = (m_currentTimestampSlotIndex - m_maximumNumberOfSlotsToSynch) + 1;
    }
    
    public int getSlotSize()
    {
        return m_slotSize;
    }
    
    public long getFromSlotIndex(
            BackupDataSetRecord.RecordType recordType, 
            String dependentId, 
            String antecedentId)
    {
        long                        index;
        BackupDataSetRecord         record;
        
        
        // was the dependence synched before?
        record = m_latestSuccessfulBackupSet.getRecord(recordType, dependentId, antecedentId);
        
        // yes it was
        if (record != null)
        {
            // first slot of latest synch + 1 : that is the next slot after the last ON synched slot
            index = record.getIndexOfLastOnSlot() + 1;
            
            // but no older than max history
            if (index < m_smallestPossibleSlotToSynch)
            {
                index = m_smallestPossibleSlotToSynch;
            }
        }
        else
        {
            // else take max history
            index = m_smallestPossibleSlotToSynch;
        }
        
        return index;
    }
    
    public long getIndexOfLastOnSlot(
            BackupDataSetRecord.RecordType recordType, 
            String dependentId, 
            String antecedentId)
    {
        long                        index = 0;
        BackupDataSetRecord         record;
        
        
        record = m_latestSuccessfulBackupSet.getRecord(
                recordType, 
                dependentId, 
                antecedentId);
        
        if (record != null)
        {
            index = record.getIndexOfLastOnSlot();
        }
        
        return index;        
    }
    
    public long getToSlotIndex()
    {
        return m_currentTimestampSlotIndex;
    }
}
