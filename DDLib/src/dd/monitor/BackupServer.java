
package dd.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;
import java.util.TreeSet;

public class BackupServer 
{
    private static BackupServer                         m_instance;
    private ServiceMonitors                             m_serviceMonitors = new ServiceMonitors();
    private MetadataCache                               m_metadataCache = new MetadataCache();
    private TreeMap<String, TreeSet<String> >           m_ipDependencies = new TreeMap< >();
    
    
    private BackupServer()
    {}
    
    public static BackupServer getInstance()
    {
        if (m_instance == null)
        {
            m_instance = new BackupServer();
        }
        
        return m_instance;
    }
    
    public void DKL_storeIpDependence(String dependentIp, String antecedentService)
    {
        TreeSet<String>         antecedentSet;
        
        
        if (!m_ipDependencies.containsKey(dependentIp))
        {
            m_ipDependencies.put(dependentIp, new TreeSet<String>());
        }

        antecedentSet = m_ipDependencies.get(dependentIp);
                
        if (!antecedentSet.contains(antecedentService))
        {
            antecedentSet.add(antecedentService);
        }
    }

    public String[] DKL_getAntecedentServicesOfDependentIp(String dependentIp)
    {
        String[]            antecedentServices = null;
        
        
        if (m_ipDependencies.containsKey(dependentIp))
        {
            antecedentServices = m_ipDependencies.get(dependentIp).toArray(new String[0]);
        }
        
        return antecedentServices;
    }
    
    public MetadataCache getMetadataCache()
    {
        return m_metadataCache;
    }
        
    public void storeBackupData(
            String nodeId, 
            long fromTimestamp, 
            long toTimestamp, 
            int timeSlotSize, 
            String[] backupData)
    {
        int                 serviceIndex = 0;
        String              serviceId;
        String[]            serviceBackupData;
        String              nodeServiceId;
        

        while(true)
        {
            serviceId = getServiceIdFromBackupData(serviceIndex, backupData);
            if(serviceId == null) break;
            
            serviceBackupData = getServiceBackupDataFromNodeBackupData(
                    serviceIndex, 
                    backupData);
            
            nodeServiceId = getNodeServiceId(nodeId, serviceId);
            
            m_serviceMonitors.addBackupData(
                    nodeServiceId, 
                    fromTimestamp, 
                    toTimestamp, 
                    timeSlotSize, 
                    serviceBackupData);
            
            serviceIndex++;
        }
    }
    
    private String[] getServiceBackupDataFromNodeBackupData(int serviceIndex, String[] backupData)
    {
        int                 serviceCounter = -1;
        String              element;
        int                 dataStartPosition = -1;
        int                 dataEndPosition = -1;
        int                 dataLength;
        String[]            serviceBackupData;
        
        
        for(int i = 0; i < backupData.length; i++)
        {
            element = backupData[i];

            if (element.startsWith(ServiceMonitor.BACKUP_SERVICE_ID_PREFIX))
            {
                serviceCounter++;
                
                if (serviceCounter == serviceIndex)
                {
                    dataStartPosition = i + 1;
                }
                
                if (serviceCounter == (serviceIndex + 1))
                {
                    dataEndPosition = i - 1;
                    break;
                }
            }
        }
        
        if (dataEndPosition == -1)
        {
            dataEndPosition = backupData.length - 1;
        }
        
        dataLength = (dataEndPosition - dataStartPosition)  + 1;
        serviceBackupData = new String[dataLength];
        
        System.arraycopy(backupData, dataStartPosition, serviceBackupData, 0, dataLength);
        
        return serviceBackupData;
    }
    
    private String getServiceIdFromBackupData(int serviceIndex, String[] backupData)
    {
        int                 serviceCounter = -1;
        String              element = null;
        
        
        for(int i = 0; i < backupData.length; i++)
        {
            element = backupData[i];

            if (element.startsWith(ServiceMonitor.BACKUP_SERVICE_ID_PREFIX))
            {
                serviceCounter++;
                if (serviceCounter == serviceIndex) break;
            }
            
            element = null;
        }

        if (element != null)
        {
            element = element.substring(1, element.length());
        }
        
        return element;
    }
    
    public String[] getInterDependenciesForTimeWindow(String nodeId, String serviceId, long fromTimestamp, long toTimestamp)
    {
        if (nodeId == null || nodeId.length() == 0 || serviceId == null || serviceId.length() == 0) return null;

        ServiceMonitor                          serviceMonitor;
        Collection<InterDependency>             interDependencies; 
        ArrayList<String>                       strIDs = new ArrayList<>();                               
        String                                  nodeServiceId = getNodeServiceId(nodeId, serviceId);
                
        
        serviceMonitor = m_serviceMonitors.get(nodeServiceId);
        
        interDependencies = serviceMonitor.getInterDependencies(fromTimestamp, toTimestamp); 
        
        for (InterDependency interDependency : interDependencies)
        {
            strIDs.add(interDependency.getTarget());
        }
        
        return strIDs.toArray(new String[0]);
    }
    
    private String getNodeServiceId(String nodeId, String serviceId)
    {
        return nodeId + "*" + serviceId;
    }
}