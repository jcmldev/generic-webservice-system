
package dd.monitor.multihopgp;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class BackupDataSet 
{
    private LoggingContext                                  m_log;
    private long                                            m_slotSize;
    private Map<String, BackupDataSetRecord>                m_records;
    
    
    public BackupDataSet(LoggingContext log, long slotSize)
    {
        m_log = log;
        m_records = new TreeMap<>();
        m_slotSize = slotSize;
    }
    
    public BackupDataSet(LoggingContext log, long slotSize, Map<String, BackupDataSetRecord> records)
    {
        m_log = log;
        m_slotSize = slotSize;
        m_records = records;
    }
    
    public static BackupDataSet loadRecordsFromLocalData(
            LoggingContext log, 
            long slotSize, 
            String[] records)
    {
        if (records == null) 
        {
            records = new String[0];
        }
        
        Map<String, BackupDataSetRecord> recordsMap = new TreeMap<>();
        int numberOfSetRecords = records.length / 5;
        BackupDataSetRecord setRecord;
        int baseRecordIndex;
        
        String recordType;
        String dependentId;
        String antecedentId;
        long indexOfLastOnSlot;
        String occuranceSet;
        
        
        for(int i = 0; i < numberOfSetRecords; i++)
        {
            baseRecordIndex = i * 5;
            
            recordType = records[baseRecordIndex];
            dependentId = records[baseRecordIndex + 1];
            antecedentId = records[baseRecordIndex + 2];
            indexOfLastOnSlot = Long.parseLong(records[baseRecordIndex + 3]);
            occuranceSet = records[baseRecordIndex + 4];
            
            setRecord = new BackupDataSetRecord(
                    BackupDataSetRecord.RecordType.fromString(recordType),
                    0,
                    dependentId, 
                    antecedentId, 
                    indexOfLastOnSlot, 
                    occuranceSet, 
                    slotSize);
            
            recordsMap.put(setRecord.getKey(), setRecord);
        }
        
        return new BackupDataSet(log, slotSize, recordsMap);
    }
    
    public static BackupDataSet loadRecordsFromTransferedData(
            LoggingContext log, 
            long slotSize, 
            String[] records)
    {
        Map<String, BackupDataSetRecord>            recordsMap = new TreeMap<>();
        int                                         numberOfSetRecords = records.length / 3;
        BackupDataSetRecord                         setRecord;
        int                                         baseRecordIndex;
        String                                      key;
        String                                      dependentId;
        String                                      antecedentId;
        int                                         hash;
        long                                        indexOfLastOnSlot;
        String                                      occuranceSet;
        BackupDataSetRecord.RecordType              recordType;
        int                                         delimiterIndex;
        
        
        for(int i = 0; i < numberOfSetRecords; i++)
        {
            baseRecordIndex = i * 3;
            
            key = records[baseRecordIndex];
            indexOfLastOnSlot = Long.parseLong(records[baseRecordIndex + 1]);
            occuranceSet = records[baseRecordIndex + 2];
        
            
            // contains url
            delimiterIndex = key.indexOf("http", 0);
            
            // contains client process id
            if (delimiterIndex == -1)
            {
                delimiterIndex = key.indexOf(":", 0);
            }
            
            // is it idendifier(s) or hash
            if (delimiterIndex > 0)
            {
                recordType = BackupDataSetRecord.RecordType.fromString(key.substring(0, 1));
                delimiterIndex = key.indexOf("http", 5);
                
                // single url == service
                if (delimiterIndex == -1)
                {
                    dependentId = key.substring(1);
                    antecedentId = "";
                }
                // two urls == dependence
                else
                {                
                    dependentId = key.substring(1, delimiterIndex);
                    antecedentId = key.substring(delimiterIndex, key.length());
                }
                
                hash = 0;
            }
            else
            {
                recordType = BackupDataSetRecord.RecordType.UseHash;
                hash = Integer.parseInt(key);
                dependentId = "";
                antecedentId = "";
            }
            
            setRecord = new BackupDataSetRecord(
                    recordType,
                    hash,
                    dependentId, 
                    antecedentId, 
                    indexOfLastOnSlot, 
                    occuranceSet, 
                    slotSize);
            
            recordsMap.put(setRecord.getKey(), setRecord);
        }
        
        return new BackupDataSet(log, slotSize, recordsMap);
    }
    
    public void addDependenceData(
            BackupDataSetRecord.RecordType recordType,
            String dependentId,
            String antecedentId,
            TimeSlotSet slotSet,
            long indexOfLastOnSlot)
    {
        BackupDataSetRecord         record;
        
        
        record = BackupDataSetRecord.makeRecordFromNewBackupData(
                recordType,
                dependentId, 
                antecedentId, 
                indexOfLastOnSlot, 
                slotSet, 
                m_slotSize);

        m_records.put(record.getKey(), record);
    }
    
    public BackupDataSetRecord getRecord(
            BackupDataSetRecord.RecordType recordType, 
            String dependentId, 
            String antecedentId)
    {
        BackupDataSetRecord record = new BackupDataSetRecord(recordType, 0, dependentId, antecedentId, 0, null, 0);
        
        
        return m_records.get(record.getKey());
    }
    
    public BackupDataSetRecord[] getRecords()
    {
        return m_records.values().toArray(new BackupDataSetRecord[0]);
    }
        
    public int size()
    {
        return m_records.size();
    }
    
    public String[] toStringArray()
    {
        List<String>        array = new ArrayList<>();
        
        
        for(BackupDataSetRecord record : m_records.values())
        {
            addRecordToArray(record, array);
        }
        
        return array.toArray(new String[0]);
    }
    
    private void addRecordToArray(
            BackupDataSetRecord record, 
            List<String> array)
    {
        array.add(BackupDataSetRecord.RecordType.toString(record.getType()));
        array.add(record.getDependentId());
        array.add(record.getAntecedentId());
        array.add(Long.toString(record.getIndexOfLastOnSlot()));
        array.add(record.getOccuranceSet());
    }
    
    public String[] getDataToTransfer(BackupDataSet lastSuccessfulSet, String targetIp)
    {
        List<String>                    array = new ArrayList<>();
        BackupDataSetRecord             previousRecord;
        
        
        for(BackupDataSetRecord record : m_records.values())
        {
            if (record.containsAnySlots())
            {
                previousRecord = null;
                
                if (lastSuccessfulSet != null)
                {
                    previousRecord = lastSuccessfulSet.getRecord(
                            record.getType(),
                            record.getDependentId(), 
                            record.getAntecedentId());
                }
                
                if (!shouldTheRecordBeExcludedBecauseItOriginatesAtTarget(record, targetIp))
                {
                    addRecordToArrayHash(
                            record, 
                            array,
                            previousRecord);
                }
            }
        }
        
        return array.toArray(new String[0]);
    }
    
    private boolean shouldTheRecordBeExcludedBecauseItOriginatesAtTarget(BackupDataSetRecord record, String targetIp)
    {
        String          dependentIp;
        boolean         isIdUrl = record.getDependentId().contains("http");
        
        
        if (isIdUrl)
        {
            //http://10.0.0.1:80/sssssss
            dependentIp = Network.getHostIpFromUrl(
                    m_log, 
                    record.getDependentId());
        }
        else
        {
            dependentIp = record.getDependentId();
        }
        
        if (ExperimentConfiguration.getUseSingleMachineDebugCode(m_log))
        {
            targetIp = "testing on local machine";
            m_log.log("avoiding exclusion of local ip from synchronization");
        }
        
        return (dependentIp.equals(targetIp));
    }
    
    private void addRecordToArrayHash(
            BackupDataSetRecord record, 
            List<String> array,
            BackupDataSetRecord previousRecord)
    {
        String      key;
        boolean     canHashBeUsed = false;
        
        
        // this will work almost in all cases, however, in exceptions it might result in hash when full key should be used
        if (previousRecord != null)
        {
            if (previousRecord.getIndexOfLastOnSlot() > 0)
            {
                canHashBeUsed = true;
            }
        }
        
        if (canHashBeUsed)
        {
            key = Integer.toString(
                    record.calculateHash()
                    );
        }
        else
        {
            key = 
                    BackupDataSetRecord.RecordType.toString(record.getType()) +
                    record.getDependentId() + 
                    record.getAntecedentId();
        }

        array.add(key);
        array.add(Long.toString(record.getIndexOfLastOnSlot()));
        array.add(record.getOccuranceSet());
    }
    
    public void writeOut(LoggingContext log)
    {
        log.log("BackupDataSet - size: " + m_records.size() + " - slotSize: " + m_slotSize);
        
        for(BackupDataSetRecord record : m_records.values())
        {
            record.writeOut(log);
        }        
    }
}
