
package dd.gpdklmanager;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.Timestamp;
import dd.Wsdl;
import dd.bck.Backup;
import dd.bck.Backup_Service;
import dd.bck.DKLStoreIpDependenceResponse;
import dd.bck.GPAddMetadataResponse;
import dd.bck.GPMultihopStoreBackupDataResponse;
import dd.bck.GPStoreBackupDataResponse;
import dd.gpdklmanager.dkl.DklTarget;
import dd.gpdklmanager.multihopgp.MultihopGpTarget;
import dd.monitor.multihopgp.BackupDataSet;
import dd.output.GpTrace;
import dd.output.LoggingContext;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;


public class BackupOnRemoteHost 
{
    private LoggingContext                              m_log;
    private SynchronizationTarget                       m_synchronizationTarget;
    private long                                        m_synchronizationTimestamp;
    private BackupDataSet                               m_synchronizationBackupSet;
    private Backup                                      m_port;    
    private static Backup_Service                       m_service;
    private int                                         m_logCycleIndex;
    private int                                         m_logPayloadSize;
    
    
    public BackupOnRemoteHost (LoggingContext log, SynchronizationTarget synchronizationTarget)
    {
        m_log = log;
        m_synchronizationTarget = synchronizationTarget;
        m_port = getPort();
    }
        
    private Backup_Service getService()
    {
        URL                                   wsdlUrl;
            
        
        if (m_service == null)
        {
            wsdlUrl = Wsdl.getWsdlFilePathURLInGfDir(m_log, "Backup");
            m_service = new Backup_Service(wsdlUrl);
        }

        return m_service;
    }
    
    private Backup getPort()
    {
        Backup                       port = getService().getBackupPort();
        String                                serviceUrl;
        
        
        serviceUrl = m_synchronizationTarget.getServiceAddress();

        Network.setServiceUrl(
                (BindingProvider) port, 
                serviceUrl, 
                m_synchronizationTarget.getServiceTimeout());
        
        return port;
    }
    
    public void GP_storeBackupData(
            String nodeId,
            long fromTimestamp, 
            long toTimestamp, 
            int timeSlotSize, 
            String[] backupData)
    {
        Backup                      port = m_port;
        
        
        m_synchronizationTimestamp = toTimestamp;
        
        m_log.log("Asynch request to store backup data sent to: " + m_synchronizationTarget.getHost());
        
        try 
        {    
            AsyncHandler<GPStoreBackupDataResponse> asyncHandler = 
                    new AsyncHandler<GPStoreBackupDataResponse>() 
            {

                @Override
                public void handleResponse(Response<GPStoreBackupDataResponse> response) 
                {
                    try 
                    {
                        //System.out.println(response.get().toString());
                        response.get().toString();
                        
                        m_synchronizationTarget.setLastSuccessfulSynchronizationTimestamp(m_synchronizationTimestamp);
                        
                        m_log.log(
                                "Backup synchronization with node: " + 
                                m_synchronizationTarget.getHost() + 
                                " was succesful");

                    } 
                    catch (Exception ex) 
                    {
                        m_log.log(
                                "Backup synchronization with node: " + 
                                m_synchronizationTarget.getHost() +
                                " failed with exception: ");

                        m_log.log(ex, false);
                    }
                }
            };
            
            java.util.concurrent.Future<? extends Object> result = port.gpStoreBackupDataAsync(
                    nodeId, 
                    fromTimestamp, 
                    toTimestamp, 
                    timeSlotSize, 
                    Arrays.asList(backupData),
                    asyncHandler);              
            
        } 
        catch (Exception ex) 
        {
            m_log.log(
                    "Request for synchronization with node: " + 
                    m_synchronizationTarget.getHost() + " "
                    + "failed with exception: ");
            
            m_log.log(ex, false);
        }
    }
    
