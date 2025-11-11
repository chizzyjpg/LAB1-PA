
package uy.volando.publicar;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para dataVueloEspecifico complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="dataVueloEspecifico">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="fecha" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         <element name="duracion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="maxAsientosTur" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="maxAsientosEjec" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         <element name="Druta" type="{http://ws.volando.uy/}dataRuta" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataVueloEspecifico", propOrder = {
    "nombre",
    "fecha",
    "duracion",
    "maxAsientosTur",
    "maxAsientosEjec",
    "fechaAlta",
    "druta"
})
public class DataVueloEspecifico {

    protected String nombre;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fecha;
    protected int duracion;
    protected int maxAsientosTur;
    protected int maxAsientosEjec;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAlta;
    @XmlElement(name = "Druta")
    protected DataRuta druta;

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
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
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad fecha.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecha() {
        return fecha;
    }

    /**
     * Define el valor de la propiedad fecha.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecha(XMLGregorianCalendar value) {
        this.fecha = value;
    }

    /**
     * Obtiene el valor de la propiedad duracion.
     * 
     */
    public int getDuracion() {
        return duracion;
    }

    /**
     * Define el valor de la propiedad duracion.
     * 
     */
    public void setDuracion(int value) {
        this.duracion = value;
    }

    /**
     * Obtiene el valor de la propiedad maxAsientosTur.
     * 
     */
    public int getMaxAsientosTur() {
        return maxAsientosTur;
    }

    /**
     * Define el valor de la propiedad maxAsientosTur.
     * 
     */
    public void setMaxAsientosTur(int value) {
        this.maxAsientosTur = value;
    }

    /**
     * Obtiene el valor de la propiedad maxAsientosEjec.
     * 
     */
    public int getMaxAsientosEjec() {
        return maxAsientosEjec;
    }

    /**
     * Define el valor de la propiedad maxAsientosEjec.
     * 
     */
    public void setMaxAsientosEjec(int value) {
        this.maxAsientosEjec = value;
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
     * Obtiene el valor de la propiedad druta.
     * 
     * @return
     *     possible object is
     *     {@link DataRuta }
     *     
     */
    public DataRuta getDruta() {
        return druta;
    }

    /**
     * Define el valor de la propiedad druta.
     * 
     * @param value
     *     allowed object is
     *     {@link DataRuta }
     *     
     */
    public void setDruta(DataRuta value) {
        this.druta = value;
    }

}
