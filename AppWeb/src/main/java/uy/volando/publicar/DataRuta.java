
package uy.volando.publicar;

import java.math.BigDecimal;
import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dataRuta complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="dataRuta">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="idRuta" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="descripcionCorta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="ciudadOrigen" type="{http://ws.volando.uy/}dataCiudad" minOccurs="0"/>
 *         <element name="ciudadDestino" type="{http://ws.volando.uy/}dataCiudad" minOccurs="0"/>
 *         <element name="hora" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         <element name="costoTurista" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         <element name="costoEjecutivo" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         <element name="costoEquipajeExtra" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="categoria" type="{http://ws.volando.uy/}dataCategoria" minOccurs="0"/>
 *         <element name="nicknameAerolinea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="estado" type="{http://ws.volando.uy/}estadoRuta" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataRuta", propOrder = {
    "idRuta",
    "nombre",
    "descripcion",
    "descripcionCorta",
    "ciudadOrigen",
    "ciudadDestino",
    "hora",
    "fechaAlta",
    "costoTurista",
    "costoEjecutivo",
    "costoEquipajeExtra",
    "categoria",
    "nicknameAerolinea",
    "estado"
})
public class DataRuta {

    protected int idRuta;
    protected String nombre;
    protected String descripcion;
    protected String descripcionCorta;
    protected DataCiudad ciudadOrigen;
    protected DataCiudad ciudadDestino;
    protected int hora;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAlta;
    protected BigDecimal costoTurista;
    protected BigDecimal costoEjecutivo;
    protected int costoEquipajeExtra;
    protected DataCategoria categoria;
    protected String nicknameAerolinea;
    @XmlSchemaType(name = "string")
    protected EstadoRuta estado;

    /**
     * Gets the value of the idRuta property.
     * 
     */
    public int getIdRuta() {
        return idRuta;
    }

    /**
     * Sets the value of the idRuta property.
     * 
     */
    public void setIdRuta(int value) {
        this.idRuta = value;
    }

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
     * Gets the value of the descripcionCorta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    /**
     * Sets the value of the descripcionCorta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionCorta(String value) {
        this.descripcionCorta = value;
    }

    /**
     * Gets the value of the ciudadOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link DataCiudad }
     *     
     */
    public DataCiudad getCiudadOrigen() {
        return ciudadOrigen;
    }

    /**
     * Sets the value of the ciudadOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataCiudad }
     *     
     */
    public void setCiudadOrigen(DataCiudad value) {
        this.ciudadOrigen = value;
    }

    /**
     * Gets the value of the ciudadDestino property.
     * 
     * @return
     *     possible object is
     *     {@link DataCiudad }
     *     
     */
    public DataCiudad getCiudadDestino() {
        return ciudadDestino;
    }

    /**
     * Sets the value of the ciudadDestino property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataCiudad }
     *     
     */
    public void setCiudadDestino(DataCiudad value) {
        this.ciudadDestino = value;
    }

    /**
     * Gets the value of the hora property.
     * 
     */
    public int getHora() {
        return hora;
    }

    /**
     * Sets the value of the hora property.
     * 
     */
    public void setHora(int value) {
        this.hora = value;
    }

    /**
     * Gets the value of the fechaAlta property.
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
     * Sets the value of the fechaAlta property.
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
     * Gets the value of the costoTurista property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCostoTurista() {
        return costoTurista;
    }

    /**
     * Sets the value of the costoTurista property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCostoTurista(BigDecimal value) {
        this.costoTurista = value;
    }

    /**
     * Gets the value of the costoEjecutivo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCostoEjecutivo() {
        return costoEjecutivo;
    }

    /**
     * Sets the value of the costoEjecutivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCostoEjecutivo(BigDecimal value) {
        this.costoEjecutivo = value;
    }

    /**
     * Gets the value of the costoEquipajeExtra property.
     * 
     */
    public int getCostoEquipajeExtra() {
        return costoEquipajeExtra;
    }

    /**
     * Sets the value of the costoEquipajeExtra property.
     * 
     */
    public void setCostoEquipajeExtra(int value) {
        this.costoEquipajeExtra = value;
    }

    /**
     * Gets the value of the categoria property.
     * 
     * @return
     *     possible object is
     *     {@link DataCategoria }
     *     
     */
    public DataCategoria getCategoria() {
        return categoria;
    }

    /**
     * Sets the value of the categoria property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataCategoria }
     *     
     */
    public void setCategoria(DataCategoria value) {
        this.categoria = value;
    }

    /**
     * Gets the value of the nicknameAerolinea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNicknameAerolinea() {
        return nicknameAerolinea;
    }

    /**
     * Sets the value of the nicknameAerolinea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNicknameAerolinea(String value) {
        this.nicknameAerolinea = value;
    }

    /**
     * Gets the value of the estado property.
     * 
     * @return
     *     possible object is
     *     {@link EstadoRuta }
     *     
     */
    public EstadoRuta getEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstadoRuta }
     *     
     */
    public void setEstado(EstadoRuta value) {
        this.estado = value;
    }

}
