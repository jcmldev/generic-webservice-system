
package dd.output;

import dd.Configuration;
import fl.messagemonitor.SymptomMonitor;
import java.io.IOException;
import java.io.PrintWriter;

public class WsaSymptomGtTrace 
{
    private static PrintWriter      m_output;
    
    
    
    public static void recordFault(
            LoggingContext log,
            SymptomMonitor.ProcessingFlowDirection flowDirection,
            String conversationId,
            String source, 
            String target, 
            SymptomMonitor.SymptomType symptomType,
            long timestamp)
    {
        
        writeLineIntoOutput(
                log,
                "" + timestamp + "," +
                conversationId + "," +
                symptomType.toString() + "," +
                flowDirection.toString() + "," +
                source + "," +
                target);   
    }
    
    private static synchronized void writeLineIntoOutput(LoggingContext log, String line)
    {
        if (m_output == null)
        {
            try 
            {
                m_output = new PrintWriter(Configuration.getWsaSymptomGtFile(log));
                m_output.println("timestamp,conversationId,type,flowDirection,dependentUrl,antecedentUrl");
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