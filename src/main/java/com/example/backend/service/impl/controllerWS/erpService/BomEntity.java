
package com.example.backend.service.impl.controllerWS.erpService;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>bomEntity complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="bomEntity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="changeTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lineResource" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="mainResource" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="materialCount" type="{http://www.w3.org/2001/XMLSchema}double" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="materials" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="standardOutput" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="workerCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bomEntity", propOrder = {
    "changeTime",
    "id",
    "lineResource",
    "mainResource",
    "materialCount",
    "materials",
    "standardOutput",
    "workerCount"
})
public class BomEntity {

    protected String changeTime;
    protected String id;
    @XmlElement(nillable = true)
    protected List<String> lineResource;
    @XmlElement(nillable = true)
    protected List<String> mainResource;
    @XmlElement(nillable = true)
    protected List<Double> materialCount;
    @XmlElement(nillable = true)
    protected List<String> materials;
    protected String standardOutput;
    protected int workerCount;

    /**
     * 获取changeTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChangeTime() {
        return changeTime;
    }

    /**
     * 设置changeTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChangeTime(String value) {
        this.changeTime = value;
    }

    /**
     * 获取id属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the lineResource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lineResource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLineResource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLineResource() {
        if (lineResource == null) {
            lineResource = new ArrayList<String>();
        }
        return this.lineResource;
    }

    /**
     * Gets the value of the mainResource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mainResource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMainResource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMainResource() {
        if (mainResource == null) {
            mainResource = new ArrayList<String>();
        }
        return this.mainResource;
    }

    /**
     * Gets the value of the materialCount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the materialCount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMaterialCount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getMaterialCount() {
        if (materialCount == null) {
            materialCount = new ArrayList<Double>();
        }
        return this.materialCount;
    }

    /**
     * Gets the value of the materials property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the materials property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMaterials().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMaterials() {
        if (materials == null) {
            materials = new ArrayList<String>();
        }
        return this.materials;
    }

    /**
     * 获取standardOutput属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardOutput() {
        return standardOutput;
    }

    /**
     * 设置standardOutput属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardOutput(String value) {
        this.standardOutput = value;
    }

    /**
     * 获取workerCount属性的值。
     * 
     */
    public int getWorkerCount() {
        return workerCount;
    }

    /**
     * 设置workerCount属性的值。
     * 
     */
    public void setWorkerCount(int value) {
        this.workerCount = value;
    }

}
