
package uy.volando.publicar;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * <p>Java class for estadoRuta</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
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
