
package uy.volando.publicar;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * <p>Clase Java para estadoRuta.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * <pre>{@code
 * <simpleType name="estadoRuta">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="CONFIRMADA"/>
 *     <enumeration value="RECHAZADA"/>
 *     <enumeration value="INGRESADA"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 * 
 */
@XmlType(name = "estadoRuta")
@XmlEnum
public enum EstadoRuta {

    CONFIRMADA,
    RECHAZADA,
    INGRESADA;

    public String value() {
        return name();
    }

    public static EstadoRuta fromValue(String v) {
        return valueOf(v);
    }

}
