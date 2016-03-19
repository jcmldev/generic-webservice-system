
package dd.bck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Monitor_recordClientConversationFault complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Monitor_recordClientConversationFault">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="conversationId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="clientProcessId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="frontEndServiceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="faultTimestamp" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="faultType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exceptionType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exceptionMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exceptionCauseType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="exceptionCauseMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Monitor_recordClientConversationFault", propOrder = {
    "conversationId",
    "clientProcessId",
    "frontEndServiceId",
    "faultTimestamp",
    "faultType",
    "exceptionType",
    "exceptionMessage",
    "exceptionCauseType",
    "exceptionCauseMessage"
})
public class MonitorRecordClientConversationFault {

    protected int conversationId;
    protected String clientProcessId;
    protected String frontEndServiceId;
    protected long faultTimestamp;
    protected String faultType;
    protected String exceptionType;
    protected String exceptionMessage;
    protected String exceptionCauseType;
    protected String exceptionCauseMessage;

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
     * Gets the value of the clientProcessId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientProcessId() {
        return clientProcessId;
    }

    /**
     * Sets the value of the clientProcessId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientProcessId(String value) {
        this.clientProcessId = value;
    }

    /**
     * Gets the value of the frontEndServiceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrontEndServiceId() {
        return frontEndServiceId;
    }

    /**
     * Sets the value of the frontEndServiceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrontEndServiceId(String value) {
        this.frontEndServiceId = value;
    }

    /**
     * Gets the value of the faultTimestamp property.
     * 
     */
    public long getFaultTimestamp() {
        return faultTimestamp;
    }

    /**
     * Sets the value of the faultTimestamp property.
     * 
     */
    public void setFaultTimestamp(long value) {
        this.faultTimestamp = value;
    }

    /**
     * Gets the value of the faultType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaultType() {
        return faultType;
    }

    /**
     * Sets the value of the faultType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaultType(String value) {
        this.faultType = value;
    }

    /**
     * Gets the value of the exceptionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExceptionType() {
        return exceptionType;
    }

    /**
     * Sets the value of the exceptionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExceptionType(String value) {
        this.exceptionType = value;
    }

    /**
     * Gets the value of the exceptionMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    /**
     * Sets the value of the exceptionMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExceptionMessage(String value) {
        this.exceptionMessage = value;
    }

    /**
     * Gets the value of the exceptionCauseType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExceptionCauseType() {
        return exceptionCauseType;
    }

    /**
     * Sets the value of the exceptionCauseType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExceptionCauseType(String value) {
        this.exceptionCauseType = value;
    }

    /**
     * Gets the value of the exceptionCauseMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExceptionCauseMessage() {
        return exceptionCauseMessage;
    }

    /**
     * Sets the value of the exceptionCauseMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExceptionCauseMessage(String value) {
        this.exceptionCauseMessage = value;
    }

}
