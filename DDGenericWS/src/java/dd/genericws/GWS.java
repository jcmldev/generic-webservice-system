
package dd.genericws;

import dd.conversation.MessageDependenceList;
import dd.output.ServiceGtTrace;
import dd.output.LoggingContext;
import java.net.URL;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.AddressingFeature;

/* Generic configurable Web Service
 * Features:
 * 1) is able to be deployed as multiple applications on single server
 * 2) has configurable set of methods and dependencies 
 */

@Addressing(enabled=true, required=true, responses= AddressingFeature.Responses.ALL)
@WebService(serviceName = "GWS")
public class GWS 
{
    @Resource(name="m_wsContext") 
    WebServiceContext                   m_wsContext;
    
    @WebMethod(operationName = "invokeMethod")
    public String invokeMethod(
            @WebParam(name = "methodId") int methodId,
            @WebParam(name = "messageDepependenceList") String messageDependenceListString) throws Exception
    {
        int                     conversationId = getConversationId();
        LoggingContext          log = LoggingContext.getContext(this, "" + conversationId);
        
        
        try
        {    
            MessageDependenceList           messageDependenceList = MessageDependenceList.fromString(messageDependenceListString);
            MessageDependenceList           resultMessageDependenceList;
            String                          contextPath = getContextPath(log);
            //String                          dependentUrl = messageDependenceList.getItems().get(messageDependenceList.getItems().size() - 1).getSource();
            String                          dependentUrl = dd.conversation.Addressing.getFromUriFromHeader(m_wsContext);
            MethodInvocationTask            task;

            log.log(
                    "Request received on " + contextPath + 
                    " - requested method id: " + methodId +
                    " - conversation id: " + conversationId);

            log.log("Incoming message dependence list");
            messageDependenceList.writeOut(log);

            ServiceGtTrace.serviceReceivedRequest(
                    log,
                    contextPath,
                    conversationId,
                    dependentUrl, 
                    getEndpointAddress(), 
                    methodId);

            task = new MethodInvocationTask(
                    log,
                    contextPath, 
                    methodId, 
                    conversationId, 
                    getEndpointAddress(), 
                    messageDependenceList);

            try
            {
                resultMessageDependenceList = task.execute();
            }
            catch(Exception ex)
            {
                log.log(ex);
                throw (ex);
            }
            finally
            {
                ServiceGtTrace.serviceSentResponse(
                        log,
                        contextPath,
                        conversationId,
                        dependentUrl, 
                        getEndpointAddress(), 
                        methodId);
            }

            log.log("Outgoing message dependence list");
            messageDependenceList.writeOut(log);

            return resultMessageDependenceList.toString();
            
        }
        catch(Exception ex)
        {
            log.log(ex);
            throw(ex);
        }
    }
    
    private int getConversationId() throws Exception
    {
        LoggingContext log = LoggingContext.getContext(this, "");
        int conversationId;
        
        
        try
        {
            conversationId = dd.conversation.Addressing.getConversationIdFromHeader(m_wsContext);
        }
        catch(Exception ex)
        {
            log.log("problem with extracting conversation id from header");
            log.log(ex);
            throw ex;
        }
        
        return conversationId;
    }
    
    private String getEndpointAddress()
    {
        return (String)m_wsContext.getMessageContext().get("com.sun.xml.ws.transport.http.servlet.requestURL");
    }
    
    private String getContextPath(LoggingContext log)
    {
        String                    requestUrl;
        URL                       url = null;
        String                    path;
        String                    application;
                
        
        requestUrl = (String)m_wsContext.getMessageContext().get("com.sun.xml.ws.transport.http.servlet.requestURL");

        try 
        {
            url = new URL(requestUrl);
        } 
        catch (Exception ex) 
        {
            log.log(ex);
        }
        
        path = url.getPath();
        application = path.substring(1, path.lastIndexOf("/"));

        return application;
    }
}