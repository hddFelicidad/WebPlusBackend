
package com.example.backend.service.impl.controllerWS.erpService;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>materialAttr的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="materialAttr">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FINISHED"/>
 *     &lt;enumeration value="SEMI_FINISHED"/>
 *     &lt;enumeration value="RAW"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "materialAttr")
@XmlEnum
public enum MaterialAttr {

    FINISHED,
    SEMI_FINISHED,
    RAW;

    public String value() {
        return name();
    }

    public static MaterialAttr fromValue(String v) {
        return valueOf(v);
    }

}
