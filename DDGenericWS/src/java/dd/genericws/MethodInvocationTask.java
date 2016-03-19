package dd.genericws;

import dd.conversation.MessageDependenceList;
import dd.genericws.config.Method;
import dd.genericws.config.MethodStep;
import dd.genericws.config.WebServiceConfiguration;
import dd.output.LoggingContext;
import java.util.Random;

public class MethodInvocationTask 
{
    private LoggingContext              m_log;
    private Method                      m_method;
    private int                         m_conversationId;
    private String                      m_endpointAddress;
    private String                      m_serviceName;
    private MessageDependenceList       m_currentMessageDependenceList;
 
    
    MethodInvocationTask(
            LoggingContext log,
            String contextPath, 
            int methodId, 
            int conversationId, 
            String endpointAddress,
            MessageDependenceList messageDependenceList) 
    {
        m_log = log;
        WebServiceConfiguration.loadConfiguration(m_log, contextPath);
        m_method = WebServiceConfiguration.getInstance().getMethods().getMethod(methodId);
        m_conversationId = conversationId;
        m_endpointAddress = endpointAddress;
        m_serviceName = contextPath;
        m_currentMessageDependenceList = messageDependenceList;
    }
    
    public MessageDependenceList execute() throws Exception
    {
        executePlan();
        
        return m_currentMessageDependenceList;
    }    
    
    private void executePlan() throws Exception
    {
        executeDelay(
                m_method.getPlan().getStartDelayDistributionStart(), 
                m_method.getPlan().getStartDelayDistributionEnd());

        executeSteps();
        
        executeDelay(
                m_method.getPlan().getEndDelayDistributionStart(), 
                m_method.getPlan().getEndDelayDistributionEnd());
    }
    
    private void executeSteps() throws Exception
    {
        MethodStep[]            steps = m_method.getPlan().getSteps();
        
        
        for(MethodStep step: steps)
        {
            executeStep(step);
        }
    }
    
    private void executeStep(MethodStep step) throws Exception
    {
        //TargetServiceWrapper targetService = new TargetServiceWrapper(m_log);
        GWSWrapperForMobileNetwork targetService = new GWSWrapperForMobileNetwork(m_log);
        
        m_currentMessageDependenceList = targetService.invokeMethod(
                m_serviceName,
                step.getTargetServiceName(),
                step.getTargetMethod(), 
                m_conversationId,
                m_endpointAddress,
                m_currentMessageDependenceList);

        executeDelay(
                step.getEndDelayDistributionStart(), 
                step.getEndDelayDistributionEnd());
    }
    
    private synchronized void executeDelay(int delayDistributionStart, int delayDistributionEnd)
    {
        int delay;
        
        
        if (delayDistributionEnd == 0) return;
        
        delay = getRandomDelay(delayDistributionStart, delayDistributionEnd);
        
        try 
        {
            this.wait(delay);
        } 
        catch (Exception ex) 
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