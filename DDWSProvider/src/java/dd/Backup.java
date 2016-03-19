
package dd;

import dd.monitor.BackupServer;
import dd.monitor.ServerMonitor;
import dd.monitor.multihopgp.MultihopGpBackupServer;
import dd.output.LoggingContext;
import dd.output.ServiceGtTrace;
import fl.ServiceFaultGtTrace;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;


@WebService(serviceName = "Backup")
public class Backup {

    @Resource  
    WebServiceContext m_webServiceContext;

    private String getRequestIp()
    {
        MessageContext mc = m_webServiceContext.getMessageContext();  
        HttpServletRequest req = (HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST);  
        
        
        return req.getRemoteHost();
    }
    

    @WebMethod(operationName = "GP_getDependentHosts")
    public String[] GP_getDependentHosts() 
    {
        LoggingContext          log = LoggingContext.getContext(this, "");
        String[]                result;
        
        
        log.log("GP_getDependentHosts");
        ServerMonitor.getInstance().getDepedentHosts().writeOut(log);
        
        result = ServerMonitor.getInstance().getDepedentHosts().getHosts();
        
        log.log("result", result);
        
        return result;
    }

    
    @WebMethod(operationName = "GP_getBackupData")
    public String[] GP_getBackupData(
            @WebParam(name = "fromTimestamp") long fromTimestamp,
            @WebParam(name = "toTimestamp") long toTimestamp,
            @WebParam(name = "timeSlotSize") int timeSlotSize) 
    {
        LoggingContext          log = LoggingContext.getContext(this, "");
        String[]                result;
        
        
        log.log("GP_getBackupData");
        log.log("fromTimestamp=" + fromTimestamp);
        log.log("toTimestamp=" + toTimestamp);
        log.log("timeSlotSize=" + timeSlotSize);
        
        ServerMonitor.getInstance().writeOut(log);
        
        result = ServerMonitor.getInstance().getBackupData(
                fromTimestamp, 
                toTimestamp, 
                timeSlotSize);
        
        log.log("result", result);
        
        return result;
    }
    
    @WebMethod(operationName = "GP_storeBackupData")
    public void GP_storeBackupData(
            @WebParam(name = "nodeId") String nodeId, 
            @WebParam(name = "fromTimestamp") long fromTimestamp, 
            @WebParam(name = "toTimestamp") long toTimestamp, 
            @WebParam(name = "timeSlotSize") int timeSlotSize, 
            @WebParam(name = "backupData") String[] backupData) 
    {
        LoggingContext          log = LoggingContext.getContext(this, "");
        
        
        log.log("GP_storeBackupData");
        log.log("nodeId=" + nodeId);
        log.log("fromTimestamp=" + fromTimestamp);
        log.log("toTimestamp=" + toTimestamp);
        log.log("timeSlotSize=" + timeSlotSize);
        log.log("backupData", backupData);
        
        BackupServer.getInstance().storeBackupData(
                nodeId, 
                fromTimestamp, 
                toTimestamp, 
                timeSlotSize, 
                backupData);
        
        ServerMonitor.getInstance().writeOut(log);
    }
    
    /*
     return value null - means the dependence data for the time window are not available
     return empty or full array - means dependence data for the time window are available
     (what if data are available only for part of the time window?)
     */
    @WebMethod(operationName = "GP_getInterDependenciesForTimeWindow")
    public String[] GP_getInterDependenciesForTimeWindow(
            @WebParam(name = "nodeId") String nodeId, 
            @WebParam(name = "serviceId") String serviceId, 
            @WebParam(name = "fromTimestamp") long fromTimestamp, 
            @WebParam(name = "toTimestamp") long toTimestamp)
    {
        LoggingContext          log = LoggingContext.getContext(this, "");
        String[]                result;
        
        
        log.log("GP_getInterDependenciesForTimeWindow");
        log.log("nodeId=" + nodeId);
        log.log("serviceId=" + serviceId);
        log.log("fromTimestamp=" + fromTimestamp);
        log.log("toTimestamp=" + toTimestamp);
        
        BackupServer.getInstance().getMetadataCache().writeOut(log);
        
        result = BackupServer.getInstance().getInterDependenciesForTimeWindow(
                nodeId, 
                serviceId, 
                fromTimestamp, 
                toTimestamp);
        
        log.log("result", result);
        
        return result;
    }

