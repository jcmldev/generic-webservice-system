package dd.genericclient2;

import dd.Network;
import dd.Timestamp;
import dd.conversation.MessageDependenceList;
import dd.genericclient2.config.ClientConfiguration;
import dd.genericclient2.config.ClientStep;
import dd.output.ConversationTrace;
import dd.output.LoggingContext;
import java.util.Random;

public class ClientTask 
{
    private LoggingContext              m_log = LoggingContext.getContext(this, "");
    private ClientConfiguration         m_clientConfiguration = new ClientConfiguration(m_log);
    private int                         m_conversationIdIndex;
    private String                      m_lastConversationFrontEndService;
    private long                        m_lastConversationStartTimestamp;
    private long                        m_lastConversationEndTimestamp;
    private java.lang.Exception         m_lastConversationException;

    
    public ClientTask()
    {
        m_conversationIdIndex = m_clientConfiguration.getConversationIdSeed();
    }
 
    public void execute() {
    
        int                             counter = 0;
        boolean                         runDiscovery;
                
        
        m_log.log("DDGenericClient started ... ");
        
        while(true)
        {
            counter++;
            m_conversationIdIndex++;
            
            m_log.log("Client step: " + counter + " - conversation id: " + m_conversationIdIndex + "...................");
            
            executeStep();
            
            runDiscovery = ((counter % m_clientConfiguration.getDependenceDiscoveryFrequency()) == 0);
            
            if (runDiscovery)
            {
                executeDependenceDiscovery();
            }
            
            executeDelay(
                m_clientConfiguration.getStepDelayDistributionStart(), 
                m_clientConfiguration.getStepDelayDistributionEnd());
        }
    }
    
    private void executeDependenceDiscovery()
    {
        try
        {
            m_log.log("Starting dependence discovery for conversation: " + m_conversationIdIndex);
            
            DependenceDiscoveryClientRunner.executeDependenceDiscovery(
                    m_log,
                    m_conversationIdIndex,
                    m_lastConversationFrontEndService,
                    m_lastConversationException,
                    m_lastConversationStartTimestamp, 
                    m_lastConversationEndTimestamp);
        }
        catch(java.lang.Exception ex)
        {
            m_log.log("ClientTask.executeDependenceDiscovery");
            m_log.log(ex);
            ex.printStackTrace();
        }

    }
    
    private MessageDependenceList executeStep()
    {
        int                     numberOfSteps = m_clientConfiguration.getSteps().size();
        Random                  r = new Random();
        int                     stepToExecute = r.nextInt(numberOfSteps);
        ClientStep              step = m_clientConfiguration.getSteps().get(stepToExecute);
        MessageDependenceList   messageDependenceList;
        
        
        m_lastConversationStartTimestamp = Timestamp.now();

        messageDependenceList = startConversation(step);
        
        m_lastConversationEndTimestamp = Timestamp.now();
        
        m_log.log("Conversation duration: " + 
                (m_lastConversationEndTimestamp - m_lastConversationStartTimestamp) + 
                "ms");
        
        ConversationTrace.recordConversation(
                    m_log,
                    m_conversationIdIndex,
                    m_lastConversationFrontEndService,
                    m_lastConversationStartTimestamp, 
                    m_lastConversationEndTimestamp,
                    (messageDependenceList == null),
                    Exception_Exception.class,
                    m_lastConversationException);
        
        return messageDependenceList;
    }
    
    private MessageDependenceList startConversation(ClientStep step)
    {
        //GWSWrapper                  targetService;
        GWSWrapperForMobileNetwork  targetService;
        MessageDependenceList       messageDependenceList = new MessageDependenceList();
        String                      sourceId = Network.getClientApplicationProcessId(m_log);
        
        
        m_lastConversationException = null;
        
        try
        {            
            /*
            targetService = new GWSWrapper(
                    m_log,
                    step.getTargetService());
*/
            targetService = new GWSWrapperForMobileNetwork(
                m_log,
                step.getTargetService());

            m_lastConversationFrontEndService = targetService.getTargetServiceUrl(); 
                
            messageDependenceList = targetService.invokeMethod(
                    step.getTargetMethod(), 
                    m_conversationIdIndex, 
                    sourceId,
                    messageDependenceList);
        }
        catch(java.lang.Exception ex)
        {
            m_log.log("Conversation id: " + m_conversationIdIndex + " - ended with exception: " + ex.getMessage());
            m_log.log(ex);
            m_lastConversationException = ex;
            return null;
        }
        
        messageDependenceList.writeOut(m_log);
        
        //dd.monitor.ServerMonitor.getInstance().writeOut();
        
        return messageDependenceList;
    }
    
    private synchronized void executeDelay(int delayDistributionStart, int delayDistributionEnd)
    {
        int delay;
        
        
        if (delayDistributionEnd == 0) return;
        
        delay = getRandomDelay(delayDistributionStart, delayDistributionEnd);
        
        
        m_log.log("Client workload delay: " + delay + "ms");
        
        try 
        {
            this.wait(delay);
        } 
        catch (java.lang.Exception ex) 
        {
            m_log.log(ex);
        }
    }
    
    private int getRandomDelay(int delayDistributionStart, int delayDistributionEnd)
    {
        Random      r = new Random();
        int         delay = r.nextInt((delayDistributionEnd - delayDistributionStart) + 1);
    
        
        delay += delayDistributionStart;
        
        return delay;
    }
}