
package com.example.backend.service.impl.controllerWS.attendanceService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.backend.service.impl.controllerWS.attendanceService package. 
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

    private final static QName _GetCalendarInfo_QNAME = new QName("http://Service/", "getCalendarInfo");
    private final static QName _GetClassInfoResponse_QNAME = new QName("http://Service/", "getClassInfoResponse");
    private final static QName _GetCalendarInfoResponse_QNAME = new QName("http://Service/", "getCalendarInfoResponse");
    private final static QName _GetClassInfo_QNAME = new QName("http://Service/", "getClassInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.backend.service.impl.controllerWS.attendanceService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetCalendarInfoResponse }
     * 
     */
    public GetCalendarInfoResponse createGetCalendarInfoResponse() {
        return new GetCalendarInfoResponse();
    }

    /**
     * Create an instance of {@link GetClassInfo }
     * 
     */
    public GetClassInfo createGetClassInfo() {
        return new GetClassInfo();
    }

    /**
     * Create an instance of {@link GetCalendarInfo }
     * 
     */
    public GetCalendarInfo createGetCalendarInfo() {
        return new GetCalendarInfo();
    }

    /**
     * Create an instance of {@link GetClassInfoResponse }
     * 
     */
    public GetClassInfoResponse createGetClassInfoResponse() {
        return new GetClassInfoResponse();
    }

    /**
     * Create an instance of {@link ClassEntity }
     * 
     */
    public ClassEntity createClassEntity() {
        return new ClassEntity();
    }

    /**
     * Create an instance of {@link CalendarEntity }
     * 
     */
    public CalendarEntity createCalendarEntity() {
        return new CalendarEntity();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCalendarInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getCalendarInfo")
    public JAXBElement<GetCalendarInfo> createGetCalendarInfo(GetCalendarInfo value) {
        return new JAXBElement<GetCalendarInfo>(_GetCalendarInfo_QNAME, GetCalendarInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetClassInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getClassInfoResponse")
    public JAXBElement<GetClassInfoResponse> createGetClassInfoResponse(GetClassInfoResponse value) {
        return new JAXBElement<GetClassInfoResponse>(_GetClassInfoResponse_QNAME, GetClassInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCalendarInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getCalendarInfoResponse")
    public JAXBElement<GetCalendarInfoResponse> createGetCalendarInfoResponse(GetCalendarInfoResponse value) {
        return new JAXBElement<GetCalendarInfoResponse>(_GetCalendarInfoResponse_QNAME, GetCalendarInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetClassInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getClassInfo")
    public JAXBElement<GetClassInfo> createGetClassInfo(GetClassInfo value) {
        return new JAXBElement<GetClassInfo>(_GetClassInfo_QNAME, GetClassInfo.class, null, value);
    }

}
