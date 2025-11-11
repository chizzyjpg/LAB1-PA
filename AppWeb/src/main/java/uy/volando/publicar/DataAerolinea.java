
package uy.volando.publicar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para dataAerolinea complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
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
     * Obtiene el valor de la propiedad descGeneral.
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
     * Define el valor de la propiedad descGeneral.
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
     * Obtiene el valor de la propiedad sitioWeb.
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
     * Define el valor de la propiedad sitioWeb.
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