    @WebMethod(operationName = "GP_getMetadata")
    public String[] GP_getMetadata() 
    {
        LoggingContext          log = LoggingContext.getContext(this, "");
        String[]                result;
        
        
        log.log("GP_getMetadata");
        
        BackupServer.getInstance().getMetadataCache().writeOut(log);
        
        result = BackupServer.getInstance().getMetadataCache().getMetadata();
        
        log.log("result", result);
        
        return result;
    }
    
    @WebMethod(operationName = "GP_getBackupTargetsOfNode")
    public String[] GP_getBackupTargetsOfNode(
            @WebParam(name = "nodeId") String nodeId) 
    {
        LoggingContext          log = LoggingContext.getContext(this, "");
        String[]                result;
        
        
        log.log("GP_getBackupTargetsOfNode");
        log.log("nodeId=" + nodeId);
        
        BackupServer.getInstance().getMetadataCache().writeOut(log);
        
        result = BackupServer.getInstance().getMetadataCache().getBackupTargetsOfNode(nodeId);
        
        log.log("result", result);
        
        return result;
    }

    @WebMethod(operationName = "GP_addMetadata")
    public void GP_addMetadata(
            @WebParam(name = "metadataRecords") String[] metadataRecords) 
    {
        LoggingContext          log = LoggingContext.getContext(this, "");
        
        
        log.log("GP_addMetadata");
        log.log("metadataRecords", metadataRecords);
        
        BackupServer.getInstance().getMetadataCache().addMetadata(metadataRecords);
        
        BackupServer.getInstance().getMetadataCache().writeOut(log);
    }

    @WebMethod(operationName = "GP_addTargetOfNode")
    public void GP_addTargetOfNode(
            @WebParam(name = "nodeId") String nodeId,
            @WebParam(name = "targetId") String targetId) 
    {
        LoggingContext          log = LoggingContext.getContext(this, "");
        
        
        log.log("GP_addTargetOfNode");
        log.log("nodeId=" + nodeId);
        log.log("targetId=" + targetId);
        
        BackupServer.getInstance().getMetadataCache().addTargetOfNode(nodeId, targetId);
        
        BackupServer.getInstance().getMetadataCache().writeOut(log);
    }
    
    @WebMethod(operationName = "DKL_storeIpDependence")
    public void DKL_storeIpDependence(
            @WebParam(name = "dependentIp") String dependentIp, 
            @WebParam(name = "antecedentService") String antecedentService) 
    {
        LoggingContext          log = LoggingContext.getContext(this, "");
        
        
        log.log("DKL_storeIpDependence");
        log.log("dependentIp=" + dependentIp);
        log.log("antecedentService=" + antecedentService);
        
        BackupServer.getInstance().DKL_storeIpDependence(dependentIp, antecedentService);
    }
    
