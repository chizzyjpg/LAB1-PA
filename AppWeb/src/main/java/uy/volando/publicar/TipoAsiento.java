
package uy.volando.publicar;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * <p>Java class for tipoAsiento</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * <pre>{@code
 * <simpleType name="tipoAsiento">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="TURISTA"/>
 *     <enumeration value="EJECUTIVO"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 * 
 */
@XmlType(name = "tipoAsiento")
@XmlEnum
public enum TipoAsiento {

    TURISTA,
    EJECUTIVO;

    public String value() {
        return name();
    }

    public static TipoAsiento fromValue(String v) {
        return valueOf(v);
    }

}
