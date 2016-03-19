
package dd.monitor.dependencestorage;

public class BitArrayBasedContainerFactory extends DependenceContainerFactory
{
    private int             m_timeSlotSize;
    
    
    public BitArrayBasedContainerFactory(int timeSlotSize)
    {
        m_timeSlotSize = timeSlotSize;
    }

    @Override
    public DependenceContainer createContainer(long containerStart, long containerPeriod) 
    {
        return new BitArrayBasedContainer(containerStart, containerPeriod, m_timeSlotSize);
    }
}
