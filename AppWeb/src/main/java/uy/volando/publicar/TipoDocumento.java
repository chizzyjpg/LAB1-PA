
package uy.volando.publicar;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * <p>Java class for tipoDocumento</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * <pre>{@code
 * <simpleType name="tipoDocumento">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="CEDULA"/>
 *     <enumeration value="PASAPORTE"/>
 *     <enumeration value="OTRO"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 * 
 */
@XmlType(name = "tipoDocumento")
@XmlEnum
public enum TipoDocumento {

    CEDULA,
    PASAPORTE,
    OTRO;

    public String value() {
        return name();
    }

    public static TipoDocumento fromValue(String v) {
        return valueOf(v);
    }

}
