
package com.example.backend.service.impl.controllerWS.attendanceService;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>className�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="className">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="WHOLE"/>
 *     &lt;enumeration value="DAY"/>
 *     &lt;enumeration value="NIGHT"/>
 *     &lt;enumeration value="REST"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "className")
@XmlEnum
public enum ClassName {

    WHOLE,
    DAY,
    NIGHT,
    REST;

    public String value() {
        return name();
    }

    public static ClassName fromValue(String v) {
        return valueOf(v);
    }

}
