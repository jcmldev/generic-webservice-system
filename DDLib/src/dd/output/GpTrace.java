
package dd.output;

import dd.Configuration;
import java.io.IOException;
import java.io.PrintWriter;


public class GpTrace 
{
    private static PrintWriter      m_trace;

        
    public static void writeSendIntoOutput(
            LoggingContext log, 
            int cycleIndex, 
            String targetIp,
            long sendTimestamp, 
            long sendEndTimestamp,
            boolean sendSuccess,
            String faultType,
            String faultMessage,
            int payloadSize)
    {
        String      line;
        
        line = cycleIndex + "," +
                targetIp + "," +
                sendTimestamp + "," +
                sendEndTimestamp + "," +
                sendSuccess + "," +
                faultType + "," +
                faultMessage + "," +
                payloadSize;

        writeLineIntoOutput(log, line);
    }
    
    private static synchronized void writeLineIntoOutput(LoggingContext log, String line)
    {
        if (m_trace == null)
        {
            try 
            {
                m_trace = new PrintWriter(Configuration.getGpFile(log));
                m_trace.println("cycleIndex,targetIp,sendTimestamp,sendEndTimestamp,sendSuccess,faultType,faultMessage,payloadSize");
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
