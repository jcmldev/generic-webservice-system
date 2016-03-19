
package dd.bck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GP_Multihop_getInterDependenciesForTimeWindowAntecedent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GP_Multihop_getInterDependenciesForTimeWindowAntecedent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dependentServiceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "GP_Multihop_getInterDependenciesForTimeWindowAntecedent", propOrder = {
    "dependentServiceId",
    "fromTimestamp",
    "toTimestamp"
})
public class GPMultihopGetInterDependenciesForTimeWindowAntecedent {

    protected String dependentServiceId;
    protected long fromTimestamp;
    protected long toTimestamp;

    /**
     * Gets the value of the dependentServiceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDependentServiceId() {
        return dependentServiceId;
    }

    /**
     * Sets the value of the dependentServiceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDependentServiceId(String value) {
        this.dependentServiceId = value;
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
