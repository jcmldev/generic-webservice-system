
package fl.serverlog;

import fl.ServiceFaultGtTrace;
import java.util.Date;

public class LogEntry 
{
    public static final String                  ATTRIBUTES_DELIMITER = "<=>";
    public static final String                  LOG_ENTRY_PREFIX = "ddfl.";
    
    private Date                                m_timestamp;
    private String                              m_source;
    private String                              m_target;
    private ServiceFaultGtTrace.FaultType       m_faultType;
    
    
        
    public LogEntry(Date timestamp, String faultType, String attributes)
    {
        String[]        attrs = getAttributesFromString(attributes);
        
        
        m_timestamp = timestamp;
        
        faultType = faultType.substring(LOG_ENTRY_PREFIX.length());
        m_faultType = ServiceFaultGtTrace.faultTypeFromString(faultType);

        m_source = attrs[0];
        m_target = (attrs.length > 1) ? attrs[1] : "";
    }

    public LogEntry(Date timestamp, ServiceFaultGtTrace.FaultType faultType, String source, String target)
    {
        m_timestamp = timestamp;
        m_source = source;
        m_target = target;
        m_faultType = faultType;
    }

    public Date getTimestamp()
    {
        return m_timestamp;
    }
    
    public ServiceFaultGtTrace.FaultType getFaultType() 
    {
        return m_faultType;
    }

    public static boolean isRecordClassLogEntry(String className)
    {
        return className.startsWith(LOG_ENTRY_PREFIX);
    }
    
    public String getRecordClassTypeName() 
    {
        return LOG_ENTRY_PREFIX + m_faultType.name();
    }
        
    public String getSource()
    {
        return m_source;
    }
    
    public String getTarget()
    {
        return m_target;
    }

    public String getContent() 
    {
        return attributesToString(new String[]{m_source, m_target});
    }
    
    private String[] getAttributesFromString(String attributes)
    {
        return attributes.split(ATTRIBUTES_DELIMITER);
    }

    private String attributesToString(String[] attributes)
    {
        String str = "";
        
        
        for(String attr : attributes)
        {
            str += attr + ATTRIBUTES_DELIMITER;
        }
        
        str = str.substring(0, str.length() - 1 - ATTRIBUTES_DELIMITER.length());
            
        return str;
    }
}
