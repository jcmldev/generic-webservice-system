
package dd.monitor.dependencestorage;


public abstract class DependenceContainerFactory 
{
    public abstract DependenceContainer createContainer(long containerStart, long containerPeriod);
}
