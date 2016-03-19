
package dd.ddpwrapper;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "DependenceDataProvider", targetNamespace = "http://ws.dd/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface DependenceDataProvider {


    /**
     * 
     * @param serviceId
     * @param fromTimestamp
     * @param conversationId
     * @param toTimestamp
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod(operationName = "getInterDependenciesForTimeWindow_Distributed")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getInterDependenciesForTimeWindow_Distributed", targetNamespace = "http://ws.dd/", className = "dd.ddpwrapper.GetInterDependenciesForTimeWindowDistributed")
    @ResponseWrapper(localName = "getInterDependenciesForTimeWindow_DistributedResponse", targetNamespace = "http://ws.dd/", className = "dd.ddpwrapper.GetInterDependenciesForTimeWindowDistributedResponse")
    @Action(input = "http://ws.dd/DependenceDataProvider/getInterDependenciesForTimeWindow_DistributedRequest", output = "http://ws.dd/DependenceDataProvider/getInterDependenciesForTimeWindow_DistributedResponse")
    public List<String> getInterDependenciesForTimeWindowDistributed(
        @WebParam(name = "conversationId", targetNamespace = "")
        int conversationId,
        @WebParam(name = "serviceId", targetNamespace = "")
        String serviceId,
        @WebParam(name = "fromTimestamp", targetNamespace = "")
        long fromTimestamp,
        @WebParam(name = "toTimestamp", targetNamespace = "")
        long toTimestamp);

    /**
     * 
     * @param serviceId
     * @param fromTimestamp
     * @param conversationId
     * @param toTimestamp
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getInterDependenciesForTimeWindow", targetNamespace = "http://ws.dd/", className = "dd.ddpwrapper.GetInterDependenciesForTimeWindow")
    @ResponseWrapper(localName = "getInterDependenciesForTimeWindowResponse", targetNamespace = "http://ws.dd/", className = "dd.ddpwrapper.GetInterDependenciesForTimeWindowResponse")
    @Action(input = "http://ws.dd/DependenceDataProvider/getInterDependenciesForTimeWindowRequest", output = "http://ws.dd/DependenceDataProvider/getInterDependenciesForTimeWindowResponse")
    public List<String> getInterDependenciesForTimeWindow(
        @WebParam(name = "conversationId", targetNamespace = "")
        int conversationId,
        @WebParam(name = "serviceId", targetNamespace = "")
        String serviceId,
        @WebParam(name = "fromTimestamp", targetNamespace = "")
        long fromTimestamp,
        @WebParam(name = "toTimestamp", targetNamespace = "")
        long toTimestamp);

}
