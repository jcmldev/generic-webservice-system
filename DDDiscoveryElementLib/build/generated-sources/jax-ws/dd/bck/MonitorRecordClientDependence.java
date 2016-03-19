
package dd.bck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Monitor_recordClientDependence complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Monitor_recordClientDependence">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="clientProcessId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="frontEndServiceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dependenceTimestamp" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Monitor_recordClientDependence", propOrder = {
    "clientProcessId",
    "frontEndServiceId",
    "dependenceTimestamp"
})
public class MonitorRecordClientDependence {

    protected String clientProcessId;
    protected String frontEndServiceId;
    protected long dependenceTimestamp;

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
     * Gets the value of the dependenceTimestamp property.
     * 
     */
    public long getDependenceTimestamp() {
        return dependenceTimestamp;
    }

    /**
     * Sets the value of the dependenceTimestamp property.
     * 
     */
    public void setDependenceTimestamp(long value) {
        this.dependenceTimestamp = value;
    }

}