    public void GP_addMetadata(List<String> metadata, long synchronizationTimestamp)
    {
        m_synchronizationTimestamp = synchronizationTimestamp;
        
        m_log.log("Asynch request sent to update metadata to node:" + m_synchronizationTarget.getHost());
        m_log.log("Metadata size=" + metadata.size());
        
        try 
        {    
            AsyncHandler<GPAddMetadataResponse> asyncHandler = 
                    new AsyncHandler<GPAddMetadataResponse>() 
            {

                @Override
                public void handleResponse(Response<GPAddMetadataResponse> response) 
                {
                    try 
                    {
                        //System.out.println(response.get().toString());
                        response.get().toString();
                        
                        m_log.log(
                                "Metadata update on node: " + 
                                m_synchronizationTarget.getHost() + 
                                " was successful");

                        m_synchronizationTarget.setLastSuccessfulSynchronizationTimestamp(m_synchronizationTimestamp);
                        
                    } 
                    catch (Exception ex) 
                    {
                        m_log.log(
                                "Metadata update on node: " + 
                                m_synchronizationTarget.getHost() +
                                " failed with exception: ");
                        
                        m_log.log(ex, false);
                    }
                }
            };
            
            java.util.concurrent.Future<? extends Object> result = m_port.gpAddMetadataAsync(
                    metadata,
                    asyncHandler);              
            
        } 
        catch (Exception ex) 
        {
            m_log.log(
                    "Metadata request for update on node: " + 
                    m_synchronizationTarget.getHost() + " "
                    + "failed with exception: ");
            
            m_log.log(ex, false);
        }
    }
        
    public void DKL_storeIpDependence(String dependentIp, String antecedentService)
    {
        m_synchronizationTimestamp = Timestamp.now();
        
        m_log.log(
                "DKL - Asynch request sent to store dependent ip: " + 
                dependentIp+ 
                " on service:" + 
                antecedentService+ 
                " sent to node: " + 
                m_synchronizationTarget.getHost());
        
        try 
        {    
            AsyncHandler<DKLStoreIpDependenceResponse> asyncHandler = 
                    new AsyncHandler<DKLStoreIpDependenceResponse>() 
            {

                @Override
                public void handleResponse(Response<DKLStoreIpDependenceResponse> response) 
                {
                    DklTarget                            dklTarget = (DklTarget)m_synchronizationTarget;
        
                    
                    try 
                    {
                        //System.out.println(response.get().toString());
                        response.get().toString();
                        
                        m_synchronizationTarget.setLastSuccessfulSynchronizationTimestamp(m_synchronizationTimestamp);
                        
                        m_log.log(
                                "DKL - Storing of dependent ip: " + 
                                dklTarget.getDependentIp() + 
                                " on service:" + 
                                dklTarget.getAntecedentService() + 
                                " sent to node: " + 
                                dklTarget.getHost() +
                                " was succesful");
                    } 
                    catch (Exception ex) 
                    {
                        m_log.log(
                                "DKL - Storing of dependent ip: " + 
                                dklTarget.getDependentIp() + 
                                " on service:" + 
                                dklTarget.getAntecedentService() + 
                                " sent to node: " + 
                                dklTarget.getHost() +
                                " failed with exception: ");
                        
                        m_log.log(ex, false);
                    }
                }
            };
            
            java.util.concurrent.Future<? extends Object> result = m_port.dklStoreIpDependenceAsync(
                    dependentIp, 
                    antecedentService, 
                    asyncHandler);         
            
        } 
        catch (Exception ex) 
        {
            m_log.log(
                    "DKL - Sending request for storing of dependent ip: " +
                    dependentIp + 
                    " on service:" + 
                    antecedentService + 
                    " sent to node: " + 
                    m_synchronizationTarget.getHost() + 
                    " failed with exception: ");
            
            m_log.log(ex, false);
        }
    }

