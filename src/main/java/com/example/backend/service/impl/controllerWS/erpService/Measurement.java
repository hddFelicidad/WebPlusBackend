
package com.example.backend.service.impl.controllerWS.erpService;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>measurement�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="measurement">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PCS"/>
 *     &lt;enumeration value="KG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "measurement")
@XmlEnum
public enum Measurement {

    PCS,
    KG;

    public String value() {
        return name();
    }

    public static Measurement fromValue(String v) {
        return valueOf(v);
    }

}
