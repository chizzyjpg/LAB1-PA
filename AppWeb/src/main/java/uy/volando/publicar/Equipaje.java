
package uy.volando.publicar;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * <p>Clase Java para equipaje.</p>
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.</p>
 * <pre>{@code
 * <simpleType name="equipaje">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="BOLSO"/>
 *     <enumeration value="MOCHILAYCARRYON"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 * 
 */
@XmlType(name = "equipaje")
@XmlEnum
public enum Equipaje {

    BOLSO,
    MOCHILAYCARRYON;

    public String value() {
        return name();
    }

    public static Equipaje fromValue(String v) {
        return valueOf(v);
    }

}
