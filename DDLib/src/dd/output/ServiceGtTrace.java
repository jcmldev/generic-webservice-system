
package dd.output;

import dd.Configuration;
import dd.Timestamp;
import java.io.IOException;
import java.io.PrintWriter;

public class ServiceGtTrace 
{
    
    public enum MessageDirection
    {
        Incoming,
        Outgoing
    };
    
    public enum MessageType
    {
        Request,
        Response
    };
        
    
    private static PrintWriter      m_serviceGtOutput;
    
    
    public static void serviceReceivedRequest(
            LoggingContext log,
            String serviceName,
            int conversationId, 
            String dependentUrl, 
            String antecedentUrl, 
            int methodId)
    {
        recordServiceMessage(
                log,
                serviceName,
                Timestamp.now(),
                conversationId,
                MessageType.Request,
                MessageDirection.Incoming,
                dependentUrl,
                antecedentUrl,
                methodId);
    }

    public static void serviceSentResponse(
            LoggingContext log,
            String serviceName,
            int conversationId, 
            String dependentUrl, 
            String antecedentUrl, 
            int methodId)
    {
        recordServiceMessage(
                log,
                serviceName,
                Timestamp.now(),
                conversationId,
                MessageType.Response,
                MessageDirection.Outgoing,
                dependentUrl,
                antecedentUrl,
                methodId);
    }

    public static void serviceSentRequest(
            LoggingContext log,
            String serviceName,
            int conversationId, 
            String dependentUrl, 
            String antecedentUrl, 
            int methodId)
    {
        recordServiceMessage(
                log,
                serviceName,
                Timestamp.now(),
                conversationId,
                MessageType.Request,
                MessageDirection.Outgoing,
                dependentUrl,
                antecedentUrl,
                methodId);    
    }
    
    public static void serviceReceivedResponse(
            LoggingContext log,
            String serviceName,
            int conversationId, 
            String dependentUrl, 
            String antecedentUrl, 
            int methodId)
    {
        recordServiceMessage(
                log,
                serviceName,
                Timestamp.now(),
                conversationId,
                MessageType.Response,
                MessageDirection.Incoming,
                dependentUrl,
                antecedentUrl,
                methodId);
    }

    private static void recordServiceMessage(
            LoggingContext log,
            String serviceName,
            long timestamp, 
            int conversationId,
            MessageType type,
            MessageDirection direction,
            String dependentUrl, 
            String antecedentUrl, 
            int methodId)
    {
        writeLineIntoOutput(
                log,
                "" + timestamp + "," +
                conversationId + "," +
                (type == MessageType.Request ? "1" : "2") + "," +
                (direction == MessageDirection.Incoming ? "in" : "out") + "," +
                dependentUrl + "," +
                antecedentUrl + "," +
                methodId);
    }
    
    private static synchronized void writeLineIntoOutput(LoggingContext log, String line)
    {
        if (m_serviceGtOutput == null)
        {
            try 
            {
                m_serviceGtOutput = new PrintWriter(Configuration.getServiceGtFile(log));
                m_serviceGtOutput.println("timestamp,conversationId,type,direction,dependentUrl,antecedentUrl,methodId");
            } 
            catch (IOException ex) 
            {
                log.log(ex);
            }
        }
    
        m_serviceGtOutput.println(line);
        m_serviceGtOutput.flush();
    }
}