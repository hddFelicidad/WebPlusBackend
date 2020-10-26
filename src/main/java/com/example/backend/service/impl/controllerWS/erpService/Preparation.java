
package com.example.backend.service.impl.controllerWS.erpService;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>preparation的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="preparation">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MAKE"/>
 *     &lt;enumeration value="BUY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "preparation")
@XmlEnum
public enum Preparation {

    MAKE,
    BUY;

    public String value() {
        return name();
    }

    public static Preparation fromValue(String v) {
        return valueOf(v);
    }

}
