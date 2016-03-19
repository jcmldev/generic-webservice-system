
package dd.monitor.dependencestorage;

import dd.output.LoggingContext;
import java.util.Set;
import java.util.TreeSet;

public class TimestampBasedContainer extends DependenceContainer
{
    private     Set<Long>    m_timestamps = new TreeSet<>();

    
    public TimestampBasedContainer (long containerStart, long containerPeriod)
    {
        super(containerStart, containerPeriod);
    }
    
    @Override
    public void addOccurrence(long timestamp) 
    {
        m_timestamps.add(new Long(timestamp));
    }

    @Override
    public boolean hasOccuredWithinTimewindow(long fromTimestamp, long toTimestamp) 
    {
        return simpleExpensiveCheck(fromTimestamp, toTimestamp);
    }
    
    private boolean simpleExpensiveCheck(long fromTimestamp, long toTimestamp)
    {
        Long[] elements = m_timestamps.toArray(new Long[0]);
        int size = elements.length;
        
        // find number which is smaller than to and bigger than from
        for (int i = 0; i < size; i++)
        {
            long element = elements[i].longValue();
            
            if (element <= toTimestamp && element >= fromTimestamp)
            {
                return true;
            }
        }
        
        return false;
    }    

    @Override
    public void writeOut(LoggingContext log) 
    {
        log.log("Timestamp occurance storage - size: " + m_timestamps.size());
        
        for(Long element : m_timestamps)
        {
            log.log(element.toString());
        }
    }
}
