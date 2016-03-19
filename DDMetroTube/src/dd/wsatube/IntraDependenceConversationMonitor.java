
package dd.wsatube;

import java.util.HashMap;
import java.util.Map;


/*
 * This is simple and prototype mechanims to keep track of conversations as they pass throught service
 * - it just keeps track of conversation id and in requests (including the target service - there might be more than one service in process)
 * - it does not release memory - just a simulation prototype
 */

public class IntraDependenceConversationMonitor 
{
    private static Map<String, String>         m_conversationSources = new HashMap<>();
    
    
    public static void recordIntraDependenceSource(WsaMessageFieldReader incomingRequest)
    {
        String key = incomingRequest.getConversationId() + incomingRequest.getTo();
        
        m_conversationSources.put(key, incomingRequest.getFrom());
    }
    
    public static String getIntraDependenceSource(WsaMessageFieldReader outboundMessage)
    {
        String key = outboundMessage.getConversationId() + outboundMessage.getFrom();

        
        if(m_conversationSources.containsKey(key))
        {
            return m_conversationSources.get(key);
        }
        else
        {
            return null;
        }
    }
    
}
