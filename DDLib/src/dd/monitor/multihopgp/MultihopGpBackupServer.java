
package dd.monitor.multihopgp;

import dd.ExperimentConfiguration;
import dd.Network;
import dd.dependenceset.DependencySet;
import dd.graph.DependenceGraph;
import dd.monitor.ServerMonitor;
import dd.output.LoggingContext;
import java.util.Map;
import java.util.TreeMap;

public class MultihopGpBackupServer 
{
    private static MultihopGpBackupServer                           m_instance;   
 
    private LoggingContext                                          m_log;
    private Map<String, Dependence>                                 m_dependencies = new TreeMap<>(); 
    private long                                                    m_slotSize;

    
    private MultihopGpBackupServer()
    {
        m_slotSize = ExperimentConfiguration.getGossipProtocolMultihopBackupTimeSlotSize(m_log);
    }
    
    private void initializeContext(String requestIp)
    {
        m_log = LoggingContext.getContext(this, requestIp);
    }
    
    public static MultihopGpBackupServer getInstance(String requestIp)
    {
        if (m_instance == null)
        {
            m_instance = new MultihopGpBackupServer();
        }
        
        m_instance.initializeContext(requestIp);
        
        return m_instance;
    }
        
    public void storeBackupData(
            String[] backupData)
    {
        Dependence          dependence;
        TimeSlotSet         slotSet;
        
        
        synchronized(this)
        {
            //m_log.log("storing backup data", backupData);
            
            BackupDataSet       dataSet = BackupDataSet.loadRecordsFromTransferedData(m_log, m_slotSize, backupData);

            
            for(BackupDataSetRecord record : dataSet.getRecords())
            {
                dependence = getDependence(record);
                slotSet = record.getSlotSet();
                
                if (slotSet != null)
                {
                    if (dependence != null)
                    {
                        dependence.getBackupContainer().addChunkOfHistoryData(slotSet);            
                    }
                    else
                    {
                        m_log.log("dependence not found!!!");
                        record.writeOut(m_log);
                    }
                }
                else
                {
                    m_log.log("storeBackupDataRecord - slotSet is empty !!! this means that some records are sent over network empty");
                    record.writeOut(m_log);
                }            
            }
        }
    }
    
    // here it is not efficient, but this is just a prototype
    private Dependence getDependence(BackupDataSetRecord record)
    {
        BackupDataSetRecord.RecordType      type;
        String                              dependentId;
        String                              antecedentId;
        Dependence                          dependence = null;
    
        
        if (record.getHashId() != 0)
        {
            //record.writeOut(m_log);
            
            for(String key : m_dependencies.keySet())
            {
                //m_log.log("key hash", key.hashCode());
                
                if (key.hashCode() == record.getHashId())
                {
                    dependence = m_dependencies.get(key);
                    break;
                }
            }
        }
        else
        {
            type = record.getType();
            dependentId = record.getDependentId();
            antecedentId = record.getAntecedentId();
            
            dependence = new Dependence(m_log, type, dependentId, antecedentId);
            
            if (m_dependencies.containsKey(dependence.getId()))
            {
                dependence = m_dependencies.get(dependence.getId());
            }
            else
            {
                m_dependencies.put(dependence.getId(), dependence);
                
                //m_log.log("added dependence", dependence.getId());
                //m_log.log("id hash", dependence.getId().hashCode());
                
            }
        }
        
        return dependence;
    }
    
    public String[] getBackupData(
            String[] latestSuccessfulBackup,
            boolean includeLocalData)

    {
        synchronized(this)
        {
            BackupDataCalculator            backupCalculator = new BackupDataCalculator(m_log, latestSuccessfulBackup);
            BackupDataSet                   dataSet = new BackupDataSet(m_log, m_slotSize);
            Dependence[]                    dependencies = m_dependencies.values().toArray(new Dependence[0]);


            for(Dependence dependence : dependencies)
            {
                addBackupDependenceData(
                        dependence, 
                        backupCalculator, 
                        dataSet);
            }

            if (includeLocalData)
            {
                addLocalDependencies(
                        dataSet, 
                        backupCalculator);
            }

            return dataSet.toStringArray();
        }
    }
    
    private void addBackupDependenceData(
            Dependence dependence,
            BackupDataCalculator backupCalculator,
            BackupDataSet dataSet)
    {
        long                            slotIndexFrom;
        long                            slotIndexTo;
        long                            indexOfLastOnSlot;
        TimeSlotSet                     slotSet;

        
        slotIndexFrom = backupCalculator.getFromSlotIndex(
                dependence.getType(),
                dependence.getDependentServiceId(), 
                dependence.getAntecedentServiceId());

        slotIndexTo = backupCalculator.getToSlotIndex();

        indexOfLastOnSlot = backupCalculator.getIndexOfLastOnSlot(        
                dependence.getType(),
                dependence.getDependentServiceId(), 
                dependence.getAntecedentServiceId());

        slotSet = dependence.getBackupContainer().getBackupData(slotIndexFrom, slotIndexTo);
        
        dataSet.addDependenceData(
                dependence.getType(),
                dependence.getDependentServiceId(), 
                dependence.getAntecedentServiceId(), 
                slotSet,
                indexOfLastOnSlot);    
    }
    
