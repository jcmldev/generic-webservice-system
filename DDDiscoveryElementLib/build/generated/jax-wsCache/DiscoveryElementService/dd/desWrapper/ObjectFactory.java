
package dd.desWrapper;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dd.desWrapper package. 
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

    private final static QName _RunDiscoveryStartingWithFrontendsResponse_QNAME = new QName("http://ws.dd/", "runDiscoveryStartingWithFrontendsResponse");
    private final static QName _RunDiscoveryStartingWithFrontends_QNAME = new QName("http://ws.dd/", "runDiscoveryStartingWithFrontends");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dd.desWrapper
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RunDiscoveryStartingWithFrontends }
     * 
     */
    public RunDiscoveryStartingWithFrontends createRunDiscoveryStartingWithFrontends() {
        return new RunDiscoveryStartingWithFrontends();
    }

    /**
     * Create an instance of {@link RunDiscoveryStartingWithFrontendsResponse }
     * 
     */
    public RunDiscoveryStartingWithFrontendsResponse createRunDiscoveryStartingWithFrontendsResponse() {
        return new RunDiscoveryStartingWithFrontendsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunDiscoveryStartingWithFrontendsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dd/", name = "runDiscoveryStartingWithFrontendsResponse")
    public JAXBElement<RunDiscoveryStartingWithFrontendsResponse> createRunDiscoveryStartingWithFrontendsResponse(RunDiscoveryStartingWithFrontendsResponse value) {
        return new JAXBElement<RunDiscoveryStartingWithFrontendsResponse>(_RunDiscoveryStartingWithFrontendsResponse_QNAME, RunDiscoveryStartingWithFrontendsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunDiscoveryStartingWithFrontends }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.dd/", name = "runDiscoveryStartingWithFrontends")
    public JAXBElement<RunDiscoveryStartingWithFrontends> createRunDiscoveryStartingWithFrontends(RunDiscoveryStartingWithFrontends value) {
        return new JAXBElement<RunDiscoveryStartingWithFrontends>(_RunDiscoveryStartingWithFrontends_QNAME, RunDiscoveryStartingWithFrontends.class, null, value);
    }

}
