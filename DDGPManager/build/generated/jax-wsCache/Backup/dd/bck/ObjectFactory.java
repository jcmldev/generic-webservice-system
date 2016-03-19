
package dd.bck;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the dd.bck package. 
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

    private final static QName _GPGetMetadataResponse_QNAME = new QName("http://dd/", "GP_getMetadataResponse");
    private final static QName _GPMultihopStoreBackupData_QNAME = new QName("http://dd/", "GP_Multihop_storeBackupData");
    private final static QName _GPGetBackupTargetsOfNode_QNAME = new QName("http://dd/", "GP_getBackupTargetsOfNode");
    private final static QName _GPStoreBackupData_QNAME = new QName("http://dd/", "GP_storeBackupData");
    private final static QName _GPMultihopStoreBackupDataResponse_QNAME = new QName("http://dd/", "GP_Multihop_storeBackupDataResponse");
    private final static QName _GPMultihopGetBackupDataResponse_QNAME = new QName("http://dd/", "GP_Multihop_getBackupDataResponse");
    private final static QName _DKLStoreIpDependence_QNAME = new QName("http://dd/", "DKL_storeIpDependence");
    private final static QName _GPAddMetadata_QNAME = new QName("http://dd/", "GP_addMetadata");
    private final static QName _GPMultihopGetInterDependenciesForTimeWindowResponse_QNAME = new QName("http://dd/", "GP_Multihop_getInterDependenciesForTimeWindowResponse");
    private final static QName _GPGetInterDependenciesForTimeWindowResponse_QNAME = new QName("http://dd/", "GP_getInterDependenciesForTimeWindowResponse");
    private final static QName _GPMultihopGetInterDependenciesForTimeWindow_QNAME = new QName("http://dd/", "GP_Multihop_getInterDependenciesForTimeWindow");
    private final static QName _DKLGetAntecedentServicesOfDependentIp_QNAME = new QName("http://dd/", "DKL_getAntecedentServicesOfDependentIp");
    private final static QName _DKLStoreIpDependenceResponse_QNAME = new QName("http://dd/", "DKL_storeIpDependenceResponse");
    private final static QName _GPGetBackupTargetsOfNodeResponse_QNAME = new QName("http://dd/", "GP_getBackupTargetsOfNodeResponse");
    private final static QName _GPGetBackupDataResponse_QNAME = new QName("http://dd/", "GP_getBackupDataResponse");
    private final static QName _GPAddTargetOfNode_QNAME = new QName("http://dd/", "GP_addTargetOfNode");
    private final static QName _GPGetDependentHostsResponse_QNAME = new QName("http://dd/", "GP_getDependentHostsResponse");
    private final static QName _GPAddTargetOfNodeResponse_QNAME = new QName("http://dd/", "GP_addTargetOfNodeResponse");
    private final static QName _GPGetInterDependenciesForTimeWindow_QNAME = new QName("http://dd/", "GP_getInterDependenciesForTimeWindow");
    private final static QName _GPGetMetadata_QNAME = new QName("http://dd/", "GP_getMetadata");
    private final static QName _GPGetDependentHosts_QNAME = new QName("http://dd/", "GP_getDependentHosts");
    private final static QName _DKLGetAntecedentServicesOfDependentIpResponse_QNAME = new QName("http://dd/", "DKL_getAntecedentServicesOfDependentIpResponse");
    private final static QName _GPGetBackupData_QNAME = new QName("http://dd/", "GP_getBackupData");
    private final static QName _GPMultihopGetBackupData_QNAME = new QName("http://dd/", "GP_Multihop_getBackupData");
    private final static QName _GPAddMetadataResponse_QNAME = new QName("http://dd/", "GP_addMetadataResponse");
    private final static QName _GPStoreBackupDataResponse_QNAME = new QName("http://dd/", "GP_storeBackupDataResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: dd.bck
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GPGetBackupData }
     * 
     */
    public GPGetBackupData createGPGetBackupData() {
        return new GPGetBackupData();
    }

    /**
     * Create an instance of {@link GPMultihopGetBackupData }
     * 
     */
    public GPMultihopGetBackupData createGPMultihopGetBackupData() {
        return new GPMultihopGetBackupData();
    }

    /**
     * Create an instance of {@link GPAddMetadataResponse }
     * 
     */
    public GPAddMetadataResponse createGPAddMetadataResponse() {
        return new GPAddMetadataResponse();
    }

    /**
     * Create an instance of {@link GPStoreBackupDataResponse }
     * 
     */
    public GPStoreBackupDataResponse createGPStoreBackupDataResponse() {
        return new GPStoreBackupDataResponse();
    }

    /**
     * Create an instance of {@link GPGetBackupDataResponse }
     * 
     */
    public GPGetBackupDataResponse createGPGetBackupDataResponse() {
        return new GPGetBackupDataResponse();
    }

    /**
     * Create an instance of {@link GPAddTargetOfNode }
     * 
     */
    public GPAddTargetOfNode createGPAddTargetOfNode() {
        return new GPAddTargetOfNode();
    }

    /**
     * Create an instance of {@link GPAddTargetOfNodeResponse }
     * 
     */
    public GPAddTargetOfNodeResponse createGPAddTargetOfNodeResponse() {
        return new GPAddTargetOfNodeResponse();
    }

    /**
     * Create an instance of {@link GPGetDependentHostsResponse }
     * 
     */
    public GPGetDependentHostsResponse createGPGetDependentHostsResponse() {
        return new GPGetDependentHostsResponse();
    }

    /**
     * Create an instance of {@link GPGetInterDependenciesForTimeWindow }
     * 
     */
    public GPGetInterDependenciesForTimeWindow createGPGetInterDependenciesForTimeWindow() {
        return new GPGetInterDependenciesForTimeWindow();
    }

    /**
     * Create an instance of {@link GPGetMetadata }
     * 
     */
    public GPGetMetadata createGPGetMetadata() {
        return new GPGetMetadata();
    }

    /**
     * Create an instance of {@link GPGetDependentHosts }
     * 
     */
    public GPGetDependentHosts createGPGetDependentHosts() {
        return new GPGetDependentHosts();
    }

    /**
     * Create an instance of {@link DKLGetAntecedentServicesOfDependentIpResponse }
     * 
     */
    public DKLGetAntecedentServicesOfDependentIpResponse createDKLGetAntecedentServicesOfDependentIpResponse() {
        return new DKLGetAntecedentServicesOfDependentIpResponse();
    }

    /**
     * Create an instance of {@link DKLStoreIpDependence }
     * 
     */
    public DKLStoreIpDependence createDKLStoreIpDependence() {
        return new DKLStoreIpDependence();
    }

    /**
     * Create an instance of {@link GPAddMetadata }
     * 
     */
    public GPAddMetadata createGPAddMetadata() {
        return new GPAddMetadata();
    }

    /**
     * Create an instance of {@link GPMultihopGetInterDependenciesForTimeWindowResponse }
     * 
     */
    public GPMultihopGetInterDependenciesForTimeWindowResponse createGPMultihopGetInterDependenciesForTimeWindowResponse() {
        return new GPMultihopGetInterDependenciesForTimeWindowResponse();
    }

    /**
     * Create an instance of {@link GPGetInterDependenciesForTimeWindowResponse }
     * 
     */
    public GPGetInterDependenciesForTimeWindowResponse createGPGetInterDependenciesForTimeWindowResponse() {
        return new GPGetInterDependenciesForTimeWindowResponse();
    }

    /**
     * Create an instance of {@link GPMultihopGetInterDependenciesForTimeWindow }
     * 
     */
    public GPMultihopGetInterDependenciesForTimeWindow createGPMultihopGetInterDependenciesForTimeWindow() {
        return new GPMultihopGetInterDependenciesForTimeWindow();
    }

    /**
     * Create an instance of {@link DKLGetAntecedentServicesOfDependentIp }
     * 
     */
    public DKLGetAntecedentServicesOfDependentIp createDKLGetAntecedentServicesOfDependentIp() {
        return new DKLGetAntecedentServicesOfDependentIp();
    }

    /**
     * Create an instance of {@link DKLStoreIpDependenceResponse }
     * 
     */
    public DKLStoreIpDependenceResponse createDKLStoreIpDependenceResponse() {
        return new DKLStoreIpDependenceResponse();
    }

    /**
     * Create an instance of {@link GPGetBackupTargetsOfNodeResponse }
     * 
     */
    public GPGetBackupTargetsOfNodeResponse createGPGetBackupTargetsOfNodeResponse() {
        return new GPGetBackupTargetsOfNodeResponse();
    }

    /**
     * Create an instance of {@link GPGetMetadataResponse }
     * 
     */
    public GPGetMetadataResponse createGPGetMetadataResponse() {
        return new GPGetMetadataResponse();
    }

    /**
     * Create an instance of {@link GPMultihopStoreBackupData }
     * 
     */
    public GPMultihopStoreBackupData createGPMultihopStoreBackupData() {
        return new GPMultihopStoreBackupData();
    }

    /**
     * Create an instance of {@link GPStoreBackupData }
     * 
     */
    public GPStoreBackupData createGPStoreBackupData() {
        return new GPStoreBackupData();
    }

    /**
     * Create an instance of {@link GPMultihopStoreBackupDataResponse }
     * 
     */
    public GPMultihopStoreBackupDataResponse createGPMultihopStoreBackupDataResponse() {
        return new GPMultihopStoreBackupDataResponse();
    }

    /**
     * Create an instance of {@link GPGetBackupTargetsOfNode }
     * 
     */
    public GPGetBackupTargetsOfNode createGPGetBackupTargetsOfNode() {
        return new GPGetBackupTargetsOfNode();
    }

    /**
     * Create an instance of {@link GPMultihopGetBackupDataResponse }
     * 
     */
    public GPMultihopGetBackupDataResponse createGPMultihopGetBackupDataResponse() {
        return new GPMultihopGetBackupDataResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPGetMetadataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_getMetadataResponse")
    public JAXBElement<GPGetMetadataResponse> createGPGetMetadataResponse(GPGetMetadataResponse value) {
        return new JAXBElement<GPGetMetadataResponse>(_GPGetMetadataResponse_QNAME, GPGetMetadataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPMultihopStoreBackupData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_Multihop_storeBackupData")
    public JAXBElement<GPMultihopStoreBackupData> createGPMultihopStoreBackupData(GPMultihopStoreBackupData value) {
        return new JAXBElement<GPMultihopStoreBackupData>(_GPMultihopStoreBackupData_QNAME, GPMultihopStoreBackupData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPGetBackupTargetsOfNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_getBackupTargetsOfNode")
    public JAXBElement<GPGetBackupTargetsOfNode> createGPGetBackupTargetsOfNode(GPGetBackupTargetsOfNode value) {
        return new JAXBElement<GPGetBackupTargetsOfNode>(_GPGetBackupTargetsOfNode_QNAME, GPGetBackupTargetsOfNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPStoreBackupData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_storeBackupData")
    public JAXBElement<GPStoreBackupData> createGPStoreBackupData(GPStoreBackupData value) {
        return new JAXBElement<GPStoreBackupData>(_GPStoreBackupData_QNAME, GPStoreBackupData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPMultihopStoreBackupDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_Multihop_storeBackupDataResponse")
    public JAXBElement<GPMultihopStoreBackupDataResponse> createGPMultihopStoreBackupDataResponse(GPMultihopStoreBackupDataResponse value) {
        return new JAXBElement<GPMultihopStoreBackupDataResponse>(_GPMultihopStoreBackupDataResponse_QNAME, GPMultihopStoreBackupDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPMultihopGetBackupDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_Multihop_getBackupDataResponse")
    public JAXBElement<GPMultihopGetBackupDataResponse> createGPMultihopGetBackupDataResponse(GPMultihopGetBackupDataResponse value) {
        return new JAXBElement<GPMultihopGetBackupDataResponse>(_GPMultihopGetBackupDataResponse_QNAME, GPMultihopGetBackupDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DKLStoreIpDependence }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "DKL_storeIpDependence")
    public JAXBElement<DKLStoreIpDependence> createDKLStoreIpDependence(DKLStoreIpDependence value) {
        return new JAXBElement<DKLStoreIpDependence>(_DKLStoreIpDependence_QNAME, DKLStoreIpDependence.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPAddMetadata }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_addMetadata")
    public JAXBElement<GPAddMetadata> createGPAddMetadata(GPAddMetadata value) {
        return new JAXBElement<GPAddMetadata>(_GPAddMetadata_QNAME, GPAddMetadata.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPMultihopGetInterDependenciesForTimeWindowResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_Multihop_getInterDependenciesForTimeWindowResponse")
    public JAXBElement<GPMultihopGetInterDependenciesForTimeWindowResponse> createGPMultihopGetInterDependenciesForTimeWindowResponse(GPMultihopGetInterDependenciesForTimeWindowResponse value) {
        return new JAXBElement<GPMultihopGetInterDependenciesForTimeWindowResponse>(_GPMultihopGetInterDependenciesForTimeWindowResponse_QNAME, GPMultihopGetInterDependenciesForTimeWindowResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPGetInterDependenciesForTimeWindowResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_getInterDependenciesForTimeWindowResponse")
    public JAXBElement<GPGetInterDependenciesForTimeWindowResponse> createGPGetInterDependenciesForTimeWindowResponse(GPGetInterDependenciesForTimeWindowResponse value) {
        return new JAXBElement<GPGetInterDependenciesForTimeWindowResponse>(_GPGetInterDependenciesForTimeWindowResponse_QNAME, GPGetInterDependenciesForTimeWindowResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPMultihopGetInterDependenciesForTimeWindow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_Multihop_getInterDependenciesForTimeWindow")
    public JAXBElement<GPMultihopGetInterDependenciesForTimeWindow> createGPMultihopGetInterDependenciesForTimeWindow(GPMultihopGetInterDependenciesForTimeWindow value) {
        return new JAXBElement<GPMultihopGetInterDependenciesForTimeWindow>(_GPMultihopGetInterDependenciesForTimeWindow_QNAME, GPMultihopGetInterDependenciesForTimeWindow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DKLGetAntecedentServicesOfDependentIp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "DKL_getAntecedentServicesOfDependentIp")
    public JAXBElement<DKLGetAntecedentServicesOfDependentIp> createDKLGetAntecedentServicesOfDependentIp(DKLGetAntecedentServicesOfDependentIp value) {
        return new JAXBElement<DKLGetAntecedentServicesOfDependentIp>(_DKLGetAntecedentServicesOfDependentIp_QNAME, DKLGetAntecedentServicesOfDependentIp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DKLStoreIpDependenceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "DKL_storeIpDependenceResponse")
    public JAXBElement<DKLStoreIpDependenceResponse> createDKLStoreIpDependenceResponse(DKLStoreIpDependenceResponse value) {
        return new JAXBElement<DKLStoreIpDependenceResponse>(_DKLStoreIpDependenceResponse_QNAME, DKLStoreIpDependenceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPGetBackupTargetsOfNodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_getBackupTargetsOfNodeResponse")
    public JAXBElement<GPGetBackupTargetsOfNodeResponse> createGPGetBackupTargetsOfNodeResponse(GPGetBackupTargetsOfNodeResponse value) {
        return new JAXBElement<GPGetBackupTargetsOfNodeResponse>(_GPGetBackupTargetsOfNodeResponse_QNAME, GPGetBackupTargetsOfNodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPGetBackupDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_getBackupDataResponse")
    public JAXBElement<GPGetBackupDataResponse> createGPGetBackupDataResponse(GPGetBackupDataResponse value) {
        return new JAXBElement<GPGetBackupDataResponse>(_GPGetBackupDataResponse_QNAME, GPGetBackupDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPAddTargetOfNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_addTargetOfNode")
    public JAXBElement<GPAddTargetOfNode> createGPAddTargetOfNode(GPAddTargetOfNode value) {
        return new JAXBElement<GPAddTargetOfNode>(_GPAddTargetOfNode_QNAME, GPAddTargetOfNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPGetDependentHostsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_getDependentHostsResponse")
    public JAXBElement<GPGetDependentHostsResponse> createGPGetDependentHostsResponse(GPGetDependentHostsResponse value) {
        return new JAXBElement<GPGetDependentHostsResponse>(_GPGetDependentHostsResponse_QNAME, GPGetDependentHostsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPAddTargetOfNodeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_addTargetOfNodeResponse")
    public JAXBElement<GPAddTargetOfNodeResponse> createGPAddTargetOfNodeResponse(GPAddTargetOfNodeResponse value) {
        return new JAXBElement<GPAddTargetOfNodeResponse>(_GPAddTargetOfNodeResponse_QNAME, GPAddTargetOfNodeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPGetInterDependenciesForTimeWindow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_getInterDependenciesForTimeWindow")
    public JAXBElement<GPGetInterDependenciesForTimeWindow> createGPGetInterDependenciesForTimeWindow(GPGetInterDependenciesForTimeWindow value) {
        return new JAXBElement<GPGetInterDependenciesForTimeWindow>(_GPGetInterDependenciesForTimeWindow_QNAME, GPGetInterDependenciesForTimeWindow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPGetMetadata }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_getMetadata")
    public JAXBElement<GPGetMetadata> createGPGetMetadata(GPGetMetadata value) {
        return new JAXBElement<GPGetMetadata>(_GPGetMetadata_QNAME, GPGetMetadata.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPGetDependentHosts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_getDependentHosts")
    public JAXBElement<GPGetDependentHosts> createGPGetDependentHosts(GPGetDependentHosts value) {
        return new JAXBElement<GPGetDependentHosts>(_GPGetDependentHosts_QNAME, GPGetDependentHosts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DKLGetAntecedentServicesOfDependentIpResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "DKL_getAntecedentServicesOfDependentIpResponse")
    public JAXBElement<DKLGetAntecedentServicesOfDependentIpResponse> createDKLGetAntecedentServicesOfDependentIpResponse(DKLGetAntecedentServicesOfDependentIpResponse value) {
        return new JAXBElement<DKLGetAntecedentServicesOfDependentIpResponse>(_DKLGetAntecedentServicesOfDependentIpResponse_QNAME, DKLGetAntecedentServicesOfDependentIpResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPGetBackupData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_getBackupData")
    public JAXBElement<GPGetBackupData> createGPGetBackupData(GPGetBackupData value) {
        return new JAXBElement<GPGetBackupData>(_GPGetBackupData_QNAME, GPGetBackupData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPMultihopGetBackupData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_Multihop_getBackupData")
    public JAXBElement<GPMultihopGetBackupData> createGPMultihopGetBackupData(GPMultihopGetBackupData value) {
        return new JAXBElement<GPMultihopGetBackupData>(_GPMultihopGetBackupData_QNAME, GPMultihopGetBackupData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPAddMetadataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_addMetadataResponse")
    public JAXBElement<GPAddMetadataResponse> createGPAddMetadataResponse(GPAddMetadataResponse value) {
        return new JAXBElement<GPAddMetadataResponse>(_GPAddMetadataResponse_QNAME, GPAddMetadataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GPStoreBackupDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dd/", name = "GP_storeBackupDataResponse")
    public JAXBElement<GPStoreBackupDataResponse> createGPStoreBackupDataResponse(GPStoreBackupDataResponse value) {
        return new JAXBElement<GPStoreBackupDataResponse>(_GPStoreBackupDataResponse_QNAME, GPStoreBackupDataResponse.class, null, value);
    }

}
