
package test;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "GpAndDklService", targetNamespace = "http://backup.dd/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface GpAndDklService {


    /**
     * 
     * @param nodeId
     * @param serviceId
     * @param fromTimestamp
     * @param toTimestamp
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod(operationName = "GP_getInterDependenciesForTimeWindow")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "GP_getInterDependenciesForTimeWindow", targetNamespace = "http://backup.dd/", className = "test.GPGetInterDependenciesForTimeWindow")
    @ResponseWrapper(localName = "GP_getInterDependenciesForTimeWindowResponse", targetNamespace = "http://backup.dd/", className = "test.GPGetInterDependenciesForTimeWindowResponse")
    @Action(input = "http://backup.dd/GpAndDklService/GP_getInterDependenciesForTimeWindowRequest", output = "http://backup.dd/GpAndDklService/GP_getInterDependenciesForTimeWindowResponse")
    public List<String> gpGetInterDependenciesForTimeWindow(
        @WebParam(name = "nodeId", targetNamespace = "")
        String nodeId,
        @WebParam(name = "serviceId", targetNamespace = "")
        String serviceId,
        @WebParam(name = "fromTimestamp", targetNamespace = "")
        long fromTimestamp,
        @WebParam(name = "toTimestamp", targetNamespace = "")
        long toTimestamp);

    /**
     * 
     * @param dependentIp
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod(operationName = "DKL_getAntecedentServicesOfDependentIp")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "DKL_getAntecedentServicesOfDependentIp", targetNamespace = "http://backup.dd/", className = "test.DKLGetAntecedentServicesOfDependentIp")
    @ResponseWrapper(localName = "DKL_getAntecedentServicesOfDependentIpResponse", targetNamespace = "http://backup.dd/", className = "test.DKLGetAntecedentServicesOfDependentIpResponse")
    @Action(input = "http://backup.dd/GpAndDklService/DKL_getAntecedentServicesOfDependentIpRequest", output = "http://backup.dd/GpAndDklService/DKL_getAntecedentServicesOfDependentIpResponse")
    public List<String> dklGetAntecedentServicesOfDependentIp(
        @WebParam(name = "dependentIp", targetNamespace = "")
        String dependentIp);

    /**
     * 
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod(operationName = "GP_getDependentHosts")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "GP_getDependentHosts", targetNamespace = "http://backup.dd/", className = "test.GPGetDependentHosts")
    @ResponseWrapper(localName = "GP_getDependentHostsResponse", targetNamespace = "http://backup.dd/", className = "test.GPGetDependentHostsResponse")
    @Action(input = "http://backup.dd/GpAndDklService/GP_getDependentHostsRequest", output = "http://backup.dd/GpAndDklService/GP_getDependentHostsResponse")
    public List<String> gpGetDependentHosts();

    /**
     * 
     * @param timeSlotSize
     * @param fromTimestamp
     * @param toTimestamp
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod(operationName = "GP_getBackupData")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "GP_getBackupData", targetNamespace = "http://backup.dd/", className = "test.GPGetBackupData")
    @ResponseWrapper(localName = "GP_getBackupDataResponse", targetNamespace = "http://backup.dd/", className = "test.GPGetBackupDataResponse")
    @Action(input = "http://backup.dd/GpAndDklService/GP_getBackupDataRequest", output = "http://backup.dd/GpAndDklService/GP_getBackupDataResponse")
    public List<String> gpGetBackupData(
        @WebParam(name = "fromTimestamp", targetNamespace = "")
        long fromTimestamp,
        @WebParam(name = "toTimestamp", targetNamespace = "")
        long toTimestamp,
        @WebParam(name = "timeSlotSize", targetNamespace = "")
        int timeSlotSize);

    /**
     * 
     * @param nodeId
     * @param timeSlotSize
     * @param fromTimestamp
     * @param backupData
     * @param toTimestamp
     */
    @WebMethod(operationName = "GP_storeBackupData")
    @RequestWrapper(localName = "GP_storeBackupData", targetNamespace = "http://backup.dd/", className = "test.GPStoreBackupData")
    @ResponseWrapper(localName = "GP_storeBackupDataResponse", targetNamespace = "http://backup.dd/", className = "test.GPStoreBackupDataResponse")
    @Action(input = "http://backup.dd/GpAndDklService/GP_storeBackupDataRequest", output = "http://backup.dd/GpAndDklService/GP_storeBackupDataResponse")
    public void gpStoreBackupData(
        @WebParam(name = "nodeId", targetNamespace = "")
        String nodeId,
        @WebParam(name = "fromTimestamp", targetNamespace = "")
        long fromTimestamp,
        @WebParam(name = "toTimestamp", targetNamespace = "")
        long toTimestamp,
        @WebParam(name = "timeSlotSize", targetNamespace = "")
        int timeSlotSize,
        @WebParam(name = "backupData", targetNamespace = "")
        List<String> backupData);

    /**
     * 
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod(operationName = "GP_getMetadata")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "GP_getMetadata", targetNamespace = "http://backup.dd/", className = "test.GPGetMetadata")
    @ResponseWrapper(localName = "GP_getMetadataResponse", targetNamespace = "http://backup.dd/", className = "test.GPGetMetadataResponse")
    @Action(input = "http://backup.dd/GpAndDklService/GP_getMetadataRequest", output = "http://backup.dd/GpAndDklService/GP_getMetadataResponse")
    public List<String> gpGetMetadata();

    /**
     * 
     * @param nodeId
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod(operationName = "GP_getBackupTargetsOfNode")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "GP_getBackupTargetsOfNode", targetNamespace = "http://backup.dd/", className = "test.GPGetBackupTargetsOfNode")
    @ResponseWrapper(localName = "GP_getBackupTargetsOfNodeResponse", targetNamespace = "http://backup.dd/", className = "test.GPGetBackupTargetsOfNodeResponse")
    @Action(input = "http://backup.dd/GpAndDklService/GP_getBackupTargetsOfNodeRequest", output = "http://backup.dd/GpAndDklService/GP_getBackupTargetsOfNodeResponse")
    public List<String> gpGetBackupTargetsOfNode(
        @WebParam(name = "nodeId", targetNamespace = "")
        String nodeId);

    /**
     * 
     * @param metadataRecords
     */
    @WebMethod(operationName = "GP_addMetadata")
    @RequestWrapper(localName = "GP_addMetadata", targetNamespace = "http://backup.dd/", className = "test.GPAddMetadata")
    @ResponseWrapper(localName = "GP_addMetadataResponse", targetNamespace = "http://backup.dd/", className = "test.GPAddMetadataResponse")
    @Action(input = "http://backup.dd/GpAndDklService/GP_addMetadataRequest", output = "http://backup.dd/GpAndDklService/GP_addMetadataResponse")
    public void gpAddMetadata(
        @WebParam(name = "metadataRecords", targetNamespace = "")
        List<String> metadataRecords);

    /**
     * 
     * @param nodeId
     * @param targetId
     */
    @WebMethod(operationName = "GP_addTargetOfNode")
    @RequestWrapper(localName = "GP_addTargetOfNode", targetNamespace = "http://backup.dd/", className = "test.GPAddTargetOfNode")
    @ResponseWrapper(localName = "GP_addTargetOfNodeResponse", targetNamespace = "http://backup.dd/", className = "test.GPAddTargetOfNodeResponse")
    @Action(input = "http://backup.dd/GpAndDklService/GP_addTargetOfNodeRequest", output = "http://backup.dd/GpAndDklService/GP_addTargetOfNodeResponse")
    public void gpAddTargetOfNode(
        @WebParam(name = "nodeId", targetNamespace = "")
        String nodeId,
        @WebParam(name = "targetId", targetNamespace = "")
        String targetId);

    /**
     * 
     * @param dependentIp
     * @param antecedentService
     */
    @WebMethod(operationName = "DKL_storeIpDependence")
    @RequestWrapper(localName = "DKL_storeIpDependence", targetNamespace = "http://backup.dd/", className = "test.DKLStoreIpDependence")
    @ResponseWrapper(localName = "DKL_storeIpDependenceResponse", targetNamespace = "http://backup.dd/", className = "test.DKLStoreIpDependenceResponse")
    @Action(input = "http://backup.dd/GpAndDklService/DKL_storeIpDependenceRequest", output = "http://backup.dd/GpAndDklService/DKL_storeIpDependenceResponse")
    public void dklStoreIpDependence(
        @WebParam(name = "dependentIp", targetNamespace = "")
        String dependentIp,
        @WebParam(name = "antecedentService", targetNamespace = "")
        String antecedentService);

    /**
     * 
     * @param lastSuccessfulyTransferedBackupData
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod(operationName = "GP_Multihop_getBackupData")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "GP_Multihop_getBackupData", targetNamespace = "http://backup.dd/", className = "test.GPMultihopGetBackupData")
    @ResponseWrapper(localName = "GP_Multihop_getBackupDataResponse", targetNamespace = "http://backup.dd/", className = "test.GPMultihopGetBackupDataResponse")
    @Action(input = "http://backup.dd/GpAndDklService/GP_Multihop_getBackupDataRequest", output = "http://backup.dd/GpAndDklService/GP_Multihop_getBackupDataResponse")
    public List<String> gpMultihopGetBackupData(
        @WebParam(name = "lastSuccessfulyTransferedBackupData", targetNamespace = "")
        List<String> lastSuccessfulyTransferedBackupData);

    /**
     * 
     * @param backupData
     */
    @WebMethod(operationName = "GP_Multihop_storeBackupData")
    @RequestWrapper(localName = "GP_Multihop_storeBackupData", targetNamespace = "http://backup.dd/", className = "test.GPMultihopStoreBackupData")
    @ResponseWrapper(localName = "GP_Multihop_storeBackupDataResponse", targetNamespace = "http://backup.dd/", className = "test.GPMultihopStoreBackupDataResponse")
    @Action(input = "http://backup.dd/GpAndDklService/GP_Multihop_storeBackupDataRequest", output = "http://backup.dd/GpAndDklService/GP_Multihop_storeBackupDataResponse")
    public void gpMultihopStoreBackupData(
        @WebParam(name = "backupData", targetNamespace = "")
        List<String> backupData);

    /**
     * 
     * @param fromTimestamp
     * @param toTimestamp
     * @param dependentServiceId
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod(operationName = "GP_Multihop_getInterDependenciesForTimeWindow")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "GP_Multihop_getInterDependenciesForTimeWindow", targetNamespace = "http://backup.dd/", className = "test.GPMultihopGetInterDependenciesForTimeWindow")
    @ResponseWrapper(localName = "GP_Multihop_getInterDependenciesForTimeWindowResponse", targetNamespace = "http://backup.dd/", className = "test.GPMultihopGetInterDependenciesForTimeWindowResponse")
    @Action(input = "http://backup.dd/GpAndDklService/GP_Multihop_getInterDependenciesForTimeWindowRequest", output = "http://backup.dd/GpAndDklService/GP_Multihop_getInterDependenciesForTimeWindowResponse")
    public List<String> gpMultihopGetInterDependenciesForTimeWindow(
        @WebParam(name = "dependentServiceId", targetNamespace = "")
        String dependentServiceId,
        @WebParam(name = "fromTimestamp", targetNamespace = "")
        long fromTimestamp,
        @WebParam(name = "toTimestamp", targetNamespace = "")
        long toTimestamp);

}
