
package dd.output;

import dd.Configuration;
import fl.ServiceFaultGtTrace;
import java.io.IOException;
import java.io.PrintWriter;


public class ConversationTrace 
{
    private static PrintWriter      m_trace;
    
    
    public synchronized static void recordConversation(
        LoggingContext log,
        int conversationId,
        String frontEndService,
        long startTimestamp,
        long endTimestamp,
        boolean conversationFailed,
        Class classOfWSExceptionWrapper,
        Exception conversationException)
    {
        String      line;
        String      faultType = "";
        String      exceptionType = "";
        String      exceptionMessage = "";
        
        
        if (conversationFailed)
        {
            faultType = ServiceFaultGtTrace.ClientProcess_getFaultTypeFromException(
                    classOfWSExceptionWrapper, 
                    conversationException).name();
            
            exceptionType = conversationException.getClass().getName();
            exceptionMessage = conversationException.getMessage();            
        }
        
        line = conversationId + "," +
                frontEndService + "," +
                startTimestamp + "," +
                endTimestamp + "," +
                conversationFailed  + "," +
                faultType + "," +
                exceptionType + "," +
                exceptionMessage;

        writeLineIntoOutput(log, line);
    }
    
    private static void writeLineIntoOutput(LoggingContext log, String line)
    {
        if (m_trace == null)
        {
            try 
            {
                m_trace = new PrintWriter(Configuration.getConversationFile(log));
                m_trace.println("conversationId,frontEndService,startTimestamp,endTimestamp,conversationFailed,fautlType,exceptionType,exceptionMessage");
            } 
            catch (IOException ex) 
            {
                log.log(ex);
            }
        }
    
        m_trace.println(line);
        m_trace.flush();
    }
}
