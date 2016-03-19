
package dd.bck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GP_getBackupData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GP_getBackupData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fromTimestamp" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="toTimestamp" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="timeSlotSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GP_getBackupData", propOrder = {
    "fromTimestamp",
    "toTimestamp",
    "timeSlotSize"
})
public class GPGetBackupData {

    protected long fromTimestamp;
    protected long toTimestamp;
    protected int timeSlotSize;

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

}
