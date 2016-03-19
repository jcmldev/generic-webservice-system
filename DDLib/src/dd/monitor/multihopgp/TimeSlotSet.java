
package dd.monitor.multihopgp;

import dd.output.LoggingContext;
import java.util.BitSet;



/*
 * BitSet representing data as timeslots of given size
 * slot represents period of time
 * the index of given slot (period of time) is calculated as start of this period from beggining of time (java time)
 * the set however represents only limited set of slots covering certain period of time
 * the index of first slot is thus the start index of the slot 
 * the bitset then represents values of slots relatively to the start index
 */

// the slot set exposes absolute slot indexes but works internally with relative indexes
public class TimeSlotSet 
{
    private long            m_indexOfFirstSlot;
    private long            m_slotSize;
    BitSet                  m_bitSet;
    
    
    public TimeSlotSet(
            long indexOfFirstSlot,
            long slotSize)
    {
        m_indexOfFirstSlot = indexOfFirstSlot;
        m_slotSize = slotSize;
        m_bitSet = new BitSet();
    }

    public TimeSlotSet(
        long indexOfFirstSlot,
        long slotSize,
        BitSet bitSet)
{
        m_indexOfFirstSlot = indexOfFirstSlot;
        m_slotSize = slotSize;
        m_bitSet = bitSet;
    }
        
    public long getSlotSize()
    {
        return m_slotSize;
    }
    
    public BitSet getBitSet()
    {
        return m_bitSet;
    }
    
    private int getRelativeSlotIndexFromAbsoluteSlotIndex(long absoluteSlotIndex)
    {
        return (int)(absoluteSlotIndex - m_indexOfFirstSlot);
    }
    
    public long getFirstSlotIndex()
    {
        return m_indexOfFirstSlot;
    }
    
    public long getLastSlotIndex()
    {
        if (m_bitSet.length() == 0)
        {
            return m_indexOfFirstSlot;
        }
        else
        {
            return m_indexOfFirstSlot + m_bitSet.length() - 1;
        }
    }
    
    public int getLenght()
    {
        return m_bitSet.length();
    }
    
    public boolean getSlotValue(long slotIndex)
    {
        if (slotIndex < m_indexOfFirstSlot) return false;
        
        int relativeSlotIndex = getRelativeSlotIndexFromAbsoluteSlotIndex(slotIndex);
        
        
        return m_bitSet.get(relativeSlotIndex);
    }
    
    public void setSlotValue(long slotIndex, boolean value)
    {
        if (slotIndex < m_indexOfFirstSlot) return;
        
        int relativeSlotIndex = getRelativeSlotIndexFromAbsoluteSlotIndex(slotIndex);
    
        
        m_bitSet.set(relativeSlotIndex, value);
    }
    
    // adds another set such that performs OR on matching slots and adds those not already in
    private void add(TimeSlotSet setToAdd)
    {
        assert setToAdd.m_indexOfFirstSlot >= m_indexOfFirstSlot;
        
        for (long i = setToAdd.getFirstSlotIndex(); i <= setToAdd.getLastSlotIndex(); i++)
        {
            setSlotValue(i, getSlotValue(i) || setToAdd.getSlotValue(i));
        }
    }
    
    public static TimeSlotSet joinTwoSets(TimeSlotSet a, TimeSlotSet b)
    {
        TimeSlotSet          newSet;
        long                    firstSlotIndex;
        
        
        firstSlotIndex = a.getFirstSlotIndex() < b.getFirstSlotIndex() ? a.getFirstSlotIndex() : b.getFirstSlotIndex();
        
        newSet = new TimeSlotSet(firstSlotIndex, a.m_slotSize);
        newSet.add(a);
        newSet.add(b);
        
        return newSet;
    }
    
    private long getSlotIndexOfFirstONSlot(long fromSlotIndex, long toSlotIndex)
    {
        long                    firstSetBit;
        long                    slotStartFrom;
        int                     relativeStartSlot;
        
        
        slotStartFrom = m_indexOfFirstSlot < fromSlotIndex ? fromSlotIndex : m_indexOfFirstSlot;  
        relativeStartSlot = getRelativeSlotIndexFromAbsoluteSlotIndex(slotStartFrom);
        
        firstSetBit = m_bitSet.nextSetBit(relativeStartSlot);
        
        if (firstSetBit == -1)
        {
            return -1;
        }
        else
        {
            firstSetBit += m_indexOfFirstSlot;
            
            if (firstSetBit > toSlotIndex) 
            {
                return -1;
            }
            
            return firstSetBit;
        }
    }
    
    // returns subset, however, the start slot index is based on first ON slot
    // if such a slot does not exist in the subset the start slot index equals to from index and set is empty
    public TimeSlotSet getSubSet(long fromSlotIndex, long toSlotIndex)
    {
        TimeSlotSet             newSet;
        long                    indexOfFirstOnSlotInSubSet = getSlotIndexOfFirstONSlot(fromSlotIndex, toSlotIndex);
        
        
        // there is no single ON slot
        if (indexOfFirstOnSlotInSubSet == -1)
        {
            newSet = new TimeSlotSet(fromSlotIndex, m_slotSize);
        }
        // some slots are ON
        else
        {
            newSet = new TimeSlotSet(indexOfFirstOnSlotInSubSet, m_slotSize);
            
            for (long i = indexOfFirstOnSlotInSubSet; i <= toSlotIndex; i++)
            {
                newSet.setSlotValue(i, getSlotValue(i));
            }
        }
        
        return newSet;
    }
    
    public static long getSlotIndexOfTimestamp(long slotSize, long timestamp)
    {
        long index;
        
        
        timestamp = timestamp - (timestamp % slotSize);
        index = timestamp / slotSize;
        
        return index;
    }
    
    public static long getStartTimestampOfSlot(long slotSize, long slotIndex)
    {
        return slotIndex * slotSize;
    }

    public static long getEndTimestampOfSlot(long slotSize, long slotIndex)
    {
        return getStartTimestampOfSlot(slotSize, slotIndex) + slotSize - 1;
    }
    
    public void writeOut(LoggingContext log)
    {
        log.log("State of time slot set - cardinality: " + m_bitSet.cardinality());
        
        for(long i = getFirstSlotIndex(); i <= getLastSlotIndex(); i++)
        {
            if (getSlotValue(i))
            {
                log.log(
                        "Set to true slot - index: " + 
                        i + 
                        " - from: " + 
                        getStartTimestampOfSlot(m_slotSize, i) + 
                        " - to: " + 
                        getEndTimestampOfSlot(m_slotSize, i));
            }
        }
    }
}
