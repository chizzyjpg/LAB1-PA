
package uy.volando.publicar;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * <p>Clase Java para tipoAsiento.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
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