    @WebMethod(operationName = "DKL_getAntecedentServicesOfDependentIp")
    public String[] DKL_getAntecedentServicesOfDependentIp(
            @WebParam(name = "dependentIp") String dependentIp) 
    {
        LoggingContext          log = LoggingContext.getContext(this, "");
        String[]                result;
        
        
        log.log("DKL_getAntecedentServicesOfDependentIp");
        log.log("dependentIp=" + dependentIp);
        
        result = BackupServer.getInstance().DKL_getAntecedentServicesOfDependentIp(dependentIp);
        
        log.log("result", result);
        
        return result;
    }
    
        
    @WebMethod(operationName = "GP_Multihop_getBackupData")
    public String[] GP_Multihop_getBackupData(
            @WebParam(name = "lastSuccessfulyTransferedBackupData") String[] lastSuccessfulyTransferedBackupData) 
    {
        try
        {
            LoggingContext          log = LoggingContext.getContext(this, "");
            String[]                result;

            //log.log("thread count: " + Thread.activeCount());
            
            log.log("GP_Multihop_getBackupData");
            log.log("lastSuccessfulyTransferedBackupData size = " + lastSuccessfulyTransferedBackupData.length);
            //log.log("lastSuccessfulyTransferedBackupData", lastSuccessfulyTransferedBackupData);
            
            result = MultihopGpBackupServer.getInstance(getRequestIp()).getBackupData(
                    lastSuccessfulyTransferedBackupData,
                    true);

            log.log("result size = " + result.length);

            return result;
        }
        catch(Exception ex)
        {
            getCrashContext().log(ex);
            return null;
        }
    }
    
    private LoggingContext getCrashContext()
    {
        return LoggingContext.getContext(this, "");
        //return LoggingContext.getContext(new Exception(), "");
    }
    
    @WebMethod(operationName = "GP_Multihop_storeBackupData")
    public void GP_Multihop_storeBackupData(
            @WebParam(name = "backupData") String[] backupData) 
    {
        try
        {
            LoggingContext          log = LoggingContext.getContext(this, getRequestIp());

            //log.log("thread count: " + Thread.activeCount());
            
            log.log("GP_Multihop_storeBackupData");
            log.log("backupData", backupData, true);

            MultihopGpBackupServer.getInstance(getRequestIp()).storeBackupData(
                    backupData);

            // source of cuncurrency exception in combination with writing into store !!!
            //ServerMonitor.getInstance().writeOut(log);
        }
        catch(Exception ex)
        {
            getCrashContext().log(ex);
        }
    }
    
    @WebMethod(operationName = "GP_Multihop_getInterDependenciesForTimeWindow")
    public String[] GP_Multihop_getInterDependenciesForTimeWindow(
            @WebParam(name = "dependentServiceId") String dependentServiceId, 
            @WebParam(name = "fromTimestamp") long fromTimestamp, 
            @WebParam(name = "toTimestamp") long toTimestamp)
    {
        try
        {
            LoggingContext          log = LoggingContext.getContext(this, "");
            String[]                result;

            //log.log("thread count: " + Thread.activeCount());
            
            log.log("GP_Multihop_getInterDependenciesForTimeWindow");
            log.log("serviceId=" + dependentServiceId);
            log.log("fromTimestamp=" + fromTimestamp);
            log.log("toTimestamp=" + toTimestamp);

            result = MultihopGpBackupServer.getInstance(getRequestIp()).getInterDependenciesForTimeWindow(
                    dependentServiceId, 
                    fromTimestamp, 
                    toTimestamp); 

            log.log("result", result, true);

            return result;
            
        }
        catch(Exception ex)
        {
            getCrashContext().log(ex);
            return null;
        }
    }
    
    @WebMethod(operationName = "GP_Multihop_getInterDependenciesForTimeWindowAntecedent")
    public String[] GP_Multihop_getInterDependenciesForTimeWindowAntecedent(
            @WebParam(name = "dependentServiceId") String dependentServiceId, 
            @WebParam(name = "fromTimestamp") long fromTimestamp, 
            @WebParam(name = "toTimestamp") long toTimestamp)
    {
        try
        {
            LoggingContext          log = LoggingContext.getContext(this, "");
            String[]                result;

            //log.log("thread count: " + Thread.activeCount());
            
            log.log("GP_Multihop_getInterDependenciesForTimeWindowAntecedent");
            log.log("serviceId=" + dependentServiceId);
            log.log("fromTimestamp=" + fromTimestamp);
            log.log("toTimestamp=" + toTimestamp);

            result = MultihopGpBackupServer.getInstance(getRequestIp()).getInterDependenciesForTimeWindowAntecedent(
                    dependentServiceId, 
                    fromTimestamp, 
                    toTimestamp); 

            //log.log("result", result);

            return result;   
        }
        catch(Exception ex)
        {
            getCrashContext().log(ex);
            return null;
        }
    }    

