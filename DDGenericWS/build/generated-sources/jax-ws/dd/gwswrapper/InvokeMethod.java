
package dd.gwswrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for invokeMethod complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="invokeMethod">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="methodId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="messageDepependenceList" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "invokeMethod", propOrder = {
    "methodId",
    "messageDepependenceList"
})
public class InvokeMethod {

    protected int methodId;
    protected String messageDepependenceList;

    /**
     * Gets the value of the methodId property.
     * 
     */
    public int getMethodId() {
        return methodId;
    }

    /**
     * Sets the value of the methodId property.
     * 
     */
    public void setMethodId(int value) {
        this.methodId = value;
    }

    /**
     * Gets the value of the messageDepependenceList property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageDepependenceList() {
        return messageDepependenceList;
    }

    /**
     * Sets the value of the messageDepependenceList property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageDepependenceList(String value) {
        this.messageDepependenceList = value;
    }

}
