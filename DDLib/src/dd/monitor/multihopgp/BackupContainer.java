
package dd.monitor.multihopgp;

import dd.output.LoggingContext;


/*
 This container holds backup data of single service
 - the container maintains data such that when new data are added the container adds them to single bitset structure
 - thus there are no overlaps
 - the timeSlotSize is given from configuration - and has to be same! accross all synchronizations
 */

public class BackupContainer 
{
    private TimeSlotSet             m_slotSet;
    
        
    public BackupContainer()
    {
        
    }
    
    public void addChunkOfHistoryData(
            TimeSlotSet slotSet)
    {
        if (m_slotSet == null)
        {
            m_slotSet = slotSet;
        }
        else
        {
            m_slotSet = TimeSlotSet.joinTwoSets(m_slotSet, slotSet);
        }
    }
    
    public TimeSlotSet getBackupData(long fromSlot, long toSlot)
    {   
        if(m_slotSet == null) 
        {
            return null;
        }
        
        return m_slotSet.getSubSet(fromSlot, toSlot);
    }

    public boolean hasOccuredWithinTimewindow(long fromTimestamp, long toTimestamp) 
    {
        if(m_slotSet == null) 
        {
            return false;
        }
        
        long                fromSlotIndex = TimeSlotSet.getSlotIndexOfTimestamp(m_slotSet.getSlotSize(), fromTimestamp);
        long                toSlotIndex = TimeSlotSet.getSlotIndexOfTimestamp(m_slotSet.getSlotSize(), toTimestamp);
        
        
        for(long i = fromSlotIndex; i <= toSlotIndex; i++)
        {
            if (m_slotSet.getSlotValue(i))
            {
                return true;
            }
        }
        
        return false;
    }
    
    public long getLatestToTimestampOfOccuranceSlotWithinTimewindow(long fromTimestamp, long toTimestamp) 
    {
        if (!hasOccuredWithinTimewindow(fromTimestamp, toTimestamp))
        {
            return -1;
        }
        
        long                slotEndTimestamp;
        long                lastSlotEndTimestamp = 0;
        long                fromSlotIndex = TimeSlotSet.getSlotIndexOfTimestamp(m_slotSet.getSlotSize(), fromTimestamp);
        long                toSlotIndex = TimeSlotSet.getSlotIndexOfTimestamp(m_slotSet.getSlotSize(), toTimestamp);
        
        
        for(long i = fromSlotIndex; i <= toSlotIndex; i++)
        {
            if (m_slotSet.getSlotValue(i))
            {
                slotEndTimestamp = TimeSlotSet.getEndTimestampOfSlot(m_slotSet.getSlotSize(), i);
                lastSlotEndTimestamp = (slotEndTimestamp > lastSlotEndTimestamp) ? slotEndTimestamp : lastSlotEndTimestamp;            
            }
        }
        
        return lastSlotEndTimestamp;
    }

    public long getFirstFromTimestampOfOccuranceSlotWithinTimewindow(long fromTimestamp, long toTimestamp) 
    {
        if (!hasOccuredWithinTimewindow(fromTimestamp, toTimestamp))
        {
            return -1;
        }
        
        
        //LoggingContext log = LoggingContext.getContext(new BackupContainer(), "");
        //log.log("from", fromTimestamp);
        //log.log("to", toTimestamp);
        //m_slotSet.writeOut(log);
        
        long                slotStartTimestamp;
        long                firstSlotStartTimestamp = 0;
        long                fromSlotIndex = TimeSlotSet.getSlotIndexOfTimestamp(m_slotSet.getSlotSize(), fromTimestamp);
        long                toSlotIndex = TimeSlotSet.getSlotIndexOfTimestamp(m_slotSet.getSlotSize(), toTimestamp);
        
        
        for(long i = fromSlotIndex; i <= toSlotIndex; i++)
        {
            slotStartTimestamp = TimeSlotSet.getStartTimestampOfSlot(m_slotSet.getSlotSize(), i);

            if (m_slotSet.getSlotValue(i))
            {
                if (firstSlotStartTimestamp == 0)
                {
                    firstSlotStartTimestamp = slotStartTimestamp;
                }
                else
                {
                    firstSlotStartTimestamp = (slotStartTimestamp < firstSlotStartTimestamp) ? slotStartTimestamp : firstSlotStartTimestamp;
                }
            }
        }
        
        //log.log("outcome", firstSlotStartTimestamp);
        
        return firstSlotStartTimestamp;
    }
    
    public void writeOut(LoggingContext log)
    {
        m_slotSet.writeOut(log);
    }
}
