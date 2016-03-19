package dd.conversation;

import dd.output.LoggingContext;

public class MessageDependenceListItem 
{
    private static final String            STRING_ELEMENT_DELIMITER = "*"; 
    
    private String                  m_source;
    private String                  m_target;
    private int                     m_methodId;
    
    
    public MessageDependenceListItem(String source, String target, int methodId)
    {
        m_source = source;
        m_target = target;
        m_methodId = methodId;
    }
    
    public String getSource()
    {
        return m_source;
    }

    public String getTarget()
    {
        return m_target;
    }

    public int getMethodId()
    {
        return m_methodId;
    }
    
    public static MessageDependenceListItem fromString(String string)
    {
        String[]                            strings;
        MessageDependenceListItem           item = null;
        
        strings = string.split("\\" + STRING_ELEMENT_DELIMITER);
        
        if (strings.length == 3)
        {
            item = new MessageDependenceListItem(
                strings[0],
                strings[1],
                Integer.parseInt(strings[2]));
        }
        
        return item;
    }
    
    @Override
    public String toString()
    {
        return m_source + STRING_ELEMENT_DELIMITER + m_target + STRING_ELEMENT_DELIMITER + m_methodId;
    }
    
    public void writeOut(LoggingContext log)
    {
        log.log("    Source: " + m_source + " Target: " + m_target + " Method: " + m_methodId);
    }   
}