    private void addLocalDependencies(
            BackupDataSet dataSet,
            BackupDataCalculator backupCalculator)
    {                    
            ServerMonitor.getInstance().addBackupDataIntoMultihopGpDataSet(
                    dataSet,
                    backupCalculator);
    }
    
    public String[] getInterDependenciesForTimeWindow(String dependentServiceId, long fromTimestamp, long toTimestamp)
    {
        synchronized(this)
        {
            boolean                 dependenceOccurred;
            DependencySet           dependencySet = new DependencySet();


            for (Dependence dependence : m_dependencies.values())
            {
                if (dependence.getType() == BackupDataSetRecord.RecordType.Dependence)
                {
                    if (dependence.getDependentServiceId().equals(dependentServiceId))
                    {
                        dependenceOccurred = dependence.getBackupContainer().hasOccuredWithinTimewindow(
                                fromTimestamp, 
                                toTimestamp);

                        if (dependenceOccurred)
                        {
                            addDependenceIntoAntecedentList(
                                    dependencySet,
                                    dependence, 
                                    DependenceGraph.ReachabilityStatus.GP,
                                    fromTimestamp, 
                                    toTimestamp);
                        }
                    }
                }
            }

            return dependencySet.toStringArray();
        }
    }
    
    public String[] getInterDependenciesForTimeWindowAntecedent(String dependentServiceId, long fromTimestamp, long toTimestamp)
    {
        synchronized(this)
        {
            boolean                 dependenceOccurred;
            String                  serviceNodeIp = Network.getHostIpFromUrl(m_log, dependentServiceId);
            DependencySet           dependencySet = new DependencySet();


            for (Dependence dependence : m_dependencies.values())
            {
                if (dependence.getType() == BackupDataSetRecord.RecordType.Dependence)
                {
                    if (dependence.getDependentServiceId().equals(serviceNodeIp))
                    {
                        dependenceOccurred = dependence.getBackupContainer().hasOccuredWithinTimewindow(
                                fromTimestamp, 
                                toTimestamp);

                        if (dependenceOccurred)
                        {
                            addDependenceIntoAntecedentList(
                                    dependencySet,
                                    dependence, 
                                    DependenceGraph.ReachabilityStatus.GPAntecedent,
                                    fromTimestamp, 
                                    toTimestamp);
                        }
                    }
                }
            }

            return dependencySet.toStringArray();
        }
    }
    
    private void addDependenceIntoAntecedentList(
            DependencySet outputDs, 
            Dependence dependence,
            DependenceGraph.ReachabilityStatus reachabilityStatus,
            long fromTimestamp, 
            long toTimestamp)
    {
        outputDs.addDependencyInformation(
                dependence.getDependentServiceId(), 
                dependence.getAntecedentServiceId(), 
                DependenceGraph.FlStatus.Unknown, 
                DependenceGraph.FlStatus.Unknown,
                0,
                0);
        
        addDependenceSymptoms(
                outputDs, 
                dependence, 
                reachabilityStatus,
                fromTimestamp, 
                toTimestamp);

        // add dependent service symptoms 
        dependence = new Dependence(m_log, BackupDataSetRecord.RecordType.Dependence, dependence.getDependentServiceId(), "");
        addDependenceSymptoms(outputDs, dependence, reachabilityStatus, fromTimestamp, toTimestamp);
    }
        
