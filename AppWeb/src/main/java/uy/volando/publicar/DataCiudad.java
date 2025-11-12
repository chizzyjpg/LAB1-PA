
package uy.volando.publicar;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para dataCiudad complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="dataCiudad">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="pais" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="nombreAeropuerto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="descripcionAeropuerto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         <element name="sitioWeb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataCiudad", propOrder = {
    "nombre",
    "pais",
    "nombreAeropuerto",
    "descripcionAeropuerto",
    "fechaAlta",
    "sitioWeb"
})
public class DataCiudad {

    protected String nombre;
    protected String pais;
    protected String nombreAeropuerto;
    protected String descripcionAeropuerto;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAlta;
    protected String sitioWeb;

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     *     
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
    }
    /**
     * Obtiene el valor de la propiedad pais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPais() {
        return pais;
    }

    /**
     * Define el valor de la propiedad pais.
     * 
     * @param value
     *     allowed object is
    public void setPais(String value) {
    }

    /**
     * Obtiene el valor de la propiedad nombreAeropuerto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreAeropuerto() {
        return nombreAeropuerto;
    }

    /**
     * Define el valor de la propiedad nombreAeropuerto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreAeropuerto(String value) {
        this.nombreAeropuerto = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionAeropuerto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionAeropuerto() {
        return descripcionAeropuerto;
    }

    /**
     * Define el valor de la propiedad descripcionAeropuerto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionAeropuerto(String value) {
        this.descripcionAeropuerto = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaAlta.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Define el valor de la propiedad fechaAlta.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaAlta(XMLGregorianCalendar value) {
        this.fechaAlta = value;
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
