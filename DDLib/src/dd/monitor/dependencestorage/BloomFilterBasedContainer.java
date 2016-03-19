
package dd.monitor.dependencestorage;

//import com.skjegstad.utils.BloomFilter;
import dd.output.LoggingContext;


public class BloomFilterBasedContainer extends DependenceContainer
{
    private int                                 m_timeSlotSize;                 
    //private BloomFilter<Long>                   m_bloomFilter;
    

    public BloomFilterBasedContainer(
            long containerStart, 
            long containerPeriod, 
            int timeSlotSize,
            double falsePositiveProbability)
    {
        super(containerStart, containerPeriod);
        m_timeSlotSize = timeSlotSize;

        int expectedNumberOfElements = (int)containerPeriod / timeSlotSize;
/*
        m_bloomFilter = new BloomFilter<>(
                falsePositiveProbability, 
                expectedNumberOfElements);
                * */
    }
    
    private long getSlotForTimestamp(long timestamp)
    {
        return timestamp - (timestamp % m_timeSlotSize);
    }
    
    @Override
    public void addOccurrence(long timestamp) 
    {
        // the slot is determined by rounding the number to slot precision 
        long slotTimestamp = getSlotForTimestamp(timestamp);
        
        
        //m_bloomFilter.add(slotTimestamp);
    }

    @Override
    public boolean hasOccuredWithinTimewindow(long fromTimestamp, long toTimestamp) 
    {
        long                    slotFrom = getSlotForTimestamp(fromTimestamp);
        long                    slotTo = getSlotForTimestamp(toTimestamp);
        
        
        // have to check if any of the slots within the time window is set to 1
        for(long slot = slotFrom; slot <= slotTo; slot += m_timeSlotSize)
        {
            /*
            if (m_bloomFilter.contains(slot))
            {
                return true;
            }
            */
        }
        
        return false;        
    }

    @Override
    public void writeOut(LoggingContext log) 
    {
        /*
        log.log("BloomFilter - size: " + m_bloomFilter.size());
        log.log(m_bloomFilter.getBitSet().toString());
    
    */ }
    
}
