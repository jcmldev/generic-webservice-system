
package dd.monitor.dependencestorage;

import dd.output.LoggingContext;
import java.util.BitSet;


public class BitArrayBasedContainer extends DependenceContainer
{
    private int                         m_timeSlotSize;
    private int                         m_numberOfSlots;
    private BitSet                      m_occuranceSet;
    

    public BitArrayBasedContainer (
            long containerStart, 
            long containerPeriod, 
            int timeSlotSize)
    {
        super(containerStart, containerPeriod);
        m_timeSlotSize = timeSlotSize;
        m_numberOfSlots = (int)containerPeriod / timeSlotSize;
        m_occuranceSet = new BitSet(m_numberOfSlots);
    }
    
    public BitArrayBasedContainer (
            long containerStart, 
            long containerPeriod, 
            int timeSlotSize,
            BitSet occuranceSet)
    {
        super(containerStart, containerPeriod);
        m_timeSlotSize = timeSlotSize;
        m_numberOfSlots = (int)containerPeriod / timeSlotSize;
        m_occuranceSet = occuranceSet;
    }

    @Override
    public void addOccurrence(long timestamp) 
    {
        int slotIndex = getSlotIndex(timestamp);
        
        
        if ((timestamp < m_containerStart) || (timestamp > getEnd()))
        {
            // will cause exception
            slotIndex = -1;
        }
        
        m_occuranceSet.set(slotIndex);
    }
    
    private int getSlotIndex(long timestamp)
    {
        long timeSinceContainerStart = timestamp - m_containerStart;
        int slotIndex;
        
        
        slotIndex = (int)(
                (timeSinceContainerStart - (timeSinceContainerStart % m_timeSlotSize)) 
                    / m_timeSlotSize); 
    
        return slotIndex;
    }

    @Override
    public boolean hasOccuredWithinTimewindow(long fromTimestamp, long toTimestamp) 
    {
        int             fromSlotIndex;
        int             toSlotIndex;
        boolean         result;
        
        
        
        if (fromTimestamp > getEnd()) return false;
        if (toTimestamp < m_containerStart) return false;
        
        if (fromTimestamp < m_containerStart) 
        {
            fromSlotIndex = 0;
        }
        else
        {
            fromSlotIndex = getSlotIndex(fromTimestamp);
        }
        
        toSlotIndex = getSlotIndex(toTimestamp);
        
        if (toSlotIndex >= m_numberOfSlots) 
        {
            toSlotIndex = m_numberOfSlots - 1;
        }
        
        if (fromSlotIndex == toSlotIndex)
        {
            result = m_occuranceSet.get(fromSlotIndex);
        }
        else
        {
            toSlotIndex++; // to is exclusive
            result = ! m_occuranceSet.get(fromSlotIndex, toSlotIndex).isEmpty();
        }
        
        return result;
    }

    @Override
    public void writeOut(LoggingContext log) 
    {
        log.log(
                "Bit container - start:" + 
                getStart() + 
                " end: " + 
                getEnd() + 
                " slot size: " +
                m_timeSlotSize +
                " number of slots: " + 
                m_numberOfSlots +
                " cardinality: " + 
                m_occuranceSet.cardinality());
        log.log(m_occuranceSet.toString());
    }    
}
