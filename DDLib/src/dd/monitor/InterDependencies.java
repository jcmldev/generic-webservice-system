package dd.monitor;

import dd.monitor.multihopgp.BackupDataCalculator;
import dd.monitor.multihopgp.BackupDataSet;
import dd.monitor.multihopgp.BackupDataSetRecord;
import dd.monitor.multihopgp.TimeSlotSet;
import dd.output.LoggingContext;
import fl.FlOccuranceProvider;
import fl.ServiceFaultGtTrace;
import java.util.*;

public class InterDependencies 
{

    private Map<String, InterDependency>     m_dependencies = new TreeMap<>();
    
    
    public InterDependencies()
    {}
    
    public InterDependency get(String target)
    {
        InterDependency dep;
        
        
        dep = m_dependencies.get(target);
        
        if (dep == null)
        {
            dep = new InterDependency(target);
            m_dependencies.put(dep.getTarget(), dep);
        }
                
        return dep;
    }
    
    public Collection<InterDependency> getDependenciesForTimeWindow(long fromTimestamp, long toTimestamp)
    {
        Set<InterDependency>    foundDependencies = new TreeSet<>();
        
        
        for (InterDependency interDependency : m_dependencies.values()) 
        {
            if (interDependency.hasOccuredWithinTimewindow(fromTimestamp, toTimestamp))
            {
                foundDependencies.add(interDependency);
            }
        }
        
        return foundDependencies;
    }
    
    public void writeOut(LoggingContext log)
    {
        for (InterDependency interDependency : m_dependencies.values()) 
        {
            interDependency.writeOut(log);
        }
    }
    
    /*
    public void remove (String strSource, String target)
    {
        m_dependencies.remove(strSource + target);
    }
    */
    
    public ArrayList<String> getBackupData(long fromTimestamp, long toTimestamp, int timeSlotSize)
    {
        ArrayList<String>           resultArray = new ArrayList<>();
        ArrayList<String>           interDependencyArray;
    
        
        for (InterDependency interDependency : m_dependencies.values()) 
        {
            interDependencyArray = interDependency.getBackupData(fromTimestamp, toTimestamp, timeSlotSize);
            resultArray.addAll(interDependencyArray);
        }
        
        return resultArray;
    }
    
    public void addBackupData(
            long fromTimestamp, 
            long toTimestamp, 
            int timeSlotSize, 
            String[] backupData)
    {
        int                         numberOfDependencies = backupData.length / 2;
        String                      targetId;
        String                      targetBacakupData;
        InterDependencyBackup       interDependencyBackup;
        
        
        for(int i = 0; i < numberOfDependencies; i++)
        {
            targetId = backupData[(i * 2)];
            targetBacakupData = backupData[(i * 2) + 1];
            
            if (m_dependencies.containsKey(targetId))
            {
                interDependencyBackup = (InterDependencyBackup)get(targetId);
            }
            else
            {
                interDependencyBackup = new InterDependencyBackup(targetId);
                m_dependencies.put(interDependencyBackup.getTarget(), interDependencyBackup);
            }
            
            interDependencyBackup.addBackupData(fromTimestamp, toTimestamp, timeSlotSize, targetBacakupData);
        }
    }
    
    public void addBackupDataIntoMultihopGpDataSet(
            LoggingContext log,
            BackupDataSet dataSet,
            BackupDataCalculator backupCalculator,
            String parentService)
    {
        long            fromSlotIndex;
        long            fromTimestamp;
        long            toSlotIndex;
        long            toTimestamp;
        long            indexOfLastOnSlot;
        TimeSlotSet     slotSet;
        
        
        toSlotIndex = backupCalculator.getToSlotIndex();
        toTimestamp = TimeSlotSet.getEndTimestampOfSlot(backupCalculator.getSlotSize(), toSlotIndex);
        
        for (InterDependency interDependency : m_dependencies.values()) 
        {
            fromSlotIndex = backupCalculator.getFromSlotIndex(
                    BackupDataSetRecord.RecordType.Dependence, 
                    parentService, 
                    interDependency.getTarget());
            
            fromTimestamp = TimeSlotSet.getStartTimestampOfSlot(backupCalculator.getSlotSize(), fromSlotIndex);

            indexOfLastOnSlot = backupCalculator.getIndexOfLastOnSlot(
                    BackupDataSetRecord.RecordType.Dependence,
                    parentService, 
                    interDependency.getTarget());
            
            slotSet = interDependency.getBackupDataForMultihopGp(
                    fromTimestamp, 
                    toTimestamp, 
                    backupCalculator.getSlotSize());
            
            dataSet.addDependenceData(
                    BackupDataSetRecord.RecordType.Dependence,
                    parentService, 
                    interDependency.getTarget(), 
                    slotSet,
                    indexOfLastOnSlot);
            
            addSymptomSetsToBackupSet(
                    log, 
                    backupCalculator, 
                    dataSet, 
                    parentService, 
                    interDependency.getTarget());
        }

        addSymptomSetsToBackupSet(
                log, 
                backupCalculator, 
                dataSet, 
                parentService, 
                "");
    }
    