    private void addDependenceSymptoms(
            DependencySet outputDs, 
            Dependence dependence,
            DependenceGraph.ReachabilityStatus reachabilityStatus,
            long fromTimestamp, 
            long toTimestamp)
    {
        DependenceGraph.FlStatus        flStatusEX;
        DependenceGraph.FlStatus        flStatusTO;
        boolean                         exElementFault;
        boolean                         exTransitiveFault;
        boolean                         toFault;
        boolean                         isService = dependence.getAntecedentServiceId().length() == 0;
        long                            lastEXTimestamp;
        long                            firstTOTimestamp;
        long                            toFromTimestamp = fromTimestamp + ExperimentConfiguration.getServiceSystemMessageExchangeTimeout(m_log);
        long                            toToTimestamp = toTimestamp + ExperimentConfiguration.getFlTimeoutSymptomsTimeWindowSize(m_log);
  
        
        // the last EX/TO timestamps do not apply to transitive symptoms
        
        exElementFault = getSymptomOccurance(dependence, BackupDataSetRecord.RecordType.SymptomService, fromTimestamp, toTimestamp);
        lastEXTimestamp = getSymptomTimestamp(dependence, BackupDataSetRecord.RecordType.SymptomService, fromTimestamp, toTimestamp);
                
        if (!exElementFault)
        {
            exElementFault = getSymptomOccurance(dependence, BackupDataSetRecord.RecordType.SymptomNetwork, fromTimestamp, toTimestamp);
            lastEXTimestamp = getSymptomTimestamp(dependence, BackupDataSetRecord.RecordType.SymptomNetwork, fromTimestamp, toTimestamp);   
        }

        exTransitiveFault = getSymptomOccurance(dependence, BackupDataSetRecord.RecordType.SymptomTransitive, fromTimestamp, toTimestamp);

        toFault = getSymptomOccurance(dependence, BackupDataSetRecord.RecordType.SymptomTimeout, toFromTimestamp, toToTimestamp);
        firstTOTimestamp = getSymptomTimestamp(dependence, BackupDataSetRecord.RecordType.SymptomTimeout, toFromTimestamp, toToTimestamp);
        
        flStatusEX = exElementFault ? DependenceGraph.FlStatus.ElementFault : DependenceGraph.FlStatus.NoFault;
        flStatusTO = toFault ? DependenceGraph.FlStatus.ElementFault : DependenceGraph.FlStatus.NoFault;
        
        
        if (isService)
        {
            outputDs.addServiceInformation(
                    dependence.getDependentServiceId(), 
                    reachabilityStatus, 
                    flStatusEX, 
                    flStatusTO,
                    lastEXTimestamp,
                    firstTOTimestamp);
        }
        else
        {
            outputDs.addDependencyInformation(
                    dependence.getDependentServiceId(), 
                    dependence.getAntecedentServiceId(), 
                    flStatusEX, 
                    flStatusTO,
                    lastEXTimestamp,
                    firstTOTimestamp);
        }
        
        flStatusEX = exTransitiveFault ? DependenceGraph.FlStatus.TransitiveFault : DependenceGraph.FlStatus.NoFault;

        if (isService)
        {
            outputDs.addServiceInformation(
                    dependence.getDependentServiceId(), 
                    reachabilityStatus, 
                    flStatusEX, 
                    flStatusTO,
                    lastEXTimestamp,
                    firstTOTimestamp);
        }
        else
        {
            outputDs.addDependencyInformation(
                    dependence.getDependentServiceId(), 
                    dependence.getAntecedentServiceId(), 
                    flStatusEX, 
                    flStatusTO,
                    lastEXTimestamp,
                    firstTOTimestamp);    
        }
    }
 
    private boolean getSymptomOccurance(
            Dependence dependence, 
            BackupDataSetRecord.RecordType recordType,
            long fromTimestamp, 
            long toTimestamp)
    {
        String          key;
        Dependence      symptomContainer;
        boolean         result = false;
        
        
        key = (new Dependence(m_log, recordType, dependence.getDependentServiceId(), dependence.getAntecedentServiceId())).getId();

        if (m_dependencies.containsKey(key))
        {   
            symptomContainer = m_dependencies.get(key);
            result = symptomContainer.getBackupContainer().hasOccuredWithinTimewindow(fromTimestamp, toTimestamp);
        }
                
        return result;
    }
    
    private long getSymptomTimestamp(
            Dependence dependence, 
            BackupDataSetRecord.RecordType recordType,
            long fromTimestamp, 
            long toTimestamp)
    {
        String          key;
        Dependence      symptomContainer;
        long            result = 0;
        
        
        key = (new Dependence(m_log, recordType, dependence.getDependentServiceId(), dependence.getAntecedentServiceId())).getId();

        if (m_dependencies.containsKey(key))
        {   
            symptomContainer = m_dependencies.get(key);
            
            if (recordType==BackupDataSetRecord.RecordType.SymptomTimeout)
            {
                result = symptomContainer.getBackupContainer().getFirstFromTimestampOfOccuranceSlotWithinTimewindow(
                        fromTimestamp, 
                        toTimestamp);
            }
            else
            {
                result = symptomContainer.getBackupContainer().getLatestToTimestampOfOccuranceSlotWithinTimewindow(
                        fromTimestamp, 
                        toTimestamp);
            }
        }
        
        return result;
    }
    
    public String[] getSystemDg(long fromTimestamp, long toTimestamp)
    {
        synchronized(this)
        {
            boolean                 dependenceOccurred;
            DependencySet           dependencySet;

            
            dependencySet = ServerMonitor.getInstance().getAllInterDependenciesForTimeWindow(
                    m_log, 
                    fromTimestamp, 
                    toTimestamp);

            for (Dependence dependence : m_dependencies.values())
            {
                if (dependence.getType() == BackupDataSetRecord.RecordType.Dependence)
                {
                    dependenceOccurred = dependence.getBackupContainer().hasOccuredWithinTimewindow(
                            fromTimestamp, 
                            toTimestamp);

                    if (dependenceOccurred)
                    {
                        addDependenceIntoAntecedentList(
                                dependencySet,
                                dependence, 
                                DependenceGraph.ReachabilityStatus.GP,
                                fromTimestamp, 
                                toTimestamp);
                    }
                }
            }

            return dependencySet.toStringArray();
        }
    }
}
