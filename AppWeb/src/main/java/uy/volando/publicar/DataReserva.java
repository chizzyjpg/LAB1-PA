
package uy.volando.publicar;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para dataReserva complex type.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * 
 * <pre>{@code
 * <complexType name="dataReserva">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="idReserva" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="fechaReserva" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         <element name="tipoAsiento" type="{http://ws.volando.uy/}tipoAsiento" minOccurs="0"/>
 *         <element name="equipaje" type="{http://ws.volando.uy/}equipaje" minOccurs="0"/>
 *         <element name="cantEquipajeExtra" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="costoTotal" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         <element name="pasajes" type="{http://ws.volando.uy/}dataPasaje" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="nickCliente" type="{http://ws.volando.uy/}dataCliente" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataReserva", propOrder = {
    "idReserva",
    "fechaReserva",
    "tipoAsiento",
    "equipaje",
    "cantEquipajeExtra",
    "costoTotal",
    "pasajes",
    "nickCliente"
})
public class DataReserva {

    protected int idReserva;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaReserva;
    @XmlSchemaType(name = "string")
    protected TipoAsiento tipoAsiento;
    @XmlSchemaType(name = "string")
    protected Equipaje equipaje;
    protected int cantEquipajeExtra;
    protected Float costoTotal;
    @XmlElement(nillable = true)
    protected List<DataPasaje> pasajes;
    protected DataCliente nickCliente;

    /**
     * Obtiene el valor de la propiedad idReserva.
     * 
     */
    public int getIdReserva() {
        return idReserva;
    }

    /**
     * Define el valor de la propiedad idReserva.
     * 
     */
    public void setIdReserva(int value) {
        this.idReserva = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaReserva.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaReserva() {
        return fechaReserva;
    }

    /**
     * Define el valor de la propiedad fechaReserva.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaReserva(XMLGregorianCalendar value) {
        this.fechaReserva = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoAsiento.
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
     * Define el valor de la propiedad tipoAsiento.
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
     * Obtiene el valor de la propiedad equipaje.
     * 
     * @return
     *     possible object is
     *     {@link Equipaje }
     *     
     */
    public Equipaje getEquipaje() {
        return equipaje;
    }

    /**
     * Define el valor de la propiedad equipaje.
     * 
     * @param value
     *     allowed object is
     *     {@link Equipaje }
     *     
     */
    public void setEquipaje(Equipaje value) {
        this.equipaje = value;
    }

    /**
     * Obtiene el valor de la propiedad cantEquipajeExtra.
     * 
     */
    public int getCantEquipajeExtra() {
        return cantEquipajeExtra;
    }

    /**
     * Define el valor de la propiedad cantEquipajeExtra.
     * 
     */
    public void setCantEquipajeExtra(int value) {
        this.cantEquipajeExtra = value;
    }

    /**
     * Obtiene el valor de la propiedad costoTotal.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getCostoTotal() {
        return costoTotal;
    }

    /**
     * Define el valor de la propiedad costoTotal.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setCostoTotal(Float value) {
        this.costoTotal = value;
    }

    /**
     * Gets the value of the pasajes property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pasajes property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getPasajes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataPasaje }
     * </p>
     * 
     * 
     * @return
     *     The value of the pasajes property.
     */
    public List<DataPasaje> getPasajes() {
        if (pasajes == null) {
            pasajes = new ArrayList<>();
        }
        return this.pasajes;
    }

    /**
     * Obtiene el valor de la propiedad nickCliente.
     * 
     * @return
     *     possible object is
     *     {@link DataCliente }
     *     
     */
    public DataCliente getNickCliente() {
        return nickCliente;
    }

    /**
     * Define el valor de la propiedad nickCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link DataCliente }
     *     
     */
    public void setNickCliente(DataCliente value) {
        this.nickCliente = value;
    }

}
