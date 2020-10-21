
package com.example.backend.service.impl.controllerWS.erpService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.backend.service.impl.controllerWS.erpService package. 
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

    private final static QName _GetAllLineResources_QNAME = new QName("http://Service/", "getAllLineResources");
    private final static QName _GetAllBOMs_QNAME = new QName("http://Service/", "getAllBOMs");
    private final static QName _GetResourceTeamInfoResponse_QNAME = new QName("http://Service/", "getResourceTeamInfoResponse");
    private final static QName _GetAllBOMsResponse_QNAME = new QName("http://Service/", "getAllBOMsResponse");
    private final static QName _GetMaterialInfoById_QNAME = new QName("http://Service/", "getMaterialInfoById");
    private final static QName _GetLineResourceByIdResponse_QNAME = new QName("http://Service/", "getLineResourceByIdResponse");
    private final static QName _GetBOMByIdResponse_QNAME = new QName("http://Service/", "getBOMByIdResponse");
    private final static QName _GetResourceTeamInfo_QNAME = new QName("http://Service/", "getResourceTeamInfo");
    private final static QName _GetLineResourceById_QNAME = new QName("http://Service/", "getLineResourceById");
    private final static QName _GetBOMById_QNAME = new QName("http://Service/", "getBOMById");
    private final static QName _GetAllLineResourcesResponse_QNAME = new QName("http://Service/", "getAllLineResourcesResponse");
    private final static QName _GetMaterialInfoByIdResponse_QNAME = new QName("http://Service/", "getMaterialInfoByIdResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.backend.service.impl.controllerWS.erpService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetBOMByIdResponse }
     * 
     */
    public GetBOMByIdResponse createGetBOMByIdResponse() {
        return new GetBOMByIdResponse();
    }

    /**
     * Create an instance of {@link GetResourceTeamInfo }
     * 
     */
    public GetResourceTeamInfo createGetResourceTeamInfo() {
        return new GetResourceTeamInfo();
    }

    /**
     * Create an instance of {@link GetLineResourceById }
     * 
     */
    public GetLineResourceById createGetLineResourceById() {
        return new GetLineResourceById();
    }

    /**
     * Create an instance of {@link GetAllLineResourcesResponse }
     * 
     */
    public GetAllLineResourcesResponse createGetAllLineResourcesResponse() {
        return new GetAllLineResourcesResponse();
    }

    /**
     * Create an instance of {@link GetMaterialInfoByIdResponse }
     * 
     */
    public GetMaterialInfoByIdResponse createGetMaterialInfoByIdResponse() {
        return new GetMaterialInfoByIdResponse();
    }

    /**
     * Create an instance of {@link GetBOMById }
     * 
     */
    public GetBOMById createGetBOMById() {
        return new GetBOMById();
    }

    /**
     * Create an instance of {@link GetAllBOMs }
     * 
     */
    public GetAllBOMs createGetAllBOMs() {
        return new GetAllBOMs();
    }

    /**
     * Create an instance of {@link GetResourceTeamInfoResponse }
     * 
     */
    public GetResourceTeamInfoResponse createGetResourceTeamInfoResponse() {
        return new GetResourceTeamInfoResponse();
    }

    /**
     * Create an instance of {@link GetAllLineResources }
     * 
     */
    public GetAllLineResources createGetAllLineResources() {
        return new GetAllLineResources();
    }

    /**
     * Create an instance of {@link GetAllBOMsResponse }
     * 
     */
    public GetAllBOMsResponse createGetAllBOMsResponse() {
        return new GetAllBOMsResponse();
    }

    /**
     * Create an instance of {@link GetMaterialInfoById }
     * 
     */
    public GetMaterialInfoById createGetMaterialInfoById() {
        return new GetMaterialInfoById();
    }

    /**
     * Create an instance of {@link GetLineResourceByIdResponse }
     * 
     */
    public GetLineResourceByIdResponse createGetLineResourceByIdResponse() {
        return new GetLineResourceByIdResponse();
    }

    /**
     * Create an instance of {@link MaterialEntity }
     * 
     */
    public MaterialEntity createMaterialEntity() {
        return new MaterialEntity();
    }

    /**
     * Create an instance of {@link LineEntity }
     * 
     */
    public LineEntity createLineEntity() {
        return new LineEntity();
    }

    /**
     * Create an instance of {@link ResourceEntity }
     * 
     */
    public ResourceEntity createResourceEntity() {
        return new ResourceEntity();
    }

    /**
     * Create an instance of {@link BomEntity }
     * 
     */
    public BomEntity createBomEntity() {
        return new BomEntity();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllLineResources }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getAllLineResources")
    public JAXBElement<GetAllLineResources> createGetAllLineResources(GetAllLineResources value) {
        return new JAXBElement<GetAllLineResources>(_GetAllLineResources_QNAME, GetAllLineResources.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllBOMs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getAllBOMs")
    public JAXBElement<GetAllBOMs> createGetAllBOMs(GetAllBOMs value) {
        return new JAXBElement<GetAllBOMs>(_GetAllBOMs_QNAME, GetAllBOMs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResourceTeamInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getResourceTeamInfoResponse")
    public JAXBElement<GetResourceTeamInfoResponse> createGetResourceTeamInfoResponse(GetResourceTeamInfoResponse value) {
        return new JAXBElement<GetResourceTeamInfoResponse>(_GetResourceTeamInfoResponse_QNAME, GetResourceTeamInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllBOMsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getAllBOMsResponse")
    public JAXBElement<GetAllBOMsResponse> createGetAllBOMsResponse(GetAllBOMsResponse value) {
        return new JAXBElement<GetAllBOMsResponse>(_GetAllBOMsResponse_QNAME, GetAllBOMsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMaterialInfoById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getMaterialInfoById")
    public JAXBElement<GetMaterialInfoById> createGetMaterialInfoById(GetMaterialInfoById value) {
        return new JAXBElement<GetMaterialInfoById>(_GetMaterialInfoById_QNAME, GetMaterialInfoById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLineResourceByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getLineResourceByIdResponse")
    public JAXBElement<GetLineResourceByIdResponse> createGetLineResourceByIdResponse(GetLineResourceByIdResponse value) {
        return new JAXBElement<GetLineResourceByIdResponse>(_GetLineResourceByIdResponse_QNAME, GetLineResourceByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBOMByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getBOMByIdResponse")
    public JAXBElement<GetBOMByIdResponse> createGetBOMByIdResponse(GetBOMByIdResponse value) {
        return new JAXBElement<GetBOMByIdResponse>(_GetBOMByIdResponse_QNAME, GetBOMByIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResourceTeamInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getResourceTeamInfo")
    public JAXBElement<GetResourceTeamInfo> createGetResourceTeamInfo(GetResourceTeamInfo value) {
        return new JAXBElement<GetResourceTeamInfo>(_GetResourceTeamInfo_QNAME, GetResourceTeamInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLineResourceById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getLineResourceById")
    public JAXBElement<GetLineResourceById> createGetLineResourceById(GetLineResourceById value) {
        return new JAXBElement<GetLineResourceById>(_GetLineResourceById_QNAME, GetLineResourceById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBOMById }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getBOMById")
    public JAXBElement<GetBOMById> createGetBOMById(GetBOMById value) {
        return new JAXBElement<GetBOMById>(_GetBOMById_QNAME, GetBOMById.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllLineResourcesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getAllLineResourcesResponse")
    public JAXBElement<GetAllLineResourcesResponse> createGetAllLineResourcesResponse(GetAllLineResourcesResponse value) {
        return new JAXBElement<GetAllLineResourcesResponse>(_GetAllLineResourcesResponse_QNAME, GetAllLineResourcesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMaterialInfoByIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Service/", name = "getMaterialInfoByIdResponse")
    public JAXBElement<GetMaterialInfoByIdResponse> createGetMaterialInfoByIdResponse(GetMaterialInfoByIdResponse value) {
        return new JAXBElement<GetMaterialInfoByIdResponse>(_GetMaterialInfoByIdResponse_QNAME, GetMaterialInfoByIdResponse.class, null, value);
    }

}
