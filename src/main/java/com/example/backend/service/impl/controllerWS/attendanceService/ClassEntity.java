
package com.example.backend.service.impl.controllerWS.attendanceService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>classEntity complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="classEntity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="classCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="className" type="{http://Service/}className" minOccurs="0"/>
 *         &lt;element name="workingHours" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "classEntity", propOrder = {
    "classCode",
    "className",
    "workingHours"
})
public class ClassEntity {

    protected int classCode;
    @XmlSchemaType(name = "string")
    protected ClassName className;
    protected String workingHours;

    /**
     * ��ȡclassCode���Ե�ֵ��
     * 
     */
    public int getClassCode() {
        return classCode;
    }

    /**
     * ����classCode���Ե�ֵ��
     * 
     */
    public void setClassCode(int value) {
        this.classCode = value;
    }

    /**
     * ��ȡclassName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ClassName }
     *     
     */
    public ClassName getClassName() {
        return className;
    }

    /**
     * ����className���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ClassName }
     *     
     */
    public void setClassName(ClassName value) {
        this.className = value;
    }

    /**
     * ��ȡworkingHours���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkingHours() {
        return workingHours;
    }

    /**
     * ����workingHours���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkingHours(String value) {
        this.workingHours = value;
    }

}
