
package test;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GP_storeBackupData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GP_storeBackupData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nodeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fromTimestamp" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="toTimestamp" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="timeSlotSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="backupData" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GP_storeBackupData", propOrder = {
    "nodeId",
    "fromTimestamp",
    "toTimestamp",
    "timeSlotSize",
    "backupData"
})
public class GPStoreBackupData {

    protected String nodeId;
    protected long fromTimestamp;
    protected long toTimestamp;
    protected int timeSlotSize;
    @XmlElement(nillable = true)
    protected List<String> backupData;

    /**
     * Gets the value of the nodeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * Sets the value of the nodeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeId(String value) {
        this.nodeId = value;
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

    /**
     * Gets the value of the timeSlotSize property.
     * 
     */
    public int getTimeSlotSize() {
        return timeSlotSize;
    }

    /**
     * Sets the value of the timeSlotSize property.
     * 
     */
    public void setTimeSlotSize(int value) {
        this.timeSlotSize = value;
    }

    /**
     * Gets the value of the backupData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the backupData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBackupData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getBackupData() {
        if (backupData == null) {
            backupData = new ArrayList<String>();
        }
        return this.backupData;
    }

}
