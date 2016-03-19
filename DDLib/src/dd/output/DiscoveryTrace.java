
package dd.output;

import dd.Configuration;
import java.io.IOException;
import java.io.PrintWriter;

public class DiscoveryTrace 
{
    private static PrintWriter      m_trace;
    
    
    public static void writeDgEdgeIntoOutput(
            LoggingContext log, 
            int conversationId, 
            String sourceId, 
            String targetId,
            String exStatus,
            String toStatus,
            long lastEx,
            long firstTo,
            String targetReachabilityStatus,
            String targetExStatus,
            String targetToStatus,
            long targetLastEx,
            long targetFirstTo)
    {
        String      line;
        
        line = conversationId + "," +
                sourceId + "," +
                targetId + "," +
                exStatus + "," +
                toStatus + "," +
                lastEx + "," +
                firstTo + "," +
                targetReachabilityStatus + "," +
                targetExStatus + "," +
                targetToStatus + "," +
                targetLastEx + "," +
                targetFirstTo;

        writeLineIntoOutput(log, line);
    }
    
    private static synchronized void writeLineIntoOutput(LoggingContext log, String line)
    {
        if (m_trace == null)
        {
            try 
            {
                m_trace = new PrintWriter(Configuration.getDgFile(log));
                m_trace.println("conversationId,dependentUrl,antecedentUrl,exStatus,toStatus,lastEx,firstTo,targetReachabilityStatus,targetExStatus,targetToStatus,targetLastEx,targetFirstTo");
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
