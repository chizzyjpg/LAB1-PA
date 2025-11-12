
package uy.volando.publicar;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uy.volando.publicar package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _DataCategoria_QNAME = new QName("http://ws.volando.uy/", "dataCategoria");
    private static final QName _DataCiudad_QNAME = new QName("http://ws.volando.uy/", "dataCiudad");
    private static final QName _DataPasaje_QNAME = new QName("http://ws.volando.uy/", "dataPasaje");
    private static final QName _DataReserva_QNAME = new QName("http://ws.volando.uy/", "dataReserva");
    private static final QName _DataRuta_QNAME = new QName("http://ws.volando.uy/", "dataRuta");
    private static final QName _DataVueloEspecifico_QNAME = new QName("http://ws.volando.uy/", "dataVueloEspecifico");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uy.volando.publicar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataPasaje }
     * 
     * @return
     *     the new instance of {@link DataPasaje }
     */
    public DataPasaje createDataPasaje() {
        return new DataPasaje();
    }

    /**
     * Create an instance of {@link DataReserva }
     * 
     * @return
     *     the new instance of {@link DataReserva }
     */
    public DataReserva createDataReserva() {
        return new DataReserva();
    }

    /**
     * Create an instance of {@link DataVueloEspecifico }
     * 
     * @return
     *     the new instance of {@link DataVueloEspecifico }
     */
    public DataVueloEspecifico createDataVueloEspecifico() {
        return new DataVueloEspecifico();
    }

    /**
     * Create an instance of {@link DataAerolinea }
     * 
     * @return
     *     the new instance of {@link DataAerolinea }
     */
    public DataAerolinea createDataAerolinea() {
        return new DataAerolinea();
    }

    /**
     * Create an instance of {@link DataPaquete }
     * 
     * @return
     *     the new instance of {@link DataPaquete }
     */

    public DataPaquete createDataPaquete() {
        return new DataPaquete();
    }    
    /** 
     * Create an instance of {@link DataCliente }
     * 
     * @return
     *     the new instance of {@link DataCliente }
     */

    public DataCliente createDataCliente() {
        return new DataCliente();
    }

    /**
     * Create an instance of {@link DataRuta }
     * 
     * @return
     *     the new instance of {@link DataRuta }
     */
    public DataRuta createDataRuta() {
        return new DataRuta();
    }

    /**
     * Create an instance of {@link DataCiudad }
     * 
     * @return
     *     the new instance of {@link DataCiudad }
     */
    public DataCiudad createDataCiudad() {
        return new DataCiudad();
    }

    /**
     * Create an instance of {@link DataCategoria }
     * 
     * @return
     *     the new instance of {@link DataCategoria }
     */
    public DataCategoria createDataCategoria() {
        return new DataCategoria();
    }

    /**
     * Create an instance of {@link DataPaqueteArray }
     * 
     * @return
     *     the new instance of {@link DataPaqueteArray }
     */
    public DataPaqueteArray createDataPaqueteArray() {
        return new DataPaqueteArray();
    }

    /**
     * Create an instance of {@link DataReservaArray }
     * 
     * @return
     *     the new instance of {@link DataReservaArray }
     */
    public DataReservaArray createDataReservaArray() {
        return new DataReservaArray();
    }

    /**
     * Create an instance of {@link DataCategoriaArray }
     * 
     * @return
     *     the new instance of {@link DataCategoriaArray }
     */
    public DataCategoriaArray createDataCategoriaArray() {
        return new DataCategoriaArray();
    }

    /**
     * Create an instance of {@link DataVueloEspecificoArray }
     * 
     * @return
     *     the new instance of {@link DataVueloEspecificoArray }
     */
    public DataVueloEspecificoArray createDataVueloEspecificoArray() {
        return new DataVueloEspecificoArray();
    }

    /**
     * Create an instance of {@link DataRutaArray }
     * 
     * @return
     *     the new instance of {@link DataRutaArray }
     */
    public DataRutaArray createDataRutaArray() {
        return new DataRutaArray();
    }

    /**
     * Create an instance of {@link DataAerolineaArray }
     * 
     * @return
     *     the new instance of {@link DataAerolineaArray }
     */
    public DataAerolineaArray createDataAerolineaArray() {
        return new DataAerolineaArray();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataCategoria }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DataCategoria }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.volando.uy/", name = "dataCategoria")
    public JAXBElement<DataCategoria> createDataCategoria(DataCategoria value) {
        return new JAXBElement<>(_DataCategoria_QNAME, DataCategoria.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataCiudad }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DataCiudad }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.volando.uy/", name = "dataCiudad")
    public JAXBElement<DataCiudad> createDataCiudad(DataCiudad value) {
        return new JAXBElement<>(_DataCiudad_QNAME, DataCiudad.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataPasaje }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DataPasaje }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.volando.uy/", name = "dataPasaje")
    public JAXBElement<DataPasaje> createDataPasaje(DataPasaje value) {
        return new JAXBElement<>(_DataPasaje_QNAME, DataPasaje.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataReserva }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DataReserva }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.volando.uy/", name = "dataReserva")
    public JAXBElement<DataReserva> createDataReserva(DataReserva value) {
        return new JAXBElement<>(_DataReserva_QNAME, DataReserva.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataRuta }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DataRuta }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.volando.uy/", name = "dataRuta")
    public JAXBElement<DataRuta> createDataRuta(DataRuta value) {
        return new JAXBElement<>(_DataRuta_QNAME, DataRuta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataVueloEspecifico }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DataVueloEspecifico }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.volando.uy/", name = "dataVueloEspecifico")
    public JAXBElement<DataVueloEspecifico> createDataVueloEspecifico(DataVueloEspecifico value) {
        return new JAXBElement<>(_DataVueloEspecifico_QNAME, DataVueloEspecifico.class, null, value);
    }

}
