
package com.example.backend.service.impl.controllerWS.orderService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.backend.service.impl.controllerWS.orderService package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetOrderInfoById_QNAME = new QName("http://Service/", "getOrderInfoById");
    private final static QName _GetAllOrdersResponse_QNAME = new QName("http://Service/", "getAllOrdersResponse");
    private final static QName _GetOrderInfoByIdResponse_QNAME = new QName("http://Service/", "getOrderInfoByIdResponse");
    private final static QName _GetAllOrders_QNAME = new QName("http://Service/", "getAllOrders");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.backend.service.impl.controllerWS.orderService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetAllOrdersResponse }
     * 
     */
    public GetAllOrdersResponse createGetAllOrdersResponse() {
        return new GetAllOrdersResponse();
    }

    /**
     * Create an instance of {@link GetOrderInfoById }
     * 
     */
    public GetOrderInfoById createGetOrderInfoById() {
        return new GetOrderInfoById();
    }

    /**
     * Create an instance of {@link GetOrderInfoByIdResponse }
     * 
     */
    public GetOrderInfoByIdResponse createGetOrderInfoByIdResponse() {
        return new GetOrderInfoByIdResponse();
    }

    /**
     * Create an instance of {@link GetAllOrders }
     * 
     */
    public GetAllOrders createGetAllOrders() {
        return new GetAllOrders();
    }

    /**
     * Create an instance of {@link OrderEntity }
     * 
     */
    public OrderEntity createOrderEntity() {
        return new OrderEntity();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOrderInfoById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getOrderInfoById")
    public JAXBElement<GetOrderInfoById> createGetOrderInfoById(GetOrderInfoById value) {
        return new JAXBElement<GetOrderInfoById>(_GetOrderInfoById_QNAME, GetOrderInfoById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllOrdersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getAllOrdersResponse")
    public JAXBElement<GetAllOrdersResponse> createGetAllOrdersResponse(GetAllOrdersResponse value) {
        return new JAXBElement<GetAllOrdersResponse>(_GetAllOrdersResponse_QNAME, GetAllOrdersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetOrderInfoByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getOrderInfoByIdResponse")
    public JAXBElement<GetOrderInfoByIdResponse> createGetOrderInfoByIdResponse(GetOrderInfoByIdResponse value) {
        return new JAXBElement<GetOrderInfoByIdResponse>(_GetOrderInfoByIdResponse_QNAME, GetOrderInfoByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllOrders }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getAllOrders")
    public JAXBElement<GetAllOrders> createGetAllOrders(GetAllOrders value) {
        return new JAXBElement<GetAllOrders>(_GetAllOrders_QNAME, GetAllOrders.class, null, value);
    }

}
