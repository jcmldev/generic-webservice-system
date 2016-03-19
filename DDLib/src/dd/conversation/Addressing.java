package dd.conversation;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.developer.JAXWSProperties;
import com.sun.xml.ws.developer.WSBindingProvider;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceContext;

public class Addressing 
{
    public static final String                 ADDRESSING_CONVERSATION_ID_HEADER = "ConversationID";
    public static final String                 ADDRESSING_FROM_HEADER = "From";
    public static final String                 ADDRESSING_TO_HEADER = "To";
    public static final String                 ADDRESSING_MESSAGE_ID_HEADER = "MessageID";
    public static final String                 ADDRESSING_RELATES_TO_HEADER = "RelatesTo";
    public static final String                 ADDRESSING_NAMESPACE = "http://www.w3.org/2005/08/addressing";


    
    public static Header createHeader(String name, String value)
    {
        return Headers.create(new QName(ADDRESSING_NAMESPACE, name), value);
    }
    
    public static void setHeadersOnPort(WSBindingProvider port, int conversationId, String fromUri)
    {
        port.setOutboundHeaders(
                new Header[]{
                    createHeader(ADDRESSING_CONVERSATION_ID_HEADER, Integer.toString(conversationId)),
                    createHeader(ADDRESSING_FROM_HEADER, fromUri)}
                );
    }
    
    public static String getValueOfHeader(Message message, String headerName)
    {
        HeaderList          hl = (HeaderList) message.getHeaders();
        Header              h;
        String              value = "";
        
        
        h = hl.get(new QName(ADDRESSING_NAMESPACE, headerName), true);
        if (h != null)
        {
            value = h.getStringContent();    
        }
        
        return value;
    }

    public static String getValueOfHeader(WebServiceContext context, String headerName)
    {
        HeaderList hl = (HeaderList) context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
        Header h = hl.get(new QName(ADDRESSING_NAMESPACE, headerName), true);
        
        return h.getStringContent();
    }

    public static int getConversationIdFromHeader(WebServiceContext context)
    {
        return Integer.parseInt(getValueOfHeader(context, ADDRESSING_CONVERSATION_ID_HEADER));
    }

    public static String getFromUriFromHeader(WebServiceContext context)
    {
        return getValueOfHeader(context, ADDRESSING_FROM_HEADER);
    }
}
