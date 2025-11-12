
package uy.volando.publicar;

import java.math.BigDecimal;
import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para dataRuta complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
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
     * Obtiene el valor de la propiedad idRuta.
     * 
     */
    public int getIdRuta() {
        return idRuta;
    }

    /**
     * Define el valor de la propiedad idRuta.
     * 
     */
    public void setIdRuta(int value) {
        this.idRuta = value;
    }

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
     * Obtiene el valor de la propiedad descripcion.
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
     * Define el valor de la propiedad descripcion.
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
     * Obtiene el valor de la propiedad descripcionCorta.
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
     * Define el valor de la propiedad descripcionCorta.
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
     * Obtiene el valor de la propiedad ciudadOrigen.
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
     * Define el valor de la propiedad ciudadOrigen.
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
     * Obtiene el valor de la propiedad ciudadDestino.
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
     * Define el valor de la propiedad ciudadDestino.
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
     * Obtiene el valor de la propiedad hora.
     * 
     */
    public int getHora() {
        return hora;
    }

    /**
     * Define el valor de la propiedad hora.
     * 
     */
    public void setHora(int value) {
        this.hora = value;
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
     * Obtiene el valor de la propiedad costoTurista.
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
     * Define el valor de la propiedad costoTurista.
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
     * Obtiene el valor de la propiedad costoEjecutivo.
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
     * Define el valor de la propiedad costoEjecutivo.
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
     * Obtiene el valor de la propiedad costoEquipajeExtra.
     * 
     */
    public int getCostoEquipajeExtra() {
        return costoEquipajeExtra;
    }

    /**
     * Define el valor de la propiedad costoEquipajeExtra.
     * 
     */
    public void setCostoEquipajeExtra(int value) {
        this.costoEquipajeExtra = value;
    }

    /**
     * Obtiene el valor de la propiedad categoria.
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
     * Define el valor de la propiedad categoria.
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
     * Obtiene el valor de la propiedad nicknameAerolinea.
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
     * Define el valor de la propiedad nicknameAerolinea.
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
     * Obtiene el valor de la propiedad estado.
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
     * Define el valor de la propiedad estado.
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