    private void addSymptomSetsToBackupSet(
            LoggingContext log,
            BackupDataCalculator backupCalculator,
            BackupDataSet dataSet,
            String parentService,
            String antecedentService)
    {
        addSymptomSetToBackupSet(
                log, 
                backupCalculator, 
                dataSet, 
                parentService, 
                antecedentService, 
                BackupDataSetRecord.RecordType.SymptomService, 
                ServiceFaultGtTrace.FaultType.Service);
        
        addSymptomSetToBackupSet(
                log, 
                backupCalculator, 
                dataSet, 
                parentService, 
                antecedentService, 
                BackupDataSetRecord.RecordType.SymptomTransitive, 
                ServiceFaultGtTrace.FaultType.Transitive);

        addSymptomSetToBackupSet(
                log, 
                backupCalculator, 
                dataSet, 
                parentService, 
                antecedentService, 
                BackupDataSetRecord.RecordType.SymptomTimeout, 
                ServiceFaultGtTrace.FaultType.Timeout);

        addSymptomSetToBackupSet(
                log, 
                backupCalculator, 
                dataSet, 
                parentService, 
                antecedentService, 
                BackupDataSetRecord.RecordType.SymptomNetwork, 
                ServiceFaultGtTrace.FaultType.Network);
    }
    
    private void addSymptomSetToBackupSet(
            LoggingContext log,
            BackupDataCalculator backupCalculator,
            BackupDataSet dataSet,
            String parentService,
            String antecedentService,
            BackupDataSetRecord.RecordType recordType,
            ServiceFaultGtTrace.FaultType faultType1)
    {
        TimeSlotSet                         symptomSet;
        long                                indexOfLastOnSlot;
        
        
        symptomSet = getSymptomSlotSet(
                log, 
                backupCalculator, 
                parentService, 
                antecedentService, 
                recordType, 
                faultType1);

        indexOfLastOnSlot = backupCalculator.getIndexOfLastOnSlot(
                recordType,
                parentService, 
                antecedentService);
        
        dataSet.addDependenceData(
                recordType,
                parentService, 
                antecedentService, 
                symptomSet,
                indexOfLastOnSlot);
    }
    
    private TimeSlotSet getSymptomSlotSet(
            LoggingContext log,
            BackupDataCalculator backupCalculator,
            String parentService,
            String antecedentService,
            BackupDataSetRecord.RecordType recordType,
            ServiceFaultGtTrace.FaultType faultType)
    {
        long            fromSlotIndex;
        long            startTimestamp;
        long            toSlotIndex;
        long            endTimestamp;
        TimeSlotSet     slotSet = null;
        FlOccuranceProvider     fl = new FlOccuranceProvider(log, parentService);
        boolean         isDependence = antecedentService.length() > 0;
        boolean         slotState;
        
        
        toSlotIndex = backupCalculator.getToSlotIndex();
        
        fromSlotIndex = backupCalculator.getFromSlotIndex(
                recordType, 
                parentService, 
                antecedentService);

        for(long i = fromSlotIndex; i <= toSlotIndex; i++)
        {
            startTimestamp = TimeSlotSet.getStartTimestampOfSlot(backupCalculator.getSlotSize(), i);
            endTimestamp = TimeSlotSet.getEndTimestampOfSlot(backupCalculator.getSlotSize(), i);

            if (isDependence)
            {
                slotState = fl.getOccuranceOfDependenceSymptom(
                        antecedentService, 
                        startTimestamp, 
                        endTimestamp, 
                        faultType);
            }
            else
            {
                slotState = fl.getOccuranceOfServiceSymptom(
                        startTimestamp, 
                        endTimestamp, 
                        faultType);
            }
            
            if (slotState)
            {   
                if (slotSet == null)
                {
                    slotSet = new TimeSlotSet(i, backupCalculator.getSlotSize());
                }
                
                slotSet.setSlotValue(i, slotState);
                
                //debug
                //System.out.println("occurance - from / to" + stepFromTimestamp + "/" + stepToTimestamp);
            }
        }
        
        slotSet = slotSet != null ? slotSet : new TimeSlotSet(0, 0); 
        
        return slotSet;
    }
}
