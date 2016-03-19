
package dd.genericclient2;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.conversation.MessageDependenceList;
import dd.discoveryElement.DiscoveryStartingWithFrontends;
import dd.discoveryclient.DependenceDiscoverySystemWideDg;
import dd.output.LoggingContext;


public class DependenceDiscoveryClientRunner implements Runnable
{
    private int                                 m_conversationId;
    private String                              m_frontEndService;
    private java.lang.Exception                 m_conversationException;
    private long                                m_conversationStartTimestamp;
    private long                                m_conversationEndTimestamp;
    private LoggingContext                      m_log;
    
    
    public DependenceDiscoveryClientRunner(
            int conversationId,
            String frontEndService,
            java.lang.Exception conversationException,
            long lastConversationStartTimestamp,
            long lastConversationEndTimestamp)
    {
        m_log = LoggingContext.getContext(this, "" + conversationId);
        m_conversationId = conversationId;
        m_frontEndService = frontEndService;
        m_conversationException = conversationException;
        m_conversationStartTimestamp = lastConversationStartTimestamp;
        m_conversationEndTimestamp = lastConversationEndTimestamp;
    }

    @Override
    public void run() 
    {
        int             delayAfterConversation = ExperimentConfiguration.getClientDependenceDiscoveryDelayAfterConversation(m_log);
        
        
        executeDelay(delayAfterConversation);
        
        if (!ExperimentConfiguration.getDependenceDiscoveryUseThirdParty(m_log))
        {
            m_log.log("Starting discovery from local host for conversation: " + m_conversationId);
            
            runDiscoveryOfSingleConversation();
            runDiscoveryOfSystemWideDg();
        }

        /*
        while(attemptIndex <= numberOfAttempts)
        {
            m_log.log("Conversation: " + m_conversationId +  " - request for discovery sent to ip: " + getIpOfDEService() + " attempt: " + attemptIndex);
            attemptResult = runDiscoveryStartingWithFrontends();
            
            if (attemptResult)
            {
                m_log.log("Conversation: " + m_conversationId +  " - discovery success after: " + attemptIndex + " attempts");
                break;
            }
            
            attemptIndex++;
        }
        */
    }
    
    private void runDiscoveryOfSingleConversation()
    {
        DiscoveryStartingWithFrontends          dswf = new DiscoveryStartingWithFrontends(m_log);
        String                                  clientId = Network.getClientApplicationProcessId(m_log);
        
        
        if (ExperimentConfiguration.getDependenceDiscoveryRunConversationDiscovery(m_log))
        {
            dswf.runDiscoveryForSingleConversation(
                    m_conversationId, 
                    clientId,
                    m_conversationStartTimestamp, 
                    m_conversationEndTimestamp,
                    m_frontEndService,
                    m_conversationException,
                    Exception_Exception.class);
        }
    }
    
    /*
    private boolean runDiscoveryStartingWithFrontends()
    {
        DiscoveryElementServiceWrapper          desWrapper = new DiscoveryElementServiceWrapper(getIpOfDEService());
        String                                  clientId = LocalDependenceDiscoveryData.getClientApplicationProcessId();
        String[]                                frontendUrls;
    
        
        frontendUrls = LocalDependenceDiscoveryData.getHostProcessInterDependenciesForTimeWindow(
                m_conversationStartTimestamp, 
                m_conversationEndTimestamp);
    
        return desWrapper.runDiscoveryStartingWithFrontends(
                m_conversationId, 
                clientId, 
                frontendUrls, 
                m_conversationStartTimestamp, 
                m_conversationEndTimestamp);
    }
    
    private String getIpOfDEService()
    {
        String          nodeIp;
        
        
        if (ExperimentConfiguration.getInstance().getDependenceDiscoveryUseThirdParty())
        {
            nodeIp = ExperimentConfiguration.getInstance().getDependenceDiscoveryThirdPartyIp();
        }
        else
        {
            nodeIp = "127.0.0.1";
        }
        
        return nodeIp;
    }
    */
    private synchronized void executeDelay(int delay)
    {        
        m_log.log("Conversation: " + m_conversationId +  " - discovery delay after conversation: " + delay + "ms");
        
        if (delay == 0) return;
        
        try 
        {
            this.wait(delay);
        } 
        catch (java.lang.Exception ex) 
        {
            m_log.log(ex);
        }
    }
    
    public static void executeDependenceDiscovery(
            LoggingContext log,
            int conversationId,
            String frontEndService,
            java.lang.Exception conversationException,
            long lastConversationStartTimestamp,
            long lastConversationEndTimestamp)
    {
        DependenceDiscoveryClientRunner     runner = new DependenceDiscoveryClientRunner(
                conversationId,
                frontEndService,
                conversationException, 
                lastConversationStartTimestamp, 
                lastConversationEndTimestamp);
        
        if (ExperimentConfiguration.getClientDependenceDiscoveryRunInParallelToWorkload(log))
        {
            log.log("Discovery is running in parallel to workload - experiment mode");
        
            // for running on VM
            (new Thread(runner)).start();
        }
        else
        {
            log.log("Discovery is blocking workload - debug mode");
            // for debug purposes - to avoid multiple requests in paralel
            runner.run();
        }
    }
    
    private void runDiscoveryOfSystemWideDg()
    {
        long fromTimestamp = m_conversationEndTimestamp - ExperimentConfiguration.getDependenceDiscoverySystemWideTimeWindowSize(m_log);
        long toTimestamp = m_conversationEndTimestamp;
    
        
        if (ExperimentConfiguration.getDependenceDiscoveryRunSystemWideDiscovery(m_log))
        {
            DependenceDiscoverySystemWideDg.getSystemDg(
                    m_conversationId * 1000, 
                    fromTimestamp, 
                    toTimestamp);
        }
    }
}