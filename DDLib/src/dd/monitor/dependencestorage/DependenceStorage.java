
package dd.monitor.dependencestorage;

import dd.output.LoggingContext;
import java.util.LinkedList;
import java.util.Queue;


        // the storage needs to maintain whole history period separated into chunks
        // thus there is always some extra older data in order to cover whole needed period
        // threfore there is one current container + the number of containers configured covering whole past
        

public class DependenceStorage 
{
    private Queue<DependenceContainer>          m_historyContainers = new LinkedList<>();
    private DependenceContainer                 m_currentContainer;
    private long                                m_currentContainerEnd;
    private DependenceContainerFactory          m_containerFactory;
    private int                                 m_containerPeriod;
    private int                                 m_numberOfContainers;
    private long                                m_lastAddedTimestamp;
    
    
    public DependenceStorage(
            int maintainedHistoryPeriod,
            int numberOfContainers,
            DependenceContainerFactory containerFactory)
    {
        m_containerFactory = containerFactory;
        m_containerPeriod = maintainedHistoryPeriod / numberOfContainers;
        m_numberOfContainers = numberOfContainers;
        m_currentContainerEnd = 0;
        m_lastAddedTimestamp = -1;
    }
    
    private void createFirstContainer(long minTimestampToStore)
    {
        m_currentContainer = m_containerFactory.createContainer(minTimestampToStore, m_containerPeriod);        
        m_currentContainerEnd = m_currentContainer.getEnd();    
    }
    
    private void swapContainer(long minTimestampToStore)
    {
        if (m_currentContainerEnd == 0)
        {
            createFirstContainer(minTimestampToStore);
        }
        else
        {
            m_historyContainers.add(m_currentContainer);

            if (m_historyContainers.size() > m_numberOfContainers)
            {
                m_historyContainers.remove();
            }

            m_currentContainer = m_containerFactory.createContainer(m_currentContainerEnd + 1, m_containerPeriod);
            m_currentContainerEnd = m_currentContainer.getEnd();
        }
        
        // when service is long time not called the time gap is longer than lenght of container
        // thus recursively call swap untill the current container contains slot for timestamp to store
        // this also removes obsolete containers
        if (m_currentContainerEnd < minTimestampToStore)
        {
            swapContainer(minTimestampToStore);
        }
    }
    
    // the assumption is that every timestamp added is higher than the previous one
    public void addOccurrence(long timestamp) 
    {
        assert timestamp >= m_lastAddedTimestamp;
        
        if (m_currentContainerEnd < timestamp)
        {
            swapContainer(timestamp);
        }
    
        m_currentContainer.addOccurrence(timestamp);
    }

    public boolean hasOccuredWithinTimewindow(long fromTimestamp, long toTimestamp) 
    {
        boolean response = false;
     
        /*
         the answer may be split over several containers
         for container to be queried the following conditions have to hold
         - to >= start
         - from <= end
         */
    
        if (fromTimestamp > m_currentContainer.getEnd()) 
            return false;
        
        if (toTimestamp >= m_currentContainer.getStart())
        {
            response = m_currentContainer.hasOccuredWithinTimewindow(fromTimestamp, toTimestamp);
        }
        
        if (!response)
        {
            for(DependenceContainer dbc : m_historyContainers)
            {
                if ((toTimestamp >= dbc.getStart()) && (fromTimestamp <= dbc.getEnd()))
                {
                    response = dbc.hasOccuredWithinTimewindow(fromTimestamp, toTimestamp);
                }
                
                if (response) break;
            }
        }
        
        return response;
    }

    public void writeOut(LoggingContext log) 
    {
        log.log("StorageContainerBased - number of history containers: " +  (m_historyContainers.size() + 1));
        m_currentContainer.writeOut(log);

        log.log("history queue...");
        for(DependenceContainer dc : m_historyContainers)
        {
            dc.writeOut(log);
        }
    }   
    
}
