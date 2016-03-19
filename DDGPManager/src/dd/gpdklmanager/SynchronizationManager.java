
package dd.gpdklmanager;

import dd.ExperimentConfiguration;
import dd.output.LoggingContext;


public abstract class SynchronizationManager implements Runnable
{
    protected LoggingContext          m_log;
    protected int                     m_cycleIndex;
    
    
    public SynchronizationManager(LoggingContext log, int cycleIndex)
    {
        m_log = log;
        m_cycleIndex = cycleIndex;
    }
    
    protected abstract void runSynchronization();
    
    public static void runSynchronization(SynchronizationManager manager)
    {
        
        
        if (ExperimentConfiguration.getUseSingleMachineDebugCode(null))
        {
            //log.log("DEBUG - !!! Manager is running in thread of main synchronization !!!");
            manager.run();
        }
        else
        {
            (new Thread(manager)).start();
        }
    }

    @Override
    public void run() {
        runSynchronization();
    }
    
}
