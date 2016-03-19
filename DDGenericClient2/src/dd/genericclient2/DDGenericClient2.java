package dd.genericclient2;

import dd.ClientStartSignal;
import dd.ExperimentConfiguration;
import dd.RuntimeHelper;
import dd.genericclient2.config.ClientConfiguration;
import dd.output.LoggingContext;
import java.util.Date;


public class DDGenericClient2 {
    
    private static LoggingContext              m_log = LoggingContext.getContext(new DDGenericClient2(), "");
    
    // !!! before running the system make sure that the configuration of http threads in 
    // !!! glassfish is increased from default 5 to higher number i.e. 50
    
    /*
     How it works ...
     1) selects randomly one of target interfaces and sends request
     2) after receiving response, waits random period before sending another request
     3) after pre configured number of requests sends request for dependencies
     */
    
    public static void main(String[] args) 
    {          
        ClientTask          clientTask;
                
     /*
      * DEBUG !!!!!!!!!!!!!!
      * 1) in class DependenceDiscoveryClientRunner method executeDependenceDiscovery 
      *         - set the execution to single threaded
      * 2) in LocalDependenceDiscoveryData method getHostProcessInterDependenciesForTimeWindow 
      *         - allow fixed loading of outgoing root dependencies - in debug it does not work as in runtime
      * 
      */
        
        try
        {
            if (shouldClientStart())
            {
                clientTask = new ClientTask();
                
                ClientStartSignal.waitForSignalToStart(m_log);
                runInitialDelay();
                clientTask.execute();
            }
            else
            {
                m_log.log("Client's configuration was not found - client is not starting");
            }
        }
        catch(java.lang.Exception ex)
        {
            m_log.log("Fatal exception in client ...");
            m_log.log(ex);
        }
    }
    
    private static void runInitialDelay()
    {
        int                 workloadDelay;
        
        
        workloadDelay = ExperimentConfiguration.getClientWorloadStartDelayDistributionStart(m_log);
        
        workloadDelay += 
                ((ExperimentConfiguration.getClientWorloadStartDelayDistributionEnd(m_log) - 
                    ExperimentConfiguration.getClientWorloadStartDelayDistributionStart(m_log)) *
                Math.random());
        
        m_log.log("DD Generic client is starting with workload delay: " + workloadDelay + "ms");
        
        RuntimeHelper.waitDelay(m_log, workloadDelay);
    }
    
    private static boolean shouldClientStart()
    {
        boolean         isThereClientsConfiguration;
        
        
        try
        {
            new ClientConfiguration(m_log);
            isThereClientsConfiguration = true;
        }
        catch(java.lang.Exception ex)
        {
            isThereClientsConfiguration = false;
        }
        
        return isThereClientsConfiguration;
    }
}
