
package dd.monitor.dependencestorage;

import dd.output.LoggingContext;
import java.util.LinkedList;
import java.util.Queue;


public class BackupContainer extends DependenceContainer
{
    private Queue<BitArrayBasedContainer>       m_containers = new LinkedList<>();

    
    public BackupContainer() 
    {
        super(0, 0);
    }
    
    public void addBackupContainer(BitArrayBasedContainer container)
    {
        m_containers.add(container);
    }
    
    @Override
    public void addOccurrence(long timestamp) 
    {
        throw new UnsupportedOperationException("Not intendet to be used.");
    }

    @Override
    public boolean hasOccuredWithinTimewindow(long fromTimestamp, long toTimestamp) {
        boolean response = false;

        
        for(BitArrayBasedContainer c : m_containers)
        {
            if ((toTimestamp >= c.getStart()) && (fromTimestamp <= c.getEnd()))
            {
                response = c.hasOccuredWithinTimewindow(fromTimestamp, toTimestamp);
            }

            if (response) break;
        }

        return response;
    }

    @Override
    public void writeOut(LoggingContext log) 
    {
    
    }
    
}