    public void GP_Multihop_storeBackupData(
            long toTimestamp, 
            String[] backupData,
            String[] lastSuccessfulBackupData,
            int cycleIndex)
    {
        Backup              port = m_port;
        BackupDataSet   backupSet;
        BackupDataSet   lastSuccessfulBackupDataSet;
        String[]            backupDataToTransfer;


        
        backupSet = BackupDataSet.loadRecordsFromLocalData(
                m_log, 
                ExperimentConfiguration.getGossipProtocolMultihopBackupTimeSlotSize(m_log), 
                backupData);
        
        lastSuccessfulBackupDataSet = BackupDataSet.loadRecordsFromLocalData(
                m_log, 
                ExperimentConfiguration.getGossipProtocolMultihopBackupTimeSlotSize(m_log), 
                lastSuccessfulBackupData);
        
        //backupSet.writeOut(m_log);
        
        backupDataToTransfer = backupSet.getDataToTransfer(
                lastSuccessfulBackupDataSet, 
                m_synchronizationTarget.getHost());

        m_synchronizationTimestamp = toTimestamp;
        m_synchronizationBackupSet = backupSet;
        m_logCycleIndex = cycleIndex;
        
        m_log.log("Size of data received from local backup store: " + backupSet.size() + " - size of data to be transfered: " + backupDataToTransfer.length / 9);

        if (backupDataToTransfer.length == 0)
        {
            m_log.log("No data to synch ...");
            return;
        }
        
        m_log.log("Asynch request to store backup data sent to: " + m_synchronizationTarget.getHost());
        
        try 
        {    
            AsyncHandler<GPMultihopStoreBackupDataResponse> asyncHandler = 
                    new AsyncHandler<GPMultihopStoreBackupDataResponse>() 
            {

                @Override
                public void handleResponse(Response<GPMultihopStoreBackupDataResponse> response) 
                {
                    try 
                    {
                        response.get().toString();
                        
                        m_log.log(
                                "Backup synchronization with node: " + 
                                m_synchronizationTarget.getHost() + 
                                " was succesful");
                        
                        logSendIntoOutput(true, null);
                        
                        if (m_synchronizationTarget.getLastSuccessfulSynchronizationTimestamp() < m_synchronizationTimestamp)
                        {

                            m_synchronizationTarget.setLastSuccessfulSynchronizationTimestamp(
                                    m_synchronizationTimestamp);

                            ((MultihopGpTarget)m_synchronizationTarget).setLastSuccessfulBackupData( 
                                    m_synchronizationBackupSet.toStringArray());
                        }
                        else
                        {
                            m_log.log("The synchronization response was received after sucessful newer synched happend!!!!");
                        }
                        
                    } 
                    catch (Exception ex) 
                    {
                        logSendIntoOutput(false, ex);
                        
                        m_log.log(
                                "Backup synchronization with node: " + 
                                m_synchronizationTarget.getHost() +
                                " failed with exception: ");

                        m_log.log(ex, false);
                    }
                }
            };
            
            logFindPayloadSize(backupDataToTransfer);
                    
            java.util.concurrent.Future<? extends Object> result = port.gpMultihopStoreBackupDataAsync(
                    Arrays.asList(backupDataToTransfer),
                    asyncHandler);              
            
        } 
        catch (Exception ex) 
        {
            m_log.log(
                    "Request for synchronization with node: " + 
                    m_synchronizationTarget.getHost() + " "
                    + "failed with exception: ");
            
            m_log.log(ex, false);
        }
    }
    
    private void logFindPayloadSize(
            String[] dataToTransfer)
    {
        m_logPayloadSize = 0;
     
        for (String line : dataToTransfer)
        {
            m_logPayloadSize += line.length();
        }
    }
    
    private void logSendIntoOutput(
            boolean sendSuccess,
            Exception ex)
    {
        String          faultType = "";
        String          faultMessage = "";
        
        
        if (ex != null)
        {
            faultType = ex.getClass().getName();
            faultMessage = ex.getMessage();
            faultMessage = faultMessage.replaceAll("\\r\\n|\\r|\\n", " ");
            faultMessage = faultMessage.replaceAll(",", " ");
        }
        
        GpTrace.writeSendIntoOutput(
                m_log, 
                m_logCycleIndex, 
                m_synchronizationTarget.getHost(), 
                m_synchronizationTimestamp, 
                Timestamp.now(), 
                sendSuccess, 
                faultType, 
                faultMessage, 
                m_logPayloadSize);
    }
}
