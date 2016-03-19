
package dd.monitor.multihopgp;

import dd.monitor.BackupDataHelper;
import dd.output.LoggingContext;
import java.util.BitSet;




/*
 * This record takes care of recalculating the index of last ON slot based on size of the timeslotset
 * and vice versa
 * This assumes that the lenght of bitset is determined by last ON bit
 */
public class BackupDataSetRecord 
{
    public enum RecordType
    {
        Dependence,
        SymptomService,
        SymptomTransitive,
        SymptomTimeout,
        SymptomNetwork,
        UseHash;
        
        public static String toString(RecordType type)
        {
            return Integer.toString(type.ordinal());
        }
        
        public static RecordType fromString(String type)
        {
            int     typeInt = Integer.parseInt(type);
            
            return RecordType.values()[typeInt];
        }
    }
    
    private RecordType      m_type;
    private int             m_hash;
    private long            m_indexOfLastOnSlot;
    private String          m_dependentId;
    private String          m_antecedentId;
    private String          m_occuranceSet;
    private long            m_slotSize;
    
    
    public BackupDataSetRecord(
            RecordType type,
            int hash,
            String dependentId,
            String antecedentId,
            long indexOfLastOnSlot,
            String occuranceSet,
            long slotSize)
    {
        assert occuranceSet != null;
        assert occuranceSet.length() > 0 ? indexOfLastOnSlot > 0 : true;
        
        m_type = type;
        m_hash = hash;
        m_dependentId = dependentId;
        m_antecedentId = antecedentId;
        m_indexOfLastOnSlot = indexOfLastOnSlot;
        m_occuranceSet = occuranceSet;
        m_slotSize = slotSize;
    }
    
    public static BackupDataSetRecord makeRecordFromNewBackupData(
            RecordType type,
            String dependentId,
            String antecedentId,
            long indexOfLastOnSlot,
            TimeSlotSet slotSet,
            long slotSize)
    {
        String          occuranceSet = "";
        
        
        // if slot set is not empty, change last on slot index
        if (slotSet.getLenght() != 0)
        {
            indexOfLastOnSlot = slotSet.getLastSlotIndex();
            occuranceSet = BackupDataHelper.bitSetToString(slotSet.getBitSet());
        }
        
        return new BackupDataSetRecord(
                type,
                0,
                dependentId, 
                antecedentId, 
                indexOfLastOnSlot, 
                occuranceSet,
                slotSize);    
    }
    
    public String getKey()
    {
        if (m_dependentId != null && m_dependentId.length() > 0)
        {
            return m_type.name() + m_dependentId + m_antecedentId;
        }
        else
        {
            return Integer.toString(m_hash);
        }
    }
    
    public RecordType getType()
    {
        return m_type;
    }
    
    public int getHashId()
    {
        return m_hash;
    }
    
    public int calculateHash()
    {
        return getKey().hashCode();
    }
    
    public long getIndexOfLastOnSlot()
    {
        return m_indexOfLastOnSlot;
    }
    
    public String getDependentId()
    {
        return m_dependentId;
    }
    
    public String getAntecedentId()
    {
        return m_antecedentId;
    }
    
    public boolean containsAnySlots()
    {
        return m_occuranceSet.length() > 0;
    }
    
    public TimeSlotSet getSlotSet()
    {
        return getSlotSetFromString(m_occuranceSet);
    }
    
    private TimeSlotSet getSlotSetFromString(String occuranceSet)
    {
        if (!containsAnySlots()) { return null; }
        
        BitSet          bitSet;
        long            indexOfFirstSlot;
        TimeSlotSet     slotSet;
        
        
        bitSet = BackupDataHelper.getBackupDataFromString(occuranceSet);
        indexOfFirstSlot = m_indexOfLastOnSlot - (bitSet.length() - 1);
        slotSet = new TimeSlotSet(indexOfFirstSlot, m_slotSize, bitSet);
        
        return slotSet;
    }
    
    public String getOccuranceSet()
    {
        return m_occuranceSet;
    }
        
    public void writeOut(LoggingContext log)
    {
        log.log("BackupDataSetRecordHash");
        log.log("m_type", m_type.name());
        log.log("m_hash", m_hash);
        log.log("key", getKey());
        log.log("calculated hash", calculateHash());
        log.log("m_indexOfLastOnSlot", m_indexOfLastOnSlot);
        log.log("m_dependentId", m_dependentId);
        log.log("m_antecedentId", m_antecedentId);
        log.log("m_occuranceSet", m_occuranceSet);
        log.log("m_slotSize", m_slotSize);
    }
}
