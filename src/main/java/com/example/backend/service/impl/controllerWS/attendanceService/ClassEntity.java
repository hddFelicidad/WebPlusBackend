
package com.example.backend.service.impl.controllerWS.attendanceService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>classEntity complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
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
     * 获取classCode属性的值。
     * 
     */
    public int getClassCode() {
        return classCode;
    }

    /**
     * 设置classCode属性的值。
     * 
     */
    public void setClassCode(int value) {
        this.classCode = value;
    }

    /**
     * 获取className属性的值。
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
     * 设置className属性的值。
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
     * 获取workingHours属性的值。
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
     * 设置workingHours属性的值。
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
