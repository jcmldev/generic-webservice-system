
package dd.ws;

import dd.output.LoggingContext;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


@WebService(serviceName = "DiscoveryElementService")
public class DiscoveryElementService {

    /*
     * This method requires front end dependencies as input and than sends requests to nodes of those front ends
     * (unlike alternative aproach which would be to send request to the node of front end and start from there)
     * 
     */
    @WebMethod(operationName = "runDiscoveryStartingWithFrontends")
    public void runDiscoveryStartingWithFrontends(
            @WebParam(name = "conversationId") int conversationId,
            @WebParam(name = "clientId") String clientId,
            @WebParam(name = "frontendUrls") String[] frontendUrls,
            @WebParam(name = "fromTimestamp") long fromTimestamp, 
            @WebParam(name = "toTimestamp") long toTimestamp)
    {
        LoggingContext                                          log = LoggingContext.getContext(this, "" + conversationId);
        RequestForDiscoveryStartingWithFrontends                request = new RequestForDiscoveryStartingWithFrontends(log);

        
        log.log("runDiscoveryStartingWithFrontends");
        
        try
        {
            request.runDiscovery(
                    conversationId,
                    clientId, 
                    frontendUrls, 
                    fromTimestamp, 
                    toTimestamp);        
        }
        catch(Exception ex)
        {
           log.log(ex);
        }
    }
}
