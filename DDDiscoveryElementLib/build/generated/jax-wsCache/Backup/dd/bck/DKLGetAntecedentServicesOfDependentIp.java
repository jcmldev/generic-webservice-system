
package dd.bck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DKL_getAntecedentServicesOfDependentIp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DKL_getAntecedentServicesOfDependentIp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dependentIp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DKL_getAntecedentServicesOfDependentIp", propOrder = {
    "dependentIp"
})
public class DKLGetAntecedentServicesOfDependentIp {

    protected String dependentIp;

    /**
     * Gets the value of the dependentIp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDependentIp() {
        return dependentIp;
    }

    /**
     * Sets the value of the dependentIp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDependentIp(String value) {
        this.dependentIp = value;
    }

}