    @WebMethod(operationName = "Monitor_recordClientDependence")
    public void Monitor_recordClientDependence(
            @WebParam(name = "clientProcessId") String clientProcessId,
            @WebParam(name = "frontEndServiceId") String frontEndServiceId,
            @WebParam(name = "dependenceTimestamp") long dependenceTimestamp)
    {
        try
        {
            LoggingContext          log = LoggingContext.getContext(this, "");

            log.log("Monitor_recordClientDependence");
            log.log("clientProcessId", clientProcessId);
            log.log("frontEndServiceId", frontEndServiceId);
            log.log("dependenceTimestamp", dependenceTimestamp);

            ServerMonitor           serverMonitor = ServerMonitor.getInstance();
        
        
            serverMonitor.addInterDependencyOccurrence(
                    clientProcessId, 
                    frontEndServiceId,
                    dependenceTimestamp);            
        }
        catch(Exception ex)
        {
            getCrashContext().log(ex);
        }
    }    
    
    @WebMethod(operationName = "Monitor_recordClientConversationFault")
    public void Monitor_recordClientFault(
            @WebParam(name = "conversationId") int conversationId,
            @WebParam(name = "clientProcessId") String clientProcessId,
            @WebParam(name = "frontEndServiceId") String frontEndServiceId,
            @WebParam(name = "faultTimestamp") long faultTimestamp,
            @WebParam(name = "faultType") String faultType,
            @WebParam(name = "exceptionType") String exceptionType,
            @WebParam(name = "exceptionMessage") String exceptionMessage,
            @WebParam(name = "exceptionCauseType") String exceptionCauseType,
            @WebParam(name = "exceptionCauseMessage") String exceptionCauseMessage)
    {
        try
        {
            LoggingContext          log = LoggingContext.getContext(this, "");

            log.log("Monitor_recordClientConversationFault");
            log.log("conversationId", conversationId);
            log.log("clientProcessId", clientProcessId);
            log.log("frontEndServiceId", frontEndServiceId);
            log.log("faultTimestamp", faultTimestamp);
            log.log("faultType", faultType);
            log.log("exceptionType", exceptionType);
            log.log("exceptionMessage", exceptionMessage);

            ServiceFaultGtTrace.recordClientNetworkException(
                    log, 
                    faultTimestamp, 
                    conversationId, 
                    clientProcessId, 
                    frontEndServiceId, 
                    ServiceFaultGtTrace.FaultType.valueOf(faultType),
                    exceptionType, 
                    exceptionMessage,
                    exceptionCauseType,
                    exceptionCauseMessage);
        }
        catch(Exception ex)
        {
            getCrashContext().log(ex);
        }
    }    

    @WebMethod(operationName = "GP_Multihop_getSystemDG")
    public String[] GP_Multihop_getSystemDG(
            @WebParam(name = "fromTimestamp") long fromTimestamp, 
            @WebParam(name = "toTimestamp") long toTimestamp)
    {
        try
        {
            LoggingContext          log = LoggingContext.getContext(this, "");
            String[]                result;

            log.log("GP_Multihop_getSystemDG");
            log.log("fromTimestamp=" + fromTimestamp);
            log.log("toTimestamp=" + toTimestamp);

            result = MultihopGpBackupServer.getInstance(getRequestIp()).getSystemDg(
                    fromTimestamp, 
                    toTimestamp);

            //log.log("result", result);

            return result;   
        }
        catch(Exception ex)
        {
            getCrashContext().log(ex);
            return null;
        }
    }    
}
