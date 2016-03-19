
package dd.output;

import dd.Configuration;
import dd.Timestamp;
import java.io.IOException;
import java.io.PrintWriter;

public class WsaMessageGtTrace 
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
        
    
    private static PrintWriter      m_output;
    
    
    public static void receivedRequest(
            LoggingContext log,
            String conversationId, 
            String dependentUrl, 
            String antecedentUrl)
    {
        recordMessage(
                log,
                Timestamp.now(),
                conversationId,
                MessageType.Request,
                MessageDirection.Incoming,
                dependentUrl,
                antecedentUrl);
    }

    public static void sentResponse(
            LoggingContext log,
            String conversationId, 
            String dependentUrl, 
            String antecedentUrl)
    {
        recordMessage(
                log,
                Timestamp.now(),
                conversationId,
                MessageType.Response,
                MessageDirection.Outgoing,
                dependentUrl,
                antecedentUrl);
    }

    public static void sentRequest(
            LoggingContext log,
            String conversationId, 
            String dependentUrl, 
            String antecedentUrl)
    {
        recordMessage(
                log,
                Timestamp.now(),
                conversationId,
                MessageType.Request,
                MessageDirection.Outgoing,
                dependentUrl,
                antecedentUrl);    
    }
    
    public static void receivedResponse(
            LoggingContext log,
            String conversationId, 
            String dependentUrl, 
            String antecedentUrl)
    {
        recordMessage(
                log,
                Timestamp.now(),
                conversationId,
                MessageType.Response,
                MessageDirection.Incoming,
                dependentUrl,
                antecedentUrl);
    }

    private static void recordMessage(
            LoggingContext log,
            long timestamp, 
            String conversationId,
            MessageType type,
            MessageDirection direction,
            String dependentUrl, 
            String antecedentUrl)
    {
        writeLineIntoOutput(
                log,
                "" + timestamp + "," +
                conversationId + "," +
                (type == MessageType.Request ? "1" : "2") + "," +
                (direction == MessageDirection.Incoming ? "in" : "out") + "," +
                dependentUrl + "," +
                antecedentUrl);
    }
    
    private static synchronized void writeLineIntoOutput(LoggingContext log, String line)
    {
        if (m_output == null)
        {
            try 
            {
                m_output = new PrintWriter(Configuration.getWsaMessageGtFile(log));
                m_output.println("timestamp,conversationId,type,direction,dependentUrl,antecedentUrl");
            } 
            catch (IOException ex) 
            {
                log.log(ex);
            }
        }
    
        m_output.println(line);
        m_output.flush();
    }
}