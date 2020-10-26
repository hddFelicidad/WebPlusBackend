
package com.example.backend.service.impl.controllerWS.erpService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>materialEntity complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="materialEntity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="materialAttr" type="{http://Service/}materialAttr" minOccurs="0"/>
 *         &lt;element name="measurement" type="{http://Service/}measurement" minOccurs="0"/>
 *         &lt;element name="preparation" type="{http://Service/}preparation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "materialEntity", propOrder = {
    "description",
    "id",
    "materialAttr",
    "measurement",
    "preparation"
})
public class MaterialEntity {

    protected String description;
    protected String id;
    @XmlSchemaType(name = "string")
    protected MaterialAttr materialAttr;
    @XmlSchemaType(name = "string")
    protected Measurement measurement;
    @XmlSchemaType(name = "string")
    protected Preparation preparation;

    /**
     * 获取description属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置description属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
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
     * 获取materialAttr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link MaterialAttr }
     *     
     */
    public MaterialAttr getMaterialAttr() {
        return materialAttr;
    }

    /**
     * 设置materialAttr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link MaterialAttr }
     *     
     */
    public void setMaterialAttr(MaterialAttr value) {
        this.materialAttr = value;
    }

    /**
     * 获取measurement属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Measurement }
     *     
     */
    public Measurement getMeasurement() {
        return measurement;
    }

    /**
     * 设置measurement属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Measurement }
     *     
     */
    public void setMeasurement(Measurement value) {
        this.measurement = value;
    }

    /**
     * 获取preparation属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Preparation }
     *     
     */
    public Preparation getPreparation() {
        return preparation;
    }

    /**
     * 设置preparation属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Preparation }
     *     
     */
    public void setPreparation(Preparation value) {
        this.preparation = value;
    }

}
