
package dd.monitor.dependencestorage;


public class BloomFilterBasedContainerFactory extends DependenceContainerFactory
{
    private int                     m_timeSlotSize;
    private double                  m_falsePositiveProbability;
    
    
    public BloomFilterBasedContainerFactory(int timeSlotSize, double falsePositiveProbability)
    {
        m_timeSlotSize = timeSlotSize;
        m_falsePositiveProbability = falsePositiveProbability;
    }

    @Override
    public DependenceContainer createContainer(long containerStart, long containerPeriod) {
        return new BloomFilterBasedContainer(
                containerStart, 
                containerPeriod, 
                m_timeSlotSize, 
                m_falsePositiveProbability);
    }
}
