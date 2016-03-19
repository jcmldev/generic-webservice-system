package dd.monitor;

import dd.monitor.multihopgp.BackupDataCalculator;
import dd.monitor.multihopgp.BackupDataSet;
import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ServiceMonitors {

    private Map<String, ServiceMonitor>     m_serviceMonitors = new TreeMap<>();

    public ServiceMonitor get(String id)
    {
        ServiceMonitor      serviceMonitor;
        
        
        if (m_serviceMonitors.containsKey(id))
        {
            serviceMonitor = m_serviceMonitors.get(id);
        }
        else
        {
            serviceMonitor = new ServiceMonitor(id);
            m_serviceMonitors.put(id, serviceMonitor);
        }
        
        return serviceMonitor;
    }
    
    public ServiceMonitor[] getMonitors()
    {
        return m_serviceMonitors.values().toArray(new ServiceMonitor[0]);
    }
    
    public void writeOut(LoggingContext log)
    {
        for(ServiceMonitor serviceMonitor : m_serviceMonitors.values())
        {
            serviceMonitor.writeOut(log);
        }
    }
    
    public String[] getBackupData(long fromTimestamp, long toTimestamp, int timeSlotSize)
    {
        ArrayList<String>           resultArray = new ArrayList<>();
        ArrayList<String>           serviceArray;
    
        
        for (ServiceMonitor serviceMonitor : m_serviceMonitors.values())
        {
            serviceArray = serviceMonitor.getBackupData(fromTimestamp, toTimestamp, timeSlotSize);
            resultArray.addAll(serviceArray);
        }
        
        return resultArray.toArray(new String[0]);
    }
    
    public void addBackupData(String serviceId, long fromTimestamp, long toTimestamp, int timeSlotSize, String[] backupData)
    {
        ServiceMonitor           serviceMonitor = get(serviceId);
        
        
        serviceMonitor.addBackupData(fromTimestamp, toTimestamp, timeSlotSize, backupData);
    }
    
    public synchronized void addBackupDataIntoMultihopGpDataSet(
        BackupDataSet dataSet,
        BackupDataCalculator backupCalculator)
    {
        
        for (ServiceMonitor serviceMonitor : m_serviceMonitors.values())
        {
            serviceMonitor.addBackupDataIntoMultihopGpDataSet(
                    dataSet, 
                    backupCalculator);
        }
        
    }
}