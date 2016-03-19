
package dd.monitor.dependencestorage;


public class TimestampBasedContainerFactory extends DependenceContainerFactory
{
    @Override
    public DependenceContainer createContainer(long containerStart, long containerPeriod) 
    {
        return new TimestampBasedContainer(containerStart, containerPeriod);
    }   
}
