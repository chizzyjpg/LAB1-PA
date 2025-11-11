
package uy.volando.publicar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dataPaquete complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="dataPaquete">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="cantRutas" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="tipoAsiento" type="{http://ws.volando.uy/}tipoAsiento" minOccurs="0"/>
 *         <element name="descuento" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="validez" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="costo" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         <element name="rutasIncluidas" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataPaquete", propOrder = {
    "nombre",
    "descripcion",
    "cantRutas",
    "tipoAsiento",
    "descuento",
    "validez",
    "costo",
    "rutasIncluidas"
})
public class DataPaquete {

    protected String nombre;
    protected String descripcion;
    protected int cantRutas;
    @XmlSchemaType(name = "string")
    protected TipoAsiento tipoAsiento;
    protected int descuento;
    protected int validez;
    protected BigDecimal costo;
    @XmlElement(nillable = true)
    protected List<String> rutasIncluidas;

    /**
     * Gets the value of the nombre property.
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
     * Sets the value of the nombre property.
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
     * Gets the value of the descripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Gets the value of the cantRutas property.
     * 
     */
    public int getCantRutas() {
        return cantRutas;
    }

    /**
     * Sets the value of the cantRutas property.
     * 
     */
    public void setCantRutas(int value) {
        this.cantRutas = value;
    }

    /**
     * Gets the value of the tipoAsiento property.
     * 
     * @return
     *     possible object is
     *     {@link TipoAsiento }
     *     
     */
    public TipoAsiento getTipoAsiento() {
        return tipoAsiento;
    }

    /**
     * Sets the value of the tipoAsiento property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoAsiento }
     *     
     */
    public void setTipoAsiento(TipoAsiento value) {
        this.tipoAsiento = value;
    }

    /**
     * Gets the value of the descuento property.
     * 
     */
    public int getDescuento() {
        return descuento;
    }

    /**
     * Sets the value of the descuento property.
     * 
     */
    public void setDescuento(int value) {
        this.descuento = value;
    }

    /**
     * Gets the value of the validez property.
     * 
     */
    public int getValidez() {
        return validez;
    }

    /**
     * Sets the value of the validez property.
     * 
     */
    public void setValidez(int value) {
        this.validez = value;
    }

    /**
     * Gets the value of the costo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCosto() {
        return costo;
    }

    /**
     * Sets the value of the costo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCosto(BigDecimal value) {
        this.costo = value;
    }

    /**
     * Gets the value of the rutasIncluidas property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rutasIncluidas property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getRutasIncluidas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * </p>
     * 
     * 
     * @return
     *     The value of the rutasIncluidas property.
     */
    public List<String> getRutasIncluidas() {
        if (rutasIncluidas == null) {
            rutasIncluidas = new ArrayList<>();
        }
        return this.rutasIncluidas;
    }

}
