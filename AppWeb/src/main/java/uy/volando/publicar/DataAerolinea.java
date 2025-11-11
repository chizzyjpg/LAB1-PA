
package uy.volando.publicar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dataAerolinea complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="dataAerolinea">
 *   <complexContent>
 *     <extension base="{http://ws.volando.uy/}dataUsuario">
 *       <sequence>
 *         <element name="descGeneral" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="sitioWeb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       </sequence>
 *     </extension>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataAerolinea", propOrder = {
    "descGeneral",
    "sitioWeb"
})
public class DataAerolinea
    extends DataUsuario
{

    protected String descGeneral;
    protected String sitioWeb;

    /**
     * Gets the value of the descGeneral property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescGeneral() {
        return descGeneral;
    }

    /**
     * Sets the value of the descGeneral property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescGeneral(String value) {
        this.descGeneral = value;
    }

    /**
     * Gets the value of the sitioWeb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSitioWeb() {
        return sitioWeb;
    }

    /**
     * Sets the value of the sitioWeb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSitioWeb(String value) {
        this.sitioWeb = value;
    }

}
