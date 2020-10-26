
package com.example.backend.service.impl.controllerWS.attendanceService;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>calendarEntity complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="calendarEntity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="classCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resourceCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="workDays" type="{http://Service/}workDay" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "calendarEntity", propOrder = {
    "classCode",
    "remarks",
    "resourceCode",
    "workDays"
})
public class CalendarEntity {

    protected int classCode;
    protected String remarks;
    protected String resourceCode;
    @XmlElement(nillable = true)
    @XmlSchemaType(name = "string")
    protected List<WorkDay> workDays;

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
     * ��ȡremarks���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * ����remarks���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarks(String value) {
        this.remarks = value;
    }

    /**
     * ��ȡresourceCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceCode() {
        return resourceCode;
    }

    /**
     * ����resourceCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceCode(String value) {
        this.resourceCode = value;
    }

    /**
     * Gets the value of the workDays property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the workDays property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWorkDays().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WorkDay }
     * 
     * 
     */
    public List<WorkDay> getWorkDays() {
        if (workDays == null) {
            workDays = new ArrayList<WorkDay>();
        }
        return this.workDays;
    }

}
