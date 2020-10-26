
package com.example.backend.service.impl.controllerWS.personnelService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.backend.service.impl.controllerWS.personnelService package. 
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

    private final static QName _IdAuthentication_QNAME = new QName("http://Service/", "idAuthentication");
    private final static QName _GetStaffInfoById_QNAME = new QName("http://Service/", "getStaffInfoById");
    private final static QName _GetStaffInfoByIdResponse_QNAME = new QName("http://Service/", "getStaffInfoByIdResponse");
    private final static QName _IdAuthenticationResponse_QNAME = new QName("http://Service/", "idAuthenticationResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.backend.service.impl.controllerWS.personnelService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IdAuthenticationResponse }
     * 
     */
    public IdAuthenticationResponse createIdAuthenticationResponse() {
        return new IdAuthenticationResponse();
    }

    /**
     * Create an instance of {@link IdAuthentication }
     * 
     */
    public IdAuthentication createIdAuthentication() {
        return new IdAuthentication();
    }

    /**
     * Create an instance of {@link GetStaffInfoById }
     * 
     */
    public GetStaffInfoById createGetStaffInfoById() {
        return new GetStaffInfoById();
    }

    /**
     * Create an instance of {@link GetStaffInfoByIdResponse }
     * 
     */
    public GetStaffInfoByIdResponse createGetStaffInfoByIdResponse() {
        return new GetStaffInfoByIdResponse();
    }

    /**
     * Create an instance of {@link PersonnelEntity }
     * 
     */
    public PersonnelEntity createPersonnelEntity() {
        return new PersonnelEntity();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdAuthentication }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "idAuthentication")
    public JAXBElement<IdAuthentication> createIdAuthentication(IdAuthentication value) {
        return new JAXBElement<IdAuthentication>(_IdAuthentication_QNAME, IdAuthentication.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStaffInfoById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getStaffInfoById")
    public JAXBElement<GetStaffInfoById> createGetStaffInfoById(GetStaffInfoById value) {
        return new JAXBElement<GetStaffInfoById>(_GetStaffInfoById_QNAME, GetStaffInfoById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStaffInfoByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getStaffInfoByIdResponse")
    public JAXBElement<GetStaffInfoByIdResponse> createGetStaffInfoByIdResponse(GetStaffInfoByIdResponse value) {
        return new JAXBElement<GetStaffInfoByIdResponse>(_GetStaffInfoByIdResponse_QNAME, GetStaffInfoByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IdAuthenticationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "idAuthenticationResponse")
    public JAXBElement<IdAuthenticationResponse> createIdAuthenticationResponse(IdAuthenticationResponse value) {
        return new JAXBElement<IdAuthenticationResponse>(_IdAuthenticationResponse_QNAME, IdAuthenticationResponse.class, null, value);
    }

}
