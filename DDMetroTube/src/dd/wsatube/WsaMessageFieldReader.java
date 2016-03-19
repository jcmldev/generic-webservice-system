
package dd.wsatube;

import com.sun.grizzly.http.HttpWorkerThread;
import com.sun.grizzly.http.ProcessorTask;
import com.sun.grizzly.tcp.ActionCode;
import com.sun.grizzly.tcp.Request;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.message.FaultMessage;
import dd.conversation.Addressing;
import dd.output.LoggingContext;


public class WsaMessageFieldReader 
{
    private Message         m_message;
    
    
    public WsaMessageFieldReader(Packet packet)
    {
        m_message = packet.getMessage();
    }
    
    public String getFrom()
    {
        return Addressing.getValueOfHeader(m_message, Addressing.ADDRESSING_FROM_HEADER);
    }

    public String getTo()
    {
        return Addressing.getValueOfHeader(m_message, Addressing.ADDRESSING_TO_HEADER);
    }
    
    public String getConversationId()
    {
        return Addressing.getValueOfHeader(m_message, Addressing.ADDRESSING_CONVERSATION_ID_HEADER);
    }
    
    public String getMessageId()
    {
        return Addressing.getValueOfHeader(m_message, Addressing.ADDRESSING_MESSAGE_ID_HEADER);
    }
    
    public String getRelatesToId()
    {
        return Addressing.getValueOfHeader(m_message, Addressing.ADDRESSING_RELATES_TO_HEADER);
    }
    
    public boolean isMessageWsa()
    {
        return (getMessageId().length() > 0);
    }
    
    public boolean isMessageTransitiveFault()
    {
        return (m_message instanceof FaultMessage);
    }
        
    public static String getIncomingRequestMessageSourceHostIp(LoggingContext log)
    {
        
        HttpWorkerThread        pHttpWorkerThread;
        ProcessorTask           pProcessorTask;
        Request                 pRequest;
        String                  host = null;


        try
        {   
            pHttpWorkerThread = (HttpWorkerThread) Thread.currentThread();
            pProcessorTask = pHttpWorkerThread.getProcessorTask();
            pRequest = pProcessorTask.getRequest();
            
            pRequest.action(ActionCode.ACTION_REQ_HOST_ADDR_ATTRIBUTE, null); 
            host = pRequest.remoteAddr().toString();
        }
        catch(Exception e)
        {
            log.log("getIncomingRequestMessageSourceHostIp");
            log.log(e);        
        }
               
        return host;
    }

}
