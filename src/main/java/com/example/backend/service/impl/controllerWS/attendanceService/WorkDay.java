
package com.example.backend.service.impl.controllerWS.attendanceService;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>workDay�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="workDay">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SUN"/>
 *     &lt;enumeration value="MON"/>
 *     &lt;enumeration value="TUE"/>
 *     &lt;enumeration value="WED"/>
 *     &lt;enumeration value="THU"/>
 *     &lt;enumeration value="FRI"/>
 *     &lt;enumeration value="SAT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "workDay")
@XmlEnum
public enum WorkDay {

    SUN,
    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT;

    public String value() {
        return name();
    }

    public static WorkDay fromValue(String v) {
        return valueOf(v);
    }

}
