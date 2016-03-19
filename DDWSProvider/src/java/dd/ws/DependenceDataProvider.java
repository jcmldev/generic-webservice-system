
package dd.ws;

import dd.Configuration;
import dd.Network;
import dd.output.LoggingContext;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.w3c.dom.Element;


@WebService(serviceName = "DependenceDataProvider")
public class DependenceDataProvider {
    
    @WebMethod(operationName = "getInterDependenciesForTimeWindow")
    public String[] getInterDependenciesForTimeWindow(
            @WebParam(name = "conversationId") int conversationId,
            @WebParam(name = "serviceId") String serviceId, 
            @WebParam(name = "fromTimestamp") long fromTimestamp, 
            @WebParam(name = "toTimestamp") long toTimestamp) throws Exception 
    {
        LoggingContext                                  log = LoggingContext.getContext(this, "" + conversationId);
        RequestForLocalDependencies                     request = new RequestForLocalDependencies(log);
        
        
        checkIfShouldDiscoveryFail(log);
        
        return request.getInterDependenciesForTimeWindow(
                serviceId, 
                fromTimestamp, 
                toTimestamp);
    }

    @WebMethod(operationName = "getInterDependenciesForTimeWindow_Distributed")
    public String[] getInterDependenciesForTimeWindow_Distributed(
            @WebParam(name = "conversationId") int conversationId,
            @WebParam(name = "serviceId") String serviceId, 
            @WebParam(name = "fromTimestamp") long fromTimestamp, 
            @WebParam(name = "toTimestamp") long toTimestamp) throws Exception
    {
        LoggingContext                                  log = LoggingContext.getContext(this, "" + conversationId);
        RequestForDistributedDependencies               request = new RequestForDistributedDependencies(log);
        
        
        checkIfShouldDiscoveryFail(log);
        
        return request.getInterDependenciesForTimeWindow(
                conversationId,
                serviceId, 
                fromTimestamp, 
                toTimestamp);
    }
    
    private void checkIfShouldDiscoveryFail(LoggingContext log) throws Exception
    {
        Element             nodeElement;
        String              attribute;
        String              message;
        
        
        nodeElement = Configuration.getNodeElement(
                log, 
                Network.getLocalHostIp(log));
        
        attribute = nodeElement.getAttribute("dependenceDataProviderRespondsWithFault");
        
        if (Boolean.parseBoolean(attribute))
        {
            message = "Dependence discovery is configured to fail - node: " + Network.getLocalHostIp(log);
            log.log(message);
            throw (new Exception(message));
        }        
    }
}
