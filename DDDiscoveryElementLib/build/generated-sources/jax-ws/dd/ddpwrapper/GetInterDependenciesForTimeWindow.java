
package dd.ddpwrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getInterDependenciesForTimeWindow complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getInterDependenciesForTimeWindow">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="conversationId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fromTimestamp" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="toTimestamp" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getInterDependenciesForTimeWindow", propOrder = {
    "conversationId",
    "serviceId",
    "fromTimestamp",
    "toTimestamp"
})
public class GetInterDependenciesForTimeWindow {

    protected int conversationId;
    protected String serviceId;
    protected long fromTimestamp;
    protected long toTimestamp;

    /**
     * Gets the value of the conversationId property.
     * 
     */
    public int getConversationId() {
        return conversationId;
    }

    /**
     * Sets the value of the conversationId property.
     * 
     */
    public void setConversationId(int value) {
        this.conversationId = value;
    }

    /**
     * Gets the value of the serviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    /**
     * Gets the value of the fromTimestamp property.
     * 
     */
    public long getFromTimestamp() {
        return fromTimestamp;
    }

    /**
     * Sets the value of the fromTimestamp property.
     * 
     */
    public void setFromTimestamp(long value) {
        this.fromTimestamp = value;
    }

    /**
     * Gets the value of the toTimestamp property.
     * 
     */
    public long getToTimestamp() {
        return toTimestamp;
    }

    /**
     * Sets the value of the toTimestamp property.
     * 
     */
    public void setToTimestamp(long value) {
        this.toTimestamp = value;
    }

}
