
package dd.ddpwrapper;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dd.ddpwrapper package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetInterDependenciesForTimeWindowDistributedResponse_QNAME = new QName("http://ws.dd/", "getInterDependenciesForTimeWindow_DistributedResponse");
    private final static QName _GetInterDependenciesForTimeWindowResponse_QNAME = new QName("http://ws.dd/", "getInterDependenciesForTimeWindowResponse");
    private final static QName _GetInterDependenciesForTimeWindow_QNAME = new QName("http://ws.dd/", "getInterDependenciesForTimeWindow");
    private final static QName _GetInterDependenciesForTimeWindowDistributed_QNAME = new QName("http://ws.dd/", "getInterDependenciesForTimeWindow_Distributed");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dd.ddpwrapper
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetInterDependenciesForTimeWindow }
     * 
     */
    public GetInterDependenciesForTimeWindow createGetInterDependenciesForTimeWindow() {
        return new GetInterDependenciesForTimeWindow();
    }

    /**
     * Create an instance of {@link GetInterDependenciesForTimeWindowDistributed }
     * 
     */
    public GetInterDependenciesForTimeWindowDistributed createGetInterDependenciesForTimeWindowDistributed() {
        return new GetInterDependenciesForTimeWindowDistributed();
    }

    /**
     * Create an instance of {@link GetInterDependenciesForTimeWindowDistributedResponse }
     * 
     */
    public GetInterDependenciesForTimeWindowDistributedResponse createGetInterDependenciesForTimeWindowDistributedResponse() {
        return new GetInterDependenciesForTimeWindowDistributedResponse();
    }

    /**
     * Create an instance of {@link GetInterDependenciesForTimeWindowResponse }
     * 
     */
    public GetInterDependenciesForTimeWindowResponse createGetInterDependenciesForTimeWindowResponse() {
        return new GetInterDependenciesForTimeWindowResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInterDependenciesForTimeWindowDistributedResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dd/", name = "getInterDependenciesForTimeWindow_DistributedResponse")
    public JAXBElement<GetInterDependenciesForTimeWindowDistributedResponse> createGetInterDependenciesForTimeWindowDistributedResponse(GetInterDependenciesForTimeWindowDistributedResponse value) {
        return new JAXBElement<GetInterDependenciesForTimeWindowDistributedResponse>(_GetInterDependenciesForTimeWindowDistributedResponse_QNAME, GetInterDependenciesForTimeWindowDistributedResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInterDependenciesForTimeWindowResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dd/", name = "getInterDependenciesForTimeWindowResponse")
    public JAXBElement<GetInterDependenciesForTimeWindowResponse> createGetInterDependenciesForTimeWindowResponse(GetInterDependenciesForTimeWindowResponse value) {
        return new JAXBElement<GetInterDependenciesForTimeWindowResponse>(_GetInterDependenciesForTimeWindowResponse_QNAME, GetInterDependenciesForTimeWindowResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInterDependenciesForTimeWindow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dd/", name = "getInterDependenciesForTimeWindow")
    public JAXBElement<GetInterDependenciesForTimeWindow> createGetInterDependenciesForTimeWindow(GetInterDependenciesForTimeWindow value) {
        return new JAXBElement<GetInterDependenciesForTimeWindow>(_GetInterDependenciesForTimeWindow_QNAME, GetInterDependenciesForTimeWindow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetInterDependenciesForTimeWindowDistributed }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dd/", name = "getInterDependenciesForTimeWindow_Distributed")
    public JAXBElement<GetInterDependenciesForTimeWindowDistributed> createGetInterDependenciesForTimeWindowDistributed(GetInterDependenciesForTimeWindowDistributed value) {
        return new JAXBElement<GetInterDependenciesForTimeWindowDistributed>(_GetInterDependenciesForTimeWindowDistributed_QNAME, GetInterDependenciesForTimeWindowDistributed.class, null, value);
    }

}
