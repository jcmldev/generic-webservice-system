
package dd.output;

import dd.Configuration;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;


/*
 * Because logging provided by glassfish is impossible to use for more than single line output
 * Thus this logger writes log output in context of requests and does not contain the additional glassfish text
 * This means that every record first contains information to which conversation it belongs to and than its own body
 * Every service can have its own log such that the log files are create per service and ip address in the root folder GF
 */

public class LoggingContext 
{
    private static Map<String, PrintWriter>         m_outputs = new TreeMap<>();
    
    private String                                  m_serviceName;
    private String                                  m_linePrefix;
    private boolean                                 m_writeAlsoIntoSystemOut;
    
    
    protected LoggingContext(String serviceName,  String linePrefix, boolean writeAlsoIntoSystemOut)
    {
        if (linePrefix == null)
        {
            linePrefix = "";
        }
        
        m_serviceName = serviceName;
        m_linePrefix = linePrefix;
        m_writeAlsoIntoSystemOut = writeAlsoIntoSystemOut;
    }
    
    public static LoggingContext getContext(Object o, String linePrefix)
    {
        return new LoggingContext(
                o.getClass().getSimpleName(), 
                linePrefix,
                true);
    }
    
    public static LoggingContext getContext(Object o)
    {
        return new LoggingContext(
                o.getClass().getSimpleName(), 
                "",
                true);
    }
    
    private PrintWriter getOutput()
    {
        PrintWriter     output;
        
        
        output = m_outputs.get(m_serviceName);
        
        if (output == null)
        {
            output = getNewOutput();
        }
        
        return output;
    }

    // this is to avoid race condition!!!
    private synchronized PrintWriter getNewOutput()
    {
        PrintWriter     output;
        
        
        output = m_outputs.get(m_serviceName);
        
        if (output == null)
        {
            try
            {
                output = new PrintWriter(Configuration.getServiceLogFile(m_serviceName));
            }
            catch(Exception ex)
            {
                System.out.println("LoggingContext - Cant open file for output of log");
                ex.printStackTrace(System.out);
            }
            
            m_outputs.put(m_serviceName, output);
        }
        
        return output;
    }
    
    public void log(String line)
    {
        Date        now = new Date();
        String      timePrefix = now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds() + " - ";
        PrintWriter output = getOutput();
        
        
        line = timePrefix + m_linePrefix + " " + line;
        
        output.println(line);
        output.flush();
        
        if (m_writeAlsoIntoSystemOut)
        {
            System.out.println(line);
        }
    }
    
    public void log(String attributeName, String attributeValue)
    {
        attributeValue = attributeValue == null ? "" : attributeValue;
        log(attributeName + ": " + attributeValue);
    }

    public void log(String attributeName, long attributeValue)
    {
        log(attributeName + ": " + attributeValue);
    }

    public void log(String headline, String[] lines)
    {
        log(headline, lines, false);
    }
    
    public void log(String headline, String[] lines, boolean writeHeadlineOnly)
    {
        int                 arraySize = (lines == null? 0 : lines.length);
        PrintWriter         output = getOutput();
        String              array;
        

        if (headline != null)
        {
            log(headline + " (array size=" + arraySize + ")");
        }
        
        if (!writeHeadlineOnly)
        {
            array = arrayToString(lines);
            output.print(array);

            if (m_writeAlsoIntoSystemOut)
            {
                System.out.println(array);
            }

            output.flush();
        }
    }
    
    private String arrayToString(String[] lines)
    {
        String              eol = System.getProperty( "line.separator" );
        StringBuilder       sb = new StringBuilder();
        
        
        for (String line : lines)
        {
            line = m_linePrefix + "     - " + line;
            sb.append(line);
            sb.append(eol);
        }
        
        return sb.toString();
    }

    public void log(Exception ex)
    {
        log(ex, true);
    }
    
    public void log(Exception ex, boolean printStack)
    {
        PrintWriter         output = getOutput();
        
        
        log("Exception: " + ex.getMessage());
        
        if (printStack)
        {
            ex.printStackTrace(output);
        }
        
        if (m_writeAlsoIntoSystemOut)
        {
            ex.printStackTrace(System.out);
        }
        
        output.flush();
    }
}
