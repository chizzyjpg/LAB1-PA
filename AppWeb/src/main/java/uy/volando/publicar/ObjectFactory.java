
package uy.volando.publicar;

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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uy.volando.publicar
     * 
     */
    public ObjectFactory() {
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

}
