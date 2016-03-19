
package dd.genericclient2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dd.genericclient2 package. 
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

    private final static QName _InvokeMethod_QNAME = new QName("http://genericws.dd/", "invokeMethod");
    private final static QName _InvokeMethodResponse_QNAME = new QName("http://genericws.dd/", "invokeMethodResponse");
    private final static QName _Exception_QNAME = new QName("http://genericws.dd/", "Exception");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dd.genericclient2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link InvokeMethod }
     * 
     */
    public InvokeMethod createInvokeMethod() {
        return new InvokeMethod();
    }

    /**
     * Create an instance of {@link InvokeMethodResponse }
     * 
     */
    public InvokeMethodResponse createInvokeMethodResponse() {
        return new InvokeMethodResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvokeMethod }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://genericws.dd/", name = "invokeMethod")
    public JAXBElement<InvokeMethod> createInvokeMethod(InvokeMethod value) {
        return new JAXBElement<InvokeMethod>(_InvokeMethod_QNAME, InvokeMethod.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvokeMethodResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://genericws.dd/", name = "invokeMethodResponse")
    public JAXBElement<InvokeMethodResponse> createInvokeMethodResponse(InvokeMethodResponse value) {
        return new JAXBElement<InvokeMethodResponse>(_InvokeMethodResponse_QNAME, InvokeMethodResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://genericws.dd/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

}
