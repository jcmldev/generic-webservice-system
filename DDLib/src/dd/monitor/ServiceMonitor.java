
package dd.monitor;

import dd.Network;
import dd.monitor.multihopgp.BackupDataCalculator;
import dd.monitor.multihopgp.BackupDataSet;
import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.Collection;


public class ServiceMonitor 
{    
    private final String                m_serviceId;
    private InterDependencies           m_interDependencies = new InterDependencies();
    
    
    public ServiceMonitor(String serviceId)
    {
        m_serviceId = serviceId;
    }
    
    public String getServiceId()
    {
        return m_serviceId;
    }
    
    public void addInterDependencyOccurrence(String strTarget)
    {
        InterDependency  dep = m_interDependencies.get(strTarget);
        
        
        dep.addOccurrence();
    }
    
    public void addInterDependencyOccurrence(String strTarget, long timestamp)
    {
        InterDependency  dep = m_interDependencies.get(strTarget);
        
        
        dep.addOccurrence(timestamp);
    }
    
    public Collection<InterDependency> getInterDependencies(long fromTimestamp, long toTimestamp)
    {
        return m_interDependencies.getDependenciesForTimeWindow(fromTimestamp, toTimestamp);
    }  
    
    public void writeOut(LoggingContext log)
    {
        log.log("Service Id: " + m_serviceId);
        m_interDependencies.writeOut(log);
    }
    
    public static final String                 BACKUP_SERVICE_ID_PREFIX = "-";
    
    public ArrayList<String> getBackupData(long fromTimestamp, long toTimestamp, int timeSlotSize)
    {
        ArrayList<String>           resultArray;
    
        
        resultArray = m_interDependencies.getBackupData(fromTimestamp, toTimestamp, timeSlotSize);
        resultArray.add(0, BACKUP_SERVICE_ID_PREFIX + m_serviceId);
        
        return resultArray;
    }
    
    public void addBackupData(long fromTimestamp, long toTimestamp, int timeSlotSize, String[] backupData)
    {
        m_interDependencies.addBackupData(fromTimestamp, toTimestamp, timeSlotSize, backupData);
    }
    
    public void addBackupDataIntoMultihopGpDataSet(
        BackupDataSet dataSet,
        BackupDataCalculator backupCalculator)
    {
        LoggingContext          log = LoggingContext.getContext(this,"");
        String                  backupId = getServiceBackupId(log);
        
        
        m_interDependencies.addBackupDataIntoMultihopGpDataSet(
                log,
                dataSet, 
                backupCalculator,
                backupId);
    }
    
    private String getServiceBackupId(LoggingContext log)
    {
        int                     gwsIndex = m_serviceId.indexOf("/GWS");
        String                  backupId;
        
        
        if (gwsIndex > -1)
        {
            backupId = m_serviceId.substring(1, gwsIndex);
            
            backupId = Network.getGenericWsUrl(
                log, 
                Network.getLocalHostIp(log), 
                backupId);
        }
        else
        {
            backupId = m_serviceId;
        }
            
        return backupId;
    }
}