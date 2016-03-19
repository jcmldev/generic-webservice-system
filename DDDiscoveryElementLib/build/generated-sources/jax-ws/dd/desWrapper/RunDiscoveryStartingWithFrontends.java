
package dd.desWrapper;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for runDiscoveryStartingWithFrontends complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="runDiscoveryStartingWithFrontends">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="conversationId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="clientId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="frontendUrls" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "runDiscoveryStartingWithFrontends", propOrder = {
    "conversationId",
    "clientId",
    "frontendUrls",
    "fromTimestamp",
    "toTimestamp"
})
public class RunDiscoveryStartingWithFrontends {

    protected int conversationId;
    protected String clientId;
    @XmlElement(nillable = true)
    protected List<String> frontendUrls;
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
     * Gets the value of the clientId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the value of the clientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientId(String value) {
        this.clientId = value;
    }

    /**
     * Gets the value of the frontendUrls property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the frontendUrls property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFrontendUrls().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFrontendUrls() {
        if (frontendUrls == null) {
            frontendUrls = new ArrayList<String>();
        }
        return this.frontendUrls;
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
