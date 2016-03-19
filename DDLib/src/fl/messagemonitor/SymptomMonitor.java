
package fl.messagemonitor;

import dd.monitor.ServerMonitor;


/*
 * The symptom data are stored into dependence monitor structures
 * - this is not ideal but as prototype it should be enought
 * 
 */



public class SymptomMonitor 
{
    
    public final String SYMPTOM_ATTRIBUTES_DELIMITER = "_SYMP_";
    
    
    public enum SymptomType
    {
        EX,
        SENDF,
        TO
    }
    
    public enum ProcessingFlowDirection
    {
        Outbound,
        Inbound
    }
    
    private static SymptomMonitor          m_symptomMonitor;
    
    
    
    public static SymptomMonitor getInstance()
    {
        if (m_symptomMonitor == null)
        {
            m_symptomMonitor = new SymptomMonitor();
        }
        
        return m_symptomMonitor;
    }
    
    
    public void recordInterDependenceFault(
            ProcessingFlowDirection flowDirection, 
            String source, 
            String target, 
            SymptomType symptomType)
    {
        source = flowDirection.toString() + 
                SYMPTOM_ATTRIBUTES_DELIMITER + 
                symptomType + 
                SYMPTOM_ATTRIBUTES_DELIMITER + 
                source;
    
        ServerMonitor.getInstance().addInterDependencyOccurrence(
                source, 
                target);
    }

    public void recordIntraDependenceFault(
            String source, 
            String middleService,
            String target, 
            SymptomType symptomType)
    {
        source = symptomType + 
                SYMPTOM_ATTRIBUTES_DELIMITER + 
                source;
    
        ServerMonitor.getInstance().addIntraDependencyOccurrence(
                source, 
                middleService, 
                target);    
    }

    public void recordInterDependenceFault(
            ProcessingFlowDirection flowDirection, 
            String source, 
            String target, 
            SymptomType symptomType,
            long symptomTimestamp)
    {
        source = flowDirection.toString() + 
                SYMPTOM_ATTRIBUTES_DELIMITER + 
                symptomType + 
                SYMPTOM_ATTRIBUTES_DELIMITER + 
                source;
    
        ServerMonitor.getInstance().addInterDependencyOccurrence(
                source, 
                target, 
                symptomTimestamp);    
    }

    public void recordIntraDependenceFault(
            String source, 
            String middleService,
            String target, 
            SymptomType symptomType,
            long symptomTimestamp)
    {
        source = symptomType + 
                SYMPTOM_ATTRIBUTES_DELIMITER + 
                source;
    
        ServerMonitor.getInstance().addIntraDependencyOccurrence(
                source, 
                middleService, 
                target,
                symptomTimestamp);        
    }
}
